package frc.robot;

import frc.robot.commands.SetArmPosition;
import frc.robot.subsystems.Arm.ArmDirection;
import harkerrobolib.wrappers.HSGamepad;
import harkerrobolib.wrappers.LogitechAnalogGamepad;
import harkerrobolib.wrappers.XboxGamepad;

/**
 * Holds both driver and operator gamepads.
 * 
 * @since 1/7/19
 */
public class OI {
    private HSGamepad driverGamepad;
    private HSGamepad operatorGamepad;
    private static OI instance;

    private static final int DRIVER_PORT = 0;
    private static final int OPERATOR_PORT = 1;

    public static final double DRIVER_DEADBAND = 0.1;
    public static final double OPERATOR_DEADBAND = 0.1;

    public static final boolean HAS_TWO_CONTROLLERS = true;
    private OI() {
        driverGamepad = new XboxGamepad(DRIVER_PORT);
        operatorGamepad = new LogitechAnalogGamepad(OPERATOR_PORT);
    }
    
    public void initBindings() {
        driverGamepad.getButtonBumperLeft().whenPressed(new SetArmPosition(ArmDirection.UP));
        driverGamepad.getButtonBumperRight().whenPressed(new SetArmPosition(ArmDirection.DOWN));
    }

    public HSGamepad getDriverGamepad() {
        return driverGamepad;
    }

    public HSGamepad getOperatorGamepad() {
        return operatorGamepad;
    }
    
    public static OI getInstance() {
        if(instance == null){
            instance = new OI();
        }
        return instance;
    }
}