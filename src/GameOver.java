package com.clit_it.brendadillon;

import java.util.Random;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;


public class GameOver {
	
	private Context	mContext;
	
	public static final int NOT_OVER_YET = 0;
	public static final int WON = 1;
	public static final int TOO_MANY_PIGEONS = 2;
	public static final int BRENDA_IS_FROZEN = 3;
	
	private int mode;
	private Paint paint;
	private Paint paintbuttons;
	private Typeface tf;
	private Resources res;	
	
	private Bitmap brenda_dance, brenda_frozen, venice_ticket; //the dancing winner
	private Bitmap pommes; //the rotating weapon
	private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
	private int frameNr;		// number of frames in animation
	private int currentFrame;	// the current frame
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)
	
	public int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	public int spriteHeight;	// the height of the sprite

	private int y;
	
	private long last_time=0;
	private boolean showText=false;
	public boolean clickable=false;
	
	public GameOver(Context context){
		
		mContext=context;
		
		mode=NOT_OVER_YET;
		
		res = context.getResources();
		
		
		tf = Typeface.createFromAsset(res.getAssets(), "fonts/Antaviana Bold.ttf");
		paint = new Paint();
		paint.setTypeface(tf);
		paint.setTextSize(60);
		paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		paintbuttons = new Paint();
		paintbuttons.setTypeface(tf);
		paintbuttons.setTextSize(45);
		paintbuttons.setFlags(Paint.UNDERLINE_TEXT_FLAG);
		paintbuttons.setAntiAlias(true);
		paintbuttons.setColor(Color.WHITE);
		
		initBrenda();

	}
	
	public void setMode(int m) {
		this.mode=m;
	}

	public int getMode() {
		return this.mode;
	}
	
	public void update(long gameTime) {
		if(mode!=NOT_OVER_YET){
			if (gameTime > frameTicker + framePeriod) {
				frameTicker = gameTime;
				// increment the frame
				currentFrame++;
				if (currentFrame >= frameNr) {
					currentFrame = 0;
				}
			}
			// define the rectangle to cut out sprite
			this.sourceRect.left = currentFrame * spriteWidth;
			this.sourceRect.right = this.sourceRect.left + spriteWidth;
		
			if(last_time==0){
				last_time=gameTime;
			}
			if(last_time+2000<=gameTime){
				setShowText(true);
			}
		}
//		
//		if(mode==BRENDA_IS_FROZEN){
//			if (gameTime > frameTicker + framePeriod) {
//				frameTicker = gameTime;
//				// increment the frame
//				currentFrame++;
//				if (currentFrame >= frameNr) {
//					currentFrame = 0;
//				}
//			}
//			// define the rectangle to cut out sprite
//			this.sourceRect.left = currentFrame * spriteWidth;
//			this.sourceRect.right = this.sourceRect.left + spriteWidth;
//		
//			if(last_time==0){
//				last_time=gameTime;
//			}
//			if(last_time+2000<=gameTime){
//				setShowText(true);
//			}
//		}
		
	}
	
	
	private int getRandomColor(){
		Random random = new Random();
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		return (0xff000000 + (r << 16) + (g << 8) + b);
	}
	
	public void initBrenda(){
		
		brenda_dance = BitmapFactory.decodeResource(res, R.drawable.brenda_sprite03);
		brenda_frozen = BitmapFactory.decodeResource(res, R.drawable.brenda_frozen);
		venice_ticket = BitmapFactory.decodeResource(res, R.drawable.venice_ticket);
		pommes = BitmapFactory.decodeResource(res, R.drawable.pommes);
		currentFrame = 0;
		frameNr = 6;
		spriteWidth = brenda_dance.getWidth() / 6;
		spriteHeight = brenda_dance.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / 7;
		frameTicker = 0l;
		
		y=(int)(6*paint.getTextSize());
	}
	
	private int angle=0;
	private void doDrawDancingPommes(Canvas c) {
		c.drawBitmap(RotateBitmap(pommes,-angle), c.getWidth()-4*pommes.getWidth(), c.getHeight()-4*pommes.getHeight(), null);
		c.drawBitmap(RotateBitmap(pommes,angle), 4*pommes.getWidth(), c.getHeight()-4*pommes.getHeight(), null);
		c.drawBitmap(RotateBitmap(pommes,-angle), (c.getWidth()/2-pommes.getWidth()/2), c.getHeight()-4*pommes.getHeight(), null);
		if(angle<360)
			angle+=5;
		else angle=0;
	}
	 
	 public Bitmap RotateBitmap(Bitmap source, float angle)	 {
		 Matrix matrix = new Matrix();
		 matrix.postRotate(angle);
		 return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	 }	
	
	
	public void draw(Canvas c){
		
		mContext.getResources().getConfiguration();
		if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			// LANDSCAPE
			switch(mode){
			case NOT_OVER_YET:
				break;
			case WON: c.drawColor(getRandomColor());
					paint.setColor(getRandomColor());
//					doDrawDancingPommes(c);
					c.drawText("Y O U", 40, y/3, paint);
					c.drawText("W I N !", c.getWidth()-(paint.measureText("W I N !"))-40, y/3, paint);
					Rect destRect = new Rect(c.getWidth()/2-spriteWidth/2,(c.getHeight()/2-spriteHeight+50), c.getWidth()/2-spriteWidth/2 + spriteWidth, (c.getHeight()/2-spriteHeight+50) + spriteHeight);
					c.drawBitmap(brenda_dance, sourceRect, destRect, null);
				break;
			case TOO_MANY_PIGEONS: c.drawColor(Color.BLACK);
				paint.setColor(getRandomColor());
				c.drawText("O M G !", 40, y/3, paint);
				Rect destRect2 = new Rect(c.getWidth()/2-spriteWidth/2,(c.getHeight()/2-spriteHeight), c.getWidth()/2-spriteWidth/2 + spriteWidth, (c.getHeight()/2-spriteHeight) + spriteHeight);
				c.drawBitmap(venice_ticket, sourceRect, destRect2, null);
				break;
			case BRENDA_IS_FROZEN: c.drawColor(Color.BLACK);
				paint.setColor(getRandomColor());
				c.drawText("Y O U", 40, y/3, paint);
				c.drawText("L O S E !", (c.getWidth())-(paint.measureText("L O S E !"))-40, y/3, paint);
				Rect destRect1 = new Rect(c.getWidth()/2-spriteWidth/2,(c.getHeight()/2-spriteHeight+50), c.getWidth()/2-spriteWidth/2 + spriteWidth, (c.getHeight()/2-spriteHeight+50) + spriteHeight);
				c.drawBitmap(brenda_frozen, sourceRect, destRect1, null);
				break;
			}
			
			if(mode!=NOT_OVER_YET && clickable ){
				paintbuttons.setColor(getRandomColor());
				c.drawText("AGAIN", c.getWidth()-paintbuttons.measureText("AGAIN")-40, c.getHeight()-paintbuttons.getTextSize()-10, paintbuttons);
				c.drawText("QUIT", 40, c.getHeight()-paintbuttons.getTextSize()-10, paintbuttons);
			}
		}
		else { //PORTRAIT
			switch(mode){
			case NOT_OVER_YET:
				break;
			case WON: c.drawColor(getRandomColor());
					paint.setColor(getRandomColor());
					doDrawDancingPommes(c);
					c.drawText("Y O U   W I N !", c.getWidth()/2-paint.measureText("Y O U   W I N !")/2, y/3, paint);
					Rect destRect = new Rect(c.getWidth()/2-spriteWidth/2,(c.getHeight()/2-spriteHeight+50), c.getWidth()/2-spriteWidth/2 + spriteWidth, (c.getHeight()/2-spriteHeight+50) + spriteHeight);
					c.drawBitmap(brenda_dance, sourceRect, destRect, null);
				break;
			case TOO_MANY_PIGEONS: c.drawColor(Color.BLACK);
				paint.setColor(getRandomColor());
				c.drawText("O M G !", c.getWidth()/2-paint.measureText("O M G !")/2, y/3, paint);
				Rect destRect2 = new Rect(c.getWidth()/2-spriteWidth/2,(c.getHeight()/2-spriteHeight), c.getWidth()/2-spriteWidth/2 + spriteWidth, (c.getHeight()/2-spriteHeight) + spriteHeight);
				c.drawBitmap(venice_ticket, sourceRect, destRect2, null);
				break;
			case BRENDA_IS_FROZEN: c.drawColor(Color.BLACK);
				paint.setColor(getRandomColor());
				c.drawText("Y O U   L O S E !", c.getWidth()/2-paint.measureText("Y O U   L O S E !")/2, y/3, paint);
				Rect destRect1 = new Rect(c.getWidth()/2-spriteWidth/2,(c.getHeight()/2-spriteHeight+50), c.getWidth()/2-spriteWidth/2 + spriteWidth, (c.getHeight()/2-spriteHeight+50) + spriteHeight);
				c.drawBitmap(brenda_frozen, sourceRect, destRect1, null);
				break;
			}
			
			if(mode!=NOT_OVER_YET && clickable ){
				paintbuttons.setColor(getRandomColor());
				c.drawText("AGAIN", c.getWidth()-paintbuttons.measureText("AGAIN")-40, c.getHeight()/2+3*paintbuttons.getTextSize()-40, paintbuttons);
				c.drawText("QUIT", 40, c.getHeight()/2+3*paintbuttons.getTextSize()-40, paintbuttons);
			}
		}
	}

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

}
