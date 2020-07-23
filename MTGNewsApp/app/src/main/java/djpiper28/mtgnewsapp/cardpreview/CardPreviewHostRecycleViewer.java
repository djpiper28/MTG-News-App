package djpiper28.mtgnewsapp.cardpreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import djpiper28.mtgnewsapp.R;
import forohfor.scryfall.api.Card;

public class CardPreviewHostRecycleViewer extends RecyclerView.Adapter<CardPreviewHostRecycleViewer.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Card> cards;

    // data is passed into the constructor
    public CardPreviewHostRecycleViewer(Context context, List<Card> cards) {
        this.mInflater = LayoutInflater.from(context);
        this.cards = cards;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_card_view_pager, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewPager2 pager = holder.itemView.findViewById(R.id.view_pager);
        CardViewPagerAdapter adapter = new CardViewPagerAdapter(holder.itemView.getContext(), cards.get(position));

        pager.setAdapter(adapter);
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return cards.size();
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