package com.example.bilalhussain.winreward;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoard extends AppCompatActivity {


    public static TextView score_text, tv_info, wallet_text;
    LinearLayout spin_button, logout_btn, redeem_button, watch_video_buuton, share_id, rate_button,play_button;


    private RewardedVideoAd mRewardedVideoAd;
    InterstitialAd mInterstitialAd;


    int myIntValue;
    DatabaseReference user_id_child;
    String user_id;
    DatabaseReference databaseReference;
    FirebaseUserAdapter firebaseUserAdapter;
    String ph;
    int points;
    int w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        firebaseUserAdapter = new FirebaseUserAdapter();
        spin_button = findViewById(R.id.spin_linear_id);
        logout_btn = findViewById(R.id.linear_logout_id);
        wallet_text = findViewById(R.id.wallet_text_score_id);
        watch_video_buuton = findViewById(R.id.linear_watch_video_id);
        tv_info = findViewById(R.id.tv_inf);
        redeem_button=findViewById(R.id.linear_redeem_id);
        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-7444905396856865/7866740673");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
       // redeem_button.setVisibility(LinearLayout.GONE);
        mInterstitialAd.show();


      //  MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);


        SharedPreferences prefs = getSharedPreferences("LOGIN", MODE_PRIVATE);
        ph = prefs.getString("phone", "");

        getuser(ph);
        SharedPreferences prefscore = getSharedPreferences("SCR", MODE_PRIVATE);
        w = prefscore.getInt("score", 0);


        spin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, Spinwheel.class));

            }
        });

        mRewardedVideoAd.loadAd("ca-app-pub-7444905396856865/8056590161",
                new AdRequest.Builder().build());
        watch_video_buuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashBoard.this, "Loading Video", Toast.LENGTH_SHORT).show();
                if(isNetworkStatusAvialable (getApplicationContext())) {
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();

                        mRewardedVideoAd.loadAd("ca-app-pub-7444905396856865/8056590161",
                                new AdRequest.Builder().build());

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "internet is not avialable try again", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
            //    Toast.makeText(getBaseContext(), "Ad triggered reward.", Toast.LENGTH_SHORT).show();
                }

            @Override
            public void onRewardedVideoAdLoaded() {
            //    Toast.makeText(getBaseContext(), "Ad loaded wait ..........", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
            //    Toast.makeText(getBaseContext(), "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
             //   Toast.makeText(getBaseContext(), "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
             //   Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();

                mRewardedVideoAd.loadAd("ca-app-pub-7444905396856865/8056590161",
                        new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
              //  Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();

                mRewardedVideoAd.loadAd("ca-app-pub-7444905396856865/8056590161",
                        new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
            //    Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();

                mRewardedVideoAd.loadAd("ca-app-pub-7444905396856865/8056590161",
                        new AdRequest.Builder().build());

            }

            @Override
            public void onRewardedVideoCompleted() {
               // Toast.makeText(DashBoard.this, "Completed", Toast.LENGTH_SHORT).show();
                firebaseUserAdapter.addpoints(ph,points+w+1000);
                SharedPreferences.Editor editor = getSharedPreferences("SCR", MODE_PRIVATE).edit();
                wallet_text.setText("0");
                editor.putInt("score", 0);
                editor.apply();

                mRewardedVideoAd.loadAd("ca-app-pub-7444905396856865/8056590161",
                        new AdRequest.Builder().build());

            }
        });

redeem_button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(DashBoard.this,Redeem.class));
    }
});
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
                editor.putInt("flg", 0);
                editor.putString("phone", "");
                editor.apply();
                startActivity(new Intent(DashBoard.this, Login.class));
                finish();
            }
        });
    }

    public String getuser(final String phone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + phone);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = dataSnapshot.child("Name").getValue(String.class);
                String bal = dataSnapshot.child("Total Balance").getValue(String.class);
                points = dataSnapshot.child("Points").getValue(Integer.class);
           //     tv_info.setText(name + "(" + phone + ")\n" + "Balance: " + bal + "$"+" Points: "+points);
                tv_info.setText(name + "(" + phone + ")\n" +"Points: "+points);
                wallet_text.setText(points + " Points");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        return phone;
    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }

}