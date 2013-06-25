package pl.kofun.asokoban;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class MapView extends View {

	Paint paint = new Paint();
	
	private int map_height;
	private int map_width;
	private String map;  
	
	private Bitmap wall;
	private Bitmap player;
	private Bitmap cube;
	private Bitmap completeCube;
	private Bitmap placeHere;

	private static final int FIELD_DIM = 62;
	
	public MapView(Context context) {
		super(context);
		init(null, 0);
	}

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public MapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		
		wall = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.brick), FIELD_DIM, FIELD_DIM, true);
		player = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.player), FIELD_DIM, FIELD_DIM, true); 
		cube = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.crate), FIELD_DIM, FIELD_DIM, true);
		completeCube = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.crate_com), FIELD_DIM, FIELD_DIM, true);
		placeHere = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placehere), FIELD_DIM, FIELD_DIM, true);
		
		if(attrs != null)
		{
			final TypedArray a = getContext().obtainStyledAttributes(attrs,	R.styleable.MapView, defStyle, 0);
			
			map_height = a.getInt(R.styleable.MapView_map_height, 0);
			map_width = a.getInt(R.styleable.MapView_map_width, 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		DrawMap(canvas);

	}
	
	private void DrawMap(Canvas canvas)
	{	
		canvas.drawColor(0);
		for(int i=0; i< map_height; ++i)
			for(int j=0; j < map_width; ++j )
			{
				switch(map.charAt(i * map_width + j))
				{
				case '#': canvas.drawBitmap(wall, j * FIELD_DIM, i * FIELD_DIM, paint); break;
				case 'o': canvas.drawBitmap(cube, j * FIELD_DIM, i * FIELD_DIM, paint); break;
				case 'O': canvas.drawBitmap(completeCube, j * FIELD_DIM, i * FIELD_DIM, paint); break;
				case 's': canvas.drawBitmap(player, j * FIELD_DIM, i * FIELD_DIM, paint); break;
				case '*': canvas.drawBitmap(placeHere, j * FIELD_DIM, i * FIELD_DIM, paint); break;
				}			
		}
	}
	
	public void setMapData(int height, int width, String data)
	{
		map_height = height;
		map_width = width;
		
		map = data;
		invalidate();
		requestLayout();
	}
	
	public void updateMapData(String data)
	{
		map = data;
		invalidate();
		requestLayout();
	}
}
