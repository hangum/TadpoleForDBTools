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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.internal.design.example.stacks.ViewStackPresentation;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.ui.interactiondesign.layout.ElementBuilder;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


public class StackPresentationBuider extends ElementBuilder {

  public static final String BOTTOM_BORDER = "bottomBorder"; //$NON-NLS-1$
  public static final String RIGHT_BORDER = "rightBorder"; //$NON-NLS-1$
  public static final String LEFT_BORDER = "leftBorder"; //$NON-NLS-1$
  public static final String TOP_BORDER = "topBorder"; //$NON-NLS-1$
  public static final String RIGHT = "right"; //$NON-NLS-1$
  public static final String LEFT = "left"; //$NON-NLS-1$
  private Image tabInactiveBgActive;
  private Composite content;
  private Image borderBottom;
  private Image borderTop;
  private Image borderLeft;
  private Image borderRight;
  private Composite tabBar;
  private Image leftCorner;
  private Image rightCorner;
  private Label leftCornerLabel;
  private Label rightCornerLabel;
  private final Map labelMap;

  public StackPresentationBuider( Composite parent, String layoutSetId ) {
    super( parent, layoutSetId );
    labelMap = new HashMap();
    init();
  }

  private void init() {
    tabInactiveBgActive
      = createImageById( ILayoutSetConstants.STACK_TAB_INACTIVE_BG_ACTIVE );
    borderBottom = createImageById( ILayoutSetConstants.STACK_BORDER_BOTTOM );
    borderTop = createImageById( ILayoutSetConstants.STACK_BORDER_TOP );
    borderLeft = createImageById( ILayoutSetConstants.STACK_BORDER_LEFT );
    borderRight = createImageById( ILayoutSetConstants.STACK_BORDER_RIGHT );
    leftCorner
      = createImageById( ILayoutSetConstants.STACK_TABBAR_LEFT_ACTIVE );
    rightCorner
      = createImageById( ILayoutSetConstants.STACK_TABBAR_RIGHT_ACTIVE );
  }

  private Image createImageById( final String id ) {
    LayoutSet set = getLayoutSet();
    return createImage( set.getImagePath( id ) );
  }

  @Override
  public void addControl( Control control, Object layoutData ) {
  }

  @Override
  public void addControl( Control control, String positionId ) {
  }

  @Override
  public void addImage( Image image, Object layoutData ) {
  }

  @Override
  public void addImage( Image image, String positionId ) {
  }

  @Override
  public void build() {
    getParent().setLayout( new FillLayout() );
    Composite stack = createFrame();
    stack.setLayout( new FormLayout() );

    tabBar = new Composite( stack, SWT.NONE );
    tabBar.setLayout( new FormLayout() );
    tabBar.setBackgroundImage( tabInactiveBgActive );
    FormData fdTabBar = new FormData();
    tabBar.setLayoutData( fdTabBar );
    fdTabBar.top = new FormAttachment( 0 );
    fdTabBar.left = new FormAttachment( 0 );
    fdTabBar.right = new FormAttachment( 100 );
    fdTabBar.height = tabInactiveBgActive.getBounds().height;

    if( rightCorner != null && leftCorner != null ) {
      leftCornerLabel = new Label( stack.getParent(), SWT.NONE );
      leftCornerLabel.setImage( leftCorner );
      FormData fdLeftCorner = new FormData();
      leftCornerLabel.setLayoutData( fdLeftCorner );
      fdLeftCorner.left = new FormAttachment( 0, 3 );
      fdLeftCorner.top = new FormAttachment( 0, 7 );

      rightCornerLabel = new Label( stack.getParent(), SWT.NONE );
      rightCornerLabel.setImage( rightCorner );
      FormData fdRightCorner = new FormData();
      rightCornerLabel.setLayoutData( fdRightCorner );
      fdRightCorner.right = new FormAttachment( 100, -3 );
      fdRightCorner.top = new FormAttachment( 0, 7 );
      rightCornerLabel.moveAbove( null );
      leftCornerLabel.moveAbove( null );
      labelMap.put( LEFT, leftCornerLabel );
      labelMap.put( RIGHT, rightCornerLabel );
    }

    content = new Composite( stack, SWT.NONE );
    FormData fdContent = new FormData();
    content.setLayoutData( fdContent );
    fdContent.top = new FormAttachment( tabBar );
    fdContent.left = new FormAttachment( 0 );
    fdContent.right = new FormAttachment( 100 );
    fdContent.bottom = new FormAttachment( 100 );
  }

