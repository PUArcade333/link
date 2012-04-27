package puArcade.princetonTD.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.link.R;

public class PrincetonTD extends Activity implements OnClickListener {

	private int score = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set layout
		initWindow();
		setContentView(R.layout.menu);

		// set click listeners
		Button solo = (Button)findViewById(R.id.solo);
		solo.setOnClickListener(click);
		Button multi = (Button)findViewById(R.id.multi);
		multi.setOnClickListener(click);
	}

	private void initWindow() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	private OnClickListener click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.solo) {
				Intent newGame = new Intent(PrincetonTD.this, puArcade.princetonTD.main.GameSoloActivity.class);
				startActivityForResult(newGame, 0);
			}
			else if (v.getId() == R.id.multi) {
				// not implemented
				finish();
			}

        }
	};
	
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.solo) {
			Intent newGame = new Intent(this, GameSoloActivity.class);
			startActivityForResult(newGame, 0);
		}
		else if (arg0.getId() == R.id.multi) {
			// not implemented
			finish();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				TextView scoreText = (TextView)findViewById(R.id.score);
				scoreText.setText("" + data.getIntExtra("SCORE",0));
			}
		}
	}

	// to send score back to linker
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra("SCORE", score);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
