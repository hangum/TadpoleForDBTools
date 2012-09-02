/*******************************************************************************
 * @license
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 * 
 * extension sql - hangum
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*global define */

define("orion/editor/sqlContentAssist", [], function() {
	var keywords = [
	                 "SELECT * FROM table_name;",
	                 "INSERT INTO table_name (column, column1, column2) VALUES ('column', 'column2', 'column3');",
			         "UPDATE table_name SET column1 = 'change_value' WHERE column2 = 'value';",			         
			         "DELETE FROM table_name WHERE column1 = 'value';"
			       ];

	function getSQLPrefix(buffer, offset) {
		var index = offset;
		while (index && /[A-Za-z\-]/.test(buffer.charAt(index - 1))) {
			index--;
		}
		return index ? buffer.substring(index, offset) : "";
	}

	/**
	 * @name orion.contentAssist.SQLContentAssistProvider
	 * @class Provides content assist for CSS keywords.
	 */
	function SQLContentAssistProvider() {
	}
	SQLContentAssistProvider.prototype = /** @lends orion.editor.SQLContentAssistProvider.prototype */ {
		/**
		 * @param {String} buffer The entire buffer being edited.
		 * @param {Number} offset The offset at which proposals will be inserted.
		 * @param {Object} context Extra information about the editor state.
		 */
		computeProposals: function(buffer, offset, context) {
			var SQLPrefix = getSQLPrefix(buffer, offset);
			var proposals = [];
			for (var i=0; i < keywords.length; i++) {
					var keyword = keywords[i];
					if (keyword.indexOf(SQLPrefix) === 0) {
						proposals.push({
							proposal: keyword.substring(SQLPrefix.length),
							description: keyword
						});
					}
			}
			return proposals;
		}
	};

	return {
		SQLContentAssistProvider: SQLContentAssistProvider
	};
});
