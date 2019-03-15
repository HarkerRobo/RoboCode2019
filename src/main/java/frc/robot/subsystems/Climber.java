package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;
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

   private HSTalon talon;
   private VictorSPX victor;
    
   private static final boolean TALON_INVERTED; 
   private static final boolean VICTOR_INVERTED; 
    
   private static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake; 
   private static final int CONT_CURRENT_LIMIT;
   private static final int PEAK_CURRENT_LIMIT;
   private static final int PEAK_TIME;
    
   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         TALON_INVERTED = false;
         VICTOR_INVERTED = false;
         CONT_CURRENT_LIMIT = 40;
         PEAK_CURRENT_LIMIT = 80;
         PEAK_TIME = 1000;
         } else {
            TALON_INVERTED = false;
            VICTOR_INVERTED = false;
            CONT_CURRENT_LIMIT = 40; 
            PEAK_CURRENT_LIMIT = 80;
            PEAK_TIME = 1000;
         }
   }

   /**
    * Represents the direction in which the robot should climb. 
    * UP means to begin climbing onto level 3; DOWN means to perform a reverse climb.
    */
    public enum ClimbDirection {
      UP (1), DOWN(-1);
      private int sign;
      private ClimbDirection(int direction) {
         this.sign = direction;
      }

      public int getSign () {
         return sign;
      }
   }
    
    private Climber() {
      talon = new HSTalon(CAN_IDs.CLIMBER_TALON);
      victor = new VictorSPX(CAN_IDs.CLIMBER_VICTOR);
   }
    
    public void talonInit() {
      talon.setNeutralMode(NEUTRAL_MODE);
      victor.setNeutralMode(NEUTRAL_MODE);

      talon.setInverted(TALON_INVERTED);
      victor.setInverted(VICTOR_INVERTED);

      talon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
      talon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
      talon.configPeakCurrentDuration(PEAK_TIME);
      victor.follow(talon);
   }

    public void initDefaultCommand() {
        // setDefaultCommand();
   }

   public void setClimberOutput(ClimbDirection direction, double magnitude) {
      if(direction == ClimbDirection.DOWN) {
         talon.set(ControlMode.PercentOutput, magnitude * direction.getSign());
         } else {
            talon.set(ControlMode.PercentOutput, magnitude * direction.getSign());
         }
   }

   public static Climber getInstance() {
      if(instance == null) {
         instance = new Climber();
      }
      return instance;
   }
    
   public IMotorController getController () {
      return talon;
   }
}