/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*global eclipse:true orion:true dojo window editorServiceHandler editor:true */
/*jslint devel:true*/

/**
 * This file demonstrates one way in which a web application could be structured
 * to easily allow different behavior depending on whether the application is hosted
 * in a browser or in an Eclipse Workbench
 **/

/**
 * This object encapsulates the set of actions an editor may perform that would
 * behave differently depending on where the web application is hosted.
 * 
 * The web application implementor may install a set of functions in this object
 * that may differ for browser hosting vs. Eclipse Workbench hosting.
 * 
 * Not all functions need to be hooked.  Also, some functions may only be hooked for
 * one type of hosting scenerio.
 * 
 * This object would be created by the web application developer as they are defining
 * the separation of responsibilities for their web application.
 * 
 * This object is global because it needs to be referenced by the Eclipse Workbench
 **/

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
	
	RE_NEW : 41,
	reNew: function(editor) {},
	
	SQL_TO_APPLICATION : 45,
	sqlToApplication: function(editor) {},
	
	SET_FOCUS : 999,
	setTextFocus: function(editor) {}
	
};

var editor;

function initEmbeddedEditor(){
	var editorDomNode = dojo.byId("editor");
	
	var textViewFactory = function() {
		return new orion.textview.TextView({
			parent: editorDomNode,
//			stylesheet: ["../../orion/textview/textview.css", "../../orion/textview/rulers.css", "../../tadpole/textview/textstyler.css", "../../tadpole/editor/htmlStyles.css"],
			stylesheet: ["../../orion/textview/textview.css", "../../orion/textview/rulers.css", "../../tadpole/textview/textstyler.css"],
			tabSize: 4
		});
	};

	var contentAssistFactory = function(editor) {
		var contentAssist = new orion.editor.ContentAssist(editor, "contentassist");
		contentAssist.addProvider(new orion.editor.CssContentAssistProvider(), "css", "\\.css$");
		contentAssist.addProvider(new orion.editor.JavaScriptContentAssistProvider(), "js", "\\.js$");
		contentAssist.addProvider(new orion.editor.JavaScriptContentAssistProvider(), "js", "\\.mysql$");
		contentAssist.addProvider(new orion.editor.JavaScriptContentAssistProvider(), "js", "\\.oracle$");
		contentAssist.addProvider(new orion.editor.JavaScriptContentAssistProvider(), "js", "\\.sqlite$");
		contentAssist.addProvider(new orion.editor.JavaScriptContentAssistProvider(), "js", "\\.mssql$");
		return contentAssist;
	};
	
	// Canned highlighters for js, java, and css. Grammar-based highlighter for html
	var syntaxHighlighter = {
		styler: null, 
		
		highlight: function(fileName, textView) {
			
			if (this.styler) {
				this.styler.destroy();
				this.styler = null;
			}
			if (fileName) {
				var splits = fileName.split(".");
				var extension = splits.pop().toLowerCase();
		
				if (splits.length > 0) {
					switch(extension) {
						case "mysql":
							this.styler = new examples.textview.TextStyler(textView, "mysql");
							break;
						case "oracle":
							this.styler = new examples.textview.TextStyler(textView, "oracle");
							break;
						case "sqlite":
							this.styler = new examples.textview.TextStyler(textView, "sqlite");
							break;
						case "mssql":
							this.styler = new examples.textview.TextStyler(textView, "mssql");
							break;
//						case "html":
//							this.styler = new orion.editor.TextMateStyler(textView, orion.editor.HtmlGrammar.grammar);
//							break;
					}
				}
			}
		}
	};
	
	var annotationFactory = new orion.editor.AnnotationFactory();

	var keyBindingFactory = function(editor, keyModeStack, undoStack, contentAssist) {
		
		// Create keybindings for generic editing
		var genericBindings = new orion.editor.TextActions(editor, undoStack);
		keyModeStack.push(genericBindings);
		
		// create keybindings for source editing
		var codeBindings = new orion.editor.SourceCodeActions(editor, undoStack, contentAssist);
		keyModeStack.push(codeBindings);
		
//		
//		input dialog not use the rap.
//		
		// save binding
		editor.getTextView().setKeyBinding(new orion.textview.KeyBinding("s", true), "save");
		editor.getTextView().setKeyBinding(new orion.textview.KeyBinding("ㄴ", true), "save");
		editor.getTextView().setAction("save", function(){
			// The save function is called through the editorService allowing Eclipse and Browser hosted instances to behave differently
			editorService.saveS(editor);
			return true;
		});
		// speaking of save...
//		dojo.byId("save").onclick = function() {editorService.saveS(editor);};
		
		// execute query
		editor.getTextView().setKeyBinding(new orion.textview.KeyBinding("e", true), "executeQuery");
		editor.getTextView().setKeyBinding(new orion.textview.KeyBinding("ㄷ", true), "executeQuery");
		editor.getTextView().setAction("executeQuery", function(){
			// The save function is called through the editorService allowing Eclipse and Browser hosted instances to behave differently
			editorService.executeQuery(editor);
			return true;
		});
		
		// execute query
		editor.getTextView().setKeyBinding(new orion.textview.KeyBinding("r", true), "executeQuery");
		editor.getTextView().setKeyBinding(new orion.textview.KeyBinding("ㄱ", true), "executeQuery");
		editor.getTextView().setAction("executeQuery", function(){
			// The save function is called through the editorService allowing Eclipse and Browser hosted instances to behave differently
			editorService.executeQuery(editor);
			return true;
		});

	};
	
	editor = new orion.editor.Editor({
		textViewFactory: textViewFactory,
		undoStackFactory: new orion.editor.UndoFactory(),
		annotationFactory: annotationFactory,
		lineNumberRulerFactory: new orion.editor.LineNumberRulerFactory(),
		contentAssistFactory: contentAssistFactory,
		keyBindingFactory: keyBindingFactory,
		statusReporter: editorService.statusChanged,
		domNode: editorDomNode
	});
	
	dojo.connect(editor, "onDirtyChange", this, editorService.dirtyChanged); // Hooks the onDirtyChange event listener through the editorService
	
	editor.installTextView();
	
	// Set editor input by calling through editorService
	editor.onInputChange(editorService.getContentName(), null, editorService.getInitialContent());
	
	// Set the syntax highlighter
	syntaxHighlighter.highlight(editorService.getContentName(), editor.getTextView());
} // end of initEmbeddedEditor
	
