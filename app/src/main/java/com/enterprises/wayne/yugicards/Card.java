package com.enterprises.wayne.yugicards;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ahmed on 7/25/2016.
 */
public class Card implements Serializable
{
    /* attributes */
    String title;
    String type;
    String description;
    String imageUrl;

    /* getters and setters */
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
}
