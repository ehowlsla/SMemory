package com.myspringway.secretmemory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.WriteActivity;
import com.myspringway.secretmemory.cardstack.SwipeDeck;
import com.myspringway.secretmemory.fragment.adapter.SwipeDeckAdapter;
import com.myspringway.secretmemory.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardFragment extends Fragment {

    private static final String TAG = CardFragment.class.getSimpleName();

    private List<Post> data;
    private SwipeDeckAdapter adapter;

    @BindView(R.id.swipe_deck)
    SwipeDeck swipe_deck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        initFirebase();
        setSwipeSystem(savedInstanceState);

        return view;
    }

    private void initFirebase() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build();
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("msg_length_limit", 140L);

        remoteConfig.setConfigSettings(remoteConfigSettings);
        remoteConfig.setDefaults(defaultConfigMap);

        data = new ArrayList<>();

        mPostReference.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                            Post post = dataSnap.getValue(Post.class);
                            data.add(post);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "ValueEventListener: onCancelled: " + databaseError.getMessage());
                    }
                }
        );

        fetchConfig();
    }

    private void fetchConfig() {

    }

    public void resetCardPosition() {
        if(swipe_deck != null) {
            swipe_deck.resetCardPosition();
        }
    }

    private void setSwipeSystem(Bundle savedInstanceState) {
        adapter = new SwipeDeckAdapter(data, getContext(), getLayoutInflater(savedInstanceState));
        swipe_deck.setAdapter(adapter);
        swipe_deck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.d(TAG, "" + position);
                Log.d(TAG, "" + data.size());

            }

            @Override
            public void cardSwipedRight(int position) {
                Log.d(TAG, "" + position);
                Log.d(TAG, "" + data.size());

            }

            @Override
            public void cardsDepleted() {

            }

            @Override
            public void cardActionDown() {
            }

            @Override
            public void cardActionUp() {
            }

        });
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
                    // Unstar the post and remove self from stars
                    p.numOfLike = p.numOfLike - 1;
                    p.likes.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.numOfLike = p.numOfLike + 1;
                    p.likes.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction: onComplete:" + databaseError);
            }
        });
    }

    @OnClick(R.id.write)
    void goWrite() {
        Intent intent = new Intent(getActivity(), WriteActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}