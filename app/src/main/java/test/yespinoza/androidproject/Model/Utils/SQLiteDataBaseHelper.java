package test.yespinoza.androidproject.Model.Utils;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    private static SQLiteDataBaseHelper oDataBase;

    private SQLiteDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static void initiatetInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        oDataBase = new SQLiteDataBaseHelper(context, name, factory, version);
    }

    public static SQLiteDataBaseHelper getInstance() {
        return oDataBase;
    }

    public static int getLastIdTable(String tableName, boolean closeSession) {
        try {
            Cursor oCursor = oDataBase.getReadableDatabase().rawQuery("select max(id) from " + tableName, null);

            if (oCursor.moveToFirst()) {
                return oCursor.getInt(0);
            }
        } catch (Exception oException) {
            throw oException;
        }finally {
            try {
                if(closeSession)
                    oDataBase.getReadableDatabase().close();
            } catch (Exception oException) {

            }
        }
        return 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User (id int primary key, idNumber text, name text, lastName text, password text, email text, phone text, dateOfBirth text, address text)");
        //db.execSQL("create table producto (codigo int primary key, descripcion text, precio real)");
        //db.execSQL("create table deseos (id int, codigo int, cantidad int, primary key (id,codigo))");
    }

    //Se ejecuta sólo si se cambia la versión de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table User (id int primary key, fullName text, password text, email text, phone text, dateOfBirth text, address text)");
        //db.execSQL("create table producto (codigo int primary key, descripcion text, precio real)");
        //db.execSQL("create table deseos (id int primary key, codigo int primary key, cantidad int)");
    }
}
