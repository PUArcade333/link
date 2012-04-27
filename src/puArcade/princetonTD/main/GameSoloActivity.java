package puArcade.princetonTD.main;

import com.link.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ImageButton;

public class GameSoloActivity extends Activity implements OnClickListener {

	private GameBoardView gbv;
	private RefreshHandler mRedrawHandler = new RefreshHandler();

	private TextView scoreText;
	private TextView waveText;
	private TextView lifeText;
	private TextView goldText;


	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			GameSoloActivity.this.updateUI();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private void updateUI(){
		mRedrawHandler.sleep(1000);
		scoreText.setText("Score: " + gbv.getScore());
		waveText.setText("Next wave: " + gbv.getWave());
		lifeText.setText("Lives: " + gbv.getLives());
		goldText.setText("Gold: " + gbv.getGold());
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindow();

		setContentView(R.layout.main);
		gbv = (GameBoardView)findViewById(R.id.surfaceView1);
		gbv.requestFocusFromTouch();

		// Tower buttons
		final ImageButton arrow = (ImageButton)findViewById(R.id.tdbutton1);
		final ImageButton canon = (ImageButton)findViewById(R.id.tdbutton2);
		final ImageButton aa = (ImageButton)findViewById(R.id.tdbutton3);
		final ImageButton ice = (ImageButton)findViewById(R.id.tdbutton4);
		final ImageButton elect = (ImageButton)findViewById(R.id.tdbutton5);
		final ImageButton fire = (ImageButton)findViewById(R.id.tdbutton6);
		final ImageButton air = (ImageButton)findViewById(R.id.tdbutton7);
		final ImageButton earth = (ImageButton)findViewById(R.id.tdbutton8);
		arrow.setOnClickListener(this);
		canon.setOnClickListener(this);
		aa.setOnClickListener(this);
		ice.setOnClickListener(this);
		elect.setOnClickListener(this);
		fire.setOnClickListener(this);
		air.setOnClickListener(this);
		earth.setOnClickListener(this);

		// text fields
		scoreText = (TextView)findViewById(R.id.text1);
		waveText = (TextView)findViewById(R.id.text2);
		lifeText = (TextView)findViewById(R.id.text3);
		goldText = (TextView)findViewById(R.id.text4);

		updateUI();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}


	private void initWindow() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	public void onClick(View v) {
		gbv.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra("SCORE", gbv.getScore());
			setResult(Activity.RESULT_OK, resultIntent);
			gbv.finish();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
