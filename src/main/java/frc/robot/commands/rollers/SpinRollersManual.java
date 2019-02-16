package frc.robot.commands.rollers;

import frc.robot.OI;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch.ScoreState;
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
        double driverRightX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightX(), OI.DRIVER_DEADBAND);      
        if((int) Math.signum(driverRightX) == RollerDirection.IN.getSign() && Math.abs(driverRightX) > Rollers.HATCH_STOW_SPEED)
            new LoadOrScoreHatch(ScoreState.LOAD);
        if (driverRightX > 0) // joystick up
            Rollers.getInstance().moveRollers(Math.abs(driverRightX), RollerDirection.OUT);
        else if (driverRightX <= 0) // joystick down
            Rollers.getInstance().moveRollers(Math.abs(driverRightX), RollerDirection.IN);
	}
}