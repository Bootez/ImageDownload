package com.example.imagedownload;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    
    final static int SUCCESS = 0;
    private Button button;
    private ImageView imageView;
    private Bitmap bm;
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                if (bm != null) {
                    imageView.setImageBitmap(bm);
                }
                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.image);
        button = (Button)findViewById(R.id.button);
        
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        bm = getBitmapFromURL("http://www.baidu.com/img/bdlogo.gif");
                        Message msg = new Message();
                        msg.what = SUCCESS;
                        mHandler.sendMessage(msg);
                    };
                }.start();
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    Bitmap getBitmapFromURL(String addr) {
        try {
            
            URL url = new URL(addr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(inputStream);
            
            return bm;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
