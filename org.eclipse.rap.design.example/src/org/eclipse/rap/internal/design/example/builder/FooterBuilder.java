/*******************************************************************************
 * Copyright (c) 2009, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.design.example.builder;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.ui.interactiondesign.layout.ElementBuilder;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


public class FooterBuilder extends ElementBuilder {

  private Image left;
  private Image bg;
  private Image right;
  private Composite footerCenter;

  public FooterBuilder( Composite parent, String layoutSetId ) {
    super( parent, layoutSetId );
    initLayoutData();
  }

  private void initLayoutData() {
    LayoutSet set = getLayoutSet();
    left = createImage( set.getImagePath( ILayoutSetConstants.FOOTER_LEFT ) );
    bg = createImage( set.getImagePath( ILayoutSetConstants.FOOTER_BG ) );
    right = createImage( set.getImagePath( ILayoutSetConstants.FOOTER_RIGHT ) );
  }

  public void addControl( Control control, Object layoutData ) {
  }

  public void addControl( Control control, String positionId ) {
  }

  public void addImage( Image image, Object layoutData ) {
  }

  public void addImage( Image image, String positionId ) {
  }

  public void build() {
    getParent().setLayout( new FormLayout() );
    
    Label leftLabel = new Label( getParent(), SWT.NONE );
    leftLabel.setImage( left );
    FormData fdLeftLabel = new FormData();
    leftLabel.setLayoutData( fdLeftLabel );
    fdLeftLabel.left = new FormAttachment( 0, 0 );
    fdLeftLabel.top = new FormAttachment( 0, 0 );
    fdLeftLabel.width = left.getBounds().width;
    fdLeftLabel.height = left.getBounds().height;
    
    Label rightLabel = new Label( getParent(), SWT.NONE );
    rightLabel.setImage( right );
    FormData fdRightLabel = new FormData();
    rightLabel.setLayoutData( fdRightLabel );
    fdRightLabel.right = new FormAttachment( 100, 0 );
    fdRightLabel.top = new FormAttachment( 0, 0 );
    fdRightLabel.height = right.getBounds().height;
    fdRightLabel.width = right.getBounds().width;
    
    footerCenter = new Composite( getParent(), SWT.NONE );
    footerCenter.setBackgroundImage( bg );
    FormData fdFooterCenter = new FormData();
    footerCenter.setLayoutData( fdFooterCenter );
    fdFooterCenter.left = new FormAttachment( leftLabel );
    fdFooterCenter.right = new FormAttachment( rightLabel );
    fdFooterCenter.top = new FormAttachment( 0, 0 );
    fdFooterCenter.height = bg.getBounds().height; 
    
  }

  public void dispose() {
  }

  public Control getControl() {
    return footerCenter;
  }

  public Point getSize() {
    return footerCenter.getSize();
  }
}
