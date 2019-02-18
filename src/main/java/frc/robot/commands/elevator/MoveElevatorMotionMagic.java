package frc.robot.commands.elevator;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.Global;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Elevator;

/**
 * Moves the elevator to a specified setpoint
 * using motion magic. 
 * 
 * @author Dawson Chen
 * @author Angela Jia
 * @since 1/14/19
 */
public class MoveElevatorMotionMagic extends Command {

    private int setpoint;
    
     /**
     * Elevator motion magic constants
     */
    public static final double KF = 0.31;
    public static final double KP = 0.26;
    public static final double KI = 0.0015;
    public static final double KD = 5;
    public static final int IZONE = 500;
    public static final int MOTION_MAGIC_ACCELERATION = 10000;
    public static final int CRUISE_VELOCITY = 5000;

    public static final boolean MOTION_MAGIC_SENSOR_PHASE = false;
    public static final int ALLOWABLE_ERROR = 100;

    private Supplier<Integer> setpointLambda;

    public MoveElevatorMotionMagic(int setpoint) {
        requires(Elevator.getInstance());
        //try {throw new RuntimeException();} catch (Exception e) {e.printStackTrace();}
        this.setpoint = setpoint;
    }

    public MoveElevatorMotionMagic (Supplier<Integer> setpointLambda) {
        this(0);
        this.setpointLambda = setpointLambda;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished() {
        return Math.abs(setpoint - Elevator.getInstance().getMasterTalon().getSelectedSensorPosition()) <= ALLOWABLE_ERROR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize() {
       
        // if(Elevator.getInstance().isBelow(Elevator.SAFE_LOW_PASSTHROUGH_POSITION)  && Arm.getInstance().getDirection() == ArmDirection.UP) {
        //         new SetArmPosition(ArmDirection.DOWN).start();
        //}
        if (setpointLambda != null) {this.setpoint = setpointLambda.get();}
        System.out.println("EL MOTION MAGIC " + setpoint);
        Elevator.getInstance().setUpMotionMagic();
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        Elevator.getInstance().getMasterTalon().set(ControlMode.MotionMagic, setpoint);  
        SmartDashboard.putNumber("el error", Elevator.getInstance().getMasterTalon().getClosedLoopError()) ;
    
    }

    public void end () {System.out.println("command ended");}
}
