package com.clit_it.brendadillon;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class DillonMoves {

	private Bitmap dillon_waits;		// the animation sequence
	private Bitmap dillon_shits;		// the animation sequence
	private Bitmap dillon_vanishes;		// the animation sequence	
	private Bitmap dillon_eats;		// the animation sequence	
	private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
	private int frameNr;		// number of frames in animation
	private int currentFrame;	// the current frame
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)
	
	public int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	public int spriteHeight;	// the height of the sprite
	
	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)

	public int mode, level;
	
	private boolean dillonIsInitialized;
	private boolean dillonHasMoved;
	private boolean dillonWantsToShit;
	private boolean dillonShits;
	
	public static final int NO_DILLON = 0;
	public static final int DILLON_APPEARS = 1;
	public static final int DILLON_WAITS = 2;
	public static final int DILLON_SHITS = 3;
	public static final int DILLON_VANISHES = 4;
	public static final int DILLON_EATS = 5;
	public static final int DILLON_ON_ITS_WAY = 6;
	
	public static final int EASY = 0;
	public static final int DEFAULT = 1;
	public static final int HARD = 2;
	public static final int UNPLAYABLE = 3;
	
	private Random random;
	private int speed;	
	private int speed_min,speed_max;	
	private int noShitTime_max, noShitTime_min;
	private int sitStillSeconds, waitTillMoveChange, sitStillRandom, waitTillMoveRandom;

	private int varianzTreffflaecheRechts, varianzTreffflaecheLinks;
	
	public int goal_x;
	
	private long gameTime,shitTime,noShitTime,random_noShitTime, vanishTime, appearingTime, eatingTime;
	
	public DillonMoves(Bitmap dillon_waits, Bitmap dillon_shits, Bitmap dillon_vanishes, Bitmap dillon_eats, int x, int y, int width, int height, int fps, int frameCount) {
		this.dillon_waits = dillon_waits;
		this.dillon_shits = dillon_shits;
		this.dillon_vanishes = dillon_vanishes;
		this.dillon_eats = dillon_eats;
		this.x = x;
		this.y = y;
		currentFrame = 0;
		frameNr = frameCount;
		spriteWidth = dillon_waits.getWidth() / frameCount;
		spriteHeight = dillon_waits.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
		
		mode=DILLON_APPEARS;
		level=DEFAULT;
		
		dillonIsInitialized=false;
		dillonHasMoved=false;
		dillonWantsToShit=false;
		dillonShits=false;
				
		gameTime=-1;
		shitTime=-1;
		vanishTime=-1;
		appearingTime=-1;
		eatingTime=-1;
		noShitTime=-1;
		
		random = new Random();
		
		goal_x=0;

	}
	
	public void setBitmapSizes(int width, int height){
		{
			
			int x = (width*320)/540;
			int y = (width*80)/540;
			
			this.dillon_waits = Bitmap.createScaledBitmap(dillon_waits, x, y, true);
			this.dillon_shits = Bitmap.createScaledBitmap(dillon_shits, x, y, true);
			this.dillon_vanishes = Bitmap.createScaledBitmap(dillon_vanishes, x, y, true);
			this.dillon_eats = Bitmap.createScaledBitmap(dillon_eats, x, y, true);

			spriteWidth = dillon_waits.getWidth() / frameNr;
			spriteHeight = dillon_waits.getHeight();
			sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

		}		
	}
	
	
	public void setDillonIsInitialized(boolean b) {
		dillonIsInitialized=b;
	}
	
	public boolean getDillonIsInitialized() {
		return dillonIsInitialized;
	}
	
	public void setLevel(int l){
		level = l;
		
		switch(level){
		case EASY:
			speed_min=1;
			speed_max=4; //definiere Intervall aus dem zufällig speed gewählt wird
			noShitTime_min=1000;
			noShitTime_max=7000; //definiere Zufallsintervall, wie lange Dillon nicht gackt
			sitStillSeconds=100; //Dillon bewegt sich sitStillSeconds + random(sitStillRandom) nicht
			sitStillRandom=800;
			waitTillMoveChange=100; //Wie lange soll Dillon in eine Richtung läuft= waitTillMoveChange+random(waitTillMoveRandom)
			waitTillMoveRandom=500;
			varianzTreffflaecheLinks=10; //Trefffläche des Bildes wird von links um x Pixel beschnitten
			varianzTreffflaecheRechts=5; //Trefffläche wird von rechts um x Pixel beschnitten
			break;
		case DEFAULT:
			speed_min=2;
			speed_max=4; //definiere Intervall aus dem zufällig speed gewählt wird
			noShitTime_min=1000;
			noShitTime_max=5000; //definiere Zufallsintervall, wie lange Dillon nicht gackt
			sitStillSeconds=100; //Dillon bewegt sich sitStillSeconds + random(sitStillRandom) nicht
			sitStillRandom=400;
			waitTillMoveChange=100; //Wie lange soll Dillon in eine Richtung läuft= waitTillMoveChange+random(waitTillMoveRandom)
			waitTillMoveRandom=400;
			varianzTreffflaecheLinks=20; //Trefffläche des Bildes wird von links um x Pixel beschnitten
			varianzTreffflaecheRechts=5; //Trefffläche wird von rechts um x Pixel beschnitten
			break;
		case HARD:
			//HARDNESS-SETTINGS
			speed_min=2;
			speed_max=4; //definiere Intervall aus dem zufällig speed gewählt wird
			noShitTime_min=1000;
			noShitTime_max=4000; //definiere Zufallsintervall, wie lange Dillon nicht gackt
			sitStillSeconds=100; //Dillon bewegt sich sitStillSeconds + random(sitStillRandom) nicht
			sitStillRandom=300;
			waitTillMoveChange=100; //Wie lange soll Dillon in eine Richtung läuft= waitTillMoveChange+random(waitTillMoveRandom)
			waitTillMoveRandom=300;
			varianzTreffflaecheLinks=25; //Trefffläche des Bildes wird von links um x Pixel beschnitten
			varianzTreffflaecheRechts=7; //Trefffläche wird von rechts um x Pixel beschnitten
			break;
		case UNPLAYABLE:
			speed_min=3;
			speed_max=5; //definiere Intervall aus dem zufällig speed gewählt wird
			noShitTime_min=1000;
			noShitTime_max=100; //definiere Zufallsintervall, wie lange Dillon nicht gackt
			sitStillSeconds=100; //Dillon bewegt sich sitStillSeconds + random(sitStillRandom) nicht
			sitStillRandom=200;
			waitTillMoveChange=80; //Wie lange soll Dillon in eine Richtung läuft= waitTillMoveChange+random(waitTillMoveRandom)
			waitTillMoveRandom=200;
			varianzTreffflaecheLinks=30; //Trefffläche des Bildes wird von links um x Pixel beschnitten
			varianzTreffflaecheRechts=7; //Trefffläche wird von rechts um x Pixel beschnitten
			break;
		}
	}
	
	public int getLevel(){
		return level;
	}
	
	
	
	public void setXPosition(int x) {
		this.x=x;
	}
	
	public int getXPosition() {
		return x;
	}
	
	public void setYPosition(int y){
		this.y=y;		
	}
	
	public int getYPosition() {
		return y;
	}
	
	public void resetMovement(){
		int direction=0;
		int speed=0;
		speed=random.nextInt(speed_max)+speed_min; 		//DILLON GESCHWINDIGKEIT
		switch(random.nextInt(3)%3){
		case 0: direction=0;
			break;
		case 1: direction=-1;
			break;
		case 2: direction=1;
			break;
		}
		this.speed= speed*direction;
		
		setDillonHasMoved(false);
		this.gameTime=-1;
	}
	
	public void changePosition(float displaywidth, long gameTime){
		if(this.gameTime==-1){
			this.gameTime=gameTime;
			sitStillSeconds = sitStillSeconds+random.nextInt(sitStillRandom);
			waitTillMoveChange=waitTillMoveChange+random.nextInt(waitTillMoveRandom);
		} 
		
		if(speed==0){
			if (this.gameTime+sitStillSeconds<=gameTime){
				this.gameTime=-1;
				setDillonHasMoved(true);
			}
		} else {
			if (this.gameTime+waitTillMoveChange<=gameTime){
				this.gameTime=-1;
				setDillonHasMoved(true);
			} 
			if (this.gameTime+waitTillMoveChange>gameTime){
				if((x+speed)>=-spriteWidth/2 && (x+speed)<=displaywidth-(spriteWidth-spriteWidth/3)){		
					setXPosition((int)(x+speed));
				}else{
					if((x+speed)<=-spriteWidth/2){
						speed=(random.nextInt(speed_max)+speed_min); 		//DILLON GESCHWINDIGKEIT
					} else 
						if((x+speed)>=displaywidth-(spriteWidth-spriteWidth/3)){
						speed=(random.nextInt(speed_max)+speed_min)*(-1); 		//DILLON GESCHWINDIGKEIT
					}
				}
			}
		}
	}
	
	public boolean getDillonHasMoved(){
		return dillonHasMoved;
	}
	
	public void setDillonHasMoved(boolean b){
		dillonHasMoved=b;
	}
	
	public void setDillonShits(boolean b) {
		dillonShits=b;	
	}
	public boolean getDillonShits() {
		return dillonShits;
	}
	
	public void setDillonWantsToShit(boolean b){
		dillonWantsToShit=b;
	}
	
	public boolean getDillonWantsToShit(){
		return dillonWantsToShit;
	}
	
	public int getMode(){
		return mode;
	}
	
	public void setMode(int m){
		mode=m;
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

		if(getMode()==DILLON_APPEARS){
			if(this.appearingTime==-1){
				this.appearingTime=gameTime;
			} 
			if(appearingTime+600<=gameTime){
				setMode(DILLON_EATS);
				appearingTime=-1;
			}
		} 
		
		if(getMode()==DILLON_EATS){
			if(this.eatingTime==-1){
				this.eatingTime=gameTime;
			} 
			if(eatingTime+600<=gameTime){
				setMode(DILLON_WAITS);
				eatingTime=-1;
			}
		} 
		
		if(getMode()==DILLON_VANISHES){
			setDillonShits(false);
			setDillonWantsToShit(false);
			if(this.vanishTime==-1){
				this.vanishTime=gameTime;
			} 
			if(vanishTime+600<=gameTime){
				setMode(NO_DILLON);
			}
		} 
		
		if(getDillonShits()==true){
			setMode(DILLON_SHITS);
			if(this.shitTime==-1){
				this.shitTime=gameTime;
			} 
			if(shitTime+200<=gameTime){
				setMode(DILLON_WAITS);
				setDillonShits(false);
				shitTime=-1;
			}
		}

		if(getDillonWantsToShit()==false){
			if(this.noShitTime==-1){
				this.noShitTime=gameTime;
				random_noShitTime=noShitTime_min+random.nextInt(noShitTime_max);
			} 
			if(noShitTime+random_noShitTime<=gameTime){  
				setDillonWantsToShit(true);
				noShitTime=-1;
			}
		}
		
		if(mode==DILLON_ON_ITS_WAY){
			sitStillSeconds = 0;
			waitTillMoveChange = 0;

			if(x<goal_x){
				x++;
			} else if(x>goal_x){
				x--;
			}
		}
	}
	
	public boolean hasReachedPommes(){
		if(x >= goal_x-15 && x <= goal_x+15)
			return true;
		else
			return false;
	}

	public void draw(Canvas canvas) {
		
		switch(mode){
		case DILLON_WAITS: Rect destRect = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(dillon_waits, sourceRect, destRect, null);
			break;
		case DILLON_SHITS: Rect destRect2 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(dillon_shits, sourceRect, destRect2, null);
			break;
		case DILLON_APPEARS: Rect destRect3 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(dillon_vanishes, sourceRect, destRect3, null);
			break;
		case DILLON_EATS: Rect destRect5 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(dillon_eats, sourceRect, destRect5, null);
			break;			
		case DILLON_VANISHES: Rect destRect4 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(dillon_vanishes, sourceRect, destRect4, null);
			break;
		case DILLON_ON_ITS_WAY: Rect destRect6 = new Rect(x, y, x + spriteWidth, y + spriteHeight);
				canvas.drawBitmap(dillon_waits, sourceRect, destRect6, null);
			break;
		case NO_DILLON: //dillon is gone
			break;
		}
		
	}

	public int getHeight() {
		return spriteHeight;
	}

	public boolean hasPommesHit(int xPommesPosition) {
		if(xPommesPosition >= this.x+varianzTreffflaecheLinks && xPommesPosition <= this.x+spriteWidth-varianzTreffflaecheRechts){
			return true;
		} else {
			return false;
		}
	}
	


	
}
