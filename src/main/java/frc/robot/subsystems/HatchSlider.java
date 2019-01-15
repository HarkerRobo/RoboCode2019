package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;

/**
 * Represents the hatch slider on the robot.
 * 
 * @since 1/12/19
 * @author
 */
public class HatchSlider extends Subsystem {
    private static DoubleSolenoid solenoid;

    private static HatchSlider hatchSlider;

    private HatchSlider () {
        solenoid = new DoubleSolenoid(CAN_IDs.HATCH_FORWARD_CHANNEL, CAN_IDs.HATCH_REVERSE_CHANNEL);
    }
    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand();
    }

    public static HatchSlider getInstance () {
        if (hatchSlider == null)
            hatchSlider = new HatchSlider();
        return hatchSlider;
    }

    public static DoubleSolenoid getSolenoid()
    {
        return solenoid;
    }

}