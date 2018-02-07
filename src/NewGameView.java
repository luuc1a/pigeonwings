package com.clit_it.brendadillon;


import java.lang.ref.WeakReference;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class NewGameView extends SurfaceView implements SurfaceHolder.Callback {

//	private static final String TAG="NewGameView()";
	
	private NewGameThread thread;

	Context mContext;	
	public static String filename = "GameData";	
	SharedPreferences data;
	
	/** Pointer to the text view to display "Paused.." etc. */
    private TextView mTextViewText;   
	
	private static long timeGameStarted;
    
    /** Stuff we want to use **/
    
    private BrendaMoves brenda;
    
    public DillonMoves[] dillon;
    private int dilloncounter;
    private int dillon_max;
    public int numberOfDillons;
    private boolean noDillon;
    
    public ShitMoves[] shit;
    private int shitcounter;
    private int shit_max;
    
    public PommesMoves pommes;
    
    private Paint myPaint, paintReset, paint1, paintLoading;
    
    private Random random;
	private long gameTime, goodieTime;//, sloganTime;
	
	private long boredomTime,frozenTime;
    
    private int bottomOfGameView;
    
    private Bitmap to_the_right, to_the_left, shoot;
    private Bitmap phone_line;//, reset, slogan_combo;
//    private boolean draw_slogan;

    private int theLuckyDillon;
    private boolean isTheLuckyOneChosen;
    
//    private Bitmap background_image;
    
    public GameOver gameOver;
    
    private int back_color;
    
//    private int level;
    
	private String time="";
	
	private boolean	screenIsClickable=false;
    
	
	public NewGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// adding the callback (this) to the surface holder to intercept events
		//This line sets the current class (MainGamePanel) as the handler for the events happening on the actual surface.
		getHolder().addCallback(this);
		
		mContext = context;
		data = mContext.getSharedPreferences(filename, 0); //0 steht für mode_private: nur diese app kann auf die daten zugreifen.

		
		
		/** CREATE AND LOAD HERE THE STUFF YOU WANT TO DRAW */

		initializeStuff();
		initializeBitmaps();
		
		timeGameStarted = System.currentTimeMillis();
		
//		restoreData();

		/* make the GamePanel focusable so it can handle events
		If GamePanel is setFocusable, it can handle events. 
		We added the callback and made it focusable in the constructor so we won’t miss.*/
		setFocusable(true);
	}
	
	public void initializeStuff(){
		
		gameOver = new GameOver(mContext);
		
		random = new Random();
		bottomOfGameView=0;
		
		to_the_right = BitmapFactory.decodeResource(getResources(), R.drawable.to_the_right);
		to_the_left = BitmapFactory.decodeResource(getResources(), R.drawable.to_the_left);
		shoot = BitmapFactory.decodeResource(getResources(), R.drawable.shoot);
		phone_line = BitmapFactory.decodeResource(getResources(), R.drawable.phone_line);
//		reset = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.reset),12,12,true);
		
		myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.STROKE); 

		paintReset = new Paint();
		paintReset.setTypeface(Typeface.SANS_SERIF);
		paintReset.setTextSize(18);
		paintReset.setAntiAlias(true);
		paintReset.setColor(Color.BLACK);
        
		paint1 = new Paint();
		paint1.setTypeface(Typeface.createFromAsset(mContext.getResources().getAssets(), "fonts/Antaviana Bold.ttf"));
		paint1.setTextSize(40);
		paint1.setAntiAlias(true);
		paint1.setColor(Color.BLACK);
		
		paintLoading = new Paint();
		paintLoading.setTypeface(Typeface.createFromAsset(mContext.getResources().getAssets(), "fonts/Antaviana Bold.ttf"));
		paintLoading.setTextSize(60);
		paintLoading.setAntiAlias(true);
		paintLoading.setColor(Color.BLACK);
		
		
		
		dillon_max=10;
		shit_max=dillon_max;
		
		setNoDillon(true);
		
        setDilloncounter(0);
        numberOfDillons=0;
		
		setShitcounter(0);
		
		back_color=0x99000000 + (120 << 16) + (120 << 8) + 120;
		
//		this.level=DillonMoves.DEFAULT;
		
		gameTime=-1;
		goodieTime=-1;
		boredomTime=-1;
		frozenTime=-1;
//		sloganTime=-1;
		
