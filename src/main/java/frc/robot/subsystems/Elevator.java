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

<<<<<<< HEAD
    private static Elevator el; 
    private HSTalon elTalon;
    private VictorSPX victorOne;
    private VictorSPX victorTwo;
    
    private static final int PEAK_CURRENT_LIMIT = 0;
    private static final int CONT_CURRENT_LIMIT = 0;
    private static final int PEAK_CURRENT_TIME = 0;
    private static final boolean INVERTED_MASTER = false;
    private static final boolean INVERTED_VICT_ONE = false;
    private static final boolean INVERTED_VICT_TWO = false;

    public static final int REVERSE_SOFT_LIMIT= 0;
    public static final int POSITION_PID = 0;
    public static final double INTAKE_POSITION = 0.0;
    public static final double LOW_SCORING_POSITION = 60.0;
    public static final double MEDIUM_SCORING_POSITION = 120.0;
    public static final double HIGH_SCORING_POSITION = 180.0;
    public static final int MAX_SPEED = 0;
    public static final int SLOW_DOWN_PERCENT = 0;
    public static final int FFGRAV = 0;
    public static final int MAX_OUTPUT_FACTOR = 1;
    public static final double MIN_LESS_OUTPUT_FACTOR = 0;
    public static final double MIN_MORE_OUTPUT_FACTOR = -0.5;

    public static final int POSITION_SLOT_INDEX = 0;
    public static final int MOTIONMAGIC_SLOT_INDEX = 1;

    public static final int PEAK_ACCELERATION = 0;
    public static final int ZERO_CURRENT_SPIKE = 0;
    private static final int CRUISE_VELOCITY = 0;

    private Elevator() {
        elTalon = new HSTalon(CAN_IDs.EL_MASTER);
        victorOne = new VictorSPX(CAN_IDs.EL_VICTOR_ONE);
        victorTwo = new VictorSPX(CAN_IDs.EL_VICTOR_TWO);
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
        victorOne.follow(elTalon);
        victorTwo.follow(elTalon);
        victorOne.setNeutralMode(NeutralMode.Brake);
        victorTwo.setNeutralMode(NeutralMode.Brake);
        elTalon.setNeutralMode(NeutralMode.Brake);
        elTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
        elTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        elTalon.configPeakCurrentDuration(PEAK_CURRENT_TIME);
        elTalon.setInverted(INVERTED_MASTER);
        victorOne.setInverted(INVERTED_VICT_ONE);
        victorTwo.setInverted(INVERTED_VICT_TWO);

        Elevator.getInstance().getMaster().config_kF(POSITION_SLOT_INDEX, MoveElevatorPosition.kF);
        Elevator.getInstance().getMaster().config_kP(POSITION_SLOT_INDEX, MoveElevatorPosition.kP);
        Elevator.getInstance().getMaster().config_kI(POSITION_SLOT_INDEX, MoveElevatorPosition.kI);
        Elevator.getInstance().getMaster().config_kD(POSITION_SLOT_INDEX, MoveElevatorPosition.kD);
        Elevator.getInstance().getMaster().configMotionAcceleration(Elevator.PEAK_ACCELERATION);
        Elevator.getInstance().getMaster().configMotionCruiseVelocity(Elevator.CRUISE_VELOCITY);
    }

    /**
     * Moves the elevator at a certain speed forward
     * @param speed is the inputed speed at which the elevator is to move forward at
     */
    public void moveElevatorVelocity(double speed) {
        elTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    
        elTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    
        elTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    
        elTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    }

    /**
     * Gets the master talon of the elevator.
     * @return the master talon of the elevator
     */
    public HSTalon getMaster() {
        return elTalon;
    }

    /**
     * Gets one of the victor sensors
     * @return the first of the victor sensors
     */
    public VictorSPX getVictorOne() {
        return victorOne;
    }

    /**
     * Gets second one of the victor sensors
     * @return the second of the victor sensors
     */
    public VictorSPX getVictorTwo() {
        return victorTwo;
    }
