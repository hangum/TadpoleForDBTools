/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.mongodb.core.dto.MongoDBCollectionInfoDTO;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.MongoDBCollectionComparator;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBEditorInput;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBTableEditor;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.util.NumberFormatUtils;
import com.mongodb.CommandResult;
import com.mongodb.DB;

/**
 * collection information composite
 * 
 * @author hangum
 *
 */
public class CollectionInformationComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CollectionInformationComposite.class);
	
	private UserDBDAO userDB;
	
	private MongoInfoFilter filter;
	private MongoDBCollectionComparator collectionSorter;
	private Text textFilter;
	
	private TreeViewer treeViewerCollections;
	private List<MongoDBCollectionInfoDTO> collectionList = new ArrayList<MongoDBCollectionInfoDTO>();
	
	private Label lblCollection;
	private Label lblSizes ;
	private Label lblStorages;
	private Label lblIndex;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CollectionInformationComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);

		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setSize(590, 35);
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblName = new Label(compositeHead, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(Messages.CollectionInformationComposite_0);
		
		textFilter = new Text(compositeHead, SWT.BORDER);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textFilter.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				filter.setSearchString(textFilter.getText());
				treeViewerCollections.refresh();
			}
		});
		
		Button btnRefresh = new Button(compositeHead, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData(userDB);
			}
		});
		btnRefresh.setText(Messages.CollectionInformationComposite_1);
		
		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setSize(590, 199);
		compositeBody.setLayout(new GridLayout(1, false));
		
		treeViewerCollections = new TreeViewer(compositeBody, SWT.BORDER | SWT.VIRTUAL | SWT.FULL_SELECTION);
		treeViewerCollections.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				try {
					
					IStructuredSelection is = (IStructuredSelection)event.getSelection();
					Object selElement = is.getFirstElement();
					
					if(selElement instanceof MongoDBCollectionInfoDTO) {
						MongoDBCollectionInfoDTO dto = (MongoDBCollectionInfoDTO)selElement;					
						
						dto.setChild(MongoDBQuery.collectionColumn(userDB, dto.getName()));
						treeViewerCollections.refresh(dto, true);
						
						treeViewerCollections.expandToLevel(dto, 1);
					}
				} catch(Exception e) {
					logger.error("mongodb treeview refresh", e); //$NON-NLS-1$
				}
			}
		});
		treeViewerCollections.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object selElement = is.getFirstElement();
				
				if(selElement instanceof MongoDBCollectionInfoDTO) {
					MongoDBCollectionInfoDTO dto = (MongoDBCollectionInfoDTO)selElement;
					
					MongoDBEditorInput input = new MongoDBEditorInput(dto.getName(), userDB, dto.getChild());
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						page.openEditor(input, MongoDBTableEditor.ID);
					} catch (PartInitException e) {
						logger.error("Load the table data", e); //$NON-NLS-1$
						
						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(null, "Error", "An error has occurred.", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				
			}
		});
		Tree tree = treeViewerCollections.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeViewerCollections.setUseHashlookup(true);
		
		createTableColumn();
		
		treeViewerCollections.setContentProvider(new MongoInfoContentProvider());
		treeViewerCollections.setLabelProvider(new MongoInfoLabelProvider());
		treeViewerCollections.setInput(collectionList);

		collectionSorter = new MongoDBCollectionComparator();
		treeViewerCollections.setSorter(collectionSorter);
		
		filter = new MongoInfoFilter();
		treeViewerCollections.addFilter(filter);

		Group grpSummary = new Group(this, SWT.NONE);
		grpSummary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSummary.setSize(590, 45);
		grpSummary.setText(Messages.CollectionInformationComposite_2);
		grpSummary.setLayout(new GridLayout(4, false));
		
		lblCollection = new Label(grpSummary, SWT.BORDER);
		lblCollection.setText("Collection"); //$NON-NLS-1$
		
		lblSizes = new Label(grpSummary, SWT.BORDER);
		lblSizes.setText("Sizes"); //$NON-NLS-1$
		
		lblStorages = new Label(grpSummary, SWT.BORDER);
		lblStorages.setText("Storages"); //$NON-NLS-1$
		
		lblIndex = new Label(grpSummary, SWT.BORDER);
		lblIndex.setText("Index"); //$NON-NLS-1$
	}
	
	private void createTableColumn() {
		String[] columnName = {"Name", "Count", "Size", "Storage", "Index", "Last Extent Size", "AvgObj","Padding" };  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		int[] columnSize = {200, 100, 100, 100, 100, 100, 100, 100 };
		
		try {
			// reset column 
			for(int i=0; i<columnName.length; i++) {
				final int index = i;
				final TreeViewerColumn tableColumn = new TreeViewerColumn(treeViewerCollections, SWT.LEFT);
				tableColumn.getColumn().setText( columnName[i] );
				tableColumn.getColumn().setWidth( columnSize[i] );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
				
				tableColumn.getColumn().addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						collectionSorter.setColumn(index);
						
						treeViewerCollections.getTree().setSortDirection(treeViewerCollections.getTree().getSortDirection());
						treeViewerCollections.getTree().setSortColumn(tableColumn.getColumn());
						treeViewerCollections.refresh();
					}
				});
			}	// end for
			
		} catch(Exception e) { 
			logger.error("MongoDB Table Editor", e); //$NON-NLS-1$
		}		
	}

	/**
	 * 초기 데이터를 로드합니다.
	 */
	public void initData(UserDBDAO userDB) {
		if(this.userDB == null) this.userDB = userDB;
		collectionList.clear();
		
		try {
			DB mongoDB = MongoConnectionManager.getInstance(userDB);
			
			for (String col : mongoDB.getCollectionNames()) {
				
				CommandResult commandResult = mongoDB.getCollection(col).getStats();
				//logger.debug(commandResult);
				
				MongoDBCollectionInfoDTO info = new MongoDBCollectionInfoDTO();			
				info.setName(col);

				try {
					info.setCount(commandResult.getInt("count")); //$NON-NLS-1$
					info.setSize(commandResult.getInt("size")); //$NON-NLS-1$
					info.setStorage(commandResult.getInt("storageSize"));				 //$NON-NLS-1$
					info.setIndex(commandResult.getInt("totalIndexSize"));				 //$NON-NLS-1$
					try { 
						info.setAvgObj(commandResult.getDouble("avgObjSize"));				 //$NON-NLS-1$					
					} catch(NullPointerException npe) {
						info.setAvgObj(0);				 //$NON-NLS-1$
					}
					info.setPadding(commandResult.getInt("paddingFactor"));				 //$NON-NLS-1$
					
					try { 
						info.setLastExtentSize(commandResult.getDouble("lastExtentSize"));				 //$NON-NLS-1$					
					} catch(NullPointerException npe) {
						info.setLastExtentSize(0);				 //$NON-NLS-1$
					}
					
				} catch(Exception e) {
					logger.error("collection info error [" + col + "]", e); //$NON-NLS-1$ //$NON-NLS-2$
				}
				collectionList.add(info);
			}
			treeViewerCollections.setInput(collectionList);

			// summary 정보를 표시합니다.
			double dblSize = 0, dblStorage = 0, dblIndex = 0;
			for (MongoDBCollectionInfoDTO info : collectionList) {
				dblSize += info.getSize();
				dblStorage += info.getStorage();
				dblIndex += info.getIndex();
			}
			lblCollection.setText(collectionList.size() + " Collections");			 //$NON-NLS-1$
			lblSizes.setText("Size " + NumberFormatUtils.kbMbFormat(dblSize)); //$NON-NLS-1$
			lblStorages.setText("Storage " + NumberFormatUtils.kbMbFormat(dblStorage)); //$NON-NLS-1$
			lblIndex.setText("Index " + NumberFormatUtils.kbMbFormat(dblIndex)); //$NON-NLS-1$

		} catch (Exception e) {
			logger.error("mongodb collection infomtion init", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "MongoDB Information", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
/**
 * mongodb content provider
 * @author hangum
 *
 */
class MongoInfoContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List list = (List)inputElement;
		if(list.isEmpty()) return list.toArray();
		
		if(list.get(0) instanceof MongoDBCollectionInfoDTO) {
			return ((List<MongoDBCollectionInfoDTO>)inputElement).toArray();
		} else {
			return ((List<TableColumnDAO>)inputElement).toArray();
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof MongoDBCollectionInfoDTO) {
			MongoDBCollectionInfoDTO dto = (MongoDBCollectionInfoDTO) parentElement;
			return dto.getChild().toArray();
		} else if(parentElement instanceof CollectionFieldDAO) {
			CollectionFieldDAO dao = (CollectionFieldDAO) parentElement;
			return dao.getChildren().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof MongoDBCollectionInfoDTO) {
			MongoDBCollectionInfoDTO info = (MongoDBCollectionInfoDTO)element;
			
			return info.getChild().size() > 0;
		} else if(element instanceof CollectionFieldDAO) {
			CollectionFieldDAO dao = (CollectionFieldDAO)element;
			
			return dao.getChildren().size() > 0;
		}
		return false;
	}
	
}

/**
 * mongodb lable provider
 * @author hangum
 *
 */
class MongoInfoLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof MongoDBCollectionInfoDTO) {
			MongoDBCollectionInfoDTO info = (MongoDBCollectionInfoDTO)element;
			
