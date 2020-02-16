package easyjarexporter.tasks;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.ui.jarpackager.IJarExportRunnable;
import org.eclipse.jdt.ui.jarpackager.JarPackageData;
import org.eclipse.swt.widgets.Shell;

import easyjarexporter.constants.Constants;
import easyjarexporter.utils.MCPBuilder;

public class JarExportTask extends Job {

	private MCPBuilder mcpBuilder = null;
	
	private Shell parentShell;
	
	private IEclipsePreferences preferences;
	
	private IResource resource;
	
	private IFile[] filesToExport;
	

	public JarExportTask(String name, Shell parentShell, IFile[] filesToExport) {
		super(name);
		this.parentShell = parentShell;
		this.filesToExport = filesToExport;
		preferences = InstanceScope.INSTANCE.getNode("easyjarexporter.preferences");
		setUser(true);
	}

//	@Override
//	protected IStatus run(IProgressMonitor monitor) {
//		monitor.beginTask("Easily Jar Exporting...", 100);
//		try {
//			if (monitor.isCanceled()) {
//				return Status.CANCEL_STATUS;
//			}
//			monitor.subTask("Building...");
//			monitor.worked(50);
//			mcpBuilder.build();
//			if (monitor.isCanceled()) {
//				return Status.CANCEL_STATUS;
//			}
//			monitor.subTask("Exporting...");
//			monitor.worked(50);
//			mcpBuilder.export();
//		} finally {
//			monitor.done();
//		}
//		return Status.OK_STATUS;
//	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		JarPackageData description = new JarPackageData();
		String base = preferences.get("OUTPUT_DIR", System.getProperty("user.home"));
		 java.nio.file.Path path =  Paths.get(base, "easily-exported.jar");
		IPath location = new Path(path.toString());
		description.setJarLocation(location);
		description.setSaveManifest(false);
//		description.setExportClassFiles(false);
//		description.setExportJavaFiles(false);
//		description.setExportStructuralOnly(true);
//		description.areDirectoryEntriesIncluded();
//		description.setIncludeDirectoryEntries(true);
		
//		IResource[] arr = new IResource[] { resource.getProject().findMember(resource.getProjectRelativePath()) };
		description.setElements(filesToExport);
		
		
		
		
//		description.setExportClassFiles(true);
//		description.setExportJavaFiles(true);
		IJarExportRunnable runnable = description.createJarExportRunnable(parentShell);
		try {
			runnable.run(monitor);
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return Status.OK_STATUS;
	}

	@Override
	public boolean belongsTo(Object family) {
		return Constants.FAMILY_EASY_JAR_EXPORT == family;
	}
}
