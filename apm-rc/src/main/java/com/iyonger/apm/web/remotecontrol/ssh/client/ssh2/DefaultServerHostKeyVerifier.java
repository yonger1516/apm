package com.iyonger.apm.web.remotecontrol.ssh.client.ssh2;

import ch.ethz.ssh2.ServerHostKeyVerifier;

public class DefaultServerHostKeyVerifier implements ServerHostKeyVerifier {

    @Override
    public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm, byte[] serverHostKey)
                                                                                                                      throws Exception {
        return true;
    }

}
