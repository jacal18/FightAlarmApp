package com.fightalarm.fightalarm.providers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Abiwax on 16-01-01.
 */
public class OtherLocalStore {
    public static final String SP_NAME  ="otherDetails";
    SharedPreferences otherLocalDatabase;



    public OtherLocalStore(Context context){
        otherLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void setEmailStored(boolean emailShown){
        SharedPreferences.Editor spEditor = otherLocalDatabase.edit();
        spEditor.putBoolean("savedEmail", emailShown);
        spEditor.commit();
    }
    public boolean getEmailStored(){
        if(otherLocalDatabase.getBoolean("savedEmail",false) == true){
            return true;
        }
        else{
            return false;
        }
    }

    public void setUserInfo(String userInfo){
        SharedPreferences.Editor spEditor = otherLocalDatabase.edit();
        spEditor.putString("userInfo", userInfo);
        spEditor.commit();
    }
    public String getUserInfo(){
        String userInfo = otherLocalDatabase.getString("userInfo","");
        return userInfo;
    }

    public void setBadge(int badge){
        SharedPreferences.Editor spEditor = otherLocalDatabase.edit();
        spEditor.putInt("badgecount", badge);
        spEditor.commit();
    }
    public int getBadge(){
        int badge = otherLocalDatabase.getInt("badgecount",0);
        return badge;
    }


    public void setDescription(String description){
        SharedPreferences.Editor spEditor = otherLocalDatabase.edit();
        spEditor.putString("alaramdescription", description);
        spEditor.commit();
    }
    public String getDescription(){
        String description = otherLocalDatabase.getString("alaramdescription","A fight you subscribed to has started.");
        return description;
    }

}
