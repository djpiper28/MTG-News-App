package djpiper28.mtgnewsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.io.IOException;
import java.util.List;

import djpiper28.news.NewsItem;

import static djpiper28.mtgnewsapp.ArticleViewActivity.urlForArticle;

public class RecycleViewerAdapterNews extends RecyclerView.Adapter<RecycleViewerAdapterNews.ViewHolder> {

    private List<NewsItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecycleViewerAdapterNews(Context context, List<NewsItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    private void expandForNews(ViewHolder parent, NewsItem newsItem) {
        TextView title = parent.title;
        TextView publishInfo = parent.publishInfo;
        TextView articleText = parent.articleText;
        ImageView myImageView = parent.myImageView;

        title.setText(newsItem.getTitle());
        publishInfo.setText(newsItem.getPublishedDate() + " " + newsItem.getAuthor());
        articleText.setText(newsItem.getDescription());

        myImageView.setOnClickListener(event -> {
            String url = newsItem.getLink();

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            Intent intent = new Intent(parent.itemView.getContext(), ArticleViewActivity.class);
            urlForArticle = url;

            parent.itemView.getContext().startActivity(intent);
        });

        try {
            Glide
                    .with(parent.itemView)
                    .load(newsItem.getImageURL().toString())
                    .centerCrop()
                    .transform(new RoundedCorners(24))
                    .placeholder(R.drawable.reload)
                    .into(myImageView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_news, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsItem news = mData.get(position);
        expandForNews(holder, news);
        if (position == mData.size() - 1) {
            holder.itemView.findViewById(R.id.newsContainer).setPaddingRelative(0, 12, 0, 150);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public NewsItem getItem(int id) {
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
        TextView title;
        TextView publishInfo;
        TextView articleText;
        ImageView myImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            publishInfo = itemView.findViewById(R.id.publishInfo);
            articleText = itemView.findViewById(R.id.articleText);
            myImageView = itemView.findViewById(R.id.backgroundImage);

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
