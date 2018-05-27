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
package com.hangum.tadpole.rdb.core;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.rdb.core.messages"; //$NON-NLS-1$

	public String doNotConnectionDB;

	public String format;
	public String AddTable;

	public String ViewQuery;
	public String ResourceHistoryDialog_0;
	
	public String CreatTime;
	public String Compare;
	public String Sec;
	public String AbstractLoginComposite_3;
	public String AbstractLoginComposite_4;
	public String Trigger;
	public String Event;
	public String Database;
	public String Collation;
	public String Definer;
	public String Modified;
	public String Created;
	public String AbstractObjectComposite_16;
	public String Comment;
	public String AbstractObjectComposite_18;
	public String AbstractObjectComposite_19;
	public String Table;
	public String TableName;
	public String IndexName;
	public String Type;
	public String Field;
	public String Key;
	public String Statement;
	public String Null;
	public String Default;
	public String Extra;
	public String Timing;
	public String AbstractObjectComposite_6;
	public String AbstractObjectComposite_8;
	public String AbstractObjectComposite_9;
	public String AbstractQueryAction_1;
	public String AWSRDSLoginComposite_0;
	public String AWSRDSLoginComposite_1;
	public String DisplayName;
	public String AWSRDSLoginComposite_11;
	public String IP;
	public String Port;
	public String Instance;
	public String Charset;
	public String User;
	public String AddDatabase;
	public String AWSRDSLoginComposite_20;
	public String AWSRDSLoginComposite_3;
	public String AssesKey;
	public String SecretKey;
	public String AWSRDSLoginComposite_6;
	public String AWSRDSLoginComposite_7;
	public String AWSRDSLoginComposite_8;
	public String TableColumnSelectionAction_1;
	public String TableEditPart_8;
	
	public String ConnectDatabaseAction_1;
	public String Detail;
	public String Host;
	public String DBLoginDialog_11;
	public String Password;
	public String DBLoginDialog_27;
	public String DBLoginDialog_42;
	public String DBLoginDialog_43;
	public String DBLoginDialog_44;
	public String DBLoginDialog_45;
	public String DBLoginDialog_47;
	public String DBLoginDialog_9;
	public String GenerateSQLSelectAction_0;
	public String GenerateSQLSelectAction_1;
	public String MainEditor_0;
	public String MainEditor_10;
	public String SQL;
	public String MainEditor_12;
	public String MainEditor_19;
	public String MainEditor_21;
	
	public String MainEditor_27;
	public String MainEditor_29;
	public String MainEditor_3;
	public String Rows;
	public String MainEditor_34;
	public String MainEditor_35;
	public String MainEditor_36;
	public String MainEditor_4;
	public String MainEditor_40;
	public String MainEditor_41;
	public String Commit;
	public String MainEditor_45;
	public String MainEditor_47;
	public String Rollback;
	public String MainEditor_51;
	public String MainEditor_7;
	public String PingTest;
	public String DeleteDBAction_1;
	public String DeleteDBAction_2;
	public String DeleteResourceAction_0;
	public String DeleteResourceAction_4;
	public String ERDAllTableViewAction_3;
	public String ERDDeleteAction_1;
	public String ERDDeleteAction_2;
	public String ExecuteProcedureDialog_0;
	public String Execute;
	public String ExecuteProcedureDialog_8;
	public String Exit;
	public String ExitAction_3;
	public String ExitAction_4;
	public String ExitAction_5;
	public String Logout;
	public String ObjectExplorer;
	public String ExplorerViewer_1;
	public String ExplorerViewer_29;
	public String ExplorerViewer_39;
	public String ExplorerViewer_61;
	public String ExplorerViewer_71;
	public String ExplorerViewer_76;
	public String ExplorerViewer_81;
	public String ExplorerViewer_86;
	public String ExtensionBrowserAction_1;
	public String ExtensionBrowserAction_3;
	public String ExtensionBrowserURLDialog_0;
	public String List;
	
	public String IsUse;
	public String URL;
	public String GenerateSQLDeleteAction_0;
	public String GenerateSQLInsertAction_0;
	public String GenerateSQLUpdateAction_13;
	public String TableInformation;
	public String Alias;
	public String AllColumn;
	public String GenerateStatmentDMLDialog_15;
	public String GenerateStatmentDMLDialog_2;
	public String ColumnName;
	public String DataType;
	public String GrantCheckerUtils_0;
	
	public String ObjectDeleteAction_0;
	public String ObjectDeleteAction_1;
	public String ObjectDeleteAction_10;
	public String ObjectDeleteAction_16;
	public String ObjectDeleteAction_17;
	public String ObjectDeleteAction_18;
	public String ObjectDeleteAction_24;
	public String ObjectDeleteAction_25;
	public String ObjectDeleteAction_3;
	public String ObjectDeleteAction_30;
	public String ObjectDeleteAction_36;
	public String ObjectDeleteAction_4;
	public String ObjectDeleteAction_42;
	public String ObjectDeleteAction_9;
	public String ObjectMongodbReIndexAction_2;
	public String ObjectMongodbRenameAction_7;
	public String ObjectMongodbRenameAction_8;
	public String ERDView;
	public String EditSQL;
	public String QueryHistoryComposite_10;
	public String Result;
	public String Message;
	
	public String SQLiteLoginComposite_11;
	public String SQLiteLoginComposite_12;
	public String SQLiteLoginComposite_13;
	public String SQLiteLoginComposite_14;
	public String SQLiteLoginComposite_17;
	public String SQLiteLoginComposite_18;
	public String SQLiteLoginComposite_19;
	public String SQLiteLoginComposite_23;
	public String SQLiteLoginComposite_24;
	public String SQLiteLoginComposite_25;
	public String SQLiteLoginComposite_29;
	public String SQLiteLoginComposite_5;
	public String SQLiteLoginComposite_7;
	public String Functions;
	public String TadpoleFunctionComposite_1;
	public String TadpoleFunctionComposite_2;
	public String TadpoleFunctionComposite_4;
	public String TadpoleFunctionComposite_5;
	public String TadpoleFunctionComposite_6;
	public String Indexes;
	public String TadpoleIndexesComposite_1;
	public String TadpoleIndexesComposite_2;
	public String SEQ;
	public String Order;
	public String Collections;
	public String TadpoleMongoDBCollectionComposite_10;
	public String TadpoleMongoDBCollectionComposite_11;
	public String TadpoleMongoDBCollectionComposite_12;
	public String TadpoleMongoDBCollectionComposite_13;
	public String TadpoleMongoDBCollectionComposite_14;
	public String TadpoleMongoDBCollectionComposite_15;
	public String TadpoleMongoDBCollectionComposite_16;
	public String TadpoleMongoDBCollectionComposite_2;
	public String TadpoleMongoDBCollectionComposite_3;
	public String TadpoleMongoDBCollectionComposite_4;
	public String TadpoleMongoDBCollectionComposite_5;
	public String TadpoleMongoDBCollectionComposite_6;
	public String TadpoleMongoDBCollectionComposite_7;
	public String TadpoleMongoDBCollectionComposite_9;
	public String TadpoleMongoDBIndexesComposite_0;
	public String TadpoleMongoDBIndexesComposite_10;
	public String TadpoleMongoDBIndexesComposite_11;
	public String TadpoleMongoDBIndexesComposite_12;
	public String TadpoleMongoDBIndexesComposite_2;
	public String TadpoleMongoDBIndexesComposite_8;
	public String TadpoleMongoDBIndexesComposite_9;
	public String TadpoleMongoDBJavaScriptComposite_2;
	public String TadpoleMongoDBJavaScriptComposite_4;
	public String TadpoleMongoDBJavaScriptComposite_5;
	public String TadpoleMongoDBJavaScriptComposite_6;
	public String TadpoleMongoDBJavaScriptComposite_8;
	public String Package;
	public String TadpolePackageComposite_3;
	public String TadpolePackageComposite_4;
	public String ViewDDL;
	public String TadpolePackageComposite_7;
	public String TadpolePackageComposite_8;
	public String Procedures;
	public String TadpoleProcedureComposite_1;
	public String TadpoleProcedureComposite_3;
	public String TadpoleProcedureComposite_6;
	public String TadpoleProcedureComposite_7;
	public String Synonym;
	public String TadpoleSynonymComposite_11;
	public String TadpoleSynonymComposite_21;
	public String TadpoleSynonymComposite_23;
	public String TadpoleSynonymComposite_25;
	public String TadpoleSynonymComposite_5;
	public String TadpoleSynonymComposite_7;
	public String TadpoleTableComposite_0;
	public String TadpoleTableComposite_1;
	public String TadpoleTableComposite_10;
	public String TadpoleTableComposite_11;
	public String TadpoleTableComposite_12;
	public String TadpoleTableComposite_14;
	public String TadpoleTableComposite_18;
	public String TadpoleTableComposite_3;
	public String TadpoleTableComposite_8;
	public String Triggers;
	public String TadpoleTriggerComposite_1;
	public String TadpoleTriggerComposite_2;
	public String TadpoleTriggerComposite_5;
	public String Views;
	public String TadpoleViewerComposite_1;
	public String TadpoleViewerComposite_2;
	public String TadpoleViewerComposite_6;
	public String FileNameValidator_0;
	public String MySQLLoginComposite_2;
	public String MySQLLoginComposite_4;
	public String SQLToStringDialog_2;
	public String Language;
	public String SQLToStringDialog_4;
	public String MainEditorInput_0;
	public String ManagerViewer_0;
	public String ManagerViewer_10;
	public String ManagerViewer_4;
	public String MSSQLLoginComposite_10;
	public String MSSQLLoginComposite_8;
	public String TableEditPart_lblWhere_text;
	public String DataStatus;
	public String TableViewerEditPart_10;
	public String TableViewerEditPart_2;
	public String TbUtils_0;
	public String TbUtils_1;
	public String InputValue;
	public String TextViewerEditingSupport_2;
	public String MainEditor_tltmExecuteAll_text;
	public String CharacterSet;
	public String MainEditor_btnDetailView_text;
	public String GroupName;
	public String HomePage;
	public String MainEditor_tltmExecute_toolTipText_1;
	public String ModifyDBDialog_1;
	public String ModifyDBDialog_2;
	public String Variable;
	public String SQLToStringDialog_btnNewButton_text;
	public String OracleObjectCompileAction_11;
	public String OracleObjectCompileAction_12;
	public String OracleObjectCompileAction_13;
	public String OracleObjectCompileAction_5;
	public String SettingOtherInfo;
	public String ReadOnly;
	public String OthersConnectionRDBGroup_2;
	public String OthersConnectionRDBGroup_5;
	public String OthersConnectionRDBGroup_6;
	public String ServerSideJavaScriptEditor_tbtmEvalJavaScript_text_1;
	
	public String SessionListEditor_13;
	public String SessionListEditor_15;
	
	public String PID;
	public String Command;
	public String Time;
	public String State;
	public String SessionListEditor_3;
	public String SessionListEditor_4;
	public String Query;
	public String SessionListEditor_8;
	public String SessionListEditorInput_0;
	public String MongoDBLoginComposite_lblReplicaSet_text;
	public String MongoDBLoginComposite_lblExLocalhostlocalhost_text;
	
	public String MainSQLEditorAPIServiceDialog_0;
	public String Argument;
	public String MainSQLEditorAPIServiceDialog_2;
	public String MainSQLEditorAPIServiceDialog_3;
	public String MainSQLEditorAPIServiceDialog_7;
	
	public String SystemMessage;
	public String ErrorMessage;
	public String MessageComposite_4;
	public String MessageComposite_5;
	public String OperationType;
	public String ParameterDialog_0;
	public String ParameterDialog_1;
	public String ParameterDialog_2;
	public String ParameterDialog_4;
	public String PostgresLoginComposite_1;
	public String ConnectionInfo;
	public String Preference;
	public String MSSQLLoginComposite_preDBInfo_text;
	public String TableDataEditorAction_1;
	public String TableDirectEditorComposite_1;
	public String TableDirectEditorComposite_17;
	public String TableDirectEditorComposite_19;
	public String TableDirectEditorComposite_btnDdlSourceView_text;
	public String ShowTables;
	public String ObjectDeleteAction_synonym;
	public String ObjectDropAction_1;
	public String Compile;
	public String ObjectEditor_13;
	public String ObjectEditor_2;
	public String ObjectEditor_7;
	public String RDBDBInfosEditor_1;
	public String RDBDBInfosEditor_2;
	public String RDBDBInfosEditor_3;

	public String RDBDBInfosEditor_4; /* Property Summary */
	public String RDBInformationComposite_0;
	public String RDBInformationComposite_1;

	public String Value;

	public String RDBInformationComposite_17;
	public String JDBCURL;
	public String AutoCommit;
	public String Profile;
	public String TableDirectEditorComposite_lblOrderBy_text;
	public String DriverType;
	public String ExternalBrowser;
	public String QueryStop;
	public String SQLiteLoginComposite_btnFileUpload_text;
	public String SQLiteLoginComposite_btnCreationDb_text;
	public String SQLUpdateDialog_1;
	public String SQLUpdateDialog_2;
	public String SQLUpdateDialog_5;
	public String SQLUpdateDialog_8;
	public String Update;
	public String TableInformationEditor_1;
	public String TDBErroDialog_2;
	public String TDBInfoDialog_0;
	public String RecordViewDialog_0;
	public String RecordViewDialog_1;
	public String RecordViewDialog_2;
	public String RecordViewDialog_5;
	public String RecordViewDialog_7;
	public String QueryPlan;
	public String ResultSetComposite_0;
	public String ResultSetComposite_1;
	public String ResultSetComposite_10;
	public String Download;
	public String ResultSetComposite_16;
	public String ResultSetComposite_3;

	public String ResultSetComposite_6;
	public String ResultSetComposite_8;
	public String JDBCOptions;
	public String DBMSOutput;
	public String ObjectType;
	public String ObjectName;
	public String TablesComposite_btnCsvExport_text;
	public String ColumnsComposite_btnDownload_text;
	public String ResultSetComposite_btnColumnDetail_text;
	public String MessageComposite_lblGoogleSearch_1_text;
	public String ChangeRotation;
	public String DeleteColumn;
	public String TableColumnDeleteAction_2;
	public String TableColumnDeleteAction_3;
	public String ObjectRenameAction_0;
	public String ObjectRenameAction_2;
	public String AddColumn;
	public String ObjectRenameValidator_0;
	public String ModifyColumn;
	public String MySQLTableColumnDialog_0;
	public String MySQLTableColumnDialog_1;
	public String MySQLTableColumnDialog_2;
	public String MySQLTableColumnDialog_20;
	public String MySQLTableColumnDialog_23;
	public String MySQLTableColumnDialog_27;
	public String MySQLTableColumnDialog_29;
	public String MySQLTableColumnDialog_3;
	public String MySQLTableColumnDialog_31;
	public String ManagerViewer_Resources;
	public String Extensions;
	public String Schemas;
	public String ResourceDetailDialog_delete;
	public String ResourceDetailDialog_name_empty;
	public String SQLTemplateDialog_NameEmpty;
	public String SQLTemplateDialog_SQLEmpty;
	public String SQLTemplateView_del_equestion;
	public String SQLTemplateView_Person;
	public String SQLTemplateView_PUBLIC_Person;
	public String TadpoleTableComposite_Drivernotfound;
	public String TadpoleTableComposite_driverMsg;
	public String MainEditor_DoesnotSupport;

	public String SQLTemplateView_Addpublictemplate;
	public String SQLTemplate;
	public String JDBCDriverManager;
	public String JDBCDriverSetting_DriverList;
	public String JDBCDriverSetting_Path;
	public String JDBCDriverSetting_FileList;
	
	public String JDBCDriverSetting_JARUpload;
	
	public String DoYouWnatDownload;
	public String DatabaseInformation;
	public String DriverInformation;
	public String NotSupportDatabase;
	public String ExecuteQuery;
	public String Column;

	public String DBLoginDialog_13;
	public String DBLoginDialog_15;

	public String IsEnable;

	public String TestConnection;

	public String MySQLTableColumnDialog_25;

	public String AutoRecoverMsg;
	public String AutoRecoverMsg_mysql;

	public String CompileObjectEditorOpen;

	public String GenerateSampleData;

	public String MySQLTaableCreateDialog;

	public String TableCreationNameAlter;

	public String TableCreationWantToCreate;

	public String TableCreationError;

	public String TableEncoding;

	public String TableCollation;

	public String TableType;

	public String Overload;

	public String PleaseTableName;

	public String TadpoleTableComposite_Relation;

	public String DropSynonym;

	public String InputTableName;

	public String GenerateUpdateStatement;

	public String SelectWhereColumn;

	public String PleaseSelectWhereColumn;

	public String GroupDBSelected;

	public String PleaseEndedTransaction;

	public String Preview;

	public String ExportData;

	public String PreviewMsg;

	public String ResultSetDownloadDialog_notSelect;

	public String AbstractExportCompositeFileEmpty;

	public String AbstractExportCompositeEncoding;

	public String FileName;
	public String encoding;

	public String IncludeScheme;

	public String Record;

	public String Compact;

	public String ExportJSONCompositeSchemaMSG;

	public String ExportJSONCompositeRecordMSG;

	public String Separator;

	public String Tab;

	public String Comma;
	public String etc;

	public String IncludeHead;

	public String ExportTextCompositeEmptySeparator;
	
	public String ExportTextCompositeEmptySeparatorOne;
	public String ExportSQLComposite_TargetTable;
	public String DMLType;
	public String ForEachMatter;
	public String ExportSQLComposite_MergeMatchColumn;
	public String ExportSQLComposite_PleaseTargetInput;
	public String ExportSQLComposite_PleaseCommitCount;
	public String ExportSQLComposite_PleaseMergeMath;

	public String Columns;

	public String Constraints;

	public String jdbcdriver;

	public String DriverNotFound;

	public String DriverNotFoundMSG;

	public String DBLoginDialog_AddDBOverMsg;

	public String MainEditorServiceEnd;

	public String SendEditor;

	public String ISApiUse;

	public String ResourceManageEditor_27;

	public String ResourceManageEditor_30;

	public String ResourceManageEditor_32;
	
	/* for Property Summary */
	public String PropertyComposite_Name;
	public String PropertyComposite_Value;
	
	public String Menu_SettingsAndPreferences;

	public String ms;

	public String Delimiter;

	public String Header;

	public String SelectOthersDB;

	public String DeleteDriver;

	public String MainEditorServiceEndGoPay;

	public String HELP;

	public String CreateConstraints;

	public String DropConstraints;

	public String NotFountObject;

	public String DeleteConstraint;
	public String DoNotSupportObject;

	public String ObjectEditorCompileError;

	public String ShowProfileResult;

	public String DoNotShowProfileResult;

	public String WhetherProfile;

	public String SelectSearchObject;

	public String ExportSQLComposite_UpdateMsg;

	public String Pin;

	public String Unpin;

	public String Sequence;

	public String SequenceCreated;

	public String SequenceDelete;

	public String IsDropSequence;

	public String DBLinkCreated;

	public String DBLinkDrop;

	public String IsDropDBLink;

	public String ResultSetComposite_row_to_editor;

	public String ResultSetComposite_column_to_editor;

	public String PleaseSelectRowData;

	public String TableSpaceManager;
	public String RiseError;
	public String WorkHasCompleted;

	public String CreateJob;
	public String ChangeJob;
	public String RemoveJob;
	public String JobManager;
	public String CreateJobDialog_FirstStartDatetime;
	public String CreateJobDialog_specification;
	public String CreateJobDialog_NextIterationExecutionCycle;
	public String CreateJobDialog_IterationExecutionCycle;
	public String CreateJobDialog_analysis;
	public String CreateJobDialog_executedScript;
	public String EveryNight;
	public String Every7Days;
	public String Every30Days;
	public String EverySunday;
	public String Every6Morning;
	public String Every3Hours;
	public String EveryFristDayMonth;
	public String EveryFirstDayAm;
	public String CreateJobDialog_JobSelectMsg;
	public String CreateJobDialog_JobCompleted;
	public String CreateJobDialog_RegException;
	public String CreateJobDialog_DelException;
	public String CreateJobDialog_DoesnotDeleteTarget;
	public String CreateJobDialog_JobDeleted;

	public String CreateJava;
	public String ChangeJava;
	public String DropJava;
	public String Compilejava;
	public String doesnotSupportJavaObject;
	public String DeletedJavaObject;
	public String CreateOrChangedJavaObject;
	public String CreateOrChangedErrorJavaObject;
	public String DeletedErrorJavaObject;

	public String SessionListEditor_GreatThan10Sec;

	public String ObjectExecutionException;

	public String ClipboardDialog;

	public String ExecuteQueryAndClose;

	public String CantnotFoundTable;

	public String SelectObject;

	public String DBType;

	public String DBReplication;

	public String DBReadOnly;

	public String DownloadQueryResult;

	public String CheckDataAndRunQeury;

	public String AreYouModifyAllData;

	public String UpdateDeleteConfirmDialog_Message;

	public String UpdateDeleteConfirmDialog_PleaseSelect;

	public String UpdateDeleteConfirmDialog_findData;

	public String UpdateDeleteConfirmDialog_findDataOver;
	
	public String LEDGER_AFTER_LABEL;

	public String CheckSQLStatement;
	
	public String RowChangeCount;
	
	public String BatchErrorMsg;
	
	public String WarnEditorIsOpen;

	public String DoNotSupportDynamoDB;

	public String ConfirmCommit;

	public String ConfirmRollback;

	public String TextResultView;

	public String TadpoleHistoryHubDataLocation;

	public String NoResultNoFile;
	
	
	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
