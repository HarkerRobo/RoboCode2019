package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.RobotMap.RobotType;
import frc.robot.commands.drivetrain.DriveWithVelocityManual;
import frc.robot.util.Pair;
import harkerrobolib.subsystems.HSDrivetrain;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.SpeedUnit;
import harkerrobolib.wrappers.HSPigeon;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents the drivetrain on the robot.
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * @author Rohan Rashingkar
 * @author Anirudh Kotamraju
 * @author Angela Jia
 * @author Arnav Gupta
 * @author Dawson Chen
 * @since 1/7/19
 */
public class Drivetrain extends HSDrivetrain {
   private static Drivetrain instance;

   private final static boolean LEFT_MASTER_INVERTED;
   private final static boolean RIGHT_MASTER_INVERTED;
   private final static boolean LEFT_FOLLOWER_INVERTED;
   private final static boolean RIGHT_FOLLOWER_INVERTED;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         LEFT_MASTER_INVERTED = true;
         RIGHT_MASTER_INVERTED = false;
         LEFT_FOLLOWER_INVERTED = true;
         RIGHT_FOLLOWER_INVERTED = false;
      } else {
         LEFT_MASTER_INVERTED = true;
         RIGHT_MASTER_INVERTED = false;
         LEFT_FOLLOWER_INVERTED = true;
         RIGHT_FOLLOWER_INVERTED = false;
      }
   }

   private final static double MAX_FORWARD_VELOCITY = 13; // 12
   private final static double MAX_TURN_VELOCITY = 7.25; // 5

   private static int TALON_PEAK_LIMIT = 20;
   private static int TALON_PEAK_TIME = 750;
   private static int TALON_CONTINUOUS_LIMIT = 15;

   public static final int ALLOWABLE_ERROR = 0;
   public static final int POSITION_SLOT_INDEX = 0;
   public static final int ANGLE_SLOT_INDEX = 1;
   public static final int VELOCITY_SLOT_INDEX = 2;
   public static final int MOTION_PROF_SLOT = 3;
   public static final boolean LEFT_PIGEON_PHASE = true;
   public static final boolean RIGHT_PIGEON_PHASE = true;
   public static final boolean RIGHT_POSITION_PHASE = false;
   public static final boolean LEFT_POSITION_PHASE = false;

   public static final int WHEEL_DIAMETER = 4;

   /**
    * Creates new instance of Drivetrain.
    */
   private Drivetrain() {
      super(new HSTalon(CAN_IDs.DT_LEFT_MASTER), new HSTalon(CAN_IDs.DT_RIGHT_MASTER),
            new VictorSPX(CAN_IDs.DT_LEFT_FOLLOWER), new VictorSPX(CAN_IDs.DT_RIGHT_FOLLOWER),
            new HSPigeon(CAN_IDs.PIGEON));
   }

   /**
    * Intializes the default command of the drivetrain.
    */
   @Override
   protected void initDefaultCommand() {
      // setDefaultCommand(new AlignWithLimelight(198, 0, 4));
      setDefaultCommand(new DriveWithVelocityManual());
   }

   /**
    * A method to initialize the Talons for the start of the match.
    */
   public void talonInit() {
      getLeftMaster().configFactoryDefault();
      getRightMaster().configFactoryDefault();

      followMasters();
      resetTalonInverts();
      setNeutralMode(NeutralMode.Brake);
      getLeftMaster().configVoltageCompSaturation(Global.BAT_SATURATION_VOLTAGE);
      getRightMaster().configVoltageCompSaturation(Global.BAT_SATURATION_VOLTAGE);
      getLeftMaster().enableVoltageCompensation(true);
      getRightMaster().enableVoltageCompensation(true);
      // getLeftMaster().configContinuousCurrentLimit(TALON_CONTINUOUS_LIMIT, 10);
      // getRightMaster().configContinuousCurrentLimit(TALON_CONTINUOUS_LIMIT, 10);
      // getLeftMaster().configPeakCurrentDuration(TALON_PEAK_TIME, 10);
      // getRightMaster().configPeakCurrentDuration(TALON_PEAK_TIME, 10);
      // getLeftMaster().configPeakCurrentLimit(TALON_PEAK_LIMIT, 10);
      // getRightMaster().configPeakCurrentLimit(TALON_PEAK_LIMIT, 10);
      setCurrentLimit(TALON_PEAK_LIMIT, TALON_PEAK_TIME, TALON_CONTINUOUS_LIMIT);
      getLeftMaster().enableCurrentLimit(true);
      getRightMaster().enableCurrentLimit(true);

      configVelocityClosedLoop();
   }

   public void configVelocityClosedLoop() {
      Drivetrain.getInstance().getLeftMaster().config_kP(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.LEFT_KP);
      Drivetrain.getInstance().getLeftMaster().config_kI(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.LEFT_KI);
      Drivetrain.getInstance().getLeftMaster().config_kD(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.LEFT_KD);
      Drivetrain.getInstance().getLeftMaster().config_kF(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.LEFT_KF);

      Drivetrain.getInstance().getRightMaster().config_kP(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.RIGHT_KP);
      Drivetrain.getInstance().getRightMaster().config_kI(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.RIGHT_KI);
      Drivetrain.getInstance().getRightMaster().config_kD(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.RIGHT_KD);
      Drivetrain.getInstance().getRightMaster().config_kF(Drivetrain.VELOCITY_SLOT_INDEX,
            DriveWithVelocityManual.RIGHT_KF);

      Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
      Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
      Drivetrain.getInstance().configBothFeedbackSensors(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
      Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
      Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);
   }

   public void resetTalonInverts() {
      invertTalons(LEFT_MASTER_INVERTED, RIGHT_MASTER_INVERTED, LEFT_FOLLOWER_INVERTED, RIGHT_FOLLOWER_INVERTED);
   }

   public void arcadeDrivePercentOutput(double forwardPercent, double turnPercent) {
      double forwardSpeed = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, forwardPercent * MAX_FORWARD_VELOCITY,
            SpeedUnit.ENCODER_UNITS);
      double turnSpeed = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, turnPercent * MAX_TURN_VELOCITY,
            SpeedUnit.ENCODER_UNITS);

      getLeftMaster().set(ControlMode.PercentOutput,
            (forwardSpeed + turnSpeed) * DriveWithVelocityManual.LEFT_KF / 1023.0);
      getRightMaster().set(ControlMode.PercentOutput,
            (forwardSpeed - turnSpeed) * DriveWithVelocityManual.RIGHT_KF / 1023.0);
   }

   public void arcadeDriveVelocity(double forwardPercent, double turnPercent) {
      double forwardSpeed = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, forwardPercent * MAX_FORWARD_VELOCITY,
            SpeedUnit.ENCODER_UNITS);
      double turnSpeed = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, turnPercent * MAX_TURN_VELOCITY,
            SpeedUnit.ENCODER_UNITS);

      SmartDashboard.putNumber("left velocity", forwardSpeed);
      SmartDashboard.putNumber("current percent", getLeftMaster().getMotorOutputPercent());
      SmartDashboard.putNumber("current velocity", getLeftMaster().getSelectedSensorVelocity());

      // SmartDashboard.putNumber("", getLeftMaster().);
      getLeftMaster().set(ControlMode.Velocity, forwardSpeed + turnSpeed);
      getRightMaster().set(ControlMode.Velocity, forwardSpeed - turnSpeed);
   }

   /**
    * Sets the output of the talons from a pair. The first and second values in the
    * pair correspond to the outputs of the left and right talons, respectively.
    * Both values in the pair should be in the range [-1, 1].
    * 
    * @param outputs a pair repsresenting the outputs of the left and right talons
    */
   public void arcadeDrivePercentOutput(Pair<Double, Double> outputs) {
      getLeftMaster().set(ControlMode.PercentOutput, outputs.getFirst());
      getRightMaster().set(ControlMode.PercentOutput, outputs.getSecond());
   }

   public boolean hasHatchPanel() {
      return getLeftMaster().getSensorCollection().isFwdLimitSwitchClosed();
   }

   public boolean hasCargo() {
      return getRightMaster().getSensorCollection().isFwdLimitSwitchClosed();
   }

   /**
    * Gets outputs for the left and right talons in order to travel at a given
    * speed and turn weighted to specific degree.
    * 
    * @param speed the speed to travel at [-1, 1]
    * @param turn  the amount to turn [-1, 1]
    */
   public Pair<Double, Double> getTalonOutputs(double speed, double turn) {
      return new Pair<Double, Double>(speed + turn, speed - turn);
   }

   /**
    * Returns an instance of Drivetrain.
    * 
    * @return an instance of Drivetrain
    */
   public static Drivetrain getInstance() {
      if (instance == null) {
         instance = new Drivetrain();
      }
      return instance;
   }

}