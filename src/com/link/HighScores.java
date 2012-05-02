/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class HighScores extends Activity {
	private Button backtolobby;
	private LinearLayout highscores_list;
	private LayoutParams lparams;
	
	private static final String AUTHCODE = "cos333";
	private final String scoresurl = "http://webscript.princeton.edu/~pcao/cos333/gethighscores.php";
	private ArrayList<Score> scoreList;
	
	private class Score {
		String netid;
		String score;
		String gameid;
		public Score(String gameid, String netid, String score) {
			this.netid = netid;
			this.score = score;
			this.gameid = gameid;
		}
		public String getNetid() {
			return netid;
		}
		public String getScore() {
			return score;
		}
		public String getGameid() {
			return gameid;
		}
	}
	
	private class GetHighScoresViaPHP extends AsyncTask<String, String, String[]> {
    	protected String[] doInBackground(String... params) {
    		String getscoresurl;
    		//String gameid;
    		
    		String[] result = new String[] { "error", "" }; // to be returned
    		
    		try {
    			getscoresurl = params[0];
    			//gameid = params[1];
    		} catch (Exception e) {
    			e.printStackTrace();
    			result[0] = "error";
    			return result;
    		}

    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("auth", AUTHCODE));
    		//nameValuePairs.add(new BasicNameValuePair("gameid", gameid));
    		
    		InputStream content;
    		
    		// try getting http response
    		try {
    			// TODO: check for https functionality
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpPost httppost = new HttpPost(getscoresurl);	        
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        HttpResponse response = httpclient.execute(httppost);
    	        HttpEntity entity = response.getEntity();
    	        content = entity.getContent();
    	    } catch(Exception e){
    	        Log.e("log_tag","Error in internet connection " + e.toString());
    	        result[0] = "error";
    			return result;
    	    }
    		
    		// try reading http response
    		String output = "";
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content,"utf-8"), 8);
    	        String line;
    	        while((line = reader.readLine()) != null){
    	            output += line + "\n";
    	        }
    	        content.close();
    		} catch(Exception e){
    	        Log.e("log_tag", "Error converting result " + e.toString());
    	        result[0] = "error";
    			return result;
    	    }
    		Log.e("log_tag", "output: " + output);
    		result[0] = "yes";
    		result[1] = output;
    		return result;
    	}
    	protected void onPostExecute(String results[]) { // print lobby results
    		//final String scoressuccess = "yes";
    		final String scoresfailure = "error";
    		String scoresresult = results[0]; // status of the post
    		String scorestext = results[1];
    		System.out.println("got highscores: " + scorestext);
    		
    		String[] scoresdata = scorestext.split(";");
    		String[] gameid = new String[scoresdata.length/3];
    		String[] netid = new String[scoresdata.length/3];
    		String[] score = new String[scoresdata.length/3];
    		for (int i = 0; i < scoresdata.length/3; i++)
    		{
    			gameid[i] = scoresdata[i*3];
    			netid[i] = scoresdata[i*3+1];
    			score[i] = scoresdata[i*3+2];
    		}
    		scoreList = new ArrayList<Score>();

            for (int i = 0; i < netid.length; i++) {
                scoreList.add(new Score(gameid[i], netid[i], score[i]));
            }
    		
    		
    		if (!scoresresult.equals(scoresfailure)) {
    			// success
    			System.out.println("success");
    			addNewHighScoresTV(scorestext);

    		} else {
    			// failure
    			System.out.println("failed");
    		}
    	}
    }
	private void addNewHighScoresTV(String text) {
		System.out.println("adding text " + text);
		TextView tv = new TextView(this);
        tv.setLayoutParams(lparams);
        tv.setText(text);
        highscores_list.addView(tv);
		
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.highscores);
        
        GetHighScoresViaPHP task = new GetHighScoresViaPHP();
        task.execute(scoresurl, "0");
        //task.execute(scoresurl, "1");
        highscores_list = (LinearLayout) findViewById(R.id.highscores_layout);
        
        lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        
        
        backtolobby = (Button) findViewById(R.id.returntolobby);
        backtolobby.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        finish();
			}
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent msg) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		System.out.println("pressed back!");
	        finish();
    	}
    	return true;
    }
}
