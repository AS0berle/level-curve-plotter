package mathWrappers;

public class Pair<T1, T2> {
    
    private T1 x;
    private T2 y;

    public Pair(T1 first, T2 second) {
        x = first;
        y = second;
    }

    public T1 getFirst() {
        return x;
    }

    public T2 getSecond() {
        return y;
    }

    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ")";
    }
}
