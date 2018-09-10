package com.example.administrator.vpnservicedemo.packet;

public class PacketHandler {
    public static int flag = 0;
    public static byte[] bytes;
    byte[] mdata;
    int moffset;
    public static final int CASE1 = 1;
    public static final int CASE2 = 2;

    public PacketHandler(byte[] data, int offset) {
        mdata = data;
        moffset = offset;
        if (flag == 2) {
            byte[] newData = new byte[mdata.length + bytes.length];
            System.arraycopy(bytes, 0, newData, 0, bytes.length);
            System.arraycopy(mdata, 0, newData, bytes.length, mdata.length);
            getAllIp(newData);
        }

    }

    public static final int TRUNCATION = 4;

    /**
     * 1 所有IP数据包的总长度小于数组长度
     * 2 所有IP数据包的总长度大于数组长度（数据包被截断）
     *
     * @return flag
     */
    public int getAllIp(byte[] newdData) {

        while (TRUNCATION <= newdData.length - moffset) { //剩下的数组长度小于4时说明包的长度标志位不在该数组中

            //获取下一个包的长度如果大于剩下的数组长度说明ip包断了 或者 长度为0 即总长度小于数组长度
            if (getIpLength(moffset) == 0) {
                flag = CASE1;
                break;
            }
            if (getIpLength(moffset) > newdData.length - moffset) {
                flag = CASE2;
                break;
            }

            IPPacket ipPacket = new IPPacket(newdData, moffset);
            ipPacket.getHeader();
            moffset = ipPacket.getNextOffset();
        }
        if (flag == CASE2) {
            bytes = new byte[newdData.length - moffset]; // 被截断ip
        }
        return flag;

    }

    public int getIpLength(int offset) {
        return ((mdata[offset + IPPacket.IP_LENGTH] & 0xff) << 8) | (mdata[offset + IPPacket.IP_LENGTH + 1] & 0xff);// IP_LENGTH = 2 ,3 为总长度所在下标
    }
}
