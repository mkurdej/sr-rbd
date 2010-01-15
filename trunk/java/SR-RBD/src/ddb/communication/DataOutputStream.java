package ddb.communication;

import java.io.IOException;
import java.io.OutputStream;

public class DataOutputStream extends java.io.DataOutputStream
{
	public DataOutputStream(OutputStream out) {
		super(out);
	}
	
	public void writeString(String s) throws IOException {
		
		// transform string to bytes
		byte[] bytes = s.getBytes();
		
		// write length
		writeInt(bytes.length);
		
		// write content to stream
		write(bytes, 0, bytes.length);
	}
}
