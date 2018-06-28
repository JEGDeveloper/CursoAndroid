package test.yespinoza.androidproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import test.yespinoza.androidproject.Activity.Login;
import test.yespinoza.androidproject.Model.User;

public class UserAdapter {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserAdapter.currentUser = currentUser;
    }

    public static void updateUserPreference(Context pContext){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("UserName", currentUser.getId());
        myEditor.putString("Password", currentUser.getPassword());
        myEditor.commit();
    }
}
