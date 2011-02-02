package com.example.VoiceRecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class VoiceRecorder extends Activity implements OnClickListener, OnTouchListener
{
	Button recordButton = null;
	Button playButton = null;	
	MediaRecorder recorder = null;
	final String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.main);
	   recordButton = (Button)findViewById(R.id.record_button);
	   playButton = (Button)findViewById(R.id.play_button);
	   recordButton.setOnTouchListener(this);
	   playButton.setOnClickListener(this);
	}

	public boolean onTouch(View v, MotionEvent event) {		
	   if(event.getAction() == MotionEvent.ACTION_DOWN) {	
		   recorder = new MediaRecorder();
		   try {
	          synchronized(this){
	            String state = android.os.Environment.getExternalStorageState();
	        	if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
	        	   throw new IOException("SD Card is not mounted.  It is " + state + ".");
	        	}

	        	// make sure the directory we plan to store the recording in exists
	        	File directory = new File(filename).getParentFile();
	        	if (!directory.exists() && !directory.mkdirs()) {
	        	   throw new IOException("Path to file could not be created.");
	            }

	        	recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        	recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        	recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        	recorder.setOutputFile(filename);
	        	recorder.prepare();
	        	recorder.start();	        	  
	    	    v.setPressed(true);
	          }
	        } catch(IOException e) {
	        	e.printStackTrace();
	        }
	   }
	   else if (event.getAction() == MotionEvent.ACTION_UP) {
		   try{
			   recorder.stop();
			   recorder.release();
			   v.setPressed(false);
		   }
		   catch(IllegalStateException e){
			   e.printStackTrace();
		   }
	   }
	   return true;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.play_button) {
			MediaPlayer mediaPlayer = new MediaPlayer();			      
			try {
			   
			    File file = new File(filename);
			    FileInputStream fis = new FileInputStream(file);
			    mediaPlayer.setDataSource(fis.getFD());
			    mediaPlayer.prepare();
			    mediaPlayer.start();

			} catch(FileNotFoundException e){
				e.printStackTrace();			 
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}