package frc.robot.commands.rollers;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import harkerrobolib.util.MathUtil;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Allows manual control over the rollers for intake and outtake
 * 
 * @author Chirag Kaushik
 * @author Shahzeb Lakhani
 * @since January 10, 2019
 */
public class SpinRollersManual extends IndefiniteCommand {
	public SpinRollersManual() {
		requires(Rollers.getInstance());
    }	

    @Override
	public void execute() {
        double driverRightTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightTrigger(), OI.DRIVER_DEADBAND);
        double operatorRightY = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getRightY(), OI.OPERATOR_DEADBAND);
        
        if(Math.abs(operatorRightY) > 0) {
            if(operatorRightY > 0)
                Rollers.getInstance().moveRollers(operatorRightY, RollerDirection.OUT);
            else
                Rollers.getInstance().moveRollers(-1 * operatorRightY, RollerDirection.IN);
		} else if(driverRightTrigger > 0) {
            Rollers.getInstance().moveRollers(driverRightTrigger, RollerDirection.IN);
        } else {
            Rollers.getInstance().stopRollers();
        }
	}
} 