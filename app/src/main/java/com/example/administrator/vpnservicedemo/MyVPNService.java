package com.example.administrator.vpnservicedemo;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.example.administrator.vpnservicedemo.packet.PacketHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MyVPNService extends VpnService {
    public static final String TAG = "yangge'packet:  ";
    public static final int MAXCUSHION = 32767;
    public static final String MYADRESS = "10.0.10.0";
    public static final int ADDRESS_PREFIXLENGTH = 32;
    public static final int ROUTE_PREFIXLENGTH = 0;
    public static final String MYROUTE = "0.0.0.0";
    public static final String MYSESSION = "yang's capture";
    public static final int START_OFFSET = 0;
    public static final int MAXMTU = 1500;
    ParcelFileDescriptor minterface;
    boolean isReady = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {
            @Override
            public void run() {
                work();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void work() {
        if (!isReady) {
            build();
        }
        if (isReady) {
            readPackets();
        }
    }

    public void build() {
        Builder builder = new Builder();
        builder.setMtu(MAXMTU);
        builder.addAddress(MYADRESS, ADDRESS_PREFIXLENGTH);
        builder.addRoute(MYROUTE, ROUTE_PREFIXLENGTH);
        builder.setSession(MYSESSION);
        minterface = builder.establish();
        isReady = true;

    }

    public void readPackets() {
        FileInputStream in = new FileInputStream(minterface.getFileDescriptor());
        ByteBuffer packet = ByteBuffer.allocate(MAXCUSHION);
        while (true) {
            try {
                int length = in.read(packet.array());

                if (length > 0) {
//                    Log.i(TAG, new String (packet.array()));//乱码
                    byte[] b = new byte[length];
                    System.arraycopy(packet.array(), 0, b, 0, length);
                    PacketHandler packetHandler = new PacketHandler(b, START_OFFSET);
                    packet.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
