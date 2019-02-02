package frc.robot.util;

/**
 * Represents a pair of two values of different types.
 * 
 * @author Finn Frankis
 * @since 2/2/19
 */
public class Pair <K, V> {
    private K first;
    private V second;

    public Pair (K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst () {
        return first;
    }

    public V getSecond () {
        return second;
    }
}