package frc.robot.util;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Contains various Pneumatics methods.
 * 
 * @author Angela Jia
 * @since 1/29/19
 */
public class PneumaticsUtil {

   /**
    * Switches the solenoid value based on the given value.
    * 
    * @param value DoubleSolenoid value that is switched (either kReverse or kForward)
    *
    * @return the value that is not the given value
    */
   public static DoubleSolenoid.Value switchSolenoidValue(DoubleSolenoid.Value value) {
        return value == DoubleSolenoid.Value.kForward ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward;
   }
}