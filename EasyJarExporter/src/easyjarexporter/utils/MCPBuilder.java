package easyjarexporter.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import easyjarexporter.exceptions.MCPBuildException;
import easyjarexporter.exceptions.MCPJarExportException;

public class MCPBuilder {

	private static final String JAR_DIR = "build/load";

	private List<String> cpJarFiles;

	private IEclipsePreferences preferences;

	private List<String> selectedFiles;

	public MCPBuilder(List<String> selectedFiles) {
		cpJarFiles = new ArrayList<String>();
		preferences = InstanceScope.INSTANCE.getNode("easyjarexporter.preferences");
		this.selectedFiles = selectedFiles;
	}

	private void bringJarFiles(final File folder) throws FileNotFoundException {
		if (Files.exists(folder.toPath())) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					bringJarFiles(fileEntry);
				} else if (fileEntry.getName().endsWith(".jar")) {
					cpJarFiles.add(fileEntry.getAbsolutePath());
				}
			}
		} else {
			throw new FileNotFoundException(
					"While getting class path jar files at " + folder.toPath().toString() + " cannot be found.");
		}
	}

	public List<String> getJarFiles() {
		return this.cpJarFiles;
	}

	public String createMcpBuildCommand() throws FileNotFoundException {
		StringBuilder buildCommand = new StringBuilder();
		String mcpCoreRoot = preferences.get("MCP_CORE_ROOT_DIR", System.getProperty("user.home"));
		Path folder = Paths.get(mcpCoreRoot, JAR_DIR);
		bringJarFiles(new File(folder.toString()));
		buildCommand.append("javac -cp .:");
		for (String file : this.cpJarFiles) {
			buildCommand.append(file + ":");
		}
		buildCommand.append(" -d " + preferences.get("OUTPUT_DIR", null));
		selectedFiles.forEach(item -> buildCommand.append(" " + item));
		return buildCommand.toString();
	}

	private String createExportJarCommand() {
		StringBuilder exportCommand = new StringBuilder();
		exportCommand.append("jar cf " + preferences.get("JAR_NAME", "easily_exported.jar"));
		exportCommand.append(" " + preferences.get("OUTPUT_DIR", System.getProperty("user.home")));
		return exportCommand.toString();
	}

	public void build() throws MCPBuildException {
		String command = null;
		try {
			command = createMcpBuildCommand();
		} catch (FileNotFoundException e) {
			throw new MCPBuildException(e.getMessage());
		}
		MCPBuildCommandExecuter executer = new MCPBuildCommandExecuter(command,
				preferences.get("MCP_CORE_ROOT_DIR", System.getProperty("user.home")));
		String result = executer.execute();
		if (!result.isEmpty()) {
			throw new MCPBuildException("While executing '" + command + "' exception occured. [" + result + "]");
		}
	}

	public void export() throws MCPJarExportException {
		String command = createExportJarCommand();
		MCPBuildCommandExecuter executer = new MCPBuildCommandExecuter(command,
				preferences.get("OUTPUT_DIR", System.getProperty("user.home")));
		String result = executer.execute();
		if (!result.isEmpty()) {
			throw new MCPJarExportException("While executing '" + command + "' exception occured. [" + result + "]");
		}
	}
}
