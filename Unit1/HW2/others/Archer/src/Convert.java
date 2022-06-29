import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import calculate.CalcMul;
import calculate.Calculate;
import calculate.Function;
import parsing.Result;
import parsing.Result.Type;

public class Convert {
    public HashMap<String, Function> functions;

    public Convert() {
        this.functions = new HashMap<>();
    }

    public Calculate doExpr(Result result) {
        // expr = 0:empty 1:[0:sign 1:empty] 2:item 3:{0:empty 1:sign 2:empty 3:item} 4:empty
        List<Result> resultList = result.getResultList();
        Result subResultSE = resultList.get(1);
        Result subResultIT = resultList.get(2);
        Result subResultSI = resultList.get(3);

        Calculate expr = doItem(subResultIT);
        if (subResultSE.getType() != Type.Empty) {
            if (doSignIsNeg(subResultSE.getResultList().get(0))) {
                expr = expr.neg();
            }
        }

        Calculate oneItem;
        for (Result r : subResultSI.getResultList()) {
            List<Result> subList = r.getResultList();
            oneItem = doItem(subList.get(3));
            if (doSignIsNeg(subList.get(1))) {
                oneItem = oneItem.neg();
            }
            expr = expr.add(oneItem);
        }

        return expr;
    }

    public Calculate doItem(Result result) {
        // item = 0:[0:sign 1:empty] 1:factor 2:{ 0:empty 1:"*" 2:empty 3:factor }
        List<Result> resultList = result.getResultList();
        Result subResultSE = resultList.get(0);
        Result subResultFA = resultList.get(1);
        Result subResultAF = resultList.get(2);

        Calculate item = doFactor(subResultFA);
        for (Result r : subResultAF.getResultList()) {
            List<Result> subList = r.getResultList();
            item = item.mul(doFactor(subList.get(3)));
        }

        if (subResultSE.getType() != Type.Empty) {
            if (doSignIsNeg(subResultSE.getResultList().get(0))) {
                item = item.neg();
            }
        }

        return item;
    }

    public Calculate doFactor(Result result) {
        // factor = 0:factor_var | 1:factor_const | 2:factor_expr
        int choice = result.getChoiceNo();
        Result inner = result.getChoice();

        switch (choice) {
            case 0: // factor_var
                return doFactorVar(inner);
            case 1: // factor_const
                return CalcMul.newDec(doFactorConst(inner));
            case 2: // factor_expr
                return doFactorExpr(inner);
            default:
                throw new RuntimeException("impossible.");
        }
    }

    public Calculate doFactorVar(Result result) {
        // factor_var = 0:func_pow | 1:func_tri | 2:func_call | 3:func_sum
        int choice = result.getChoiceNo();
        Result inner = result.getChoice();

        switch (choice) {
            case 0: // func_pow
                return doFuncPow(inner);
            case 1: // func_tri
                return doFuncTri(inner);
            case 2: // func_call
                return doFuncCall(inner);
            case 3: // func_sum
                return doFuncSum(inner);
            default:
                throw new RuntimeException("impossible.");
        }
    }

    public BigInteger doFactorConst(Result result) {
        // factor_const = 0:signed_decimal
        return doSignedDecimal(result.getResultList().get(0));
    }

    public Calculate doFactorExpr(Result result) {
        // factor_expr = 0:"(" 1:expr 2:")" 3:[0:empty 1:power]
        List<Result> resultList = result.getResultList();
        Result subExp = resultList.get(1);
        Result subEmP = resultList.get(3);
        Calculate expr = doExpr(subExp);
        if (subEmP.getType() != Type.Empty) {
            BigInteger power = doPower(subEmP.getResultList().get(1));
            expr = expr.pow(power.intValueExact());
        }
        return expr;
    }

    public CalcMul doFuncPow(Result result) {
        // func_pow = 0:(0:var_func | 1:"i") 1:[0:empty 1:power]
        List<Result> resultList = result.getResultList();
        Result subVI = resultList.get(0);
        Result subEP = resultList.get(1);

        CalcMul var;
        int varPower = 1;
        if (subEP.getType() != Type.Empty) {
            BigInteger power = doPower(subEP.getResultList().get(1));
            varPower = power.intValueExact();
        }

        switch (subVI.getChoiceNo()) {
            case 0: // var_func
                var = CalcMul.newVar(doVarFunc(subVI.getChoice()), varPower);
                break;
            case 1: // "i"
                var = CalcMul.newVar(subVI.getChoice().getString(), varPower);
                break;
            default:
                throw new RuntimeException("impossible.");
        }

        return var;
    }

    public CalcMul doFuncTri(Result result) {
        // func_tri = 0:("sin" | "cos") 1:empty 2:"(" 3:empty
        // 4:factor 5:empty 6:")" 7:[0:empty 1:power]
        List<Result> resultList = result.getResultList();
        Result subSC = resultList.get(0);
        Result subFA = resultList.get(4);
        Result subEP = resultList.get(7);

        Calculate inner = doFactor(subFA);
        BigInteger power = BigInteger.ONE;
        if (subEP.getType() != Type.Empty) {
            power = doPower(subEP.getResultList().get(1));
        }

        switch (subSC.getChoiceNo()) {
            case 0:
                return CalcMul.newSin(inner, power.intValueExact());
            case 1:
                return CalcMul.newCos(inner, power.intValueExact());
            default:
                throw new RuntimeException("impossible.");
        }
    }

