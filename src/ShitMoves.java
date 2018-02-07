package com.clit_it.brendadillon;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ShitMoves {
	
	private Bitmap shit_down, shit_falling;		// the shit-drawings
	private Bitmap[] shit_goodie;
	

	public int bitmapWidth;	// the width of the bitmap
	public int bitmapHeight;	// the height of the bitmap

	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)

	private int linePosition,bottomOfView;
	
	public int mode;	//defines the state of the shit: lying, flying, hanging
	
	public static final int NO_SHIT = 0;
	public static final int SHIT_FALLING = 1;
	public static final int SHIT_DOWN = 2;
	public static final int SHIT_HIT=3;
	
	private boolean shitHitBrenda;
	private boolean dillonWantsToShit;
	
	private boolean isInitialized;	
	private long gameTime;
	
	public boolean isGoodieThere;
	public int goodie;
	public static final int SCHIRM = 0;
	public static final int PANFLOETE = 1;
	
	private int shitOnGroundTime;
	
	
	public ShitMoves(Bitmap shit_falling, Bitmap shit_down, Bitmap schirm, Bitmap panfloete, int x, int y) {
		this.shit_down = shit_down;
		this.shit_falling = shit_falling;
		this.x = x;		
		this.y = y;
		bottomOfView=0;
		linePosition=0;
		bitmapWidth = shit_down.getWidth();
		bitmapHeight = shit_down.getHeight();

		mode=NO_SHIT;
		gameTime=-1;
		shitHitBrenda=false;
		dillonWantsToShit=false;
		isInitialized=false;
		
		shitOnGroundTime=5000;
		
		shit_goodie = new Bitmap[2];
		shit_goodie[SCHIRM] = schirm;
		shit_goodie[PANFLOETE] = panfloete;
		
	}
	
	public void setBitmapSizes(int width, int height){
		{
		int x = (width*25)/540;
		
		this.shit_down = Bitmap.createScaledBitmap(shit_down, x, x, true);
		this.shit_falling = Bitmap.createScaledBitmap(shit_falling, x, x, true);

		bitmapWidth = shit_down.getWidth();
		bitmapHeight = shit_down.getHeight();
		
		shit_goodie[SCHIRM] = Bitmap.createScaledBitmap(shit_goodie[SCHIRM], x, x, true);
		shit_goodie[PANFLOETE] = Bitmap.createScaledBitmap(shit_goodie[PANFLOETE], x, x, true);
		
		}
	}
	

	public void setIsInitialized(boolean b){
		isInitialized=b;
	}
	
	public boolean getIsInitialized(){
		return isInitialized;
	}

	public int getMode(){
		return mode;
	}

	public void setMode(int m){
		mode=m;
	}
	
	public void setDillonWantsToShit(boolean b) {
		dillonWantsToShit=b;
	}
	public boolean getDillonWantsToShit() {
		return dillonWantsToShit;
	}
	
	public void setLinePosition(int linePosition){
		this.linePosition=linePosition;
	}
	
	public boolean checkIfBrendaStandsOnGoodie(int brendaPositionX) {
		if(this.mode==SHIT_DOWN){
			if (this.x+bitmapWidth/2 >= brendaPositionX-80 && this.x+bitmapWidth/2<=brendaPositionX){
				setGoodieThere(false);
					return true;
			} else 
				return false;
		}
		else {
			return false;
		}
	}
	
	public void checkIfShitHitBrenda(int brendaPositionX, int brendaPositionY) {
		if (this.x+bitmapWidth/2 >= brendaPositionX-80 && this.x+bitmapWidth/2<=brendaPositionX
				&& this.y >= brendaPositionY){
			setShitHitBrenda(true);
		}
	}
	
	
	public void setShitHitBrenda(boolean b) {
		shitHitBrenda=b;
	}
	public boolean getShitHitBrenda() {
		return shitHitBrenda;
	}
	
	public void updateShitHitBrenda(int brendaPositionX) {
		if (this.x+bitmapWidth/2 >= brendaPositionX-80 && this.x+bitmapWidth/2<=brendaPositionX
				&& this.y <= bottomOfView-70){
			setShitHitBrenda(true);
			} else 
				setShitHitBrenda(false);
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
	
	public void setBottomOfGameView(int x){
		this.bottomOfView=x;
	}
	
		
	public void update(long gameTime) {
		
		if(mode==SHIT_FALLING){
			if(y>=bottomOfView){
				setMode(SHIT_DOWN);
			}
			changePosition();
		}
		
		if(getShitHitBrenda()==true){
			setMode(SHIT_HIT);
		}
		
// WENN DER KACK VON SELBST VERSCHWINDEN SOLL
		if(mode==SHIT_DOWN || mode==SHIT_HIT){
			if(this.gameTime==-1){
				this.gameTime=gameTime;
			} 
			if (this.gameTime+getShitOnGroundTime()<=gameTime){
				setMode(NO_SHIT);
				isGoodieThere=false;
				this.gameTime=-1;
				setYPosition(linePosition);
			}
		}
	}	
	
	public void changePosition(){
		this.x=x+1;
		this.y=y+9;
	}
	
	
	public void draw(Canvas canvas) {
		
		switch(mode){
		case NO_SHIT: 
			break;
		case SHIT_FALLING:
			canvas.drawBitmap(shit_falling,x,y, null);
			break;
		case SHIT_DOWN:
			canvas.drawBitmap(shit_down, x, bottomOfView, null);
			if(isGoodieThere()) canvas.drawBitmap(shit_goodie[goodie],x,bottomOfView,null);
			break;
		case SHIT_HIT: 
			break;
		}
	}

	public int getShitOnGroundTime() {
		return shitOnGroundTime;
	}

	public void setShitOnGroundTime(int level) {
		switch(level){
		case DillonMoves.EASY: 		
			this.shitOnGroundTime = 5000;
		break;
		case DillonMoves.DEFAULT: 		
			this.shitOnGroundTime = 4000;
		break;
		case DillonMoves.HARD: 		
			this.shitOnGroundTime = 2000;
		break;
		case DillonMoves.UNPLAYABLE: 		
			this.shitOnGroundTime = 800;
		break;
		
		}

	}

	public boolean isGoodieThere() {
		return isGoodieThere;
	}

	public void setGoodieThere(boolean newbool) {
		if(newbool==true && mode==SHIT_FALLING)
			this.isGoodieThere = newbool;

		if(newbool==false)
			this.isGoodieThere = newbool;
	}	
}