package test.yespinoza.androidproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import test.yespinoza.androidproject.Model.Entity.User;

public class Project extends Application {

    public static Project instance;
    private Activity currentActivity;
    private User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Project getInstance() {
        if(instance == null)
            instance = new Project();
        return instance;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User pCurrentUser) {
        currentUser = pCurrentUser;
    }
    public void updateUserPreference(Context pContext){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("UserName", currentUser.getUserName());
        myEditor.putString("Password", currentUser.getPassword());
        myEditor.commit();
    }
}