			switch(columnIndex) {
			case 0: return info.getName();
			case 1: return NumberFormatUtils.commaFormat(info.getCount());
			case 2: return NumberFormatUtils.kbMbFormat(info.getSize());
			case 3: return NumberFormatUtils.kbMbFormat(info.getStorage());
			case 4: return NumberFormatUtils.kbMbFormat(info.getIndex());
			case 5: return NumberFormatUtils.commaFormat(info.getLastExtentSize());
			case 6: return NumberFormatUtils.commaFormat(info.getAvgObj());
			case 7: return NumberFormatUtils.commaFormat(info.getPadding());
			}
			return "*** not set column ***"; //$NON-NLS-1$
		} else {
			CollectionFieldDAO dao = (CollectionFieldDAO) element;
			
			switch(columnIndex) {
			case 0: return dao.getField();
			case 1: return dao.getType();
			case 2: return dao.getKey();
			case 3: return ""; //$NON-NLS-1$
			case 4: return ""; //$NON-NLS-1$
			case 5: return ""; //$NON-NLS-1$
			case 6: return ""; //$NON-NLS-1$
			case 7: return ""; //$NON-NLS-1$
			}			
			return "*** not set column ***"; //$NON-NLS-1$
		}
	}
	
}

/**
 * name filetr
 * 
 * @author hangum
 *
 */
class MongoInfoFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchString == null || searchString.length() == 0) {
			return true;
		}

		if(element instanceof MongoDBCollectionInfoDTO) {
			MongoDBCollectionInfoDTO user = (MongoDBCollectionInfoDTO)element;
			if(user.getName().matches(searchString)) return true;
		}
	
		return false;
	}
	
}