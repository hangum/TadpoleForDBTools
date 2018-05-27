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
package com.hangum.tadpole.importexport.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.importexport.core.messages"; //$NON-NLS-1$
	public String CsvToRDBImportDialog_0;
	public String CsvToRDBImportDialog_1;
	public String CsvToRDBImportDialog_10;
	public String CsvToRDBImportDialog_11;
	public String CsvToRDBImportDialog_14;
	public String CsvToRDBImportDialog_16;
	public String CsvToRDBImportDialog_19;
	public String CsvToRDBImportDialog_2;
	public String CsvToRDBImportDialog_21;
	public String CsvToRDBImportDialog_24;
	public String CsvToRDBImportDialog_26;
	public String CsvToRDBImportDialog_29;
	public String CsvToRDBImportDialog_3;
	public String CsvToRDBImportDialog_5;
	public String CsvToRDBImportDialog_51;
	public String CsvToRDBImportDialog_6;
	public String CsvToRDBImportDialog_7;
	public String MongoDBImportEditor_0;
	public String MongoDBImportEditor_1;
	public String MongoDBImportEditor_11;
	public String MongoDBImportEditor_12;
	public String MongoDBImportEditor_2;
	public String MongoDBImportEditor_5;
	public String MongoDBImportEditor_6;
	public String MongoDBImportEditor_8;
	public String QueryToMongoDBImport_2;
	public String QueryToMongoDBImport_5;
	public String TableColumnLIstComposite_1;
	public String SQLToDBImportDialog_0;
	public String SQLToDBImportDialog_1;
	public String SQLToDBImportDialog_lblSeprator_text;
	public String SQLToDBImportDialog_Ignore;
	public String SQLToDBImportDialog_ImportException;
	public String SQLToDBImportDialog_Insert;
	public String SQLToDBImportDialog_LoadException;
	public String SQLToDBImportDialog_LogEmpty;
	public String SQLToDBImportDialog_ReadError;
	public String SQLToDBImportDialog_SaveLog;
	public String SQLToDBImportDialog_StoreData;
	public String SQLToDBImportDialog_UploadQuestion;
	public String CsvToRDBImportDialog_btnCopyNew_text;
	public String CsvToRDBImportDialog_lblExistsData_text;
	public String CsvToRDBImportDialog_btnTruncate_text;
	public String CsvToRDBImportDialog_btnDeleteAll_text;
	public String CsvToRDBImportDialog_lblDisable_text;
	public String CsvToRDBImportDialog_btnTrigger_text;
	public String CsvToRDBImportDialog_btnPk_text;
	public String CsvToRDBImportDialog_btnFk_text;
	public String CsvToRDBImportDialog_btnDownloadSql_text;
	public String CsvToRDBImportDialog_btnSaveLog_text;
	public String CsvToRDBImportDialog_lblExecuteType_text;
	public String CsvToRDBImportDialog_btnInsert_text;
	public String CsvToRDBImportDialog_btnUpdate_text;
	public String CsvToRDBImportDialog_btnDelete_text;
	public String CsvToRDBImportDialog_lblBatchSize_text;
	public String CsvToRDBImportDialog_lblException_text;
	public String CsvToRDBImportDialog_btnIgnore_text;
	public String CsvToRDBImportDialog_btnStop_text;
	public String CsvToRDBImportDialog_btnIgnore_toolTipText;
	public String CsvToRDBImportDialog_btnStop_toolTipText;
	public String CsvToRDBImportDialog_btnRadioButton_text;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
