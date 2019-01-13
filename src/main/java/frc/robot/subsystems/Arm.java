package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;

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
    
    public enum ArmDirection {        
        UP (DoubleSolenoid.Value.kForward), DOWN (DoubleSolenoid.Value.kReverse);
        private DoubleSolenoid.Value state;
        private ArmDirection(DoubleSolenoid.Value state)
        {
            this.state = state;
        }     
        public DoubleSolenoid.Value getState() {
            return state;
        }  
    }

    private static Arm instance;

    private DoubleSolenoid solenoid;
    private Compressor compressor;

    private static final boolean INITIAL_COMPRESSOR_STATE = true;
    
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