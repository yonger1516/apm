package com.enniu.qa.apm.remotecontrol.ssh.client;

import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.schmizz.sshj.SSHClient;

/**
 * @author 一少
 */
public class SshjSSHClient extends AbstractSSHClient {

    private final static Logger logger = LoggerFactory.getLogger(SshjSSHClient.class);

    private SSHClient           ssh;

    public SshjSSHClient(String host, String username, String password){
        super(host, username, password);
    }

    /*
     * (non-Javadoc)
     * @see com.alipay.one.core.service.ssh.SSHClient#connect()
     */
    @Override
    public boolean connect() throws Exception {
        ssh = new SSHClient();
        try {
            ssh.useCompression();
            ssh.loadKnownHosts();
            ssh.connect(host);
            connected = true;
            ssh.authPassword(username, password);
        } catch (IOException e) {
            logger.error("connect error", e);
            if (connected) {
                try {
                    ssh.disconnect();
                } catch (IOException e1) {
                }
            }
            connected = false;
            ssh = null;
            throw e;
        }
        return connected;
    }

    /*
     * (non-Javadoc)
     * @see com.alipay.one.core.service.ssh.SSHClient#disconnect()
     */
    @Override
    public void disconnect() {
        if (connected) {
            try {
                ssh.disconnect();
            } catch (IOException e) {
                logger.warn("disconnect error", e);
            }
            ssh = null;
        }
        executorService.shutdownNow();
    }

    /*
     * (non-Javadoc)
     * @see com.alipay.one.core.service.ssh.SSHClient#runCommand(java.lang.String)
     */
    @Override
    public String executeCommand(String command) {
        String result = null;
        Session session = null;
        logger.info("command[" + command + "] ");
        try {
            session = ssh.startSession();
            // session.allocatePTY("dumb", 0, 0, 0, 0, Collections.<PTYMode, Integer> emptyMap());
            Command cmd = session.exec(command);
            result = IOUtils.readFully(cmd.getInputStream()).toString("gbk");
            cmd.join(timeout, TimeUnit.SECONDS);
            logger.info("command[" + command + "] exit status: " + cmd.getExitStatus());
        } catch (IOException e) {
            logger.error("command[" + command + "] error", e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    private void closeSession(Session session) {
        if (session != null) try {
            session.close();
        } catch (IOException e) {
            logger.warn("session close error", e);
        }
    }

    @Override
    public boolean executeCommandNoResponse(String command) {
        Session session = null;
        logger.info("command[" + command + "] ");
        try {
            session = ssh.startSession();
            // session.allocatePTY("dumb", 0, 0, 0, 0, Collections.<PTYMode, Integer> emptyMap());
            Command cmd = session.exec(command);
            while (true) {
                if (cmd.getExitStatus() != null) {
                    logger.info("command[" + command + "] exit status: " + cmd.getExitStatus());
                    return cmd.getExitStatus() == 0;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
            logger.error("command[" + command + "] error", e);
        } finally {
            closeSession(session);
        }
        return false;
    }
}