//		draw_slogan=false;
		isTheLuckyOneChosen=false;
		
	}
	
	public void initializeBitmaps(){
		
		setBrenda(new BrendaMoves(
				mContext,
				BitmapFactory.decodeResource(getResources(), R.drawable.brenda_waits_s)
				, BitmapFactory.decodeResource(getResources(), R.drawable.brenda_runs_left_s)
				, BitmapFactory.decodeResource(getResources(), R.drawable.brenda_runs_right_s)
				, 7, 400    // initial position
				, 6, 8));    // FPS and number of frames in the animation		
		
		pommes = new PommesMoves(
			BitmapFactory.decodeResource(getResources(), R.drawable.pommes_ground)
			, BitmapFactory.decodeResource(getResources(), R.drawable.pommes)
			, BitmapFactory.decodeResource(getResources(), R.drawable.pommes_up)
			, 310, 0 // initial x and y position (left, top of image)
			);
		
		dillon = new DillonMoves[dillon_max+1];	
		shit = new ShitMoves[shit_max+1];
		

		if(isNoDillon()==false){
			
			for(int i=0; i<=getDilloncounter();i++){
				dillon[i] = new DillonMoves(
						BitmapFactory.decodeResource(getResources(), R.drawable.dillon_waits)
						, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_shits)
						, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_vanishes)
						, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_eats)
						, 190, 0    // initial position
						, 70, 70    // width and height of sprite
						, 4, 4    // FPS and number of frames in the animation
						);
				dillon[i].setLevel(getRandomLevel());
			}

			
			for(int i=0;i<=getShitcounter();i++){
				shit[i] = new ShitMoves(
					BitmapFactory.decodeResource(getResources(), R.drawable.shit_falling)
					, BitmapFactory.decodeResource(getResources(), R.drawable.shit_down)
					, BitmapFactory.decodeResource(getResources(), R.drawable.schirm_down)
					, BitmapFactory.decodeResource(getResources(), R.drawable.pan_down)
					, 0, 0 // initial x and y position (left, top of image)
					);
			}
		}

//		background_image = BitmapFactory.decodeResource(getResources(), R.drawable.frozen_city);		
//		slogan_combo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.slogan_combo),100,50,true);
		
	}


	
//	public void setTextViewText(String message, int visibility){
//		thread.setTextViewText(message, visibility);
//	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height){
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		//if you run the program, click on the Home button, 
		//and then run your program again it will crash. 
		//This is because you cannot restart the same thread twice in Java. 
		//If you want to run a thread again, you need to create a new one.
		//for that reason: create a new thread in surfaceCreated

		thread = new NewGameThread(getHolder(), this, new HandleXMLObjects(this));
		
		thread.resumeThread();
		
		// start the game loop
		thread.setRunning(true);
		thread.start();
		screenIsClickable=true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		thread.pauseThread();
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
//				((Activity)getContext()).finish();
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
//		Log.d(TAG, "Thread was shut down cleanly");
	}

	
	

/************************************************************/	
/**Here's what handles the XML Objects from NewGame activity*/	
	static class HandleXMLObjects extends Handler {
		private final WeakReference<NewGameView> mService; 

	    HandleXMLObjects(NewGameView service) {
	        mService = new WeakReference<NewGameView>(service);
	    }
	    @Override
	    public void handleMessage(Message msg) {
	    	NewGameView service = mService.get();
	         if (service != null) {
	              service.handleMessage(msg);
	         } 
	    }
	}
	
	private void handleMessage(Message m){
        mTextViewText.setVisibility(m.getData().getInt("viz"));
        mTextViewText.setText(m.getData().getString("text"));
        Typeface tf = Typeface.createFromAsset(mContext.getResources().getAssets(), "fonts/Antaviana Bold.ttf");
        mTextViewText.setTypeface(tf);
        mTextViewText.setTextSize(25);
        mTextViewText.setTextColor(Color.WHITE);
	}	
	
	/** Installs a Pointer to the TextView */
	public void setTextView(TextView tv) {
		mTextViewText = tv;		
	}
	
