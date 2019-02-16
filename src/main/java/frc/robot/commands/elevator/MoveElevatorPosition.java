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
    public static final double POSITION_PID_kF = 0.0;
    public static final double POSITION_PID_kP = 0.0;
    public static final double POSITION_PID_kI = 0.0;
    public static final double POSITION_PID_kD = 0.0;

    private static final boolean SENSOR_PHASE = false;
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
        Elevator.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        Elevator.getInstance().getMasterTalon().selectProfileSlot(Elevator.POSITION_PID_SLOT_INDEX, Global.PID_PRIMARY);
        Elevator.getInstance().getMasterTalon().setSensorPhase(SENSOR_PHASE);
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
		return Math.abs(Elevator.getInstance().getMasterTalon().getClosedLoopError(Global.PID_PRIMARY)) <= ALLOWABLE_ERROR;
	}
        
}