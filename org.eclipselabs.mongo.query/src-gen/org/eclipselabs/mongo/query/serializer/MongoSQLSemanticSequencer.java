package org.eclipselabs.mongo.query.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;
import org.eclipselabs.mongo.query.query.AndWhereEntry;
import org.eclipselabs.mongo.query.query.BooleanArrayExpression;
import org.eclipselabs.mongo.query.query.BooleanExpression;
import org.eclipselabs.mongo.query.query.Database;
import org.eclipselabs.mongo.query.query.DateArrayExpression;
import org.eclipselabs.mongo.query.query.DateExpression;
import org.eclipselabs.mongo.query.query.DoubleArrayExpression;
import org.eclipselabs.mongo.query.query.DoubleExpression;
import org.eclipselabs.mongo.query.query.LongArrayExpression;
import org.eclipselabs.mongo.query.query.LongExpression;
import org.eclipselabs.mongo.query.query.Model;
import org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry;
import org.eclipselabs.mongo.query.query.NullArrayExpression;
import org.eclipselabs.mongo.query.query.NullExpression;
import org.eclipselabs.mongo.query.query.OrWhereEntry;
import org.eclipselabs.mongo.query.query.QueryPackage;
import org.eclipselabs.mongo.query.query.ReplacableValue;
import org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry;
import org.eclipselabs.mongo.query.query.StringArrayExpression;
import org.eclipselabs.mongo.query.query.StringExpression;
import org.eclipselabs.mongo.query.services.MongoSQLGrammarAccess;