/***********************************************************/	
	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {

//		try {
//			Thread.sleep(16);    // reduziert die wiederholungen dieser funktion auf 60mal/sek.
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		if(screenIsClickable){
		
			switch(me.getAction()){
			
			case MotionEvent.ACTION_DOWN:
					performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					
					if(me.getY() >= paintReset.getTextSize() &&
							me.getX()>=2*(this.getWidth()/3) && me.getX()<=this.getWidth()) {	///rechtes Bildschirmdrittel berührt
						getBrenda().setMode(BrendaMoves.BRENDA_RUNS_LEFT);
						getBrenda().setCurrentSpeed(getBrenda().getSpeed());		
						getBrenda().setWantsToThrowThePommes(false);
					}
					
					if(me.getY() >= paintReset.getTextSize() &&
							(me.getX()<=this.getWidth()/3 && me.getX()>=0)) {	///linkes Bildschirmdrittel berührt
						getBrenda().setMode(BrendaMoves.BRENDA_RUNS_RIGHT);
						getBrenda().setCurrentSpeed(-(getBrenda().getSpeed()));
						getBrenda().setWantsToThrowThePommes(false);
					}		
				
					if(me.getY() >= paintReset.getTextSize() &&
							me.getX()<=2*(this.getWidth()/3) && me.getX()>=this.getWidth()/3){ ///mittleres Bildschirmdrittel berührt
						getBrenda().setCurrentSpeed(0);
						getBrenda().setMode(BrendaMoves.BRENDA_WAITS);
						getBrenda().setWantsToThrowThePommes(true);
					}
	
//					if(me.getY()<=paintReset.getTextSize() && me.getY()>=0 
//							&& me.getX()>=(this.getWidth()-paintReset.getTextSize()-paintReset.measureText("RESET")) && me.getX()<=this.getWidth()){
//						setLoadingScreen();
//						try{
//							((Activity)getContext()).startActivity(new Intent("com.clit_it.brendadillon.NEWGAME"));
//						} finally {
//							((Activity)getContext()).finish();
//						}
//					}				
				break;
	
			case MotionEvent.ACTION_UP:
				getBrenda().setMode(BrendaMoves.BRENDA_WAITS);
				getBrenda().setCurrentSpeed(0);
				break;
			
	//		case MotionEvent.ACTION_MOVE:
	//			mNewGameThread.updateTouchPosition(me.getX(), me.getY(), v.getWidth(), v.getHeight());
	//			break;
			}
		} else {
			if(gameOver.clickable){
				switch(me.getAction()){
					case MotionEvent.ACTION_DOWN:
						///rechtes Bildschirmdrittel berührt   // AGAIN
						if(me.getY() >= paintReset.getTextSize() &&
						me.getX()>=2*(this.getWidth()/3) && me.getX()<=this.getWidth()) {
							performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
							setLoadingScreen();
							
							try{
								((Activity)getContext()).startActivity(new Intent("com.clit_it.brendadillon.NEWGAME"));
							} finally {
								((Activity)getContext()).finish();
							}
						}
						
						///linkes Bildschirmdrittel berührt   // QUIT
						if(me.getY() >= paintReset.getTextSize() &&
								(me.getX()<=this.getWidth()/3 && me.getX()>=0)) {
							// manual SHUTDOWN of the Activity
							thread.setRunning(false);
							((Activity)getContext()).finish();
						}
				}	
			}
		}
			
		//your onTouch function must return true. 
		//This avoids other listeners to take control of the event and mess up your application.
		return true;
	}


	private void setRandomBackgroundColor(){
		 int r = random.nextInt(255);
		 int g = random.nextInt(255);
		 int b = random.nextInt(255);
		 back_color= 0x99000000 + (r << 16) + (g << 8) + b;
	}
	
	public void setBackgroundColor(int color){
		 back_color = color;
	}
	
	public int getBackgroundColor(){
		 return back_color;
	}
	
//	public void setLevel(int level){
//		this.level=level;
//		if(isNoDillon()==false){
//			for(int i=0; i<=dilloncounter;i++){
//				dillon[i].setLevel(this.level);
//			}
//			for(int i=0; i<=shitcounter;i++){
//				shit[i].setShitOnGroundTime(dillon[i].getLevel());
//			}
//		}
//	}
	
