/*******************************************************************************
 * @license
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 * 
 * Contributors: 
 *		Felipe Heidrich (IBM Corporation) - initial API and implementation
 *		Silenio Quarti (IBM Corporation) - initial API and implementation
 *		Mihai Sucan (Mozilla Foundation) - fix for Bug#334583 Bug#348471 Bug#349485 Bug#350595 Bug#360726 Bug#361180 Bug#362835 Bug#362428 Bug#362286 Bug#354270 Bug#361474 Bug#363945 Bug#366312 Bug#370584
 ******************************************************************************/

/*global window document navigator setTimeout clearTimeout setInterval clearInterval define */

define("orion/textview/textView", ['orion/textview/textModel', 'orion/textview/keyBinding', 'orion/textview/eventTarget'], function(mTextModel, mKeyBinding, mEventTarget) { //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$

	/** @private */
	function addHandler(node, type, handler, capture) {
		if (typeof node.addEventListener === "function") { //$NON-NLS-0$
			node.addEventListener(type, handler, capture === true);
		} else {
			node.attachEvent("on" + type, handler); //$NON-NLS-0$
		}
	}
	/** @private */
	function removeHandler(node, type, handler, capture) {
		if (typeof node.removeEventListener === "function") { //$NON-NLS-0$
			node.removeEventListener(type, handler, capture === true);
		} else {
			node.detachEvent("on" + type, handler); //$NON-NLS-0$
		}
	}
	var userAgent = navigator.userAgent;
	var isIE;
	if (document.selection && window.ActiveXObject && /MSIE/.test(userAgent)) {
		isIE = document.documentMode ? document.documentMode : 7;
	}
	var isFirefox = parseFloat(userAgent.split("Firefox/")[1] || userAgent.split("Minefield/")[1]) || undefined; //$NON-NLS-1$ //$NON-NLS-0$
	var isOpera = userAgent.indexOf("Opera") !== -1; //$NON-NLS-0$
	var isChrome = userAgent.indexOf("Chrome") !== -1; //$NON-NLS-0$
	var isSafari = userAgent.indexOf("Safari") !== -1 && !isChrome; //$NON-NLS-0$
	var isWebkit = userAgent.indexOf("WebKit") !== -1; //$NON-NLS-0$
	var isPad = userAgent.indexOf("iPad") !== -1; //$NON-NLS-0$
	var isMac = navigator.platform.indexOf("Mac") !== -1; //$NON-NLS-0$
	var isWindows = navigator.platform.indexOf("Win") !== -1; //$NON-NLS-0$
	var isLinux = navigator.platform.indexOf("Linux") !== -1; //$NON-NLS-0$
	var isW3CEvents = typeof window.document.documentElement.addEventListener === "function"; //$NON-NLS-0$
	var isRangeRects = (!isIE || isIE >= 9) && typeof window.document.createRange().getBoundingClientRect === "function"; //$NON-NLS-0$
	var platformDelimiter = isWindows ? "\r\n" : "\n"; //$NON-NLS-1$ //$NON-NLS-0$
	
	/** 
	 * Constructs a new Selection object.
	 * 
	 * @class A Selection represents a range of selected text in the view.
	 * @name orion.textview.Selection
	 */
	function Selection (start, end, caret) {
		/**
		 * The selection start offset.
		 *
		 * @name orion.textview.Selection#start
		 */
		this.start = start;
		/**
		 * The selection end offset.
		 *
		 * @name orion.textview.Selection#end
		 */
		this.end = end;
		/** @private */
		this.caret = caret; //true if the start, false if the caret is at end
	}
	Selection.prototype = /** @lends orion.textview.Selection.prototype */ {
		/** @private */
		clone: function() {
			return new Selection(this.start, this.end, this.caret);
		},
		/** @private */
		collapse: function() {
			if (this.caret) {
				this.end = this.start;
			} else {
				this.start = this.end;
			}
		},
		/** @private */
		extend: function (offset) {
			if (this.caret) {
				this.start = offset;
			} else {
				this.end = offset;
			}
			if (this.start > this.end) {
				var tmp = this.start;
				this.start = this.end;
				this.end = tmp;
				this.caret = !this.caret;
			}
		},
		/** @private */
		setCaret: function(offset) {
			this.start = offset;
			this.end = offset;
			this.caret = false;
		},
		/** @private */
		getCaret: function() {
			return this.caret ? this.start : this.end;
		},
		/** @private */
		toString: function() {
			return "start=" + this.start + " end=" + this.end + (this.caret ? " caret is at start" : " caret is at end"); //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		},
		/** @private */
		isEmpty: function() {
			return this.start === this.end;
		},
		/** @private */
		equals: function(object) {
			return this.caret === object.caret && this.start === object.start && this.end === object.end;
		}
	};
	/**
	 * @class This object describes the options for the text view.
	 * <p>
	 * <b>See:</b><br/>
	 * {@link orion.textview.TextView}<br/>
	 * {@link orion.textview.TextView#setOptions}
	 * {@link orion.textview.TextView#getOptions}	 
	 * </p>		 
	 * @name orion.textview.TextViewOptions
	 *
	 * @property {String|DOMElement} parent the parent element for the view, it can be either a DOM element or an ID for a DOM element.
	 * @property {orion.textview.TextModel} [model] the text model for the view. If it is not set the view creates an empty {@link orion.textview.TextModel}.
	 * @property {Boolean} [readonly=false] whether or not the view is read-only.
	 * @property {Boolean} [fullSelection=true] whether or not the view is in full selection mode.
	 * @property {Boolean} [tabMode=true] whether or not the tab keypress is consumed by the view or is used for focus traversal.
	 * @property {Boolean} [expandTab=false] whether or not the tab key inserts white spaces.
	 * @property {String} [themeClass] the CSS class for the view theming.
	 * @property {Number} [tabSize] The number of spaces in a tab.
	 */
	/**
	 * Constructs a new text view.
	 * 
	 * @param {orion.textview.TextViewOptions} options the view options.
	 * 
	 * @class A TextView is a user interface for editing text.
	 * @name orion.textview.TextView
	 * @borrows orion.textview.EventTarget#addEventListener as #addEventListener
	 * @borrows orion.textview.EventTarget#removeEventListener as #removeEventListener
	 * @borrows orion.textview.EventTarget#dispatchEvent as #dispatchEvent
	 */
	function TextView (options) {
		this._init(options);
	}
	
	TextView.prototype = /** @lends orion.textview.TextView.prototype */ {
		/**
		 * Adds a ruler to the text view at the specified position.
		 * <p>
		 * The position is relative to the ruler location.
		 * </p>
		 *
		 * @param {orion.textview.Ruler} ruler the ruler.
		 * @param {Number} [index=length] the ruler index.
		 */
		addRuler: function (ruler, index) {
			ruler.setView(this);
			var rulers = this._rulers;
			if (index !== undefined) {
				var i, sideIndex;
				for (i = 0, sideIndex=0; i < rulers.length && sideIndex < index; i++) {
					if (ruler.getLocation() === rulers[i].getLocation()) {
						sideIndex++;
					}
				}
				rulers.splice(sideIndex, 0, ruler);
				index = sideIndex;
			} else {
				rulers.push(ruler);
			}
			this._createRuler(ruler, index);
			this._updatePage();
		},
		computeSize: function() {
			var w = 0, h = 0;
			var model = this._model, clientDiv = this._clientDiv;
			if (!clientDiv) { return {width: w, height: h}; }
			var clientWidth = clientDiv.style.width;
			/*
			* Feature in WekKit. Webkit limits the width of the lines
			* computed below to the width of the client div.  This causes
			* the lines to be wrapped even though "pre" is set.  The fix
			* is to set the width of the client div to a "auto"
			* before computing the lines width.  Note that this value is
			* reset to the appropriate value further down.
			*/
			if (isWebkit) {
				clientDiv.style.width = "auto"; //$NON-NLS-0$
			}
			var lineCount = model.getLineCount();
			for (var lineIndex=0; lineIndex<lineCount; lineIndex++) {
				var child = this._getLineNode(lineIndex), dummy = null;
				if (!child || child.lineChanged || child.lineRemoved) {
					child = dummy = this._createLine(clientDiv, null, document, lineIndex, model);
				}
				var rect = this._getLineBoundingClientRect(child);
				w = Math.max(w, rect.right - rect.left);
				h += rect.bottom - rect.top;
				if (dummy) { clientDiv.removeChild(dummy); }
			}
			if (isWebkit) {
				clientDiv.style.width = clientWidth;
			}
			var viewPadding = this._getViewPadding();
			w += viewPadding.right + viewPadding.left;
			h += viewPadding.bottom + viewPadding.top;
			return {width: w, height: h};
		},
		/**
		 * Converts the given rectangle from one coordinate spaces to another.
		 * <p>The supported coordinate spaces are:
		 * <ul>
		 *   <li>"document" - relative to document, the origin is the top-left corner of first line</li>
		 *   <li>"page" - relative to html page that contains the text view</li>
		 * </ul>
		 * </p>
		 * <p>All methods in the view that take or return a position are in the document coordinate space.</p>
		 *
		 * @param rect the rectangle to convert.
		 * @param rect.x the x of the rectangle.
		 * @param rect.y the y of the rectangle.
		 * @param rect.width the width of the rectangle.
		 * @param rect.height the height of the rectangle.
		 * @param {String} from the source coordinate space.
		 * @param {String} to the destination coordinate space.
		 *
		 * @see #getLocationAtOffset
		 * @see #getOffsetAtLocation
		 * @see #getTopPixel
		 * @see #setTopPixel
		 */
		convert: function(rect, from, to) {
			if (!this._clientDiv) { return; }
			var scroll = this._getScroll();
			var viewPad = this._getViewPadding();
			var viewRect = this._viewDiv.getBoundingClientRect();
			if (from === "document") { //$NON-NLS-0$
				if (rect.x !== undefined) {
					rect.x += - scroll.x + viewRect.left + viewPad.left;
				}
				if (rect.y !== undefined) {
					rect.y += - scroll.y + viewRect.top + viewPad.top;
				}
			}
			//At this point rect is in the widget coordinate space
			if (to === "document") { //$NON-NLS-0$
				if (rect.x !== undefined) {
					rect.x += scroll.x - viewRect.left - viewPad.left;
				}
				if (rect.y !== undefined) {
					rect.y += scroll.y - viewRect.top - viewPad.top;
				}
			}
			return rect;
		},
		/**
		 * Destroys the text view. 
		 * <p>
		 * Removes the view from the page and frees all resources created by the view.
		 * Calling this function causes the "Destroy" event to be fire so that all components
		 * attached to view can release their references.
		 * </p>
		 *
		 * @see #onDestroy
		 */
		destroy: function() {
			/* Destroy rulers*/
			for (var i=0; i< this._rulers.length; i++) {
				this._rulers[i].setView(null);
			}
			this.rulers = null;
			
			this._destroyView();

			var e = {type: "Destroy"}; //$NON-NLS-0$
			this.onDestroy(e);

			this._parent = null;
			this._model = null;
			this._selection = null;
			this._doubleClickSelection = null;
			this._keyBindings = null;
			this._actions = null;
		},
		/**
		 * Gives focus to the text view.
		 */
		focus: function() {
			if (!this._clientDiv) { return; }
			/*
			* Feature in Chrome. When focus is called in the clientDiv without
			* setting selection the browser will set the selection to the first dom 
			* element, which can be above the client area. When this happen the 
			* browser also scrolls the window to show that element.
			* The fix is to call _updateDOMSelection() before calling focus().
			*/
			this._updateDOMSelection();
			if (isOpera) { this._clientDiv.blur(); }
			this._clientDiv.focus();
			/*
			* Feature in Safari. When focus is called the browser selects the clientDiv
			* itself. The fix is to call _updateDOMSelection() after calling focus().
			*/
			this._updateDOMSelection();
		},
		/**
		 * Check if the text view has focus.
		 *
		 * @returns {Boolean} <code>true</code> if the text view has focus, otherwise <code>false</code>.
		 */
		hasFocus: function() {
			return this._hasFocus;
		},
		/**
		 * Returns the action description for a given action ID.
		 *
		 * @returns {orion.textview.ActionDescrition} the action description
		 */
		getActionDescription: function(actionID) {
			var action = this._actions[actionID];
			if (action) {
				return action.actionDescription;
			}
			return undefined;
		},
		/**
		 * Returns all action IDs defined in the text view.
		 * <p>
		 * There are two types of actions, the predefined actions of the view 
		 * and the actions added by application code.
		 * </p>
		 * <p>
		 * The predefined actions are:
		 * <ul>
		 *   <li>Navigation actions. These actions move the caret collapsing the selection.</li>
		 *     <ul>
		 *       <li>"lineUp" - moves the caret up by one line</li>
		 *       <li>"lineDown" - moves the caret down by one line</li>
		 *       <li>"lineStart" - moves the caret to beginning of the current line</li>
		 *       <li>"lineEnd" - moves the caret to end of the current line </li>
		 *       <li>"charPrevious" - moves the caret to the previous character</li>
		 *       <li>"charNext" - moves the caret to the next character</li>
		 *       <li>"pageUp" - moves the caret up by one page</li>
		 *       <li>"pageDown" - moves the caret down by one page</li>
		 *       <li>"wordPrevious" - moves the caret to the previous word</li>
		 *       <li>"wordNext" - moves the caret to the next word</li>
		 *       <li>"textStart" - moves the caret to the beginning of the document</li>
		 *       <li>"textEnd" - moves the caret to the end of the document</li>
		 *     </ul>
		 *   <li>Selection actions. These actions move the caret extending the selection.</li>
		 *     <ul>
		 *       <li>"selectLineUp" - moves the caret up by one line</li>
		 *       <li>"selectLineDown" - moves the caret down by one line</li>
		 *       <li>"selectLineStart" - moves the caret to beginning of the current line</li>
		 *       <li>"selectLineEnd" - moves the caret to end of the current line </li>
		 *       <li>"selectCharPrevious" - moves the caret to the previous character</li>
		 *       <li>"selectCharNext" - moves the caret to the next character</li>
		 *       <li>"selectPageUp" - moves the caret up by one page</li>
		 *       <li>"selectPageDown" - moves the caret down by one page</li>
		 *       <li>"selectWordPrevious" - moves the caret to the previous word</li>
		 *       <li>"selectWordNext" - moves the caret to the next word</li>
		 *       <li>"selectTextStart" - moves the caret to the beginning of the document</li>
		 *       <li>"selectTextEnd" - moves the caret to the end of the document</li>
		 *       <li>"selectAll" - selects the entire document</li>
		 *     </ul>
		 *   <li>Edit actions. These actions modify the text view text</li>
		 *     <ul>
		 *       <li>"deletePrevious" - deletes the character preceding the caret</li>
		 *       <li>"deleteNext" - deletes the charecter following the caret</li>
		 *       <li>"deleteWordPrevious" - deletes the word preceding the caret</li>
		 *       <li>"deleteWordNext" - deletes the word following the caret</li>
		 *       <li>"tab" - inserts a tab character at the caret</li>
		 *       <li>"shiftTab" - noop</li>
		 *       <li>"toggleTabMode" - toggles tab mode.</li>
		 *       <li>"enter" - inserts a line delimiter at the caret</li>
		 *     </ul>
		 *   <li>Clipboard actions.</li>
		 *     <ul>
		 *       <li>"copy" - copies the selected text to the clipboard</li>
		 *       <li>"cut" - copies the selected text to the clipboard and deletes the selection</li>
		 *       <li>"paste" - replaces the selected text with the clipboard contents</li>
		 *     </ul>
		 * </ul>
		 * </p>
		 *
		 * @param {Boolean} [defaultAction=false] whether or not the predefined actions are included.
		 * @returns {String[]} an array of action IDs defined in the text view.
		 *
		 * @see #invokeAction
		 * @see #setAction
		 * @see #setKeyBinding
		 * @see #getKeyBindings
		 */
		getActions: function (defaultAction) {
			var result = [];
			var actions = this._actions;
			for (var i in actions) {
				if (actions.hasOwnProperty(i)) {
					if (!defaultAction && actions[i].defaultHandler) { continue; }
					result.push(i);
				}
			}
			return result;
		},
		/**
		 * Returns the bottom index.
		 * <p>
		 * The bottom index is the line that is currently at the bottom of the view.  This
		 * line may be partially visible depending on the vertical scroll of the view. The parameter
		 * <code>fullyVisible</code> determines whether to return only fully visible lines. 
		 * </p>
		 *
		 * @param {Boolean} [fullyVisible=false] if <code>true</code>, returns the index of the last fully visible line. This
		 *    parameter is ignored if the view is not big enough to show one line.
		 * @returns {Number} the index of the bottom line.
		 *
		 * @see #getTopIndex
		 * @see #setTopIndex
		 */
		getBottomIndex: function(fullyVisible) {
			if (!this._clientDiv) { return 0; }
			return this._getBottomIndex(fullyVisible);
		},
		/**
		 * Returns the bottom pixel.
		 * <p>
		 * The bottom pixel is the pixel position that is currently at
		 * the bottom edge of the view.  This position is relative to the
		 * beginning of the document.
		 * </p>
		 *
		 * @returns {Number} the bottom pixel.
		 *
		 * @see #getTopPixel
		 * @see #setTopPixel
		 * @see #convert
		 */
		getBottomPixel: function() {
			if (!this._clientDiv) { return 0; }
			return this._getScroll().y + this._getClientHeight();
		},
		/**
		 * Returns the caret offset relative to the start of the document.
		 *
		 * @returns the caret offset relative to the start of the document.
		 *
		 * @see #setCaretOffset
		 * @see #setSelection
		 * @see #getSelection
		 */
		getCaretOffset: function () {
			var s = this._getSelection();
			return s.getCaret();
		},
		/**
		 * Returns the client area.
		 * <p>
		 * The client area is the portion in pixels of the document that is visible. The
		 * client area position is relative to the beginning of the document.
		 * </p>
		 *
		 * @returns the client area rectangle {x, y, width, height}.
		 *
		 * @see #getTopPixel
		 * @see #getBottomPixel
		 * @see #getHorizontalPixel
		 * @see #convert
		 */
		getClientArea: function() {
			if (!this._clientDiv) { return {x: 0, y: 0, width: 0, height: 0}; }
			var scroll = this._getScroll();
			return {x: scroll.x, y: scroll.y, width: this._getClientWidth(), height: this._getClientHeight()};
		},
		/**
		 * Returns the horizontal pixel.
		 * <p>
		 * The horizontal pixel is the pixel position that is currently at
		 * the left edge of the view.  This position is relative to the
		 * beginning of the document.
		 * </p>
		 *
		 * @returns {Number} the horizontal pixel.
		 *
		 * @see #setHorizontalPixel
		 * @see #convert
		 */
		getHorizontalPixel: function() {
			if (!this._clientDiv) { return 0; }
			return this._getScroll().x;
		},
		/**
		 * Returns all the key bindings associated to the given action ID.
		 *
		 * @param {String} actionID the action ID.
		 * @returns {orion.textview.KeyBinding[]} the array of key bindings associated to the given action ID.
		 *
		 * @see #setKeyBinding
		 * @see #setAction
		 */
		getKeyBindings: function (actionID) {
			var result = [];
			var keyBindings = this._keyBindings;
			for (var i = 0; i < keyBindings.length; i++) {
				if (keyBindings[i].actionID === actionID) {
					result.push(keyBindings[i].keyBinding);
				}
			}
			return result;
		},
		/**
		 * Returns the line height for a given line index.  Returns the default line
		 * height if the line index is not specified.
		 *
		 * @param {Number} [lineIndex] the line index.
		 * @returns {Number} the height of the line in pixels.
		 *
		 * @see #getLinePixel
		 */
		getLineHeight: function(lineIndex) {
			if (!this._clientDiv) { return 0; }
			return this._getLineHeight();
		},
		/**
		 * Returns the top pixel position of a given line index relative to the beginning
		 * of the document.
		 * <p>
		 * Clamps out of range indices.
		 * </p>
		 *
		 * @param {Number} lineIndex the line index.
		 * @returns {Number} the pixel position of the line.
		 *
		 * @see #setTopPixel
		 * @see #convert
		 */
		getLinePixel: function(lineIndex) {
			if (!this._clientDiv) { return 0; }
			lineIndex = Math.min(Math.max(0, lineIndex), this._model.getLineCount());
			var lineHeight = this._getLineHeight();
			return lineHeight * lineIndex;
		},
		/**
		 * Returns the {x, y} pixel location of the top-left corner of the character
		 * bounding box at the specified offset in the document.  The pixel location
		 * is relative to the document.
		 * <p>
		 * Clamps out of range offsets.
		 * </p>
		 *
		 * @param {Number} offset the character offset
		 * @returns the {x, y} pixel location of the given offset.
		 *
		 * @see #getOffsetAtLocation
		 * @see #convert
		 */
		getLocationAtOffset: function(offset) {
			if (!this._clientDiv) { return {x: 0, y: 0}; }
			var model = this._model;
			offset = Math.min(Math.max(0, offset), model.getCharCount());
			var lineIndex = model.getLineAtOffset(offset);
			var scroll = this._getScroll();
			var viewRect = this._viewDiv.getBoundingClientRect();
			var viewPad = this._getViewPadding();
			var x = this._getOffsetToX(offset) + scroll.x - viewRect.left - viewPad.left;
			var y = this.getLinePixel(lineIndex);
			return {x: x, y: y};
		},
		/**
		 * Returns the specified view options.
		 * <p>
		 * The returned value is either a <code>orion.textview.TextViewOptions</code> or an option value. An option value is returned when only one string paremeter
		 * is specified. A <code>orion.textview.TextViewOptions</code> is returned when there are no paremeters, or the parameters are a list of options names or a
		 * <code>orion.textview.TextViewOptions</code>. All view options are returned when there no paremeters.
		 * </p>
		 *
		 * @param {String|orion.textview.TextViewOptions} [options] The options to return.
		 * @return {Object|orion.textview.TextViewOptions} The requested options or an option value.
		 *
		 * @see #setOptions
		 */
		getOptions: function() {
			var options;
			if (arguments.length === 0) {
				options = this._defaultOptions();
			} else if (arguments.length === 1) {
				var arg = arguments[0];
				if (typeof arg === "string") { //$NON-NLS-0$
					return this._clone(this["_" + arg]); //$NON-NLS-0$
				}
				options = arg;
			} else {
				options = {};
				for (var index in arguments) {
					if (arguments.hasOwnProperty(index)) {
						options[arguments[index]] = undefined;
					}
				}
			}
			for (var option in options) {
				if (options.hasOwnProperty(option)) {
					options[option] = this._clone(this["_" + option]); //$NON-NLS-0$
				}
			}
			return options;
		},
		/**
		 * Returns the text model of the text view.
		 *
		 * @returns {orion.textview.TextModel} the text model of the view.
		 */
		getModel: function() {
			return this._model;
		},
		/**
		 * Returns the character offset nearest to the given pixel location.  The
		 * pixel location is relative to the document.
		 *
		 * @param x the x of the location
		 * @param y the y of the location
		 * @returns the character offset at the given location.
		 *
		 * @see #getLocationAtOffset
		 */
		getOffsetAtLocation: function(x, y) {
			if (!this._clientDiv) { return 0; }
			var scroll = this._getScroll();
			var viewRect = this._viewDiv.getBoundingClientRect();
			var viewPad = this._getViewPadding();
			var lineIndex = this._getYToLine(y - scroll.y + viewRect.top + viewPad.top);
			x += -scroll.x + viewRect.left + viewPad.left;
			var offset = this._getXToOffset(lineIndex, x);
			return offset;
		},
		/**
		 * Get the view rulers.
		 *
		 * @returns the view rulers
		 *
		 * @see #addRuler
		 */
		getRulers: function() {
			return this._rulers.slice(0);
		},
		/**
		 * Returns the text view selection.
		 * <p>
		 * The selection is defined by a start and end character offset relative to the
		 * document. The character at end offset is not included in the selection.
		 * </p>
		 * 
		 * @returns {orion.textview.Selection} the view selection
		 *
		 * @see #setSelection
		 */
		getSelection: function () {
			var s = this._getSelection();
			return {start: s.start, end: s.end};
		},
		/**
		 * Returns the text for the given range.
		 * <p>
		 * The text does not include the character at the end offset.
		 * </p>
		 *
		 * @param {Number} [start=0] the start offset of text range.
		 * @param {Number} [end=char count] the end offset of text range.
		 *
		 * @see #setText
		 */
		getText: function(start, end) {
			var model = this._model;
			return model.getText(start, end);
		},
		/**
		 * Returns the top index.
		 * <p>
		 * The top index is the line that is currently at the top of the view.  This
		 * line may be partially visible depending on the vertical scroll of the view. The parameter
		 * <code>fullyVisible</code> determines whether to return only fully visible lines. 
		 * </p>
		 *
		 * @param {Boolean} [fullyVisible=false] if <code>true</code>, returns the index of the first fully visible line. This
		 *    parameter is ignored if the view is not big enough to show one line.
		 * @returns {Number} the index of the top line.
		 *
		 * @see #getBottomIndex
		 * @see #setTopIndex
		 */
		getTopIndex: function(fullyVisible) {
			if (!this._clientDiv) { return 0; }
			return this._getTopIndex(fullyVisible);
		},
		/**
		 * Returns the top pixel.
		 * <p>
		 * The top pixel is the pixel position that is currently at
		 * the top edge of the view.  This position is relative to the
		 * beginning of the document.
		 * </p>
		 *
		 * @returns {Number} the top pixel.
		 *
		 * @see #getBottomPixel
		 * @see #setTopPixel
		 * @see #convert
		 */
		getTopPixel: function() {
			if (!this._clientDiv) { return 0; }
			return this._getScroll().y;
		},
		/**
		 * Executes the action handler associated with the given action ID.
		 * <p>
		 * The application defined action takes precedence over predefined actions unless
		 * the <code>defaultAction</code> paramater is <code>true</code>.
		 * </p>
		 * <p>
		 * If the application defined action returns <code>false</code>, the text view predefined
		 * action is executed if present.
		 * </p>
		 *
		 * @param {String} actionID the action ID.
		 * @param {Boolean} [defaultAction] whether to always execute the predefined action.
		 * @returns {Boolean} <code>true</code> if the action was executed.
		 *
		 * @see #setAction
		 * @see #getActions
		 */
		invokeAction: function (actionID, defaultAction) {
			if (!this._clientDiv) { return; }
			var action = this._actions[actionID];
			if (action) {
				if (!defaultAction && action.handler) {
					if (action.handler()) { return; }
				}
				if (action.defaultHandler) { return action.defaultHandler(); }
			}
			return false;
		},
		/**
		* Returns if the view is destroyed.
		* @returns {Boolean} <code>true</code> if the view is destroyed.
		*/
		isDestroyed: function () {
			return !this._clientDiv;
		},
		/** 
		 * @class This is the event sent when the user right clicks or otherwise invokes the context menu of the view. 
		 * <p> 
		 * <b>See:</b><br/> 
		 * {@link orion.textview.TextView}<br/> 
		 * {@link orion.textview.TextView#event:onContextMenu} 
		 * </p> 
		 * 
		 * @name orion.textview.ContextMenuEvent 
		 * 
		 * @property {Number} x The pointer location on the x axis, relative to the document the user is editing. 
		 * @property {Number} y The pointer location on the y axis, relative to the document the user is editing. 
		 * @property {Number} screenX The pointer location on the x axis, relative to the screen. This is copied from the DOM contextmenu event.screenX property. 
		 * @property {Number} screenY The pointer location on the y axis, relative to the screen. This is copied from the DOM contextmenu event.screenY property. 
		 * @property {Boolean} preventDefault Determines whether the user agent context menu should be shown. It is not shown by default.
		 */ 
		/** 
		 * This event is sent when the user invokes the view context menu. 
		 * 
		 * @event 
		 * @param {orion.textview.ContextMenuEvent} contextMenuEvent the event 
		 */ 
		onContextMenu: function(contextMenuEvent) {
			return this.dispatchEvent(contextMenuEvent); 
		}, 
		onDragStart: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		onDrag: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		onDragEnd: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		onDragEnter: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		onDragOver: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		onDragLeave: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		onDrop: function(dragEvent) {
			return this.dispatchEvent(dragEvent);
		},
		/**
		 * @class This is the event sent when the text view is destroyed.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onDestroy}
		 * </p>
		 * @name orion.textview.DestroyEvent
		 */
		/**
		 * This event is sent when the text view has been destroyed.
		 *
		 * @event
		 * @param {orion.textview.DestroyEvent} destroyEvent the event
		 *
		 * @see #destroy
		 */
		onDestroy: function(destroyEvent) {
			return this.dispatchEvent(destroyEvent);
		},
		/**
		 * @class This object is used to define style information for the text view.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onLineStyle}
		 * </p>		 
		 * @name orion.textview.Style
		 * 
		 * @property {String} styleClass A CSS class name.
		 * @property {Object} style An object with CSS properties.
		 * @property {String} tagName A DOM tag name.
		 * @property {Object} attributes An object with DOM attributes.
		 */
		/**
		 * @class This object is used to style range.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onLineStyle}
		 * </p>		 
		 * @name orion.textview.StyleRange
		 * 
		 * @property {Number} start The start character offset, relative to the document, where the style should be applied.
		 * @property {Number} end The end character offset (exclusive), relative to the document, where the style should be applied.
		 * @property {orion.textview.Style} style The style for the range.
		 */
		/**
		 * @class This is the event sent when the text view needs the style information for a line.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onLineStyle}
		 * </p>		 
		 * @name orion.textview.LineStyleEvent
		 * 
		 * @property {orion.textview.TextView} textView The text view.		 
		 * @property {Number} lineIndex The line index.
		 * @property {String} lineText The line text.
		 * @property {Number} lineStart The character offset, relative to document, of the first character in the line.
		 * @property {orion.textview.Style} style The style for the entire line (output argument).
		 * @property {orion.textview.StyleRange[]} ranges An array of style ranges for the line (output argument).		 
		 */
		/**
		 * This event is sent when the text view needs the style information for a line.
		 *
		 * @event
		 * @param {orion.textview.LineStyleEvent} lineStyleEvent the event
		 */
		onLineStyle: function(lineStyleEvent) {
			return this.dispatchEvent(lineStyleEvent);
		},
		/**
		 * @class This is the event sent when the text in the model has changed.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onModelChanged}<br/>
		 * {@link orion.textview.TextModel#onChanged}
		 * </p>
		 * @name orion.textview.ModelChangedEvent
		 * 
		 * @property {Number} start The character offset in the model where the change has occurred.
		 * @property {Number} removedCharCount The number of characters removed from the model.
		 * @property {Number} addedCharCount The number of characters added to the model.
		 * @property {Number} removedLineCount The number of lines removed from the model.
		 * @property {Number} addedLineCount The number of lines added to the model.
		 */
		/**
		 * This event is sent when the text in the model has changed.
		 *
		 * @event
		 * @param {orion.textview.ModelChangedEvent} modelChangedEvent the event
		 */
		onModelChanged: function(modelChangedEvent) {
			return this.dispatchEvent(modelChangedEvent);
		},
		/**
		 * @class This is the event sent when the text in the model is about to change.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onModelChanging}<br/>
		 * {@link orion.textview.TextModel#onChanging}
		 * </p>
		 * @name orion.textview.ModelChangingEvent
		 * 
		 * @property {String} text The text that is about to be inserted in the model.
		 * @property {Number} start The character offset in the model where the change will occur.
		 * @property {Number} removedCharCount The number of characters being removed from the model.
		 * @property {Number} addedCharCount The number of characters being added to the model.
		 * @property {Number} removedLineCount The number of lines being removed from the model.
		 * @property {Number} addedLineCount The number of lines being added to the model.
		 */
		/**
		 * This event is sent when the text in the model is about to change.
		 *
		 * @event
		 * @param {orion.textview.ModelChangingEvent} modelChangingEvent the event
		 */
		onModelChanging: function(modelChangingEvent) {
			return this.dispatchEvent(modelChangingEvent);
		},
		/**
		 * @class This is the event sent when the text is modified by the text view.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onModify}
		 * </p>
		 * @name orion.textview.ModifyEvent
		 */
		/**
		 * This event is sent when the text view has changed text in the model.
		 * <p>
		 * If the text is changed directly through the model API, this event
		 * is not sent.
		 * </p>
		 *
		 * @event
		 * @param {orion.textview.ModifyEvent} modifyEvent the event
		 */
		onModify: function(modifyEvent) {
			return this.dispatchEvent(modifyEvent);
		},
		onMouseDown: function(mouseEvent) {
			return this.dispatchEvent(mouseEvent);
		},
		onMouseUp: function(mouseEvent) {
			return this.dispatchEvent(mouseEvent);
		},
		onMouseMove: function(mouseEvent) {
			return this.dispatchEvent(mouseEvent);
		},
		onMouseOver: function(mouseEvent) {
			return this.dispatchEvent(mouseEvent);
		},
		onMouseOut: function(mouseEvent) {
			return this.dispatchEvent(mouseEvent);
		},
		/**
		 * @class This is the event sent when the selection changes in the text view.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onSelection}
		 * </p>		 
		 * @name orion.textview.SelectionEvent
		 * 
		 * @property {orion.textview.Selection} oldValue The old selection.
		 * @property {orion.textview.Selection} newValue The new selection.
		 */
		/**
		 * This event is sent when the text view selection has changed.
		 *
		 * @event
		 * @param {orion.textview.SelectionEvent} selectionEvent the event
		 */
		onSelection: function(selectionEvent) {
			return this.dispatchEvent(selectionEvent);
		},
		/**
		 * @class This is the event sent when the text view scrolls.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onScroll}
		 * </p>		 
		 * @name orion.textview.ScrollEvent
		 * 
		 * @property oldValue The old scroll {x,y}.
		 * @property newValue The new scroll {x,y}.
		 */
		/**
		 * This event is sent when the text view scrolls vertically or horizontally.
		 *
		 * @event
		 * @param {orion.textview.ScrollEvent} scrollEvent the event
		 */
		onScroll: function(scrollEvent) {
			return this.dispatchEvent(scrollEvent);
		},
		/**
		 * @class This is the event sent when the text is about to be modified by the text view.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onVerify}
		 * </p>
		 * @name orion.textview.VerifyEvent
		 * 
		 * @property {String} text The text being inserted.
		 * @property {Number} start The start offset of the text range to be replaced.
		 * @property {Number} end The end offset (exclusive) of the text range to be replaced.
		 */
		/**
		 * This event is sent when the text view is about to change text in the model.
		 * <p>
		 * If the text is changed directly through the model API, this event
		 * is not sent.
		 * </p>
		 * <p>
		 * Listeners are allowed to change these parameters. Setting text to null
		 * or undefined stops the change.
		 * </p>
		 *
		 * @event
		 * @param {orion.textview.VerifyEvent} verifyEvent the event
		 */
		onVerify: function(verifyEvent) {
			return this.dispatchEvent(verifyEvent);
		},
		/**
		 * @class This is the event sent when the text view is focused.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onFocus}<br/>
		 * </p>
		 * @name orion.textview.FocusEvent
		 */
		/**
		 * This event is sent when the text view is focused.
		 *
		 * @event
		 * @param {orion.textview.FocusEvent} focusEvent the event
		 */
		onFocus: function(focusEvent) {
			return this.dispatchEvent(focusEvent);
		},
		/**
		 * @class This is the event sent when the text view goes out of focus.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#event:onBlur}<br/>
		 * </p>
		 * @name orion.textview.BlurEvent
		 */
		/**
		 * This event is sent when the text view goes out of focus.
		 *
		 * @event
		 * @param {orion.textview.BlurEvent} blurEvent the event
		 */
		onBlur: function(blurEvent) {
			return this.dispatchEvent(blurEvent);
		},
		/**
		 * Redraws the entire view, including rulers.
		 *
		 * @see #redrawLines
		 * @see #redrawRange
		 * @see #setRedraw
		 */
		redraw: function() {
			if (this._redrawCount > 0) { return; }
			var lineCount = this._model.getLineCount();
			var rulers = this.getRulers();
			for (var i = 0; i < rulers.length; i++) {
				this.redrawLines(0, lineCount, rulers[i]);
			}
			this.redrawLines(0, lineCount); 
		},
		/**
		 * Redraws the text in the given line range.
		 * <p>
		 * The line at the end index is not redrawn.
		 * </p>
		 *
		 * @param {Number} [startLine=0] the start line
		 * @param {Number} [endLine=line count] the end line
		 *
		 * @see #redraw
		 * @see #redrawRange
		 * @see #setRedraw
		 */
		redrawLines: function(startLine, endLine, ruler) {
			if (this._redrawCount > 0) { return; }
			if (startLine === undefined) { startLine = 0; }
			if (endLine === undefined) { endLine = this._model.getLineCount(); }
			if (startLine === endLine) { return; }
			var div = this._clientDiv;
			if (!div) { return; }
			if (ruler) {
				var location = ruler.getLocation();//"left" or "right"
				var divRuler = location === "left" ? this._leftDiv : this._rightDiv; //$NON-NLS-0$
				var cells = divRuler.firstChild.rows[0].cells;
				for (var i = 0; i < cells.length; i++) {
					if (cells[i].firstChild._ruler === ruler) {
						div = cells[i].firstChild;
						break;
					}
				}
			}
			if (ruler) {
				div.rulerChanged = true;
			}
			if (!ruler || ruler.getOverview() === "page") { //$NON-NLS-0$
				var child = div.firstChild;
				while (child) {
					var lineIndex = child.lineIndex;
					if (startLine <= lineIndex && lineIndex < endLine) {
						child.lineChanged = true;
					}
					child = child.nextSibling;
				}
			}
			if (!ruler) {
				if (startLine <= this._maxLineIndex && this._maxLineIndex < endLine) {
					this._checkMaxLineIndex = this._maxLineIndex;
					this._maxLineIndex = -1;
					this._maxLineWidth = 0;
				}
			}
			this._queueUpdatePage();
		},
		/**
		 * Redraws the text in the given range.
		 * <p>
		 * The character at the end offset is not redrawn.
		 * </p>
		 *
		 * @param {Number} [start=0] the start offset of text range
		 * @param {Number} [end=char count] the end offset of text range
		 *
		 * @see #redraw
		 * @see #redrawLines
		 * @see #setRedraw
		 */
		redrawRange: function(start, end) {
			if (this._redrawCount > 0) { return; }
			var model = this._model;
			if (start === undefined) { start = 0; }
			if (end === undefined) { end = model.getCharCount(); }
			var startLine = model.getLineAtOffset(start);
			var endLine = model.getLineAtOffset(Math.max(start, end - 1)) + 1;
			this.redrawLines(startLine, endLine);
		},
		/**
		 * Removes a ruler from the text view.
		 *
		 * @param {orion.textview.Ruler} ruler the ruler.
		 */
		removeRuler: function (ruler) {
			var rulers = this._rulers;
			for (var i=0; i<rulers.length; i++) {
				if (rulers[i] === ruler) {
					rulers.splice(i, 1);
					ruler.setView(null);
					this._destroyRuler(ruler);
					this._updatePage();
					break;
				}
			}
		},
		resize: function() {
			if (!this._clientDiv) { return; }
			this._handleResize(null);
		},
		/**
		 * @class This object describes an action for the text view.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.TextView}<br/>
		 * {@link orion.textview.TextView#setAction}
		 * </p>		 
		 * @name orion.textview.ActionDescription
		 *
		 * @property {String} [name] the name to be used when showing the action as text.
		 */
		/**
		 * Associates an application defined handler to an action ID.
		 * <p>
		 * If the action ID is a predefined action, the given handler executes before
		 * the default action handler.  If the given handler returns <code>true</code>, the
		 * default action handler is not called.
		 * </p>
		 *
		 * @param {String} actionID the action ID.
		 * @param {Function} handler the action handler.
		 * @param {orion.textview.ActionDescription} [actionDescription=undefined] the action description.
		 *
		 * @see #getActions
		 * @see #invokeAction
		 */
		setAction: function(actionID, handler, actionDescription) {
			if (!actionID) { return; }
			var actions = this._actions;
			var action = actions[actionID];
			if (!action) { 
				action = actions[actionID] = {};
			}
			action.handler = handler;
			action.actionDescription = actionDescription;
		},
		/**
		 * Associates a key binding with the given action ID. Any previous
		 * association with the specified key binding is overwriten. If the
		 * action ID is <code>null</code>, the association is removed.
		 * 
		 * @param {orion.textview.KeyBinding} keyBinding the key binding
		 * @param {String} actionID the action ID
		 */
		setKeyBinding: function(keyBinding, actionID) {
			var keyBindings = this._keyBindings;
			for (var i = 0; i < keyBindings.length; i++) {
				var kb = keyBindings[i]; 
				if (kb.keyBinding.equals(keyBinding)) {
					if (actionID) {
						kb.actionID = actionID;
					} else {
						if (kb.predefined) {
							kb.actionID = null;
						} else {
							keyBindings.splice(i, 1);
						}
					}
					return;
				}
			}
			if (actionID) {
				keyBindings.push({keyBinding: keyBinding, actionID: actionID});
			}
		},
		/**
		 * Sets the caret offset relative to the start of the document.
		 *
		 * @param {Number} caret the caret offset relative to the start of the document.
		 * @param {Boolean} [show=true] if <code>true</code>, the view will scroll if needed to show the caret location.
		 *
		 * @see #getCaretOffset
		 * @see #setSelection
		 * @see #getSelection
		 */
		setCaretOffset: function(offset, show) {
			var charCount = this._model.getCharCount();
			offset = Math.max(0, Math.min (offset, charCount));
			var selection = new Selection(offset, offset, false);
			this._setSelection (selection, show === undefined || show);
		},
		/**
		 * Sets the horizontal pixel.
		 * <p>
		 * The horizontal pixel is the pixel position that is currently at
		 * the left edge of the view.  This position is relative to the
		 * beginning of the document.
		 * </p>
		 *
		 * @param {Number} pixel the horizontal pixel.
		 *
		 * @see #getHorizontalPixel
		 * @see #convert
		 */
		setHorizontalPixel: function(pixel) {
			if (!this._clientDiv) { return; }
			pixel = Math.max(0, pixel);
			this._scrollView(pixel - this._getScroll().x, 0);
		},
		/**
		 * Sets whether the view should update the DOM.
		 * <p>
		 * This can be used to improve the performance.
		 * </p><p>
		 * When the flag is set to <code>true</code>,
		 * the entire view is marked as needing to be redrawn. 
		 * Nested calls to this method are stacked.
		 * </p>
		 *
		 * @param {Boolean} redraw the new redraw state
		 * 
		 * @see #redraw
		 */
		setRedraw: function(redraw) {
			if (redraw) {
				if (--this._redrawCount === 0) {
					this.redraw();
				}
			} else {
				this._redrawCount++;
			}
		},
		/**
		 * Sets the text model of the text view.
		 *
		 * @param {orion.textview.TextModel} model the text model of the view.
		 */
		setModel: function(model) {
			if (!model) { return; }
			if (model === this._model) { return; }
			this._model.removeEventListener("Changing", this._modelListener.onChanging); //$NON-NLS-0$
			this._model.removeEventListener("Changed", this._modelListener.onChanged); //$NON-NLS-0$
			var oldLineCount = this._model.getLineCount();
			var oldCharCount = this._model.getCharCount();
			var newLineCount = model.getLineCount();
			var newCharCount = model.getCharCount();
			var newText = model.getText();
			var e = {
				type: "ModelChanging", //$NON-NLS-0$
				text: newText,
				start: 0,
				removedCharCount: oldCharCount,
				addedCharCount: newCharCount,
				removedLineCount: oldLineCount,
				addedLineCount: newLineCount
			};
			this.onModelChanging(e);
			this._model = model;
			e = {
				type: "ModelChanged", //$NON-NLS-0$
				start: 0,
				removedCharCount: oldCharCount,
				addedCharCount: newCharCount,
				removedLineCount: oldLineCount,
				addedLineCount: newLineCount
			};
			this.onModelChanged(e); 
			this._model.addEventListener("Changing", this._modelListener.onChanging); //$NON-NLS-0$
			this._model.addEventListener("Changed", this._modelListener.onChanged); //$NON-NLS-0$
			this._reset();
			this._updatePage();
		},
		/**
		 * Sets the view options for the view.
		 *
		 * @param {orion.textview.TextViewOptions} options the view options.
		 * 
		 * @see #getOptions
		 */
		setOptions: function (options) {
			var defaultOptions = this._defaultOptions();
			for (var option in options) {
				if (options.hasOwnProperty(option)) {
					var newValue = options[option], oldValue = this["_" + option]; //$NON-NLS-0$
					if (this._compare(oldValue, newValue)) { continue; }
					var update = defaultOptions[option] ? defaultOptions[option].update : null;
					if (update) {
						update.call(this, newValue);
						continue;
					}
					this["_" + option] = this._clone(newValue); //$NON-NLS-0$
				}
			}
		},
		/**
		 * Sets the text view selection.
		 * <p>
		 * The selection is defined by a start and end character offset relative to the
		 * document. The character at end offset is not included in the selection.
		 * </p>
		 * <p>
		 * The caret is always placed at the end offset. The start offset can be
		 * greater than the end offset to place the caret at the beginning of the
		 * selection.
		 * </p>
		 * <p>
		 * Clamps out of range offsets.
		 * </p>
		 * 
		 * @param {Number} start the start offset of the selection
		 * @param {Number} end the end offset of the selection
		 * @param {Boolean} [show=true] if <code>true</code>, the view will scroll if needed to show the caret location.
		 *
		 * @see #getSelection
		 */
		setSelection: function (start, end, show) {
			var caret = start > end;
			if (caret) {
				var tmp = start;
				start = end;
				end = tmp;
			}
			var charCount = this._model.getCharCount();
			start = Math.max(0, Math.min (start, charCount));
			end = Math.max(0, Math.min (end, charCount));
			var selection = new Selection(start, end, caret);
			this._setSelection(selection, show === undefined || show);
		},
		/**
		 * Replaces the text in the given range with the given text.
		 * <p>
		 * The character at the end offset is not replaced.
		 * </p>
		 * <p>
		 * When both <code>start</code> and <code>end</code> parameters
		 * are not specified, the text view places the caret at the beginning
		 * of the document and scrolls to make it visible.
		 * </p>
		 *
		 * @param {String} text the new text.
		 * @param {Number} [start=0] the start offset of text range.
		 * @param {Number} [end=char count] the end offset of text range.
		 *
		 * @see #getText
		 */
		setText: function (text, start, end) {
			var reset = start === undefined && end === undefined;
			if (start === undefined) { start = 0; }
			if (end === undefined) { end = this._model.getCharCount(); }
			this._modifyContent({text: text, start: start, end: end, _code: true}, !reset);
			if (reset) {
				this._columnX = -1;
				this._setSelection(new Selection (0, 0, false), true);
				
				/*
				* Bug in Firefox.  For some reason, the caret does not show after the
				* view is refreshed.  The fix is to toggle the contentEditable state and
				* force the clientDiv to loose and receive focus if it is focused.
				*/
				if (isFirefox) {
					this._fixCaret();
				}
			}
		},
		/**
		 * Sets the top index.
		 * <p>
		 * The top index is the line that is currently at the top of the text view.  This
		 * line may be partially visible depending on the vertical scroll of the view.
		 * </p>
		 *
		 * @param {Number} topIndex the index of the top line.
		 *
		 * @see #getBottomIndex
		 * @see #getTopIndex
		 */
		setTopIndex: function(topIndex) {
			if (!this._clientDiv) { return; }
			var model = this._model;
			if (model.getCharCount() === 0) {
				return;
			}
			var lineCount = model.getLineCount();
			var lineHeight = this._getLineHeight();
			var pageSize = Math.max(1, Math.min(lineCount, Math.floor(this._getClientHeight () / lineHeight)));
			if (topIndex < 0) {
				topIndex = 0;
			} else if (topIndex > lineCount - pageSize) {
				topIndex = lineCount - pageSize;
			}
			var pixel = topIndex * lineHeight - this._getScroll().y;
			this._scrollView(0, pixel);
		},
		/**
		 * Sets the top pixel.
		 * <p>
		 * The top pixel is the pixel position that is currently at
		 * the top edge of the view.  This position is relative to the
		 * beginning of the document.
		 * </p>
		 *
		 * @param {Number} pixel the top pixel.
		 *
		 * @see #getBottomPixel
		 * @see #getTopPixel
		 * @see #convert
		 */
		setTopPixel: function(pixel) {
			if (!this._clientDiv) { return; }
			var lineHeight = this._getLineHeight();
			var clientHeight = this._getClientHeight();
			var lineCount = this._model.getLineCount();
			pixel = Math.min(Math.max(0, pixel), lineHeight * lineCount - clientHeight);
			this._scrollView(0, pixel - this._getScroll().y);
		},
		/**
		 * Scrolls the selection into view if needed.
		 *
		 * @returns true if the view was scrolled. 
		 *
		 * @see #getSelection
		 * @see #setSelection
		 */
		showSelection: function() {
			return this._showCaret(true);
		},
		update: function(styleChanged, sync) {
			if (!this._clientDiv) { return; }
			if (styleChanged) {
				this._updateStyle();
			}
			if (sync === undefined || sync) {
				this._updatePage();
			} else {
				this._queueUpdatePage();
			}
		},
		
		/**************************************** Event handlers *********************************/
		_handleRootMouseDown: function (e) {
			if (!e) { e = window.event; }
			if (isFirefox && e.which === 1) {
				this._clientDiv.contentEditable = false;
				(this._overlayDiv || this._clientDiv).draggable = true;
				this._ignoreBlur = true;
			}
			
			/* Prevent clicks outside of the client div from taking focus away. */
			var topNode = this._overlayDiv || this._clientDiv;
			/* Use view div on IE 8 otherwise it is not possible to scroll. */
			if (isIE < 9) { topNode = this._viewDiv; }
			var temp = e.target ? e.target : e.srcElement;
			while (temp) {
				if (topNode === temp) {
					return;
				}
				temp = temp.parentNode;
			}
			if (e.preventDefault) { e.preventDefault(); }
			if (e.stopPropagation){ e.stopPropagation(); }
			if (!isW3CEvents) {
				/*
				* In IE 8 is not possible to prevent the default handler from running
				* during mouse down event using usual API. The workaround is to give
				* focus back to the client div.
				*/ 
				var self = this;
				setTimeout(function() {
					self._clientDiv.focus();
				}, 0);
			}
		},
		_handleRootMouseUp: function (e) {
			if (!e) { e = window.event; }
			if (isFirefox && e.which === 1) {
				this._clientDiv.contentEditable = true;
				(this._overlayDiv || this._clientDiv).draggable = false;
				
				/*
				* Bug in Firefox.  For some reason, Firefox stops showing the caret
				* in some cases. For example when the user cancels a drag operation 
				* by pressing ESC.  The fix is to detect that the drag operation was
				* cancelled,  toggle the contentEditable state and force the clientDiv
				* to loose and receive focus if it is focused.
				*/
				this._fixCaret();
				this._ignoreBlur = false;
			}
		},
		_handleBlur: function (e) {
			if (!e) { e = window.event; }
			if (this._ignoreBlur) { return; }
			this._hasFocus = false;
			/*
			* Bug in IE 8 and earlier. For some reason when text is deselected
			* the overflow selection at the end of some lines does not get redrawn.
			* The fix is to create a DOM element in the body to force a redraw.
			*/
			if (isIE < 9) {
				if (!this._getSelection().isEmpty()) {
					var child = document.createElement("DIV"); //$NON-NLS-0$
					var rootDiv = this._rootDiv;
					rootDiv.appendChild(child);
					rootDiv.removeChild(child);
				}
			}
			if (this._selDiv1) {
				var color = "lightgray"; //$NON-NLS-0$
				this._selDiv1.style.background = color;
				this._selDiv2.style.background = color;
				this._selDiv3.style.background = color;
				/* Clear browser selection if selection is within clientDiv */
				var temp;
				if (window.getSelection) {
					var sel = window.getSelection();
					temp = sel.anchorNode;
					while (temp) {
						if (temp === this._clientDiv) {
							if (sel.rangeCount > 0) { sel.removeAllRanges(); }
							break;
						}
						temp = temp.parentNode;
					}
				} else if (document.selection) {
					this._ignoreSelect = false;
					temp = document.selection.createRange().parentElement();
					while (temp) {
						if (temp === this._clientDiv) {
							document.selection.empty();
							break;
						}
						temp = temp.parentNode;
					}
					this._ignoreSelect = true;
				}
			}
			if (!this._ignoreFocus) {
				this.onBlur({type: "Blur"}); //$NON-NLS-0$
			}
		},
		_handleContextMenu: function (e) {
			if (!e) { e = window.event; }
			if (isIE && this._lastMouseButton === 3) {
				// We need to update the DOM selection, because on
				// right-click the caret moves to the mouse location.
				// See bug 366312 and 376508.
				this._updateDOMSelection();
			}
			var preventDefault = true;
			if (this.isListening("ContextMenu")) { //$NON-NLS-0$
				var evt = this._createMouseEvent("ContextMenu", e); //$NON-NLS-0$
				evt.screenX = e.screenX;
				evt.screenY = e.screenY;
				evt.preventDefault = true;
				this.onContextMenu(evt);
				preventDefault = evt.preventDefault;
			}
			if (preventDefault) {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleCopy: function (e) {
			if (this._ignoreCopy) { return; }
			if (!e) { e = window.event; }
			if (this._doCopy(e)) {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleCut: function (e) {
			if (!e) { e = window.event; }
			if (this._doCut(e)) {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleDataModified: function(e) {
			this._startIME();
		},
		_handleDblclick: function (e) {
			if (!e) { e = window.event; }
			var time = e.timeStamp ? e.timeStamp : new Date().getTime();
			this._lastMouseTime = time;
			if (this._clickCount !== 2) {
				this._clickCount = 2;
				this._handleMouse(e);
			}
		},
		_handleDragStart: function (e) {
			if (!e) { e = window.event; }
			if (isFirefox) {
				var self = this;
				setTimeout(function() {
					self._clientDiv.contentEditable = true;
					self._clientDiv.draggable = false;
					self._ignoreBlur = false;
				}, 0);
			}
			if (this.isListening("DragStart") && this._dragOffset !== -1) { //$NON-NLS-0$
				this._isMouseDown = false;
				this.onDragStart(this._createMouseEvent("DragStart", e)); //$NON-NLS-0$
				this._dragOffset = -1;
			} else {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleDrag: function (e) {
			if (!e) { e = window.event; }
			if (this.isListening("Drag")) { //$NON-NLS-0$
				this.onDrag(this._createMouseEvent("Drag", e)); //$NON-NLS-0$
			}
		},
		_handleDragEnd: function (e) {
			if (!e) { e = window.event; }
			this._dropTarget = false;
			this._dragOffset = -1;
			if (this.isListening("DragEnd")) { //$NON-NLS-0$
				this.onDragEnd(this._createMouseEvent("DragEnd", e)); //$NON-NLS-0$
			}
			if (isFirefox) {
				this._fixCaret();
				/*
				* Bug in Firefox.  For some reason, Firefox stops showing the caret when the 
				* selection is dropped onto itself. The fix is to detected the case and 
				* call fixCaret() a second time.
				*/
				if (e.dataTransfer.dropEffect === "none" && !e.dataTransfer.mozUserCancelled) { //$NON-NLS-0$
					this._fixCaret();
				}
			}
		},
		_handleDragEnter: function (e) {
			if (!e) { e = window.event; }
			var prevent = true;
			this._dropTarget = true;
			if (this.isListening("DragEnter")) { //$NON-NLS-0$
				prevent = false;
				this.onDragEnter(this._createMouseEvent("DragEnter", e)); //$NON-NLS-0$
			}
			/*
			* Webkit will not send drop events if this event is not prevented, as spec in HTML5.
			* Firefox and IE do not follow this spec for contentEditable. Note that preventing this 
			* event will result is loss of functionality (insertion mark, etc).
			*/
			if (isWebkit || prevent) {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleDragOver: function (e) {
			if (!e) { e = window.event; }
			var prevent = true;
			if (this.isListening("DragOver")) { //$NON-NLS-0$
				prevent = false;
				this.onDragOver(this._createMouseEvent("DragOver", e)); //$NON-NLS-0$
			}
			/*
			* Webkit will not send drop events if this event is not prevented, as spec in HTML5.
			* Firefox and IE do not follow this spec for contentEditable. Note that preventing this 
			* event will result is loss of functionality (insertion mark, etc).
			*/
			if (isWebkit || prevent) {
				if (prevent) { e.dataTransfer.dropEffect = "none"; } //$NON-NLS-0$
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleDragLeave: function (e) {
			if (!e) { e = window.event; }
			this._dropTarget = false;
			if (this.isListening("DragLeave")) { //$NON-NLS-0$
				this.onDragLeave(this._createMouseEvent("DragLeave", e)); //$NON-NLS-0$
			}
		},
		_handleDrop: function (e) {
			if (!e) { e = window.event; }
			this._dropTarget = false;
			if (this.isListening("Drop")) { //$NON-NLS-0$
				this.onDrop(this._createMouseEvent("Drop", e)); //$NON-NLS-0$
			}
			/*
			* This event must be prevented otherwise the user agent will modify
			* the DOM. Note that preventing the event on some user agents (i.e. IE)
			* indicates that the operation is cancelled. This causes the dropEffect to 
			* be set to none  in the dragend event causing the implementor to not execute
			* the code responsible by the move effect.
			*/
			if (e.preventDefault) { e.preventDefault(); }
			return false;
		},
		_handleFocus: function (e) {
			if (!e) { e = window.event; }
			this._hasFocus = true;
			if (isPad && this._lastTouchOffset !== undefined) {
				this.setCaretOffset(this._lastTouchOffset, true);
				this._lastTouchOffset = undefined;
			} else {
				this._updateDOMSelection();
			}
			if (this._selDiv1) {
				var color = this._highlightRGB;
				this._selDiv1.style.background = color;
				this._selDiv2.style.background = color;
				this._selDiv3.style.background = color;
			}
			if (!this._ignoreFocus) {
				this.onFocus({type: "Focus"}); //$NON-NLS-0$
			}
		},
		_handleKeyDown: function (e) {
			if (!e) { e = window.event; }
			var modifier = false;
			switch (e.keyCode) {
				case 16: /* Shift */
				case 17: /* Control */
				case 18: /* Alt */
				case 91: /* Command */
					modifier = true;
					break;
				default:
					this._setLinksVisible(false);
			}
			if (e.keyCode === 229) {
				if (this._readonly) {
					if (e.preventDefault) { e.preventDefault(); }
					return false;
				}
				var startIME = true;
				
				/*
				* Bug in Safari. Some Control+key combinations send key events
				* with keyCode equals to 229. This is unexpected and causes the
				* view to start an IME composition. The fix is to ignore these
				* events.
				*/
				if (isSafari && isMac) {
					if (e.ctrlKey) {
						startIME = false;
					}
				}
				if (startIME) {
					this._startIME();
				}
			} else {
				if (!modifier) {
					this._commitIME();
				}
			}
			/*
			* Feature in Firefox. When a key is held down the browser sends 
			* right number of keypress events but only one keydown. This is
			* unexpected and causes the view to only execute an action
			* just one time. The fix is to ignore the keydown event and 
			* execute the actions from the keypress handler.
			* Note: This only happens on the Mac and Linux (Firefox 3.6).
			*
			* Feature in Opera.  Opera sends keypress events even for non-printable
			* keys.  The fix is to handle actions in keypress instead of keydown.
			*/
			if (((isMac || isLinux) && isFirefox < 4) || isOpera) {
				this._keyDownEvent = e;
				return true;
			}
			
			if (this._doAction(e)) {
				if (e.preventDefault) {
					e.preventDefault(); 
					e.stopPropagation(); 
				} else {
					e.cancelBubble = true;
					e.returnValue = false;
					e.keyCode = 0;
				}
				return false;
			}
		},
		_handleKeyPress: function (e) {
			if (!e) { e = window.event; }
			/*
			* Feature in Embedded WebKit.  Embedded WekKit on Mac runs in compatibility mode and
			* generates key press events for these Unicode values (Function keys).  This does not
			* happen in Safari or Chrome.  The fix is to ignore these key events.
			*/
			if (isMac && isWebkit) {
				if ((0xF700 <= e.keyCode && e.keyCode <= 0xF7FF) || e.keyCode === 13 || e.keyCode === 8) {
					if (e.preventDefault) { e.preventDefault(); }
					return false;
				}
			}
			if (((isMac || isLinux) && isFirefox < 4) || isOpera) {
				if (this._doAction(this._keyDownEvent)) {
					if (e.preventDefault) { e.preventDefault(); }
					return false;
				}
			}
			var ctrlKey = isMac ? e.metaKey : e.ctrlKey;
			if (e.charCode !== undefined) {
				if (ctrlKey) {
					switch (e.charCode) {
						/*
						* In Firefox and Safari if ctrl+v, ctrl+c ctrl+x is canceled
						* the clipboard events are not sent. The fix to allow
						* the browser to handles these key events.
						*/
						case 99://c
						case 118://v
						case 120://x
							return true;
					}
				}
			}
			var ignore = false;
			if (isMac) {
				if (e.ctrlKey || e.metaKey) { ignore = true; }
			} else {
				if (isFirefox) {
					//Firefox clears the state mask when ALT GR generates input
					if (e.ctrlKey || e.altKey) { ignore = true; }
				} else {
					//IE and Chrome only send ALT GR when input is generated
					if (e.ctrlKey ^ e.altKey) { ignore = true; }
				}
			}
			if (!ignore) {
				var key = isOpera ? e.which : (e.charCode !== undefined ? e.charCode : e.keyCode);
				if (key > 31) {
					this._doContent(String.fromCharCode (key));
					if (e.preventDefault) { e.preventDefault(); }
					return false;
				}
			}
		},
		_handleKeyUp: function (e) {
			if (!e) { e = window.event; }
			var ctrlKey = isMac ? e.metaKey : e.ctrlKey;
			if (!ctrlKey) {
				this._setLinksVisible(false);
			}
			// don't commit for space (it happens during JP composition)  
			if (e.keyCode === 13) {
				this._commitIME();
			}
		},
		_handleLinkClick: function (e) {
			if (!e) { e = window.event; }
			var ctrlKey = isMac ? e.metaKey : e.ctrlKey;
			if (!ctrlKey) {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleMouse: function (e) {
			var result = true;
			var target = window;
			if (isIE || (isFirefox && !this._overlayDiv)) { target = this._clientDiv; }
			if (this._overlayDiv) {
				if (this._hasFocus) {
					this._ignoreFocus = true;
				}
				var self = this;
				setTimeout(function () {
					self.focus();
					self._ignoreFocus = false;
				}, 0);
			}
			if (this._clickCount === 1) {
				result = this._setSelectionTo(e.clientX, e.clientY, e.shiftKey, !isOpera && this._hasFocus && this.isListening("DragStart")); //$NON-NLS-0$
				if (result) { this._setGrab(target); }
			} else {
				/*
				* Feature in IE8 and older, the sequence of events in the IE8 event model
				* for a doule-click is:
				*
				*	down
				*	up
				*	up
				*	dblclick
				*
				* Given that the mouse down/up events are not balanced, it is not possible to
				* grab on mouse down and ungrab on mouse up.  The fix is to grab on the first
				* mouse down and ungrab on mouse move when the button 1 is not set.
				*/
				if (isW3CEvents) { this._setGrab(target); }
				
				this._doubleClickSelection = null;
				this._setSelectionTo(e.clientX, e.clientY, e.shiftKey);
				this._doubleClickSelection = this._getSelection();
			}
			return result;
		},
		_handleMouseDown: function (e) {
			if (!e) { e = window.event; }
			if (this.isListening("MouseDown")) { //$NON-NLS-0$
				this.onMouseDown(this._createMouseEvent("MouseDown", e)); //$NON-NLS-0$
			}
			if (this._linksVisible) {
				var target = e.target || e.srcElement;
				if (target.tagName !== "A") { //$NON-NLS-0$
					this._setLinksVisible(false);
				} else {
					return;
				}
			}
			this._commitIME();

			var button = e.which; // 1 - left, 2 - middle, 3 - right
			if (!button) { 
				// if IE 8 or older
				if (e.button === 4) { button = 2; }
				if (e.button === 2) { button = 3; }
				if (e.button === 1) { button = 1; }
			}

			// For middle click we always need getTime(). See _getClipboardText().
			var time = button !== 2 && e.timeStamp ? e.timeStamp : new Date().getTime();
			var timeDiff = time - this._lastMouseTime;
			var deltaX = Math.abs(this._lastMouseX - e.clientX);
			var deltaY = Math.abs(this._lastMouseY - e.clientY);
			var sameButton = this._lastMouseButton === button;
			this._lastMouseX = e.clientX;
			this._lastMouseY = e.clientY;
			this._lastMouseTime = time;
			this._lastMouseButton = button;

			if (button === 1) {
				this._isMouseDown = true;
				if (sameButton && timeDiff <= this._clickTime && deltaX <= this._clickDist && deltaY <= this._clickDist) {
					this._clickCount++;
				} else {
					this._clickCount = 1;
				}
				if (this._handleMouse(e) && (isIE >= 9 || isOpera || isChrome || (isFirefox && !this._overlayDiv))) {
					if (!this._hasFocus) {
						this.focus();
					}
					e.preventDefault();
				}
			}
			if (isFirefox && this._lastMouseButton === 3) {
				// We need to update the DOM selection, because on
				// right-click the caret moves to the mouse location.
				// See bug 366312 and 376508.
				this._updateDOMSelection();
			}
		},
		_handleMouseOver: function (e) {
			if (!e) { e = window.event; }
			if (this.isListening("MouseOver")) { //$NON-NLS-0$
				this.onMouseOver(this._createMouseEvent("MouseOver", e)); //$NON-NLS-0$
			}
		},
		_handleMouseOut: function (e) {
			if (!e) { e = window.event; }
			if (this.isListening("MouseOut")) { //$NON-NLS-0$
				this.onMouseOut(this._createMouseEvent("MouseOut", e)); //$NON-NLS-0$
			}
		},
		_handleMouseMove: function (e) {
			if (!e) { e = window.event; }
			var inClient = this._isClientDiv(e);
			if (this.isListening("MouseMove")) { //$NON-NLS-0$
				if (inClient){
					this.onMouseMove(this._createMouseEvent("MouseMove", e)); //$NON-NLS-0$
				}
			}
			if (this._dropTarget) {
				return;
			}
			/*
			* Bug in IE9. IE sends one mouse event when the user changes the text by
			* pasting or undo.  These operations usually happen with the Ctrl key
			* down which causes the view to enter link mode.  Link mode does not end
			* because there are no further events.  The fix is to only enter link
			* mode when the coordinates of the mouse move event have changed.
			*/
			var changed = this._linksVisible || this._lastMouseMoveX !== e.clientX || this._lastMouseMoveY !== e.clientY;
			this._lastMouseMoveX = e.clientX;
			this._lastMouseMoveY = e.clientY;
			this._setLinksVisible(changed && !this._isMouseDown && (isMac ? e.metaKey : e.ctrlKey));

			/*
			* Feature in IE8 and older, the sequence of events in the IE8 event model
			* for a doule-click is:
			*
			*	down
			*	up
			*	up
			*	dblclick
			*
			* Given that the mouse down/up events are not balanced, it is not possible to
			* grab on mouse down and ungrab on mouse up.  The fix is to grab on the first
			* mouse down and ungrab on mouse move when the button 1 is not set.
			*
			* In order to detect double-click and drag gestures, it is necessary to send
			* a mouse down event from mouse move when the button is still down and isMouseDown
			* flag is not set.
			*/
			if (!isW3CEvents) {
				if (e.button === 0) {
					this._setGrab(null);
					return true;
				}
				if (!this._isMouseDown && e.button === 1 && (this._clickCount & 1) !== 0 && inClient) {
					this._clickCount = 2;
					return this._handleMouse(e, this._clickCount);
				}
			}
			if (!this._isMouseDown || this._dragOffset !== -1) {
				return;
			}
			
			var x = e.clientX;
			var y = e.clientY;
			var viewPad = this._getViewPadding();
			var viewRect = this._viewDiv.getBoundingClientRect();
			var width = this._getClientWidth (), height = this._getClientHeight();
			var leftEdge = viewRect.left + viewPad.left;
			var topEdge = viewRect.top + viewPad.top;
			var rightEdge = viewRect.left + viewPad.left + width;
			var bottomEdge = viewRect.top + viewPad.top + height;
			var model = this._model;
			var caretLine = model.getLineAtOffset(this._getSelection().getCaret());
			if (y < topEdge && caretLine !== 0) {
				this._doAutoScroll("up", x, y - topEdge); //$NON-NLS-0$
			} else if (y > bottomEdge && caretLine !== model.getLineCount() - 1) {
				this._doAutoScroll("down", x, y - bottomEdge); //$NON-NLS-0$
			} else if (x < leftEdge) {
				this._doAutoScroll("left", x - leftEdge, y); //$NON-NLS-0$
			} else if (x > rightEdge) {
				this._doAutoScroll("right", x - rightEdge, y); //$NON-NLS-0$
			} else {
				this._endAutoScroll();
				this._setSelectionTo(x, y, true);
			}
		},
		_isClientDiv: function(e) {
			var topNode = this._overlayDiv || this._clientDiv;
			var temp = e.target ? e.target : e.srcElement;
			while (temp) {
				if (topNode === temp) {
					return true;
				}
				temp = temp.parentNode;
			}
			return false;
		},
		_createMouseEvent: function(type, e) {
			var scroll = this._getScroll();
			var viewRect = this._viewDiv.getBoundingClientRect();
			var viewPad = this._getViewPadding();
			var x = e.clientX + scroll.x - viewRect.left - viewPad.left;
			var y = e.clientY + scroll.y - viewRect.top - viewPad.top;
			return {
				type: type,
				event: e,
				x: x,
				y: y
			};
		},
		_handleMouseUp: function (e) {
			if (!e) { e = window.event; }
			var left = e.which ? e.button === 0 : e.button === 1;
			if (this.isListening("MouseUp")) { //$NON-NLS-0$
				if (this._isClientDiv(e) || (left && this._isMouseDown)) {
					this.onMouseUp(this._createMouseEvent("MouseUp", e)); //$NON-NLS-0$
				}
			}
			if (this._linksVisible) {
				return;
			}
			if (left && this._isMouseDown) {
				if (this._dragOffset !== -1) {
					var selection = this._getSelection();
					selection.extend(this._dragOffset);
					selection.collapse();
					this._setSelection(selection, true, true);
					this._dragOffset = -1;
				}
				this._isMouseDown = false;
				this._endAutoScroll();
				
				/*
				* Feature in IE8 and older, the sequence of events in the IE8 event model
				* for a doule-click is:
				*
				*	down
				*	up
				*	up
				*	dblclick
				*
				* Given that the mouse down/up events are not balanced, it is not possible to
				* grab on mouse down and ungrab on mouse up.  The fix is to grab on the first
				* mouse down and ungrab on mouse move when the button 1 is not set.
				*/
				if (isW3CEvents) { this._setGrab(null); }

				/*
				* Note that there cases when Firefox sets the DOM selection in mouse up.
				* This happens for example after a cancelled drag operation.
				*
				* Note that on Chrome and IE, the caret stops blicking if mouse up is
				* prevented.
				*/
				if (isFirefox) {
					e.preventDefault();
				}
			}
		},
		_handleMouseWheel: function (e) {
			if (!e) { e = window.event; }
			var lineHeight = this._getLineHeight();
			var pixelX = 0, pixelY = 0;
			// Note: On the Mac the correct behaviour is to scroll by pixel.
			if (isIE || isOpera) {
				pixelY = (-e.wheelDelta / 40) * lineHeight;
			} else if (isFirefox) {
				var pixel;
				if (isMac) {
					pixel = e.detail * 3;
				} else {
					var limit = 256;
					pixel = Math.max(-limit, Math.min(limit, e.detail)) * lineHeight;
				}
				if (e.axis === e.HORIZONTAL_AXIS) {
					pixelX = pixel;
				} else {
					pixelY = pixel;
				}
			} else {
				//Webkit
				if (isMac) {
					/*
					* In Safari, the wheel delta is a multiple of 120. In order to
					* convert delta to pixel values, it is necessary to divide delta
					* by 40.
					*
					* In Chrome and Safari 5, the wheel delta depends on the type of the
					* mouse. In general, it is the pixel value for Mac mice and track pads,
					* but it is a multiple of 120 for other mice. There is no presise
					* way to determine if it is pixel value or a multiple of 120.
					* 
					* Note that the current approach does not calculate the correct
					* pixel value for Mac mice when the delta is a multiple of 120.
					*/
					var denominatorX = 40, denominatorY = 40;
					if (e.wheelDeltaX % 120 !== 0) { denominatorX = 1; }
					if (e.wheelDeltaY % 120 !== 0) { denominatorY = 1; }
					pixelX = -e.wheelDeltaX / denominatorX;
					if (-1 < pixelX && pixelX < 0) { pixelX = -1; }
					if (0 < pixelX && pixelX < 1) { pixelX = 1; }
					pixelY = -e.wheelDeltaY / denominatorY;
					if (-1 < pixelY && pixelY < 0) { pixelY = -1; }
					if (0 < pixelY && pixelY < 1) { pixelY = 1; }
				} else {
					pixelX = -e.wheelDeltaX;
					var linesToScroll = 8;
					pixelY = (-e.wheelDeltaY / 120 * linesToScroll) * lineHeight;
				}
			}
			/* 
			* Feature in Safari. If the event target is removed from the DOM 
			* safari stops smooth scrolling. The fix is keep the element target
			* in the DOM and remove it on a later time. 
			*
			* Note: Using a timer is not a solution, because the timeout needs to
			* be at least as long as the gesture (which is too long).
			*/
			if (isSafari) {
				var lineDiv = e.target;
				while (lineDiv && lineDiv.lineIndex === undefined) {
					lineDiv = lineDiv.parentNode;
				}
				this._mouseWheelLine = lineDiv;
			}
			var oldScroll = this._getScroll();
			this._scrollView(pixelX, pixelY);
			var newScroll = this._getScroll();
			if (oldScroll.x !== newScroll.x || oldScroll.y !== newScroll.y) {
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handlePaste: function (e) {
			if (this._ignorePaste) { return; }
			if (!e) { e = window.event; }
			if (this._doPaste(e)) {
				if (isIE) {
					/*
					 * Bug in IE,  
					 */
					var self = this;
					this._ignoreFocus = true;
					setTimeout(function() {
						self._updateDOMSelection();
						this._ignoreFocus = false;
					}, 0);
				}
				if (e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_handleResize: function (e) {
			if (!e) { e = window.event; }
			var newWidth = this._parent.clientWidth;
			var newHeight = this._parent.clientHeight;
			if (this._parentWidth !== newWidth || this._parentHeight !== newHeight) {
				this._parentWidth = newWidth;
				this._parentHeight = newHeight;
				/*
				* Feature in IE7. For some reason, sometimes Internet Explorer 7 
				* returns incorrect values for element.getBoundingClientRect() when 
				* inside a resize handler. The fix is to queue the work.
				*/
				if (isIE < 9) {
					this._queueUpdatePage();
				} else {
					this._updatePage();
				}
			}
		},
		_handleRulerEvent: function (e) {
			if (!e) { e = window.event; }
			var target = e.target ? e.target : e.srcElement;
			var lineIndex = target.lineIndex;
			var element = target;
			while (element && !element._ruler) {
				if (lineIndex === undefined && element.lineIndex !== undefined) {
					lineIndex = element.lineIndex;
				}
				element = element.parentNode;
			}
			var ruler = element ? element._ruler : null;
			if (lineIndex === undefined && ruler && ruler.getOverview() === "document") { //$NON-NLS-0$
				var clientHeight = this._getClientHeight ();
				var lineCount = this._model.getLineCount ();
				var viewPad = this._getViewPadding();
				var viewRect = this._viewDiv.getBoundingClientRect();
				var trackHeight = clientHeight + viewPad.top + viewPad.bottom - 2 * this._metrics.scrollWidth;
				lineIndex = Math.floor(((e.clientY - viewRect.top) - this._metrics.scrollWidth) * lineCount / trackHeight);
				if (!(0 <= lineIndex && lineIndex < lineCount)) {
					lineIndex = undefined;
				}
			}
			if (ruler) {
				switch (e.type) {
					case "click": //$NON-NLS-0$
						if (ruler.onClick) { ruler.onClick(lineIndex, e); }
						break;
					case "dblclick": //$NON-NLS-0$
						if (ruler.onDblClick) { ruler.onDblClick(lineIndex, e); }
						break;
					case "mousemove": //$NON-NLS-0$
						if (ruler.onMouseMove) { ruler.onMouseMove(lineIndex, e); }
						break;
					case "mouseover": //$NON-NLS-0$
						if (ruler.onMouseOver) { ruler.onMouseOver(lineIndex, e); }
						break;
					case "mouseout": //$NON-NLS-0$
						if (ruler.onMouseOut) { ruler.onMouseOut(lineIndex, e); }
						break;
				}
			}
		},
		_handleScroll: function () {
			var scroll = this._getScroll();
			var oldX = this._hScroll;
			var oldY = this._vScroll;
			if (oldX !== scroll.x || oldY !== scroll.y) {
				this._hScroll = scroll.x;
				this._vScroll = scroll.y;
				this._commitIME();
				this._updatePage(oldY === scroll.y);
				var e = {
					type: "Scroll", //$NON-NLS-0$
					oldValue: {x: oldX, y: oldY},
					newValue: scroll
				};
				this.onScroll(e);
			}
		},
		_handleSelectStart: function (e) {
			if (!e) { e = window.event; }
			if (this._ignoreSelect) {
				if (e && e.preventDefault) { e.preventDefault(); }
				return false;
			}
		},
		_getModelOffset: function(node, offset) {
			if (!node) { return; }
			var lineNode;
			if (node.tagName === "DIV") { //$NON-NLS-0$
				lineNode = node;
			} else {
				lineNode = node.parentNode.parentNode;
			}
			var lineOffset = 0;
			var lineIndex = lineNode.lineIndex;
			if (node.tagName !== "DIV") { //$NON-NLS-0$
				var child = lineNode.firstChild;
				while (child) {
					var textNode = child.firstChild;
					if (textNode === node) {
						if (child.ignoreChars) { lineOffset -= child.ignoreChars; }
						lineOffset += offset;
						break;
					}
					if (child.ignoreChars) { lineOffset -= child.ignoreChars; }
					lineOffset += textNode.data.length;
					child = child.nextSibling;
				}
			}
			return Math.max(0, lineOffset) + this._model.getLineStart(lineIndex);
		},
		_handleSelectionChange: function (e) {
			var selection = window.getSelection();
			var start = this._getModelOffset(selection.anchorNode, selection.anchorOffset);
			var end = this._getModelOffset(selection.focusNode, selection.focusOffset);
			if (start === undefined || end === undefined) {
			    return;
			}
			this._setSelection(new Selection(start, end), false, false, false);
		},
		_handleTouchStart: function (e) {
			if (this._touchScrollTimer) {
				this._vScrollDiv.style.display = "none"; //$NON-NLS-0$
				this._hScrollDiv.style.display = "none"; //$NON-NLS-0$
				clearInterval(this._touchScrollTimer);
				this._touchScrollTimer = null;
			}
			var touches = e.touches;
			if (touches.length === 1) {
				var touch = touches[0];
				this._touchStartX = touch.clientX;
				this._touchStartY = touch.clientY;
				this._lastTouchOffset = this._getXToOffset(this._getYToLine(touch.clientY), touch.clientX);
				this._touchStartTime = e.timeStamp;
				this._touching = true;
			}
		},
		_handleTouchMove: function (e) {
			var touches = e.touches;
			if (touches.length === 1) {
				var touch = touches[0];
				this._touchCurrentX = touch.clientX;
				this._touchCurrentY = touch.clientY;
				var interval = 10;
				if (!this._touchScrollTimer && (e.timeStamp - this._touchStartTime) < (interval*20)) {
					this._vScrollDiv.style.display = "block"; //$NON-NLS-0$
					this._hScrollDiv.style.display = "block"; //$NON-NLS-0$
					var self = this;
					this._touchScrollTimer = setInterval(function() {
						var deltaX = 0, deltaY = 0;
						if (self._touching) {
							deltaX = self._touchStartX - self._touchCurrentX;
							deltaY = self._touchStartY - self._touchCurrentY;
							self._touchSpeedX = deltaX / interval;
							self._touchSpeedY = deltaY / interval;
							self._touchStartX = self._touchCurrentX;
							self._touchStartY = self._touchCurrentY;
						} else {
							if (Math.abs(self._touchSpeedX) < 0.1 && Math.abs(self._touchSpeedY) < 0.1) {
								self._vScrollDiv.style.display = "none"; //$NON-NLS-0$
								self._hScrollDiv.style.display = "none"; //$NON-NLS-0$
								clearInterval(self._touchScrollTimer);
								self._touchScrollTimer = null;
								return;
							} else {
								deltaX = self._touchSpeedX * interval;
								deltaY = self._touchSpeedY * interval;
								self._touchSpeedX *= 0.95;
								self._touchSpeedY *= 0.95;
							}
						}
						self._scrollView(deltaX, deltaY);
					}, interval);
				}
				if (this._touchScrollTimer) {
					e.preventDefault();
				}
			}
		},
		_handleTouchEnd: function (e) {
			var touches = e.touches;
			if (touches.length === 0) {
				this._touching = false;
			}
		},

		/************************************ Actions ******************************************/
		_doAction: function (e) {
			var keyBindings = this._keyBindings;
			for (var i = 0; i < keyBindings.length; i++) {
				var kb = keyBindings[i];
				if (kb.keyBinding.match(e)) {
					if (kb.actionID) {
						var actions = this._actions;
						var action = actions[kb.actionID];
						if (action) {
							if (action.handler) {
								if (!action.handler()) {
									if (action.defaultHandler) {
										return typeof(action.defaultHandler()) === "boolean"; //$NON-NLS-0$
									} else {
										return false;
									}
								}
							} else if (action.defaultHandler) {
								return typeof(action.defaultHandler()) === "boolean"; //$NON-NLS-0$
							}
						}
					}
					return true;
				}
			}
			return false;
		},
		_doBackspace: function (args) {
			var selection = this._getSelection();
			if (selection.isEmpty()) {
				var model = this._model;
				var caret = selection.getCaret();
				var lineIndex = model.getLineAtOffset(caret);
				var lineStart = model.getLineStart(lineIndex);
				if (caret === lineStart) {
					if (lineIndex > 0) {
						selection.extend(model.getLineEnd(lineIndex - 1));
					}
				} else {
					var removeTab = false;
					if (this._expandTab && args.unit === "character" && (caret - lineStart) % this._tabSize === 0) { //$NON-NLS-0$
						var lineText = model.getText(lineStart, caret);
						removeTab = !/[^ ]/.test(lineText); // Only spaces between line start and caret.
					}
					if (removeTab) {
						selection.extend(caret - this._tabSize);
					} else {
						selection.extend(this._getOffset(caret, args.unit, -1));
					}
				}
			}
			this._modifyContent({text: "", start: selection.start, end: selection.end}, true);
			return true;
		},
		_doContent: function (text) {
			var selection = this._getSelection();
			this._modifyContent({text: text, start: selection.start, end: selection.end, _ignoreDOMSelection: true}, true);
		},
		_doCopy: function (e) {
			var selection = this._getSelection();
			if (!selection.isEmpty()) {
				var text = this._getBaseText(selection.start, selection.end);
				return this._setClipboardText(text, e);
			}
			return true;
		},
		_doCursorNext: function (args) {
			if (!args.select) {
				if (this._clearSelection("next")) { return true; } //$NON-NLS-0$
			}
			var model = this._model;
			var selection = this._getSelection();
			var caret = selection.getCaret();
			var lineIndex = model.getLineAtOffset(caret);
			if (caret === model.getLineEnd(lineIndex)) {
				if (lineIndex + 1 < model.getLineCount()) {
					selection.extend(model.getLineStart(lineIndex + 1));
				}
			} else {
				selection.extend(this._getOffset(caret, args.unit, 1));
			}
			if (!args.select) { selection.collapse(); }
			this._setSelection(selection, true);
			return true;
		},
		_doCursorPrevious: function (args) {
			if (!args.select) {
				if (this._clearSelection("previous")) { return true; } //$NON-NLS-0$
			}
			var model = this._model;
			var selection = this._getSelection();
			var caret = selection.getCaret();
			var lineIndex = model.getLineAtOffset(caret);
			if (caret === model.getLineStart(lineIndex)) {
				if (lineIndex > 0) {
					selection.extend(model.getLineEnd(lineIndex - 1));
				}
			} else {
				selection.extend(this._getOffset(caret, args.unit, -1));
			}
			if (!args.select) { selection.collapse(); }
			this._setSelection(selection, true);
			return true;
		},
		_doCut: function (e) {
			var selection = this._getSelection();
			if (!selection.isEmpty()) {
				var text = this._getBaseText(selection.start, selection.end);
				this._doContent("");
				return this._setClipboardText(text, e);
			}
			return true;
		},
		_doDelete: function (args) {
			var selection = this._getSelection();
			if (selection.isEmpty()) {
				var model = this._model;
				var caret = selection.getCaret();
				var lineIndex = model.getLineAtOffset(caret);
				if (caret === model.getLineEnd (lineIndex)) {
					if (lineIndex + 1 < model.getLineCount()) {
						selection.extend(model.getLineStart(lineIndex + 1));
					}
				} else {
					selection.extend(this._getOffset(caret, args.unit, 1));
				}
			}
			this._modifyContent({text: "", start: selection.start, end: selection.end}, true);
			return true;
		},
		_doEnd: function (args) {
			var selection = this._getSelection();
			var model = this._model;
			if (args.ctrl) {
				selection.extend(model.getCharCount());
			} else {
				var lineIndex = model.getLineAtOffset(selection.getCaret());
				selection.extend(model.getLineEnd(lineIndex)); 
			}
			if (!args.select) { selection.collapse(); }
			this._setSelection(selection, true);
			return true;
		},
		_doEnter: function (args) {
			var model = this._model;
			var selection = this._getSelection();
			this._doContent(model.getLineDelimiter()); 
			if (args && args.noCursor) {
				selection.end = selection.start;
				this._setSelection(selection);
			}
			return true;
		},
		_doHome: function (args) {
			var selection = this._getSelection();
			var model = this._model;
			if (args.ctrl) {
				selection.extend(0);
			} else {
				var lineIndex = model.getLineAtOffset(selection.getCaret());
				selection.extend(model.getLineStart(lineIndex)); 
			}
			if (!args.select) { selection.collapse(); }
			this._setSelection(selection, true);
			return true;
		},
		_doLineDown: function (args) {
			var model = this._model;
			var selection = this._getSelection();
			var caret = selection.getCaret();
			var lineIndex = model.getLineAtOffset(caret);
			var x = this._columnX;
			var scrollX = this._getScroll().x;
			if (x === -1 || args.wholeLine || (args.select && isIE)) {
				var offset = args.wholeLine ? model.getLineEnd(lineIndex + 1) : caret;
				x = this._getOffsetToX(offset) + scrollX;
			}
			if (lineIndex + 1 < model.getLineCount()) {
				selection.extend(this._getXToOffset(lineIndex + 1, x - scrollX));
				if (!args.select) { selection.collapse(); }
				this._setSelection(selection, true, true);
			} else {
				if (args.select) {
					selection.extend(model.getCharCount());
					this._setSelection(selection, true, true);
				}
			}
			this._columnX = x;
			return true;
		},
		_doLineUp: function (args) {
			var model = this._model;
			var selection = this._getSelection();
			var caret = selection.getCaret();
			var lineIndex = model.getLineAtOffset(caret);
			var x = this._columnX;
			var scrollX = this._getScroll().x;
			if (x === -1 || args.wholeLine || (args.select && isIE)) {
				var offset = args.wholeLine ? model.getLineStart(lineIndex - 1) : caret;
				x = this._getOffsetToX(offset) + scrollX;
			}
			if (lineIndex > 0) {
				selection.extend(this._getXToOffset(lineIndex - 1, x - scrollX));
				if (!args.select) { selection.collapse(); }
				this._setSelection(selection, true, true);
			} else {
				if (args.select) {
					selection.extend(0);
					this._setSelection(selection, true, true);
				}
			}
			this._columnX = x;
			return true;
		},
		_doPageDown: function (args) {
			var model = this._model;
			var selection = this._getSelection();
			var caret = selection.getCaret();
			var caretLine = model.getLineAtOffset(caret);
			var lineCount = model.getLineCount();
			if (caretLine < lineCount - 1) {
				var scroll = this._getScroll();
				var clientHeight = this._getClientHeight();
				var lineHeight = this._getLineHeight();
				var lines = Math.floor(clientHeight / lineHeight);
				var scrollLines = Math.min(lineCount - caretLine - 1, lines);
				scrollLines = Math.max(1, scrollLines);
				var x = this._columnX;
				if (x === -1 || (args.select && isIE)) {
					x = this._getOffsetToX(caret) + scroll.x;
				}
				selection.extend(this._getXToOffset(caretLine + scrollLines, x - scroll.x));
				if (!args.select) { selection.collapse(); }
				var verticalMaximum = lineCount * lineHeight;
				var scrollOffset = scroll.y + scrollLines * lineHeight;
				if (scrollOffset + clientHeight > verticalMaximum) {
					scrollOffset = verticalMaximum - clientHeight;
				}
				this._setSelection(selection, true, true, scrollOffset - scroll.y);
				this._columnX = x;
			}
			return true;
		},
		_doPageUp: function (args) {
			var model = this._model;
			var selection = this._getSelection();
			var caret = selection.getCaret();
			var caretLine = model.getLineAtOffset(caret);
			if (caretLine > 0) {
				var scroll = this._getScroll();
				var clientHeight = this._getClientHeight();
				var lineHeight = this._getLineHeight();
				var lines = Math.floor(clientHeight / lineHeight);
				var scrollLines = Math.max(1, Math.min(caretLine, lines));
				var x = this._columnX;
				if (x === -1 || (args.select && isIE)) {
					x = this._getOffsetToX(caret) + scroll.x;
				}
				selection.extend(this._getXToOffset(caretLine - scrollLines, x - scroll.x));
				if (!args.select) { selection.collapse(); }
				var scrollOffset = Math.max(0, scroll.y - scrollLines * lineHeight);
				this._setSelection(selection, true, true, scrollOffset - scroll.y);
				this._columnX = x;
			}
			return true;
		},
		_doPaste: function(e) {
			var self = this;
			var result = this._getClipboardText(e, function(text) {
				if (text) {
					if (isLinux && self._lastMouseButton === 2) {
						var timeDiff = new Date().getTime() - self._lastMouseTime;
						if (timeDiff <= self._clickTime) {
							self._setSelectionTo(self._lastMouseX, self._lastMouseY);
						}
					}
					self._doContent(text);
				}
			});
			return result !== null;
		},
		_doScroll: function (args) {
			var type = args.type;
			var model = this._model;
			var lineCount = model.getLineCount();
			var clientHeight = this._getClientHeight();
			var lineHeight = this._getLineHeight();
			var verticalMaximum = lineCount * lineHeight;
			var verticalScrollOffset = this._getScroll().y;
			var pixel;
			switch (type) {
				case "textStart": pixel = 0; break; //$NON-NLS-0$
				case "textEnd": pixel = verticalMaximum - clientHeight; break; //$NON-NLS-0$
				case "pageDown": pixel = verticalScrollOffset + clientHeight; break; //$NON-NLS-0$
				case "pageUp": pixel = verticalScrollOffset - clientHeight; break; //$NON-NLS-0$
				case "centerLine": //$NON-NLS-0$
					var selection = this._getSelection();
					var lineStart = model.getLineAtOffset(selection.start);
					var lineEnd = model.getLineAtOffset(selection.end);
					var selectionHeight = (lineEnd - lineStart + 1) * lineHeight;
					pixel = (lineStart * lineHeight) - (clientHeight / 2) + (selectionHeight / 2);
					break;
			}
			if (pixel !== undefined) {
				pixel = Math.min(Math.max(0, pixel), verticalMaximum - clientHeight);
				this._scrollView(0, pixel - verticalScrollOffset);
			}
			return true;
		},
		_doSelectAll: function (args) {
			var model = this._model;
			var selection = this._getSelection();
			selection.setCaret(0);
			selection.extend(model.getCharCount());
			this._setSelection(selection, false);
			return true;
		},
		_doTab: function (args) {
			if(!this._tabMode || this._readonly) { return; }
			var text = "\t"; //$NON-NLS-0$
			if (this._expandTab) {
				var model = this._model;
				var caret = this._getSelection().getCaret();
				var lineIndex = model.getLineAtOffset(caret);
				var lineStart = model.getLineStart(lineIndex);
				var spaces = this._tabSize - ((caret - lineStart) % this._tabSize);
				text = (new Array(spaces + 1)).join(" "); //$NON-NLS-0$
			}
			this._doContent(text);
			return true;
		},
		_doShiftTab: function (args) {
			if(!this._tabMode || this._readonly) { return; }
			return true;
		},
		_doTabMode: function (args) {
			this._tabMode = !this._tabMode;
			return true;
		},
		
		/************************************ Internals ******************************************/
		_applyStyle: function(style, node, reset) {
			if (reset) {
				node.className = "";
				var attrs = node.attributes;
				for (var i= attrs.length; i-->0;) {
					if (attrs[i].specified) {
						node.removeAttributeNode(attrs[i]); 
					}
				}
			}
			if (!style) {
				return;
			}
			if (style.styleClass) {
				node.className = style.styleClass;
			}
			var properties = style.style;
			if (properties) {
				for (var s in properties) {
					if (properties.hasOwnProperty(s)) {
						node.style[s] = properties[s];
					}
				}
			}
			var attributes = style.attributes;
			if (attributes) {
				for (var a in attributes) {
					if (attributes.hasOwnProperty(a)) {
						node.setAttribute(a, attributes[a]);
					}
				}
			}
		},
		_autoScroll: function () {
			var selection = this._getSelection();
			var line;
			var x = this._autoScrollX;
			if (this._autoScrollDir === "up" || this._autoScrollDir === "down") { //$NON-NLS-1$ //$NON-NLS-0$
				var scroll = this._autoScrollY / this._getLineHeight();
				scroll = scroll < 0 ? Math.floor(scroll) : Math.ceil(scroll);
				line = this._model.getLineAtOffset(selection.getCaret());
				line = Math.max(0, Math.min(this._model.getLineCount() - 1, line + scroll));
			} else if (this._autoScrollDir === "left" || this._autoScrollDir === "right") { //$NON-NLS-1$ //$NON-NLS-0$
				line = this._getYToLine(this._autoScrollY);
				x += this._getOffsetToX(selection.getCaret());
			}
			selection.extend(this._getXToOffset(line, x));
			this._setSelection(selection, true);
		},
		_autoScrollTimer: function () {
			this._autoScroll();
			var self = this;
			this._autoScrollTimerID = setTimeout(function () {self._autoScrollTimer();}, this._AUTO_SCROLL_RATE);
		},
		_calculateMetrics: function() {
			var parent = this._clientDiv;
			var c = " "; //$NON-NLS-0$
			var line = document.createElement("DIV"); //$NON-NLS-0$
			line.style.lineHeight = "normal"; //$NON-NLS-0$
			var model = this._model;
			var lineText = model.getLine(0);
			var e = {type:"LineStyle", textView: this, 0: 0, lineText: lineText, lineStart: 0}; //$NON-NLS-0$
			this.onLineStyle(e);
			this._applyStyle(e.style, line);
			line.style.position = "fixed"; //$NON-NLS-0$
			line.style.left = "-1000px"; //$NON-NLS-0$
			var span1 = document.createElement("SPAN"); //$NON-NLS-0$
			span1.appendChild(document.createTextNode(c));
			line.appendChild(span1);
			var span2 = document.createElement("SPAN"); //$NON-NLS-0$
			span2.style.fontStyle = "italic"; //$NON-NLS-0$
			span2.appendChild(document.createTextNode(c));
			line.appendChild(span2);
			var span3 = document.createElement("SPAN"); //$NON-NLS-0$
			span3.style.fontWeight = "bold"; //$NON-NLS-0$
			span3.appendChild(document.createTextNode(c));
			line.appendChild(span3);
			var span4 = document.createElement("SPAN"); //$NON-NLS-0$
			span4.style.fontWeight = "bold"; //$NON-NLS-0$
			span4.style.fontStyle = "italic"; //$NON-NLS-0$
			span4.appendChild(document.createTextNode(c));
			line.appendChild(span4);
			parent.appendChild(line);
			var lineRect = line.getBoundingClientRect();
			var spanRect1 = span1.getBoundingClientRect();
			var spanRect2 = span2.getBoundingClientRect();
			var spanRect3 = span3.getBoundingClientRect();
			var spanRect4 = span4.getBoundingClientRect();
			var h1 = spanRect1.bottom - spanRect1.top;
			var h2 = spanRect2.bottom - spanRect2.top;
			var h3 = spanRect3.bottom - spanRect3.top;
			var h4 = spanRect4.bottom - spanRect4.top;
			var fontStyle = 0;
			var lineHeight = Math.max(1, lineRect.bottom - lineRect.top);
			if (h2 > h1) {
				fontStyle = 1;
			}
			if (h3 > h2) {
				fontStyle = 2;
			}
			if (h4 > h3) {
				fontStyle = 3;
			}
			var style;
			if (fontStyle !== 0) {
				style = {style: {}};
				if ((fontStyle & 1) !== 0) {
					style.style.fontStyle = "italic"; //$NON-NLS-0$
				}
				if ((fontStyle & 2) !== 0) {
					style.style.fontWeight = "bold"; //$NON-NLS-0$
				}
			}
			var trim = this._getLineTrim(line);
			parent.removeChild(line);
			
			// calculate pad and scroll width
			var pad = this._getPadding(this._viewDiv);
			var div1 = document.createElement("DIV"); //$NON-NLS-0$
			div1.style.position = "fixed"; //$NON-NLS-0$
			div1.style.left = "-1000px"; //$NON-NLS-0$
			div1.style.paddingLeft = pad.left + "px"; //$NON-NLS-0$
			div1.style.paddingTop = pad.top + "px"; //$NON-NLS-0$
			div1.style.paddingRight = pad.right + "px"; //$NON-NLS-0$
			div1.style.paddingBottom = pad.bottom + "px"; //$NON-NLS-0$
			div1.style.width = "100px"; //$NON-NLS-0$
			div1.style.height = "100px"; //$NON-NLS-0$
			var div2 = document.createElement("DIV"); //$NON-NLS-0$
			div2.style.width = "100%"; //$NON-NLS-0$
			div2.style.height = "100%"; //$NON-NLS-0$
			div1.appendChild(div2);
			parent.appendChild(div1);
			var rect1 = div1.getBoundingClientRect();
			var rect2 = div2.getBoundingClientRect();
			div1.style.overflow = 'hidden'; //$NON-NLS-0$
			div2.style.height = "200px"; //$NON-NLS-0$
			var w1 = div1.clientWidth;
			div1.style.overflow = 'scroll'; //$NON-NLS-0$
			var w2 = div1.clientWidth;
			parent.removeChild(div1);
			var scrollWidth = w1 - w2;
			pad = {
				left: rect2.left - rect1.left,
				top: rect2.top - rect1.top,
				right: rect1.right - rect2.right,
				bottom: rect1.bottom - rect2.bottom
			};
			return {lineHeight: lineHeight, largestFontStyle: style, lineTrim: trim, viewPadding: pad, scrollWidth: scrollWidth};
		},
		_clearSelection: function (direction) {
			var selection = this._getSelection();
			if (selection.isEmpty()) { return false; }
			if (direction === "next") { //$NON-NLS-0$
				selection.start = selection.end;
			} else {
				selection.end = selection.start;
			}
			this._setSelection(selection, true);
			return true;
		},
		_clone: function (obj) {
			/*Note that this code only works because of the limited types used in TextViewOptions */
			if (obj instanceof Array) {
				return obj.slice(0);
			}
			return obj;
		},
		_compare: function (s1, s2) {
			if (s1 === s2) { return true; }
			if (s1 && !s2 || !s1 && s2) { return false; }
			if ((s1 && s1.constructor === String) || (s2 && s2.constructor === String)) { return false; }
			if (s1 instanceof Array || s2 instanceof Array) {
				if (!(s1 instanceof Array && s2 instanceof Array)) { return false; }
				if (s1.length !== s2.length) { return false; }
				for (var i = 0; i < s1.length; i++) {
					if (!this._compare(s1[i], s2[i])) {
						return false;
					}
				}
				return true;
			}
			if (!(s1 instanceof Object) || !(s2 instanceof Object)) { return false; }
			var p;
			for (p in s1) {
				if (s1.hasOwnProperty(p)) {
					if (!s2.hasOwnProperty(p)) { return false; }
					if (!this._compare(s1[p], s2[p])) {return false; }
				}
			}
			for (p in s2) {
				if (!s1.hasOwnProperty(p)) { return false; }
			}
			return true;
		},
		_commitIME: function () {
			if (this._imeOffset === -1) { return; }
			// make the state of the IME match the state the view expects it be in
			// when the view commits the text and IME also need to be committed
			// this can be accomplished by changing the focus around
			this._scrollDiv.focus();
			this._clientDiv.focus();
			
			var model = this._model;
			var lineIndex = model.getLineAtOffset(this._imeOffset);
			var lineStart = model.getLineStart(lineIndex);
			var newText = this._getDOMText(lineIndex);
			var oldText = model.getLine(lineIndex);
			var start = this._imeOffset - lineStart;
			var end = start + newText.length - oldText.length;
			if (start !== end) {
				var insertText = newText.substring(start, end);
				this._doContent(insertText);
			}
			this._imeOffset = -1;
		},
		_convertDelimiter: function (text, addTextFunc, addDelimiterFunc) {
				var cr = 0, lf = 0, index = 0, length = text.length;
				while (index < length) {
					if (cr !== -1 && cr <= index) { cr = text.indexOf("\r", index); } //$NON-NLS-0$
					if (lf !== -1 && lf <= index) { lf = text.indexOf("\n", index); } //$NON-NLS-0$
					var start = index, end;
					if (lf === -1 && cr === -1) {
						addTextFunc(text.substring(index));
						break;
					}
					if (cr !== -1 && lf !== -1) {
						if (cr + 1 === lf) {
							end = cr;
							index = lf + 1;
						} else {
							end = cr < lf ? cr : lf;
							index = (cr < lf ? cr : lf) + 1;
						}
					} else if (cr !== -1) {
						end = cr;
						index = cr + 1;
					} else {
						end = lf;
						index = lf + 1;
					}
					addTextFunc(text.substring(start, end));
					addDelimiterFunc();
				}
		},
		_createActions: function () {
			var KeyBinding = mKeyBinding.KeyBinding;
			//no duplicate keybindings
			var bindings = this._keyBindings = [];

			// Cursor Navigation
			bindings.push({actionID: "lineUp",		keyBinding: new KeyBinding(38), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "lineDown",	keyBinding: new KeyBinding(40), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "charPrevious",	keyBinding: new KeyBinding(37), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "charNext",	keyBinding: new KeyBinding(39), predefined: true}); //$NON-NLS-0$
			if (isMac) {
				bindings.push({actionID: "scrollPageUp",		keyBinding: new KeyBinding(33), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "scrollPageDown",	keyBinding: new KeyBinding(34), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "pageUp",		keyBinding: new KeyBinding(33, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "pageDown",	keyBinding: new KeyBinding(34, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineStart",	keyBinding: new KeyBinding(37, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineEnd",		keyBinding: new KeyBinding(39, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "wordPrevious",	keyBinding: new KeyBinding(37, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "wordNext",	keyBinding: new KeyBinding(39, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "scrollTextStart",	keyBinding: new KeyBinding(36), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "scrollTextEnd",		keyBinding: new KeyBinding(35), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "textStart",	keyBinding: new KeyBinding(38, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "textEnd",		keyBinding: new KeyBinding(40, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "scrollPageUp",	keyBinding: new KeyBinding(38, null, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "scrollPageDown",		keyBinding: new KeyBinding(40, null, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineStart",	keyBinding: new KeyBinding(37, null, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineEnd",		keyBinding: new KeyBinding(39, null, null, null, true), predefined: true}); //$NON-NLS-0$
				//TODO These two actions should be changed to paragraph start and paragraph end  when word wrap is implemented
				bindings.push({actionID: "lineStart",	keyBinding: new KeyBinding(38, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineEnd",		keyBinding: new KeyBinding(40, null, null, true), predefined: true}); //$NON-NLS-0$
			} else {
				bindings.push({actionID: "pageUp",		keyBinding: new KeyBinding(33), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "pageDown",	keyBinding: new KeyBinding(34), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineStart",	keyBinding: new KeyBinding(36), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineEnd",		keyBinding: new KeyBinding(35), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "wordPrevious",	keyBinding: new KeyBinding(37, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "wordNext",	keyBinding: new KeyBinding(39, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "textStart",	keyBinding: new KeyBinding(36, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "textEnd",		keyBinding: new KeyBinding(35, true), predefined: true}); //$NON-NLS-0$
			}
			if (isFirefox && isLinux) {
				bindings.push({actionID: "lineUp",		keyBinding: new KeyBinding(38, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "lineDown",	keyBinding: new KeyBinding(40, true), predefined: true}); //$NON-NLS-0$
			}

			// Select Cursor Navigation
			bindings.push({actionID: "selectLineUp",		keyBinding: new KeyBinding(38, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "selectLineDown",		keyBinding: new KeyBinding(40, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "selectCharPrevious",	keyBinding: new KeyBinding(37, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "selectCharNext",		keyBinding: new KeyBinding(39, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "selectPageUp",		keyBinding: new KeyBinding(33, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "selectPageDown",		keyBinding: new KeyBinding(34, null, true), predefined: true}); //$NON-NLS-0$
			if (isMac) {
				bindings.push({actionID: "selectLineStart",	keyBinding: new KeyBinding(37, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectLineEnd",		keyBinding: new KeyBinding(39, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectWordPrevious",	keyBinding: new KeyBinding(37, null, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectWordNext",	keyBinding: new KeyBinding(39, null, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectTextStart",	keyBinding: new KeyBinding(36, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectTextEnd",		keyBinding: new KeyBinding(35, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectTextStart",	keyBinding: new KeyBinding(38, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectTextEnd",		keyBinding: new KeyBinding(40, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectLineStart",	keyBinding: new KeyBinding(37, null, true, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectLineEnd",		keyBinding: new KeyBinding(39, null, true, null, true), predefined: true}); //$NON-NLS-0$
				//TODO These two actions should be changed to select paragraph start and select paragraph end  when word wrap is implemented
				bindings.push({actionID: "selectLineStart",	keyBinding: new KeyBinding(38, null, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectLineEnd",		keyBinding: new KeyBinding(40, null, true, true), predefined: true}); //$NON-NLS-0$
			} else {
				if (isLinux) {
					bindings.push({actionID: "selectWholeLineUp",		keyBinding: new KeyBinding(38, true, true), predefined: true}); //$NON-NLS-0$
					bindings.push({actionID: "selectWholeLineDown",		keyBinding: new KeyBinding(40, true, true), predefined: true}); //$NON-NLS-0$
				}
				bindings.push({actionID: "selectLineStart",		keyBinding: new KeyBinding(36, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectLineEnd",		keyBinding: new KeyBinding(35, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectWordPrevious",	keyBinding: new KeyBinding(37, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectWordNext",		keyBinding: new KeyBinding(39, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectTextStart",		keyBinding: new KeyBinding(36, true, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "selectTextEnd",		keyBinding: new KeyBinding(35, true, true), predefined: true}); //$NON-NLS-0$
			}

			//Misc
			bindings.push({actionID: "deletePrevious",		keyBinding: new KeyBinding(8), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "deletePrevious",		keyBinding: new KeyBinding(8, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "deleteNext",		keyBinding: new KeyBinding(46), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "deleteWordPrevious",	keyBinding: new KeyBinding(8, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "deleteWordPrevious",	keyBinding: new KeyBinding(8, true, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "deleteWordNext",		keyBinding: new KeyBinding(46, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "tab",			keyBinding: new KeyBinding(9), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "shiftTab",			keyBinding: new KeyBinding(9, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "enter",			keyBinding: new KeyBinding(13), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "enter",			keyBinding: new KeyBinding(13, null, true), predefined: true}); //$NON-NLS-0$
			bindings.push({actionID: "selectAll",		keyBinding: new KeyBinding('a', true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
			bindings.push({actionID: "toggleTabMode",	keyBinding: new KeyBinding('m', true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
			if (isMac) {
				bindings.push({actionID: "deleteNext",		keyBinding: new KeyBinding(46, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "deleteWordPrevious",	keyBinding: new KeyBinding(8, null, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "deleteWordNext",		keyBinding: new KeyBinding(46, null, null, true), predefined: true}); //$NON-NLS-0$
			}
				
			/*
			* Feature in IE/Chrome: prevent ctrl+'u', ctrl+'i', and ctrl+'b' from applying styles to the text.
			*
			* Note that Chrome applies the styles on the Mac with Ctrl instead of Cmd.
			*/
			if (!isFirefox) {
				var isMacChrome = isMac && isChrome;
				bindings.push({actionID: null, keyBinding: new KeyBinding('u', !isMacChrome, false, false, isMacChrome), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: null, keyBinding: new KeyBinding('i', !isMacChrome, false, false, isMacChrome), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: null, keyBinding: new KeyBinding('b', !isMacChrome, false, false, isMacChrome), predefined: true}); //$NON-NLS-0$
			}

			if (isFirefox) {
				bindings.push({actionID: "copy", keyBinding: new KeyBinding(45, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "paste", keyBinding: new KeyBinding(45, null, true), predefined: true}); //$NON-NLS-0$
				bindings.push({actionID: "cut", keyBinding: new KeyBinding(46, null, true), predefined: true}); //$NON-NLS-0$
			}

			// Add the emacs Control+ ... key bindings.
			if (isMac) {
				bindings.push({actionID: "lineStart", keyBinding: new KeyBinding("a", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "lineEnd", keyBinding: new KeyBinding("e", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "lineUp", keyBinding: new KeyBinding("p", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "lineDown", keyBinding: new KeyBinding("n", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "charPrevious", keyBinding: new KeyBinding("b", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "charNext", keyBinding: new KeyBinding("f", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "deletePrevious", keyBinding: new KeyBinding("h", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "deleteNext", keyBinding: new KeyBinding("d", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				bindings.push({actionID: "deleteLineEnd", keyBinding: new KeyBinding("k", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				if (isFirefox) {
					bindings.push({actionID: "scrollPageDown", keyBinding: new KeyBinding("v", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
					bindings.push({actionID: "deleteLineStart", keyBinding: new KeyBinding("u", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
					bindings.push({actionID: "deleteWordPrevious", keyBinding: new KeyBinding("w", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
				} else {
					bindings.push({actionID: "pageDown", keyBinding: new KeyBinding("v", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
					bindings.push({actionID: "centerLine", keyBinding: new KeyBinding("l", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
					bindings.push({actionID: "enterNoCursor", keyBinding: new KeyBinding("o", false, false, false, true), predefined: true}); //$NON-NLS-1$ //$NON-NLS-0$
					//TODO implement: y (yank), t (transpose)
				}
			}

			//1 to 1, no duplicates
			var self = this;
			this._actions = {
				"lineUp": {defaultHandler: function() {return self._doLineUp({select: false});}}, //$NON-NLS-0$
				"lineDown": {defaultHandler: function() {return self._doLineDown({select: false});}}, //$NON-NLS-0$
				"lineStart": {defaultHandler: function() {return self._doHome({select: false, ctrl:false});}}, //$NON-NLS-0$
				"lineEnd": {defaultHandler: function() {return self._doEnd({select: false, ctrl:false});}}, //$NON-NLS-0$
				"charPrevious": {defaultHandler: function() {return self._doCursorPrevious({select: false, unit:"character"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"charNext": {defaultHandler: function() {return self._doCursorNext({select: false, unit:"character"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"pageUp": {defaultHandler: function() {return self._doPageUp({select: false});}}, //$NON-NLS-0$
				"pageDown": {defaultHandler: function() {return self._doPageDown({select: false});}}, //$NON-NLS-0$
				"scrollPageUp": {defaultHandler: function() {return self._doScroll({type: "pageUp"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"scrollPageDown": {defaultHandler: function() {return self._doScroll({type: "pageDown"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"wordPrevious": {defaultHandler: function() {return self._doCursorPrevious({select: false, unit:"word"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"wordNext": {defaultHandler: function() {return self._doCursorNext({select: false, unit:"word"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"textStart": {defaultHandler: function() {return self._doHome({select: false, ctrl:true});}}, //$NON-NLS-0$
				"textEnd": {defaultHandler: function() {return self._doEnd({select: false, ctrl:true});}}, //$NON-NLS-0$
				"scrollTextStart": {defaultHandler: function() {return self._doScroll({type: "textStart"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"scrollTextEnd": {defaultHandler: function() {return self._doScroll({type: "textEnd"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"centerLine": {defaultHandler: function() {return self._doScroll({type: "centerLine"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				
				"selectLineUp": {defaultHandler: function() {return self._doLineUp({select: true});}}, //$NON-NLS-0$
				"selectLineDown": {defaultHandler: function() {return self._doLineDown({select: true});}}, //$NON-NLS-0$
				"selectWholeLineUp": {defaultHandler: function() {return self._doLineUp({select: true, wholeLine: true});}}, //$NON-NLS-0$
				"selectWholeLineDown": {defaultHandler: function() {return self._doLineDown({select: true, wholeLine: true});}}, //$NON-NLS-0$
				"selectLineStart": {defaultHandler: function() {return self._doHome({select: true, ctrl:false});}}, //$NON-NLS-0$
				"selectLineEnd": {defaultHandler: function() {return self._doEnd({select: true, ctrl:false});}}, //$NON-NLS-0$
				"selectCharPrevious": {defaultHandler: function() {return self._doCursorPrevious({select: true, unit:"character"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"selectCharNext": {defaultHandler: function() {return self._doCursorNext({select: true, unit:"character"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"selectPageUp": {defaultHandler: function() {return self._doPageUp({select: true});}}, //$NON-NLS-0$
				"selectPageDown": {defaultHandler: function() {return self._doPageDown({select: true});}}, //$NON-NLS-0$
				"selectWordPrevious": {defaultHandler: function() {return self._doCursorPrevious({select: true, unit:"word"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"selectWordNext": {defaultHandler: function() {return self._doCursorNext({select: true, unit:"word"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"selectTextStart": {defaultHandler: function() {return self._doHome({select: true, ctrl:true});}}, //$NON-NLS-0$
				"selectTextEnd": {defaultHandler: function() {return self._doEnd({select: true, ctrl:true});}}, //$NON-NLS-0$

				"deletePrevious": {defaultHandler: function() {return self._doBackspace({unit:"character"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"deleteNext": {defaultHandler: function() {return self._doDelete({unit:"character"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"deleteWordPrevious": {defaultHandler: function() {return self._doBackspace({unit:"word"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"deleteWordNext": {defaultHandler: function() {return self._doDelete({unit:"word"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"deleteLineStart": {defaultHandler: function() {return self._doBackspace({unit: "line"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"deleteLineEnd": {defaultHandler: function() {return self._doDelete({unit: "line"});}}, //$NON-NLS-1$ //$NON-NLS-0$
				"tab": {defaultHandler: function() {return self._doTab();}}, //$NON-NLS-0$
				"shiftTab": {defaultHandler: function() {return self._doShiftTab();}}, //$NON-NLS-0$
				"enter": {defaultHandler: function() {return self._doEnter();}}, //$NON-NLS-0$
				"enterNoCursor": {defaultHandler: function() {return self._doEnter({noCursor:true});}}, //$NON-NLS-0$
				"selectAll": {defaultHandler: function() {return self._doSelectAll();}}, //$NON-NLS-0$
				"copy": {defaultHandler: function() {return self._doCopy();}}, //$NON-NLS-0$
				"cut": {defaultHandler: function() {return self._doCut();}}, //$NON-NLS-0$
				"paste": {defaultHandler: function() {return self._doPaste();}}, //$NON-NLS-0$
				
				"toggleTabMode": {defaultHandler: function() {return self._doTabMode();}} //$NON-NLS-0$
			};
		},
		_createLine: function(parent, div, document, lineIndex, model) {
			var lineText = model.getLine(lineIndex);
			var lineStart = model.getLineStart(lineIndex);
			var e = {type:"LineStyle", textView: this, lineIndex: lineIndex, lineText: lineText, lineStart: lineStart}; //$NON-NLS-0$
			this.onLineStyle(e);
			var lineDiv = div || document.createElement("DIV"); //$NON-NLS-0$
			if (!div || !this._compare(div.viewStyle, e.style)) {
				this._applyStyle(e.style, lineDiv, div);
				if (div) { div._trim = null; }
				lineDiv.viewStyle = e.style;
				lineDiv.setAttribute("role", "presentation"); //$NON-NLS-1$ //$NON-NLS-0$
			}
			lineDiv.lineIndex = lineIndex;
			var ranges = [];
			var data = {tabOffset: 0, ranges: ranges};
			this._createRanges(e.ranges, lineText, 0, lineText.length, lineStart, data);
			
			/*
			* A trailing span with a whitespace is added for three different reasons:
			* 1. Make sure the height of each line is the largest of the default font
			* in normal, italic, bold, and italic-bold.
			* 2. When full selection is off, Firefox, Opera and IE9 do not extend the 
			* selection at the end of the line when the line is fully selected. 
			* 3. The height of a div with only an empty span is zero.
			*/
			var c = " "; //$NON-NLS-0$
			if (!this._fullSelection && isIE < 9) {
				/* 
				* IE8 already selects extra space at end of a line fully selected,
				* adding another space at the end of the line causes the selection 
				* to look too big. The fix is to use a zero-width space (\uFEFF) instead. 
				*/
				c = "\uFEFF"; //$NON-NLS-0$
			}
			if (isWebkit) {
				/*
				* Feature in WekKit. Adding a regular white space to the line will
				* cause the longest line in the view to wrap even though "pre" is set.
				* The fix is to use the zero-width non-joiner character (\u200C) instead.
				* Note: To not use \uFEFF because in old version of Chrome this character 
				* shows a glyph;
				*/
				c = "\u200C"; //$NON-NLS-0$
			}
			ranges.push({text: c, style: this._metrics.largestFontStyle, ignoreChars: 1});
			
			var range, span, style, oldSpan, oldStyle, text, oldText, end = 0, oldEnd = 0, next;
			var changeCount, changeStart;
			if (div) {
				var modelChangedEvent = div.modelChangedEvent;
				if (modelChangedEvent) {
					if (modelChangedEvent.removedLineCount === 0 && modelChangedEvent.addedLineCount === 0) {
						changeStart = modelChangedEvent.start - lineStart;
						changeCount = modelChangedEvent.addedCharCount - modelChangedEvent.removedCharCount;
					} else {
						changeStart = -1;
					}
					div.modelChangedEvent = undefined;
				}
				oldSpan = div.firstChild;
			}
			for (var i = 0; i < ranges.length; i++) {
				range = ranges[i];
				text = range.text;
				end += text.length;
				style = range.style;
				if (oldSpan) {
					oldText = oldSpan.firstChild.data;
					oldStyle = oldSpan.viewStyle;
					if (oldText === text && this._compare(style, oldStyle)) {
						oldEnd += oldText.length;
						oldSpan._rectsCache = undefined;
						span = oldSpan = oldSpan.nextSibling;
						continue;
					} else {
						while (oldSpan) {
							if (changeStart !== -1) {
								var spanEnd = end;
								if (spanEnd >= changeStart) {
									spanEnd -= changeCount;
								}
								var t = oldSpan.firstChild.data;
								var length = t ? t.length : 0;
								if (oldEnd + length > spanEnd) { break; }
								oldEnd += length;
							}
							next = oldSpan.nextSibling;
							lineDiv.removeChild(oldSpan);
							oldSpan = next;
						}
					}
				}
				span = this._createSpan(lineDiv, document, text, style, range.ignoreChars);
				if (oldSpan) {
					lineDiv.insertBefore(span, oldSpan);
				} else {
					lineDiv.appendChild(span);
				}
				if (div) {
					div.lineWidth = undefined;
				}
			}
			if (div) {
				var tmp = span ? span.nextSibling : null;
				while (tmp) {
					next = tmp.nextSibling;
					div.removeChild(tmp);
					tmp = next;
				}
			} else {
				parent.appendChild(lineDiv);
			}
			return lineDiv;
		},
		_createRanges: function(ranges, text, start, end, lineStart, data) {
			if (start >= end) { return; }
			if (ranges) {
				for (var i = 0; i < ranges.length; i++) {
					var range = ranges[i];
					if (range.end <= lineStart + start) { continue; }
					var styleStart = Math.max(lineStart + start, range.start) - lineStart;
					if (styleStart >= end) { break; }
					var styleEnd = Math.min(lineStart + end, range.end) - lineStart;
					if (styleStart < styleEnd) {
						styleStart = Math.max(start, styleStart);
						styleEnd = Math.min(end, styleEnd);
						if (start < styleStart) {
							this._createRange(text, start, styleStart, null, data);
						}
						while (i + 1 < ranges.length && ranges[i + 1].start - lineStart === styleEnd && this._compare(range.style, ranges[i + 1].style)) {
							range = ranges[i + 1];
							styleEnd = Math.min(lineStart + end, range.end) - lineStart;
							i++;
						}
						this._createRange(text, styleStart, styleEnd, range.style, data);
						start = styleEnd;
					}
				}
			}
			if (start < end) {
				this._createRange(text, start, end, null, data);
			}
		},
		_createRange: function(text, start, end, style, data) {
			if (start >= end) { return; }
			var tabSize = this._customTabSize, range;
			if (tabSize && tabSize !== 8) {
				var tabIndex = text.indexOf("\t", start); //$NON-NLS-0$
				while (tabIndex !== -1 && tabIndex < end) {
					if (start < tabIndex) {
						range = {text: text.substring(start, tabIndex), style: style};
						data.ranges.push(range);
						data.tabOffset += range.text.length;
					}
					var spacesCount = tabSize - (data.tabOffset % tabSize);
					if (spacesCount > 0) {
						//TODO hack to preserve text length in getDOMText()
						var spaces = "\u00A0"; //$NON-NLS-0$
						for (var i = 1; i < spacesCount; i++) {
							spaces += " "; //$NON-NLS-0$
						}
						range = {text: spaces, style: style, ignoreChars: spacesCount - 1};
						data.ranges.push(range);
						data.tabOffset += range.text.length;
					}
					start = tabIndex + 1;
					tabIndex = text.indexOf("\t", start); //$NON-NLS-0$
				}
			}
			if (start < end) {
				range = {text: text.substring(start, end), style: style};
				data.ranges.push(range);
				data.tabOffset += range.text.length;
			}
		},
		_createSpan: function(parent, document, text, style, ignoreChars) {
			var isLink = style && style.tagName === "A"; //$NON-NLS-0$
			if (isLink) { parent.hasLink = true; }
			var tagName = isLink && this._linksVisible ? "A" : "SPAN"; //$NON-NLS-1$ //$NON-NLS-0$
			var child = document.createElement(tagName);
			child.appendChild(document.createTextNode(text));
			this._applyStyle(style, child);
			if (tagName === "A") { //$NON-NLS-0$
				var self = this;
				addHandler(child, "click", function(e) { return self._handleLinkClick(e); }, false); //$NON-NLS-0$
			}
			child.viewStyle = style;
			if (ignoreChars) {
				child.ignoreChars = ignoreChars;
			}
			return child;
		},
		_createRuler: function(ruler, index) {
			if (!this._clientDiv) { return; }
			var side = ruler.getLocation();
			var rulerParent = side === "left" ? this._leftDiv : this._rightDiv; //$NON-NLS-0$
			rulerParent.style.display = "block";
			var div = document.createElement("DIV"); //$NON-NLS-0$
			div._ruler = ruler;
			div.rulerChanged = true;
			div.style.position = "relative"; //$NON-NLS-0$
			var row = rulerParent.firstChild.rows[0];
			var length = row.cells.length;
			index = index === undefined || index < 0 || index > length ? length : index;
			var cell = row.insertCell(index);
			cell.vAlign = "top"; //$NON-NLS-0$
			cell.appendChild(div);
		},
		_createView: function() {
			if (this._clientDiv) { return; }
			var parent = this._parent;
			while (parent.hasChildNodes()) { parent.removeChild(parent.lastChild); }

			var rootDiv = document.createElement("DIV"); //$NON-NLS-0$
			this._rootDiv = rootDiv;
			rootDiv.tabIndex = -1;
			rootDiv.style.position = "relative"; //$NON-NLS-0$
			rootDiv.style.overflow = "hidden"; //$NON-NLS-0$
			rootDiv.style.width = "100%"; //$NON-NLS-0$
			rootDiv.style.height = "100%"; //$NON-NLS-0$
			parent.style.overflow = "hidden"; //$NON-NLS-0$
			rootDiv.setAttribute("role", "application"); //$NON-NLS-1$ //$NON-NLS-0$
			parent.appendChild(rootDiv);
			
			var leftDiv = document.createElement("DIV"); //$NON-NLS-0$
			leftDiv.className = "textviewLeftRuler"; //$NON-NLS-0$
			this._leftDiv = leftDiv;
			leftDiv.tabIndex = -1;
			leftDiv.style.overflow = "hidden"; //$NON-NLS-0$
			leftDiv.style.MozUserSelect = "none"; //$NON-NLS-0$
			leftDiv.style.WebkitUserSelect = "none"; //$NON-NLS-0$
			leftDiv.style.position = "absolute"; //$NON-NLS-0$
			leftDiv.style.cursor = "default"; //$NON-NLS-0$
			leftDiv.style.display = "none";
			leftDiv.setAttribute("aria-hidden", "true"); //$NON-NLS-1$ //$NON-NLS-0$
			var table = document.createElement("TABLE"); //$NON-NLS-0$
			leftDiv.appendChild(table);
			table.cellPadding = "0px"; //$NON-NLS-0$
			table.cellSpacing = "0px"; //$NON-NLS-0$
			table.border = "0px"; //$NON-NLS-0$
			table.insertRow(0);
			rootDiv.appendChild(leftDiv);

			var viewDiv = document.createElement("DIV"); //$NON-NLS-0$
			viewDiv.className = "textview"; //$NON-NLS-0$
			this._viewDiv = viewDiv;
			viewDiv.tabIndex = -1;
			viewDiv.style.overflow = "auto"; //$NON-NLS-0$
			viewDiv.style.position = "absolute"; //$NON-NLS-0$
			viewDiv.style.top = "0px"; //$NON-NLS-0$
			viewDiv.style.borderWidth = "0px"; //$NON-NLS-0$
			viewDiv.style.margin = "0px"; //$NON-NLS-0$
			viewDiv.style.outline = "none"; //$NON-NLS-0$
			rootDiv.appendChild(viewDiv);
			
			var rightDiv = document.createElement("DIV"); //$NON-NLS-0$
			rightDiv.className = "textviewRightRuler"; //$NON-NLS-0$
			this._rightDiv = rightDiv;
			rightDiv.tabIndex = -1;
			rightDiv.style.display = "none";
			rightDiv.style.overflow = "hidden"; //$NON-NLS-0$
			rightDiv.style.MozUserSelect = "none"; //$NON-NLS-0$
			rightDiv.style.WebkitUserSelect = "none"; //$NON-NLS-0$
			rightDiv.style.position = "absolute"; //$NON-NLS-0$
			rightDiv.style.cursor = "default"; //$NON-NLS-0$
			rightDiv.setAttribute("aria-hidden", "true"); //$NON-NLS-1$ //$NON-NLS-0$
			table = document.createElement("TABLE"); //$NON-NLS-0$
			rightDiv.appendChild(table);
			table.cellPadding = "0px"; //$NON-NLS-0$
			table.cellSpacing = "0px"; //$NON-NLS-0$
			table.border = "0px"; //$NON-NLS-0$
			table.insertRow(0);
			rootDiv.appendChild(rightDiv);
				
			var scrollDiv = document.createElement("DIV"); //$NON-NLS-0$
			this._scrollDiv = scrollDiv;
			scrollDiv.style.margin = "0px"; //$NON-NLS-0$
			scrollDiv.style.borderWidth = "0px"; //$NON-NLS-0$
			scrollDiv.style.padding = "0px"; //$NON-NLS-0$
			viewDiv.appendChild(scrollDiv);
			
			if (isFirefox) {
				var clipboardDiv = document.createElement("DIV"); //$NON-NLS-0$
				this._clipboardDiv = clipboardDiv;
				clipboardDiv.style.position = "fixed"; //$NON-NLS-0$
				clipboardDiv.style.whiteSpace = "pre"; //$NON-NLS-0$
				clipboardDiv.style.left = "-1000px"; //$NON-NLS-0$
				rootDiv.appendChild(clipboardDiv);
			}

			if (!isIE && !isPad) {
				var clipDiv = document.createElement("DIV"); //$NON-NLS-0$
				this._clipDiv = clipDiv;
				clipDiv.style.position = "absolute"; //$NON-NLS-0$
				clipDiv.style.overflow = "hidden"; //$NON-NLS-0$
				clipDiv.style.margin = "0px"; //$NON-NLS-0$
				clipDiv.style.borderWidth = "0px"; //$NON-NLS-0$
				clipDiv.style.padding = "0px"; //$NON-NLS-0$
				rootDiv.appendChild(clipDiv);
				
				var clipScrollDiv = document.createElement("DIV"); //$NON-NLS-0$
				this._clipScrollDiv = clipScrollDiv;
				clipScrollDiv.style.position = "absolute"; //$NON-NLS-0$
				clipScrollDiv.style.height = "1px"; //$NON-NLS-0$
				clipScrollDiv.style.top = "-1000px"; //$NON-NLS-0$
				clipDiv.appendChild(clipScrollDiv);
			}
			
			this._setFullSelection(this._fullSelection, true);

			var clientDiv = document.createElement("DIV"); //$NON-NLS-0$
			clientDiv.className = "textviewContent"; //$NON-NLS-0$
			this._clientDiv = clientDiv;
			clientDiv.style.whiteSpace = "pre"; //$NON-NLS-0$
			clientDiv.style.position = "absolute"; //$NON-NLS-0$
			clientDiv.style.borderWidth = "0px"; //$NON-NLS-0$
			clientDiv.style.margin = "0px"; //$NON-NLS-0$
			clientDiv.style.padding = "0px"; //$NON-NLS-0$
			clientDiv.style.outline = "none"; //$NON-NLS-0$
			clientDiv.style.zIndex = "1"; //$NON-NLS-0$
			clientDiv.style.WebkitUserSelect = "text"; //$NON-NLS-0$
			clientDiv.setAttribute("spellcheck", "false"); //$NON-NLS-1$ //$NON-NLS-0$
			if (isPad) {
				clientDiv.style.WebkitTapHighlightColor = "transparent"; //$NON-NLS-0$
			}
			(this._clipDiv || rootDiv).appendChild(clientDiv);
			
			if (isPad) {
				var vScrollDiv = document.createElement("DIV"); //$NON-NLS-0$
				this._vScrollDiv = vScrollDiv;
				vScrollDiv.style.position = "absolute"; //$NON-NLS-0$
				vScrollDiv.style.borderWidth = "1px"; //$NON-NLS-0$
				vScrollDiv.style.borderColor = "white"; //$NON-NLS-0$
				vScrollDiv.style.borderStyle = "solid"; //$NON-NLS-0$
				vScrollDiv.style.borderRadius = "4px"; //$NON-NLS-0$
				vScrollDiv.style.backgroundColor = "black"; //$NON-NLS-0$
				vScrollDiv.style.opacity = "0.5"; //$NON-NLS-0$
				vScrollDiv.style.margin = "0px"; //$NON-NLS-0$
				vScrollDiv.style.padding = "0px"; //$NON-NLS-0$
				vScrollDiv.style.outline = "none"; //$NON-NLS-0$
				vScrollDiv.style.zIndex = "3"; //$NON-NLS-0$
				vScrollDiv.style.width = "8px"; //$NON-NLS-0$
				vScrollDiv.style.display = "none"; //$NON-NLS-0$
				rootDiv.appendChild(vScrollDiv);
				var hScrollDiv = document.createElement("DIV"); //$NON-NLS-0$
				this._hScrollDiv = hScrollDiv;
				hScrollDiv.style.position = "absolute"; //$NON-NLS-0$
				hScrollDiv.style.borderWidth = "1px"; //$NON-NLS-0$
				hScrollDiv.style.borderColor = "white"; //$NON-NLS-0$
				hScrollDiv.style.borderStyle = "solid"; //$NON-NLS-0$
				hScrollDiv.style.borderRadius = "4px"; //$NON-NLS-0$
				hScrollDiv.style.backgroundColor = "black"; //$NON-NLS-0$
				hScrollDiv.style.opacity = "0.5"; //$NON-NLS-0$
				hScrollDiv.style.margin = "0px"; //$NON-NLS-0$
				hScrollDiv.style.padding = "0px"; //$NON-NLS-0$
				hScrollDiv.style.outline = "none"; //$NON-NLS-0$
				hScrollDiv.style.zIndex = "3"; //$NON-NLS-0$
				hScrollDiv.style.height = "8px"; //$NON-NLS-0$
				hScrollDiv.style.display = "none"; //$NON-NLS-0$
				rootDiv.appendChild(hScrollDiv);
			}

			if (isFirefox && !clientDiv.setCapture) {
				var overlayDiv = document.createElement("DIV"); //$NON-NLS-0$
				this._overlayDiv = overlayDiv;
				overlayDiv.style.position = clientDiv.style.position;
				overlayDiv.style.borderWidth = clientDiv.style.borderWidth;
				overlayDiv.style.margin = clientDiv.style.margin;
				overlayDiv.style.padding = clientDiv.style.padding;
				overlayDiv.style.cursor = "text"; //$NON-NLS-0$
				overlayDiv.style.zIndex = "2"; //$NON-NLS-0$
				(this._clipDiv || rootDiv).appendChild(overlayDiv);
			}
			clientDiv.contentEditable = "true"; //$NON-NLS-0$
			clientDiv.setAttribute("role", "textbox"); //$NON-NLS-1$ //$NON-NLS-0$
			clientDiv.setAttribute("aria-multiline", "true"); //$NON-NLS-1$ //$NON-NLS-0$
			this._setReadOnly(this._readonly);
			this._setThemeClass(this._themeClass, true);
			this._setTabSize(this._tabSize, true);
			if (!document.getElementById("_textviewStyle")) { //$NON-NLS-0$
				var styleText = "";
				if (isWebkit && this._metrics.scrollWidth > 0) {
					styleText += "\n.textviewContainer ::-webkit-scrollbar-corner {background: #eeeeee;}"; //$NON-NLS-0$
				}
				if (isFirefox && isMac && this._highlightRGB && this._highlightRGB !== "Highlight") { //$NON-NLS-0$
					styleText += "\n.textviewContainer ::-moz-selection {background: " + this._highlightRGB + ";}"; //$NON-NLS-1$ //$NON-NLS-0$
				}
				if (styleText) {
					var stylesheet = document.createElement("STYLE"); //$NON-NLS-0$
					stylesheet.id = "_textviewStyle"; //$NON-NLS-0$
					var head = document.getElementsByTagName("HEAD")[0] || document.documentElement; //$NON-NLS-0$
					stylesheet.appendChild(document.createTextNode(styleText));
					head.insertBefore(stylesheet, head.firstChild);
				}
			}
			this._hookEvents();
			var rulers = this._rulers;
			for (var i=0; i<rulers.length; i++) {
				this._createRuler(rulers[i]);
			}
			this._updatePage();
		},
		_defaultOptions: function() {
			return {
				parent: {value: undefined, update: null},
				model: {value: undefined, update: this.setModel},
				readonly: {value: false, update: this._setReadOnly},
				fullSelection: {value: true, update: this._setFullSelection},
				tabMode: { value: true, update: null },
				tabSize: {value: 8, update: this._setTabSize},
				expandTab: {value: false, update: null},
				themeClass: {value: undefined, update: this._setThemeClass}
			};
		},
		_destroyRuler: function(ruler) {
			var side = ruler.getLocation();
			var rulerParent = side === "left" ? this._leftDiv : this._rightDiv; //$NON-NLS-0$
			if (rulerParent) {
				var row = rulerParent.firstChild.rows[0];
				var cells = row.cells;
				for (var index = 0; index < cells.length; index++) {
					var cell = cells[index];
					if (cell.firstChild._ruler === ruler) { break; }
				}
				if (index === cells.length) { return; }
				row.cells[index]._ruler = undefined;
				row.deleteCell(index);
				if (cells.length === 0) {
					rulerParent.style.display = "none";
				}
			}
		},
		_destroyView: function() {
			var clientDiv = this._clientDiv;
			if (!clientDiv) { return; }
			this._setGrab(null);
			this._unhookEvents();

			/* Destroy timers */
			if (this._autoScrollTimerID) {
				clearTimeout(this._autoScrollTimerID);
				this._autoScrollTimerID = null;
			}
			if (this._updateTimer) {
				clearTimeout(this._updateTimer);
				this._updateTimer = null;
			}
			
			var rootDiv = this._rootDiv;
			rootDiv.parentNode.removeChild(rootDiv);

			/* Destroy DOM */
			this._selDiv1 = null;
			this._selDiv2 = null;
			this._selDiv3 = null;
			this._clipboardDiv = null;
			this._rootDiv = null;
			this._scrollDiv = null;
			this._viewDiv = null;
			this._clipDiv = null;
			this._clipScrollDiv = null;
			this._clientDiv = null;
			this._overlayDiv = null;
			this._leftDiv = null;
			this._rightDiv = null;
			this._vScrollDiv = null;
			this._hScrollDiv = null;
		},
		_doAutoScroll: function (direction, x, y) {
			this._autoScrollDir = direction;
			this._autoScrollX = x;
			this._autoScrollY = y;
			if (!this._autoScrollTimerID) {
				this._autoScrollTimer();
			}
		},
		_endAutoScroll: function () {
			if (this._autoScrollTimerID) { clearTimeout(this._autoScrollTimerID); }
			this._autoScrollDir = undefined;
			this._autoScrollTimerID = undefined;
		},
		_fixCaret: function() {
			var clientDiv = this._clientDiv;
			if (clientDiv) {
				var hasFocus = this._hasFocus;
				this._ignoreFocus = true;
				if (hasFocus) { clientDiv.blur(); }
				clientDiv.contentEditable = false;
				clientDiv.contentEditable = true;
				if (hasFocus) { clientDiv.focus(); }
				this._ignoreFocus = false;
			}
		},
		_getBaseText: function(start, end) {
			var model = this._model;
			/* This is the only case the view access the base model, alternatively the view could use a event to application to customize the text */
			if (model.getBaseModel) {
				start = model.mapOffset(start);
				end = model.mapOffset(end);
				model = model.getBaseModel();
			}
			return model.getText(start, end);
		},
		_getBorder: function (node) {
			var left,top,right,bottom;
			if (window.getComputedStyle) {
				var style = window.getComputedStyle(node, null);
				left = style.getPropertyValue("border-left-width"); //$NON-NLS-0$
				top = style.getPropertyValue("border-top-width"); //$NON-NLS-0$
				right = style.getPropertyValue("border-right-width"); //$NON-NLS-0$
				bottom = style.getPropertyValue("border-bottom-width"); //$NON-NLS-0$
			} else if (node.currentStyle) {
				left = node.currentStyle.borderLeftWidth;
				top = node.currentStyle.borderTopWidth;
				right = node.currentStyle.borderRightWidth;
				bottom = node.currentStyle.borderBottomWidth;
			}
			return {
				left: parseInt(left, 10) || 0,
				top: parseInt(top, 10) || 0,
				right: parseInt(right, 10) || 0,
				bottom: parseInt(bottom, 10) || 0
			};
		},
		_getBoundsAtOffset: function (offset) {
			var model = this._model;
			var clientDiv = this._clientDiv;
			var lineIndex = model.getLineAtOffset(offset);
			var dummy;
			var child = this._getLineNode(lineIndex);
			if (!child) {
				child = dummy = this._createLine(clientDiv, null, document, lineIndex, model);
			}
			var result = null;
			if (offset < model.getLineEnd(lineIndex)) {
				var lineOffset = model.getLineStart(lineIndex);
				var lineChild = child.firstChild;
				while (lineChild) {
					var textNode = lineChild.firstChild;
					var nodeLength = textNode.length; 
					if (lineChild.ignoreChars) {
						nodeLength -= lineChild.ignoreChars;
					}
					if (lineOffset + nodeLength > offset) {
						var index = offset - lineOffset;
						var range;
						if (isRangeRects) {
							range = document.createRange();
							range.setStart(textNode, index);
							range.setEnd(textNode, index + 1);
							result = range.getBoundingClientRect();
						} else if (isIE) {
							range = document.body.createTextRange();
							range.moveToElementText(lineChild);
							range.collapse();
							range.moveEnd("character", index + 1); //$NON-NLS-0$
							range.moveStart("character", index); //$NON-NLS-0$
							result = range.getBoundingClientRect();
						} else {
							var text = textNode.data;
							lineChild.removeChild(textNode);
							lineChild.appendChild(document.createTextNode(text.substring(0, index)));
							var span = document.createElement("SPAN"); //$NON-NLS-0$
							span.appendChild(document.createTextNode(text.substring(index, index + 1)));
							lineChild.appendChild(span);
							lineChild.appendChild(document.createTextNode(text.substring(index + 1)));
							result = span.getBoundingClientRect();
							lineChild.innerHTML = "";
							lineChild.appendChild(textNode);
							if (!dummy) {
								/*
								 * Removing the element node that holds the selection start or end
								 * causes the selection to be lost. The fix is to detect this case
								 * and restore the selection. 
								 */
								var s = this._getSelection();
								if ((lineOffset <= s.start && s.start < lineOffset + nodeLength) ||  (lineOffset <= s.end && s.end < lineOffset + nodeLength)) {
									this._updateDOMSelection();
								}
							}
						}
						if (isIE) {
							var logicalXDPI = window.screen.logicalXDPI;
							var deviceXDPI = window.screen.deviceXDPI;
							result.left = result.left * logicalXDPI / deviceXDPI;
							result.right = result.right * logicalXDPI / deviceXDPI;
						}
						break;
					}
					lineOffset += nodeLength;
					lineChild = lineChild.nextSibling;
				}
			}
			if (!result) {
				var rect = this._getLineBoundingClientRect(child);
				result = {left: rect.right, right: rect.right};
			}
			if (dummy) { clientDiv.removeChild(dummy); }
			return result;
		},
		_getBottomIndex: function (fullyVisible) {
			var child = this._bottomChild;
			if (fullyVisible && this._getClientHeight() > this._getLineHeight()) {
				var rect = child.getBoundingClientRect();
				var clientRect = this._clientDiv.getBoundingClientRect();
				if (rect.bottom > clientRect.bottom) {
					child = this._getLinePrevious(child) || child;
				}
			}
			return child.lineIndex;
		},
		_getClientHeight: function() {
			var viewPad = this._getViewPadding();
			return Math.max(0, this._viewDiv.clientHeight - viewPad.top - viewPad.bottom);
		},
		_getClientWidth: function() {
			var viewPad = this._getViewPadding();
			return Math.max(0, this._viewDiv.clientWidth - viewPad.left - viewPad.right);
		},
		_getClipboardText: function (event, handler) {
			var delimiter = this._model.getLineDelimiter();
			var clipboadText, text;
			if (window.clipboardData) {
				//IE
				clipboadText = [];
				text = window.clipboardData.getData("Text"); //$NON-NLS-0$
				this._convertDelimiter(text, function(t) {clipboadText.push(t);}, function() {clipboadText.push(delimiter);});
				text = clipboadText.join("");
				if (handler) { handler(text); }
				return text;
			}
			if (isFirefox) {
				this._ignoreFocus = true;
				var clipboardDiv = this._clipboardDiv;
				clipboardDiv.innerHTML = "<pre contenteditable=''></pre>"; //$NON-NLS-0$
				clipboardDiv.firstChild.focus();
				var self = this;
				var _getText = function() {
					var noteText = self._getTextFromElement(clipboardDiv);
					clipboardDiv.innerHTML = "";
					clipboadText = [];
					self._convertDelimiter(noteText, function(t) {clipboadText.push(t);}, function() {clipboadText.push(delimiter);});
					return clipboadText.join("");
				};
				
				/* Try execCommand first. Works on firefox with clipboard permission. */
				var result = false;
				this._ignorePaste = true;

				/* Do not try execCommand if middle-click is used, because if we do, we get the clipboard text, not the primary selection text. */
				if (!isLinux || this._lastMouseButton !== 2) {
					try {
						result = document.execCommand("paste", false, null); //$NON-NLS-0$
					} catch (ex) {
						/* Firefox can throw even when execCommand() works, see bug 362835. */
						result = clipboardDiv.childNodes.length > 1 || clipboardDiv.firstChild && clipboardDiv.firstChild.childNodes.length > 0;
					}
				}
				this._ignorePaste = false;
				if (!result) {
					/* Try native paste in DOM, works for firefox during the paste event. */
					if (event) {
						setTimeout(function() {
							self.focus();
							text = _getText();
							if (text && handler) {
								handler(text);
							}
							self._ignoreFocus = false;
						}, 0);
						return null;
					} else {
						/* no event and no clipboard permission, paste can't be performed */
						this.focus();
						this._ignoreFocus = false;
						return "";
					}
				}
				this.focus();
				this._ignoreFocus = false;
				text = _getText();
				if (text && handler) {
					handler(text);
				}
				return text;
			}
			//webkit
			if (event && event.clipboardData) {
				/*
				* Webkit (Chrome/Safari) allows getData during the paste event
				* Note: setData is not allowed, not even during copy/cut event
				*/
				clipboadText = [];
				text = event.clipboardData.getData("text/plain"); //$NON-NLS-0$
				this._convertDelimiter(text, function(t) {clipboadText.push(t);}, function() {clipboadText.push(delimiter);});
				text = clipboadText.join("");
				if (text && handler) {
					handler(text);
				}
				return text;
			} else {
				//TODO try paste using extension (Chrome only)
			}
			return "";
		},
		_getDOMText: function(lineIndex) {
			var child = this._getLineNode(lineIndex);
			var lineChild = child.firstChild;
			var text = "";
			while (lineChild) {
				var textNode = lineChild.firstChild;
				while (textNode) {
					if (lineChild.ignoreChars) {
						for (var i = 0; i < textNode.length; i++) {
							var ch = textNode.data.substring(i, i + 1);
							if (ch !== " ") { //$NON-NLS-0$
								text += ch;
							}
						}
					} else {
						text += textNode.data;
					}
					textNode = textNode.nextSibling;
				}
				lineChild = lineChild.nextSibling;
			}
			return text;
		},
		_getTextFromElement: function(element) {
			var document = element.ownerDocument;
			var window = document.defaultView;
			if (!window.getSelection) {
				return element.innerText || element.textContent;
			}

			var newRange = document.createRange();
			newRange.selectNode(element);

			var selection = window.getSelection();
			var oldRanges = [], i;
			for (i = 0; i < selection.rangeCount; i++) {
				oldRanges.push(selection.getRangeAt(i));
			}

			this._ignoreSelect = true;
			selection.removeAllRanges();
			selection.addRange(newRange);

			var text = selection.toString();

			selection.removeAllRanges();
			for (i = 0; i < oldRanges.length; i++) {
				selection.addRange(oldRanges[i]);
			}

			this._ignoreSelect = false;
			return text;
		},
		_getViewPadding: function() {
			return this._metrics.viewPadding;
		},
		_getLineTrim: function(line) {
			var trim = line._trim;
			if (!trim) {
				trim = this._getPadding(line);
				var border = this._getBorder(line);
				trim.left += border.left;
				trim.top += border.top;
				trim.right += border.right;
				trim.bottom += border.bottom;
				line._trim = trim;
			}
			return trim;
		},
		_getLineBoundingClientRect: function (child, noTrim) {
			var rect = child.getBoundingClientRect();
			rect = {left: rect.left, top: rect.top, right: rect.left, bottom: rect.bottom};
			var lastChild = child.lastChild;
			//Remove any artificial trailing whitespace in the line
			while (lastChild && lastChild.ignoreChars === lastChild.firstChild.length) {
				lastChild = lastChild.previousSibling;
			}
			if (lastChild) {
				var lastRect = lastChild.getBoundingClientRect();
				rect.right = lastRect.right + this._getLineTrim(child).right;
			}
			if (noTrim) {
				var padding = this._getLineTrim(child);
				rect.left = rect.left + padding.left;
				rect.right = rect.right - padding.right;
			}
			return rect;
		},
		_getLineHeight: function() {
			return this._metrics.lineHeight;
		},
		_getLineNode: function (lineIndex) {
			var clientDiv = this._clientDiv;
			var child = clientDiv.firstChild;
			while (child) {
				if (lineIndex === child.lineIndex) {
					return child;
				}
				child = child.nextSibling;
			}
			return undefined;
		},
		_getLineNext: function (lineNode) {
			var node = lineNode ? lineNode.nextSibling : this._clientDiv.firstChild;
			while (node && node.lineIndex === -1) {
				node = node.nextSibling;
			}
			return node;
		},
		_getLinePrevious: function (lineNode) {
			var node = lineNode ? lineNode.previousSibling : this._clientDiv.lastChild;
			while (node && node.lineIndex === -1) {
				node = node.previousSibling;
			}
			return node;
		},
		_getOffset: function (offset, unit, direction) {
			if (unit === "line") { //$NON-NLS-0$
				var model = this._model;
				var lineIndex = model.getLineAtOffset(offset);
				if (direction > 0) {
					return model.getLineEnd(lineIndex);
				}
				return model.getLineStart(lineIndex);
			}
			if (unit === "wordend") { //$NON-NLS-0$
				return this._getOffset_W3C(offset, unit, direction);
			}
			return isIE ? this._getOffset_IE(offset, unit, direction) : this._getOffset_W3C(offset, unit, direction);
		},
		_getOffset_W3C: function (offset, unit, direction) {
			function _isPunctuation(c) {
				return (33 <= c && c <= 47) || (58 <= c && c <= 64) || (91 <= c && c <= 94) || c === 96 || (123 <= c && c <= 126);
			}
			function _isWhitespace(c) {
				return c === 32 || c === 9;
			}
			if (unit === "word" || unit === "wordend") { //$NON-NLS-1$ //$NON-NLS-0$
				var model = this._model;
				var lineIndex = model.getLineAtOffset(offset);
				var lineText = model.getLine(lineIndex);
				var lineStart = model.getLineStart(lineIndex);
				var lineEnd = model.getLineEnd(lineIndex);
				var lineLength = lineText.length;
				var offsetInLine = offset - lineStart;
				
				
				var c, previousPunctuation, previousLetterOrDigit, punctuation, letterOrDigit;
				if (direction > 0) {
					if (offsetInLine === lineLength) { return lineEnd; }
					c = lineText.charCodeAt(offsetInLine);
					previousPunctuation = _isPunctuation(c); 
					previousLetterOrDigit = !previousPunctuation && !_isWhitespace(c);
					offsetInLine++;
					while (offsetInLine < lineLength) {
						c = lineText.charCodeAt(offsetInLine);
						punctuation = _isPunctuation(c);
						if (unit === "wordend") { //$NON-NLS-0$
							if (!punctuation && previousPunctuation) { break; }
						} else {
							if (punctuation && !previousPunctuation) { break; }
						}
						letterOrDigit  = !punctuation && !_isWhitespace(c);
						if (unit === "wordend") { //$NON-NLS-0$
							if (!letterOrDigit && previousLetterOrDigit) { break; }
						} else {
							if (letterOrDigit && !previousLetterOrDigit) { break; }
						}
						previousLetterOrDigit = letterOrDigit;
						previousPunctuation = punctuation;
						offsetInLine++;
					}
				} else {
					if (offsetInLine === 0) { return lineStart; }
					offsetInLine--;
					c = lineText.charCodeAt(offsetInLine);
					previousPunctuation = _isPunctuation(c); 
					previousLetterOrDigit = !previousPunctuation && !_isWhitespace(c);
					while (0 < offsetInLine) {
						c = lineText.charCodeAt(offsetInLine - 1);
						punctuation = _isPunctuation(c);
						if (unit === "wordend") { //$NON-NLS-0$
							if (punctuation && !previousPunctuation) { break; }
						} else {
							if (!punctuation && previousPunctuation) { break; }
						}
						letterOrDigit  = !punctuation && !_isWhitespace(c);
						if (unit === "wordend") { //$NON-NLS-0$
							if (letterOrDigit && !previousLetterOrDigit) { break; }
						} else {
							if (!letterOrDigit && previousLetterOrDigit) { break; }
						}
						previousLetterOrDigit = letterOrDigit;
						previousPunctuation = punctuation;
						offsetInLine--;
					}
				}
				return lineStart + offsetInLine;
			}
			return offset + direction;
		},
		_getOffset_IE: function (offset, unit, direction) {
			var model = this._model;
			var lineIndex = model.getLineAtOffset(offset);
			var clientDiv = this._clientDiv;
			var dummy;
			var child = this._getLineNode(lineIndex);
			if (!child) {
				child = dummy = this._createLine(clientDiv, null, document, lineIndex, model);
			}
			var result = 0, range, length;
			var lineOffset = model.getLineStart(lineIndex);
			if (offset === model.getLineEnd(lineIndex)) {
				range = document.body.createTextRange();
				range.moveToElementText(child.lastChild);
				length = range.text.length;
				range.moveEnd(unit, direction);
				result = offset + range.text.length - length;
			} else if (offset === lineOffset && direction < 0) {
				result = lineOffset;
			} else {
				var lineChild = child.firstChild;
				while (lineChild) {
					var textNode = lineChild.firstChild;
					var nodeLength = textNode.length;
					if (lineChild.ignoreChars) {
						nodeLength -= lineChild.ignoreChars;
					}
					if (lineOffset + nodeLength > offset) {
						range = document.body.createTextRange();
						if (offset === lineOffset && direction < 0) {
							range.moveToElementText(lineChild.previousSibling);
						} else {
							range.moveToElementText(lineChild);
							range.collapse();
							range.moveEnd("character", offset - lineOffset); //$NON-NLS-0$
						}
						length = range.text.length;
						range.moveEnd(unit, direction);
						result = offset + range.text.length - length;
						break;
					}
					lineOffset = nodeLength + lineOffset;
					lineChild = lineChild.nextSibling;
				}
			}
			if (dummy) { clientDiv.removeChild(dummy); }
			return result;
		},
		_getOffsetToX: function (offset) {
			return this._getBoundsAtOffset(offset).left;
		},
		_getPadding: function (node) {
			var left,top,right,bottom;
			if (window.getComputedStyle) {
				var style = window.getComputedStyle(node, null);
				left = style.getPropertyValue("padding-left"); //$NON-NLS-0$
				top = style.getPropertyValue("padding-top"); //$NON-NLS-0$
				right = style.getPropertyValue("padding-right"); //$NON-NLS-0$
				bottom = style.getPropertyValue("padding-bottom"); //$NON-NLS-0$
			} else if (node.currentStyle) {
				left = node.currentStyle.paddingLeft;
				top = node.currentStyle.paddingTop;
				right = node.currentStyle.paddingRight;
				bottom = node.currentStyle.paddingBottom;
			}
			return {
				left: parseInt(left, 10) || 0, 
				top: parseInt(top, 10) || 0,
				right: parseInt(right, 10) || 0,
				bottom: parseInt(bottom, 10) || 0
			};
		},
		_getScroll: function() {
			var viewDiv = this._viewDiv;
			return {x: viewDiv.scrollLeft, y: viewDiv.scrollTop};
		},
		_getSelection: function () {
			return this._selection.clone();
		},
		_getTopIndex: function (fullyVisible) {
			var child = this._topChild;
			if (fullyVisible && this._getClientHeight() > this._getLineHeight()) {
				var rect = child.getBoundingClientRect();
				var viewPad = this._getViewPadding();
				var viewRect = this._viewDiv.getBoundingClientRect();
				if (rect.top < viewRect.top + viewPad.top) {
					child = this._getLineNext(child) || child;
				}
			}
			return child.lineIndex;
		},
		_getXToOffset: function (lineIndex, x) {
			var model = this._model;
			var lineStart = model.getLineStart(lineIndex);
			var lineEnd = model.getLineEnd(lineIndex);
			if (lineStart === lineEnd) {
				return lineStart;
			}
			var clientDiv = this._clientDiv;
			var dummy;
			var child = this._getLineNode(lineIndex);
			if (!child) {
				child = dummy = this._createLine(clientDiv, null, document, lineIndex, model);
			}
			var lineRect = this._getLineBoundingClientRect(child, true);
			if (x < lineRect.left) { x = lineRect.left; }
			if (x > lineRect.right) { x = lineRect.right; }
			/*
			* Bug in IE 8 and earlier. The coordinates of getClientRects() are relative to
			* the browser window.  The fix is to convert to the frame window before using it. 
			*/
			var deltaX = 0, rects;
			if (isIE < 9) {
				rects = child.getClientRects();
				var minLeft = rects[0].left;
				for (var i=1; i<rects.length; i++) {
					minLeft = Math.min(rects[i].left, minLeft);
				}
				deltaX = minLeft - lineRect.left - this._getLineTrim(child).left;
			}
			var scrollX = this._getScroll().x;
			function _getClientRects(element) {
				var rects, newRects, i, r;
				if (!element._rectsCache) {
					rects = element.getClientRects();
					newRects = [rects.length];
					for (i = 0; i<rects.length; i++) {
						r = rects[i];
						newRects[i] = {left: r.left - deltaX + scrollX, top: r.top, right: r.right - deltaX + scrollX, bottom: r.bottom};
					}
					element._rectsCache = newRects; 
				}
				rects = element._rectsCache;
				newRects = [rects.length];
				for (i = 0; i<rects.length; i++) {
					r = rects[i];
					newRects[i] = {left: r.left - scrollX, top: r.top, right: r.right - scrollX, bottom: r.bottom};
				}
				return newRects;
			}
			var logicalXDPI = isIE ? window.screen.logicalXDPI : 1;
			var deviceXDPI = isIE ? window.screen.deviceXDPI : 1;
			var offset = lineStart;
			var lineChild = child.firstChild;
			done:
			while (lineChild) {
				var textNode = lineChild.firstChild;
				var nodeLength = textNode.length;
				if (lineChild.ignoreChars) {
					nodeLength -= lineChild.ignoreChars;
				}
				rects = _getClientRects(lineChild);
				for (var j = 0; j < rects.length; j++) {
					var rect = rects[j];
					if (rect.left <= x && x < rect.right) {
						var range, start, end;
						if (isIE || isRangeRects) {
							range = isRangeRects ? document.createRange() : document.body.createTextRange();
							var high = nodeLength;
							var low = -1;
							while ((high - low) > 1) {
								var mid = Math.floor((high + low) / 2);
								start = low + 1;
								end = mid === nodeLength - 1 && lineChild.ignoreChars ? textNode.length : mid + 1;
								if (isRangeRects) {
									range.setStart(textNode, start);
									range.setEnd(textNode, end);
								} else {
									range.moveToElementText(lineChild);
									range.move("character", start); //$NON-NLS-0$
									range.moveEnd("character", end - start); //$NON-NLS-0$
								}
								rects = range.getClientRects();
								var found = false;
								for (var k = 0; k < rects.length; k++) {
									rect = rects[k];
									var rangeLeft = rect.left * logicalXDPI / deviceXDPI - deltaX;
									var rangeRight = rect.right * logicalXDPI / deviceXDPI - deltaX;
									if (rangeLeft <= x && x < rangeRight) {
										found = true;
										break;
									}
								}
								if (found) {
									high = mid;
								} else {
									low = mid;
								}
							}
							offset += high;
							start = high;
							end = high === nodeLength - 1 && lineChild.ignoreChars ? textNode.length : Math.min(high + 1, textNode.length);
							if (isRangeRects) {
								range.setStart(textNode, start);
								range.setEnd(textNode, end);
							} else {
								range.moveToElementText(lineChild);
								range.move("character", start); //$NON-NLS-0$
								range.moveEnd("character", end - start); //$NON-NLS-0$
							}
							rect = range.getClientRects()[0];
							//TODO test for character trailing (wrong for bidi)
							if (x > ((rect.left * logicalXDPI / deviceXDPI - deltaX) + ((rect.right - rect.left) * logicalXDPI / deviceXDPI / 2))) {
								offset++;
							}
						} else {
							var newText = [];
							for (var q = 0; q < nodeLength; q++) {
								newText.push("<span>"); //$NON-NLS-0$
								if (q === nodeLength - 1) {
									newText.push(textNode.data.substring(q));
								} else {
									newText.push(textNode.data.substring(q, q + 1));
								}
								newText.push("</span>"); //$NON-NLS-0$
							}
							lineChild.innerHTML = newText.join("");
							var rangeChild = lineChild.firstChild;
							while (rangeChild) {
								rect = rangeChild.getBoundingClientRect();
								if (rect.left <= x && x < rect.right) {
									//TODO test for character trailing (wrong for bidi)
									if (x > rect.left + (rect.right - rect.left) / 2) {
										offset++;
									}
									break;
								}
								offset++;
								rangeChild = rangeChild.nextSibling;
							}
							if (!dummy) {
								lineChild.innerHTML = "";
								lineChild.appendChild(textNode);
								/*
								 * Removing the element node that holds the selection start or end
								 * causes the selection to be lost. The fix is to detect this case
								 * and restore the selection. 
								 */
								var s = this._getSelection();
								if ((offset <= s.start && s.start < offset + nodeLength) || (offset <= s.end && s.end < offset + nodeLength)) {
									this._updateDOMSelection();
								}
							}
						}
						break done;
					}
				}
				offset += nodeLength;
				lineChild = lineChild.nextSibling;
			}
			if (dummy) { clientDiv.removeChild(dummy); }
			return Math.min(lineEnd, Math.max(lineStart, offset));
		},
		_getYToLine: function (y) {
			var viewPad = this._getViewPadding();
			var viewRect = this._viewDiv.getBoundingClientRect();
			y -= viewRect.top + viewPad.top;
			var lineHeight = this._getLineHeight();
			var lineIndex = Math.floor((y + this._getScroll().y) / lineHeight);
			var lineCount = this._model.getLineCount();
			return Math.max(0, Math.min(lineCount - 1, lineIndex));
		},
		_hookEvents: function() {
			var self = this;
			this._modelListener = {
				/** @private */
				onChanging: function(modelChangingEvent) {
					self._onModelChanging(modelChangingEvent);
				},
				/** @private */
				onChanged: function(modelChangedEvent) {
					self._onModelChanged(modelChangedEvent);
				}
			};
			this._model.addEventListener("Changing", this._modelListener.onChanging); //$NON-NLS-0$
			this._model.addEventListener("Changed", this._modelListener.onChanged); //$NON-NLS-0$
			
			var handlers = this._handlers = [];
			var clientDiv = this._clientDiv, viewDiv = this._viewDiv, rootDiv = this._rootDiv;
			var topNode = this._overlayDiv || clientDiv;
			var grabNode = isIE ? document : window;
			handlers.push({target: window, type: "resize", handler: function(e) { return self._handleResize(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "blur", handler: function(e) { return self._handleBlur(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "focus", handler: function(e) { return self._handleFocus(e);}}); //$NON-NLS-0$
			handlers.push({target: viewDiv, type: "focus", handler: function(e) { clientDiv.focus(); }}); //$NON-NLS-0$
			handlers.push({target: viewDiv, type: "scroll", handler: function(e) { return self._handleScroll(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "keydown", handler: function(e) { return self._handleKeyDown(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "keypress", handler: function(e) { return self._handleKeyPress(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "keyup", handler: function(e) { return self._handleKeyUp(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "selectstart", handler: function(e) { return self._handleSelectStart(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "contextmenu", handler: function(e) { return self._handleContextMenu(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "copy", handler: function(e) { return self._handleCopy(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "cut", handler: function(e) { return self._handleCut(e);}}); //$NON-NLS-0$
			handlers.push({target: clientDiv, type: "paste", handler: function(e) { return self._handlePaste(e);}}); //$NON-NLS-0$
			if (isPad) {
				handlers.push({target: document, type: "selectionchange", handler: function(e) { return self._handleSelectionChange(e); }}); //$NON-NLS-0$
				handlers.push({target: clientDiv, type: "touchstart", handler: function(e) { return self._handleTouchStart(e); }}); //$NON-NLS-0$
				handlers.push({target: clientDiv, type: "touchmove", handler: function(e) { return self._handleTouchMove(e); }}); //$NON-NLS-0$
				handlers.push({target: clientDiv, type: "touchend", handler: function(e) { return self._handleTouchEnd(e); }}); //$NON-NLS-0$
			} else {
				handlers.push({target: clientDiv, type: "mousedown", handler: function(e) { return self._handleMouseDown(e);}}); //$NON-NLS-0$
				handlers.push({target: clientDiv, type: "mouseover", handler: function(e) { return self._handleMouseOver(e);}}); //$NON-NLS-0$
				handlers.push({target: clientDiv, type: "mouseout", handler: function(e) { return self._handleMouseOut(e);}}); //$NON-NLS-0$
				handlers.push({target: grabNode, type: "mouseup", handler: function(e) { return self._handleMouseUp(e);}}); //$NON-NLS-0$
				handlers.push({target: grabNode, type: "mousemove", handler: function(e) { return self._handleMouseMove(e);}}); //$NON-NLS-0$
				handlers.push({target: rootDiv, type: "mousedown", handler: function(e) { return self._handleRootMouseDown(e);}}); //$NON-NLS-0$
				handlers.push({target: rootDiv, type: "mouseup", handler: function(e) { return self._handleRootMouseUp(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "dragstart", handler: function(e) { return self._handleDragStart(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "drag", handler: function(e) { return self._handleDrag(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "dragend", handler: function(e) { return self._handleDragEnd(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "dragenter", handler: function(e) { return self._handleDragEnter(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "dragover", handler: function(e) { return self._handleDragOver(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "dragleave", handler: function(e) { return self._handleDragLeave(e);}}); //$NON-NLS-0$
				handlers.push({target: topNode, type: "drop", handler: function(e) { return self._handleDrop(e);}}); //$NON-NLS-0$
				handlers.push({target: this._clientDiv, type: isFirefox ? "DOMMouseScroll" : "mousewheel", handler: function(e) { return self._handleMouseWheel(e); }}); //$NON-NLS-1$ //$NON-NLS-0$
				if (isFirefox && !isWindows) {
					var MutationObserver = window.MutationObserver || window.MozMutationObserver;
					if (MutationObserver) {
						this._mutationObserver = new MutationObserver(function(mutations) { self._handleDataModified(mutations); });
						this._mutationObserver.observe(clientDiv, {subtree: true, characterData: true});
					} else {
						handlers.push({target: this._clientDiv, type: "DOMCharacterDataModified", handler: function (e) { return self._handleDataModified(e); }}); //$NON-NLS-0$
					}
				}
				if (this._overlayDiv) {
					handlers.push({target: this._overlayDiv, type: "mousedown", handler: function(e) { return self._handleMouseDown(e);}}); //$NON-NLS-0$
					handlers.push({target: this._overlayDiv, type: "mouseover", handler: function(e) { return self._handleMouseOver(e);}}); //$NON-NLS-0$
					handlers.push({target: this._overlayDiv, type: "mouseout", handler: function(e) { return self._handleMouseOut(e);}}); //$NON-NLS-0$
					handlers.push({target: this._overlayDiv, type: "contextmenu", handler: function(e) { return self._handleContextMenu(e); }}); //$NON-NLS-0$
				}
				if (!isW3CEvents) {
					handlers.push({target: this._clientDiv, type: "dblclick", handler: function(e) { return self._handleDblclick(e); }}); //$NON-NLS-0$
				}
			}

			var leftDiv = this._leftDiv, rightDiv = this._rightDiv;
			if (isIE) {
				handlers.push({target: leftDiv, type: "selectstart", handler: function() {return false;}}); //$NON-NLS-0$
			}
			handlers.push({target: leftDiv, type: isFirefox ? "DOMMouseScroll" : "mousewheel", handler: function(e) { return self._handleMouseWheel(e); }}); //$NON-NLS-1$ //$NON-NLS-0$
			handlers.push({target: leftDiv, type: "click", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: leftDiv, type: "dblclick", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: leftDiv, type: "mousemove", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: leftDiv, type: "mouseover", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: leftDiv, type: "mouseout", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			if (isIE) {
				handlers.push({target: rightDiv, type: "selectstart", handler: function() {return false;}}); //$NON-NLS-0$
			}
			handlers.push({target: rightDiv, type: isFirefox ? "DOMMouseScroll" : "mousewheel", handler: function(e) { return self._handleMouseWheel(e); }}); //$NON-NLS-1$ //$NON-NLS-0$
			handlers.push({target: rightDiv, type: "click", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: rightDiv, type: "dblclick", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: rightDiv, type: "mousemove", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: rightDiv, type: "mouseover", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			handlers.push({target: rightDiv, type: "mouseout", handler: function(e) { self._handleRulerEvent(e); }}); //$NON-NLS-0$
			
			for (var i=0; i<handlers.length; i++) {
				var h = handlers[i];
				addHandler(h.target, h.type, h.handler, h.capture);
			}
		},
		_init: function(options) {
			var parent = options.parent;
			if (typeof(parent) === "string") { //$NON-NLS-0$
				parent = window.document.getElementById(parent);
			}
			if (!parent) { throw "no parent"; } //$NON-NLS-0$
			options.parent = parent;
			options.model = options.model || new mTextModel.TextModel();
			var defaultOptions = this._defaultOptions();
			for (var option in defaultOptions) {
				if (defaultOptions.hasOwnProperty(option)) {
					var value;
					if (options[option] !== undefined) {
						value = options[option];
					} else {
						value = defaultOptions[option].value;
					}
					this["_" + option] = value; //$NON-NLS-0$
				}
			}
			this._rulers = [];
			this._selection = new Selection (0, 0, false);
			this._linksVisible = false;
			this._redrawCount = 0;
			this._maxLineWidth = 0;
			this._maxLineIndex = -1;
			this._ignoreSelect = true;
			this._ignoreFocus = false;
			this._hasFocus = false;
			this._columnX = -1;
			this._dragOffset = -1;

			/* Auto scroll */
			this._autoScrollX = null;
			this._autoScrollY = null;
			this._autoScrollTimerID = null;
			this._AUTO_SCROLL_RATE = 50;
			this._grabControl = null;
			this._moseMoveClosure  = null;
			this._mouseUpClosure = null;
			
			/* Double click */
			this._lastMouseX = 0;
			this._lastMouseY = 0;
			this._lastMouseTime = 0;
			this._clickCount = 0;
			this._clickTime = 250;
			this._clickDist = 5;
			this._isMouseDown = false;
			this._doubleClickSelection = null;
			
			/* Scroll */
			this._hScroll = 0;
			this._vScroll = 0;

			/* IME */
			this._imeOffset = -1;
			
			/* Create elements */
			this._createActions();
			this._createView();
		},
		_isLinkURL: function(string) {
			return string.toLowerCase().lastIndexOf(".css") === string.length - 4; //$NON-NLS-0$
		},
		_modifyContent: function(e, updateCaret) {
			if (this._readonly && !e._code) {
				return;
			}
			e.type = "Verify"; //$NON-NLS-0$
			this.onVerify(e);

			if (e.text === null || e.text === undefined) { return; }
			
			var model = this._model;
			try {
				if (e._ignoreDOMSelection) { this._ignoreDOMSelection = true; }
				model.setText (e.text, e.start, e.end);
			} finally {
				if (e._ignoreDOMSelection) { this._ignoreDOMSelection = false; }
			}
			
			if (updateCaret) {
				var selection = this._getSelection ();
				selection.setCaret(e.start + e.text.length);
				this._setSelection(selection, true);
			}
			this.onModify({type: "Modify"}); //$NON-NLS-0$
		},
		_onModelChanged: function(modelChangedEvent) {
			modelChangedEvent.type = "ModelChanged"; //$NON-NLS-0$
			this.onModelChanged(modelChangedEvent);
			modelChangedEvent.type = "Changed"; //$NON-NLS-0$
			var start = modelChangedEvent.start;
			var addedCharCount = modelChangedEvent.addedCharCount;
			var removedCharCount = modelChangedEvent.removedCharCount;
			var addedLineCount = modelChangedEvent.addedLineCount;
			var removedLineCount = modelChangedEvent.removedLineCount;
			var selection = this._getSelection();
			if (selection.end > start) {
				if (selection.end > start && selection.start < start + removedCharCount) {
					// selection intersects replaced text. set caret behind text change
					selection.setCaret(start + addedCharCount);
				} else {
					// move selection to keep same text selected
					selection.start +=  addedCharCount - removedCharCount;
					selection.end +=  addedCharCount - removedCharCount;
				}
				this._setSelection(selection, false, false);
			}
			
			var model = this._model;
			var startLine = model.getLineAtOffset(start);
			var child = this._getLineNext();
			while (child) {
				var lineIndex = child.lineIndex;
				if (startLine <= lineIndex && lineIndex <= startLine + removedLineCount) {
					if (startLine === lineIndex && !child.modelChangedEvent && !child.lineRemoved) {
						child.modelChangedEvent = modelChangedEvent;
						child.lineChanged = true;
					} else {
						child.lineRemoved = true;
						child.lineChanged = false;
						child.modelChangedEvent = null;
					}
				}
				if (lineIndex > startLine + removedLineCount) {
					child.lineIndex = lineIndex + addedLineCount - removedLineCount;
				}
				child = this._getLineNext(child);
			}
			if (startLine <= this._maxLineIndex && this._maxLineIndex <= startLine + removedLineCount) {
				this._checkMaxLineIndex = this._maxLineIndex;
				this._maxLineIndex = -1;
				this._maxLineWidth = 0;
			}
			this._updatePage();
		},
		_onModelChanging: function(modelChangingEvent) {
			modelChangingEvent.type = "ModelChanging"; //$NON-NLS-0$
			this.onModelChanging(modelChangingEvent);
			modelChangingEvent.type = "Changing"; //$NON-NLS-0$
		},
		_queueUpdatePage: function() {
			if (this._updateTimer) { return; }
			var self = this;
			this._updateTimer = setTimeout(function() { 
				self._updateTimer = null;
				self._updatePage();
			}, 0);
		},
		_resetLineWidth: function() {
			var clientDiv = this._clientDiv;
			if (clientDiv) {
				var child = clientDiv.firstChild;
				while (child) {
					child.lineWidth = undefined;
					child = child.nextSibling;
				}
			}
		},
		_reset: function() {
			this._maxLineIndex = -1;
			this._maxLineWidth = 0;
			this._columnX = -1;
			this._topChild = null;
			this._bottomChild = null;
			this._partialY = 0;
			this._setSelection(new Selection (0, 0, false), false, false);
			if (this._viewDiv) {
				this._viewDiv.scrollLeft = 0;
				this._viewDiv.scrollTop = 0;
			}
			var clientDiv = this._clientDiv;
			if (clientDiv) {
				var child = clientDiv.firstChild;
				while (child) {
					child.lineRemoved = true;
					child = child.nextSibling;
				}
				/*
				* Bug in Firefox.  For some reason, the caret does not show after the
				* view is refreshed.  The fix is to toggle the contentEditable state and
				* force the clientDiv to loose and receive focus if it is focused.
				*/
				if (isFirefox) {
					this._ignoreFocus = false;
					var hasFocus = this._hasFocus;
					if (hasFocus) { clientDiv.blur(); }
					clientDiv.contentEditable = false;
					clientDiv.contentEditable = true;
					if (hasFocus) { clientDiv.focus(); }
					this._ignoreFocus = false;
				}
			}
		},
		_scrollView: function (pixelX, pixelY) {
			/*
			* Always set _ensureCaretVisible to false so that the view does not scroll
			* to show the caret when scrollView is not called from showCaret().
			*/
			this._ensureCaretVisible = false;
			
			/*
			* Scrolling is done only by setting the scrollLeft and scrollTop fields in the
			* view div. This causes an updatePage from the scroll event. In some browsers 
			* this event is asynchronous and forcing update page to run synchronously
			* leads to redraw problems. 
			* On Chrome 11, the view redrawing at times when holding PageDown/PageUp key.
			* On Firefox 4 for Linux, the view redraws the first page when holding 
			* PageDown/PageUp key, but it will not redraw again until the key is released.
			*/
			var viewDiv = this._viewDiv;
			if (pixelX) { viewDiv.scrollLeft += pixelX; }
			if (pixelY) { viewDiv.scrollTop += pixelY; }
		},
		_setClipboardText: function (text, event) {
			var clipboardText;
			if (window.clipboardData) {
				//IE
				clipboardText = [];
				this._convertDelimiter(text, function(t) {clipboardText.push(t);}, function() {clipboardText.push(platformDelimiter);});
				return window.clipboardData.setData("Text", clipboardText.join("")); //$NON-NLS-0$
			}
			if (event && event.clipboardData) {
				//webkit
				clipboardText = [];
				this._convertDelimiter(text, function(t) {clipboardText.push(t);}, function() {clipboardText.push(platformDelimiter);});
				if (event.clipboardData.setData("text/plain", clipboardText.join(""))) { //$NON-NLS-0$
					return true;
				}
			}
			var child = document.createElement("PRE"); //$NON-NLS-0$
			child.style.position = "fixed"; //$NON-NLS-0$
			child.style.left = "-1000px"; //$NON-NLS-0$
			this._convertDelimiter(text, 
				function(t) {
					child.appendChild(document.createTextNode(t));
				}, 
				function() {
					child.appendChild(document.createElement("BR")); //$NON-NLS-0$
				}
			);
			child.appendChild(document.createTextNode(" ")); //$NON-NLS-0$
			this._clientDiv.appendChild(child);
			var range = document.createRange();
			range.setStart(child.firstChild, 0);
			range.setEndBefore(child.lastChild);
			var sel = window.getSelection();
			if (sel.rangeCount > 0) { sel.removeAllRanges(); }
			sel.addRange(range);
			var self = this;
			/** @ignore */
			var cleanup = function() {
				if (child && child.parentNode === self._clientDiv) {
					self._clientDiv.removeChild(child);
				}
				self._updateDOMSelection();
			};
			var result = false;
			/* 
			* Try execCommand first, it works on firefox with clipboard permission,
			* chrome 5, safari 4.
			*/
			this._ignoreCopy = true;
			try {
				result = document.execCommand("copy", false, null); //$NON-NLS-0$
			} catch (e) {}
			this._ignoreCopy = false;
			if (!result) {
				if (event) {
					setTimeout(cleanup, 0);
					return false;
				}
			}
			/* no event and no permission, copy can not be done */
			cleanup();
			return true;
		},
		_setDOMSelection: function (startNode, startOffset, endNode, endOffset) {
			var startLineNode, startLineOffset, endLineNode, endLineOffset;
			var offset = 0;
			var lineChild = startNode.firstChild;
			var node, nodeLength, model = this._model;
			var startLineEnd = model.getLine(startNode.lineIndex).length;
			while (lineChild) {
				node = lineChild.firstChild;
				nodeLength = node.length;
				if (lineChild.ignoreChars) {
					nodeLength -= lineChild.ignoreChars;
				}
				if (offset + nodeLength > startOffset || offset + nodeLength >= startLineEnd) {
					startLineNode = node;
					startLineOffset = startOffset - offset;
					if (lineChild.ignoreChars && nodeLength > 0 && startLineOffset === nodeLength) {
						startLineOffset += lineChild.ignoreChars; 
					}
					break;
				}
				offset += nodeLength;
				lineChild = lineChild.nextSibling;
			}
			offset = 0;
			lineChild = endNode.firstChild;
			var endLineEnd = this._model.getLine(endNode.lineIndex).length;
			while (lineChild) {
				node = lineChild.firstChild;
				nodeLength = node.length;
				if (lineChild.ignoreChars) {
					nodeLength -= lineChild.ignoreChars;
				}
				if (nodeLength + offset > endOffset || offset + nodeLength >= endLineEnd) {
					endLineNode = node;
					endLineOffset = endOffset - offset;
					if (lineChild.ignoreChars && nodeLength > 0 && endLineOffset === nodeLength) {
						endLineOffset += lineChild.ignoreChars; 
					}
					break;
				}
				offset += nodeLength;
				lineChild = lineChild.nextSibling;
			}
			
			this._setDOMFullSelection(startNode, startOffset, startLineEnd, endNode, endOffset, endLineEnd);

			if (!this._hasFocus) { return; }
			var range;
			if (window.getSelection) {
				//W3C
				range = document.createRange();
				range.setStart(startLineNode, startLineOffset);
				range.setEnd(endLineNode, endLineOffset);
				var sel = window.getSelection();
				this._ignoreSelect = false;
				if (sel.rangeCount > 0) { sel.removeAllRanges(); }
				sel.addRange(range);
				this._ignoreSelect = true;
			} else if (document.selection) {
				//IE < 9
				var body = document.body;

				/*
				* Bug in IE. For some reason when text is deselected the overflow
				* selection at the end of some lines does not get redrawn.  The
				* fix is to create a DOM element in the body to force a redraw.
				*/
				var child = document.createElement("DIV"); //$NON-NLS-0$
				body.appendChild(child);
				body.removeChild(child);
				
				range = body.createTextRange();
				range.moveToElementText(startLineNode.parentNode);
				range.moveStart("character", startLineOffset); //$NON-NLS-0$
				var endRange = body.createTextRange();
				endRange.moveToElementText(endLineNode.parentNode);
				endRange.moveStart("character", endLineOffset); //$NON-NLS-0$
				range.setEndPoint("EndToStart", endRange); //$NON-NLS-0$
				this._ignoreSelect = false;
				range.select();
				this._ignoreSelect = true;
			}
		},
		_setDOMFullSelection: function(startNode, startOffset, startLineEnd, endNode, endOffset, endLineEnd) {
			if (!this._selDiv1) { return; }
			var selDiv = this._selDiv1;
			selDiv.style.width = "0px"; //$NON-NLS-0$
			selDiv.style.height = "0px"; //$NON-NLS-0$
			selDiv = this._selDiv2;
			selDiv.style.width = "0px"; //$NON-NLS-0$
			selDiv.style.height = "0px"; //$NON-NLS-0$
			selDiv = this._selDiv3;
			selDiv.style.width = "0px"; //$NON-NLS-0$
			selDiv.style.height = "0px"; //$NON-NLS-0$
			if (startNode === endNode && startOffset === endOffset) { return; }
			var model = this._model;
			var viewPad = this._getViewPadding();
			var clientRect = this._clientDiv.getBoundingClientRect();
			var viewRect = this._viewDiv.getBoundingClientRect();
			var left = viewRect.left + viewPad.left;
			var right = clientRect.right;
			var top = viewRect.top + viewPad.top;
			var bottom = clientRect.bottom;
			var hd = 0, vd = 0;
			if (this._clipDiv) {
				var clipRect = this._clipDiv.getBoundingClientRect();
				hd = clipRect.left - this._clipDiv.scrollLeft;
				vd = clipRect.top;
			} else {
				var rootpRect = this._rootDiv.getBoundingClientRect();
				hd = rootpRect.left;
				vd = rootpRect.top;
			}
			var startLineBounds, l;
			startLineBounds = this._getLineBoundingClientRect(startNode);
			if (startOffset === 0) {
				l = startLineBounds.left;
			} else {
				if (startOffset >= startLineEnd) {
					l = startLineBounds.right;
				} else {
					this._ignoreDOMSelection = true;
					l = this._getBoundsAtOffset(model.getLineStart(startNode.lineIndex) + startOffset).left;
					this._ignoreDOMSelection = false;
				}
			}
			var r;
			var endLineBounds = this._getLineBoundingClientRect(endNode);
			if (endOffset === 0) {
				r = endLineBounds.left;
			} else {
				if (endOffset >= endLineEnd) {
					r = endLineBounds.right;
				} else {
					this._ignoreDOMSelection = true;
					r = this._getBoundsAtOffset(model.getLineStart(endNode.lineIndex) + endOffset).left;
					this._ignoreDOMSelection = false;
				}
			}
			var sel1Div = this._selDiv1;
			var sel1Left = Math.min(right, Math.max(left, l));
			var sel1Top = Math.min(bottom, Math.max(top, startLineBounds.top));
			var sel1Right = right;
			var sel1Bottom = Math.min(bottom, Math.max(top, startLineBounds.bottom));
			sel1Div.style.left = (sel1Left - hd) + "px"; //$NON-NLS-0$
			sel1Div.style.top = (sel1Top - vd) + "px"; //$NON-NLS-0$
			sel1Div.style.width = Math.max(0, sel1Right - sel1Left) + "px"; //$NON-NLS-0$
			sel1Div.style.height = Math.max(0, sel1Bottom - sel1Top) + "px"; //$NON-NLS-0$
			if (startNode === endNode) {
				sel1Right = Math.min(r, right);
				sel1Div.style.width = Math.max(0, sel1Right - sel1Left) + "px"; //$NON-NLS-0$
			} else {
				var sel3Left = left;
				var sel3Top = Math.min(bottom, Math.max(top, endLineBounds.top));
				var sel3Right = Math.min(right, Math.max(left, r));
				var sel3Bottom = Math.min(bottom, Math.max(top, endLineBounds.bottom));
				var sel3Div = this._selDiv3;
				sel3Div.style.left = (sel3Left - hd) + "px"; //$NON-NLS-0$
				sel3Div.style.top = (sel3Top - vd) + "px"; //$NON-NLS-0$
				sel3Div.style.width = Math.max(0, sel3Right - sel3Left) + "px"; //$NON-NLS-0$
				sel3Div.style.height = Math.max(0, sel3Bottom - sel3Top) + "px"; //$NON-NLS-0$
				if (sel3Top - sel1Bottom > 0) {
					var sel2Div = this._selDiv2;
					sel2Div.style.left = (left - hd)  + "px"; //$NON-NLS-0$
					sel2Div.style.top = (sel1Bottom - vd) + "px"; //$NON-NLS-0$
					sel2Div.style.width = Math.max(0, right - left) + "px"; //$NON-NLS-0$
					sel2Div.style.height = Math.max(0, sel3Top - sel1Bottom) + "px"; //$NON-NLS-0$
				}
			}
		},
		_setGrab: function (target) {
			if (target === this._grabControl) { return; }
			if (target) {
				if (target.setCapture) { target.setCapture(); }
				this._grabControl = target;
			} else {
				if (this._grabControl.releaseCapture) { this._grabControl.releaseCapture(); }
				this._grabControl = null;
			}
		},
		_setLinksVisible: function(visible) {
			if (this._linksVisible === visible) { return; }
			this._linksVisible = visible;
			/*
			* Feature in IE.  The client div looses focus and does not regain it back
			* when the content editable flag is reset. The fix is to remember that it
			* had focus when the flag is cleared and give focus back to the div when
			* the flag is set.
			*/
			if (isIE && visible) {
				this._hadFocus = this._hasFocus;
			}
			var clientDiv = this._clientDiv;
			clientDiv.contentEditable = !visible;
			if (this._hadFocus && !visible) {
				clientDiv.focus();
			}
			if (this._overlayDiv) {
				this._overlayDiv.style.zIndex = visible ? "-1" : "1"; //$NON-NLS-1$ //$NON-NLS-0$
			}
			var line = this._getLineNext();
			while (line) {
				if (line.hasLink) {
					var lineChild = line.firstChild;
					while (lineChild) {
						var next = lineChild.nextSibling;
						var style = lineChild.viewStyle;
						if (style && style.tagName === "A") { //$NON-NLS-0$
							line.replaceChild(this._createSpan(line, document, lineChild.firstChild.data, style), lineChild);
						}
						lineChild = next;
					}
				}
				line = this._getLineNext(line);
			}
		},
		_setSelection: function (selection, scroll, update, pageScroll) {
			if (selection) {
				this._columnX = -1;
				if (update === undefined) { update = true; }
				var oldSelection = this._selection; 
				if (!oldSelection.equals(selection)) {
					this._selection = selection;
					var e = {
						type: "Selection", //$NON-NLS-0$
						oldValue: {start:oldSelection.start, end:oldSelection.end},
						newValue: {start:selection.start, end:selection.end}
					};
					this.onSelection(e);
				}
				/* 
				* Always showCaret(), even when the selection is not changing, to ensure the
				* caret is visible. Note that some views do not scroll to show the caret during
				* keyboard navigation when the selection does not chanage. For example, line down
				* when the caret is already at the last line.
				*/
				if (scroll) { update = !this._showCaret(false, pageScroll); }
				
				/* 
				* Sometimes the browser changes the selection 
				* as result of method calls or "leaked" events. 
				* The fix is to set the visual selection even
				* when the logical selection is not changed.
				*/
				if (update) { this._updateDOMSelection(); }
			}
		},
		_setSelectionTo: function (x, y, extent, drag) {
			var model = this._model, offset;
			var selection = this._getSelection();
			var lineIndex = this._getYToLine(y);
			if (this._clickCount === 1) {
				offset = this._getXToOffset(lineIndex, x);
				if (drag && !extent) {
					if (selection.start <= offset && offset < selection.end) {
						this._dragOffset = offset;
						return false;
					}
				}
				selection.extend(offset);
				if (!extent) { selection.collapse(); }
			} else {
				var word = (this._clickCount & 1) === 0;
				var start, end;
				if (word) {
					offset = this._getXToOffset(lineIndex, x);
					if (this._doubleClickSelection) {
						if (offset >= this._doubleClickSelection.start) {
							start = this._doubleClickSelection.start;
							end = this._getOffset(offset, "wordend", +1); //$NON-NLS-0$
						} else {
							start = this._getOffset(offset, "word", -1); //$NON-NLS-0$
							end = this._doubleClickSelection.end;
						}
					} else {
						start = this._getOffset(offset, "word", -1); //$NON-NLS-0$
						end = this._getOffset(start, "wordend", +1); //$NON-NLS-0$
					}
				} else {
					if (this._doubleClickSelection) {
						var doubleClickLine = model.getLineAtOffset(this._doubleClickSelection.start);
						if (lineIndex >= doubleClickLine) {
							start = model.getLineStart(doubleClickLine);
							end = model.getLineEnd(lineIndex);
						} else {
							start = model.getLineStart(lineIndex);
							end = model.getLineEnd(doubleClickLine);
						}
					} else {
						start = model.getLineStart(lineIndex);
						end = model.getLineEnd(lineIndex);
					}
				}
				selection.setCaret(start);
				selection.extend(end);
			} 
			this._setSelection(selection, true, true);
			return true;
		},
		_setFullSelection: function(fullSelection, init) {
			this._fullSelection = fullSelection;
			if (isWebkit) {
				this._fullSelection = true;
			}
			var parent = this._clipDiv || this._rootDiv;
			if (!parent) {
				return;
			}
			if (!this._fullSelection) {
				if (this._selDiv1) {
					parent.removeChild(this._selDiv1);
					this._selDiv1 = null;
				}
				if (this._selDiv2) {
					parent.removeChild(this._selDiv2);
					this._selDiv2 = null;
				}
				if (this._selDiv3) {
					parent.removeChild(this._selDiv3);
					this._selDiv3 = null;
				}
				return;
			}
			
			if (!this._selDiv1 && (this._fullSelection && !isPad)) {
				this._highlightRGB = isWebkit ? "transparent" : "Highlight"; //$NON-NLS-1$ //$NON-NLS-0$
				var selDiv1 = document.createElement("DIV"); //$NON-NLS-0$
				this._selDiv1 = selDiv1;
				selDiv1.style.position = "absolute"; //$NON-NLS-0$
				selDiv1.style.borderWidth = "0px"; //$NON-NLS-0$
				selDiv1.style.margin = "0px"; //$NON-NLS-0$
				selDiv1.style.padding = "0px"; //$NON-NLS-0$
				selDiv1.style.outline = "none"; //$NON-NLS-0$
				selDiv1.style.background = this._highlightRGB;
				selDiv1.style.width = "0px"; //$NON-NLS-0$
				selDiv1.style.height = "0px"; //$NON-NLS-0$
				selDiv1.style.zIndex = "0"; //$NON-NLS-0$
				parent.appendChild(selDiv1);
				var selDiv2 = document.createElement("DIV"); //$NON-NLS-0$
				this._selDiv2 = selDiv2;
				selDiv2.style.position = "absolute"; //$NON-NLS-0$
				selDiv2.style.borderWidth = "0px"; //$NON-NLS-0$
				selDiv2.style.margin = "0px"; //$NON-NLS-0$
				selDiv2.style.padding = "0px"; //$NON-NLS-0$
				selDiv2.style.outline = "none"; //$NON-NLS-0$
				selDiv2.style.background = this._highlightRGB;
				selDiv2.style.width = "0px"; //$NON-NLS-0$
				selDiv2.style.height = "0px"; //$NON-NLS-0$
				selDiv2.style.zIndex = "0"; //$NON-NLS-0$
				parent.appendChild(selDiv2);
				var selDiv3 = document.createElement("DIV"); //$NON-NLS-0$
				this._selDiv3 = selDiv3;
				selDiv3.style.position = "absolute"; //$NON-NLS-0$
				selDiv3.style.borderWidth = "0px"; //$NON-NLS-0$
				selDiv3.style.margin = "0px"; //$NON-NLS-0$
				selDiv3.style.padding = "0px"; //$NON-NLS-0$
				selDiv3.style.outline = "none"; //$NON-NLS-0$
				selDiv3.style.background = this._highlightRGB;
				selDiv3.style.width = "0px"; //$NON-NLS-0$
				selDiv3.style.height = "0px"; //$NON-NLS-0$
				selDiv3.style.zIndex = "0"; //$NON-NLS-0$
				parent.appendChild(selDiv3);
				
				/*
				* Bug in Firefox. The Highlight color is mapped to list selection
				* background instead of the text selection background.  The fix
				* is to map known colors using a table or fallback to light blue.
				*/
				if (isFirefox && isMac) {
					var style = window.getComputedStyle(selDiv3, null);
					var rgb = style.getPropertyValue("background-color"); //$NON-NLS-0$
					switch (rgb) {
						case "rgb(119, 141, 168)": rgb = "rgb(199, 208, 218)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						case "rgb(127, 127, 127)": rgb = "rgb(198, 198, 198)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						case "rgb(255, 193, 31)": rgb = "rgb(250, 236, 115)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						case "rgb(243, 70, 72)": rgb = "rgb(255, 176, 139)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						case "rgb(255, 138, 34)": rgb = "rgb(255, 209, 129)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						case "rgb(102, 197, 71)": rgb = "rgb(194, 249, 144)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						case "rgb(140, 78, 184)": rgb = "rgb(232, 184, 255)"; break; //$NON-NLS-1$ //$NON-NLS-0$
						default: rgb = "rgb(180, 213, 255)"; break; //$NON-NLS-0$
					}
					this._highlightRGB = rgb;
					selDiv1.style.background = rgb;
					selDiv2.style.background = rgb;
					selDiv3.style.background = rgb;
				}
				if (!init) {
					this._updateDOMSelection();
				}
			}
		},
		_setReadOnly: function (readOnly) {
			this._readonly = readOnly;
			this._clientDiv.setAttribute("aria-readonly", readOnly ? "true" : "false"); //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
		},
		_setTabSize: function (tabSize, init) {
			this._tabSize = tabSize;
			this._customTabSize = undefined;
			var clientDiv = this._clientDiv;
			if (isOpera) {
				if (clientDiv) { clientDiv.style.OTabSize = this._tabSize+""; }
			} else if (isFirefox >= 4) {
				if (clientDiv) {  clientDiv.style.MozTabSize = this._tabSize+""; }
			} else if (this._tabSize !== 8) {
				this._customTabSize = this._tabSize;
			}
			if (!init) {
				this.redrawLines();
				this._resetLineWidth();
			}
		},
		_setThemeClass: function (themeClass, init) {
			this._themeClass = themeClass;
			var viewContainerClass = "textviewContainer"; //$NON-NLS-0$
			if (this._themeClass) { viewContainerClass += " " + this._themeClass; } //$NON-NLS-0$
			this._rootDiv.className = viewContainerClass;
			this._updateStyle(init);
		},
		_showCaret: function (allSelection, pageScroll) {
			if (!this._clientDiv) { return; }
			var model = this._model;
			var selection = this._getSelection();
			var scroll = this._getScroll();
			var caret = selection.getCaret();
			var start = selection.start;
			var end = selection.end;
			var startLine = model.getLineAtOffset(start); 
			var endLine = model.getLineAtOffset(end);
			var endInclusive = Math.max(Math.max(start, model.getLineStart(endLine)), end - 1);
			var viewPad = this._getViewPadding();
			
			var clientWidth = this._getClientWidth();
			var leftEdge = viewPad.left;
			var rightEdge = viewPad.left + clientWidth;
			var bounds = this._getBoundsAtOffset(caret === start ? start : endInclusive);
			var left = bounds.left;
			var right = bounds.right;
			var minScroll = clientWidth / 4;
			if (allSelection && !selection.isEmpty() && startLine === endLine) {
				bounds = this._getBoundsAtOffset(caret === end ? start : endInclusive);
				var selectionWidth = caret === start ? bounds.right - left : right - bounds.left;
				if ((clientWidth - minScroll) > selectionWidth) {
					if (left > bounds.left) { left = bounds.left; }
					if (right < bounds.right) { right = bounds.right; }
				}
			}
			var viewRect = this._viewDiv.getBoundingClientRect(); 
			left -= viewRect.left;
			right -= viewRect.left;
			var pixelX = 0;
			if (left < leftEdge) {
				pixelX = Math.min(left - leftEdge, -minScroll);
			}
			if (right > rightEdge) {
				var maxScroll = this._scrollDiv.scrollWidth - scroll.x - clientWidth;
				pixelX = Math.min(maxScroll,  Math.max(right - rightEdge, minScroll));
			}

			var pixelY = 0;
			var topIndex = this._getTopIndex(true);
			var bottomIndex = this._getBottomIndex(true);
			var caretLine = model.getLineAtOffset(caret);
			var clientHeight = this._getClientHeight();
			if (!(topIndex <= caretLine && caretLine <= bottomIndex)) {
				var lineHeight = this._getLineHeight();
				var selectionHeight = allSelection ? (endLine - startLine) * lineHeight : 0;
				pixelY = caretLine * lineHeight;
				pixelY -= scroll.y;
				if (pixelY + lineHeight > clientHeight) {
					pixelY -= clientHeight - lineHeight;
					if (caret === start && start !== end) {
						pixelY += Math.min(clientHeight - lineHeight, selectionHeight);
					}
				} else {
					if (caret === end) {
						pixelY -= Math.min (clientHeight - lineHeight, selectionHeight);
					}
				}
				if (pageScroll) {
					if (pageScroll > 0) {
						if (pixelY > 0) {
							pixelY = Math.max(pixelY, pageScroll);
						}
					} else {
						if (pixelY < 0) {
							pixelY = Math.min(pixelY, pageScroll);
						}
					}
				}
			}

			if (pixelX !== 0 || pixelY !== 0) {
				this._scrollView (pixelX, pixelY);
				/*
				* When the view scrolls it is possible that one of the scrollbars can show over the caret.
				* Depending on the browser scrolling can be synchronous (Safari), in which case the change 
				* can be detected before showCaret() returns. When scrolling is asynchronous (most browsers), 
				* the detection is done during the next update page.
				*/
				if (clientHeight !== this._getClientHeight() || clientWidth !== this._getClientWidth()) {
					this._showCaret();
				} else {
					this._ensureCaretVisible = true;
				}
				return true;
			}
			return false;
		},
		_startIME: function () {
			if (this._imeOffset !== -1) { return; }
			var selection = this._getSelection();
			if (!selection.isEmpty()) {
				this._modifyContent({text: "", start: selection.start, end: selection.end}, true);
			}
			this._imeOffset = selection.start;
		},
		_unhookEvents: function() {
			this._model.removeEventListener("Changing", this._modelListener.onChanging); //$NON-NLS-0$
			this._model.removeEventListener("Changed", this._modelListener.onChanged); //$NON-NLS-0$
			this._modelListener = null;
			for (var i=0; i<this._handlers.length; i++) {
				var h = this._handlers[i];
				removeHandler(h.target, h.type, h.handler);
			}
			this._handlers = null;
			if (this._mutationObserver) {
				this._mutationObserver.disconnect();
				this._mutationObserver = null;
			}
		},
		_updateDOMSelection: function () {
			if (this._ignoreDOMSelection) { return; }
			if (!this._clientDiv) { return; }
			var selection = this._getSelection();
			var model = this._model;
			var startLine = model.getLineAtOffset(selection.start);
			var endLine = model.getLineAtOffset(selection.end);
			var firstNode = this._getLineNext();
			/*
			* Bug in Firefox. For some reason, after a update page sometimes the 
			* firstChild returns null incorrectly. The fix is to ignore show selection.
			*/
			if (!firstNode) { return; }
			var lastNode = this._getLinePrevious();
			
			var topNode, bottomNode, topOffset, bottomOffset;
			if (startLine < firstNode.lineIndex) {
				topNode = firstNode;
				topOffset = 0;
			} else if (startLine > lastNode.lineIndex) {
				topNode = lastNode;
				topOffset = 0;
			} else {
				topNode = this._getLineNode(startLine);
				topOffset = selection.start - model.getLineStart(startLine);
			}

			if (endLine < firstNode.lineIndex) {
				bottomNode = firstNode;
				bottomOffset = 0;
			} else if (endLine > lastNode.lineIndex) {
				bottomNode = lastNode;
				bottomOffset = 0;
			} else {
				bottomNode = this._getLineNode(endLine);
				bottomOffset = selection.end - model.getLineStart(endLine);
			}
			this._setDOMSelection(topNode, topOffset, bottomNode, bottomOffset);
		},
		_updatePage: function(hScrollOnly) {
			if (this._redrawCount > 0) { return; }
			if (this._updateTimer) {
				clearTimeout(this._updateTimer);
				this._updateTimer = null;
				hScrollOnly = false;
			}
			var clientDiv = this._clientDiv;
			if (!clientDiv) { return; }
			var model = this._model;
			var scroll = this._getScroll();
			var viewPad = this._getViewPadding();
			var lineCount = model.getLineCount();
			var lineHeight = this._getLineHeight();
			var firstLine = Math.max(0, scroll.y) / lineHeight;
			var topIndex = Math.floor(firstLine);
			var lineStart = Math.max(0, topIndex - 1);
			var top = Math.round((firstLine - lineStart) * lineHeight);
			var partialY = this._partialY = Math.round((firstLine - topIndex) * lineHeight);
			var scrollWidth, scrollHeight = lineCount * lineHeight;
			var leftWidth, leftRect, clientWidth, clientHeight;
			if (hScrollOnly) {
				clientWidth = this._getClientWidth();
				clientHeight = this._getClientHeight();
				leftWidth = 0;
				if (this._leftDiv) {
					leftRect = this._leftDiv.getBoundingClientRect();
					leftWidth = leftRect.right - leftRect.left;
				}
				scrollWidth = Math.max(this._maxLineWidth, clientWidth);
			} else {
				var parent = this._parent;
				var rootDiv = this._rootDiv;
				var parentWidth = parent.clientWidth;
				var parentHeight = parent.clientHeight;

				/* Update view height in order to have client height computed */
				var viewDiv = this._viewDiv;
				viewDiv.style.height = Math.max(0, (parentHeight - viewPad.top - viewPad.bottom)) + "px"; //$NON-NLS-0$
				clientHeight = this._getClientHeight();
				var linesPerPage = Math.floor((clientHeight + partialY) / lineHeight);
				var bottomIndex = Math.min(topIndex + linesPerPage, lineCount - 1);
				var lineEnd = Math.min(bottomIndex + 1, lineCount - 1);
				
				var lineIndex, lineWidth;
				var child = clientDiv.firstChild;
				while (child) {
					lineIndex = child.lineIndex;
					var nextChild = child.nextSibling;
					if (!(lineStart <= lineIndex && lineIndex <= lineEnd) || child.lineRemoved || child.lineIndex === -1) {
						if (this._mouseWheelLine === child) {
							child.style.display = "none"; //$NON-NLS-0$
							child.lineIndex = -1;
						} else {
							clientDiv.removeChild(child);
						}
					}
					child = nextChild;
				}
	
				child = this._getLineNext();
				var frag = document.createDocumentFragment();
				for (lineIndex=lineStart; lineIndex<=lineEnd; lineIndex++) {
					if (!child || child.lineIndex > lineIndex) {
						this._createLine(frag, null, document, lineIndex, model);
					} else {
						if (frag.firstChild) {
							clientDiv.insertBefore(frag, child);
							frag = document.createDocumentFragment();
						}
						if (child && child.lineChanged) {
							child = this._createLine(frag, child, document, lineIndex, model);
							child.lineChanged = false;
						}
						child = this._getLineNext(child);
					}
				}
				if (frag.firstChild) { clientDiv.insertBefore(frag, child); }
	
				/*
				* Feature in WekKit. Webkit limits the width of the lines
				* computed below to the width of the client div.  This causes
				* the lines to be wrapped even though "pre" is set.  The fix
				* is to set the width of the client div to "auto"
				* before computing the lines width.  Note that this value is
				* reset to the appropriate value further down.
				*/ 
				if (isWebkit) {
					clientDiv.style.width = "auto"; //$NON-NLS-0$
				}
	
				var rect;
				child = this._getLineNext();
				while (child) {
					lineWidth = child.lineWidth;
					if (lineWidth === undefined) {
						rect = this._getLineBoundingClientRect(child);
						lineWidth = child.lineWidth = Math.ceil(rect.right - rect.left);
					}
					if (lineWidth >= this._maxLineWidth) {
						this._maxLineWidth = lineWidth;
						this._maxLineIndex = child.lineIndex;
					}
					if (child.lineIndex === topIndex) { this._topChild = child; }
					if (child.lineIndex === bottomIndex) { this._bottomChild = child; }
					if (this._checkMaxLineIndex === child.lineIndex) { this._checkMaxLineIndex = -1; }
					child = this._getLineNext(child);
				}
				if (this._checkMaxLineIndex !== -1) {
					lineIndex = this._checkMaxLineIndex;
					this._checkMaxLineIndex = -1;
					if (0 <= lineIndex && lineIndex < lineCount) {
						var dummy = this._createLine(clientDiv, null, document, lineIndex, model);
						rect = this._getLineBoundingClientRect(dummy);
						lineWidth = rect.right - rect.left;
						if (lineWidth >= this._maxLineWidth) {
							this._maxLineWidth = lineWidth;
							this._maxLineIndex = lineIndex;
						}
						clientDiv.removeChild(dummy);
					}
				}
	
				// Update rulers
				this._updateRuler(this._leftDiv, topIndex, bottomIndex);
				this._updateRuler(this._rightDiv, topIndex, bottomIndex);
				
				leftWidth = 0;
				if (this._leftDiv) {
					leftRect = this._leftDiv.getBoundingClientRect();
					leftWidth = leftRect.right - leftRect.left;
				}
				var rightWidth = 0;
				if (this._rightDiv) {
					var rightRect = this._rightDiv.getBoundingClientRect();
					rightWidth = rightRect.right - rightRect.left;
				}
				viewDiv.style.left = leftWidth + "px"; //$NON-NLS-0$
				viewDiv.style.width = Math.max(0, parentWidth - leftWidth - rightWidth - viewPad.left - viewPad.right) + "px"; //$NON-NLS-0$
				if (this._rightDiv) {
					this._rightDiv.style.left = (parentWidth - rightWidth) + "px"; //$NON-NLS-0$
				}
				/* Need to set the height first in order for the width to consider the vertical scrollbar */
				var scrollDiv = this._scrollDiv;
				scrollDiv.style.height = scrollHeight + "px"; //$NON-NLS-0$
				/*
				* TODO if frameHeightWithoutHScrollbar < scrollHeight  < frameHeightWithHScrollbar and the horizontal bar is visible, 
				* then the clientWidth is wrong because the vertical scrollbar is showing. To correct code should hide both scrollbars 
				* at this point.
				*/
				clientWidth = this._getClientWidth();
				var width = Math.max(this._maxLineWidth, clientWidth);
				/*
				* Except by IE 8 and earlier, all other browsers are not allocating enough space for the right padding 
				* in the scrollbar. It is possible this a bug since all other paddings are considered.
				*/
				scrollWidth = width;
				if (!isIE || isIE >= 9) { width += viewPad.right; }
				scrollDiv.style.width = width + "px"; //$NON-NLS-0$
				if (this._clipScrollDiv) {
					this._clipScrollDiv.style.width = width + "px"; //$NON-NLS-0$
				}
				/* Get the left scroll after setting the width of the scrollDiv as this can change the horizontal scroll offset. */
				scroll = this._getScroll();
				var rulerHeight = clientHeight + viewPad.top + viewPad.bottom;
				this._updateRulerSize(this._leftDiv, rulerHeight);
				this._updateRulerSize(this._rightDiv, rulerHeight);
			}
			if (this._vScrollDiv) {
				var trackHeight = clientHeight - 8;
				var thumbHeight = Math.max(15, Math.ceil(Math.min(1, trackHeight / (scrollHeight + viewPad.top + viewPad.bottom)) * trackHeight));
				this._vScrollDiv.style.left = (leftWidth + clientWidth - 8) + "px"; //$NON-NLS-0$
				this._vScrollDiv.style.top = Math.floor(Math.max(0, (scroll.y * trackHeight / scrollHeight))) + "px"; //$NON-NLS-0$
				this._vScrollDiv.style.height = thumbHeight + "px"; //$NON-NLS-0$
			}
			if (this._hScrollDiv) {
				var trackWidth = clientWidth - 8;
				var thumbWidth = Math.max(15, Math.ceil(Math.min(1, trackWidth / (this._maxLineWidth + viewPad.left + viewPad.right)) * trackWidth));
				this._hScrollDiv.style.left = leftWidth + Math.floor(Math.max(0, Math.floor(scroll.x * trackWidth / this._maxLineWidth))) + "px"; //$NON-NLS-0$
				this._hScrollDiv.style.top = (clientHeight - 9) + "px"; //$NON-NLS-0$
				this._hScrollDiv.style.width = thumbWidth + "px"; //$NON-NLS-0$
			}
			var left = scroll.x;	
			var clipDiv = this._clipDiv;
			var overlayDiv = this._overlayDiv;
			var clipLeft, clipTop;
			if (clipDiv) {
				clipDiv.scrollLeft = left;			
				clipLeft = leftWidth + viewPad.left;
				clipTop = viewPad.top;
				var clipWidth = clientWidth;
				var clipHeight = clientHeight;
				var clientLeft = 0, clientTop = -top;
				if (scroll.x === 0) {
					clipLeft -= viewPad.left;
					clipWidth += viewPad.left;
					clientLeft = viewPad.left;
				} 
				if (scroll.x + clientWidth === scrollWidth) {
					clipWidth += viewPad.right;
				}
				if (scroll.y === 0) {
					clipTop -= viewPad.top;
					clipHeight += viewPad.top;
					clientTop += viewPad.top;
				}
				if (scroll.y + clientHeight === scrollHeight) { 
					clipHeight += viewPad.bottom; 
				}
				clipDiv.style.left = clipLeft + "px"; //$NON-NLS-0$
				clipDiv.style.top = clipTop + "px"; //$NON-NLS-0$
				clipDiv.style.width = clipWidth + "px"; //$NON-NLS-0$
				clipDiv.style.height = clipHeight + "px"; //$NON-NLS-0$
				clientDiv.style.left = clientLeft + "px"; //$NON-NLS-0$
				clientDiv.style.top = clientTop + "px"; //$NON-NLS-0$
				clientDiv.style.width = scrollWidth + "px"; //$NON-NLS-0$
				clientDiv.style.height = (clientHeight + top) + "px"; //$NON-NLS-0$
				if (overlayDiv) {
					overlayDiv.style.left = clientDiv.style.left;
					overlayDiv.style.top = clientDiv.style.top;
					overlayDiv.style.width = clientDiv.style.width;
					overlayDiv.style.height = clientDiv.style.height;
				}
			} else {
				clipLeft = left;
				clipTop = top;
				var clipRight = left + clientWidth;
				var clipBottom = top + clientHeight;
				if (clipLeft === 0) { clipLeft -= viewPad.left; }
				if (clipTop === 0) { clipTop -= viewPad.top; }
				if (clipRight === scrollWidth) { clipRight += viewPad.right; }
				if (scroll.y + clientHeight === scrollHeight) { clipBottom += viewPad.bottom; }
				clientDiv.style.clip = "rect(" + clipTop + "px," + clipRight + "px," + clipBottom + "px," + clipLeft + "px)"; //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
				clientDiv.style.left = (-left + leftWidth + viewPad.left) + "px"; //$NON-NLS-0$
				clientDiv.style.width = (isWebkit ? scrollWidth : clientWidth + left) + "px"; //$NON-NLS-0$
				if (!hScrollOnly) {
					clientDiv.style.top = (-top + viewPad.top) + "px"; //$NON-NLS-0$
					clientDiv.style.height = (clientHeight + top) + "px"; //$NON-NLS-0$
				}
				if (overlayDiv) {
					overlayDiv.style.clip = clientDiv.style.clip;
					overlayDiv.style.left = clientDiv.style.left;
					overlayDiv.style.width = clientDiv.style.width;
					if (!hScrollOnly) {
						overlayDiv.style.top = clientDiv.style.top;
						overlayDiv.style.height = clientDiv.style.height;
					}
				}
			}
			this._updateDOMSelection();

			/*
			* If the client height changed during the update page it means that scrollbar has either been shown or hidden.
			* When this happens update page has to run again to ensure that the top and bottom lines div are correct.
			* 
			* Note: On IE, updateDOMSelection() has to be called before getting the new client height because it
			* forces the client area to be recomputed.
			*/
			var ensureCaretVisible = this._ensureCaretVisible;
			this._ensureCaretVisible = false;
			if (clientHeight !== this._getClientHeight()) {
				this._updatePage();
				if (ensureCaretVisible) {
					this._showCaret();
				}
			}
		},
		_updateRulerSize: function (divRuler, rulerHeight) {
			if (!divRuler) { return; }
			var partialY = this._partialY;
			var lineHeight = this._getLineHeight();
			var cells = divRuler.firstChild.rows[0].cells;
			for (var i = 0; i < cells.length; i++) {
				var div = cells[i].firstChild;
				var offset = lineHeight;
				if (div._ruler.getOverview() === "page") { offset += partialY; } //$NON-NLS-0$
				div.style.top = -offset + "px"; //$NON-NLS-0$
				div.style.height = (rulerHeight + offset) + "px"; //$NON-NLS-0$
				div = div.nextSibling;
			}
			divRuler.style.height = rulerHeight + "px"; //$NON-NLS-0$
		},
		_updateRuler: function (divRuler, topIndex, bottomIndex) {
			if (!divRuler) { return; }
			var cells = divRuler.firstChild.rows[0].cells;
			var lineHeight = this._getLineHeight();
			var viewPad = this._getViewPadding();
			for (var i = 0; i < cells.length; i++) {
				var div = cells[i].firstChild;
				var ruler = div._ruler;
				if (div.rulerChanged) {
					this._applyStyle(ruler.getRulerStyle(), div);
				}
				
				var widthDiv;
				var child = div.firstChild;
				if (child) {
					widthDiv = child;
					child = child.nextSibling;
				} else {
					widthDiv = document.createElement("DIV"); //$NON-NLS-0$
					widthDiv.style.visibility = "hidden"; //$NON-NLS-0$
					div.appendChild(widthDiv);
				}
				var lineIndex, annotation;
				if (div.rulerChanged) {
					if (widthDiv) {
						lineIndex = -1;
						annotation = ruler.getWidestAnnotation();
						if (annotation) {
							this._applyStyle(annotation.style, widthDiv);
							if (annotation.html) {
								widthDiv.innerHTML = annotation.html;
							}
						}
						widthDiv.lineIndex = lineIndex;
						widthDiv.style.height = (lineHeight + viewPad.top) + "px"; //$NON-NLS-0$
					}
				}

				var overview = ruler.getOverview(), lineDiv, frag, annotations;
				if (overview === "page") { //$NON-NLS-0$
					annotations = ruler.getAnnotations(topIndex, bottomIndex + 1);
					while (child) {
						lineIndex = child.lineIndex;
						var nextChild = child.nextSibling;
						if (!(topIndex <= lineIndex && lineIndex <= bottomIndex) || child.lineChanged) {
							div.removeChild(child);
						}
						child = nextChild;
					}
					child = div.firstChild.nextSibling;
					frag = document.createDocumentFragment();
					for (lineIndex=topIndex; lineIndex<=bottomIndex; lineIndex++) {
						if (!child || child.lineIndex > lineIndex) {
							lineDiv = document.createElement("DIV"); //$NON-NLS-0$
							annotation = annotations[lineIndex];
							if (annotation) {
								this._applyStyle(annotation.style, lineDiv);
								if (annotation.html) {
									lineDiv.innerHTML = annotation.html;
								}
								lineDiv.annotation = annotation;
							}
							lineDiv.lineIndex = lineIndex;
							lineDiv.style.height = lineHeight + "px"; //$NON-NLS-0$
							frag.appendChild(lineDiv);
						} else {
							if (frag.firstChild) {
								div.insertBefore(frag, child);
								frag = document.createDocumentFragment();
							}
							if (child) {
								child = child.nextSibling;
							}
						}
					}
					if (frag.firstChild) { div.insertBefore(frag, child); }
				} else {
					var clientHeight = this._getClientHeight ();
					var lineCount = this._model.getLineCount ();
					var contentHeight = lineHeight * lineCount;
					var trackHeight = clientHeight + viewPad.top + viewPad.bottom - 2 * this._metrics.scrollWidth;
					var divHeight;
					if (contentHeight < trackHeight) {
						divHeight = lineHeight;
					} else {
						divHeight = trackHeight / lineCount;
					}
					if (div.rulerChanged) {
						var count = div.childNodes.length;
						while (count > 1) {
							div.removeChild(div.lastChild);
							count--;
						}
						annotations = ruler.getAnnotations(0, lineCount);
						frag = document.createDocumentFragment();
						for (var prop in annotations) {
							lineIndex = prop >>> 0;
							if (lineIndex < 0) { continue; }
							lineDiv = document.createElement("DIV"); //$NON-NLS-0$
							annotation = annotations[prop];
							this._applyStyle(annotation.style, lineDiv);
							lineDiv.style.position = "absolute"; //$NON-NLS-0$
							lineDiv.style.top = this._metrics.scrollWidth + lineHeight + Math.floor(lineIndex * divHeight) + "px"; //$NON-NLS-0$
							if (annotation.html) {
								lineDiv.innerHTML = annotation.html;
							}
							lineDiv.annotation = annotation;
							lineDiv.lineIndex = lineIndex;
							frag.appendChild(lineDiv);
						}
						div.appendChild(frag);
					} else if (div._oldTrackHeight !== trackHeight) {
						lineDiv = div.firstChild ? div.firstChild.nextSibling : null;
						while (lineDiv) {
							lineDiv.style.top = this._metrics.scrollWidth + lineHeight + Math.floor(lineDiv.lineIndex * divHeight) + "px"; //$NON-NLS-0$
							lineDiv = lineDiv.nextSibling;
						}
					}
					div._oldTrackHeight = trackHeight;
				}
				div.rulerChanged = false;
				div = div.nextSibling;
			}
		},
		_updateStyle: function (init) {
			if (!init && isIE) {
				this._rootDiv.style.lineHeight = "normal"; //$NON-NLS-0$
			}
			var metrics = this._metrics = this._calculateMetrics();
			if (isIE) {
				this._rootDiv.style.lineHeight = (metrics.lineHeight - (metrics.lineTrim.top + metrics.lineTrim.bottom)) + "px"; //$NON-NLS-0$
			} else {
				this._rootDiv.style.lineHeight = "normal"; //$NON-NLS-0$
			}
			if (!init) {
				this.redraw();
				this._resetLineWidth();
			}
		}
	};//end prototype
	mEventTarget.EventTarget.addMixin(TextView.prototype);
	
	return {TextView: TextView};
});

