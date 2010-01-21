// +
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Security.Permissions;

namespace RBD
{
    class Program
    {
        const String LOGGING_NAME = "Config";
        static void Main(string[] args)
        {
            if (Config.Load(args.Length == 0 ? "./config.xml" : args[0]))
            {
                // launch
                Logger.getInstance().log("Starting node", LOGGING_NAME, Logger.Level.INFO);
                Dispatcher dispatcher = new Dispatcher();

                Logger.getInstance().log("Running dispatcher", LOGGING_NAME, Logger.Level.INFO);

                try
                {
                    dispatcher.Run();
                }
                catch (ThreadInterruptedException e)
                {
                    Logger.getInstance().log("InterruptedException" + e.Message, LOGGING_NAME, Logger.Level.SEVERE);
                }
            }
            else
            {
                Logger.getInstance().log("Error while reading config", LOGGING_NAME, Logger.Level.INFO);
            }

            Logger.getInstance().log("Closing node", LOGGING_NAME, Logger.Level.INFO);
        }
    }
}
