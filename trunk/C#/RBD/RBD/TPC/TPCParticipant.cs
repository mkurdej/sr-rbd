// +- TODO exceptions + logging, check

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using RBD.Msg;
using RBD.Msg.Client;

namespace RBD.TPC
{
    public abstract class TPCParticipant : MessageRecipient, TimeoutListener
    {
        public const int TIMEOUT = 5000;

        /**
         * <!-- begin-UML-doc --> Identyfikator obecnie wykonywanej
         * transakcji. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public String getTransactionId()
        {
            return TransactionId;
        }
        public void setTransactionId(String transactionId)
        {
            TransactionId = transactionId;
        }
        public String TransactionId { get; set; }

        /**
         * <!-- begin-UML-doc --> Nazwa tabeli, na ktorej wykonywana jest
         * transakcja. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public String getTableName()
        {
            return TableName;
        }
        public void setTableName(String tableName)
        {
            TableName = tableName;
        }
        public String TableName { get; set; }

        private TimeoutGenerator timeoutGenerator;

        private MessageQueue messageQueue;

        protected volatile bool stopped = false;

        private HashSet<EndTransactionListener> endTransactionListeners;

        /**
         * <!-- begin-UML-doc --> Zapytanie, ktore ma zostac wykonane w bazie
         * danych. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        private String queryString;

        public RBD.DB.DatabaseState getDatabaseState()
        {
            return DatabaseState;
        }
        public void setDatabaseState(RBD.DB.DatabaseState databaseState)
        {
            DatabaseState = databaseState;
        }
        public RBD.DB.DatabaseState DatabaseState;

        protected RBD.DB.DbConnector connector;

        public TPCParticipant()
        {
            this.endTransactionListeners = new HashSet<EndTransactionListener>();
            this.timeoutGenerator = new TimeoutGenerator();
            this.messageQueue = new MessageQueue();

            this.timeoutGenerator.setTimeoutListener(this);

            startThread();
        }

        /**
         * <!-- begin-UML-doc --> Wstawienie wiadomosci do kolejki wiadomosci.
         * Wiadomosc zostania odebrana gdy zostanie wywolana metoda
         * waitForMessage(). <!-- end-UML-doc -->
         * 
         * @param message
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void processMessage(Message message)
        {
            //try {
            this.messageQueue.putMessage(message);
            //} catch(InterruptedException e) {
            //    Logger.getInstance().log(e.getMessage(), "TPC", Level.SEVERE);
            //} 

            ////this.onNewMessage(message);
        }

        /**
         * <!-- begin-UML-doc --> Oczekiwanie na nadejscie nowej wiadomosci. Gdy
         * nadejdzie nowa wiadomosc zostanie wywolany callback onNewMessage(). <!--
         * end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        private void waitForMessage()
        {
            //try {
            Message msg = this.messageQueue.getMessage();
            onNewMessage(msg);
            //} catch (InterruptedException e) {
            //    Logger.getInstance().log(e.getMessage(), "TPC", Logger.Level.SEVERE);
            //}
        }

        /**
         * <!-- begin-UML-doc -->
         * <p>
         * Wlacza timer.
         * </p>
         * <!-- end-UML-doc -->
         * 
         * @param miliseconds
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        protected void startTimer(long miliseconds)
        {
            this.timeoutGenerator.startTimer(miliseconds);
        }

        /**
         * <!-- begin-UML-doc --> Wylacza timer. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        protected void stopTimer()
        {
            this.timeoutGenerator.stopTimer();
        }

        /**
         * <!-- begin-UML-doc --> Metoda wywolywana, gdy nadejdzie nowa wiadomosc.
         * <!-- end-UML-doc -->
         * 
         * @param message
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        protected abstract void onNewMessage(Message message);

        /**
         * <!-- begin-UML-doc -->
         * <p>
         * Konczy transakcje. Trzeba poinformowac Dispatchera o koncu transakcji,
         * aby mogl zwolnic zasoby, zakonczyc watek. Wywolywana jest metoda
         * cleanupTransaction() w celu dokonania dodatakowych czynnosci w
         * zakonczeniu transakcji.
         * </p>
         * <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        //final
        public void endTransaction()
        {
            stopThread();
            cleanupTransaction();
            notifyEndTransactionListeners();
        }

        /**
         * <!-- begin-UML-doc --> Dokonanie dodatakowych czynnosci&nbsp;przy
         * zakonczeniu transakcji. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        protected abstract void cleanupTransaction();

        /**
         * <!-- begin-UML-doc --> Rozpoczyna&nbsp;prace nowego&nbsp;watku.
         * Oczekiwanie na nadejscie wiadomosci. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        private void startThread()
        {
            /*
            // można by skorzystać z domknięcia (jest typizowane)
            public Thread StartTheThread(SomeType param1, SomeOtherType param2) {
              var t = new Thread(() => RealStart(param1, param2));
              t.Start();
              return t;
            }
            private static void RealStart(SomeType param1, SomeOtherType param2) {
              ...
            }
            */
            new Thread(new ThreadStart(this.run)).Start();
            //    new Thread(new Runnable() {
            //        @Override
            //        public void run() {
            //            Logger.getInstance().log(
            //                    "startThread " + Thread.currentThread(), "TPC",
            //                    Level.INFO);
            //            while (!stopped) {
            //                waitForMessage();
            //            }
            //        }
            //    }, "TPC_THREAD").start();
        }

