package ddb;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import org.w3c.dom.Document;

public class Config {
	
	private final static String LOGGING_NAME = "Config";
	
	private static int port;
	private static int maxNodes;
	private static String database;
    private static String host;
    private static String user;
    private static String password;
    
    public static int Port()
    {
    	return port;
    }
    
    public static int MaxNodes()
    {
    	return maxNodes;
    }
    
    public static int MinNodes()
    {
    	return maxNodes == -1 ? Integer.MAX_VALUE : maxNodes / 2 + 1;
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
	    
	    	port 		= Integer.parseInt(xPath.evaluate("/config/port", inputSource));
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
