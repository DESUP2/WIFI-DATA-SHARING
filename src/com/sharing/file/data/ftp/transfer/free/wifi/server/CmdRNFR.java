
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.File;

import android.util.Log;

public class CmdRNFR extends FtpCmd implements Runnable {
    private static final String TAG = CmdRNFR.class.getSimpleName();

    protected String input;

    public CmdRNFR(SessionThread sessionThread, String input) {
        super(sessionThread);
        this.input = input;
    }

    @Override
    public void run() {
        Log.d(TAG, "Executing RNFR");
        String param = getParameter(input);
        String errString = null;
        File file = null;
        mainblock: {
            file = inputPathToChrootedFile(sessionThread.getWorkingDir(), param);
            if (violatesChroot(file)) {
                errString = "550 Invalid name or chroot violation\r\n";
                break mainblock;
            }
            if (!file.exists()) {
                errString = "450 Cannot rename nonexistent file\r\n";
            }
        }
        if (errString != null) {
            sessionThread.writeString(errString);
            Log.d(TAG, "RNFR failed: " + errString.trim());
            sessionThread.setRenameFrom(null);
        } else {
            sessionThread.writeString("350 Filename noted, now send RNTO\r\n");
            sessionThread.setRenameFrom(file);
        }
    }
}
