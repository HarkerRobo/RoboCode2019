package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.commands.drivetrain.AlignWithLimelightDrive;
import frc.robot.commands.drivetrain.ToggleLimelightViewMode;
import frc.robot.commands.groups.OuttakeBallOrHatch;
import frc.robot.commands.groups.SetScoringPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.commands.groups.StowHatchAndCargoIntake;
import frc.robot.util.CustomOperatorGamepad;
import frc.robot.util.ToggleSolenoid;
import harkerrobolib.wrappers.HSGamepad;
import harkerrobolib.wrappers.XboxGamepad;

/**
 * Class that manages input and output from gamepads.
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
        driverGamepad.getButtonY().whilePressed(new MoveWristPosition(1000));
        // driverGamepad.getButtonBumperLeft().whileActive(new AlignWithLimelightDrive(198, 0, 4));
        // driverGamepad.getButtonBumperRight().whenPressed(new ToggleLimelightViewMode());
        driverGamepad.getUpDPadButton().whenPressed(new ToggleSolenoid(new DoubleSolenoid(0, 2)));
        driverGamepad.getDownDPadButton().whenPressed(new ToggleSolenoid(new DoubleSolenoid(1, 3)));
        driverGamepad.getRightDPadButton().whenPressed(new ToggleSolenoid(new DoubleSolenoid(4, 5)));
        driverGamepad.getLeftDPadButton().whenPressed(new ToggleSolenoid(new DoubleSolenoid(6,7)));
        // customOperatorGamepad.getForwardOneButton().whenPressed(new SetScoringPosition(Location.F1));
        // customOperatorGamepad.getForwardTwoButton().whenPressed(new SetScoringPosition(Location.F2));
        // customOperatorGamepad.getForwardThreeButton().whenPressed(new SetScoringPosition(Location.F3));
        // customOperatorGamepad.getBackwardOneButton().whenPressed(new SetScoringPosition(Location.B1));
        // customOperatorGamepad.getBackwardTwoButton().whenPressed(new SetScoringPosition(Location.B2));
        // customOperatorGamepad.getBackwardThreeButton().whenPressed(new SetScoringPosition(Location.B3));

        // customOperatorGamepad.getZeroButton().whenPressed(new SetScoringPosition(Location.ZERO));
        // customOperatorGamepad.getOuttakeButton().whilePressed(new OuttakeBallOrHatch());
        // customOperatorGamepad.getIntakeHatchButton().whenPressed(new SetScoringPosition(Location.HATCH_INTAKE));
        // customOperatorGamepad.getStowButton().whenPressed(new StowHatchAndCargoIntake());
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
