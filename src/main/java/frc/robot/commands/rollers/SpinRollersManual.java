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
 * @since 1/10/19
 */
public class SpinRollersManual extends IndefiniteCommand {
	public SpinRollersManual() {
		requires(Rollers.getInstance());
    }	

    /**
     * The driver controller will take priority and spin the roller according to the amount that the right
     * and left triggers are pressed.
     * {@inheritDoc}
     */
    @Override
	public void execute() {
        double driverRightY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), OI.DRIVER_DEADBAND);      

        if (driverRightY > 0) // joystick up
            Rollers.getInstance().moveRollers(Math.abs(driverRightY), RollerDirection.OUT);
        else if (driverRightY < 0) // joystick down
            Rollers.getInstance().moveRollers(Math.abs(driverRightY), RollerDirection.IN);
	}
}