package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;

/**
 * Move the elevator 
 * 
 * @author Dawson Chen
 * @since 1/14/19
 */
public class MoveElevatorMotionMagic extends Command {

	private double setpoint;

    public MoveElevatorMotionMagic(double setpoint) {
        requires(Elevator.getInstance());
        this.setpoint = setpoint;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {
        Elevator.getInstance().getMaster().selectProfileSlot(Elevator.MOTIONMAGIC_SLOT_INDEX, Global.PID_PRIMARY);
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);
    }

    @Override
    protected void execute() {
        Elevator.getInstance().getMaster().set(ControlMode.MotionMagic, setpoint);  
    }
}