package com.qu.jian.btremote.Aty;


import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qu.jian.btremote.Config;
import com.qu.jian.btremote.Net.BTIOStream;
import com.qu.jian.btremote.Net.BluetoothConnection;
import com.qu.jian.btremote.R;

import java.io.IOException;
import java.io.InputStream;

public class Fragment_Interact extends Fragment{


    private BluetoothConnection bluetoothConnection;
    private BTIOStream btioStream;
    private Vibrator vibrator;
    private Button button_sendText,button_sendMedia;
    private TextView textView_interact,textView_tip;
    private static String text="";
    private String str_et=null;
    private EditText editText_sendText;
    private String tpWay=null;
    private LocalBroadcastManager localBroadcastManager;
    MyHandler myHandler;


    public Fragment_Interact() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothConnection= (BluetoothConnection) getArguments().getSerializable(BluetoothConnection.BLUETOOTHCONNECTION_KEY);//获取从Activity传过来的对象
        vibrator=(Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        myHandler=new MyHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_interact, container, false);
        createViews(view);
        pressDownUp();
        tpWay= Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY);
        button_sendText.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if(editText_sendText.getText().toString().equals("")){
                        textView_tip.setText(R.string.content_cannot_be_empty);
                    }else {
                        str_et = editText_sendText.getText().toString();
                        if(btioStream!=null){
                            btioStream.sendData(str_et);
                            Calendar calendar = Calendar.getInstance();
                            String time = calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND);
                            text = text + "\n" + time + "-OUT:  " + str_et;
                            textView_interact.setText(text);
                            textView_tip.setText(R.string.long_touch_to_clear_content);
                            editText_sendText.setText("");
                        }else Toast.makeText(getActivity(),"未创建输出流",Toast.LENGTH_SHORT).show();
                    }
                }
        });
        button_sendMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {textView_tip.setText(R.string.featurn_isnot_availiable);}
            });
        textView_tip.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    text = "";
                    textView_interact.setText(text);
                    textView_tip.setText(R.string.screen_have_clear);
                    vibrator.vibrate(300);
                    return false;
                }
        });
        return view;
    }

    private void createViews(View view){
        button_sendMedia= (Button) view.findViewById(R.id.button_sendMidia);
        button_sendText= (Button) view.findViewById(R.id.button_sendText);
        editText_sendText= (EditText) view.findViewById(R.id.editText_sendText);
        textView_interact=(TextView)view.findViewById(R.id.textView_interact);
        textView_interact.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_tip=(TextView)view.findViewById(R.id.textView_interact_tip);
    }

    private void pressDownUp(){
        button_sendMedia.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        button_sendMedia.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                        break;
                    case MotionEvent.ACTION_UP:
                        button_sendMedia.setBackgroundColor(getResources().getColor(R.color.skyblue));
                        break;
                }
                return false;
            }
        });
        button_sendText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        button_sendText.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                        break;
                    case MotionEvent.ACTION_UP:
                        button_sendText.setBackgroundColor(getResources().getColor(R.color.skyblue));
                        break;
                }
                return false;
            }
        });
        button_sendText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editText_sendText.setText("");
                vibrator.vibrate(300);
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        localBroadcastManager= LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Config.BD_BTOK);
        intentFilter.addAction(Config.BD_TEST);
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }


    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case Config.BD_BTOK:
                    btioStream=new BTIOStream(bluetoothConnection.getBluetoothSocket(),getActivity());
                    getData(bluetoothConnection.getBluetoothSocket());
                    textView_tip.setText("已创建输出流");
                    break;
                case Config.BD_TEST:

                    break;
            }
        }
    };

    class MyHandler extends Handler {
        public MyHandler(){
        }
        public MyHandler(Looper looper){
            super(looper);
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle=message.getData();
            String data=bundle.getString(Config.MESSAGE_INTERACT);
            Calendar calendar = Calendar.getInstance();
            String time = calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND);
            text+="\n"+time+"-IN:  "+data;
            textView_interact.setText(text);
        }
    }

    InputStream inputStream;
    public String getData(BluetoothSocket bluetoothSocket){
        final String[] s = new String[1];
        try {
            inputStream=bluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread workerThread=new Thread(new Runnable(){
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        int avnum=inputStream.available();
                        if(avnum>0){
                            StringBuffer sb=new StringBuffer();
                            byte[] bytes=new byte[avnum];
                            inputStream.read(bytes);
                            for(int i=0;i<avnum;i++){
                                sb.append((char)bytes[i]);
                            }
                            s[0] =sb.toString();
                            Message message=new Message();
                            Bundle bundle=new Bundle();
                            bundle.putString(Config.MESSAGE_INTERACT,s[0]);
                            message.setData(bundle);
                            Fragment_Interact.this.myHandler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        workerThread.start();
        return s[0];
    }
}
