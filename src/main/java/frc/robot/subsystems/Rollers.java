package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.rollers.SpinRollersManual;
import harkerrobolib.wrappers.HSTalon;

/**
 * Moves the cargo to the cargo space.
 * 
 * @author Shahzeb Lakhani
 * @author Chirag Kaushik
 * @since 1/10/19
 */
public class Rollers extends Subsystem {
   public enum RollerDirection {
      IN(-1), OUT(1);

      private int sign;

      private RollerDirection(int sign) {
         this.sign = sign;
      }

      public int getSign() {
         return sign;
      }
   }

   private static final boolean INVERTED;
   private static final boolean BOTTOM_INVERTED;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         INVERTED = true;
         BOTTOM_INVERTED = false;
      } else {
         INVERTED = false;
         BOTTOM_INVERTED = true;
      }
   }

   private static final int CONTINUOUS_CURRENT_LIMIT = 7;
   private static final int PEAK_CURRENT_LIMIT = 10;
   private static final int PEAK_TIME = 500;
   private static final double ROCKET_HIGH_SCORING_SPEED = 0.8;
   private static final double TOP_SPIN = 0.1;
   private static final double INTAKE_SPEED = 0.7;

   public static final double DEFAULT_ROLLER_MAGNITUDE = 0.7;
   public static final double ROLLER_SHOOTING_SPEED = 0.65;
   public static final double HATCH_STOW_SPEED = 0.75;

   public static final double CARGO_BAY_OUTPUT_REDUCTION = 0.35;

   public static final double STOP_INTAKING_CURRENT = 15;

   public static final double ARBITRARY_FF = 0.15;
   private static Rollers instance;
   private HSTalon talon;

   private static double currentOutput;

   /**
    * Creates new Talons
    */
   private Rollers() {
      talon = new HSTalon(CAN_IDs.ROLLERS_TALON);
      currentOutput = 0.0;
   }

   @Override
   protected void initDefaultCommand() {
      setDefaultCommand(new SpinRollersManual());
   }

   /**
    * Initialises the Talons
    */
   public void talonInit() {
      talon.configFactoryDefault();

      talon.setNeutralMode(NeutralMode.Coast);

      talon.setInverted(INVERTED);

      System.out.println(INVERTED);

      setupCurrentLimits();
   }

   public void setupCurrentLimits() {
      talon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);

      talon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);

      talon.configPeakCurrentDuration(PEAK_TIME);

      talon.enableCurrentLimit(false);
   }

   public HSTalon talon() {
      return talon;
   }

   public void stopRollers() {
      currentOutput = 0;
      talon.set(ControlMode.Disabled, 0);
   }

   public void moveRollers(double output, RollerDirection direction) {
      currentOutput = output;
      talon.set(ControlMode.PercentOutput, output * direction.getSign(), DemandType.ArbitraryFeedForward, ARBITRARY_FF * RollerDirection.IN.getSign());
   }

   public double getRecommendedRollersInput() {
      return INTAKE_SPEED;
   }

   /**
    * Pair<top output, bottom output>
    */
   public double getRecommendedRollersOutput() {
      if (OI.getInstance().getCargoBayToggleMode()
            && Elevator.getInstance().isAt(Location.CARGO_SHIP_BACK.getCargoHeight())) {
         return DEFAULT_ROLLER_MAGNITUDE - CARGO_BAY_OUTPUT_REDUCTION;
      }

      return DEFAULT_ROLLER_MAGNITUDE;
   }

   /**
    * Gets the talon instance.
    * 
    * @return talon instance
    */
   public static Rollers getInstance() {
      if (instance == null)
         instance = new Rollers();
      return instance;
   }

   public double getCurrentOutput() {
      return currentOutput;
   }
}