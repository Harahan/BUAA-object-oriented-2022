package syntax;

import lex.Lexer;
import lex.UnknownTokenException;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * origin grammar seen the at the bottom
 * changed grammar:
 * Terminal
 * + - * ** ( ) x NUM
 */

public class Parser
{
    private final Lexer lexer;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
    }

    public HashMap<BigInteger, Polynomial> calculate()
    {
        HashMap<BigInteger, Polynomial> polynomials = null;
        try
        {
            polynomials = Expr.calculate(lexer);
        } catch (UnknownTokenException | SyntaxException e)
        {
            System.out.println(e.getMessage());
        }
        return polynomials;
    }

}

// origin grammar given
// * EXP          -> BLANK_TERM ADD_SUB BLANK_TERM TERM BLANK_TERM|BLANK_TERM TERM BLANK_TERM
//                      |EXP ADD_SUB BLANK_TERM TERM BLANK_TERM
// * TERM         -> FACTOR|ADD_SUB BLANK_TERM FACTOR|TERM BLANK_TERM * BLANK_TERM FACTOR
// * FACTOR       -> FACTOR_VAR|FACTOR_CON|FACTOR_EXP
// * FACTOR_VAR   -> POWER
// * FACTOR_CON   -> INT
// * FACTOR_EXP   -> ( EXP )|( EXP ) BLANK_TERM INDEX
// * POWER        -> x|x BLANK_TERM INDEX
// * INDEX        -> ** BLANK_TERM INT
// * INT          -> UN_INT|ADD_SUB UN_INT
// * UN_INT       -> DIGIT|DIGIT UN_INT
// * DIGIT        -> 0|1...|9
// * BLANK_TERM   -> ε|BLANK_CHAR BLANK_TERM
// * BLANK_CHAR   -> SPACE|TAB    // \x20|\t
// * ADD_SUB      -> +|-

// Operator Precedence Grammar(reserved and incorrect now)
// * Table
//         *      +   -   *   **  (   )   x   NUM END
//         *  +   <   <   <   E   <   E   <   <   E
//         *  -   <   <   <   E   <   E   <   <   E
//         *  *   E   E   E   E   E   E   E   E   E
//         *  **  =   E   E   E   E   E   E   E   E
//         *  (   E   E   E   E   E   =   E   E   E
//         *  )   E   E   E   E   E   E   E   E   E
//         *  x   E   E   E   E   E   E   E   E   E
//         *  NUM E   E   E   E   E   E   E   E   E
//         *  END E   E   E   E   E   E   E   E   E
//         *
//         * EXP          -> MID_TERM|EXP + TERM|EXP - TERM
//         * MID_TERM     -> + TERM|- TERM|TERM
//         * TERM         -> FACTOR|+ FACTOR|- FACTOR|TERM * FACTOR
//         * FACTOR       -> POWER|FACTOR_CON|FACTOR_EXP
//         * FACTOR_CON   -> MID_NUM|+ MID_NUM|- MID_NUM
//         * FACTOR_EXP   -> ( EXP )|( EXP ) INDEX
//         * POWER        -> x|x INDEX
//         * INDEX        -> ** MID_NUM|** + MID_NUM
//         * MID_NUM      -> NUM
//         *
//         * FIRST_VT
//         *      EXP          + - * x NUM (
//         *      MID_TERM     + - * x NUM (
//         *      TERM         + - * x NUM (
//         *      FACTOR       x NUM + - (
//         *      FACTOR_CON   NUM + -
//         *      FACTOR_EXP   (
//         *      POWER        x
//         *      INDEX        **
//         *      MID_NUM      NUM
//         * LAST_VT
//         *      EXP          + - * ** x ) NUM
//         *      MID_TERM     + - * ** x ) NUM
//         *      TERM         + - * ** x ) NUM
//         *      FACTOR       + - ** x ) NUM
//         *      FACTOR_CON   + - NUM
//         *      FACTOR_EXP   ) ** + NUM
//         *      POWER        x ** + NUM
//         *      INDEX        ** + NUM
//         *      MID_NUM      NUM

//
//        * S            -> x _A F_TERM|NUM F_TERM|+ _D|- _D|( EXP ) _A F_TERM
//        * EXP          -> TERM F_TERM
//        * F_TERM       -> e|+ EXP|- EXP
//        * TERM         -> x _A|NUM|+ _C|- _C|( EXP ) _A
//        * R_TERM       -> FACTOR _B
//        * FACTOR       -> x _A|NUM|+ NUM|- NUM|( EXP ) _A
//        * INT          -> NUM|+ NUM|- NUM
//        * _A           -> e|** INT
//        * _B           -> e|* R_TERM
//        * _C           -> NUM _B|x _A _B|+ NUM _B|- NUM _B|( EXP ) _A _B
//        * _D           -> x _A _F|NUM _F|+ _H|- _H|( EXP ) _A _F
//        * _F           -> e|+ EXP|- EXP|* R_TERM F_TERM
//        * _H           -> NUM _B F_TERM|x _A _B F_TERM
//                          |+ NUM _B F_TERM|- NUM _B F_TERM|( EXP ) _A _B F_TERM