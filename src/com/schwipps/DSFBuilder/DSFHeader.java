package com.schwipps.DSFBuilder;

import java.nio.ByteBuffer;

public class DSFHeader {
	private byte[] b;
	
	public DSFHeader(byte[] b) {
		this.b = b;
		// TODO Auto-generated constructor stub
	}
	public DSFHeader(int instanceID, int messageType, int messageLength, boolean ackRequired, int iddVersion, int checksumSize){
		
		
	}
	
	//Byte methods
	public byte[] getByte() {
		return b;
	}
	public int getLength() {
		return b.length;
	}
	
	
	//Header GET field methods
	public int getInstanceID() {
		byte[] tempB = new byte[4];
		tempB[0] = (byte)0x00;
		tempB[1] = (byte)0x00;
		tempB[2] = (byte)0xFF;
		tempB[3] = (byte)0xFF;
		return ByteBuffer.wrap(tempB).getInt();

	}
	/*
	public int getMessageType() {
		
	}
	
	public int getMessageLength() {

		return ;
	}
	
	public boolean getAckRequired() {
		
	}
	
	public int getIDDVersion() {
		
	}
	
	public int getChecksumSize() {
		//BigEndian
		int bit1 = (b[7] >> 1) & 1;
		int bit2 = (b[7] >> 2) & 1;
		
		switch(bit1) {
		case(0):
			break;
		case(1):
			return 4;
		}
		
		switch(bit2) {
		case(0):
			break;
		case(1):
			return 2;
		}
		return 0;
	}
	
	
	//Header SET field methods
	public void setInstanceID() {
		
	}
	
	public void setMessageType() {
		
	}
	
	public void setMessageLength() {

	}
	
	public void setAckRequired(boolean value) {
		
	}
	
	public void getIDDVersion(int val) {
		
	}
	
	public void setChecksumSize() {

	}
	*/
}
