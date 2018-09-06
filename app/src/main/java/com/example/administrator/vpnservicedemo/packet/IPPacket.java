package com.example.administrator.vpnservicedemo.packet;

public class IPPacket extends Packet{
    public static final int IPVERSION_AND_HEADERLENGTH= 0,//版本号4 bit和头长4 bit
    TYPE_OF_SERVICE = 1,//服务类型
    IP_LENGTH = 2,//数据包总长度2byte
    IDENTIFICATION = 4,//标志2byte
    TIME_OF_LIVE = 8 ,//生命期
    IP_PROTOCOL =9,//协议
    HEADER_CHECKSUM = 10,//头校验2byte
    IP_SOURCE = 12,//源地址4bit
    IP_DESTINATION = 16 ;//目标地址4byte

    private byte mVersion,mHeaderLength,mProtocol;
    private byte[] mSourceIp , mDestIp;
    private int length;
    private int TOL;
    public Packet mPacket;
    public IPPacket(byte[] data) {
        super(data,0);
        mVersion = (byte)(data[IPVERSION_AND_HEADERLENGTH]>>4);
        mHeaderLength = (byte)(data[IPVERSION_AND_HEADERLENGTH]&0x0f);
        mHeaderLength *= 4;
        length = ((data[IP_LENGTH]&0xff)<<8)|(data[IP_LENGTH+1]&0xff);
        TOL = data[TIME_OF_LIVE];
        mProtocol = data[IP_PROTOCOL];

        mSourceIp = new byte[4];
        mSourceIp[0] =data[IP_SOURCE];
        mSourceIp[1] =data[IP_SOURCE + 1];
        mSourceIp[2] =data[IP_SOURCE + 2];
        mSourceIp[3] =data[IP_SOURCE + 3];

        mDestIp = new byte[4];
        mDestIp[0] = data[IP_DESTINATION];
        mDestIp[1] = data[IP_DESTINATION + 1];
        mDestIp[2] = data[IP_DESTINATION + 2];
        mDestIp[3] = data[IP_DESTINATION + 3];

        if(mProtocol == 6){
            mPacket = new TcpPacket(data,mHeaderLength,this);
        }
        else{
            //其他协议数据包
            mPacket = new Packet(data,0);
        }

    }

    public String getHeader(){
        StringBuilder  sb = new StringBuilder();
        sb.append("IPPacket  Version : ").append(mVersion)
                .append("  IHL : ").append(mHeaderLength)
                .append(" IP_length: ").append(length)
                .append(" time of live : ").append(TOL)
                .append("  Protocol ").append(mProtocol)
                .append(" SourceIP : ").append(ipToString(mSourceIp))
                .append(" destIP : ").append(ipToString(mDestIp));
        return sb.toString();
    }
    public byte[] getmSourceIp() {
        return mSourceIp;
    }
    public String ipToString(byte[] ip){
        StringBuilder sb = new StringBuilder(15);//xxx.xxx.xxx.xxx
        sb.append(mSourceIp[0]).append(".")
                .append(mSourceIp[1]).append(".")
                .append(mSourceIp[2]).append(".")
                .append(mSourceIp[3]);
        return sb.toString();
    }

    public byte[] getmDestIp() {
        return mDestIp;
    }
    public Packet getIpData(){
            return mPacket;
    }

}
