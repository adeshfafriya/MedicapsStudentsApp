package in.ac.medicaps.medicapsstudentsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "libinfo.db";
    public static final String DATABASE_TABLE = "info_table";
    public static final String COL_6 = "ID";
    public static final String COL_1 = "book_id";
    public static final String COL_2 = "book_name";
    public static final String COL_3 = "issue_date";
    public static final String COL_4 = "return_date";
    public static final String COL_5 = "reminder_date";
    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME,  null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table" + DATABASE_TABLE "(book_id INTEGER, book_name TEXT, issue_date INTEGER, return_date INTEGER, reminder_dste INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE );
        onCreate(db);

    }
    public boolean insertdata (String book_id, String book_name, String issue_date, String return_date, String reminder_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, book_id);
        contentValues.put(COL_2, book_name);
        contentValues.put(COL_3, issue_date);
        contentValues.put(COL_4, return_date);
        contentValues.put(COL_5, reminder_date);
        long result = db.insert(DATABASE_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;


    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE,null);
        return res;
    }

    public boolean updateData(String book_id, String book_name, String issue_date, String return_date, String reminder_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, book_id);
        contentValues.put(COL_2, book_name);
        contentValues.put(COL_3, issue_date);
        contentValues.put(COL_4, return_date);
        contentValues.put(COL_5, reminder_date);
        db.update(DATABASE_TABLE, contentValues, "id = ?", new String[]{id} );
        return true;


    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATABASE_TABLE ,"ID = ?", new String[] {id});


    }
}