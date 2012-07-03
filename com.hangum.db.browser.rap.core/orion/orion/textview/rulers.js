/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

/*global window define */

/**
 * @namespace The global container for Orion APIs.
 */ 
var orion = orion || {};
orion.textview = orion.textview || {};

orion.textview.Ruler = (function() {
	function Ruler (rulerLocation, rulerOverview, rulerStyle) {
		this._location = rulerLocation || "left";
		this._overview = rulerOverview || "page";
		this._rulerStyle = rulerStyle;
		this._view = null;
	}
	Ruler.prototype = {
		setView: function (view) {
			if (this._onModelChanged && this._view) {
				this._view.removeEventListener("ModelChanged", this, this._onModelChanged); 
			}
			this._view = view;
			if (this._onModelChanged && this._view) {
				this._view.addEventListener("ModelChanged", this, this._onModelChanged);
			}
		},
		getLocation: function() {
			return this._location;
		},
		getOverview: function(view) {
			return this._overview;
		}
	};
	return Ruler;
}());

orion.textview.LineNumberRuler = (function() {
	function LineNumberRuler (rulerLocation, rulerStyle, oddStyle, evenStyle) {
		orion.textview.Ruler.call(this, rulerLocation, "page", rulerStyle);
		this._oddStyle = oddStyle || {style: {backgroundColor: "white"}};
		this._evenStyle = evenStyle || {style: {backgroundColor: "white"}};
		this._numOfDigits = 0;
	}
	LineNumberRuler.prototype = new orion.textview.Ruler(); 
	LineNumberRuler.prototype.getStyle = function(lineIndex) {
		if (lineIndex === undefined) {
			return this._rulerStyle;
		} else {
			return lineIndex & 1 ? this._oddStyle : this._evenStyle;
		}
	};
	LineNumberRuler.prototype.getHTML = function(lineIndex) {
		if (lineIndex === -1) {
			var model = this._view.getModel();
			return model.getLineCount();
		} else {
			return lineIndex + 1;
		}
	};
	LineNumberRuler.prototype._onModelChanged = function(e) {
		var start = e.start;
		var model = this._view.getModel();
		var lineCount = model.getLineCount();
		var numOfDigits = (lineCount+"").length;
		if (this._numOfDigits !== numOfDigits) {
			this._numOfDigits = numOfDigits;
			var startLine = model.getLineAtOffset(start);
			this._view.redrawLines(startLine, lineCount, this);
		}
	};
	return LineNumberRuler;
}());

orion.textview.AnnotationRuler = (function() {
	function AnnotationRuler (rulerLocation, rulerStyle, defaultAnnotation) {
		orion.textview.Ruler.call(this, rulerLocation, "page", rulerStyle);
		this._defaultAnnotation = defaultAnnotation;
		this._annotations = [];
	}
	AnnotationRuler.prototype = new orion.textview.Ruler();
	AnnotationRuler.prototype.clearAnnotations = function() {
		this._annotations = [];
		var lineCount = this._view.getModel().getLineCount();
		this._view.redrawLines(0, lineCount, this);
		if (this._overviewRuler) {
			this._view.redrawLines(0, lineCount, this._overviewRuler);
		}
	};
	AnnotationRuler.prototype.getAnnotation = function(lineIndex) {
		return this._annotations[lineIndex];
	};
	AnnotationRuler.prototype.getAnnotations = function() {
		return this._annotations;
	};
	AnnotationRuler.prototype.getStyle = function(lineIndex) {
		switch (lineIndex) {
			case undefined:
				return this._rulerStyle;
			case -1:
				return this._defaultAnnotation ? this._defaultAnnotation.style : null;
			default:
				return this._annotations[lineIndex] && this._annotations[lineIndex].style ? this._annotations[lineIndex].style : null;
		}
	};
	AnnotationRuler.prototype.getHTML = function(lineIndex) {
		if (lineIndex === -1) {
			return this._defaultAnnotation ? this._defaultAnnotation.html : "";
		} else {
			return this._annotations[lineIndex] && this._annotations[lineIndex].html ? this._annotations[lineIndex].html : "";
		}
	};
	AnnotationRuler.prototype.setAnnotation = function(lineIndex, annotation) {
		if (lineIndex === undefined) { return; }
		this._annotations[lineIndex] = annotation;
		this._view.redrawLines(lineIndex, lineIndex + 1, this);
		if (this._overviewRuler) {
			this._view.redrawLines(lineIndex, lineIndex + 1, this._overviewRuler);
		}
	};
	AnnotationRuler.prototype._onModelChanged = function(e) {
		var start = e.start;
		var removedLineCount = e.removedLineCount;
		var addedLineCount = e.addedLineCount;
		var linesChanged = addedLineCount - removedLineCount;
		if (linesChanged) {
			var model = this._view.getModel();
			var startLine = model.getLineAtOffset(start);
			var newLines = [], lines = this._annotations;
			var changed = false;
			for (var prop in lines) {
				var i = prop >>> 0;
				if (!(startLine < i && i < startLine + removedLineCount)) {
					var newIndex = i;
					if (i > startLine) {
						newIndex += linesChanged;
						changed = true;
					}
					newLines[newIndex] = lines[i];
				} else {
					changed = true;
				}
			}
			this._annotations = newLines;
			if (changed) {
				var lineCount = model.getLineCount();
				this._view.redrawLines(startLine, lineCount, this);
				//TODO redraw overview (batch it for performance)
				if (this._overviewRuler) {
					this._view.redrawLines(0, lineCount, this._overviewRuler);
				}
			}
		}
	};
	return AnnotationRuler;
}());

orion.textview.OverviewRuler = (function() {
	function OverviewRuler (rulerLocation, rulerStyle, annotationRuler) {
		orion.textview.Ruler.call(this, rulerLocation, "document", rulerStyle);
		this._annotationRuler = annotationRuler;
		if (annotationRuler) {
			annotationRuler._overviewRuler = this;
		}
	}
	OverviewRuler.prototype = new orion.textview.Ruler();
	OverviewRuler.prototype.getAnnotations = function() {
		var annotations = this._annotationRuler.getAnnotations();
		var lines = [];
		for (var prop in annotations) {
			var i = prop >>> 0;
			if (annotations[i] !== undefined) {
				lines.push(i);
			}
		}
		return lines;
	};
	OverviewRuler.prototype.getStyle = function(lineIndex) {
		var result, style;
		if (lineIndex === undefined) {
			result = this._rulerStyle || {};
			style = result.style || (result.style = {});
			style.lineHeight = "1px";
			style.fontSize = "1px";
			style.width = "14px";
		} else {
			if (lineIndex !== -1) {
				var annotation = this._annotationRuler.getAnnotation(lineIndex);
				result = annotation.overviewStyle || {};
			} else {
				result = {};
			}
			style = result.style || (result.style = {});
			style.cursor = "pointer";
			style.width = "8px";
			style.height = "3px";
			style.left = "2px";
		}
		return result;
	};
	OverviewRuler.prototype.getHTML = function(lineIndex) {
		return "&nbsp;";
	};
	OverviewRuler.prototype.onClick = function(lineIndex, e) {
		if (lineIndex === undefined) { return; }
		this._view.setTopIndex(lineIndex);
	};
	return OverviewRuler;
}());

if (typeof window !== "undefined" && typeof window.define !== "undefined") {
	define([], function() {
		return orion.textview;
	});
}
