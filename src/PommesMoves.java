package com.clit_it.brendadillon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class PommesMoves {

	private Bitmap pommes_down, pommes_flies, pommes_up;		// the pommeses
	

	public int bitmapWidth;	// the width of the bitmap
	public int bitmapHeight;	// the height of the bitmap

	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)

	private int linePosition;
	
	public int mode;	//defines the state of the pommes: lying, flying, hanging
	
	private boolean pickedUp;
	private boolean pommesIsInitialized;
	private boolean hitDillon;
	
	private long gameTime;
	
	public static final int POMMES_LIES = 0;
	public static final int POMMES_CARRIED = 1;
	public static final int POMMES_FLIES = 2;
	public static final int POMMES_UP= 3;
	public static final int NO_POMMES = 4;
	public static final int POMMES_DIDNT_HIT = 5;
	public static final int POMMES_TO_EAT = 6;
	
	
	public PommesMoves(Bitmap pommes_down, Bitmap pommes_flies, Bitmap pommes_up, int x, int y) {
		this.pommes_down = pommes_down;
		this.pommes_flies = pommes_flies;
		this.pommes_up = pommes_up;
		this.x = x;		
		this.y = y;
		linePosition=0;
		bitmapWidth = pommes_down.getWidth();
		bitmapHeight = pommes_down.getHeight();
		mode=POMMES_LIES;
		pommesIsInitialized=false;
		gameTime=-1;
		hitDillon=false;
	}
	
	public void setBitmapSizes(int width, int height){
		{
		int x = (width*25)/540;
		
		this.pommes_down = Bitmap.createScaledBitmap(pommes_down, x, x, true);
		this.pommes_flies = Bitmap.createScaledBitmap(pommes_flies, x, x, true);
		this.pommes_up = Bitmap.createScaledBitmap(pommes_up, x, x, true);

		bitmapWidth = pommes_down.getWidth();
		bitmapHeight = pommes_down.getHeight();
		
		}
	}
	
	public void reset(){
		mode=POMMES_LIES;
		gameTime=-1;
		hitDillon=false;
		pickedUp=false;
		pommesIsInitialized=false;
	}
	
	public int getMode(){
		return mode;
	}
	public void setMode(int m){
		mode=m;
	}
	
	public void setPommesIsInitialized(boolean b) {
		pommesIsInitialized=b;
	}

	public boolean getPommesIsInitialized() {
		return pommesIsInitialized;
	}

	public void setPommesHitDillon(boolean b) {
		hitDillon=b;
	}

	public boolean getPommesHitDillon() {
		return hitDillon;
	}
	
	public void setXPosition(int x){
		this.x=x;
	}
	
	public void setYPosition(int y){
		this.y=y;
	}
	
	public int getXPosition(){
		return x;
	}

	public int getYPosition(){
		return y;
	}

	
	public void setLinePosition(int linePosition){
		this.linePosition=linePosition;
	}
	
	public void update(long gameTime) {
		if((mode==POMMES_FLIES) && (y<=linePosition)){
			mode=POMMES_UP;
		}
		
		if(mode==POMMES_DIDNT_HIT){
			if(this.gameTime==-1){
				this.gameTime=gameTime;
			} else if (this.gameTime+1800<=gameTime){
				this.mode=POMMES_TO_EAT;
			}
		}
	}
	
	
	public boolean isItPickedUp (){
		return pickedUp;
	}
	
	public boolean checkIfBrendaStandsOnPommes(int brendaPositionX) {
		if (this.x+bitmapWidth/2 >= brendaPositionX-80 && this.x+bitmapWidth/2<=brendaPositionX){
			pickedUp=true;
			return pickedUp;
		}
		else {
			pickedUp=false;
			return pickedUp;
		}
	}
	
	
	private float angle=0;

	
	public void draw(Canvas canvas) {
		
		switch(mode){
		case POMMES_LIES: canvas.drawBitmap(pommes_down, x, y, null);
			break;
		case POMMES_CARRIED:
			canvas.drawBitmap(pommes_flies,x, y-20, null);
			break;
		case POMMES_FLIES: 
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			y-=9;
			canvas.drawBitmap(Bitmap.createBitmap(pommes_flies, 0, 0, pommes_flies.getWidth(), pommes_flies.getHeight(), matrix, true),x,y,null);
			angle+=2;
			break;
		case POMMES_UP: 
			canvas.drawBitmap(pommes_up, x, linePosition-2, null);
			break;
		case NO_POMMES: 
			break;
		case POMMES_DIDNT_HIT: 
			canvas.drawBitmap(pommes_up, x, linePosition-2, null);
			break;
		case POMMES_TO_EAT: 
			canvas.drawBitmap(pommes_up, x, linePosition-2, null);
			break;
		}
	}



	
}
