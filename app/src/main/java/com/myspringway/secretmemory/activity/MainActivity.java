package com.myspringway.secretmemory.activity;

import android.os.Bundle;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.fragment.CardFragment;
import com.myspringway.secretmemory.fragment.adapter.SwipeDeckAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    private static final String TAG = "MainActivity";

    public static final String EXTRA_POST_KEY = "post_key";

//    private ArrayList<String> testData;
    private SwipeDeckAdapter adapter;

    private CardFragment fragment;
    private String mPostKey;
    private ArrayList testData;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mPostRef;

    public MainActivity() {
        super(R.string.title_bar_slide);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
//        if (mPostKey == null) {
//            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
//        }

        fragment = new CardFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Post post = dataSnapshot.getValue(Post.class);
//
//                mainText.setText(post.pos_content);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                Toast.makeText(MainActivity.this, "정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        mPostRef.addValueEventListener(postListener);
//
//        mPostListener = postListener;
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if (mPostListener != null) {
//            mPostRef.removeEventListener(mPostListener);
//        }
//    }
//


    @OnClick(R.id.menu)
    void goMenuClick() {
        toggle();
        fragment.resetCardPosition();
    }

//    @Override
//    public void getSlidingMenu() {
//        super.getSlidingMenu();
//        fragment.resetCardPosition();
//    }
}


