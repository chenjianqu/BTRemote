package com.qu.jian.btremote;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by Jianqv on 2017/8/27.
 */

public class Config{
    public static final String APP_ID="com.jian.qu.btremote";
    public static final String MESSAGE_INTERACT = "message_interact";

    public static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int CHANNEL_AUTO=1000;
    public static final int CHANNEL_MANUAL=1001;
    public static final int CHANNEL_GRAVITY=1002;
    public static final int CHANNEL_RESET=1003;
    public static final int CHANNEL_SITTING=1004;

    public static final int FUNCTION_AUTO=2000;
    public static final int FUNCTION_GRAB=2001;
    public static final int FUNCTION_HANDS=2002;
    public static final int FUNCTION_ARMTS=2003;
    public static final int FUNCTION_ARMMS=2004;
    public static final int FUNCTION_ARMBS=2005;
    public static final int FUNCTION_PTTS=2006;
    public static final int FUNCTION_PTBS=2007;
    public static final int FUNCTION_F=2008;
    public static final int FUNCTION_B=2009;
    public static final int FUNCTION_S=2011;
    public static final int FUNCTION_L=2012;
    public static final int FUNCTION_R=2013;
    public static final int FUNCTION_ADD=2014;
    public static final int FUNCTION_SUB=2015;
    public static final int FUNCTION_NULL=-1;

    public static final int SITTING_SELECT_VDSTREAM = 3000;
    public static final int SITTING_SELECT_MANUAL_ORDER = 3001;
    public static final int SITTING_SELECT_SEEKBARVALUE =3002 ;
    public static final int SITTING_SELECT_GRAVITYMODE = 3003;
    public static final int SITTING_SELECT_GRAVITYVALUE = 3004;
    public static final int SITTING_SELECT_EXIT =3005 ;
    public static final int SITTING_SELECT_AUTO_BT = 3006;
    public static final int SITTING_SELECT_AUTO_WIFI = 3007;
    public static final int SITTING_SELECT_TPWAY = 3008;
    public static final int SITTING_SELECT_GRAVITYNUM =3009 ;

    public static final int SELECTED_BUTTON_CLOSE=4000;
    public static final int SELECTED_BUTTON_SEARCH=4001;
    public static final int SELECTED_BUTTON_BLUETOOTH=4002;
    public static final int SELECTED_BUTTON_WIFIDIRECT=4003;
    public static final int SELECTED_BUTTON_SENSOR =4004 ;

    public static final String IS_WHAT_BT="isBt";
    public static final String IS_WHAT_VD="isVd";

    public static final String KEY_SITTING_AUTO="auto";
    public static final String KEY_SITTING_GRAB="grab";
    public static final String KEY_SITTING_HAND="hands";
    public static final String KEY_SITTING_ARMTS="armts";
    public static final String KEY_SITTING_ARMMS="armms";
    public static final String KEY_SITTING_ARMBS="armbs";
    public static final String KEY_SITTING_PTTS="ptts";
    public static final String KEY_SITTING_PTBS="ptbs";
    public static final String KEY_SITTING_F="f";
    public static final String KEY_SITTING_L="l";
    public static final String KEY_SITTING_R="r";
    public static final String KEY_SITTING_S="s";
    public static final String KEY_SITTING_B="b";
    public static final String KEY_SITTING_SB_MAX="sb_max";
    public static final String KEY_SITTING_SB_MIN="sb_min";
    public static final String KEY_SITTING_GV_MAX="gv_max";
    public static final String KEY_SITTING_GV_MIN="gv_min";
    public static final String KEY_SITTING_GV_CF ="gv_cf" ;
    public static final String KEY_SITTING_GN_CF ="gn_cf" ;//加减按键的标识符
    //public static final String KEY_SITTING_GN_MAX ="gn_max" ;//加减按键的最大值
    //public static final String KEY_SITTING_GN_MIN ="gn_min" ;
    public static final String KEY_SITTING_GRA_ADD="gra_add";//加减按键的键值
    public static final String KEY_SITTING_GRA_SUB="gra_sub";//加减按键的键值
    public static final String KEY_SITTING_VDSTREAM="vdstream";
    public static final String KEY_SITTING_GRAVITY_MODE="gravity_Mode";
    public static final String KEY_SITTING_AUTONET_WIFI ="auto_net_wifi" ;
    public static final String KEY_SITTING_AUTONET_BT ="auto_net_bt" ;
    public static final String KEY_SITTING_TPWAY = "transport_way";
    public static final String KEY_IF_FIRSTTIME = "if_firsttime";
    public static final String KEY_SITTING_MANUAL ="manual_order" ;
    public static final String KEY_SITTING_INTERATE ="interate_order" ;
    public static final String KEY_SITTING_GRAVITY ="gravity_order" ;
    public static final String KEY_SITTING_RESET ="reset_order" ;

