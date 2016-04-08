package com.enniu.qa.apm.remotecontrol;

import com.enniu.qa.apm.remotecontrol.ssh.client.SSHClient;
import com.enniu.qa.apm.remotecontrol.ssh.factory.SSHClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;
import java.util.concurrent.Future;

/**
 * Created by fuyong on 2/18/16.
 */
public class SshServer {
	private static final Logger logger= LoggerFactory.getLogger(SshServer.class.getSimpleName());

	private SSHClient client       = null;

	private final static String LOG_FILENAME = "/var/log/apm-deploy.log";
	private SSHClientFactory    sshClientFactory;
	private String ip;
	private String username;
	private String password;

	public SshServer(String ip,String username,String password,SSHClientFactory factory){
		this.ip=ip;
		this.username=username;
		this.password=password;
		this.sshClientFactory=factory;

	}

	public void connect() throws Exception {
		client = sshClientFactory.getNewSSHClient(ip,username,password);
		client.connect();
	}

	private String getAntxKey(String all) {
		int indexOfEq = all.indexOf("=");
		return all.substring(0, indexOfEq);
	}


	private void checkAndClearLog() {
		//client.executeCommand("mkdir -p /home/admin/logs/one");
		client.executeCommand("echo '======task start======\r\n' > " + LOG_FILENAME);
	}

	public Future<Boolean> execCommand(Vector<String> options) {
		checkAndClearLog();

		StringBuilder sb = new StringBuilder();

		for(String option:options){
			sb.append(option);
			sb.append(";");
		}

		String command=sb.substring(0, sb.length() - 1);
		//sb.append("echo '======task done======' >> " + LOG_FILENAME);

		logger.info("Exec command:"+command);
		return client.executeCommandNoResponseAsync(command);
	}

	public void disconnect() {
		client.disconnect();
	}
}
