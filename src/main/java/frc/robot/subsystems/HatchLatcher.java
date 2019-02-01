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
 * @author Shahzeb Lakhani
 * @author Angela Jia
 * @author Aimee Wang
 * @since 1/14/19
 */
public class HatchLatcher extends Subsystem {

    public enum ExtenderDirection {
        IN(DoubleSolenoid.Value.kReverse), OUT(DoubleSolenoid.Value.kForward);
        private DoubleSolenoid.Value value;

        private ExtenderDirection(DoubleSolenoid.Value value) {
            this.value = value;
        }

        public DoubleSolenoid.Value getValue() {
            return value;
        }

        public static ExtenderDirection convertDirection(DoubleSolenoid.Value value) {
            return value == IN.getValue() ? IN : OUT;
        }
    }

    public enum FlowerDirection {
        OPEN(DoubleSolenoid.Value.kReverse), CLOSED(DoubleSolenoid.Value.kForward);
        private DoubleSolenoid.Value value;

        private FlowerDirection(DoubleSolenoid.Value value) {
            this.value = value;
        }

        public DoubleSolenoid.Value getValue() {
            return value;
        }
        public static FlowerDirection convertDirection(DoubleSolenoid.Value value) {
            return value == OPEN.getValue() ? OPEN : CLOSED;
        }
    }

    private static HatchLatcher instance;
    private DoubleSolenoid extender;
    private DoubleSolenoid flower;
    private Compressor compressor;
    private static boolean COMPRESSOR_INITAL_STATE = true;

    private HatchLatcher() {
        compressor = new Compressor(CAN_IDs.PCM);
        extender = new DoubleSolenoid(CAN_IDs.EXTENDER_FORWARD_CHANNEL, CAN_IDs.EXTENDER_REVERSE_CHANNEL);
        flower = new DoubleSolenoid(CAN_IDs.FLOWER_FORWARD_CHANNEL, CAN_IDs.FLOWER_REVERSE_CHANNEL);
        compressor.setClosedLoopControl(COMPRESSOR_INITAL_STATE);
    }

    @Override
    public void initDefaultCommand() {
    }

    public DoubleSolenoid.Value getExtenderState() {
        return extender.get();
    }

    public void setExtenderState(ExtenderDirection state) {
        extender.set(state.getValue());
    }

    public DoubleSolenoid.Value getFlowerState() {
        return flower.get();
    }

    public void setFlowerState(FlowerDirection flowerDirection) {
        flower.set(flowerDirection.getValue());
    }

    public Compressor getCompressor() {
        return compressor;
    }

    public static HatchLatcher getInstance() {
        if (instance == null) {
            instance = new HatchLatcher();
        }
        return instance;
    }

}