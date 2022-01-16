package com.sharing.file.data.ftp.transfer.free.wifi.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sharing.file.data.ftp.transfer.wifi.server.free.R;
import com.startapp.android.publish.StartAppAd;

public class MainActivity extends Activity {
	private StartAppAd startAppAd = new StartAppAd(this); 
public ViewPager viewPager;
StartAppAd startappad = new StartAppAd(this);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			
		setContentView(R.layout.activity_ftp__start__stop);
		
		
		StartAppAd.init(this, "102371226", "203835604");
	    StartAppAd.showSlider(this);
		  startappad.showAd();
	    	startappad.loadAd();
		
	    viewPager = (ViewPager) findViewById(R.id.view_pager);
	    ImagePagerAdapter adapter = new ImagePagerAdapter();
	    viewPager.setAdapter(adapter);
	}
	  @Override 
	    public void onBackPressed() { 
	        startAppAd.onBackPressed(); 
	        super.onBackPressed(); 
	    } 
	    
	private class ImagePagerAdapter extends PagerAdapter {

		private int[] mImages = new int[] 
		{
	        R.drawable.h1,
	        R.drawable.h2,
	        R.drawable.h3,
	        R.drawable.h4,
	        
	    };

	    @Override
	    public int getCount() {
	      return mImages.length;
	    }

	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	      return view == ((ImageView) object);
	    }
	    
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	      Context context = MainActivity.this;
	      ImageView imageView = new ImageView(context);
	      int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
	      imageView.setPadding(padding, padding, padding, padding);
	      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	      imageView.setImageResource(mImages[position]);
	      ((ViewPager) container).addView(imageView, 0);
	      
	      	return imageView;
	    }
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	      ((ViewPager) container).removeView((ImageView) object);
	    }
	}
	}

