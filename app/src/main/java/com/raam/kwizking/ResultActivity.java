package com.raam.kwizking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raam.kwizking.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    int POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //To get result of quiz answered questions by user

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        long points = correctAnswers * POINTS;

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));
        binding.earnedCoins.setText(String.valueOf(points));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        //After Result Comes By This Points Will Be Updated in Wallet

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(points));

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String body = "*"+
                        "Kwiz King App"+"*"+"\n\n"+
                        "My Score = "+
                        getIntent().getIntExtra("correct", 0)+
                "/"+getIntent().getIntExtra("total", 0)+"\n"+
                        "Earned Coins = "+
                        getIntent().getIntExtra("points", correctAnswers * POINTS)+"\n\n"+
                        "Hey, I Am Playing Quiz and Earning Money...\nDownload Kwiz King App Now, Play Quiz and Earn Money :- https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Kwiz King");
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });

        binding.restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

        //binding.shareBtn.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View v) {
                 //   Intent share = new Intent(Intent.ACTION_SEND);
                  //  share.setType("text/plain");
                 //   String inviteBody = "Download Kwiz King App, Play Quiz and Earn Money :- https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                 //   String inviteSub = "Kwiz King App";

                 //   share.putExtra(Intent.EXTRA_SUBJECT,inviteSub);
                  //  share.putExtra(Intent.EXTRA_TEXT,inviteBody);

                  //  startActivity(Intent.createChooser(share, "Invite Using"));

     //       }
     //   });

    }
}