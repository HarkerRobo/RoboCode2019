package frc.robot.subsystems;

import frc.robot.RobotMap.CAN_IDs;
import harkerrobolib.subsystems.HSArm;

import harkerrobolib.wrappers.HSTalon;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Picks up cargo from the human player station and the ground
 * 
 * @author Chirag Kaushik
 * @version January 11, 2019
 */
public class Arm {
    private static Arm instance;

    private DoubleSolenoid solenoid;

    private Arm() {
        solenoid = new DoubleSold(CAN_IDs.ARM_FORWARD_CHANNEL, CAN_IDs.ARM_REVERSE_CHANNEL);
    }
    
    @Override
    public void initDefaultCommand() {
        setDefaultCommand();
    }

    public void setArmPosition(double position) {
        getTalon().set(ControlMode.Position, position);
    }

    public static Arm getInstance() {
        if(instance == null) {
            instance = new Arm();
        }
        return instance;
    }
}