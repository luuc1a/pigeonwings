package com.clit_it.brendadillon;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;


///////////////////////////////// THREAD //////////////////////////////////////////

public class NewGameThread extends Thread {

//	private static final String TAG="NewGameThread()";
	
	 /** Handle to the surface manager object we interact with */
    private SurfaceHolder mSurfaceHolder;

    /** Message handler used by thread to interact with TextView */
    private Handler mHandler;
    
 	// The actual view that handles inputs
 	// and draws to the surface
 	private NewGameView mNewGameView;
    
    /** Indicate whether the surface has been created & is ready to draw */
    private boolean running;
    private boolean mPause;
    
	
	public NewGameThread(SurfaceHolder mSurfaceHolder, NewGameView mNewGameView, Handler mHandler) {
		super();
		this.mSurfaceHolder = mSurfaceHolder;
		this.mNewGameView = mNewGameView;
		this.mHandler = mHandler;
	}
	
	
	
	public void setTextViewText(String message, int visibility){
		synchronized(mSurfaceHolder){
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
//			b.putString("text", "Let's have a look at what you've guessed ... ");
//			b.putInt("viz", View.VISIBLE);
			b.putString("text", message);
			b.putInt("viz", visibility);
			msg.setData(b);
			mHandler.sendMessage(msg);	
		}
	}

	
	
	
	public void setRunning(boolean running) {
		this.running = running;
	}
    
	public void resumeThread() {
		Log.i("NewGameView.NewGameThread.java", "resumeThread()");
//		Toast rt = Toast.makeText(mContextView, "resumeThread()-NewGame", Toast.LENGTH_SHORT);
//		rt.show();
		synchronized(mSurfaceHolder) {
			mPause = false;
		}
	}
    
	public void pauseThread(){
		Log.i("NewGameView.NewGameThread.java", "pauseThread()");
//		Toast rt = Toast.makeText(mContextView, "pauseThread()-NewGame", Toast.LENGTH_SHORT);
//		rt.show();
		synchronized (mSurfaceHolder){
		    mPause = true;
		}
	}

	@Override
	public void run() {
		
//		Log.d(TAG, "Starting game loop");
		
		Canvas canvas;
		
		while (running) {
			
			if(mPause){
				break;
			}

			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.mSurfaceHolder.lockCanvas();
				
				synchronized (mSurfaceHolder) {
					
					this.mNewGameView.update();
					
					this.mNewGameView.render(canvas);
					
				}
			} finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}
	
}


