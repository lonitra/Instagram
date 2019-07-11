package com.example.instagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.instagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    private Button btnPost;
    private ImageView ivPreview;
    private EditText etCaption;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnPost = findViewById(R.id.btnPost);
        ivPreview = findViewById(R.id.ivPreview);
        etCaption = findViewById(R.id.etCaption);
        photoFile = (File) getIntent().getExtras().get("image");
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        ivPreview.setImageBitmap(takenImage);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description = etCaption.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                final File file = new File (String.valueOf(photoFile));
                final ParseFile parseFile = new ParseFile(file);
                createPost(description, parseFile, user);
            }
        });

    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);
        newPost.setLikes(0);
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.d("CreatePost", "Post successful");
                    finish();
                } else {
                    Log.d("CreatePost", "Post failed");
                    e.printStackTrace();
                }
            }
        });

    }
}
