package calculate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CalcSum implements Calculate {
    private final Set<CalcMul> muls;

    private CalcSum(List<CalcMul> muls) {
        this.muls = new HashSet<>(muls);
    }

    private CalcSum(Set<CalcMul> muls) {
        this.muls = muls;
    }

    public static CalcSum fromMuls(CalcMul... muls) {
        return new CalcSum(Arrays.asList(muls));
    }

    public static CalcSum fromTwoMul(CalcMul a, CalcMul b) {
        return CalcSum.fromMuls(a, b);
    }

    public static CalcSum fromList(List<CalcMul> list) {
        return new CalcSum(new ArrayList<>(list));
    }

    public Calculate pow(int power) {
        Calculate result = this;
        for (int i = 1; i < power; i++) {
            result = result.mul(this);
        }
        return result;
    }

    public Calculate substitute(HashMap<String, Calculate> varValues) {
        Calculate result = CalcMul.ZERO;
        for (CalcMul mul : this.muls) {
            result = result.add(mul.substitute(varValues));
        }
        return result;
    }

    private static void addToSet(Set<CalcMul> target, CalcMul item) {
        if (item.equals(CalcMul.ZERO)) {
            return;
        }
        ArrayList<CalcMul> pointer = new ArrayList<>(1);
        target.removeIf(new Predicate<CalcMul>() {
            // should only remove one
            public boolean test(CalcMul t) {
                boolean result = t.bodyEquals(item);
                if (result) {
                    pointer.add(t);
                }
                return result;
            }
        });
        if (pointer.size() > 1) {
            throw new RuntimeException("imposibble.");
        }
        if (pointer.size() > 0) {
            CalcMul newItem = (CalcMul) pointer.get(0).add(item);

            if (!newItem.equals(CalcMul.ZERO)) {
                target.add(newItem);
            }
        } else {
            target.add(item);
        }
    }

    public Calculate add(Calculate rhs) {
        Set<CalcMul> nmuls = new HashSet<>(this.muls);
        if (rhs instanceof CalcMul) {
            CalcMul rhsMul = (CalcMul) rhs;
            CalcSum.addToSet(nmuls, rhsMul);
        } else if (rhs instanceof CalcSum) {
            for (CalcMul rhsMul : ((CalcSum) rhs).muls) {
                CalcSum.addToSet(nmuls, rhsMul);
            }
        } else {
            throw new RuntimeException("impossible.");
        }
        return new CalcSum(nmuls);
    }

    public Calculate mul(Calculate rhs) {
        Set<CalcMul> rMuls = new HashSet<>();
        if (rhs instanceof CalcMul) {
            if (rhs.equals(CalcMul.ZERO)) {
                return CalcMul.ZERO;
            }
            rMuls.add((CalcMul) rhs);
        } else if (rhs instanceof CalcSum) {
            rMuls.addAll(((CalcSum) rhs).muls);
        } else {
            throw new RuntimeException("impossible.");
        }

        Set<CalcMul> nmuls = new HashSet<>();

        for (CalcMul myMul : this.muls) {
            for (CalcMul rhsMul : rMuls) {
                Calculate result = myMul.mul(rhsMul);
                if (result instanceof CalcMul) {
                    CalcSum.addToSet(nmuls, (CalcMul) result);
                } else if (result instanceof CalcSum) {
                    for (CalcMul rr : ((CalcSum) result).muls) {
                        CalcSum.addToSet(nmuls, rr);
                    }
                } else {
                    throw new RuntimeException("impossible.");
                }
            }
        }

        if (nmuls.size() > 1) {
            return new CalcSum(nmuls);
        } else {
            return (Calculate) nmuls.toArray()[0];
        }
    }

    public String toString() {
        ArrayList<String> segments = new ArrayList<>();
        for (CalcMul factor : this.muls) {
            segments.add(factor.toString());
        }
        return String.join("", segments);
    }

    // for hashmap
    public boolean equals(Object other) {
        if (!(other instanceof CalcSum)) {
            return false;
        }
        return this.muls.equals(((CalcSum) other).muls);
    }

    // fuck hashmap
    public int hashCode() {
        return 0;
    }

    public CalcSum neg() {
        List<CalcMul> negated = new ArrayList<>();
        for (CalcMul mul : this.muls) {
            negated.add(mul.neg());
        }
        return new CalcSum(negated);
    }
}
