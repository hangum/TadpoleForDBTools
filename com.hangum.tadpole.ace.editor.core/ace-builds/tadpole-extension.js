/**
 * tadpole ace editor extension.
 * 
 *  ace example at https://github.com/ajaxorg/ace/wiki/Embedding-API
 */
var editorService = {
	/** initialize editor */
	initEditor : function(varExt){},
	/** set editor focus */
	setFocus : function() {},

//	/** define font */
//	SetFont : 0,
//	setFont : function(varFontName, varFontSize){},
	
	SetTabSize : 5,
	setTabSize : function(varTabSize) {},
	
	GET_ALL_TEXT : 10,
	getAllText : function() {},
	
	GET_SELECTED_TEXT : 11,
	getSelectedText : function(varDelimiter) {},
	
	/** insert text */
	insertText : function(varText) {},
	
	/** add text */
	addText : function(varText) {},
	
	/**
	 * 에디터 기존 내용을 지운 후에 새롭게 텍스트를 넣습니다.
	 * -예, 쿼리 포멧 기능
	 */
	reNewText : function(varText) {},
	
	/** help dialog */
	HELP_POPUP : 60,
	helpDialog : function() {},
	
	/** dirty chage event */
	DIRTY_CHANGED : 1,
		
	SAVE : 20,
	EXECUTE_QUERY 		: 25,
	EXECUTE_ALL_QUERY 	: 26,
	EXECUTE_PLAN  		: 30,
	EXECUTE_FORMAT		: 35
};

var editor;

/**
 * initialize editor
 */
{
	ace.require("ace/ext/language_tools");
	editor = ace.edit("editor");
	editor.setTheme("ace/theme/eclipse");
	editor.setShowPrintMargin(false);
	editor.setHighlightActiveLine(true);
	editor.resize();
	
	editor.getSession().setMode("ace/mode/sql");
	
	// auto completion
	editor.setOptions({
	    enableBasicAutocompletion: true,
	    enableSnippets: true
	});
	
	// bookmarker
	editor.getSession().setFoldStyle("markbegin");
};

/**
 * 에디터를 초기화 합니다. 
 */
editorService.initEditor = function(varExt) {
	// 확장자 지정.
	editor.getSession().setMode(varExt);
};

/**
 * set editor focue
 */
editorService.setFocus = function() {
	editor.focus();
};

/**
 * 에디터 dirty_change
 */
editor.getSession().on('change', function() {
	console.log('Editor change event');
	try {
		AceEditorBrowserHandler(editorService.DIRTY_CHANGED);
	} catch(e) {
		console.log(e);
	}
});

//==[ Define short key ]======================================================================================================================
/**
 *  execute
 */
editor.commands.addCommand({
    name: 'save',
    bindKey: {win: 'Ctrl-S',  mac: 'Command-S'},
    exec: function(editor) {
    	console.log("Save query");
    	
    	getAllTextJava(editorService.SAVE);
    },
    readOnly: false
});
/**
 *  execute
 */
editor.commands.addCommand({
    name: 'executeQuery',
    bindKey: {win: 'F5',  mac: 'F5'},
    exec: function(editor) {
    	console.log("execute query");
    	
    	getSelectedTextJava(editorService.EXECUTE_QUERY);
    },
    readOnly: false
});
/*
 * execute plan
 */
editor.commands.addCommand({
    name: 'executePlan',
    bindKey: {win: 'Ctrl-E',  mac: 'Command-E'},
    exec: function(editor) {
    	console.log("execute plan");
    	
    	getSelectedTextJava(editorService.EXECUTE_PLAN);
    },
    readOnly: false
});
/*
 * format 
 */
editor.commands.addCommand({
    name: 'format',
    bindKey: {win: 'Ctrl-Shift-F',  mac: 'Command-Shift-F'},
    exec: function(editor) {
    	console.log("format");
    	
    	getSelectedTextJava(editorService.EXECUTE_FORMAT);
    },
    readOnly: false
});
/*
 * 실행한 쿼리창으로 이동.
 */
editor.commands.addCommand({
    name: 'moveHistoryWindow',
    bindKey: {win: 'Ctrl-H',  mac: 'Command-H'},
    exec: function(editor) {
    	console.log("move to history page");
//    	editorService.getContent();
    },
    readOnly: false
});
/*
 * 블럭지정한 부분을 소문자로.
 */
editor.commands.addCommand({
    name: 'changeLowCase',
    bindKey: {win: 'Ctrl-Shift-Y',  mac: 'Command-Shift-Y'},
    exec: function(editor) {
    	console.log("Change Low Cage ");
    	editor.toLowerCase();
    },
    readOnly: false
});
/*
 * 블럭지정한 부분을 소문자로.
 */
editor.commands.addCommand({
    name: 'changeUpperCase',
    bindKey: {win: 'Ctrl-Shift-X',  mac: 'Command-Shift-X'},
    exec: function(editor) {
    	console.log("Change Upper case");
    	editor.toUpperCase();
    },
    readOnly: false
});
/*
 * 단축키 도움말창으로 이동.
 */
editor.commands.addCommand({
    name: 'helpDialog',
    bindKey: {win: 'Ctrl-Shift-L',  mac: 'Command-Shift-L'},
    exec: function(editor) {
    	console.log("Show shortcut dialog");
    	editorService.helpDialog();
    },
    readOnly: false
});
/*
 * 에디터 창 전체 지우기.
 */
