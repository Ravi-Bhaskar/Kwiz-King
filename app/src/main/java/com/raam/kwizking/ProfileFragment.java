package com.raam.kwizking;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.raam.kwizking.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Intent intent;
    StorageReference storageReference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        intent = new Intent(getActivity(), LoginActivity.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + auth.getCurrentUser().getUid() + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(binding.profileImage);
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });


        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                binding.nameBoxx.setText(String.valueOf(user.getName()));
                binding.emailBox.setText(String.valueOf(user.getEmail()));
                binding.passwordBox.setText(String.valueOf(user.getPass()));

                binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.signOut();
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //profileImageView.setImageURI(imageUri);
                Toast.makeText(getActivity(), "Uploading Please Wait...", Toast.LENGTH_SHORT).show();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/"+auth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(binding.profileImage);
                        Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}