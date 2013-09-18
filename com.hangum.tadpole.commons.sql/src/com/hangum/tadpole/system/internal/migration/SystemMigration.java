/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.system.internal.migration;

/**
 * System abstract class
 * 
 * @author hangum
 *
 */
public abstract class SystemMigration {

	public abstract void migration(String major_version, String sub_version) throws Exception;
}
