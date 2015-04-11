/**
 * tadpole ace editor extension.
 *  ace example at https://github.com/ajaxorg/ace/wiki/Embedding-API
 *  Default keyboard shortcuts : https://github.com/ajaxorg/ace/wiki/Default-Keyboard-Shortcuts 
 */
var editorService = {
	/** initialize editor */
	initEditor : function(varExt, varKeyword, varInitText) {},
	
	/** set editor focus */
	setFocus : function() {},

//	/** define font */
//	SetFont : 0,
//	setFont : function(varFontName, varFontSize){},
	
	/** 자바에서 저장했을때 호출 합니다 */
	saveData : function() {},
//	executeFlag : function() {},

	setTabSize : function(varTabSize) {},
	getAllText : function() {},
	getSelectedText : function(varDelimiter) {},
	
	/** insert text */
	insertText : function(varText) {},
	
	/** add text */
	addText : function(varText) {},
	
	/** is block text */
	isBlockText : function() {},
	
	/**
	 * 에디터 기존 내용을 지운 후에 새롭게 텍스트를 넣습니다.
	 * -예, 쿼리 포멧 기능
	 */
	reNewText : function(varText) {},
	
	/** help dialog */
	HELP_POPUP : "60",
	helpDialog : function() {},
	
	/** dirty chage event */
	DIRTY_CHANGED 		: "1",
		
	SAVE 				: "15",
	EXECUTE_QUERY 		: "25",
	EXECUTE_ALL_QUERY 	: "26",
	EXECUTE_PLAN  		: "30",
	EXECUTE_FORMAT		: "35"
};

var editor;
/** 에디터가 저장 할 수 있는 상태인지 */
var isEdited = false;
///** 자바에서 처리가 끝났는지 */
//var isJavaRunning = false;
/** open 된 에디터 타입 */
var varEditorType = 'TABLES';

// enable live auto completion
var completions = [];

/** initialize editor */
{
	var langTools = ace.require("ace/ext/language_tools");
	editor = ace.edit("editor");
	document.getElementById('editor').style.fontSize= '12px';
	
	var StatusBar = ace.require('ace/ext/statusbar').StatusBar;
    var statusBar = new StatusBar(editor, document.getElementById('statusBar'));
    
	editor.setTheme("ace/theme/eclipse");
	editor.setShowPrintMargin(true);
	editor.setHighlightActiveLine(true);
	
	/*
	 *  autocomplete
	 *  http://stackoverflow.com/questions/13545433/autocompletion-in-ace-editor 
	 */
	editor.setOptions({
	    enableBasicAutocompletion: true,
	    enableSnippets: true
//	    ,
//	    enableLiveAutocompletion: true
	});
	
//	var completer = {
//	        getCompletions: function(editor, session, pos, prefix, callback) {
//	        	var text = editor.getValue(); 
//	        	
//	        	if (prefix.length === 0) { 
//	        		callback(null, []); 
//	        		return 
//	        	} 
//	        	 
////	        	completions.push({ caption: "test", snippet: "test", meta: "table" });
//	        	callback(null, completions); 
//	        } 
//	} 
//	langTools.addCompleter(completer); 
};

/** 
 * 에디터를 초기화 합니다. 
 * @param varMode mode
 * @param varTableList table list
 * @param varType editorType (sql or procedure )
 * @param varInitText
 * 
 */
editorService.initEditor = function(varMode, varType, varTableList, varInitText) {
	varEditorType = varType;
	
	try {
		var tables = varTableList.split("|");
		for(var i=0; i<tables.length; i++) {
			completions.push({ caption: tables[i], snippet: tables[i], meta: "Table" });
		}
	} catch(e) {
		console.log(e);
	}
	
	try {
		var EditSession = ace.require("ace/edit_session").EditSession;
		var UndoManager = ace.require("./undomanager").UndoManager;

		var doc = new EditSession(varInitText);
		doc.setUndoManager(new UndoManager());
		doc.setMode(varMode);
		doc.on('change', function() {
			if(!isEdited) {
				try {
					AceEditorBrowserHandler(editorService.DIRTY_CHANGED);
				} catch(e) {
					console.log(e);
				}
				isEdited = true;
			}
		});
		
		editor.setSession(doc);
		editor.focus();
	} catch(e) {
		console.log(e);
	}
};

