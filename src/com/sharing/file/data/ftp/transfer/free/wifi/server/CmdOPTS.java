
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import android.util.Log;

public class CmdOPTS extends FtpCmd implements Runnable {
    private static final String TAG = CmdOPTS.class.getSimpleName();

    public static final String message = "TEMPLATE!!";
    private final String input;

    public CmdOPTS(SessionThread sessionThread, String input) {
        super(sessionThread);
        this.input = input;
    }

    @Override
    public void run() {
        String param = getParameter(input);
        String errString = null;

        mainBlock: {
            if (param == null) {
                errString = "550 Need argument to OPTS\r\n";
                Log.w(TAG, "Couldn't understand empty OPTS command");
                break mainBlock;
            }
            String[] splits = param.split(" ");
            if (splits.length != 2) {
                errString = "550 Malformed OPTS command\r\n";
                Log.w(TAG, "Couldn't parse OPTS command");
                break mainBlock;
            }
            String optName = splits[0].toUpperCase();
            String optVal = splits[1].toUpperCase();
            if (optName.equals("UTF8")) {
                // OK, whatever. Don't really know what to do here. We
                // always operate in UTF8 mode.
                if (optVal.equals("ON")) {
                    Log.d(TAG, "Got OPTS UTF8 ON");
                    sessionThread.setEncoding("UTF-8");
                } else {
                    Log.i(TAG, "Ignoring OPTS UTF8 for something besides ON");
                }
                break mainBlock;
            } else {
                Log.d(TAG, "Unrecognized OPTS option: " + optName);
                errString = "502 Unrecognized option\r\n";
                break mainBlock;
            }
        }
        if (errString != null) {
            sessionThread.writeString(errString);
        } else {
            sessionThread.writeString("200 OPTS accepted\r\n");
            Log.d(TAG, "Handled OPTS ok");
        }
    }

}
