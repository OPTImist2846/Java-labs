package task5;

public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }

    public boolean comparePairs(Pair<T, U> otherPair) {
        return this.first.equals(otherPair.getFirst()) &&
                this.second.equals(otherPair.getSecond());
    }

    @Override
    public String toString() {
        return "Парa [" + first + ", " + second + "]";
    }
}