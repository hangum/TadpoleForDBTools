package com.hangum.db.browser.rap.core.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.define.PreferenceDefine;
import com.hangum.db.util.ApplicationArgumentUtils;

/**
 * Preference value initialize
 * 
 * @author hangum
 *
 */
public class AbstractPreferenceInitializer extends org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer {

	public AbstractPreferenceInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		// sql result size
		store.setDefault(PreferenceDefine.SELECT_DEFAULT_PREFERENCE, PreferenceDefine.SELECT_DEFAULT_PREFERENCE_VALUE);

		if(ApplicationArgumentUtils.isStandaloneMode()) {
			store.setDefault(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, PreferenceDefine.SESSION_STANDALONE_DEFAULT_PREFERENCE_VALUE);
		} else {
			store.setDefault(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, PreferenceDefine.SESSION_SERVER_DEFAULT_PREFERENCE_VALUE);
		}
		
	}

}
