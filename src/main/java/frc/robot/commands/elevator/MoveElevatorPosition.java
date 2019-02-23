package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap;
import frc.robot.RobotMap.Global;
import frc.robot.RobotMap.RobotType;
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
    public static final double KF;
    public static final double KP;
    public static final double KI;
    public static final double KD;
    public static final int IZONE;

    private static final int ALLOWABLE_ERROR;
    static {
        if(RobotMap.ROBOT_TYPE == RobotType.COMP){
            KF = 0.0;
            KP = 0.09;
            KI = 0.0007;
            KD = 0.0;
            IZONE = 1500;
            
            ALLOWABLE_ERROR = 0;
        }
        else {
            KF = 0.0;
            KP = 0.09;
            KI = 0.0007;
            KD = 0.0;
            IZONE = 1500;
    
            ALLOWABLE_ERROR = 0;
        }
    }
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