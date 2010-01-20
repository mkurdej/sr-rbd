// + TODO check

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

        private String timestamp()
        {
            DateTime now = DateTime.Now;
            String timeString = String.Format("{0:00}", now.Hour) + ":"
                        + String.Format("{0:00}", now.Minute) + ":"
                        + String.Format("{0:00}", now.Second) + "."
                        + String.Format("{0:000}", now.Millisecond);
            return timeString;
        }

        private void printLog(String msg, String who, String level)
        {
            StringBuilder sb = new StringBuilder();
            sb.AppendFormat("{0} ", timestamp());
            sb.AppendFormat("<{0,3}> ", AppDomain.GetCurrentThreadId());   // Thread.CurrentThread.ManagedThreadId
            sb.AppendFormat("({0}) ", who);
            sb.AppendFormat("[{0,-15}] : ", level);
            sb.AppendFormat("{0}", msg);
            Console.WriteLine(sb.ToString());
        }

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
