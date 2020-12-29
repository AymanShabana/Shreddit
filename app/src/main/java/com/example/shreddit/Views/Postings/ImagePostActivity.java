package com.example.shreddit.Views.Postings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostingViewModel;
import com.example.shreddit.databinding.ActivityImagePostBinding;
import com.example.shreddit.databinding.ActivityLinkPostBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ImagePostActivity extends AppCompatActivity {
    private ActivityImagePostBinding binding;
    private PostingViewModel postingViewModel;
    private Uri imageUri;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);
        binding = ActivityImagePostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        postingViewModel = new ViewModelProvider(this).get(PostingViewModel.class);
        CropImage.activity().start(ImagePostActivity.this);
        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
    }
    private void upload() {
        if(imageUri != null && !binding.subName.getText().toString().isEmpty() && !binding.titleTxt.getText().toString().isEmpty()){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.postBtn.setVisibility(View.GONE);
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("PostImages").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            StorageTask uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    else
                    {
                        return filePath.getDownloadUrl();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Uri downloadUri = (Uri) task.getResult();
                    imageUrl = downloadUri.toString();
                    String board = binding.subName.getText().toString();
                    String title = binding.titleTxt.getText().toString();
                    Post post = new Post("",title,title.toUpperCase(),board,"https://",imageUrl,"",0,0,new Date().getTime()/1000,imageUrl,"","image");
                    postingViewModel.insert(post,new MyCallbackInterface(){
                        @Override
                        public void onAuthFinished(String result) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.postBtn.setVisibility(View.VISIBLE);
                            if(result.equals("success")){
                                //Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                            else{
                                binding.progressBar.setVisibility(View.GONE);
                                binding.postBtn.setVisibility(View.VISIBLE);
                                //Snackbar.make(binding.parentLayout, "Error: "+result, Snackbar.LENGTH_LONG).show();
                            }
                        }

                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        else{
            Toast.makeText(this, "Fill all the requirements.", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri=result.getUri();
            binding.postImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}