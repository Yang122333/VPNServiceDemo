package com.example.administrator.vpnservicedemo.packet;

public class TcpPacket extends Packet {
    public static final int TCP_SOURCE_PORT = 0,//源端口号 2byte
    TCP_DEST_PORT = 2,// 目的端口号2byte
    TCP_SEQ = 4,//序列号4byte
    TCP_ACK = 8,//确认号4byte
    TCP_HEADERLENGTH = 12,//头部长度4bit
    TCP_IDENTIFICATION = 13 , //标志位 后6bit,
    TCP_WINDOWSIZE = 14;//窗口尺寸 2byte


    public static final byte URG=0x20;
    public static final byte ACK=0x10;
    public static final byte PSH=0x08;
    public static final byte RST=0x04;
    public static final byte SYN=0x02;
    public static final byte FIN=0x01;

    private IPPacket mIpInfo=null;
    private int mSourcePort,mDestPort;
    private int seq_number,ack_number;
    public int mHeaderLength;
    private int mWindowSize;
    private int dataLen=0;
    public boolean urg, ack, psh, rst, syn, fin;
    public TcpPacket(byte[] data ,int offset,IPPacket ipPacket) {
        super(data,offset);
        mIpInfo = ipPacket;
        //2byte
        mSourcePort = (data[offset]&0xff<<8)|(data[offset+1]&0xff);
        mDestPort = (data[offset+TCP_DEST_PORT]&0xff<<8)|(data[offset+TCP_DEST_PORT+1]&0xff);

        seq_number = (data[offset + TCP_SEQ]&0xff)<<(32-8)|(data[offset + TCP_SEQ + 1]&0xff)<<(32-8*2)|
                (data[offset + TCP_SEQ + 2]&0xff)<<(32-8*3)|(data[offset + TCP_SEQ + 3]&0xff)<<(32-8*4);

        ack_number =(data[offset + TCP_ACK]&0xff)<<(32-8)|(data[offset + TCP_ACK + 1]&0xff)<<(32-8*2)|
                (data[offset + TCP_ACK + 2]&0xff)<<(32-8*3)|(data[offset + TCP_ACK + 3]&0xff)<<(32-8*4);

        mHeaderLength = (data[offset + TCP_HEADERLENGTH]&0xff)>>4;
        mHeaderLength *= 4;
        dataLen = data.length-offset-mHeaderLength;
        urg = (data[offset+TCP_IDENTIFICATION]&0xff)<<26>>31== 1;   //先<<26清除比urg位高的位数值然后>>31置位最低位 然后与1比较
        ack = (data[offset+TCP_IDENTIFICATION]&0xff)<<27>>31== 1;
        psh = (data[offset+TCP_IDENTIFICATION]&0xff)<<28>>31== 1;
        rst = (data[offset+TCP_IDENTIFICATION]&0xff)<<29>>31== 1;
        syn = (data[offset+TCP_IDENTIFICATION]&0xff)<<30>>31== 1;
        fin = (data[offset+TCP_IDENTIFICATION]&0xff)<<31>>31== 1;

        mWindowSize = ((data[offset+TCP_WINDOWSIZE]&0xff)<<8)|(data[offset+TCP_WINDOWSIZE+1]&0xff);
    }

    public int getmDestPort() {
        return mDestPort;
    }

    public int getmSourcePort() {
        return mSourcePort;
    }

    public int getAck_number() {
        return ack_number;
    }

    public int getSeq_number() {
        return seq_number;
    }
    public String getHeader(){
        StringBuilder sb = new StringBuilder();
        sb.append("  TcpPacket : sourcePort:").append(mSourcePort).append("  destPort:").append(mDestPort)
                .append("  seq:").append(seq_number).append("  ack:").append(ack_number)
                .append("  headerLength:").append(mHeaderLength).append("  Identification:");  //urg, ack, psh, rst, syn, fin TCP_IDENTIFICATION
        if(urg){
            sb.append("URG,  ");
        }
        if(ack){
            sb.append("ACK,  ");
        }
        if(psh){
            sb.append("PSH,  ");
        }
        if(rst){
            sb.append("RST,  ");
        }
        if(syn){
            sb.append("SYN,  ");
        }
        if(urg){
            sb.append("URG  ");
        }
        sb.append("  dataLen:").append(dataLen);

        return sb.toString();
    }

}
