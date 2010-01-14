package ddb.msg;

import java.io.IOException;

import ddb.communication.DataInputStream;
import ddb.communication.DataOutputStream;
import ddb.msg.Message;

public class HelloMessage extends Message 
{
	// TODO: zaimplementowac knostrukt przyjmujacy numer wersji bazy danych
	// TODO: dodac zmienna przechowywujaca numer wersji bazy danych
	
	@Override
	public void fromBinary(DataInputStream s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageType getType() {
		return MessageType.HELLO_MESSAGE;
	}

	@Override
	public void toBinary(DataOutputStream s) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