=======
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

   public static final int RAIL_POSITION; // TUNE
   public static final int BALL_INTAKING_HEIGHT; // Tune

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
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
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

         HATCH_INTAKING_POSITION = 8100;
         CARGO_INTAKING_POSITION = 0;

         RAIL_POSITION = 20500; // TUNE
         BALL_INTAKING_HEIGHT = 100; // Tune

         FFGRAV = 0.095;
         ZERO_CURRENT_SPIKE = 0;

         LOW_SCORING_POSITION_HATCH = 8100;
         LOW_ROCKET_SCORING_POSITION_CARGO = 5418;
         MEDIUM_SCORING_POSITION_HATCH = 13020;
         MEDIUM_SCORING_POSITION_HATCH_BACK = 22830;
         MEDIUM_ROCKET_SCORING_POSITION_CARGO = 12000;
         HIGH_SCORING_POSITION_HATCH = 22830;
         HIGH_ROCKET_SCORING_POSITION_CARGO = 22700;
         CARGO_SHIP_SCORING_POSITION_CARGO_FRONT = 18200;
         CARGO_SHIP_SCORING_POSITION_CARGO_BACK = 15151;

         ARM_COLLISION_HEIGHT = 18000; // TUNE

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

         HATCH_INTAKING_POSITION = 8100;
         CARGO_INTAKING_POSITION = 0;

         RAIL_POSITION = 20500; // TUNE
         BALL_INTAKING_HEIGHT = 100; // Tune

         FFGRAV = 0.095;
         ZERO_CURRENT_SPIKE = 0;

         LOW_SCORING_POSITION_HATCH = 8100;
         LOW_ROCKET_SCORING_POSITION_CARGO = 5418;
         MEDIUM_SCORING_POSITION_HATCH = 13020;
         MEDIUM_SCORING_POSITION_HATCH_BACK = 22830;
         MEDIUM_ROCKET_SCORING_POSITION_CARGO = 12000;
         HIGH_SCORING_POSITION_HATCH = 22830;
         HIGH_ROCKET_SCORING_POSITION_CARGO = 22700;
         CARGO_SHIP_SCORING_POSITION_CARGO_FRONT = 18200;
         CARGO_SHIP_SCORING_POSITION_CARGO_BACK = 15151;

         ARM_COLLISION_HEIGHT = 16000;
         LIMELIGHT_NECESSARY_ELEVATOR_HEIGHT = 7000;
      }
   }

   /**
    * Constants used to ramp down past elevator soft limit
    */
   public static final int REVERSE_SOFT_LIMIT = 0;
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
      if (el == null)
         el = new Elevator();
      return el;
   }

   public void talonInit() {
      masterTalon.configFactoryDefault();
      leftFrontVictor.configFactoryDefault();
      leftBackVictor.configFactoryDefault();
      followerTalon.configFactoryDefault();

      try {
         Thread.sleep(50);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      followMasters();
      
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


      masterTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
      masterTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
      masterTalon.configPeakCurrentDuration(PEAK_TIME);
      masterTalon.enableCurrentLimit(true);

      masterTalon.configVoltageCompSaturation(Global.BAT_SATURATION_VOLTAGE);
      masterTalon.enableVoltageCompensation(true);

      // setUpPositionPID();
      setUpMotionMagic();
   }

   public void followMasters() {
      leftFrontVictor.follow(masterTalon);
      leftBackVictor.follow(masterTalon);
      followerTalon.follow(masterTalon);
   }
   public void moveElevatorPercentOutput(double speed) {
      getMasterTalon().set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
   }


   public void setUpMotionMagic() {
      Elevator.getInstance().getMasterTalon()
            .configMotionAcceleration(MoveElevatorMotionMagic.MOTION_MAGIC_ACCELERATION);
      Elevator.getInstance().getMasterTalon().configMotionCruiseVelocity(MoveElevatorMotionMagic.CRUISE_VELOCITY);

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
    * Determines whether the elevator is above a given position, within the
    * tolerance of an allowable error. Must have preconfigured the selected sensor
    * as an encoder.
    */
   public boolean isAbove(int position) {
      return isAbove(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY), position);
   }

   public boolean isAbove(int comparedPosition, int comparisonPosition) {
      return comparedPosition - comparisonPosition > MoveElevatorMotionMagic.ALLOWABLE_ERROR;
   }

   /**
    * Determines whether the elevator is below a given position, within the
    * tolerance of an allowable error. Must have preconfigured the selected sensor
    * as an encoder.
    */
   public boolean isBelow(int position) {
      return isBelow(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY), position);
   }

   /**
    * Determines whether the elevator is below a given position, within the
    * tolerance of an allowable error. Must have preconfigured the selected sensor
    * as an encoder.
    */
   public boolean isBelow(int comparedPosition, int comparisonPosition) {
      return comparedPosition - comparisonPosition < -MoveElevatorMotionMagic.ALLOWABLE_ERROR;
   }

   public boolean isAt(int position) {
      return Math.abs(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY)
            - position) <= MoveElevatorMotionMagic.ALLOWABLE_ERROR;
   }

   public void setElevator(ControlMode mode, double value) {
      Elevator.getInstance().getMasterTalon().set(mode, value, DemandType.ArbitraryFeedForward, FFGRAV);
   }

   public int getCurrentPositionEncoder() {
      return getMasterTalon().getSelectedSensorPosition();
   }
>>>>>>> ac58b745ae8c3a643e884c68f815bec4f1529829
}