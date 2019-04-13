package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;
import frc.robot.commands.climber.MoveClimber;
import harkerrobolib.wrappers.HSTalon;

/**
 * Controls the climb of the robot.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @since 3/14/19
 */
public class Climber extends Subsystem {

   private static Climber instance;

   private HSTalon leftTalon;
   private HSTalon rightTalon;
   private DoubleSolenoid solenoid;
    
   private static final boolean LEFT_TALON_INVERTED; 
   private static final boolean RIGHT_TALON_INVERTED; 
    
   private static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake; 
   private static final int CONT_CURRENT_LIMIT_LEFT;
   private static final int PEAK_CURRENT_LIMIT_LEFT;
   private static final int CONT_CURRENT_LIMIT_RIGHT;
   private static final int PEAK_CURRENT_LIMIT_RIGHT;
   private static final int PEAK_TIME;
   private static final DoubleSolenoid.Value CLIMBER_FREE_VALUE;
   private static final DoubleSolenoid.Value CLIMBER_RIGID_VALUE;
   public static final double CLIMB_SPEED = 1;
   public static final double ARBITRARY_FF = 0.04;
    
   public static final double DEFAULT_SUCTION_OUTPUT = 0.0;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         LEFT_TALON_INVERTED = false;
         RIGHT_TALON_INVERTED = false;
         CONT_CURRENT_LIMIT_LEFT = 40;
         PEAK_CURRENT_LIMIT_LEFT = 80;
         CONT_CURRENT_LIMIT_RIGHT = 40; 
         PEAK_CURRENT_LIMIT_RIGHT = 80;
         PEAK_TIME = 1000;
         CLIMBER_FREE_VALUE = DoubleSolenoid.Value.kForward;
         CLIMBER_RIGID_VALUE = DoubleSolenoid.Value.kReverse;
      } else {
         LEFT_TALON_INVERTED = false;
         RIGHT_TALON_INVERTED = false;
         CONT_CURRENT_LIMIT_LEFT = 40; 
         PEAK_CURRENT_LIMIT_LEFT = 80;
         CONT_CURRENT_LIMIT_RIGHT = 40; 
         PEAK_CURRENT_LIMIT_RIGHT = 80;
         PEAK_TIME = 1000;
         CLIMBER_FREE_VALUE = DoubleSolenoid.Value.kForward;
         CLIMBER_RIGID_VALUE = DoubleSolenoid.Value.kReverse;
      }
   }
    
   public enum ClimberState {
      FREE(CLIMBER_FREE_VALUE), RIGID(CLIMBER_RIGID_VALUE);
      private DoubleSolenoid.Value state;
      private ClimberState(DoubleSolenoid.Value state) {
         this.state = state;
      }
      public DoubleSolenoid.Value getState() {
         return state;
      }
   }

    private Climber() {
      leftTalon = new HSTalon(CAN_IDs.CLIMBER_TALON_LEFT);
      rightTalon = new HSTalon(CAN_IDs.CLIMBER_TALON_RIGHT);
   }
    
    public void talonInit() {
      leftTalon.setNeutralMode(NEUTRAL_MODE);
      rightTalon.setNeutralMode(NEUTRAL_MODE);

      leftTalon.setInverted(LEFT_TALON_INVERTED);
      rightTalon.setInverted(RIGHT_TALON_INVERTED);

      leftTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT_LEFT);
      rightTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT_RIGHT);
      leftTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT_LEFT);
      rightTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT_RIGHT);
      leftTalon.configPeakCurrentDuration(PEAK_TIME);
      rightTalon.configPeakCurrentDuration(PEAK_TIME);

      rightTalon.follow(leftTalon);
   }

    public void initDefaultCommand() {
        setDefaultCommand(new MoveClimber(DEFAULT_SUCTION_OUTPUT));
   }

   public void setClimberOutput(double magnitude) {
      setClimberOutput(magnitude, magnitude);
   }

   public void setClimberOutput(double leftMagnitude, double rightMagnitude) {
      leftTalon.set(ControlMode.PercentOutput, leftMagnitude, DemandType.ArbitraryFeedForward, ARBITRARY_FF);
   }

   public void disableClimber() {
      leftTalon.set(ControlMode.Disabled, 0);
   }

   public void setClimberArmState(DoubleSolenoid.Value value) {
      solenoid.set(value);
   }

   public DoubleSolenoid.Value getState() {
      return solenoid.get();
   }

   public static Climber getInstance() {
      if(instance == null) {
         instance = new Climber();
      }
      return instance;
   }
   
   public HSTalon getLeftTalon() {
      return leftTalon;
   }

   public HSTalon getRightTalon() {
      return rightTalon;
   }
}