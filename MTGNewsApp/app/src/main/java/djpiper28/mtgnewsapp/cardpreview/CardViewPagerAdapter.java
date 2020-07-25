package djpiper28.mtgnewsapp.cardpreview;

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

import java.util.List;

import djpiper28.mtgnewsapp.CardPreviewActivity;
import djpiper28.mtgnewsapp.R;
import djpiper28.settings.SettingsLoader;
import forohfor.scryfall.api.Card;

public class CardViewPagerAdapter extends RecyclerView.Adapter<CardViewPagerAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final Card card;

    // data is passed into the constructor
    public CardViewPagerAdapter(Context context, Card card) {
        this.mInflater = LayoutInflater.from(context);
        this.card = card;
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
        if (position == 1) {
            TextView cardName = holder.itemView.findViewById(R.id.card_name);
            cardName.setText(card.getName());

            TextView oracleText = holder.itemView.findViewById(R.id.oracle_text);
            String pt = card.getPower() == null || card.getPower().equals("") ? "" : card.getPower() + "/" + card.getToughness();

            oracleText.setText(String.format("%s\n\n%s\n\n%s\n\n%s %s (%s) - %s", card.getTypeLine(), card.getOracleText(), pt, card.getCollectorNumber(), card.getRarity().toUpperCase().toCharArray()[0], card.getSetCode().toUpperCase(), card.getArtist()));

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
            ImageView image = holder.itemView.findViewById(R.id.card_image);
            TextView cardName = holder.itemView.findViewById(R.id.card_name);
            cardName.setText(card.getName());

            // Set max sizes
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) holder.itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = (int) (displayMetrics.heightPixels * 0.9f);
            int width = displayMetrics.widthPixels;
            image.setMaxHeight(height);
            image.setMaxWidth(width);

            // Use smallish images to save space
            Glide
                    .with(holder.itemView)
                    .load(card.getImageURI("normal"))
                    .placeholder(R.drawable.reload)
                    .into(image);
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