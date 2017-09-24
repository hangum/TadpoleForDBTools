/*******************************************************************************
 * Copyright (c) 2008, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.design.example.managers;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;


public class ContribItem extends ToolBarContributionItem2
{

  private IToolBarManager toolBarManager;

  public ContribItem( 
    final IToolBarManager toolBarManager, 
    final String id ) 
  {
    super( toolBarManager, id);
    this.toolBarManager = toolBarManager;
  }

  public int getCurrentHeight() {
    return 0;
  }

  public int getCurrentWidth() {
    return 0;
  }

  public int getMinimumItemsToShow() {
    return 0;
  }

  public IToolBarManager getToolBarManager() {
    return toolBarManager;
  }

  public boolean getUseChevron() {
    return false;
  }

  public void setCurrentHeight( int currentHeight ) {
  }

  public void setCurrentWidth( int currentWidth ) {
  }

  public void setMinimumItemsToShow( int minimumItemsToShow ) {
  }

  public void setUseChevron( boolean value ) {
  }
  
  public boolean isVisible() {
    boolean visibleItem = false;
    if ( toolBarManager != null ) {
      IContributionItem[] contributionItems = toolBarManager.getItems();
      for( int i = 0; i < contributionItems.length && !visibleItem; i++ ) {
        IContributionItem contributionItem = contributionItems[ i ];
        if( ( !contributionItem.isGroupMarker() )
            && (!contributionItem.isSeparator() )
            && ( contributionItem.isVisible() ) ) 
        {
            visibleItem = true;
        }
      }
    }

    return ( visibleItem || super.isVisible() );
  }
}
