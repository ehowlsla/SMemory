package com.myspringway.secretmemory.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.myspringway.secretmemory.dialog.PopupLoading;
import com.myspringway.secretmemory.helper.SharedPreferenceHelper;
import com.myspringway.secretmemory.library.ImageUtils;
import com.myspringway.secretmemory.library.SoftKeyboard;
import com.myspringway.secretmemory.model.Post;
import com.myspringway.secretmemory.view.DynamicTag;

import org.apmem.tools.layouts.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission;

public class WriteActivity extends Activity {

    private static final String TAG = WriteActivity.class.getSimpleName();

    private static final int GALLERY_SELECT = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 20;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    private PopupLoading popupLoading;
    private List<DynamicTag> dynamicTagList;

    private boolean isSave = true;
    private boolean isImageUpload = false;

    private String picturePath = "";
    private Uri sourceUri = null;
    private Uri mDownloadUrl = null;

    @BindView(R.id.bg)
    ImageView bg;

    @BindView(R.id.close)
    ImageView close;

    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.body)
    EditText body;

    @BindView(R.id.tag_body)
    EditText tag_body;

    @BindView(R.id.album)
    ImageView album;

    @BindView(R.id.hash)
    TextView hash;

    @BindView(R.id.toolbar_layout)
    RelativeLayout toolbar_layout;

    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    @BindView(R.id.tag_values)
    FlowLayout tag_values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ButterKnife.bind(this);

        initFirebase();
        initObject();

        // TODO: 이미지가 null일 경우 자동 처리 로직 추가
        save.setEnabled(false);

        dynamicTagList = new ArrayList<>();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void initObject() {
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        SoftKeyboard softKeyboard = new SoftKeyboard(root_layout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                goTagEditHide();
            }

            @Override
            public void onSoftKeyboardShow() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tag_body.isFocused() && tag_body.getText().toString().length() == 0) {
                            tag_body.setText("#");
                            tag_body.setSelection(tag_body.getText().toString().length());
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.close)
    void onClose() {
        goFinish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.album)
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
        return ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void openDialogToGetPermission() {
        ActivityCompat.requestPermissions(this, new String[]{permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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
        bg.setImageBitmap(resize);
    }

    @OnClick(R.id.save)
    public void goSave() {
        if (isValidTag()) { goTagEditHide(); }
        startLoading();
        if (isEmptyBody(body)) {
            stopLoading();
            Toast.makeText(WriteActivity.this, getResources().getString(R.string.error_write_text_null), Toast.LENGTH_SHORT).show();
            return;
        }
        uploadFromUri(sourceUri);
        final String memberId = getMemberUid();
        mDatabaseRef.child("members").child(memberId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mDownloadUrl == null) {
                            // TODO: 사용자가 이미지를 설정하지 않은 경우 로직 추가 필요
                            writeNewPost("", body.getText().toString());

                        } else {
                            picturePath = mDownloadUrl.toString();
                            writeNewPost(picturePath, body.getText().toString());
                            stopLoading();
                            if (isSave) {
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "ValueEventListener:onCancelled:" + String.valueOf(databaseError.toException()));
                    }
                }
        );
    }

    private boolean isValidTag() {
        return ((tag_body.getText().toString().length() > 0) && (tag_values.getChildCount() == 0));
    }

    private boolean isEmptyBody(EditText editText) {
        return TextUtils.isEmpty(editText.getText());
    }

    private void goTagEditHide() {
        if (tag_body.getText().toString().length() == 0)
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tag_body.isFocused()) {
                    tag_values.removeAllViews();
                    dynamicTagList.clear();
                    String[] values = tag_body.getText().toString().replace("#", " ").split(" ");
                    String value = "";
                    for (String tag : values) {
                        if (tag.trim().length() <= 0) continue;
                        value += " #" + tag;
                        //add
                        DynamicTag view = new DynamicTag(getApplicationContext());
                        view.setValue("#" + tag, false);
                        dynamicTagList.add(view);
                        tag_values.addView(view.view);
                    }
                    tag_body.setText(value.trim());
                    tag_body.setSelection(tag_body.getText().toString().length());
                    tag_body.setVisibility(View.GONE);
                    tag_values.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void uploadFromUri(Uri fileUri) {
        if (fileUri == null) {
            return;
        }

        final StorageReference photoRef = mStorageRef.child("photos")
                .child(fileUri.getLastPathSegment());

        photoRef.putFile(fileUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        }
                        isImageUpload = true;
                        save.setEnabled(true);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDownloadUrl = null;
                        Toast.makeText(WriteActivity.this, getResources().getString(R.string.error_img_upload_fail), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getMemberUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void startLoading() {
        popupLoading = new PopupLoading();
        popupLoading.show(getFragmentManager(), "loading");
    }

    private void stopLoading() {
        if (popupLoading != null) popupLoading.dismiss();

    }

    private void writeNewPost(String img, String body) {
        // Create new post at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
        String key = mDatabaseRef.child("posts").push().getKey();
        Post post = new Post(mAuth.getCurrentUser().getEmail(), img, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);

        mDatabaseRef.updateChildren(childUpdates);
        isSave = true;
    }

    private void goFinish() {
        isSave = false;
        finish();
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, GALLERY_SELECT);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.error_permission_deny), Toast.LENGTH_SHORT).show();
                }
        }
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
            Log.e(TAG, "ResultError:" + e.getLocalizedMessage());
        }
    }

    @OnClick(R.id.hash)
    void goHash() {
        tag_body.setVisibility(View.VISIBLE);
        tag_values.setVisibility(View.GONE);

        String value = tag_body.getText().toString();
        if (value.length() != 0) tag_body.setText(value + " #");
        else tag_body.setText("#");
        tag_body.setSelection(tag_body.getText().toString().length());
        tag_body.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tag_body, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnTextChanged(R.id.tag_body)
    void goTagChange(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 0 && start + 1 <= s.toString().length() && " ".equals(s.toString().substring(start, start + 1))) {
            String value = tag_body.getText().toString();

            if (value.length() != 0) {
                tag_body.setText(value + " #");
            } else {
                tag_body.setText("#");
            }

            if (value.indexOf("#") != 0) {
                tag_body.setText("#" + tag_body.getText().toString());
            }
            tag_body.setSelection(tag_body.getText().toString().length());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isSave) {
            SharedPreferenceHelper.setValue(getApplicationContext(), "body", "");
            SharedPreferenceHelper.setValue(getApplicationContext(), "tag", "");
        } else {
            SharedPreferenceHelper.setValue(getApplicationContext(), "body", body.getText().toString());
            SharedPreferenceHelper.setValue(getApplicationContext(), "tag", tag_body.getText().toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        body.setText(SharedPreferenceHelper.getValue(getApplicationContext(), "body"));
        tag_body.setText(SharedPreferenceHelper.getValue(getApplicationContext(), "tag"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sourceUri == null) {
            return;
        }

        File temp = new File(sourceUri.getPath());
        if (temp.exists()) {
            temp.delete();
        }
    }

}

