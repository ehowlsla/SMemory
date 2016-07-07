package com.myspringway.secretmemory.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.model.Comment;
import com.myspringway.secretmemory.model.Member;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends Activity {

    private static final String TAG = "CommentActivity";

    @BindView(R.id.comment_text)
    EditText mCommentText;

    @BindView(R.id.comment_btn)
    SubmitProcessButton mCommentBtn;

    @BindView(R.id.recycler_comments)
    RecyclerView mCommentRecycler;

    private DatabaseReference mCommentRef;
    private CommentAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        initFirebase();
    }

    private void initFirebase() {
        mCommentRef = FirebaseDatabase.getInstance().getReference().child("post-comments");
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView authorView;
        public TextView bodyView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.comment_author);
            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new CommentAdapter(this, mCommentRef);
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private Context mContext;
        private DatabaseReference mDatabaseRef;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseRef = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        mComments.set(commentIndex, newComment);

                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // TODO: 로직 추가 필요
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments", Toast.LENGTH_SHORT).show();
                }
            };

            ref.addChildEventListener(childEventListener);
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.com_author);
            holder.bodyView.setText(comment.com_text);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseRef.removeEventListener(mChildEventListener);
            }
        }
    }

    @OnClick(R.id.comment_btn)
    public void onCommnetBtnClicked(View v) {
        mCommentBtn.setProgress(0);
        final String uid = getUid();
        mCommentBtn.setProgress(10);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mCommentBtn.setProgress(30);
                        String authorName = getUid();
                        mCommentBtn.setProgress(40);
                        String commentText = mCommentText.getText().toString();
                        mCommentBtn.setProgress(50);
                        Comment comment = new Comment(uid, authorName, commentText);
                        mCommentBtn.setProgress(60);

                        mCommentRef.push().setValue(comment);
                        mCommentBtn.setProgress(70);
                        mCommentText.setText(null);
                        mCommentBtn.setProgress(99);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mCommentBtn.setProgress(-1);
                        Log.e(TAG, databaseError.getMessage().toString());
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.cleanupListener();
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
