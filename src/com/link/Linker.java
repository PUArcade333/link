package com.link;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Linker extends Activity {
	
	// game ids
	private final int SNAKE_ID = 0;
	private final int SQUIRRELHUNT_ID = 1;
	private final int TD_ID = 2;
	private final int TABLES_ID = 3;
	private final int CONNECT_ID = 4;
	
	private Button button1;
	private Button button2;
	
	private String netid = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(click1);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(click2);
		
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			netid = extras.getString("netid");
		}

    }
	private OnClickListener click1 = new OnClickListener() {
		@Override
		public void onClick(View v) {
	        Intent myIntent = new Intent(Linker.this, SquirrelHunt.class);
	        Linker.this.startActivityForResult(myIntent, SQUIRRELHUNT_ID);
		}
	};

	private OnClickListener click2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
	        Intent myIntent = new Intent(Linker.this, Snake.class);
	        Linker.this.startActivity(myIntent);
	        }
	};
	
	public void setNetid(String netid) {
		this.netid = netid;
	}
}
