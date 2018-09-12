package com.qu.jian.btremote.Net;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jianqv on 2017/8/25.
 */

public class BTIOStream{
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;//输出流
    private InputStream inputStream;
    private Context context;

    public BTIOStream(BluetoothSocket bluetoothSocket, Context c){
        this.bluetoothSocket=bluetoothSocket;
        context=c;
    }


    public void sendData(String data) {//在onClick()和进度条改变事件中被调用,把要输出的东西通过输出流输出到目的地
        if(bluetoothSocket==null) Toast.makeText(context,"bluetoothSocket为空",Toast.LENGTH_LONG).show();
        else {
        try {
            outputStream = bluetoothSocket.getOutputStream();//通过连接的接口获得输出流对象
        } catch (IOException e) {
            Log.d("tag","fail to get the outStream");
            Toast.makeText(context,"获取输出流失败",Toast.LENGTH_SHORT).show();
        }
        byte[] msgBuffer = data.getBytes();//byte类型,表示-128到127的整数
        startTread(msgBuffer);
        }
    }

    private void startTread(final byte[] m){
        final Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(i<m.length){
                    try {
                        outputStream.write(m);//将 msgBuffer.length 个字节从指定的 byte 数组写入此输出流
                        outputStream.flush();
                    } catch (IOException e) {
                        Log.d("tag","fail to send the data");
                        Toast.makeText(context,"输出流写入数据失败",Toast.LENGTH_SHORT).show();
                    }
                    i++;
                }
            }
        });
        thread.start();
    }

    void sendthread(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        thread.start();
    }

    public void closeSocket(){
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
