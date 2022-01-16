
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import android.util.Log;

public class CmdTemplate extends FtpCmd implements Runnable {
    private static final String TAG = CmdTemplate.class.getSimpleName();

    public static final String message = "TEMPLATE!!";

    public CmdTemplate(SessionThread sessionThread, String input) {
        super(sessionThread);
    }

    @Override
    public void run() {
        sessionThread.writeString(message);
        Log.i(TAG, "Template log message");
    }

}
