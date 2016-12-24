package com.enterprises.wayne.yugicards;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.List;

/**
 * test the local SQL database
 */
public class DatabaseTest extends ApplicationTestCase<Application>
{
    public DatabaseTest()
    {
        super(Application.class);
    }

    @LargeTest
    public void testDatabase()
    {
        // open the database
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        // create a card
        Card card = new Card();
        card.setTitle("card2");
        card.setDescription("description2");
        card.setType("monster");
        card.setImageUrl("image2");

        // insert into the database
        databaseHelper.insertCard(card);

        // read from the database
        List<Card> cardList = databaseHelper.getCards("monster");
        int cardCount = 0;
        for (Card readCard : cardList)
        {
            assertEquals("monster", readCard.getType());
            if (readCard.getTitle().equals(card.getTitle()))
            {
                cardCount++;
                assertEquals(card.getDescription(), readCard.getDescription());
                assertEquals(card.getType(), readCard.getType());
                assertEquals(card.getImageUrl(), readCard.getImageUrl());
            }
        }
        assertEquals(1, cardCount);
        databaseHelper.close();
    }
}
