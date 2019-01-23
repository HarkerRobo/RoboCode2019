package frc.robot.util;

import java.util.function.Function;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Allows for
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * 
 * @since 1/21/19
 */
public class PIDSourceCustomGet implements PIDSource {
    private Supplier<Double> pidGet;
    private Function<Double, Double> linearizationFunction;
    private PIDSourceType sourceType;

    /**
     * Creates a custom PID source getter without a linearization function.
     * 
     * @param pidGet the supplier which returns the sensor value
     * @param sourceType the PID source type (displacement or rate)
     */    
    public PIDSourceCustomGet(Supplier<Double> pidGet, PIDSourceType sourceType) {
        this(pidGet, (value) -> (value), sourceType);
    }

    /**
     * Creates a custom PID source getter with an linearization function.
     * 
     * @param pidGet the supplier which returns the sensor value
     * @param linearizationFunction the function which will be applied to the sensor value to linearize it 
     * @param sourceType the PID source type (displacement or rate)
     */
    public PIDSourceCustomGet(Supplier<Double> pidGet, Function<Double, Double> linearizationFunction, PIDSourceType sourceType) {
        this.pidGet = pidGet;
        this.linearizationFunction = linearizationFunction;                
        this.sourceType = sourceType;
    }



    @Override
    public void setPIDSourceType(PIDSourceType sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return sourceType;
    }

    @Override
    public double pidGet() {
        return linearizationFunction.apply(pidGet.get());
    }

}