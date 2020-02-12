package easyjarexporter.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class EasyJarExporterPreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "easyjarexporter.preferences");
		store.setDefault("MCP_CORE_ROOT_DIR", System.getProperty("user.dir"));
		store.setDefault("OUTPUT_DIR", System.getProperty("user.dir"));
	}

}
