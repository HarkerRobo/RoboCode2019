package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
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

    private HSTalon masterTalon;
    private VictorSPX leftFrontVictor;
    private VictorSPX leftBackVictor;
    private HSTalon followerTalon;
    public static final int SAFE_LOW_PASSTHROUGH_POSITION = 0;
    public static final int SAFE_HIGH_PASSTHROUGH_POSITION = 1000; // tune

    private static final boolean SENSOR_PHASE = true;

    private static final int PEAK_CURRENT_LIMIT = 25;
    private static final int CONT_CURRENT_LIMIT = 15;
    private static final int PEAK_CURRENT_TIME = 150;

    private static final int RIGHT_TALON_FEEDBACK_COEFFICIENT = 1;

    private static final boolean INVERTED_MASTER = true;
    private static final boolean INVERTED_VICT_LEFT_FRONT = true;
    private static final boolean INVERTED_VICT_LEFT_BACK = true;
    private static final boolean INVERTED_VICT_RIGHT = true;

    public static final int INTAKE_POSITION = 0;
    
    public static final int HATCH_INTAKING_POSITION = 0;
    public static final int CARGO_INTAKING_POSITION = 20;

    public static final int RAIL_POSITION = 100; //TUNE
    public static final int BALL_INTAKING_HEIGHT = 100; //Tune
    
    public static final int FFGRAV = 0;
    public static final int ZERO_CURRENT_SPIKE = 0;

    public static final int LOW_SCORING_POSITION_HATCH = 0; 
    public static final int LOW_SCORING_POSITION_CARGO = 0; 
    public static final int MEDIUM_SCORING_POSITION_HATCH = 0; 
    public static final int MEDIUM_SCORING_POSITION_CARGO = 0; 
    public static final int HIGH_SCORING_POSITION_HATCH = 0; 
    public static final int HIGH_SCORING_POSITION_CARGO = 0; 

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
        masterTalon = new HSTalon(CAN_IDs.EL_MASTER);
        leftFrontVictor = new VictorSPX(CAN_IDs.EL_VICTOR_LEFT_FRONT);
        leftBackVictor = new VictorSPX(CAN_IDs.EL_VICTOR_LEFT_BACK);
        followerTalon = new HSTalon(CAN_IDs.EL_TALON_FOLLOWER);
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
        leftFrontVictor.follow(masterTalon);
        leftBackVictor.follow(masterTalon);
        followerTalon.follow(masterTalon);

        masterTalon.setNeutralMode(NeutralMode.Brake);
        leftFrontVictor.setNeutralMode(NeutralMode.Brake);
        leftBackVictor.setNeutralMode(NeutralMode.Brake);
        followerTalon.setNeutralMode(NeutralMode.Brake);

        masterTalon.setInverted(INVERTED_MASTER);
        leftFrontVictor.setInverted(INVERTED_VICT_LEFT_FRONT);
        leftBackVictor.setInverted(INVERTED_VICT_LEFT_BACK);
        followerTalon.setInverted(INVERTED_VICT_RIGHT);
        
        masterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        masterTalon.setSensorPhase(SENSOR_PHASE);
        // masterTalon.configRemoteFeedbackFilter(followerTalon.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, Global.REMOTE_0);
        // masterTalon.configSelectedFeedbackCoefficient(Elevator.RIGHT_TALON_FEEDBACK_COEFFICIENT);
        // masterTalon.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor0);

        masterTalon.setSelectedSensorPosition(0);

        masterTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
        masterTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        masterTalon.configPeakCurrentDuration(PEAK_CURRENT_TIME);
        masterTalon.enableCurrentLimit(true);

        // setUpPositionPID();
        // setUpMotionMagic();
    }

    public void moveElevatorVelocity(double speed) {
        getMasterTalon().set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    }

    public void setUpPositionPID() {
        getMasterTalon().configClosedLoopConstants(POSITION_PID_SLOT_INDEX, new Gains().kP(MoveElevatorPosition.POSITION_PID_kP)
                                                                                  .kI(MoveElevatorPosition.POSITION_PID_kI)
                                                                                  .kD(MoveElevatorPosition.POSITION_PID_kD)
                                                                                  .kF(MoveElevatorPosition.POSITION_PID_kF));
    }

    public void setUpMotionMagic() {
        Elevator.getInstance().getMasterTalon().configMotionAcceleration(MoveElevatorMotionMagic.MOTION_MAGIC_ACCELERATION);
        Elevator.getInstance().getMasterTalon().configMotionCruiseVelocity(MoveElevatorMotionMagic.CRUISE_VELOCITY);
        getMasterTalon().configClosedLoopConstants(MOTION_MAGIC_SLOT_INDEX, new Gains().kP(MoveElevatorMotionMagic.MOTION_MAGIC_kP)
                                                                                  .kI(MoveElevatorMotionMagic.MOTION_MAGIC_kI)
                                                                                  .kD(MoveElevatorMotionMagic.MOITION_MAGIC_kD)
                                                                                  .kF(MoveElevatorMotionMagic.MOTION_MAGIC_kF));
    }
    
    public HSTalon getMasterTalon() {
        return masterTalon;
    }

    public VictorSPX getLeftFrontVictor() {
        return leftFrontVictor;
    }

    public VictorSPX getLeftBackVictor() {
        return leftBackVictor;
    }

    public HSTalon getFollowerTalon() {
        return followerTalon;
    }

    /**
     * Determines whether the elevator is above a given position, within the tolerance of an allowable error.
     * Must have preconfigured the selected sensor as an encoder.
     */
    public boolean isAbove (int position) {
        return isAbove(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY), position);            
    }

    public boolean isAbove(int comparedPosition, int comparisonPosition) {
        return comparedPosition - comparisonPosition > MoveElevatorMotionMagic.ALLOWABLE_ERROR;
    }

    /**
     * Determines whether the elevator is below a given position, within the tolerance of an allowable error.
     * Must have preconfigured the selected sensor as an encoder.
     */
    public boolean isBelow (int position) {
        return isBelow(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY), position);
    }

    /**
     * Determines whether the elevator is below a given position, within the tolerance of an allowable error.
     * Must have preconfigured the selected sensor as an encoder.
     */
    public boolean isBelow (int comparedPosition, int comparisonPosition) {
        return comparedPosition - comparisonPosition < -MoveElevatorMotionMagic.ALLOWABLE_ERROR;
    }

    public boolean isAt (int position) {
        return Math.abs(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY) - position)
                            <= MoveElevatorMotionMagic.ALLOWABLE_ERROR;
    }    
}