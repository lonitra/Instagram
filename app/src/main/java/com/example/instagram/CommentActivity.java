package com.example.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;

public class CommentActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        etComment = findViewById(R.id.etComment);
        btnSend = findViewById(R.id.btnSend);

        sendButtonClickSetUp();

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
        ParseObject object = ParseObject.create("Comment");
        //Comment newComment = new Comment();
//        newComment.setText(etComment.getText().toString());
//        newComment.setPostId(getIntent().getStringExtra("postId"));
//        newComment.setUser(ParseUser.getCurrentUser());
//
//        newComment.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null) {
//                    Log.d("CommentActivity", "Comment saved successfully");
//                } else {
//                    Log.d("CommentActivity", "Comment save failed");
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}

/*

1. Get creating comment working in the normal way. Figure out why it's asking for ParseObject.create
2. After comment creation, reload RecyclerList
3. Make query for finding comments by post ID
4. On entering CommentActivity, make request for all comments matching post ID
5. What is `return this` on the queries?
 */
