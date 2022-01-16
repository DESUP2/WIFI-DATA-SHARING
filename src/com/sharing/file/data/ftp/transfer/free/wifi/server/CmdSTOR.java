
package com.sharing.file.data.ftp.transfer.free.wifi.server;

public class CmdSTOR extends CmdAbstractStore implements Runnable {
    protected String input;

    public CmdSTOR(SessionThread sessionThread, String input) {
        super(sessionThread, CmdSTOR.class.toString());
        this.input = input;
    }

    @Override
    public void run() {
        doStorOrAppe(getParameter(input), false);
    }
}
