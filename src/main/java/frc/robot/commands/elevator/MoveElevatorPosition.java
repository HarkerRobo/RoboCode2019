package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;

/**
 * Moves the elevator to a specified encoder position
 * using position PID.
 * 
 * @author Aimee W
 * @author Shahzeb L
 * @author Jatin K
 * @author Angela Jia
 * 
 * @since 1/14/19
 */
public class MoveElevatorPosition extends Command
{
    private double setpoint;

     /**
     * Elevator position PID constants
     */
    public static final double KF = 0.0;
    public static final double KP = 0.09;
    public static final double KI = 0.0007;
    public static final double KD = 0.0;
    public static final int IZONE = 1500;

    private static final int ALLOWABLE_ERROR = 0;

    public MoveElevatorPosition(double setpoint)
    {
        requires(Elevator.getInstance());
        this.setpoint = setpoint;
    } 

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize()
    {
        //Elevator.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        Elevator.getInstance().getMasterTalon().selectProfileSlot(Elevator.POSITION_PID_SLOT_INDEX, Global.PID_PRIMARY);
        Elevator.getInstance().setUpPositionPID();
    }
         
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        Elevator.getInstance().getMasterTalon().set(ControlMode.Position, setpoint);   

            
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	protected boolean isFinished() {
		return false;// Math.abs(Elevator.getInstance().getMasterTalon().getClosedLoopError(Global.PID_PRIMARY)) <= ALLOWABLE_ERROR;
	}
        
}