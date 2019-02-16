package frc.robot.commands.wrist;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;

/**
 * Sets the wrist along the path that allows it
 * to hit the limit switch at the front.
 * 
 * @author Angela Jia
 * @since 2/8/19
 */
public class ZeroWrist extends Command {
    private static final double ZERO_SPEED = 0.2;
    private static final int CURRENT_SPIKE = 15;
    
    private ArrayList<Double> currentVals;
    private int VALUES_TO_SAMPLE = 10;
    private int startTime = 0;
    
    public ZeroWrist() {
        requires(Wrist.getInstance());
    }

    public void initialize() {
        startTime = Robot.getTime();
    }

    @Override
    public void execute() {
        Wrist.getInstance().setWristPercentOutput(ZERO_SPEED, WristDirection.TO_FRONT);
    }

    /**
     * @return true if wrist has hit hard limit on front side
     */
	@Override
	protected boolean isFinished() {
        if ((Robot.getTime() - startTime) > Wrist.PEAK_TIME) {
            currentVals.add(Wrist.getInstance().getMasterTalon().getOutputCurrent());
            if (currentVals.size() >= VALUES_TO_SAMPLE) {
                currentVals.remove(0);
                
                boolean isAllValid = true;
                for (double currentVal : currentVals) {isAllValid = isAllValid && currentVal > CURRENT_SPIKE;}
                return isAllValid;
            }
        }
		return false; 
	}    
}