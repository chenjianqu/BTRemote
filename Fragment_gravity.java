package com.qu.jian.btremote.Aty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qu.jian.btremote.Config;
import com.qu.jian.btremote.Net.BTIOStream;
import com.qu.jian.btremote.Net.BluetoothConnection;
import com.qu.jian.btremote.R;

public class Fragment_gravity extends Fragment{
    private SensorManager sensorManager;
    private Sensor sensorGravity;
    private FrameLayout frameLayout;
    private ImageView imageView;
    private TextView textView_tip,textView_sensorValue;
    private BluetoothConnection bluetoothConnection;
    private Button button_sub,button_add,button_stop;
    private boolean flag_on=false;
    private String tpWay=Config.TPWAY_BT_AND_WD;
    private BTIOStream btioStream;

    private static int flag_Mode=-1;//选择使用哪个重力轴
    private static float gv_max;
    private static float gv_min;
    private static String g_ch_flag=null;
    private static String s_ch_flag=null;
    private String key_add=null;
    private String key_sub=null;
    String data=null;

    public Fragment_gravity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {//先执行onCreate(),再执行onCreateView(),然后依次是onResume,onPause,onStop,onDestroyView,onDestroy,onDetch
        super.onCreate(savedInstanceState);
        bluetoothConnection= (BluetoothConnection) getArguments().getSerializable(BluetoothConnection.BLUETOOTHCONNECTION_KEY);//获取从Activity传过来的对象
        sensorManager=(SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);//在Fragment下需要getContext
        sensorGravity=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        tpWay=Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_gravity,container,false);
        createViews(rootView);
        flag_Mode=Config.getCacheIntConfig(getActivity(),Config.KEY_SITTING_GRAVITY_MODE);
        gv_max=Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MAX);
        gv_min=Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MIN);
        g_ch_flag=Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GV_CF);
        s_ch_flag=Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GN_CF);
        key_add=Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_ADD);
        key_sub=Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_SUB);

        if(bluetoothConnection.isEnable()){

            button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data=key_add;
                    if(s_ch_flag==null)sendData(data);
                    else sendData(s_ch_flag+data);
                }
            });
            button_add.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action=event.getAction();
                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            button_add.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                            break;
                        case MotionEvent.ACTION_UP:
                            button_add.setBackgroundColor(getResources().getColor(R.color.skyblue));
                            break;
                    }
                    return false;
                }
            });
            button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data=key_sub;
                if(s_ch_flag==null)sendData(data);
                else sendData(s_ch_flag+data);
            }
        });
        button_sub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        button_sub.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                        break;
                    case MotionEvent.ACTION_UP:
                        button_sub.setBackgroundColor(getResources().getColor(R.color.skyblue));
                        break;
                    }
                    return false;
                }
            });
            button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag_on){
                        flag_on=false;
                        imageView.setVisibility(View.INVISIBLE);
                    }else {//动作为打开
                        textView_tip.setText(R.string.gravityMode);
                        imageView.setVisibility(View.VISIBLE);
                        flag_on=true;
                    }
                }
            });
            button_stop.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action=event.getAction();
                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            button_stop.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                            break;
                        case MotionEvent.ACTION_UP:
                            button_stop.setBackgroundColor(getResources().getColor(R.color.skyblue));
                            break;
                    }
                    return false;
                }
            });
        }
        else textView_tip.setText("请打开蓝牙");


        return rootView;
    }


    private double each=0;
    private double each1=0;
    private double each2=0;
    private float s=0;
    private float s1=0;
    private float s2=0;
    private float f=0;
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(flag_on){
                FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)imageView.getLayoutParams();
                switch (flag_Mode){
                    case Config.AXIS_X:
                        s=event.values[0];
                        if(each==0)each=(frameLayout.getWidth()-imageView.getWidth())/100.0;
                        params.leftMargin=(int)(each*50-s*50.0/10.0*each);
                        params.bottomMargin=2;
                        textView_sensorValue.setText(String.format("%.2f",event.values[0]));
                        f=event.values[0];
                        break;
                    case Config.AXIS_Y:
                        s=event.values[1];
                        if(each==0)each=(frameLayout.getHeight()-imageView.getHeight())/100.0;
                        params.bottomMargin=(int)(each*50-s*50.0/10.0*each);
                        params.leftMargin=2;
                        textView_sensorValue.setText(String.format("%.2f",event.values[1]));
                        f=event.values[1];
                        break;
                    case Config.AXIS_Z:
                        s1=event.values[0];
                        s2=event.values[1];
                        if(each2==0)each2=(frameLayout.getHeight()-imageView.getHeight())/100.0;
                        if(each1==0)each1=(frameLayout.getWidth()-imageView.getWidth())/100.0;
                        params.bottomMargin=(int)(each2*50-s2*50.0/10.0*each2);
                        params.leftMargin=(int)(each1*50-s1*50.0/10.0*each1);
                        textView_sensorValue.setText(String.format("%.2f",event.values[2]));
                        f=event.values[2];
                        break;
                }
                float data1,data2,data3;
                data1=(f-Config.G_MIN)/(Math.abs(Config.G_MIN)+Config.G_MAX);
                data2=data1*(Math.abs(gv_max)-Math.abs(gv_min));
                data3=data2+gv_min;
                if(g_ch_flag==null)sendData(String.valueOf((int)data3));
                else sendData(g_ch_flag+String.valueOf((int)data3));
                imageView.setLayoutParams(params);
            }else {
                textView_sensorValue.setText("");
            }
        }
    };



    private void sendData(String s){
        if(btioStream!=null){
            btioStream.sendData(s);
            textView_tip.setText("发送数据:"+s);
        }else textView_tip.setText("未创建输出流");
    }


    private void createViews(View rootView){
        frameLayout= (FrameLayout) rootView.findViewById(R.id.frameLayout);
        imageView=(ImageView)rootView.findViewById(R.id.imageView_cursor);
        button_add= (Button) rootView.findViewById(R.id.button_gravity_accerelate);
        button_sub= (Button) rootView.findViewById(R.id.button_gravity_decelerate);
        button_stop=(Button)rootView.findViewById(R.id.button_gravity_stop);
        textView_tip=(TextView)rootView.findViewById(R.id.textView_gravity_tip);
        textView_sensorValue=(TextView)rootView.findViewById(R.id.textView_sensorValue);
    }


    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(listener);//取消监听对象的注册
    }
    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(listener,sensorGravity,SensorManager.SENSOR_DELAY_NORMAL);
    }

    LocalBroadcastManager localBroadcastManager;
    @Override
    public void onActivityCreated(Bundle savedInstanceState){//注册本地广播接收器
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        super.onActivityCreated(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Config.BD_SITTING_GRAVITY_MO);
        intentFilter.addAction(Config.BD_SITTING_GV_MAX);
        intentFilter.addAction(Config.BD_SITTING_GV_MIN);
        intentFilter.addAction(Config.BD_SITTING_GRA_ADD);
        intentFilter.addAction(Config.BD_SITTING_GRA_SUB);
        intentFilter.addAction(Config.BD_SITTING_GV_CF);
        intentFilter.addAction(Config.BD_BTOK);
        intentFilter.addAction(Config.BD_TEST);
        localBroadcastManager.registerReceiver(mbroadcastReceiver,intentFilter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(localBroadcastManager!=null&&mbroadcastReceiver!=null)localBroadcastManager.unregisterReceiver(mbroadcastReceiver);//注销本地广播
    }


    //用于接收设置广播
    private BroadcastReceiver mbroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case Config.BD_SITTING_GRAVITY_MO:
                    flag_Mode=Integer.parseInt(intent.getStringExtra(Config.KEY_SITTING_GRAVITY_MODE));
                    break;
                case Config.BD_SITTING_GV_MAX:
                    gv_max=Float.parseFloat(intent.getStringExtra(Config.KEY_SITTING_GV_MAX));
                    break;
                case Config.BD_SITTING_GV_MIN:
                    gv_min=Float.parseFloat(intent.getStringExtra(Config.KEY_SITTING_GV_MIN));
                    break;
                case Config.BD_SITTING_GRA_ADD:
                    key_add=intent.getStringExtra(Config.KEY_SITTING_GRA_ADD);
                    break;
                case Config.BD_SITTING_GRA_SUB:
                    key_sub=intent.getStringExtra(Config.KEY_SITTING_GRA_SUB);
                    break;
                case Config.BD_SITTING_GV_CF:
                    g_ch_flag=intent.getStringExtra(Config.KEY_SITTING_GV_CF);
                    break;
                case Config.BD_TPWAY:
                    tpWay=intent.getStringExtra(Config.KEY_SITTING_TPWAY);
                    break;
                case Config.BD_BTOK:
                    btioStream=new BTIOStream(bluetoothConnection.getBluetoothSocket(),getActivity());
                    textView_tip.setText("已创建输出流");
                    break;
                case Config.BD_TEST:
                    break;
            }
        }
    };
}
