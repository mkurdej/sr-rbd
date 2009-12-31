/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ddb;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author xeonic
 */
public class Logger
{

    private final static Logger instance = new Logger();

    public enum Level
    {

        INFO, WARNING, SEVERE
    };

    private Logger()
    {
    }

    synchronized public static Logger getInstance()
    {
        return instance;
    }

    private String timestamp()
    {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int miliseconds = calendar.get(Calendar.MILLISECOND);

        DecimalFormat hourFormat = new DecimalFormat("#00");
        DecimalFormat minuteFormat = new DecimalFormat("#00");
        DecimalFormat secondsFormat = new DecimalFormat("#00");
        DecimalFormat milisecondsFormat = new DecimalFormat("#000");

        String timeString = hourFormat.format(hour) + ":"
                + minuteFormat.format(minute) + ":"
                + secondsFormat.format(seconds) + "."
                + milisecondsFormat.format(miliseconds);

        return timeString;
    }

    private void info(String msg, String who, String time)
    {
        System.out.println(time + " [" + who + "] (INFO):  " + msg);
    }

    private void warning(String msg, String who, String time)
    {
        System.out.println(time + " [" + who + "] (WARNING):  " + msg);
    }

    private void severe(String msg, String who, String time)
    {
        System.out.println(time + " [" + who + "] (SEVERE):  " + msg);
    }

    synchronized public void log(String msg, String who, Level level)
    {
        switch (level)
        {
            case INFO:
                info(msg, who, timestamp());
                break;
            case SEVERE:
                severe(msg, who, timestamp());
                System.exit(1);
                break;
            case WARNING:
                warning(msg, who, timestamp());
                break;
        }
    }
}