editor.commands.addCommand({
    name: 'cleagePage',
    bindKey: {win: 'Ctrl-F7',  mac: 'Command-F7'},
    exec: function(editor) {
    	console.log("Clear page");
    	editor.setValue("");
    },
    readOnly: false
});
//==[ Define short key ]======================================================================================================================


//==[ Define method ]======================================================================================================================

/**
 * define font information
 * 
 * 폰트 종류와 사이즈가 변화가 있으면 에디터가 깨지는 현상이 있음. 
 */
editorService.setFont = function(varFontName, varFontSize) {
	console.log("##Define setFont(" + varFontName + ", " + varFontSize + ")");
	
	try {
		document.getElementById('editor').style.fontSize= varFontSize + 'px';
		document.getElementById('editor').style.fontFamily= varFontName;
	} catch(e) {
		console.log(e);
	}
};

/**
 * define tab size
 */
editorService.setTabSize = function(varTabSize) {
	console.log("##Define tab size (" + varTabSize + ")");
	
	try {
		editor.getSession().setTabSize(varTabSize);
	} catch(e) {
		console.log(e);
	}
} 

/**
 * getAllText
 */
editorService.getAllText = function() {
	console.log("######################## called getAllText() method ######################");
	var varEditorContent = editor.getValue();
	console.log("\t editor content value is ==> " + varEditorContent);
	
	return varEditorContent;
};
getAllTextJava = function() {
	AceEditorBrowserHandler(editorService.GET_ALL_TEXT, editorService.getAllText());
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
	console.log("####### called getQueryText() method demiliter is " + varDelimiter);
	
	var varEditorContent = editor.getValue();
//	console.log("\t editor content value is ==> " + varEditorContent);
	if("" == varEditorContent) return "";
	
	try {
		var varSelectionContent = editor.getSelectedText();
//		console.log("\t selection Content is ==> " + varSelectionContent + ".");
		if("" != varSelectionContent)  {
			return varSelectionContent;
		} else {
			
			// 행번호를 가져온다.
			var intRowCursornPostion = editor.getCursorPosition().row;
			var strReturnQuery = "";
			
			// 쿼리의 시작부분을 찾는다.
			var intStartLine = intRowCursornPostion-1;
			var arryPreQuery = new Array();
			var intArrayPostion = 0;
			for(i=intStartLine; i>=0; i--) {
				// console.log("\t find text row number : " + i);
				
				var startQueryLine = editor.session.getLine(i);
				var lastIndexOf = startQueryLine.lastIndexOf(varDelimiter);
				if(lastIndexOf != -1) {
					arryPreQuery[intArrayPostion] = startQueryLine.substring(lastIndexOf+1);
					console.log('\t\t==> 검색 쿼리.' + startQueryLine.substring(lastIndexOf+1));
					break;
				} else {
					console.log("\t not found text " + startQueryLine);
					arryPreQuery[intArrayPostion] = startQueryLine;
				}
				
				intArrayPostion++;
			}
			
			// 쿼리 앞 부분의 데이터를 처리합니다. 
			for(i=0; i<intArrayPostion; i++) {
				// 배열은 0부터 시작하므로 -1을 하였습니다.
				//
				// 쿼리의 배열을 역순으로 집어 넣어서 역순으로 가져오면서 쿼리를 만들어야 합니다. 
				// [0] select 
 				// [1]  * from test
				//
				strReturnQuery += arryPreQuery[ (intArrayPostion-i)-1 ];
			}
			
			// 라인 종료 지점에 종료 문자가 있다면  해당 쿼리가 종료인것이다.
			if(editor.session.getLine(intRowCursornPostion).indexOf(varDelimiter) >= 0) {
				console.log(" 하나의 쿼리는 ==>" + strReturnQuery);
				strReturnQuery += editor.session.getLine(intRowCursornPostion);
			// 쿼리의 종료 지점을 찾습니다.
			} else {
				for(i=intRowCursornPostion; i<editor.session.getLength(); i++) {
					var startQueryLine = editor.session.getLine(i);
					var lastIndexOf = startQueryLine.lastIndexOf(varDelimiter);
					if(lastIndexOf != -1) {
						strReturnQuery += startQueryLine.substring(0, lastIndexOf);
						break;
					} else {
						strReturnQuery += startQueryLine + "\n";
					}
				}
			}
			
			// 쿼리 내용이 없다면..
			if(strReturnQuery.trim() == '') {
				console.log('\t##[Caution] query not found, so add all editor text');
				strReturnQuery = varEditorContent;
			}
			console.log("===========>>>>>" + strReturnQuery);
			
			return strReturnQuery;
		}
	} catch(e) {
		console.log("******************* Rise exception **********************************************")
		console.log(e);
	}
};
getSelectedTextJava = function() {
	AceEditorBrowserHandler(editorService.GET_SELECTED_TEXT, editorService.getSelectedText(";"));
};


/**
 * insert text
 */
editorService.insertText = function(varText) {
	console.log("**insertText " + varText);
	
	editor.insert(varText);
	editor.focus();
};

editorService.addText = function(varText) {
	console.log("**addText " + varText);
	
	editor.navigateLineEnd();
	
	editor.insert("\n" + varText);
	editor.focus();
};

editorService.reNewText = function(varText) {
	console.log("**rwNewText " + varText);
	
	editor.setValue(varText);
}

/**
 * help dilaog
 */
editorService.helpDialog = function() {
	console.log("** called help dialog ");
	AceEditorBrowserHandler(editorService.HELP_POPUP);
};