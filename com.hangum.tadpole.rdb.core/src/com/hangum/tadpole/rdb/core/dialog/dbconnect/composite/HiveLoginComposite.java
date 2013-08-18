package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;

public class HiveLoginComposite extends MySQLLoginComposite {
	
	public HiveLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample HIVE 0.1.0", DBDefine.HIVE_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
}