  private Composite createFrame() {
    Composite frameComp = new Composite( getParent(), SWT.NONE );
    frameComp.setData( RWT.CUSTOM_VARIANT, "compGray" ); //$NON-NLS-1$
    frameComp.setLayout( new FormLayout() );
    frameComp.setBackgroundMode( SWT.INHERIT_FORCE );

    Label left = new Label( frameComp, SWT.NONE );
    left.setData( RWT.CUSTOM_VARIANT, "stackBorder" ); //$NON-NLS-1$
    left.setBackgroundImage( borderLeft );
    FormData fdLeft = new FormData();
    left.setLayoutData( fdLeft );
    fdLeft.top = new FormAttachment( 0, borderTop.getBounds().height );
    fdLeft.bottom
      = new FormAttachment( 100, - borderBottom.getBounds().height + 1 );
    fdLeft.left = new FormAttachment( 0 );
    fdLeft.width = borderLeft.getBounds().width;
    labelMap.put( LEFT_BORDER, left );

    Label right = new Label( frameComp, SWT.NONE );
    right.setData( RWT.CUSTOM_VARIANT, "stackBorder" ); //$NON-NLS-1$
    right.setBackgroundImage( borderRight );
    FormData fdRight = new FormData();
    right.setLayoutData( fdRight );
    fdRight.top = new FormAttachment( 0, borderTop.getBounds().height );
    fdRight.bottom
      = new FormAttachment( 100, - borderBottom.getBounds().height + 1 );
    fdRight.right = new FormAttachment( 100 );
    fdRight.width = borderRight.getBounds().width;
    labelMap.put( RIGHT_BORDER, right );

    Label top = new Label( frameComp, SWT.NONE );
    top.setData( RWT.CUSTOM_VARIANT, "stackBorder" ); //$NON-NLS-1$
    top.setBackgroundImage( borderTop );
    FormData fdTop = new FormData();
    top.setLayoutData( fdTop );
    fdTop.top = new FormAttachment( 0 );
    fdTop.left = new FormAttachment( left );
    fdTop.right = new FormAttachment( right );
    fdTop.height = borderTop.getBounds().height;
    labelMap.put( TOP_BORDER, top );

    Label bottom = new Label( frameComp, SWT.NONE );
    bottom.setData( RWT.CUSTOM_VARIANT, "stackBorder" ); //$NON-NLS-1$
    bottom.setBackgroundImage( borderBottom );
    FormData fdBottom = new FormData();
    bottom.setLayoutData( fdBottom );
    fdBottom.bottom = new FormAttachment( 100 );
    fdBottom.left = new FormAttachment( left );
    fdBottom.right = new FormAttachment( right );
    fdBottom.height = borderBottom.getBounds().height;
    labelMap.put( BOTTOM_BORDER, bottom );

    Composite result = new Composite( frameComp, SWT.NONE );
    result.setData( RWT.CUSTOM_VARIANT, "compGray" ); //$NON-NLS-1$
    FormData fdResult = new FormData();
    result.setLayoutData( fdResult );
    fdResult.top = new FormAttachment( top );
    fdResult.left = new FormAttachment( left );
    fdResult.right = new FormAttachment( right );
    fdResult.bottom = new FormAttachment( bottom );

    return result;
  }

  @Override
  public void dispose() {
  }

  @Override
  public Control getControl() {
    return content;
  }

  @Override
  public Point getSize() {
    Point result = null;
    if( content != null ) {
      result = content.getSize();
    }
    return result;
  }

  @Override
  public Object getAdapter( final Class adapter ) {
    Object result = null;
    if( adapter == ViewStackPresentation.class ) {
      result = tabBar;
    } else if( adapter == Map.class ) {
      result = labelMap;
    }
    return result;
  }
}
