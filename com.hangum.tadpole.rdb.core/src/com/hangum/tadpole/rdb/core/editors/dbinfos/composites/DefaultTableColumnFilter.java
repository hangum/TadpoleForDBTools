package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;

public class DefaultTableColumnFilter extends ViewerFilter {
	protected String searchString;
	protected AbstractDAO dao = null;

	public void setSearchString(String s) {
		this.searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		} else {
			dao = (AbstractDAO) element;
			String targetString = "";
			for (TableColumn tc : ((TableViewer) viewer).getTable().getColumns()) {
				targetString = dao.getvalue((String) tc.getData("column")).toLowerCase();
				if (targetString.matches(searchString)) {
					return true;
				}
			}
		}
		return false;
	}
}
