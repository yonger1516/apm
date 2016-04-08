package com.enniu.qa.apm.remotecontrol.ssh.client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractSSHClient implements SSHClient {

    protected String          host;
    protected String          username;
    protected String          password;
    protected int             timeout                        = DEFAULT_COMMAND_TIMEOUT_SECOND;
    /**
     * 默认超时时间
     */
    private static final int  DEFAULT_COMMAND_TIMEOUT_SECOND = 10 * 60;

    protected boolean         connected;

    protected ExecutorService executorService                = Executors.newCachedThreadPool();

    public AbstractSSHClient(String host, String username, String password){
        super();
        this.host = host;
        this.username = username;
        this.password = password;
    }

    @Override
    public Future<String> runCommandAsync(final String command) {
        return executorService.submit(new Callable<String>() {

            @Override
            public String call() {
                return executeCommand(command);
            }
        });
    }

    @Override
    public Future<Boolean> executeCommandNoResponseAsync(final String command) {
        return executorService.submit(new Callable<Boolean>() {

            @Override
            public Boolean call() {
                return executeCommandNoResponse(command);
            }
        });
    }

    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String toString() {
        return username + "(" + password + ")" + "@" + host;
    }

}
