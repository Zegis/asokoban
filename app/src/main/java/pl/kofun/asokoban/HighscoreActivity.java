package pl.kofun.asokoban;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HighscoreActivity extends MenuActivity{

	private String nameToAdd;
	private int valueToAdd;
	
	SharedPreferences sharedPref;
	
	int[] score;
	String[] name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore);

		Intent intent = getIntent();
		
		nameToAdd = intent.getStringExtra(DisplayMessageActivity.EXTRA_NAME);
		valueToAdd = intent.getIntExtra(DisplayMessageActivity.EXTRA_VALUE, 0);
		

		
		loadHighscore();
	}
	
	private void loadHighscore()
	{
		
		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		
		score = new int[3];
		score[0] = sharedPref.getInt(getString(R.string.PlaceValue3rd), 300);
		score[1] = sharedPref.getInt(getString(R.string.PlaceValue2nd), 200);
		score[2] = sharedPref.getInt(getString(R.string.PlaceValue1st), 100);
		
		name = new String[3];
		name[0] = sharedPref.getString(getString(R.string.PlaceName3rd), getString(R.string.app_name));
		name[1] = sharedPref.getString(getString(R.string.PlaceName2nd), getString(R.string.app_name));
		name[2] = sharedPref.getString(getString(R.string.PlaceName1st), getString(R.string.app_name));
		
		if(valueToAdd != 0)
		{
			for(int i=2; i>=0; --i)
			{
				if(valueToAdd < score[i])
				{
					score[i] = valueToAdd;
					name[i] = nameToAdd;
					break;
				}
			}
		}
		
		TextView scores = (TextView) findViewById(R.id.Scores);
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=2; i>=0; --i)
		{
			builder.append(name[i]);
			builder.append(" - ");
			builder.append(score[i]);
			builder.append("\n\n");
		}
		
		scores.setText(builder.toString());
		scores.setTextSize(40);
	}
	
	protected void onStop()
	{
		super.onStop();
		
		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedPref.edit();
		
		edit.putInt(getString(R.string.PlaceValue3rd), score[0]);
		edit.putInt(getString(R.string.PlaceValue2nd), score[1]);
		edit.putInt(getString(R.string.PlaceValue1st), score[2]);
		
		edit.putString(getString(R.string.PlaceName3rd), name[0]);
		edit.putString(getString(R.string.PlaceName2nd), name[1]);
		edit.putString(getString(R.string.PlaceName1st), name[2]);
		
		edit.commit();
	}

}
