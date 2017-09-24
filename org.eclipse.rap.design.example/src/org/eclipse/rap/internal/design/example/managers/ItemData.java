/*******************************************************************************
 * Copyright (c) 2010, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.design.example.managers;

import org.eclipse.swt.graphics.Image;

public class ItemData {

  private final String id;
  private final String text;
  private final String toolTipText;
  private final Image image;

  public ItemData( final String id,
                   final String text,
                   final String toolTipText,
                   final Image image )
  {
    this.id = id;
    this.text = text;
    this.toolTipText = toolTipText;
    this.image = image;
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public String getToolTipText() {
    return toolTipText;
  }

  public Image getImage() {
    return image;
  }

}
