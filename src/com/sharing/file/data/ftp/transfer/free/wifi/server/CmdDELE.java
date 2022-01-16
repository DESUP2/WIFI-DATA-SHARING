package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.File;

import android.util.Log;

import com.sharing.file.data.ftp.transfer.free.wifi.MediaUpdater;

public class CmdDELE extends FtpCmd implements Runnable {
    private static final String TAG = CmdDELE.class.getSimpleName();

    protected String input;

    public CmdDELE(SessionThread sessionThread, String input) {
        super(sessionThread);
        this.input = input;
    }

    @Override
    public void run() {
        Log.d(TAG, "DELE executing");
        String param = getParameter(input);
        File storeFile = inputPathToChrootedFile(sessionThread.getWorkingDir(), param);
        String errString = null;
        if (violatesChroot(storeFile)) {
            errString = "550 Invalid name or chroot violation\r\n";
        } else if (storeFile.isDirectory()) {
            errString = "550 Can't DELE a directory\r\n";
        } else if (!storeFile.delete()) {
            errString = "450 Error deleting file\r\n";
        }

        if (errString != null) {
            sessionThread.writeString(errString);
            Log.i(TAG, "DELE failed: " + errString.trim());
        } else {
            sessionThread.writeString("250 File successfully deleted\r\n");
            MediaUpdater.notifyFileDeleted(storeFile.getPath());
        }
        Log.d(TAG, "DELE finished");
    }

}
