package com.example.administrator.vpnservicedemo;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.example.administrator.vpnservicedemo.packet.IPPacket;
import com.example.administrator.vpnservicedemo.packet.TcpPacket;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MyVPNService extends VpnService {
    public static final String TAG = "yangge'packet:  ";
    public static final int maxMtu = 1500;
    ParcelFileDescriptor minterface ;
    boolean isReady = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(){
            @Override
            public void run() {
                work();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void work(){
        if(!isReady){
            build();
        }
        if(isReady){
            readPackets();
        }
    }
    public void  build(){
        Builder builder = new Builder();
        builder.setMtu(maxMtu);
        builder.addAddress("10.0.10.0",32);
        builder.addRoute("0.0.0.0",0);
        builder.setSession("yang's capture");
        minterface = builder.establish();
        isReady = true;

    }

    public void readPackets(){
        FileInputStream in = new FileInputStream(minterface.getFileDescriptor());
        ByteBuffer packet = ByteBuffer.allocate(32767);
        while (true){
            try {
                int length = in.read(packet.array());
                if(length > 0){
//                    Log.i(TAG, new String (packet.array()));//乱码
                    IPPacket ipPacket = new IPPacket(packet.array());
                    TcpPacket tcpPacket = (TcpPacket) ipPacket.mPacket;
                    Log.i(TAG, Arrays.toString(packet.array()));
                    Log.i(TAG, ipPacket.getHeader());
                    Log.i(TAG,tcpPacket.getHeader() );

                    packet.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
