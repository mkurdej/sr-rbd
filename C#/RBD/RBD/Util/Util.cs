// +-

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RBD.Util
{
    /**
     * Contains functions shared across various classes and packages
     * 
     * @author Marek Kurdej (curdeius[at]gmail.com)
     * 
     */
    class Util
    {
        /**
         * Private constructor to prevent instantiating the class
         */
        private Util()
        {
        }

        //static byte[] mac = getHardwareAddress();
        static Random rnd = new Random();
        const int GUID_LENGTH = 16; // bytes
        static byte[] bytes = new byte[GUID_LENGTH];

        //static {
        //    // get MAC address
        //    mac = getHardwareAddress();
        //    rnd = new Random();
        //}

        private static long lastTime = Int64.MinValue;

        /**
         * Generates a pseudo-GUID, based on current time and MAC address.
         * 
         * @return GUID as a String
         */
        public static String generateGUID()
        {
            //// get time (always different value even if the resolution of the clock
            //// is small)
            //long time = getNewTime();
            //// get random number based on time
            ////rnd.setSeed(time); // not needed in C# -- a time-dependent seed set in constructor
            //rnd.NextBytes(bytes);
            //StringBuilder guid = new StringBuilder();
            //for (int i = 0; i < bytes.Length; ++i)
            //{
            //    guid.append(bytes[i] ^ mac[i % mac.length]);
            //}
            //return guid.ToString();
            return System.Guid.NewGuid().ToString("D"); // 32-digits separated by hyphens
        }

        private static long getNewTime()
        {
            // TODO rename
            long timeMillis = DateTime.Now.Ticks; //System.currentTimeMillis();
            if (timeMillis > lastTime)
            {
                lastTime = timeMillis;
            }
            else
            {
                timeMillis = ++lastTime;
            }
            return timeMillis;
        }

        //public static String getMACAddress()
        //{
        //    return formatMAC(getHardwareAddress());
        //}

        //private static byte[] getHardwareAddress()
        //{
            //try
            //{
            //    InetAddress address = InetAddress.getLocalHost();
            //    NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //    if (ni != null)
            //    {
            //        return ni.getHardwareAddress();
            //    }
            //}
            //catch (Exception e)
            //{
            //    e.printStackTrace();
            //}
        //    return null;
        //}

        private static String formatMAC(byte[] macBytes)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < macBytes.Length; i++)
            {
                //sb.AppendFormat(String.format("%02X%s", macBytes[i],
                //        (i < macBytes.Length - 1) ? "-" : ""));
            }
            return sb.ToString();
        }
    }
}