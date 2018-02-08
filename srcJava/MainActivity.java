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

public class MainActivity extends Activity {

	Button button1;
	Typeface tf;
	TextView tv1,tv2;
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
		
		iv = (ImageView) findViewById(R.id.imageView1);
		
		tv1 = (TextView) findViewById(R.id.textview1);
		tv1.setTypeface(tf);
		tv1.setTextSize(40);
		tv1.setTextColor(Color.BLACK);
		tv1.setText("");

		tv2 = (TextView) findViewById(R.id.textview2);
		tv2.setTypeface(tf);
		tv2.setTextSize(25);
		tv2.setTextColor(Color.BLACK);
		tv2.setText("\"Pigeonwings and fries, please!\"");

		
		button1 = (Button) findViewById(R.id.button1);
		button1.setTypeface(tf);
		button1.setTextSize(40);
		button1.setTextColor(Color.BLACK);
//		button1.setBackgroundColor(Color.TRANSPARENT);
		button1.setText("Start");
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				button1.setClickable(false);
				button1.setVisibility(View.INVISIBLE);
				iv.setVisibility(View.INVISIBLE);
				tv2.setVisibility(View.INVISIBLE);
				
				tv1.setText("LOADING ...");
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