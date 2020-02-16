package easyjarexporter.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import easyjarexporter.constants.Constants;
import easyjarexporter.tasks.JarExportTask;
import easyjarexporter.utils.MCPBuilder;
import easyjarexporter.views.FileListView;

public class JarExportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		FileListView fileListView = (FileListView) getViewById(event, 0);
		List<String> selectedFiles = new ArrayList<String>();
		MCPBuilder mcpBuilder = null;

		if (fileListView != null) {
			selectedFiles = fileListView.getSelectedFiles();
//			selectedFiles.add(
//					"/home/sia/eclipse-workspaces/saydi_snap/mcp_core_root/ims_svc/src/main/java/com/nortelnetworks/ims/cap/svc/xas/eventhandler/XASHandlerTermInitiate.java");
			if(selectedFiles.size() > 0) {
				mcpBuilder = new MCPBuilder(selectedFiles);
				JarExportTask jarExportTask = new JarExportTask("Jar Export Task", window.getShell(), handleSelectedFilesPath(selectedFiles));
				jarExportTask.schedule();
			}
		}
		return null;
	}

	private IViewPart getViewById(ExecutionEvent event, int id) throws ExecutionException {
		IViewPart viewer = null;
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IViewReference[] refs = window.getActivePage().getViewReferences();

		for (IViewReference ref : refs) {
			if (Constants.views[id].equals(ref.getId())) {
				viewer = ref.getView(true);
			}
		}
		return viewer;
	}

	private void showInfoMessage(Shell parent, String message) {
		MessageDialog.openInformation(parent, "MCP Builder Information", message);
	}
	
	private IFile[] handleSelectedFilesPath(List<String> files) {
		IFile[] resolvedFiles = new IFile[files.size()];
		int i = 0;
		for(String sPath : files) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(sPath));
			if(file != null) {
				resolvedFiles[i] = file;
			}
			++i;
		}
		return resolvedFiles;
	}
}
