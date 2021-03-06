package woact.android.zhenik.pj.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String TAG = "DatabaseHelper:> ";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    protected static final String DATABASE_NAME = "bank";
    // Table Names
    protected static final String TABLE_USERS = "users";
    protected static final String TABLE_GROUPS = "groups";
    protected static final String TABLE_USER_GROUP = "user_group";
    protected static final String TABLE_INVITATIONS = "invitations";
    // Common column names
    protected static final String KEY_ID = "id";
    // USERS Table - column names
    protected static final String KEY_USER_NAME = "userName";
    protected static final String KEY_PASSWORD = "password";
    protected static final String KEY_FULL_NAME = "fullName";
    protected static final String KEY_MONEY = "money";
    protected static final String KEY_SCORE = "score";
    // GROUPS Table - column names
    protected static final String KEY_TOTAL_MONEY = "totalMoney";
    protected static final String KEY_AVAILABLE_MONEY = "availableMoney";
    protected static final String KEY_GROUP_NAME = "groupName";
    // USER_GROUP Table
    protected static final String KEY_USER_ID = "userId";
    protected static final String KEY_GROUP_ID = "groupId";
    protected static final String KEY_INVESTMENT = "investment";
    protected static final String KEY_LOAN = "loan";
    // INVITATIONS_TABLE
    protected static final String KEY_SEND_BY_ID = "sendBy";
    protected static final String KEY_RECEIVED_BY_ID = "receivedBy";
//    protected static final String KEY_GROUP_ID = "receivedBy";


    // users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_NAME + " TEXT NOT NULL UNIQUE,"
            + KEY_PASSWORD + " TEXT NOT NULL,"
            + KEY_FULL_NAME + " TEXT NOT NULL,"
            + KEY_MONEY + " REAL DEFAULT 0,"
            + KEY_SCORE + " INTEGER DEFAULT 0)";

    // group table create statement
    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE "
            + TABLE_GROUPS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TOTAL_MONEY + " REAL DEFAULT 0,"
            + KEY_AVAILABLE_MONEY  + " REAL DEFAULT 0,"
            + KEY_GROUP_NAME + " TEXT DEFAULT 'NONAME')";

    /**
     * CREATE TABLE suppliers (
     supplier_id integer PRIMARY KEY,
     supplier_name text NOT NULL,
     group_id integer NOT NULL,
     FOREIGN KEY (group_id) REFERENCES supplier_groups(group_id)
     );
     * */
    private static final String CREATE_TABLE_USER_GROUP = "CREATE TABLE "
            + TABLE_USER_GROUP + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_ID + " INTEGER NOT NULL,"
            + KEY_GROUP_ID + " INTEGER NOT NULL,"
            + KEY_INVESTMENT + " REAL DEFAULT 0,"
            + KEY_LOAN + " REAL DEFAULT 0,"
            + " FOREIGN KEY (" +KEY_USER_ID+") REFERENCES "+TABLE_USERS+"("+KEY_ID+"),"
            + " FOREIGN KEY (" +KEY_GROUP_ID+") REFERENCES "+TABLE_GROUPS+"("+KEY_ID+")"+")";

    // invitations table create statement
    private static final String CREATE_TABLE_INVITATIONS = "CREATE TABLE "
            + TABLE_INVITATIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_SEND_BY_ID + " INTEGER NOT NULL,"
            + KEY_RECEIVED_BY_ID + " INTEGER NOT NULL,"
            + KEY_GROUP_ID + " INTEGER NOT NULL,"
            + " FOREIGN KEY (" +KEY_SEND_BY_ID+") REFERENCES "+TABLE_USERS+"("+KEY_ID+"),"
            + " FOREIGN KEY (" +KEY_RECEIVED_BY_ID+") REFERENCES "+TABLE_USERS+"("+KEY_ID+"),"
            + " FOREIGN KEY (" +KEY_GROUP_ID+") REFERENCES "+TABLE_GROUPS+"("+KEY_ID+")"+")";
    /**
     * Synchronized singleton
     * */
    private static DatabaseHelper instance;
    private DatabaseHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_GROUPS);
        db.execSQL(CREATE_TABLE_USER_GROUP);
        db.execSQL(CREATE_TABLE_INVITATIONS);
        Log.d(TAG, "db was created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        Log.d(TAG, "drop tables, change vers from "+oldVersion+" to new "+newVersion);
        onCreate(db);
    }



//
//    // Deleting single contact
//    public void deleteUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int rowsDeleted = db.delete(TABLE_USERS, KEY_ID + " = ?",
//                  new String[] { String.valueOf(user.getId()) });
//        db.close();
////        Log.d(TAG, rowsDeleted+"");
//    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

}