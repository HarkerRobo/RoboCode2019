package frc.robot.commands.rollers;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import frc.robot.util.Pair;
import harkerrobolib.commands.IndefiniteCommand;

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
        Robot.log("SpinRollersManual constructed.");        
    }	

    /**
     * The driver controller will take priority and spin the roller according to the amount that the right
     * and left triggers are pressed.
     * {@inheritDoc}
     */
    @Override
	public void execute() {
        Pair<Double, Double> output = new Pair<Double, Double> (0.0 , 0.0);
        RollerDirection rollerDirection = RollerDirection.IN;
        if(OI.getInstance().getDriver() == OI.Driver.CHRIS){
            if(OI.getInstance().getDriverGamepad().getButtonXState()) {
                output = Rollers.getInstance().getRecommendedRollersOutput();
                rollerDirection = RollerDirection.OUT;
            } else if(OI.getInstance().getDriverGamepad().getButtonYState()) {
                    output = Rollers.getInstance().getRecommendedRollersInput(); 
                rollerDirection = RollerDirection.IN;
            } else {
                output = new Pair<Double, Double>(0.0, 0.0);
                rollerDirection = RollerDirection.IN;
            }
        } else {
            if(OI.getInstance().getDriverGamepad().getButtonXState())  {
                output = Rollers.getInstance().getRecommendedRollersInput();
                rollerDirection = rollerDirection.IN;
            } else {
                if(OI.getInstance().getDriverGamepad().getButtonYState()) {
                    output = Rollers.getInstance().getRecommendedRollersOutput();
                    rollerDirection = rollerDirection.OUT;
                }
            }
        }
        
        // if(rollerDirection == RollerDirection.IN && (Math.abs(output.getFirst()) > Rollers.HATCH_STOW_SPEED || 
                                                    //  Math.abs(output.getSecond()) > Rollers.HATCH_STOW_SPEED)){}
            // (new LoadOrScoreHatch(ScoreState.LOAD)).start();
        Rollers.getInstance().moveRollers(output.getFirst(), output.getSecond(), rollerDirection);
	}
}