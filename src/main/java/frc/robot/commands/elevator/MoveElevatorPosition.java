package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;

/**
 * Moves the elevator to a specified encoder position
 * using position PID.
 * 
 * @author Aimee W
 * @author Shahzeb L
 * @author Jatin K
 */
public class MoveElevatorPosition extends Command
{
    private double setpoint;

    public static final double kF = 0.0;
    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    public MoveElevatorPosition(double setpoint)
    {
        requires(Elevator.getInstance());
        this.setpoint = setpoint;
    } 

    @Override
    public void initialize()
    {
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        
        Elevator.getInstance().getMaster().selectProfileSlot(Elevator.POSITION_SLOT_INDEX, Global.PID_PRIMARY);
    }
         
    public void execute()
    {
        Elevator.getInstance().getMaster().set(ControlMode.Position, setpoint);    
            
    }
    
    @Override
	protected boolean isFinished() {
		return false;
	}
        
}