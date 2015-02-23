/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;

/**
 * Result Message Composite
 * 
 * @author hangum
 *
 */
public class MessageComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(MessageComposite.class);
	private Text textMessage;
	
	/** tadpole message */
//	private TableViewer tableViewerMessage;
//	private List<TadpoleMessageDAO> listMessage = new ArrayList<TadpoleMessageDAO>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MessageComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		textMessage = new Text(this, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//	//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
//		tableViewerMessage = new TableViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
//		tableViewerMessage.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				
//				IStructuredSelection is = (IStructuredSelection)event.getSelection();
//				Object selElement = is.getFirstElement();
//				if(selElement instanceof TadpoleMessageDAO) {
//					TadpoleMessageDAO tmd = (TadpoleMessageDAO)selElement;
//					TadpoleMessageDialog dlg = new TadpoleMessageDialog(null, Messages.MainEditor_20, SQLHistoryLabelProvider.dateToStr(tmd.getDateExecute()), tmd.getStrMessage());
//					dlg.open();
//				}
//			}
//		});
//		Table tableMessage = tableViewerMessage.getTable();
//		tableMessage.setData(RWT.CUSTOM_ITEM_HEIGHT, 68);
//		tableMessage.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
//		tableMessage.setLinesVisible(true);
//		tableMessage.setHeaderVisible(true);
//		tableMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		tableMessage.setSortDirection(SWT.DOWN);
//		
//		// auto column layout
//		AutoResizeTableLayout layoutColumnLayoutMsg = new AutoResizeTableLayout(tableViewerMessage.getTable());
//		tableViewerMessage.getTable().setLayout(layoutColumnLayoutMsg);
//		
//		SQLMessageSorter sorterMessage = new SQLMessageSorter();
//		createTableMessageColumn(tableViewerMessage, sorterMessage, layoutColumnLayoutMsg);
//		
//		tableViewerMessage.setLabelProvider(new SQLHistoryLabelProvider());
//		tableViewerMessage.setContentProvider(new ArrayContentProvider());
//		tableViewerMessage.setInput(listMessage);
//		tableViewerMessage.setComparator(sorterMessage);
//		
//		Composite compositeMessageSub = new Composite(this, SWT.NONE);
//		compositeMessageSub.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		GridLayout gl_compositeMessageSubBtn = new GridLayout(3, false);
//		gl_compositeMessageSubBtn.marginWidth = 1;
//		gl_compositeMessageSubBtn.marginHeight = 0;
//		compositeMessageSub.setLayout(gl_compositeMessageSubBtn);
//		
////		Button btnExportMessage = new Button(compositeMessageSub, SWT.NONE);
////		btnExportMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
////		btnExportMessage.addSelectionListener(new SelectionAdapter() {
////			@Override
////			public void widgetSelected(SelectionEvent e) {
////				StringBuffer sbExportData = new StringBuffer();
////				
////				for(TadpoleMessageDAO dao : listMessage) {
////					sbExportData.append( dao.getStrMessage() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
////				}
////				
//////				downloadExtFile(getUserDB().getDisplay_name() + "_Message.txt", sbExportData.toString()); //$NON-NLS-1$
////			}
////		});
////		btnExportMessage.setText(Messages.MainEditor_43);
//		
//		Label labelMsgDumy = new Label(compositeMessageSub, SWT.NONE);
//		labelMsgDumy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Button btnMessageViewClear = new Button(compositeMessageSub, SWT.NONE);
//		btnMessageViewClear.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				listMessage.clear();
//				tableViewerMessage.refresh();
//			}
//		});
//		btnMessageViewClear.setText(Messages.MainEditor_btnClear_text);

	}

	/**
	 * table view refresh
	 * 
	 * @param tadpoleMessageDAO
	 */
	public void addAfterRefresh(TadpoleMessageDAO tadpoleMessageDAO) {
		textMessage.setText(tadpoleMessageDAO.getStrMessage());
//		listMessage.add(tadpoleMessageDAO);
//		tableViewerMessage.refresh();		
	}
	
//	/**
//	 * error message tableviewer
//	 * 
//	 * @param tableViewerMessage
//	 * @param sorterMessage
//	 * @param layoutColumnLayoutMsg
//	 */
//	public void createTableMessageColumn(TableViewer tableViewerMessage, SQLMessageSorter sorterMessage, AutoResizeTableLayout layoutColumnLayoutMsg) {
//		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerMessage, SWT.NONE);
//		TableColumn tblclmnDate = tableViewerColumn.getColumn();
//		tblclmnDate.setWidth(140);
//		tblclmnDate.setText(Messages.MainEditor_14);
//		tblclmnDate.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnDate, 0));
//		layoutColumnLayoutMsg.addColumnData(new ColumnPixelData(160));
//		
//		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerMessage, SWT.NONE);
//		TableColumn tblclmnSql = tableViewerColumn_1.getColumn();
//		tblclmnSql.setWidth(500);
//		tblclmnSql.setText("Message"); //$NON-NLS-1$
//		tblclmnSql.addSelectionListener(getSelectionAdapter(tableViewerMessage, sorterMessage, tblclmnSql, 1));
//		layoutColumnLayoutMsg.addColumnData(new ColumnWeightData(500));
//	}
//	
//	/**
//	 * tablecolumn adapter
//	 * @param column
//	 * @param index
//	 * @return
//	 */
//	private SelectionAdapter getSelectionAdapter(final TableViewer viewer, final DefaultViewerSorter comparator, final TableColumn column, final int index) {
//		SelectionAdapter selectionAdapter = new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				comparator.setColumn(index);
//				
//				viewer.getTable().setSortDirection(comparator.getDirection());
//				viewer.getTable().setSortColumn(column);
//				viewer.refresh();
//			}
//		};
//		return selectionAdapter;
//	}
	
	@Override
	protected void checkSubclass() {
	}

}
