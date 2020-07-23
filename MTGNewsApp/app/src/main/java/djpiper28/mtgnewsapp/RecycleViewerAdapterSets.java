package djpiper28.mtgnewsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;

import java.text.DateFormat;
import java.util.List;

import djpiper28.mtgnewsapp.cardpreview.CardPreviewHostActivity;
import djpiper28.mtgnewsapp.cardpreview.CardSearchAndSorter;
import djpiper28.settings.SettingsLoader;
import forohfor.scryfall.api.MTGCardQuery;
import forohfor.scryfall.api.Set;

public class RecycleViewerAdapterSets extends RecyclerView.Adapter<RecycleViewerAdapterSets.ViewHolder> {

    private List<Set> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecycleViewerAdapterSets(Context context, List<Set> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    private void expandForSets(ViewHolder parent, Set set) {
        View view = parent.itemView;
        ImageView setImage = view.findViewById(R.id.SetIcon);

        try {
            GlideToVectorYou
                    .init()
                    .with(parent.itemView.getContext())
                    .withListener(new GlideToVectorYouListener() {
                        @Override
                        public void onLoadFailed() {
                            Toast.makeText(parent.itemView.getContext(), "Load failed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResourceReady() {
                            Paint paint = new Paint();
                            ColorFilter colorFilter;

                            // Try catch just in case the colour has been set to some strange value - who knows!
                            try {
                                colorFilter = new PorterDuffColorFilter(SettingsLoader.getSettingsLoader().getSettings().getPrimaryColour(), PorterDuff.Mode.SRC_ATOP);
                            } catch (Exception e) {
                                e.printStackTrace();
                                colorFilter = new PorterDuffColorFilter(ResourcesCompat.getColor(parent.itemView.getResources(), R.color.colorPrimary, null), PorterDuff.Mode.SRC_ATOP);
                            }

                            paint.setColorFilter(colorFilter);
                            setImage.setLayerPaint(paint);
                        }
                    })
                    .setPlaceHolder(R.drawable.reload, R.drawable.reload)
                    .getRequestBuilder()
                    .centerCrop()
                    .load(Uri.parse(set.getSetIconURI()))
                    .into(setImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView titleText = view.findViewById(R.id.SetTitle);
        titleText.setText(set.getName());

        TextView releaseDate = view.findViewById(R.id.ReleaseDate);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(parent.itemView.getContext());
        releaseDate.setText(String.format("Released date: %s", dateFormat.format(set.getReleasedAt()).split(",")[0]));

        CardView viewPreviews = view.findViewById(R.id.CardView);
        viewPreviews.setOnClickListener(event -> {
            CardSearchAndSorter.resetCache();

            (new Thread(() -> {
                try {
                    CardPreviewHostActivity.set = set;
                    CardPreviewHostActivity.cards = MTGCardQuery.search("set:" + set.getCode());

                    Log.i("Intent Change", "Changing to set preview view, " + CardPreviewHostActivity.cards.size() + " cards set:" + set.getCode());

                    Intent intent = new Intent(parent.itemView.getContext(), CardPreviewHostActivity.class);
                    parent.itemView.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(parent.itemView.getContext(), "Error viewing set.", Toast.LENGTH_LONG).show();
                }
            })).start();
        });
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_set_preview, parent, false);

        return new RecycleViewerAdapterSets.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Set set = mData.get(position);

        expandForSets(holder, set);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public Set getItem(int id) {
        return mData.get(id);
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
