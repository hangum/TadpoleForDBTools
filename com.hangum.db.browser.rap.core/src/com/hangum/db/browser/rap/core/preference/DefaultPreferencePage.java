package com.hangum.db.browser.rap.core.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.define.PreferenceDefine;

/**
 * tadpole default preference
 * 
 * @author hangum
 *
 */
public class DefaultPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Create the preference page.
	 */
	public DefaultPreferencePage() {
		super(FLAT);
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		{
			// Create the field editors
			IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(
					PreferenceDefine.SELECT_DEFAULT_PREFERENCE, 
					Messages.DefaultPreferencePage_0, getFieldEditorParent());
			integerFieldEditor.setTextLimit(4);
			integerFieldEditor.setValidRange(100, PreferenceDefine.SELECT_DEFAULT_MAX_PREFERENCE_VALUE);
			addField(integerFieldEditor);
		}
		{
			
			IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(PreferenceDefine.SESSION_DFEAULT_PREFERENCE, Messages.DefaultPreferencePage_2, getFieldEditorParent());
			integerFieldEditor.setTextLimit(5);
			integerFieldEditor.setValidRange(5, PreferenceDefine.SELECT_DEFAULT_MAX_PREFERENCE_VALUE);
			addField(integerFieldEditor);
		}
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
//		setDescription(Messages.DefaultPreferencePage_1);
	}

}
