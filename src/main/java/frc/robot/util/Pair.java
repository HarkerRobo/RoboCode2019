package frc.robot.util;

/**
 * Represents a pair of two values of different types.
 * 
 * @author Finn Frankis
 * @since 2/2/19
 */
public class Pair <T1, T2> {
    private T1 first;
    private T2 second;

    public Pair (T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst () {
        return first;
    }

    public T2 getSecond () {
        return second;
    }
}