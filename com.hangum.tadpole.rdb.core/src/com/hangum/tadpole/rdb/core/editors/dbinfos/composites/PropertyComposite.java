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
package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.csv.CSVUtils;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Property Composite
 * 
 * This class is to  the properties of a database. 
 * Users can check the current value of each property.
 * 
 * @TODO 
 * - Setting the value on the screen without directly throwing a query to the database.
 * 
 * @author sun.han
 *
 */
public class PropertyComposite extends DBInfosComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PropertyComposite.class);
	
	private UserDBDAO userDB;
	private TableViewer propertyViewer;
	
	private PropertyFilter propertyFilter;
	private Text text;
	
	/* Download service handler */
	private Composite composite;
	private DownloadServiceHandler downloadServiceHandler;
	private List listTableInform = new ArrayList<>();

	/**
	 * Create a composite.
	 * @param parent
	 * @param style
	 */
	public PropertyComposite(Composite parent, int style, UserDBDAO userDB) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		this.userDB = userDB;
		
		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(CommonMessages.get().Filter);
		
		text = new Text(compositeHead, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				propertyFilter.setSearchString(text.getText());
				propertyViewer.refresh();
			}
		});
		
		Button btnRefresh = new Button(compositeHead, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI(true);
			}
		});
		btnRefresh.setText(CommonMessages.get().Refresh);
		
		propertyViewer = new TableViewer(this, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		Table table = propertyViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumn();
		
		propertyViewer.setContentProvider(ArrayContentProvider.getInstance());
		propertyViewer.setLabelProvider(new PropertyInformLabelProvider(userDB));
		
		propertyFilter = new PropertyFilter();
		propertyViewer.addFilter(propertyFilter);
		
		composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Button btnCsvExport = new Button(composite, SWT.NONE);
		btnCsvExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				downloadCSVFile();
			}
		});
		btnCsvExport.setBounds(0, 0, 94, 28);
		btnCsvExport.setText(Messages.get().TablesComposite_btnCsvExport_text);

//		initUI();
		registerServiceHandler();
	}
	
	/**
	 * Create columns.
	 */
	private void createColumn() {
		
		String[] columnName = { Messages.get().PropertyComposite_Name, Messages.get().PropertyComposite_Value };
		int[] columnSize = { 300, 600 };
		int[] columnAlign = { SWT.LEFT, SWT.LEFT };
		
		createColumn( columnName, columnSize, columnAlign );
	}
	
	/**
	 *  Build real columns with attributes. 
	 * 
	 * @param name
	 * @param size
	 */
	private void createColumn(String[] name, int[] size, int[] align) {
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(propertyViewer, align[i]);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
		}
	}
	
	/**
	 * Initialize User Interface. 
	 */
	public void initUI(boolean isRefresh) {
		if(isRefresh) {
			listTableInform.clear();
		} else {
			if(listTableInform.size() != 0) return;
		}
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			listTableInform = sqlClient.queryForList("getProperties", userDB.getDb()); //$NON-NLS-1$
			
			propertyViewer.setInput(listTableInform);
			propertyViewer.refresh();
		} catch (Exception e) {
			logger.error("An error occurred while getting the propery list from " + userDB.getDisplay_name() + ".", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
		}
		
		// google analytic
		AnalyticCaller.track(RDBDBInfosEditor.ID, "PropertyComposite"); //$NON-NLS-1$
	}
	
	/**
	 * Download a CSV file. 
	 */
	private void downloadCSVFile() {
		if(propertyViewer.getTable().getItemCount() == 0) return;
		if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, CommonMessages.get().DoYouWantDownload)) return;
		
		try {
			byte[] strCVSContent = CSVUtils.tableToCSV(propertyViewer.getTable());//makeData(propertyViewer.getTable());
			downloadExtFile(userDB.getDisplay_name() + "_Properties.csv", strCVSContent); //$NON-NLS-1$
			
			MessageDialog.openInformation(null, CommonMessages.get().Information, CommonMessages.get().DownloadIsComplete);
		} catch (Exception e) {
			logger.error("An exception occurred while writing into a csv file.", e); //$NON-NLS-1$
		}		
	}

	/**
	 * Download a file.
	 * 
	 * @param fileName
	 * @param newContents
	 */
	public void downloadExtFile(String fileName, byte[] newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents);
		
		DownloadUtils.provideDownload(composite, downloadServiceHandler.getId());
	}
	
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	};
}

/**
 * Database label provider
 * @author sun.han
 *
 */
class PropertyInformLabelProvider extends LabelProvider implements ITableLabelProvider {
	private UserDBDAO userDB;
	
	public PropertyInformLabelProvider(UserDBDAO userDB) {
		this.userDB  = userDB;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map resultMap = (HashMap)element;
		String result;
		
		switch( columnIndex ) {
			case 0:
				result =  "" + resultMap.get("PROPERTY_NAME");
				break;
		
			case 1:
				result =  "" + resultMap.get("VALUE");
				break;
				
			default: 
				result =  "Invalid column index";
				break;
		} /* end of switch */
		
		return result;
	}
	
}

/**
 * Name filter
 * 
 * @author sun.han
 *
 */
class PropertyFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String str) {
		this.searchString = ".*" + str + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
		
		Map resultMap = (HashMap)element;
		String propertyName = ""; //$NON-NLS-1$
		if(resultMap.get("PROPERTY_NAME") != null) 
			propertyName = ""+resultMap.get("PROPERTY_NAME"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		else 
			propertyName = ""+resultMap.get("name"); //$NON-NLS-1$ //$NON-NLS-2$
		
		if(propertyName.matches(searchString)) return true;
		return false;
	}
	
}
