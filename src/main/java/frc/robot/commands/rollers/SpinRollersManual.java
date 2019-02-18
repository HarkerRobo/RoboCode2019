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
        double output = 0;
        RollerDirection rollerDirection = RollerDirection.IN;
        if(OI.getInstance().getDriver() == OI.Driver.PRANAV) {
            double driverRightTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightTrigger(), OI.DRIVER_DEADBAND);      
            double driverLeftTrigger = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND);    
            if(driverRightTrigger > 0) {
                output = Rollers.getInstance().getRecommendedRollersOutput();
                rollerDirection = RollerDirection.OUT;
            } else {
                output = driverLeftTrigger;
                rollerDirection = RollerDirection.IN;
            }
        } else if(OI.getInstance().getDriver() == OI.Driver.CHRIS){
            if(OI.getInstance().getDriverGamepad().getButtonXState()) {
                output = Rollers.getInstance().getRecommendedRollersOutput();
                rollerDirection = RollerDirection.OUT;
            } else {
                if(OI.getInstance().getDriverGamepad().getLeftTrigger() > 0) {
                    output = Rollers.DEFAULT_ROLLER_MAGNITUDE;
                    rollerDirection = RollerDirection.IN;
                }
            }
        } else {
            if(OI.getInstance().getDriverGamepad().getButtonXState())  {
                output = Rollers.getInstance().getRecommendedRollersOutput();
                rollerDirection = rollerDirection.IN;
            } else {
                if(OI.getInstance().getDriverGamepad().getButtonYState()) {
                    output = Rollers.getInstance().getRecommendedRollersOutput();
                    rollerDirection = rollerDirection.OUT;
                }
            }
        }
        
        if((int) Math.signum(output) == RollerDirection.IN.getSign() && Math.abs(output) > Rollers.HATCH_STOW_SPEED)
            new LoadOrScoreHatch(ScoreState.LOAD);
        Rollers.getInstance().moveRollers(output, rollerDirection);
	}
}