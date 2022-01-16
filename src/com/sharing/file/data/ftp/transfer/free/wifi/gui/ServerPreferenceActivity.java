package com.sharing.file.data.ftp.transfer.free.wifi.gui;

import java.net.InetAddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.sharing.file.data.ftp.transfer.free.wifi.FtpServerApp;
import com.sharing.file.data.ftp.transfer.free.wifi.FtpServerService;
import com.sharing.file.data.ftp.transfer.free.wifi.Settings;
import com.sharing.file.data.ftp.transfer.wifi.server.free.R;
import com.startapp.android.publish.StartAppAd;

public class ServerPreferenceActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {
	 StartAppAd startappad = new StartAppAd(this);
	public static ServerPreferenceActivity s;
	
    private static String TAG = ServerPreferenceActivity.class.getSimpleName();

    EditTextPreference mPassWordPref;
    public CheckBoxPreference running_state;
    
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
		
        addPreferencesFromResource(R.xml.preferences);
        
//        StartAppAd startappad = new StartAppAd(this);
//		StartAppAd.init(this, "102371226", "203835604");
//		StartAppSearch.init(this, "102371226", "203835604");
//		startappad.showAd();
//		startappad.loadAd();
		
		 StartAppAd.init(this, "102371226", "203835604");
		    StartAppAd.showSlider(this);
			  startappad.showAd();
		    	startappad.loadAd();
		

        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(this);
        
        Resources resources = getResources();
        

        
        
        EditTextPreference username_pref = (EditTextPreference) findPreference("username");
        username_pref.setSummary(settings.getString("username",
                resources.getString(R.string.username_default)));
        username_pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newUsername = (String) newValue;
                if (preference.getSummary().equals(newUsername))
                    return false;
                if (!newUsername.matches("[a-zA-Z0-9]+")) {
                    Toast.makeText(ServerPreferenceActivity.this,
                            R.string.username_validation_error, Toast.LENGTH_LONG).show();
                    return false;
                }
                preference.setSummary(newUsername);
                stopServer();
                return true;
            }
        });

        
        mPassWordPref = (EditTextPreference) findPreference("password");
        String password = resources.getString(R.string.password_default);
        password = settings.getString("password", password);
        mPassWordPref.setSummary(transformPassword(password));
        mPassWordPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newPassword = (String) newValue;
                preference.setSummary(transformPassword(newPassword));
         
                return true;
            }
        });

        EditTextPreference portnum_pref = (EditTextPreference) findPreference("portNum");
        portnum_pref.setSummary(settings.getString("portNum",
                resources.getString(R.string.portnumber_default)));
        portnum_pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newPortnumString = (String) newValue;
                if (preference.getSummary().equals(newPortnumString))
                    return false;
                int portnum = 0;
                try {
                    portnum = Integer.parseInt(newPortnumString);
                } catch (Exception e) {
                }
                if (portnum <= 0 || 65535 < portnum) {
                    Toast.makeText(ServerPreferenceActivity.this,
                            R.string.port_validation_error, Toast.LENGTH_LONG).show();
                    return false;
                }
                preference.setSummary(newPortnumString);
                stopServer();
                return true;
            }
        });

        

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        if (key.equals("show_password")) {
			Context context = FtpServerApp.getAppContext();
            Resources res = context.getResources();
            String password = res.getString(R.string.password_default);
            password = sp.getString("password", password);
            mPassWordPref.setSummary(transformPassword(password));
        }
    }

    public void startServer() {
		sendBroadcast(new Intent(FtpServerService.ACTION_START_FTPSERVER));
    }

    public void stopServer() {
		sendBroadcast(new Intent(FtpServerService.ACTION_STOP_FTPSERVER));
    }
    @Override 
    public void onBackPressed() { 
        startappad.onBackPressed(); 
        super.onBackPressed(); 
    } 
    @SuppressWarnings("deprecation")
	@Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        // make this class listen for preference changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        Log.v(TAG, "Registering the FTP server actions");
        IntentFilter filter = new IntentFilter();
        filter.addAction(FtpServerService.ACTION_STARTED);
        filter.addAction(FtpServerService.ACTION_STOPPED);
        filter.addAction(FtpServerService.ACTION_FAILEDTOSTART);
        registerReceiver(ftpServerReceiver, filter);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();

        Log.v(TAG, "Unregistering the FTPServer actions");
        unregisterReceiver(ftpServerReceiver);

        // unregister the listener
        @SuppressWarnings("deprecation")
		SharedPreferences sprefs = getPreferenceScreen().getSharedPreferences();
        sprefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This receiver will check FTPServer.ACTION* messages and will update the button,
     * running_state, if the server is running and will also display at what url the
     * server is running.
     */
    
    BroadcastReceiver ftpServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "FTPServerService action received: " + intent.getAction());
            @SuppressWarnings("deprecation")
			CheckBoxPreference running_state = (CheckBoxPreference) findPreference("running_state");
            if (intent.getAction().equals(FtpServerService.ACTION_STARTED)) {
                running_state.setChecked(true);
                // Fill in the FTP server address
                InetAddress address = FtpServerService.getLocalInetAddress();
                if (address == null) {
                    Log.v(TAG, "Unable to retreive local ip address");
                    running_state.setSummary(R.string.cant_get_url);
                    return;
                }
                String iptext = "ftp://" + address.getHostAddress() + ":"
                        + Settings.getPortNumber() + "/";
                Resources resources = getResources();
                String summary = resources.getString(R.string.running_summary_started,
                        iptext);
                running_state.setSummary(summary);
            } else if (intent.getAction().equals(FtpServerService.ACTION_STOPPED)) {
                running_state.setChecked(false);
                running_state.setSummary(R.string.running_summary_stopped);
            } else if (intent.getAction().equals(FtpServerService.ACTION_FAILEDTOSTART)) {
                running_state.setChecked(false);
                running_state.setSummary(R.string.running_summary_failed);
            }
        }
    };

    static private String transformPassword(String password) {
		Context context = FtpServerApp.getAppContext();
        SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        boolean showPassword = res.getString(R.string.show_password_default)
				.equals("true") ? true : false;
        showPassword = sp.getBoolean("show_password", showPassword);
        if (showPassword == true)
            return password;
        else {
            StringBuilder sb = new StringBuilder(password.length());
            for (int i = 0; i < password.length(); ++i)
                sb.append('*');
            return sb.toString();
        }
    }

}