//	public int getLevel(){
//		return level;
//	}
	
	public int getRandomLevel(){
		
		if(dilloncounter >= (dillon_max-4)){
			return random.nextInt(10)%4;
		}
		
		return random.nextInt(10)%3;
		
//		return level;
	}
	

	private boolean loadingScreen=false;
	
	private void setLoadingScreen(){
		loadingScreen=true;
	}
	
	private boolean getLoadingScreen(){
		return loadingScreen;
	}
	
	public void render(Canvas c) {
		
		if(c!=null){ /**If MainGamePanel renders, with canvas=null, activity will crash*/
			
			if(getLoadingScreen()){
				thread.setTextViewText(" ", View.INVISIBLE);
				c.drawColor(Color.BLACK);
				c.drawColor(back_color);
				c.drawText("LOADING ...", c.getWidth()/2-paintLoading.measureText("LOADING ...")/2, c.getHeight()/2-paintLoading.getTextSize()/2, paintLoading);
			} else {
			
				c.drawColor(Color.DKGRAY);
	//			c.drawBitmap(background_image, 0, 0, null);
				c.drawColor(back_color);
				
//				c.drawBitmap(reset, c.getWidth()-reset.getWidth()-paintReset.measureText("RESET"), paintReset.getTextSize()/2-reset.getHeight()/2, null);
//				c.drawText("RESET", c.getWidth()-paintReset.measureText("RESET"), paintReset.getTextSize(), paintReset);
				
				for(int i=0;i<=c.getWidth()/phone_line.getWidth();i++){
					c.drawBitmap(phone_line, i*phone_line.getWidth(), c.getHeight()/5, null);
				}
				
				c.drawBitmap(to_the_left, (c.getWidth()/3)/2-to_the_left.getWidth()/2, getBottomOfGameView()+(c.getHeight()-getBottomOfGameView())/2-to_the_left.getHeight()/2, null);
				c.drawBitmap(shoot, (c.getWidth()/3+(c.getWidth()/3)/2)-shoot.getWidth()/2, getBottomOfGameView()+(c.getHeight()-getBottomOfGameView())/2-shoot.getHeight()/2, null);
				c.drawBitmap(to_the_right, (2*c.getWidth()/3+(c.getWidth()/3)/2)-to_the_right.getWidth()/2, getBottomOfGameView()+(c.getHeight()-getBottomOfGameView())/2-to_the_right.getHeight()/2, null);
				
				if(getBrenda().getBrendaIsInitialized())
					getBrenda().draw(c);
				
				if(pommes.getPommesIsInitialized())
						pommes.draw(c);
				
				
				if(isNoDillon()==false){
					for(int i=0;i<=getDilloncounter();i++){
						if(dillon[i].getDillonIsInitialized())
							dillon[i].draw(c);
					}
		
					for(int i=0;i<=getShitcounter();i++){
						if(shit[i].getIsInitialized())
							shit[i].draw(c);
					}
				}
				
//				if(draw_slogan){
//					c.drawBitmap(slogan_combo, c.getWidth()/2-slogan_combo.getWidth()/2, c.getHeight()/2-slogan_combo.getHeight(), null);
//				}
				
				gameOver.draw(c);
			}
		}		
	}
	
	public void setBottomOfGameView(){
		getResources().getConfiguration();
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			bottomOfGameView = this.getHeight()-100;
		} else bottomOfGameView = this.getHeight()-160;
	}
	
	public int checkIfXPositionIsInGameView(int x, int bitmap_width){

		if(x>=this.getWidth()){
			x=(this.getWidth()-bitmap_width);
		}
		if(x<=0){
			x=0;
		}
		return x;
		
	}
	
	public int getBottomOfGameView(){
		return bottomOfGameView;
	}
	
