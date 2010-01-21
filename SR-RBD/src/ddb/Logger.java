/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ddb;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;

/**
 *
 * @author xeonic
 */
public class Logger
{

    private static Logger instance = null;

    public enum Level
    {
        INFO, WARNING, SEVERE
    };

    private Logger()
    {
    }

    synchronized public static Logger getInstance()
    {
    	if( instance == null )
    		instance = new Logger();
    	
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

    private void printLog(String msg, String who, String level)
    {
    	StringBuilder sb = new StringBuilder();
    	Formatter formatter = new Formatter(sb);

    	formatter.format("%1$s <%2$3s> (%4$s) [%3$-15s] : %5$S", 
			timestamp(), 
			Thread.currentThread().getId(), 
			who, 
			level,
			msg
		);

    	System.out.println(formatter.toString());
    }

    synchronized public void log(String msg, String who, Level level)
    {
        switch (level)
        {
            case INFO:
            	printLog(msg, who, "INFO");
                break;
            case WARNING:
            	printLog(msg, who, "WARNING");
                break;
            case SEVERE:
            	printLog(msg, who, "SEVERE");
                System.exit(1);
                break;
            
        }
    }
}
