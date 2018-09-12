package com.tw.flag.my_sensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                            implements SensorEventListener,
        LocationListener
{

    SensorManager sensorManager;//传感器管理器
    Sensor sensorAccelerometer;//加速传感器对象
    Sensor sensorLight;//光线传感器
    Sensor sensorProximity;//距离传感器
    Sensor sensorGyroscope;//陀螺仪
    Sensor sensorPressure;//气压传感器
    Sensor sensorGeomagneticfield;//地磁传感器
    Sensor sensorGrivity;
    Sensor sensorLinerAcceleration;
    Sensor sensorOrientation;
    TextView textView_ac,textView_li,textView_pro,textView_gy,textView_pre,textView_gmf,textView_gr,textView_lac,textView_or,textView_gps;
    ImageView imageView;
    Boolean bpro=true,bgy=true,bpre=true,bgmf=true,bac=true,bli=true,bgr=true,blac=true,bor=true;
    private float sa_x,sa_y,sa_z;
    private float sa_sum;
    private float sg_x,sg_y,sg_z;
    Vibrator vb;
    private double mx=0;
    RelativeLayout activityMain;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//设置手机不随屏幕旋转
        textView_ac=(TextView)findViewById(R.id.textView);
        textView_li=(TextView)findViewById(R.id.textView2);
        textView_pro=(TextView)findViewById(R.id.textView3);
        textView_gy=(TextView)findViewById(R.id.textView4);
        textView_pre=(TextView)findViewById(R.id.textView5);
        textView_gmf=(TextView)findViewById(R.id.textView6);
        textView_gr=(TextView)findViewById(R.id.textView7);
        textView_lac=(TextView)findViewById(R.id.textView9);
        textView_or=(TextView)findViewById(R.id.textView8);
        textView_gps= (TextView) findViewById(R.id.textView10);
        imageView=(ImageView)findViewById(R.id.imageView);
        activityMain=(RelativeLayout) findViewById(R.id.activity_main);
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        String best = locationManager.getBestProvider(new Criteria(), true);
        if (best != null) {
            textView_gps.setText("定位信息获取中...");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"无法获得定位权限",Toast.LENGTH_LONG).show();
                return;
            }
            locationManager.requestLocationUpdates(best,3000, (float) 3.0,this);
        } else {
            textView_gps.setText("---------------\n定位系统不可用\n或已被禁止使用定位系统\n---------------");
        }

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);//从系统服务获得传感器管理器
        sensorAccelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获取加速传感器
        sensorLight=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorProximity=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorGyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorPressure=sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorGrivity=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorOrientation=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorGeomagneticfield=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorLinerAcceleration=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(sensorGyroscope==null){
            bgy=false;
            textView_gy.setText("---------------\n陀螺仪不可用\n---------------");
        }
        if(sensorGrivity==null){
            bgr=false;
            textView_gr.setText("---------------\n重力传感器不可用\n---------------");
        }
        if(sensorPressure==null){
            bpre=false;
            textView_pre.setText("---------------\n压力传感器不可用\n---------------");
        }
        if(sensorAccelerometer==null){
            bac=false;
            textView_ac.setText("---------------\n加速度传感器不可用\n---------------");
        }
        if(sensorProximity==null){
            bpro=false;
            textView_pro.setText("---------------\n距离传感器不可用\n---------------");
        }
        if(sensorGeomagneticfield==null){
            bgmf=false;
            textView_gmf.setText("---------------\n地磁传感器不可用\n---------------");
        }
        if(sensorLight==null){
            bli=false;
            textView_li.setText("---------------\n光线传感器不可用\n---------------");
        }
        if(sensorLinerAcceleration==null){
            blac=false;
            textView_lac.setText("---------------\n线性加速度传感器不可用\n---------------");
        }
        if(sensorOrientation==null){
            bor=false;
            textView_or.setText("---------------\n方向传感器不可用\n---------------");
        }
        vb=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(bac)
            sensorManager.registerListener(this,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);//向加速传感器注册监听对像,第三个参数是传感器速度更新的速度，有四个常量可选
        if(bli)
            sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        if(bgr)
            sensorManager.registerListener(this,sensorGrivity,sensorManager.SENSOR_DELAY_NORMAL);
        if(bpre)
            sensorManager.registerListener(this,sensorPressure,SensorManager.SENSOR_DELAY_NORMAL);
        if(bpro)
            sensorManager.registerListener(this,sensorProximity,SensorManager.SENSOR_DELAY_NORMAL);
        if(bgy)
            sensorManager.registerListener(this,sensorGyroscope,SensorManager.SENSOR_DELAY_NORMAL);
        if(bgmf)
            sensorManager.registerListener(this,sensorGeomagneticfield,SensorManager.SENSOR_DELAY_NORMAL);
        if(bor)
            sensorManager.registerListener(this,sensorOrientation,SensorManager.SENSOR_DELAY_NORMAL);
        if(blac)
            sensorManager.registerListener(this,sensorLinerAcceleration,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){//当切换到其他界面时
        super.onPause();
        sensorManager.unregisterListener(this);//取消监听对象的注册
    }

    //不知道如何同时读取几个传感器的值

    @Override
    public void onSensorChanged(SensorEvent event) {//加速度值改变时
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                sa_x=event.values[0];
                sa_y=event.values[1];
                sa_z=event.values[2];
                textView_ac.setText(String.format("---------------\n加速度传感器：\nX轴：%1.2f m/s²\nY轴：%1.2f m/s²\nZ轴：%1.2f m/s²\n---------------",sa_x,sa_y,sa_z));
                ifShake();
                break;
            case Sensor.TYPE_LIGHT:
                textView_li.setText(String.format("---------------\n光线传感器：%1.2f lx\n---------------",event.values[0]));
                break;
            case Sensor.TYPE_PRESSURE:
                textView_pre.setText(String.format("---------------\n压力传感器：%1.2f hPa\n---------------",event.values[0]));
                break;
            case Sensor.TYPE_PROXIMITY:
                textView_pro.setText(String.format("---------------\n距离传感器：%1.2f cm\n---------------",event.values[0]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                textView_gy.setText(String.format("---------------\n陀螺仪：\nX轴：%1.2f r/s\nY轴：%1.2f r/s\nZ轴：%1.2f r/s\n---------------",event.values[0],event.values[1],event.values[2]));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                textView_gmf.setText(String.format("---------------\n地磁感应器：\nX轴：%1.2f μT\nY轴：%1.2f μT\nZ轴：%1.2f μT\n---------------",event.values[0],event.values[1],event.values[2]));
                break;
            case Sensor.TYPE_GRAVITY:
                sg_x=event.values[0];
                sg_y=event.values[1];
                sg_z=event.values[2];
                textView_gr.setText(String.format("---------------\n重力感应器：\nX轴：%1.2f m/s²\nY轴：%1.2f m/s²\nZ轴：%1.2f m/s²\n---------------",sg_x,sg_y,sg_z));
                ifSwing();
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                textView_lac.setText(String.format("---------------\n线性加速度传感器：\nX轴：%1.2f m/s²\nY轴：%1.2f m/s²\nZ轴：%1.2f m/s²\n---------------",event.values[0],event.values[1],event.values[2]));
                break;
            case Sensor.TYPE_ORIENTATION:
                textView_or.setText(String.format("---------------\n方向传感器：\nX轴：%1.2f °\nY轴：%1.2f °\nZ轴：%1.2f °\n---------------",event.values[1],event.values[2],event.values[0]));
                break;
        }
    }

    private void ifSwing() {
        if(mx==0)
            mx=(activityMain.getWidth()-imageView.getWidth())/50.0;
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.leftMargin=(int)(mx*25-sg_x*25.0/10.0*mx);
        params.bottomMargin=5;
        imageView.setLayoutParams(params);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {//精确度改变时不需要处理
    }

    private void ifShake(){
        sa_sum=Math.abs(sa_x)+Math.abs(sa_y)+Math.abs(sa_z);
        if(sa_sum>32){
            vb.vibrate(2000);
            Toast.makeText(this,"你摇了一下？",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateView(Location location) {//更新UI
        if (location != null) {
            String str = "定位提供者:" + location.getProvider();
            str += String.format("---------------\n%.4f °N\n%.4f °E\n:Heiht:%.2f m\nSpeed:%.2f m/s\n-----", location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed());
            textView_gps.setText(str);
        }
    }

    @Override
    public void onLocationChanged(Location location) {//GPS监听事件
        String str = "---------------\n定位提供者:" + location.getProvider();
        str += String.format("\n%.4f °N\n%.4f °E\n:Heiht:%.4f m\nSpeed:%.4f m/s\n---------------", location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed());
        textView_gps.setText(str);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"无法获得定位权限",Toast.LENGTH_LONG).show();
            return;
        }
        updateView(locationManager.getLastKnownLocation(provider));
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    long lastClickTime=0;
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        if(lastClickTime<=0){
            Toast.makeText(this,"再按一次返回键退出应用",Toast.LENGTH_SHORT).show();
            lastClickTime= System.currentTimeMillis();
        }else{
            long currentTime=System.currentTimeMillis();
            if(currentTime-lastClickTime<1000){
                finish();
            }
            else{
                Toast.makeText(this,"再按一次返回键退出应用",Toast.LENGTH_SHORT).show();
                lastClickTime=currentTime;
            }
        }

    }
}




