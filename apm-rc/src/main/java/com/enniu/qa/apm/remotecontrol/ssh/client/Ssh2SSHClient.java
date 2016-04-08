package com.enniu.qa.apm.remotecontrol.ssh.client;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ServerHostKeyVerifier;
import ch.ethz.ssh2.Session;
import com.enniu.qa.apm.remotecontrol.ssh.client.ssh2.DefaultServerHostKeyVerifier;
import net.schmizz.sshj.common.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author 一少
 */
public class Ssh2SSHClient extends AbstractSSHClient {

    private final static Logger logger   = LoggerFactory.getLogger(Ssh2SSHClient.class);

    private Connection                         conn;
    private final static ServerHostKeyVerifier verifier = new DefaultServerHostKeyVerifier();

    public Ssh2SSHClient(String host, String username, String password){
        super(host, username, password);
    }

    /*
     * (non-Javadoc)
     * @see com.alipay.one.core.service.ssh.SSHClient#connect()
     */
    @Override
    public boolean connect() throws Exception {
        conn = new Connection(host);
        try {
            conn.connect(verifier);
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                connected = false;
                return connected;
            }
            connected = true;
        } catch (IOException e) {
            logger.error("connect error", e);
            if (connected) {
                try {
                    conn.close();
                } catch (Exception e1) {
                }
            }
            connected = false;
            conn = null;
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
                conn.close();
            } catch (Exception e) {
                logger.warn("disconnect error", e);
            }
            conn = null;
        }
        executorService.shutdownNow();
    }

    public String executeCommand(String command) {

        String result = null;
        Session session = null;
        logger.info("command[" + command + "] ");
        try {
            session = conn.openSession();
            session.execCommand(command);
            result = IOUtils.readFully(session.getStdout()).toString("gbk");
            logger.info("command[" + command + "] exit status: " + session.getExitStatus());
        } catch (IOException e) {
            logger.error("command[" + command + "] error", e);
        } finally {
            logger.info("closeSession");
            closeSession(session);
        }
        return result;

    }

    /*
     * (non-Javadoc)
     * @see com.alipay.one.core.service.ssh.SSHClient#runCommand(java.lang.String)
     */
    @Override
    public boolean executeCommandNoResponse(String command) {
        Session session = null;
        logger.info("command[" + command + "] ");
        try {
            session = conn.openSession();
            session.execCommand(command);
            session.waitForCondition(ChannelCondition.EXIT_STATUS, 20 * 60 * 1000);
            logger.info("command[" + command + "] exit status: " + session.getExitStatus());
            return session.getExitStatus() == 0;
        } catch (IOException e) {
            logger.error("command[" + command + "] error", e);
        } finally {
            logger.info("closeSession");
            closeSession(session);
        }
        return false;
    }

    private void closeSession(Session session) {
        if (session != null) try {
            session.close();
        } catch (Exception e) {
            logger.warn("session close error", e);
        }
    }
}
