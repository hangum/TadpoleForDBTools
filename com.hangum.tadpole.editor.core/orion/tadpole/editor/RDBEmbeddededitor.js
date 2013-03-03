/*******************************************************************************
 * @license
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*global examples orion:true window define*/
/*jslint browser:true devel:true*/

var editorService = {
	DIRTY_CHANGED : 1,
	dirtyChanged: function(dirty) {}, // Called by the editor when its dirty state changes

	GET_CONTENT_NAME : 5,
	getContentName: function() {},		// Called to get the current content name.  A file name for example

	GET_INITIAL_CONTENT : 10,
	getInitialContent: function() {},	// Called to get the initial contents for the editor

	SAVE : 15,
	save: function(editor) {},			// Called to persist the contents of the editor
	
	SAVE_S : 16,
	saveS: function(editor) {},

	STATUS_CHANGED : 20,
	statusChanged: function(message, isError) {},	// Called by the editor to report status line changes
	
	EXECUTE_QUERY : 25,
	executeQuery: function(editor) {},
	
	EXECUTE_ALL_QUERY : 26,
	executeAllQuery: function(editor) {},
	
	EXECUTE_PLAN : 30,
	executePlan: function(editor) {},
	
	EXECUTE_FORMAT : 35,
	executeFormat: function(editor) {},
	
	APPEND_QUERY_TEXT : 40,
	appendQueryText: function(editor) {},
	
	RE_NEW_TEXT  : 41,
	reNewText: function(editor) {},
	
	SQL_TO_APPLICATION : 45,
	sqlToApplication: function(editor) {},
	
	DOWNLOAD_SQL : 50,
	downloadSQL: function(editor) {},
	
	MOVE_HISTORY_PAGE : 55,
	moveHistoryPage: function(editor) {},
	
	HELP_POPUP : 60,
	helpPopup: function(editor) {},
	
	SET_FOCUS : 999,
	setTextFocus: function(editor) {}
	
};

var editor;
var syntaxHighlighter;
var contentAssist;
var sqlContentAssistProvider;
//var jsContentAssistProvider;

