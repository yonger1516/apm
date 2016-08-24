package com.iyonger.apm.web.remotecontrol.ssh.factory;

import com.iyonger.apm.web.remotecontrol.ssh.client.SSHClient;
import com.iyonger.apm.web.remotecontrol.ssh.client.Ssh2SSHClient;
import org.springframework.stereotype.Service;

@Service
public class SSHClientFactory {

    public SSHClient getNewSSHClient(String host, String username, String password) {
        return new Ssh2SSHClient(host, username, password);
    }
}
