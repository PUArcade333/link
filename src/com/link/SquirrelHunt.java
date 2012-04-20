package com.link;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class SquirrelHunt extends Activity implements OnGestureListener {

	private GestureDetector gestureScanner;
	private TextView text;
	private TextView text2;
	private SquirrelView mySquirrelView;
	private boolean started = false;
	private int score;
	private int counter;
	private int type;
	
	private static final int BROWNSQ = 1;
	private static final int REDSQ = 2;
	private static final int SKUNK = 3;
	private static final int TREE = 4;
	
	private RefreshHandler mRefreshHandler = new RefreshHandler();
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		return;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		return;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		if (mySquirrelView != null)
		{
			if (mySquirrelView.getGameOver())
				text.setText("Game Over! Score: " + score);
			else
			{				
				mySquirrelView.update();
				mRefreshHandler.sleep(50);
	
		//		text.setText("" + e.getX() + " AND " + e.getY());
				
				if (mySquirrelView.checkTouched(e.getX(), e.getY()))
				{
					type = mySquirrelView.getType();
					
					if (type == BROWNSQ)
					{
						score += 10;
						counter++;
						text.setText("Score: " + score);
					
						if (counter > 5)
						{
							score += 10 * (counter - 5);
							text.setText("Score: " + score + " - " + counter + "in a row!");
						}
					}
					else if (type == REDSQ)
					{
						score += 100;
						counter++;
						text.setText("Score: " + score);
						
						if (counter > 5)
						{
//							score += 10 * (counter - 5);
							text.setText("Score: " + score + " - " + counter + "in a row!");
						}
					}
					else if (type == SKUNK)
					{
						score -= 100;
						
						if (score < 0)
							score = 0;
						counter = 0;
						text.setText("Score: " + score + " - Oops!");
					}
					
					
					mySquirrelView.updateNew();
				}
				else //if (type != SKUNK)
				{
					text.setText("Score: " + score + " - Miss!");
					counter = 0;
				}
			}
		}		
//		if (!started)
//			text2.setText("" + e.getRawX() + " AND " + e.getRawY());

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gestureScanner = new GestureDetector(this);

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.squirrelgame);
		text = (TextView) findViewById(R.id.gametext);
		
		mySquirrelView = (SquirrelView) findViewById(R.id.game);
		score = 0;
		counter = 0;
		
		started = true;
		
		mySquirrelView.update();
		mySquirrelView.setTextView(text);
		text.setText("Score: " + score);
	}
	
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (mySquirrelView != null)
				mySquirrelView.update();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	@Override
	protected void onStop() {
		System.exit(0);
	}

//	@Override
//	protected void onPause() {
//		System.exit(0);
//	}
}	