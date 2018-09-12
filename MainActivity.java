package com.qu.jian.btremote;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qu.jian.btremote.Aty.Fragment_Interact;
import com.qu.jian.btremote.Aty.Fragment_gravity;
import com.qu.jian.btremote.Aty.Fragment_reset;
import com.qu.jian.btremote.Aty.Fragment_sensor;
import com.qu.jian.btremote.Aty.Fragment_sitting;
import com.qu.jian.btremote.Net.BTIOStream;
import com.qu.jian.btremote.Net.BluetoothConnection;
import com.qu.jian.btremote.Net.WDIOStream;
import com.qu.jian.btremote.Net.WifiDirectAdmin;

public class MainActivity extends AppCompatActivity{

    private LinearLayout channel_autoNavigation,channel_manual,channel_gravity,channel_reset,channel_sitting,leftFunction;
    private ImageView imageView_channel_autoNavigation,imageView_channel_manual,imageView_channel_gravity,imageView_channel_reset,imageView_channel_sitting;
    private TextView txv_channel_Na,txv_channel_Ma,txv_channel_Gr,txv_channel_Re,txv_channel_Sitting;
    private Button button_grab,button_auto,button_holderHandServo,button_holderTopServo,button_holderMiddleServo,button_holderButtonServo,button_pantiltTopServo,button_pantiltButtonServo;
    private Button button_turnForward,button_turnBack,button_turnLeft,button_turnRight,button_stop,button_sub,button_add;
    private TextView textView_speed,textView_angle,textView_mainAty_tip;
    private SeekBar seekBar;
    private FragmentManager fragmentManager;
    private Fragment_reset fragmentReset;
    private Fragment_gravity fragmentGravity;
    private Fragment_Interact fragmentInteract;
    private Fragment_sitting fragmentSitting;
    private Fragment_sensor fragmentSensor;
    private BluetoothConnection bluetoothConnection;
    private BTIOStream btioStream;
    private WifiDirectAdmin wifiDirectAdmin;
    private WDIOStream wdioStream;
    private boolean flag_auto=true;
    private Vibrator vibrator;

