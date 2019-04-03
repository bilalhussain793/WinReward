package com.example.bilalhussain.winreward;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Spinwheel extends AppCompatActivity {
    int n;

    ImageView imageView_wheel;
    ImageButton imageButton_spin;

    int degree = 0;
    int degree_old = 0;

    Random r;
    int score ;


    public static final float FACTOR = 15f;
    InterstitialAd mInterstitialAd;

    TextView textView,tv2;
    String user_id;

    int intValue;
    int current_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinwheel);
        imageView_wheel = (ImageView) findViewById(R.id.wheel);
        imageButton_spin = (ImageButton) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textview);
        tv2=findViewById(R.id.textview1);

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-7444905396856865/7866740673");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        SharedPreferences prefs = getSharedPreferences("SCR", MODE_PRIVATE);
        final int w = prefs.getInt("score", 0);
        score=w;


        r = new Random();

        imageButton_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                degree_old = degree % 360;

                degree = r.nextInt(3600) + 720;

                RotateAnimation rotateAnimation = new RotateAnimation(degree_old, degree,
                        RotateAnimation.RELATIVE_TO_SELF, .5f,
                        RotateAnimation.RELATIVE_TO_SELF, .5f);


                rotateAnimation.setDuration(3600);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());

                rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        textView.setText("score");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {


                        AdRequest adRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adRequest);

                        n = intValue + score;
                        textView.setText( currentNumber(360 - (degree % 360)));
                        tv2.setText(currentNumber(360 - (degree % 360)));


                        SharedPreferences editor = getSharedPreferences("LOGIN", MODE_PRIVATE);
                        String phone=editor.getString("phone", "");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference("users/"+phone);

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                int value = dataSnapshot.child("Points").getValue(Integer.class);
                                myRef.child("Points").setValue(value+(Integer.parseInt(textView.getText().toString())));
                                textView.setText(""+0);

                           //     Toast.makeText(Spinwheel.this, ""+currentNumber(360 - (degree % 360)), Toast.LENGTH_SHORT).show();
                                finish();

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value

                            }
                        });

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView_wheel.startAnimation(rotateAnimation);


            }
        });
    }
    private String currentNumber(int degree){

        String text = "";



        if(degree>= (FACTOR*1) && degree<(FACTOR*3)  ){

            text = "2";

            score = score+2;


        }


        if(degree>= (FACTOR*3) && degree<(FACTOR*5)  ){

            text = "3";
            score = score+3;
        }

        if(degree>= (FACTOR*5) && degree<(FACTOR*7)  ){

            text = "10";
            score = score+10;
        }

        if(degree>= (FACTOR*7) && degree<(FACTOR*9)  ){

            text = "5";
            score = score+5;

        }

        if(degree>= (FACTOR*9) && degree<(FACTOR*11)  ){

            text = "6";
            score = score+6;
        }

        if(degree>= (FACTOR*11) && degree<(FACTOR*13)  ){

            text = "7";
            score = score+7;
        }

        if(degree>= (FACTOR*13) && degree<(FACTOR*15)  ){

            text = "8";
            score = score+8;
        }

        if(degree>= (FACTOR*15) && degree<(FACTOR*17)  ){

            text = "9";
            score = score+9;
        }

        if(degree>= (FACTOR*17) && degree<(FACTOR*19)  ){

            text = "100";
            score = score+100;
        }

        if(degree>= (FACTOR*19) && degree<(FACTOR*21)  ){

            text = "11";
            score = score+11;
        }

        if(degree>= (FACTOR*21) && degree<(FACTOR*23)  ){

            text = "12";
            score = score+12;
        }

        if(degree>= (FACTOR*23) && degree<(360) || degree>=0 && degree <(FACTOR*1) ){

            text = "0 point";

        }

        return text;



    }
}
