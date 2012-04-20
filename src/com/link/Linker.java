package com.link;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Linker extends Activity {
	
	private Button button1;
	private Button button2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        

        button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(click1);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(click2);
    }
	private OnClickListener click1 = new OnClickListener() {
		@Override
		public void onClick(View v) {
	        Intent myIntent = new Intent(Linker.this, SquirrelHunt.class);
	        Linker.this.startActivity(myIntent);        
	        
		}
	};

	private OnClickListener click2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
	        Intent myIntent = new Intent(Linker.this, Snake.class);
	        Linker.this.startActivity(myIntent); 		
	        }
	};	
}
