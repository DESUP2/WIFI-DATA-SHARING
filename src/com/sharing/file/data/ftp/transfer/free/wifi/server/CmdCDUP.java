
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.File;
import java.io.IOException;

import android.util.Log;

public class CmdCDUP extends FtpCmd implements Runnable {
    private static final String TAG = CmdCDUP.class.getSimpleName();
    protected String input;

    public CmdCDUP(SessionThread sessionThread, String input) {
        super(sessionThread);
    }

    @Override
    public void run() {
        Log.d(TAG, "CDUP executing");
        File newDir;
        String errString = null;
        mainBlock: {
            File workingDir = sessionThread.getWorkingDir();
            newDir = workingDir.getParentFile();
            if (newDir == null) {
                errString = "550 Current dir cannot find parent\r\n";
                break mainBlock;
            }
            // Ensure the new path does not violate the chroot restriction
            if (violatesChroot(newDir)) {
                errString = "550 Invalid name or chroot violation\r\n";
                break mainBlock;
            }

            try {
                newDir = newDir.getCanonicalFile();
                if (!newDir.isDirectory()) {
                    errString = "550 Can't CWD to invalid directory\r\n";
                    break mainBlock;
                } else if (newDir.canRead()) {
                    sessionThread.setWorkingDir(newDir);
                } else {
                    errString = "550 That path is inaccessible\r\n";
                    break mainBlock;
                }
            } catch (IOException e) {
                errString = "550 Invalid path\r\n";
                break mainBlock;
            }
        }
        if (errString != null) {
            sessionThread.writeString(errString);
            Log.i(TAG, "CDUP error: " + errString);
        } else {
            sessionThread.writeString("200 CDUP successful\r\n");
            Log.d(TAG, "CDUP success");
        }
    }
}
