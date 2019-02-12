package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.commands.elevator.MoveElevatorManual;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.elevator.MoveElevatorPosition;
import harkerrobolib.util.Gains;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents the elevator on the robot.
 * 
 * @author Angela Jia
 * @author Finn Frankis
 * @author Chirag Kaushik
 * @since 1/10/19
 */
public class Elevator extends Subsystem {
    private static Elevator el; 

    private HSTalon talon;
    private VictorSPX leftFrontVictor;
    private VictorSPX leftBackVictor;
    private VictorSPX rightVictor;
    public static final int SAFE_LOW_PASSTHROUGH_POSITION = 0;

    private static final int PEAK_CURRENT_LIMIT = 0;
    private static final int CONT_CURRENT_LIMIT = 0;
    private static final int PEAK_CURRENT_TIME = 0;

    private static final boolean INVERTED_MASTER = false;
    private static final boolean INVERTED_VICT_LEFT_FRONT = false;
    private static final boolean INVERTED_VICT_LEFT_BACK = false;
    private static final boolean INVERTED_VICT_RIGHT = false;

    public static final int INTAKE_POSITION = 0;
    public static final int LOW_SCORING_POSITION = 60;
    public static final int MEDIUM_SCORING_POSITION = 120;
    public static final int HIGH_SCORING_POSITION = 180;

    public static final int LOW_MIDDLE_BOUNDARY = (LOW_SCORING_POSITION + MEDIUM_SCORING_POSITION)/2;
    public static final int RAIL_POSITION = 100; //TUNE
    
    public static final int FFGRAV = 0;
    public static final int ZERO_CURRENT_SPIKE = 0;

    /**
     * Constants used to ramp down past elevator soft limit
     */
    public static final int REVERSE_SOFT_LIMIT= 0;
    public static final int MAX_SPEED = 0;
    public static final int SLOW_DOWN_PERCENT = 0;
    public static final int MAX_OUTPUT_FACTOR = 1;
    public static final double MIN_LESS_OUTPUT_FACTOR = 0;
    public static final double MIN_MORE_OUTPUT_FACTOR = -0.5;

    /**
     * PID slot indices
     */
    public static final int POSITION_PID_SLOT_INDEX = 0;
    public static final int MOTION_MAGIC_SLOT_INDEX = 1;

    private Elevator() {
        talon = new HSTalon(CAN_IDs.EL_MASTER);
        leftFrontVictor = new VictorSPX(CAN_IDs.EL_VICTOR_LEFT_FRONT);
        leftBackVictor = new VictorSPX(CAN_IDs.EL_VICTOR_LEFT_BACK);
        rightVictor = new VictorSPX(CAN_IDs.EL_VICTOR_RIGHT);
    }

    
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new MoveElevatorManual());
    }

    public static Elevator getInstance() {
        if(el == null)
            el = new Elevator();
        return el;
    }


    public void talonInit() {
        leftFrontVictor.follow(talon);
        leftBackVictor.follow(talon);
        rightVictor.follow(talon);

        talon.setNeutralMode(NeutralMode.Brake);
        leftFrontVictor.setNeutralMode(NeutralMode.Brake);
        leftBackVictor.setNeutralMode(NeutralMode.Brake);
        rightVictor.setNeutralMode(NeutralMode.Brake);

        talon.setInverted(INVERTED_MASTER);
        leftFrontVictor.setInverted(INVERTED_VICT_LEFT_FRONT);
        leftBackVictor.setInverted(INVERTED_VICT_LEFT_BACK);
        rightVictor.setInverted(INVERTED_VICT_RIGHT);
        
        talon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
        talon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        talon.configPeakCurrentDuration(PEAK_CURRENT_TIME);

        setUpPositionPID();
        setUpMotionMagic();
    }

    public void moveElevatorVelocity(double speed) {
        talon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    
        talon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    
        talon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    
        talon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    }

    public void setUpPositionPID() {
        getMaster().configClosedLoopConstants(POSITION_PID_SLOT_INDEX, new Gains().kP(MoveElevatorPosition.POSITION_PID_kP)
                                                                                  .kI(MoveElevatorPosition.POSITION_PID_kI)
                                                                                  .kD(MoveElevatorPosition.POSITION_PID_kD)
                                                                                  .kF(MoveElevatorPosition.POSITION_PID_kF));
    }

    public void setUpMotionMagic() {
        Elevator.getInstance().getMaster().configMotionAcceleration(MoveElevatorMotionMagic.MOTION_MAGIC_ACCELERATION);
        Elevator.getInstance().getMaster().configMotionCruiseVelocity(MoveElevatorMotionMagic.CRUISE_VELOCITY);
        getMaster().configClosedLoopConstants(MOTION_MAGIC_SLOT_INDEX, new Gains().kP(MoveElevatorMotionMagic.MOTION_MAGIC_kP)
                                                                                  .kI(MoveElevatorMotionMagic.MOTION_MAGIC_kI)
                                                                                  .kD(MoveElevatorMotionMagic.MOITION_MAGIC_kD)
                                                                                  .kF(MoveElevatorMotionMagic.MOTION_MAGIC_kF));
    }
    
    public HSTalon getMaster() {
        return talon;
    }

    public VictorSPX getLeftFrontVictor() {
        return leftFrontVictor;
    }

    public VictorSPX getLeftBackVictor() {
        return leftBackVictor;
    }

    public VictorSPX getRightVictor() {
        return rightVictor;
    }

    /**
     * Determines whether the elevator is below a given position, within the tolerance of an allowable error.
     * Must have preconfigured the selected sensor as an encoder.
     */
    public boolean isBelow (int position) {
        return getMaster().getSelectedSensorPosition(Global.PID_PRIMARY) - position 
                            < -MoveElevatorMotionMagic.ALLOWABLE_ERROR;
    }

    /**
     * Determines whether the elevator is above a given position, within the tolerance of an allowable error.
     * Must have preconfigured the selected sensor as an encoder.
     */
    public boolean isAbove (int position) {
        return getMaster().getSelectedSensorPosition(Global.PID_PRIMARY) - position 
                            > MoveElevatorMotionMagic.ALLOWABLE_ERROR;                    
    }

    public boolean isAbove(int comparedPosition, int comparisonPosition) {
        return comparedPosition - comparisonPosition > MoveElevatorMotionMagic.ALLOWABLE_ERROR;
    }
    public boolean isAt (int position) {
        return Math.abs(getMaster().getSelectedSensorPosition(Global.PID_PRIMARY) - position)
                            <= MoveElevatorMotionMagic.ALLOWABLE_ERROR;
    }    
}