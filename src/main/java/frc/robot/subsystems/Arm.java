package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.util.Pair;

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
        UP (DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward), DOWN (DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kReverse);
        private DoubleSolenoid.Value leftState;
        private DoubleSolenoid.Value rightState;
        private ArmDirection(DoubleSolenoid.Value leftState, DoubleSolenoid.Value rightState) {
            this.leftState = leftState;
            this.rightState = rightState;
        }    
        public Pair<DoubleSolenoid.Value, DoubleSolenoid.Value> getStates() {
            return new Pair<DoubleSolenoid.Value , DoubleSolenoid.Value >(leftState, rightState);
        }  

        public static ArmDirection convertDirection (Pair<DoubleSolenoid.Value, DoubleSolenoid.Value> states) {
            return states.equals(UP.getStates()) ? UP : DOWN;
        }
    }

    private static Arm instance;

    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    
    private Arm() {
        leftSolenoid = new DoubleSolenoid(CAN_IDs.ARM_LEFT_FORWARD_CHANNEL, CAN_IDs.ARM_LEFT_REVERSE_CHANNEL);
        rightSolenoid = new DoubleSolenoid(CAN_IDs.ARM_RIGHT_FORWARD_CHANNEL, CAN_IDs.ARM_RIGHT_REVERSE_CHANNEL);
    }
    
    public void initDefaultCommand() {
        //setDefaultCommand();
    }

    public ArmDirection getStates() {
        return ArmDirection.convertDirection(new Pair<DoubleSolenoid.Value, DoubleSolenoid.Value> (leftSolenoid.get(), rightSolenoid.get()));
    }

    public void setState(ArmDirection direction) {
        leftSolenoid.set(direction.getStates().getFirst());
        rightSolenoid.set(direction.getStates().getSecond());
    }

    public static Arm getInstance() {
        if(instance == null) {
            instance = new Arm();
        }
        return instance;
    }
}