package com.enterprises.wayne.yugicards;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment
{
    public static String ARG_CARD = "argumentCard";


    public DetailsFragment()
    {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    /**
     * only use this to get an instance of the details fragment
     *
     * @return a new instance of the Details Fragment
     */
    public static DetailsFragment newInstance(Card card)
    {
        DetailsFragment detailsFragment = new DetailsFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_CARD, card);
        detailsFragment.setArguments(arguments);

        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // reference view
        TextView textViewTitle = (TextView) view.findViewById(R.id.text_view_title);
        TextView textViewDescription = (TextView) view.findViewById(R.id.text_view_description);
        ImageView imageViewCard = (ImageView) view.findViewById(R.id.image_view_card);

        // set card's data to view
        Card card = (Card) getArguments().getSerializable(ARG_CARD);
        textViewTitle.setText(card.getTitle());
        textViewDescription.setText(card.getDescription());
        Picasso.with(getContext())
                .load(card.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(imageViewCard);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Card card = (Card) getArguments().getSerializable(ARG_CARD);

        if (item.getItemId() == R.id.menu_item_share)
            if (card != null)
            {
                // share the card's description
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, card.getTitle() + "\n" + card.getDescription());
                sendIntent.setType("text/plain");
                Intent chooserIntent = Intent.createChooser(sendIntent, getString(R.string.share_card));

                // Verify that the intent will resolve to an activity
                if (chooserIntent.resolveActivity(getContext().getPackageManager()) != null)
                    startActivity(chooserIntent);
            }

        return super.onOptionsItemSelected(item);
    }
}
