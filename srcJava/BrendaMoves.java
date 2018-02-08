package com.clit_it.brendadillon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

public class BrendaMoves {

	private Paint paint;
	private Typeface tf;
	
	private Bitmap bitmap_waits;		// the animation sequence
	private Bitmap bitmap_runs_left;		// the animation sequence
	private Bitmap bitmap_runs_right;		// the animation sequence
	private Bitmap schirm, panfloete, notes, rain;
	
	private Bitmap[] brenda_waits_mask, brenda_r_mask, brenda_l_mask;
		
	private Rect sourceRect,sourceRectRain;	// the rectangle to be drawn from the animation bitmap
	private int frameNr;		// number of frames in animation
	private int frameCount;
	private int currentFrame;	// the current frame
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)
	
	public int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	public int spriteHeight;	// the height of the sprite
	
	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)

	private double speed, speedmax, current_speed;
	
	private int mode;	//defines whether brenda is waiting, running to the left or running to the right
	
	public static final int BRENDA_WAITS = 0;
	public static final int BRENDA_RUNS_RIGHT = 1;
	public static final int BRENDA_RUNS_LEFT = 2;
	public static final int BRENDA_WAS_HIT = 3;
	
	
	private boolean brendaWantsToThrowThePommes;
	private boolean brendaCarriesAPommes;
	private boolean hasCarriedAPommes;
	private boolean brendaIsInitialized;
	private boolean brendaWasHit;
	private boolean brendaIsFrozen;
	public boolean brendaCarriesGoodie;
	public boolean it_is_raining;
	private boolean omgPigeon;
	
	private boolean bored;
	private int bored_mode;
	
	private long decreaseSpeedTime, goodieTime, omgTime;
	
	private int hit_counter;
	private int shitMaskState;	
	
	private int goodie;		
	private int angle,notes_x,notes_y,rain_x;

	
	
	public BrendaMoves(Context mContext, Bitmap bitmap_waits, Bitmap bitmap_runs_left, Bitmap bitmap_runs_right, int x, int y, int fps, int frameCount) {
		this.bitmap_waits = bitmap_waits;
		this.bitmap_runs_left = bitmap_runs_left;
		this.bitmap_runs_right = bitmap_runs_right;
		this.x = x;
		this.y = y;
		currentFrame = 0;
		frameNr = frameCount;
		this.frameCount = frameCount;
		spriteWidth = bitmap_waits.getWidth() / frameCount;
		spriteHeight = bitmap_waits.getHeight();
		framePeriod = 1000 / fps;
		frameTicker = 0l;
		
		decreaseSpeedTime = -1;
		setGoodieTime(-1);
		omgTime = -1;
		
		speedmax=5;
		speed=speedmax;
		current_speed=0;
		setHitCounter(0);
		
		brendaWantsToThrowThePommes=false;
		brendaCarriesAPommes=false;
		hasCarriedAPommes=false;
		brendaIsInitialized=false;
		brendaWasHit=false;
		setBrendaIsFrozen(false);
		brendaCarriesGoodie=false;
		it_is_raining=false;
		omgPigeon=false;
		
		bored=false;
		bored_mode=0;

		tf = Typeface.createFromAsset(mContext.getResources().getAssets(), "fonts/Antaviana Bold.ttf");
		paint = new Paint();
		paint.setTypeface(tf);
		paint.setTextSize(20);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		
		setShitMaskState(0);

		brenda_waits_mask = new Bitmap[5];
		for(int i=1;i<5;i++){
			brenda_waits_mask[i]=BitmapFactory.decodeResource(mContext.getResources(), mContext.getResources().getIdentifier("drawable/brenda_s"+i, null, mContext.getPackageName()));
		}
		
		brenda_r_mask = new Bitmap[5];
		for(int i=1;i<5;i++){
			brenda_r_mask[i]=BitmapFactory.decodeResource(mContext.getResources(), mContext.getResources().getIdentifier("drawable/brenda_r"+i, null, mContext.getPackageName()));
		}
		
		brenda_l_mask = new Bitmap[5];
		for(int i=1;i<5;i++){
			brenda_l_mask[i]=BitmapFactory.decodeResource(mContext.getResources(), mContext.getResources().getIdentifier("drawable/brenda_l"+i, null, mContext.getPackageName()));		
		}

		brenda_waits_mask[0]=BitmapFactory.decodeResource(mContext.getResources(), mContext.getResources().getIdentifier("drawable/brenda_s"+0, null, mContext.getPackageName()));
		brenda_r_mask[0]=brenda_waits_mask[0];
		brenda_l_mask[0]=brenda_waits_mask[0];
		
		schirm=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.schirm_up);
		panfloete=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.panfloete);
		notes=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.notes);
		
		rain=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rain);
		sourceRectRain = new Rect(0, 0, spriteWidth, rain.getHeight());
		
		angle=0;
		notes_x=0; notes_y=0;
		rain_x=0;
	}
	
	public void setBitmapSizes(int width, int height){
		
		{
			int x = (width*800)/540;
			int y = (width*85)/540;
			
			this.bitmap_waits = Bitmap.createScaledBitmap(bitmap_waits, x, y, true);
			this.bitmap_runs_left = Bitmap.createScaledBitmap(bitmap_runs_left, x, y, true);
			this.bitmap_runs_right = Bitmap.createScaledBitmap(bitmap_runs_right, x, y, true);
			
			float t = (float)bitmap_waits.getWidth() / (float)frameCount;
			int s =  bitmap_waits.getWidth() / frameCount;
			int rest = 0;
			if(t-(float)s > 0) rest=1;
			if(t-(float)s < 0) rest=-1;
			
			spriteWidth = (bitmap_waits.getWidth() / frameCount)+rest;
			spriteHeight = bitmap_waits.getHeight();
			sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
			
			for(int i=1;i<5;i++){
				brenda_waits_mask[i]=Bitmap.createScaledBitmap(brenda_waits_mask[i], x, y, true);
			}
		
			for(int i=1;i<5;i++){
				brenda_r_mask[i]=Bitmap.createScaledBitmap(brenda_r_mask[i], x, y, true);
			}
		
			for(int i=1;i<5;i++){
				brenda_l_mask[i]=Bitmap.createScaledBitmap(brenda_l_mask[i], x, y, true);		
			}
		}
		
		{
			int x = (100*width)/540;
			int y = (110*width)/540;
			schirm=Bitmap.createScaledBitmap(schirm, x, y, true);
		}
		
		{
			int x = (width*25)/540;
			panfloete=Bitmap.createScaledBitmap(panfloete, x, x, true);
			notes=Bitmap.createScaledBitmap(notes, x, x, true);
		}
		
		{
			int x = (800*width)/540;
			int y = (133*width)/540;
			rain=Bitmap.createScaledBitmap(rain, x, y, true);
			sourceRectRain = new Rect(0, 0, spriteWidth, rain.getHeight());			
		}
		
	}
	
	
	public void setBrendaIsInitialized(boolean b){
		brendaIsInitialized=b;
	}
	
	public boolean getBrendaIsInitialized(){
		return brendaIsInitialized;
	}
	
	public void setXPosition(int x){
		this.x=x;	
	}
	
	public void setYPosition(int y){
		this.y=y;	
	}
	
	public int getXPosition(){
		return (this.x+spriteWidth);
	}
		
	public int getYPosition(){
		return this.y;
	}
	

	
	public void update(long gameTime) {
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
		
		if(brendaCarriesGoodie){
			
			if(goodie==ShitMoves.SCHIRM){
				if(getGoodieTime()==-1)
					setGoodieTime(gameTime);

				if(getGoodieTime()+8000<=gameTime){
					brendaCarriesGoodie=false;
					setGoodieTime(-1);
				}
			}
			
			if(goodie==ShitMoves.PANFLOETE){
				if(getGoodieTime()==-1){
					setGoodieTime(gameTime);
					rain_x=0;
				}
				
				if(getGoodieTime()+3500<gameTime){
					it_is_raining=true;
				}
				
				if(getGoodieTime()+7000<gameTime){
					brendaCarriesGoodie=false;
					it_is_raining=false;
					if(getShitMaskState()>0){
						setShitMaskState(getShitMaskState() - 1);
					}
					increaseSpeed(1);
					setGoodieTime(-1);
				}
				
				if(it_is_raining){
					this.sourceRectRain.left = this.sourceRect.left;
					this.sourceRectRain.right = this.sourceRect.right;
				}
				
				//angle für wackelnde noten
				if(angle<5){
					angle++; 
				}
				else {
					angle=-5;
				}
				
				if(notes_x%2!=0){
					notes_x++; 
					notes_y++;
				}
				else {
					notes_x--; 
					notes_y--;
				}

			}	
		} 
		
		if(getHitByShit()==true){
			
			setMode(BRENDA_WAS_HIT);
				
			if(decreaseSpeedTime==-1){
				decreaseSpeedTime=gameTime;
				decreaseSpeed(1);
				if(getShitMaskState() < 4){
					setShitMaskState(getShitMaskState() + 1);
				}
				setHitCounter(getHitCounter() + 1);
				Log.i("BrendaMoves","Brenda HitCounter="+getHitCounter());
			}
		
			if(decreaseSpeedTime+200<gameTime){
				setMode(BRENDA_WAITS);
				decreaseSpeedTime=-1;
				setHitByShit(false);
			}
		}
		
		if(getOMGPigeon()){
			if(omgTime==-1){
				omgTime=gameTime;
			}
			if(omgTime+1200<gameTime){
				setOMGPigeon(false);
			}
		}
		
	}
	

	public void setMode(int m){
		mode=m;
	}
	
	public int getMode(){
		return mode;
	}
	
	public void setSpeed(double d){
		this.speed=d;
	}
	
	public void setCurrentSpeed(double d){
		this.current_speed=d;
	}

	public void increaseSpeed(double d){
		if(this.speed+d <= speedmax ){
			this.speed=this.speed+d;
		}
		if(this.speed+d>speedmax){
			this.speed=speedmax;
		}
	}
	
	public void decreaseSpeed(double d){
		if(this.speed-d > 0){
			this.speed=this.speed-d;
		} 
		if(this.speed-d<=0){
			this.speed=0;
			setBrendaIsFrozen(true);
		}
	}
	
	public double getSpeed(){
		return this.speed;
	}
	
	public void changePosition(float displaywidth){
		if((x+current_speed)>=-spriteWidth/3 && (x+current_speed)<=displaywidth-(spriteWidth-spriteWidth/3))
			x=x+(int)current_speed;
	}
	
	public void draw(Canvas canvas) {
		
		switch(mode){
		case BRENDA_WAITS: Rect destRect = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(bitmap_waits, sourceRect, destRect, null);
				canvas.drawBitmap(addShitMask(BRENDA_WAITS), sourceRect, destRect, null);
			break;
		case BRENDA_RUNS_RIGHT: Rect destRect0 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(bitmap_runs_left, sourceRect, destRect0, null);
				canvas.drawBitmap(addShitMask(BRENDA_RUNS_RIGHT), sourceRect, destRect0, null);
			break;
		case BRENDA_RUNS_LEFT: Rect destRect1 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(bitmap_runs_right, sourceRect, destRect1, null);
				canvas.drawBitmap(addShitMask(BRENDA_RUNS_LEFT), sourceRect, destRect1, null);
			break;
		case BRENDA_WAS_HIT: 
			break; 
		}

		if(brendaCarriesGoodie==true){
			switch(goodie){
			case ShitMoves.SCHIRM:
				switch(mode){
				case BRENDA_WAITS: canvas.drawBitmap(schirm, x, y-25, null);
				break;
				case BRENDA_RUNS_RIGHT: canvas.drawBitmap(schirm, x-38, y-25, null);
				break;
				case BRENDA_RUNS_LEFT: canvas.drawBitmap(schirm, x+28, y-25, null);
				break;
				}
			break;
			
			case ShitMoves.PANFLOETE:
				switch(mode){
				case BRENDA_WAITS: 
					canvas.drawBitmap(panfloete, x+45, y+45, null);
					if(it_is_raining){
						if(rain_x>=x){
							Rect destRect1 = new Rect(x, y-55, x + spriteWidth, y + rain.getHeight()-55);
							canvas.drawBitmap(rain, sourceRectRain, destRect1, null);
						} else {
							rain_x+=5;
							Rect destRect1 = new Rect(rain_x, y-55, rain_x + spriteWidth, y + rain.getHeight()-55);
							canvas.drawBitmap(rain, sourceRectRain, destRect1, null);
						}
					} else
						canvas.drawBitmap(rotateBitmap(notes,angle), x+60+notes_x, y-45+notes_y, null);
				break;
				case BRENDA_RUNS_RIGHT: 
					canvas.drawBitmap(panfloete, x+5, y+45, null);
					if(it_is_raining){
						if(rain_x>=x){
							Rect destRect1 = new Rect(x, y-55, x + spriteWidth, y + rain.getHeight()-55);
							canvas.drawBitmap(rain, sourceRectRain, destRect1, null);
						} else {
							rain_x+=5;
							Rect destRect1 = new Rect(rain_x, y-55, rain_x + spriteWidth, y + rain.getHeight()-55);
							canvas.drawBitmap(rain, sourceRectRain, destRect1, null);
						}
					} else
						canvas.drawBitmap(rotateBitmap(notes,angle), x+22+notes_x, y-45+notes_y, null);
				break;
				case BRENDA_RUNS_LEFT: 
					canvas.drawBitmap(panfloete, x+73, y+45, null);
					if(it_is_raining){
						if(rain_x>=x){
							Rect destRect1 = new Rect(x, y-55, x + spriteWidth, y + rain.getHeight()-55);
							canvas.drawBitmap(rain, sourceRectRain, destRect1, null);
						} else {
							rain_x+=5;
							Rect destRect1 = new Rect(rain_x, y-55, rain_x + spriteWidth, y + rain.getHeight()-55);
							canvas.drawBitmap(rain, sourceRectRain, destRect1, null);
						}
					} else
						canvas.drawBitmap(rotateBitmap(notes,angle), x+88+notes_x, y-45+notes_y, null);
				break;
				}
					
			break;
			}
				
		}
		
		if(getBored()==true){
			switch (getStateOfBeingBored()){
				case 1:	canvas.drawText("I'm bored.", x+10, y-15, paint);
					break;
				case 2:	canvas.drawText("What could I possibly do!?", x+7, y-16, paint);
				break;
				case 3:	
					if(brendaCarriesAPommes || hasCarriedAPommes) {
						canvas.drawText("This frie is really groce!", x+8, y-17, paint);
						hasCarriedAPommes=true;
					}
					else canvas.drawText("That frie over there looks really groce!", x+8, y-17, paint);
				break;
				default:
					break;
			}
		}
		
		if(getOMGPigeon()==true){
			canvas.drawText("Oh no! I hate pigeons!!", x+4, y-15, paint);
		}
	}
	
	public void setOMGPigeon(boolean b){
		omgPigeon = b;
	}
	
	public boolean getOMGPigeon(){
		return omgPigeon;
	}
	
	private Bitmap rotateBitmap(Bitmap source, float angle)	 {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}
	
	
	private Bitmap addShitMask(int m){
		
		switch(m){
			case BRENDA_WAITS:
				return brenda_waits_mask[getShitMaskState()];		
			case BRENDA_RUNS_RIGHT:
				return brenda_l_mask[getShitMaskState()];		
			case BRENDA_RUNS_LEFT:
				return brenda_r_mask[getShitMaskState()];		
		}
		
		return brenda_waits_mask[0]; //leere ShitMask
	}
	
	public boolean hasThrownThePommes() {
		if(brendaIsFrozen==false && brendaCarriesAPommes==true && brendaWantsToThrowThePommes==true)
			return true;
		else return false;
	}

	public void setWantsToThrowThePommes(boolean b) {
		brendaWantsToThrowThePommes=b;
	}

	public void setCarriesAPommes(boolean b) {
		brendaCarriesAPommes=b;
	}

	public boolean getCarriesAPommes() {
		return brendaCarriesAPommes;
	}

	public void setHitByShit(boolean b) {
		brendaWasHit=b;
	}
	
	public boolean getHitByShit() {
		return brendaWasHit;
	}

	public int getShitMaskState() {
		return shitMaskState;
	}

	public void setShitMaskState(int shitMaskState) {
		this.shitMaskState = shitMaskState;
	}

	public int getHitCounter() {
		return hit_counter;
	}

	public void setHitCounter(int hit_counter) {
		this.hit_counter = hit_counter;
	}

	public void setBored(boolean b) {
		bored=b;
	}
	public boolean getBored() {
		return bored;
	}
	
	public void increaseStateOfBeingBored() {
		if(this.bored_mode<3)
			this.bored_mode++;
		else this.bored_mode=1;
	}
	public int getStateOfBeingBored() {
		return this.bored_mode;
	}

	public boolean isBrendaIsFrozen() {
		return brendaIsFrozen;
	}

	public void setBrendaIsFrozen(boolean brendaIsFrozen) {
		this.brendaIsFrozen = brendaIsFrozen;
	}
	
	public void setGoodie(int goodie) {
		this.goodie=goodie;
	}

	public long getGoodieTime() {
		return goodieTime;
	}

	public void setGoodieTime(long goodieTime) {
		this.goodieTime = goodieTime;
	}	
}
