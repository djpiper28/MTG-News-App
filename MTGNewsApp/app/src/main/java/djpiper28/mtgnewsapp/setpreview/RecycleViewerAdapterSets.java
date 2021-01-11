package djpiper28.mtgnewsapp.setpreview;

import android.content.Context;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import djpiper28.mtgnewsapp.R;
import djpiper28.mtgnewsapp.setpreview.cardpreview.CardPreviewHostActivity;
import djpiper28.mtgnewsapp.setpreview.cardpreview.CardSearchAndSorter;
import djpiper28.mtgnewsapp.settings.SettingsLoader;
import forohfor.scryfall.api.MTGCardQuery;
import forohfor.scryfall.api.Set;

public class RecycleViewerAdapterSets extends RecyclerView.Adapter<RecycleViewerAdapterSets.ViewHolder> {

    private final List<Set> mData;
    private final LayoutInflater mInflater;
    private int unreleasedPointer;
    private ItemClickListener mClickListener;
    private boolean openingTab = false;

    // data is passed into the constructor
    public RecycleViewerAdapterSets(Context context, List<Set> data, int unreleasedPointe) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.unreleasedPointer = unreleasedPointe;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_set_preview, parent, false);

        return new RecycleViewerAdapterSets.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder parent, int position) {
        Set set = mData.get(position);

        View view = parent.itemView;
        ImageView setImage = view.findViewById(R.id.SetIcon);

        if (set.getParentSetCode() == null || set.getParentSetCode().equals("")) {
            ((CardView) parent.itemView.findViewById(R.id.CardView)).setPadding(40, 10, 10, 10);
        }

        try {
            GlideToVectorYou
                    .init()
                    .with(parent.itemView.getContext())
                    .withListener(new GlideToVectorYouListener() {
                        @Override
                        public void onLoadFailed() {
                            //Toast.makeText(parent.itemView.getContext(), "Load failed", Toast.LENGTH_SHORT).show();
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

                    .setPlaceHolder(R.drawable.reload, R.drawable.ic_close_black_24dp)
                    .getRequestBuilder()
                    .centerCrop()
                    .load(Uri.parse(set.getSetIconURI()))
                    .into(setImage);

            TextView titleText = view.findViewById(R.id.SetTitle);
            titleText.setText(set.getName());

            Date twoWeeksAgo = Calendar.getInstance().getTime();
            twoWeeksAgo.setTime(twoWeeksAgo.getTime() - 14L * 24L * 36000L);
            String newStr = set.getReleasedAt().after(twoWeeksAgo) ? "( New ! )" : "";

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(parent.itemView.getContext());

            TextView releaseDate = view.findViewById(R.id.ReleaseDate);
            releaseDate.setText(String.format("Released date: %s %s", dateFormat.format(set.getReleasedAt()).split(",")[0], newStr));

            CardView viewPreviews = view.findViewById(R.id.CardView);
            viewPreviews.setOnClickListener(event -> {
                CardSearchAndSorter.resetCache();
                if (!openingTab) {
                    openingTab = true;
                    // Run immediately to assert that no rapid spamming can open many threads

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

                        openingTab = false;

                    })).start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public void setPointer(int pointer) {
        this.unreleasedPointer = pointer;
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
