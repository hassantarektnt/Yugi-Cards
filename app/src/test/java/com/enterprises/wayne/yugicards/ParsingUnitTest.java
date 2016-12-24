package com.enterprises.wayne.yugicards;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ParsingUnitTest
{
    @Test
    public void testParseCard() throws Exception
    {
        String jsonString =
                "{\n" +
                        "            \"imageUrl\": \"https://ycgscripts-chakrasitesinc.netdna-ssl.com/images/lob/LOB-EN001.jpg\",\n" +
                        "            \"description\": \"This legendary dragon is a powerful engine of destruction. Virtually invincible, very few have faced this awesome creature and lived to tell the tale.\",\n" +
                        "            \"title\": \"Blue-Eyes White Dragon\",\n" +
                        "            \"type\": \"Monster\"\n" +
                        "}";
        Card card = ParsingUtils.parseCard(jsonString);

        assertEquals("Blue-Eyes White Dragon", card.getTitle());
        assertEquals("Monster", card.getType());
        assertEquals("This legendary dragon is a powerful engine of destruction. Virtually invincible, very few have faced this awesome creature and lived to tell the tale.", card.getDescription());
        assertEquals("https://ycgscripts-chakrasitesinc.netdna-ssl.com/images/lob/LOB-EN001.jpg", card.getImageUrl());
    }

    @Test
    public void testParseResponse() throws Exception
    {
        String response =
                "{\n" +
                        "    \"cards\": [\n" +
                        "        {\n" +
                        "            \"imageUrl\": \"https://ycgscripts-chakrasitesinc.netdna-ssl.com/images/lob/LOB-EN001.jpg\",\n" +
                        "            \"description\": \"This legendary dragon is a powerful engine of destruction. Virtually invincible, very few have faced this awesome creature and lived to tell the tale.\",\n" +
                        "            \"title\": \"Blue-Eyes White Dragon\",\n" +
                        "            \"type\": \"Monster\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"imageUrl\": \"https://ycgscripts-chakrasitesinc.netdna-ssl.com/images/bpt/BPT-007.jpg\",\n" +
                        "            \"description\": \"The ultimate wizard in terms of attack and defense.\",\n" +
                        "            \"title\": \"Dark Magician\",\n" +
                        "            \"type\": \"Monster\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}\n";
        List<Card> cardList = ParsingUtils.parseResponse(response);

        assertNotNull(cardList);
        assertEquals(2, cardList.size());
        boolean foundBlueEyedDragon = false;
        for (int i = 0; i < cardList.size(); i++)
            if (cardList.get(i).getTitle().equals("Blue-Eyes White Dragon"))
            {
                foundBlueEyedDragon = true;

                Card card = cardList.get(i);
                assertEquals("Monster", card.getType());
                assertEquals("This legendary dragon is a powerful engine of destruction. Virtually invincible, very few have faced this awesome creature and lived to tell the tale.", card.getDescription());
                assertEquals("https://ycgscripts-chakrasitesinc.netdna-ssl.com/images/lob/LOB-EN001.jpg", card.getImageUrl());
            }
        assertTrue(foundBlueEyedDragon);
    }


}