    public static final String KEY_ADRESS_BT="adress_bt";
    public static final String KEY_NAME_BT ="name_bt" ;
    public static final String KEY_ADRESS_WD="adress_wd";

    public static final int FLAG_TPWAY_ONLYWIFID =1 ;
    public static final int FLAG_TPWAY_WIFIDANDBT =0 ;

    public static final int FLAG_ON =1 ;
    public static final int FLAG_OFF =0 ;

    public static final String[] ARRAY_GRAVITY_AXIS={"X轴","Y轴","Z轴"};
    public static final int AXIS_X =0 ;
    public static final int AXIS_Y =1 ;
    public static final int AXIS_Z =2 ;

    public static final float G_MAX= (float) 9.80;
    public static final float G_MIN= (float)-9.80;


    public static final String TPWAY_BT_AND_WD="tpway_btandwd";
    public static final String TPWAY_ONLY_WD="tpway_onlywd";

    public static final char SEND_MODE_ADD='a';
    public static final char SEND_MODE_SUB='s';


    public static final String BD_SITTING_GRAVITY_MO ="com.qu.jian.btremote.broadcast_sitting_gravityMde" ;
    public static final String BD_SITTING_SB_MAX ="com.qu.jian.btremote.broadcast_sitting_sb_max" ;
    public static final String BD_SITTING_SB_MIN ="com.qu.jian.btremote.broadcast_sitting_sb_min"  ;
    public static final String BD_SITTING_GV_MAX ="com.qu.jian.btremote.broadcast_sitting_gv_max" ;
    public static final String BD_SITTING_GV_MIN ="com.qu.jian.btremote.broadcast_sitting_gv_min" ;
    public static final String BD_SITTING_GV_CF ="com.qu.jian.btremote.broadcast_sitting_gv_cf" ;
    //public static final String BD_SITTING_GN_CF ="com.qu.jian.btremote.broadcast_sitting_gn_cf" ;
    //public static final String BD_SITTING_GN_MIN ="com.qu.jian.btremote.broadcast_sitting_gn_min" ;
    //public static final String BD_SITTING_GN_MAX ="com.qu.jian.btremote.broadcast_sitting_gn_max" ;
    public static final String BD_SITTING_GRA_ADD ="com.qu.jian.btremote.broadcast_sitting_gra_add" ;
    public static final String BD_SITTING_GRA_SUB ="com.qu.jian.btremote.broadcast_sitting_gra_sub" ;
    public static final String BD_SITTING_MANUAL_AUTO ="com.qu.jian.btremote.broadcast_sitting_manual_auto" ;
    public static final String BD_SITTING_MANUAL_GRAB = "com.qu.jian.btremote.broadcast_sitting_manual_grab";
    public static final String BD_SITTING_MANUAL_HAND = "com.qu.jian.btremote.broadcast_sitting_manual_hand";
    public static final String BD_SITTING_MANUAL_ARMTS = "com.qu.jian.btremote.broadcast_sitting_manual_armts";
    public static final String BD_SITTING_MANUAL_ARMMS = "com.qu.jian.btremote.broadcast_sitting_manual_armms";
    public static final String BD_SITTING_MANUAL_ARMBS = "com.qu.jian.btremote.broadcast_sitting_manual_armbs";
    public static final String BD_SITTING_MANUAL_PTTS = "com.qu.jian.btremote.broadcast_sitting_manual_ptts";
    public static final String BD_SITTING_MANUAL_PTBS = "com.qu.jian.btremote.broadcast_sitting_manual_ptbs";
    public static final String BD_SITTING_MANUAL_F = "com.qu.jian.btremote.broadcast_sitting_manual_a";
    public static final String BD_SITTING_MANUAL_B = "com.qu.jian.btremote.broadcast_sitting_manual_b";
    public static final String BD_SITTING_MANUAL_S = "com.qu.jian.btremote.broadcast_sitting_manual_s";
    public static final String BD_SITTING_MANUAL_L = "com.qu.jian.btremote.broadcast_sitting_manual_l";
    public static final String BD_SITTING_MANUAL_R = "com.qu.jian.btremote.broadcast_sitting_manual_r";
    public static final String BD_SITTING_MANUAL = "com.qu.jian.btremote.broadcast_sitting_manual";
    public static final String BD_SITTING_GRAVITY = "com.qu.jian.btremote.broadcast_sitting_gravity";
    public static final String BD_SITTING_INTERATE = "com.qu.jian.btremote.broadcast_sitting_interate";
    public static final String BD_SITTING_RESET = "com.qu.jian.btremote.broadcast_sitting_reset";
    public static final String BD_TPWAY ="com.qu.jian.btremote.broadcast_sitting_tpway" ;
    public static final String BD_AUTONET_WIFI ="com.qu.jian.btremote.broadcast_sitting_autonet_wifi" ;
    public static final String BD_AUTONET_BT ="com.qu.jian.btremote.broadcast_sitting_autonet_bt" ;
    public static final String BD_VDSTREAM ="com.qu.jian.btremote.broadcast_sitting_vdstream" ;
    public static final String BD_BTOK ="com.qu.jian.btremote.broadcast_bd_bt_ok" ;
    public static final String BD_TEST = "com.qu.jian.btremote.broadcast_bd_text";


