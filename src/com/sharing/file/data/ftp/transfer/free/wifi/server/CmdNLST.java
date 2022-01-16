package com.sharing.file.data.ftp.transfer.free.wifi.server;

import java.io.File;

import android.util.Log;

public class CmdNLST extends CmdAbstractListing implements Runnable {
    private static final String TAG = CmdNLST.class.getSimpleName();

    // The approximate number of milliseconds in 6 months
    public final static long MS_IN_SIX_MONTHS = 6 * 30 * 24 * 60 * 60 * 1000;
    private final String input;

    public CmdNLST(SessionThread sessionThread, String input) {
        super(sessionThread, input);
        this.input = input;
    }

    @Override
    public void run() {
        String errString = null;

        mainblock: {
            String param = getParameter(input);
            if (param.startsWith("-")) {
                // Ignore options to list, which start with a dash
                param = "";
            }
            File fileToList = null;
            if (param.equals("")) {
                fileToList = sessionThread.getWorkingDir();
            } else {
                if (param.contains("*")) {
                    errString = "550 NLST does not support wildcards\r\n";
                    break mainblock;
                }
                fileToList = new File(sessionThread.getWorkingDir(), param);
                if (violatesChroot(fileToList)) {
                    errString = "450 Listing target violates chroot\r\n";
                    break mainblock;
                } else if (fileToList.isFile()) {
                    // Bernstein suggests that NLST should fail when a
                    // parameter is given and the parameter names a regular
                    // file (not a directory).
                    errString = "550 NLST for regular files is unsupported\r\n";
                    break mainblock;
                }
            }
            String listing;
            if (fileToList.isDirectory()) {
                StringBuilder response = new StringBuilder();
                errString = listDirectory(response, fileToList);
                if (errString != null) {
                    break mainblock;
                }
                listing = response.toString();
            } else {
                listing = makeLsString(fileToList);
                if (listing == null) {
                    errString = "450 Couldn't list that file\r\n";
                    break mainblock;
                }
            }
            errString = sendListing(listing);
            if (errString != null) {
                break mainblock;
            }
        }

        if (errString != null) {
            sessionThread.writeString(errString);
            Log.d(TAG, "NLST failed with: " + errString);
        } else {
            Log.d(TAG, "NLST completed OK");
        }
        // The success or error response over the control connection will
        // have already been handled by sendListing, so we can just quit now.
    }

    @Override
    protected String makeLsString(File file) {
        if (!file.exists()) {
            Log.i(TAG, "makeLsString had nonexistent file");
            return null;
        }

        // See Daniel Bernstein's explanation of NLST format at:
        // http://cr.yp.to/ftp/list/binls.html
        // This stuff is almost entirely based on his recommendations.

        String lastNamePart = file.getName();
        // Many clients can't handle files containing these symbols
        if (lastNamePart.contains("*") || lastNamePart.contains("/")) {
            Log.i(TAG, "Filename omitted due to disallowed character");
            return null;
        } else {
            Log.d(TAG, "Filename: " + lastNamePart);
            return lastNamePart + "\r\n";
        }
    }
}
