package org.eclipselabs.mongo.query.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.eclipselabs.mongo.query.services.MongoSQLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalMongoSQLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_SINGED_LONG", "RULE_SIGNED_DOUBLE", "RULE_STRING", "RULE_DATE", "RULE_BOOL", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "'SELECT'", "'FROM'", "'WHERE'", "'mongo://'", "':'", "'/'", "','", "'*'", "'OR'", "'AND'", "'('", "')'", "'?'", "'null'", "'true'", "'false'", "'['", "']'", "'$all'", "'$in'", "'in'", "'$nin'", "'not in'", "'<'", "'>'", "'<='", "'>='", "'='", "'!='", "'like'", "'not like'"
    };
    public static final int RULE_ID=4;
    public static final int T__29=29;
    public static final int RULE_DATE=8;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int RULE_BOOL=9;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int RULE_SIGNED_DOUBLE=6;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int RULE_SINGED_LONG=5;
    public static final int RULE_SL_COMMENT=11;
    public static final int RULE_ML_COMMENT=10;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_STRING=7;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int RULE_WS=12;

    // delegates
    // delegators


        public InternalMongoSQLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalMongoSQLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalMongoSQLParser.tokenNames; }
    public String getGrammarFileName() { return "../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g"; }



     	private MongoSQLGrammarAccess grammarAccess;
     	
        public InternalMongoSQLParser(TokenStream input, MongoSQLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Model";	
       	}
       	
       	@Override
       	protected MongoSQLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}



    // $ANTLR start "entryRuleModel"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:68:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:69:2: (iv_ruleModel= ruleModel EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:70:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_ruleModel_in_entryRuleModel75);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModel85); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:77:1: ruleModel returns [EObject current=null] : (otherlv_0= 'SELECT' ( (lv_attrs_1_0= ruleColumnList ) ) otherlv_2= 'FROM' ( (lv_db_3_0= ruleDatabase ) ) (otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) ) )? ) ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        AntlrDatatypeRuleToken lv_attrs_1_0 = null;

        EObject lv_db_3_0 = null;

        EObject lv_whereEntry_5_0 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:80:28: ( (otherlv_0= 'SELECT' ( (lv_attrs_1_0= ruleColumnList ) ) otherlv_2= 'FROM' ( (lv_db_3_0= ruleDatabase ) ) (otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) ) )? ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:81:1: (otherlv_0= 'SELECT' ( (lv_attrs_1_0= ruleColumnList ) ) otherlv_2= 'FROM' ( (lv_db_3_0= ruleDatabase ) ) (otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) ) )? )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:81:1: (otherlv_0= 'SELECT' ( (lv_attrs_1_0= ruleColumnList ) ) otherlv_2= 'FROM' ( (lv_db_3_0= ruleDatabase ) ) (otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) ) )? )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:81:3: otherlv_0= 'SELECT' ( (lv_attrs_1_0= ruleColumnList ) ) otherlv_2= 'FROM' ( (lv_db_3_0= ruleDatabase ) ) (otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) ) )?
            {
            otherlv_0=(Token)match(input,13,FOLLOW_13_in_ruleModel122); 

                	newLeafNode(otherlv_0, grammarAccess.getModelAccess().getSELECTKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:85:1: ( (lv_attrs_1_0= ruleColumnList ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:86:1: (lv_attrs_1_0= ruleColumnList )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:86:1: (lv_attrs_1_0= ruleColumnList )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:87:3: lv_attrs_1_0= ruleColumnList
            {
             
            	        newCompositeNode(grammarAccess.getModelAccess().getAttrsColumnListParserRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleColumnList_in_ruleModel143);
            lv_attrs_1_0=ruleColumnList();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getModelRule());
            	        }
                   		set(
                   			current, 
                   			"attrs",
                    		lv_attrs_1_0, 
                    		"ColumnList");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_14_in_ruleModel155); 

                	newLeafNode(otherlv_2, grammarAccess.getModelAccess().getFROMKeyword_2());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:107:1: ( (lv_db_3_0= ruleDatabase ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:108:1: (lv_db_3_0= ruleDatabase )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:108:1: (lv_db_3_0= ruleDatabase )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:109:3: lv_db_3_0= ruleDatabase
            {
             
            	        newCompositeNode(grammarAccess.getModelAccess().getDbDatabaseParserRuleCall_3_0()); 
            	    
            pushFollow(FOLLOW_ruleDatabase_in_ruleModel176);
            lv_db_3_0=ruleDatabase();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getModelRule());
            	        }
                   		set(
                   			current, 
                   			"db",
                    		lv_db_3_0, 
                    		"Database");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:125:2: (otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) ) )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==15) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:125:4: otherlv_4= 'WHERE' ( (lv_whereEntry_5_0= ruleWhereEntry ) )
                    {
                    otherlv_4=(Token)match(input,15,FOLLOW_15_in_ruleModel189); 

                        	newLeafNode(otherlv_4, grammarAccess.getModelAccess().getWHEREKeyword_4_0());
                        
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:129:1: ( (lv_whereEntry_5_0= ruleWhereEntry ) )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:130:1: (lv_whereEntry_5_0= ruleWhereEntry )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:130:1: (lv_whereEntry_5_0= ruleWhereEntry )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:131:3: lv_whereEntry_5_0= ruleWhereEntry
                    {
                     
                    	        newCompositeNode(grammarAccess.getModelAccess().getWhereEntryWhereEntryParserRuleCall_4_1_0()); 
                    	    
                    pushFollow(FOLLOW_ruleWhereEntry_in_ruleModel210);
                    lv_whereEntry_5_0=ruleWhereEntry();

                    state._fsp--;


                    	        if (current==null) {
                    	            current = createModelElementForParent(grammarAccess.getModelRule());
                    	        }
                           		set(
                           			current, 
                           			"whereEntry",
                            		lv_whereEntry_5_0, 
                            		"WhereEntry");
                    	        afterParserOrEnumRuleCall();
                    	    

                    }


                    }


                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleDatabase"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:155:1: entryRuleDatabase returns [EObject current=null] : iv_ruleDatabase= ruleDatabase EOF ;
    public final EObject entryRuleDatabase() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDatabase = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:156:2: (iv_ruleDatabase= ruleDatabase EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:157:2: iv_ruleDatabase= ruleDatabase EOF
            {
             newCompositeNode(grammarAccess.getDatabaseRule()); 
            pushFollow(FOLLOW_ruleDatabase_in_entryRuleDatabase248);
            iv_ruleDatabase=ruleDatabase();

            state._fsp--;

             current =iv_ruleDatabase; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDatabase258); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDatabase"


    // $ANTLR start "ruleDatabase"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:164:1: ruleDatabase returns [EObject current=null] : (otherlv_0= 'mongo://' ( (lv_url_1_0= RULE_ID ) ) (otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) ) )? otherlv_4= '/' ( (lv_dbName_5_0= RULE_ID ) ) otherlv_6= '/' ( (lv_name_7_0= RULE_ID ) ) ) ;
    public final EObject ruleDatabase() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_url_1_0=null;
        Token otherlv_2=null;
        Token lv_port_3_0=null;
        Token otherlv_4=null;
        Token lv_dbName_5_0=null;
        Token otherlv_6=null;
        Token lv_name_7_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:167:28: ( (otherlv_0= 'mongo://' ( (lv_url_1_0= RULE_ID ) ) (otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) ) )? otherlv_4= '/' ( (lv_dbName_5_0= RULE_ID ) ) otherlv_6= '/' ( (lv_name_7_0= RULE_ID ) ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:168:1: (otherlv_0= 'mongo://' ( (lv_url_1_0= RULE_ID ) ) (otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) ) )? otherlv_4= '/' ( (lv_dbName_5_0= RULE_ID ) ) otherlv_6= '/' ( (lv_name_7_0= RULE_ID ) ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:168:1: (otherlv_0= 'mongo://' ( (lv_url_1_0= RULE_ID ) ) (otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) ) )? otherlv_4= '/' ( (lv_dbName_5_0= RULE_ID ) ) otherlv_6= '/' ( (lv_name_7_0= RULE_ID ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:168:3: otherlv_0= 'mongo://' ( (lv_url_1_0= RULE_ID ) ) (otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) ) )? otherlv_4= '/' ( (lv_dbName_5_0= RULE_ID ) ) otherlv_6= '/' ( (lv_name_7_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,16,FOLLOW_16_in_ruleDatabase295); 

                	newLeafNode(otherlv_0, grammarAccess.getDatabaseAccess().getMongoKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:172:1: ( (lv_url_1_0= RULE_ID ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:173:1: (lv_url_1_0= RULE_ID )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:173:1: (lv_url_1_0= RULE_ID )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:174:3: lv_url_1_0= RULE_ID
            {
            lv_url_1_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDatabase312); 

            			newLeafNode(lv_url_1_0, grammarAccess.getDatabaseAccess().getUrlIDTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDatabaseRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"url",
                    		lv_url_1_0, 
                    		"ID");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:190:2: (otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==17) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:190:4: otherlv_2= ':' ( (lv_port_3_0= RULE_SINGED_LONG ) )
                    {
                    otherlv_2=(Token)match(input,17,FOLLOW_17_in_ruleDatabase330); 

                        	newLeafNode(otherlv_2, grammarAccess.getDatabaseAccess().getColonKeyword_2_0());
                        
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:194:1: ( (lv_port_3_0= RULE_SINGED_LONG ) )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:195:1: (lv_port_3_0= RULE_SINGED_LONG )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:195:1: (lv_port_3_0= RULE_SINGED_LONG )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:196:3: lv_port_3_0= RULE_SINGED_LONG
                    {
                    lv_port_3_0=(Token)match(input,RULE_SINGED_LONG,FOLLOW_RULE_SINGED_LONG_in_ruleDatabase347); 

                    			newLeafNode(lv_port_3_0, grammarAccess.getDatabaseAccess().getPortSINGED_LONGTerminalRuleCall_2_1_0()); 
                    		

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getDatabaseRule());
                    	        }
                           		setWithLastConsumed(
                           			current, 
                           			"port",
                            		lv_port_3_0, 
                            		"SINGED_LONG");
                    	    

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,18,FOLLOW_18_in_ruleDatabase366); 

                	newLeafNode(otherlv_4, grammarAccess.getDatabaseAccess().getSolidusKeyword_3());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:216:1: ( (lv_dbName_5_0= RULE_ID ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:217:1: (lv_dbName_5_0= RULE_ID )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:217:1: (lv_dbName_5_0= RULE_ID )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:218:3: lv_dbName_5_0= RULE_ID
            {
            lv_dbName_5_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDatabase383); 

            			newLeafNode(lv_dbName_5_0, grammarAccess.getDatabaseAccess().getDbNameIDTerminalRuleCall_4_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDatabaseRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"dbName",
                    		lv_dbName_5_0, 
                    		"ID");
            	    

            }


            }

            otherlv_6=(Token)match(input,18,FOLLOW_18_in_ruleDatabase400); 

                	newLeafNode(otherlv_6, grammarAccess.getDatabaseAccess().getSolidusKeyword_5());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:238:1: ( (lv_name_7_0= RULE_ID ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:239:1: (lv_name_7_0= RULE_ID )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:239:1: (lv_name_7_0= RULE_ID )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:240:3: lv_name_7_0= RULE_ID
            {
            lv_name_7_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleDatabase417); 

            			newLeafNode(lv_name_7_0, grammarAccess.getDatabaseAccess().getNameIDTerminalRuleCall_6_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDatabaseRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_7_0, 
                    		"ID");
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDatabase"


    // $ANTLR start "entryRuleColumnList"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:264:1: entryRuleColumnList returns [String current=null] : iv_ruleColumnList= ruleColumnList EOF ;
    public final String entryRuleColumnList() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleColumnList = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:265:2: (iv_ruleColumnList= ruleColumnList EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:266:2: iv_ruleColumnList= ruleColumnList EOF
            {
             newCompositeNode(grammarAccess.getColumnListRule()); 
            pushFollow(FOLLOW_ruleColumnList_in_entryRuleColumnList459);
            iv_ruleColumnList=ruleColumnList();

            state._fsp--;

             current =iv_ruleColumnList.getText(); 
            match(input,EOF,FOLLOW_EOF_in_entryRuleColumnList470); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleColumnList"


    // $ANTLR start "ruleColumnList"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:273:1: ruleColumnList returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )* ) | kw= '*' ) ;
    public final AntlrDatatypeRuleToken ruleColumnList() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:276:28: ( ( (this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )* ) | kw= '*' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:277:1: ( (this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )* ) | kw= '*' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:277:1: ( (this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )* ) | kw= '*' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RULE_ID) ) {
                alt4=1;
            }
            else if ( (LA4_0==20) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:277:2: (this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )* )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:277:2: (this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )* )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:277:7: this_ID_0= RULE_ID (kw= ',' this_ID_2= RULE_ID )*
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleColumnList511); 

                    		current.merge(this_ID_0);
                        
                     
                        newLeafNode(this_ID_0, grammarAccess.getColumnListAccess().getIDTerminalRuleCall_0_0()); 
                        
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:284:1: (kw= ',' this_ID_2= RULE_ID )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==19) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:285:2: kw= ',' this_ID_2= RULE_ID
                    	    {
                    	    kw=(Token)match(input,19,FOLLOW_19_in_ruleColumnList530); 

                    	            current.merge(kw);
                    	            newLeafNode(kw, grammarAccess.getColumnListAccess().getCommaKeyword_0_1_0()); 
                    	        
                    	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleColumnList545); 

                    	    		current.merge(this_ID_2);
                    	        
                    	     
                    	        newLeafNode(this_ID_2, grammarAccess.getColumnListAccess().getIDTerminalRuleCall_0_1_1()); 
                    	        

                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:299:2: kw= '*'
                    {
                    kw=(Token)match(input,20,FOLLOW_20_in_ruleColumnList572); 

                            current.merge(kw);
                            newLeafNode(kw, grammarAccess.getColumnListAccess().getAsteriskKeyword_1()); 
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleColumnList"


    // $ANTLR start "entryRuleWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:312:1: entryRuleWhereEntry returns [EObject current=null] : iv_ruleWhereEntry= ruleWhereEntry EOF ;
    public final EObject entryRuleWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:313:2: (iv_ruleWhereEntry= ruleWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:314:2: iv_ruleWhereEntry= ruleWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getWhereEntryRule()); 
            pushFollow(FOLLOW_ruleWhereEntry_in_entryRuleWhereEntry612);
            iv_ruleWhereEntry=ruleWhereEntry();

            state._fsp--;

             current =iv_ruleWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleWhereEntry622); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWhereEntry"


    // $ANTLR start "ruleWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:321:1: ruleWhereEntry returns [EObject current=null] : (this_AndWhereEntry_0= ruleAndWhereEntry ( () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+ )? ) ;
    public final EObject ruleWhereEntry() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_AndWhereEntry_0 = null;

        EObject lv_entries_3_0 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:324:28: ( (this_AndWhereEntry_0= ruleAndWhereEntry ( () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+ )? ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:325:1: (this_AndWhereEntry_0= ruleAndWhereEntry ( () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+ )? )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:325:1: (this_AndWhereEntry_0= ruleAndWhereEntry ( () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+ )? )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:326:5: this_AndWhereEntry_0= ruleAndWhereEntry ( () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+ )?
            {
             
                    newCompositeNode(grammarAccess.getWhereEntryAccess().getAndWhereEntryParserRuleCall_0()); 
                
            pushFollow(FOLLOW_ruleAndWhereEntry_in_ruleWhereEntry669);
            this_AndWhereEntry_0=ruleAndWhereEntry();

            state._fsp--;

             
                    current = this_AndWhereEntry_0; 
                    afterParserOrEnumRuleCall();
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:334:1: ( () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+ )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==21) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:334:2: () (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:334:2: ()
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:335:5: 
                    {

                            current = forceCreateModelElementAndAdd(
                                grammarAccess.getWhereEntryAccess().getOrWhereEntryEntriesAction_1_0(),
                                current);
                        

                    }

                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:340:2: (otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) ) )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==21) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:340:4: otherlv_2= 'OR' ( (lv_entries_3_0= ruleAndWhereEntry ) )
                    	    {
                    	    otherlv_2=(Token)match(input,21,FOLLOW_21_in_ruleWhereEntry691); 

                    	        	newLeafNode(otherlv_2, grammarAccess.getWhereEntryAccess().getORKeyword_1_1_0());
                    	        
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:344:1: ( (lv_entries_3_0= ruleAndWhereEntry ) )
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:345:1: (lv_entries_3_0= ruleAndWhereEntry )
                    	    {
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:345:1: (lv_entries_3_0= ruleAndWhereEntry )
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:346:3: lv_entries_3_0= ruleAndWhereEntry
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getWhereEntryAccess().getEntriesAndWhereEntryParserRuleCall_1_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleAndWhereEntry_in_ruleWhereEntry712);
                    	    lv_entries_3_0=ruleAndWhereEntry();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getWhereEntryRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"entries",
                    	            		lv_entries_3_0, 
                    	            		"AndWhereEntry");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);


                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWhereEntry"


    // $ANTLR start "entryRuleAndWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:370:1: entryRuleAndWhereEntry returns [EObject current=null] : iv_ruleAndWhereEntry= ruleAndWhereEntry EOF ;
    public final EObject entryRuleAndWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAndWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:371:2: (iv_ruleAndWhereEntry= ruleAndWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:372:2: iv_ruleAndWhereEntry= ruleAndWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getAndWhereEntryRule()); 
            pushFollow(FOLLOW_ruleAndWhereEntry_in_entryRuleAndWhereEntry752);
            iv_ruleAndWhereEntry=ruleAndWhereEntry();

            state._fsp--;

             current =iv_ruleAndWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAndWhereEntry762); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAndWhereEntry"


    // $ANTLR start "ruleAndWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:379:1: ruleAndWhereEntry returns [EObject current=null] : (this_ConcreteWhereEntry_0= ruleConcreteWhereEntry ( () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+ )? ) ;
    public final EObject ruleAndWhereEntry() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_ConcreteWhereEntry_0 = null;

        EObject lv_entries_3_0 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:382:28: ( (this_ConcreteWhereEntry_0= ruleConcreteWhereEntry ( () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+ )? ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:383:1: (this_ConcreteWhereEntry_0= ruleConcreteWhereEntry ( () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+ )? )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:383:1: (this_ConcreteWhereEntry_0= ruleConcreteWhereEntry ( () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+ )? )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:384:5: this_ConcreteWhereEntry_0= ruleConcreteWhereEntry ( () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+ )?
            {
             
                    newCompositeNode(grammarAccess.getAndWhereEntryAccess().getConcreteWhereEntryParserRuleCall_0()); 
                
            pushFollow(FOLLOW_ruleConcreteWhereEntry_in_ruleAndWhereEntry809);
            this_ConcreteWhereEntry_0=ruleConcreteWhereEntry();

            state._fsp--;

             
                    current = this_ConcreteWhereEntry_0; 
                    afterParserOrEnumRuleCall();
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:392:1: ( () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+ )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==22) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:392:2: () (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:392:2: ()
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:393:5: 
                    {

                            current = forceCreateModelElementAndAdd(
                                grammarAccess.getAndWhereEntryAccess().getAndWhereEntryEntriesAction_1_0(),
                                current);
                        

                    }

                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:398:2: (otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) ) )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==22) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:398:4: otherlv_2= 'AND' ( (lv_entries_3_0= ruleConcreteWhereEntry ) )
                    	    {
                    	    otherlv_2=(Token)match(input,22,FOLLOW_22_in_ruleAndWhereEntry831); 

                    	        	newLeafNode(otherlv_2, grammarAccess.getAndWhereEntryAccess().getANDKeyword_1_1_0());
                    	        
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:402:1: ( (lv_entries_3_0= ruleConcreteWhereEntry ) )
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:403:1: (lv_entries_3_0= ruleConcreteWhereEntry )
                    	    {
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:403:1: (lv_entries_3_0= ruleConcreteWhereEntry )
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:404:3: lv_entries_3_0= ruleConcreteWhereEntry
                    	    {
                    	     
                    	    	        newCompositeNode(grammarAccess.getAndWhereEntryAccess().getEntriesConcreteWhereEntryParserRuleCall_1_1_1_0()); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleConcreteWhereEntry_in_ruleAndWhereEntry852);
                    	    lv_entries_3_0=ruleConcreteWhereEntry();

                    	    state._fsp--;


                    	    	        if (current==null) {
                    	    	            current = createModelElementForParent(grammarAccess.getAndWhereEntryRule());
                    	    	        }
                    	           		add(
                    	           			current, 
                    	           			"entries",
                    	            		lv_entries_3_0, 
                    	            		"ConcreteWhereEntry");
                    	    	        afterParserOrEnumRuleCall();
                    	    	    

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    }
                    break;

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAndWhereEntry"


    // $ANTLR start "entryRuleConcreteWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:428:1: entryRuleConcreteWhereEntry returns [EObject current=null] : iv_ruleConcreteWhereEntry= ruleConcreteWhereEntry EOF ;
    public final EObject entryRuleConcreteWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConcreteWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:429:2: (iv_ruleConcreteWhereEntry= ruleConcreteWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:430:2: iv_ruleConcreteWhereEntry= ruleConcreteWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getConcreteWhereEntryRule()); 
            pushFollow(FOLLOW_ruleConcreteWhereEntry_in_entryRuleConcreteWhereEntry892);
            iv_ruleConcreteWhereEntry=ruleConcreteWhereEntry();

            state._fsp--;

             current =iv_ruleConcreteWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleConcreteWhereEntry902); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConcreteWhereEntry"


    // $ANTLR start "ruleConcreteWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:437:1: ruleConcreteWhereEntry returns [EObject current=null] : (this_ParWhereEntry_0= ruleParWhereEntry | this_ExpressionWhereEntry_1= ruleExpressionWhereEntry ) ;
    public final EObject ruleConcreteWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject this_ParWhereEntry_0 = null;

        EObject this_ExpressionWhereEntry_1 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:440:28: ( (this_ParWhereEntry_0= ruleParWhereEntry | this_ExpressionWhereEntry_1= ruleExpressionWhereEntry ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:441:1: (this_ParWhereEntry_0= ruleParWhereEntry | this_ExpressionWhereEntry_1= ruleExpressionWhereEntry )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:441:1: (this_ParWhereEntry_0= ruleParWhereEntry | this_ExpressionWhereEntry_1= ruleExpressionWhereEntry )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==23) ) {
                alt9=1;
            }
            else if ( (LA9_0==RULE_ID) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:442:5: this_ParWhereEntry_0= ruleParWhereEntry
                    {
                     
                            newCompositeNode(grammarAccess.getConcreteWhereEntryAccess().getParWhereEntryParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleParWhereEntry_in_ruleConcreteWhereEntry949);
                    this_ParWhereEntry_0=ruleParWhereEntry();

                    state._fsp--;

                     
                            current = this_ParWhereEntry_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:452:5: this_ExpressionWhereEntry_1= ruleExpressionWhereEntry
                    {
                     
                            newCompositeNode(grammarAccess.getConcreteWhereEntryAccess().getExpressionWhereEntryParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleExpressionWhereEntry_in_ruleConcreteWhereEntry976);
                    this_ExpressionWhereEntry_1=ruleExpressionWhereEntry();

                    state._fsp--;

                     
                            current = this_ExpressionWhereEntry_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConcreteWhereEntry"


    // $ANTLR start "entryRuleParWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:468:1: entryRuleParWhereEntry returns [EObject current=null] : iv_ruleParWhereEntry= ruleParWhereEntry EOF ;
    public final EObject entryRuleParWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:469:2: (iv_ruleParWhereEntry= ruleParWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:470:2: iv_ruleParWhereEntry= ruleParWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getParWhereEntryRule()); 
            pushFollow(FOLLOW_ruleParWhereEntry_in_entryRuleParWhereEntry1011);
            iv_ruleParWhereEntry=ruleParWhereEntry();

            state._fsp--;

             current =iv_ruleParWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleParWhereEntry1021); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParWhereEntry"


    // $ANTLR start "ruleParWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:477:1: ruleParWhereEntry returns [EObject current=null] : (otherlv_0= '(' this_WhereEntry_1= ruleWhereEntry otherlv_2= ')' ) ;
    public final EObject ruleParWhereEntry() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_WhereEntry_1 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:480:28: ( (otherlv_0= '(' this_WhereEntry_1= ruleWhereEntry otherlv_2= ')' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:481:1: (otherlv_0= '(' this_WhereEntry_1= ruleWhereEntry otherlv_2= ')' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:481:1: (otherlv_0= '(' this_WhereEntry_1= ruleWhereEntry otherlv_2= ')' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:481:3: otherlv_0= '(' this_WhereEntry_1= ruleWhereEntry otherlv_2= ')'
            {
            otherlv_0=(Token)match(input,23,FOLLOW_23_in_ruleParWhereEntry1058); 

                	newLeafNode(otherlv_0, grammarAccess.getParWhereEntryAccess().getLeftParenthesisKeyword_0());
                
             
                    newCompositeNode(grammarAccess.getParWhereEntryAccess().getWhereEntryParserRuleCall_1()); 
                
            pushFollow(FOLLOW_ruleWhereEntry_in_ruleParWhereEntry1080);
            this_WhereEntry_1=ruleWhereEntry();

            state._fsp--;

             
                    current = this_WhereEntry_1; 
                    afterParserOrEnumRuleCall();
                
            otherlv_2=(Token)match(input,24,FOLLOW_24_in_ruleParWhereEntry1091); 

                	newLeafNode(otherlv_2, grammarAccess.getParWhereEntryAccess().getRightParenthesisKeyword_2());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParWhereEntry"


    // $ANTLR start "entryRuleExpressionWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:506:1: entryRuleExpressionWhereEntry returns [EObject current=null] : iv_ruleExpressionWhereEntry= ruleExpressionWhereEntry EOF ;
    public final EObject entryRuleExpressionWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpressionWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:507:2: (iv_ruleExpressionWhereEntry= ruleExpressionWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:508:2: iv_ruleExpressionWhereEntry= ruleExpressionWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getExpressionWhereEntryRule()); 
            pushFollow(FOLLOW_ruleExpressionWhereEntry_in_entryRuleExpressionWhereEntry1127);
            iv_ruleExpressionWhereEntry=ruleExpressionWhereEntry();

            state._fsp--;

             current =iv_ruleExpressionWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleExpressionWhereEntry1137); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpressionWhereEntry"


    // $ANTLR start "ruleExpressionWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:515:1: ruleExpressionWhereEntry returns [EObject current=null] : (this_SingleExpressionWhereEntry_0= ruleSingleExpressionWhereEntry | this_MultiExpressionWhereEntry_1= ruleMultiExpressionWhereEntry ) ;
    public final EObject ruleExpressionWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject this_SingleExpressionWhereEntry_0 = null;

        EObject this_MultiExpressionWhereEntry_1 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:518:28: ( (this_SingleExpressionWhereEntry_0= ruleSingleExpressionWhereEntry | this_MultiExpressionWhereEntry_1= ruleMultiExpressionWhereEntry ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:519:1: (this_SingleExpressionWhereEntry_0= ruleSingleExpressionWhereEntry | this_MultiExpressionWhereEntry_1= ruleMultiExpressionWhereEntry )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:519:1: (this_SingleExpressionWhereEntry_0= ruleSingleExpressionWhereEntry | this_MultiExpressionWhereEntry_1= ruleMultiExpressionWhereEntry )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 31:
                case 32:
                case 34:
                    {
                    alt10=2;
                    }
                    break;
                case 33:
                    {
                    int LA10_3 = input.LA(3);

                    if ( ((LA10_3>=RULE_SINGED_LONG && LA10_3<=RULE_DATE)||(LA10_3>=25 && LA10_3<=28)) ) {
                        alt10=1;
                    }
                    else if ( (LA10_3==29) ) {
                        alt10=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case 35:
                    {
                    int LA10_4 = input.LA(3);

                    if ( (LA10_4==29) ) {
                        alt10=2;
                    }
                    else if ( ((LA10_4>=RULE_SINGED_LONG && LA10_4<=RULE_DATE)||(LA10_4>=25 && LA10_4<=28)) ) {
                        alt10=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                    {
                    alt10=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:520:5: this_SingleExpressionWhereEntry_0= ruleSingleExpressionWhereEntry
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionWhereEntryAccess().getSingleExpressionWhereEntryParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleSingleExpressionWhereEntry_in_ruleExpressionWhereEntry1184);
                    this_SingleExpressionWhereEntry_0=ruleSingleExpressionWhereEntry();

                    state._fsp--;

                     
                            current = this_SingleExpressionWhereEntry_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:530:5: this_MultiExpressionWhereEntry_1= ruleMultiExpressionWhereEntry
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionWhereEntryAccess().getMultiExpressionWhereEntryParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleMultiExpressionWhereEntry_in_ruleExpressionWhereEntry1211);
                    this_MultiExpressionWhereEntry_1=ruleMultiExpressionWhereEntry();

                    state._fsp--;

                     
                            current = this_MultiExpressionWhereEntry_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpressionWhereEntry"


    // $ANTLR start "entryRuleSingleExpressionWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:546:1: entryRuleSingleExpressionWhereEntry returns [EObject current=null] : iv_ruleSingleExpressionWhereEntry= ruleSingleExpressionWhereEntry EOF ;
    public final EObject entryRuleSingleExpressionWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSingleExpressionWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:547:2: (iv_ruleSingleExpressionWhereEntry= ruleSingleExpressionWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:548:2: iv_ruleSingleExpressionWhereEntry= ruleSingleExpressionWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getSingleExpressionWhereEntryRule()); 
            pushFollow(FOLLOW_ruleSingleExpressionWhereEntry_in_entryRuleSingleExpressionWhereEntry1246);
            iv_ruleSingleExpressionWhereEntry=ruleSingleExpressionWhereEntry();

            state._fsp--;

             current =iv_ruleSingleExpressionWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSingleExpressionWhereEntry1256); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSingleExpressionWhereEntry"


    // $ANTLR start "ruleSingleExpressionWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:555:1: ruleSingleExpressionWhereEntry returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_rhs_2_0= ruleExpression ) ) ) ;
    public final EObject ruleSingleExpressionWhereEntry() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Enumerator lv_operator_1_0 = null;

        EObject lv_rhs_2_0 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:558:28: ( ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_rhs_2_0= ruleExpression ) ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:559:1: ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_rhs_2_0= ruleExpression ) ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:559:1: ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_rhs_2_0= ruleExpression ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:559:2: ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleOperator ) ) ( (lv_rhs_2_0= ruleExpression ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:559:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:560:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:560:1: (lv_name_0_0= RULE_ID )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:561:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSingleExpressionWhereEntry1298); 

            			newLeafNode(lv_name_0_0, grammarAccess.getSingleExpressionWhereEntryAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getSingleExpressionWhereEntryRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:577:2: ( (lv_operator_1_0= ruleOperator ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:578:1: (lv_operator_1_0= ruleOperator )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:578:1: (lv_operator_1_0= ruleOperator )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:579:3: lv_operator_1_0= ruleOperator
            {
             
            	        newCompositeNode(grammarAccess.getSingleExpressionWhereEntryAccess().getOperatorOperatorEnumRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleOperator_in_ruleSingleExpressionWhereEntry1324);
            lv_operator_1_0=ruleOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSingleExpressionWhereEntryRule());
            	        }
                   		set(
                   			current, 
                   			"operator",
                    		lv_operator_1_0, 
                    		"Operator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:595:2: ( (lv_rhs_2_0= ruleExpression ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:596:1: (lv_rhs_2_0= ruleExpression )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:596:1: (lv_rhs_2_0= ruleExpression )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:597:3: lv_rhs_2_0= ruleExpression
            {
             
            	        newCompositeNode(grammarAccess.getSingleExpressionWhereEntryAccess().getRhsExpressionParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleExpression_in_ruleSingleExpressionWhereEntry1345);
            lv_rhs_2_0=ruleExpression();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getSingleExpressionWhereEntryRule());
            	        }
                   		set(
                   			current, 
                   			"rhs",
                    		lv_rhs_2_0, 
                    		"Expression");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSingleExpressionWhereEntry"


    // $ANTLR start "entryRuleExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:621:1: entryRuleExpression returns [EObject current=null] : iv_ruleExpression= ruleExpression EOF ;
    public final EObject entryRuleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:622:2: (iv_ruleExpression= ruleExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:623:2: iv_ruleExpression= ruleExpression EOF
            {
             newCompositeNode(grammarAccess.getExpressionRule()); 
            pushFollow(FOLLOW_ruleExpression_in_entryRuleExpression1381);
            iv_ruleExpression=ruleExpression();

            state._fsp--;

             current =iv_ruleExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleExpression1391); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpression"


    // $ANTLR start "ruleExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:630:1: ruleExpression returns [EObject current=null] : (this_DoubleExpression_0= ruleDoubleExpression | this_LongExpression_1= ruleLongExpression | this_StringExpression_2= ruleStringExpression | this_NullExpression_3= ruleNullExpression | this_DateExpression_4= ruleDateExpression | this_BooleanExpression_5= ruleBooleanExpression | this_ReplacableValue_6= ruleReplacableValue ) ;
    public final EObject ruleExpression() throws RecognitionException {
        EObject current = null;

        EObject this_DoubleExpression_0 = null;

        EObject this_LongExpression_1 = null;

        EObject this_StringExpression_2 = null;

        EObject this_NullExpression_3 = null;

        EObject this_DateExpression_4 = null;

        EObject this_BooleanExpression_5 = null;

        EObject this_ReplacableValue_6 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:633:28: ( (this_DoubleExpression_0= ruleDoubleExpression | this_LongExpression_1= ruleLongExpression | this_StringExpression_2= ruleStringExpression | this_NullExpression_3= ruleNullExpression | this_DateExpression_4= ruleDateExpression | this_BooleanExpression_5= ruleBooleanExpression | this_ReplacableValue_6= ruleReplacableValue ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:634:1: (this_DoubleExpression_0= ruleDoubleExpression | this_LongExpression_1= ruleLongExpression | this_StringExpression_2= ruleStringExpression | this_NullExpression_3= ruleNullExpression | this_DateExpression_4= ruleDateExpression | this_BooleanExpression_5= ruleBooleanExpression | this_ReplacableValue_6= ruleReplacableValue )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:634:1: (this_DoubleExpression_0= ruleDoubleExpression | this_LongExpression_1= ruleLongExpression | this_StringExpression_2= ruleStringExpression | this_NullExpression_3= ruleNullExpression | this_DateExpression_4= ruleDateExpression | this_BooleanExpression_5= ruleBooleanExpression | this_ReplacableValue_6= ruleReplacableValue )
            int alt11=7;
            switch ( input.LA(1) ) {
            case RULE_SIGNED_DOUBLE:
                {
                alt11=1;
                }
                break;
            case RULE_SINGED_LONG:
                {
                alt11=2;
                }
                break;
            case RULE_STRING:
                {
                alt11=3;
                }
                break;
            case 26:
                {
                alt11=4;
                }
                break;
            case RULE_DATE:
                {
                alt11=5;
                }
                break;
            case 27:
            case 28:
                {
                alt11=6;
                }
                break;
            case 25:
                {
                alt11=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:635:5: this_DoubleExpression_0= ruleDoubleExpression
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getDoubleExpressionParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleDoubleExpression_in_ruleExpression1438);
                    this_DoubleExpression_0=ruleDoubleExpression();

                    state._fsp--;

                     
                            current = this_DoubleExpression_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:645:5: this_LongExpression_1= ruleLongExpression
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getLongExpressionParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleLongExpression_in_ruleExpression1465);
                    this_LongExpression_1=ruleLongExpression();

                    state._fsp--;

                     
                            current = this_LongExpression_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:655:5: this_StringExpression_2= ruleStringExpression
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getStringExpressionParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleStringExpression_in_ruleExpression1492);
                    this_StringExpression_2=ruleStringExpression();

                    state._fsp--;

                     
                            current = this_StringExpression_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:665:5: this_NullExpression_3= ruleNullExpression
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getNullExpressionParserRuleCall_3()); 
                        
                    pushFollow(FOLLOW_ruleNullExpression_in_ruleExpression1519);
                    this_NullExpression_3=ruleNullExpression();

                    state._fsp--;

                     
                            current = this_NullExpression_3; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 5 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:675:5: this_DateExpression_4= ruleDateExpression
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getDateExpressionParserRuleCall_4()); 
                        
                    pushFollow(FOLLOW_ruleDateExpression_in_ruleExpression1546);
                    this_DateExpression_4=ruleDateExpression();

                    state._fsp--;

                     
                            current = this_DateExpression_4; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 6 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:685:5: this_BooleanExpression_5= ruleBooleanExpression
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getBooleanExpressionParserRuleCall_5()); 
                        
                    pushFollow(FOLLOW_ruleBooleanExpression_in_ruleExpression1573);
                    this_BooleanExpression_5=ruleBooleanExpression();

                    state._fsp--;

                     
                            current = this_BooleanExpression_5; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 7 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:695:5: this_ReplacableValue_6= ruleReplacableValue
                    {
                     
                            newCompositeNode(grammarAccess.getExpressionAccess().getReplacableValueParserRuleCall_6()); 
                        
                    pushFollow(FOLLOW_ruleReplacableValue_in_ruleExpression1600);
                    this_ReplacableValue_6=ruleReplacableValue();

                    state._fsp--;

                     
                            current = this_ReplacableValue_6; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpression"


    // $ANTLR start "entryRuleReplacableValue"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:711:1: entryRuleReplacableValue returns [EObject current=null] : iv_ruleReplacableValue= ruleReplacableValue EOF ;
    public final EObject entryRuleReplacableValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReplacableValue = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:712:2: (iv_ruleReplacableValue= ruleReplacableValue EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:713:2: iv_ruleReplacableValue= ruleReplacableValue EOF
            {
             newCompositeNode(grammarAccess.getReplacableValueRule()); 
            pushFollow(FOLLOW_ruleReplacableValue_in_entryRuleReplacableValue1635);
            iv_ruleReplacableValue=ruleReplacableValue();

            state._fsp--;

             current =iv_ruleReplacableValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleReplacableValue1645); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleReplacableValue"


    // $ANTLR start "ruleReplacableValue"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:720:1: ruleReplacableValue returns [EObject current=null] : ( (lv_value_0_0= '?' ) ) ;
    public final EObject ruleReplacableValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:723:28: ( ( (lv_value_0_0= '?' ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:724:1: ( (lv_value_0_0= '?' ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:724:1: ( (lv_value_0_0= '?' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:725:1: (lv_value_0_0= '?' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:725:1: (lv_value_0_0= '?' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:726:3: lv_value_0_0= '?'
            {
            lv_value_0_0=(Token)match(input,25,FOLLOW_25_in_ruleReplacableValue1687); 

                    newLeafNode(lv_value_0_0, grammarAccess.getReplacableValueAccess().getValueQuestionMarkKeyword_0());
                

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getReplacableValueRule());
            	        }
                   		setWithLastConsumed(current, "value", lv_value_0_0, "?");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleReplacableValue"


    // $ANTLR start "entryRuleDoubleExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:747:1: entryRuleDoubleExpression returns [EObject current=null] : iv_ruleDoubleExpression= ruleDoubleExpression EOF ;
    public final EObject entryRuleDoubleExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDoubleExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:748:2: (iv_ruleDoubleExpression= ruleDoubleExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:749:2: iv_ruleDoubleExpression= ruleDoubleExpression EOF
            {
             newCompositeNode(grammarAccess.getDoubleExpressionRule()); 
            pushFollow(FOLLOW_ruleDoubleExpression_in_entryRuleDoubleExpression1735);
            iv_ruleDoubleExpression=ruleDoubleExpression();

            state._fsp--;

             current =iv_ruleDoubleExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDoubleExpression1745); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDoubleExpression"


    // $ANTLR start "ruleDoubleExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:756:1: ruleDoubleExpression returns [EObject current=null] : ( (lv_value_0_0= RULE_SIGNED_DOUBLE ) ) ;
    public final EObject ruleDoubleExpression() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:759:28: ( ( (lv_value_0_0= RULE_SIGNED_DOUBLE ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:760:1: ( (lv_value_0_0= RULE_SIGNED_DOUBLE ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:760:1: ( (lv_value_0_0= RULE_SIGNED_DOUBLE ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:761:1: (lv_value_0_0= RULE_SIGNED_DOUBLE )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:761:1: (lv_value_0_0= RULE_SIGNED_DOUBLE )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:762:3: lv_value_0_0= RULE_SIGNED_DOUBLE
            {
            lv_value_0_0=(Token)match(input,RULE_SIGNED_DOUBLE,FOLLOW_RULE_SIGNED_DOUBLE_in_ruleDoubleExpression1786); 

            			newLeafNode(lv_value_0_0, grammarAccess.getDoubleExpressionAccess().getValueSIGNED_DOUBLETerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDoubleExpressionRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"value",
                    		lv_value_0_0, 
                    		"SIGNED_DOUBLE");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDoubleExpression"


    // $ANTLR start "entryRuleLongExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:786:1: entryRuleLongExpression returns [EObject current=null] : iv_ruleLongExpression= ruleLongExpression EOF ;
    public final EObject entryRuleLongExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLongExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:787:2: (iv_ruleLongExpression= ruleLongExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:788:2: iv_ruleLongExpression= ruleLongExpression EOF
            {
             newCompositeNode(grammarAccess.getLongExpressionRule()); 
            pushFollow(FOLLOW_ruleLongExpression_in_entryRuleLongExpression1826);
            iv_ruleLongExpression=ruleLongExpression();

            state._fsp--;

             current =iv_ruleLongExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLongExpression1836); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLongExpression"


    // $ANTLR start "ruleLongExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:795:1: ruleLongExpression returns [EObject current=null] : ( (lv_value_0_0= RULE_SINGED_LONG ) ) ;
    public final EObject ruleLongExpression() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:798:28: ( ( (lv_value_0_0= RULE_SINGED_LONG ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:799:1: ( (lv_value_0_0= RULE_SINGED_LONG ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:799:1: ( (lv_value_0_0= RULE_SINGED_LONG ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:800:1: (lv_value_0_0= RULE_SINGED_LONG )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:800:1: (lv_value_0_0= RULE_SINGED_LONG )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:801:3: lv_value_0_0= RULE_SINGED_LONG
            {
            lv_value_0_0=(Token)match(input,RULE_SINGED_LONG,FOLLOW_RULE_SINGED_LONG_in_ruleLongExpression1877); 

            			newLeafNode(lv_value_0_0, grammarAccess.getLongExpressionAccess().getValueSINGED_LONGTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getLongExpressionRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"value",
                    		lv_value_0_0, 
                    		"SINGED_LONG");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLongExpression"


    // $ANTLR start "entryRuleStringExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:825:1: entryRuleStringExpression returns [EObject current=null] : iv_ruleStringExpression= ruleStringExpression EOF ;
    public final EObject entryRuleStringExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStringExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:826:2: (iv_ruleStringExpression= ruleStringExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:827:2: iv_ruleStringExpression= ruleStringExpression EOF
            {
             newCompositeNode(grammarAccess.getStringExpressionRule()); 
            pushFollow(FOLLOW_ruleStringExpression_in_entryRuleStringExpression1917);
            iv_ruleStringExpression=ruleStringExpression();

            state._fsp--;

             current =iv_ruleStringExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleStringExpression1927); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStringExpression"


    // $ANTLR start "ruleStringExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:834:1: ruleStringExpression returns [EObject current=null] : ( (lv_value_0_0= RULE_STRING ) ) ;
    public final EObject ruleStringExpression() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:837:28: ( ( (lv_value_0_0= RULE_STRING ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:838:1: ( (lv_value_0_0= RULE_STRING ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:838:1: ( (lv_value_0_0= RULE_STRING ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:839:1: (lv_value_0_0= RULE_STRING )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:839:1: (lv_value_0_0= RULE_STRING )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:840:3: lv_value_0_0= RULE_STRING
            {
            lv_value_0_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleStringExpression1968); 

            			newLeafNode(lv_value_0_0, grammarAccess.getStringExpressionAccess().getValueSTRINGTerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getStringExpressionRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"value",
                    		lv_value_0_0, 
                    		"STRING");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStringExpression"


    // $ANTLR start "entryRuleNullExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:864:1: entryRuleNullExpression returns [EObject current=null] : iv_ruleNullExpression= ruleNullExpression EOF ;
    public final EObject entryRuleNullExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNullExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:865:2: (iv_ruleNullExpression= ruleNullExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:866:2: iv_ruleNullExpression= ruleNullExpression EOF
            {
             newCompositeNode(grammarAccess.getNullExpressionRule()); 
            pushFollow(FOLLOW_ruleNullExpression_in_entryRuleNullExpression2008);
            iv_ruleNullExpression=ruleNullExpression();

            state._fsp--;

             current =iv_ruleNullExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNullExpression2018); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNullExpression"


    // $ANTLR start "ruleNullExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:873:1: ruleNullExpression returns [EObject current=null] : ( (lv_value_0_0= 'null' ) ) ;
    public final EObject ruleNullExpression() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:876:28: ( ( (lv_value_0_0= 'null' ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:877:1: ( (lv_value_0_0= 'null' ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:877:1: ( (lv_value_0_0= 'null' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:878:1: (lv_value_0_0= 'null' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:878:1: (lv_value_0_0= 'null' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:879:3: lv_value_0_0= 'null'
            {
            lv_value_0_0=(Token)match(input,26,FOLLOW_26_in_ruleNullExpression2060); 

                    newLeafNode(lv_value_0_0, grammarAccess.getNullExpressionAccess().getValueNullKeyword_0());
                

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getNullExpressionRule());
            	        }
                   		setWithLastConsumed(current, "value", lv_value_0_0, "null");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNullExpression"


    // $ANTLR start "entryRuleDateExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:900:1: entryRuleDateExpression returns [EObject current=null] : iv_ruleDateExpression= ruleDateExpression EOF ;
    public final EObject entryRuleDateExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDateExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:901:2: (iv_ruleDateExpression= ruleDateExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:902:2: iv_ruleDateExpression= ruleDateExpression EOF
            {
             newCompositeNode(grammarAccess.getDateExpressionRule()); 
            pushFollow(FOLLOW_ruleDateExpression_in_entryRuleDateExpression2108);
            iv_ruleDateExpression=ruleDateExpression();

            state._fsp--;

             current =iv_ruleDateExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDateExpression2118); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDateExpression"


    // $ANTLR start "ruleDateExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:909:1: ruleDateExpression returns [EObject current=null] : ( (lv_value_0_0= RULE_DATE ) ) ;
    public final EObject ruleDateExpression() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:912:28: ( ( (lv_value_0_0= RULE_DATE ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:913:1: ( (lv_value_0_0= RULE_DATE ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:913:1: ( (lv_value_0_0= RULE_DATE ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:914:1: (lv_value_0_0= RULE_DATE )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:914:1: (lv_value_0_0= RULE_DATE )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:915:3: lv_value_0_0= RULE_DATE
            {
            lv_value_0_0=(Token)match(input,RULE_DATE,FOLLOW_RULE_DATE_in_ruleDateExpression2159); 

            			newLeafNode(lv_value_0_0, grammarAccess.getDateExpressionAccess().getValueDATETerminalRuleCall_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDateExpressionRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"value",
                    		lv_value_0_0, 
                    		"DATE");
            	    

            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDateExpression"


    // $ANTLR start "entryRuleBooleanExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:939:1: entryRuleBooleanExpression returns [EObject current=null] : iv_ruleBooleanExpression= ruleBooleanExpression EOF ;
    public final EObject entryRuleBooleanExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBooleanExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:940:2: (iv_ruleBooleanExpression= ruleBooleanExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:941:2: iv_ruleBooleanExpression= ruleBooleanExpression EOF
            {
             newCompositeNode(grammarAccess.getBooleanExpressionRule()); 
            pushFollow(FOLLOW_ruleBooleanExpression_in_entryRuleBooleanExpression2199);
            iv_ruleBooleanExpression=ruleBooleanExpression();

            state._fsp--;

             current =iv_ruleBooleanExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBooleanExpression2209); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBooleanExpression"


    // $ANTLR start "ruleBooleanExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:948:1: ruleBooleanExpression returns [EObject current=null] : ( ( (lv_true_0_0= 'true' ) ) | ( (lv_true_1_0= 'false' ) ) ) ;
    public final EObject ruleBooleanExpression() throws RecognitionException {
        EObject current = null;

        Token lv_true_0_0=null;
        Token lv_true_1_0=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:951:28: ( ( ( (lv_true_0_0= 'true' ) ) | ( (lv_true_1_0= 'false' ) ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:952:1: ( ( (lv_true_0_0= 'true' ) ) | ( (lv_true_1_0= 'false' ) ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:952:1: ( ( (lv_true_0_0= 'true' ) ) | ( (lv_true_1_0= 'false' ) ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==27) ) {
                alt12=1;
            }
            else if ( (LA12_0==28) ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:952:2: ( (lv_true_0_0= 'true' ) )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:952:2: ( (lv_true_0_0= 'true' ) )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:953:1: (lv_true_0_0= 'true' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:953:1: (lv_true_0_0= 'true' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:954:3: lv_true_0_0= 'true'
                    {
                    lv_true_0_0=(Token)match(input,27,FOLLOW_27_in_ruleBooleanExpression2252); 

                            newLeafNode(lv_true_0_0, grammarAccess.getBooleanExpressionAccess().getTrueTrueKeyword_0_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getBooleanExpressionRule());
                    	        }
                           		setWithLastConsumed(current, "true", lv_true_0_0, "true");
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:968:6: ( (lv_true_1_0= 'false' ) )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:968:6: ( (lv_true_1_0= 'false' ) )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:969:1: (lv_true_1_0= 'false' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:969:1: (lv_true_1_0= 'false' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:970:3: lv_true_1_0= 'false'
                    {
                    lv_true_1_0=(Token)match(input,28,FOLLOW_28_in_ruleBooleanExpression2289); 

                            newLeafNode(lv_true_1_0, grammarAccess.getBooleanExpressionAccess().getTrueFalseKeyword_1_0());
                        

                    	        if (current==null) {
                    	            current = createModelElement(grammarAccess.getBooleanExpressionRule());
                    	        }
                           		setWithLastConsumed(current, "true", lv_true_1_0, "false");
                    	    

                    }


                    }


                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBooleanExpression"


    // $ANTLR start "entryRuleMultiExpressionWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:991:1: entryRuleMultiExpressionWhereEntry returns [EObject current=null] : iv_ruleMultiExpressionWhereEntry= ruleMultiExpressionWhereEntry EOF ;
    public final EObject entryRuleMultiExpressionWhereEntry() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMultiExpressionWhereEntry = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:992:2: (iv_ruleMultiExpressionWhereEntry= ruleMultiExpressionWhereEntry EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:993:2: iv_ruleMultiExpressionWhereEntry= ruleMultiExpressionWhereEntry EOF
            {
             newCompositeNode(grammarAccess.getMultiExpressionWhereEntryRule()); 
            pushFollow(FOLLOW_ruleMultiExpressionWhereEntry_in_entryRuleMultiExpressionWhereEntry2338);
            iv_ruleMultiExpressionWhereEntry=ruleMultiExpressionWhereEntry();

            state._fsp--;

             current =iv_ruleMultiExpressionWhereEntry; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMultiExpressionWhereEntry2348); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMultiExpressionWhereEntry"


    // $ANTLR start "ruleMultiExpressionWhereEntry"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1000:1: ruleMultiExpressionWhereEntry returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleArrayOperator ) ) ( (lv_rhs_2_0= ruleArrayExpression ) ) ) ;
    public final EObject ruleMultiExpressionWhereEntry() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Enumerator lv_operator_1_0 = null;

        EObject lv_rhs_2_0 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1003:28: ( ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleArrayOperator ) ) ( (lv_rhs_2_0= ruleArrayExpression ) ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1004:1: ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleArrayOperator ) ) ( (lv_rhs_2_0= ruleArrayExpression ) ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1004:1: ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleArrayOperator ) ) ( (lv_rhs_2_0= ruleArrayExpression ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1004:2: ( (lv_name_0_0= RULE_ID ) ) ( (lv_operator_1_0= ruleArrayOperator ) ) ( (lv_rhs_2_0= ruleArrayExpression ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1004:2: ( (lv_name_0_0= RULE_ID ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1005:1: (lv_name_0_0= RULE_ID )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1005:1: (lv_name_0_0= RULE_ID )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1006:3: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMultiExpressionWhereEntry2390); 

            			newLeafNode(lv_name_0_0, grammarAccess.getMultiExpressionWhereEntryAccess().getNameIDTerminalRuleCall_0_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getMultiExpressionWhereEntryRule());
            	        }
                   		setWithLastConsumed(
                   			current, 
                   			"name",
                    		lv_name_0_0, 
                    		"ID");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1022:2: ( (lv_operator_1_0= ruleArrayOperator ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1023:1: (lv_operator_1_0= ruleArrayOperator )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1023:1: (lv_operator_1_0= ruleArrayOperator )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1024:3: lv_operator_1_0= ruleArrayOperator
            {
             
            	        newCompositeNode(grammarAccess.getMultiExpressionWhereEntryAccess().getOperatorArrayOperatorEnumRuleCall_1_0()); 
            	    
            pushFollow(FOLLOW_ruleArrayOperator_in_ruleMultiExpressionWhereEntry2416);
            lv_operator_1_0=ruleArrayOperator();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getMultiExpressionWhereEntryRule());
            	        }
                   		set(
                   			current, 
                   			"operator",
                    		lv_operator_1_0, 
                    		"ArrayOperator");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1040:2: ( (lv_rhs_2_0= ruleArrayExpression ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1041:1: (lv_rhs_2_0= ruleArrayExpression )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1041:1: (lv_rhs_2_0= ruleArrayExpression )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1042:3: lv_rhs_2_0= ruleArrayExpression
            {
             
            	        newCompositeNode(grammarAccess.getMultiExpressionWhereEntryAccess().getRhsArrayExpressionParserRuleCall_2_0()); 
            	    
            pushFollow(FOLLOW_ruleArrayExpression_in_ruleMultiExpressionWhereEntry2437);
            lv_rhs_2_0=ruleArrayExpression();

            state._fsp--;


            	        if (current==null) {
            	            current = createModelElementForParent(grammarAccess.getMultiExpressionWhereEntryRule());
            	        }
                   		set(
                   			current, 
                   			"rhs",
                    		lv_rhs_2_0, 
                    		"ArrayExpression");
            	        afterParserOrEnumRuleCall();
            	    

            }


            }


            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMultiExpressionWhereEntry"


    // $ANTLR start "entryRuleArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1066:1: entryRuleArrayExpression returns [EObject current=null] : iv_ruleArrayExpression= ruleArrayExpression EOF ;
    public final EObject entryRuleArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1067:2: (iv_ruleArrayExpression= ruleArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1068:2: iv_ruleArrayExpression= ruleArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleArrayExpression_in_entryRuleArrayExpression2473);
            iv_ruleArrayExpression=ruleArrayExpression();

            state._fsp--;

             current =iv_ruleArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleArrayExpression2483); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleArrayExpression"


    // $ANTLR start "ruleArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1075:1: ruleArrayExpression returns [EObject current=null] : (this_DoubleArrayExpression_0= ruleDoubleArrayExpression | this_LongArrayExpression_1= ruleLongArrayExpression | this_StringArrayExpression_2= ruleStringArrayExpression | this_NullArrayExpression_3= ruleNullArrayExpression | this_DateArrayExpression_4= ruleDateArrayExpression | this_BooleanArrayExpression_5= ruleBooleanArrayExpression ) ;
    public final EObject ruleArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject this_DoubleArrayExpression_0 = null;

        EObject this_LongArrayExpression_1 = null;

        EObject this_StringArrayExpression_2 = null;

        EObject this_NullArrayExpression_3 = null;

        EObject this_DateArrayExpression_4 = null;

        EObject this_BooleanArrayExpression_5 = null;


         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1078:28: ( (this_DoubleArrayExpression_0= ruleDoubleArrayExpression | this_LongArrayExpression_1= ruleLongArrayExpression | this_StringArrayExpression_2= ruleStringArrayExpression | this_NullArrayExpression_3= ruleNullArrayExpression | this_DateArrayExpression_4= ruleDateArrayExpression | this_BooleanArrayExpression_5= ruleBooleanArrayExpression ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1079:1: (this_DoubleArrayExpression_0= ruleDoubleArrayExpression | this_LongArrayExpression_1= ruleLongArrayExpression | this_StringArrayExpression_2= ruleStringArrayExpression | this_NullArrayExpression_3= ruleNullArrayExpression | this_DateArrayExpression_4= ruleDateArrayExpression | this_BooleanArrayExpression_5= ruleBooleanArrayExpression )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1079:1: (this_DoubleArrayExpression_0= ruleDoubleArrayExpression | this_LongArrayExpression_1= ruleLongArrayExpression | this_StringArrayExpression_2= ruleStringArrayExpression | this_NullArrayExpression_3= ruleNullArrayExpression | this_DateArrayExpression_4= ruleDateArrayExpression | this_BooleanArrayExpression_5= ruleBooleanArrayExpression )
            int alt13=6;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==29) ) {
                switch ( input.LA(2) ) {
                case RULE_BOOL:
                    {
                    alt13=6;
                    }
                    break;
                case RULE_DATE:
                    {
                    alt13=5;
                    }
                    break;
                case RULE_SIGNED_DOUBLE:
                    {
                    alt13=1;
                    }
                    break;
                case 26:
                    {
                    alt13=4;
                    }
                    break;
                case RULE_STRING:
                    {
                    alt13=3;
                    }
                    break;
                case RULE_SINGED_LONG:
                    {
                    alt13=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1080:5: this_DoubleArrayExpression_0= ruleDoubleArrayExpression
                    {
                     
                            newCompositeNode(grammarAccess.getArrayExpressionAccess().getDoubleArrayExpressionParserRuleCall_0()); 
                        
                    pushFollow(FOLLOW_ruleDoubleArrayExpression_in_ruleArrayExpression2530);
                    this_DoubleArrayExpression_0=ruleDoubleArrayExpression();

                    state._fsp--;

                     
                            current = this_DoubleArrayExpression_0; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1090:5: this_LongArrayExpression_1= ruleLongArrayExpression
                    {
                     
                            newCompositeNode(grammarAccess.getArrayExpressionAccess().getLongArrayExpressionParserRuleCall_1()); 
                        
                    pushFollow(FOLLOW_ruleLongArrayExpression_in_ruleArrayExpression2557);
                    this_LongArrayExpression_1=ruleLongArrayExpression();

                    state._fsp--;

                     
                            current = this_LongArrayExpression_1; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 3 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1100:5: this_StringArrayExpression_2= ruleStringArrayExpression
                    {
                     
                            newCompositeNode(grammarAccess.getArrayExpressionAccess().getStringArrayExpressionParserRuleCall_2()); 
                        
                    pushFollow(FOLLOW_ruleStringArrayExpression_in_ruleArrayExpression2584);
                    this_StringArrayExpression_2=ruleStringArrayExpression();

                    state._fsp--;

                     
                            current = this_StringArrayExpression_2; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 4 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1110:5: this_NullArrayExpression_3= ruleNullArrayExpression
                    {
                     
                            newCompositeNode(grammarAccess.getArrayExpressionAccess().getNullArrayExpressionParserRuleCall_3()); 
                        
                    pushFollow(FOLLOW_ruleNullArrayExpression_in_ruleArrayExpression2611);
                    this_NullArrayExpression_3=ruleNullArrayExpression();

                    state._fsp--;

                     
                            current = this_NullArrayExpression_3; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 5 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1120:5: this_DateArrayExpression_4= ruleDateArrayExpression
                    {
                     
                            newCompositeNode(grammarAccess.getArrayExpressionAccess().getDateArrayExpressionParserRuleCall_4()); 
                        
                    pushFollow(FOLLOW_ruleDateArrayExpression_in_ruleArrayExpression2638);
                    this_DateArrayExpression_4=ruleDateArrayExpression();

                    state._fsp--;

                     
                            current = this_DateArrayExpression_4; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;
                case 6 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1130:5: this_BooleanArrayExpression_5= ruleBooleanArrayExpression
                    {
                     
                            newCompositeNode(grammarAccess.getArrayExpressionAccess().getBooleanArrayExpressionParserRuleCall_5()); 
                        
                    pushFollow(FOLLOW_ruleBooleanArrayExpression_in_ruleArrayExpression2665);
                    this_BooleanArrayExpression_5=ruleBooleanArrayExpression();

                    state._fsp--;

                     
                            current = this_BooleanArrayExpression_5; 
                            afterParserOrEnumRuleCall();
                        

                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleArrayExpression"


    // $ANTLR start "entryRuleDoubleArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1146:1: entryRuleDoubleArrayExpression returns [EObject current=null] : iv_ruleDoubleArrayExpression= ruleDoubleArrayExpression EOF ;
    public final EObject entryRuleDoubleArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDoubleArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1147:2: (iv_ruleDoubleArrayExpression= ruleDoubleArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1148:2: iv_ruleDoubleArrayExpression= ruleDoubleArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getDoubleArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleDoubleArrayExpression_in_entryRuleDoubleArrayExpression2700);
            iv_ruleDoubleArrayExpression=ruleDoubleArrayExpression();

            state._fsp--;

             current =iv_ruleDoubleArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDoubleArrayExpression2710); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDoubleArrayExpression"


    // $ANTLR start "ruleDoubleArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1155:1: ruleDoubleArrayExpression returns [EObject current=null] : (otherlv_0= '[' ( (lv_values_1_0= RULE_SIGNED_DOUBLE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleDoubleArrayExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_values_1_0=null;
        Token otherlv_2=null;
        Token lv_values_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1158:28: ( (otherlv_0= '[' ( (lv_values_1_0= RULE_SIGNED_DOUBLE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) ) )* otherlv_4= ']' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1159:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_SIGNED_DOUBLE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) ) )* otherlv_4= ']' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1159:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_SIGNED_DOUBLE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) ) )* otherlv_4= ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1159:3: otherlv_0= '[' ( (lv_values_1_0= RULE_SIGNED_DOUBLE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleDoubleArrayExpression2747); 

                	newLeafNode(otherlv_0, grammarAccess.getDoubleArrayExpressionAccess().getLeftSquareBracketKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1163:1: ( (lv_values_1_0= RULE_SIGNED_DOUBLE ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1164:1: (lv_values_1_0= RULE_SIGNED_DOUBLE )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1164:1: (lv_values_1_0= RULE_SIGNED_DOUBLE )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1165:3: lv_values_1_0= RULE_SIGNED_DOUBLE
            {
            lv_values_1_0=(Token)match(input,RULE_SIGNED_DOUBLE,FOLLOW_RULE_SIGNED_DOUBLE_in_ruleDoubleArrayExpression2764); 

            			newLeafNode(lv_values_1_0, grammarAccess.getDoubleArrayExpressionAccess().getValuesSIGNED_DOUBLETerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDoubleArrayExpressionRule());
            	        }
                   		addWithLastConsumed(
                   			current, 
                   			"values",
                    		lv_values_1_0, 
                    		"SIGNED_DOUBLE");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1181:2: (otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) ) )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==19) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1181:4: otherlv_2= ',' ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) )
            	    {
            	    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleDoubleArrayExpression2782); 

            	        	newLeafNode(otherlv_2, grammarAccess.getDoubleArrayExpressionAccess().getCommaKeyword_2_0());
            	        
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1185:1: ( (lv_values_3_0= RULE_SIGNED_DOUBLE ) )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1186:1: (lv_values_3_0= RULE_SIGNED_DOUBLE )
            	    {
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1186:1: (lv_values_3_0= RULE_SIGNED_DOUBLE )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1187:3: lv_values_3_0= RULE_SIGNED_DOUBLE
            	    {
            	    lv_values_3_0=(Token)match(input,RULE_SIGNED_DOUBLE,FOLLOW_RULE_SIGNED_DOUBLE_in_ruleDoubleArrayExpression2799); 

            	    			newLeafNode(lv_values_3_0, grammarAccess.getDoubleArrayExpressionAccess().getValuesSIGNED_DOUBLETerminalRuleCall_2_1_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDoubleArrayExpressionRule());
            	    	        }
            	           		addWithLastConsumed(
            	           			current, 
            	           			"values",
            	            		lv_values_3_0, 
            	            		"SIGNED_DOUBLE");
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            otherlv_4=(Token)match(input,30,FOLLOW_30_in_ruleDoubleArrayExpression2818); 

                	newLeafNode(otherlv_4, grammarAccess.getDoubleArrayExpressionAccess().getRightSquareBracketKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDoubleArrayExpression"


    // $ANTLR start "entryRuleLongArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1215:1: entryRuleLongArrayExpression returns [EObject current=null] : iv_ruleLongArrayExpression= ruleLongArrayExpression EOF ;
    public final EObject entryRuleLongArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLongArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1216:2: (iv_ruleLongArrayExpression= ruleLongArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1217:2: iv_ruleLongArrayExpression= ruleLongArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getLongArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleLongArrayExpression_in_entryRuleLongArrayExpression2854);
            iv_ruleLongArrayExpression=ruleLongArrayExpression();

            state._fsp--;

             current =iv_ruleLongArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleLongArrayExpression2864); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLongArrayExpression"


    // $ANTLR start "ruleLongArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1224:1: ruleLongArrayExpression returns [EObject current=null] : (otherlv_0= '[' ( (lv_values_1_0= RULE_SINGED_LONG ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleLongArrayExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_values_1_0=null;
        Token otherlv_2=null;
        Token lv_values_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1227:28: ( (otherlv_0= '[' ( (lv_values_1_0= RULE_SINGED_LONG ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) ) )* otherlv_4= ']' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1228:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_SINGED_LONG ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) ) )* otherlv_4= ']' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1228:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_SINGED_LONG ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) ) )* otherlv_4= ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1228:3: otherlv_0= '[' ( (lv_values_1_0= RULE_SINGED_LONG ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleLongArrayExpression2901); 

                	newLeafNode(otherlv_0, grammarAccess.getLongArrayExpressionAccess().getLeftSquareBracketKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1232:1: ( (lv_values_1_0= RULE_SINGED_LONG ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1233:1: (lv_values_1_0= RULE_SINGED_LONG )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1233:1: (lv_values_1_0= RULE_SINGED_LONG )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1234:3: lv_values_1_0= RULE_SINGED_LONG
            {
            lv_values_1_0=(Token)match(input,RULE_SINGED_LONG,FOLLOW_RULE_SINGED_LONG_in_ruleLongArrayExpression2918); 

            			newLeafNode(lv_values_1_0, grammarAccess.getLongArrayExpressionAccess().getValuesSINGED_LONGTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getLongArrayExpressionRule());
            	        }
                   		addWithLastConsumed(
                   			current, 
                   			"values",
                    		lv_values_1_0, 
                    		"SINGED_LONG");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1250:2: (otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==19) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1250:4: otherlv_2= ',' ( (lv_values_3_0= RULE_SINGED_LONG ) )
            	    {
            	    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleLongArrayExpression2936); 

            	        	newLeafNode(otherlv_2, grammarAccess.getLongArrayExpressionAccess().getCommaKeyword_2_0());
            	        
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1254:1: ( (lv_values_3_0= RULE_SINGED_LONG ) )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1255:1: (lv_values_3_0= RULE_SINGED_LONG )
            	    {
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1255:1: (lv_values_3_0= RULE_SINGED_LONG )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1256:3: lv_values_3_0= RULE_SINGED_LONG
            	    {
            	    lv_values_3_0=(Token)match(input,RULE_SINGED_LONG,FOLLOW_RULE_SINGED_LONG_in_ruleLongArrayExpression2953); 

            	    			newLeafNode(lv_values_3_0, grammarAccess.getLongArrayExpressionAccess().getValuesSINGED_LONGTerminalRuleCall_2_1_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getLongArrayExpressionRule());
            	    	        }
            	           		addWithLastConsumed(
            	           			current, 
            	           			"values",
            	            		lv_values_3_0, 
            	            		"SINGED_LONG");
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            otherlv_4=(Token)match(input,30,FOLLOW_30_in_ruleLongArrayExpression2972); 

                	newLeafNode(otherlv_4, grammarAccess.getLongArrayExpressionAccess().getRightSquareBracketKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLongArrayExpression"


    // $ANTLR start "entryRuleStringArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1284:1: entryRuleStringArrayExpression returns [EObject current=null] : iv_ruleStringArrayExpression= ruleStringArrayExpression EOF ;
    public final EObject entryRuleStringArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStringArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1285:2: (iv_ruleStringArrayExpression= ruleStringArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1286:2: iv_ruleStringArrayExpression= ruleStringArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getStringArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleStringArrayExpression_in_entryRuleStringArrayExpression3008);
            iv_ruleStringArrayExpression=ruleStringArrayExpression();

            state._fsp--;

             current =iv_ruleStringArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleStringArrayExpression3018); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStringArrayExpression"


    // $ANTLR start "ruleStringArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1293:1: ruleStringArrayExpression returns [EObject current=null] : (otherlv_0= '[' ( (lv_values_1_0= RULE_STRING ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleStringArrayExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_values_1_0=null;
        Token otherlv_2=null;
        Token lv_values_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1296:28: ( (otherlv_0= '[' ( (lv_values_1_0= RULE_STRING ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) ) )* otherlv_4= ']' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1297:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_STRING ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) ) )* otherlv_4= ']' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1297:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_STRING ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) ) )* otherlv_4= ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1297:3: otherlv_0= '[' ( (lv_values_1_0= RULE_STRING ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleStringArrayExpression3055); 

                	newLeafNode(otherlv_0, grammarAccess.getStringArrayExpressionAccess().getLeftSquareBracketKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1301:1: ( (lv_values_1_0= RULE_STRING ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1302:1: (lv_values_1_0= RULE_STRING )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1302:1: (lv_values_1_0= RULE_STRING )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1303:3: lv_values_1_0= RULE_STRING
            {
            lv_values_1_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleStringArrayExpression3072); 

            			newLeafNode(lv_values_1_0, grammarAccess.getStringArrayExpressionAccess().getValuesSTRINGTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getStringArrayExpressionRule());
            	        }
                   		addWithLastConsumed(
                   			current, 
                   			"values",
                    		lv_values_1_0, 
                    		"STRING");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1319:2: (otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==19) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1319:4: otherlv_2= ',' ( (lv_values_3_0= RULE_STRING ) )
            	    {
            	    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleStringArrayExpression3090); 

            	        	newLeafNode(otherlv_2, grammarAccess.getStringArrayExpressionAccess().getCommaKeyword_2_0());
            	        
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1323:1: ( (lv_values_3_0= RULE_STRING ) )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1324:1: (lv_values_3_0= RULE_STRING )
            	    {
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1324:1: (lv_values_3_0= RULE_STRING )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1325:3: lv_values_3_0= RULE_STRING
            	    {
            	    lv_values_3_0=(Token)match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleStringArrayExpression3107); 

            	    			newLeafNode(lv_values_3_0, grammarAccess.getStringArrayExpressionAccess().getValuesSTRINGTerminalRuleCall_2_1_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getStringArrayExpressionRule());
            	    	        }
            	           		addWithLastConsumed(
            	           			current, 
            	           			"values",
            	            		lv_values_3_0, 
            	            		"STRING");
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            otherlv_4=(Token)match(input,30,FOLLOW_30_in_ruleStringArrayExpression3126); 

                	newLeafNode(otherlv_4, grammarAccess.getStringArrayExpressionAccess().getRightSquareBracketKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStringArrayExpression"


    // $ANTLR start "entryRuleNullArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1353:1: entryRuleNullArrayExpression returns [EObject current=null] : iv_ruleNullArrayExpression= ruleNullArrayExpression EOF ;
    public final EObject entryRuleNullArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNullArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1354:2: (iv_ruleNullArrayExpression= ruleNullArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1355:2: iv_ruleNullArrayExpression= ruleNullArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getNullArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleNullArrayExpression_in_entryRuleNullArrayExpression3162);
            iv_ruleNullArrayExpression=ruleNullArrayExpression();

            state._fsp--;

             current =iv_ruleNullArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNullArrayExpression3172); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNullArrayExpression"


    // $ANTLR start "ruleNullArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1362:1: ruleNullArrayExpression returns [EObject current=null] : (otherlv_0= '[' ( (lv_values_1_0= 'null' ) ) (otherlv_2= ',' ( (lv_values_3_0= 'null' ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleNullArrayExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_values_1_0=null;
        Token otherlv_2=null;
        Token lv_values_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1365:28: ( (otherlv_0= '[' ( (lv_values_1_0= 'null' ) ) (otherlv_2= ',' ( (lv_values_3_0= 'null' ) ) )* otherlv_4= ']' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1366:1: (otherlv_0= '[' ( (lv_values_1_0= 'null' ) ) (otherlv_2= ',' ( (lv_values_3_0= 'null' ) ) )* otherlv_4= ']' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1366:1: (otherlv_0= '[' ( (lv_values_1_0= 'null' ) ) (otherlv_2= ',' ( (lv_values_3_0= 'null' ) ) )* otherlv_4= ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1366:3: otherlv_0= '[' ( (lv_values_1_0= 'null' ) ) (otherlv_2= ',' ( (lv_values_3_0= 'null' ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleNullArrayExpression3209); 

                	newLeafNode(otherlv_0, grammarAccess.getNullArrayExpressionAccess().getLeftSquareBracketKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1370:1: ( (lv_values_1_0= 'null' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1371:1: (lv_values_1_0= 'null' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1371:1: (lv_values_1_0= 'null' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1372:3: lv_values_1_0= 'null'
            {
            lv_values_1_0=(Token)match(input,26,FOLLOW_26_in_ruleNullArrayExpression3227); 

                    newLeafNode(lv_values_1_0, grammarAccess.getNullArrayExpressionAccess().getValuesNullKeyword_1_0());
                

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getNullArrayExpressionRule());
            	        }
                   		addWithLastConsumed(current, "values", lv_values_1_0, "null");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1385:2: (otherlv_2= ',' ( (lv_values_3_0= 'null' ) ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==19) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1385:4: otherlv_2= ',' ( (lv_values_3_0= 'null' ) )
            	    {
            	    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleNullArrayExpression3253); 

            	        	newLeafNode(otherlv_2, grammarAccess.getNullArrayExpressionAccess().getCommaKeyword_2_0());
            	        
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1389:1: ( (lv_values_3_0= 'null' ) )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1390:1: (lv_values_3_0= 'null' )
            	    {
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1390:1: (lv_values_3_0= 'null' )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1391:3: lv_values_3_0= 'null'
            	    {
            	    lv_values_3_0=(Token)match(input,26,FOLLOW_26_in_ruleNullArrayExpression3271); 

            	            newLeafNode(lv_values_3_0, grammarAccess.getNullArrayExpressionAccess().getValuesNullKeyword_2_1_0());
            	        

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getNullArrayExpressionRule());
            	    	        }
            	           		addWithLastConsumed(current, "values", lv_values_3_0, "null");
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            otherlv_4=(Token)match(input,30,FOLLOW_30_in_ruleNullArrayExpression3298); 

                	newLeafNode(otherlv_4, grammarAccess.getNullArrayExpressionAccess().getRightSquareBracketKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNullArrayExpression"


    // $ANTLR start "entryRuleDateArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1416:1: entryRuleDateArrayExpression returns [EObject current=null] : iv_ruleDateArrayExpression= ruleDateArrayExpression EOF ;
    public final EObject entryRuleDateArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDateArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1417:2: (iv_ruleDateArrayExpression= ruleDateArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1418:2: iv_ruleDateArrayExpression= ruleDateArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getDateArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleDateArrayExpression_in_entryRuleDateArrayExpression3334);
            iv_ruleDateArrayExpression=ruleDateArrayExpression();

            state._fsp--;

             current =iv_ruleDateArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleDateArrayExpression3344); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDateArrayExpression"


    // $ANTLR start "ruleDateArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1425:1: ruleDateArrayExpression returns [EObject current=null] : (otherlv_0= '[' ( (lv_values_1_0= RULE_DATE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleDateArrayExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_values_1_0=null;
        Token otherlv_2=null;
        Token lv_values_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1428:28: ( (otherlv_0= '[' ( (lv_values_1_0= RULE_DATE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) ) )* otherlv_4= ']' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1429:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_DATE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) ) )* otherlv_4= ']' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1429:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_DATE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) ) )* otherlv_4= ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1429:3: otherlv_0= '[' ( (lv_values_1_0= RULE_DATE ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleDateArrayExpression3381); 

                	newLeafNode(otherlv_0, grammarAccess.getDateArrayExpressionAccess().getLeftSquareBracketKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1433:1: ( (lv_values_1_0= RULE_DATE ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1434:1: (lv_values_1_0= RULE_DATE )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1434:1: (lv_values_1_0= RULE_DATE )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1435:3: lv_values_1_0= RULE_DATE
            {
            lv_values_1_0=(Token)match(input,RULE_DATE,FOLLOW_RULE_DATE_in_ruleDateArrayExpression3398); 

            			newLeafNode(lv_values_1_0, grammarAccess.getDateArrayExpressionAccess().getValuesDATETerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getDateArrayExpressionRule());
            	        }
                   		addWithLastConsumed(
                   			current, 
                   			"values",
                    		lv_values_1_0, 
                    		"DATE");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1451:2: (otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) ) )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==19) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1451:4: otherlv_2= ',' ( (lv_values_3_0= RULE_DATE ) )
            	    {
            	    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleDateArrayExpression3416); 

            	        	newLeafNode(otherlv_2, grammarAccess.getDateArrayExpressionAccess().getCommaKeyword_2_0());
            	        
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1455:1: ( (lv_values_3_0= RULE_DATE ) )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1456:1: (lv_values_3_0= RULE_DATE )
            	    {
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1456:1: (lv_values_3_0= RULE_DATE )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1457:3: lv_values_3_0= RULE_DATE
            	    {
            	    lv_values_3_0=(Token)match(input,RULE_DATE,FOLLOW_RULE_DATE_in_ruleDateArrayExpression3433); 

            	    			newLeafNode(lv_values_3_0, grammarAccess.getDateArrayExpressionAccess().getValuesDATETerminalRuleCall_2_1_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getDateArrayExpressionRule());
            	    	        }
            	           		addWithLastConsumed(
            	           			current, 
            	           			"values",
            	            		lv_values_3_0, 
            	            		"DATE");
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            otherlv_4=(Token)match(input,30,FOLLOW_30_in_ruleDateArrayExpression3452); 

                	newLeafNode(otherlv_4, grammarAccess.getDateArrayExpressionAccess().getRightSquareBracketKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDateArrayExpression"


    // $ANTLR start "entryRuleBooleanArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1485:1: entryRuleBooleanArrayExpression returns [EObject current=null] : iv_ruleBooleanArrayExpression= ruleBooleanArrayExpression EOF ;
    public final EObject entryRuleBooleanArrayExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBooleanArrayExpression = null;


        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1486:2: (iv_ruleBooleanArrayExpression= ruleBooleanArrayExpression EOF )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1487:2: iv_ruleBooleanArrayExpression= ruleBooleanArrayExpression EOF
            {
             newCompositeNode(grammarAccess.getBooleanArrayExpressionRule()); 
            pushFollow(FOLLOW_ruleBooleanArrayExpression_in_entryRuleBooleanArrayExpression3488);
            iv_ruleBooleanArrayExpression=ruleBooleanArrayExpression();

            state._fsp--;

             current =iv_ruleBooleanArrayExpression; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBooleanArrayExpression3498); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBooleanArrayExpression"


    // $ANTLR start "ruleBooleanArrayExpression"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1494:1: ruleBooleanArrayExpression returns [EObject current=null] : (otherlv_0= '[' ( (lv_values_1_0= RULE_BOOL ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) ) )* otherlv_4= ']' ) ;
    public final EObject ruleBooleanArrayExpression() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_values_1_0=null;
        Token otherlv_2=null;
        Token lv_values_3_0=null;
        Token otherlv_4=null;

         enterRule(); 
            
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1497:28: ( (otherlv_0= '[' ( (lv_values_1_0= RULE_BOOL ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) ) )* otherlv_4= ']' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1498:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_BOOL ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) ) )* otherlv_4= ']' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1498:1: (otherlv_0= '[' ( (lv_values_1_0= RULE_BOOL ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) ) )* otherlv_4= ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1498:3: otherlv_0= '[' ( (lv_values_1_0= RULE_BOOL ) ) (otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) ) )* otherlv_4= ']'
            {
            otherlv_0=(Token)match(input,29,FOLLOW_29_in_ruleBooleanArrayExpression3535); 

                	newLeafNode(otherlv_0, grammarAccess.getBooleanArrayExpressionAccess().getLeftSquareBracketKeyword_0());
                
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1502:1: ( (lv_values_1_0= RULE_BOOL ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1503:1: (lv_values_1_0= RULE_BOOL )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1503:1: (lv_values_1_0= RULE_BOOL )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1504:3: lv_values_1_0= RULE_BOOL
            {
            lv_values_1_0=(Token)match(input,RULE_BOOL,FOLLOW_RULE_BOOL_in_ruleBooleanArrayExpression3552); 

            			newLeafNode(lv_values_1_0, grammarAccess.getBooleanArrayExpressionAccess().getValuesBOOLTerminalRuleCall_1_0()); 
            		

            	        if (current==null) {
            	            current = createModelElement(grammarAccess.getBooleanArrayExpressionRule());
            	        }
                   		addWithLastConsumed(
                   			current, 
                   			"values",
                    		lv_values_1_0, 
                    		"BOOL");
            	    

            }


            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1520:2: (otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==19) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1520:4: otherlv_2= ',' ( (lv_values_3_0= RULE_BOOL ) )
            	    {
            	    otherlv_2=(Token)match(input,19,FOLLOW_19_in_ruleBooleanArrayExpression3570); 

            	        	newLeafNode(otherlv_2, grammarAccess.getBooleanArrayExpressionAccess().getCommaKeyword_2_0());
            	        
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1524:1: ( (lv_values_3_0= RULE_BOOL ) )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1525:1: (lv_values_3_0= RULE_BOOL )
            	    {
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1525:1: (lv_values_3_0= RULE_BOOL )
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1526:3: lv_values_3_0= RULE_BOOL
            	    {
            	    lv_values_3_0=(Token)match(input,RULE_BOOL,FOLLOW_RULE_BOOL_in_ruleBooleanArrayExpression3587); 

            	    			newLeafNode(lv_values_3_0, grammarAccess.getBooleanArrayExpressionAccess().getValuesBOOLTerminalRuleCall_2_1_0()); 
            	    		

            	    	        if (current==null) {
            	    	            current = createModelElement(grammarAccess.getBooleanArrayExpressionRule());
            	    	        }
            	           		addWithLastConsumed(
            	           			current, 
            	           			"values",
            	            		lv_values_3_0, 
            	            		"BOOL");
            	    	    

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            otherlv_4=(Token)match(input,30,FOLLOW_30_in_ruleBooleanArrayExpression3606); 

                	newLeafNode(otherlv_4, grammarAccess.getBooleanArrayExpressionAccess().getRightSquareBracketKeyword_3());
                

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBooleanArrayExpression"


    // $ANTLR start "ruleArrayOperator"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1554:1: ruleArrayOperator returns [Enumerator current=null] : ( (enumLiteral_0= '$all' ) | (enumLiteral_1= '$in' ) | (enumLiteral_2= 'in' ) | (enumLiteral_3= '$nin' ) | (enumLiteral_4= 'not in' ) ) ;
    public final Enumerator ruleArrayOperator() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;

         enterRule(); 
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1556:28: ( ( (enumLiteral_0= '$all' ) | (enumLiteral_1= '$in' ) | (enumLiteral_2= 'in' ) | (enumLiteral_3= '$nin' ) | (enumLiteral_4= 'not in' ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1557:1: ( (enumLiteral_0= '$all' ) | (enumLiteral_1= '$in' ) | (enumLiteral_2= 'in' ) | (enumLiteral_3= '$nin' ) | (enumLiteral_4= 'not in' ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1557:1: ( (enumLiteral_0= '$all' ) | (enumLiteral_1= '$in' ) | (enumLiteral_2= 'in' ) | (enumLiteral_3= '$nin' ) | (enumLiteral_4= 'not in' ) )
            int alt20=5;
            switch ( input.LA(1) ) {
            case 31:
                {
                alt20=1;
                }
                break;
            case 32:
                {
                alt20=2;
                }
                break;
            case 33:
                {
                alt20=3;
                }
                break;
            case 34:
                {
                alt20=4;
                }
                break;
            case 35:
                {
                alt20=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1557:2: (enumLiteral_0= '$all' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1557:2: (enumLiteral_0= '$all' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1557:4: enumLiteral_0= '$all'
                    {
                    enumLiteral_0=(Token)match(input,31,FOLLOW_31_in_ruleArrayOperator3656); 

                            current = grammarAccess.getArrayOperatorAccess().getMongo_allEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_0, grammarAccess.getArrayOperatorAccess().getMongo_allEnumLiteralDeclaration_0()); 
                        

                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1563:6: (enumLiteral_1= '$in' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1563:6: (enumLiteral_1= '$in' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1563:8: enumLiteral_1= '$in'
                    {
                    enumLiteral_1=(Token)match(input,32,FOLLOW_32_in_ruleArrayOperator3673); 

                            current = grammarAccess.getArrayOperatorAccess().getMongo_inEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_1, grammarAccess.getArrayOperatorAccess().getMongo_inEnumLiteralDeclaration_1()); 
                        

                    }


                    }
                    break;
                case 3 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1569:6: (enumLiteral_2= 'in' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1569:6: (enumLiteral_2= 'in' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1569:8: enumLiteral_2= 'in'
                    {
                    enumLiteral_2=(Token)match(input,33,FOLLOW_33_in_ruleArrayOperator3690); 

                            current = grammarAccess.getArrayOperatorAccess().getSql_inEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_2, grammarAccess.getArrayOperatorAccess().getSql_inEnumLiteralDeclaration_2()); 
                        

                    }


                    }
                    break;
                case 4 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1575:6: (enumLiteral_3= '$nin' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1575:6: (enumLiteral_3= '$nin' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1575:8: enumLiteral_3= '$nin'
                    {
                    enumLiteral_3=(Token)match(input,34,FOLLOW_34_in_ruleArrayOperator3707); 

                            current = grammarAccess.getArrayOperatorAccess().getMongo_ninEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_3, grammarAccess.getArrayOperatorAccess().getMongo_ninEnumLiteralDeclaration_3()); 
                        

                    }


                    }
                    break;
                case 5 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1581:6: (enumLiteral_4= 'not in' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1581:6: (enumLiteral_4= 'not in' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1581:8: enumLiteral_4= 'not in'
                    {
                    enumLiteral_4=(Token)match(input,35,FOLLOW_35_in_ruleArrayOperator3724); 

                            current = grammarAccess.getArrayOperatorAccess().getSql_notInEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_4, grammarAccess.getArrayOperatorAccess().getSql_notInEnumLiteralDeclaration_4()); 
                        

                    }


                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleArrayOperator"


    // $ANTLR start "ruleOperator"
    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1591:1: ruleOperator returns [Enumerator current=null] : ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= 'like' ) | (enumLiteral_7= 'not like' ) | (enumLiteral_8= 'not in' ) | (enumLiteral_9= 'in' ) ) ;
    public final Enumerator ruleOperator() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;
        Token enumLiteral_6=null;
        Token enumLiteral_7=null;
        Token enumLiteral_8=null;
        Token enumLiteral_9=null;

         enterRule(); 
        try {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1593:28: ( ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= 'like' ) | (enumLiteral_7= 'not like' ) | (enumLiteral_8= 'not in' ) | (enumLiteral_9= 'in' ) ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1594:1: ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= 'like' ) | (enumLiteral_7= 'not like' ) | (enumLiteral_8= 'not in' ) | (enumLiteral_9= 'in' ) )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1594:1: ( (enumLiteral_0= '<' ) | (enumLiteral_1= '>' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '=' ) | (enumLiteral_5= '!=' ) | (enumLiteral_6= 'like' ) | (enumLiteral_7= 'not like' ) | (enumLiteral_8= 'not in' ) | (enumLiteral_9= 'in' ) )
            int alt21=10;
            switch ( input.LA(1) ) {
            case 36:
                {
                alt21=1;
                }
                break;
            case 37:
                {
                alt21=2;
                }
                break;
            case 38:
                {
                alt21=3;
                }
                break;
            case 39:
                {
                alt21=4;
                }
                break;
            case 40:
                {
                alt21=5;
                }
                break;
            case 41:
                {
                alt21=6;
                }
                break;
            case 42:
                {
                alt21=7;
                }
                break;
            case 43:
                {
                alt21=8;
                }
                break;
            case 35:
                {
                alt21=9;
                }
                break;
            case 33:
                {
                alt21=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1594:2: (enumLiteral_0= '<' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1594:2: (enumLiteral_0= '<' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1594:4: enumLiteral_0= '<'
                    {
                    enumLiteral_0=(Token)match(input,36,FOLLOW_36_in_ruleOperator3769); 

                            current = grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_0, grammarAccess.getOperatorAccess().getLessThenEnumLiteralDeclaration_0()); 
                        

                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1600:6: (enumLiteral_1= '>' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1600:6: (enumLiteral_1= '>' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1600:8: enumLiteral_1= '>'
                    {
                    enumLiteral_1=(Token)match(input,37,FOLLOW_37_in_ruleOperator3786); 

                            current = grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_1, grammarAccess.getOperatorAccess().getGreaterThenEnumLiteralDeclaration_1()); 
                        

                    }


                    }
                    break;
                case 3 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1606:6: (enumLiteral_2= '<=' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1606:6: (enumLiteral_2= '<=' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1606:8: enumLiteral_2= '<='
                    {
                    enumLiteral_2=(Token)match(input,38,FOLLOW_38_in_ruleOperator3803); 

                            current = grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_2, grammarAccess.getOperatorAccess().getLessEqualEnumLiteralDeclaration_2()); 
                        

                    }


                    }
                    break;
                case 4 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1612:6: (enumLiteral_3= '>=' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1612:6: (enumLiteral_3= '>=' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1612:8: enumLiteral_3= '>='
                    {
                    enumLiteral_3=(Token)match(input,39,FOLLOW_39_in_ruleOperator3820); 

                            current = grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_3, grammarAccess.getOperatorAccess().getGreaterEqualEnumLiteralDeclaration_3()); 
                        

                    }


                    }
                    break;
                case 5 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1618:6: (enumLiteral_4= '=' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1618:6: (enumLiteral_4= '=' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1618:8: enumLiteral_4= '='
                    {
                    enumLiteral_4=(Token)match(input,40,FOLLOW_40_in_ruleOperator3837); 

                            current = grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_4, grammarAccess.getOperatorAccess().getEqualEnumLiteralDeclaration_4()); 
                        

                    }


                    }
                    break;
                case 6 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1624:6: (enumLiteral_5= '!=' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1624:6: (enumLiteral_5= '!=' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1624:8: enumLiteral_5= '!='
                    {
                    enumLiteral_5=(Token)match(input,41,FOLLOW_41_in_ruleOperator3854); 

                            current = grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_5, grammarAccess.getOperatorAccess().getNotEqualEnumLiteralDeclaration_5()); 
                        

                    }


                    }
                    break;
                case 7 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1630:6: (enumLiteral_6= 'like' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1630:6: (enumLiteral_6= 'like' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1630:8: enumLiteral_6= 'like'
                    {
                    enumLiteral_6=(Token)match(input,42,FOLLOW_42_in_ruleOperator3871); 

                            current = grammarAccess.getOperatorAccess().getLikeEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_6, grammarAccess.getOperatorAccess().getLikeEnumLiteralDeclaration_6()); 
                        

                    }


                    }
                    break;
                case 8 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1636:6: (enumLiteral_7= 'not like' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1636:6: (enumLiteral_7= 'not like' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1636:8: enumLiteral_7= 'not like'
                    {
                    enumLiteral_7=(Token)match(input,43,FOLLOW_43_in_ruleOperator3888); 

                            current = grammarAccess.getOperatorAccess().getNotLikeEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_7, grammarAccess.getOperatorAccess().getNotLikeEnumLiteralDeclaration_7()); 
                        

                    }


                    }
                    break;
                case 9 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1642:6: (enumLiteral_8= 'not in' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1642:6: (enumLiteral_8= 'not in' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1642:8: enumLiteral_8= 'not in'
                    {
                    enumLiteral_8=(Token)match(input,35,FOLLOW_35_in_ruleOperator3905); 

                            current = grammarAccess.getOperatorAccess().getNotInEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_8, grammarAccess.getOperatorAccess().getNotInEnumLiteralDeclaration_8()); 
                        

                    }


                    }
                    break;
                case 10 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1648:6: (enumLiteral_9= 'in' )
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1648:6: (enumLiteral_9= 'in' )
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1648:8: enumLiteral_9= 'in'
                    {
                    enumLiteral_9=(Token)match(input,33,FOLLOW_33_in_ruleOperator3922); 

                            current = grammarAccess.getOperatorAccess().getInEnumLiteralDeclaration_9().getEnumLiteral().getInstance();
                            newLeafNode(enumLiteral_9, grammarAccess.getOperatorAccess().getInEnumLiteralDeclaration_9()); 
                        

                    }


                    }
                    break;

            }


            }

             leaveRule(); 
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOperator"

    // Delegated rules


 

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel75 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModel85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_ruleModel122 = new BitSet(new long[]{0x0000000000100010L});
    public static final BitSet FOLLOW_ruleColumnList_in_ruleModel143 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleModel155 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_ruleDatabase_in_ruleModel176 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_15_in_ruleModel189 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_ruleWhereEntry_in_ruleModel210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDatabase_in_entryRuleDatabase248 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDatabase258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_ruleDatabase295 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDatabase312 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_17_in_ruleDatabase330 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_SINGED_LONG_in_ruleDatabase347 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleDatabase366 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDatabase383 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleDatabase400 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleDatabase417 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleColumnList_in_entryRuleColumnList459 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleColumnList470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleColumnList511 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleColumnList530 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleColumnList545 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_20_in_ruleColumnList572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleWhereEntry_in_entryRuleWhereEntry612 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleWhereEntry622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAndWhereEntry_in_ruleWhereEntry669 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_ruleWhereEntry691 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_ruleAndWhereEntry_in_ruleWhereEntry712 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_ruleAndWhereEntry_in_entryRuleAndWhereEntry752 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAndWhereEntry762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleConcreteWhereEntry_in_ruleAndWhereEntry809 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_22_in_ruleAndWhereEntry831 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_ruleConcreteWhereEntry_in_ruleAndWhereEntry852 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ruleConcreteWhereEntry_in_entryRuleConcreteWhereEntry892 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleConcreteWhereEntry902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParWhereEntry_in_ruleConcreteWhereEntry949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleExpressionWhereEntry_in_ruleConcreteWhereEntry976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleParWhereEntry_in_entryRuleParWhereEntry1011 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleParWhereEntry1021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_ruleParWhereEntry1058 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_ruleWhereEntry_in_ruleParWhereEntry1080 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleParWhereEntry1091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleExpressionWhereEntry_in_entryRuleExpressionWhereEntry1127 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleExpressionWhereEntry1137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSingleExpressionWhereEntry_in_ruleExpressionWhereEntry1184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMultiExpressionWhereEntry_in_ruleExpressionWhereEntry1211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSingleExpressionWhereEntry_in_entryRuleSingleExpressionWhereEntry1246 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSingleExpressionWhereEntry1256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSingleExpressionWhereEntry1298 = new BitSet(new long[]{0x00000FFA00000000L});
    public static final BitSet FOLLOW_ruleOperator_in_ruleSingleExpressionWhereEntry1324 = new BitSet(new long[]{0x000000001E0001E0L});
    public static final BitSet FOLLOW_ruleExpression_in_ruleSingleExpressionWhereEntry1345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleExpression_in_entryRuleExpression1381 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleExpression1391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleExpression_in_ruleExpression1438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongExpression_in_ruleExpression1465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringExpression_in_ruleExpression1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNullExpression_in_ruleExpression1519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDateExpression_in_ruleExpression1546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBooleanExpression_in_ruleExpression1573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleReplacableValue_in_ruleExpression1600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleReplacableValue_in_entryRuleReplacableValue1635 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleReplacableValue1645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_ruleReplacableValue1687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleExpression_in_entryRuleDoubleExpression1735 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDoubleExpression1745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_SIGNED_DOUBLE_in_ruleDoubleExpression1786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongExpression_in_entryRuleLongExpression1826 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLongExpression1836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_SINGED_LONG_in_ruleLongExpression1877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringExpression_in_entryRuleStringExpression1917 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStringExpression1927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleStringExpression1968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNullExpression_in_entryRuleNullExpression2008 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNullExpression2018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_ruleNullExpression2060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDateExpression_in_entryRuleDateExpression2108 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDateExpression2118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_DATE_in_ruleDateExpression2159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBooleanExpression_in_entryRuleBooleanExpression2199 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBooleanExpression2209 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_ruleBooleanExpression2252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_ruleBooleanExpression2289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMultiExpressionWhereEntry_in_entryRuleMultiExpressionWhereEntry2338 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMultiExpressionWhereEntry2348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMultiExpressionWhereEntry2390 = new BitSet(new long[]{0x0000000F80000000L});
    public static final BitSet FOLLOW_ruleArrayOperator_in_ruleMultiExpressionWhereEntry2416 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ruleArrayExpression_in_ruleMultiExpressionWhereEntry2437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleArrayExpression_in_entryRuleArrayExpression2473 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleArrayExpression2483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleArrayExpression_in_ruleArrayExpression2530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongArrayExpression_in_ruleArrayExpression2557 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringArrayExpression_in_ruleArrayExpression2584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNullArrayExpression_in_ruleArrayExpression2611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDateArrayExpression_in_ruleArrayExpression2638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBooleanArrayExpression_in_ruleArrayExpression2665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDoubleArrayExpression_in_entryRuleDoubleArrayExpression2700 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDoubleArrayExpression2710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleDoubleArrayExpression2747 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_SIGNED_DOUBLE_in_ruleDoubleArrayExpression2764 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_19_in_ruleDoubleArrayExpression2782 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_SIGNED_DOUBLE_in_ruleDoubleArrayExpression2799 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_30_in_ruleDoubleArrayExpression2818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleLongArrayExpression_in_entryRuleLongArrayExpression2854 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleLongArrayExpression2864 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleLongArrayExpression2901 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_SINGED_LONG_in_ruleLongArrayExpression2918 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_19_in_ruleLongArrayExpression2936 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_SINGED_LONG_in_ruleLongArrayExpression2953 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_30_in_ruleLongArrayExpression2972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleStringArrayExpression_in_entryRuleStringArrayExpression3008 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleStringArrayExpression3018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleStringArrayExpression3055 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleStringArrayExpression3072 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_19_in_ruleStringArrayExpression3090 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleStringArrayExpression3107 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_30_in_ruleStringArrayExpression3126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleNullArrayExpression_in_entryRuleNullArrayExpression3162 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNullArrayExpression3172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleNullArrayExpression3209 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleNullArrayExpression3227 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_19_in_ruleNullArrayExpression3253 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_ruleNullArrayExpression3271 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_30_in_ruleNullArrayExpression3298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleDateArrayExpression_in_entryRuleDateArrayExpression3334 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleDateArrayExpression3344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleDateArrayExpression3381 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RULE_DATE_in_ruleDateArrayExpression3398 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_19_in_ruleDateArrayExpression3416 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_RULE_DATE_in_ruleDateArrayExpression3433 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_30_in_ruleDateArrayExpression3452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBooleanArrayExpression_in_entryRuleBooleanArrayExpression3488 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBooleanArrayExpression3498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_ruleBooleanArrayExpression3535 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_RULE_BOOL_in_ruleBooleanArrayExpression3552 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_19_in_ruleBooleanArrayExpression3570 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_RULE_BOOL_in_ruleBooleanArrayExpression3587 = new BitSet(new long[]{0x0000000040080000L});
    public static final BitSet FOLLOW_30_in_ruleBooleanArrayExpression3606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_ruleArrayOperator3656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_ruleArrayOperator3673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_ruleArrayOperator3690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_ruleArrayOperator3707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_ruleArrayOperator3724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_ruleOperator3769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_ruleOperator3786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_ruleOperator3803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_ruleOperator3820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_ruleOperator3837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_ruleOperator3854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_ruleOperator3871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_ruleOperator3888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_ruleOperator3905 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_ruleOperator3922 = new BitSet(new long[]{0x0000000000000002L});

}