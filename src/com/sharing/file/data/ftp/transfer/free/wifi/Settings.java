
package com.sharing.file.data.ftp.transfer.free.wifi;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings {

    private final static String TAG = Settings.class.getSimpleName();

    public static String getUserName() {
        final SharedPreferences sp = getSharedPreferences();
        return sp.getString("username", "ftp");
    }

    public static String getPassWord() {
        final SharedPreferences sp = getSharedPreferences();
        return sp.getString("password", "ftp");
    }

    public static File getChrootDir() {
        final SharedPreferences sp = getSharedPreferences();
        String dirName = sp.getString("chrootDir", "/");
        File chrootDir = new File(dirName);
        if (!chrootDir.isDirectory()) {
            Log.e(TAG, "Chroot dir is invalid");
            return null;
        }
        return chrootDir;
    }

    public static int getPortNumber() {
        final SharedPreferences sp = getSharedPreferences();
		// TODO: port is always an number, so store this accordenly
		String portString = sp.getString("portNum", "2121");
        int port = Integer.valueOf(portString);
        Log.v(TAG, "Using port: " + port);
        return port;
    }

    public static boolean shouldTakeFullWakeLock() {
        final SharedPreferences sp = getSharedPreferences();
        return sp.getBoolean("stayAwake", false);
    }

    /**
     * @return the SharedPreferences for this application
     */
    private static SharedPreferences getSharedPreferences() {
        final Context context = FtpServerApp.getAppContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // cleaning up after his
    protected static int inputBufferSize = 256;
    protected static boolean allowOverwrite = false;
    protected static int dataChunkSize = 8192; // do file I/O in 8k chunks
    protected static int sessionMonitorScrollBack = 10;
    protected static int serverLogScrollBack = 10;

    public static int getInputBufferSize() {
        return inputBufferSize;
    }

    public static void setInputBufferSize(int inputBufferSize) {
        Settings.inputBufferSize = inputBufferSize;
    }

    public static boolean isAllowOverwrite() {
        return allowOverwrite;
    }

    public static void setAllowOverwrite(boolean allowOverwrite) {
        Settings.allowOverwrite = allowOverwrite;
    }

    public static int getDataChunkSize() {
        return dataChunkSize;
    }

    public static void setDataChunkSize(int dataChunkSize) {
        Settings.dataChunkSize = dataChunkSize;
    }

    public static int getSessionMonitorScrollBack() {
        return sessionMonitorScrollBack;
    }

    public static void setSessionMonitorScrollBack(int sessionMonitorScrollBack) {
        Settings.sessionMonitorScrollBack = sessionMonitorScrollBack;
    }

    public static int getServerLogScrollBack() {
        return serverLogScrollBack;
    }

    public static void setLogScrollBack(int serverLogScrollBack) {
        Settings.serverLogScrollBack = serverLogScrollBack;
    }

}
