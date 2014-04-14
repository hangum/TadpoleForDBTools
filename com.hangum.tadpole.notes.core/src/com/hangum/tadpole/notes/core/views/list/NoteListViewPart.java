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
package com.hangum.tadpole.notes.core.views.list;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.notes.core.Activator;
import com.hangum.tadpole.notes.core.Messages;
import com.hangum.tadpole.notes.core.define.NotesDefine.NOTE_TYPES;
import com.hangum.tadpole.notes.core.dialogs.NewNoteDialog;
import com.hangum.tadpole.notes.core.dialogs.ViewDialog;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.NotesDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_Notes;
import com.swtdesigner.ResourceManager;

import org.eclipse.swt.widgets.Button;

/**
 * Notes
 * 
 * @author hangum
 *
 */
public class NoteListViewPart extends ViewPart {
	public static final String ID = "com.hangum.tadpole.notes.core.view.list"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(NoteListViewPart.class);

	private Combo comboTypes;
	private TableViewer tableViewer;
	private Text textFilter;
	private Combo comboRead;
	
	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;
	
	public NoteListViewPart() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		setPartName(Messages.NoteListViewPart_0);
		
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		Composite compositeToolbar = new Composite(parent, SWT.NONE);
		compositeToolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeToolbar = new GridLayout(1, false);
		gl_compositeToolbar.horizontalSpacing = 1;
		gl_compositeToolbar.marginHeight = 1;
		gl_compositeToolbar.marginWidth = 1;
		compositeToolbar.setLayout(gl_compositeToolbar);
		
		ToolBar toolBar = new ToolBar(compositeToolbar, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(0, 0, 88, 20);
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		tltmRefresh.setToolTipText(Messages.NoteListViewPart_1);
		tltmRefresh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
		
		ToolItem tltmCreate = new ToolItem(toolBar, SWT.NONE);
		tltmCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewNoteDialog dialog = new NewNoteDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				if(dialog.OK == dialog.open()) {
					initData();
				}
			}
		});
		tltmCreate.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/notes_new.png")); //$NON-NLS-1$
		tltmCreate.setToolTipText(Messages.NoteListViewPart_2);
		
		final ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(!iss.isEmpty()) {
					try {
						if(MessageDialog.openQuestion(null, Messages.NoteListViewPart_3, Messages.NoteListViewPart_4)) {
							NotesDAO noteDao = (NotesDAO)iss.getFirstElement();
							TadpoleSystem_Notes.deleteNote(noteDao.getSeq());
							
							initData();
						}
					} catch(Exception ee) {
						logger.error("delete note", ee); //$NON-NLS-1$
					}
				}
				
			}
		});
		tltmDelete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/notes_delete.png")); //$NON-NLS-1$
		tltmDelete.setToolTipText(Messages.NoteListViewPart_6);
		tltmDelete.setEnabled(false);
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(5, false);
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.verticalSpacing = 1;
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblFilter = new Label(compositeBody, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.NoteListViewPart_7);
		
		comboTypes = new Combo(compositeBody, SWT.READ_ONLY);
		comboTypes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 80;
		gd_combo.minimumWidth = 80;
		comboTypes.setLayoutData(gd_combo);
		
		comboTypes.add(Messages.NoteListViewPart_8);
		comboTypes.setData(Messages.NoteListViewPart_8, "Send"); //$NON-NLS-1$
		
		comboTypes.add(Messages.NoteListViewPart_9);
		comboTypes.setData(Messages.NoteListViewPart_9, "Receive"); //$NON-NLS-1$
		
		comboTypes.select(1);
		
		comboRead = new Combo(compositeBody, SWT.READ_ONLY);
		comboRead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		comboRead.add(Messages.NoteListViewPart_10);
		comboRead.setData(Messages.NoteListViewPart_10, "Read"); //$NON-NLS-1$
		
		comboRead.add(Messages.NoteListViewPart_11);
		comboRead.setData(Messages.NoteListViewPart_11, "Not yet Read"); //$NON-NLS-1$
		
		comboRead.select(0);
		
		new Label(compositeBody, SWT.NONE);
		
		textFilter = new Text(compositeBody, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					initData();
				}
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeDate = new Composite(compositeBody, SWT.NONE);
		compositeDate.setLayout(new GridLayout(5, false));
		compositeDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		Label lblStart = new Label(compositeDate, SWT.NONE);
		lblStart.setText(Messages.NoteListViewPart_lblStart_text_1);
		
		dateTimeStart = new DateTime(compositeDate, SWT.BORDER);
		
		// 일주일전 시간을 얻는다.
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000));
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		Label label = new Label(compositeDate, SWT.NONE);
		label.setText(Messages.NoteListViewPart_label_text);
		
		dateTimeEnd = new DateTime(compositeDate, SWT.BORDER);
		
		Button buttonSearch = new Button(compositeDate, SWT.NONE);
		buttonSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		buttonSearch.setText(Messages.NoteListViewPart_button_text);
		
		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				tltmDelete.setEnabled(true);
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(!iss.isEmpty()) {
					
					String selComboType = (String)comboTypes.getData(comboTypes.getText());
					NOTE_TYPES noteType = selComboType.equals("Send")?NOTE_TYPES.SEND:NOTE_TYPES.RECEIVE; //$NON-NLS-1$
					ViewDialog dialog = new ViewDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), (NotesDAO)iss.getFirstElement(), noteType);
					if(Dialog.OK == dialog.open()) {
						initData();
					}
				}
			}
		});
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		
		createColumns();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new NoteListLabelProvider());
		
		initData();
	}
	
	/**
	 * initialize data
	 */
	public void initData() {
		try {
			String selComboType = comboTypes.getData(comboTypes.getText()).toString();
			String selComboRead = comboRead.getData(comboRead.getText()).toString();
			
			Calendar calendarStart = Calendar.getInstance();
			calendarStart.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
			
			Calendar calendarEnd = Calendar.getInstance();
			calendarEnd.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
			
			List<NotesDAO> listNotes = TadpoleSystem_Notes.getNoteList(SessionManager.getSeq(), selComboType, 
							selComboRead, textFilter.getText(),
							new java.sql.Date(calendarStart.getTimeInMillis()), new java.sql.Date(calendarEnd.getTimeInMillis())
					);
			tableViewer.setInput(listNotes);
		} catch(Exception e) {
			logger.error("Get note list", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * create columns
	 */
	private void createColumns() {
		String[] names 	= {Messages.NoteListViewPart_14, Messages.NoteListViewPart_15, Messages.NoteListViewPart_16};
		int[] sizes		= {150,  300, 150};
		
		for(int i=0; i<names.length; i++) {
			String name = names[i];
			int size = sizes[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnEngine = tableViewerColumn.getColumn();
			tblclmnEngine.setWidth(size);
			tblclmnEngine.setText(name);
		}
	}

	@Override
	public void setFocus() {
		textFilter.setFocus();
	}

}
/**
 * note list label provider
 * 
 * @author hangum
 *
 */
class NoteListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		NotesDAO dto = (NotesDAO)element;

		switch(columnIndex) {
		case 0: return ""+dto.getReceiveUserId(); //$NON-NLS-1$
		case 1: return dto.getTitle();
		case 2: return dto.getCreate_time();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}