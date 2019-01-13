package frc.robot;

import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.intake.SpinIntakeIndefinite;
import frc.robot.commands.rollers.SpinRollersIndefinite;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Intake.IntakeDirection;
import frc.robot.subsystems.Rollers.RollerDirection;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.ParallelCommandGroup;
import harkerrobolib.wrappers.HSDPadButton;
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

        initBindings();
    }
    
    public void initBindings() {
        driverGamepad.getButtonBumperLeft().whenPressed(new SetArmPosition(ArmDirection.UP));
        driverGamepad.getButtonBumperRight().whenPressed(new SetArmPosition(ArmDirection.DOWN));
        driverGamepad.getButtonX().whenPressed(new MoveWristPosition(Wrist.MAX_BACKWARD_POSITION));
        driverGamepad.getButtonB().whenPressed(new MoveWristPosition(Wrist.MAX_FORWARD_POSITION));
        
        operatorGamepad.getButtonA().whilePressed(new SpinRollersIndefinite(1,RollerDirection.IN));
        operatorGamepad.getButtonY().whilePressed(new SpinRollersIndefinite(1,RollerDirection.OUT));

        HSDPadButton upButton = new HSDPadButton(operatorGamepad,0);
        HSDPadButton leftButton = new HSDPadButton(operatorGamepad,270);
        HSDPadButton downButton = new HSDPadButton(operatorGamepad,180);
        HSDPadButton rightButton = new HSDPadButton(operatorGamepad,90);
        upButton.whenPressed(new SetArmPosition(ArmDirection.UP));
        downButton.whenPressed(new SetArmPosition(ArmDirection.DOWN));

        driverGamepad.getButtonA().whilePressed(new ParallelCommandGroup(
            new SpinRollersIndefinite(1.0, RollerDirection.IN),
            new SpinIntakeIndefinite(1.0, IntakeDirection.IN)));
        driverGamepad.getButtonA().whenReleased(new MoveWristPosition(Wrist.MAX_FORWARD_POSITION));

        driverGamepad.getButtonX().whenPressed(new 

        )
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