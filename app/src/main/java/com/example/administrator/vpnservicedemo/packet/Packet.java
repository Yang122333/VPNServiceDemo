package com.example.administrator.vpnservicedemo.packet;

import java.util.Arrays;

public class Packet {

    protected byte[] data;
    private int offset;
    public Packet(byte[] data ,int offset){
        this.data = data;
        this.offset = offset;
    }

    /**
     *一开始打算直接返回数据部分后来发现在构
     // 造函数的时候无法获取头部长度，只有在IPpacket构造函数中的才能获取ip的头部长度
     * @return data
     * // 返回数据部分//
     */
    @Override
    public String toString() {
//        byte[] mdata =new byte[data.length-offset]; //
//        System.arraycopy(data,offset,mdata,0,data.length-offset);
       return Arrays.toString(data);
    }
}
