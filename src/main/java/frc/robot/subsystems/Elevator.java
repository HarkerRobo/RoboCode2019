package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.RobotMap.RobotType;
import frc.robot.commands.elevator.MoveElevatorManual;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.elevator.MoveElevatorPosition;
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
    public static final int SAFE_LOW_PASSTHROUGH_POSITION_HATCH;
    public static final int SAFE_LOW_PASSTHROUGH_POSITION_CARGO;
    public static final int SAFE_HIGH_PASSTHROUGH_POSITION; // tune
    public static final int MAX_POSITION;

    private static final boolean SENSOR_PHASE;

    public static final int PEAK_CURRENT_LIMIT;
    public static final int CONT_CURRENT_LIMIT;
    public static final int PEAK_TIME;

    private static final int RIGHT_TALON_FEEDBACK_COEFFICIENT;

    private static final boolean INVERTED_MASTER;
    private static final boolean INVERTED_VICT_LEFT_FRONT;
    private static final boolean INVERTED_VICT_LEFT_BACK;
    private static final boolean INVERTED_TALON_FOLLOWER;

    public static final int INTAKE_POSITION;
    
    public static final int HATCH_INTAKING_POSITION;
    public static final int CARGO_INTAKING_POSITION;

    public static final int RAIL_POSITION; //TUNE
    public static final int BALL_INTAKING_HEIGHT; //Tune
    
    public static final double FFGRAV;
    public static final int ZERO_CURRENT_SPIKE;

    public static final int LOW_SCORING_POSITION_HATCH; 
    public static final int LOW_ROCKET_SCORING_POSITION_CARGO; 
    public static final int MEDIUM_SCORING_POSITION_HATCH; 
    public static final int MEDIUM_SCORING_POSITION_HATCH_BACK;
    public static final int MEDIUM_ROCKET_SCORING_POSITION_CARGO; 
    public static final int HIGH_SCORING_POSITION_HATCH; 
    public static final int HIGH_ROCKET_SCORING_POSITION_CARGO; 
    public static final int CARGO_SHIP_SCORING_POSITION_CARGO_FRONT;
    public static final int CARGO_SHIP_SCORING_POSITION_CARGO_BACK;
    public static final int ARM_COLLISION_HEIGHT;

    public static final double NOMINAL_OUTPUT = 0.06;

    public static final int LIMELIGHT_NECESSARY_ELEVATOR_HEIGHT;

    static {
        if(RobotMap.ROBOT_TYPE == RobotType.COMP) {
            INVERTED_MASTER = true;
            INVERTED_VICT_LEFT_FRONT = true;
            INVERTED_VICT_LEFT_BACK = true;
            INVERTED_TALON_FOLLOWER = true;
            
            SAFE_LOW_PASSTHROUGH_POSITION_HATCH = 0;
            SAFE_LOW_PASSTHROUGH_POSITION_CARGO = 0;
            SAFE_HIGH_PASSTHROUGH_POSITION = 6000; // tune
            MAX_POSITION = 22794;

            SENSOR_PHASE = true;

            PEAK_CURRENT_LIMIT = 30;
            CONT_CURRENT_LIMIT = 20;
            PEAK_TIME = 150;

            RIGHT_TALON_FEEDBACK_COEFFICIENT = 1;
            

            INTAKE_POSITION = 0;

            HATCH_INTAKING_POSITION = 0;
            CARGO_INTAKING_POSITION = 0;

            RAIL_POSITION = 20500; //TUNE
            BALL_INTAKING_HEIGHT = 100; //Tune

            FFGRAV = 0.095;
            ZERO_CURRENT_SPIKE = 0;

            // LOW_SCORING_POSITION_HATCH = 7746; 
            // LOW_ROCKET_SCORING_POSITION_CARGO = 5418; 
            // MEDIUM_SCORING_POSITION_HATCH = 12000; 
            // MEDIUM_SCORING_POSITION_HATCH_BACK = MEDIUM_SCORING_POSITION_HATCH;
            // MEDIUM_ROCKET_SCORING_POSITION_CARGO = 12000; 
            // HIGH_SCORING_POSITION_HATCH = 22500; 
            // HIGH_ROCKET_SCORING_POSITION_CARGO = 22500; 
            // CARGO_SHIP_SCORING_POSITION_CARGO_FRONT = 18375;
            // CARGO_SHIP_SCORING_POSITION_CARGO_BACK = 15151;

            LOW_SCORING_POSITION_HATCH = 10338; 
            LOW_ROCKET_SCORING_POSITION_CARGO = 5418; 
            MEDIUM_SCORING_POSITION_HATCH = 13020; 
            MEDIUM_SCORING_POSITION_HATCH_BACK = 22830;
            MEDIUM_ROCKET_SCORING_POSITION_CARGO = 12000; 
            HIGH_SCORING_POSITION_HATCH = 22830; 
            HIGH_ROCKET_SCORING_POSITION_CARGO = 22700; 
            CARGO_SHIP_SCORING_POSITION_CARGO_FRONT = 18200;
            CARGO_SHIP_SCORING_POSITION_CARGO_BACK = 15151;

            ARM_COLLISION_HEIGHT = 10000; // TUNE

            LIMELIGHT_NECESSARY_ELEVATOR_HEIGHT = 7000;
            } else {
                INVERTED_MASTER = false;
                INVERTED_VICT_LEFT_FRONT = true;
                INVERTED_VICT_LEFT_BACK = true;
                INVERTED_TALON_FOLLOWER = true;
                    
                SAFE_LOW_PASSTHROUGH_POSITION_HATCH = 0;
                SAFE_LOW_PASSTHROUGH_POSITION_CARGO = 0;
                SAFE_HIGH_PASSTHROUGH_POSITION = 6000; // tune
                MAX_POSITION = 22794;
                    
                SENSOR_PHASE = false;
                    
                PEAK_CURRENT_LIMIT = 30;
                CONT_CURRENT_LIMIT = 20;
                PEAK_TIME = 150;
                    
                RIGHT_TALON_FEEDBACK_COEFFICIENT = 1;
                    
                    
                INTAKE_POSITION = 0;
                    
                HATCH_INTAKING_POSITION = 7000;
                CARGO_INTAKING_POSITION = 0;
                    
                RAIL_POSITION = 20500; //TUNE
                BALL_INTAKING_HEIGHT = 100; //Tune
                    
                FFGRAV = 0.095;
                ZERO_CURRENT_SPIKE = 0;
                    
                LOW_SCORING_POSITION_HATCH = 10338; 
                LOW_ROCKET_SCORING_POSITION_CARGO = 5418; 
                MEDIUM_SCORING_POSITION_HATCH = 13020; 
                MEDIUM_SCORING_POSITION_HATCH_BACK = 22830;
                MEDIUM_ROCKET_SCORING_POSITION_CARGO = 12000; 
                HIGH_SCORING_POSITION_HATCH = 22830; 
                HIGH_ROCKET_SCORING_POSITION_CARGO = 22700; 
                CARGO_SHIP_SCORING_POSITION_CARGO_FRONT = 18200;
                CARGO_SHIP_SCORING_POSITION_CARGO_BACK = 15151;
                
                ARM_COLLISION_HEIGHT = 10000;
                LIMELIGHT_NECESSARY_ELEVATOR_HEIGHT = 7000;
        }
    }

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
        masterTalon.configFactoryDefault();
        leftFrontVictor.configFactoryDefault();
        leftBackVictor.configFactoryDefault();
        followerTalon.configFactoryDefault();

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
        followerTalon.setInverted(INVERTED_TALON_FOLLOWER);
        
        masterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        masterTalon.setSensorPhase(SENSOR_PHASE);
        masterTalon.configNominalOutputForward(NOMINAL_OUTPUT);
        masterTalon.configNominalOutputReverse(NOMINAL_OUTPUT);
        // masterTalon.configRemoteFeedbackFilter(followerTalon.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, Global.REMOTE_0);
        // masterTalon.configSelectedFeedbackCoefficient(Elevator.RIGHT_TALON_FEEDBACK_COEFFICIENT);
        // masterTalon.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor0);

        masterTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
        masterTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        masterTalon.configPeakCurrentDuration(PEAK_TIME);
        masterTalon.enableCurrentLimit(true);

        masterTalon.configVoltageCompSaturation(Global.BAT_SATURATION_VOLTAGE);
        masterTalon.enableVoltageCompensation(true);

        // setUpPositionPID();
        // setUpMotionMagic();
    }

    public void moveElevatorPercentOutput(double speed) {
        getMasterTalon().set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    }

    public void setUpPositionPID() {
        // getMasterTalon().configClosedLoopConstants(POSITION_PID_SLOT_INDEX, new Gains().kP(MoveElevatorPosition.KP)
        //                                                                           .kI(MoveElevatorPosition.KI)
        //                                                                           .kD(MoveElevatorPosition.KD)
        //                                                                           .kF(MoveElevatorPosition.KF));
        getMasterTalon().config_kF(POSITION_PID_SLOT_INDEX, MoveElevatorPosition.KF);
        getMasterTalon().config_kP(POSITION_PID_SLOT_INDEX, MoveElevatorPosition.KP);
        getMasterTalon().config_kI(POSITION_PID_SLOT_INDEX, MoveElevatorPosition.KI);
        getMasterTalon().config_kD(POSITION_PID_SLOT_INDEX, MoveElevatorPosition.KD);
        getMasterTalon().config_IntegralZone(POSITION_PID_SLOT_INDEX, MoveElevatorPosition.IZONE);
    }

    public void setUpMotionMagic() {
        Elevator.getInstance().getMasterTalon().configMotionAcceleration(MoveElevatorMotionMagic.MOTION_MAGIC_ACCELERATION);
        Elevator.getInstance().getMasterTalon().configMotionCruiseVelocity(MoveElevatorMotionMagic.CRUISE_VELOCITY);
 // getMasterTalon().configClosedLoopConstants(MOTION_MAGIC_SLOT_INDEX, new Gains().kP(MoveElevatorMotionMagic.KP)
                                                                                //   .kI(MoveElevatorMotionMagic.KI)
                                                                                //   .kD(MoveElevatorMotionMagic.KD)
                                                                                //   .kF(MoveElevatorMotionMagic.KF));   
        getMasterTalon().config_kF(MOTION_MAGIC_SLOT_INDEX, MoveElevatorMotionMagic.KF);
        getMasterTalon().config_kP(MOTION_MAGIC_SLOT_INDEX, MoveElevatorMotionMagic.KP);
        getMasterTalon().config_kI(MOTION_MAGIC_SLOT_INDEX, MoveElevatorMotionMagic.KI);
        getMasterTalon().config_kD(MOTION_MAGIC_SLOT_INDEX, MoveElevatorMotionMagic.KD);
        getMasterTalon().config_IntegralZone(MOTION_MAGIC_SLOT_INDEX, MoveElevatorMotionMagic.IZONE);

        getMasterTalon().selectProfileSlot(Elevator.MOTION_MAGIC_SLOT_INDEX, Global.PID_PRIMARY);
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

    public void setElevator (ControlMode mode, double value) {
        Elevator.getInstance().getMasterTalon().set(mode, value, DemandType.ArbitraryFeedForward, FFGRAV);
    }

    public int getCurrentPositionEncoder() {
        return getMasterTalon().getSelectedSensorPosition();
    }
}