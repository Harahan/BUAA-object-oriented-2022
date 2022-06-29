import java.math.BigInteger;

public interface ValueBody extends Comparable<ValueBody> {
    int getId();

    BigInteger getPrice();

    void use(Adventurer adventurer) throws Exception;

    void print();
}
