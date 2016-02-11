/**
 * 	tadpole ace editor extension.
 *  ace example : 
 *  	https://github.com/ajaxorg/ace/wiki/Embedding-API
 *  
 *  Default keyboard shortcuts : 
 *  	https://github.com/ajaxorg/ace/wiki/Default-Keyboard-Shortcuts
 *  
 *  Dynamically update syntax highlighting mode rules for the Ace Editor:
 *  	http://stackoverflow.com/questions/22166784/dynamically-update-syntax-highlighting-mode-rules-for-the-ace-editor
 *  
 *  @date 2015.05.
 *  @author hangum, hangum@gmail.com
 */
var editorService = {
	/** initialize editor */
	RDBinitEditor : function(varMode, varType, varTableList, varInitText, varAutoSave, varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter) {},
	MONGODBinitEditor : function(varMode, varInitText, varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter) {},
	/** change editor style */
	changeEditorStyle : function(varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter) {},
	
	/** set editor focus */
	setFocus : function() {},
	
	/** define theme */
	setTheme : function(varTheme) {},

	/** define fontsize */
	setFontSize : function(varFontSize) {},
	
	/** define wrap */
	setWrap : function(varBool, varLimit) {},
	
	/** 자바에서 저장했을때 호출 합니다 */
	saveData : function() {},

	setTabSize : function(varTabSize) {},
	getAllText : function() {},
	getSelectedText : function() {},
	setSelectedText : function() {},
	
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
	CONTENT_ASSIST		: "5",
		
	SAVE 				: "15",
	AUTO_SAVE			: "16",
	EXECUTE_QUERY 		: "25",
	EXECUTE_ALL_QUERY 	: "26",
	EXECUTE_PLAN  		: "30",
	EXECUTE_FORMAT		: "35",
	
	F4_DML_OPEN			: "40",
	GENERATE_SELECT		: "45"
};

//var editor;
/** 에디터가 저장 할 수 있는 상태인지 */
var isEdited = false;
/** 에디터에서 사용하는 쿼리 분리자 */
var varDelimiter = ";";
/** open 된 에디터 타입 */
var varEditorType = 'TABLES';
/** default key word list */
var default_keywordList = [];
/** auto save에서 사용하기 위해 마지막으로 호출한 값을 기록 */
var strLastContent;

/** initialize editor */
//{
	var langTools = ace.require("ace/ext/language_tools");
	var editor = ace.edit("editor");
	
	var StatusBar = ace.require('ace/ext/statusbar').StatusBar;
    var statusBar = new StatusBar(editor, document.getElementById('statusBar'));
    var EditSession = ace.require("ace/edit_session").EditSession;
	var UndoManager = ace.require("./undomanager").UndoManager;
	editor.resize(true)
	editor.setShowPrintMargin(true);
	editor.setHighlightActiveLine(true);
	
	editor.setOptions({
	    enableBasicAutocompletion: true,
	    enableSnippets: true,
	    enableLiveAutocompletion: false
	}); 
	
//};

/** 
 * 에디터를 초기화 합니다. 
 * @param varMode sql type(ex: sqlite, pgsql), EditorDefine#EXT_SQLite
 * @param varTableList table list
 * @param varType editorType (sql or procedure )
 * @param varInitText
 * @param varAutoSave
 * @param varTheme is editor theme
 * @param varFontSize  font size of editor
 * @param varIsWrap Wrap of editor
 * @param varWarpLimit Wrap limit of editor
 * @param varIsShowGutter Show gutter is editoe
 */
