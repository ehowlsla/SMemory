package com.myspringway.secretmemory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.CommentActivity;
import com.myspringway.secretmemory.activity.MainActivity;
import com.myspringway.secretmemory.activity.WriteActivity;
import com.myspringway.secretmemory.cardstack.SwipeDeck;
import com.myspringway.secretmemory.cardstack.SwipeFrameLayout;
import com.myspringway.secretmemory.fragment.adapter.SwipeDeckAdapter;
import com.myspringway.secretmemory.model.Post;
import com.myspringway.secretmemory.viewholder.PostViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardFragment extends Fragment {

    private static final String TAG = "CardFragment";
    public static final String EXTRA_POST_KEY = "post_key";

    private List<Post> data;
    private DatabaseReference mPostReference;
    private String mPostkey;

    private List<String> testData;
    private DatabaseReference mDataRef;
    private SwipeDeckAdapter adapter;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
//    private int i;

//    @BindView(R.id.frame)
//    SwipeFlingAdapterView list;

    @BindView(R.id.swipe_deck)
    SwipeDeck swipe_deck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostkey = mPostReference.getKey();

        data = new ArrayList<>();

        if (data.size() == 0) {
            // TODO: 데이터가 없을 때 로직 추가, 아래는 테스트용 더미 데이터
            data.add(new Post(getEmail(), "imgUri", "bodyText"));
            data.add(new Post(getEmail(), "imgUri", "bodyText"));
            data.add(new Post(getEmail(), "imgUri", "bodyText"));
        }

        Query queryRef = mPostReference.orderByChild("posts");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post newPost = dataSnapshot.getValue(Post.class);
                data.add(new Post(newPost.author, newPost.pos_imgData, newPost.pos_content));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new SwipeDeckAdapter(data, getContext(), getLayoutInflater(savedInstanceState));
        swipe_deck.setAdapter(adapter);

        swipe_deck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
//                Log.i(TAG, "cardActionDown");
            }

            @Override
            public void cardActionUp() {
//                Log.i(TAG, "cardActionUp");
            }

        });

        return view;
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void onLikeClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.likes.containsKey(getUid())) {
                    p.numOfLike = --p.numOfLike;
                    p.likes.remove(getUid());
                } else {
                    p.numOfLike = ++p.numOfLike;
                    p.likes.put(getUid(), true);
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    public void resetCardPosition() {
        if(swipe_deck != null) {
            swipe_deck.resetCardPosition();
        }
    }

    @OnClick(R.id.write)
    void goWrite() {
        Intent intent = new Intent(getActivity(), WriteActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    private String getEmail() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return userEmail;
    }
}
