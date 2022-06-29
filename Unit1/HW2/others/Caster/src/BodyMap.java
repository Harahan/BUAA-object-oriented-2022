import java.math.BigInteger;
import java.util.HashMap;

public class BodyMap {
    public HashMap<String,BigInteger> getBodyMap() {
        return this.bodyMap;
    }
    
    private HashMap<String, BigInteger> bodyMap;
    
    public BodyMap(String body,BigInteger index) {
        HashMap<String,BigInteger> bodyMap = new HashMap<>();
        bodyMap.put(body,index);
        this.bodyMap = bodyMap;
    }
    
    public void add(String body,BigInteger index) {
        if (this.bodyMap.containsKey(body)) {
            this.bodyMap.put(body,index.add(this.bodyMap.get(body)));
        } else { this.bodyMap.put(body,index); }
    }
    
    public BodyMap mult(BodyMap multper) {
        BodyMap ans = new BodyMap("1",BigInteger.ZERO);
        HashMap<String, BigInteger> multperMap = multper.getBodyMap();
        for (String body : this.bodyMap.keySet()) {
            ans.add(body,this.bodyMap.get(body));
        }
        for (String body : multperMap.keySet()) {
            ans.add(body,multperMap.get(body));
        }
        return ans;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }
        HashMap<String,BigInteger> b = ((BodyMap) obj).getBodyMap();
        if (this.bodyMap.size() != b.size()) { return false; }
        for (String str : this.bodyMap.keySet()) {
            if (!b.containsKey(str)) { return false; }
            if (b.get(str).compareTo(this.bodyMap.get(str)) != 0) { return false; }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int prime = 10;
        for (String str : this.bodyMap.keySet()) {
            prime = prime + str.length();
        }
        return prime;
    }
    
}
