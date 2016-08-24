package com.iyonger.apm.web.remotecontrol.ssh.client;

import java.util.concurrent.Future;

public interface SSHClient {

    public boolean connect() throws Exception;

    public void disconnect();

    public String executeCommand(String command);

    public boolean executeCommandNoResponse(String command);

    public Future<String> runCommandAsync(final String command);

    public Future<Boolean> executeCommandNoResponseAsync(final String command);

    public void setHost(String host);

    public void setPassword(String password);

    public void setUsername(String username);

    public boolean isConnected();

    /**
     * 超时时间，单位：秒
     * 
     * @param timeout
     */
    public void setTimeout(int timeout);

}
