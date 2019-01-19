package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;
import harkerrobolib.util.Gains;

/**
 * Moves the elevator to a specified setpoint
 * using motion magic. 
 * 
 * @author Dawson Chen
 * @author Angela Jia
 * @since 1/14/19
 */
public class MoveElevatorMotionMagic extends Command {

    private double setpoint;
    
     /**
     * Elevator motion magic constants
     */
    public static final double MOTION_MAGIC_kF = 0.0;
    public static final double MOTION_MAGIC_kP = 0.0;
    public static final double MOTION_MAGIC_kI = 0.0;
    public static final double MOITION_MAGIC_kD = 0.0;
    public static final int MOTION_MAGIC_ACCELERATION = 0;
    public static final int CRUISE_VELOCITY = 0;

    private static final boolean MOTION_MAGIC_SENSOR_PHASE = false;
    private static final int ALLOWABLE_ERROR = 0;

    public MoveElevatorMotionMagic(double setpoint) {
        requires(Elevator.getInstance());
        this.setpoint = setpoint;
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(Elevator.getInstance().getMaster().getClosedLoopError(Global.PID_PRIMARY)) <= ALLOWABLE_ERROR;
    }

    @Override
    protected void initialize() {
        Elevator.getInstance().getMaster().selectProfileSlot(Elevator.MOTION_MAGIC_SLOT_INDEX, Global.PID_PRIMARY);
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);
        Elevator.getInstance().getMaster().setSensorPhase(MOTION_MAGIC_SENSOR_PHASE);
    }

    @Override
    protected void execute() {
        Elevator.getInstance().getMaster().set(ControlMode.MotionMagic, setpoint);  
    }
}