// Created embedded editor
dojo.addOnLoad(function() {
		
	// Install functions for servicing Eclipse Workbench hosted applications
	function installWorkbenchHooks() {
		// Register a getContentName implementation
		
		// Register a function that will be called by the editor when the editor's dirty state changes
		editorService.dirtyChanged = function(dirty) {
			// This is a function created in Eclipse and registered with the page.
			editorServiceHandler(editorService.DIRTY_CHANGED, dirty);
		};

		// Register a getContentName implementation
		editorService.getContentName = function() {
			// This is a function created in Eclipse and registered with the page.
			return editorServiceHandler(editorService.GET_CONTENT_NAME);
		};
		
		// Register an implementation that can return initial content for the editor
		editorService.getInitialContent = function() {
			// This is a function created in Eclipse and registered with the page.
			return editorServiceHandler(editorService.GET_INITIAL_CONTENT);
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
				editor.onInputChange(null, null, null, true);
			}
			return result;
		};
		
		// Register an implementation that can save the editors contents.		
		editorService.saveS = function() {
			var result = editorServiceHandler(editorService.SAVE_S, editor.getContents());
			
			if (result) {
				editor.onInputChange(null, null, null, true);
			}
			return result;
		};
		
		// query
		editorService.executeQuery = function() {
			
			editorServiceHandler(editorService.EXECUTE_QUERY, editor.getEditorText());//CaretOffsetAndContent());
		};
		
		editorService.executeAllQuery = function() {
			editorServiceHandler(editorService.EXECUTE_ALL_QUERY, editor.getCaretOffsetAndContent());
		};
		
		// query plan
		editorService.executePlan = function() {
			editorServiceHandler(editorService.EXECUTE_PLAN, editor.getCaretOffsetAndContent());
		};
		
		// query format
		editorService.executeFormat = function() {
			var sql = editorServiceHandler(editorService.EXECUTE_FORMAT, editor.getContents());
			editor.onInputChange(null, null, sql, false);
		};
		
		// append query text
		editorService.appendQueryText = function() {
			var sql = editorServiceHandler(editorService.APPEND_QUERY_TEXT, '');
			editor.appendQueryText(sql);
			editor.setTextFocus();
		};
		
		// re new query text
		editorService.reNew = function() {
			var sql = editorServiceHandler(editorService.RE_NEW, '');
			editor.onInputChange(null, null, sql, false);
		};
		
		// sql to application string 
		editorService.sqlToApplication = function() {
			editorServiceHandler(editorService.SQL_TO_APPLICATION, editor.getCaretOffsetAndContent());
		}

		// text set focus
		editorService.setTextFocus = function() {
			editor.setTextFocus();
		};
	}
	
	installWorkbenchHooks();
	
	// Initialize the editor
	initEmbeddedEditor();
});