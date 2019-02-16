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

    /**
     * {@inheritDoc}
     */
	@Override
	protected boolean isFinished() {
		return Elevator.getInstance().getMasterTalon().getOutputCurrent() >= Elevator.ZERO_CURRENT_SPIKE;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        Elevator.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Elevator.getInstance().getMasterTalon().set(ControlMode.PercentOutput, ZERO_DOWN_SPEED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() {
        Elevator.getInstance().getMasterTalon().setSelectedSensorPosition(ZERO_POSITION, Global.PID_PRIMARY);
    }
}