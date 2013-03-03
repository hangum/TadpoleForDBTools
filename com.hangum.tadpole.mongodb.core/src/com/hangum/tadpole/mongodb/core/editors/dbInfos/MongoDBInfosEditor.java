/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.editors.dbInfos;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites.CollectionInformationComposite;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites.InstanceInformationComposite;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites.LockComposite;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites.ReplicaSetComposite;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites.ShardingComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.util.TadpoleWidgetUtils;
import com.mongodb.CommandResult;

/**
 * monogodb의 데이터 베이스 정보를 보여준다.
 * 
 * - collection list(Name, Count, Size, Storage, Index, AvgObj, Padding)
 * 	- field list
 * 
 * summary info
 * 
 * @author hangum
 *
 */
public class MongoDBInfosEditor extends EditorPart {
	public static final String ID = "com.hangum.tadpole.mongodb.core.editor.dbInfos"; //$NON-NLS-1$
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBInfosEditor.class);
	
	private UserDBDAO userDB;
	private CommandResult commandResult;
	
	InstanceInformationComposite compositeServerStatus;
	CollectionInformationComposite compositeCollectionSummary;

	public MongoDBInfosEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginHeight = 2;
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		try {
			commandResult = MongoDBQuery.serverStatusCommandResult(userDB);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);		
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		CTabItem tbtmServerStatus = new CTabItem(tabFolder, SWT.NONE);
		tbtmServerStatus.setText(Messages.MongoDBInfosEditor_0);
		
		compositeServerStatus = new InstanceInformationComposite(tabFolder, SWT.NONE, commandResult);
		tbtmServerStatus.setControl(compositeServerStatus);
		compositeServerStatus.setLayout(new GridLayout(2, false));
		new Label(compositeServerStatus, SWT.NONE);
		
		CTabItem tbtmCollectionSummary = new CTabItem(tabFolder, SWT.NONE);
		tbtmCollectionSummary.setText(Messages.MongoDBInfosEditor_1);
		
		compositeCollectionSummary = new CollectionInformationComposite(tabFolder, SWT.NONE);
		tbtmCollectionSummary.setControl(compositeCollectionSummary);
		GridLayout gl_compositeCollectionSummary = new GridLayout(1, false);
		gl_compositeCollectionSummary.verticalSpacing = 2;
		gl_compositeCollectionSummary.horizontalSpacing = 2;
		gl_compositeCollectionSummary.marginHeight = 2;
		gl_compositeCollectionSummary.marginWidth = 2;
		compositeCollectionSummary.setLayout(gl_compositeCollectionSummary);
		
		CTabItem tbtmLocaks = new CTabItem(tabFolder, SWT.NONE);
		tbtmLocaks.setText(Messages.MongoDBInfosEditor_2);
		
		LockComposite compositeLock = new LockComposite(tabFolder, SWT.NONE, commandResult);
		tbtmLocaks.setControl(compositeLock);
		compositeLock.setLayout(gl_compositeCollectionSummary);
		
		CTabItem tbtmReplicaInformation = new CTabItem(tabFolder, SWT.NONE);
		tbtmReplicaInformation.setText(Messages.MongoDBInfosEditor_3);		
		ReplicaSetComposite compositeReplicaSet = new ReplicaSetComposite(tabFolder, SWT.NONE, commandResult);
		tbtmReplicaInformation.setControl(compositeReplicaSet);
		compositeReplicaSet.setLayout(gl_compositeCollectionSummary);
		
		CTabItem tbtmShardingInformation = new CTabItem(tabFolder, SWT.NONE);
		tbtmShardingInformation.setText(Messages.MongoDBInfosEditor_4);
		
		ShardingComposite compositeSharding = new ShardingComposite(tabFolder, SWT.NONE, userDB);
		tbtmShardingInformation.setControl(compositeSharding);
		compositeSharding.setLayout(gl_compositeCollectionSummary);

		tabFolder.setSelection(1);
		initData();
	}
	
	/**
	 * 초기 데이터를 로드합니다.
	 */
	private void initData() {
		compositeServerStatus.initData();
		compositeCollectionSummary.initData(userDB);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		MongoDBInfosInput moInput = (MongoDBInfosInput)input;
		this.userDB = moInput.getUserDB();		
		setPartName(moInput.getName());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
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
