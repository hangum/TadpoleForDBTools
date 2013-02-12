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
/*global define */

define("orion/editor/cssContentAssist", [], function() {
	var keywords = ["alignment-adjust", "alignment-baseline", "animation", "animation-delay", "animation-direction", "animation-duration",
			"animation-iteration-count", "animation-name", "animation-play-state", "animation-timing-function", "appearance",
			"azimuth", "backface-visibility", "background", "background-attachment", "background-clip", "background-color",
			"background-image", "background-origin", "background-position", "background-repeat", "background-size", "baseline-shift",
			"binding", "bleed", "bookmark-label", "bookmark-level", "bookmark-state", "bookmark-target", "border", "border-bottom",
			"border-bottom-color", "border-bottom-left-radius", "border-bottom-right-radius", "border-bottom-style", "border-bottom-width",
			"border-collapse", "border-color", "border-image", "border-image-outset", "border-image-repeat", "border-image-slice",
			"border-image-source", "border-image-width", "border-left", "border-left-color", "border-left-style", "border-left-width",
			"border-radius", "border-right", "border-right-color", "border-right-style", "border-right-width", "border-spacing", "border-style",
			"border-top", "border-top-color", "border-top-left-radius", "border-top-right-radius", "border-top-style", "border-top-width",
			"border-width", "bottom", "box-align", "box-decoration-break", "box-direction", "box-flex", "box-flex-group", "box-lines",
			"box-ordinal-group", "box-orient", "box-pack", "box-shadow", "box-sizing", "break-after", "break-before", "break-inside",
			"caption-side", "clear", "clip", "color", "color-profile", "column-count", "column-fill", "column-gap", "column-rule",
			"column-rule-color", "column-rule-style", "column-rule-width", "column-span", "column-width", "columns", "content", "counter-increment",
			"counter-reset", "crop", "cue", "cue-after", "cue-before", "cursor", "direction", "display", "dominant-baseline",
			"drop-initial-after-adjust", "drop-initial-after-align", "drop-initial-before-adjust", "drop-initial-before-align", "drop-initial-size",
			"drop-initial-value", "elevation", "empty-cells", "fit", "fit-position", "flex-align", "flex-flow", "flex-inline-pack", "flex-order",
			"flex-pack", "float", "float-offset", "font", "font-family", "font-size", "font-size-adjust", "font-stretch", "font-style",
			"font-variant", "font-weight", "grid-columns", "grid-rows", "hanging-punctuation", "height", "hyphenate-after",
			"hyphenate-before", "hyphenate-character", "hyphenate-lines", "hyphenate-resource", "hyphens", "icon", "image-orientation",
			"image-rendering", "image-resolution", "inline-box-align", "left", "letter-spacing", "line-height", "line-stacking",
			"line-stacking-ruby", "line-stacking-shift", "line-stacking-strategy", "list-style", "list-style-image", "list-style-position",
			"list-style-type", "margin", "margin-bottom", "margin-left", "margin-right", "margin-top", "mark", "mark-after", "mark-before",
			"marker-offset", "marks", "marquee-direction", "marquee-loop", "marquee-play-count", "marquee-speed", "marquee-style", "max-height",
			"max-width", "min-height", "min-width", "move-to", "nav-down", "nav-index", "nav-left", "nav-right", "nav-up", "opacity", "orphans",
			"outline", "outline-color", "outline-offset", "outline-style", "outline-width", "overflow", "overflow-style", "overflow-x",
			"overflow-y", "padding", "padding-bottom", "padding-left", "padding-right", "padding-top", "page", "page-break-after", "page-break-before",
			"page-break-inside", "page-policy", "pause", "pause-after", "pause-before", "perspective", "perspective-origin", "phonemes", "pitch",
			"pitch-range", "play-during", "position", "presentation-level", "punctuation-trim", "quotes", "rendering-intent", "resize",
			"rest", "rest-after", "rest-before", "richness", "right", "rotation", "rotation-point", "ruby-align", "ruby-overhang", "ruby-position",
			"ruby-span", "size", "speak", "speak-header", "speak-numeral", "speak-punctuation", "speech-rate", "stress", "string-set", "table-layout",
			"target", "target-name", "target-new", "target-position", "text-align", "text-align-last", "text-decoration", "text-emphasis",
			"text-height", "text-indent", "text-justify", "text-outline", "text-shadow", "text-transform", "text-wrap", "top", "transform",
			"transform-origin", "transform-style", "transition", "transition-delay", "transition-duration", "transition-property",
			"transition-timing-function", "unicode-bidi", "vertical-align", "visibility", "voice-balance", "voice-duration", "voice-family",
			"voice-pitch", "voice-pitch-range", "voice-rate", "voice-stress", "voice-volume", "volume", "white-space", "white-space-collapse",
			"widows", "width", "word-break", "word-spacing", "word-wrap", "z-index"];

	function getCSSPrefix(buffer, offset) {
		var index = offset;
		while (index && /[A-Za-z\-]/.test(buffer.charAt(index - 1))) {
			index--;
		}
		return index ? buffer.substring(index, offset) : "";
	}

	/**
	 * @name orion.contentAssist.CssContentAssistProvider
	 * @class Provides content assist for CSS keywords.
	 */
	function CssContentAssistProvider() {
	}
	CssContentAssistProvider.prototype = /** @lends orion.editor.CssContentAssistProvider.prototype */ {
		/**
		 * @param {String} buffer The entire buffer being edited.
		 * @param {Number} offset The offset at which proposals will be inserted.
		 * @param {Object} context Extra information about the editor state.
		 */
		computeProposals: function(buffer, offset, context) {
			var cssPrefix = getCSSPrefix(buffer, offset);
			var proposals = [];
			for (var i=0; i < keywords.length; i++) {
					var keyword = keywords[i];
					if (keyword.indexOf(cssPrefix) === 0) {
						proposals.push({
							proposal: keyword.substring(cssPrefix.length),
							description: keyword
						});
					}
			}
			return proposals;
		}
	};

	return {
		CssContentAssistProvider: CssContentAssistProvider
	};
});
