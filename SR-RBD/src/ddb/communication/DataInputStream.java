package ddb.communication;

import java.io.IOException;
import java.io.InputStream;

import ddb.Config;

public class DataInputStream extends java.io.DataInputStream
{
	public DataInputStream(InputStream in){
		super(in);
	}
	
	public String readString() throws IOException
	{
		// read bytes length
		int length = readInt();
		
		// read bytes
		byte[] bytes = new byte[length];
		int left = length;
		
		while(left > 0)
			left -= read(bytes, length - left, left);
		
		// convert bytes using given encoding
		return new String(bytes, Config.getCharset());
	}
}
