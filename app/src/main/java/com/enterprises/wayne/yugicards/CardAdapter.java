package com.enterprises.wayne.yugicards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 8/27/2016.
 */
public class CardAdapter
        extends RecyclerView.Adapter<CardAdapter.CardViewHolder>
{

    private final Context mContext;
    private List<Card> mData;
    private Listener mListener;

    public CardAdapter(Context context)
    {
        mContext = context;
        mData = new ArrayList<>();
    }

    /**
     * registers to be invoked on click
     */
    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void setData(List<Card> newData)
    {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.row_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder,
                                 int position)
    {
        Card card = mData.get(position);
        Picasso.with(mContext)
                .load(card.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    class CardViewHolder
            extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public CardViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.
                    findViewById(R.id.image_view);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mListener != null)
                    {
                        Card card = mData.get(getAdapterPosition());
                        mListener.onCardClicked(card);
                    }
                }
            });
        }
    }

    public interface Listener
    {
        void onCardClicked(Card card);
    }
}
