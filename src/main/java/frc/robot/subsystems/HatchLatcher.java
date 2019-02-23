package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;

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
    public static final DoubleSolenoid.Value EXTENDER_IN_VALUE;
    public static final DoubleSolenoid.Value EXTENDER_OUT_VALUE;

    public static final DoubleSolenoid.Value FLOWER_OPEN_VALUE;
    public static final DoubleSolenoid.Value FLOWER_CLOSED_VALUE;

    static {
        if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
            EXTENDER_IN_VALUE = DoubleSolenoid.Value.kForward;
            EXTENDER_OUT_VALUE = DoubleSolenoid.Value.kReverse;

            
            FLOWER_OPEN_VALUE = DoubleSolenoid.Value.kReverse;
            FLOWER_CLOSED_VALUE = DoubleSolenoid.Value.kForward;
        } else {
            EXTENDER_IN_VALUE = DoubleSolenoid.Value.kForward;
            EXTENDER_OUT_VALUE = DoubleSolenoid.Value.kReverse;

            FLOWER_OPEN_VALUE = DoubleSolenoid.Value.kReverse;
            FLOWER_CLOSED_VALUE = DoubleSolenoid.Value.kForward;
        }
    }
    
    public enum ExtenderDirection {
        IN(EXTENDER_IN_VALUE), OUT(EXTENDER_OUT_VALUE);
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
        OPEN(FLOWER_OPEN_VALUE), CLOSED(FLOWER_CLOSED_VALUE);
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
    public static final double EXTENDER_EXTEND_TIME = 1.0;
    public static final double FLOWER_ACTUATION_TIME = 0.5;

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

    public ExtenderDirection getExtenderState() {
        return ExtenderDirection.convertDirection(extender.get());
    }

    public void setExtenderState(ExtenderDirection state) {
        extender.set(state.getValue());
    }

    public FlowerDirection getFlowerState() {
        return FlowerDirection.convertDirection(flower.get());
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

    public boolean hasHatch () {
        return flower.get() == FlowerDirection.OPEN.getValue();
    }
}