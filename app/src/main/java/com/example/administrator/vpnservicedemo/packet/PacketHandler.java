package com.example.administrator.vpnservicedemo.packet;

public class PacketHandler {
    byte[] mdata ;
    int moffset;
     public  PacketHandler(byte[] data ,int offset){
        mdata = data;
        moffset = offset;
        getAllIp();
     }


    /**
     *
     * @return flag
     * 1 所有IP数据包的总长度小于数组长度
     * 2 所有IP数据包的总长度大于数组长度（数据包被截断）
     *    （1）包头的长度标志位不在该数组中
     *    （2）包头的长度标志位在该数组中
     */
     public int getAllIp(){
         int flag = 0;
         while(4 <= mdata.length -moffset ){ //剩下的数组长度小于4时说明包的长度标志位不在该数组中

             //获取下一个包的长度如果大于剩下的数组长度说明ip包断了 或者 长度为0 即总长度小于数组长度
             if(getIpLength(moffset) == 0){
                 flag = 1;
                 break;
             }
             if(getIpLength(moffset) > mdata.length -moffset){
                 flag =2;
                 break;
             }

             IPPacket ipPacket = new IPPacket(mdata,moffset);
             ipPacket.getHeader();
             moffset = ipPacket.getNextOffset();
         }
         if(flag == 2){
             byte[] bytes = new byte[mdata.length - moffset];
             if(bytes[0] == 0){              // 属于第一种情况
                  flag = 1;
             }
         }
        return flag;


     }
     public int getIpLength(int offset){
         return ((mdata[offset+2]&0xff)<<8)|(mdata[offset+3]&0xff);// IP_LENGTH = 2 ,3 为总长度所在下标
     }
}