    public static void cacheStringConfig(Context context, String KEY,String content){
        SharedPreferences.Editor editor=context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putString(KEY,content);
        editor.commit();
    }
    public static String getCacheStringConfig(Context context,String KEY){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getString(KEY,null);
    }
    public static void cacheFloatConfig(Context context, String KEY,float content){
        SharedPreferences.Editor editor=context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY,content);
        editor.commit();
    }
    public static float getCacheFloatConfig(Context context, String KEY){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getFloat(KEY, (float) -1);
    }

    public static void cacheBooleanConfig(Context context, String KEY,boolean content){
        SharedPreferences.Editor editor=context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY,content);
        editor.commit();
    }
    public static boolean getCacheBooleanConfig(Context context, String KEY){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getBoolean(KEY,false);
    }
    public static void cacheIntConfig(Context context, String KEY,int content){
        SharedPreferences.Editor editor=context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putInt(KEY,content);
        editor.commit();
    }
    public static int getCacheIntConfig(Context context, String KEY){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getInt(KEY,0);
    }

    public static void forStartAPP(Context context){
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_TPWAY)==null)Config.cacheStringConfig(context,KEY_SITTING_TPWAY,TPWAY_BT_AND_WD);
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_ARMBS)==null)Config.cacheStringConfig(context,KEY_SITTING_ARMBS,"r");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_ARMMS)==null)Config.cacheStringConfig(context,KEY_SITTING_ARMMS,"q");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_HAND)==null)Config.cacheStringConfig(context,KEY_SITTING_HAND,"o");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_ARMTS)==null)Config.cacheStringConfig(context,KEY_SITTING_ARMTS,"p");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_PTTS)==null)Config.cacheStringConfig(context,KEY_SITTING_PTTS,"s");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_PTBS)==null)Config.cacheStringConfig(context,KEY_SITTING_PTBS,"t");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_AUTO)==null)Config.cacheStringConfig(context,KEY_SITTING_AUTO,"a");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_GRAB)==null)Config.cacheStringConfig(context,KEY_SITTING_GRAB,"j");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_B)==null)Config.cacheStringConfig(context,KEY_SITTING_B,"i");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_F)==null)Config.cacheStringConfig(context,KEY_SITTING_F,"h");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_S)==null)Config.cacheStringConfig(context,KEY_SITTING_S,"g");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_L)==null)Config.cacheStringConfig(context,KEY_SITTING_L,"e");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_R)==null)Config.cacheStringConfig(context,KEY_SITTING_R,"f");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_MANUAL)==null)Config.cacheStringConfig(context,KEY_SITTING_MANUAL,"b");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_RESET)==null)Config.cacheStringConfig(context,KEY_SITTING_RESET,"c");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_INTERATE)==null)Config.cacheStringConfig(context,KEY_SITTING_INTERATE,"f");
        if(Config.getCacheStringConfig(context,Config.KEY_SITTING_GRAVITY)==null)Config.cacheStringConfig(context,KEY_SITTING_GRAVITY,"d");
        if(!Config.getCacheBooleanConfig(context,Config.KEY_IF_FIRSTTIME)){
            Config.cacheFloatConfig(context,Config.KEY_SITTING_SB_MAX, (float) 100.0);
            Config.cacheFloatConfig(context,Config.KEY_SITTING_SB_MIN, (float) 0.0);
            Config.cacheFloatConfig(context,Config.KEY_SITTING_GV_MAX, (float) 100.0);
            Config.cacheFloatConfig(context,Config.KEY_SITTING_GV_MIN, (float) 0.0);
            Config.cacheStringConfig(context,Config.KEY_SITTING_GRA_ADD,"m");
            Config.cacheStringConfig(context,Config.KEY_SITTING_GRA_SUB,"n");
            //Config.cacheFloatConfig(context,Config.KEY_SITTING_GN_MIN, (float) 0.0);
            //Config.cacheFloatConfig(context,Config.KEY_SITTING_GN_MAX, (float) 100.0);
            Config.cacheBooleanConfig(context,Config.KEY_IF_FIRSTTIME,true);
        }
    }
}
