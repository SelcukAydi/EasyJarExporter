package easyjarexporter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MCPBuildCommandExecuter {

	private String command;
	
	public MCPBuildCommandExecuter(String command) {
		this.command = command;
	}
	
	public void execute() {
		String[] args = new String[] { "/bin/bash", "-c", command};
		try {
			ProcessBuilder pb = new ProcessBuilder(args);
			pb.directory(new File("/home/sia/eclipse-workspaces/saydi_snap/mcp_core_root"));
			Process proc = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	
	
}
