package com.clit_it.brendadillon;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RestartTheGame extends Activity {

	Button button1;
	Typeface tf;
	TextView tv;
	ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		No Sleeping Screen
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_main);
		
		tf = Typeface.createFromAsset(getAssets(), "fonts/Antaviana Bold.ttf");
		
//		iv = (ImageView) findViewById(R.id.imageview1);
//		iv.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dillon_waits), 800, 200, true));
		
		tv = (TextView) findViewById(R.id.textview1);
		tv.setTypeface(tf);
		tv.setTextSize(40);
		tv.setTextColor(Color.BLACK);
		tv.setText("");
		
		
		button1 = (Button) findViewById(R.id.button1);
		button1.setTypeface(tf);
		button1.setTextSize(40);
		button1.setTextColor(Color.BLACK);
//		button1.setBackgroundColor(Color.TRANSPARENT);
		button1.setText("Play again!");
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				button1.setClickable(false);
				button1.setVisibility(View.INVISIBLE);
				tv.setText("LOADING ...");
				Thread thread = new Thread(){
					public void run(){
						try {
							startActivity(new Intent("com.clit_it.brendadillon.NEWGAME"));
							} 		
						finally {
							finish();
						}
					}
				};
				thread.start();
			}
		});	
		
	}

}