function initEmbeddedEditor(){
//	console.log('set initEmbedded editor');
	
	define([
		"require", 
		"orion/textview/textView",
		"orion/textview/keyBinding",
		"tadpole/textview/textStyler",
//		"orion/editor/textMateStyler",
//		"orion/editor/htmlGrammar",
		"orion/editor/editor",
		"orion/editor/editorFeatures",
		"orion/editor/contentAssist",
//		"orion/editor/jsContentAssist",
		"orion/editor/sqlContentAssist"],
	
	function(require, mTextView, mKeyBinding, mTextStyler, /*mTextMateStyler, mHtmlGrammar,*/ mEditor, mEditorFeatures, mContentAssist, mSQLContentAssist){
		
		var editorDomNode = document.getElementById("editor");
		
		var textViewFactory = function() {
			return new mTextView.TextView({
				parent: editorDomNode,
				tabSize: 4
			});
		};
		
		var contentAssistFactory = {
			createContentAssistMode: function(editor) {
				contentAssist = new mContentAssist.ContentAssist(editor.getTextView());
				var contentAssistWidget = new mContentAssist.ContentAssistWidget(contentAssist, "contentassist");
				return new mContentAssist.ContentAssistMode(contentAssist, contentAssistWidget);
			}
		};
		sqlContentAssistProvider = new mSQLContentAssist.SQLContentAssistProvider();
		
		// Canned highlighters for js, java, and css. Grammar-based highlighter for html
		syntaxHighlighter = {
			styler: null, 
			
			highlight: function(fileName, editor) {
				if (this.styler) {
					this.styler.destroy();
					this.styler = null;
				}
				if (fileName) {
					var splits = fileName.split(".");
					var extension = splits.pop().toLowerCase();
					var textView = editor.getTextView();
					var annotationModel = editor.getAnnotationModel();
					if (splits.length > 0) {
//						switch(extension) {
//							case "mysql":
//							case "java":
//							case "css":
								this.styler = new mTextStyler.TextStyler(textView, extension, annotationModel);
//								break;
//							case "html":
//								this.styler = new mTextMateStyler.TextMateStyler(textView, new mHtmlGrammar.HtmlGrammar());
//								break;
//						}
					}
				}
			}
		};
		
		var annotationFactory = new mEditorFeatures.AnnotationFactory();		
		var keyBindingFactory = function(editor, keyModeStack, undoStack, contentAssist) {
			
			// Create keybindings for generic editing
			var genericBindings = new mEditorFeatures.TextActions(editor, undoStack);
			keyModeStack.push(genericBindings);
			
			// create keybindings for source editing
			var codeBindings = new mEditorFeatures.SourceCodeActions(editor, undoStack, contentAssist);
			keyModeStack.push(codeBindings);
			
			// save(ctrl + s)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(83, true), "save");
			editor.getTextView().setAction("save", function(){
				editorService.saveS(editor);
				return true;
			});
			
			// execute query
			// f5
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(116), "executeQuery");
			
			// ctrl + enter
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(13, true), "executeQuery");
			editor.getTextView().setAction("executeQuery", function(){
				editorService.executeQuery(editor);
				return true;
			});
			
			// execute plan(ctrl + e)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(69, true), "executePlan");
			editor.getTextView().setAction("executePlan", function(){
				editorService.executePlan(editor);
				return true;
			});
			
			// 쿼리 정렬 (ctrl + shift + f)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(70, true, true), "executeFormat");
			editor.getTextView().setAction("executeFormat", function(){
				editorService.executeFormat(editor);
				return true;
			});
			
			// 히스토리페이지(ctrl + h)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(72, true), "moveHistoryPage");
			editor.getTextView().setAction("moveHistoryPage", function(){
				editorService.moveHistoryPage(editor);
				return true;
			});
			
			// to low case(ctrl + shift + y)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(89, true, true), "lowCaseText");
			editor.getTextView().setAction("lowCaseText", function(){
				editorService.lowCaseText(editor);
				return true;
			});
			
			// to upper case(ctrl + shift + x)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(88, true, true), "upperCaseText");
			editor.getTextView().setAction("upperCaseText", function(){
				editorService.upperCaseText(editor);
				return true;
			});
			
			// 에디터 도움말(ctrl + shift + l)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(76, true, true), "helpPopup");
			editor.getTextView().setAction("helpPopup", function(){
				editorService.helpPopup(editor);
				return true;
			});
			
			// 화면 모두 clear(f7)
			editor.getTextView().setKeyBinding(new mKeyBinding.KeyBinding(118), "allClearText");
			editor.getTextView().setAction("allClearText", function(){
				editorService.allClearText(editor);
				return true;
			});
			
		};
			
		var statusReporter = function(message, isError) {
		};
		
		editor = new mEditor.Editor({
			textViewFactory: textViewFactory,
			undoStackFactory: new mEditorFeatures.UndoFactory(),
			annotationFactory: annotationFactory,
			lineNumberRulerFactory: new mEditorFeatures.LineNumberRulerFactory(),
			contentAssistFactory: contentAssistFactory,
			keyBindingFactory: keyBindingFactory, 
			statusReporter: statusReporter,
			domNode: editorDomNode
		});
			
		editor.addEventListener("DirtyChanged", function(evt) {
			if (editor.isDirty()) {
//				dirtyIndicator = "*";
				editorServiceHandler(editorService.DIRTY_CHANGED, true);
			} else {
//				dirtyIndicator = "";
//				console.log("[2]dirty changed event saved");
			}
		});		
		editor.installTextView();
		
//		try {
//			editorService.getInitialContent();
//		} catch(err) {
////			console.log("[error msg]" + err);
//		}
		
//		contentAssist.addEventListener("Activating", function() {
//			contentAssist.setProviders([sqlContentAssistProvider]);
//		});
		
		console.log("[editor]" + editor);
		
		// end of code to run when content changes.
//		console.log('====== end ==== ');

	});
}
	
