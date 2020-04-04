package com.example.openeyes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class RecordDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECORD = "create table RecordItem(" +
            "imageUrl text," +
            "headIconUrl text," +
            "title text," +
            "authorName text," +
            "tag text," +
            "playUrl text," +
            "videoDescription text," +
            "authorDescription text," +
            "backgroundUrl text)";
//    private Context mContext;
    public RecordDatabaseHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
//        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECORD);
//        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