/**  자바에서 에디터가 저장되었을때 에디터 수정 메시지를 받기위해 호출되어 집니다. */
editorService.saveData = function() {
	isEdited = false;
}
/** set editor focus */
editorService.setFocus = function() {
	editor.focus();
};
//editorService.executeFlag = function() {
////	console.log('\t end java program....');
//	isJavaRunning = false;
//};

///** user autocomplete */
//editor.commands.on("afterExec", function(e){
////	console.log("--> command --> " + e.command.name);
//	
//	if (e.command.name == "insertstring"&&/^[\\.]$/.test(e.args)) {
//		var all = editor.completers;
//		editor.completers = completions;
//    	editor.execCommand("startAutocomplete");
//    	editor.completers = all;
//    	
////    } else if(e.command.name == "startAutocomplete") {
////    	var all = e.editor.completers;
////    	
////		var completer = {
////		        getCompletions: function(editor, session, pos, prefix, callback) {
////		        	var text = editor.getValue(); 
////		        	
////		        	if (prefix.length === 0) { 
////		        		callback(null, []); 
////		        		return 
////		        	} 
////		        	 
////	//	        	completions.push({ caption: "test", snippet: "test", meta: "table" });
////		        	callback(null, completions); 
////		        } 
////		} 
////		langTools.addCompleter(completer);
//	}
//})


//==[ Define short key ]======================================================================================================================
var shortcutErrMsg = 'Opps, an execution error has occured!\nEither click the "SQL" button of the tool bar, or open a new editor window.';
editor.commands.addCommand({
    name: 'save',
    bindKey: {win: 'Ctrl-S',  mac: 'Command-S'},
    exec: function(editor) {
    	try {
    		var boolDoSave = AceEditorBrowserHandler(editorService.SAVE, editorService.getAllText());
    		if(boolDoSave) editorService.saveData();
    	}catch(e) {
    		console.log(e);
    		alert(shortcutErrMsg);
    	}
    },
    readOnly: false
});

/**
 * editor 텍스트가 block인지 유무?
 * @returns {Boolean}
 */
editorService.isBlockText = function() {
	var isBlock = false;
	if("" != editor.getSelectedText())  isBlock = true;
	
	return isBlock;
}