    public BigInteger doPower(Result result) {
        // power = 0:"**" 1:empty 2:["+"] 3:decimal
        List<Result> resultList = result.getResultList();
        return doDecimal(resultList.get(3));
    }

    public BigInteger doSignedDecimal(Result result) {
        // signed_decimal = 0:[sign] 1:decimal
        List<Result> resultList = result.getResultList();
        Result subSI = resultList.get(0);
        Result subDE = resultList.get(1);
        boolean isNeg = false;
        if (subSI.getType() != Type.Empty) {
            isNeg = doSignIsNeg(subSI);
        }
        BigInteger resultInt = doDecimal(subDE);
        if (isNeg) {
            resultInt = resultInt.negate();
        }
        return resultInt;
    }

    public BigInteger doDecimal(Result result) {
        // decimal = regex: [0-9]+
        String strResult = result.getString();
        return new BigInteger(strResult);
    }

    public boolean doSignIsNeg(Result result) {
        // sign = regex: [+-]
        String strResult = result.getString();
        return strResult.equals("-");
    }

    public Function doFuncDef(Result result) {
        // func_def = 0:func_name 1:empty 2:"(" 3:empty 4:var_func 5:empty
        // . . . . . . 6:{0:"," 1:empty 2:var_func, 3:empty}[0-2] 7:")"
        // . . . . . . 8:empty 9:"=" 10:empty 11:func_expr
        List<Result> resultList = result.getResultList();
        Result subFN = resultList.get(0);
        Result subV1 = resultList.get(4);
        Result subVX = resultList.get(6);
        Result subEX = resultList.get(11);
        String funcName = doFuncName(subFN);
        ArrayList<String> funcArgNames = new ArrayList<>();
        funcArgNames.add(doVarFunc(subV1));
        for (Result r : subVX.getResultList()) {
            List<Result> subList = r.getResultList();
            Result subVF = subList.get(2);
            funcArgNames.add(doVarFunc(subVF));
        }
        Function func = new Function(doFuncExpr(subEX), funcArgNames);
        this.functions.put(funcName, func);
        return func;
    }

    public String doVarFunc(Result result) {
        // var_func = regex: [xyz]
        String strResult = result.getString();
        return strResult;
    }

    public Calculate doFuncCall(Result result) {
        // func_call = 0:func_name 1:empty 2:"(" 3:empty 4:factor 5:empty
        // . . . . . . 6:{0:"," 1:empty 2:factor 3:empty}[0-2] 7:")"
        List<Result> resultList = result.getResultList();
        Result subFN = resultList.get(0);
        Result subF1 = resultList.get(4);
        Result subFX = resultList.get(6);

        String funcName = doFuncName(subFN);
        ArrayList<Calculate> funcArgs = new ArrayList<>();

        funcArgs.add(doFactor(subF1));
        for (Result r : subFX.getResultList()) {
            List<Result> subList = r.getResultList();
            Result subFA = subList.get(2);
            funcArgs.add(doFactor(subFA));
        }

        return this.functions.get(funcName).call(funcArgs);
    }

    public String doFuncName(Result result) {
        // func_name = regex: [fgh]
        String strResult = result.getString();
        return strResult;
    }

    public Calculate doFuncSum(Result result) {
        // func_sum = 0:"sum" 1:"(" 2:empty 3:"i" 4:empty
        // 5:{ 0:"," 1:empty 2:factor_const 3:empty }[2] 6:"," 7:empty 8:sum_expr 9:empty 10:")"
        List<Result> resultList = result.getResultList();
        List<Result> lstFC = resultList.get(5).getResultList();
        Result subSE = resultList.get(8);
        Result fcBeg = lstFC.get(0).getResultList().get(2);
        Result fcEnd = lstFC.get(1).getResultList().get(2);

        Calculate summary = CalcMul.ZERO;

        BigInteger iBeg = doFactorConst(fcBeg);
        BigInteger iEnd = doFactorConst(fcEnd);
        Calculate inner = doSumExpr(subSE);
        HashMap<String, Calculate> iSub = new HashMap<>();
        for (BigInteger i = iBeg; i.compareTo(iEnd) <= 0; i = i.add(BigInteger.ONE)) {
            iSub.put("i", CalcMul.newDec(i));
            summary = summary.add(inner.substitute(iSub));
        }
        return summary;
    }

    public Calculate doFuncExpr(Result result) {
        // func_expr = expr
        return doExpr(result.getResultList().get(0));
    }

    public Calculate doSumExpr(Result result) {
        // sum_expr = factor
        return doFactor(result.getResultList().get(0));
    }
}
