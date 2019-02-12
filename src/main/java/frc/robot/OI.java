package frc.robot;

import frc.robot.commands.drivetrain.AlignWithLimelightDrive;
import frc.robot.commands.drivetrain.ToggleLimelightViewMode;
import frc.robot.util.CustomOperatorGamepad;
import harkerrobolib.wrappers.HSDPadButton;
import harkerrobolib.wrappers.HSGamepad;
import harkerrobolib.wrappers.XboxGamepad;

/**
 * Contains both driver and operator gamepads.
 * 
 * @since 1/7/19
 */
public class OI {
    private HSGamepad driverGamepad;
    private CustomOperatorGamepad customOperatorGamepad;
    private static OI instance;

    private static final int DRIVER_PORT = 0;
    private static final int OPERATOR_PORT = 1;

    public static final double DRIVER_DEADBAND = 0.1;
    public static final double OPERATOR_DEADBAND_JOYSTICK = 0.1;
    public static final double OPERATOR_DEADBAND_TRIGGER = 0.1;

    public static final boolean HAS_TWO_CONTROLLERS = true;

    //DPad angles in degrees
    public static final int DPAD_UP_ANGLE = 0;
    public static final int DPAD_LEFT_ANGLE = 270;
    public static final int DPAD_RIGHT_ANGLE = 90;
    public static final int DPAD_DOWN_ANGLE = 180;

    private OI() {
        driverGamepad = new XboxGamepad(DRIVER_PORT);
        customOperatorGamepad = new CustomOperatorGamepad(OPERATOR_PORT);
        initBindings();
    }
    
    public void initBindings() {
        //driver bumpers
        driverGamepad.getButtonBumperLeft().whileActive(new AlignWithLimelightDrive(198, 0, 4));
        driverGamepad.getButtonBumperRight().whenPressed(new ToggleLimelightViewMode());

        //driver dpad
        HSDPadButton driverUpDPad = new HSDPadButton(driverGamepad, DPAD_UP_ANGLE);
        HSDPadButton driverLeftDPad = new HSDPadButton(driverGamepad, DPAD_LEFT_ANGLE);
        HSDPadButton driverDownDPad = new HSDPadButton(driverGamepad, DPAD_DOWN_ANGLE);
        HSDPadButton driverRightDPad = new HSDPadButton(driverGamepad, DPAD_RIGHT_ANGLE);
    }  

    public HSGamepad getDriverGamepad() {
        return driverGamepad;
    }

    public CustomOperatorGamepad getCustomOperatorGamepad() {
        return customOperatorGamepad;
    }
    
    public static OI getInstance() {
        if(instance == null){
            instance = new OI();
        }
        return instance;
    }
}
