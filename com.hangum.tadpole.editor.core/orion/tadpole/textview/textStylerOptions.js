/*******************************************************************************
 * @license
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 * 
 * Contributors: 
 *		Anton McConville (IBM Corporation) - initial API and implementation
 *		Felipe Heidrich (IBM Corporation) - initial API and implementation 
 ******************************************************************************/

/*global window document define localStorage*/

define("tadpole/textview/textStylerOptions", ['orion/bootstrap'], function(mBootstrap) {

	var CATEGORY = "JavaScript Editor";
	var USER_THEME = "userTheme";
	
	
	
	var preferences;

	/**
	 * Constructs ...
	 * 
	 * Working with local storage for initial settings proof of concept
	 */
	function TextStylerOptions (styler) {
	
		this._styler = styler;
		this._view = this._styler.view;
		var self = this;
		this._listener = {
			onStorage: function(e) {
				self._onStorage(e);
			}
		};
		
		var stylerOptions = this;
		
		if (this._view) {
			mBootstrap.startup().then(function(core ) {
				preferences = core.preferences;
				stylerOptions.preferences = preferences;
				stylerOptions._updateStylesheet(preferences);
				stylerOptions.storageKey = preferences.listenForChangedSettings( stylerOptions._listener.onStorage );
			});
		}
	}
	
	TextStylerOptions.prototype = /** @lends examples.textview.TextStylerOptions.prototype */ {
		_getSetting: function(subcategories, subcategory, element){
			var value;
			for(var sub = 0; sub < subcategories.length; sub++){
				if(subcategories[sub].label === subcategory){
					for(var item = 0; item < subcategories[sub].data.length; item++){
						if(subcategories[sub].data[item].label === element){
							value = subcategories[sub].data[item].value;
							break;
						}
					}
				}
			}
			return value;
		}, 
		_getStyleSheet: function (subcategories, theme, sUtil) {
			console.log( subcategories );
			var result = [];
			result.push("");
			
			//view container
			var family = this.preferences.getSetting(subcategories, "Font", "Family").toLowerCase();
			if(family === "sans serif"){
				family = '"Menlo", "Consolas", "Vera Mono", "monospace"';
			}else{
				family = 'monospace';
			}	
			var size = this.preferences.getSetting(subcategories, "Font", "Size");
			var color = this.preferences.getSetting(subcategories, "Font", "Color");
			var background = this.preferences.getSetting(subcategories, "Font", "Background");
			result.push("." + theme + " {");
			result.push("\tfont-family: " + family + ";");
			result.push("\tfont-size: " + size + ";");
			result.push("\tcolor: " + color + ";");
			result.push("}");
			
			//view
			result.push("." + theme + " .textview {");
			result.push("\tbackground-color: " + background + ";");
			result.push("}");
			
			// Annotations Ruler
			
			var annotationsBackground = this.preferences.getSetting(subcategories, "Annotations Ruler", "Background");
			console.log( annotationsBackground );
			result.push("." + theme + ".ruler.annotations{");
			result.push("\tbackground-color: " + 'red' + ";");
			result.push("}");

			//ruler
			result.push("." + theme + " .ruler {");
			result.push("\tbackground-color: " + annotationsBackground + ";");
			result.push("}");
			
			//rulerLines
			var lineNumberBackground = this.preferences.getSetting(subcategories, "Line Number Ruler", "Background");
			var lineNumberForeground = this.preferences.getSetting(subcategories, "Line Number Ruler", "Color");
			result.push("." + theme + " .rulerLines {");
			result.push("\tcolor: " + lineNumberForeground + ";");
			result.push("\tbackground-color: " + lineNumberBackground + ";");
			result.push("}");
			
			var evenlineNumberBackground = this.preferences.getSetting(subcategories, "Line Number Ruler", "Even Rows Background");
			var evenlineNumberForeground = this.preferences.getSetting(subcategories, "Line Number Ruler", "Even Rows Color");
			result.push("." + theme + " .rulerLines.even {");
			result.push("\tcolor: " + evenlineNumberForeground + ";");
			result.push("\tbackground-color: " + evenlineNumberBackground + ";");
			result.push("}");

			var oddlineNumberBackground = this.preferences.getSetting(subcategories, "Line Number Ruler", "Odd Rows Background");
			var oddlineNumberForeground = this.preferences.getSetting(subcategories, "Line Number Ruler", "Odd Rows Color");
			result.push("." + theme + " .rulerLines.even {");
			result.push("\tcolor: " + oddlineNumberForeground + ";");
			result.push("\tbackground-color: " + oddlineNumberBackground + ";");
			result.push("}");

			var overviewBackground = this.preferences.getSetting(subcategories, "Overview Ruler", "Background");
			var overviewForeground = this.preferences.getSetting(subcategories, "Overview Ruler", "Color");
			result.push("." + theme + " .ruler.overview {");
			result.push("\tcolor: " + overviewForeground + ";");
			result.push("\tbackground-color: " + overviewBackground + ";");
			result.push("}");

			var foldingBackground = this.preferences.getSetting(subcategories, "Folding Ruler", "Background");
			var foldingForeground = this.preferences.getSetting(subcategories, "Folding Ruler", "Color");
			result.push("." + theme + " .ruler.folding {");
			result.push("\tcolor: " + foldingForeground + ";");
			result.push("\tbackground-color: " + foldingBackground + ";");
			result.push("}");


			


			var _this = this;
			var styler = this._styler;
			function defineRule(token, settingName) {
				var className = styler.getClassNameForToken(token);
				console.log( className );
				
				if (className) {
					var color = preferences.getSetting(subcategories, settingName, "Color");
					var weight = preferences.getSetting(subcategories, settingName, "Weight").toLowerCase();
					result.push("." + theme + " ." + className +  " {");
					result.push("\tcolor: " + color + ";");
					result.push("\tfont-weight: " + weight + ";");
					result.push("}");
				}
			}
			if (styler.getClassNameForToken) {
				defineRule("keyword", "Keyword Types");
				defineRule("string", "String Types");
				defineRule("singleLineComment", "Comment Types");
				defineRule("multiLineComment", "Comment Types");
				defineRule("docComment", "Comment Types");
				defineRule("docHtmlComment", "Comment Types");
			}							
			return result.join("\n");//easier for debuggin 
		},
		_onStorage: function (e) {
			if( e.key === this.storageKey ){
				this._updateStylesheet( this.preferences );
			}
		},
		_updateStylesheet: function (preferences, sUtil) {

			var storage;
			var stylerOptions = this;
			
			preferences.getPreferences('/settings', 2).then( function(prefs){	
			
				var data = prefs.get(CATEGORY);
				
				if( data !== undefined ){
			
					storage = JSON.parse( prefs.get(CATEGORY) );	
					if (!storage) { return; }
					if (stylerOptions._stylesheet) {
						stylerOptions._stylesheet.parentNode.removeChild(stylerOptions._stylesheet);
						stylerOptions._stylesheet = null;
					}
					var stylesheet = stylerOptions._stylesheet = document.createElement("STYLE");
					stylesheet.appendChild(document.createTextNode(stylerOptions._getStyleSheet( storage, USER_THEME, sUtil)));
					var head = document.getElementsByTagName("HEAD")[0] || document.documentElement;
					head.appendChild(stylesheet);
					var view = stylerOptions._view;
					var options = {themeClass:null};
					view.getOptions(options);
					var theme = options.themeClass;
					if (theme) {
						theme = theme.replace(USER_THEME, "");
						if (theme) { theme += " "; }
						theme += USER_THEME;
					} else {
						theme = USER_THEME;
					}
					options.themeClass = theme;
					view.setOptions(options);
					view.update(true);
				}
			} );
		}
	};
	return {TextStylerOptions: TextStylerOptions};
});
