package com.hangum.tadpole.manager.core.editor.auth;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 어드민, 메니저, DBA가 사용하는 사용자리스트 컴포짖.
 * 
 * @author hangum
 *
 */
public class UserListComposite extends Composite {
	private Text textSearch;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public UserListComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeHead = new Composite(composite, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setText("Refresh");
		
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setText("Add");
		
		ToolItem tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.setText("Modify");
		
		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");
		
		textSearch = new Text(compositeHead, SWT.BORDER);
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeBody = new Composite(composite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(compositeBody, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn = treeViewerColumn.getColumn();
		treeColumn.setWidth(130);
		treeColumn.setText("Group Name");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_1 = treeViewerColumn_1.getColumn();
		treeColumn_1.setWidth(100);
		treeColumn_1.setText("User");
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_2 = treeViewerColumn_2.getColumn();
		treeColumn_2.setWidth(200);
		treeColumn_2.setText("email");
		
		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_3 = treeViewerColumn_3.getColumn();
		treeColumn_3.setWidth(150);
		treeColumn_3.setText("Name");
		
		TreeViewerColumn treeViewerColumn_4 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_4 = treeViewerColumn_4.getColumn();
		treeColumn_4.setWidth(60);
		treeColumn_4.setText("Approval");
		
		TreeViewerColumn treeViewerColumn_5 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_5 = treeViewerColumn_5.getColumn();
		treeColumn_5.setWidth(60);
		treeColumn_5.setText("Delete");
		
		TreeViewerColumn treeViewerColumn_6 = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn treeColumn_6 = treeViewerColumn_6.getColumn();
		treeColumn_6.setWidth(120);
		treeColumn_6.setText("Create tiem");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
