package com.myspringway.secretmemory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.constants.AppConstant;
import com.myspringway.secretmemory.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityComment extends AppCompatActivity {

    private static final String TAG = ActivityComment.class.getSimpleName();

    @BindView(R.id.toolbar_bg)
    ImageView mToolbarBg;

    @BindView(R.id.toolbar_text)
    TextView mToolbarText;

    @BindView(R.id.comment_text)
    EditText mCommentText;

    @BindView(R.id.comment_btn)
    TextView comment_btn;
//    ActionProcessButton mCommentBtn;

    @BindView(R.id.recycler_comments)
    RecyclerView mCommentRecycler;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private DatabaseReference mCommentRef;
    private CommentAdapter mAdapter;

    private String imgUrl;
    private String txtContent;
    private String postId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        getIntentData(getIntent());

        setToolbar();

        initFirebase();

        Picasso
                .with(this)
                .load(imgUrl)
                .fit()
                .into(mToolbarBg);

        mToolbarText.setText(txtContent);
    }

    private void getIntentData(Intent intent) {
        imgUrl = intent.getStringExtra(AppConstant.IMG_URL);
        txtContent = intent.getStringExtra(AppConstant.TXT_CONTENT);
        postId = intent.getStringExtra(AppConstant.USER_CONTENT_ID);
    }

    private void setToolbar() {
        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Nullable
    private void initFirebase() {
        if (postId != null) {
            mCommentRef = FirebaseDatabase.getInstance().getReference().child(postId).child("post-comments");
        } else {
            Toast.makeText(this, "잘못된 데이터가 수신되었습니다.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "initFirebase: postId is null");
        }
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView authorView;
        TextView bodyView;

        CommentViewHolder(View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.comment_author);
            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new CommentAdapter(this, mCommentRef);
        mCommentRecycler.setAdapter(mAdapter);

        mCommentRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private Context mContext;
        private DatabaseReference mDatabaseRef;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        CommentAdapter(final Context context, DatabaseReference ref) {
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
//                    Comment movedComment = dataSnapshot.getValue(Comment.class);
//                    String commentKey = dataSnapshot.getKey();

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
        final String uid = getUid();
//        mCommentBtn.setProgress(50);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String authorName = getUid();
                        String commentText = mCommentText.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        mCommentRef.push().setValue(comment);
                        mCommentText.setText(null);
//                        mCommentBtn.setProgress(100);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        mCommentBtn.setProgress(-1);
                        Log.e(TAG, databaseError.getMessage());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(0, R.animator.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, R.animator.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, R.animator.fade_out);
    }
}
