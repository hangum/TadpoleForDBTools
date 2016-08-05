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
package com.hangum.tadpole.rdb.core.dialog.job;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJobDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.executer.procedure.ProcedureExecutor;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

/**
 * procedure 실행 다이얼로그.
 * 
 * @author hangum
 * 
 */
public class CreateJobDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	protected int ID_CREATE_JOB = IDialogConstants.CLIENT_ID + 1;
	protected int ID_CHANGE_JOB = IDialogConstants.CLIENT_ID + 2;
	protected int ID_DROP_JOB = IDialogConstants.CLIENT_ID + 3;
	private static final Logger logger = Logger.getLogger(CreateJobDialog.class);
	private ProcedureExecutor procedureExecutor;

	private Shell parentShell;
	private UserDBDAO userDB;
	private OracleJobDAO jobDao;

	private List<ProcedureFunctionDAO> showObjects;

	private List<InOutParameterDAO> parameterList = new ArrayList<InOutParameterDAO>();

	private Button btnCreateJob;
	private Button btnSpecify;
	private Button btnNext;
	private Button btnParse;
	private Button btnNoParse;
	private Button btnDropJob;
	private Combo comboRepeat;
	private Combo comboObject;
	private Combo comboSubObject;
	private Combo comboType;
	private Text textScript;
	private Group grpTables;

	private DateTime dateStartDate;
	private DateTime dateStartTime;
	private Text textPreview;
	private Label label_1;
	private Text textRepeat;
	private Label lblNewLabel;
	private Label lblJobId;
	private Text textJob;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param userDB
	 * @param jobDao
	 */
	public CreateJobDialog(Shell parentShell, UserDBDAO userDB, OracleJobDAO jobDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

		this.parentShell = parentShell;
		this.userDB = userDB;
		this.jobDao = jobDao;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Job Manager");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite containerInput = (Composite) super.createDialogArea(parent);
		GridLayout gl_containerInput = (GridLayout) containerInput.getLayout();
		gl_containerInput.verticalSpacing = 1;
		gl_containerInput.horizontalSpacing = 1;
		gl_containerInput.marginHeight = 1;
		gl_containerInput.marginWidth = 1;

		Composite compositeHead = new Composite(containerInput, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));

		lblJobId = new Label(compositeHead, SWT.NONE);
		lblJobId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJobId.setText("Job ID");

		textJob = new Text(compositeHead, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
		GridData gd_textJob = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textJob.widthHint = 100;
		textJob.setLayoutData(gd_textJob);
		textJob.setText(this.jobDao.getJob() + "");

		Label lblObjectType = new Label(compositeHead, SWT.NONE);
		lblObjectType.setText("최초 시작일시");

		Composite composite = new Composite(compositeHead, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		btnSpecify = new Button(composite, SWT.RADIO);
		btnSpecify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 최초 실행일시를 직접 지정하도록 선택했으면...
				dateStartDate.setEnabled(true);
				dateStartTime.setEnabled(true);

				Calendar c = Calendar.getInstance();
				dateStartDate.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dateStartTime.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dateStartTime.setTime(c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));

				createScript();
			}
		});
		btnSpecify.setText("직접지정");

		btnNext = new Button(composite, SWT.RADIO);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateStartDate.setEnabled(false);
				dateStartTime.setEnabled(false);
				createScript();
			}
		});
		btnNext.setText("다음 반복 실행주기");

		if (this.jobDao.getJob() > 0) {
			btnNext.setSelection(true);
		} else {
			btnSpecify.setSelection(true);
		}

		dateStartDate = new DateTime(composite, SWT.BORDER);
		dateStartDate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				createScript();
			}
		});
		GridData gd_dateStartDate = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_dateStartDate.widthHint = 120;
		dateStartDate.setLayoutData(gd_dateStartDate);

		dateStartTime = new DateTime(composite, SWT.BORDER | SWT.TIME);
		dateStartTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				createScript();
			}
		});
		GridData gd_dateStartTime = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_dateStartTime.widthHint = 120;
		dateStartTime.setLayoutData(gd_dateStartTime);
		dateStartTime.setSize(104, 27);

		Label lblObjectName = new Label(compositeHead, SWT.NONE);
		lblObjectName.setText("반복실행 주기");

		Composite composite_3 = new Composite(compositeHead, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));

		comboRepeat = new Combo(composite_3, SWT.NONE);
		comboRepeat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setRepeatString();
				createScript();
			}
		});
		comboRepeat.setItems(new String[] { "매일 저녁 자정에", "7일마다 자정에", "30일마다 자정에", "일요일 마다 자정에", "매일 오전 6시에", "3시간에 한번씩", "매달 1일 자정에", "매달 1일 오전 6시 30분에" });
		GridData gd_comboRepeat = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboRepeat.widthHint = 250;
		comboRepeat.setLayoutData(gd_comboRepeat);

		textRepeat = new Text(composite_3, SWT.BORDER);
		textRepeat.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				createScript();
			}
		});
		GridData gd_textRepeat = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textRepeat.minimumWidth = 200;
		gd_textRepeat.widthHint = 300;
		textRepeat.setLayoutData(gd_textRepeat);

		if (this.jobDao.getJob() <= 0) {
			comboRepeat.select(0);
			this.setRepeatString();
		} else {
			textRepeat.setText(this.jobDao.getInterval());
		}

		Label lblParsing = new Label(compositeHead, SWT.NONE);
		lblParsing.setText("구문분석");

		Composite composite_2 = new Composite(compositeHead, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));

		btnParse = new Button(composite_2, SWT.RADIO);
		btnParse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createScript();
			}
		});

		GridData gd_btnParse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnParse.widthHint = 120;
		btnParse.setLayoutData(gd_btnParse);
		btnParse.setText("Parse");

		btnNoParse = new Button(composite_2, SWT.RADIO);
		btnNoParse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createScript();
			}
		});
		GridData gd_btnNoParse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNoParse.widthHint = 120;
		btnNoParse.setLayoutData(gd_btnNoParse);
		btnNoParse.setText("No Parse");

		btnParse.setSelection(true);

		// 새로 생성할때만 선택가능하다. 수정중일때는 사용불가.
		composite_2.setEnabled(this.jobDao.getJob() <= 0);

		lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setText("실행작업");

		Composite composite_1 = new Composite(compositeHead, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(3, false));

		comboType = new Combo(composite_1, SWT.READ_ONLY);
		comboType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (StringUtils.startsWithIgnoreCase(PublicTadpoleDefine.OBJECT_TYPE.PACKAGES.name(), comboType.getText()) || StringUtils.startsWithIgnoreCase(PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES.name(), comboType.getText())) {
					initMainObject(comboType.getText());
				} else {
					//PL/SQL블럭 스크립트를 생성한다.
					textScript.setText("DBMS_OUTPUT.PUT_LINE('today is ' || to_char(sysdate)); ");
					createScript();
				}
			}
		});
		comboType.setItems(new String[] { "Procedure", "Package", "PL/SQL Block" });
		comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboType.select(0);

		comboObject = new Combo(composite_1, SWT.READ_ONLY);
		comboObject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (StringUtils.startsWithIgnoreCase(PublicTadpoleDefine.OBJECT_TYPE.PACKAGES.name(), comboType.getText())) {
					// 패키지 이면 패키지 목록(함수, 프로시져)를 로딩한다.
					initPackgeBodys(comboObject.getText());
				} else if (StringUtils.startsWithIgnoreCase(PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES.name(), comboType.getText())) {
					// 프로시져일때는 아규먼트 목록을 로딩한다.
					initParameters(comboObject.getSelectionIndex());
				}
				createScript();
			}
		});
		GridData gd_comboObject = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboObject.widthHint = 200;
		comboObject.setLayoutData(gd_comboObject);

		comboSubObject = new Combo(composite_1, SWT.READ_ONLY);
		comboSubObject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initParameters(comboSubObject.getSelectionIndex());
				createScript();
			}
		});
		comboSubObject.setEnabled(false);
		GridData gd_comboSubObject = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboSubObject.widthHint = 200;
		comboSubObject.setLayoutData(gd_comboSubObject);

		SashForm sashForm = new SashForm(containerInput, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		grpTables = new Group(sashForm, SWT.NONE);
		GridLayout gl_grpTables = new GridLayout(1, false);
		gl_grpTables.horizontalSpacing = 2;
		gl_grpTables.verticalSpacing = 2;
		gl_grpTables.marginHeight = 2;
		gl_grpTables.marginWidth = 2;
		grpTables.setLayout(gl_grpTables);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTables.setText("실행할 스크립트");

		textScript = new Text(grpTables, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textScript.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				createScript();
			}
		});
		textScript.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				createScript();
			}
		});
		GridData gd_textScript = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_textScript.heightHint = 100;
		gd_textScript.minimumHeight = 100;
		textScript.setLayoutData(gd_textScript);

		textScript.setText(this.jobDao.getWhat());

		label_1 = new Label(grpTables, SWT.NONE);
		label_1.setText("미리보기");

		textPreview = new Text(grpTables, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		initMainObject(this.comboType.getText());

		createScript();

		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return containerInput;
	}

	private void createScript() {
		this.textPreview.setText(StringUtils.EMPTY);

		StringBuffer script = new StringBuffer();

		if (this.jobDao.getJob() > 0) {
			script.append("BEGIN\n");
			script.append("SYS.DBMS_JOB.CHANGE(\n");
			script.append("	job => " + this.jobDao.getJob() + "\n");
		} else {
			script.append("DECLARE\n");
			script.append("	v_jobid int;\n");
			script.append("BEGIN\n");
			script.append("SYS.DBMS_JOB.SUBMIT(\n");
			script.append("	job => v_jobid\n");
		}
		script.append("	,what => '" + getScript() + "'\n");

		if (this.btnSpecify.getSelection()) {
			script.append("	,next_date => to_date('" + getStartDateString() + "', 'yyyy-mm-dd hh24:mi:ss')\n");
		} else {
			script.append("	,next_date => " + this.textRepeat.getText().trim() + "\n");
		}

		script.append("	,interval => '" + this.textRepeat.getText().trim() + "'\n");
		if (this.jobDao.getJob() <= 0) {
			script.append("	,no_parse => " + (this.btnNoParse.getSelection() ? "TRUE" : "FALSE") + "\n");
		}
		script.append(");\n");
		//script.append(":JobNumber := to_char(v_jobid);\n");
		script.append("END;\n");

		this.textPreview.setText(script.toString());
	}

	private String getStartDateString() {
		StringBuffer result = new StringBuffer();
		result.append(this.dateStartDate.getYear()).append("-");
		result.append(this.dateStartDate.getMonth() + 1 < 10 ? "0" + (this.dateStartDate.getMonth() + 1) : this.dateStartDate.getMonth() + 1).append("-");
		result.append(this.dateStartDate.getDay()).append(" ");

		result.append(this.dateStartTime.getHours()).append(":");
		result.append(this.dateStartTime.getMinutes()).append(":");
		result.append(this.dateStartTime.getSeconds());

		return result.toString();
	}

	private void setRepeatString() {
		String text = "";
		switch (this.comboRepeat.getSelectionIndex()) {
		case 0:
			text = "TRUNC(SYSDATE+1)";//"매일 저녁 자정에"
			break;
		case 1:
			text = "TRUNC(SYSDATE+7)";//"7일마다 자정에"
			break;
		case 2:
			text = "TRUNC(SYSDATE+30)";//"30일마다 자정에"
			break;
		case 3:
			text = "NEXT_DAY(TRUNC(SYSDATE), 'SUNDAY')";//"일요일 마다 자정에"
			break;
		case 4:
			text = "TRUNC(SYSDATE+1)+6/24";//"매일 오전 6시에"
			break;
		case 5:
			text = "SYSDATE+90/1440 ";//"3시간에 한번씩"
			break;
		case 6:
			text = "TRUNC(LAST_DAY(SYSDATE)) + 1";//"매달 1일 자정에"
			break;
		case 7:
			text = "TRUNC(LAST_DAY(SYSDATE)) + 1 + 6/24 + 30/1440";//"매달 1일 오전 6시 30분에"
			break;
		}
		this.textRepeat.setText(text);
	}

	private String getScript() {
		StringBuffer result = new StringBuffer();
		result.append(this.textScript.getText().trim());

		if (!StringUtils.endsWith(result.toString(), ";")) {
			result.append(";");
		}

		return result.toString().replace("'", "''");
	}

	private void initMainObject(String type) {

		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

			if (StringUtils.startsWithIgnoreCase(PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES.name(), type)) {
				comboSubObject.setEnabled(false);
				comboSubObject.removeAll();
				showObjects = sqlClient.queryForList("procedureList", userDB.getSchema()); //$NON-NLS-1$
			} else if (StringUtils.startsWithIgnoreCase(PublicTadpoleDefine.OBJECT_TYPE.PACKAGES.name(), type)) {
				showObjects = sqlClient.queryForList("packageList", userDB.getSchema()); //$NON-NLS-1$
			} else {
				comboSubObject.setEnabled(false);
				comboSubObject.removeAll();
				showObjects = new ArrayList<ProcedureFunctionDAO>();
			}

			comboObject.removeAll();
			for (ProcedureFunctionDAO dao : showObjects) {
				if (dao.getOverload() > 0) {
					comboObject.add(dao.getName() + ";" + dao.getOverload());
				} else {
					comboObject.add(dao.getName());
				}
			}
		} catch (TadpoleSQLManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initPackgeBodys(String packageName) {

		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("schema_name", userDB.getSchema());
			paramMap.put("package_name", packageName);

			showObjects = sqlClient.queryForList("packageBodyList", paramMap); //$NON-NLS-1$

			comboSubObject.setEnabled(true);
			comboSubObject.removeAll();

			for (int idx = showObjects.size() - 1; idx >= 0; idx--) {
				if (StringUtils.equalsIgnoreCase("FUNCTION", showObjects.get(idx).getType())) {
					showObjects.remove(idx);
				}
			}

			for (ProcedureFunctionDAO dao : showObjects) {
				if (StringUtils.equalsIgnoreCase("PROCEDURE", dao.getType())) {
					if (dao.getOverload() > 0) {
						comboSubObject.add(dao.getName() + ";" + dao.getOverload());
					} else {
						comboSubObject.add(dao.getName());
					}
				}

			}
		} catch (TadpoleSQLManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initParameters(int index) {

		try {
			ProcedureFunctionDAO dao = this.showObjects.get(index);
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("schema_name", dao.getSchema_name() == null ? userDB.getSchema() : dao.getSchema_name()); //$NON-NLS-1$
			map.put("package_name", dao.getPackagename());
			map.put("object_name", dao.getName());
			map.put("overload", dao.getOverload() + "");

			String strParam = "";
			List<InOutParameterDAO> inParamList = client.queryForList("getProcedureInParamter", map);
			for (InOutParameterDAO param : inParamList) {
				strParam += param.getName() + "/* " + param.getType() + ":" + param.getRdbType() + " */" + ",";
			}
			String strVariable = "";
			List<InOutParameterDAO> outParamList = client.queryForList("getProcedureOutParamter", map);
			for (InOutParameterDAO param : outParamList) {
				if (StringUtils.equals("OUT", param.getType())) {
					strVariable += param.getName() + " " + param.getRdbType() + "; ";
					strParam += param.getName() + "/* " + param.getType() + ":" + param.getRdbType() + " */" + ",";
				}
			}
			strParam = StringUtils.removeEnd(strParam.toString(), ",");

			if (!StringUtils.isBlank(strVariable)) {
				strVariable = "DECLARE " + strVariable;
			}

			String strWhat = strVariable + "BEGIN ";

			if (!StringUtils.isBlank(dao.getSchema_name())) {
				strWhat += SQLUtil.makeIdentifierName(userDB, dao.getSchema_name()) + ".";
			}
			strWhat += SQLUtil.makeIdentifierName(userDB, dao.getName()) + "(";
			strWhat += strParam;
			strWhat += "); ";
			strWhat += "END;";

			this.textScript.setText(strWhat);

		} catch (TadpoleSQLManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * initialize procedure IN information
	 */
	private List<InOutParameterDAO> getInParameter() throws Exception {
		return procedureExecutor.getInParameters();
	}

	private List<InOutParameterDAO> getOutParameters() throws Exception {
		return procedureExecutor.getOutParameters();
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (this.jobDao.getJob() > 0) {
			btnCreateJob = createButton(parent, ID_CREATE_JOB, "Change Job", false);
			btnDropJob = createButton(parent, ID_DROP_JOB, "Drop Job", false);
		} else {
			btnCreateJob = createButton(parent, ID_CREATE_JOB, "Create Job", false);
		}
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == ID_CREATE_JOB) {
			//Excute script
			RequestResultDAO reqReResultDAO = new RequestResultDAO();
			try {
				String stmt = this.textPreview.getText().trim();

				if (StringUtils.isBlank(this.textScript.getText().trim())) {
					MessageDialog.openInformation(this.getShell(), Messages.get().Information, "job에서 실행할 작업내용을 작성하십시오.");
					return;
				}
				stmt = StringUtils.removeEnd(stmt, ";") + ";";

				ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, this.textPreview.getText().trim());
			} catch (Exception e) {
				logger.error(e);
			} //$NON-NLS-1$
			if (PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqReResultDAO.getResult())) {
				MessageDialog.openError(this.parentShell, Messages.get().Error, "Job을 등록/변경 중 오류가 발생했습니다.\n" + reqReResultDAO.getMesssage() + reqReResultDAO.getException().getMessage());
			} else {
				MessageDialog.openInformation(this.parentShell, Messages.get().Information, "Job 등록 및 변경 작업이 완료 되었습니다.");
				this.okPressed();
			}
		} else if (buttonId == ID_DROP_JOB) {
			//Excute script
			RequestResultDAO reqReResultDAO = new RequestResultDAO();
			try {
				String drop_stmp = "";
				if (jobDao.getJob() <= 0) {
					// job_id가 없으면 ...에러.
					MessageDialog.openWarning(parentShell, Messages.get().Warning, "삭제 대상 job이 업습니다.");
					return;
				} else {
					drop_stmp = "begin sys.dbms_job.remove('" + jobDao.getJob() + "'); commit;end;";
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, drop_stmp);
				}
			} catch (Exception e) {
				logger.error(e);
			} //$NON-NLS-1$
			if (PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqReResultDAO.getResult())) {
				MessageDialog.openError(this.parentShell, Messages.get().Error, "Job을 삭제하는중 오류가 발생했습니다.\n" + reqReResultDAO.getMesssage() + reqReResultDAO.getException().getMessage());
			} else {
				MessageDialog.openInformation(this.parentShell, Messages.get().Information, "Job을 삭제하였습니다.");
				okPressed();
			}
		} else {
			okPressed();
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 700);
	}

}
