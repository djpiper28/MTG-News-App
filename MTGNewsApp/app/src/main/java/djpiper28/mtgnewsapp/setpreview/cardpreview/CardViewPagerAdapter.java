package djpiper28.mtgnewsapp.setpreview.cardpreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import djpiper28.mtgnewsapp.R;
import djpiper28.mtgnewsapp.settings.SettingsLoader;
import forohfor.scryfall.api.Card;

public class CardViewPagerAdapter extends RecyclerView.Adapter<CardViewPagerAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Card card;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public CardViewPagerAdapter(Context context, Card card) {
        this.card = card;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == 1) {
            view = mInflater.inflate(R.layout.fragment_card_view_oracle, parent, false);
        } else {
            view = mInflater.inflate(R.layout.fragment_card_view_image_and_title, parent, false);
        }

        return new CardViewPagerAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            boolean singleSided = card.getCardFaces() == null || card.getCardFaces().size() == 1;

            if (position == 1) {
                TextView cardName = holder.itemView.findViewById(R.id.card_name);
                cardName.setText(card.getName());

                TextView oracleText = holder.itemView.findViewById(R.id.oracle_text);
                String pt = card.getPower() == null || card.getPower().equals("") ? "" : card.getPower() + "/" + card.getToughness();

                if (singleSided) {
                    oracleText.setText(String.format("%s\n\n%s\n\n%s\n\n%s %s (%s) - %s", card.getTypeLine(), card.getOracleText(), pt, card.getCollectorNumber(),
                            card.getRarity().toUpperCase().toCharArray()[0], card.getSetCode().toUpperCase(), card.getArtist()));
                } else {
                    oracleText.setText(String.format("%s\n\n%s\n\n===========\n\n%s\n\n%s\n\n%s %s (%s) - %s",  card.getTypeLine(),  card.getCardFaces().get(0).getOracleText(),
                            card.getCardFaces().get(1).getOracleText(), pt, card.getCollectorNumber(),
                            card.getRarity().toUpperCase().toCharArray()[0], card.getSetCode().toUpperCase(),  card.getArtist()));
                }

                Button openButton = holder.itemView.findViewById(R.id.open_card_on_scryfall);
                openButton.setOnClickListener(click -> {
                    CardPreviewActivity.url = card.getScryfallURI();
                    Intent intent = new Intent(holder.itemView.getContext(), CardPreviewActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                });

                openButton.setBackgroundColor(SettingsLoader.getSettingsLoader().getSettings().getAccentColour());

                RecyclerView manmoji = holder.itemView.findViewById(R.id.manamoji_holder);

                List<Drawable> drawableList = Manamoji.getManamojis(Manamoji.getKeys(card), holder.itemView.getContext());
                manmoji.setAdapter(new RecycleViewerManamoji(holder.itemView.getContext(), drawableList));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                manmoji.setLayoutManager(linearLayoutManager);
            } else {
                TextView cardName = holder.itemView.findViewById(R.id.card_name);
                cardName.setText(card.getName());

                ImageView image = holder.itemView.findViewById(R.id.card_image);

                // Set max sizes
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) holder.itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = (int) (displayMetrics.heightPixels * 0.9f);
                int width = displayMetrics.widthPixels;
                image.setMaxHeight(height);
                image.setMaxWidth(width);

                // Use smallish images to save space
                String imageStr = singleSided ? card.getImageURI("normal") : card.getCardFaces().get(0).getImageURI("normal");
                if (!singleSided) {
                    FloatingActionButton fab = holder.itemView.findViewById(R.id.flipCard);
                    fab.setBackgroundColor(SettingsLoader.getSettingsLoader().getSettings().getAccentColour());
                    int[] side = {0};

                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(click -> {
                        fab.setEnabled(false);
                        fab.setRotation(fab.getRotation() + 180 % 360);
                        side[0] = (side[0] + 1) % 2;

                        Glide
                                .with(holder.itemView)
                                .load(card.getCardFaces().get(side[0]).getImageURI("normal"))
                                .placeholder(R.drawable.reload)
                                .into(image);
                        fab.setEnabled(true);
                    });
                }

                Glide
                        .with(holder.itemView)
                        .load(imageStr)
                        .placeholder(R.drawable.reload)
                        .into(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return 2;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}