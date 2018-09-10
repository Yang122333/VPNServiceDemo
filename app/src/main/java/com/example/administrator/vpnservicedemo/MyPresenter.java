package com.example.administrator.vpnservicedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;

public class MyPresenter {
    public static final int VPN_START = 1;
    private Context mContext;

    public MyPresenter(Context context) {
        mContext = context;
    }

    public void startVpn() {
        Intent intent = VpnService.prepare(mContext);
        if (intent != null) {
            ((Activity) mContext).startActivityForResult(intent, VPN_START);
        } else {
            Intent intent1 = new Intent(mContext, MyVPNService.class);
            mContext.startService(intent1);
        }

    }
}
