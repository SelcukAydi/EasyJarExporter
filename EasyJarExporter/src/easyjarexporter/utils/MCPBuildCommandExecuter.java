package easyjarexporter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MCPBuildCommandExecuter {

	private String command;
	
	private String workingDir;
	
	public MCPBuildCommandExecuter(String command, String workingDir) {
		this.command = command;
		this.workingDir = workingDir;
	}
	
	public String execute() {
		String[] bash = new String[] { "/bin/bash", "-c", command };
		String result = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(bash);
			pb.directory(new File(workingDir));
			Process proc = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null || (line = errorReader.readLine()) != null ) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			result = builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}
}
