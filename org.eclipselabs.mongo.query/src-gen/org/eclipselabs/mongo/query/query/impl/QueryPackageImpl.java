/**
 */
package org.eclipselabs.mongo.query.query.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipselabs.mongo.query.query.AndWhereEntry;
import org.eclipselabs.mongo.query.query.ArrayExpression;
import org.eclipselabs.mongo.query.query.ArrayOperator;
import org.eclipselabs.mongo.query.query.BooleanArrayExpression;
import org.eclipselabs.mongo.query.query.BooleanExpression;
import org.eclipselabs.mongo.query.query.Database;
import org.eclipselabs.mongo.query.query.DateArrayExpression;
import org.eclipselabs.mongo.query.query.DateExpression;
import org.eclipselabs.mongo.query.query.DoubleArrayExpression;
import org.eclipselabs.mongo.query.query.DoubleExpression;
import org.eclipselabs.mongo.query.query.Expression;
import org.eclipselabs.mongo.query.query.ExpressionWhereEntry;
import org.eclipselabs.mongo.query.query.LongArrayExpression;
import org.eclipselabs.mongo.query.query.LongExpression;
import org.eclipselabs.mongo.query.query.Model;
import org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry;
import org.eclipselabs.mongo.query.query.NullArrayExpression;
import org.eclipselabs.mongo.query.query.NullExpression;
import org.eclipselabs.mongo.query.query.Operator;
import org.eclipselabs.mongo.query.query.OrWhereEntry;
import org.eclipselabs.mongo.query.query.QueryFactory;
import org.eclipselabs.mongo.query.query.QueryPackage;
import org.eclipselabs.mongo.query.query.ReplacableValue;
import org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry;
import org.eclipselabs.mongo.query.query.StringArrayExpression;
import org.eclipselabs.mongo.query.query.StringExpression;
import org.eclipselabs.mongo.query.query.WhereEntry;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QueryPackageImpl extends EPackageImpl implements QueryPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass databaseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass whereEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionWhereEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass singleExpressionWhereEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass replacableValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass doubleExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass longExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass nullExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dateExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass booleanExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass multiExpressionWhereEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass doubleArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass longArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass nullArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dateArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass booleanArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orWhereEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass andWhereEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum arrayOperatorEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum operatorEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipselabs.mongo.query.query.QueryPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private QueryPackageImpl()
  {
    super(eNS_URI, QueryFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link QueryPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static QueryPackage init()
  {
    if (isInited) return (QueryPackage)EPackage.Registry.INSTANCE.getEPackage(QueryPackage.eNS_URI);

    // Obtain or create and register package
    QueryPackageImpl theQueryPackage = (QueryPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof QueryPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new QueryPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theQueryPackage.createPackageContents();

    // Initialize created meta-data
    theQueryPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theQueryPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(QueryPackage.eNS_URI, theQueryPackage);
    return theQueryPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModel()
  {
    return modelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModel_Attrs()
  {
    return (EAttribute)modelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_Db()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_WhereEntry()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDatabase()
  {
    return databaseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabase_Url()
  {
    return (EAttribute)databaseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabase_Port()
  {
    return (EAttribute)databaseEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabase_DbName()
  {
    return (EAttribute)databaseEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabase_Name()
  {
    return (EAttribute)databaseEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWhereEntry()
  {
    return whereEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExpressionWhereEntry()
  {
    return expressionWhereEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getExpressionWhereEntry_Name()
  {
    return (EAttribute)expressionWhereEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSingleExpressionWhereEntry()
  {
    return singleExpressionWhereEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSingleExpressionWhereEntry_Operator()
  {
    return (EAttribute)singleExpressionWhereEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSingleExpressionWhereEntry_Rhs()
  {
    return (EReference)singleExpressionWhereEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExpression()
  {
    return expressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getReplacableValue()
  {
    return replacableValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getReplacableValue_Value()
  {
    return (EAttribute)replacableValueEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDoubleExpression()
  {
    return doubleExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDoubleExpression_Value()
  {
    return (EAttribute)doubleExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLongExpression()
  {
    return longExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLongExpression_Value()
  {
    return (EAttribute)longExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStringExpression()
  {
    return stringExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringExpression_Value()
  {
    return (EAttribute)stringExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNullExpression()
  {
    return nullExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getNullExpression_Value()
  {
    return (EAttribute)nullExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDateExpression()
  {
    return dateExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDateExpression_Value()
  {
    return (EAttribute)dateExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBooleanExpression()
  {
    return booleanExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBooleanExpression_True()
  {
    return (EAttribute)booleanExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMultiExpressionWhereEntry()
  {
    return multiExpressionWhereEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMultiExpressionWhereEntry_Operator()
  {
    return (EAttribute)multiExpressionWhereEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMultiExpressionWhereEntry_Rhs()
  {
    return (EReference)multiExpressionWhereEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getArrayExpression()
  {
    return arrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDoubleArrayExpression()
  {
    return doubleArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDoubleArrayExpression_Values()
  {
    return (EAttribute)doubleArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLongArrayExpression()
  {
    return longArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLongArrayExpression_Values()
  {
    return (EAttribute)longArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStringArrayExpression()
  {
    return stringArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringArrayExpression_Values()
  {
    return (EAttribute)stringArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNullArrayExpression()
  {
    return nullArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getNullArrayExpression_Values()
  {
    return (EAttribute)nullArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDateArrayExpression()
  {
    return dateArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDateArrayExpression_Values()
  {
    return (EAttribute)dateArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBooleanArrayExpression()
  {
    return booleanArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBooleanArrayExpression_Values()
  {
    return (EAttribute)booleanArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrWhereEntry()
  {
    return orWhereEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrWhereEntry_Entries()
  {
    return (EReference)orWhereEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAndWhereEntry()
  {
    return andWhereEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getAndWhereEntry_Entries()
  {
    return (EReference)andWhereEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getArrayOperator()
  {
    return arrayOperatorEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getOperator()
  {
    return operatorEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QueryFactory getQueryFactory()
  {
    return (QueryFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    modelEClass = createEClass(MODEL);
    createEAttribute(modelEClass, MODEL__ATTRS);
    createEReference(modelEClass, MODEL__DB);
    createEReference(modelEClass, MODEL__WHERE_ENTRY);

    databaseEClass = createEClass(DATABASE);
    createEAttribute(databaseEClass, DATABASE__URL);
    createEAttribute(databaseEClass, DATABASE__PORT);
    createEAttribute(databaseEClass, DATABASE__DB_NAME);
    createEAttribute(databaseEClass, DATABASE__NAME);

    whereEntryEClass = createEClass(WHERE_ENTRY);

    expressionWhereEntryEClass = createEClass(EXPRESSION_WHERE_ENTRY);
    createEAttribute(expressionWhereEntryEClass, EXPRESSION_WHERE_ENTRY__NAME);

    singleExpressionWhereEntryEClass = createEClass(SINGLE_EXPRESSION_WHERE_ENTRY);
    createEAttribute(singleExpressionWhereEntryEClass, SINGLE_EXPRESSION_WHERE_ENTRY__OPERATOR);
    createEReference(singleExpressionWhereEntryEClass, SINGLE_EXPRESSION_WHERE_ENTRY__RHS);

    expressionEClass = createEClass(EXPRESSION);

    replacableValueEClass = createEClass(REPLACABLE_VALUE);
    createEAttribute(replacableValueEClass, REPLACABLE_VALUE__VALUE);

    doubleExpressionEClass = createEClass(DOUBLE_EXPRESSION);
    createEAttribute(doubleExpressionEClass, DOUBLE_EXPRESSION__VALUE);

    longExpressionEClass = createEClass(LONG_EXPRESSION);
    createEAttribute(longExpressionEClass, LONG_EXPRESSION__VALUE);

    stringExpressionEClass = createEClass(STRING_EXPRESSION);
    createEAttribute(stringExpressionEClass, STRING_EXPRESSION__VALUE);

    nullExpressionEClass = createEClass(NULL_EXPRESSION);
    createEAttribute(nullExpressionEClass, NULL_EXPRESSION__VALUE);

    dateExpressionEClass = createEClass(DATE_EXPRESSION);
    createEAttribute(dateExpressionEClass, DATE_EXPRESSION__VALUE);

    booleanExpressionEClass = createEClass(BOOLEAN_EXPRESSION);
    createEAttribute(booleanExpressionEClass, BOOLEAN_EXPRESSION__TRUE);

    multiExpressionWhereEntryEClass = createEClass(MULTI_EXPRESSION_WHERE_ENTRY);
    createEAttribute(multiExpressionWhereEntryEClass, MULTI_EXPRESSION_WHERE_ENTRY__OPERATOR);
    createEReference(multiExpressionWhereEntryEClass, MULTI_EXPRESSION_WHERE_ENTRY__RHS);

    arrayExpressionEClass = createEClass(ARRAY_EXPRESSION);

    doubleArrayExpressionEClass = createEClass(DOUBLE_ARRAY_EXPRESSION);
    createEAttribute(doubleArrayExpressionEClass, DOUBLE_ARRAY_EXPRESSION__VALUES);

    longArrayExpressionEClass = createEClass(LONG_ARRAY_EXPRESSION);
    createEAttribute(longArrayExpressionEClass, LONG_ARRAY_EXPRESSION__VALUES);

    stringArrayExpressionEClass = createEClass(STRING_ARRAY_EXPRESSION);
    createEAttribute(stringArrayExpressionEClass, STRING_ARRAY_EXPRESSION__VALUES);

    nullArrayExpressionEClass = createEClass(NULL_ARRAY_EXPRESSION);
    createEAttribute(nullArrayExpressionEClass, NULL_ARRAY_EXPRESSION__VALUES);

    dateArrayExpressionEClass = createEClass(DATE_ARRAY_EXPRESSION);
    createEAttribute(dateArrayExpressionEClass, DATE_ARRAY_EXPRESSION__VALUES);

    booleanArrayExpressionEClass = createEClass(BOOLEAN_ARRAY_EXPRESSION);
    createEAttribute(booleanArrayExpressionEClass, BOOLEAN_ARRAY_EXPRESSION__VALUES);

    orWhereEntryEClass = createEClass(OR_WHERE_ENTRY);
    createEReference(orWhereEntryEClass, OR_WHERE_ENTRY__ENTRIES);

    andWhereEntryEClass = createEClass(AND_WHERE_ENTRY);
    createEReference(andWhereEntryEClass, AND_WHERE_ENTRY__ENTRIES);

    // Create enums
    arrayOperatorEEnum = createEEnum(ARRAY_OPERATOR);
    operatorEEnum = createEEnum(OPERATOR);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    expressionWhereEntryEClass.getESuperTypes().add(this.getWhereEntry());
    singleExpressionWhereEntryEClass.getESuperTypes().add(this.getExpressionWhereEntry());
    replacableValueEClass.getESuperTypes().add(this.getExpression());
    doubleExpressionEClass.getESuperTypes().add(this.getExpression());
    longExpressionEClass.getESuperTypes().add(this.getExpression());
    stringExpressionEClass.getESuperTypes().add(this.getExpression());
    nullExpressionEClass.getESuperTypes().add(this.getExpression());
    dateExpressionEClass.getESuperTypes().add(this.getExpression());
    booleanExpressionEClass.getESuperTypes().add(this.getExpression());
    multiExpressionWhereEntryEClass.getESuperTypes().add(this.getExpressionWhereEntry());
    doubleArrayExpressionEClass.getESuperTypes().add(this.getArrayExpression());
    longArrayExpressionEClass.getESuperTypes().add(this.getArrayExpression());
    stringArrayExpressionEClass.getESuperTypes().add(this.getArrayExpression());
    nullArrayExpressionEClass.getESuperTypes().add(this.getArrayExpression());
    dateArrayExpressionEClass.getESuperTypes().add(this.getArrayExpression());
    booleanArrayExpressionEClass.getESuperTypes().add(this.getArrayExpression());
    orWhereEntryEClass.getESuperTypes().add(this.getWhereEntry());
    andWhereEntryEClass.getESuperTypes().add(this.getWhereEntry());

    // Initialize classes and features; add operations and parameters
    initEClass(modelEClass, Model.class, "Model", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getModel_Attrs(), ecorePackage.getEString(), "attrs", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_Db(), this.getDatabase(), null, "db", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_WhereEntry(), this.getWhereEntry(), null, "whereEntry", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(databaseEClass, Database.class, "Database", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDatabase_Url(), ecorePackage.getEString(), "url", null, 0, 1, Database.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatabase_Port(), ecorePackage.getELong(), "port", null, 0, 1, Database.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatabase_DbName(), ecorePackage.getEString(), "dbName", null, 0, 1, Database.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatabase_Name(), ecorePackage.getEString(), "name", null, 0, 1, Database.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(whereEntryEClass, WhereEntry.class, "WhereEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(expressionWhereEntryEClass, ExpressionWhereEntry.class, "ExpressionWhereEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getExpressionWhereEntry_Name(), ecorePackage.getEString(), "name", null, 0, 1, ExpressionWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(singleExpressionWhereEntryEClass, SingleExpressionWhereEntry.class, "SingleExpressionWhereEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSingleExpressionWhereEntry_Operator(), this.getOperator(), "operator", null, 0, 1, SingleExpressionWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSingleExpressionWhereEntry_Rhs(), this.getExpression(), null, "rhs", null, 0, 1, SingleExpressionWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(replacableValueEClass, ReplacableValue.class, "ReplacableValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getReplacableValue_Value(), ecorePackage.getEString(), "value", null, 0, 1, ReplacableValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(doubleExpressionEClass, DoubleExpression.class, "DoubleExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDoubleExpression_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, DoubleExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(longExpressionEClass, LongExpression.class, "LongExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLongExpression_Value(), ecorePackage.getELong(), "value", null, 0, 1, LongExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringExpressionEClass, StringExpression.class, "StringExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStringExpression_Value(), ecorePackage.getEString(), "value", null, 0, 1, StringExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(nullExpressionEClass, NullExpression.class, "NullExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getNullExpression_Value(), ecorePackage.getEString(), "value", null, 0, 1, NullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dateExpressionEClass, DateExpression.class, "DateExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDateExpression_Value(), ecorePackage.getEDate(), "value", null, 0, 1, DateExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(booleanExpressionEClass, BooleanExpression.class, "BooleanExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBooleanExpression_True(), ecorePackage.getEString(), "true", null, 0, 1, BooleanExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(multiExpressionWhereEntryEClass, MultiExpressionWhereEntry.class, "MultiExpressionWhereEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMultiExpressionWhereEntry_Operator(), this.getArrayOperator(), "operator", null, 0, 1, MultiExpressionWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMultiExpressionWhereEntry_Rhs(), this.getArrayExpression(), null, "rhs", null, 0, 1, MultiExpressionWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(arrayExpressionEClass, ArrayExpression.class, "ArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(doubleArrayExpressionEClass, DoubleArrayExpression.class, "DoubleArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDoubleArrayExpression_Values(), ecorePackage.getEDouble(), "values", null, 0, -1, DoubleArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(longArrayExpressionEClass, LongArrayExpression.class, "LongArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLongArrayExpression_Values(), ecorePackage.getELong(), "values", null, 0, -1, LongArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringArrayExpressionEClass, StringArrayExpression.class, "StringArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStringArrayExpression_Values(), ecorePackage.getEString(), "values", null, 0, -1, StringArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(nullArrayExpressionEClass, NullArrayExpression.class, "NullArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getNullArrayExpression_Values(), ecorePackage.getEString(), "values", null, 0, -1, NullArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dateArrayExpressionEClass, DateArrayExpression.class, "DateArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDateArrayExpression_Values(), ecorePackage.getEDate(), "values", null, 0, -1, DateArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(booleanArrayExpressionEClass, BooleanArrayExpression.class, "BooleanArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBooleanArrayExpression_Values(), ecorePackage.getEString(), "values", null, 0, -1, BooleanArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(orWhereEntryEClass, OrWhereEntry.class, "OrWhereEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrWhereEntry_Entries(), this.getWhereEntry(), null, "entries", null, 0, -1, OrWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(andWhereEntryEClass, AndWhereEntry.class, "AndWhereEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getAndWhereEntry_Entries(), this.getWhereEntry(), null, "entries", null, 0, -1, AndWhereEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(arrayOperatorEEnum, ArrayOperator.class, "ArrayOperator");
    addEEnumLiteral(arrayOperatorEEnum, ArrayOperator.MONGO_ALL);
    addEEnumLiteral(arrayOperatorEEnum, ArrayOperator.MONGO_IN);
    addEEnumLiteral(arrayOperatorEEnum, ArrayOperator.SQL_IN);
    addEEnumLiteral(arrayOperatorEEnum, ArrayOperator.MONGO_NIN);
    addEEnumLiteral(arrayOperatorEEnum, ArrayOperator.SQL_NOT_IN);

    initEEnum(operatorEEnum, Operator.class, "Operator");
    addEEnumLiteral(operatorEEnum, Operator.LESS_THEN);
    addEEnumLiteral(operatorEEnum, Operator.GREATER_THEN);
    addEEnumLiteral(operatorEEnum, Operator.LESS_EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.GREATER_EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.NOT_EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.LIKE);
    addEEnumLiteral(operatorEEnum, Operator.NOT_LIKE);
    addEEnumLiteral(operatorEEnum, Operator.NOT_IN);
    addEEnumLiteral(operatorEEnum, Operator.IN);

    // Create resource
    createResource(eNS_URI);
  }

} //QueryPackageImpl
