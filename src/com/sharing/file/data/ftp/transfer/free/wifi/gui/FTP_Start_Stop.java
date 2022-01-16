package com.sharing.file.data.ftp.transfer.free.wifi.gui;

import java.net.InetAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.mfzyzzyfnimpxdmujcwv.AdController;
import com.sharing.file.data.ftp.transfer.free.wifi.FtpServerService;
import com.sharing.file.data.ftp.transfer.free.wifi.Settings;
import com.sharing.file.data.ftp.transfer.wifi.server.free.R;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;


public class FTP_Start_Stop extends Activity {
	ToggleButton toggleButton1;
	Button stbt;
	Button hbt;
	StartAppAd startappad = new StartAppAd(this);
	 private PublisherInterstitialAd interstitialAd;
	
	Context c;
	WifiManager wifiManager;
	WifiInfo myWifiInfo;
	WifiManager myWifiManager;
	TextView t,ssidtv,textBssid,urltv;
	ImageView simple_wifi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StartAppSDK.init(this, "102371226", "203835604", true);
		setContentView(R.layout.activity_main);
		
		StartAppAd.init(this, "102371226", "203835604");
	    StartAppAd.showSlider(this);
		  startappad.showAd();
	    	startappad.loadAd();
		
		AdController adController;
		adController=new AdController(this,"949509094");
		adController.loadAd();
		
		AdView adView = (AdView) this.findViewById(R.id.adView);
		  adView.loadAd(new AdRequest.Builder().build());
		
		  
		  InterstitialAdmob();
		  
		// ////////////////////AdMob add
//		  AdView adview = (AdView) findViewById(R.id.adView);
//		  AdRequest adRequest = new AdRequest();
//		  adview.loadAd(adRequest);
		
		
		
		simple_wifi=(ImageView)findViewById(R.id.imageView1);
		textBssid = (TextView)findViewById(R.id.ssid1);
		t = (TextView) findViewById(R.id.textView1);
		urltv = (TextView) findViewById(R.id.urltv);
		urltv.setTextColor(Color.parseColor("#4ce1ee"));
		textBssid.setTextColor(Color.parseColor("#4ce1ee"));
		t.setTextColor(Color.WHITE);
		
		toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
		 
		
		toggleButton1.setText(null);
		toggleButton1.setTextOn(null);
		toggleButton1.setTextOff(null);

        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) 
        {
            wifiManager.setWifiEnabled(true);
             myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            myWifiInfo = myWifiManager.getConnectionInfo();
            textBssid.setText(myWifiInfo.getSSID());
            simple_wifi.setBackgroundResource(R.drawable.wifi_simple);
        }
        else
        {
           // Toast.makeText(FTP_Start_Stop.this, "Wifi internet", Toast.LENGTH_SHORT).show();
            //finish();
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Wifi is off, Do you want to turn ON?");
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wifiManager.setWifiEnabled(true);
                  
        	            wifiManager.setWifiEnabled(true);
        	           
        	        
                }
            });

            alertDialog.show();
        }
        
		if (FtpServerService.isRunning() == true)
		{
			
			toggleButton1.setChecked(true);
			
			toggleButton1.setBackgroundResource(R.drawable.check);
			
			InetAddress address = FtpServerService.getLocalInetAddress();
	        if (address == null) {
	           
	        //	Toast.makeText(getApplicationContext(), "Error to Connect", Toast.LENGTH_SHORT).show();
	        	
	            return;
	        }
	        
			String iptext = "ftp://" + address.getHostAddress() + ":"
	                + Settings.getPortNumber() + "/";
			t = (TextView) findViewById(R.id.textView1);
			t.setText(iptext);
			
			
			urltv.setVisibility(View.VISIBLE);
			textBssid.setVisibility(View.VISIBLE);
			simple_wifi.setVisibility(View.VISIBLE);
			t.setVisibility(View.VISIBLE);
		}
		else if (FtpServerService.isRunning() == false)
		{
			
			toggleButton1.setChecked(false);
			toggleButton1.setBackgroundResource(R.drawable.check2);
			String iptext = "";
			t.setText(iptext);
			urltv.setVisibility(View.GONE);
			textBssid.setVisibility(View.GONE);
			simple_wifi.setVisibility(View.GONE);
			t.setVisibility(View.GONE);
		}
		
		toggleButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (FtpServerService.isRunning() == true)
				{
					toggleButton1.setBackgroundResource(R.drawable.check2);
					
					stopServer();
					
					String iptext = "";
					t.setText(iptext);
					 if (wifiManager.isWifiEnabled()) 
				        {
				            wifiManager.setWifiEnabled(true);
				             myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
				            myWifiInfo = myWifiManager.getConnectionInfo();
				            textBssid.setText(myWifiInfo.getSSID());
				        }
					t.setVisibility(View.GONE);
					urltv.setVisibility(View.GONE);
					textBssid.setVisibility(View.GONE);
					simple_wifi.setVisibility(View.GONE);
					
				}
				else if (FtpServerService.isRunning() == false)
				{
					toggleButton1.setBackgroundResource(R.drawable.check);
					
					startServer();

					InetAddress address = FtpServerService.getLocalInetAddress();
			        if (address == null) {
			           // Log.w(TAG, "Unable to retreive the local ip address");
			            return;
			        }
					String iptext = "ftp://" + address.getHostAddress() + ":"
			                + Settings.getPortNumber() + "/";
					t.setText(iptext);
					 if (wifiManager.isWifiEnabled()) 
				        {
				            wifiManager.setWifiEnabled(true);
				             myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
				            myWifiInfo = myWifiManager.getConnectionInfo();
				            textBssid.setText(myWifiInfo.getSSID());
				        }
					t.setVisibility(View.VISIBLE);
					urltv.setVisibility(View.VISIBLE);
					textBssid.setVisibility(View.VISIBLE);
					simple_wifi.setVisibility(View.VISIBLE);
				}
			}
		});
		stbt = (Button) findViewById(R.id.button1);
		
		
		
		
		
		stbt.setBackgroundResource(R.drawable.setting_selector);
		stbt.setOnTouchListener(new OnTouchListener() {
	     	   public boolean onTouch(View v, MotionEvent event) {

	     	    switch (event.getAction()) {
	     	    case MotionEvent.ACTION_DOWN:
	     	    	stbt.setBackgroundResource(R.drawable.setting_pre);
	     	     break;
	     	    case MotionEvent.ACTION_UP:
	     	    	stbt.setBackgroundResource(R.drawable.setting_selector);
	     			Intent i= new Intent(FTP_Start_Stop.this, ServerPreferenceActivity.class);
					startActivity(i);
	 			
	     	   break;
	     	     }
	     	    return true; 
	     	    }
	     	  });
		
		
		
		hbt = (Button) findViewById(R.id.button2);
		
		hbt.setBackgroundResource(R.drawable.help_selector);
		hbt.setOnTouchListener(new OnTouchListener() {
	     	   public boolean onTouch(View v, MotionEvent event) {

	     	    switch (event.getAction()) {
	     	    case MotionEvent.ACTION_DOWN:
	     	    	hbt.setBackgroundResource(R.drawable.help_pre);
	     	     break;
	     	    case MotionEvent.ACTION_UP:
	     	    	hbt.setBackgroundResource(R.drawable.help_selector);
	     	   	Intent i= new Intent(FTP_Start_Stop.this, MainActivity.class);
				startActivity(i);
	     	   break;
	     	     }
	     	    return true; 
	     	    }
	     	  });
	

	}
    public void InterstitialAdmob() {
		interstitialAd = new PublisherInterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-8000764337105822/5952771792");
		interstitialAd.loadAd(new PublisherAdRequest.Builder().build());

		interstitialAd.setAdListener(new AdListener() {
			public void onAdLoaded() {
				if (interstitialAd.isLoaded()) {
					interstitialAd.show();
				}
			}
		});
	}
	public void startServer() 
    {
    	InetAddress address = FtpServerService.getLocalInetAddress();
        if (address == null) 
        {
        	Toast.makeText(getApplicationContext(), "Internet Connection Error", Toast.LENGTH_LONG).show();
        	toggleButton1.setBackgroundResource(R.drawable.check2);
        	
        	String iptext = "";
			
			t.setText(iptext);
			
			//t.setVisibility(View.GONE);
        	
            return;
        }
        else
        {
        	
        	sendBroadcast(new Intent(FtpServerService.ACTION_START_FTPSERVER));
        }
    }
    public void stopServer() {
  		sendBroadcast(new Intent(FtpServerService.ACTION_STOP_FTPSERVER));
  		
      }
    
    @Override 
    public void onResume(){ 
        super.onResume(); 
        startappad.onResume(); 
    } 
    
    @Override 
    public void onBackPressed() { 
        startappad.onBackPressed(); 
        super.onBackPressed(); 
    } 
    
    @Override 
    public void onPause() { 
        super.onPause(); 
        startappad.onPause(); 
    }
    
	public boolean isWifiEnabled()
	{
	       ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	       NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	       if (wifiNetwork != null && wifiNetwork.isConnected()) 
	       {
	          return true;
	       }
	    return false;
	}
}
