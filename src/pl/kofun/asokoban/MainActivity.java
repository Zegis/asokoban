package pl.kofun.asokoban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends MenuActivity {

	public final static String EXTRA_MESSAGE = "pl.kofun.asokoban.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void sendMessage(View view)
	{
		
		EditText nameInput = (EditText) findViewById(R.id.inputName); // (EditText) view ??? Check this out later!
		String name = nameInput.getText().toString();
		
		if(ProperPlayerName(name))
		{
			Intent intent= new Intent(this, DisplayMessageActivity.class);
			
			intent.putExtra(EXTRA_MESSAGE,name);
		
			startActivity(intent);
		}
		else
		{
			TextView errorView = (TextView) findViewById(R.id.name_error_label);
			errorView.setText(R.string.name_error);
		}
	}
	
	private boolean ProperPlayerName(String name)
	{
		
		if(name.isEmpty() || name.length() <= 2)
			return false;
		else
			return true;
		
		
	}
	
}
