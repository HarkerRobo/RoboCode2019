package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;

/**
 * Represents the arm of the robot, 
 * which picks up cargo from the human player station and the ground.
 * 
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @author Angela Jia
 * @since 1/11/19
 */
public class Arm extends Subsystem {
    public static final DoubleSolenoid.Value ARM_UP_VALUE;
    public static final DoubleSolenoid.Value ARM_DOWN_VALUE;

    public static final double DOWN_SAFE_ACTUATION_TIME = 0.32;
    
    static {
        if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
           ARM_UP_VALUE = DoubleSolenoid.Value.kReverse;
           ARM_DOWN_VALUE = DoubleSolenoid.Value.kForward;
        } else {
            ARM_UP_VALUE = DoubleSolenoid.Value.kReverse;
            ARM_DOWN_VALUE = DoubleSolenoid.Value.kForward;
        }
    }
    public enum ArmDirection {        
        UP (ARM_UP_VALUE), DOWN (ARM_DOWN_VALUE);
        private DoubleSolenoid.Value state;
        private ArmDirection(DoubleSolenoid.Value state) {
            this.state = state;
        }     
        public DoubleSolenoid.Value getState() {
            return state;
        }  
    }

    private static Arm instance;

    private static final boolean INITIAL_COMPRESSOR_STATE = true;

    private DoubleSolenoid solenoid;
    private Compressor compressor;
    
    private Arm() {
        solenoid = new DoubleSolenoid(CAN_IDs.ARM_FORWARD_CHANNEL, CAN_IDs.ARM_REVERSE_CHANNEL);
        compressor = new Compressor(CAN_IDs.PCM);
        compressor.setClosedLoopControl(INITIAL_COMPRESSOR_STATE);
    }
    
    public void initDefaultCommand() {
        //setDefaultCommand();
    }

    public DoubleSolenoid.Value getState() {
        return solenoid.get();
    }

    public void setState(DoubleSolenoid.Value value) {
        solenoid.set(value);
    }

    public ArmDirection getDirection() {
        return getState() == ArmDirection.UP.getState() ? ArmDirection.UP : ArmDirection.DOWN;
    }

    public Compressor getCompressor() {
        return compressor;
    }

    public static Arm getInstance() {
        if(instance == null) {
            instance = new Arm();
        }
        return instance;
    }
}