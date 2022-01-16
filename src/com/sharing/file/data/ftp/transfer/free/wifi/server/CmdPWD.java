
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.IOException;

import com.sharing.file.data.ftp.transfer.free.wifi.Settings;

import android.util.Log;

public class CmdPWD extends FtpCmd implements Runnable {
    private static final String TAG = CmdPWD.class.getSimpleName();

    public CmdPWD(SessionThread sessionThread, String input) {
        super(sessionThread);
    }

    @Override
    public void run() {
        Log.d(TAG, "PWD executing");
        // We assume that the chroot restriction has been applied, and that
        // therefore the current directory is located somewhere within the
        // chroot directory. Therefore, we can just slice of the chroot
        // part of the current directory path in order to get the
        // user-visible path (inside the chroot directory).
        try {
            String currentDir = sessionThread.getWorkingDir().getCanonicalPath();
            currentDir = currentDir.substring(Settings.getChrootDir().getCanonicalPath()
                    .length());
            // The root directory requires special handling to restore its
            // leading slash
            if (currentDir.length() == 0) {
                currentDir = "/";
            }
            sessionThread.writeString("257 \"" + currentDir + "\"\r\n");
        } catch (IOException e) {
            // This shouldn't happen unless our input validation has failed
            Log.e(TAG, "PWD canonicalize");
            sessionThread.closeSocket(); // should cause thread termination
        }
        Log.d(TAG, "PWD complete");
    }

}
