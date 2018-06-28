package test.yespinoza.androidproject.Model.DAL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import test.yespinoza.androidproject.Model.DAO.SQLiteDataBaseHelper;
import test.yespinoza.androidproject.Model.User;

public class UserDAL {

    public static User userValidate(User pUSer) {
        SQLiteDatabase oSQLiteDatabase = SQLiteDataBaseHelper.getInstance().getReadableDatabase();
        try {
            User oUser;
            Cursor oCursor = oSQLiteDatabase.rawQuery("select idNumber, name, lastName, password, email, phone, dateOfBirth, address from User where idNumber=" + pUSer.getUserName()+ " and password = "+ pUSer.getPassword(), null);

            if (oCursor.moveToFirst()) {
                oUser = new User();
                oUser.setId(oCursor.getString(0));
                oUser.setName(oCursor.getString(1));
                oUser.setLastName(oCursor.getString(2));
                oUser.setPassword(oCursor.getString(3));
                oUser.setEmail(oCursor.getString(4));
                oUser.setPhone(oCursor.getString(5));
                oUser.setDateOfBirth(oCursor.getString(6));
                oUser.setAddress(oCursor.getString(7));
                return oUser;
            }
        } catch (Exception oException) {

        } finally {
            try {
                oSQLiteDatabase.close();
            } catch (Exception oException) {

            }
        }
        return null;
    }


    public static User consultarUser(int pId) {
        SQLiteDataBaseHelper oSQLiteDataBaseHelper = SQLiteDataBaseHelper.getInstance();
        SQLiteDatabase oSQLiteDatabase = oSQLiteDataBaseHelper.getReadableDatabase();
        try {
            User oUser;
            Cursor oCursor = oSQLiteDatabase.rawQuery("select idNumber, name, lastName, password, email, phone, dateOfBirth, address from User where idNumber=" + pId, null);

            if (oCursor.moveToFirst()) {
                oUser = new User();
                oUser.setUserName(oCursor.getString(0));
                oUser.setId(oUser.getUserName());
                oUser.setName(oCursor.getString(1));
                oUser.setLastName(oCursor.getString(2));
                oUser.setPassword(oCursor.getString(3));
                oUser.setEmail(oCursor.getString(4));
                oUser.setPhone(oCursor.getString(5));
                oUser.setDateOfBirth(oCursor.getString(6));
                oUser.setAddress(oCursor.getString(7));
                return oUser;
            }
        } catch (Exception oException) {
            throw oException;
        } finally {
            try {
                oSQLiteDatabase.close();
            } catch (Exception oException) {

            }
        }
        return null;
    }

    public static boolean insertarUser(User pUser) {
        boolean isSuccess = false;
        SQLiteDataBaseHelper oSQLiteDataBaseHelper = SQLiteDataBaseHelper.getInstance();
        SQLiteDatabase oSQLiteDatabase = oSQLiteDataBaseHelper.getWritableDatabase();
        try {
            ContentValues registro = new ContentValues();
            registro.put("id",SQLiteDataBaseHelper.getLastIdTable("User", false)+1);
            registro.put("idNumber",pUser.getId());
            registro.put("name",pUser.getName());
            registro.put("lastName",pUser.getLastName());
            registro.put("password",pUser.getPassword());
            registro.put("email",pUser.getEmail());
            registro.put("phone",pUser.getPhone());
            registro.put("dateOfBirth",pUser.getDateOfBirth());
            registro.put("address",pUser.getAddress());
            oSQLiteDatabase.insert("User",null,registro);
            isSuccess =  true;
        } catch (Exception oException) {
            isSuccess = false;
        } finally {
            try {
                oSQLiteDatabase.close();
            } catch (Exception oException) {

            }
        }
        return isSuccess;
    }

    public static boolean  modificarUser(User pUser){
        boolean isSuccess = false;
        SQLiteDataBaseHelper oSQLiteDataBaseHelper = SQLiteDataBaseHelper.getInstance();
        SQLiteDatabase oSQLiteDatabase = oSQLiteDataBaseHelper.getWritableDatabase();
        try {
            ContentValues registro = new ContentValues();
            registro.put("idNumber",pUser.getId());
            registro.put("name",pUser.getName());
            registro.put("lastName",pUser.getLastName());
            registro.put("password",pUser.getPassword());
            registro.put("email",pUser.getEmail());
            registro.put("phone",pUser.getPhone());
            registro.put("dateOfBirth",pUser.getDateOfBirth());
            registro.put("address",pUser.getAddress());
            oSQLiteDatabase.insert("User",null,registro);
            int rowsAffected = oSQLiteDatabase.update("User", registro, "idNumber=" + pUser.getId(), null);
            isSuccess = rowsAffected >= 1;
        } catch (Exception oException) {
            throw oException;
        } finally {
            try {
                oSQLiteDatabase.close();
            } catch (Exception oException) {

            }
        }
        return isSuccess;
    }

    public static boolean eliminarUser(int pId){
        boolean isSuccess = false;
        SQLiteDataBaseHelper oSQLiteDataBaseHelper = SQLiteDataBaseHelper.getInstance();
        SQLiteDatabase oSQLiteDatabase = oSQLiteDataBaseHelper.getWritableDatabase();
        try {

            ContentValues registro = new ContentValues();
            registro.put("idNumber",pId);
            int rowsAffected = oSQLiteDatabase.delete("User","idNumber="+pId,null);
            isSuccess = rowsAffected >= 1;
        } catch (Exception oException) {
            throw oException;
        } finally {
            try {
                oSQLiteDatabase.close();
            } catch (Exception oException) {

            }
        }
        return isSuccess;
    }
}
