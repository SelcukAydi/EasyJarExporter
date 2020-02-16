package easyjarexporter.tasks;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import easyjarexporter.constants.Constants;
import easyjarexporter.utils.MCPBuilder;

public class JarExportTask extends Job {

	private MCPBuilder mcpBuilder = null;

	public JarExportTask(String name, MCPBuilder mcpBuilder) {
		super(name);
		this.mcpBuilder = mcpBuilder;
		setUser(true);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Easily Jar Exporting...", 100);
		try {
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			monitor.subTask("Building...");
			monitor.worked(50);
			mcpBuilder.build();
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			monitor.subTask("Exporting...");
			monitor.worked(50);
			mcpBuilder.export();
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	@Override
	public boolean belongsTo(Object family) {
		return Constants.FAMILY_EASY_JAR_EXPORT == family;
	}
}
