package student.inti.a202sgi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserProfile.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_AGE = "age";
    public static final String COL_GENDER = "gender";
    public static final String COL_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_AGE + " TEXT, " +
                COL_GENDER + " TEXT, " +
                COL_DESCRIPTION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean updateUserProfile(String name, String age, String gender, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_AGE, age);
        values.put(COL_GENDER, gender);
        values.put(COL_DESCRIPTION, description);

        long result = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{"1"}); // Assuming single user
        return result != -1;
    }

    public Cursor getUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, COL_ID + " = ?", new String[]{"1"}, null, null, null);
    }
}

