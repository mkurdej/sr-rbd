// +-

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace RBD
{
    public class Logger
    {

        static Logger instance = null;

        public enum Level
        {
            INFO, WARNING, SEVERE
        };

        private Logger()
        {
        }

        //synchronized 
        public static Logger getInstance()
        {
            lock (typeof(Logger))
            {

                if (instance == null)
                    instance = new Logger();

                return instance;
            }
        }

        //private String timestamp()
        //{
        //    Calendar calendar = new GregorianCalendar();
        //    int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //    int minute = calendar.get(Calendar.MINUTE);
        //    int seconds = calendar.get(Calendar.SECOND);
        //    int miliseconds = calendar.get(Calendar.MILLISECOND);

        //    DecimalFormat hourFormat = new DecimalFormat("#00");
        //    DecimalFormat minuteFormat = new DecimalFormat("#00");
        //    DecimalFormat secondsFormat = new DecimalFormat("#00");
        //    DecimalFormat milisecondsFormat = new DecimalFormat("#000");

        //    String timeString = hourFormat.format(hour) + ":"
        //            + minuteFormat.format(minute) + ":"
        //            + secondsFormat.format(seconds) + "."
        //            + milisecondsFormat.format(miliseconds);

        //    return timeString;
        //}

        private void printLog(String msg, String who, String level)
        {
            //StringBuilder sb = new StringBuilder();
            //Formatter formatter = new Formatter(sb);

            //formatter.format("%1$s <%2$3s> (%4$s) [%3$-15s] : %5$S",
            //    timestamp(),
            //    Thread.currentThread().getId(),
            //    who,
            //    level,
            //    msg
            //);
            //Console.WriteLine(formatter.toString());
        }

        //synchronized 
        public void log(String msg, String who, Level level)
        {
            lock (this)
            {
                switch (level)
                {
                    case Level.INFO:
                        printLog(msg, who, "INFO");
                        break;
                    case Level.WARNING:
                        printLog(msg, who, "WARNING");
                        break;
                    case Level.SEVERE:
                        printLog(msg, who, "SEVERE");
                        Environment.Exit(1);
                        break;
                }
            }
        }
    }
}
