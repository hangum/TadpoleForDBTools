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
package com.hangum.tadpole.mongodb.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.mongodb.core.messages"; //$NON-NLS-1$
	public String Filter;
	public String CollectionInformationComposite_2;
	public String MongoDBInfosEditor_0;
	public String MongoDBInfosEditor_1;
	public String MongoDBInfosEditor_2;
	public String MongoDBInfosEditor_3;
	public String MongoDBInfosEditor_4;
	public String MongodbResultComposite_10;
	public String MongodbResultComposite_12;
	public String MongodbResultComposite_13;
	public String MongodbResultComposite_14;
	public String MongodbResultComposite_15;
	public String MongodbResultComposite_16;
	public String MongodbResultComposite_17;
	public String Date;
	public String MongodbResultComposite_19;
	public String MongodbResultComposite_20;
	public String MongodbResultComposite_21;
	public String MongodbResultComposite_25;
	public String MongodbResultComposite_26;
	public String MongodbResultComposite_3;
	public String MongodbResultComposite_4;
	public String MongodbResultComposite_5;
	public String MongodbResultComposite_6;
	public String MongodbResultComposite_7;
	public String MongodbResultComposite_8;
	public String Confirm;
	public String MongoDBTableEditor_0;
	public String MongoDBTableEditor_1;
	public String MongoDBTableEditor_2;
	public String MongoDBTableEditor_6;
	public String MongoDBTableEditor_3;
	public String Search;
	public String NewCollectionDialog_0;
	public String NewCollectionDialog_1;
	public String NewCollectionDialog_10;
	public String NewCollectionDialog_2;
	public String NewCollectionDialog_5;
	public String NewCollectionDialog_8;
	public String NewDocumentDialog_0;
	public String NewDocumentDialog_1;
	public String Insert;
	public String Cancel;
	public String ShardingComposite_0;
	public String ShardingComposite_4;
	public String Message;
	public String Close;
	public String TreeViewerEditingSupport_3;
	public String TreeViewerEditingSupport_4;
	public String UserManagerDialog_0;
	public String UserManagerDialog_1;
	public String UserManagerDialog_11;
	public String UserManagerDialog_14;
	public String UserManagerDialog_17;
	public String UserManagerDialog_19;
	public String UserManagerDialog_2;
	public String UserManagerDialog_22;
	public String UserManagerDialog_3;
	public String Delete;
	public String UserManagerDialog_5;
	public String Input;
	public String Refresh;
	public String ViewSource;
	public String DeleteMsg;
	public String Error;
	public String Add;
	public String OK;
	public String Warning;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
