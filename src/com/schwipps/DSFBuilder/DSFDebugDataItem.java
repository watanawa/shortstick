package com.schwipps.DSFBuilder;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DSFDebugDataItem {
    /* Structure
    0   DataLength      1Byte uint
    1   DataItemAddress 4Byte uint
    (Optional)
    5   Data            1..255 Byte uint

    */


    private byte[] b;
    public DSFDebugDataItem(byte[] b){
        this.b = b;
    }
    //Unused
    public DSFDebugDataItem(int dataLength, long dataItemAddress, byte[] data){
        if(data == null){
            this.b = new byte[5];
        }
        else{
            this.b = new byte[5+ data.length];
        }
        setDataLength(dataLength);
        setDataItemAddress(dataItemAddress);
        setData(data);
    }
    //Used for RequestDebugData
    public DSFDebugDataItem(int dataLength, long dataItemAddress){
        this.b = new byte[5];
        setDataLength(dataLength);
        setDataItemAddress(dataItemAddress);
    }
    public byte[] getByte(){
        return b;
    }

    public int getDataLength(){
        byte[] temp = {0x00, 0x00,0x00,b[0]};
        return ByteBuffer.allocate(4).wrap(temp).getInt();
    }
    public long getDataItemAdress(){
        byte[] temp = {0x00, 0x00,0x00,0x00,b[1],b[2],b[3],b[4]};
        return ByteBuffer.wrap(temp).getLong();
    }
    public byte[] getData(){
        if(b.length == 5) {
            return null;
        }
        return Arrays.copyOfRange(b,5,b.length);
    }
    public int getDataItemLength(){
        return (5+getDataLength());
    }


    public void setDataLength(int dataLength){
        b[0] = ByteBuffer.allocate(4).putInt(dataLength).array()[3];
    }
    public void setDataItemAddress(long dataItemAddress){
        byte[] temp = ByteBuffer.allocate(8).putLong(dataItemAddress).array();
        b[1] = temp[4];
        b[2] = temp[5];
        b[3] = temp[6];
        b[4] = temp[7];
    }
    public void setData(byte[] data){
        if(data != null && data.length < 256){
            System.arraycopy(data, 0, b, 5, data.length);
        }
    }
}
