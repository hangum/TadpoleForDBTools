package org.eclipselabs.mongo.query.serializer;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;
import org.eclipselabs.mongo.query.services.MongoSQLGrammarAccess;

@SuppressWarnings("all")
public class MongoSQLSyntacticSequencer extends AbstractSyntacticSequencer {

	protected MongoSQLGrammarAccess grammarAccess;
	protected AbstractElementAlias match_AndWhereEntry_ANDKeyword_1_1_0_q;
	protected AbstractElementAlias match_ParWhereEntry_LeftParenthesisKeyword_0_a;
	protected AbstractElementAlias match_ParWhereEntry_LeftParenthesisKeyword_0_p;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (MongoSQLGrammarAccess) access;
		match_AndWhereEntry_ANDKeyword_1_1_0_q = new TokenAlias(false, true, grammarAccess.getAndWhereEntryAccess().getANDKeyword_1_1_0());
		match_ParWhereEntry_LeftParenthesisKeyword_0_a = new TokenAlias(true, true, grammarAccess.getParWhereEntryAccess().getLeftParenthesisKeyword_0());
		match_ParWhereEntry_LeftParenthesisKeyword_0_p = new TokenAlias(true, false, grammarAccess.getParWhereEntryAccess().getLeftParenthesisKeyword_0());
	}
	
	@Override
	protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		return "";
	}
	
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (transition.getAmbiguousSyntaxes().isEmpty()) return;
		List<INode> transitionNodes = collectNodes(fromNode, toNode);
		for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
			List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
			if(match_AndWhereEntry_ANDKeyword_1_1_0_q.equals(syntax))
				emit_AndWhereEntry_ANDKeyword_1_1_0_q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_ParWhereEntry_LeftParenthesisKeyword_0_a.equals(syntax))
				emit_ParWhereEntry_LeftParenthesisKeyword_0_a(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_ParWhereEntry_LeftParenthesisKeyword_0_p.equals(syntax))
				emit_ParWhereEntry_LeftParenthesisKeyword_0_p(semanticObject, getLastNavigableState(), syntaxNodes);
			else acceptNodes(getLastNavigableState(), syntaxNodes);
		}
	}

	/**
	 * Syntax:
	 *     'AND'?
	 */
	protected void emit_AndWhereEntry_ANDKeyword_1_1_0_q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Syntax:
	 *     '('*
	 */
	protected void emit_ParWhereEntry_LeftParenthesisKeyword_0_a(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Syntax:
	 *     '('+
	 */
	protected void emit_ParWhereEntry_LeftParenthesisKeyword_0_p(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
}
