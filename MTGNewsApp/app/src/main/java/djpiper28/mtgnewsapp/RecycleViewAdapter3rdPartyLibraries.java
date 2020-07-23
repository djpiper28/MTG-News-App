package djpiper28.mtgnewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import djpiper28.settings.SettingsLoader;

public class RecycleViewAdapter3rdPartyLibraries extends RecyclerView.Adapter<RecycleViewAdapter3rdPartyLibraries.ViewHolder> {

    private List<ExternalLibraries> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecycleViewAdapter3rdPartyLibraries(Context context, List<ExternalLibraries> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_third_party_library, parent, false);

        return new RecycleViewAdapter3rdPartyLibraries.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ( (TextView) holder.itemView.findViewById(R.id.name)).setText(mData.get(position).getName());

        holder.itemView.findViewById(R.id.library_open).setOnClickListener(event -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getUrl()));
            holder.itemView.getContext().startActivity(intent);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView.findViewById(R.id.library_open).setBackgroundColor(SettingsLoader.getSettingsLoader().getSettings().getAccentColour());
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public ExternalLibraries getItem(int id) {
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
