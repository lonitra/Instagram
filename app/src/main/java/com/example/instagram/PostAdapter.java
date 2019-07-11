package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.model.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context mContext;
    List<Post> posts;
    boolean likeClick;


    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Post post = posts.get(i);
        ParseFile file = post.getImage();
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                viewHolder.ivImage.setImageBitmap(bitmap);
            }
        });
        ParseUser user = post.getUser();
        viewHolder.tvUser.setText(user.getUsername());
        viewHolder.tvCaption.setText(post.getDescription());
        viewHolder.tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

        if(post.getNumber("number") != null) {
            viewHolder.tvLikes.setText(post.getNumber("number").toString());
        }

        viewHolder.fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!likeClick) {
                    viewHolder.fabLike.setColorFilter(mContext.getResources().getColor(R.color.medium_red));
                    viewHolder.fabLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like_fill));
                } else {
                    viewHolder.fabLike.setColorFilter(Color.DKGRAY);
                    viewHolder.fabLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like));
                }
                likeClick = !likeClick;
            }
        });

        viewHolder.fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(mContext, CommentActivity.class);
                commentIntent.putExtra("postId", post.getObjectId());
                mContext.startActivity(commentIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUser;
        TextView tvCaption;
        ImageView ivImage;
        TextView tvDate;
        TextView tvLikes;
        FloatingActionButton fabLike;
        FloatingActionButton fabComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            fabLike = itemView.findViewById(R.id.fabFavorite);
            fabComment= itemView.findViewById(R.id.fabComment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent details = new Intent(mContext, PostDetailActivity.class);
                details.putExtra("PostDetails", post);
                mContext.startActivity(details);
            }
        }
    }


}