        void run()
        {
            // TODO
            Logger.getInstance().log("startThread " + Thread.CurrentThread.Name, "TPC", Logger.Level.INFO);
            while (!stopped)
            {
                waitForMessage();
            }
        }

        /**
         * <!-- begin-UML-doc --> Konczy prace watku. <!-- end-UML-doc -->
         * 
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        private void stopThread()
        {
            stopped = true;
        }

        /**
         * Dodaje obiekt, ktory chce zostac poinformowany o koncu transakcji.
         * 
         * @param listener
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void addEndTransactionListener(EndTransactionListener listener)
        {
            endTransactionListeners.Add(listener);
        }

        /**
         * Usuwa obiekt, który nie chce już więcej być informowany o końcu
         * transakcji.
         * 
         * @param listener
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void removeEndTransactionListener(EndTransactionListener listener)
        {
            endTransactionListeners.Remove(listener);
        }

        /**
         * Informuje wszystkich zainteresowanych o tym, ze zakonczono transakcje.
         */
        private void notifyEndTransactionListeners()
        {
            foreach (EndTransactionListener listener in this.endTransactionListeners)
            {
                listener.onEndTransaction(this);
            }
        }

        /**
         * <!-- begin-UML-doc --> Ustawia zapytanie, ktore ma zostac wykonane przez
         * baze danych w ramach transakcji <!-- end-UML-doc -->
         * 
         * @param queryString
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void setQueryString(String queryString)
        {
            this.queryString = queryString;
        }

        /**
         * <!-- begin-UML-doc -->
         * Pobiera&nbsp;zapytanie,&nbsp;ktore&nbsp;ma&nbsp;zostac
         * &nbsp;wykonane&nbsp;
         * przez&nbsp;baze&nbsp;danych&nbsp;w&nbsp;ramach&nbsp;transakcji <!--
         * end-UML-doc -->
         * 
         * @return
         * @generated 
         *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public String getQueryString()
        {
            return queryString;
        }

        public void setConnector(RBD.DB.DbConnector connector)
        {
            this.connector = connector;
        }

        /** 
         * <!-- begin-UML-doc -->
         * Callback&nbsp;wywolywany,&nbsp;gdy&nbsp;wystapi&nbsp;timeout
         * <!-- end-UML-doc -->
         * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
         */
        public void onTimeout()
        {
            processMessage(new TimeoutMessage());
        }
    }
}
