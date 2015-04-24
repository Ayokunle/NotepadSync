package shopping.list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseWrapper extends SQLiteOpenHelper {

	public static final String shoppingList = "shoppingList";
    public static final String access_code = "_id";
    public static final String list = "_name";
    
    private static final String DATABASE_NAME = "shoppingList.db";
    private static final int DATABASE_VERSION = 1;
    
    // creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + "shoppingList"
            + "(" + access_code + " text primary key, "
            + list + " text );" ;
	
    public DataBaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you should do some logging in here
        // ..
        //db.execSQL("DROP TABLE IF EXISTS " + shoppingList);
        //onCreate(db);
    }
}
