package com.catch32.zumpbeta.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.process.AppUserManager;

public class SplashActivity extends AppCompatActivity
{
   TextView lblzumplink;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            lblzumplink = findViewById(R.id.lblzumplink);

            String url = "https://www.zump.co.in";
            lblzumplink.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });

            AppUserManager.resetAppInfo(this);
            new Handler().
                    postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, AppConstant.SPLASH_DELAY);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
