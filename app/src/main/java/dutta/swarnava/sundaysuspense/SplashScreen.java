package dutta.swarnava.sundaysuspense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgView=findViewById(R.id.imgView);
//        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation_fade);
//        imgView.startAnimation(animation);

        Thread thread=new Thread(){

            @Override
            public void run() {
                try {
                    sleep(4000);
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}

