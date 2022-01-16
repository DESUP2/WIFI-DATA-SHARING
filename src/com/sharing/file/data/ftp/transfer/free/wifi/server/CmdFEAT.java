package com.sharing.file.data.ftp.transfer.free.wifi.server;

import android.util.Log;

public class CmdFEAT extends FtpCmd implements Runnable {
    private static final String TAG = CmdFEAT.class.getSimpleName();

    public static final String message = "TEMPLATE!!";

    public CmdFEAT(SessionThread sessionThread, String input) {
        super(sessionThread);
    }

    @Override
    public void run() {
        // sessionThread.writeString("211 No extended features\r\n");
        sessionThread.writeString("211-Features supported\r\n");
        sessionThread.writeString(" UTF8\r\n"); // advertise UTF8 support (fixes bug 14)
        sessionThread.writeString("211 End\r\n");
        Log.d(TAG, "Gave FEAT response");
    }

}
