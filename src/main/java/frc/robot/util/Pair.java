package frc.robot.util;

/**
 * Represents a pair of two values of different types.
 * 
 * @author Finn Frankis
 * @since 2/2/19
 */
public class Pair <K, V> {
    private K firstEntry;
    private V secondEntry;

    public Pair (K firstEntry, V secondEntry) {
        this.firstEntry = firstEntry;
        this.secondEntry = secondEntry;
    }

    public K getFirstEntry () {
        return firstEntry;
    }

    public V getSecondEntry () {
        return secondEntry;
    }
}