package com.allanakshay.donboscoyouth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile_Pic extends AppCompatActivity  implements View.OnClickListener{

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    private String phonenumber;
    private String photo;


    private Button buttonChoose;
    private Button buttonUpload;

    private DatabaseReference ref;
    private FirebaseDatabase mfbdb;
     //ImageView
    private CircleImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    //firebase storage reference
    private StorageReference storageReference;

    private String newpos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__pic);
        storageReference = FirebaseStorage.getInstance().getReference();

        //getting views from layout
       buttonChoose = (Button) findViewById(R.id.profilepicchoose);
        buttonUpload = (Button) findViewById(R.id.profilepicupdate);
        imageView = (CircleImageView) findViewById(R.id.profilepicview);

        buttonChoose.setOnClickListener(this);

        buttonUpload.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void showFileChooser() {
        Intent intentfile = new Intent();
        intentfile.setType("image/*");
        intentfile.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentfile, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadFile() {


        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(Profile_Pic.this);
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            ref = mfbdb.getInstance().getReference().child("Youth_List");
            Query query = ref.orderByChild("Username").equalTo(LoginPage.userid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            newpos = snapshot.getKey();
                            phonenumber = snapshot.child("Phone Number").getValue().toString();
                            photo = snapshot.child("Imageurl").getValue().toString();

                        }
                        if (!photo.equals("")) {
                            StorageReference remove = storageReference.child(photo);
                            remove.delete();
                        }
                        ref.child(newpos).child("Imageurl").setValue("images/" + filePath.getLastPathSegment() + "_" + phonenumber);
                        StorageReference riversRef = storageReference.child("images/" + filePath.getLastPathSegment() + "_" + phonenumber);
                        riversRef.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //if the upload is successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying a success toast
                                        Toast.makeText(getApplicationContext(), "Profile Picture Successfully Uploaded", Toast.LENGTH_LONG).show();

                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //if the upload is not successfull
                                        //hiding the progress dialog
                                        progressDialog.dismiss();

                                        //and displaying error message
                                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //calculating progress percentage
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                        //displaying percentage in progress dialog
                                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                    }
                                });

                    } catch (Exception e) {}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        //if there is not any file
        else {
            Toast.makeText(getApplicationContext(), "Invalid File Chosen", Toast.LENGTH_SHORT).show();
        }


    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("id", LoginPage.userid);
        outState.putString("connection", CoverPage.connectedstate);
        outState.putString("fb_id", LoginPage.fb_user_id);
        outState.putString("image", ImageSet.setimage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        LoginPage.userid = savedInstanceState.getString("id");
        CoverPage.connectedstate = savedInstanceState.getString("connection");
        LoginPage.fb_user_id = savedInstanceState.getString("fb_id");
        ImageSet.setimage = savedInstanceState.getString("image");
    }
}
