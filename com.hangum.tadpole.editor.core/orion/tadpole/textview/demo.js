/*******************************************************************************
 * @license
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Mihai Sucan (Mozilla Foundation) - fix for bug 350636
 *******************************************************************************/
 
/*globals define window document setTimeout */


define(['tadpole/textview/demoSetup', 'tests/textview/test-performance'],   
 
function(mSetup, mTestPerformance) {

	function clearConsole () {
		var console = window.document.getElementById('console');
		if (!console) { return; }
		while (console.hasChildNodes()) { console.removeChild(console.lastChild); }
	}
	
	function showConsole () {
		var console = window.document.getElementById('console');
		if (!console) { return; }
		var consoleCol = window.document.getElementById('consoleCol');
		var consoleHeader = window.document.getElementById('consoleHeader');
		var consoleActions = window.document.getElementById('consoleActions');
		consoleCol.style.display = consoleHeader.style.display = consoleActions.style.display = "block";
		if (mSetup.view) { mSetup.view.resize(); }
	}
	
	function hideConsole () {
		var console = window.document.getElementById('console');
		if (!console) { return; }
		var consoleCol = window.document.getElementById('consoleCol');
		var consoleHeader = window.document.getElementById('consoleHeader');
		var consoleActions = window.document.getElementById('consoleActions');
		consoleCol.style.display = consoleHeader.style.display = consoleActions.style.display = "none";
		if (mSetup.view) { mSetup.view.resize(); }
	}
	
	function log (text) {
		var console = window.document.getElementById('console');
		if (!console) { return; }
		showConsole();
		for (var n = 1; n < arguments.length; n++) {
			text += " ";
			text += arguments[n];
		}
		console.appendChild(document.createTextNode(text));
		console.appendChild(document.createElement("br"));
		console.scrollTop = console.scrollHeight;
	}
	window.log = log;

	var bCreateJava = document.getElementById("createJavaSample");
	var bCreateJS = document.getElementById("createJavaScriptSample");
	var bCreateHTML = document.getElementById("createHtmlSample");
	var bCreatePlain = document.getElementById("createPlainTextSample");
	var bCreateBidi = document.getElementById("createBidiTextSample");
	var bCreateLoad = document.getElementById("createLoad");
	var sLangSelect = document.getElementById("langSelect");
	var tURLContent = document.getElementById("urlContent");
	var bSetOptions = document.getElementById("setOptions");
	var bClearLog = document.getElementById("clearLog");
	var bHideLog = document.getElementById("hideLog");
	var bTest = document.getElementById("test");
	var bPerform = document.getElementById("performanceTest");
	var sPerform = document.getElementById("performanceTestSelect");
	var sTheme = document.getElementById("themeSelect");
	var bReadOnly = document.getElementById('readOnly');
	var bFullSel = document.getElementById('fullSelection');
	var bExpandTab = document.getElementById('expandTab');
	var sTabSize = document.getElementById('tabSize');
	
	function getOptions() {
		return {
			readonly: bReadOnly.checked,
			fullSelection: bFullSel.checked,
			expandTab: bExpandTab.checked,
			tabSize: parseInt(sTabSize.value, 10),
			themeClass: sTheme.value
		};
	}
	
	function updateOptions() {
		var view = mSetup.view;
		var options = view.getOptions();
		bReadOnly.checked = options.readonly;
		bFullSel.checked = options.fullSelection;
		bExpandTab.checked = options.expandTab;
		sTabSize.value = options.tabSize;
		sTheme.value = options.themeClass;
	}

	function setOptions() {
		var view = mSetup.checkView(getOptions());
		view.focus();
		updateOptions();
	}

	function setupView(text, lang) {
		var view = mSetup.setupView(text, lang, getOptions());
		view.focus();
		updateOptions();
		return view;
	}
	
	function createJavaSample() {
		return setupView(mSetup.getFile("text.txt"), "java");
	}
	
	function createJavaScriptSample() {
		return setupView(mSetup.getFile("/orion/textview/textView.js"), "js");
	}

	function createHtmlSample() {
		return setupView(mSetup.getFile("/tadpole/textview/demo.html"), "html");
	}
	
	function createPlainTextSample() {
		var lineCount = 50000;
		var lines = [];
		for(var i = 0; i < lineCount; i++) {
			lines.push("This is the line of text number "+i);
		}
		return setupView(lines.join("\r\n"), null);
	}
	
	function createBidiTextSample() {
		var lines = [];
		lines.push("Hello \u0644\u0645\u0646\u0647");
		return setupView(lines.join("\r\n"), null);
	}
	
	function createLoad() {
		var text = tURLContent.value ? mSetup.getFile(tURLContent.value) : "";
		return setupView(text, sLangSelect.value);
	}

	function test() {
		log("test");
	}
	
	function performanceTest() {
		mTestPerformance[sPerform.value]();
	}
	
	/* Adding events */
	bCreateJava.onclick = createJavaSample;
	bCreateJS.onclick = createJavaScriptSample;
	bCreateHTML.onclick = createHtmlSample;
	bCreatePlain.onclick = createPlainTextSample;
	bCreateBidi.onclick = createBidiTextSample;
	bCreateLoad.onclick = createLoad;
	bSetOptions.onclick = setOptions;
	bClearLog.onclick = clearConsole;
	bHideLog.onclick = hideConsole;
	bTest.onclick = test;
	bPerform.onclick = performanceTest;
	var prefix = "test";
	mTestPerformance.noDojo = true;
	for (var property in mTestPerformance) {
		if (property.indexOf(prefix) === 0) {
			var option = document.createElement("OPTION");
			option.setAttribute("value", property);
			option.appendChild(document.createTextNode(property.substring(prefix.length	)));
			sPerform.appendChild(option);
		}
	}
 });
