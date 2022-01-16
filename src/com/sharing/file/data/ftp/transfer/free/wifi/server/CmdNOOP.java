package com.sharing.file.data.ftp.transfer.free.wifi.server;

public class CmdNOOP extends FtpCmd implements Runnable {
    public static final String message = "TEMPLATE!!";

    public CmdNOOP(SessionThread sessionThread, String input) {
        super(sessionThread);
    }

    @Override
    public void run() {
        sessionThread.writeString("200 NOOP ok\r\n");
        // myLog.l(Log.INFO, "Executing NOOP, done");
    }

}
