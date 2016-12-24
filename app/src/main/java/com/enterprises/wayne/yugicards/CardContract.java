package com.enterprises.wayne.yugicards;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * defines table and coloumn names for the Card table
 */
public class CardContract
{
    public static final String CONTENT_AUTHORITY = "com.enterprises.wayne.yugicards";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CARD = "card";

    public static final class CardEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD;

        public static final String TABLE_NAME = "Card";

        public static final String COLOUMN_DESCRIPTION = "description";
        public static final String COLOUMN_TYPE = "type";
        public static final String COLOUMN_IMAGE_URL = "image_url";

    }
}
