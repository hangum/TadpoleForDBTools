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
/**
 */
package com.hangum.tadpole.rdb.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Relation Kind</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.hangum.tadpole.rdb.model.RdbPackage#getRelationKind()
 * @model
 * @generated
 */
public enum RelationKind implements Enumerator {
	/**
	 * The '<em><b>ONLY ONE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ONLY_ONE_VALUE
	 * @generated
	 * @ordered
	 */
	ONLY_ONE(1, "ONLY_ONE", "ONLY_ONE"),

	/**
	 * The '<em><b>ZERO OR ONE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ZERO_OR_ONE_VALUE
	 * @generated
	 * @ordered
	 */
	ZERO_OR_ONE(3, "ZERO_OR_ONE", "ZERO_ONE"),

	/**
	 * The '<em><b>ZERO OR MANY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ZERO_OR_MANY_VALUE
	 * @generated
	 * @ordered
	 */
	ZERO_OR_MANY(4, "ZERO_OR_MANY", "ZERO_MANY"),

	/**
	 * The '<em><b>ONE OR MANY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ONE_OR_MANY_VALUE
	 * @generated
	 * @ordered
	 */
	ONE_OR_MANY(5, "ONE_OR_MANY", "ONE_MANY");

	/**
	 * The '<em><b>ONLY ONE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ONLY ONE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ONLY_ONE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ONLY_ONE_VALUE = 1;

	/**
	 * The '<em><b>ZERO OR ONE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ZERO OR ONE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ZERO_OR_ONE
	 * @model literal="ZERO_ONE"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_OR_ONE_VALUE = 3;

	/**
	 * The '<em><b>ZERO OR MANY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ZERO OR MANY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ZERO_OR_MANY
	 * @model literal="ZERO_MANY"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_OR_MANY_VALUE = 4;

	/**
	 * The '<em><b>ONE OR MANY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ONE OR MANY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ONE_OR_MANY
	 * @model literal="ONE_MANY"
	 * @generated
	 * @ordered
	 */
	public static final int ONE_OR_MANY_VALUE = 5;

	/**
	 * An array of all the '<em><b>Relation Kind</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final RelationKind[] VALUES_ARRAY =
		new RelationKind[] {
			ONLY_ONE,
			ZERO_OR_ONE,
			ZERO_OR_MANY,
			ONE_OR_MANY,
		};

	/**
	 * A public read-only list of all the '<em><b>Relation Kind</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<RelationKind> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Relation Kind</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static RelationKind get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			RelationKind result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Relation Kind</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static RelationKind getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			RelationKind result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Relation Kind</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static RelationKind get(int value) {
		switch (value) {
			case ONLY_ONE_VALUE: return ONLY_ONE;
			case ZERO_OR_ONE_VALUE: return ZERO_OR_ONE;
			case ZERO_OR_MANY_VALUE: return ZERO_OR_MANY;
			case ONE_OR_MANY_VALUE: return ONE_OR_MANY;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private RelationKind(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //RelationKind
