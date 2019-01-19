package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;

/**
 * Controls the Hatch Panel Intake
 * 
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @author Austin Wang
 * @author Anirudh Kotamraju
 * @since 1/14/19
 */
public class HatchPusher extends Subsystem {
    public enum PushDirection {
        IN(DoubleSolenoid.Value.kReverse), OUT(DoubleSolenoid.Value.kForward);
        private DoubleSolenoid.Value value;
        private PushDirection(DoubleSolenoid.Value value) {
            this.value = value;
        }
        public DoubleSolenoid.Value getValue() {
            return value;
        }
    }

    private static HatchPusher instance;
    private DoubleSolenoid doubleSolenoid;
    private Compressor compressor;
    private static boolean COMPRESSOR_INITAL_STATE = true;

    private HatchPusher() {
        compressor = new Compressor(CAN_IDs.PCM);
        doubleSolenoid = new DoubleSolenoid(CAN_IDs.HATCH_FORWARD_CHANNEL, CAN_IDs.HATCH_REVERSE_CHANNEL);
        compressor.setClosedLoopControl(COMPRESSOR_INITAL_STATE);
    }

    @Override
    public void initDefaultCommand() {
    }

    public DoubleSolenoid.Value getSolenoidState() {
        return doubleSolenoid.get();        
    }

    public void setSolenoidState(DoubleSolenoid.Value state){
        doubleSolenoid.set(state);
    }

    public Compressor getCompressor() {
        return compressor;
    }
   
    public static HatchPusher getInstance() {
        if(instance == null){
           instance = new HatchPusher();                                                                                        
        }
        return instance;
    }
    

}