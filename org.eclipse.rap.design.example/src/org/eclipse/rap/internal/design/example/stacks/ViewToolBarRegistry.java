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
package org.eclipse.rap.internal.design.example.stacks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.SingletonUtil;
import org.eclipse.swt.widgets.Control;

/**
 * This class acts as a registry for ViewStackPresentations. This is necessary
 * because the same view can be in different parts. If a toolbar for one part 
 * change the others should be notified.
 */
public class ViewToolBarRegistry {

  private List presentationList = new ArrayList();

  private ViewToolBarRegistry() {
  }

  public static ViewToolBarRegistry getInstance() {
    return SingletonUtil.getSessionInstance( ViewToolBarRegistry.class );
  }

  public void addViewPartPresentation( ViewStackPresentation presentation ) {
    presentationList.add( presentation );
  }

  public void removeViewPartPresentation( ViewStackPresentation presentation ) {
    presentationList.remove( presentation );
  }

  public void fireToolBarChanged() {
    for( int i = 0; i < presentationList.size(); i++ ) {
      if( presentationList.get( i ) != null ) {
        ViewStackPresentation presentation 
          = ( ViewStackPresentation ) presentationList.get( i );
        presentation.catchToolbarChange();
      }
    }
  }

  public void moveAllToolbarsBellow( Control control ) {
    for( int i = 0; i < presentationList.size(); i++ ) {
      if( presentationList.get( i ) != null ) {
        ViewStackPresentation presentation = ( ViewStackPresentation ) presentationList.get( i );
        presentation.hideAllToolBars( control );
      }
    }
  }

}
