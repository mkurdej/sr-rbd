using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace RBD
{
    class Logger
    {
        static Logger instance = new Logger();

        Logger()
        {
        }

        public static Logger getInstance()
        {
            return instance;
        }

        public enum Level { INFO, WARNING, SEVERE };

        private String timestamp()
        {
            DateTime currentTime = DateTime.Now;
            return currentTime.ToString("HH:mm:ss.fff");
        }

        private void info(String msg, String who)
        {
            Console.WriteLine(timestamp() + " [" + who + "](INFO):  " + msg);
        }

        private void warning(String msg, String who)
        {
            Console.WriteLine(timestamp() + " [" + who + "](WARNING):  " + msg);
        }

        private void severe(String msg, String who)
        {
            Console.WriteLine(timestamp() + " [" + who + "](SEVERE):  " + msg);
        }

        public void log(String msg, String who, Level level)
        {
            lock (typeof(Logger))
            {
                switch (level)
                {
                    case Level.INFO:
                        info(msg, who);
                        break;
                    case Level.WARNING:
                        warning(msg, who);
                        break;
                    case Level.SEVERE:
                        severe(msg, who);
                        Thread.Sleep(10000);
                        Environment.Exit(1);
                        break;
                }
            }
        }
    }
}
