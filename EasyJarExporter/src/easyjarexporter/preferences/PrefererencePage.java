package easyjarexporter.preferences;

import java.io.File;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

public class PrefererencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private DirectoryFieldEditor mcpCoreRootDir;
	private DirectoryFieldEditor outputDir;
	private StringFieldEditor jarName;
	
	public PrefererencePage() {
		super(GRID);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
	}
	
	@Override
	public void init(IWorkbench arg0) {
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, "easyjarexporter.preferences"));
		setDescription("Set your preferences for only this workspace");
	}

	@Override
	protected void createFieldEditors() {
		mcpCoreRootDir = new DirectoryFieldEditor("mcp_core_root_dir", "&MCP Core Root Directory:", getFieldEditorParent());
		outputDir = new DirectoryFieldEditor("output_dir", "&Output Directory:", getFieldEditorParent());
		jarName = new StringFieldEditor("export_jar_name", "Exported Jar File Name", getFieldEditorParent());
		
		addField(mcpCoreRootDir);
		addField(outputDir);
		addField(jarName);
		
//        addField(new BooleanFieldEditor("BOOLEAN_VALUE", "&A boolean preference", getFieldEditorParent()));
//
//        addField(new RadioGroupFieldEditor("CHOICE", "A &multiple-choice preference", 1,
//                new String[][] { { "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
//        addField(new StringFieldEditor("MySTRING1", "A &text preference:", getFieldEditorParent()));
//        addField(new StringFieldEditor("MySTRING2", "A t&ext preference:", getFieldEditorParent()));
	}
	
	@Override
	protected void performApply() {
		if(!performOk()) {
			return;
		}
		
		IPreferenceStore store = getPreferenceStore();
		store.setValue("MCP_CORE_ROOT_DIR", mcpCoreRootDir.getStringValue());
		store.setValue("OUTPUT_DIR", outputDir.getStringValue());
		
		mcpCoreRootDir.store();
		outputDir.store();
		jarName.store();
		
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode("easyjarexporter.preferences");
		
		try {
			preferences.flush();
			showInfoMessage("Your preferences are set for only this workspace.", 
					"Success Information", 
					getFieldEditorParent().getShell());
		} catch (BackingStoreException e) {
			showErrorMessage("Could not set your preferences.", "Unknown Error", getFieldEditorParent().getShell());
		}
	}
	
	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();
		store.setDefault("MCP_CORE_ROOT_DIR", System.getProperty("user.home"));
		store.setDefault("OUTPUT_DIR", System.getProperty("user.home"));
	}
	
	private void initializeValues() {
		IPreferenceStore store = getPreferenceStore();
		mcpCoreRootDir.setStringValue(store.getString("MCP_CORE_ROOT_DIR"));
		outputDir.setStringValue(store.getString("OUTPUT_DIR"));
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
		initializeValues();
	}
	
	@Override
	public boolean performOk() {
		File file1 = new File(mcpCoreRootDir.getStringValue());
		if(file1 != null && file1.exists() && !file1.isDirectory() || !file1.canRead() || !file1.canExecute() || !file1.canWrite()) {
			showErrorMessage(mcpCoreRootDir.getLabelText().replaceAll(":", "") + " is not a real directory or does not exist or you don't have "
					+ "valid permissions. Please check your inputs again.", "Directory Error", getFieldEditorParent().getShell());
			return false;
		}
		
		File file2 = new File(outputDir.getStringValue());
		if(file2 != null && !file2.isDirectory() || !file2.canRead() || !file2.canExecute() || !file2.canWrite()) {
			showErrorMessage(outputDir.getLabelText().replaceAll(":", "") + " is not a real directory or you don't have "
					+ "valid permissions. Please check your inputs again.", "Directory Error", getFieldEditorParent().getShell());
			return false;
		}
		return super.performOk();
	}
	
	private void showErrorMessage(String message, String title, Shell parent) {
		MessageDialog.openError(parent, title, message);
	}
	
	private void showInfoMessage(String message, String title, Shell parent) {
		MessageDialog.openInformation(parent, title, message);
	}
}