//	private void drawSlogan(boolean b){
//		draw_slogan=b;
//	}

	
	//Create the game update method in the game panel and trigger it from the thread
	public void update() {
		int width=this.getWidth();
		int height=this.getHeight();			
		long now=System.currentTimeMillis();
		
		{
			int p = (width*80)/540;
			to_the_right = Bitmap.createScaledBitmap(to_the_right,p,p/2,true);
			to_the_left = Bitmap.createScaledBitmap(to_the_left,p,p/2,true);
			shoot = Bitmap.createScaledBitmap(shoot,p,p/2,true);
		}
		
		{
			int p = (width*160)/540;
			phone_line = Bitmap.createScaledBitmap(phone_line,p,p/10,true);
		}
		
		if(gameOver.getMode()==GameOver.NOT_OVER_YET){
				
			//INITIALISIERUNG BRENDA//
			if(getBrenda().getBrendaIsInitialized()==false){
				setBottomOfGameView();  ///ACHTUNG bei brenda.getXPosition kommt die rechte untere Eche retour!
				
				getBrenda().setBitmapSizes(width, height);
				
				int x = checkIfXPositionIsInGameView(getBrenda().getXPosition()-getBrenda().spriteWidth, getBrenda().spriteWidth);
				getBrenda().setXPosition(x);
				getBrenda().setYPosition(getBottomOfGameView()-getBrenda().spriteHeight);
				getBrenda().setBrendaIsInitialized(true);
				
				//Hintergrundbild wird skaliert
				//	background_image=Bitmap.createScaledBitmap(background_image,width,height, true);
				}
				
				//INITIALISIERUNG POMMES//
				if(pommes.getPommesIsInitialized()==false){
					setBottomOfGameView();
					pommes.setBitmapSizes(width, height);
					int x = checkIfXPositionIsInGameView(pommes.getXPosition(), pommes.bitmapWidth);
					pommes.setXPosition(x);
					pommes.setYPosition(getBottomOfGameView()-pommes.bitmapHeight);
					pommes.setLinePosition(height/5);
					pommes.setPommesIsInitialized(true);
				}						

				
				//BRENDAS BEWEGUNG NACH LINKS/RECHTS WIRD AKTUALISIERT//
				if(getBrenda().getMode()==BrendaMoves.BRENDA_RUNS_LEFT || getBrenda().getMode()==BrendaMoves.BRENDA_RUNS_RIGHT){
					getBrenda().changePosition(width);
				}

				//BRENDA - POMMES INTERACTION//
				if(pommes.mode==PommesMoves.POMMES_LIES){
					if(pommes.checkIfBrendaStandsOnPommes(getBrenda().getXPosition())){
						getBrenda().setCarriesAPommes(true);
						pommes.mode=PommesMoves.POMMES_CARRIED;
					}
				}
				
				if(getBrenda().getCarriesAPommes()){
					if(getBrenda().getMode()==BrendaMoves.BRENDA_WAITS){
						pommes.setXPosition(getBrenda().getXPosition()-2*pommes.bitmapWidth);
					}
					
					if(getBrenda().getMode()==BrendaMoves.BRENDA_RUNS_RIGHT){
						pommes.setXPosition(getBrenda().getXPosition()-3*pommes.bitmapWidth-pommes.bitmapWidth/2);
					}
					
					if(getBrenda().getMode()==BrendaMoves.BRENDA_RUNS_LEFT){
						pommes.setXPosition(getBrenda().getXPosition()-pommes.bitmapWidth/2);
					}
					
				}
				
				if(getBrenda().hasThrownThePommes()){
					pommes.mode=PommesMoves.POMMES_FLIES;
					getBrenda().setCarriesAPommes(false);
					getBrenda().setWantsToThrowThePommes(false);
				}
				
				
				//POMMES SOLL IN MODUS 5 GEHEN, WENN DILLON NICHT GETROFFEN WURDE//
				if(pommes.mode==PommesMoves.POMMES_UP && pommes.getPommesHitDillon()==false){
					pommes.mode=PommesMoves.POMMES_DIDNT_HIT;
				}
				
				
				//WÄHREND NOCH KEINE DILLONS DA SIND:
				if(isNoDillon()==true || boredomTime!=-1){
					//BRENDAS BOREDOM
					int seconds_now = (int)(now-timeGameStarted)/1000;
					if(boredomTime==-1){
						brenda.increaseStateOfBeingBored();
						boredomTime = seconds_now;
					}
					if(boredomTime+5<=seconds_now){
						brenda.setBored(true);
					}
					if(isNoDillon()==false){
						boredomTime=-1;
						brenda.setBored(false);
					}
					
					if(brenda.getBored()==true && boredomTime+8<=seconds_now){
						brenda.setBored(false);
						boredomTime=-1;
					} 
					
					
					//WENN DAS POMMES OBEN HÄNGT, KOMMT EIN ERSTER DILLON inkl. SHIT
					if(pommes.mode==PommesMoves.POMMES_TO_EAT){
						pommes.mode=PommesMoves.NO_POMMES;
						
						dillon[dilloncounter] = new DillonMoves(
								BitmapFactory.decodeResource(getResources(), R.drawable.dillon_waits)
								, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_shits)
								, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_vanishes)
								, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_eats)
								, (pommes.getXPosition()-54), 0    // initial position
								, 70, 70    // width and height of sprite
								, 4, 4);    // FPS and number of frames in the animation
						dillon[dilloncounter].setLevel(DillonMoves.HARD);
							
						shit[shitcounter] = new ShitMoves(
								BitmapFactory.decodeResource(getResources(), R.drawable.shit_falling)
								, BitmapFactory.decodeResource(getResources(), R.drawable.shit_down)
	 							, BitmapFactory.decodeResource(getResources(), R.drawable.schirm_down)
	 							, BitmapFactory.decodeResource(getResources(), R.drawable.pan_down)
								, 0, 0 // initial x and y position (left, top of image)
								);
					
						setNoDillon(false);
						brenda.setOMGPigeon(true);
					}
				}
				
				
				//DAS POMMES SOLL WIEDER AM BODEN LIEGEN, WENN ES WEG IST//
				if(pommes.mode == PommesMoves.NO_POMMES){
					int pos=0;
					if(getBrenda().getXPosition()<=width/2)
						pos=width-random.nextInt(width/4)-10;
					else if(getBrenda().getXPosition()>width/2)
						pos=random.nextInt(width/4)+10;
					
					pommes.reset();
					pommes.setXPosition(pos);
					pommes.setYPosition(getBottomOfGameView()-pommes.bitmapHeight);
					pommes.setPommesIsInitialized(true);
					
				}
				
				//brenda updates
				getBrenda().update(now);
				
				//pommes update
				pommes.update(now);
				
				
