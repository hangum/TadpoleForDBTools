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
package com.hangum.tadpole.monitoring.core.editors.monitoring;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Tadpole Monitoring editor
 * 
 * @author hangum
 *
 */
public class MonitoringEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.monitoring";
	
	public MonitoringEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		
		Composite compositeMonitoring = new Composite(sashForm, SWT.NONE);
		
		Composite compositeQuery = new Composite(sashForm, SWT.NONE);

		sashForm.setWeights(new int[] {1, 1});
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		MonitoringEditorInput esqli = (MonitoringEditorInput) input;
		setPartName(esqli.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void setFocus() {
	}

}
