package com.myspringway.secretmemory.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.library.ClearableEditText;
import com.myspringway.secretmemory.library.ImageUtils;
import com.myspringway.secretmemory.library.SoftKeyboard;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yoontaesup on 2016. 10. 18..
 */

public class JoinProfileActivity extends Activity {


    private static final int GALLERY_SELECT = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 20;

    @BindView(R.id.profile)
    CircleImageView profile;

    @BindView(R.id.nickname)
    ClearableEditText nickname;

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    private SoftKeyboard mSoftKeyboard;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    private String picturePath = "";
    private Uri sourceUri = null;
    private Uri mDownloadUrl = null;

    boolean isImageUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_profile);
        ButterKnife.bind(this);
        initFirebase();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initObject();
    }

    private void initObject() {
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        mSoftKeyboard = new SoftKeyboard(root_layout, im);
        mSoftKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                setVisibleBottom(true);
                updateNextEnable();
            }

            @Override
            public void onSoftKeyboardShow() {
                setVisibleBottom(false);
            }
        });
    }

    void updateNextEnable() {
        runOnUiThread(() -> {
            Boolean enable = nickname.getText().length() > 0;

            next.setEnabled(enable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                next.setBackground(getDrawable(enable == true ? R.drawable.green_click : R.color.black_20));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                next.setBackground(getResources().getDrawable(enable == true ? R.drawable.green_click : R.color.black_20));
            } else {
                next.setBackgroundDrawable(getResources().getDrawable(enable == true ? R.drawable.green_click : R.color.black_20));
            }
        });
    }

    private void setVisibleBottom(final boolean isVisible) {
        runOnUiThread(() -> {
            nickname.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            next.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        });
    }

    @OnClick(R.id.prev)
    void prevPage() {
        finish();
        overridePendingTransition(0, R.animator.fade_out);
    }


    @OnClick(R.id.next)
    void nextPage() {
        if (nickname.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.join_name_info, Toast.LENGTH_SHORT).show();
            return;
        }
//        presenter.getName(name.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoftKeyboard != null) mSoftKeyboard.unRegisterSoftKeyboardCallback();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.profile)
    public void goAlbum() {
        if (checkDeviceVersion()) {
            if (havePermissionYet()) {
                openDialogToGetPermission();
            }
        }
        openGalleryToGetImage();
    }

    private boolean checkDeviceVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean havePermissionYet() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void openDialogToGetPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void openGalleryToGetImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_SELECT);
    }

    private void copyImageToStorage(Intent data) {
        sourceUri = data.getData();
        File origin = ImageUtils.getImageFile(getApplicationContext(), sourceUri);
        sourceUri = ImageUtils.createTempFile();
        File temp = new File(sourceUri.getPath());
        ImageUtils.copyFile(origin, temp);

        Bitmap resize = ImageUtils.getImage(getApplicationContext(), sourceUri);
        ImageUtils.SaveBitmapToFileCache(resize, sourceUri.getPath());
        profile.setImageBitmap(resize);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case GALLERY_SELECT:
                        copyImageToStorage(data);
                        uploadFromUri(sourceUri);
                        break;
                }
            }
        } catch (Exception e) {
//            Log.e(TAG, "ResultError:" + e.getLocalizedMessage());
        }
    }


    private void uploadFromUri(Uri fileUri) {
        if (fileUri == null) {
            return;
        }

        final StorageReference photoRef = mStorageRef.child("profile")
                .child(fileUri.getLastPathSegment());

        photoRef.putFile(fileUri)
                .addOnSuccessListener(this, taskSnapshot -> {
                    if (taskSnapshot.getMetadata() != null) {
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                    }
                    isImageUpload = true;
                    updateProfile();
//                        save.setEnabled(true);
                })
                .addOnFailureListener(this, e -> {
                    mDownloadUrl = null;
                        Toast.makeText(JoinProfileActivity.this, getResources().getString(R.string.error_img_upload_fail), Toast.LENGTH_SHORT).show();
                });
    }

    private String getMemberUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    void updateProfile() {
//        final String memberId = getMemberUid();
//        mDatabaseRef.child("members").child(memberId).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        picturePath = mDownloadUrl.toString();
//                        writeNewPost(picturePath, body.getText().toString());
//                        stopLoading();
//                        if (isSave) {
//                            finish();
//                        }
//
//                        if (mDownloadUrl == null) {
//                            // TODO: 사용자가 이미지를 설정하지 않은 경우 로직 추가 필요
//                            writeNewPost("", body.getText().toString());
//
//                        } else {
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e(TAG, "ValueEventListener:onCancelled:" + String.valueOf(databaseError.toException()));
//                    }
//                }
//        );
    }

}
