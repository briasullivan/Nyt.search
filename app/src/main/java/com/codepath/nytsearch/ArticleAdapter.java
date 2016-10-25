package com.codepath.nytsearch;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.nytsearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by briasullivan on 10/20/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE = 0;
    private static final int TEXT = 1;

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView headline;
        public TextView newsdesk;
        public DynamicHeightImageView thumbnail;

        public ImageViewHolder(View itemView) {
            super(itemView);
            headline = (TextView) itemView.findViewById(R.id.tvHeadline);
            thumbnail = (DynamicHeightImageView) itemView.findViewById(R.id.ivThumbnail);
            newsdesk = (TextView) itemView.findViewById(R.id.tvNewsDesk);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView headline;
        public TextView snippet;
        public TextView newsdesk;

        public TextViewHolder(View itemView) {
            super(itemView);
            headline = (TextView) itemView.findViewById(R.id.tvHeadlineText);
            snippet = (TextView) itemView.findViewById(R.id.tvSnippet);
            newsdesk = (TextView) itemView.findViewById(R.id.tvNewsDeskText);
        }
    }


    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case IMAGE:
                View imageArticleView = inflater.inflate(R.layout.item_article_image, parent, false);
                viewHolder = new ImageViewHolder(imageArticleView);
                break;
            case TEXT:
            default:
                View textArticleView = inflater.inflate(R.layout.item_article_text, parent, false);
                viewHolder = new TextViewHolder(textArticleView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);

        switch (holder.getItemViewType()) {
            case IMAGE:
                ImageViewHolder iHolder = (ImageViewHolder) holder;
                configureImageViewHolder(iHolder, article);
                break;
            case TEXT:
            default:
                TextViewHolder tHolder = (TextViewHolder) holder;
                configureTextViewHolder(tHolder, article);
                break;
        }
    }

    private void configureImageViewHolder(ImageViewHolder holder, Article article) {
        TextView tvHeadline = holder.headline;
        TextView tvNewsDesk = holder.newsdesk;

        String headline = article.getHeadline();
        tvHeadline.setText((headline != null) ? headline : "Headline");
        String newsDesk = article.getNewsDesk();
        tvNewsDesk.setText(newsDesk);
        if (newsDesk == null || newsDesk == "" || newsDesk.equals("None")) {
            tvNewsDesk.setVisibility(View.GONE);
        } else {
            tvNewsDesk.setVisibility(View.VISIBLE);
        }
        DynamicHeightImageView imageView = holder.thumbnail;

        String imageUrl = article.getThumbNail();
        imageView.setHeightRatio(((double) article.getHeight()) / article.getWidth());
        Picasso.with(context).load(imageUrl).placeholder(R.drawable.article_placeholder).into(imageView);
        holder.itemView.setOnClickListener(getArticleOnClickListener(article));
    }

    private void configureTextViewHolder(TextViewHolder holder, Article article) {
        TextView tvHeadline = holder.headline;
        TextView tvSnippet = holder.snippet;
        TextView tvNewsDesk = holder.newsdesk;

        tvHeadline.setText(article.getHeadline());
        tvSnippet.setText(article.getSnippet());
        String newsDesk = article.getNewsDesk();
        tvNewsDesk.setText(newsDesk);
        if (newsDesk == null || newsDesk == "" || newsDesk.equals("None")) {
            tvNewsDesk.setVisibility(View.GONE);
        } else {
            tvNewsDesk.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(getArticleOnClickListener(article));
    }

    private View.OnClickListener getArticleOnClickListener(final Article article) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_action_share);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());
                int requestCode = 100;
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setActionButton(bitmap, "Share Article", pendingIntent, true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl((Activity)getContext(), Uri.parse(article.getWebUrl()));
            }
        };
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position).getThumbNail() != null) {
            return IMAGE;
        }
        return TEXT;
    }
}
