package com.hangum.tadpole.mongodb.core.preference;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.db.define.PreferenceDefine;
import com.hangum.tadpole.mongodb.core.Activator;

/** 
 * mongodb prefernec page
 * 
 *  @author hangum
 */
public class MongoDBPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBPreferencePage.class);

	/**
	 * Create the preference page.
	 */
	public MongoDBPreferencePage() {
		super(FLAT);
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		{
			IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(PreferenceDefine.MONGO_DEFAULT_LIMIT, PreferenceDefine.MONGO_DEFAULT_LIMIT, getFieldEditorParent());
			integerFieldEditor.setStringValue(PreferenceDefine.MONGO_DEFAULT_LIMIT_VALUE);
			integerFieldEditor.setTextLimit(5);
			addField(integerFieldEditor);
		}
		{
			IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT, PreferenceDefine.MONGO_DEFAULT_MAX_COUNT, getFieldEditorParent());
			integerFieldEditor.setStringValue(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT_VALUE);
			integerFieldEditor.setTextLimit(5);
			addField(integerFieldEditor);
		}
		
		addField(new RadioGroupFieldEditor(PreferenceDefine.MONGO_DEFAULT_FIND, "Default Find Page", 2, 
				new String[][]{{"Basic Search", PreferenceDefine.MONGO_DEFAULT_FIND_BASIC}, 
								{"Extend Search", PreferenceDefine.MONGO_DEFAULT_FIND_EXTEND}}, getFieldEditorParent(), false));
		
		addField(new RadioGroupFieldEditor(PreferenceDefine.MONGO_DEFAULT_RESULT, "Default Result Page", 3, 
				new String[][]{{"Tree View", PreferenceDefine.MONGO_DEFAULT_RESULT_TREE},				
								{"Table View", PreferenceDefine.MONGO_DEFAULT_RESULT_TABLE}, 
								{"Text View", PreferenceDefine.MONGO_DEFAULT_RESULT_TEXT}}, getFieldEditorParent(), false));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

}
