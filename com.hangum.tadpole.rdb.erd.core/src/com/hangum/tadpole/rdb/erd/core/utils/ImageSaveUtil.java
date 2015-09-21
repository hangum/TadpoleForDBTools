/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.utils;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 *
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 15.
 *
 */
public class ImageSaveUtil {

//	public static boolean save(IEditorPart editorPart, GraphicalViewer viewer, String saveFilePath, int format) {
//		Assert.isNotNull(editorPart, "null editorPart passed to ImageSaveUtil::save");
//		Assert.isNotNull(viewer, "null viewer passed to ImageSaveUtil::save");
//		Assert.isNotNull(saveFilePath, "null saveFilePath passed to ImageSaveUtil::save");
//
//		if (format != SWT.IMAGE_BMP && format != SWT.IMAGE_JPEG && format != SWT.IMAGE_ICO)
//			throw new IllegalArgumentException("Save format not supported");
//
//		try {
//			saveEditorContentsAsImage(editorPart, viewer, saveFilePath, format);
//		} catch (Exception ex) {
//			MessageDialog.openError(editorPart.getEditorSite().getShell(), "Save Error",
//					"Could not save editor contents");
//			return false;
//		}
//
//		return true;
//	}

//	public static boolean save(IEditorPart editorPart, GraphicalViewer viewer) {
//		Assert.isNotNull(editorPart, "null editorPart passed to ImageSaveUtil::save");
//		Assert.isNotNull(viewer, "null viewer passed to ImageSaveUtil::save");
//
//		String saveFilePath = "/Users/hangum/Downloads/gefout.png";// getSaveFilePath(editorPart,
//																	// viewer,
//																	// -1);
//		if (saveFilePath == null)
//			return false;
//
//		int format = SWT.IMAGE_JPEG;
//		if (saveFilePath.endsWith(".jpeg"))
//			format = SWT.IMAGE_JPEG;
//		else if (saveFilePath.endsWith(".bmp"))
//			format = SWT.IMAGE_BMP;
//		else if (saveFilePath.endsWith(".ico"))
//			format = SWT.IMAGE_ICO;
//
//		return save(editorPart, viewer, saveFilePath, format);
//	}

	// private static String getSaveFilePath(IEditorPart editorPart,
	// GraphicalViewer viewer, int format)
	// {
	// FileDialog fileDialog = new
	// FileDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);
	//
	// String[] filterExtensions = new String[] {"*.jpeg", "*.bmp", "*.ico"/*,
	// "*.png", "*.gif"*/};
	// if( format == SWT.IMAGE_BMP )
	// filterExtensions = new String[] {"*.bmp"};
	// else if( format == SWT.IMAGE_JPEG )
	// filterExtensions = new String[] {"*.jpeg"};
	// else if( format == SWT.IMAGE_ICO )
	// filterExtensions = new String[] {"*.ico"};
	// fileDialog.setFilterExtensions(filterExtensions);
	//
	// return fileDialog.open();
	// }

	public static void saveEditorContentsAsImage( GraphicalViewer viewer, String saveFilePath,
			int format) {
		/*
		 * 1. First get the figure whose visuals we want to save as image. So we
		 * would like to save the rooteditpart which actually hosts all the
		 * printable layers.
		 * 
		 * NOTE: ScalableRootEditPart manages layers and is registered
		 * graphicalviewer's editpartregistry with the key LayerManager.ID ...
		 * well that is because ScalableRootEditPart manages all layers that are
		 * hosted on a FigureCanvas. Many layers exist for doing different
		 * things
		 */
		ScalableRootEditPart rootEditPart = (ScalableRootEditPart) viewer.getEditPartRegistry().get(LayerManager.ID);
		IFigure rootFigure = ((LayerManager) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);// rootEditPart.getFigure();
		Rectangle rootFigureBounds = rootFigure.getBounds();

		/*
		 * 2. Now we want to get the GC associated with the control on which all
		 * figures are painted by SWTGraphics. For that first get the SWT
		 * Control associated with the viewer on which the rooteditpart is set
		 * as contents
		 */
		Control figureCanvas = viewer.getControl();
		GC figureCanvasGC = new GC(figureCanvas);

		/*
		 * 3. Create a new Graphics for an Image onto which we want to paint
		 * rootFigure
		 */
		Image img = new Image(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay(), rootFigureBounds.width, rootFigureBounds.height);
//		GC imageGC = new GC(img);
		GC imageGC = new GC(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay());
		imageGC.drawImage(img, rootFigureBounds.width, rootFigureBounds.height);
		
		imageGC.setBackground(figureCanvasGC.getBackground());
		imageGC.setForeground(figureCanvasGC.getForeground());
		imageGC.setFont(figureCanvasGC.getFont());
		// imageGC.setLineStyle(figureCanvasGC.getLineStyle());
		imageGC.setLineWidth(figureCanvasGC.getLineWidth());
		// imageGC.setXORMode(figureCanvasGC.getXORMode());
		Graphics imgGraphics = new SWTGraphics(imageGC);

		/*
		 * 4. Draw rootFigure onto image. After that image will be ready for
		 * save
		 */
		rootFigure.paint(imgGraphics);

		/* 5. Save image */
		ImageData[] imgData = new ImageData[1];
		imgData[0] = img.getImageData();

		ImageLoader imgLoader = new ImageLoader();
		imgLoader.data = imgData;
		imgLoader.save(saveFilePath, format);

		/* release OS resources */
		figureCanvasGC.dispose();
		imageGC.dispose();
		img.dispose();
	}

}
