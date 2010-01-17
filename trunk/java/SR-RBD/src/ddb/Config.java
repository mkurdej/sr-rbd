package ddb;

import java.net.InetAddress;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import org.w3c.dom.Document;

public class Config {
	
	private final static String LOGGING_NAME = "Config";
	
	private static InetAddress tcpaddress;
	private static int tcpPort;
	private static int udpPort;
	private static int maxNodes;
	private static String database;
    private static String host;
    private static String user;
    private static String password;
    
    public static int TcpPort()
    {
    	return tcpPort;
    }
    
    public static InetAddress TcpAddress()
    {
    	return tcpaddress;
    }
    
    public static int UdpPort()
    {
    	return udpPort;
    }
    
    public static int MaxNodes()
    {
    	return maxNodes;
    }
    
    public static int MinOtherNodes()
    {
    	return maxNodes == -1 ? 0 : maxNodes / 2;
    }
    
    public static String Database()
    {
    	return database;
    }
    
    public static String Host()
    {
    	return host;
    }
    
    public static String User()
    {
    	return user;
    }
    
    public static String Password()
    {
    	return password;
    }
    
    public static boolean Load(String path)
    {
    	try
    	{
	    	Document inputSource = DocumentBuilderFactory.newInstance().
	    		newDocumentBuilder().parse(path);    
	    	
	    	XPathFactory factory = XPathFactory.newInstance();
	    	XPath xPath = factory.newXPath();
	    
	    	tcpaddress 	= InetAddress.getByName(xPath.evaluate("/config/tcpaddress", inputSource));
	    	tcpPort 	= Integer.parseInt(xPath.evaluate("/config/tcpport", inputSource));
	    	udpPort 	= Integer.parseInt(xPath.evaluate("/config/udpport", inputSource));
	    	maxNodes 	= Integer.parseInt(xPath.evaluate("/config/maxnodes", inputSource));
	    	database 	= xPath.evaluate("/config/database", inputSource);
	    	host 		= xPath.evaluate("/config/host", inputSource);
	    	user 		= xPath.evaluate("/config/user", inputSource);
	    	password 	= xPath.evaluate("/config/password", inputSource);
    	}
    	catch(Exception ex)
    	{
    		Logger.getInstance().log(
				"Config parsing error " + ex.toString(), 
				LOGGING_NAME, 
				Logger.Level.WARNING);
    		
    		return false;
    	}
    	
    	return true;
    }
	
}
