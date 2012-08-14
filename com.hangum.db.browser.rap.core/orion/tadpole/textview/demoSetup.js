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
 *******************************************************************************/
 
/*globals define window document setTimeout XMLHttpRequest log */
 
define(["require", 
		"orion/textview/keyBinding",
		"orion/textview/textModel",
		"orion/textview/annotations", 
		"orion/textview/projectionTextModel", 
		"orion/textview/textView", 
		"orion/textview/textDND", 
		"orion/textview/rulers",
		"orion/textview/undoStack",
		"orion/textview/eventTarget",
		"orion/editor/textMateStyler",
		"orion/editor/htmlGrammar",
		"tadpole/textview/textStyler"],   
 
function(require, mKeyBinding, mTextModel, mAnnotations, mProjectionTextModel, mTextView, mTextDND, mRulers, mUndoStack, mEventTarget, mTextMateStyler, mHtmlGrammar, mTextStyler) {

	var exports = {};
	var view = null;
	var styler = null;
	var annotationStyler = null;
	var loadedThemes = [];
	var isMac = window.navigator.platform.indexOf("Mac") !== -1;
	
	var AnnotationType = mAnnotations.AnnotationType;
		
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
	exports.getFile = getFile;
	
	function loadTheme(theme) {
		if (theme) {
			for (var i=0; i<loadedThemes.length; i++) {
				if (theme === loadedThemes[i]) {
					return;
				}
			}
			loadedThemes.push(theme);
			require(["text!tadpole/textview/themes/" + theme + ".css"], function(cssText) {
				var stylesheet;
				if (document.createStyleSheet) {
					stylesheet = document.createStyleSheet();
					stylesheet.cssText = cssText;
				} else {
					stylesheet = document.createElement("STYLE");
					var head = document.getElementsByTagName("HEAD")[0] || document.documentElement;
					stylesheet.appendChild(document.createTextNode(cssText));
					head.appendChild(stylesheet);
				}
				view.update(true);
			});
		}
	}
	
	function checkView(options) {
		if (view) {
			if (options) {
				loadTheme(options.themeClass);
				view.setOptions(options);
			}
			return view;
		}
		
		var baseModel =  new mTextModel.TextModel(), viewModel = baseModel;
		var foldingEnabled = true;
		if (foldingEnabled) {
			viewModel = new mProjectionTextModel.ProjectionTextModel(baseModel);
		}
		options = options || {};
		loadTheme(options.themeClass);
		options.parent = options.parent || "divParent";
		options.model = viewModel;
		exports.view = view = new mTextView.TextView(options);
		
		/* Undo stack */
		var undoStack = exports.undoStack = new mUndoStack.UndoStack(view, 200);
		exports.textDND = new mTextDND.TextDND(view, undoStack);
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

		view.setKeyBinding(new mKeyBinding.KeyBinding('s', true), "save");
		view.setAction("save", function() {
			log("*****************SAVE");
			return true;
		});

		var annotationModel = view.annotationModel = new mAnnotations.AnnotationModel(baseModel);
		/* Example: Adding a keyBinding and action*/
		view.setKeyBinding(new mKeyBinding.KeyBinding('h', true), "collapseAll");
		view.setAction("collapseAll", function() {
			log("*****************COLLAPSE");
			var iter = annotationModel.getAnnotations(0, baseModel.getCharCount());
			view.setRedraw(false);
			while (iter.hasNext()) {
				var a = iter.next();
				if (a.type === AnnotationType.ANNOTATION_FOLDING) {
					a.collapse();
					annotationModel.modifyAnnotation(a);
				}
			}
			view.setRedraw(true);
			return true;
		});
		/* Example: Adding a keyBinding and action*/
		view.setKeyBinding(new mKeyBinding.KeyBinding('j', true), "expandAll");
		view.setAction("expandAll", function() {
			log("*****************EXPAND");
			var iter = annotationModel.getAnnotations(0, baseModel.getCharCount());
			view.setRedraw(false);
			while (iter.hasNext()) {
				var a = iter.next();
				if (a.type === AnnotationType.ANNOTATION_FOLDING) {
					a.expand();
					annotationModel.modifyAnnotation(a);
				}
			}
			view.setRedraw(true);
			return true;
		});
		

		/* Adding the Rulers */
		var annotationRuler = view.annotationRuler = new mRulers.AnnotationRuler(annotationModel, "left", {styleClass: "ruler annotations"});
		annotationRuler.addAnnotationType(AnnotationType.ANNOTATION_BREAKPOINT);
		annotationRuler.addAnnotationType(AnnotationType.ANNOTATION_BOOKMARK);
		annotationRuler.addAnnotationType(AnnotationType.ANNOTATION_ERROR);
		annotationRuler.addAnnotationType(AnnotationType.ANNOTATION_WARNING);
		annotationRuler.addAnnotationType(AnnotationType.ANNOTATION_TASK);
		annotationRuler.setMultiAnnotation({html: "<div class='annotationHTML multiple'></div>"});
		annotationRuler.setMultiAnnotationOverlay({html: "<div class='annotationHTML overlay'></div>"});
		annotationRuler.onDblClick =  function(lineIndex, e) {
			if (lineIndex === undefined) { return; }
			var model = this._view.getModel();
			var start = model.getLineStart(lineIndex);
			var end = model.getLineEnd(lineIndex, true);
			if (model.getBaseModel) {
				start = model.mapOffset(start);
				end = model.mapOffset(end);
			}
			var type;
			if (isMac ? e.metaKey : e.ctrlKey) {
				if (e.shiftKey && e.altKey) {
					type = AnnotationType.ANNOTATION_WARNING;
				} else if (e.altKey) {
					type = AnnotationType.ANNOTATION_ERROR;
				} else if (e.shiftKey) {
					type = AnnotationType.ANNOTATION_BOOKMARK;
				} else {
					type = AnnotationType.ANNOTATION_TASK;
				}
			} else {
				type = AnnotationType.ANNOTATION_BREAKPOINT;
			}
			var annotations = annotationModel.getAnnotations(start, end);
			var annotation, temp;
			while ((temp = annotations.next()) !== null) {
				if (temp.type === type) {
					annotation = temp;
					break;
				}
			}
			if (annotation) {
				annotationModel.removeAnnotation(annotation);
			} else {
				annotation = AnnotationType.createAnnotation(type, start, end);
				annotation.title += ": " + model.getLine(lineIndex);
				annotationModel.addAnnotation(annotation);
			}
		};
		var linesRuler = view.lines = new mRulers.LineNumberRuler(annotationModel, "left", {styleClass: "ruler lines"}, {styleClass: "rulerLines odd"}, {styleClass: "rulerLines even"});
		linesRuler.onDblClick = annotationRuler.onDblClick;
		var overviewRuler = new mRulers.OverviewRuler(annotationModel, "right", {styleClass: "ruler overview"});
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_BREAKPOINT);
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_BOOKMARK);
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_ERROR);
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_WARNING);
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_TASK);
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_MATCHING_BRACKET);
		overviewRuler.addAnnotationType(AnnotationType.ANNOTATION_CURRENT_BRACKET);
		
		view.addRuler(annotationRuler);
		view.addRuler(linesRuler);
		if (foldingEnabled) {
			var foldingRuler = view.folding = new mRulers.FoldingRuler(annotationModel, "left", {styleClass: "ruler folding"});
			foldingRuler.addAnnotationType(AnnotationType.ANNOTATION_FOLDING);
			view.addRuler(foldingRuler);
		}
		view.addRuler(overviewRuler);
		return view;
	}
	exports.checkView = checkView;
	
	function setupView(text, lang, options) {
		checkView(options);
		if (styler) {
			styler.destroy();
			styler = null;
			annotationStyler.destroy();
			annotationStyler = null;
		}
		switch (lang) {
			case "js":
			case "java":
			case "css":
				styler = new mTextStyler.TextStyler(view, lang, view.annotationModel);
				styler.setHighlightCaretLine(true);
				break;
			case "html":
				styler = new mTextMateStyler.TextMateStyler(view, new mHtmlGrammar.HtmlGrammar());
				break;
		}
		annotationStyler = new mAnnotations.AnnotationStyler(view, view.annotationModel);
		annotationStyler.addAnnotationType(AnnotationType.ANNOTATION_TASK);
		annotationStyler.addAnnotationType(AnnotationType.ANNOTATION_MATCHING_BRACKET);
		annotationStyler.addAnnotationType(AnnotationType.ANNOTATION_CURRENT_BRACKET);
		view.setText(text);
		return view;
	}
	exports.setupView = setupView;

	return exports;
});