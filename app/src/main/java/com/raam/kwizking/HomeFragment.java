package com.raam.kwizking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raam.kwizking.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentHomeBinding binding;
    FirebaseFirestore database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        final ArrayList<CategoryModel> categories = new ArrayList<>();

        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);

        database.collection("categories")
                .addSnapshotListener((value, error) -> {
                    categories.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        CategoryModel model = snapshot.toObject(CategoryModel.class);
                        model.setCategoryId(snapshot.getId());
                        categories.add(model);
                    }
                    adapter.notifyDataSetChanged();
                });


        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SpinnerActivity.class));
            }
        });


        binding.inviteFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    String inviteBody = "Download Kwiz King App, Play Quiz and Earn Money :- https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                    String inviteSub = "Kwiz King App";

                    share.putExtra(Intent.EXTRA_SUBJECT,inviteSub);
                    share.putExtra(Intent.EXTRA_TEXT,inviteBody);

                    startActivity(Intent.createChooser(share, "Invite Using"));
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}