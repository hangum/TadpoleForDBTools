package com.hangum.tadpole.mongodb.core.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.hangum.db.define.PreferenceDefine;
import com.hangum.tadpole.mongodb.core.Activator;

/**
 * mongodb prefernec initialize
 * 
 * @author hangum
 *
 */
public class MongoDBPreferenceInitializer extends AbstractPreferenceInitializer {

	public MongoDBPreferenceInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		// mongodb
		store.setDefault(PreferenceDefine.MONGO_DEFAULT_LIMIT, PreferenceDefine.MONGO_DEFAULT_LIMIT_VALUE);
		store.setDefault(PreferenceDefine.MONGO_DEFAULT_FIND, PreferenceDefine.MONGO_DEFAULT_FIND_EXTEND);
		store.setDefault(PreferenceDefine.MONGO_DEFAULT_RESULT, PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE);
	}

}
