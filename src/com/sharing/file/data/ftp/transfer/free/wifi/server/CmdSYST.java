package com.sharing.file.data.ftp.transfer.free.wifi.server;

import android.util.Log;

public class CmdSYST extends FtpCmd implements Runnable {
    private static final String TAG = CmdSYST.class.getSimpleName();

    // This is considered a safe response to the SYST command, see
    // http://cr.yp.to/ftp/syst.html
    public static final String response = "215 UNIX Type: L8\r\n";

    public CmdSYST(SessionThread sessionThread, String input) {
        super(sessionThread);
    }

    @Override
    public void run() {
        Log.d(TAG, "SYST executing");
        sessionThread.writeString(response);
        Log.d(TAG, "SYST finished");
    }
}
