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
/*global orion:true*/

/** @namespace */
var orion = orion || {};
orion.editor = orion.editor || {};

/**
 * @class orion.contentAssist.CssContentAssistProvider
 */
orion.editor.CssContentAssistProvider = (function() {
	/** @private */
	function CssContentAssistProvider() {
	}
	CssContentAssistProvider.prototype = /** @lends orion.contentAssist.CssContentAssistProvider.prototype */ {
		/**
		 * @param {String} The string buffer.substring(w+1, c) where c is the caret offset and w is the index of the 
		 * rightmost whitespace character preceding c.
		 * @param {String} buffer The entire buffer being edited
		 * @param {orion.editor.Selection} selection The current textView selection.
		 * @returns {dojo.Deferred} A future that will provide the keywords.
		 */
		getKeywords: function(prefix, buffer, selection) {
			return [ "background", "background-attachment", "background-color", "background-image",
					"background-position", "background-repeat", "border", "border-bottom",
					"border-bottom-color", "border-bottom-style", "border-bottom-width", "border-color",
					"border-left", "border-left-color", "border-left-style", "border-left-width",
					"border-right", "border-right-color", "border-right-style", "border-right-width",
					"border-style", "border-top", "border-top-color", "border-top-style", "border-top-width",
					"border-width", "bottom", "clear", "clip", "color", "cursor", "display", "float", "font",
					"font-family", "font-size", "font-style", "font-variant", "font-weight", "height",
					"horizontal-align", "left", "line-height", "list-style", "list-style-image",
					"list-style-position", "list-style-type", "margin", "margin-bottom", "margin-left",
					"margin-right", "margin-top", "max-height", "max-width", "min-height", "min-width",
					"outline", "outline-color", "outline-style", "outline-width", "overflow", "overflow-x",
					"overflow-y", "padding", "padding-bottom", "padding-left", "padding-right",
					"padding-top", "position", "right", "text-align", "text-decoration", "text-indent",
					"top", "vertical-align", "visibility", "width", "z-index" ];
		}
	};
	return CssContentAssistProvider;
}());

/**
 * @class orion.contentAssist.JavaScriptContentAssistProvider
 */
orion.editor.JavaScriptContentAssistProvider = (function() {
	/** @private */
	function JavaScriptContentAssistProvider() {
	}
	JavaScriptContentAssistProvider.prototype = /** @lends orion.contentAssist.JavaScriptContentAssistProvider.prototype */ {
		/**
		 * @param {String} The string buffer.substring(w+1, c) where c is the caret offset and w is the index of the 
		 * rightmost whitespace character preceding c.
		 * @param {String} buffer The entire buffer being edited
		 * @param {orion.editor.Selection} selection The current textView selection.
		 * @returns {dojo.Deferred} A future that will provide the keywords.
		 */
		getKeywords: function(prefix, buffer, selection) {
			return [ "SELECT * FROM table_name;", 
			         "UPDATE table_name SET column1 = 'change_value' WHERE column2 = 'value';", 
			         "INSERT INTO table_name (column, column1, column2) VALUES ('column', 'column2', 'column3');",
			         "DELETE FROM table_name WHERE column1 = 'value';"
			        ];
		}
	};
	return JavaScriptContentAssistProvider;
}());

if (typeof window !== "undefined" && typeof window.define !== "undefined") {
	define([], function() {
		return orion.editor;
	});
}

