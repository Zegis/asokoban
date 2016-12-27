package pl.kofun.asokoban;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			showAbout();
			return true;
		case R.id.menu_highscores:
			showScores();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showAbout()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.about_content)
	       	   .setTitle(R.string.menu_about);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void showScores()
	{
		Intent intent = new Intent(this, HighscoreActivity.class);
		
		startActivity(intent);
	}

}
