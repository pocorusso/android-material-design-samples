package com.pocorusso.mdsamples;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardsFragment extends Fragment {
    final static String WONDERS[] = {"Chichen Itza", "Christ the Redeemer", "Great Wall of China", "Machu Picchu", "Petra", "Taj Mahal", "Colosseum"};
    final static int IMAGES[] = {R.drawable.chichen_itza, R.drawable.christ_the_redeemer, R.drawable.great_wall_of_china, R.drawable.machu_picchu, R.drawable.petra, R.drawable.taj_mahal, R.drawable.colosseum};

    ArrayList<WonderModel> mListItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle("7 Wonders of the Modern World");
    }

    private void initializeList() {
        mListItems.clear();

        for (int i = 0; i < WONDERS.length; i++) {
            WonderModel item = new WonderModel();
            item.setCardName(WONDERS[i]);
            item.setImageResourceId(IMAGES[i]);
            item.setIsFav(0);
            item.setIsTurned(0);
            mListItems.add(item);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_cards, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.cardView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (mListItems.size() > 0 & recyclerView != null) {
            recyclerView.setAdapter(new CardViewAdapter(mListItems));
        }
        recyclerView.setLayoutManager(linearLayoutManager);

        return v;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public CardViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int) likeImageView.getTag();
                    if (id == R.drawable.ic_favorite_border) {

                        likeImageView.setTag(R.drawable.ic_favorite_filled);
                        likeImageView.setImageResource(R.drawable.ic_favorite_filled);
                        Toast.makeText(getActivity(), titleTextView.getText() + " added to favourites", Toast.LENGTH_SHORT).show();

                    } else {

                        likeImageView.setTag(R.drawable.ic_favorite_border);
                        likeImageView.setImageResource(R.drawable.ic_favorite_border);
                        Toast.makeText(getActivity(), titleTextView.getText() + " removed from favourites", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int) coverImageView.getTag()));

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }
            });
        }
    }

    public class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> {
        private ArrayList<WonderModel> list;

        public CardViewAdapter(ArrayList<WonderModel> Data) {
            list = Data;
        }

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_card, parent, false);
            CardViewHolder holder = new CardViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final CardViewHolder holder, int position) {

            holder.titleTextView.setText(list.get(position).getCardName());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.likeImageView.setTag(R.drawable.ic_favorite_border);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
