package pl.kofun.asokoban;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

public class DisplayMessageActivity extends MenuActivity implements GestureDetector.OnGestureListener
{

	static final public int  ID_MENU_RESTART = 1;
	private GestureDetectorCompat mDetector; 

	public final static String EXTRA_NAME = "pl.kofun.asokoban.PLAYERNAME";
	public final static String EXTRA_VALUE= "pl.kofun.asokoban.PLAYERVALUE";
	
	private int lvlNum = 1;
	
	private char[][] mapDataForRestart;
	private char[][] mapData;
	char oldField;
	
	private int toWin = 0;
	private int steps = 0;
	
	String name;
	
	private Point playerPos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Intent intent = getIntent();
		name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		
		setContentView(R.layout.activity_display_message);
		
		TextView text = (TextView) findViewById(R.id.Nametext);
		text.setText(name);
		
		loadLvl(lvlNum);
		
		mDetector = new GestureDetectorCompat(this,this);
		
	}
	
	private void loadLvl(int lvlNumber)
	{
		int levelWidth = 0;
		int levelHeight = 0;
		toWin = 0;
		
		InputStream stream = getResources().openRawResource(R.raw.levels);
		InputStreamReader streamreader = new InputStreamReader(stream); 
		BufferedReader reader = new BufferedReader(streamreader);
		
		String line;
		
		try{
			while( (line = reader.readLine()) != null)
			{
				if(line.matches("[-][\\d]+[-]"))
				{
					if(lvlNumber == Integer.parseInt(line.replaceAll("[\\D]","")))
					{
						line = reader.readLine();
						levelWidth = Integer.parseInt(line.split("x")[0]);
						levelHeight = Integer.parseInt(line.split("x")[1]);
						
						mapDataForRestart = new char[levelHeight][levelWidth];
						mapData = new char[levelHeight][levelWidth];
						char c;
						for(int i = 0; i < levelHeight; ++i)
							for(int j=0; j < levelWidth; ++j)
							{
								do
									c = (char)reader.read();
								while(c == '\n' || c == '\r');
									
								mapData[i][j] = c;
								mapDataForRestart[i][j] = c;
								
								if(c == 'o')
									toWin++;
								if(c == 's')
									playerPos = new Point(i,j);
								
								oldField = '.';
							}
						
						break;
					}
				}
					
			}
			
			String dataToPass = prepareMapData(mapData);
			
			MapView map = (MapView)findViewById(R.id.Map);
			map.setMapData(levelHeight, levelWidth, dataToPass);
			
		}catch(IOException e)
		{
			finish();
		}
		
	}
	
	private String prepareMapData(char[][] data)
	{
		StringBuilder build = new StringBuilder();
		for(int i=0; i < data.length; ++i)
			for(int j=0; j < data[i].length; ++j)
				build.append(data[i][j]);
		
		return build.toString();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE,ID_MENU_RESTART,120,R.string.menu_restart);
//		menu.add(Menu.NONE,ID_MENU_RESTART,120,R.string.menu_new);
		
		return true;
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ID_MENU_RESTART:
			restartApp();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
	}

	private void restartApp()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.restart_content)
	       	   .setTitle(R.string.menu_restart);
		builder.setPositiveButton(R.string.menu_accept, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				PerformAppRestart();
				
			}
		});
		builder.setNegativeButton(R.string.menu_decline, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void PerformAppRestart()
	{
		loadLvl(lvlNum);
		
		RestartSteps();
	}
	
	private void RestartSteps()
	{
		steps = 0;
		
		TextView text = (TextView)findViewById(R.id.ScoreValue);
		text.setText(Integer.toString(steps));
	}
	
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

	

	@Override
	public boolean onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {
//		Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
		Log.d("FLING", getFlingDirection(start, end).toString());
		MovePlayer(getFlingDirection(start, end));
		return true;
	}
	
	private FlingDirection getFlingDirection(MotionEvent start, MotionEvent end)
	{
		float downX = start.getX();
		float downY = start.getY();
		
		float currentX = end.getX();
		float currentY = end.getY();
		
		if( Math.abs(downX - currentX) > Math.abs(downY - currentY) )
		{
			if(downX > currentX)
				return FlingDirection.LEFT;
			else if (downX < currentX)
				return FlingDirection.RIGHT;
		}
		else
		{
			if(downY > currentY)
				return FlingDirection.UP;
			else if (downY < currentY)
				return FlingDirection.DOWN;
		}
		
		return FlingDirection.NoN;
	}
	
	private void MovePlayer(FlingDirection direction)
	{
		int newI = playerPos.x, newJ = playerPos.y;
		
		switch(direction)
		{
		case UP: newI -= 1; break;
		case DOWN: newI += 1; break;
		case LEFT: newJ -= 1; break;
		case RIGHT: newJ += 1; break;
		}
		
		if(newI <= mapData.length && newJ <= mapData[0].length)
			if(mapData[newI][newJ] == '.' || mapData[newI][newJ] == '*')
			{
				mapData[playerPos.x][playerPos.y] = oldField;
				playerPos.x = newI;
				playerPos.y = newJ;
				oldField = mapData[newI][newJ];
				mapData[newI][newJ] = 's';
				
				String dataToPass = prepareMapData(mapData);
				
				MapView map = (MapView)findViewById(R.id.Map);
				map.updateMapData(dataToPass);
				
				UpdateSteps();
			}
			else if(mapData[newI][newJ] != '#')
			{
				if (MoveCrate(newI, newJ, direction))
				{
					mapData[playerPos.x][playerPos.y] = oldField;
					playerPos.x = newI;
					playerPos.y = newJ;
					oldField = (mapData[newI][newJ] == 'o') ? '.' : '*';
					mapData[newI][newJ] = 's';
					
					String dataToPass = prepareMapData(mapData);
					
					MapView map = (MapView)findViewById(R.id.Map);
					map.updateMapData(dataToPass);
					
					UpdateSteps();
					CheckWin();
				}
			}
	}
	
	private boolean MoveCrate(int i, int j, FlingDirection direction)
	{
		boolean check = (mapData[i][j] == 'O')? true : false;
		
		switch(direction)
		{
		case UP: i -= 1; break;
		case DOWN: i += 1; break;
		case LEFT: j -= 1; break;
		case RIGHT: j += 1; break;
		}
		
		if(i <= mapData.length && j <= mapData[0].length)
		{
			if(mapData[i][j] == '.')
			{
				mapData[i][j] = 'o';
				if(check) toWin++; 
				return true;
			}
			else if (mapData[i][j] == '*')
			{
				mapData[i][j] = 'O';
				toWin--;
				return true;
			}
			
		}
			
		
		return false;
	}
	
	private void UpdateSteps()
	{
		steps++;
		
		TextView text = (TextView)findViewById(R.id.ScoreValue);
		text.setText(Integer.toString(steps));
	}
	
	private void CheckWin()
	{
		if(toWin == 0)
		{
			if(lvlNum < 2)
				loadLvl(++lvlNum);
			else
			{
				Intent intent= new Intent(this, HighscoreActivity.class);
				
				intent.putExtra(EXTRA_NAME,name);
				intent.putExtra(EXTRA_VALUE,steps);
			
				startActivity(intent);
				
				finish();
			}
		}
	}
	

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
}
