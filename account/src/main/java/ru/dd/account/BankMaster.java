package ru.dd.account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BankMaster extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "BANK";
    public static final String BANK_NAME = "NAME";
    public static final String BANK_SUM  = "SUM";

    public BankMaster(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + BANK_NAME + " TEXT NOT NULL, "
                + BANK_SUM + " INTEGER NOT NULL DEFAULT 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("BankMaster", "onUpgrade");
    }

    // метод вставки новго счета
    public static void insert (BankMaster dbBank, String tNameCreate) {
        SQLiteDatabase db = dbBank.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BANK_NAME, tNameCreate);
        db.insert(TABLE_NAME, null, cv);

        db.close();
    }

    // метод просмотра всех счетов
    public static String look (BankMaster dbBank){
        String str = "";
        SQLiteDatabase db = dbBank.getWritableDatabase();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex(BANK_NAME);
            int sumColIndex = c.getColumnIndex(BANK_SUM);

            do {
                str = str + c.getString(nameColIndex) + " - " + c.getFloat(sumColIndex) + "\n";
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return str;
    }

    // метод просмотра всех имен - вернёт false, если имя не повторяется
    public static boolean nameFind (BankMaster dbBank, String str){
        SQLiteDatabase db = dbBank.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex(BANK_NAME);
            do {
                if (str.equals(c.getString(nameColIndex))) {
                    c.close();
                    db.close();
                    return true;
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return false;
    }

    // метод удаления таблицы из БД
    public static void deleteAll (BankMaster dbBank){
        SQLiteDatabase db = dbBank.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    // метод удаления счёта
    public static void delete (BankMaster dbBank, String nameDelete){
        SQLiteDatabase db = dbBank.getWritableDatabase();
        db.delete(TABLE_NAME, BANK_NAME + " = ?", new String[] {nameDelete});
        db.close();
    }

    // метод перевода/поплнения счета
    public static boolean trans (BankMaster dbBank, String nameBank, float sumTrans){
        SQLiteDatabase db = dbBank.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, BANK_NAME + " = ?", new String[] {nameBank}, null, null, null);
        float sum = 0;
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex(BANK_SUM);
            do {
                sum = c.getFloat(nameColIndex);
            } while (c.moveToNext());
        }

        // недостаточно средств для транзакции
        if ((sum + sumTrans) < 0) {
            c.close();
            db.close();
            return false;
        }

        // обновляем счёт
        ContentValues cv = new ContentValues();
        cv.put(BANK_SUM, (sum + sumTrans));
        db.update(TABLE_NAME, cv, BANK_NAME + " = ?", new String[] {nameBank});

        c.close();
        db.close();
        return true;
    }
}