///SOBALD EIN ERSTER DILLON DA IST (noDillon==false) können all diese Sachen passieren:								
				if(isNoDillon()==false){

					
					//NEUE DILLONS SOLLEN KOMMEN, WENN POMMES HERUMHÄNGEN//
					boolean weneeddillon=false;
					int posDillon=0;
					if(pommes.mode==PommesMoves.POMMES_TO_EAT){
						posDillon=pommes.getXPosition()-54;
						weneeddillon=true;
					}
					
					if(weneeddillon){
						if(dilloncounter+1<=dillon_max){
							pommes.mode=PommesMoves.NO_POMMES;
						setDilloncounter(getDilloncounter() + 1);
						numberOfDillons++;
						dillon[dilloncounter] = new DillonMoves(
								BitmapFactory.decodeResource(getResources(), R.drawable.dillon_waits)
								, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_shits)
								, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_vanishes)
								, BitmapFactory.decodeResource(getResources(), R.drawable.dillon_eats)
								, posDillon, 0    // initial position
								, 70, 70    // width and height of sprite
								, 4, 4    // FPS and number of frames in the animation
								);
						dillon[dilloncounter].setLevel(getRandomLevel());	
					
						
					//NEUER TAUBENGACK SOLL KOMMEN, WENN EIN NEUER DILLON KOMMT//	
						setShitcounter(getShitcounter() + 1);
						shit[shitcounter] = new ShitMoves(
							BitmapFactory.decodeResource(getResources(), R.drawable.shit_falling)
							, BitmapFactory.decodeResource(getResources(), R.drawable.shit_down)
							, BitmapFactory.decodeResource(getResources(), R.drawable.schirm_down)
							, BitmapFactory.decodeResource(getResources(), R.drawable.pan_down)
							, 0, 0   // initial x and y position (left, top of image)
							);
					} else{ 
						if(isTheLuckyOneChosen==false){	
							int x=-5;
							for(int i=0;i<=getDilloncounter();i++){
								if(dillon[i].mode!=DillonMoves.NO_DILLON){
									if(x==-5){
										theLuckyDillon=i;
										x=dillon[i].getXPosition()-posDillon;
										if(x<0)
											x=-x;
									}
									
									int y = dillon[i].getXPosition()-posDillon;
									if(y<0)
										y=-y;
									
									if(y<x){
										x=y;
										theLuckyDillon=i;
									}
								}
							}
							if(x!=-5){
								isTheLuckyOneChosen=true;
								dillon[theLuckyDillon].goal_x=posDillon;
								dillon[theLuckyDillon].mode=DillonMoves.DILLON_ON_ITS_WAY;
							} else {
								isTheLuckyOneChosen=false;
								pommes.mode=PommesMoves.NO_POMMES;
							}
						}
					}
						}
					
					if(isTheLuckyOneChosen==true){
						if(dillon[theLuckyDillon].hasReachedPommes()==true){
							pommes.setMode(PommesMoves.NO_POMMES);
							dillon[theLuckyDillon].setMode(DillonMoves.DILLON_EATS);
							dillon[theLuckyDillon].goal_x=0;
							isTheLuckyOneChosen=false;
						}
					}
					
					boolean youWin=true;
//					int combo=0;
					//DILLON - POMMES INTERACTION//
					//DILLON SOLL SAMT POMMES VERSCHWINDEN, WENN ER GETROFFEN WIRD//
					/**muss vom Pommes aus abgefragt werden, sonst sind keine Hit-Kombos möglich**/
					if(pommes.mode==PommesMoves.POMMES_UP){   //also wenn das Pommes am Telefonkabel hängt
						for(int i=0;i<=getDilloncounter();i++){
							if(dillon[i].mode!=DillonMoves.NO_DILLON){
								if(dillon[i].hasPommesHit(pommes.getXPosition())){
									pommes.setPommesHitDillon(true);
									pommes.setMode(PommesMoves.NO_POMMES);
									dillon[i].setMode(DillonMoves.DILLON_VANISHES);
//									combo++;
								}
							}
						}
					}
