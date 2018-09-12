package com.qu.jian.btremote.Aty;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qu.jian.btremote.Config;
import com.qu.jian.btremote.Net.BluetoothConnection;
import com.qu.jian.btremote.Net.WifiDirectAdmin;
import com.qu.jian.btremote.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static com.qu.jian.btremote.R.id.listView_bt;


public class Fragment_reset extends Fragment{

    private Button button_bt,button_vd,button_search,button_close,button_sensor;
    private TextView textView_reset_tip;
    private ListView listView_devices;
    private ProgressBar progressBar_serachBT;
    private ArrayList<String> list_Devices=new ArrayList<String>();
    private ArrayAdapter arrayAdapter;
    private BluetoothConnection bluetoothConnection;
    private BluetoothAdapter bluetoothAdapter;
    String flag="";
    private WifiDirectAdmin wifiDirectAdmin;
    private WifiP2pManager wifiP2pManager;

    private String tpWay=null;
    private boolean booleanAutonetBT=false,booleanAutonetWD=false,booleanVD=false;

    private LocalBroadcastManager localBroadcastManager;
    private FragmentManager fragmentManager;
    private Fragment_sensor fragmentSensor;

    public Fragment_reset() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothConnection= (BluetoothConnection) getArguments().getSerializable(BluetoothConnection.BLUETOOTHCONNECTION_KEY);//获取从Activity传过来的对象
        wifiDirectAdmin=new WifiDirectAdmin(getActivity());
        wifiP2pManager=wifiDirectAdmin.getWifiP2pManager();
        booleanAutonetBT=Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_BT);
        booleanAutonetWD=Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_WIFI);
        booleanVD=Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_VDSTREAM);
        tpWay=Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY);
        if(tpWay==null)tpWay=Config.TPWAY_BT_AND_WD;
        fragmentManager=getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reset, container, false);
        createViews(view);
        arrayAdapter=new ArrayAdapter(getActivity(),R.layout.list_item1,list_Devices);
        fragmentManager=getActivity().getSupportFragmentManager();
        listView_devices.setAdapter(arrayAdapter);
        setListViewItemClick();
        setButtonClick();
        if(tpWay.equals(Config.TPWAY_ONLY_WD))button_vd.setVisibility(View.GONE);
        return view;
    }


    private void setButtonClick(){
        button_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter=bluetoothConnection.getBluetoothAdapter();
                if(bluetoothAdapter!=null) {
                    hideViews(false);
                    listView_devices.setVisibility(View.VISIBLE);
                    if(bluetoothAdapter.isEnabled()) {
                        selectingButton(Config.SELECTED_BUTTON_BLUETOOTH);
                        flag=Config.IS_WHAT_BT;
                        list_Devices.clear();
                        Set<BluetoothDevice> mybluetoothDevices;//储存蓝牙设备的容器
                        mybluetoothDevices = bluetoothAdapter.getBondedDevices();
                        for (BluetoothDevice bd:mybluetoothDevices) {
                            String str = getString(R.string.bt_had_match)+"|" + bd.getName() + "|" + bd.getAddress();
                            list_Devices.add(str); // 获取设备名称和mac地址
                        }
                        arrayAdapter.notifyDataSetChanged();
                        textView_reset_tip.setText(getString(R.string.this_phone_bt_address)+"：" + bluetoothAdapter.getAddress());
                    }else {
                        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnOn,0);
                    }
                }else Toast.makeText(getActivity(),R.string.lacking_bluetooth,Toast.LENGTH_SHORT).show();
            }
        });
        button_vd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifiP2pManager!=null){
                    flag=Config.IS_WHAT_VD;
                    hideViews(false);
                    selectingButton(Config.SELECTED_BUTTON_WIFIDIRECT);
                    listView_devices.setVisibility(View.VISIBLE);
                } else Toast.makeText(getActivity(),"本机不支持WIFI直连功能",Toast.LENGTH_SHORT).show();
            }
        });
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingButton(Config.SELECTED_BUTTON_SEARCH);
                list_Devices.clear();
                arrayAdapter.notifyDataSetChanged();
                switch (flag) {
                    case Config.IS_WHAT_BT:
                        bluetoothConnection.serchDevice();
                        break;
                    case Config.IS_WHAT_VD:
                        wifiDirectAdmin.searchDevices(actionListener);
                        break;
                }
            }
        });
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingButton(Config.SELECTED_BUTTON_CLOSE);
                listView_devices.setVisibility(View.INVISIBLE);
                progressBar_serachBT.setVisibility(View.INVISIBLE);
                switch (flag) {
                    case Config.IS_WHAT_BT:
                        bluetoothAdapter.disable();
                        textView_reset_tip.setText(R.string.bt_have_close);
                        break;
                    case Config.IS_WHAT_VD:
                        textView_reset_tip.setText(R.string.wd_have_close);
                        break;
                }
            }
        });
        button_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingButton(Config.SELECTED_BUTTON_SENSOR);
                hideViews(true);
                listView_devices.setVisibility(View.INVISIBLE);
                loadSensorFragment();
            }
        });
        hideViews(true);
        progressBar_serachBT.setVisibility(View.INVISIBLE);
        if(tpWay.equals(Config.TPWAY_BT_AND_WD)){
            if(booleanAutonetBT)autoNetBT();
            if(booleanAutonetWD)autoNetWD();
        }else if(tpWay.equals(Config.TPWAY_ONLY_WD)){
            if(booleanAutonetWD)autoNetWD();
        }
    }

    private boolean flag_sensor=false;
    private void loadSensorFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(fragmentSensor==null){
             fragmentSensor= new Fragment_sensor();
            transaction.add(R.id.frameLayout_sensor, fragmentSensor);
            transaction.addToBackStack(null);
        }
        if(flag_sensor){
            flag_sensor=false;
            transaction.hide(fragmentSensor);
            button_sensor.setText("传感器");
            button_bt.setVisibility(View.VISIBLE);
            button_vd.setVisibility(View.VISIBLE);
        }else {
            transaction.show(fragmentSensor);
            button_sensor.setText("隐藏");
            button_bt.setVisibility(View.INVISIBLE);
            button_vd.setVisibility(View.INVISIBLE);
            hideViews(true);
            flag_sensor=true;
        }
            transaction.commit();
    }

    private void autoNetWD() {
        progressBar_serachBT.setVisibility(View.VISIBLE);
        String adress=Config.getCacheStringConfig(getActivity(),Config.KEY_ADRESS_WD);
        textView_reset_tip.setText("正在连接:"+adress);
    }

    private void autoNetBT() {
        if(bluetoothConnection.getBluetoothAdapter()!=null){
            final String adress=Config.getCacheStringConfig(getActivity(),Config.KEY_ADRESS_BT);
            final String name=Config.getCacheStringConfig(getActivity(),Config.KEY_NAME_BT);
            if(adress!=null){
                AlertDialog.Builder bdr = new AlertDialog.Builder(getActivity())
                    .setTitle("蓝牙")
                    .setMessage("是否连接蓝牙："+name+" ？")
                    .setCancelable(true)
                        .setNegativeButton("取消",null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar_serachBT.setVisibility(View.VISIBLE);
                            Set<BluetoothDevice> btSet=bluetoothConnection.getBluetoothAdapter().getBondedDevices();
                            textView_reset_tip.setText("正在连接:"+adress);
                            Toast.makeText(getActivity(),"正在连接:"+adress,Toast.LENGTH_LONG).show();
                            for(BluetoothDevice bd:btSet) if(adress.equals(bd.getAddress()))
                                if(bluetoothConnection.connect(getActivity(),bd)){
                                    progressBar_serachBT.setVisibility(View.INVISIBLE);
                                    textView_reset_tip.setText("成功连接："+name);
                                    localBroadcastManager.sendBroadcast(new Intent(Config.BD_BTOK));
                                    break;
                                }
                                else progressBar_serachBT.setVisibility(View.INVISIBLE);
                        }
                    });
                bdr.show();
            }
            else Toast.makeText(getActivity(),"无历史蓝牙",Toast.LENGTH_LONG).show();
       }
    }

    private void setListViewItemClick() {
        listView_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (flag){
                    case Config.IS_WHAT_BT:
                        if(bluetoothAdapter.isDiscovering())bluetoothAdapter.cancelDiscovery();
                        String str = list_Devices.get(position);
                        String[] values = str.split("\\|");
                        String address = values[2];
                        String bt_name=values[1];
                        textView_reset_tip.setText(R.string.connecting+"："+bt_name);
                        BluetoothDevice btDev = bluetoothAdapter.getRemoteDevice(address);
                        try {
                            Boolean returnValue = false;
                            if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                                //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);
                                Toast.makeText(getActivity(),R.string.connecting+"："+bt_name,Toast.LENGTH_LONG).show();
                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                returnValue = (Boolean) createBondMethod.invoke(btDev);
                            }else if(btDev.getBondState() == BluetoothDevice.BOND_BONDED){
                                progressBar_serachBT.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),"正在连接："+bt_name,Toast.LENGTH_SHORT).show();
                                if(bluetoothConnection.connect(getActivity(),btDev)){
                                    progressBar_serachBT.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder bdr = new AlertDialog.Builder(getActivity())
                                            .setTitle("蓝牙连接成功")
                                            .setMessage("已创建远程连接端口")
                                            .setCancelable(true)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    localBroadcastManager.sendBroadcast(new Intent(Config.BD_BTOK));
                                                }
                                            });
                                    bdr.show();
                                }else {
                                    textView_reset_tip.setText("设备连接失败");
                                    progressBar_serachBT.setVisibility(View.INVISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case Config.IS_WHAT_VD:
                        String str_wd = list_Devices.get(position);
                        String[] values_wd = str_wd.split("\\|");
                        String address_wd=values_wd[1];
                        WifiP2pConfig wifiP2pConfig=new WifiP2pConfig();
                        wifiP2pConfig.deviceAddress=address_wd;
                        wifiP2pConfig.wps.setup= WpsInfo.PBC;
                        wifiP2pManager.connect(wifiDirectAdmin.getChannel(), wifiP2pConfig, new WifiP2pManager.ActionListener() {
                            /**
                             * 上述实现的WifiP2pManager.ActionListener接口只会通知你连接成功还是失败。
                             * 要监听连接状态的变化，就好实现WifiP2pManager.ConnectionInfoListener()接口
                             * **/
                            @Override
                            public void onSuccess() {
                                textView_reset_tip.setText("连接远程设备成功");
                            }
                            @Override
                            public void onFailure(int reason) {
                                textView_reset_tip.setText("连接远程设备失败");
                            }
                        });
                        break;
                }
            }
        });
    }

    private void createViews(View view){
        button_bt=(Button)view.findViewById(R.id.button_bluetooth);
        button_vd=(Button)view.findViewById(R.id.button_video);
        button_search=(Button)view.findViewById(R.id.button_search);
        button_close=(Button)view.findViewById(R.id.button_close);
        button_sensor=(Button)view.findViewById(R.id.button_sensor);
        textView_reset_tip=(TextView)view.findViewById(R.id.textView_reset_tip);
        progressBar_serachBT=(ProgressBar)view.findViewById(R.id.progressBar_searchBT);
        listView_devices=(ListView)view.findViewById(listView_bt);
    }

    private void selectingButton(int i){
        button_bt.setBackgroundColor(Color.WHITE);
        button_bt.setTextColor(getResources().getColor(R.color.skyblue));
        button_vd.setBackgroundColor(Color.WHITE);
        button_vd.setTextColor(getResources().getColor(R.color.skyblue));
        button_search.setBackgroundColor(Color.WHITE);
        button_search.setTextColor(getResources().getColor(R.color.skyblue));
        button_close.setBackgroundColor(Color.WHITE);
        button_close.setTextColor(getResources().getColor(R.color.skyblue));
        button_sensor.setBackgroundColor(Color.WHITE);
        button_sensor.setTextColor(getResources().getColor(R.color.skyblue));
        switch (i){
            case Config.SELECTED_BUTTON_BLUETOOTH:
                button_bt.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_bt.setTextColor(Color.WHITE);
                break;
            case Config.SELECTED_BUTTON_WIFIDIRECT:
                button_vd.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_vd.setTextColor(Color.WHITE);
                break;
            case Config.SELECTED_BUTTON_SEARCH:
                button_search.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_search.setTextColor(Color.WHITE);
                break;
            case Config.SELECTED_BUTTON_CLOSE:
                button_close.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_close.setTextColor(Color.WHITE);
                break;
            case Config.SELECTED_BUTTON_SENSOR:
                button_sensor.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_sensor.setTextColor(Color.WHITE);
                break;
        }
    }

    private void hideViews(boolean b){
        if(b){
            button_close.setVisibility(View.INVISIBLE);
            button_search.setVisibility(View.INVISIBLE);
        }else {
            button_close.setVisibility(View.VISIBLE);
            button_search.setVisibility(View.VISIBLE);
        }
    }

    WifiP2pManager.ActionListener actionListener=new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
            progressBar_serachBT.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"开始搜索对等点",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(int reason) {
            Toast.makeText(getActivity(),"搜索对等点失败",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        bluetoothConnection.registerBTReceiver(getActivity(),searchBTDevices);//注册蓝牙相关广播
        wifiDirectAdmin.registerWifiDirectBCReceiver(getActivity(),searchWifiDirectDevice);//注册WIFI直连相关广播
        localBroadcastReceiverRegister();//注册本地广播，用于监听设置的改变
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        bluetoothConnection.unregisterReceiver(getActivity(),searchBTDevices);
        localBroadcastManager.unregisterReceiver(broadcastReceiverSitting);
    }

    private void localBroadcastReceiverRegister(){
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Config.BD_TPWAY);
        intentFilter.addAction(Config.BD_AUTONET_BT);
        intentFilter.addAction(Config.BD_AUTONET_WIFI);
        intentFilter.addAction(Config.BD_VDSTREAM);
        localBroadcastManager.registerReceiver(broadcastReceiverSitting,intentFilter);
    }


    private BroadcastReceiver broadcastReceiverSitting=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            textView_reset_tip.setText("收到广播："+action);
            switch (action){
                case Config.BD_TPWAY:
                    tpWay=intent.getStringExtra(Config.KEY_SITTING_TPWAY);
                    Toast.makeText(getActivity(),"连接方式："+tpWay,Toast.LENGTH_SHORT).show();
                    if(tpWay.equals(Config.TPWAY_ONLY_WD))button_vd.setVisibility(View.GONE);
                    break;
                case Config.BD_AUTONET_BT:
                    booleanAutonetBT=intent.getBooleanExtra(Config.KEY_SITTING_AUTONET_BT,false);
                    break;
                case Config.BD_AUTONET_WIFI:
                    booleanAutonetWD=intent.getBooleanExtra(Config.KEY_SITTING_AUTONET_WIFI,false);
                    break;
                case Config.BD_VDSTREAM:
                    booleanVD=intent.getBooleanExtra(Config.KEY_SITTING_VDSTREAM,false);
                    break;
            }
        }
    };


    public BroadcastReceiver searchBTDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = null;
            switch (action){
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                        String str = getString(R.string.bt_had_not_match)+"|" + device.getName() + "|" + device.getAddress();
                        if (list_Devices.indexOf(str) == -1)// 防止重复添加
                            list_Devices.add(str); // 获取设备名称和mac地址
                        arrayAdapter.notifyDataSetChanged();
                    }
                    else if(device.getBondState()==BluetoothDevice.BOND_BONDED){
                        String str=getString(R.string.bt_had_match)+"|" + device.getName() + "|" + device.getAddress();
                        list_Devices.add(str);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    textView_reset_tip.setText("发现："+device.getName());
                    break;

                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            bluetoothAdapter.cancelDiscovery();
                            bluetoothConnection.connect(getActivity(),device);
                            break;
                        case BluetoothDevice.BOND_NONE:
                    }
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Toast.makeText(getActivity(),getString(R.string.start_discovery_devices),Toast.LENGTH_LONG).show();
                    progressBar_serachBT.setVisibility(View.VISIBLE);
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(getActivity(),getString(R.string.end_discovery_devices),Toast.LENGTH_LONG).show();
                    progressBar_serachBT.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private BroadcastReceiver searchWifiDirectDevice=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                    int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
                    if(tpWay.equals(Config.TPWAY_ONLY_WD)){if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED)textView_reset_tip.setText("WIFI直连已打开");
                    else if(state==WifiP2pManager.WIFI_P2P_STATE_DISABLED)textView_reset_tip.setText("wifi直连已关闭");}
                    break;

                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    Toast.makeText(getActivity(),"查找到设备",Toast.LENGTH_SHORT).show();
                    wifiP2pManager.requestPeers(wifiDirectAdmin.getChannel(),peerListListener );
                    break;

                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION://查看是否创建连接
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        wifiP2pManager.requestConnectionInfo(wifiDirectAdmin.getChannel(),wifiDirectAdmin.connectionInfoListener);
                    }else {

                    }
                    break;
                case WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION:
                    int states=intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE,-1);
                    if(states==WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED){

                    }else if(states==WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED){
                        progressBar_serachBT.setVisibility(View.INVISIBLE);
                    }
                    break;
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    break;

            }
        }
    };

    private WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            list_Devices.clear();
            for(WifiP2pDevice wifiP2pDevice:peers.getDeviceList()) list_Devices.add(wifiP2pDevice.deviceName+"|"+wifiP2pDevice.deviceAddress+"|"+wifiP2pDevice.primaryDeviceType);
            arrayAdapter.notifyDataSetChanged();
            if(list_Devices.size()<1)textView_reset_tip.setText("没有找到可用设备");
        }
    };
}
