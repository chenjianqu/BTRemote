package com.qu.jian.btremote.Aty;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.qu.jian.btremote.Config;
import com.qu.jian.btremote.R;

import java.util.ArrayList;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_sitting extends Fragment{
    private RadioGroup radioGroup_TPway;
    private Button button_save_manual_order,button_exit;
    private Spinner spinner_aler_sensorvalue;
    private Switch switch_vdStream,switch_autoNetBt,switch_autoNetWifi;
    private ArrayList<String> arrayList_gravity_sensor=new ArrayList<>();
    private ArrayAdapter adapter;
    private TextView textView_sitting_seek,textView_sitting_gm,textView_sitting_gv,textView_sitting_tip,textView_sitting_muanualOrder,textView_TPway,textView_sitting_gn;
    private LinearLayout linearLayout_sitting_manual_order,layout_sitting_seekbarMaxmin,layout_sitting_SensorMaxmin,layout_sitting_GnMaxmin;
    private EditText editText_sitting_manual_auto,editText_sitting_manual_grab,editText_sitting_manual_hands,editText_sitting_manual_armts,editText_sitting_manual_armms,editText_sitting_manual_armbs;
    private EditText editText_sitting_manual_ptts,editText_sitting_manual_ptbs,editText_sitting_manual_f,editText_sitting_manual_b,editText_sitting_manual_l,editText_sitting_manual_r,editText_sitting_manual_s;
    private EditText editText_sb_max,editText_sb_min,editText_gv_max,editText_gv_min,editText_gv_cf,editText_gra_add,editText_gra_sub;
    private EditText editText_sitting_manual,editText_sitting_interate,editText_sitting_gravity,editText_sitting_reset;

    private int flag_TPway=-1;
    private int flag_vdStream=-1;
    private int flag_autoNetBt=-1;
    private int flag_autoNetWifi=-1;
    private int flag_gravityMode=-1;
    private LocalBroadcastManager localBroadcastManager;
    private Vibrator vibrator;

    public Fragment_sitting() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sitting, container, false);
        //获取组件实例
        createViews(view);
        localBroadcastManager=LocalBroadcastManager.getInstance(getActivity());
        vibrator= (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //组件监听事件
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_EXIT);
                vibrator.vibrate(100);
                getActivity().finish();
            }});
        radioGroup_TPway.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton_onlyWIFID:
                        switch_autoNetBt.setVisibility(View.GONE);
                        //flag_TPway=Config.FLAG_TPWAY_ONLYWIFID;
                        Toast.makeText(getActivity(),"该功能未完善，后续版本开放",Toast.LENGTH_LONG).show();
                        onlyBT(false);
                        break;
                    case R.id.radioButton_WifiAndBt:
                        switch_autoNetBt.setVisibility(VISIBLE);
                        onlyBT(true);
                        flag_TPway=Config.FLAG_TPWAY_WIFIDANDBT;
                        break;
                }
            }});
        textView_TPway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_TPWAY);
            }
        });
        textView_sitting_muanualOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_MANUAL_ORDER);
            }
        });
        switch_vdStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_VDSTREAM);
            }
        });
        switch_vdStream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    flag_vdStream=Config.FLAG_ON;
                    textView_sitting_tip.setText(R.string.start_video_stream);
                }
                else {
                    flag_vdStream=Config.FLAG_OFF;
                    textView_sitting_tip.setText(R.string.end_video_stream);
                }
                switch_vdStream.setChecked(false);
                flag_vdStream=Config.FLAG_OFF;
                textView_sitting_tip.setText(R.string.fail_to_start_video_stream);
            }
        });
        switch_autoNetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_AUTO_BT);
            }
        });
        switch_autoNetBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)flag_autoNetBt=Config.FLAG_ON;
                else flag_autoNetBt=Config.FLAG_OFF;
            }
        });
        switch_autoNetWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_AUTO_WIFI);
            }
        });
        switch_autoNetWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if(isChecked)flag_autoNetWifi=Config.FLAG_ON;
                //else flag_autoNetWifi=Config.FLAG_OFF;
                switch_autoNetWifi.setChecked(false);
                flag_autoNetWifi=Config.FLAG_OFF;
                textView_sitting_tip.setText("该功能未完善");
            }
        });
        spinner_aler_sensorvalue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                flag_gravityMode=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        textView_sitting_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_SEEKBARVALUE);
            }
        });
        textView_sitting_gv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_GRAVITYVALUE);
            }
        });
        textView_sitting_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSitting(Config.SITTING_SELECT_GRAVITYMODE);
            }
        });
        textView_sitting_gn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_GRAVITYNUM);
            }
        });
        editText_sb_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_SEEKBARVALUE);
            }
        });
        editText_sb_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_SEEKBARVALUE);
            }
        });
        editText_gv_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_GRAVITYVALUE);
            }
        });
        editText_gv_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSitting(Config.SITTING_SELECT_GRAVITYVALUE);
            }
        });
        button_save_manual_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(200);
                saveConfiguration();
            }
        });
        //加载重力传感器轴
        for(String s:Config.ARRAY_GRAVITY_AXIS)arrayList_gravity_sensor.add(s);
        adapter=new ArrayAdapter(getActivity(),R.layout.list_item1_sitting_gravity_value,arrayList_gravity_sensor);
        spinner_aler_sensorvalue.setAdapter(adapter);
        //选择界面效果重置
        selectSitting(-1);
        //载入设置
        setConfig();
        if(radioGroup_TPway.getCheckedRadioButtonId()==R.id.radioButton_WifiAndBt)onlyBT(true);
        return view;
    }


    private void onlyBT(boolean b){
        if(b){
            switch_autoNetWifi.setVisibility(View.GONE);
            switch_vdStream.setVisibility(View.GONE);
        }else {
            switch_autoNetWifi.setVisibility(VISIBLE);
            switch_vdStream.setVisibility(View.VISIBLE);
        }
    }

    //保存配置
    private void saveConfiguration(){
        Intent it=new Intent();
        float t= (float) 0.0;
        t=Float.parseFloat(editText_sb_max.getText().toString());
        if(t!=Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_SB_MAX)){
            it.setAction(Config.BD_SITTING_SB_MAX);
            it.putExtra(Config.KEY_SITTING_SB_MAX,String.valueOf(t));
            Config.cacheFloatConfig(getActivity(),Config.KEY_SITTING_SB_MAX, t);
        }
        t=Float.parseFloat(editText_sb_min.getText().toString());
        if(t!=Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_SB_MIN)){
            it.setAction(Config.BD_SITTING_SB_MIN);
            it.putExtra(Config.KEY_SITTING_SB_MIN,String.valueOf(t));
            Config.cacheFloatConfig(getActivity(),Config.KEY_SITTING_SB_MIN, t);
        }
        t=Float.parseFloat(editText_gv_max.getText().toString());
        if(t!=Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MAX)) {
            it.setAction(Config.BD_SITTING_GV_MAX);
            it.putExtra(Config.KEY_SITTING_GV_MAX, String.valueOf(t));
            Config.cacheFloatConfig(getActivity(), Config.KEY_SITTING_GV_MAX, t);
        }
        if(!editText_gra_add.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_ADD))){
            it.setAction(Config.BD_SITTING_GRA_ADD);
            it.putExtra(Config.KEY_SITTING_GRA_ADD,editText_gra_add.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_ADD,editText_gra_add.getText().toString());
        }
        if(!editText_gra_sub.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_SUB))){
            it.setAction(Config.BD_SITTING_GRA_SUB);
            it.putExtra(Config.KEY_SITTING_GRA_SUB,editText_gra_sub.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_SUB,editText_gra_sub.getText().toString());
        }
        t=Float.parseFloat(editText_gv_min.getText().toString());
        if(t!=Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MIN)){
            it.setAction(Config.BD_SITTING_GV_MIN);
            it.putExtra(Config.KEY_SITTING_GV_MIN,String.valueOf(t));
            Config.cacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MIN,t);
        }
        if(!editText_gv_cf.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GV_CF))){
            it.setAction(Config.BD_SITTING_GV_CF);
            it.putExtra(Config.KEY_SITTING_GV_CF,editText_gv_cf.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_GV_CF,editText_gv_cf.getText().toString());
        }
        if(!editText_sitting_manual_auto.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_AUTO))){
            it.setAction(Config.BD_SITTING_MANUAL_AUTO);
            it.putExtra(Config.KEY_SITTING_AUTO,editText_sitting_manual_auto.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_AUTO,editText_sitting_manual_auto.getText().toString());
        }
        if(!editText_sitting_manual_grab.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRAB))){
            it.setAction(Config.BD_SITTING_MANUAL_GRAB);
            it.putExtra(Config.KEY_SITTING_GRAB,editText_sitting_manual_grab.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_GRAB,editText_sitting_manual_grab.getText().toString());
        }
        if(!editText_sitting_manual_hands.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_HAND))){
            it.setAction(Config.BD_SITTING_MANUAL_HAND);
            it.putExtra(Config.KEY_SITTING_HAND,editText_sitting_manual_hands.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_HAND,editText_sitting_manual_hands.getText().toString());
        }
        if(!editText_sitting_manual_armts.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_ARMTS))){
            it.setAction(Config.BD_SITTING_MANUAL_ARMTS);
            it.putExtra(Config.KEY_SITTING_ARMTS,editText_sitting_manual_armts.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_ARMTS,editText_sitting_manual_armts.getText().toString());
        }
        if(!editText_sitting_manual_armms.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_ARMMS))){
            it.setAction(Config.BD_SITTING_MANUAL_ARMMS);
            it.putExtra(Config.KEY_SITTING_ARMMS,editText_sitting_manual_armms.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_ARMMS,editText_sitting_manual_armms.getText().toString());
        }
        if(!editText_sitting_manual_armbs.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_ARMBS))){
            it.setAction(Config.BD_SITTING_MANUAL_ARMBS);
            it.putExtra(Config.KEY_SITTING_ARMBS,editText_sitting_manual_armbs.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_ARMBS,editText_sitting_manual_armbs.getText().toString());
        }
        if(!editText_sitting_manual_ptts.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_PTTS))){
            it.setAction(Config.BD_SITTING_MANUAL_PTTS);
            it.putExtra(Config.KEY_SITTING_PTTS,editText_sitting_manual_ptts.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_PTTS,editText_sitting_manual_ptts.getText().toString());
        }
        if(!editText_sitting_manual_ptbs.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_PTBS))){
            it.setAction(Config.BD_SITTING_MANUAL_PTBS);
            it.putExtra(Config.KEY_SITTING_PTBS,editText_sitting_manual_ptbs.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_PTBS,editText_sitting_manual_ptbs.getText().toString());
        }
        if(!editText_sitting_manual_f.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_F))){
            it.setAction(Config.BD_SITTING_MANUAL_F);
            it.putExtra(Config.KEY_SITTING_F,editText_sitting_manual_f.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_F,editText_sitting_manual_f.getText().toString());
        }
        if(!editText_sitting_manual_b.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_B))){
            it.setAction(Config.BD_SITTING_MANUAL_B);
            it.putExtra(Config.KEY_SITTING_B,editText_sitting_manual_b.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_B,editText_sitting_manual_b.getText().toString());
        }
        if(!editText_sitting_manual_s.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_S))){
            it.setAction(Config.BD_SITTING_MANUAL_S);
            it.putExtra(Config.KEY_SITTING_S,editText_sitting_manual_s.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_S,editText_sitting_manual_s.getText().toString());
        }
        if(!editText_sitting_manual_l.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_L))){
            it.setAction(Config.BD_SITTING_MANUAL_L);
            it.putExtra(Config.KEY_SITTING_L,editText_sitting_manual_l.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_L,editText_sitting_manual_l.getText().toString());
        }
        if(!editText_sitting_manual_r.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_R))){
            it.setAction(Config.BD_SITTING_MANUAL_R);
            it.putExtra(Config.KEY_SITTING_R,editText_sitting_manual_r.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_R,editText_sitting_manual_r.getText().toString());
        }
        if(!editText_sitting_manual.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_MANUAL))){
            it.setAction(Config.BD_SITTING_MANUAL);
            it.putExtra(Config.KEY_SITTING_R,editText_sitting_manual.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_MANUAL,editText_sitting_manual.getText().toString());
        }
        if(!editText_sitting_interate.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_INTERATE))){
            it.setAction(Config.BD_SITTING_INTERATE);
            it.putExtra(Config.KEY_SITTING_INTERATE,editText_sitting_interate.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_INTERATE,editText_sitting_interate.getText().toString());
        }
        if(!editText_sitting_gravity.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRAVITY))){
            it.setAction(Config.BD_SITTING_GRAVITY);
            it.putExtra(Config.KEY_SITTING_GRAVITY,editText_sitting_gravity.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_GRAVITY,editText_sitting_gravity.getText().toString());
        }
        if(!editText_sitting_reset.getText().toString().equals(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_RESET))){
            it.setAction(Config.BD_SITTING_RESET);
            it.putExtra(Config.KEY_SITTING_RESET,editText_sitting_reset.getText().toString());
            Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_RESET,editText_sitting_reset.getText().toString());
        }
        if(flag_TPway!=-1){
            it.setAction(Config.BD_TPWAY);
            if(flag_TPway==Config.FLAG_TPWAY_ONLYWIFID){
                it.putExtra(Config.KEY_SITTING_TPWAY,Config.TPWAY_ONLY_WD);
                Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY,Config.TPWAY_ONLY_WD);
            }
            else if(flag_TPway==Config.FLAG_TPWAY_WIFIDANDBT){
                it.putExtra(Config.KEY_SITTING_TPWAY,Config.TPWAY_BT_AND_WD);
                Config.cacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY,Config.TPWAY_BT_AND_WD);
            }
        }
        if(flag_autoNetBt!=-1){
            it.setAction(Config.BD_AUTONET_BT);
            if(flag_autoNetBt==Config.FLAG_ON){
                it.putExtra(Config.KEY_SITTING_AUTONET_BT,String.valueOf(Config.FLAG_ON));
                Config.cacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_BT,true);
            }
            else if(flag_autoNetBt==Config.FLAG_OFF){
                it.putExtra(Config.KEY_SITTING_AUTONET_BT,String.valueOf(Config.FLAG_OFF));
                Config.cacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_BT,false);
            }
        }
        if(flag_autoNetWifi!=-1){
            it.setAction(Config.BD_AUTONET_WIFI);
            if(flag_autoNetWifi==Config.FLAG_ON){
                it.putExtra(Config.KEY_SITTING_AUTONET_WIFI,String.valueOf(Config.FLAG_ON));
                Config.cacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_WIFI,true);
            }
            else if(flag_autoNetWifi==Config.FLAG_OFF){
                it.putExtra(Config.KEY_SITTING_AUTONET_WIFI,String.valueOf(Config.FLAG_OFF));
                Config.cacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_WIFI,false);
            }
        }
        if(flag_vdStream!=-1){
            it.setAction(Config.BD_VDSTREAM);
            if(flag_vdStream==Config.FLAG_ON){
                it.putExtra(Config.KEY_SITTING_VDSTREAM,String.valueOf(Config.FLAG_ON));
                Config.cacheBooleanConfig(getActivity(),Config.KEY_SITTING_VDSTREAM,true);
            }
            else if(flag_vdStream==Config.FLAG_OFF){
                it.putExtra(Config.KEY_SITTING_VDSTREAM,String.valueOf(Config.FLAG_OFF));
                Config.cacheBooleanConfig(getActivity(),Config.KEY_SITTING_VDSTREAM,false);
            }
        }
        if(flag_gravityMode!=-1&&flag_gravityMode!=Config.getCacheIntConfig(getActivity(),Config.KEY_SITTING_GRAVITY_MODE)){
            Config.cacheIntConfig(getActivity(),Config.KEY_SITTING_GRAVITY_MODE,flag_gravityMode);
            it.setAction(Config.BD_SITTING_GRAVITY_MO);
            it.putExtra(Config.KEY_SITTING_GRAVITY_MODE,String.valueOf(flag_gravityMode));
        }
        localBroadcastManager.sendBroadcast(it);
        selectSitting(-1);
        textView_sitting_tip.setText("设置已保存");

    }

    //初始化设置选项
    private void setConfig() {
        if(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY).equals(Config.TPWAY_BT_AND_WD))radioGroup_TPway.check(R.id.radioButton_WifiAndBt);
        else if(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY).equals(Config.TPWAY_ONLY_WD))radioGroup_TPway.check(R.id.radioButton_onlyWIFID);
        if(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_TPWAY).equals(Config.TPWAY_ONLY_WD))switch_autoNetBt.setVisibility(View.GONE);
        if(Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_VDSTREAM))switch_vdStream.setChecked(true);
        else switch_vdStream.setChecked(false);
        if(Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_BT))switch_autoNetBt.setChecked(true);
        else switch_autoNetBt.setChecked(false);
        if(Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_WIFI))switch_autoNetWifi.setChecked(true);
        else switch_autoNetWifi.setChecked(false);
        editText_sitting_manual_armbs.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_ARMBS));
        editText_sitting_manual_armms.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_ARMMS));
        editText_sitting_manual_armts.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_ARMTS));
        editText_sitting_manual_auto.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_AUTO));
        editText_sitting_manual_b.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_B));
        editText_sitting_manual_f.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_F));
        editText_sitting_manual_grab.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRAB));
        editText_sitting_manual_hands.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_HAND));
        editText_sitting_manual_l.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_L));
        editText_sitting_manual_ptbs.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_PTBS));
        editText_sitting_manual_ptts.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_PTTS));
        editText_sitting_manual_r.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_R));
        editText_sitting_manual_s.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_S));
        editText_sitting_manual.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_MANUAL));
        editText_sitting_gravity.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRAVITY));
        editText_sitting_interate.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_INTERATE));
        editText_sitting_reset.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_RESET));
        switch_vdStream.setChecked(Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_VDSTREAM));
        switch_autoNetWifi.setChecked(Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_WIFI));
        switch_autoNetBt.setChecked(Config.getCacheBooleanConfig(getActivity(),Config.KEY_SITTING_AUTONET_BT));
        editText_sb_max.setText(String.valueOf(Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_SB_MAX)));
        editText_sb_min.setText(String.valueOf(Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_SB_MIN)));
        editText_gv_max.setText(String.valueOf(Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MAX)));
        editText_gv_min.setText(String.valueOf(Config.getCacheFloatConfig(getActivity(),Config.KEY_SITTING_GV_MIN)));
        editText_gv_cf.setText(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GV_CF));
        editText_gra_add.setText(String.valueOf(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_ADD)));
        editText_gra_sub.setText(String.valueOf(Config.getCacheStringConfig(getActivity(),Config.KEY_SITTING_GRA_SUB)));
        spinner_aler_sensorvalue.setSelection(Config.getCacheIntConfig(getActivity(),Config.KEY_SITTING_GRAVITY_MODE));
    }



    //视觉效果的变化
    private void selectSitting(int i){
        radioGroup_TPway.setVisibility(View.INVISIBLE);
        linearLayout_sitting_manual_order.setVisibility(View.GONE);
        layout_sitting_seekbarMaxmin.setVisibility(View.INVISIBLE);
        layout_sitting_SensorMaxmin.setVisibility(View.INVISIBLE);
        layout_sitting_GnMaxmin.setVisibility(View.INVISIBLE);
        spinner_aler_sensorvalue.setVisibility(View.INVISIBLE);
        switch_vdStream.setBackgroundColor(Color.WHITE);
        switch_vdStream.setTextColor(getResources().getColor(R.color.skyblue));
        switch_autoNetBt.setBackgroundColor(Color.WHITE);
        switch_autoNetBt.setTextColor(getResources().getColor(R.color.skyblue));
        switch_autoNetWifi.setBackgroundColor(Color.WHITE);
        switch_autoNetWifi.setTextColor(getResources().getColor(R.color.skyblue));
        textView_TPway.setTextColor(getResources().getColor(R.color.skyblue));
        textView_TPway.setBackgroundColor(Color.WHITE);
        textView_sitting_seek.setTextColor(getResources().getColor(R.color.skyblue));
        textView_sitting_gm.setTextColor(getResources().getColor(R.color.skyblue));
        textView_sitting_gv.setTextColor(getResources().getColor(R.color.skyblue));
        textView_sitting_gn.setTextColor(getResources().getColor(R.color.skyblue));
        textView_sitting_seek.setBackgroundColor(Color.WHITE);
        textView_sitting_gm.setBackgroundColor(Color.WHITE);
        textView_sitting_gv.setBackgroundColor(Color.WHITE);
        textView_sitting_gn.setBackgroundColor(Color.WHITE);
        button_exit.setBackgroundColor(Color.GRAY);
        button_exit.setTextColor(getResources().getColor(R.color.skyblue));
        textView_sitting_muanualOrder.setBackgroundColor(Color.WHITE);
        textView_sitting_muanualOrder.setTextColor(getResources().getColor(R.color.skyblue));
        editText_sb_max.setTextColor(getResources().getColor(R.color.channel_bg));
        editText_sb_min.setTextColor(getResources().getColor(R.color.channel_bg));
        editText_gv_max.setTextColor(getResources().getColor(R.color.channel_bg));
        editText_gv_min.setTextColor(getResources().getColor(R.color.channel_bg));
        editText_gv_cf.setTextColor(getResources().getColor(R.color.channel_bg));
        editText_gra_add.setTextColor(getResources().getColor(R.color.channel_bg));
        editText_gra_sub.setTextColor(getResources().getColor(R.color.channel_bg));
        switch (i){
            case Config.SITTING_SELECT_TPWAY:
                textView_TPway.setBackgroundColor(getResources().getColor(R.color.skyblue));
                textView_TPway.setTextColor(Color.WHITE);
                radioGroup_TPway.setVisibility(VISIBLE);
                break;
            case Config.SITTING_SELECT_VDSTREAM:
                switch_vdStream.setBackgroundColor(getResources().getColor(R.color.skyblue));
                switch_vdStream.setTextColor(Color.WHITE);
                break;
            case Config.SITTING_SELECT_AUTO_BT:
                switch_autoNetBt.setBackgroundColor(getResources().getColor(R.color.skyblue));
                switch_autoNetBt.setTextColor(Color.WHITE);
                break;
            case Config.SITTING_SELECT_AUTO_WIFI:
                switch_autoNetWifi.setBackgroundColor(getResources().getColor(R.color.skyblue));
                switch_autoNetWifi.setTextColor(Color.WHITE);
                break;
            case Config.SITTING_SELECT_MANUAL_ORDER:
                textView_sitting_muanualOrder.setBackgroundColor(getResources().getColor(R.color.skyblue));
                textView_sitting_muanualOrder.setTextColor(Color.WHITE);
                linearLayout_sitting_manual_order.setVisibility(VISIBLE);
                break;
            case Config.SITTING_SELECT_SEEKBARVALUE:
                textView_sitting_seek.setTextColor(Color.WHITE);
                textView_sitting_seek.setBackgroundColor(getResources().getColor(R.color.skyblue));
                layout_sitting_seekbarMaxmin.setVisibility(VISIBLE);
                editText_sb_max.setTextColor(getResources().getColor(R.color.channel_bg));
                editText_sb_min.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
            case Config.SITTING_SELECT_GRAVITYMODE:
                textView_sitting_gm.setTextColor(Color.WHITE);
                textView_sitting_gm.setBackgroundColor(getResources().getColor(R.color.skyblue));
                spinner_aler_sensorvalue.setVisibility(VISIBLE);
                break;
            case Config.SITTING_SELECT_GRAVITYVALUE:
                textView_sitting_gv.setTextColor(Color.WHITE);
                textView_sitting_gv.setBackgroundColor(getResources().getColor(R.color.skyblue));
                layout_sitting_SensorMaxmin.setVisibility(VISIBLE);
                editText_gv_max.setTextColor(getResources().getColor(R.color.channel_bg));
                editText_gv_min.setTextColor(getResources().getColor(R.color.channel_bg));
                editText_gv_cf.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
            case Config.SITTING_SELECT_EXIT:
                button_exit.setBackgroundColor(getResources().getColor(R.color.skyblue));
                button_exit.setTextColor(Color.WHITE);
                break;
            case Config.SITTING_SELECT_GRAVITYNUM:
                textView_sitting_gn.setTextColor(Color.WHITE);
                textView_sitting_gn.setBackgroundColor(getResources().getColor(R.color.skyblue));
                layout_sitting_GnMaxmin.setVisibility(VISIBLE);
                editText_gra_add.setTextColor(getResources().getColor(R.color.channel_bg));
                editText_gra_sub.setTextColor(getResources().getColor(R.color.channel_bg));
                break;
        }
    }


    private void createViews(View view){

        button_exit= (Button) view.findViewById(R.id.button_sitting_exit);
        button_save_manual_order=(Button)view.findViewById(R.id.button_save_order_sitting);
        spinner_aler_sensorvalue=(Spinner)view.findViewById(R.id.spinner_gravity_mode);
        switch_vdStream=(Switch)view.findViewById(R.id.switch_sitting_video);
        switch_autoNetBt=(Switch)view.findViewById(R.id.switch_sitting_auto_net_BT);
        switch_autoNetWifi=(Switch)view.findViewById(R.id.switch_sitting_auto_net_wifi);
        linearLayout_sitting_manual_order=(LinearLayout)view.findViewById(R.id.layout_sitting_maunal_order);
        layout_sitting_seekbarMaxmin=(LinearLayout)view.findViewById(R.id.layout_sitting_seekbarMaxmin);
        layout_sitting_SensorMaxmin=(LinearLayout)view.findViewById(R.id.layout_sitting_gravityMaxMin);
        layout_sitting_GnMaxmin=(LinearLayout)view.findViewById(R.id.layout_sitting_GnMaxMin);
        textView_sitting_seek=(TextView)view.findViewById(R.id.textView_sitting_seekbar);
        textView_sitting_muanualOrder=(TextView)view.findViewById(R.id.textView_sitting_manualOrder);
        textView_sitting_gm=(TextView)view.findViewById(R.id.textView_sitting_gravitymode);
        textView_sitting_gv=(TextView)view.findViewById(R.id.textView_sitting_gravityvalue);
        textView_sitting_tip=(TextView)view.findViewById(R.id.textView_sitting_tip);
        textView_sitting_gn=(TextView)view.findViewById(R.id.textView_sitting_gravityInt);
        textView_TPway=(TextView)view.findViewById(R.id.textView_sitting_TransportWay);
        editText_sitting_manual_armbs=(EditText)view.findViewById(R.id.editText_sitting_armbs);
        editText_sitting_manual_armms=(EditText)view.findViewById(R.id.editText_sitting_armms);
        editText_sitting_manual_armts=(EditText)view.findViewById(R.id.editText_sitting_armts);
        editText_sitting_manual_auto=(EditText)view.findViewById(R.id.editText_sitting_auto);
        editText_sitting_manual_b=(EditText)view.findViewById(R.id.editText_sitting_B);
        editText_sitting_manual_f=(EditText)view.findViewById(R.id.editText_sitting_F);
        editText_sitting_manual_grab=(EditText)view.findViewById(R.id.editText_sitting_grab);
        editText_sitting_manual_hands=(EditText)view.findViewById(R.id.editText_sitting_hands);
        editText_sitting_manual_l=(EditText)view.findViewById(R.id.editText_sitting_L);
        editText_sitting_manual_ptbs=(EditText)view.findViewById(R.id.editText_sitting_ptbs);
        editText_sitting_manual_ptts=(EditText)view.findViewById(R.id.editText_sitting_ptts);
        editText_sitting_manual_r=(EditText)view.findViewById(R.id.editText_sitting_R);
        editText_sitting_manual_s=(EditText)view.findViewById(R.id.editText_sitting_S);
        editText_sitting_manual=(EditText)view.findViewById(R.id.editText_sitting_Manual);
        editText_sitting_interate=(EditText)view.findViewById(R.id.editText_sitting_Interate);
        editText_sitting_gravity=(EditText)view.findViewById(R.id.editText_sitting_Gravity);
        editText_sitting_reset=(EditText)view.findViewById(R.id.editText_sitting_Reset);
        editText_sb_max=(EditText)view.findViewById(R.id.editText_sitting_seekbarMax);
        editText_sb_min=(EditText)view.findViewById(R.id.editText_sitting_seekbarMin);
        editText_gv_max=(EditText)view.findViewById(R.id.editText_sitting_gravityMax);
        editText_gv_min=(EditText)view.findViewById(R.id.editText_sitting_gravityMin);
        editText_gv_cf=(EditText)view.findViewById(R.id.editText_sitting_char_gravity_flag);
        editText_gra_add=(EditText)view.findViewById(R.id.editText_sitting_gra_add);
        editText_gra_sub=(EditText)view.findViewById(R.id.editText_sitting_gra_sub);
        radioGroup_TPway=(RadioGroup)view.findViewById(R.id.radioGroup_transportWay);
    }

}
