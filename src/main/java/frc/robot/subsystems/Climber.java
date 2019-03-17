// package frc.robot.subsystems;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.NeutralMode;

// import edu.wpi.first.wpilibj.command.Subsystem;
// import frc.robot.RobotMap;
// import frc.robot.RobotMap.CAN_IDs;
// import frc.robot.RobotMap.RobotType;
// import frc.robot.commands.climber.MoveClimberManual;
// import harkerrobolib.wrappers.HSTalon;

// /**
//  * Controls the climb of the robot.
//  * 
//  * @author Finn Frankis
//  * @author Angela Jia
//  * @since 3/14/19
//  */
// public class Climber extends Subsystem {

//    private static Climber instance;

//    private HSTalon leftTalon;
//    private HSTalon rightTalon;
    
//    private static final boolean TALON_INVERTED; 
//    private static final boolean VICTOR_INVERTED; 
    
//    private static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake; 
//    private static final int CONT_CURRENT_LIMIT_LEFT;
//    private static final int PEAK_CURRENT_LIMIT_LEFT;
//    private static final int CONT_CURRENT_LIMIT_RIGHT;
//    private static final int PEAK_CURRENT_LIMIT_RIGHT;
//    private static final int PEAK_TIME;
//    public static final double CLIMB_SPEED = 1;
    
//    static {
//       if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
//          TALON_INVERTED = false;
//          VICTOR_INVERTED = false;
//          CONT_CURRENT_LIMIT_LEFT = 40;
//          PEAK_CURRENT_LIMIT_LEFT = 80;
//          CONT_CURRENT_LIMIT_RIGHT = 40; 
//          PEAK_CURRENT_LIMIT_RIGHT = 80;
//          PEAK_TIME = 1000;
//       } else {
//          TALON_INVERTED = false;
//          VICTOR_INVERTED = false;
//          CONT_CURRENT_LIMIT_LEFT = 40; 
//          PEAK_CURRENT_LIMIT_LEFT = 80;
//          CONT_CURRENT_LIMIT_RIGHT = 40; 
//          PEAK_CURRENT_LIMIT_RIGHT = 80;
//          PEAK_TIME = 1000;
//       }
//    }

//    /**
//     * Represents the direction in which the robot should climb. 
//     * UP means to begin climbing onto level 3; DOWN means to perform a reverse climb.
//     */
//     public enum ClimbDirection {
//       UP (1), DOWN(-1);
//       private int sign;
//       private ClimbDirection(int direction) {
//          this.sign = direction;
//       }

//       public int getSign () {
//          return sign;
//       }
//    }
    
//     private Climber() {
//       leftTalon = new HSTalon(CAN_IDs.CLIMBER_TALON_LEFT);
//       rightTalon = new HSTalon(CAN_IDs.CLIMBER_TALON_RIGHT);
//    }
    
//     public void talonInit() {
//       leftTalon.setNeutralMode(NEUTRAL_MODE);
//       rightTalon.setNeutralMode(NEUTRAL_MODE);

//       leftTalon.setInverted(TALON_INVERTED);
//       rightTalon.setInverted(VICTOR_INVERTED);

//       leftTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT_LEFT);
//       rightTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT_RIGHT);
//       leftTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT_LEFT);
//       rightTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT_RIGHT);
//       leftTalon.configPeakCurrentDuration(PEAK_TIME);
//       rightTalon.configPeakCurrentDuration(PEAK_TIME);

//       rightTalon.follow(leftTalon);
//    }

//     public void initDefaultCommand() {
//         setDefaultCommand(new MoveClimberManual(ClimbDirection.UP));
//    }

//    public void setClimberOutput(ClimbDirection direction, double magnitude) {
//       setClimberOutput(direction, magnitude, magnitude);
//    }

//    public void setClimberOutput(ClimbDirection direction, double leftMagnitude, double rightMagnitude) {
//       leftTalon.set(ControlMode.PercentOutput, leftMagnitude * direction.getSign());
//       rightTalon.set(ControlMode.PercentOutput, rightMagnitude * direction.getSign());
//    }

//    public void disableClimber() {
//       leftTalon.set(ControlMode.Disabled, 0);
//       rightTalon.set(ControlMode.Disabled, 0);
//    }

//    public static Climber getInstance() {
//       if(instance == null) {
//          instance = new Climber();
//       }
//       return instance;
//    }
    
//    public HSTalon getLeftTalon() {
//       return leftTalon;
//    }

//    public HSTalon getRightTalon() {
//       return rightTalon;
//    }
// }