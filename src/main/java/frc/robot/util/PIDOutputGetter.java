package frc.robot.util;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * Represents a PIDOutput type for use with WPILib's PIDController with easy output accession.
 * 
 * @author Finn Frankis
 * 
 * @since 1/12/19
 */
public class PIDOutputGetter implements PIDOutput {
    private double output;

    @Override
    public void pidWrite(double output) {
        this.output = output;    
    }

    public double getOutput() {
        return output;
    }
}