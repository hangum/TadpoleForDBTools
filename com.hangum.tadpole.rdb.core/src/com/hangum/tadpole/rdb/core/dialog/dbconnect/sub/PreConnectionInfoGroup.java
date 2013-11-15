/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.sql.template.DBOperationType;

/**
 * DB 등록시 공통으로 들어가는 정보를 나열합니다.
 * 
 * 1. 디비종류 2. 그룹명 3. 표시이름 4. 운영타입
 * 
 * @author hangum
 * 
 */
public class PreConnectionInfoGroup extends Group {
	/** already group name */
	protected List<String> listGroupName = new ArrayList<String>();
	/** group name */
	private Combo comboGroup;
	/** display name */
	private Text textDisplayName;
	/** operation type */
	private Combo comboOperationType;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public PreConnectionInfoGroup(Composite parent, int style, List<String> listGroupName) {
		super(parent, style);
		setText(Messages.PreDBInfoComposite_this_text);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		Label lblOperationType = new Label(this, SWT.NONE);
		lblOperationType.setText(Messages.MySQLLoginComposite_lblOperationType_text);

		comboOperationType = new Combo(this, SWT.READ_ONLY);
		comboOperationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (DBOperationType opType : DBOperationType.values()) {
			comboOperationType.add(opType.getTypeName());
		}
		comboOperationType.select(1);

		Label lblGroupName = new Label(this, SWT.NONE);
		lblGroupName.setText(Messages.MySQLLoginComposite_lblGroupName_text);
		comboGroup = new Combo(this, SWT.NONE);
		comboGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (String strGroup : listGroupName)
			comboGroup.add(strGroup);

		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setText(Messages.DBLoginDialog_lblNewLabel_1_text);

		textDisplayName = new Text(this, SWT.BORDER);
		textDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	public List<String> getListGroupName() {
		return listGroupName;
	}

	public void setListGroupName(List<String> listGroupName) {
		this.listGroupName = listGroupName;
	}

	public Combo getComboGroup() {
		return comboGroup;
	}

	public void setComboGroup(Combo comboGroup) {
		this.comboGroup = comboGroup;
	}

	public Text getTextDisplayName() {
		return textDisplayName;
	}

	public void setTextDisplayName(String textDisplayName) {
		this.textDisplayName.setText(textDisplayName);
	}

	public Combo getComboOperationType() {
		return comboOperationType;
	}

	public void setComboOperationType(Combo comboOperationType) {
		this.comboOperationType = comboOperationType;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