editorService.RDBinitEditor = function(varMode, varType, varTableList, varInitText, varAutoSave, varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter) {
	varEditorType = varType;
	
	var session = new EditSession(varInitText);
	strLastContent = varInitText;
	try {
		session.setUndoManager(new UndoManager());
		session.setMode(varMode);
		session.on('change', function() {
			if(!isEdited) {
				try {
					AceEditorBrowserHandler(editorService.DIRTY_CHANGED);
				} catch(e) {
					console.log(e);
				}
				isEdited = true;
			}
		});
		
		var tmpCtsList = varTableList.split("|");
		for(var i=0; i< tmpCtsList.length; i++) {
			default_keywordList.push({value: tmpCtsList[i], score: 0, meta: "Keyword"});
		}
	} catch(e) {
		console.log(e);
	}
	
	try{
		editor.setTheme("ace/theme/" + varTheme);
		editor.setFontSize(varFontSize + 'px');
		editor.renderer.setShowGutter(varIsShowGutter === 'true');
	} catch(e) {
		console.log(e);
	}
	try {
		var boolIsWrap = varIsWrap === 'true';
		session.setUseWrapMode(boolIsWrap);
		if(boolIsWrap) session.setWrapLimitRange(varWarpLimit, varWarpLimit);
	} catch(e) {
		console.log(e);
	}
	
	if(varAutoSave == 'true') autoSave();
	editor.setSession(session);
	editor.focus();
};
/**
 *  auto save
 */
autoSave = function() {
	var varAllTxt = editorService.getAllText();
	if(strLastContent != varAllTxt) {
		try {
			if('' != varAllTxt) {
				var boolDoSave = AceEditorBrowserHandler(editorService.AUTO_SAVE, varAllTxt);
				strLastContent = varAllTxt;
			}
		} catch(e) {
			console.log(e);
		}
	}
	
	window.setTimeout(autoSave, 30 * 1000);
}

/** 
 * 에디터를 초기화 합니다. 
 * @param varMode sql type(ex: sqlite, pgsql, javascript) EditorDefine#EXT_SQLite
 * @param varInitText
 */
editorService.MONGODBinitEditor = function(varMode, varInitText, varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter) {
	editorService.RDBinitEditor(varMode, 'NONE', '', varInitText, 'false', varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter);
};

/**
 * change editor style
 */
editorService.changeEditorStyle = function(varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter) {
	editor.setTheme("ace/theme/" + varTheme);
	editor.setFontSize(varFontSize + 'px');
	editor.renderer.setShowGutter(varIsShowGutter === 'true');
	
	var boolIsWrap = varIsWrap === 'true';
	session.setUseWrapMode(boolIsWrap);
	if(boolIsWrap) session.setWrapLimitRange(varWarpLimit, varWarpLimit);
	editor.setSession(session);
}

/**
 * 동적으로 키워드르 추가할 수 있는 모드
 */