@SuppressWarnings("all")
public class MongoSQLSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private MongoSQLGrammarAccess grammarAccess;
	
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == QueryPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case QueryPackage.AND_WHERE_ENTRY:
				if(context == grammarAccess.getAndWhereEntryRule() ||
				   context == grammarAccess.getAndWhereEntryAccess().getAndWhereEntryEntriesAction_1_0() ||
				   context == grammarAccess.getConcreteWhereEntryRule() ||
				   context == grammarAccess.getParWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryAccess().getOrWhereEntryEntriesAction_1_0()) {
					sequence_AndWhereEntry(context, (AndWhereEntry) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.BOOLEAN_ARRAY_EXPRESSION:
				if(context == grammarAccess.getArrayExpressionRule() ||
				   context == grammarAccess.getBooleanArrayExpressionRule()) {
					sequence_BooleanArrayExpression(context, (BooleanArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.BOOLEAN_EXPRESSION:
				if(context == grammarAccess.getBooleanExpressionRule() ||
				   context == grammarAccess.getExpressionRule()) {
					sequence_BooleanExpression(context, (BooleanExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.DATABASE:
				if(context == grammarAccess.getDatabaseRule()) {
					sequence_Database(context, (Database) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.DATE_ARRAY_EXPRESSION:
				if(context == grammarAccess.getArrayExpressionRule() ||
				   context == grammarAccess.getDateArrayExpressionRule()) {
					sequence_DateArrayExpression(context, (DateArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.DATE_EXPRESSION:
				if(context == grammarAccess.getDateExpressionRule() ||
				   context == grammarAccess.getExpressionRule()) {
					sequence_DateExpression(context, (DateExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.DOUBLE_ARRAY_EXPRESSION:
				if(context == grammarAccess.getArrayExpressionRule() ||
				   context == grammarAccess.getDoubleArrayExpressionRule()) {
					sequence_DoubleArrayExpression(context, (DoubleArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.DOUBLE_EXPRESSION:
				if(context == grammarAccess.getDoubleExpressionRule() ||
				   context == grammarAccess.getExpressionRule()) {
					sequence_DoubleExpression(context, (DoubleExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.LONG_ARRAY_EXPRESSION:
				if(context == grammarAccess.getArrayExpressionRule() ||
				   context == grammarAccess.getLongArrayExpressionRule()) {
					sequence_LongArrayExpression(context, (LongArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.LONG_EXPRESSION:
				if(context == grammarAccess.getExpressionRule() ||
				   context == grammarAccess.getLongExpressionRule()) {
					sequence_LongExpression(context, (LongExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.MODEL:
				if(context == grammarAccess.getModelRule()) {
					sequence_Model(context, (Model) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.MULTI_EXPRESSION_WHERE_ENTRY:
				if(context == grammarAccess.getAndWhereEntryRule() ||
				   context == grammarAccess.getAndWhereEntryAccess().getAndWhereEntryEntriesAction_1_0() ||
				   context == grammarAccess.getConcreteWhereEntryRule() ||
				   context == grammarAccess.getExpressionWhereEntryRule() ||
				   context == grammarAccess.getMultiExpressionWhereEntryRule() ||
				   context == grammarAccess.getParWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryAccess().getOrWhereEntryEntriesAction_1_0()) {
					sequence_MultiExpressionWhereEntry(context, (MultiExpressionWhereEntry) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.NULL_ARRAY_EXPRESSION:
				if(context == grammarAccess.getArrayExpressionRule() ||
				   context == grammarAccess.getNullArrayExpressionRule()) {
					sequence_NullArrayExpression(context, (NullArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.NULL_EXPRESSION:
				if(context == grammarAccess.getExpressionRule() ||
				   context == grammarAccess.getNullExpressionRule()) {
					sequence_NullExpression(context, (NullExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.OR_WHERE_ENTRY:
				if(context == grammarAccess.getAndWhereEntryRule() ||
				   context == grammarAccess.getAndWhereEntryAccess().getAndWhereEntryEntriesAction_1_0() ||
				   context == grammarAccess.getConcreteWhereEntryRule() ||
				   context == grammarAccess.getParWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryAccess().getOrWhereEntryEntriesAction_1_0()) {
					sequence_WhereEntry(context, (OrWhereEntry) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.REPLACABLE_VALUE:
				if(context == grammarAccess.getExpressionRule() ||
				   context == grammarAccess.getReplacableValueRule()) {
					sequence_ReplacableValue(context, (ReplacableValue) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.SINGLE_EXPRESSION_WHERE_ENTRY:
				if(context == grammarAccess.getAndWhereEntryRule() ||
				   context == grammarAccess.getAndWhereEntryAccess().getAndWhereEntryEntriesAction_1_0() ||
				   context == grammarAccess.getConcreteWhereEntryRule() ||
				   context == grammarAccess.getExpressionWhereEntryRule() ||
				   context == grammarAccess.getParWhereEntryRule() ||
				   context == grammarAccess.getSingleExpressionWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryRule() ||
				   context == grammarAccess.getWhereEntryAccess().getOrWhereEntryEntriesAction_1_0()) {
					sequence_SingleExpressionWhereEntry(context, (SingleExpressionWhereEntry) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.STRING_ARRAY_EXPRESSION:
				if(context == grammarAccess.getArrayExpressionRule() ||
				   context == grammarAccess.getStringArrayExpressionRule()) {
					sequence_StringArrayExpression(context, (StringArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case QueryPackage.STRING_EXPRESSION:
				if(context == grammarAccess.getExpressionRule() ||
				   context == grammarAccess.getStringExpressionRule()) {
					sequence_StringExpression(context, (StringExpression) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (entries+=AndWhereEntry_AndWhereEntry_1_0 entries+=ConcreteWhereEntry+)
	 */
	protected void sequence_AndWhereEntry(EObject context, AndWhereEntry semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (values+=BOOL values+=BOOL*)
	 */
	protected void sequence_BooleanArrayExpression(EObject context, BooleanArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (true='true' | true='false')
	 */
	protected void sequence_BooleanExpression(EObject context, BooleanExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (url=ID port=SINGED_LONG? dbName=ID name=ID)
	 */
	protected void sequence_Database(EObject context, Database semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (values+=DATE values+=DATE*)
	 */
	protected void sequence_DateArrayExpression(EObject context, DateArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=DATE
	 */
	protected void sequence_DateExpression(EObject context, DateExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.DATE_EXPRESSION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.DATE_EXPRESSION__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getDateExpressionAccess().getValueDATETerminalRuleCall_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (values+=SIGNED_DOUBLE values+=SIGNED_DOUBLE*)
	 */
	protected void sequence_DoubleArrayExpression(EObject context, DoubleArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=SIGNED_DOUBLE
	 */
	protected void sequence_DoubleExpression(EObject context, DoubleExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.DOUBLE_EXPRESSION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.DOUBLE_EXPRESSION__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getDoubleExpressionAccess().getValueSIGNED_DOUBLETerminalRuleCall_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (values+=SINGED_LONG values+=SINGED_LONG*)
	 */
	protected void sequence_LongArrayExpression(EObject context, LongArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=SINGED_LONG
	 */
	protected void sequence_LongExpression(EObject context, LongExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.LONG_EXPRESSION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.LONG_EXPRESSION__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getLongExpressionAccess().getValueSINGED_LONGTerminalRuleCall_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (attrs=ColumnList db=Database whereEntry=WhereEntry?)
	 */
	protected void sequence_Model(EObject context, Model semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID operator=ArrayOperator rhs=ArrayExpression)
	 */
	protected void sequence_MultiExpressionWhereEntry(EObject context, MultiExpressionWhereEntry semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.EXPRESSION_WHERE_ENTRY__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.EXPRESSION_WHERE_ENTRY__NAME));
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.MULTI_EXPRESSION_WHERE_ENTRY__OPERATOR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.MULTI_EXPRESSION_WHERE_ENTRY__OPERATOR));
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.MULTI_EXPRESSION_WHERE_ENTRY__RHS) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.MULTI_EXPRESSION_WHERE_ENTRY__RHS));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getMultiExpressionWhereEntryAccess().getNameIDTerminalRuleCall_0_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getMultiExpressionWhereEntryAccess().getOperatorArrayOperatorEnumRuleCall_1_0(), semanticObject.getOperator());
		feeder.accept(grammarAccess.getMultiExpressionWhereEntryAccess().getRhsArrayExpressionParserRuleCall_2_0(), semanticObject.getRhs());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (values+='null' values+='null'*)
	 */
	protected void sequence_NullArrayExpression(EObject context, NullArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value='null'
	 */
	protected void sequence_NullExpression(EObject context, NullExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.NULL_EXPRESSION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.NULL_EXPRESSION__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getNullExpressionAccess().getValueNullKeyword_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value='?'
	 */
	protected void sequence_ReplacableValue(EObject context, ReplacableValue semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.REPLACABLE_VALUE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.REPLACABLE_VALUE__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getReplacableValueAccess().getValueQuestionMarkKeyword_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID operator=Operator rhs=Expression)
	 */
	protected void sequence_SingleExpressionWhereEntry(EObject context, SingleExpressionWhereEntry semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.EXPRESSION_WHERE_ENTRY__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.EXPRESSION_WHERE_ENTRY__NAME));
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.SINGLE_EXPRESSION_WHERE_ENTRY__OPERATOR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.SINGLE_EXPRESSION_WHERE_ENTRY__OPERATOR));
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.SINGLE_EXPRESSION_WHERE_ENTRY__RHS) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.SINGLE_EXPRESSION_WHERE_ENTRY__RHS));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getSingleExpressionWhereEntryAccess().getNameIDTerminalRuleCall_0_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getSingleExpressionWhereEntryAccess().getOperatorOperatorEnumRuleCall_1_0(), semanticObject.getOperator());
		feeder.accept(grammarAccess.getSingleExpressionWhereEntryAccess().getRhsExpressionParserRuleCall_2_0(), semanticObject.getRhs());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (values+=STRING values+=STRING*)
	 */
	protected void sequence_StringArrayExpression(EObject context, StringArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=STRING
	 */
	protected void sequence_StringExpression(EObject context, StringExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QueryPackage.Literals.STRING_EXPRESSION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QueryPackage.Literals.STRING_EXPRESSION__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getStringExpressionAccess().getValueSTRINGTerminalRuleCall_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (entries+=WhereEntry_OrWhereEntry_1_0 entries+=AndWhereEntry+)
	 */
	protected void sequence_WhereEntry(EObject context, OrWhereEntry semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}
