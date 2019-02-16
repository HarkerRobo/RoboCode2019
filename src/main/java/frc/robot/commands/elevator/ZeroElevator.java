package frc.robot.commands.elevator;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;

/**
 * Brings the elevator to the bottom and zeros that position.
 * The command is checked for completion by monitoring the current.
 * 
 * @author Angela Jia
 * @author Arun Sundaresan
 * @author Finn Frankis
 * @since 1/14/19
 */
public class ZeroElevator extends Command {

    private static final double ZERO_SPEED = -0.2;
    private static final int CURRENT_SPIKE = 5;
    
    private ArrayList<Double> currentVals;
    private int VALUES_TO_SAMPLE = 10;
    private int startTime = 0;
    
    public ZeroElevator() {
        requires(Elevator.getInstance());
        currentVals = new ArrayList<Double>();
    }

    public void initialize() {
        startTime = Robot.getTime();
    }

    @Override
    public void execute() {
        Elevator.getInstance().getMasterTalon().set(ControlMode.PercentOutput, ZERO_SPEED);
    }

    /**
     * @return true if wrist has hit hard limit on front side
     */
	@Override
	protected boolean isFinished() {
        if ((Robot.getTime() - startTime) > Elevator.PEAK_TIME) {
            System.out.println(Elevator.getInstance().getMasterTalon().getOutputCurrent());
            currentVals.add(Elevator.getInstance().getMasterTalon().getOutputCurrent());
            if (currentVals.size() >= VALUES_TO_SAMPLE) {
                currentVals.remove(0);
                
                boolean isAllValid = true;
                for (double currentVal : currentVals) {isAllValid = isAllValid && currentVal > CURRENT_SPIKE;}
                return isAllValid;
            }
        }
		return false; 
    }    

    protected void end () {
        Elevator.getInstance().getMasterTalon().set(ControlMode.Disabled, 0);
        Elevator.getInstance().getMasterTalon().setSelectedSensorPosition(0);
        System.out.println("command over");
    }
}