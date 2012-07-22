package com.hangum.tadpole.preference.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.Messages;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * rdb preference
 * 
 * @author hangum
 *
 */
public class RDBPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Text textSelectLimit;
	private Text textResultPage;
	private Text textOraclePlan;

	public RDBPreferencePage() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.DefaultPreferencePage_0);
		
		textSelectLimit = new Text(container, SWT.BORDER);
		textSelectLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText(Messages.DefaultPreferencePage_other_labelText_1);
		
		textResultPage = new Text(container, SWT.BORDER);		
		textResultPage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		label.setText(Messages.RDBPreferencePage_label_text);
		
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText(Messages.DefaultPreferencePage_other_labelText);
		
		textOraclePlan = new Text(container, SWT.BORDER);
		textOraclePlan.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Button btnCreatePlanTable = new Button(container, SWT.NONE);
		btnCreatePlanTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCreatePlanTable.setText(Messages.RDBPreferencePage_btnCreatePlanTable_text);
		
		initDefaultValue();

		return container;
	}
	
	@Override
	public boolean performOk() {
		String txtSelectLimit = textSelectLimit.getText();
		String txtResultPage = textResultPage.getText();
		String txtOraclePlan = textOraclePlan.getText();
		
		try {
			Integer.parseInt(txtSelectLimit);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_0 + "는 숫자이어야 합니다.");			 //$NON-NLS-1$
			return false;
		}
		
		try {
			Integer.parseInt(txtResultPage);
		} catch(Exception e) {
			MessageDialog.openError(getShell(), "Confirm", Messages.DefaultPreferencePage_other_labelText_1 + "는 숫자이어야 합니다.");			 //$NON-NLS-1$
			return false;
		}
		
		if("".equals(txtOraclePlan)) {
			MessageDialog.openError(getShell(), "Confirm", "Oracle Plan 테이블이 공백 입니다.");			 //$NON-NLS-1$
			return false;
		}
		
		// 테이블에 저장 
		try {
			TadpoleSystem_UserInfoData.updateRDBUserInfoData(txtSelectLimit, txtResultPage, txtOraclePlan);
			
			// session 데이터를 수정한다.
			SessionManager.setUserInfo(PreferenceDefine.SELECT_LIMIT_COUNT, txtSelectLimit);
			SessionManager.setUserInfo(PreferenceDefine.SELECT_RESULT_PAGE_PREFERENCE, txtResultPage);
			SessionManager.setUserInfo(PreferenceDefine.ORACLE_PLAN_TABLE, txtOraclePlan);			
		} catch(Exception e) {
			e.printStackTrace();
			
			MessageDialog.openError(getShell(), "Confirm", "데이터를 수정하는 중에 오류가 발생했습니다.\n" + e.getMessage());
			return false;
		}
		
		return super.performOk();
	}
	
	@Override
	public boolean performCancel() {
		initDefaultValue();
		
		return super.performCancel();
	}
	
	@Override
	protected void performApply() {

		super.performApply();
	}
	
	@Override
	protected void performDefaults() {
		initDefaultValue();

		super.performDefaults();
	}
	
	/**
	 * 초기값을 설정 합니다.
	 */
	private void initDefaultValue() {
		textSelectLimit.setText( "" + GetPreferenceGeneral.getQueryResultCount() );
		textResultPage.setText( "" + GetPreferenceGeneral.getPageCount() );
		textOraclePlan.setText( GetPreferenceGeneral.getPlanTableName() );
		
	}
}
