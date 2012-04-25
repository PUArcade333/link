package com.link;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Linker extends Activity {
	private static final String AUTHCODE = "cos333";
	private final String sendscoreurl = "http://webscript.princeton.edu/~pcao/cos333/sendscore.php";
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
	        Linker.this.startActivityForResult(myIntent, SNAKE_ID);
        }
	};
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		String gameid;
		if (resultCode == RESULT_OK) {
			gameid = String.valueOf(requestCode);
			Bundle result = data.getExtras();
			String score = String.valueOf(result.getInt("score"));
			// send the score
			SendScoreViaPHP task = new SendScoreViaPHP();
			task.execute(new String[] { sendscoreurl, netid, gameid, score });
           }
	}
	
	private class SendScoreViaPHP extends AsyncTask<String, String, String[]> {
    	@Override
    	protected String[] doInBackground(String... params) {
    		String sendscoreurl;
    		String netidIn;
    		String gameidIn;
    		String scoreIn;
    		
    		String[] result = new String[2];
    		// get url/login/password from params
    		try {
	    		sendscoreurl = params[0];
	    		netidIn = params[1];
	    		gameidIn = params[2];
	    		scoreIn = params[3];
	    		result[1] = gameidIn; 
    		} catch (Exception e) {
    			e.printStackTrace();
    			result[0] = "error";
    			return result;
    		}
    		System.out.println("attempting to send score: " + netidIn + ", " + gameidIn + ", " + scoreIn);
    		
    		// set up login/password to be posted to PHP
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("netid", netidIn));
    		nameValuePairs.add(new BasicNameValuePair("gameid", gameidIn));
    		nameValuePairs.add(new BasicNameValuePair("score", scoreIn));
    		nameValuePairs.add(new BasicNameValuePair("auth", AUTHCODE));
    		
    		InputStream content;
    		
    		// try getting http response
    		try {
    			// TODO: check for https functionality
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpPost httppost = new HttpPost(sendscoreurl);	        
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        HttpResponse response = httpclient.execute(httppost);
    	        HttpEntity entity = response.getEntity();
    	        content = entity.getContent();
    	    } catch(Exception e){
    	        Log.e("log_tag","Error in internet connection " + e.toString());
    	        result[0] = "error";
    			return result;
    	    }
    		System.out.println("post successful");
    		
    		// try reading http response
    		String output = "";
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content,"iso-8859-1"), 8);
    	        String line;
    	        while((line = reader.readLine()) != null){
    	            output += line;
    	        }
    	        content.close();
    		} catch(Exception e){
    	        Log.e("log_tag", "Error converting result " + e.toString());
    	        result[0] = "error";
    			return result;
    	    }
    		System.out.println(output);
    		result[0] = output;
			return result;
    	}
    	@Override
    	// process result of login query (true or false)
    	protected void onPostExecute(String results[]) {
    		final String sendscoresuccess = "yes"; // output from PHP to match
    		//final String sendscorefailure = "error";
    		String sendscoreresult = results[0];
    		String gameid = results[1];
    		if (sendscoreresult.equals(sendscoresuccess)) {
    			// send score success!
    			System.out.println("score for gameid: " + gameid);
    		} else {
    			// send score failed!
    			System.out.println("send score failed: " + gameid);
    		}
    	}
    }
	
	public void setNetid(String netid) {
		this.netid = netid;
	}
}