ace.define("DynHighlightRules", [], function(require, exports, module) {
	var oop = require("ace/lib/oop");
	var TextHighlightRules = require("ace/mode/text_highlight_rules").TextHighlightRules;
	
	var DynHighlightRules = function() {
	   this.setKeywords = function(kwMap) {     
	       this.keywordRule.onMatch = this.createKeywordMapper(kwMap, "identifier");
	   }
	   this.keywordRule = {
	       regex : "\\w+",
	       onMatch : function() {return "text";}
	   }
	     
	   this.$rules = {
	        "start" : [ 
	            {
	                token: "string",
	                start: '"', 
	                end: '"',
	                next: [{ token : "constant.language.escape.lsl", regex : /\\[tn"\\]/}]
	            },
	            this.keywordRule
	        ]
	   };
	   this.normalizeRules();
	};
	
	oop.inherits(DynHighlightRules, TextHighlightRules);
	exports.DynHighlightRules = DynHighlightRules;
});

/**  자바에서 에디터가 저장되었을때 에디터 수정 메시지를 받기위해 호출되어 집니다. */
editorService.saveData = function() {
	isEdited = false;
}
/** set editor focus */
editorService.setFocus = function() {
	editor.focus();
};

//==[ Define short key ]======================================================================================================================
var shortcutErrMsg = 'Oops, an execution error has occured!\nEither click the "SQL" button of the tool bar, or open a new editor window.';
editor.commands.on("afterExec", function(e) { 
	if (e.command.name == "insertstring"&&/^[\\.\(.]$/.test(e.args)) {
		tdbContentAssist();
    }
}); 
editor.commands.addCommand({
    name: 'contentassist',
    bindKey: {win: 'Ctrl-Space',  mac: 'Ctrl-Space'},
    exec: function(editor) {
    	tdbContentAssist();
    },
    readOnly: false
});

/** content assist */
tdbContentAssist = function() {
	try {
		var arrySQL = caParsePartSQL();
		var newKeyword = AceEditorBrowserHandler(editorService.CONTENT_ASSIST, arrySQL[0], arrySQL[1]);
		var newCompletions = [];
		
		if("" != newKeyword) {
			var arryGroupKeyWord = newKeyword.split("||");
			for(var i=0; i<arryGroupKeyWord.length; i++) {
				var keyWord = arryGroupKeyWord[i].split("|");
				newCompletions.push({value: keyWord[0], score: 2, meta: keyWord[1]});
			}
		}
		
		// 마지막에 디폴트 키워드를 추가한다.
		Array.prototype.push.apply(newCompletions, default_keywordList);
		
		// setting default snippet
		editor.completers = [langTools.snippetCompleter];
		
		// 기존 content assist를 초기화한다.
		editor.completers.push({
			getCompletions: function(editor, session, pos, prefix, callback) {
				callback(null, newCompletions);
			}
		});
		editor.execCommand("startAutocomplete");
	}catch(e) {
		console.log(e);
	}
};

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
	if("" != editor.getSelectedText()) {
		return true;
	}
	
	return false;
}

editor.commands.addCommand({
    name: 'executeQuery',
    bindKey: {win: 'Ctrl-Enter',  mac: 'Command-Enter'},
    exec: function(editor) {
    	try {
    		var selectTxt = editorService.getSelectedText();
   			AceEditorBrowserHandler(editorService.EXECUTE_QUERY, selectTxt, editorService.isBlockText());
    	} catch(e) {
    		console.log(e);
    		alert(shortcutErrMsg);
    	}
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'executeObjectViewer',
    bindKey: {win: 'F4',  mac: 'F4'},
    exec: function(editor) {
    	try {
    		var varSelectionContent = editor.getSelectedText();
    		if(varSelectionContent != "") {
    			AceEditorBrowserHandler(editorService.F4_DML_OPEN, varSelectionContent);
    		} else {
    			// 현재 행의 텍스트.
    			var startQueryLine = editor.session.getLine(editor.getCursorPosition().row);
    			if(startQueryLine != "") {
	    			// 공백 배열로 만들어  제일 마지막 텍스트를 가져온다. 
    				var strObjectName = parseCursorObject();
	    			AceEditorBrowserHandler(editorService.F4_DML_OPEN, strObjectName);
    			}
    		}
    	} catch(e) {
    		console.log(e);
    	}
    },
    readOnly: false
});
editor.commands.addCommand({
    name: 'executeTableSelect',
    bindKey: {win: 'Ctrl-I',  mac: 'Command-I'},
    exec: function(editor) {
    	try {
    		var varSelectionContent = editor.getSelectedText();
    		if(varSelectionContent != "") {
    			AceEditorBrowserHandler(editorService.GENERATE_SELECT, varSelectionContent);
    		} else {
    			// 현재 행의 텍스트.
    			var startQueryLine = editor.session.getLine(editor.getCursorPosition().row);
    			if(startQueryLine != "") {
	    			var strObjectName = parseCursorObject();
	    			AceEditorBrowserHandler(editorService.GENERATE_SELECT, strObjectName);
    			}
    		}
    	} catch(e) {
    		console.log(e);
    	}
    },
    readOnly: false
});
/**
 * parse cursor object
 * @returns
 */
parseCursorObject = function() {
	// 공백 배열로 만들어  제일 마지막 텍스트를 가져온다. 
	var startQueryLine = editor.session.getLine(editor.getCursorPosition().row);
	var strBeforeTxt = startQueryLine.substring(0, editor.getCursorPosition().column);
	var strArryBeforeTxt = strBeforeTxt.split(' ');

	// 공백 배열로 만들어 제일 처음 백스트를 가져온다.
	var strAfterTxt = startQueryLine.substring(editor.getCursorPosition().column);
	var strArryAfterTxt = strAfterTxt.split(' ');

	var strObjectName = strArryBeforeTxt[strArryBeforeTxt.length-1] + strArryAfterTxt[0];
	// 마지막 문자가 ; 라면 제거해준다.
	strObjectName = strObjectName.replace(varDelimiter, "");
	
	return strObjectName;
}
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

/** define tab size */
editorService.setTabSize = function(varTabSize) {
	editor.getSession().setTabSize(varTabSize);
};
/** getAllText */
editorService.getAllText = function() {
	return editor.getValue();
};
/** set seltected text */
editorService.setSelectedText = function() {
	// selected text
	var selectTxt = editorService.getSelectedText();
	var intQueryLine = editor.getCursorPosition().row;
	editor.gotoLine(intQueryLine-1);
	
	editor.find(selectTxt);
};
/**
 * 수행해야할 작업 목록을 가져옵니다.
 * 
 * 1. 선택된 블럭이 있다면 블럭의 쿼리를 가져옵니다.
 * 2. 구분자가 없다면 쿼리 전체를 가져옵니다.
 * 3. 구분자가 2개이상이라면 구분자를 기준으로 선택된 행의 구분자를 처리합니다.
 */
editorService.getSelectedText = function() {
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
			var arrySQL = parsePartSQL();
			return arrySQL[0];
		}
	} catch(e) {
		console.log(e);
	}
};

// starts with
function stringStartsWith (string, prefix) {
    return string.slice(0, prefix.length) == prefix;
}

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
	editor.setValue("");
	editor.insert(varText);
};
/**  help dilaog */
editorService.helpDialog = function() {
	try {
		AceEditorBrowserHandler(editorService.HELP_POPUP);
	} catch(e) {
		console.log(e);
	}
};

/** content assist parse part sql */
caParsePartSQL = function() {
	var varCursor = editor.getCursorPosition();
	var partQuery = findCursorSQL(varCursor.row, varCursor.column);
	
	return partQuery;
}

/** parse part sql */
parsePartSQL = function() {
	var varCursor = editor.getCursorPosition();
	var partQuery = findCursorSQL(varCursor.row, varCursor.column);
	
	// 만약에 쿼리를 발견하지 못했다면, 자신의 윗행으로 찾아 마지막 종료 문자의 쿼리를 찾습니다.
	if(partQuery[0].trim() == "") {
		var intDelimiterLineNumber = findPreviousLineText(varCursor.row);
		if(-1 !== intDelimiterLineNumber) {
			var searchLien = editor.session.getLine(intDelimiterLineNumber);
			partQuery = findCursorSQL(intDelimiterLineNumber, searchLien.length);
		}
	}
	
	return partQuery;
}

/**
 * 선택 행의 위쪽으로 분리자가 있는 행의 번호를 리턴합니다.
 * @param varLineNum
 */
findPreviousLineText = function(currentRow) {
	var startRow = -1
	while(currentRow > 0) {
 		var textTokens = editor.session.getTokens(currentRow).filter(function(t) {
 			return t.type === "text";
 		});
	
 		for(var i=0; i<textTokens.length; i++) {
 			if(stringStartsWith(textTokens[i].value, varDelimiter)) {
 				startRow = currentRow;
 				break;
 			}
 		}
 		if(startRow >= 0) break;
 			
 		currentRow--;
 	}
	return startRow;
}

/**
 * 
 * @param varRow
 * @param varColumn
 * @returns {String}
 */
findCursorSQL = function(varRow, varColumn) {
    var maxRow = editor.session.getLength();
 	var startRow = -1, endRow = -1;
 	var realCursorPosition = 0;
 	
//	console.log("[editor current]" + varRow + ": " + varColumn);
 	
	//////////////////////////////////////////////////////
	/// 쿼리의 시작과 끝 부분을 계산한다. ////////////////////////
 	//////////////////////////////////////////////////////
 	var currentRow = varRow-1;
 	while(currentRow > 0) {
 		var textTokens = editor.session.getTokens(currentRow).filter(function(t) {
// 			console.log("=[check out tocken]==> [" + t.type + "]:[" + t.value + "]");
 			return t.type === "text";
 		});
	
 		for(var i=0; i<textTokens.length; i++) {
 			if(findDelimiterText(textTokens[i].value)) {
 				startRow = currentRow;
 				break;
 			}
 		}
 		if(startRow >= 0) break;
 		currentRow--;
 	}
 	if(startRow == -1) startRow = 0;

 	// 마지막 분리자(;)까지의 데이터를 찾는다.
 	currentRow = varRow;
 	while(currentRow < maxRow) {
 		var textTokens = editor.session.getTokens(currentRow).filter(function(t) {
 			return t.type === "text";
 		});

 		for(var i=0; i<textTokens.length; i++) {
 			if(findDelimiterText(textTokens[i].value)) {
 				endRow = currentRow;
 				break;
 			}
 		}
 		if(endRow >= 0) break;
 		currentRow++;
 	}
 	if(endRow == -1) endRow = maxRow;
 	
	//////////////////////////////////////////////////////
	/// 쿼리를 만든다. //////////////////////////////////////
 	//////////////////////////////////////////////////////
	
 	// 처음 행을 가져온다.
 	var firstLineQuery = "";
	var isStartDelemiter = false;
 	var tokens = editor.session.getTokens(startRow);
 	for(var i=0; i<tokens.length; i++) {
 		var token = tokens[i];

 		if(token.type === "text" && findDelimiterText(token.value)) {
			isStartDelemiter = true;
 		} else if(isStartDelemiter) {
 			firstLineQuery += token.value;
 		}
 	}
 	// 처음 행에 분리자가 없는 경우(즉 모든 행 전체가 쿼리인경우)
 	if(isStartDelemiter == false && firstLineQuery == "") {
 		firstLineQuery = editor.session.getLine(startRow) + "\n";
 	// 처음 줄이 ;문자만 있을 경우.
 	} else if(isStartDelemiter == true && firstLineQuery == "") {
 		firstLineQuery = "\n";
 	}

 	// 다음행부터 마지막 행까지 가져온다.
 	var middleQuery = "";
 	for(var start = startRow+1; start<endRow; start++) {
 		middleQuery += editor.session.getLine(start) + "\n";
 	}

 	// 마지막 행을 가져
	var lastLineQuery = "";
	var tokens = editor.session.getTokens(endRow);
 	for(var i=0; i<tokens.length; i++) {
 		var token = tokens[i];

 		if(token.type === "text" && findDelimiterText(token.value)) {
			break;
 		} else {
 			lastLineQuery += token.value;
 		}
 	}
	var fullyQuery = firstLineQuery + middleQuery + lastLineQuery + " ";
//	console.log("[fully query][" + firstLineQuery + "][" + middleQuery + "][" + lastLineQuery + "]");

	//////////////////////////////////////////////////////
	/// 쿼리 중에 커서의 위치를 계산한다. ////////////////////////
 	//////////////////////////////////////////////////////
	var realCurrentLine = varRow - startRow;
//	console.log("=0==> realCurrentLine : " + realCurrentLine);
	var arryQuery = fullyQuery.split("\n");
	// 라인 숫자도 포함 시킨다.
	var realCursorPosition = realCurrentLine;
	for(var i=0; i < realCurrentLine; i++) {
//		console.log("=1==> before cursor text is : " + arryQuery[i] + ":" + arryQuery[i].length);
		realCursorPosition += arryQuery[i].length;
	}
//	console.log("==2=> before cursor text is : " + varColumn);
	realCursorPosition += varColumn;//(arryQuery[realCurrentLine].substring(0, varColumn)).length;
//	console.log("[cursor position]" + realCursorPosition);
	
	//////////////////////////////////////////////////////
	/// 결과리턴 ////////////////////////
 	//////////////////////////////////////////////////////
	var arryReturnSQL = [];
	arryReturnSQL.push(fullyQuery);
	arryReturnSQL.push(realCursorPosition);
	return arryReturnSQL;
}

/**
 * 만약에 텍스트에 분리자(;)가 포함되어 있다면 true를 넘긴다.
 * @param currentTxt
 * @returns {Boolean}
 */
findDelimiterText = function(currentTxt) {
	if(currentTxt.indexOf(varDelimiter) > -1) return true;
	else return false;
}