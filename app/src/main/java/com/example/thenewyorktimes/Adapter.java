package com.example.thenewyorktimes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.thenewyorktimes.pojos.Article;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Adapter extends RecyclerView.Adapter<Adapter.BaseViewHolder> implements View.OnClickListener {

    private final List<Article> articles;
    private final List<Article> originalItems;
    private final Context context;
    private View.OnClickListener onItemClickListener;


    public Adapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.originalItems =new ArrayList<>();
        originalItems.addAll(articles);
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        return new BaseViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Article model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.published_ad.setText(Utils.DateFormat(model.getPublishedAt()));
        holder.author.setText(model.getByline());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    /**
     * Esta función es llamada por el HomeFragment y filtra las noticias apartir del String
     * @param strSearch
     */
    public void filter(String strSearch){
        if(strSearch.length()==0){
            articles.clear();
            articles.addAll(originalItems);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                articles.clear();
                List<Article> collect = originalItems.stream()
                        .filter(i -> Utils.containsIgnoreCase(i.getTitle(), strSearch))
                        .collect(Collectors.toList());
                articles.addAll(collect);
            }
            else{
                articles.clear();
                for (Article article : originalItems) {
                    if(article.getTitle().toLowerCase().contains(strSearch)) {
                        articles.add(article);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.onItemClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (v!=null)
            onItemClickListener.onClick(v);
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder{
        TextView title, desc, author, published_ad, source, time;
        ImageView imageView;
        ProgressBar progressBar;
        public BaseViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            published_ad = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.readmore);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.prograss_load_photo);

        }

    }
}