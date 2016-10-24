package com.codepath.nytsearch;

import android.content.Context;
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
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView headline;
        public TextView snippet;
        public DynamicHeightImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            headline = (TextView) itemView.findViewById(R.id.tvHeadline);
            thumbnail = (DynamicHeightImageView) itemView.findViewById(R.id.ivThumbnail);
            snippet = (TextView) itemView.findViewById(R.id.tvSnippet);
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        View articleView = inflater.inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articles.get(position);

        TextView tvHeadline = holder.headline;
        TextView tvSnippet = holder.snippet;

        String headline = article.getHeadline();
        tvHeadline.setText((headline != null) ? headline : "Headline");

        DynamicHeightImageView imageView = holder.thumbnail;

        String imageUrl = article.getThumbNail();
        if (imageUrl != null) {
            tvSnippet.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setHeightRatio(((double) article.getHeight()) / article.getWidth());
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.article_placeholder).into(imageView);
        } else {
            tvSnippet.setVisibility(View.VISIBLE);
            tvSnippet.setText(article.getSnippet());
            imageView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}