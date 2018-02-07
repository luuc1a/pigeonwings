package com.clit_it.brendadillon;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class NewGame extends Activity {


	NewGameView mNewGameView;
//	TextView tv1,tv2;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("NewGame.java", "onCreate()");
		
//		Toast rt = Toast.makeText(NewGame.this, "onCreate()-NewGame", Toast.LENGTH_SHORT);
//		rt.show();
		
//		Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		No Sleeping Screen
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.newgame);
		mNewGameView = (NewGameView) findViewById(R.id.newgameview);
		mNewGameView.setTextView((TextView) findViewById(R.id.textview1));	
		
	}
		
	
//	private int group1Id = 1;
//
//	int easyId = Menu.FIRST;
//	int normalId = Menu.FIRST +1;
//	int hardId = Menu.FIRST +2;
//	int unplayableId = Menu.FIRST +3;

//	   @Override
//	    public boolean onCreateOptionsMenu(Menu menu) {
//	    menu.add(group1Id, easyId, easyId, "").setTitle("EASY");
//	    menu.add(group1Id, normalId, normalId, "").setTitle("NORMAL");
//	    menu.add(group1Id, hardId, hardId, "").setTitle("HARD");
//	    menu.add(group1Id, unplayableId, unplayableId, "").setTitle("UNPLAYABLE");
//	    return super.onCreateOptionsMenu(menu); 
//	    }

