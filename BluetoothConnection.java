package com.qu.jian.btremote.Net;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.qu.jian.btremote.Config;
import com.qu.jian.btremote.R;

import java.io.IOException;
import java.io.Serializable;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Jianqv on 2017/8/27.
 */

public class BluetoothConnection implements Serializable{
    private static BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket btSocket;
    Context context=null;
    public final static String BLUETOOTHCONNECTION_KEY="1000";


    public BluetoothConnection(final Context context){
        this.context=context;
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            AlertDialog.Builder bdr=new AlertDialog.Builder(context)
                    .setTitle("无法使用")
                    .setMessage("缺少蓝牙功能,是否退出应用？")
                    .setCancelable(true)
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //通过直接杀死当前应用的进程来结束应用
                            android.os.Process.killProcess(android.os.Process.myPid());System.exit(0);
                            ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
                            manager.killBackgroundProcesses(context.getPackageName());
                        }
                    });
            bdr.show();
        }
    }


    public void serchDevice(){
        bluetoothAdapter.startDiscovery();
    }

    public void registerBTReceiver(Context context,BroadcastReceiver broadcastReceiver){
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        if(broadcastReceiver!=null)context.registerReceiver(broadcastReceiver, intent);
    }

    public void unregisterReceiver(Context context,BroadcastReceiver broadcastReceiver){
        if(broadcastReceiver!=null)context.unregisterReceiver(broadcastReceiver);
    }


    boolean aBoolean=false;
    public boolean connect(Context c,BluetoothDevice btDev) {
        try {
            btSocket = btDev.createRfcommSocketToServiceRecord(Config.MY_UUID);
            btSocket.connect();
            Toast.makeText(c, R.string.success_to_connect_remote_device,Toast.LENGTH_SHORT).show();
            Config.cacheStringConfig(context,Config.KEY_ADRESS_BT,btDev.getAddress());
            Config.cacheStringConfig(context,Config.KEY_NAME_BT,btDev.getName());
            aBoolean= true;
        } catch (IOException e) {
            Toast.makeText(c,R.string.fail_to_connect_remote_device,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            aBoolean=false;
        }
        return aBoolean;
    }
    public boolean isEnable(){
        boolean b=false;
        if(bluetoothAdapter!=null)if(bluetoothAdapter.isEnabled())b=true;
        return b;
    }

    public BluetoothAdapter getBluetoothAdapter(){
        return bluetoothAdapter;
    }


    public BluetoothSocket getBluetoothSocket() {
        return btSocket;
    }
}
