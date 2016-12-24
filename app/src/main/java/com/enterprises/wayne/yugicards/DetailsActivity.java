package com.enterprises.wayne.yugicards;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity
{
    /* constants */
    public static final String EXTRAS_CARD = "extrasCard";

    /* fields */
    Card card;

    /**
     * the only way to start the activity
     */
    public static Intent getIntent(Context context, Card card)
    {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRAS_CARD, card);
        return intent;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activty_with_fragment);
        getSupportActionBar().setHomeButtonEnabled(true);

        // read the card from the intent
        card = (Card) getIntent().getSerializableExtra(EXTRAS_CARD);


        if (getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container) == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, DetailsFragment.newInstance(card))
                    .commit();
    }
}
