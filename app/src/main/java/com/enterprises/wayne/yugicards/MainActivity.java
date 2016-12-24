package com.enterprises.wayne.yugicards;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CardAdapter.Listener
{
    /* constants */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public final int READ_FROM_DATABASE_ID = 42;
    public final int SAVE_FROM_DATABASE_ID = 43;

    /* UI */
    RecyclerView recyclerViewCards;
    CardAdapter adapterCards;
    SwipeRefreshLayout swipeRefreshLayout;

    /* fields */
    List<Card> mCardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference the views
        recyclerViewCards = (RecyclerView) findViewById(R.id.recycler_view_cards);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // setup recycler view
        adapterCards = new CardAdapter(this);
        adapterCards.setListener(this);
        recyclerViewCards
                .setLayoutManager
                        (new GridLayoutManager(this, 2));
        recyclerViewCards.setAdapter(adapterCards);

        // add listener
        //recyclerViewCards.setOnItemClickListener(this);

        // setup swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_item_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // TODO problem when returning from the details activity
        loadDataFromDatabase();
    }


    @Override
    public void onRefresh()
    {
        loadDataFromAPI();
    }

    /**
     * loads the cards from the local database
     */
    private void loadDataFromDatabase()
    {
        // get the card type
        String cardType = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(getString(R.string.key_pref_card_type), getString(R.string.monster));
        cardType = Character.toUpperCase(cardType.charAt(0)) + cardType.substring(1);

        final String finalCardType1 = cardType;
        android.support.v4.content.Loader<List<Card>> loader =
                getSupportLoaderManager()
                        .initLoader(READ_FROM_DATABASE_ID
                                , null
                                , new android.support.v4.app.LoaderManager.LoaderCallbacks<List<Card>>()
                                {
                                    @Override
                                    public android.support.v4.content.Loader<List<Card>> onCreateLoader(int id, Bundle args)
                                    {
                                        Log.d(LOG_TAG, "onCreateLoader " + finalCardType1);
                                        return new ReadDataLoader
                                                (MainActivity.this,
                                                        finalCardType1);
                                    }

                                    @Override
                                    public void onLoadFinished(android.support.v4.content.Loader<List<Card>> loader, List<Card> data)
                                    {
                                        Log.d(LOG_TAG, "onLoadFinished " + data.size());
                                        mCardsList = data;

                                        // update the adapter
                                        adapterCards.setData(mCardsList);

                                    }

                                    @Override
                                    public void onLoaderReset(android.support.v4.content.Loader<List<Card>> loader)
                                    {

                                    }
                                });
        loader.forceLoad();

    }

    /**
     * downloads the cards data from the backend API
     */
    private void loadDataFromAPI()
    {
        // make a GET requests
        String cardType = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(getString(R.string.key_pref_card_type), getString(R.string.monster));
        String url = "https://greek-302.herokuapp.com/cards/" + cardType;
        Ion.with(this)
                .load("GET", url)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {

                        // stop the swipe to refresh
                        swipeRefreshLayout.setRefreshing(false);

                        // check error
                        if (e != null)
                        {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // parse the data
                        MainActivity.this.mCardsList = ParsingUtils.parseResponse(result);
                        if (mCardsList == null)
                        {
                            Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // update the adapter
                        adapterCards.setData(mCardsList);

                        // save to local database
                        saveToDatabase(mCardsList);

                    }
                });
    }

    private void saveToDatabase(final List<Card> cardsList)
    {
        Loader<Void> loader = getSupportLoaderManager()
                .initLoader(
                        SAVE_FROM_DATABASE_ID,
                        null,
                        new LoaderManager.LoaderCallbacks<Void>()
                        {
                            @Override
                            public Loader<Void> onCreateLoader(int id, Bundle args)
                            {
                                Log.d(LOG_TAG, "onCreateLoader");
                                return new WriteDataLoader(MainActivity.this
                                        , cardsList);
                            }

                            @Override
                            public void onLoadFinished(Loader<Void> loader, Void data)
                            {
                                Log.d(LOG_TAG, "onLoadFinished");
                                // do nothing
                            }

                            @Override
                            public void onLoaderReset(Loader<Void> loader)
                            {
                                // do nothing
                            }
                        });
        loader.forceLoad();
    }

    @Override
    public void onCardClicked(Card card)
    {
        if (findViewById(R.id.fragment_container) == null)
        {
            // open the details activity
            startActivity(DetailsActivity.getIntent(this, card));
        } else
        {
            // show the details fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            DetailsFragment.newInstance(card))
                    .commit();
        }
    }

    /**
     * reads a list of cards from the database
     */
    static class ReadDataLoader extends AsyncTaskLoader<List<Card>>
    {
        private String mCardType;

        public ReadDataLoader(Context context, String cardType)
        {
            super(context);
            mCardType = cardType;
        }

        @Override
        public List<Card> loadInBackground()
        {
            Log.d(LOG_TAG, "loadInBackground");

            // query the local database
            Cursor cursor =
                    getContext()
                            .getContentResolver()
                            .query(CardContract.CardEntry.CONTENT_URI,
                                    null,
                                    CardContract.CardEntry.COLOUMN_TYPE + "=?",
                                    new String[]{mCardType},
                                    null,
                                    null);

            // parse data from the cursor
            List<Card> cardsList = new ArrayList<>();
            if (cursor.moveToFirst())
                do
                {
                    Card card = new Card();

                    card.setTitle(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry._ID)));
                    card.setDescription(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_DESCRIPTION)));
                    card.setType(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_TYPE)));
                    card.setImageUrl(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLOUMN_IMAGE_URL)));

                    cardsList.add(card);
                } while (cursor.moveToNext());

            Log.d(LOG_TAG, "read data " + cardsList.size());
            return cardsList;
        }
    }

    /**
     * saves a list of cards to the local database
     */
    static class WriteDataLoader extends AsyncTaskLoader<Void>
    {

        private List<Card> mCardList;

        public WriteDataLoader(Context context, List<Card> cardList)
        {
            super(context);
            mCardList = cardList;
        }

        @Override
        public Void loadInBackground()
        {
            // save to the local database
            for (Card card : mCardList)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put(CardContract.CardEntry._ID, card.getTitle());
                contentValues.put(CardContract.CardEntry.COLOUMN_DESCRIPTION, card.getDescription());
                contentValues.put(CardContract.CardEntry.COLOUMN_TYPE, card.getType());
                contentValues.put(CardContract.CardEntry.COLOUMN_IMAGE_URL, card.getImageUrl());
                getContext()
                        .getContentResolver()
                        .insert(CardContract.CardEntry.CONTENT_URI, contentValues);
            }

            return null;
        }
    }

}
