package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

<<<<<<< HEAD
import edu.wpi.first.wpilibj.DoubleSolenoid;
=======
import edu.wpi.first.wpilibj.VictorSP;
>>>>>>> 44fec7e5b7dbb699e510b15d729b3c254f9c99c1
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

<<<<<<< HEAD
   private HSTalon leftTalon;
   private HSTalon rightTalon;
   private DoubleSolenoid solenoid;
=======
   private HSTalon talon;
   private VictorSPX victor;
>>>>>>> 44fec7e5b7dbb699e510b15d729b3c254f9c99c1
    
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
    
<<<<<<< HEAD
   public static final double DEFAULT_SUCTION_OUTPUT = 0.0;

=======
   public static final double DEFAULT_SUCTION_OUTPUT = 0.5;
>>>>>>> 44fec7e5b7dbb699e510b15d729b3c254f9c99c1
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
    
<<<<<<< HEAD
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
=======
   private Climber() {
      talon = new HSTalon(CAN_IDs.CLIMBER_TALON);
      victor = new VictorSPX(CAN_IDs.CLIMBER_VICTOR);
>>>>>>> 44fec7e5b7dbb699e510b15d729b3c254f9c99c1
   }
    
    public void talonInit() {
      talon.setNeutralMode(NEUTRAL_MODE);
      victor.setNeutralMode(NEUTRAL_MODE);

      talon.setInverted(LEFT_TALON_INVERTED);
      victor.setInverted(RIGHT_TALON_INVERTED);

      talon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT_LEFT);
      talon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT_LEFT);
      talon.configPeakCurrentDuration(PEAK_TIME);

      victor.follow(talon);
   }

    public void initDefaultCommand() {
        setDefaultCommand(new MoveClimber(DEFAULT_SUCTION_OUTPUT));
   }

   public void setClimberOutput(double magnitude) {
      setClimberOutput(magnitude, magnitude);
   }

   public void setClimberOutput(double leftMagnitude, double rightMagnitude) {
      talon.set(ControlMode.PercentOutput, leftMagnitude);
      victor.set(ControlMode.PercentOutput, leftMagnitude);
   }

   public void disableClimber() {
      talon.set(ControlMode.Disabled, 0);
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
<<<<<<< HEAD
   
   public HSTalon getLeftTalon() {
      return leftTalon;
=======
    
   public HSTalon getTalon() {
      return talon;
>>>>>>> 44fec7e5b7dbb699e510b15d729b3c254f9c99c1
   }

   public VictorSPX getVictor() {
      return victor;
   }
}