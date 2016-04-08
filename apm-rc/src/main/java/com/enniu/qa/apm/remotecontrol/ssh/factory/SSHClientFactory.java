package com.enniu.qa.apm.remotecontrol.ssh.factory;

import com.enniu.qa.apm.remotecontrol.ssh.client.SSHClient;
import com.enniu.qa.apm.remotecontrol.ssh.client.Ssh2SSHClient;
import org.springframework.stereotype.Service;

@Service
public class SSHClientFactory {

    public SSHClient getNewSSHClient(String host, String username, String password) {
        return new Ssh2SSHClient(host, username, password);
    }
}
