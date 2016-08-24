package com.iyonger.apm.web.remotecontrol;

import com.iyonger.apm.web.remotecontrol.ssh.client.SSHClient;
import com.iyonger.apm.web.remotecontrol.ssh.factory.SSHClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;

/**
 * Created by fuyong on 2/18/16.
 */

@Component
public class TaskEngine {

	private static final Logger logger = LoggerFactory.getLogger(TaskEngine.class.getSimpleName());

	private ExecutorService executorService = Executors.newCachedThreadPool();
	@Autowired
	SSHClientFactory sshClientFactory;

	public boolean testConnection(String host, String username, String password) {
		SshServer ssh = new SshServer(host, username, password, sshClientFactory);
		try {

			ssh.connect();
		} catch (Exception e) {
			return false;
		}finally {
			ssh.disconnect();
		}
		return true;

	}

	public RemoteTask doTask(final RemoteTask task) throws Exception {

		SshServer ssh = new SshServer(task.getTargetId(), task.getUserName(), task.getPassword(), sshClientFactory);
		task.setSshServer(ssh);
		try {
			ssh.connect();
			Future<?> future = ssh.execCommand(task.getOptions());
			task.setFuture(future);
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					waitTaskDone(task);
				}
			});
		} catch (Exception e) {
			logger.warn("do Task error", e);
			ssh.disconnect();
			throw e;
		}

		return task;
	}

	private void release(RemoteTask task) {
		task.getSshServer().disconnect();
	}


	private void waitTaskDone(RemoteTask task) {
		logger.info("task[id=" + task.getName() + "] waiter start");
		try {
			task.getFuture().get(task.getTimeout(), TimeUnit.MILLISECONDS);
		} catch (CancellationException e) {
			logger.info("task[id=" + task.getName() + "] was cancelled.");
		} catch (InterruptedException e) {
			logger.info("task[id=" + task.getName() + "] waiter was interrupted.");
		} catch (ExecutionException e) {
			logger.error("task[id=" + task.getName() + "] ExecutionException", e);
		} catch (TimeoutException e) {
			logger.error("task[id=" + task.getName() + "] timeout [" + task.getTimeout() + "]", e);
		} catch (Exception e) {
			logger.error("task[id=" + task.getName() + "] exception", e);
		}
		release(task);
		logger.info("task[id=" + task.getName() + "] stop");

		logger.info("task[id=" + task.getName() + "] waiter stop");

	}

	public int getLineCount(String host, String username, String password, String logFileName, String... greps)
			throws Exception {
		SSHClient client = sshClientFactory.getNewSSHClient(host, username, password);
		client.connect();
		try {
			String result = client.executeCommand("cat " + logFileName + generateGrepCommand(greps)
					+ " | wc -l | awk '{print $1}'");
			return Integer.parseInt(result.trim());
		} finally {
			client.disconnect();
		}
	}

	public int moreBeforeLog(StringBuilder sb, String host, String username, String password, int nowEndLine,
	                         String logFileName, String... greps) throws Exception {
		SSHClient client = sshClientFactory.getNewSSHClient(host, username, password);
		client.connect();
		try {
			// next line
			int start = nowEndLine - 30;
			if (start <= 0) {
				start = 1;
			}
			String out = client.executeCommand("cat " + logFileName + generateGrepCommand(greps) + " | awk 'NR=="
					+ start + ",NR==" + nowEndLine + " {print $0}' ");
			sb.append(out);
			return start;
		} finally {
			client.disconnect();
		}
	}

	private String generateGrepCommand(String... greps) {
		StringBuilder result = new StringBuilder();
		if (greps != null) {
			for (String grep : greps) {
				if (grep != null && grep.trim().length() > 0) {
					result.append(" | grep '" + grep + "'");
				}
			}
		}
		return result.toString();
	}

	private String generateMoreLogCommand(String logFileName, int startLine, int endLine, String... greps) {
		StringBuilder result = new StringBuilder();
		result.append("cat " + logFileName);
		result.append(generateGrepCommand(greps));
		result.append(" | awk 'NR==" + startLine + ",NR==" + endLine + " {print $0}END{print NR}' ");
		return result.toString();
	}

	public ReadLogResult moreLog(String host, String username, String password, int startLine, String logFileName,
	                             String... greps) throws Exception {

		SSHClient client = sshClientFactory.getNewSSHClient(host, username, password);
		client.connect();
		try {
			// next line
			startLine++;
			// 每次读200行
			int endLine = startLine + 500;
			String out = client.executeCommand(generateMoreLogCommand(logFileName, startLine, endLine, greps));
			int last = out.lastIndexOf("\n");
			int last2 = out.lastIndexOf("\n", last - 1);
			if (last2 == -1) {
				last2 = 0;
			} else {
				last2++;
			}
			int endLine2 = Integer.parseInt(out.substring(last2).trim());
			endLine = endLine < endLine2 ? endLine : endLine2;
			return new ReadLogResult(startLine - 1, endLine, out.substring(0, last2));
		} finally {
			client.disconnect();
		}
	}

	public String[] listFile(String host, String username, String password, String path) throws Exception {
		SSHClient client = sshClientFactory.getNewSSHClient(host, username, password);
		client.connect();
		try {
			String out = client.executeCommand("find " + path + "|sort|awk 'NR==2,NR==10000000 {print $0}'|sed 's/"
					+ path.replaceAll("/", "\\\\/") + "//g'");
			return out.split("\n");
		} finally {
			client.disconnect();
		}
	}

	public String readAntx(String host, String username, String password) throws Exception {
		SSHClient client = sshClientFactory.getNewSSHClient(host, username, password);
		client.connect();
		try {
			String out = client.executeCommand("cat /home/admin/antx.properties");
			return out;
		} finally {
			client.disconnect();
		}
	}

	public String saveAntx(String host, String username, String password, String antx) throws Exception {
		SSHClient client = sshClientFactory.getNewSSHClient(host, username, password);
		client.connect();
		try {
			String out = client.executeCommand("cat>/home/admin/antx.properties<<EOF\n"
					+ antx.replaceAll("\\$\\{", "\\\\\\$\\{") + "\nEOF");
			return out;
		} finally {
			client.disconnect();
		}
	}
}
