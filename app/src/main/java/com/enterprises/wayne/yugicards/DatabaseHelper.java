package com.enterprises.wayne.yugicards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 7/31/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    /* constants */
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "yugy.data";
    public static final int DATABASE_VERSION = 7;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        String createSql =
                "CREATE TABLE " + CardContract.CardEntry.TABLE_NAME + "(\n" +
                         CardContract.CardEntry._ID + " TEXT PRIMARY KEY\n" +
                        ", " + CardContract.CardEntry.COLOUMN_DESCRIPTION + " TEXT \n" +
                        ", " + CardContract.CardEntry.COLOUMN_TYPE + " TEXT \n" +
                        ", " + CardContract.CardEntry.COLOUMN_IMAGE_URL + " TEXT\n" +
                        ", UNIQUE (" + CardContract.CardEntry._ID + ") ON CONFLICT REPLACE)";

        sqLiteDatabase.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        String deleteSql = "DROP TABLE IF EXISTS " + CardContract.CardEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(deleteSql);
        onCreate(sqLiteDatabase);
    }


    /**
     * adds a card to the database
     * the key is the card's title
     * replaces in case of conflict
     */
    public void insertCard(Card card)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardContract.CardEntry._ID, card.getTitle());
        contentValues.put(CardContract.CardEntry.COLOUMN_DESCRIPTION, card.getDescription());
        contentValues.put(CardContract.CardEntry.COLOUMN_TYPE, card.getType());
        contentValues.put(CardContract.CardEntry.COLOUMN_IMAGE_URL, card.getImageUrl());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(CardContract.CardEntry.TABLE_NAME, null, contentValues);
    }


    /**
     * returns all the cards in the database
     */
    public List<Card> getCards(String type)
    {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(CardContract.CardEntry.TABLE_NAME,
                null,
                CardContract.CardEntry.COLOUMN_TYPE + "=?",
                new String[]{type},
                null,
                null,
                null);

        // parse data from the cursor
        List<Card> cardList = new ArrayList<>();
        if (cursor.moveToFirst())
            do
            {
                Card card = new Card();

                card.setTitle(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry._ID)));
                card.setDescription(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_DESCRIPTION)));
                card.setType(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_TYPE)));
                card.setImageUrl(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_IMAGE_URL)));

                cardList.add(card);
            } while (cursor.moveToNext());

        return cardList;
    }
}