    private String str_auto=null,str_grab=null,str_hand=null,str_armts=null,str_armms=null,str_armbs=null,str_ptts=null,str_ptbs=null,str_f=null,str_b=null,str_s=null,str_l=null,str_r=null,str_manual=null,str_gravity=null,str_interate=null,str_reset=null;
    private float sb_Max=0,sb_Min=0;
    private String tpWay=Config.TPWAY_BT_AND_WD;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Config.forStartAPP(this);//第一次启动时加载的配置
        createViews();//获取组件实例
        setConfig();//加载配置
        registerMyReceiver();//注册广播接收器
        fragmentManager=getSupportFragmentManager();
        bluetoothConnection=new BluetoothConnection(this);
        wifiDirectAdmin=new WifiDirectAdmin(this);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //组件点击事件
        setFunctionButtonClick();
        setChannelButtonClick();
        setSpeedClick();
        loadChannelFragment(Config.CHANNEL_AUTO);
        loadChannelFragment(Config.CHANNEL_MANUAL);
        loadChannelFragment(Config.CHANNEL_GRAVITY);
        loadChannelFragment(Config.CHANNEL_SITTING);
        loadChannelFragment(Config.CHANNEL_RESET);
    }


    private void setConfig() {
        sb_Min=Config.getCacheFloatConfig(this,Config.KEY_SITTING_SB_MIN);
        sb_Max=Config.getCacheFloatConfig(this,Config.KEY_SITTING_SB_MAX);
        str_auto=Config.getCacheStringConfig(this,Config.KEY_SITTING_AUTO);
        str_grab=Config.getCacheStringConfig(this,Config.KEY_SITTING_GRAB);
        str_hand=Config.getCacheStringConfig(this,Config.KEY_SITTING_HAND);
        str_armts=Config.getCacheStringConfig(this,Config.KEY_SITTING_ARMTS);
        str_armms=Config.getCacheStringConfig(this,Config.KEY_SITTING_ARMMS);
        str_armbs=Config.getCacheStringConfig(this,Config.KEY_SITTING_ARMBS);
        str_ptts=Config.getCacheStringConfig(this,Config.KEY_SITTING_PTTS);
        str_ptbs=Config.getCacheStringConfig(this,Config.KEY_SITTING_PTBS);
        str_f=Config.getCacheStringConfig(this,Config.KEY_SITTING_F);
        str_b=Config.getCacheStringConfig(this,Config.KEY_SITTING_B);
        str_s=Config.getCacheStringConfig(this,Config.KEY_SITTING_S);
        str_l=Config.getCacheStringConfig(this,Config.KEY_SITTING_L);
        str_r=Config.getCacheStringConfig(this,Config.KEY_SITTING_R);
        str_manual=Config.getCacheStringConfig(this,Config.KEY_SITTING_MANUAL);
        str_gravity=Config.getCacheStringConfig(this,Config.KEY_SITTING_GRAVITY);
        str_interate=Config.getCacheStringConfig(this,Config.KEY_SITTING_INTERATE);
        str_reset=Config.getCacheStringConfig(this,Config.KEY_SITTING_RESET);
        tpWay=Config.getCacheStringConfig(this,Config.KEY_SITTING_TPWAY);
    }

    private void createViews() {
        channel_autoNavigation=(LinearLayout)findViewById(R.id.channel_Na);
        channel_manual=(LinearLayout)findViewById(R.id.channel_Ma);
        channel_gravity=(LinearLayout)findViewById(R.id.channel_Gr);
        channel_reset=(LinearLayout)findViewById(R.id.channel_Re);
        channel_sitting=(LinearLayout)findViewById(R.id.channel_Sitting);
        imageView_channel_autoNavigation=(ImageView)findViewById(R.id.imv_channel_autoNavigation);
        imageView_channel_manual=(ImageView)findViewById(R.id.imv_channel_manualMode);
        imageView_channel_gravity=(ImageView)findViewById(R.id.imv_channel_gravityMode);
        imageView_channel_reset=(ImageView)findViewById(R.id.imv_channel_resetMode);
        imageView_channel_sitting=(ImageView)findViewById(R.id.imv_channel_Sitting);
        txv_channel_Na=(TextView)findViewById(R.id.textView_Na);
        txv_channel_Ma=(TextView)findViewById(R.id.textView_Ma);
        txv_channel_Gr=(TextView)findViewById(R.id.textView_Gr);
        txv_channel_Re=(TextView)findViewById(R.id.textView_Re);
        txv_channel_Sitting=(TextView)findViewById(R.id.textView_Sitting);
        button_grab=(Button)findViewById(R.id.button_grab);
        button_auto=(Button)findViewById(R.id.button_Auto);
        button_holderHandServo=(Button)findViewById(R.id.button_holderHandServo);
        button_holderTopServo=(Button)findViewById(R.id.button_holderTopServo);
        button_holderMiddleServo=(Button)findViewById(R.id.button_holderMiddleServo);
        button_holderButtonServo=(Button)findViewById(R.id.button_holderButtonServo);
        button_pantiltTopServo=(Button)findViewById(R.id.button_pantlitTopServo);
        button_pantiltButtonServo=(Button)findViewById(R.id.button_pantiltButtonServo);
        button_turnForward=(Button)findViewById(R.id.button_turnForward);
        button_turnBack=(Button)findViewById(R.id.button_turnBack);
        button_turnLeft=(Button)findViewById(R.id.button_turnLeft);
        button_turnRight=(Button)findViewById(R.id.button_turnRight);
        button_stop=(Button)findViewById(R.id.button_stop);
        button_sub=(Button)findViewById(R.id.button_sub);
        button_add=(Button)findViewById(R.id.button_add);
        textView_angle=(TextView)findViewById(R.id.textView_angle);
        textView_speed=(TextView)findViewById(R.id.textView_speed);
        textView_mainAty_tip=(TextView)findViewById(R.id.textView_mainAty_tip);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        leftFunction=(LinearLayout)findViewById(R.id.layout_LeftFunction);
    }

    //自动导航模式下隐藏部分按键
    private void hideViews(boolean b){
        if(b){
            leftFunction.setVisibility(View.INVISIBLE);
            button_turnForward.setVisibility(View.INVISIBLE);
            button_turnBack.setVisibility(View.INVISIBLE);
            button_turnLeft.setVisibility(View.INVISIBLE);
            button_turnRight.setVisibility(View.INVISIBLE);
        }else {
            leftFunction.setVisibility(View.VISIBLE);
            button_turnForward.setVisibility(View.VISIBLE);
            button_turnBack.setVisibility(View.VISIBLE);
            button_turnLeft.setVisibility(View.VISIBLE);
            button_turnRight.setVisibility(View.VISIBLE);
        }
    }


    //载入Fragment
    private void loadChannelFragment(int i) {
        if(btioStream!=null)sendData(str_s);
        FragmentTransaction transanction=fragmentManager.beginTransaction();//-用碎片管理器创建碎片事务,用碎片事务来执行碎片的添加,移除,替换,显示,隐藏等操作
        if(fragmentGravity!=null) transanction.hide(fragmentGravity);//隐藏起来
        if(fragmentReset!=null)transanction.hide(fragmentReset);
        if(fragmentInteract!=null)transanction.hide(fragmentInteract);
        if(fragmentSitting!=null)transanction.hide(fragmentSitting);
        if(fragmentSensor!=null)transanction.hide(fragmentSensor);
        switch (i){
            case Config.CHANNEL_AUTO:
                selectingChannel(Config.CHANNEL_AUTO);
                button_auto.setVisibility(View.INVISIBLE);
                if(fragmentInteract==null){
                    fragmentInteract=new Fragment_Interact();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(BluetoothConnection.BLUETOOTHCONNECTION_KEY,bluetoothConnection);//把对象传递过去
                    fragmentInteract.setArguments(bundle);
                    transanction.add(R.id.container,fragmentInteract);
                    transanction.addToBackStack(null);
                }
                else transanction.show(fragmentInteract);
                sendData(str_interate);
                break;
            case Config.CHANNEL_MANUAL:
                selectingChannel(Config.CHANNEL_MANUAL);
                button_auto.setVisibility(View.VISIBLE);
                sendData(str_manual);
                break;
            case Config.CHANNEL_GRAVITY:
                selectingChannel(Config.CHANNEL_GRAVITY);
                button_auto.setVisibility(View.INVISIBLE);
                if(fragmentGravity==null) {
                    fragmentGravity=new Fragment_gravity();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(BluetoothConnection.BLUETOOTHCONNECTION_KEY,bluetoothConnection);//把对象传递过去
                    fragmentGravity.setArguments(bundle);
                    transanction.add(R.id.container, fragmentGravity);
                    transanction.addToBackStack(null);//添加到后退栈里面，按返回键不退出应用
                }
                else transanction.show(fragmentGravity);
                sendData(str_gravity);
                break;
            case Config.CHANNEL_RESET:
                selectingChannel(Config.CHANNEL_RESET);
                button_auto.setVisibility(View.INVISIBLE);
                if(fragmentReset==null) {
                    fragmentReset=new Fragment_reset();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(BluetoothConnection.BLUETOOTHCONNECTION_KEY,bluetoothConnection);//把对象传递过去
                    fragmentReset.setArguments(bundle);
                    transanction.add(R.id.container, fragmentReset);
                    transanction.addToBackStack(null);//添加到后退栈里面，按返回键不退出应用
                }
                else transanction.show(fragmentReset);
                sendData(str_reset);
                break;
            case Config.CHANNEL_SITTING:
                selectingChannel(Config.CHANNEL_SITTING);
                button_auto.setVisibility(View.INVISIBLE);
                if(fragmentSitting==null){
                    fragmentSitting=new Fragment_sitting();
                    transanction.addToBackStack(null);
                    transanction.add(R.id.container,fragmentSitting);
                }else transanction.show(fragmentSitting);
                break;
        }
        transanction.commit();
    }

    //选择下方频道的时候，显示的效果
    private void selectingChannel(int i) {
        imageView_channel_autoNavigation.setImageResource(R.drawable.auto);
        imageView_channel_manual.setImageResource(R.drawable.manual);
        imageView_channel_gravity.setImageResource(R.drawable.gravity);
        imageView_channel_reset.setImageResource(R.drawable.reset);
        imageView_channel_sitting.setImageResource(R.drawable.sitting);
        txv_channel_Na.setTextColor(Color.GRAY);
        txv_channel_Ma.setTextColor(Color.GRAY);
        txv_channel_Gr.setTextColor(Color.GRAY);
        txv_channel_Re.setTextColor(Color.GRAY);
        txv_channel_Sitting.setTextColor(Color.GRAY);
        switch (i){
            case Config.CHANNEL_AUTO:
                imageView_channel_autoNavigation.setImageResource(R.drawable.auto_1);
                txv_channel_Na.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
            case Config.CHANNEL_MANUAL:
                imageView_channel_manual.setImageResource(R.drawable.manual_1);
                txv_channel_Ma.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
            case Config.CHANNEL_GRAVITY:
                imageView_channel_gravity.setImageResource(R.drawable.gravity_1);
                txv_channel_Gr.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
            case Config.CHANNEL_RESET:
                imageView_channel_reset.setImageResource(R.drawable.reset_1);
                txv_channel_Re.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
            case Config.CHANNEL_SITTING:
                imageView_channel_sitting.setImageResource(R.drawable.sitting_1);
                txv_channel_Sitting.setTextColor(getResources().getColor(R.color.channel_bg));
            default:
                break;
        }
    }

    //当功能按键的时候，按键显示的效果
    private void selectingFunction(int i){
        button_grab.setBackgroundColor(Color.WHITE);
        button_grab.setTextColor(getResources().getColor(R.color.skyblue));
        button_auto.setBackgroundColor(Color.WHITE);
        button_auto.setTextColor(getResources().getColor(R.color.skyblue));
        button_holderHandServo.setBackgroundColor(Color.WHITE);
        button_holderHandServo.setTextColor(getResources().getColor(R.color.skyblue));
        button_holderTopServo.setBackgroundColor(Color.WHITE);
        button_holderTopServo.setTextColor(getResources().getColor(R.color.skyblue));
        button_holderMiddleServo.setBackgroundColor(Color.WHITE);
        button_holderMiddleServo.setTextColor(getResources().getColor(R.color.skyblue));
        button_holderButtonServo.setBackgroundColor(Color.WHITE);
        button_holderButtonServo.setTextColor(getResources().getColor(R.color.skyblue));
        button_pantiltTopServo.setBackgroundColor(Color.WHITE);
        button_pantiltTopServo.setTextColor(getResources().getColor(R.color.skyblue));
        button_pantiltButtonServo.setBackgroundColor(Color.WHITE);
        button_pantiltButtonServo.setTextColor(getResources().getColor(R.color.skyblue));
        button_turnForward.setBackgroundColor(Color.GRAY);
        button_stop.setBackgroundColor(Color.GRAY);
        button_turnBack.setBackgroundColor(Color.GRAY);
        button_turnLeft.setBackgroundColor(Color.GRAY);
        button_turnRight.setBackgroundColor(Color.GRAY);
        button_add.setBackgroundColor(Color.GRAY);
        button_sub.setBackgroundColor(Color.GRAY);
        switch (i){
            case Config.FUNCTION_GRAB:
                button_grab.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_grab.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_HANDS:
                button_holderHandServo.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_holderHandServo.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_ARMTS:
                button_holderTopServo.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_holderTopServo.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_ARMMS:
                button_holderMiddleServo.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_holderMiddleServo.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_ARMBS:
                button_holderButtonServo.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_holderButtonServo.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_PTTS:
                button_pantiltTopServo.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_pantiltTopServo.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_PTBS:
                button_pantiltButtonServo.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_pantiltButtonServo.setTextColor(Color.WHITE);
                break;
            case Config.FUNCTION_F:
                button_turnForward.setBackgroundColor(getResources().getColor(R.color.skyblue));
                break;
            case Config.FUNCTION_B:
                button_turnBack.setBackgroundColor(getResources().getColor(R.color.skyblue));
                break;
            case Config.FUNCTION_S:
                button_stop.setBackgroundColor(getResources().getColor(R.color.skyblue));
                break;
            case Config.FUNCTION_L:
                button_turnLeft.setBackgroundColor(getResources().getColor(R.color.skyblue));
                break;
            case Config.FUNCTION_R:
                button_turnRight.setBackgroundColor(getResources().getColor(R.color.skyblue));
                break;
            case Config.FUNCTION_AUTO:
                button_auto.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_auto.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    //功能组件点击事件
    private void setFunctionButtonClick(){
        button_grab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_GRAB);
                sendData(str_grab);
            }
        });
        button_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_auto) {//为真时表示打开时
                    flag_auto=false;
                    selectingFunction(Config.FUNCTION_AUTO);
                    hideViews(true);
                    textView_mainAty_tip.setText(R.string.autoNavigation);
                }else {//进行关闭操作时
                    flag_auto = true;
                    hideViews(false);
                    selectingFunction(Config.FUNCTION_NULL);
                    textView_mainAty_tip.setText(R.string.manualMode);
                }
                sendData(str_auto);
            }
        });
        button_holderHandServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_HANDS);
                sendData(str_hand);
            }
        });
        button_holderTopServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_ARMTS);
                sendData(str_armts);
            }
        });
        button_holderMiddleServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_ARMMS);
                sendData(str_armms);
            }
        });
        button_holderButtonServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_ARMBS);
                sendData(str_armbs);
            }
        });
        button_pantiltTopServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_PTTS);
                sendData(str_ptts);
            }
        });
        button_pantiltButtonServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_PTBS);
                sendData(str_ptbs);
            }
        });
        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_S);
                sendData(str_s);
            }
        });
        button_turnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_F);
                sendData(str_f);
            }
        });
        button_turnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_B);
                sendData(str_b);
            }
        });
        button_turnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_L);
                sendData(str_l);
            }
        });
        button_turnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingFunction(Config.FUNCTION_R);
                sendData(str_r);
            }
        });
    }

    //频道按钮点击事件
    private void setChannelButtonClick(){
        channel_autoNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingChannel(Config.CHANNEL_AUTO);
                loadChannelFragment(Config.CHANNEL_AUTO);
            }
        });
        channel_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingChannel(Config.CHANNEL_MANUAL);
                loadChannelFragment(Config.CHANNEL_MANUAL);
                hideViews(false);
            }
        });
        channel_gravity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingChannel(Config.CHANNEL_GRAVITY);
                loadChannelFragment(Config.CHANNEL_GRAVITY);
            }
        });
        channel_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingChannel(Config.CHANNEL_RESET);
                loadChannelFragment(Config.CHANNEL_RESET);
            }
        });
        channel_sitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingChannel(Config.CHANNEL_SITTING);
                loadChannelFragment(Config.CHANNEL_SITTING);
            }
        });
    }


    int progress=0;
    private void setSpeedClick(){
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress=seekBar.getProgress();
                progress+=2;
                double data=progress/100.0;
                data=data*(Math.abs(sb_Max)-Math.abs(sb_Min))+sb_Min;
                seekBar.setProgress(progress);
                textView_mainAty_tip.setText("发送值："+String.format("%.0f",data));
                sendData(String.format("%.0f",data));
            }
        });
        button_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        button_add.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                        longTouchSendData(Config.SEND_MODE_ADD,true);//长按发送数据
                        break;
                    case MotionEvent.ACTION_UP:
                        button_add.setBackgroundColor(Color.GRAY);
                        longTouchSendData(Config.SEND_MODE_ADD,false);
                        break;
                }
                return false;
            }
        });
        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress=seekBar.getProgress();
                progress-=2;
                seekBar.setProgress(progress);
                double data=progress/100.0;
                data=data*(Math.abs(sb_Max)-Math.abs(sb_Min))+sb_Min;
                textView_mainAty_tip.setText("发送值："+String.format("%.0f",data));
                sendData(String.format("%.0f",data));
            }
        });
        button_sub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        button_sub.setBackgroundColor(getResources().getColor(R.color.channel_bg));
                        longTouchSendData(Config.SEND_MODE_SUB,true);
                        break;
                    case MotionEvent.ACTION_UP:
                        button_sub.setBackgroundColor(Color.GRAY);
                        longTouchSendData(Config.SEND_MODE_SUB,false);
                        break;
                }
                return false;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float data=progress/100;
                data=data*(Math.abs(sb_Max)+Math.abs(sb_Min))+sb_Min;
                textView_mainAty_tip.setText("发送值："+String.format("%.0f",data));
                sendData(String.format("%.0f",data));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void sendData(String str){
        if(tpWay.equals(Config.TPWAY_BT_AND_WD)){
            if(btioStream!=null){
                btioStream.sendData(str);
                textView_mainAty_tip.setText("通过蓝牙发送："+str);
            }else textView_mainAty_tip.setText("未创建输出流");
        }else if(tpWay.equals(Config.TPWAY_ONLY_WD)){
            if(wdioStream!=null){
                wdioStream.sendData(str);
                textView_mainAty_tip.setText("通过WIFI发送："+str);
            }else textView_mainAty_tip.setText("未创建输出流");
        }
    }

    private void longTouchSendData(char mode,boolean b){
        if(mode==Config.SEND_MODE_ADD){
            seekBar.setProgress(progress);
        }else if(mode==Config.SEND_MODE_SUB){
            seekBar.setProgress(progress);
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(localBroadcastManager!=null&&broadcastReceiver!=null)localBroadcastManager.unregisterReceiver(broadcastReceiver);
        if(btioStream!=null)btioStream.closeSocket();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    //双击返回键退出应用
    long lastClickTime=0;
    @Override
    public void onBackPressed(){
        if(lastClickTime<=0){
            Toast.makeText(getApplicationContext(),R.string.click_again_tofinish,Toast.LENGTH_SHORT).show();
            lastClickTime= System.currentTimeMillis();
        }else{
            long currentTime=System.currentTimeMillis();
            if(currentTime-lastClickTime<1000)finish();
            else{
                Toast.makeText(getApplicationContext(),R.string.click_again_tofinish,Toast.LENGTH_SHORT).show();
                lastClickTime=currentTime;
            }
        }
    }

    //设置广播过滤器
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private void registerMyReceiver() {
        localBroadcastManager=LocalBroadcastManager.getInstance(getApplicationContext());
        intentFilter=new IntentFilter();
        intentFilter.addAction(Config.BD_SITTING_MANUAL_AUTO);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_GRAB);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_HAND);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_ARMTS);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_ARMMS);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_ARMBS);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_PTTS);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_PTBS);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_F);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_B);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_S);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_L);
        intentFilter.addAction(Config.BD_SITTING_MANUAL_R);
        intentFilter.addAction(Config.BD_SITTING_MANUAL);
        intentFilter.addAction(Config.BD_SITTING_GRAVITY);
        intentFilter.addAction(Config.BD_SITTING_INTERATE);
        intentFilter.addAction(Config.BD_SITTING_RESET);
        intentFilter.addAction(Config.BD_SITTING_SB_MAX);
        intentFilter.addAction(Config.BD_SITTING_SB_MIN);
        intentFilter.addAction(Config.BD_TPWAY);
        intentFilter.addAction(Config.BD_BTOK);
        intentFilter.addAction(Config.BD_TEST);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
    }

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case Config.BD_SITTING_MANUAL_AUTO:
                    str_auto=intent.getStringExtra(Config.KEY_SITTING_AUTO);
                    break;
                case Config.BD_SITTING_MANUAL_GRAB:
                    str_grab=intent.getStringExtra(Config.KEY_SITTING_GRAB);
                    break;
                case Config.BD_SITTING_MANUAL_HAND:
                    str_hand=intent.getStringExtra(Config.KEY_SITTING_HAND);
                    break;
                case Config.BD_SITTING_MANUAL_ARMTS:
                    str_armts=intent.getStringExtra(Config.KEY_SITTING_ARMTS);
                    break;
                case Config.BD_SITTING_MANUAL_ARMMS:
                    str_armms=intent.getStringExtra(Config.KEY_SITTING_ARMMS);
                    break;
                case Config.BD_SITTING_MANUAL_ARMBS:
                    str_armbs=intent.getStringExtra(Config.KEY_SITTING_ARMBS);
                    break;
                case Config.BD_SITTING_MANUAL_PTTS:
                    str_ptts=intent.getStringExtra(Config.KEY_SITTING_PTTS);
                    break;
                case Config.BD_SITTING_MANUAL_PTBS:
                    str_ptbs=intent.getStringExtra(Config.KEY_SITTING_PTBS);
                    break;
                case Config.BD_SITTING_MANUAL_F:
                    str_f=intent.getStringExtra(Config.KEY_SITTING_F);
                    break;
                case Config.BD_SITTING_MANUAL_B:
                    str_b=intent.getStringExtra(Config.KEY_SITTING_B);
                    break;
                case Config.BD_SITTING_MANUAL_S:
                    str_s=intent.getStringExtra(Config.KEY_SITTING_S);
                    break;
                case Config.BD_SITTING_MANUAL_L:
                    str_l=intent.getStringExtra(Config.KEY_SITTING_L);
                    break;
                case Config.BD_SITTING_MANUAL_R:
                    str_r=intent.getStringExtra(Config.KEY_SITTING_R);
                    break;
                case Config.BD_SITTING_MANUAL:
                    str_manual=intent.getStringExtra(Config.KEY_SITTING_MANUAL);
                    break;
                case Config.BD_SITTING_GRAVITY:
                    str_gravity=intent.getStringExtra(Config.KEY_SITTING_GRAVITY);
                    break;
                case Config.BD_SITTING_INTERATE:
                    str_interate=intent.getStringExtra(Config.KEY_SITTING_INTERATE);
                    break;
                case Config.BD_SITTING_RESET:
                    str_reset=intent.getStringExtra(Config.KEY_SITTING_RESET);
                    break;
                case Config.BD_SITTING_SB_MAX:
                    sb_Max=Float.parseFloat(intent.getStringExtra(Config.KEY_SITTING_SB_MAX));
                    break;
                case Config.BD_SITTING_SB_MIN:
                    sb_Min=Float.parseFloat(intent.getStringExtra(Config.KEY_SITTING_SB_MIN));
                    break;
                case Config.BD_TPWAY:
                    tpWay=intent.getStringExtra(Config.KEY_SITTING_TPWAY);
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    //Toast.makeText(getApplicationContext(),"已收到ACL_CONNECTED",Toast.LENGTH_SHORT).show();
                    //btioStream=new BTIOStream(bluetoothConnection.getBluetoothSocket(),getApplicationContext());
                    break;
                case Config.BD_BTOK:
                    btioStream=new BTIOStream(bluetoothConnection.getBluetoothSocket(),getApplicationContext());
                    textView_mainAty_tip.setText("已创建输出流");
                    break;
                case Config.BD_TEST:

                    break;
            }
        }
    };


}
