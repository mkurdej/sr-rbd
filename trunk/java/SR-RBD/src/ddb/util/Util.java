/**
 * 
 */
package ddb.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Random;

/**
 * Contains functions shared across various classes and packages
 * 
 * @author Marek Kurdej (curdeius[at]gmail.com)
 * 
 */
public class Util {

	/**
	 * Private constructor to prevent instantiating the class
	 */
	private Util() {
	}

	static byte[] mac;
	static Random rnd = null;
	static final int GUID_LENGTH = 16; // bytes
	static byte[] bytes = new byte[GUID_LENGTH];

	static {
		// get MAC address
		mac = getHardwareAddress();
		rnd = new Random();
	}

	private static long lastTime = Long.MIN_VALUE;

	/**
	 * Generates a pseudo-GUID, based on current time and MAC address.
	 * 
	 * @return GUID as a String
	 */
	public static String generateGUID() {
		// get time (always different value even if the resolution of the clock
		// is small)
		long time = getNewTime();
		// get random number based on time
		rnd.setSeed(time);
		rnd.nextBytes(bytes);
		StringBuilder guid = new StringBuilder();
		for (int i = 0; i < bytes.length; ++i) {
			guid.append(bytes[i] ^ mac[i % mac.length]);
		}
		System.out.println(guid.toString());
		return guid.toString();
	}

	private static long getNewTime() {
		long timeMillis = System.currentTimeMillis();
		if (timeMillis > lastTime) {
			lastTime = timeMillis;
		} else {
			timeMillis = ++lastTime;
		}
		return timeMillis;
	}

	public static String getMACAddress() {
		return formatMAC(getHardwareAddress());
	}

	private static byte[] getHardwareAddress() {
		try {
			/*
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			*/
			NetworkInterface ni = NetworkInterface.getNetworkInterfaces().nextElement();
			if(ni != null)
			{
				return ni.getHardwareAddress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String formatMAC(byte[] macBytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < macBytes.length; i++) {
			sb.append(String.format("%02X%s", macBytes[i],
					(i < macBytes.length - 1) ? "-" : ""));
		}
		return sb.toString();
	}

}
