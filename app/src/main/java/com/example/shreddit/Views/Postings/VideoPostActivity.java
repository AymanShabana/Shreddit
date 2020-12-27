package com.example.shreddit.Views.Postings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostingViewModel;
import com.example.shreddit.databinding.ActivityVideoPostBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.util.Date;

public class VideoPostActivity extends AppCompatActivity {
    private ActivityVideoPostBinding binding;
    private PostingViewModel postingViewModel;
    private Uri videoUri;
    private String videoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);
        binding = ActivityVideoPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        postingViewModel = new ViewModelProvider(this).get(PostingViewModel.class);
        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
//        new VideoPicker.Builder(VideoPostActivity.this)
//                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
//                .directory(VideoPicker.Directory.DEFAULT)
//                .extension(VideoPicker.Extension.MP4)
//                .enableDebuggingMode(true)
//                .build();
        showVideoChooserDialog();
    }
    private void showVideoChooserDialog() {

        final CharSequence[] options = { "From Camera", "From Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.mp4");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("From Gallery")) {
                    // Intent intent = new
                    // Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // intent.setType("image/*");
                    // startActivityForResult(Intent.createChooser(intent,
                    // "Select File"),2);

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(intent, 2);
                    //
                    // Intent photoPickerIntent = new
                    // Intent(Intent.ACTION_PICK);
                    // photoPickerIntent.setType("image/*");
                    // startActivityForResult(photoPickerIntent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void upload() {
        if(videoUri != null && !binding.subName.getText().toString().isEmpty() && !binding.titleTxt.getText().toString().isEmpty()){
            binding.progressBar.setVisibility(View.VISIBLE);
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Videos").child(System.currentTimeMillis()+".mp4");
            StorageTask uploadTask = filePath.putFile(videoUri);
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
                    videoUrl = downloadUri.toString();
                    String board = binding.subName.getText().toString();
                    String title = binding.titleTxt.getText().toString();
                    Post post = new Post("",title,title.toUpperCase(),board,"https://",videoUrl,"",0,0,new Date().getTime()/1000,videoUrl,"","video");
                    postingViewModel.insert(post,new MyCallbackInterface(){
                        @Override
                        public void onAuthFinished(String result) {
                            binding.progressBar.setVisibility(View.GONE);
                            if(result.equals("success")){
                                //Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                            else{
                                binding.progressBar.setVisibility(View.GONE);
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
            Toast.makeText(this, "No video selected.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
//            videoUri=Uri.parse(mPaths.get(0));
//            binding.videoPlayer.setVideoPath(mPaths.get(0));
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(binding.videoPlayer);
//            binding.videoPlayer.setMediaController(mediaController);
//
//        }
//        else
//        {
//            Toast.makeText(this, resultCode+"", Toast.LENGTH_SHORT).show();
//        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.mp4")) {
                        f = temp;
                        break;
                    }
                }
                videoUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.shreddit", f);
                Log.d("SelectedVideo", videoUri.toString());
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                videoUri = selectedImage;
                binding.videoPlayer.setVideoPath(videoUri.toString());
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(binding.videoPlayer);
                binding.videoPlayer.setMediaController(mediaController);
                Log.d("SelectedVideo", videoUri.toString());
            }

        }
    }
}