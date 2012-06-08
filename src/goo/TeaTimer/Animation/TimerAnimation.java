package goo.TeaTimer.Animation;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

public class TimerAnimation extends View implements OnSharedPreferenceChangeListener
{		
	Vector<TimerDrawing> mDrawings = null;
	int mIndex = 0;
	int mLastTime =0,mLastMax=0;
	
	Bitmap mBitmap = null;
	
	Context mContext;
	
	public interface TimerDrawing{
		
		/**
		 * Updates the image to be in sync with the current time
		 * @param time in milliseconds
		 * @param max the original time set in milliseconds
		 */
		public void updateImage( Canvas canvas, int time,int max);
		
		public void configure();
	}
	
	public TimerAnimation(Context context, AttributeSet attrs){
		
		super(context, attrs);
		mContext = context;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		prefs.registerOnSharedPreferenceChangeListener(this);
				
		mDrawings = new Vector<TimerDrawing>();
		mDrawings.add(new CircleAnimation(context));
		//mDrawings.add(new TrashCupAnimation(context));
		mDrawings.add(new Teapot(context));
	}
	
	public void updateImage(int time,int max){
		mLastTime = time;
		mLastMax = max;
		
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas){
		mDrawings.get(mIndex).updateImage(canvas, mLastTime, mLastMax);
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		if(key.equals("Theme")){	
			for(TimerDrawing drawing : mDrawings)
			{
				drawing.configure();
			}
		} else if(key.equals("Animation")) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
			mIndex = Integer.parseInt(prefs.getString("Animation", "0"));
		}
		invalidate();
	}

	
}