//	   @Override
//	    public boolean onOptionsItemSelected(MenuItem item) {
//
//	    switch (item.getItemId()) {
//
//	case 1:
//	    mNewGameView.setLevel(DillonMoves.EASY);
//	    return true;
//
//	case 2:
//	    mNewGameView.setLevel(DillonMoves.DEFAULT);
//	    return true;
//
//	case 3:
//	    mNewGameView.setLevel(DillonMoves.HARD);
//	    return true;
//
//	case 4:
//	    mNewGameView.setLevel(DillonMoves.UNPLAYABLE);
//	    return true;
//	default:
//	    break;
//	       }
//	    return true;
//	}
	

	@Override
	 protected void onRestoreInstanceState(Bundle savedInstanceState) {
		boolean stateWasSaved = savedInstanceState.getBoolean("stateWasSaved");
		
		if(stateWasSaved == false){
			//do nothing
		}
		else{ //if game data was saved, load it
			  
			  
			mNewGameView.initializeStuff();
			
//			mNewGameView.setLevel(savedInstanceState.getInt("LEVEL"));
			
			mNewGameView.setBackgroundColor(savedInstanceState.getInt("BACK_COLOR"));
						
			mNewGameView.setNoDillon(savedInstanceState.getBoolean("NODILLON"));
			
			mNewGameView.gameOver.setMode(savedInstanceState.getInt("GAMEOVER_MODE"));			
			
			mNewGameView.setDilloncounter(savedInstanceState.getInt("DILLONCOUNTER"));
			
			mNewGameView.numberOfDillons=savedInstanceState.getInt("NUMBERDILLONS");
			mNewGameView.setShitcounter(savedInstanceState.getInt("SHITCOUNTER"));			
			
			mNewGameView.initializeBitmaps();  ///BRENDA, DILLON, SHIT und POMMES Objekte werden neu erzeugt.

			
			if(savedInstanceState.getBoolean("NODILLON")==false){				
				for(int i=0;i<=mNewGameView.getDilloncounter();i++){
					mNewGameView.dillon[i].setMode(savedInstanceState.getInt("DILLON_MODE"+i));
					mNewGameView.dillon[i].setLevel(savedInstanceState.getInt("DILLON_LEVEL"+i));
				}
				for(int i=0;i<=mNewGameView.getShitcounter();i++){
					mNewGameView.shit[i].setMode(savedInstanceState.getInt("SHIT_MODE"+i));
					mNewGameView.shit[i].setShitOnGroundTime(savedInstanceState.getInt("DILLON_LEVEL"+i));
					mNewGameView.shit[i].setXPosition(savedInstanceState.getInt("SHIT_X"+i));
					mNewGameView.shit[i].isGoodieThere = savedInstanceState.getBoolean("GOODIE_THERE"+i);
					mNewGameView.shit[i].goodie = savedInstanceState.getInt("GOODIE"+i);
				}
			}
			
			mNewGameView.pommes.setMode(savedInstanceState.getInt("POMMES_MODE"));
			mNewGameView.pommes.setXPosition(savedInstanceState.getInt("POMMES_X"));
			
			
			mNewGameView.getBrenda().setMode(savedInstanceState.getInt("BRENDA_MODE"));
			mNewGameView.getBrenda().setCarriesAPommes(savedInstanceState.getBoolean("BRENDA_CARRIESPOMMES"));
			mNewGameView.getBrenda().setXPosition(savedInstanceState.getInt("BRENDA_X"));
			mNewGameView.getBrenda().setSpeed(savedInstanceState.getDouble("BRENDA_SPEED"));
			mNewGameView.getBrenda().setShitMaskState(savedInstanceState.getInt("BRENDA_SHITMASK"));
			mNewGameView.getBrenda().setHitCounter(savedInstanceState.getInt("BRENDA_HITCOUNTER"));

			mNewGameView.getBrenda().brendaCarriesGoodie = savedInstanceState.getBoolean("BRENDA_CARRIESGOODIE");
			mNewGameView.getBrenda().it_is_raining = savedInstanceState.getBoolean("BRENDA_RAINING");

			mNewGameView.getBrenda().setGoodieTime(savedInstanceState.getLong("GOODIETIME"));
			
		  }

		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	 protected void onSaveInstanceState(Bundle outState) {
		
//		Toast rt = Toast.makeText(NewGame.this, "onSaveInstanceState()-NewGame", Toast.LENGTH_SHORT);
//		rt.show();
		
		outState.putBoolean("stateWasSaved", true);
		
//		outState.putInt("LEVEL", mNewGameView.getLevel());
		   
		outState.putInt("BACK_COLOR", mNewGameView.getBackgroundColor());
		
		outState.putInt("GAMEOVER_MODE", mNewGameView.gameOver.getMode());

		outState.putInt("DILLONCOUNTER", mNewGameView.getDilloncounter());
		outState.putInt("NUMBERDILLONS", mNewGameView.numberOfDillons);
		outState.putInt("SHITCOUNTER", mNewGameView.getShitcounter());

		
		outState.putBoolean("NODILLON", mNewGameView.isNoDillon());

		if(mNewGameView.isNoDillon()==false){
			for(int i=0;i<=mNewGameView.getDilloncounter();i++){
				outState.putInt("DILLON_MODE"+i, mNewGameView.dillon[i].getMode());
				outState.putInt("DILLON_LEVEL"+i, mNewGameView.dillon[i].getLevel());
			}
		
			for(int i=0;i<=mNewGameView.getShitcounter();i++){
				outState.putInt("SHIT_MODE"+i, mNewGameView.shit[i].getMode());
				outState.putInt("SHIT_X"+i, mNewGameView.shit[i].getXPosition());
				outState.putBoolean("GOODIE_THERE"+i, mNewGameView.shit[i].isGoodieThere);
				outState.putInt("GOODIE"+i , mNewGameView.shit[i].goodie);

				
			}
		} else {
			for(int i=0;i<=mNewGameView.getDilloncounter();i++){
				outState.putInt("DILLON_MODE"+i, 2);
			}
		
			for(int i=0;i<=mNewGameView.getShitcounter();i++){
				outState.putInt("SHIT_MODE"+i, 0);
				outState.putInt("SHIT_X"+i, 0);
			}
		}
		
		outState.putInt("POMMES_MODE", mNewGameView.pommes.getMode());
		outState.putInt("POMMES_X", mNewGameView.pommes.getXPosition());
		

		outState.putInt("BRENDA_MODE", mNewGameView.getBrenda().getMode());
		outState.putBoolean("BRENDA_CARRIESPOMMES", mNewGameView.getBrenda().getCarriesAPommes());
		outState.putInt("BRENDA_X", mNewGameView.getBrenda().getXPosition());
		outState.putDouble("BRENDA_SPEED", mNewGameView.getBrenda().getSpeed());
		outState.putInt("BRENDA_SHITMASK", mNewGameView.getBrenda().getShitMaskState());
		outState.putInt("BRENDA_HITCOUNTER", mNewGameView.getBrenda().getHitCounter());
		
		outState.putBoolean("BRENDA_CARRIESGOODIE", mNewGameView.getBrenda().brendaCarriesGoodie);
		outState.putBoolean("BRENDA_RAINING", mNewGameView.getBrenda().it_is_raining);		
		outState.putLong("GOODIETIME", mNewGameView.getBrenda().getGoodieTime());
		
		super.onSaveInstanceState(outState);
		
	}
	

	
}
