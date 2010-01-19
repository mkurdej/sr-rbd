/*
 * ConsoleView.java
 */

package ddb.console;

import ddb.db.DBException;
import ddb.msg.InvalidMessageTypeException;
import ddb.msg.Message;
import ddb.msg.MessageType;
import ddb.msg.client.ConflictMessage;
import ddb.msg.client.ErrorMessage;
import ddb.msg.client.ResultsetMessage;
import ddb.msg.client.SuccessMessage;
import ddb.msg.client.TimeoutMessage;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import ddb.tpc.msg.TransactionMessage;
import java.awt.Cursor;
import java.io.DataInputStream;
import java.util.Random;
import javax.swing.SwingWorker;

/**
 * The application's main frame.
 */
public class ConsoleView extends FrameView {

    private static int SLEEP_MIN = 2000;
    private static int SLEEP_MAX = 6000;
    private static int SOCKET_TIMEOUT = 10000;
    private static int ATTEMPTS_THRESHOLD = 5;

    protected class ConsoleWorker extends SwingWorker<Boolean, Boolean>
    {

        @Override
        protected Boolean doInBackground() throws Exception
        {
            ConsoleView.this.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            progressBar.setVisible(true);
            progressBar.setIndeterminate(false);
            progressBar.setValue(10);

            boolean retry = true;
            int attempts = 0;

            try {
                do
                {
                    // send query
                    TransactionMessage message = new TransactionMessage(queryTextArea.getText());
                    socket.getOutputStream().write(message.Serialize());

                    progressBar.setValue(50);

                    // read results
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    int size = in.readInt();
                    int type = in.readInt();
                    byte[] data = new byte[size];

                    int left;
                    for(left = size; left > 0; )
                            left -= in.read(data, size - left, left);

                    Message result = Message.Unserialize(MessageType.fromInt(type), data, (InetSocketAddress)socket.getRemoteSocketAddress());

                    retry = false;
                    ++attempts;
                    if(result instanceof ResultsetMessage)
                    {
                         logTextArea.append("RESULT:\n");
                         logTextArea.append(((ResultsetMessage)result).getResult());
                         logTextArea.append("\n");
                    }
                    else if(result instanceof SuccessMessage)
                    {
                        logTextArea.append("SUCCESS: ");
                        logTextArea.append(((SuccessMessage)result).getDatabaseMessage());
                        logTextArea.append("\n");
                    }
                    else if(result instanceof ErrorMessage)
                    {
                        DBException ex = ((ErrorMessage)result).getException();

                        logTextArea.append("ERROR [");
                        logTextArea.append(ex.getErrorCode());
                        logTextArea.append("]: ");
                        logTextArea.append(ex.getErrorMessage());
                        logTextArea.append("\n");
                    }
                    else if(result instanceof TimeoutMessage)
                    {
                        logTextArea.append("Transaction timed out - waiting for a random time\n");
                        Thread.sleep(getSleepTime());
                        logTextArea.append("Transaction timed out - resending\n");
                        retry = true; // resend
                    }
                    else if(result instanceof ConflictMessage)
                    {
                        logTextArea.append("Transaction conflict - waiting for a random time\n");
                        Thread.sleep(getSleepTime());
                        logTextArea.append("Transaction conflict - resending\n");
                        retry = true; // resend
                    }
                    else
                    {
                        logTextArea.append("Unkonown response type: ");
                        logTextArea.append(Integer.toString(type));
                        logTextArea.append("\n");
                    }
                }
                while(retry && attempts < ATTEMPTS_THRESHOLD);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(getFrame(), "IOException " + ex.toString());
            } catch (InvalidMessageTypeException ex)
            {
                JOptionPane.showMessageDialog(getFrame(), "InvalidMessageTypeException " + ex.toString());
            } catch(InterruptedException ex)
            {
                JOptionPane.showMessageDialog(getFrame(), "InterruptedException " + ex.toString());
            }

            if(attempts >= ATTEMPTS_THRESHOLD)
            {
                JOptionPane.showMessageDialog(getFrame(), "Request failed due to too many attempts");
            }

            progressBar.setValue(0);
            progressBar.setIndeterminate(true); 
            sendButton.setEnabled(true);
            progressBar.setVisible(false);
            ConsoleView.this.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            worker = null;
            return true;
        }

    }

