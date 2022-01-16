package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.File;
import java.io.IOException;

import android.util.Log;

public class CmdCWD extends FtpCmd implements Runnable {
    private static final String TAG = CmdCWD.class.getSimpleName();

    protected String input;

    public CmdCWD(SessionThread sessionThread, String input) {
        super(sessionThread);
        this.input = input;
    }

    @Override
    public void run() {
        Log.d(TAG, "CWD executing");
        String param = getParameter(input);
        File newDir;
        String errString = null;
        mainblock: {
            newDir = inputPathToChrootedFile(sessionThread.getWorkingDir(), param);

            // Ensure the new path does not violate the chroot restriction
            if (violatesChroot(newDir)) {
                errString = "550 Invalid name or chroot violation\r\n";
                sessionThread.writeString(errString);
                Log.i(TAG, errString);
                break mainblock;
            }

            try {
                newDir = newDir.getCanonicalFile();
                Log.i(TAG, "New directory: " + newDir);
                if (!newDir.isDirectory()) {
                    sessionThread.writeString("550 Can't CWD to invalid directory\r\n");
                } else if (newDir.canRead()) {
                    sessionThread.setWorkingDir(newDir);
                    sessionThread.writeString("250 CWD successful\r\n");
                } else {
                    sessionThread.writeString("550 That path is inaccessible\r\n");
                }
            } catch (IOException e) {
                sessionThread.writeString("550 Invalid path\r\n");
                break mainblock;
            }
        }
        Log.d(TAG, "CWD complete");
    }
}
