package com.example.shreddit.Views.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.User;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.R;
import com.example.shreddit.Utils.BioDialogFragment;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostViewModel;
import com.example.shreddit.ViewModels.ProfileViewModel;
import com.example.shreddit.Views.Adapters.PostAdapter;
import com.example.shreddit.Views.PostDetailsActivity;
import com.example.shreddit.Views.Postings.ImagePostActivity;
import com.example.shreddit.databinding.FragmentHomeBinding;
import com.example.shreddit.databinding.FragmentNotifBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, BioDialogFragment.NoticeDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProfileViewModel mProfileViewModel;
    private PostAdapter adapter;
    private FragmentNotifBinding binding;
    private Uri imageUri;
    private String imageUrl;
    private String newBio;
    public NotifFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifFragment newInstance(String param1, String param2) {
        NotifFragment fragment = new NotifFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotifBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_profile);
        adapter = new PostAdapter(getContext(),new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                //Toast.makeText(ViewRequestsActivity.this, request.getRequesterId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PostDetailsActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.progressBarSubs.setVisibility(View.VISIBLE);
        mProfileViewModel.sendAdapter(adapter,binding.progressBarSubs);
        adapter.setPosts(mProfileViewModel.getAllMyPosts());

        binding.userProfileName.setText(UserFirebaseModel.mUser.getUsername());
        if(!UserFirebaseModel.mUser.getBio().isEmpty())
            binding.userProfileShortBio.setText(UserFirebaseModel.mUser.getBio());
        if(!UserFirebaseModel.mUser.getImageUrl().isEmpty()){
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            imageLoader.displayImage(UserFirebaseModel.mUser.getImageUrl(), binding.userProfilePhoto, options);
        }
        binding.dropDownOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        return view;
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_items, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile_img:
                Intent intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.profile_bio:
                showBioDialog();
                return true;
            default:
                return false;
        }
    }

    private void showBioDialog() {
        DialogFragment dialog = BioDialogFragment.newInstance(this);
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "BioDialogFragment");
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Log.i("ProfilePic","Successfull request.");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri=result.getUri();
            binding.userProfilePhoto.setImageURI(imageUri);
            upload();
        }
        else
        {
            Log.i("ProfilePic","Try again.");
        }
    }

    private void updateBio(){
        if(newBio != null){
            User user = UserFirebaseModel.mUser;
            user.setBio(newBio);
            mProfileViewModel.updateProfile(user,new MyCallbackInterface(){
                @Override
                public void onAuthFinished(String result) {
                    if(result.startsWith("success")){
                        Toast.makeText(getContext(), "Bio updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Error: "+result, Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }
    private void upload() {
        if(imageUri != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("UserImages").child(UserFirebaseModel.mUser.getUsername_c()+"."+getFileExtension(imageUri));
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
                    User user = UserFirebaseModel.mUser;
                    user.setImageUrl(imageUrl);
                    mProfileViewModel.updateProfile(user,new MyCallbackInterface(){
                        @Override
                        public void onAuthFinished(String result) {
                            if(result.startsWith("success")){
                                Toast.makeText(getContext(), "Picture updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Error: "+result, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Fill all the requirements.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        newBio = ((TextView) dialog.getDialog().findViewById(R.id.bio_text_area)).getText().toString();
        binding.userProfileShortBio.setText(newBio);
        updateBio();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();

    }
}