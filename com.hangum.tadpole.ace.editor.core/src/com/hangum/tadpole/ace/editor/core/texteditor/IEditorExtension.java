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
package com.hangum.tadpole.ace.editor.core.texteditor;

/**
 * editor의 표준 인터페이스를 정의합니다.
 * 
 * @author hangum
 *
 */
public interface IEditorExtension {
	/** 개발디비 에디터 정의 */
	public static final String DEV_DB_URL = "ace-builds/tadpole-editor.html";//"orion/tadpole/editor/RDBEmbeddededitor.html"; //$NON-NLS-1$
	
	/** 운영디비 에디터 정의 */
	public static final String REAL_DB_URL = "ace-builds/tadpole-editor.html";//"orion/tadpole/editor/REAL_RDBEmbeddededitor.html"; //$NON-NLS-1$

//public static final String EDITOR_HTML = 	
//"<!--  \n"+
//"	http://ace.c9.io/#nav=embedding 의 guide를 참고로 합니다. \n"+
//"	ace editor를 올챙이 프로젝트로 사용하기 위한 메인 페이지 입니다. \n"+
//"	html text define. \n"+
//"	@author hangum \n"+
//" --> \n"+
//"<!DOCTYPE html> \n"+
//"<html lang='en'> \n"+
//"<head> \n"+
//"  <meta charset='UTF-8'> \n"+
//"  <meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1'> \n"+
//"  <title>Editor</title> \n"+
//"  <style type='text/css' media='screen'> \n"+
//"    body { \n"+
//"        overflow: hidden; \n"+
//"    } \n"+
//"    #editor {  \n"+
//"        margin: 0; \n"+
//"        position: absolute; \n"+
//"        top: 0; \n"+
//"        bottom: 0; \n"+
//"        left: 0; \n"+
//"        right: 0; \n"+
//"    } \n"+
//"    #statusBar { \n"+
//"        margin: 0; \n"+
//"        font-size : 12px; \n"+
//"        padding: 0; \n"+
//"        position: absolute; \n"+
//"        left: 0; \n"+
//"        right: 0; \n"+
//"        bottom: 0; \n"+
//"        height: 15px; \n"+
//"        background-color: rgb(245, 245, 245); \n"+
//"        color: gray; \n"+
//"    } \n"+
//"    .ace_status-indicator { \n"+
//"        color: gray; \n"+
//"        position: absolute;\n"+
//"        right: 0;\n"+
//"        border-left: 1px solid;\n"+
//"    }\n"+
//"  </style>\n"+
//"</head>\n"+
//"\n"+
//"	<body>\n"+
//"		<pre id='editor'><%=TADPOLE-INITIALIZE-TEXT%></pre>\n"+
//"		<div id='statusBar'></div>\n"+
//"		    \n"+
//"		<script src='src-noconflict/ace.js' type='text/javascript' charset='utf-8'></script>\n"+
//"		<script src='src-noconflict/ext-language_tools.js'></script>\n"+
//"		<script src='src-noconflict/ext-statusbar.js'></script>\n"+
//"		\n"+
//"		<script src='tadpole-extension.js' type='text/javascript' charset='utf-8'></script>\n"+
//"	</body>\n"+
//"\n"+
//"</html>";

}
