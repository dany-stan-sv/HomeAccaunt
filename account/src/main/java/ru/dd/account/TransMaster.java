package ru.dd.account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransMaster extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "TRANSACT_STORY";
    public static final String TRANS_TIME = "TIME";
    public static final String BANK_NAME = "BANK";
    public static final String TRANS_SIGN  = "SIGN";
    public static final String TRANS_SUM  = "SUM";
    public static final String TRANS_CY  = "CURRENCY";
    public static final String TRANS_COMMENT  = "COMMENT";

    public TransMaster(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + TRANS_TIME + " TEXT, "
                + BANK_NAME + " TEXT NOT NULL, "
                + TRANS_SIGN + " TEXT NOT NULL DEFAULT '+', "
                + TRANS_SUM + " FLOAT NOT NULL DEFAULT 0, "
                + TRANS_CY + " TEXT NOT NULL, "
                + TRANS_COMMENT + " TEXT DEFAULT 'нет');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TransMaster", "onUpgrade");
    }

    // метод вставки новой транзакции
    public static void insert (TransMaster dbTrans, String nameBank, String sign,
                               float sum, String currency, String comment) {

        SQLiteDatabase db = dbTrans.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM.dd.yy - hh:mm");

        cv.put(TRANS_TIME, format.format(date));
        cv.put(BANK_NAME, nameBank);
        cv.put(TRANS_SIGN, sign);
        cv.put(TRANS_SUM, sum);
        cv.put(TRANS_CY, currency);
        cv.put(TRANS_COMMENT, comment);

        db.insert(TABLE_NAME, null, cv);

        db.close();
    }

    // метода просмотра истории транзакций
    public static String look (TransMaster dbBank){
        String str = "";
        SQLiteDatabase db = dbBank.getWritableDatabase();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int timeColIndex = c.getColumnIndex(TRANS_TIME);
            int nameColIndex = c.getColumnIndex(BANK_NAME);
            int signColIndex = c.getColumnIndex(TRANS_SIGN);
            int sumColIndex = c.getColumnIndex(TRANS_SUM);
            int cyColIndex = c.getColumnIndex(TRANS_CY);
            int commentColIndex = c.getColumnIndex(TRANS_COMMENT);

            do {
                str = str   + c.getString(timeColIndex) + "  |  счёт "
                            + c.getString(nameColIndex) + "  |  "
                            + c.getString(signColIndex) + " "
                            + c.getFloat(sumColIndex) + " "
                            + c.getString(cyColIndex) + ".\ncomment: "
                            + c.getString(commentColIndex) + "\n ";

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return str;
    }

    // метод удаления таблицы из БД
    public static void clear (TransMaster dbBank){
        SQLiteDatabase db = dbBank.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
