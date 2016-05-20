/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     billy.goo - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * Query Result 창에서 데이터 선택히 한 창에서 조회할수 있게
 * 해주는 창입니다. 
 * 
 * 이 창에서는 네비게이터 버튼을 이용해 다른 데이터로 이동할 수 
 * 있습니다. 
 * 
 * @author billy.goo
 *
 */
public class RecordViewDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(RecordViewDialog.class);
	private QueryExecuteResultDTO dto;
	private Composite container;

	private int loc = 0;
	private List<Map<Integer, Object>> resultSet;

	private Map<Integer, Text> controlList = new HashMap<Integer, Text>();
	private Label lblCurrentCount;
	private Label lblMaxCount;

	public RecordViewDialog(Shell parentShell, QueryExecuteResultDTO dto,
			Object selection) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.dto = dto;
		this.resultSet = dto.getDataList().getData();
		this.loc = resultSet.indexOf(selection);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().RecordViewDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;

		Composite compositeNavigation = new Composite(container, SWT.NONE);
		compositeNavigation.setLayout(new GridLayout(7, false));
		compositeNavigation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button btnFirst = new Button(compositeNavigation, SWT.CENTER);
		btnFirst.setToolTipText(Messages.get().RecordViewDialog_1);
		GridData gd_btnFirst = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFirst.widthHint = 30;
		btnFirst.setLayoutData(gd_btnFirst);
		btnFirst.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValue(0);
			}
		});
		btnFirst.setText("<<");

		Button btnPrevious = new Button(compositeNavigation, SWT.NONE);
		btnPrevious.setToolTipText(Messages.get().RecordViewDialog_2);
		GridData gd_btnPrevious = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPrevious.widthHint = 65;
		btnPrevious.setLayoutData(gd_btnPrevious);
		btnPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValue(loc - 1);
			}
		});
		btnPrevious.setText("<"); //$NON-NLS-1$

		Button btnNext = new Button(compositeNavigation, SWT.NONE);
		btnNext.setToolTipText(Messages.get().RecordViewDialog_5);
		GridData gd_btnNext = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNext.widthHint = 65;
		btnNext.setLayoutData(gd_btnNext);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValue(loc + 1);
			}
		});
		btnNext.setText(">"); //$NON-NLS-1$

		Button btnLast = new Button(compositeNavigation, SWT.NONE);
		btnLast.setToolTipText(Messages.get().RecordViewDialog_7);
		GridData gd_btnLast = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLast.widthHint = 30;
		btnLast.setLayoutData(gd_btnLast);
		btnLast.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValue(resultSet.size() - 1);
			}
		});
		btnLast.setText(">>"); //$NON-NLS-1$
		
		lblCurrentCount = new Label(compositeNavigation, SWT.CENTER);
		lblCurrentCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblCurrentCount.setText("current count"); //$NON-NLS-1$
		
		Label label_1 = new Label(compositeNavigation, SWT.NONE);
		label_1.setText("/"); //$NON-NLS-1$
		
		lblMaxCount = new Label(compositeNavigation, SWT.CENTER);
		lblMaxCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblMaxCount.setText(String.valueOf(resultSet.size()));

		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		ScrolledComposite compositeScrolled = new ScrolledComposite(container,
				SWT.H_SCROLL | SWT.V_SCROLL);
		compositeScrolled.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		compositeScrolled.setLayout(new FillLayout());
		Composite compositeContent = new Composite(compositeScrolled,
				SWT.BORDER);
		compositeContent.setLayout(new GridLayout(3, false));
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		// LOOP -------------------------------------------------------
		Map<Integer, String> column = dto.getColumnName();
		for (Integer index : column.keySet()) {
			Label lblValueName = new Label(compositeContent, SWT.NONE);
			lblValueName.setText(column.get(index));
			Label lblSeperator = new Label(compositeContent, SWT.NONE);
			lblSeperator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
					false, false, 1, 1));
			lblSeperator.setText(":"); //$NON-NLS-1$
			Text txtValue = new Text(compositeContent, SWT.BORDER
					| SWT.READ_ONLY);
			txtValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			controlList.put(index, txtValue);
		}
		compositeScrolled.setContent(compositeContent);
		compositeScrolled.setMinSize(compositeContent.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		compositeScrolled.setExpandHorizontal(true);
		compositeScrolled.setExpandVertical(true);

		setValue(loc);
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	private void setValue(int loc) {
		int limit = resultSet.size();
		if (loc >= limit) {
			return;
		} else if (loc < 0) {
			loc = 0;
		}
		this.loc = loc;
		
		lblCurrentCount.setText(String.valueOf(loc+1));
		
		Map<Integer, Object> record = resultSet.get(loc);
		for (Integer index : record.keySet()) {
			Text txt = controlList.get(index);
			if(txt != null) txt.setText(record.get(index)==null?"":record.get(index).toString()); //$NON-NLS-1$
		}
		if (getDialogArea() != null) {
			getDialogArea().redraw();
		}
	}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(460, 600);
	}
}