    public ConsoleView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ConsoleApp.getApplication().getMainFrame();
            aboutBox = new ConsoleAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ConsoleApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        IpAddressField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        PortField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ddb.console.ConsoleApp.class).getContext().getResourceMap(ConsoleView.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        IpAddressField.setText(resourceMap.getString("IpAddressField.text")); // NOI18N
        IpAddressField.setName("IpAddressField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        PortField.setText(resourceMap.getString("PortField.text")); // NOI18N
        PortField.setName("PortField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ddb.console.ConsoleApp.class).getContext().getActionMap(ConsoleView.class, this);
        connectButton.setAction(actionMap.get("connectToServer")); // NOI18N
        connectButton.setText(resourceMap.getString("connectButton.text")); // NOI18N
        connectButton.setName("connectButton"); // NOI18N

        disconnectButton.setAction(actionMap.get("disconnect")); // NOI18N
        disconnectButton.setText(resourceMap.getString("disconnectButton.text")); // NOI18N
        disconnectButton.setName("disconnectButton"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IpAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PortField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disconnectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(IpAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(PortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disconnectButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        logTextArea.setBackground(resourceMap.getColor("logTextArea.background")); // NOI18N
        logTextArea.setColumns(20);
        logTextArea.setEditable(false);
        logTextArea.setFont(resourceMap.getFont("logTextArea.font")); // NOI18N
        logTextArea.setRows(5);
        logTextArea.setName("logTextArea"); // NOI18N
        jScrollPane2.setViewportView(logTextArea);

        jSplitPane1.setRightComponent(jScrollPane2);

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        queryTextArea.setColumns(20);
        queryTextArea.setFont(resourceMap.getFont("queryTextArea.font")); // NOI18N
        queryTextArea.setRows(5);
        queryTextArea.setEnabled(false);
        queryTextArea.setName("queryTextArea"); // NOI18N
        jScrollPane1.setViewportView(queryTextArea);

        sendButton.setAction(actionMap.get("sendQuery")); // NOI18N
        sendButton.setText(resourceMap.getString("sendButton.text")); // NOI18N
        sendButton.setName("sendButton"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(sendButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void connectToServer() {

        if(socket != null)
        {
            JOptionPane.showMessageDialog(getFrame(), "Already connected");
            return;
        }

         try {
            socket = new Socket(IpAddressField.getText(), Integer.parseInt(PortField.getText()));
            //socket.setSoTimeout(SOCKET_TIMEOUT); // TODO: uncomment
         } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(getFrame(), "Invalid port number: " + ex.getMessage());
            return;
         } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(getFrame(), "UnknownHostException: " + ex.getMessage());
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(getFrame(), "IOException: " + ex.getMessage());
            return;
        }

        connectButton.setEnabled(false);
        disconnectButton.setEnabled(true);
        sendButton.setEnabled(true);
        queryTextArea.setEnabled(true);
    }

    @Action
    public void disconnect() {

        if(worker != null)
            worker.cancel(true);

        if(socket == null)
        {
            JOptionPane.showMessageDialog(getFrame(), "Not connected");
            return;
        }

        try {
            socket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(getFrame(), "IOException: " + ex.getMessage());
        }

        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        sendButton.setEnabled(false);
        queryTextArea.setEnabled(false);
        
        socket = null;
    }

    @Action
    public void sendQuery() {

        if(socket == null)
        {
            JOptionPane.showMessageDialog(getFrame(), "Not connected");
            return;
        }

        if(worker != null && !worker.isDone())
        {
            JOptionPane.showMessageDialog(getFrame(), "Already processing");
            return;
        }

        sendButton.setEnabled(false);

        worker = new ConsoleWorker();
        worker.execute();
    }

    private int getSleepTime()
    {
        Random random = new Random();
        return SLEEP_MIN + random.nextInt(SLEEP_MAX - SLEEP_MIN);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IpAddressField;
    private javax.swing.JTextField PortField;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea queryTextArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private Socket socket = null;
    private ConsoleWorker worker = null;

    private JDialog aboutBox;
}
