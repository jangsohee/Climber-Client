package dwg.climber.oil_climber;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class FollowingDB extends SQLiteOpenHelper {
    public FollowingDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE followings(id INTEGER PRIMARY KEY);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public Integer[] getCelebList() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> celebList = new ArrayList<Integer>();

        Cursor cursor = db.rawQuery("select * from followings", null);
        while(cursor.moveToNext()) {
            celebList.add(cursor.getInt(0));
        }

        Integer[] celebArray = new Integer[celebList.size()];
        celebArray = (Integer[])celebList.toArray(celebArray);

        return celebArray;
    }
}
