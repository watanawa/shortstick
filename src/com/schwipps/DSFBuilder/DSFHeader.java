package com.schwipps.DSFBuilder;

import com.schwipps.DSFBuilder.enums.MessageType;

import java.nio.ByteBuffer;

public class DSFHeader {
    /* Header Structure 8Byte
    0   InstanceID       2Byte uint  0-65,535
    2   MessageType      2Byte uint  0-65,535
    4   MessageLength    2Byte uint  0-65,535
    6   AckRequired      1Bit
    6.1 IDDVersion       7bit  uint  0-127 ALWAYS 2
    7   CheksumSize      2bit        0,2,4
    7.2 Reserved
    */


    private byte[] b;
    //+++++Constructors+++++
	public DSFHeader(byte[] b) {
		this.b = b;
	}
	public DSFHeader(int instanceID, MessageType messageType, int messageLength, boolean ackRequired, int iddVersion, int checksumSize){
	    b = new byte[8];
        setInstanceID(instanceID);
        setMessageType(messageType);
        setMessageLength(messageLength);
        setAckRequired(ackRequired);
        setIDDVersion(iddVersion);
        setChecksumSize(checksumSize);
	}
	

	public byte[] getByte() {
		return b;
	}
	public int getLength() {
		return b.length;
	}
	
	//+++++Field Getter+++++
	public int getInstanceID() {
		byte[] intTemp = new byte[4];
		intTemp[1] = 0x00;
		intTemp[2] = 0x00;
		intTemp[2] = b[0];
		intTemp[3] = b[1];
		return ByteBuffer.wrap(intTemp).getInt();
	}
	public MessageType getMessageType() {
        byte[] intTemp = new byte[4];
        intTemp[1] = 0x00;
        intTemp[2] = 0x00;
        intTemp[2] = b[2];
        intTemp[3] = b[3];
        int val = ByteBuffer.wrap(intTemp).getInt();
        for(MessageType messageType : MessageType.values()){
            if(messageType.getValue() == val ) return  messageType;
        }
        return MessageType.INVALID_MESSAGE_HANDLE;
	}
	public int getMessageLength() {
        byte[] intTemp = new byte[4];
        intTemp[1] = 0x00;
        intTemp[2] = 0x00;
        intTemp[2] = b[4];
        intTemp[3] = b[5];
        return ByteBuffer.wrap(intTemp).getInt();
	}
	public boolean getAckRequired() {
	    return ((b[6] & (0x80)) != 0) ; // 1000 0000
	}
	public int getIDDVersion() {
        byte[] intTemp = new byte[4];
        intTemp[0] = 0x00;
        intTemp[1] = 0x00;
        intTemp[2] = 0x00;
        intTemp[3] = (byte)(b[6] & 0x7F); // 0111 1111
        return ByteBuffer.wrap(intTemp).getInt();
	}
	public int getChecksumSize() {
		//BigEndian
		int bit1 = (b[7] >> 7) & 1;
		int bit2 = (b[7] >> 6) & 1;
		
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

	//+++++Field Setter+++++
    // 2Byte Uint 0-65,535
	public void setInstanceID(int val) {
        byte[] valueByte = intToByte(val);
        b[0] = valueByte[2];
        b[1] = valueByte[3];

	}
	// 2Byte Uint 0-65,535
	public void setMessageType(MessageType messageType) {
        byte[] valueByte = intToByte(messageType.getValue());
        b[2] = valueByte[2];
        b[3] = valueByte[3];
	}
    // 2Byte Uint 0-65,535
	public void setMessageLength(int val) {
        byte[] valueByte = intToByte(val);
        b[4] = valueByte[2];
        b[5] = valueByte[3];
	}

	public void setAckRequired(boolean value) {
		//Sets the first bit
	    if(value){
            b[6] |= 0x80 ; // 1000 0000 ^ ;
        }
        else{
            b[6] &= 0x7F;  // 0111 1111;
        }
	}
	// 7bit uint 0-127
	public void setIDDVersion(int val) {
        byte temp = intToByte(val)[3];
        // Take care of the AckRequired -> They are in the same byte
        if(getAckRequired()){
            b[6] = (byte)(temp|0x80); // 1000 0000
        }
        else{
            b[6] = (byte)(temp & 0x7F); //0111 1111
        }
	}
	
	public void setChecksumSize(int val) {
        // Sets the first two bit of the 7th byte
        // 00 no Checksum
        // 01 2 Byte Checksum
        // 10 4 Byte Checksum
	    byte temp = b[7];
	    switch (val){
            case(0):
                temp &= 0x3F; // 0011 1111
                break;
            case(2):
                temp &= 0x3F; // 0011 1111
                temp |= 0x40; // 0100 0000
                break;
            case(4):
                temp &= 0x3F; // 0011 1111
                temp |= 0x80; // 1000 0000
                break;
        }
        b[7] = temp;
	}

	private byte[] intToByte(int val){
	    return ByteBuffer.allocate(4).putInt(val).array();
    }

    public int getChecksum(){
        int sum = 0;
        byte[] temp = new byte[4];
        temp[0] = 0x00;
        temp[1] = 0x00;
        temp[2] = 0x00;

        for(byte byteSet : b ){
            temp[3] = byteSet;
            sum += ByteBuffer.wrap(temp).getInt();
        }
        return sum;
    }
}
