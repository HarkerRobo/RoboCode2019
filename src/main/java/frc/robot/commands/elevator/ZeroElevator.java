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

    private static final double ZERO_SPEED = -0.34;
    private static final double CURRENT_SPIKE = 2.8;
    private static final double TIMEOUT = 5000;
    
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
        Elevator.getInstance().setElevator(ControlMode.PercentOutput, ZERO_SPEED);
    }

    /**
     * @return true if wrist has hit hard limit on front side
     */
	@Override
	protected boolean isFinished() {
        if (Robot.getTime() - startTime > TIMEOUT) {
            return true;
        }

		return Elevator.getInstance().getMasterTalon().getSensorCollection().isRevLimitSwitchClosed(); 
    }    

    protected void end () {
        Elevator.getInstance().getMasterTalon().set(ControlMode.Disabled, 0);
        Elevator.getInstance().getMasterTalon().setSelectedSensorPosition(0);
        System.out.println("command over");
    }
}