editor.commands.addCommand({
    name: 'executeQuery',
    bindKey: {win: 'Ctrl-Enter',  mac: 'Command-Enter'},
    exec: function(editor) {
    	try {
   			AceEditorBrowserHandler(editorService.EXECUTE_QUERY, editorService.getSelectedText(";"), editorService.isBlockText());
    	} catch(e) {
    		console.log(e);
    		alert(shortcutErrMsg);
    	}
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'executePlan',
    bindKey: {win: 'Ctrl-E',  mac: 'Command-E'},
    exec: function(editor) {
    	try {
   			AceEditorBrowserHandler(editorService.EXECUTE_PLAN, editorService.getSelectedText(';'), editorService.isBlockText());
	    } catch(e) {
			console.log(e);
			alert(shortcutErrMsg);
		}
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'format',
    bindKey: {win: 'Ctrl-Shift-F',  mac: 'Command-Shift-F'},
    exec: function(editor) {
    	try {
    		var varFormat = AceEditorBrowserHandler(editorService.EXECUTE_FORMAT, editorService.getAllText());
    		editor.setValue(varFormat);
    	} catch(e) {
    		console.log(e);
    		alert(shortcutErrMsg);
    	}
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'changeLowCase',
    bindKey: {win: 'Ctrl-Shift-Y',  mac: 'Command-Shift-Y'},
    exec: function(editor) {
    	editor.toLowerCase();
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'changeUpperCase',
    bindKey: {win: 'Ctrl-Shift-X',  mac: 'Command-Shift-X'},
    exec: function(editor) {
    	editor.toUpperCase();
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'helpDialog',
    bindKey: {win: 'Ctrl-Shift-L',  mac: 'Command-Shift-L'},
    exec: function(editor) {
    	try {
    		editorService.helpDialog();
    	} catch(e) {
    		console.log(e);
    	}
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'cleagePage',
    bindKey: {win: 'Ctrl-F7',  mac: 'Command-F7'},
    exec: function(editor) {
    	editor.setValue("");
    },
    readOnly: false
});

/**
 * define font information
 * 폰트 종류와 사이즈가 변화가 있으면 에디터가 깨지는 현상이 있음. 
 */
editorService.setFont = function(varFontName, varFontSize) {
	try {
		document.getElementById('editor').style.fontSize= varFontSize + 'px';
		document.getElementById('editor').style.fontFamily= varFontName;
	} catch(e) {
		console.log(e);
	}
};
/** define tab size */
editorService.setTabSize = function(varTabSize) {
	editor.getSession().setTabSize(varTabSize);
};
/** getAllText */
editorService.getAllText = function() {
	return editor.getValue();
};
/**
 * 수행해야할 작업 목록을 가져옵니다.
 * 
 * 1. 선택된 블럭이 있다면 블럭의 쿼리를 가져옵니다.
 * 2. 구분자가 없다면 쿼리 전체를 가져옵니다.
 * 3. 구분자가 2개이상이라면 구분자를 기준으로 선택된 행의 구분자를 처리합니다.
 * 
 * @param varDelimiter 구분자.
 */
editorService.getSelectedText = function(varDelimiter) {
	var varEditorContent = editor.getValue();
	if("" == varEditorContent) return "";
	
	//
	// 프로시저 평선 트리거는 에디터 모두를 리턴합니다. 
	//
	if(varEditorType == "PROCEDURES" ||
		varEditorType == "FUNCTIONS" ||
		varEditorType == "TRIGGERS") {
		
		// 선택된 텍스트가 있다면 우선적으로 리턴합니다.
		var varSelectionContent = editor.getSelectedText();
		if("" != varSelectionContent)  {
			return varSelectionContent;
		} else {
			return varEditorContent;
		}
	} 
	
	//
	// 일반 에디터. 
	// 
	try {
		// 선택된 텍스트가 있다면 우선적으로 리턴합니다.
		var varSelectionContent = editor.getSelectedText();
		if("" != varSelectionContent)  {
			return varSelectionContent;
			
		// 선택된 텍스트가 없다면 구분자 만큼 넘겨줍니다.
		} else {
			var strReturnSQL = "";
			
			var startQueryLine = editor.session.getLine(editor.getCursorPosition().row);
			if(startQueryLine.lastIndexOf(varDelimiter) != -1) {
				// 선택된 행에 종료 문자가 있다면 
					// 행부터 윗 행으로 찾아가면서 종료 문자가 있는지 검사합니다.
					// 종료 문자를 찾지 못했다면 모든 행이 포함될 텍스트 이다.
//				console.log(" [1] 선택된 행에 종료 문자가 있다면 .................. ");
				
				//
				// 자신보다 한 행위의 쿼리를 읽어 들입니다.
				//
//				console.log("========== 선택된 행에 종료 문자가 있다면===");
				strReturnSQL = findPreviousChar((editor.getCursorPosition().row -1), varDelimiter);
//				console.log("[findPreviousSQL is " + strReturnSQL);

				strReturnSQL += startQueryLine.substring(0, startQueryLine.lastIndexOf(varDelimiter));
//				console.log("[fully SQL is " + strReturnSQL);
				
			} else {
//				console.log(" [2] 선택된 행에 종료 문자가 없다면 .................. ");
				// 선택된 행에 종료 문자가 없다면
				strReturnSQL = findPreviousChar((editor.getCursorPosition().row -1), varDelimiter);
//				console.log("[findPreviousSQL is " + strReturnSQL);
				
				strReturnSQL += startQueryLine + "\n";
				
				strReturnSQL += findNextCharacter((editor.getCursorPosition().row +1), varDelimiter);
//				console.log("[findNextSQL is " + strReturnSQL);
			}
			
			// 만약에 쿼리를 발견하지 못했다면, 자신의 윗행으로 찾아 마지막 종료 문자의 쿼리를 찾습니다.
			if(strReturnSQL.trim() == "") {
				var intDelimiterLineNumber = findPreviousLineText(editor.getCursorPosition().row, varDelimiter);
				if(-1 !== intDelimiterLineNumber) {
					strReturnSQL = findPreviousChar(intDelimiterLineNumber-1, varDelimiter);
//					console.log("[findPreviousSQL is " + strReturnSQL);
	
					startQueryLine = editor.session.getLine(intDelimiterLineNumber);
					strReturnSQL += startQueryLine.substring(0, startQueryLine.lastIndexOf(varDelimiter));
//					console.log("[fully SQL is " + strReturnSQL);
				}
			}

			return strReturnSQL;
		}
	} catch(e) {
		console.log(e);
	}
};

/**
 * 선택 행의 위쪽으로 분리자가 있는 행의 번호를 리턴합니다.
 * @param varLineNum
 * @param varDelimiter 
 */
findPreviousLineText = function(varLineNum, varDelimiter) {
	for(var i=varLineNum; i>=0; i--) {
		var startQueryLine = editor.session.getLine(i);
		var lastIndexOf = startQueryLine.lastIndexOf(varDelimiter);
		if(lastIndexOf != -1) {
			return i;
			break;
		}
	}
	return -1;
}

/**
 * 선택된 행에 종료 문자가 있다면 
 *		행부터 윗 행으로 찾아가면서 종료 문자가 있는지 검사합니다.
 *		종료 문자를 찾지 못했다면 모든 행이 포함될 텍스트 이다.
 * 
 * @param varLineNum
 * @param varDelimiter
 */
findPreviousChar = function(varLineNum, varDelimiter) {
	var strReturnQuery = "";
	
	var arryPreQuery = new Array();
	var intArrayPostion = 0;
	for(var i=varLineNum; i>=0; i--) {
		var startQueryLine = editor.session.getLine(i);
		var lastIndexOf = startQueryLine.lastIndexOf(varDelimiter);
		if(lastIndexOf != -1) {
			arryPreQuery[intArrayPostion] = startQueryLine.substring(lastIndexOf+1);
//			console.log('\t\t==> 검색 쿼리.' + arryPreQuery[intArrayPostion]);
			break;
		} else {
//			console.log("\t not found text " + startQueryLine);
			arryPreQuery[intArrayPostion] = startQueryLine;
		}
		
		intArrayPostion++;
	}
	
	for(i=0; i<arryPreQuery.length; i++) {
		// 배열은 0부터 시작하므로 -1을 하였습니다.
		//
		// 쿼리의 배열을 역순으로 집어 넣어서 역순으로 가져오면서 쿼리를 만들어야 합니다. 
		// [0] select 
			// [1]  * from test
		//
		strReturnQuery += arryPreQuery[ (arryPreQuery.length-i)-1 ] + "\n";
	}
	
	return strReturnQuery;
};

/**
 * 행부터 아래로 찾아가면서 종료 문자를 찾는다.
 * 	종료 문자가 없다면 아래의 모든 행이 포함될 텍스트 이다.
 * 
 * @param varLineNum
 * @param varDelimiter
 */
findNextCharacter = function(varLineNum, varDelimiter) {
	var strReturnQuery = "";
	
	var intRowCount = editor.session.getLength();
	
	var arryPreQuery = new Array();
	var intArrayPostion = 0;
	for(var i=varLineNum; i<intRowCount; i++) {
		var startQueryLine = editor.session.getLine(i);
		var lastIndexOf = startQueryLine.lastIndexOf(varDelimiter);
		if(lastIndexOf != -1) {
			arryPreQuery[intArrayPostion] = startQueryLine.substring(0, lastIndexOf+1);
//			console.log('\t\t==> 검색 쿼리.' + arryPreQuery[intArrayPostion]);
			break;
		} else {
//			console.log("\t not found text " + startQueryLine);
			arryPreQuery[intArrayPostion] = startQueryLine;
		}
		
		intArrayPostion++;
	}
	
	for(i=0; i<arryPreQuery.length; i++) {
		// 배열은 0부터 시작하므로 -1을 하였습니다.
		//
		// 쿼리의 배열을 역순으로 집어 넣어서 역순으로 가져오면서 쿼리를 만들어야 합니다. 
		// [0] select 
			// [1]  * from test
		//
		strReturnQuery += arryPreQuery[i] + "\n";
	}
	
	return strReturnQuery;
};

/**
 * insert text
 */
editorService.insertText = function(varText) {
	try {
		editor.insert(varText);
		editor.focus();
	} catch(e) {
		console.log(e);
	}
};
editorService.addText = function(varText) {
	try {
		if("" == editor.getValue()) {
			editor.insert(varText);
		} else {
			editor.gotoLine(editor.session.getLength()+1);
			editor.insert("\n" + varText);
		}
		editor.focus();
	} catch(e) {
		console.log(e);
	}
};
editorService.reNewText = function(varText) {
	editor.setValue(varText);
};
/**  help dilaog */
editorService.helpDialog = function() {
	try {
		AceEditorBrowserHandler(editorService.HELP_POPUP);
	} catch(e) {
		console.log(e);
	}

//	ace.config.loadModule("ace/ext/keybinding_menu", function(module) {
//        module.init(editor);
//        editor.showKeyboardShortcuts()
//    })
};