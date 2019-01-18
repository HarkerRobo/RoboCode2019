package frc.robot.commands.rollers;

import frc.robot.OI;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Allows manual control over the rollers for intake and outtake.
 * 
 * @author Chirag Kaushik
 * @author Shahzeb Lakhani
 * @author Dawson Chen
 * @since January 10, 2019
 */
public class SpinRollersManual extends IndefiniteCommand {
	public SpinRollersManual() {
		requires(Rollers.getInstance());
    }	

    @Override
	public void execute() {
    
        double driverLeftTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND);
        double driverRightTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightTrigger(), OI.DRIVER_DEADBAND);

        if((driverRightTrigger > 0 || driverLeftTrigger > 0)) {
            if(driverRightTrigger > driverLeftTrigger) {
                Rollers.getInstance().moveRollers(driverRightTrigger, RollerDirection.OUT);
            } else {
                Rollers.getInstance().moveRollers(driverLeftTrigger, RollerDirection.IN);
            }      
        }

        else if(OI.HAS_TWO_CONTROLLERS) {
            double operatorRightTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getRightTrigger(), OI.OPERATOR_DEADBAND_TRIGGER);
            double operatorLeftTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getLeftTrigger(), OI.OPERATOR_DEADBAND_TRIGGER);
            double operatorRightJoystick = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getRightY(), OI.OPERATOR_DEADBAND_JOYSTICK);

            if(operatorRightTrigger > 0) {
                Rollers.getInstance().moveRollers(operatorRightTrigger, RollerDirection.OUT);
            }
            else if(operatorLeftTrigger > 0) {
                Rollers.getInstance().moveRollers(operatorLeftTrigger, RollerDirection.IN);
            }
            else if(operatorRightJoystick > 0) {
                Rollers.getInstance().moveRollers(operatorRightJoystick, RollerDirection.IN);
            }
            else if(operatorRightJoystick < 0) {
                Rollers.getInstance().moveRollers(operatorRightJoystick, RollerDirection.OUT);
            }
        }
        else {
            Rollers.getInstance().stopRollers();
        }
	}
}