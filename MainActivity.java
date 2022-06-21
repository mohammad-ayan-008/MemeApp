package com.mycompany.application;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;

import org.json.JSONObject;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import java.lang.reflect.Method;
import com.android.volley.Request;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import android.util.Log;
import android.graphics.Matrix;
import android.content.Intent;
import android.widget.ProgressBar;
import java.io.FileOutputStream;
import android.icu.util.Calendar;
import java.net.URL;
import java.io.BufferedInputStream;
  

public class MainActivity extends AppCompatActivity {
   public ImageView vss; 
	public TextView vwu;
	 public String  link;
	public ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	 vss= findViewById(R.id.ImgV);
	vwu=	findViewById(R.id.text);
	 bar = findViewById(R.id.prog);
		bar.setVisibility(ProgressBar.GONE);
	 Button b= findViewById(R.id.BTN_Next);
	Button b2= findViewById(R.id.BTNShare);
	
	  b.setOnClickListener(new View.OnClickListener(){
		  public void onClick(View v){
		bar.setVisibility(ProgressBar.VISIBLE);
		getMeme();
		  }
	  });
	  
		b2.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v){
					
					Intent i= new Intent(Intent.ACTION_SEND);
					 i.setType("text/plain");
					   i.putExtra(Intent.EXTRA_TEXT,link);
					 startActivity(i);
		}
			});
    }
	
    
	public void getMeme(){
		String url = "https://meme-api.herokuapp.com/gimme";
	RequestQueue rqs= Volley.newRequestQueue(MainActivity.this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
		 try{
					vwu.setText("Response: "+response.getString("title"));
				
			
				String us= response.getString("url");
			       link=us;
				new DownloadImageTask(vss).execute(us);
				   
			}catch(Exception e){
				Toast.makeText(MainActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
				
			}
			
			}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO: Handle error
Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
				}
			});
  //: MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
			rqs.add(jsonObjectRequest);
// Access the RequestQueue through your singleton class.
		

/*don't forget to subscribe my YouTube channel for more Tutorial and mod*/
/*
https://youtube.com/channel/UC_lCMHEhEOFYgJL6fg1ZzQA */
   }
   
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;

			}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			if(urldisplay.contains(".")){
				String extension = urldisplay.substring(urldisplay.lastIndexOf("."));
				
				FileOutputStream fos= new FileOutputStream(getExternalFilesDir("Memes")+"/"+Calendar.getInstance().getTime().toString()+extension);
				  URL u= new URL(urldisplay);
				   BufferedInputStream br= new BufferedInputStream(u.openStream());
				     byte [] b= new byte[1024];
					 int c;
					while((c=br.read(b,0,1024))!=-1){
						fos.write(b,0,c);
					}
					fos.flush();
					 fos.close();
					
			}
				} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
		//	Matrix matrix = new Matrix();
			// RESIZE THE BIT MAP
		//	matrix.postScale(120,120);
			bar.setVisibility(ProgressBar.GONE);
			
			bmImage.setImageBitmap(result);
		}
	}
}