// Install functions for servicing Eclipse Workbench hosted applications
function installWorkbenchHooks() {
	console.log('set installWorkbench hooks');
	
	// Register a function that will be called by the editor when the editor's dirty state changes
	editorService.dirtyChanged = function(dirty) {
		// This is a function created in Eclipse and registered with the page.
		editorServiceHandler(editorService.DIRTY_CHANGED, dirty);
	};
	
//		// Register a getContentName implementation
//		editorService.getContentName = function() {
//			console.log("=======> editorService.getContentName = function() ");
//			// This is a function created in Eclipse and registered with the page.
//			return editorServiceHandler(editorService.GET_CONTENT_NAME);
//		};
	
	// Register an implementation that can return initial content for the editor
	editorService.getInitialContent = function() {
		try {
			console.log("0. start get content");
			var content = editorServiceHandler(editorService.GET_INITIAL_CONTENT);
			console.log("1. success get content");
			
			var idxExt = content.indexOf(":ext:");
			var varExt = content.substring(0, idxExt);
			var varCon = content.substring(idxExt+5, content.length);
			
			console.log("2. set editor content" + varExt);
			editor.setInput(varExt, null, varCon);

			syntaxHighlighter.highlight(varExt, editor);
			
			editor.highlightAnnotations();
			
			editor.setTextFocus();
		} catch(err) {
			console.log(err);
		}
	};
	
	// Register an implementation that should run when the editors status changes.
	editorService.statusChanged = function(message, isError) {
		// This is a function created in Eclipse and registered with the page.
		
		// 리소스를 너무 많이 잡아 먹는 것으로 파악되어 주석 처리 합니다. 향후 봐서 ...
//			editorServiceHandler(editorService.STATUS_CHANGED, message);
	};

	// Register an implementation that can save the editors contents.		
	editorService.save = function() {
		// This is a function created in Eclipse and registered with the page.
		var result = editorServiceHandler(editorService.SAVE, editor.getContents());
		
		if (result) {
			editor.setInput(null, null, null, true);
		}
		
		return result;
	};
	
	// Register an implementation that can save the editors contents.		
	editorService.saveS = function() {
		var result = editorServiceHandler(editorService.SAVE_S, editor.getContents());
		
		if (result) {
			editor.setInput(null, null, null, true);
		}
		return result;
	};
	
	// query
	editorService.executeQuery = function() {
		editorServiceHandler(editorService.EXECUTE_QUERY, editor.getEditorText());//CaretOffsetAndContent());
		editor.setTextFocus();
	};
	
	editorService.executeAllQuery = function() {
		editorServiceHandler(editorService.EXECUTE_ALL_QUERY, editor.getCaretOffsetAndContent());
		editor.setTextFocus();
	};
	
	// query plan
	editorService.executePlan = function() {
		editorServiceHandler(editorService.EXECUTE_PLAN, editor.getCaretOffsetAndContent());
		editor.setTextFocus();
	};
	
	// query format
	editorService.executeFormat = function() {
		var sql = editorServiceHandler(editorService.EXECUTE_FORMAT, editor.getContents());

		editor.setText(sql);
		editor.setTextFocus();
	};
	
	// append query text
	editorService.appendQueryText = function() {
		var sql = editorServiceHandler(editorService.APPEND_QUERY_TEXT, '');
		
		editor.appendQueryText(sql);
		editor.setTextFocus();
	};
	
	// re new query text
	editorService.reNewText = function() {
		var sql = editorServiceHandler(editorService.RE_NEW_TEXT, '');
		editor.onInputChange(null, null, sql, false);
	};
	
	// sql to application string 
	editorService.sqlToApplication = function() {
		editorServiceHandler(editorService.SQL_TO_APPLICATION, editor.getCaretOffsetAndContent());
	};
	
	// download sql 
	editorService.downloadSQL = function() {
		editorServiceHandler(editorService.DOWNLOAD_SQL, editor.getCaretOffsetAndContent());
	};
	
	// move history page
	editorService.moveHistoryPage = function() {
		editorServiceHandler(editorService.MOVE_HISTORY_PAGE, '');
	};
	
	// to upper case text
	editorService.upperCaseText = function() {
		editor.upperCaseText();
	};
	
	// to low case text
	editorService.lowCaseText = function() {
		editor.lowCaseText();
	};

	// text set focus
	editorService.setTextFocus = function() {
		editor.setTextFocus();
	};
	
	// help popup
	editorService.helpPopup = function() {
		editorServiceHandler(editorService.HELP_POPUP, '');
	};
	
	// all text clean
	editorService.allClearText = function() {
		editor.setText("");
	};
}

//Initialize the editor
initEmbeddedEditor();

// install editor hooks
installWorkbenchHooks();

//	// initialize the editor input
//	editorService.getInitialContent();
