package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot.Side;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.rollers.SpinRollersManual;
import frc.robot.util.Pair;
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

   private static final boolean TOP_INVERTED;
   private static final boolean BOTTOM_INVERTED;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         TOP_INVERTED = false;
         BOTTOM_INVERTED = false;
      } else {
         TOP_INVERTED = true;
         BOTTOM_INVERTED = true;
      }
   }

   private static final int CONTINUOUS_CURRENT_LIMIT = 7;
   private static final int PEAK_CURRENT_LIMIT = 10;
   private static final int PEAK_TIME = 500;
   private static final double ROCKET_HIGH_SCORING_SPEED = 0.8;
   private static final double TOP_SPIN = 0.1;

   public static final double DEFAULT_ROLLER_MAGNITUDE = 0.8;
   public static final double ROLLER_SHOOTING_SPEED = 0.65;
   public static final double HATCH_STOW_SPEED = 0.75;

   public static final double CARGO_BAY_OUTPUT_REDUCTION = 0.55;

   public static final double STOP_INTAKING_CURRENT = 15;

   private static Rollers instance;
   private HSTalon rTalonTop;

   /**
    * Creates new Talons
    */
   private Rollers() {
      rTalonTop = new HSTalon(CAN_IDs.RO_TOP);
   }

   @Override
   protected void initDefaultCommand() {
      // setDefaultCommand(new SpinRollersManual());
   }

   /**
    * Initialises the Talons
    */
   public void talonInit() {
      rTalonTop.configFactoryDefault();

      rTalonTop.setNeutralMode(NeutralMode.Coast);

      rTalonTop.setInverted(TOP_INVERTED);

      System.out.println(TOP_INVERTED);

      setupCurrentLimits();
   }

   public void setupCurrentLimits() {
      rTalonTop.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);

      rTalonTop.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);

      rTalonTop.configPeakCurrentDuration(PEAK_TIME);

      rTalonTop.enableCurrentLimit(true);
   }

   public HSTalon getTopTalon() {
      return rTalonTop;
   }

   public void stopRollers() {
      rTalonTop.set(ControlMode.Disabled, 0);
   }

   public void moveRollers(double output, RollerDirection direction) {
      rTalonTop.set(ControlMode.PercentOutput, output * direction.getSign());
   }

   public double getRecommendedRollersInput() {
      return DEFAULT_ROLLER_MAGNITUDE;
   }

   /**
    * Pair<top output, bottom output>
    */
   public double getRecommendedRollersOutput() {
      if (OI.getInstance().getCargoBayToggleMode()
            && Elevator.getInstance().isAt(Location.CARGO_SHIP_BACK.getCargoHeight())) {
         return DEFAULT_ROLLER_MAGNITUDE - CARGO_BAY_OUTPUT_REDUCTION;
      }
      // if(Math.abs(wristAngle - 90)
      // return ROLLER_SHOOTING_SPEED;
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
}