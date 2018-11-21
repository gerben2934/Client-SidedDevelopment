package nl.ralphrouwen.hue.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import nl.ralphrouwen.hue.Models.Bridge;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, DataUtil.DB_NAME, null, DataUtil.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BRIDGE_TABLE = "CREATE TABLE " + DataUtil.TABLE_BRIDGES + "("
                + DataUtil.KEY_ID + " INTEGER PRIMARY KEY, " + DataUtil.KEY_NAME + " TEXT, "
                + DataUtil.KEY_IP + " TEXT, " +  DataUtil.KEY_TOKEN + " TEXT" + " )";

        db.execSQL(CREATE_BRIDGE_TABLE);

    }

    public boolean doesExist(SQLiteDatabase db, String tableName) {
        String DOES_EXIST_TABLE = "SELECT COUNT(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(DOES_EXIST_TABLE, null);
        int count = cursor.getCount();
        cursor.close();
        if(count == 0){
            return false;
        }
        return true;
    }

    public boolean checkIfEmpty()
    {
        if (getAllBridges().size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete (drop) the old version of the table
        db.execSQL("DROP TABLE IF EXISTS " + DataUtil.DB_NAME);

        // Create the new version of the table
        onCreate(db);
    }

    public void addBridge(Bridge bridge)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataUtil.KEY_NAME, bridge.getName());
        contentValues.put(DataUtil.KEY_IP, bridge.getIp());
        contentValues.put(DataUtil.KEY_TOKEN, bridge.getToken());
        db.insert(DataUtil.TABLE_BRIDGES, null, contentValues);
    }

    public Bridge getBridge(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DataUtil.TABLE_BRIDGES,
                new String[]{DataUtil.KEY_ID, DataUtil.KEY_NAME, DataUtil.KEY_IP, DataUtil.KEY_TOKEN},
                DataUtil.KEY_ID + "=?",
                new String[] {String.valueOf(id)},
                null,null,null,null
        );

        if(cursor != null)
            cursor.moveToFirst();

        Bridge bridge = new Bridge(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3));
        return bridge;
    }

    public ArrayList<Bridge> getAllBridges() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Bridge> bridges = new ArrayList<>();

        // select all contacts:
        String selectAll = "SELECT * FROM " + DataUtil.TABLE_BRIDGES;
        Cursor cursor = db.rawQuery(selectAll, null);
//        cursor.moveToFirst();

        // TODO: 23-Oct-17 uitzoeken wat de handigste manier is om alle rijen te lezen.
        if (cursor.moveToFirst()) {
            do {
                Bridge bridge = new Bridge(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                bridges.add(bridge);
            } while (cursor.moveToNext());

        }
        return bridges;
    }
}
