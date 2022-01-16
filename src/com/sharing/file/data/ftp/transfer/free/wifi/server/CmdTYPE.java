
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import android.util.Log;

public class CmdTYPE extends FtpCmd implements Runnable {
    private static final String TAG = CmdTYPE.class.getSimpleName();

    String input;

    public CmdTYPE(SessionThread sessionThread, String input) {
        super(sessionThread);
        this.input = input;
    }

    @Override
    public void run() {
        String output;
        Log.d(TAG, "TYPE executing");
        String param = getParameter(input);
        if (param.equals("I") || param.equals("L 8")) {
            output = "200 Binary type set\r\n";
            sessionThread.setBinaryMode(true);
        } else if (param.equals("A") || param.equals("A N")) {
            output = "200 ASCII type set\r\n";
            sessionThread.setBinaryMode(false);
        } else {
            output = "503 Malformed TYPE command\r\n";
        }
        sessionThread.writeString(output);
        Log.d(TAG, "TYPE complete");
    }

}
