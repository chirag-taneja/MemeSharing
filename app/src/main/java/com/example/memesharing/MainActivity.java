package com.example.memesharing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

ProgressBar pb;
ImageView img;
String mainUrl=null;
String url=null;
String prUrl=null;
ImageButton share;

private static final String tag="swipePosition";
private  float x1,x2,y1,y2;
private static int minDistance=150;
GestureDetector gt;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        img=findViewById(R.id.img);
gt=new GestureDetector(MainActivity.this,this);



        loadMeme();




    }
    @SuppressLint("WrongConstant")
    public void loadMeme()
    {
        pb=findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        // ...

        // Instantiate the RequestQueue.



        url ="https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        if (mainUrl!=null)
        {
            prUrl=mainUrl;
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                             mainUrl=response.getString("url");
                            Log.d("url", "onResponse: ");
                            img=findViewById(R.id.img);
                            Glide.with(MainActivity.this).load(mainUrl).into(img);
                            pb.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            System.out.println("error happen");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                    }
                });

        // Access the RequestQueue through your singleton class.

        
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gt.onTouchEvent(event);
        switch (event.getAction())
        {
            //starting with swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;
                //ending with swipe gesture
            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();


                float valueX=x2-x1;
                float valueY=y2-y1;


                if (Math.abs(valueY)>minDistance)
                 {
                     if (y2>y1)
                     {
                         prLoadMeme();
                     }
                     else
                     {
                         loadMeme();
                     }
                 }

        }




        return super.onTouchEvent(event);
    }
    public void prLoadMeme()
    {
        if (prUrl!=null) {
            img = findViewById(R.id.img);
            Glide.with(MainActivity.this).load(prUrl).into(img);
            pb.setVisibility(View.INVISIBLE);
        }

    }

    public void share(View view) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,mainUrl);
        intent.setType("text/plain");
        Intent chooser=Intent.createChooser(intent,null);
        startActivity(chooser);
    }
}
