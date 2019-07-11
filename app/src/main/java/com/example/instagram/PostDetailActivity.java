package com.example.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.model.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvUsername;
    private TextView tvCaption;
    private TextView tvDate;
    private FloatingActionButton fabFavorite;
    private FloatingActionButton fabComment;
    private FloatingActionButton fabDirect;
    private TextView tvLikes;
    private boolean likeClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ivImage = findViewById(R.id.ivImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvCaption = findViewById(R.id.tvCaption);
        tvDate = findViewById(R.id.tvDate);
        fabComment = findViewById(R.id.fabComment);
        fabDirect = findViewById(R.id.fabDirect);
        fabFavorite = findViewById(R.id.fabFavorite);
        tvLikes = findViewById(R.id.tvLikes);

        Intent intent = getIntent();
        Post post = (Post) intent.getExtras().get("PostDetails");

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        if(post.getLikes() != null && post.getLikes().intValue() != 0) {
            tvLikes.setText(post.getLikes().intValue() + " likes");
        }
        ParseFile file = post.getImage();
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                ivImage.setImageBitmap(bitmap);
            }
        });

        setLikeListener(post);
        setCommentLister(post);
    }

    private void setCommentLister(final Post post) {
        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(PostDetailActivity.this, CommentActivity.class);
                commentIntent.putExtra("postId", post.getObjectId());
                commentIntent.putExtra("postCaption", post.getDescription());
                commentIntent.putExtra("username",post.getUser().getUsername());
                PostDetailActivity.this.startActivity(commentIntent);
            }
        });
    }

    //sets up like listener and updates backend to reflect number of likes
    private void setLikeListener(final Post post) {
        likeClick = false;
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!likeClick) {
                    fabFavorite.setColorFilter(PostDetailActivity.this.getResources().getColor(R.color.medium_red));
                    fabFavorite.setImageDrawable(ContextCompat.getDrawable(PostDetailActivity.this, R.drawable.ic_like_fill));
                    if(post.getLikes() == null || post.getLikes().intValue() == 0) {
                        post.setLikes(1);
                    } else {
                        post.setLikes(post.getLikes().intValue() + 1);
                    }
                } else {
                    fabFavorite.setColorFilter(Color.DKGRAY);
                    fabFavorite.setImageDrawable(ContextCompat.getDrawable(PostDetailActivity.this, R.drawable.ic_like));
                    post.setLikes(post.getLikes().intValue() - 1);
                }
                if(post.getLikes().intValue() == 0) {
                    tvLikes.setText(null);
                } else {
                    tvLikes.setText(post.getLikes().intValue() + " likes");
                }
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Log.d("SaveLikes", "Save successful");
                        } else {
                            Log.d("SaveLikes", "Save failed");
                            e.printStackTrace();
                        }
                    }
                });
                likeClick = !likeClick;
            }
        });
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
}
