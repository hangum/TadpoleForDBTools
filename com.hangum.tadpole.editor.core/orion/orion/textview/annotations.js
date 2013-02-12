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
 ******************************************************************************/

/*global define */

define("orion/textview/annotations", ['i18n!orion/textview/nls/messages', 'orion/textview/eventTarget'], function(messages, mEventTarget) { //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-0$
	/**
	 * @class This object represents a decoration attached to a range of text. Annotations are added to a
	 * <code>AnnotationModel</code> which is attached to a <code>TextModel</code>.
	 * <p>
	 * <b>See:</b><br/>
	 * {@link orion.textview.AnnotationModel}<br/>
	 * {@link orion.textview.Ruler}<br/>
	 * </p>		 
	 * @name orion.textview.Annotation
	 * 
	 * @property {String} type The annotation type (for example, orion.annotation.error).
	 * @property {Number} start The start offset of the annotation in the text model.
	 * @property {Number} end The end offset of the annotation in the text model.
	 * @property {String} html The HTML displayed for the annotation.
	 * @property {String} title The text description for the annotation.
	 * @property {orion.textview.Style} style The style information for the annotation used in the annotations ruler and tooltips.
	 * @property {orion.textview.Style} overviewStyle The style information for the annotation used in the overview ruler.
	 * @property {orion.textview.Style} rangeStyle The style information for the annotation used in the text view to decorate a range of text.
	 * @property {orion.textview.Style} lineStyle The style information for the annotation used in the text view to decorate a line of text.
	 */
	/**
	 * Constructs a new folding annotation.
	 * 
	 * @param {Number} start The start offset of the annotation in the text model.
	 * @param {Number} end The end offset of the annotation in the text model.
	 * @param {orion.textview.ProjectionTextModel} projectionModel The projection text model.
	 * 
	 * @class This object represents a folding annotation.
	 * @name orion.textview.FoldingAnnotation
	 */
	function FoldingAnnotation (start, end, projectionModel) {
		this.start = start;
		this.end = end;
		this._projectionModel = projectionModel;
		this.html = this._expandedHTML;
		this.style = this._expandedStyle;
		this.expanded = true;
	}
	
	FoldingAnnotation.prototype = /** @lends orion.textview.FoldingAnnotation.prototype */ {
		_expandedHTML: "<div class='annotationHTML expanded'></div>", //$NON-NLS-0$
		_expandedStyle: {styleClass: "annotation expanded"}, //$NON-NLS-0$
		_collapsedHTML: "<div class='annotationHTML collapsed'></div>", //$NON-NLS-0$
		_collapsedStyle: {styleClass: "annotation collapsed"}, //$NON-NLS-0$
		/**
		 * Collapses the annotation.
		 */
		collapse: function () {
			if (!this.expanded) { return; }
			this.expanded = false;
			this.html = this._collapsedHTML;
			this.style = this._collapsedStyle;
			var projectionModel = this._projectionModel;
			var baseModel = projectionModel.getBaseModel();
			this._projection = {
				start: baseModel.getLineStart(baseModel.getLineAtOffset(this.start) + 1),
				end: baseModel.getLineEnd(baseModel.getLineAtOffset(this.end), true)
			};
			projectionModel.addProjection(this._projection);
		},
		/**
		 * Expands the annotation.
		 */
		expand: function () {
			if (this.expanded) { return; }
			this.expanded = true;
			this.html = this._expandedHTML;
			this.style = this._expandedStyle;
			this._projectionModel.removeProjection(this._projection);
		}
	};
	 
	/**
	 * @class This object represents a regitry of annotation types.
	 * @name orion.textview.AnnotationType
	 */
	function AnnotationType() {
	}
	
	/**
	 * Error annotation type.
	 */
	AnnotationType.ANNOTATION_ERROR = "orion.annotation.error"; //$NON-NLS-0$
	/**
	 * Warning annotation type.
	 */
	AnnotationType.ANNOTATION_WARNING = "orion.annotation.warning"; //$NON-NLS-0$
	/**
	 * Task annotation type.
	 */
	AnnotationType.ANNOTATION_TASK = "orion.annotation.task"; //$NON-NLS-0$
	/**
	 * Breakpoint annotation type.
	 */
	AnnotationType.ANNOTATION_BREAKPOINT = "orion.annotation.breakpoint"; //$NON-NLS-0$
	/**
	 * Bookmark annotation type.
	 */
	AnnotationType.ANNOTATION_BOOKMARK = "orion.annotation.bookmark"; //$NON-NLS-0$
	/**
	 * Folding annotation type.
	 */
	AnnotationType.ANNOTATION_FOLDING = "orion.annotation.folding"; //$NON-NLS-0$
	/**
	 * Curent bracket annotation type.
	 */
	AnnotationType.ANNOTATION_CURRENT_BRACKET = "orion.annotation.currentBracket"; //$NON-NLS-0$
	/**
	 * Matching bracket annotation type.
	 */
	AnnotationType.ANNOTATION_MATCHING_BRACKET = "orion.annotation.matchingBracket"; //$NON-NLS-0$
	/**
	 * Current line annotation type.
	 */
	AnnotationType.ANNOTATION_CURRENT_LINE = "orion.annotation.currentLine"; //$NON-NLS-0$
	/**
	 * Current search annotation type.
	 */
	AnnotationType.ANNOTATION_CURRENT_SEARCH = "orion.annotation.currentSearch"; //$NON-NLS-0$
	/**
	 * Matching search annotation type.
	 */
	AnnotationType.ANNOTATION_MATCHING_SEARCH = "orion.annotation.matchingSearch"; //$NON-NLS-0$
	
	/** @private */
	var annotationTypes = {};
	
	/**
	 * Register an annotation type.
	 *
	 * @param {String} type The annotation type (for example, orion.annotation.error).
	 * @param {Object|Function} properties The common annotation properties of the registered
	 *		annotation type. All annotations create with this annotation type will expose these
	 *		properties.
	 */
	AnnotationType.registerType = function(type, properties) {
		var constructor = properties;
		if (typeof constructor !== "function") { //$NON-NLS-0$
			constructor = function(start, end, title) {
				this.start = start;
				this.end = end;
				if (title) { this.title = title; }
			};
			constructor.prototype = properties;
		}
		constructor.prototype.type = type;
		annotationTypes[type] = constructor;
		return type;
	};
	
	/**
	 * Creates an annotation of a given type with the specified start end end offsets.
	 *
	 * @param {String} type The annotation type (for example, orion.annotation.error).
	 * @param {Number} start The start offset of the annotation in the text model.
	 * @param {Number} end The end offset of the annotation in the text model.
	 * @param {String} [title] The text description for the annotation if different then the type description.
	 * @return {orion.textview.Annotation} the new annotation
	 */
	AnnotationType.createAnnotation = function(type, start, end, title) {
		return new (this.getType(type))(start, end, title);
	};
	
	/**
	 * Gets the registered annotation type with specified type. The returned
	 * value is a constructor that can be used to create annotations of the
	 * speficied type.  The constructor takes the start and end offsets of
	 * the annotation.
	 *
	 * @param {String} type The annotation type (for example, orion.annotation.error).
	 * @return {Function} The annotation type constructor ( i.e function(start, end, title) ).
	 */
	AnnotationType.getType = function(type) {
		return annotationTypes[type];
	};
	
	/** @private */
	function registerType(type, lineStyling) {
		var index = type.lastIndexOf('.'); //$NON-NLS-0$
		var suffix = type.substring(index + 1);
		var properties = {
			title: messages[suffix],
			style: {styleClass: "annotation " + suffix}, //$NON-NLS-0$
			html: "<div class='annotationHTML " + suffix + "'></div>", //$NON-NLS-1$ //$NON-NLS-0$
			overviewStyle: {styleClass: "annotationOverview " + suffix} //$NON-NLS-0$
		};
		if (lineStyling) {
			properties.lineStyle = {styleClass: "annotationLine " + suffix}; //$NON-NLS-0$
		} else {
			properties.rangeStyle = {styleClass: "annotationRange " + suffix}; //$NON-NLS-0$
		}
		AnnotationType.registerType(type, properties);
	}
	registerType(AnnotationType.ANNOTATION_ERROR);
	registerType(AnnotationType.ANNOTATION_WARNING);
	registerType(AnnotationType.ANNOTATION_TASK);
	registerType(AnnotationType.ANNOTATION_BREAKPOINT);
	registerType(AnnotationType.ANNOTATION_BOOKMARK);
	registerType(AnnotationType.ANNOTATION_CURRENT_BRACKET);
	registerType(AnnotationType.ANNOTATION_MATCHING_BRACKET);
	registerType(AnnotationType.ANNOTATION_CURRENT_SEARCH);
	registerType(AnnotationType.ANNOTATION_MATCHING_SEARCH);
	registerType(AnnotationType.ANNOTATION_CURRENT_LINE, true);
	AnnotationType.registerType(AnnotationType.ANNOTATION_FOLDING, FoldingAnnotation);
	
	/** 
	 * Constructs a new AnnotationTypeList object.
	 * 
	 * @class This represents an interface of prioritized annotation types.
	 * @name orion.textview.AnnotationTypeList
	 */
	function AnnotationTypeList () {
	}
	/**
	 * Adds in the annotation type interface into the specified object.
	 *
	 * @param {Object} object The object to add in the annotation type interface.
	 */
	AnnotationTypeList.addMixin = function(object) {
		var proto = AnnotationTypeList.prototype;
		for (var p in proto) {
			if (proto.hasOwnProperty(p)) {
				object[p] = proto[p];
			}
		}
	};	
	AnnotationTypeList.prototype = /** @lends orion.textview.AnnotationTypeList.prototype */ {
		/**
		 * Adds an annotation type to the receiver.
		 * <p>
		 * Only annotations of the specified types will be shown by
		 * the receiver.
		 * </p>
		 *
		 * @param {Object} type the annotation type to be shown
		 * 
		 * @see #removeAnnotationType
		 * @see #isAnnotationTypeVisible
		 */
		addAnnotationType: function(type) {
			if (!this._annotationTypes) { this._annotationTypes = []; }
			this._annotationTypes.push(type);
		},
		/**
		 * Gets the annotation type priority.  The priority is determined by the
		 * order the annotation type is added to the receiver.  Annotation types
		 * added first have higher priority.
		 * <p>
		 * Returns <code>0</code> if the annotation type is not added.
		 * </p>
		 *
		 * @param {Object} type the annotation type
		 * 
		 * @see #addAnnotationType
		 * @see #removeAnnotationType
		 * @see #isAnnotationTypeVisible
		 */
		getAnnotationTypePriority: function(type) {
			if (this._annotationTypes) { 
				for (var i = 0; i < this._annotationTypes.length; i++) {
					if (this._annotationTypes[i] === type) {
						return i + 1;
					}
				}
			}
			return 0;
		},
		/**
		 * Returns an array of annotations in the specified annotation model for the given range of text sorted by type.
		 *
		 * @param {orion.textview.AnnotationModel} annotationModel the annotation model.
		 * @param {Number} start the start offset of the range.
		 * @param {Number} end the end offset of the range.
		 * @return {orion.textview.Annotation[]} an annotation array.
		 */
		getAnnotationsByType: function(annotationModel, start, end) {
			var iter = annotationModel.getAnnotations(start, end);
			var annotation, annotations = [];
			while (iter.hasNext()) {
				annotation = iter.next();
				var priority = this.getAnnotationTypePriority(annotation.type);
				if (priority === 0) { continue; }
				annotations.push(annotation);
			}
			var self = this;
			annotations.sort(function(a, b) {
				return self.getAnnotationTypePriority(a.type) - self.getAnnotationTypePriority(b.type);
			});
			return annotations;
		},
		/**
		 * Returns whether the receiver shows annotations of the specified type.
		 *
		 * @param {Object} type the annotation type 
		 * @returns {Boolean} whether the specified annotation type is shown
		 * 
		 * @see #addAnnotationType
		 * @see #removeAnnotationType
		 */
		isAnnotationTypeVisible: function(type) {
			return this.getAnnotationTypePriority(type) !== 0;
		},
		/**
		 * Removes an annotation type from the receiver.
		 *
		 * @param {Object} type the annotation type to be removed
		 * 
		 * @see #addAnnotationType
		 * @see #isAnnotationTypeVisible
		 */
		removeAnnotationType: function(type) {
			if (!this._annotationTypes) { return; }
			for (var i = 0; i < this._annotationTypes.length; i++) {
				if (this._annotationTypes[i] === type) {
					this._annotationTypes.splice(i, 1);
					break;
				}
			}
		}
	};
	
	/**
	 * Constructs an annotation model.
	 * 
	 * @param {textModel} textModel The text model.
	 * 
	 * @class This object manages annotations for a <code>TextModel</code>.
	 * <p>
	 * <b>See:</b><br/>
	 * {@link orion.textview.Annotation}<br/>
	 * {@link orion.textview.TextModel}<br/> 
	 * </p>	
	 * @name orion.textview.AnnotationModel
	 * @borrows orion.textview.EventTarget#addEventListener as #addEventListener
	 * @borrows orion.textview.EventTarget#removeEventListener as #removeEventListener
	 * @borrows orion.textview.EventTarget#dispatchEvent as #dispatchEvent
	 */
	function AnnotationModel(textModel) {
		this._annotations = [];
		var self = this;
		this._listener = {
			onChanged: function(modelChangedEvent) {
				self._onChanged(modelChangedEvent);
			}
		};
		this.setTextModel(textModel);
	}

	AnnotationModel.prototype = /** @lends orion.textview.AnnotationModel.prototype */ {
		/**
		 * Adds an annotation to the annotation model. 
		 * <p>The annotation model listeners are notified of this change.</p>
		 * 
		 * @param {orion.textview.Annotation} annotation the annotation to be added.
		 * 
		 * @see #removeAnnotation
		 */
		addAnnotation: function(annotation) {
			if (!annotation) { return; }
			var annotations = this._annotations;
			var index = this._binarySearch(annotations, annotation.start);
			annotations.splice(index, 0, annotation);
			var e = {
				type: "Changed", //$NON-NLS-0$
				added: [annotation],
				removed: [],
				changed: []
			};
			this.onChanged(e);
		},
		/**
		 * Returns the text model. 
		 * 
		 * @return {orion.textview.TextModel} The text model.
		 * 
		 * @see #setTextModel
		 */
		getTextModel: function() {
			return this._model;
		},
		/**
		 * @class This object represents an annotation iterator.
		 * <p>
		 * <b>See:</b><br/>
		 * {@link orion.textview.AnnotationModel#getAnnotations}<br/>
		 * </p>		 
		 * @name orion.textview.AnnotationIterator
		 * 
		 * @property {Function} hasNext Determines whether there are more annotations in the iterator.
		 * @property {Function} next Returns the next annotation in the iterator.
		 */		
		/**
		 * Returns an iterator of annotations for the given range of text.
		 *
		 * @param {Number} start the start offset of the range.
		 * @param {Number} end the end offset of the range.
		 * @return {orion.textview.AnnotationIterator} an annotation iterartor.
		 */
		getAnnotations: function(start, end) {
			var annotations = this._annotations, current;
			//TODO binary search does not work for range intersection when there are overlaping ranges, need interval search tree for this
			var i = 0;
			var skip = function() {
				while (i < annotations.length) {
					var a =  annotations[i++];
					if ((start === a.start) || (start > a.start ? start < a.end : a.start < end)) {
						return a;
					}
					if (a.start >= end) {
						break;
					}
				}
				return null;
			};
			current = skip();
			return {
				next: function() {
					var result = current;
					if (result) { current = skip(); }
					return result;					
				},
				hasNext: function() {
					return current !== null;
				}
			};
		},
		/**
		 * Notifies the annotation model that the given annotation has been modified.
		 * <p>The annotation model listeners are notified of this change.</p>
		 * 
		 * @param {orion.textview.Annotation} annotation the modified annotation.
		 * 
		 * @see #addAnnotation
		 */
		modifyAnnotation: function(annotation) {
			if (!annotation) { return; }
			var index = this._getAnnotationIndex(annotation);
			if (index < 0) { return; }
			var e = {
				type: "Changed", //$NON-NLS-0$
				added: [],
				removed: [],
				changed: [annotation]
			};
			this.onChanged(e);
		},
		/**
		 * Notifies all listeners that the annotation model has changed.
		 *
		 * @param {orion.textview.Annotation[]} added The list of annotation being added to the model.
		 * @param {orion.textview.Annotation[]} changed The list of annotation modified in the model.
		 * @param {orion.textview.Annotation[]} removed The list of annotation being removed from the model.
		 * @param {ModelChangedEvent} textModelChangedEvent the text model changed event that trigger this change, can be null if the change was trigger by a method call (for example, {@link #addAnnotation}).
		 */
		onChanged: function(e) {
			return this.dispatchEvent(e);
		},
		/**
		 * Removes all annotations of the given <code>type</code>. All annotations
		 * are removed if the type is not specified. 
		 * <p>The annotation model listeners are notified of this change.  Only one changed event is generated.</p>
		 * 
		 * @param {Object} type the type of annotations to be removed.
		 * 
		 * @see #removeAnnotation
		 */
		removeAnnotations: function(type) {
			var annotations = this._annotations;
			var removed, i; 
			if (type) {
				removed = [];
				for (i = annotations.length - 1; i >= 0; i--) {
					var annotation = annotations[i];
					if (annotation.type === type) {
						annotations.splice(i, 1);
					}
					removed.splice(0, 0, annotation);
				}
			} else {
				removed = annotations;
				annotations = [];
			}
			var e = {
				type: "Changed", //$NON-NLS-0$
				removed: removed,
				added: [],
				changed: []
			};
			this.onChanged(e);
		},
		/**
		 * Removes an annotation from the annotation model. 
		 * <p>The annotation model listeners are notified of this change.</p>
		 * 
		 * @param {orion.textview.Annotation} annotation the annotation to be removed.
		 * 
		 * @see #addAnnotation
		 */
		removeAnnotation: function(annotation) {
			if (!annotation) { return; }
			var index = this._getAnnotationIndex(annotation);
			if (index < 0) { return; }
			var e = {
				type: "Changed", //$NON-NLS-0$
				removed: this._annotations.splice(index, 1),
				added: [],
				changed: []
			};
			this.onChanged(e);
		},
		/**
		 * Removes and adds the specifed annotations to the annotation model. 
		 * <p>The annotation model listeners are notified of this change.  Only one changed event is generated.</p>
		 * 
		 * @param {orion.textview.Annotation} remove the annotations to be removed.
		 * @param {orion.textview.Annotation} add the annotations to be added.
		 * 
		 * @see #addAnnotation
		 * @see #removeAnnotation
		 */
		replaceAnnotations: function(remove, add) {
			var annotations = this._annotations, i, index, annotation, removed = [];
			if (remove) {
				for (i = remove.length - 1; i >= 0; i--) {
					annotation = remove[i];
					index = this._getAnnotationIndex(annotation);
					if (index < 0) { continue; }
					annotations.splice(index, 1);
					removed.splice(0, 0, annotation);
				}
			}
			if (!add) { add = []; }
			for (i = 0; i < add.length; i++) {
				annotation = add[i];
				index = this._binarySearch(annotations, annotation.start);
				annotations.splice(index, 0, annotation);
			}
			var e = {
				type: "Changed", //$NON-NLS-0$
				removed: removed,
				added: add,
				changed: []
			};
			this.onChanged(e);
		},
		/**
		 * Sets the text model of the annotation model.  The annotation
		 * model listens for changes in the text model to update and remove
		 * annotations that are affected by the change.
		 * 
		 * @param {orion.textview.TextModel} textModel the text model.
		 * 
		 * @see #getTextModel
		 */
		setTextModel: function(textModel) {
			if (this._model) {
				this._model.removeEventListener("Changed", this._listener.onChanged); //$NON-NLS-0$
			}
			this._model = textModel;
			if (this._model) {
				this._model.addEventListener("Changed", this._listener.onChanged); //$NON-NLS-0$
			}
		},
		/** @ignore */
		_binarySearch: function (array, offset) {
			var high = array.length, low = -1, index;
			while (high - low > 1) {
				index = Math.floor((high + low) / 2);
				if (offset <= array[index].start) {
					high = index;
				} else {
					low = index;
				}
			}
			return high;
		},
		/** @ignore */
		_getAnnotationIndex: function(annotation) {
			var annotations = this._annotations;
			var index = this._binarySearch(annotations, annotation.start);
			while (index < annotations.length && annotations[index].start === annotation.start) {
				if (annotations[index] === annotation) {
					return index;
				}
				index++;
			}
			return -1;
		},
		/** @ignore */
		_onChanged: function(modelChangedEvent) {
			var start = modelChangedEvent.start;
			var addedCharCount = modelChangedEvent.addedCharCount;
			var removedCharCount = modelChangedEvent.removedCharCount;
			var annotations = this._annotations, end = start + removedCharCount;
			//TODO binary search does not work for range intersection when there are overlaping ranges, need interval search tree for this
			var startIndex = 0;
			if (!(0 <= startIndex && startIndex < annotations.length)) { return; }
			var e = {
				type: "Changed", //$NON-NLS-0$
				added: [],
				removed: [],
				changed: [],
				textModelChangedEvent: modelChangedEvent
			};
			var changeCount = addedCharCount - removedCharCount, i;
			for (i = startIndex; i < annotations.length; i++) {
				var annotation = annotations[i];
				if (annotation.start >= end) {
					annotation.start += changeCount;
					annotation.end += changeCount;
					e.changed.push(annotation);
				} else if (annotation.end <= start) {
					//nothing
				} else if (annotation.start < start && end < annotation.end) {
					annotation.end += changeCount;
					e.changed.push(annotation);
				} else {
					annotations.splice(i, 1);
					e.removed.push(annotation);
					i--;
				}
			}
			if (e.added.length > 0 || e.removed.length > 0 || e.changed.length > 0) {
				this.onChanged(e);
			}
		}
	};
	mEventTarget.EventTarget.addMixin(AnnotationModel.prototype);

	/**
	 * Constructs a new styler for annotations.
	 * 
	 * @param {orion.textview.TextView} view The styler view.
	 * @param {orion.textview.AnnotationModel} view The styler annotation model.
	 * 
	 * @class This object represents a styler for annotation attached to a text view.
	 * @name orion.textview.AnnotationStyler
	 * @borrows orion.textview.AnnotationTypeList#addAnnotationType as #addAnnotationType
	 * @borrows orion.textview.AnnotationTypeList#getAnnotationTypePriority as #getAnnotationTypePriority
	 * @borrows orion.textview.AnnotationTypeList#getAnnotationsByType as #getAnnotationsByType
	 * @borrows orion.textview.AnnotationTypeList#isAnnotationTypeVisible as #isAnnotationTypeVisible
	 * @borrows orion.textview.AnnotationTypeList#removeAnnotationType as #removeAnnotationType
	 */
	function AnnotationStyler (view, annotationModel) {
		this._view = view;
		this._annotationModel = annotationModel;
		var self = this;
		this._listener = {
			onDestroy: function(e) {
				self._onDestroy(e);
			},
			onLineStyle: function(e) {
				self._onLineStyle(e);
			},
			onChanged: function(e) {
				self._onAnnotationModelChanged(e);
			}
		};
		view.addEventListener("Destroy", this._listener.onDestroy); //$NON-NLS-0$
		view.addEventListener("LineStyle", this._listener.onLineStyle); //$NON-NLS-0$
		annotationModel.addEventListener("Changed", this._listener.onChanged); //$NON-NLS-0$
	}
	AnnotationStyler.prototype = /** @lends orion.textview.AnnotationStyler.prototype */ {
		/**
		 * Destroys the styler. 
		 * <p>
		 * Removes all listeners added by this styler.
		 * </p>
		 */
		destroy: function() {
			var view = this._view;
			if (view) {
				view.removeEventListener("Destroy", this._listener.onDestroy); //$NON-NLS-0$
				view.removeEventListener("LineStyle", this._listener.onLineStyle); //$NON-NLS-0$
				this.view = null;
			}
			var annotationModel = this._annotationModel;
			if (annotationModel) {
				annotationModel.removeEventListener("Changed", this._listener.onChanged); //$NON-NLS-0$
				annotationModel = null;
			}
		},
		_mergeStyle: function(result, style) {
			if (style) {
				if (!result) { result = {}; }
				if (result.styleClass && style.styleClass && result.styleClass !== style.styleClass) {
					result.styleClass += " " + style.styleClass; //$NON-NLS-0$
				} else {
					result.styleClass = style.styleClass;
				}
				var prop;
				if (style.style) {
					if (!result.style) { result.style  = {}; }
					for (prop in style.style) {
						if (!result.style[prop]) {
							result.style[prop] = style.style[prop];
						}
					}
				}
				if (style.attributes) {
					if (!result.attributes) { result.attributes  = {}; }
					for (prop in style.attributes) {
						if (!result.attributes[prop]) {
							result.attributes[prop] = style.attributes[prop];
						}
					}
				}
			}
			return result;
		},
		_mergeStyleRanges: function(ranges, styleRange) {
			if (!ranges) {
				ranges = [];
			}
			var mergedStyle, i;
			for (i=0; i<ranges.length && styleRange; i++) {
				var range = ranges[i];
				if (styleRange.end <= range.start) { break; }
				if (styleRange.start >= range.end) { continue; }
				mergedStyle = this._mergeStyle({}, range.style);
				mergedStyle = this._mergeStyle(mergedStyle, styleRange.style);
				var args = [];
				args.push(i, 1);
				if (styleRange.start < range.start) {
					args.push({start: styleRange.start, end: range.start, style: styleRange.style});
				}
				if (styleRange.start > range.start) {
					args.push({start: range.start, end: styleRange.start, style: range.style});
				}
				args.push({start: Math.max(range.start, styleRange.start), end: Math.min(range.end, styleRange.end), style: mergedStyle});
				if (styleRange.end < range.end) {
					args.push({start: styleRange.end, end: range.end, style: range.style});
				}
				if (styleRange.end > range.end) {
					styleRange = {start: range.end, end: styleRange.end, style: styleRange.style};
				} else {
					styleRange = null;
				}
				Array.prototype.splice.apply(ranges, args);
			}
			if (styleRange) {
				mergedStyle = this._mergeStyle({}, styleRange.style);
				ranges.splice(i, 0, {start: styleRange.start, end: styleRange.end, style: mergedStyle});
			}
			return ranges;
		},
		_onAnnotationModelChanged: function(e) {
			if (e.textModelChangedEvent) {
				return;
			}
			var view = this._view;
			if (!view) { return; }
			var self = this;
			var model = view.getModel();
			function redraw(changes) {
				for (var i = 0; i < changes.length; i++) {
					if (!self.isAnnotationTypeVisible(changes[i].type)) { continue; }
					var start = changes[i].start;
					var end = changes[i].end;
					if (model.getBaseModel) {
						start = model.mapOffset(start, true);
						end = model.mapOffset(end, true);
					}
					if (start !== -1 && end !== -1) {
						view.redrawRange(start, end);
					}
				}
			}
			redraw(e.added);
			redraw(e.removed);
			redraw(e.changed);
		},
		_onDestroy: function(e) {
			this.destroy();
		},
		_onLineStyle: function (e) {
			var annotationModel = this._annotationModel;
			var viewModel = e.textView.getModel();
			var baseModel = annotationModel.getTextModel();
			var start = e.lineStart;
			var end = e.lineStart + e.lineText.length;
			if (baseModel !== viewModel) {
				start = viewModel.mapOffset(start);
				end = viewModel.mapOffset(end);
			}
			var annotations = annotationModel.getAnnotations(start, end);
			while (annotations.hasNext()) {
				var annotation = annotations.next();
				if (!this.isAnnotationTypeVisible(annotation.type)) { continue; }
				if (annotation.rangeStyle) {
					var annotationStart = annotation.start;
					var annotationEnd = annotation.end;
					if (baseModel !== viewModel) {
						annotationStart = viewModel.mapOffset(annotationStart, true);
						annotationEnd = viewModel.mapOffset(annotationEnd, true);
					}
					e.ranges = this._mergeStyleRanges(e.ranges, {start: annotationStart, end: annotationEnd, style: annotation.rangeStyle});
				}
				if (annotation.lineStyle) {
					e.style = this._mergeStyle({}, e.style);
					e.style = this._mergeStyle(e.style, annotation.lineStyle);
				}
			}
		}
	};
	AnnotationTypeList.addMixin(AnnotationStyler.prototype);
	
	return {
		FoldingAnnotation: FoldingAnnotation,
		AnnotationType: AnnotationType,
		AnnotationTypeList: AnnotationTypeList,
		AnnotationModel: AnnotationModel,
		AnnotationStyler: AnnotationStyler
	};
});