//					if(combo>1){
//						drawSlogan(true);
//					}

				//BRENDA - GOODIE INTERACTION//
				if(getBrenda().brendaCarriesGoodie==false){					
					boolean goodieThere=false;
					for(int i=0;i<=shitcounter;i++){
						if(shit[i].isGoodieThere()){
							goodieThere=true;
							if(shit[i].checkIfBrendaStandsOnGoodie(getBrenda().getXPosition())){
								getBrenda().brendaCarriesGoodie=true;
								break;
							}
						}
					}
					if(goodieThere==false)
						randomGoodie(getBrenda().getHitCounter(),now);
				}					
					
//DILLON UND SHIT SCHLEIFE
					
				for(int i=0; i<=getDilloncounter();i++){
					//INITIALISIERUNG DILLON
					if(dillon[i].getDillonIsInitialized()==false){
						dillon[i].setBitmapSizes(width,height);
						dillon[i].setYPosition(height/5-dillon[i].getHeight()+2);
						dillon[i].setDillonIsInitialized(true);
					}
						
					//FÜR ALLE DILLONS DIE SICHTBAR SIND:
									
					if(dillon[i].getMode()!=DillonMoves.NO_DILLON){
						
						// DILLONS BEWEGEN SICH//
						if(dillon[i].getMode()!=DillonMoves.DILLON_ON_ITS_WAY){
							if(dillon[i].getDillonHasMoved()==false){				
								dillon[i].changePosition(width, now);			
							} else {
								dillon[i].resetMovement();
							}	
						}
						
						//MANCHE DILLONS GACKEN RUNTER - (JEDEM DILLON IST EIN GACK ZUGETEILT)
						if(dillon[i].getDillonWantsToShit()){
							if(shit[i].getMode()==ShitMoves.NO_SHIT){
								shit[i].setXPosition(dillon[i].getXPosition()+shit[i].bitmapWidth);
								shit[i].setMode(ShitMoves.SHIT_FALLING);
								dillon[i].setDillonShits(true);
							}
						}
						
						//SPIELENDE ist nicht erreicht, es existieren sichtbare Dillons
						youWin=false;
						//Dillon update
						dillon[i].update(now);
					}
					
					//INITIALISIERUNG TAUBENGACK					
					if(shit[i].getIsInitialized()==false){
						setBottomOfGameView();
						shit[i].setBitmapSizes(width,height);
						shit[i].setBottomOfGameView(getBottomOfGameView()-pommes.bitmapHeight);
						int x = checkIfXPositionIsInGameView(shit[i].getXPosition(), shit[i].bitmapWidth);
						shit[i].setXPosition(x);
						shit[i].setYPosition(height/5);
						shit[i].setLinePosition(height/5);
						shit[i].setIsInitialized(true);
						shit[i].setShitOnGroundTime(dillon[i].getLevel());
					}	
					
					//BRENDA - SHIT INTERACTION//
					shit[i].setShitHitBrenda(false);
					if(shit[i].getMode()==ShitMoves.SHIT_FALLING){
						shit[i].checkIfShitHitBrenda(getBrenda().getXPosition(), getBrenda().getYPosition()-getBrenda().spriteHeight);
						if(shit[i].getShitHitBrenda()==true) {
							if(getBrenda().brendaCarriesGoodie==false){
								getBrenda().setHitByShit(true);
								performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
								setRandomBackgroundColor(); //Hintergrundfarbe verändert sich
							}
						}
					}
					
					//SHIT - Update
					shit[i].update(now);
					
				} // ENDE DILLON-SHIT SCHLEIFE	
				
				
