package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;

/**
 * Brings the elevator to the bottom and zeros that position.
 * The command is checked for completion by monitoring the current.
 * 
 * @author Angela Jia
 * @author Arun Sundaresan
 * @since 1/14/19
 */
public class ZeroElevator extends Command {

    private static final double ZERO_DOWN_SPEED = -0.3;
    private static final int ZERO_POSITION = 0;
    public ZeroElevator() {
        requires(Elevator.getInstance());        
    }

	@Override
	protected boolean isFinished() {
		return Elevator.getInstance().getMaster().getOutputCurrent() >= Elevator.ZERO_CURRENT_SPIKE;
	}

    public void initialize() {
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
    }

    public void execute() {
        Elevator.getInstance().getMaster().set(ControlMode.PercentOutput, ZERO_DOWN_SPEED);
    }

    public void end() {
        Elevator.getInstance().getMaster().setSelectedSensorPosition(ZERO_POSITION, Global.PID_PRIMARY);
    }
}