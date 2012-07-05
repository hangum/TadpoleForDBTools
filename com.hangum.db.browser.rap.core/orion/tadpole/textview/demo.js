/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 
 /*globals window define document navigator setTimeout XMLHttpRequest PerformanceTest */
 
 
function log (text) {
	var console = window.document.getElementById('console');
	if (!console) { return; }
	for (var n = 1; n < arguments.length; n++) {
		text += " ";
		text += arguments[n];
	}
	
	var document = console.contentWindow.document;
	var t = document.createTextNode(text);
	document.body.appendChild(t);
	var br = document.createElement("br");
	document.body.appendChild(br);
	if (!console.scroll) {
		console.scroll = true;
		setTimeout(function() {
			document.body.lastChild.scrollIntoView(false);
			console.scroll = false;
		}, 0);
	}
}
 
 define(["orion/textview/keyBinding",
		"orion/textview/textModel", 
		"orion/textview/textView", 
		"orion/textview/rulers",
		"orion/textview/undoStack",
		"tadpole/textview/textStyler",
		"tests/textview/test-performance"],   
 
function(mKeyBinding, mTextModel, mTextView, mRulers, mUndoStack, mTextStyler) {
	var view = null;
	var styler = null;
	var isMac = navigator.platform.indexOf("Mac") !== -1;
	
	function clearLog () {
		var console = window.document.getElementById('console');
		if (!console) { return; }
		var document = console.contentWindow.document;
		var body = document.body;
		while (body.hasChildNodes()) { body.removeChild(body.lastChild); }
	}
	
	function getFile(file) {
		try {
			var objXml = new XMLHttpRequest();
			objXml.open("GET",file,false);
			objXml.send(null);
			return objXml.responseText;
		} catch (e) {
			return null;
		}
	}
	
	function checkView() {
		if (view) { return; }
		var stylesheets = [
			"/orion/textview/textview.css",
			"/orion/textview/rulers.css",
			"/examples/textview/textstyler.css"
		];
		var options = {
			parent: "divParent",
			model: new mTextModel.TextModel(),
			stylesheet: stylesheets,
			tabSize: 4
		};
		view = new mTextView.TextView(options);
		
		/* Undo stack */
		var undoStack = new mUndoStack.UndoStack(view, 200);
		view.setKeyBinding(new mKeyBinding.KeyBinding('z', true), "undo");
		view.setAction("undo", function() {
			undoStack.undo();
			return true;
		});
		view.setKeyBinding(isMac ? new mKeyBinding.KeyBinding('z', true, true) : new mKeyBinding.KeyBinding('y', true), "redo");
		view.setAction("redo", function() {
			undoStack.redo();
			return true;
		});
		
		/* Example: Adding a keyBinding and action*/
		view.setKeyBinding(new mKeyBinding.KeyBinding('s', true), "save");
		view.setAction("save", function() {
			log("*****************SAVE");
			return true;
		});
	
		/* Adding the Rulers */	
		var breakpoint = {
			html: "<img src='images/brkp_obj.gif'></img>",
			style: {styleClass: "ruler_annotation_breakpoint"},
			overviewStyle: {styleClass: "ruler_annotation_breakpoint_overview"}
		};
		var todo = {
			html: "<img src='images/todo.gif'></img>",
			style: {styleClass: "ruler_annotation_todo"},
			overviewStyle: {styleClass: "ruler_annotation_todo_overview"}
		};
		var annotation = new mRulers.AnnotationRuler("left", {styleClass: "ruler_annotation"}, breakpoint);
		annotation.onDblClick =  function(lineIndex, e) {
			if (lineIndex === undefined) { return; }
			annotation.setAnnotation(lineIndex, annotation.getAnnotation(lineIndex) !== undefined ? undefined : e.ctrlKey ? todo : breakpoint);
		};
		var lines = new mRulers.LineNumberRuler("left", {styleClass: "ruler_lines"}, {styleClass: "ruler_lines_odd"}, {styleClass: "ruler_lines_even"});
		lines.onDblClick = annotation.onDblClick;
		var overview = new mRulers.OverviewRuler("right", {styleClass: "ruler_overview"}, annotation);
		view.addRuler(annotation);
		view.addRuler(lines);
		view.addRuler(overview);
	}
	
	function createJavaSample() {
		checkView();
		var file =  getFile("text.txt");
		if (styler) {
			styler.destroy();
			styler = null;
		}
		styler = new mTextStyler.TextStyler(view, "java");
		view.setText(file);
	}
	
	function createJavaScriptSample() {
		checkView();
		var file =  getFile("/orion/textview/textview.js");
		if (styler) {
			styler.destroy();
			styler = null;
		}
		styler = new mTextStyler.TextStyler(view, "js");
		view.setText(file);
	}
	
	function createPlainTextSample() {
		checkView();
		var lineCount = 50000;
		var lines = [];
		for(var i = 0; i < lineCount; i++) {
			lines.push("This is the line of text number "+i);
		}
		if (styler) {
			styler.destroy();
			styler = null;
		}
		view.setText(lines.join("\r\n"));
	}
	
	function createBidiTextSample() {
		checkView();
		var lines = [];
		lines.push("Hello \u0644\u0645\u0646\u0647");
		if (styler) {
			styler.destroy();
			styler = null;
		}
		view.setText(lines.join("\r\n"));
	}
	
	function test() {
	}
	
	function performanceTest() {
		checkView();
		if (styler) {
			styler.destroy();
			styler = null;
		}
		/* Note: PerformanceTest is not using require js */
		var test = new PerformanceTest(view);
		var select = document.getElementById("performanceTestSelect");
		test[select.value]();
	}

	/* Adding events */
	document.getElementById("createJavaSample").onclick = createJavaSample;
	document.getElementById("createJavaScriptSample").onclick = createJavaScriptSample;
	document.getElementById("createPlainTextSample").onclick = createPlainTextSample;
	document.getElementById("createBidiTextSample").onclick = createBidiTextSample;
	document.getElementById("clearLog").onclick = clearLog;
	document.getElementById("test").onclick = test;
	document.getElementById("performanceTest").onclick = performanceTest;
		 
 });