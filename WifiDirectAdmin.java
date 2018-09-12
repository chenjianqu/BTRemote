package com.qu.jian.btremote.Net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

/**
 * Created by Jianqv on 2017/8/28.
 */

public class WifiDirectAdmin{

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    Context context;

    public WifiDirectAdmin(Context context){
        this.context=context;
        wifiP2pManager= (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel=wifiP2pManager.initialize(context, Looper.getMainLooper(),null);//用于后续对WiFi P2P框架的连接。
    }


    public void searchDevices(WifiP2pManager.ActionListener listener){
        wifiP2pManager.discoverPeers(channel,listener);
    }

    public void registerWifiDirectBCReceiver(Context c,BroadcastReceiver searchWifiDirectDevice){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);//连接状态发生改变
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);//wifi对等网络是否启用
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);//wifi对等网络可用列表是否发生改变
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);//本设备的配置信息是否发生改变
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);//表示该设备的配置信息发生改变
        c.registerReceiver(searchWifiDirectDevice,intentFilter);
    }

    public WifiP2pManager getWifiP2pManager(){
        return wifiP2pManager;
    }
    public WifiP2pManager.Channel getChannel(){
        return channel;
    }

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {//监听wifi直连状态的变化
            //InetAddress from WifiP2pInfo struct.
            String groupOwnerAddress =info.groupOwnerAddress.getHostAddress();
            // After thegroup negotiation, we can determine the group owner.
            if (info.groupFormed && info.isGroupOwner) {
                // Do whatever tasks are specific to the group owner.
                // One common case is creating a server thread and accepting
                // incoming connections.
            } else if (info.groupFormed) {
                // The other device acts as the client. In this case,
                // you'll want to create a client thread that connects to the group
                // owner.
            }
        }
    };



}
