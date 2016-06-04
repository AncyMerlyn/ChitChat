package com.usimedia.chitchat.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.usimedia.chitchat.model.ChatContacts;
import com.usimedia.chitchat.model.Column;

import java.text.SimpleDateFormat;

/**
 * Created by USI IT on 6/4/2016.
 */
public class LocalContacts extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chitchat";
    private static final String TABLE_NAME ="contacts";
    private static final int VERSION = 1;
    private static final Column ID;
    private static final Column EMAIL;
    private static final Column NAME;
    private static final Column STATUS;
    private static final Column LASTSEEN;
    private static final String SPACE =" ";


    static {
        ID = new Column("_id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        EMAIL = new Column("email", "TEXT");
        NAME = new Column("name", "TEXT");
        STATUS = new Column("status", "TEXT");
        LASTSEEN = new Column("lastSeen", "TEXT");
    }

    private void createTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS".concat(SPACE).concat(TABLE_NAME).concat(SPACE)
                        .concat("(").concat(SPACE).concat(ID.getName()).concat(SPACE).concat(ID.getType())
                        .concat(",").concat(SPACE).concat(EMAIL.getName()).concat(SPACE).concat(EMAIL.getType())
                        .concat(",").concat(SPACE).concat(NAME.getName()).concat(SPACE).concat(NAME.getType())
                        .concat(",").concat(SPACE).concat(STATUS.getName()).concat(SPACE).concat(STATUS.getType())
                        .concat(",").concat(SPACE).concat(LASTSEEN.getName()).concat(SPACE).concat(LASTSEEN.getType())
                        .concat(")")
        );

    }

    private void dropTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS"+SPACE+TABLE_NAME+";");
    }

    private void insertChatContacts(ChatContacts contact){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EMAIL.getName(),contact.getEmail());
        cv.put(NAME.getName(),contact.getName());
        cv.put(STATUS.getName(),contact.getStatus());
        cv.put(LASTSEEN.getName(),dateformat.format(contact.getLastSeen()));
        db.insert(TABLE_NAME,null,cv);
    }

    private Cursor getAllContacts(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT"+SPACE+"*"+SPACE+"FROM"+SPACE+TABLE_NAME+";";
        return db.rawQuery(query,null);
    }

    public LocalContacts(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropTable(db);
            createTable(db);
    }



}
