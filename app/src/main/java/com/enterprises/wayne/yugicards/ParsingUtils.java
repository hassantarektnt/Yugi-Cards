package com.enterprises.wayne.yugicards;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 7/25/2016.
 */
public class ParsingUtils
{
    /* constants */
    public static final String LOG_TAG = ParsingUtils.class.getSimpleName();

    /**
     * parses a card from a json string
     *
     * @return null if the format of jsonString is wrong
     */
    public static Card parseCard(String jsonString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);

            Card card = new Card();

            card.setTitle(jsonObject.getString("title"));
            card.setType(jsonObject.getString("type"));
            card.setDescription(jsonObject.getString("description"));
            card.setImageUrl(jsonObject.getString("imageUrl"));

            return card;
        } catch (JSONException e)
        {
            Log.e(LOG_TAG, "error in parseCard() " + e.getMessage());
            return null;
        }
    }

    /**
     * parses a list of cards from the API response
     *
     * @return null if the format of the resposne is wrong
     */
    public static List<Card> parseResponse(String responseString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("cards");

            List<Card> cardList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                cardList.add(parseCard(jsonArray.getJSONObject(i).toString()));
            return cardList;
        } catch (JSONException e)
        {
            Log.e(LOG_TAG, "error in parseResponse() " + e.getMessage());
            return null;
        }
    }
}
