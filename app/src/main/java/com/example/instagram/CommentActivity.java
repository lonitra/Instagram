package com.example.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instagram.model.Comment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btnSend;
    private String postId;
    private RecyclerView rvComments;
    private List<Comment> comments;
    private CommentAdapter adapter;
    private TextView tvCaption;
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        tvCaption = findViewById(R.id.tvCaption);
        tvCaption.setText(getIntent().getStringExtra("postCaption"));
        tvUsername = findViewById(R.id.tvUsername);
        String username = getIntent().getStringExtra("username");
        tvUsername.setText(username);
        etComment = findViewById(R.id.etComment);
        btnSend = findViewById(R.id.btnSend);
        postId = getIntent().getStringExtra("postId");
        rvComments = findViewById(R.id.rvComments);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(this, comments, getIntent().getStringExtra("postCaption"));
        rvComments.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);

        sendButtonClickSetUp();
        populateComments();

    }

    private void populateComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo("postId", postId);
        query.include("user");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if(e == null) {
                    comments.addAll(objects);
                    adapter.notifyItemInserted(0);
                    rvComments.scrollToPosition(0);
                } else {
                    Log.d("HomeActivity", "get post failed");
                }
            }
        });
    }

    private void sendButtonClickSetUp() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComment();
            }
        });
    }

    private void createComment() {
        Comment newComment = new Comment();
        newComment.setText(etComment.getText().toString());
        newComment.setPostId(postId);
        newComment.setUser(ParseUser.getCurrentUser());

        newComment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.d("CommentActivity", "Comment saved successfully");
                } else {
                    Log.d("CommentActivity", "Comment save failed");
                    e.printStackTrace();
                }
            }
        });
        etComment.setText(null);
        adapter.clear();
        populateComments();
    }
}

/*

1. Get creating comment working in the normal way. Figure out why it's asking for ParseObject.create
2. After comment creation, reload RecyclerList
3. Make query for finding comments by post ID
4. On entering CommentActivity, make request for all comments matching post ID
5. What is `return this` on the queries?
 */
