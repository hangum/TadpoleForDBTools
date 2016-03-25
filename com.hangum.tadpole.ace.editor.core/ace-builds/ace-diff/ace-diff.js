(function(root, factory) {
  if (typeof define === 'function' && define.amd) {
    define([], factory);
  } else if (typeof exports === 'object') {
    module.exports = factory(require());
  } else {
    root.AceDiff = factory(root);
  }
}(this, function() {
  'use strict';

  var Range = require('ace/range').Range;

  var C = {
    DIFF_EQUAL: 0,
    DIFF_DELETE: -1,
    DIFF_INSERT: 1,
    EDITOR_RIGHT: 'right',
    EDITOR_LEFT: 'left',
    RTL: 'rtl',
    LTR: 'ltr',
    SVG_NS: 'http://www.w3.org/2000/svg',
    DIFF_GRANULARITY_SPECIFIC: 'specific',
    DIFF_GRANULARITY_BROAD: 'broad'
  };

  // our constructor
  function AceDiff(options) {
    this.options = {};

    extend(true, this.options, {
      mode: null,
      theme: null,
      diffGranularity: C.DIFF_GRANULARITY_BROAD,
      lockScrolling: false, // not implemented yet
      showDiffs: true,
      showConnectors: true,
      maxDiffs: 5000,
      left: {
        id: 'acediff-left-editor',
        content: null,
        mode: null,
        theme: null,
        editable: true,
        copyLinkEnabled: true
      },
      right: {
        id: 'acediff-right-editor',
        content: null,
        mode: null,
        theme: null,
        editable: true,
        copyLinkEnabled: true
      },
      classes: {
        gutterID: 'acediff-gutter',
        diff: 'acediff-diff',
        connector: 'acediff-connector',
        newCodeConnectorLink: 'acediff-new-code-connector-copy',
        newCodeConnectorLinkContent: '&#8594;',
        deletedCodeConnectorLink: 'acediff-deleted-code-connector-copy',
        deletedCodeConnectorLinkContent: '&#8592;',
        copyRightContainer: 'acediff-copy-right',
        copyLeftContainer: 'acediff-copy-left'
      },
      connectorYOffset: 0
    }, options);

    // instantiate the editors in an internal data structure that will store a little info about the diffs and
    // editor content
    this.editors = {
      left: {
        ace: ace.edit(this.options.left.id),
        markers: [],
        lineLengths: []
      },
      right: {
        ace: ace.edit(this.options.right.id),
        markers: [],
        lineLengths: []
      },
      editorHeight: null
    };

    addEventHandlers(this);

    this.lineHeight = this.editors.left.ace.renderer.lineHeight; // assumption: both editors have same line heights

    // set up the editors
    this.editors.left.ace.getSession().setMode(getMode(this, C.EDITOR_LEFT));
    this.editors.right.ace.getSession().setMode(getMode(this, C.EDITOR_RIGHT));
    this.editors.left.ace.setReadOnly(!this.options.left.editable);
    this.editors.right.ace.setReadOnly(!this.options.right.editable);
    this.editors.left.ace.setTheme(getTheme(this, C.EDITOR_LEFT));
    this.editors.right.ace.setTheme(getTheme(this, C.EDITOR_RIGHT));

    createCopyContainers(this);
    createGutter(this);

    // if the data is being supplied by an option, set the editor values now
    if (this.options.left.content) {
      this.editors.left.ace.setValue(this.options.left.content, -1);
    }
    if (this.options.right.content) {
      this.editors.right.ace.setValue(this.options.right.content, -1);
    }

    // store the visible height of the editors (assumed the same)
    this.editors.editorHeight = getEditorHeight(this);

    this.diff();
  }


  // our public API
  AceDiff.prototype = {

    // allows on-the-fly changes to the AceDiff instance settings
    setOptions: function(options) {
      extend(true, this.options, options);
      this.diff();
    },

    getNumDiffs: function() {
      return this.diffs.length;
    },

    // exposes the Ace editors in case the dev needs it
    getEditors: function() {
      return {
        left: this.editors.left.ace,
        right: this.editors.right.ace
      }
    },

    // our main diffing function. I actually don't think this needs to exposed: it's called automatically,
    // but just to be safe, it's included
    diff: function() {
      var dmp = new diff_match_patch();
      var val1 = this.editors.left.ace.getSession().getValue();
      var val2 = this.editors.right.ace.getSession().getValue();
      var diff = dmp.diff_main(val2, val1);
      dmp.diff_cleanupSemantic(diff);

      this.editors.left.lineLengths  = getLineLengths(this.editors.left);
      this.editors.right.lineLengths = getLineLengths(this.editors.right);

      // parse the raw diff into something a little more palatable
      var diffs = [];
      var offset = {
        left: 0,
        right: 0
      };

      diff.forEach(function(chunk) {
        var chunkType = chunk[0];
        var text = chunk[1];

        // oddly, occasionally the algorithm returns a diff with no changes made
        if (text.length === 0) {
          return;
        }
        if (chunkType === C.DIFF_EQUAL) {
          offset.left += text.length;
          offset.right += text.length;
        } else if (chunkType === C.DIFF_DELETE) {
          diffs.push(computeDiff(this, C.DIFF_DELETE, offset.left, offset.right, text));
          offset.right += text.length;

        } else if (chunkType === C.DIFF_INSERT) {
          diffs.push(computeDiff(this, C.DIFF_INSERT, offset.left, offset.right, text));
          offset.left += text.length;
        }
      }, this);

      // simplify our computed diffs; this groups together multiple diffs on subsequent lines
      this.diffs = simplifyDiffs(this, diffs);

      // if we're dealing with too many diffs, fail silently
      if (this.diffs.length > this.options.maxDiffs) {
        return;
      }

      clearDiffs(this);
      decorate(this);
    },

    destroy: function() {

      // destroy the two editors
      var leftValue = this.editors.left.ace.getValue();
      this.editors.left.ace.destroy();
      var oldDiv = this.editors.left.ace.container;
      var newDiv = oldDiv.cloneNode(false);
      newDiv.textContent = leftValue;
      oldDiv.parentNode.replaceChild(newDiv, oldDiv);

      var rightValue = this.editors.right.ace.getValue();
      this.editors.right.ace.destroy();
      oldDiv = this.editors.right.ace.container;
      newDiv = oldDiv.cloneNode(false);
      newDiv.textContent = rightValue;
      oldDiv.parentNode.replaceChild(newDiv, oldDiv);

      document.getElementById(this.options.classes.gutterID).innerHTML = '';
    }
  };


  function getMode(acediff, editor) {
    var mode = acediff.options.mode;
    if (editor === C.EDITOR_LEFT && acediff.options.left.mode !== null) {
      mode = acediff.options.left.mode;
    }
    if (editor === C.EDITOR_RIGHT && acediff.options.right.mode !== null) {
      mode = acediff.options.right.mode;
    }
    return mode;
  }


  function getTheme(acediff, editor) {
    var theme = acediff.options.theme;
    if (editor === C.EDITOR_LEFT && acediff.options.left.theme !== null) {
      theme = acediff.options.left.theme;
    }
    if (editor === C.EDITOR_RIGHT && acediff.options.right.theme !== null) {
      theme = acediff.options.right.theme;
    }
    return theme;
  }


  function addEventHandlers(acediff) {
    var leftLastScrollTime = new Date().getTime(),
        rightLastScrollTime = new Date().getTime(),
        now;

    acediff.editors.left.ace.getSession().on('changeScrollTop', function(scroll) {
      now = new Date().getTime();
      if (rightLastScrollTime + 50 < now) {
        updateGap(acediff, 'left', scroll);
      }
    });

    acediff.editors.right.ace.getSession().on('changeScrollTop', function(scroll) {
      now = new Date().getTime();
      if (leftLastScrollTime + 50 < now) {
        updateGap(acediff, 'right', scroll);
      }
    });

    var diff = acediff.diff.bind(acediff);
    acediff.editors.left.ace.on('change', diff);
    acediff.editors.right.ace.on('change', diff);

    if (acediff.options.left.copyLinkEnabled) {
      on('#' + acediff.options.classes.gutterID, 'click', '.' + acediff.options.classes.newCodeConnectorLink, function(e) {
        copy(acediff, e, C.LTR);
      });
    }
    if (acediff.options.right.copyLinkEnabled) {
      on('#' + acediff.options.classes.gutterID, 'click', '.' + acediff.options.classes.deletedCodeConnectorLink, function(e) {
        copy(acediff, e, C.RTL);
      });
    }

    var onResize = debounce(function() {
      acediff.editors.availableHeight = document.getElementById(acediff.options.left.id).offsetHeight;

      // TODO this should re-init gutter
      acediff.diff();
    }, 250);

    window.addEventListener('resize', onResize);
  }


  function copy(acediff, e, dir) {
    var diffIndex = parseInt(e.target.getAttribute('data-diff-index'), 10);
    var diff = acediff.diffs[diffIndex];
    var sourceEditor, targetEditor;

    var startLine, endLine, targetStartLine, targetEndLine;
    if (dir === C.LTR) {
      sourceEditor = acediff.editors.left;
      targetEditor = acediff.editors.right;
      startLine = diff.leftStartLine;
      endLine = diff.leftEndLine;
      targetStartLine = diff.rightStartLine;
      targetEndLine = diff.rightEndLine;
    } else {
      sourceEditor = acediff.editors.right;
      targetEditor = acediff.editors.left;
      startLine = diff.rightStartLine;
      endLine = diff.rightEndLine;
      targetStartLine = diff.leftStartLine;
      targetEndLine = diff.leftEndLine;
    }

    var contentToInsert = '';
    for (var i=startLine; i<endLine; i++) {
      contentToInsert += getLine(sourceEditor, i) + '\n';
    }

    var startContent = '';
    for (var i=0; i<targetStartLine; i++) {
      startContent += getLine(targetEditor, i) + '\n';
    }

    var endContent = '';
    var totalLines = targetEditor.ace.getSession().getLength();
    for (var i=targetEndLine; i<totalLines; i++) {
      endContent += getLine(targetEditor, i);
      if (i<totalLines-1) {
        endContent += '\n';
      }
    }

    endContent = endContent.replace(/\s*$/, '');

    // keep track of the scroll height
    var h = targetEditor.ace.getSession().getScrollTop();
    targetEditor.ace.getSession().setValue(startContent + contentToInsert + endContent);
    targetEditor.ace.getSession().setScrollTop(parseInt(h));

    acediff.diff();
  }


  function getLineLengths(editor) {
    var lines = editor.ace.getSession().doc.getAllLines();
    var lineLengths = [];
    lines.forEach(function(line) {
      lineLengths.push(line.length + 1); // +1 for the newline char
    });
    return lineLengths;
  }


  // shows a diff in one of the two editors.
  function showDiff(acediff, editor, startLine, endLine, className) {
    var editor = acediff.editors[editor];

    if (endLine < startLine) { // can this occur? Just in case.
      endLine = startLine;
    }

    var classNames = className + ' ' + ((endLine > startLine) ? 'lines' : 'targetOnly');
    endLine--; // because endLine is always + 1

    // to get Ace to highlight the full row we just set the start and end chars to 0 and 1
    editor.markers.push(editor.ace.session.addMarker(new Range(startLine, 0, endLine, 1), classNames, 'fullLine'));
  }


  // called onscroll. Updates the gap to ensure the connectors are all lining up
  function updateGap(acediff, editor, scroll) {

    clearDiffs(acediff);
    decorate(acediff);

    // reposition the copy containers containing all the arrows
    positionCopyContainers(acediff);
  }


  function clearDiffs(acediff) {
    acediff.editors.left.markers.forEach(function(marker) {
      this.editors.left.ace.getSession().removeMarker(marker);
    }, acediff);
    acediff.editors.right.markers.forEach(function(marker) {
      this.editors.right.ace.getSession().removeMarker(marker);
    }, acediff);
  }


  function addConnector(acediff, leftStartLine, leftEndLine, rightStartLine, rightEndLine) {
    var leftScrollTop  = acediff.editors.left.ace.getSession().getScrollTop();
    var rightScrollTop = acediff.editors.right.ace.getSession().getScrollTop();

    // All connectors, regardless of ltr or rtl have the same point system, even if p1 === p3 or p2 === p4
    //  p1   p2
    //
    //  p3   p4

    acediff.connectorYOffset = 1;

    var p1_x = -1;
    var p1_y = (leftStartLine * acediff.lineHeight) - leftScrollTop + 0.5;
    var p2_x = acediff.gutterWidth + 1;
    var p2_y = rightStartLine * acediff.lineHeight - rightScrollTop + 0.5;
    var p3_x = -1;
    var p3_y = (leftEndLine * acediff.lineHeight) - leftScrollTop + acediff.connectorYOffset + 0.5;
    var p4_x = acediff.gutterWidth + 1;
    var p4_y = (rightEndLine * acediff.lineHeight) - rightScrollTop + acediff.connectorYOffset + 0.5;
    var curve1 = getCurve(p1_x, p1_y, p2_x, p2_y);
    var curve2 = getCurve(p4_x, p4_y, p3_x, p3_y);

    var verticalLine1 = 'L' + p2_x + ',' + p2_y + ' ' + p4_x + ',' + p4_y;
    var verticalLine2 = 'L' + p3_x + ',' + p3_y + ' ' + p1_x + ',' + p1_y;
    var d = curve1 + ' ' + verticalLine1 + ' ' + curve2 + ' ' + verticalLine2;

    var el = document.createElementNS(C.SVG_NS, 'path');
    el.setAttribute('d', d);
    el.setAttribute('class', acediff.options.classes.connector);
    acediff.gutterSVG.appendChild(el);
  }


  function addCopyArrows(acediff, info, diffIndex) {
    if (info.leftEndLine > info.leftStartLine && acediff.options.left.copyLinkEnabled) {
      var arrow = createArrow({
        className: acediff.options.classes.newCodeConnectorLink,
        topOffset: info.leftStartLine * acediff.lineHeight,
        tooltip: 'Copy to right',
        diffIndex: diffIndex,
        arrowContent: acediff.options.classes.newCodeConnectorLinkContent
      });
      acediff.copyRightContainer.appendChild(arrow);
    }

    if (info.rightEndLine > info.rightStartLine && acediff.options.right.copyLinkEnabled) {
      var arrow = createArrow({
        className: acediff.options.classes.deletedCodeConnectorLink,
        topOffset: info.rightStartLine * acediff.lineHeight,
        tooltip: 'Copy to left',
        diffIndex: diffIndex,
        arrowContent: acediff.options.classes.deletedCodeConnectorLinkContent
      });
      acediff.copyLeftContainer.appendChild(arrow);
    }
  }


  function positionCopyContainers(acediff) {
    var leftTopOffset = acediff.editors.left.ace.getSession().getScrollTop();
    var rightTopOffset = acediff.editors.right.ace.getSession().getScrollTop();

    acediff.copyRightContainer.style.cssText = 'top: ' + (-leftTopOffset) + 'px';
    acediff.copyLeftContainer.style.cssText = 'top: ' + (-rightTopOffset) + 'px';
  }


  /**
   * This method takes the raw diffing info from the Google lib and returns a nice clean object of the following
   * form:
   * {
   *   leftStartLine:
   *   leftEndLine:
   *   rightStartLine:
   *   rightEndLine:
   * }
   *
   * Ultimately, that's all the info we need to highlight the appropriate lines in the left + right editor, add the
   * SVG connectors, and include the appropriate <<, >> arrows.
   *
   * Note: leftEndLine and rightEndLine are always the start of the NEXT line, so for a single line diff, there will
   * be 1 separating the startLine and endLine values. So if leftStartLine === leftEndLine or rightStartLine ===
   * rightEndLine, it means that new content from the other editor is being inserted and a single 1px line will be
   * drawn.
   */
  function computeDiff(acediff, diffType, offsetLeft, offsetRight, diffText) {
    var lineInfo = {};

    // this was added in to hack around an oddity with the Google lib. Sometimes it would include a newline
    // as the first char for a diff, other times not - and it would change when you were typing on-the-fly. This
    // is used to level things out so the diffs don't appear to shift around
    var newContentStartsWithNewline = /^\n/.test(diffText);

    if (diffType === C.DIFF_INSERT) {

      // pretty confident this returns the right stuff for the left editor: start & end line & char
      var info = getSingleDiffInfo(acediff.editors.left, offsetLeft, diffText);

      // this is the ACTUAL undoctored current line in the other editor. It's always right. Doesn't mean it's
      // going to be used as the start line for the diff though.
      var currentLineOtherEditor = getLineForCharPosition(acediff.editors.right, offsetRight);
      var numCharsOnLineOtherEditor = getCharsOnLine(acediff.editors.right, currentLineOtherEditor);
      var numCharsOnLeftEditorStartLine = getCharsOnLine(acediff.editors.left, info.startLine);
      var numCharsOnLine = getCharsOnLine(acediff.editors.left, info.startLine);

      // this is necessary because if a new diff starts on the FIRST char of the left editor, the diff can comes
      // back from google as being on the last char of the previous line so we need to bump it up one
      var rightStartLine = currentLineOtherEditor;
      if (numCharsOnLine === 0 && newContentStartsWithNewline) {
        newContentStartsWithNewline = false;
      }
      if (info.startChar === 0 && isLastChar(acediff.editors.right, offsetRight, newContentStartsWithNewline)) {
        rightStartLine = currentLineOtherEditor + 1;
      }

      var sameLineInsert = info.startLine === info.endLine;

      // whether or not this diff is a plain INSERT into the other editor, or overwrites a line take a little work to
      // figure out. This feels like the hardest part of the entire script.
      var numRows = 0;
      if (

        // dense, but this accommodates two scenarios:
        // 1. where a completely fresh new line is being inserted in left editor, we want the line on right to stay a 1px line
        // 2. where a new character is inserted at the start of a newline on the left but the line contains other stuff,
        //    we DO want to make it a full line
        (info.startChar > 0 || (sameLineInsert && diffText.length < numCharsOnLeftEditorStartLine)) &&

        // if the right editor line was empty, it's ALWAYS a single line insert [not an OR above?]
        numCharsOnLineOtherEditor > 0 &&

        // if the text being inserted starts mid-line
        (info.startChar < numCharsOnLeftEditorStartLine)) {
        numRows++;
      }

      lineInfo = {
        leftStartLine: info.startLine,
        leftEndLine: info.endLine + 1,
        rightStartLine: rightStartLine,
        rightEndLine: rightStartLine + numRows
      };

    } else {
      var info = getSingleDiffInfo(acediff.editors.right, offsetRight, diffText);

      var currentLineOtherEditor = getLineForCharPosition(acediff.editors.left, offsetLeft);
      var numCharsOnLineOtherEditor = getCharsOnLine(acediff.editors.left, currentLineOtherEditor);
      var numCharsOnRightEditorStartLine = getCharsOnLine(acediff.editors.right, info.startLine);
      var numCharsOnLine = getCharsOnLine(acediff.editors.right, info.startLine);

      // this is necessary because if a new diff starts on the FIRST char of the left editor, the diff can comes
      // back from google as being on the last char of the previous line so we need to bump it up one
      var leftStartLine = currentLineOtherEditor;
      if (numCharsOnLine === 0 && newContentStartsWithNewline) {
        newContentStartsWithNewline = false;
      }
      if (info.startChar === 0 && isLastChar(acediff.editors.left, offsetLeft, newContentStartsWithNewline)) {
        leftStartLine = currentLineOtherEditor + 1;
      }

      var sameLineInsert = info.startLine === info.endLine;
      var numRows = 0;
      if (

        // dense, but this accommodates two scenarios:
        // 1. where a completely fresh new line is being inserted in left editor, we want the line on right to stay a 1px line
        // 2. where a new character is inserted at the start of a newline on the left but the line contains other stuff,
        //    we DO want to make it a full line
        (info.startChar > 0 || (sameLineInsert && diffText.length < numCharsOnRightEditorStartLine)) &&

        // if the right editor line was empty, it's ALWAYS a single line insert [not an OR above?]
        numCharsOnLineOtherEditor > 0 &&

        // if the text being inserted starts mid-line
        (info.startChar < numCharsOnRightEditorStartLine)) {
          numRows++;
      }

      lineInfo = {
        leftStartLine: leftStartLine,
        leftEndLine: leftStartLine + numRows,
        rightStartLine: info.startLine,
        rightEndLine: info.endLine + 1
      };
    }

    return lineInfo;
  }


  // helper to return the startline, endline, startChar and endChar for a diff in a particular editor. Pretty
  // fussy function
  function getSingleDiffInfo(editor, offset, diffString) {
    var info = {
      startLine: 0,
      startChar: 0,
      endLine: 0,
      endChar: 0
    };
    var endCharNum = offset + diffString.length;
    var runningTotal = 0;
    var startLineSet = false,
        endLineSet = false;

    editor.lineLengths.forEach(function(lineLength, lineIndex) {
      runningTotal += lineLength;

      if (!startLineSet && offset < runningTotal) {
        info.startLine = lineIndex;
        info.startChar = offset - runningTotal + lineLength;
        startLineSet = true;
      }

      if (!endLineSet && endCharNum <= runningTotal) {
        info.endLine = lineIndex;
        info.endChar = endCharNum - runningTotal + lineLength;
        endLineSet = true;
      }
    });

    // if the start char is the final char on the line, it's a newline & we ignore it
    if (info.startChar > 0 && getCharsOnLine(editor, info.startLine) === info.startChar) {
      info.startLine++;
      info.startChar = 0;
    }

    // if the end char is the first char on the line, we don't want to highlight that extra line
    if (info.endChar === 0) {
      info.endLine--;
    }

    var endsWithNewline = /\n$/.test(diffString);
    if (info.startChar > 0 && endsWithNewline) {
      info.endLine++;
    }

    return info;
  }


  // note that this and everything else in this script uses 0-indexed row numbers
  function getCharsOnLine(editor, line) {
    return getLine(editor, line).length;
  }


  function getLine(editor, line) {
    return editor.ace.getSession().doc.getLine(line);
  }


  function getLineForCharPosition(editor, offsetChars) {
    var lines = editor.ace.getSession().doc.getAllLines(),
        foundLine = 0,
        runningTotal = 0;

    for (var i=0; i<lines.length; i++) {
      runningTotal += lines[i].length + 1; // +1 needed for newline char
      if (offsetChars <= runningTotal) {
        foundLine = i;
        break;
      }
    }
    return foundLine;
  }


  function isLastChar(editor, char, startsWithNewline) {
    var lines = editor.ace.getSession().doc.getAllLines(),
        runningTotal = 0,
        isLastChar = false;

    for (var i=0; i<lines.length; i++) {
      runningTotal += lines[i].length + 1; // +1 needed for newline char
      var comparison = runningTotal;
      if (startsWithNewline) {
        comparison--;
      }

      if (char === comparison) {
        isLastChar = true;
        break;
      }
    }
    return isLastChar;
  }


  function createArrow(info) {
    var el = document.createElement('div');
    var props = {
      'class': info.className,
      'style': 'top:' + info.topOffset + 'px',
      title: info.tooltip,
      'data-diff-index': info.diffIndex
    };
    for (var key in props) {
      el.setAttribute(key, props[key]);
    }
    el.innerHTML = info.arrowContent;
    return el;
  }


  function createGutter(acediff) {
    acediff.gutterHeight = document.getElementById(acediff.options.classes.gutterID).clientHeight;
    acediff.gutterWidth = document.getElementById(acediff.options.classes.gutterID).clientWidth;

    var leftHeight = getTotalHeight(acediff, C.EDITOR_LEFT);
    var rightHeight = getTotalHeight(acediff, C.EDITOR_RIGHT);
    var height = Math.max(leftHeight, rightHeight, acediff.gutterHeight);

    acediff.gutterSVG = document.createElementNS(C.SVG_NS, 'svg');
    acediff.gutterSVG.setAttribute('width', acediff.gutterWidth);
    acediff.gutterSVG.setAttribute('height', height);

    document.getElementById(acediff.options.classes.gutterID).appendChild(acediff.gutterSVG);
  }

  // acediff.editors.left.ace.getSession().getLength() * acediff.lineHeight
  function getTotalHeight(acediff, editor) {
    var ed = (editor === C.EDITOR_LEFT) ? acediff.editors.left : acediff.editors.right;
    return ed.ace.getSession().getLength() * acediff.lineHeight;
  }

  // creates two contains for positioning the copy left + copy right arrows
  function createCopyContainers(acediff) {
    acediff.copyRightContainer = document.createElement('div');
    acediff.copyRightContainer.setAttribute('class', acediff.options.classes.copyRightContainer);
    acediff.copyLeftContainer = document.createElement('div');
    acediff.copyLeftContainer.setAttribute('class', acediff.options.classes.copyLeftContainer);

    document.getElementById(acediff.options.classes.gutterID).appendChild(acediff.copyRightContainer);
    document.getElementById(acediff.options.classes.gutterID).appendChild(acediff.copyLeftContainer);
  }


  function clearGutter(acediff) {
    //gutter.innerHTML = '';

    var gutterEl  = document.getElementById(acediff.options.classes.gutterID);
    gutterEl.removeChild(acediff.gutterSVG);

    createGutter(acediff);
  }


  function clearArrows(acediff) {
    acediff.copyLeftContainer.innerHTML = '';
    acediff.copyRightContainer.innerHTML = '';
  }


  /*
   * This combines multiple rows where, say, line 1 => line 1, line 2 => line 2, line 3-4 => line 3. That could be
   * reduced to a single connector line 1=4 => line 1-3
   */
  function simplifyDiffs(acediff, diffs) {
    var groupedDiffs = [];

    function compare(val) {
      return (acediff.options.diffGranularity === C.DIFF_GRANULARITY_SPECIFIC) ? val < 1 : val <= 1;
    }

    diffs.forEach(function(diff, index) {
      if (index === 0) {
        groupedDiffs.push(diff);
        return;
      }

      // loop through all grouped diffs. If this new diff lies between an existing one, we'll just add to it, rather
      // than create a new one
      var isGrouped = false;
      for (var i=0; i<groupedDiffs.length; i++) {
        if (compare(Math.abs(diff.leftStartLine - groupedDiffs[i].leftEndLine)) &&
            compare(Math.abs(diff.rightStartLine - groupedDiffs[i].rightEndLine))) {

          // update the existing grouped diff to expand its horizons to include this new diff start + end lines
          groupedDiffs[i].leftStartLine = Math.min(diff.leftStartLine, groupedDiffs[i].leftStartLine);
          groupedDiffs[i].rightStartLine = Math.min(diff.rightStartLine, groupedDiffs[i].rightStartLine);
          groupedDiffs[i].leftEndLine = Math.max(diff.leftEndLine, groupedDiffs[i].leftEndLine);
          groupedDiffs[i].rightEndLine = Math.max(diff.rightEndLine, groupedDiffs[i].rightEndLine);
          isGrouped = true;
          break;
        }
      }

      if (!isGrouped) {
        groupedDiffs.push(diff);
      }
    });

    // clear out any single line diffs (i.e. single line on both editors)
    var fullDiffs = [];
    groupedDiffs.forEach(function(diff) {
      if (diff.leftStartLine === diff.leftEndLine && diff.rightStartLine === diff.rightEndLine) {
        return;
      }
      fullDiffs.push(diff);
    });

    return fullDiffs;
  }


  function decorate(acediff) {
    clearGutter(acediff);
    clearArrows(acediff);

    acediff.diffs.forEach(function(info, diffIndex) {
      if (this.options.showDiffs) {
        showDiff(this, C.EDITOR_LEFT, info.leftStartLine, info.leftEndLine, this.options.classes.diff);
        showDiff(this, C.EDITOR_RIGHT, info.rightStartLine, info.rightEndLine, this.options.classes.diff);

        if (this.options.showConnectors) {
          addConnector(this, info.leftStartLine, info.leftEndLine, info.rightStartLine, info.rightEndLine);
        }
        addCopyArrows(this, info, diffIndex);
      }
    }, acediff);
  }


  function extend() {
    var options, name, src, copy, copyIsArray, clone, target = arguments[0] || {},
      i = 1,
      length = arguments.length,
      deep = false,
      toString = Object.prototype.toString,
      hasOwn = Object.prototype.hasOwnProperty,
      class2type = {
        "[object Boolean]": "boolean",
        "[object Number]": "number",
        "[object String]": "string",
        "[object Function]": "function",
        "[object Array]": "array",
        "[object Date]": "date",
        "[object RegExp]": "regexp",
        "[object Object]": "object"
      },

      jQuery = {
        isFunction: function(obj) {
          return jQuery.type(obj) === "function";
        },
        isArray: Array.isArray ||
        function(obj) {
          return jQuery.type(obj) === "array";
        },
        isWindow: function(obj) {
          return obj !== null && obj === obj.window;
        },
        isNumeric: function(obj) {
          return !isNaN(parseFloat(obj)) && isFinite(obj);
        },
        type: function(obj) {
          return obj === null ? String(obj) : class2type[toString.call(obj)] || "object";
        },
        isPlainObject: function(obj) {
          if (!obj || jQuery.type(obj) !== "object" || obj.nodeType) {
            return false;
          }
          try {
            if (obj.constructor && !hasOwn.call(obj, "constructor") && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
              return false;
            }
          } catch (e) {
            return false;
          }
          var key;
          for (key in obj) {}
          return key === undefined || hasOwn.call(obj, key);
        }
      };
    if (typeof target === "boolean") {
      deep = target;
      target = arguments[1] || {};
      i = 2;
    }
    if (typeof target !== "object" && !jQuery.isFunction(target)) {
      target = {};
    }
    if (length === i) {
      target = this;
      --i;
    }
    for (i; i < length; i++) {
      if ((options = arguments[i]) !== null) {
        for (name in options) {
          src = target[name];
          copy = options[name];
          if (target === copy) {
            continue;
          }
          if (deep && copy && (jQuery.isPlainObject(copy) || (copyIsArray = jQuery.isArray(copy)))) {
            if (copyIsArray) {
              copyIsArray = false;
              clone = src && jQuery.isArray(src) ? src : [];
            } else {
              clone = src && jQuery.isPlainObject(src) ? src : {};
            }
            // WARNING: RECURSION
            target[name] = extend(deep, clone, copy);
          } else if (copy !== undefined) {
            target[name] = copy;
          }
        }
      }
    }

    return target;
  }


  function getScrollingInfo(acediff, dir) {
    return (dir == C.EDITOR_LEFT) ? acediff.editors.left.ace.getSession().getScrollTop() : acediff.editors.right.ace.getSession().getScrollTop();
  }


  function getEditorHeight(acediff) {
    //editorHeight: document.getElementById(acediff.options.left.id).clientHeight
    return document.getElementById(acediff.options.left.id).offsetHeight;
  }

  // generates a Bezier curve in SVG format
  function getCurve(startX, startY, endX, endY) {
    var w = endX - startX;
    var halfWidth = startX + (w / 2);

    // position it at the initial x,y coords
    var curve = 'M ' + startX + ' ' + startY +

      // now create the curve. This is of the form "C M,N O,P Q,R" where C is a directive for SVG ("curveto"),
      // M,N are the first curve control point, O,P the second control point and Q,R are the final coords
      ' C ' + halfWidth + ',' + startY + ' ' + halfWidth + ',' + endY + ' ' + endX + ',' + endY;

    return curve;
  }


  function on(elSelector, eventName, selector, fn) {
    var element = (elSelector === 'document') ? document : document.querySelector(elSelector);

    element.addEventListener(eventName, function(event) {
      var possibleTargets = element.querySelectorAll(selector);
      var target = event.target;

      for (var i = 0, l = possibleTargets.length; i < l; i++) {
        var el = target;
        var p = possibleTargets[i];

        while(el && el !== element) {
          if (el === p) {
            return fn.call(p, event);
          }
          el = el.parentNode;
        }
      }
    });
  }


  function debounce(func, wait, immediate) {
    var timeout;
    return function() {
      var context = this, args = arguments;
      var later = function() {
        timeout = null;
        if (!immediate) func.apply(context, args);
      };
      var callNow = immediate && !timeout;
      clearTimeout(timeout);
      timeout = setTimeout(later, wait);
      if (callNow) func.apply(context, args);
    };
  }

  return AceDiff;

}));
