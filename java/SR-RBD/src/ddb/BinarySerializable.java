package ddb;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;

public interface BinarySerializable {
	public void fromBinary(DataInputStream s) throws IOException;
	public void toBinary(DataOutputStream s) throws IOException;
}
