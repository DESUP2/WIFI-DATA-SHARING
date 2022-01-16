
package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.File;
import java.io.IOException;

import android.util.Log;

public class CmdRNTO extends FtpCmd implements Runnable {
    private static final String TAG = CmdRNTO.class.getSimpleName();

    protected String input;

    public CmdRNTO(SessionThread sessionThread, String input) {
        super(sessionThread);
        this.input = input;
    }

    @Override
    public void run() {
        Log.d(TAG, "RNTO executing");
        String param = getParameter(input);
        String errString = null;
        File toFile = null;
        mainblock: {
            Log.i(TAG, "param: " + param);
            toFile = inputPathToChrootedFile(sessionThread.getWorkingDir(), param);
            Log.i(TAG, "RNTO to file: " + toFile.getPath());
            if (violatesChroot(toFile)) {
                errString = "550 Invalid name or chroot violation\r\n";
                break mainblock;
            }
            File fromFile = sessionThread.getRenameFrom();
            if (fromFile == null) {
                errString = "550 Rename error, maybe RNFR not sent\r\n";
                break mainblock;
            }
            Log.i(TAG, "RNTO from file: " + fromFile.getPath());
            // TODO: this code is working around a bug that java6 and before cannot
            // reliable move a file, once java7 is supported by Dalvik, this code can
            // be replaced with Files.move()
            File tmpFile = null;
            try {
                tmpFile = File.createTempFile("temp_" + fromFile.getName(), null,
                        sessionThread.getWorkingDir());
                if (fromFile.isDirectory()) {
                    String tmpFilePath = tmpFile.getPath();
                    tmpFile.delete();
                    tmpFile = new File(tmpFilePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                errString = "550 Error during rename operation\r\n";
                break mainblock;
            }
            if (!fromFile.renameTo(tmpFile)) {
                errString = "550 Error during rename operation\r\n";
                break mainblock;
            }
            fromFile.delete();
            if (!tmpFile.renameTo(toFile)) {
                errString = "550 Error during rename operation\r\n";
                break mainblock;
            }
        }
        if (errString != null) {
            sessionThread.writeString(errString);
            Log.i(TAG, "RNFR failed: " + errString.trim());
        } else {
            sessionThread.writeString("250 rename successful\r\n");
        }
        sessionThread.setRenameFrom(null);
        Log.d(TAG, "RNTO finished");
    }
}
