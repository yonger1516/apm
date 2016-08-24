package com.iyonger.apm.web.remotecontrol.ssh.client;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author 一少
 */
public class JSchSSHClient extends AbstractSSHClient {

    private final static Logger logger = LoggerFactory.getLogger(JSchSSHClient.class);

    private JSch                jsch;
    private Session             ssh;

    public JSchSSHClient(String host, String username, String password){
        super(host, username, password);
        jsch = new JSch();
    }

    /*
     * (non-Javadoc)
     * @see com.alipay.one.core.service.ssh.SSHClient#connect()
     */
    @Override
    public boolean connect() throws Exception {
        try {
            jsch.setKnownHosts("/Users/sand/.ssh/known_hosts");
            ssh = jsch.getSession(username, host, 22);
            ssh.setPassword(password);
            ssh.connect();
            connected = true;
        } catch (Exception e) {
            logger.error("connect error", e);
            if (connected) {
                try {
                    ssh.disconnect();
                } catch (Exception e1) {
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
            } catch (Exception e) {
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
        Channel channel = null;
        logger.info("command[" + command + "] ");
        try {
            channel = ssh.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    result = new String(tmp, 0, i);
                    System.out.println("result: " + result);
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
            }

            logger.info("command[" + command + "] exit status: " + channel.getExitStatus());
        } catch (Exception e) {
            logger.error("command[" + command + "] error", e);
        } finally {
            closeSession(channel);
        }
        return result;
    }

    private void closeSession(Channel channel) {
        if (channel != null) try {
            channel.disconnect();
        } catch (Exception e) {
            logger.warn("session close error", e);
        }
    }

    @Override
    public boolean executeCommandNoResponse(String command) {
        Channel channel = null;
        logger.info("command[" + command + "] ");
        try {
            channel = ssh.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                }
                if (channel.isClosed()) {
                    logger.info("command[" + command + "] exit status: " + channel.getExitStatus());
                    return channel.getExitStatus() == 0;
                }
            }

        } catch (Exception e) {
            logger.error("command[" + command + "] error", e);
        } finally {
            closeSession(channel);
        }
        return false;
    }

}
