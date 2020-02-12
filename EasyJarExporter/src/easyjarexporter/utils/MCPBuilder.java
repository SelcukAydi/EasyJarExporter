package easyjarexporter.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class MCPBuilder {
	
	private static final String MCP_ROOT_DIR = "/home/sia/eclipse-workspaces/saydi_snap/mcp_core_root/build/load";
	
	private List<String> cpJarFiles;
	
	private StringBuilder buildCommand;
	
	private String mcpCoreRootDir;
	
	private String outputDir;
	
	private IEclipsePreferences preferences;
	
	public MCPBuilder() {
		cpJarFiles = new ArrayList<String>();
		buildCommand = new StringBuilder();
		preferences = InstanceScope.INSTANCE.getNode("easyjarexporter.preferences");
	}
	
	public void bringJarFiles() {
		final File folder = new File(MCP_ROOT_DIR);
		bringJarFiles(folder);
	}
	
	private void bringJarFiles(final File folder) {
	    if (Files.exists(folder.toPath())) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					bringJarFiles(fileEntry);
				} else if (fileEntry.getName().endsWith(".jar")) {
					System.out.println(fileEntry.getName());
					cpJarFiles.add(fileEntry.getAbsolutePath());
				}
			}
		}
	}
	
	public List<String> getJarFiles() {
		return this.cpJarFiles;
	}
	
	public String createMcpBuildCommand() {
		buildCommand.append("javac -cp .:");
		for(String file : this.cpJarFiles) {
			buildCommand.append(file + ":");
		}
		buildCommand.append(" -d /home/sia/Desktop");
		buildCommand.append(" /home/sia/eclipse-workspaces/saydi_snap/mcp_core_root/ims_svc/src/main/java/com/nortelnetworks/ims/cap/svc/xas/eventhandler/XASHandlerTermInitiate.java");
		return buildCommand.toString();
	}
	
	private String getPreferenceString(String key) {
		IPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "easyjarexporter.preferences");
		return store.getString("MCP_CORE_ROOT_DIR");
	}
	
	public String getBuildCommand() {
		return createMcpBuildCommand();
	}
	
	
	
	
	
	
}
