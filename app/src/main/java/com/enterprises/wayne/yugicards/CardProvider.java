package com.enterprises.wayne.yugicards;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ahmed on 8/17/2016.
 */
public class CardProvider extends ContentProvider
{
    /* constants */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int CARD = 43;

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CardContract.CONTENT_AUTHORITY, CardContract.PATH_CARD, CARD);
        return matcher;
    }

    /* fields */
    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate()
    {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder)
    {
        Cursor retCursor;

        switch (sUriMatcher.match(uri))
        {
            case CARD:
            {
                retCursor = mDatabaseHelper
                        .getReadableDatabase()
                        .query(CardContract.CardEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case CARD:
                return CardContract.CardEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        // open the database
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // check which type of uri
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match)
        {
            case CARD:
                // insert and create a uri that points to the value added
                long _id = db.insert(CardContract.CardEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                {
                    returnUri = CardContract.CardEntry.CONTENT_URI.buildUpon()
                            .appendQueryParameter(CardContract.CardEntry._ID, contentValues.getAsString(CardContract.CardEntry._ID)).build();

                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify any observers
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if (selection == null) selection = "1";
        switch (match)
        {
            case CARD:
                rowsDeleted = db.delete(
                        CardContract.CardEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify any observers
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated;

        // check which uri is that
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case CARD:
                rowsUpdated = db.update(CardContract.CardEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify any observers
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