//				//SLOGANS
//				if(draw_slogan==true){
//					if(sloganTime==-1){
//						sloganTime=now;
//					}
//					if(sloganTime+1000<=now){
//						draw_slogan=false;
//					}
//				}	
				
				
				if(youWin){
					//SPIELENDE: Too many pigeons
					if(dilloncounter>=dillon_max)
						gameOver.setMode(GameOver.TOO_MANY_PIGEONS);
					else //SPIELENDE: Brenda hat alle erwischt.
						gameOver.setMode(GameOver.WON);
				}
				
				//SPIELENDE: Brenda is frozen
				if(brenda.isBrendaIsFrozen()==true){
					if(frozenTime==-1){
						frozenTime=now;
					}
					if(frozenTime+3500<=now){
						gameOver.setMode(GameOver.BRENDA_IS_FROZEN);
					}
				}				
			}			////Ende-Klammer für isNoDillon()==false
		
		}	////Ende-Klammer für GAME NOT OVER YET
		else {
			
			screenIsClickable=false;
			
			//GameOver Update
			gameOver.update(now);
				
			if(gameTime==-1){
				gameTime=now;
				setTime(gameTime);
			}
			
			if((gameTime+1500)<=now){
				if(gameOver.isShowText()==true){
					if(gameOver.getMode()==GameOver.WON)
						thread.setTextViewText("It took you "+getTime()+ getDillonEndState()+" \n", View.VISIBLE);
					if(gameOver.getMode()==GameOver.BRENDA_IS_FROZEN)
						thread.setTextViewText("It took you "+getTime()+ " to fail at this game.  \n", View.VISIBLE);
					if(gameOver.getMode()==GameOver.TOO_MANY_PIGEONS)
						thread.setTextViewText("This city ran out of pigeons and you're still alive! Here's your golden ticket to Venice! \n", View.VISIBLE);
				}
			}
			if((gameTime+4000)<=now){
				gameOver.clickable=true;
			}				
		}
	}
	
	private void randomGoodie(int hitcounter, long now){
		boolean g=true;
		int zufall=0;
		
		if(goodieTime==-1){
			goodieTime=now;
		}
		
		if(goodieTime+1500<=now){
			int start=0;
			if(shitcounter>1) start=random.nextInt(shitcounter-1);
			switch(hitcounter){
			case 0: 
				zufall=random.nextInt(shitcounter+20);
				break;
			case 1:
				for(int i=start;i<shitcounter;i++){
					if(shit[i].mode==ShitMoves.SHIT_FALLING){
						if(random.nextBoolean()) zufall=i;
						break;
					}
				}
				break;
			case 2:
				for(int i=start;i<shitcounter;i++){
					if(shit[i].mode==ShitMoves.SHIT_FALLING){
						if(random.nextBoolean()) zufall=i;
						break;
					}
				}
				break;
			case 3:
				for(int i=start;i<shitcounter;i++){
					if(shit[i].mode==ShitMoves.SHIT_FALLING){
						zufall=i;
						break;
					}
				}
				break;
			default: g=false;
			}
		
			if(g && zufall<=shitcounter){
				shit[zufall].setGoodieThere(true);
				if(hitcounter==0)
					getBrenda().setGoodie(shit[zufall].goodie=0);
				else {
					if(hitcounter == 3)
						getBrenda().setGoodie(shit[zufall].goodie=1);
					
					getBrenda().setGoodie(shit[zufall].goodie=(random.nextInt(13)%2));
				}
				goodieTime=-1;
			}
		}
	}
	
	private String getDillonEndState(){
		String text="";
		
		if(numberOfDillons==0){
			text="to lure one pigeon and get rid of it again.";
		} else text=" to lure "+Integer.toString(numberOfDillons+1)+" pigeons  -  and get rid of them again.";
		
		return text;
	}
	
	private void setTime(long gameTime) {
		int time_sec=(int)(System.currentTimeMillis()-timeGameStarted)/1000;
		int time_min=time_sec/60;
		time_sec=time_sec%60;
		int time_h=time_min/60;
		time_min=time_min%60;

		if(time_h>0){
			time = Integer.toString(time_h)+"h, "+Integer.toString(time_min)+"min "+Integer.toString(time_sec)+"sec ";
		}
		else if(time_min>0){
			time = Integer.toString(time_min)+"min "+Integer.toString(time_sec)+"sec ";					
		} else time = Integer.toString(time_sec)+"sec ";
		

	}
	
	private String getTime(){
		return time;
	}	

	public int getDilloncounter() {
		return dilloncounter;
	}

	public void setDilloncounter(int dilloncounter) {
		this.dilloncounter = dilloncounter;
	}

	public int getShitcounter() {
		return shitcounter;
	}

	public void setShitcounter(int shitcounter) {
		this.shitcounter = shitcounter;
	}

	public BrendaMoves getBrenda() {
		return brenda;
	}

	public void setBrenda(BrendaMoves brenda) {
		this.brenda = brenda;
	}

	public boolean isNoDillon() {
		return noDillon;
	}

	public void setNoDillon(boolean noDillon) {
		this.noDillon = noDillon;
	}	
	
	
}