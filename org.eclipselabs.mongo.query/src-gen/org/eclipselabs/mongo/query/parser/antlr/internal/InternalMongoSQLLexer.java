package org.eclipselabs.mongo.query.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalMongoSQLLexer extends Lexer {
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

    public InternalMongoSQLLexer() {;} 
    public InternalMongoSQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalMongoSQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:11:7: ( 'SELECT' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:11:9: 'SELECT'
            {
            match("SELECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:12:7: ( 'FROM' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:12:9: 'FROM'
            {
            match("FROM"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:13:7: ( 'WHERE' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:13:9: 'WHERE'
            {
            match("WHERE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:14:7: ( 'mongo://' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:14:9: 'mongo://'
            {
            match("mongo://"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:15:7: ( ':' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:15:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:16:7: ( '/' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:16:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:17:7: ( ',' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:17:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:18:7: ( '*' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:18:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:19:7: ( 'OR' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:19:9: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:20:7: ( 'AND' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:20:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:21:7: ( '(' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:21:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:22:7: ( ')' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:22:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:23:7: ( '?' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:23:9: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:24:7: ( 'null' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:24:9: 'null'
            {
            match("null"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:25:7: ( 'true' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:25:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:26:7: ( 'false' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:26:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:27:7: ( '[' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:27:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:28:7: ( ']' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:28:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:29:7: ( '$all' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:29:9: '$all'
            {
            match("$all"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:30:7: ( '$in' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:30:9: '$in'
            {
            match("$in"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:31:7: ( 'in' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:31:9: 'in'
            {
            match("in"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:32:7: ( '$nin' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:32:9: '$nin'
            {
            match("$nin"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:33:7: ( 'not in' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:33:9: 'not in'
            {
            match("not in"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:34:7: ( '<' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:34:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:35:7: ( '>' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:35:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:36:7: ( '<=' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:36:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:37:7: ( '>=' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:37:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:38:7: ( '=' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:38:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:39:7: ( '!=' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:39:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:40:7: ( 'like' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:40:9: 'like'
            {
            match("like"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:41:7: ( 'not like' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:41:9: 'not like'
            {
            match("not like"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "RULE_BOOL"
    public final void mRULE_BOOL() throws RecognitionException {
        try {
            int _type = RULE_BOOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:11: ( ( 'true' | 'false' | 'TRUE' | 'FALSE' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:13: ( 'true' | 'false' | 'TRUE' | 'FALSE' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:13: ( 'true' | 'false' | 'TRUE' | 'FALSE' )
            int alt1=4;
            switch ( input.LA(1) ) {
            case 't':
                {
                alt1=1;
                }
                break;
            case 'f':
                {
                alt1=2;
                }
                break;
            case 'T':
                {
                alt1=3;
                }
                break;
            case 'F':
                {
                alt1=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:14: 'true'
                    {
                    match("true"); 


                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:21: 'false'
                    {
                    match("false"); 


                    }
                    break;
                case 3 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:29: 'TRUE'
                    {
                    match("TRUE"); 


                    }
                    break;
                case 4 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1657:36: 'FALSE'
                    {
                    match("FALSE"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_BOOL"

    // $ANTLR start "RULE_SINGED_LONG"
    public final void mRULE_SINGED_LONG() throws RecognitionException {
        try {
            int _type = RULE_SINGED_LONG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1659:18: ( ( '-' )? ( '0' .. '9' )+ )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1659:20: ( '-' )? ( '0' .. '9' )+
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1659:20: ( '-' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='-') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1659:20: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1659:25: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1659:26: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SINGED_LONG"

    // $ANTLR start "RULE_DATE"
    public final void mRULE_DATE() throws RecognitionException {
        try {
            int _type = RULE_DATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1661:11: ( '0' .. '9' '0' .. '9' '0' .. '9' '0' .. '9' '-' '0' .. '1' '0' .. '9' '-' '0' .. '3' '0' .. '9' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1661:13: '0' .. '9' '0' .. '9' '0' .. '9' '0' .. '9' '-' '0' .. '1' '0' .. '9' '-' '0' .. '3' '0' .. '9'
            {
            matchRange('0','9'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            match('-'); 
            matchRange('0','1'); 
            matchRange('0','9'); 
            match('-'); 
            matchRange('0','3'); 
            matchRange('0','9'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DATE"

    // $ANTLR start "RULE_SIGNED_DOUBLE"
    public final void mRULE_SIGNED_DOUBLE() throws RecognitionException {
        try {
            int _type = RULE_SIGNED_DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:20: ( ( '-' )? ( '0' .. '9' )+ ( '.' ( '0' .. '9' )+ )? )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:22: ( '-' )? ( '0' .. '9' )+ ( '.' ( '0' .. '9' )+ )?
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:22: ( '-' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='-') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:22: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:27: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:28: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

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

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:39: ( '.' ( '0' .. '9' )+ )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='.') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:40: '.' ( '0' .. '9' )+
                    {
                    match('.'); 
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:44: ( '0' .. '9' )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1663:45: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt6 >= 1 ) break loop6;
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SIGNED_DOUBLE"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1665:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '.' )* )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1665:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '.' )*
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1665:11: ( '^' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='^') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1665:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( input.LA(1)=='.'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1665:44: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '.' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='.'||(LA9_0>='0' && LA9_0<='9')||(LA9_0>='A' && LA9_0<='Z')||LA9_0=='_'||(LA9_0>='a' && LA9_0<='z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:
            	    {
            	    if ( input.LA(1)=='.'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:13: ( ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\"') ) {
                alt12=1;
            }
            else if ( (LA12_0=='\'') ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:16: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop10:
                    do {
                        int alt10=3;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0=='\\') ) {
                            alt10=1;
                        }
                        else if ( ((LA10_0>='\u0000' && LA10_0<='!')||(LA10_0>='#' && LA10_0<='[')||(LA10_0>=']' && LA10_0<='\uFFFF')) ) {
                            alt10=2;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:62: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:82: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:87: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop11:
                    do {
                        int alt11=3;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0=='\\') ) {
                            alt11=1;
                        }
                        else if ( ((LA11_0>='\u0000' && LA11_0<='&')||(LA11_0>='(' && LA11_0<='[')||(LA11_0>=']' && LA11_0<='\uFFFF')) ) {
                            alt11=2;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:88: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1667:129: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1669:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1669:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1669:24: ( options {greedy=false; } : . )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='*') ) {
                    int LA13_1 = input.LA(2);

                    if ( (LA13_1=='/') ) {
                        alt13=2;
                    }
                    else if ( ((LA13_1>='\u0000' && LA13_1<='.')||(LA13_1>='0' && LA13_1<='\uFFFF')) ) {
                        alt13=1;
                    }


                }
                else if ( ((LA13_0>='\u0000' && LA13_0<=')')||(LA13_0>='+' && LA13_0<='\uFFFF')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1669:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>='\u0000' && LA14_0<='\t')||(LA14_0>='\u000B' && LA14_0<='\f')||(LA14_0>='\u000E' && LA14_0<='\uFFFF')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:40: ( ( '\\r' )? '\\n' )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='\n'||LA16_0=='\r') ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:41: ( '\\r' )? '\\n'
                    {
                    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:41: ( '\\r' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0=='\r') ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1671:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1673:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1673:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1673:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt17=0;
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>='\t' && LA17_0<='\n')||LA17_0=='\r'||LA17_0==' ') ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt17 >= 1 ) break loop17;
                        EarlyExitException eee =
                            new EarlyExitException(17, input);
                        throw eee;
                }
                cnt17++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    public void mTokens() throws RecognitionException {
        // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | RULE_BOOL | RULE_SINGED_LONG | RULE_DATE | RULE_SIGNED_DOUBLE | RULE_ID | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS )
        int alt18=40;
        alt18 = dfa18.predict(input);
        switch (alt18) {
            case 1 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:76: T__24
                {
                mT__24(); 

                }
                break;
            case 13 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:82: T__25
                {
                mT__25(); 

                }
                break;
            case 14 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:88: T__26
                {
                mT__26(); 

                }
                break;
            case 15 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:94: T__27
                {
                mT__27(); 

                }
                break;
            case 16 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:100: T__28
                {
                mT__28(); 

                }
                break;
            case 17 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:106: T__29
                {
                mT__29(); 

                }
                break;
            case 18 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:112: T__30
                {
                mT__30(); 

                }
                break;
            case 19 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:118: T__31
                {
                mT__31(); 

                }
                break;
            case 20 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:124: T__32
                {
                mT__32(); 

                }
                break;
            case 21 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:130: T__33
                {
                mT__33(); 

                }
                break;
            case 22 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:136: T__34
                {
                mT__34(); 

                }
                break;
            case 23 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:142: T__35
                {
                mT__35(); 

                }
                break;
            case 24 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:148: T__36
                {
                mT__36(); 

                }
                break;
            case 25 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:154: T__37
                {
                mT__37(); 

                }
                break;
            case 26 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:160: T__38
                {
                mT__38(); 

                }
                break;
            case 27 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:166: T__39
                {
                mT__39(); 

                }
                break;
            case 28 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:172: T__40
                {
                mT__40(); 

                }
                break;
            case 29 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:178: T__41
                {
                mT__41(); 

                }
                break;
            case 30 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:184: T__42
                {
                mT__42(); 

                }
                break;
            case 31 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:190: T__43
                {
                mT__43(); 

                }
                break;
            case 32 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:196: RULE_BOOL
                {
                mRULE_BOOL(); 

                }
                break;
            case 33 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:206: RULE_SINGED_LONG
                {
                mRULE_SINGED_LONG(); 

                }
                break;
            case 34 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:223: RULE_DATE
                {
                mRULE_DATE(); 

                }
                break;
            case 35 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:233: RULE_SIGNED_DOUBLE
                {
                mRULE_SIGNED_DOUBLE(); 

                }
                break;
            case 36 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:252: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 37 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:260: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 38 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:272: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 39 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:288: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 40 :
                // ../org.eclipselabs.mongo.query/src-gen/org/eclipselabs/mongo/query/parser/antlr/internal/InternalMongoSQL.g:1:304: RULE_WS
                {
                mRULE_WS(); 

                }
                break;

        }

    }


    protected DFA18 dfa18 = new DFA18(this);
    static final String DFA18_eotS =
        "\1\uffff\4\35\1\uffff\1\47\2\uffff\2\35\3\uffff\3\35\3\uffff\1"+
        "\35\1\63\1\65\2\uffff\2\35\1\uffff\1\73\3\uffff\5\35\3\uffff\1\101"+
        "\5\35\3\uffff\1\107\4\uffff\2\35\2\73\2\uffff\5\35\1\uffff\1\120"+
        "\4\35\1\uffff\2\35\1\73\1\35\1\131\3\35\1\uffff\1\135\1\uffff\1"+
        "\140\1\35\1\142\1\143\1\73\1\35\1\uffff\1\143\1\146\1\35\4\uffff"+
        "\1\150\3\uffff\1\151\4\uffff";
    static final String DFA18_eofS =
        "\152\uffff";
    static final String DFA18_minS =
        "\1\11\1\105\1\101\1\110\1\157\1\uffff\1\52\2\uffff\1\122\1\116"+
        "\3\uffff\1\157\1\162\1\141\2\uffff\1\141\1\156\2\75\2\uffff\1\151"+
        "\1\122\1\60\1\56\3\uffff\1\114\1\117\1\114\1\105\1\156\3\uffff\1"+
        "\56\1\104\1\154\1\164\1\165\1\154\3\uffff\1\56\4\uffff\1\153\1\125"+
        "\2\56\2\uffff\1\105\1\115\1\123\1\122\1\147\1\uffff\1\56\1\154\1"+
        "\40\1\145\1\163\1\uffff\1\145\1\105\1\56\1\103\1\56\2\105\1\157"+
        "\1\uffff\1\56\1\151\1\56\1\145\2\56\1\55\1\124\1\uffff\2\56\1\72"+
        "\4\uffff\1\56\3\uffff\1\56\4\uffff";
    static final String DFA18_maxS =
        "\1\172\1\105\1\122\1\110\1\157\1\uffff\1\57\2\uffff\1\122\1\116"+
        "\3\uffff\1\165\1\162\1\141\2\uffff\2\156\2\75\2\uffff\1\151\1\122"+
        "\2\71\3\uffff\1\114\1\117\1\114\1\105\1\156\3\uffff\1\172\1\104"+
        "\1\154\1\164\1\165\1\154\3\uffff\1\172\4\uffff\1\153\1\125\2\71"+
        "\2\uffff\1\105\1\115\1\123\1\122\1\147\1\uffff\1\172\1\154\1\40"+
        "\1\145\1\163\1\uffff\1\145\1\105\1\71\1\103\1\172\2\105\1\157\1"+
        "\uffff\1\172\1\154\1\172\1\145\2\172\1\71\1\124\1\uffff\2\172\1"+
        "\72\4\uffff\1\172\3\uffff\1\172\4\uffff";
    static final String DFA18_acceptS =
        "\5\uffff\1\5\1\uffff\1\7\1\10\2\uffff\1\13\1\14\1\15\3\uffff\1"+
        "\21\1\22\4\uffff\1\34\1\35\4\uffff\1\44\1\45\1\50\5\uffff\1\46\1"+
        "\47\1\6\6\uffff\1\23\1\24\1\26\1\uffff\1\32\1\30\1\33\1\31\4\uffff"+
        "\1\43\1\41\5\uffff\1\11\5\uffff\1\25\10\uffff\1\12\10\uffff\1\2"+
        "\3\uffff\1\16\1\27\1\37\1\17\1\uffff\1\36\1\40\1\42\1\uffff\1\3"+
        "\1\4\1\20\1\1";
    static final String DFA18_specialS =
        "\152\uffff}>";
    static final String[] DFA18_transitionS = {
            "\2\37\2\uffff\1\37\22\uffff\1\37\1\30\1\36\1\uffff\1\23\2\uffff"+
            "\1\36\1\13\1\14\1\10\1\uffff\1\7\1\33\1\35\1\6\12\34\1\5\1\uffff"+
            "\1\25\1\27\1\26\1\15\1\uffff\1\12\4\35\1\2\10\35\1\11\3\35\1"+
            "\1\1\32\2\35\1\3\3\35\1\21\1\uffff\1\22\2\35\1\uffff\5\35\1"+
            "\20\2\35\1\24\2\35\1\31\1\4\1\16\5\35\1\17\6\35",
            "\1\40",
            "\1\42\20\uffff\1\41",
            "\1\43",
            "\1\44",
            "",
            "\1\45\4\uffff\1\46",
            "",
            "",
            "\1\50",
            "\1\51",
            "",
            "",
            "",
            "\1\53\5\uffff\1\52",
            "\1\54",
            "\1\55",
            "",
            "",
            "\1\56\7\uffff\1\57\4\uffff\1\60",
            "\1\61",
            "\1\62",
            "\1\64",
            "",
            "",
            "\1\66",
            "\1\67",
            "\12\70",
            "\1\72\1\uffff\12\71",
            "",
            "",
            "",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "",
            "",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "",
            "",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "",
            "",
            "",
            "",
            "\1\110",
            "\1\111",
            "\1\72\1\uffff\12\70",
            "\1\72\1\uffff\12\112",
            "",
            "",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "",
            "\1\125",
            "\1\126",
            "\1\72\1\uffff\12\127",
            "\1\130",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\132",
            "\1\133",
            "\1\134",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\136\2\uffff\1\137",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\141",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\144\1\72\1\uffff\12\70",
            "\1\145",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "\1\147",
            "",
            "",
            "",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "",
            "",
            "",
            "\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32"+
            "\35",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | RULE_BOOL | RULE_SINGED_LONG | RULE_DATE | RULE_SIGNED_DOUBLE | RULE_ID | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS );";
        }
    }
 

}