package com.enterprises.wayne.yugicards;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import java.util.List;

/**
 * test the local SQL database
 */
public class ContentProviderTest extends ApplicationTestCase<Application>
{
    public static String LOG_TAG = ContentProviderTest.class.getSimpleName();

    public ContentProviderTest()
    {
        super(Application.class);
    }


    @LargeTest
    public void testInsertAndQuery()
    {
        // form the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardContract.CardEntry._ID, "card1");
        contentValues.put(CardContract.CardEntry.COLOUMN_TYPE, "monster");
        contentValues.put(CardContract.CardEntry.COLOUMN_DESCRIPTION, "description1");
        contentValues.put(CardContract.CardEntry.COLOUMN_IMAGE_URL, "image1");

        // insert into content provider
        Uri uri = getContext()
                .getContentResolver()
                .insert(CardContract.CardEntry.CONTENT_URI, contentValues);
        Cursor cursor = getContext()
                .getContentResolver()
                .query(uri, null, null, null, null);

        // check it's the same as the one we've inserted
        assertTrue(cursor.moveToFirst());
        assertEquals("card1", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry._ID)));
        assertEquals("monster", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_TYPE)));
        assertEquals("description1", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_DESCRIPTION)));
        assertEquals("image1", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_IMAGE_URL)));
        cursor.close();
    }

    @LargeTest
    public void testDelete()
    {
        // delete with no selection
        mContext.getContentResolver().delete(
                CardContract.CardEntry.CONTENT_URI,
                null,
                null
        );

        // try to query
        Cursor cursor = mContext.getContentResolver().query(
                CardContract.CardEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    @LargeTest
    public void testUpdate()
    {

        // form the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardContract.CardEntry._ID, "card1");
        contentValues.put(CardContract.CardEntry.COLOUMN_TYPE, "monster");
        contentValues.put(CardContract.CardEntry.COLOUMN_DESCRIPTION, "description1");
        contentValues.put(CardContract.CardEntry.COLOUMN_IMAGE_URL, "image1");

        // insert into content provider
        getContext()
                .getContentResolver()
                .insert(CardContract.CardEntry.CONTENT_URI, contentValues);

        // update the content values
        contentValues.put(CardContract.CardEntry.COLOUMN_DESCRIPTION, "description1updated");
        getContext()
                .getContentResolver()
                .update(CardContract.CardEntry.CONTENT_URI,
                        contentValues,
                        CardContract.CardEntry._ID + "=?",
                        new String[]{"card1"});

        // query that card
        Cursor cursor = getContext()
                .getContentResolver()
                .query(CardContract.CardEntry.CONTENT_URI,
                        null,
                        CardContract.CardEntry._ID + "=?",
                        new String[]{"card1"},
                        null,
                        null);

        // check it's the same as the one we've inserted
        assertTrue(cursor.moveToFirst());
        assertEquals("card1", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry._ID)));
        assertEquals("monster", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_TYPE)));
        assertEquals("description1updated", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_DESCRIPTION)));
        assertEquals("image1", cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_IMAGE_URL)));
        cursor.close();
    }

}
