package frc.robot;

import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.elevator.MoveElevatorPosition;
import frc.robot.commands.hatchpanelintake.SetHatchPusherDirection;
import frc.robot.commands.intake.SpinIntakeIndefinite;
import frc.robot.commands.rollers.SpinRollersIndefinite;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchPusher;
import frc.robot.subsystems.Intake.IntakeDirection;
import frc.robot.subsystems.Rollers.RollerDirection;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.ParallelCommandGroup;
import harkerrobolib.wrappers.HSDPadButton;
import harkerrobolib.wrappers.HSGamepad;
import harkerrobolib.wrappers.LogitechAnalogGamepad;
import harkerrobolib.wrappers.XboxGamepad;


/**
 * Contains both driver and operator gamepads.
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

    //DPad angles in degrees
    public static final int DPAD_UP_ANGLE = 0;
    public static final int DPAD_LEFT_ANGLE = 270;
    public static final int DPAD_RIGHT_ANGLE = 90;
    public static final int DPAD_DOWN_ANGLE = 180;

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

        HSDPadButton driverUpDPad = new HSDPadButton(operatorGamepad, DPAD_UP_ANGLE);
        HSDPadButton driverLeftDPad = new HSDPadButton(operatorGamepad, DPAD_LEFT_ANGLE);
        HSDPadButton driverDownDPad = new HSDPadButton(operatorGamepad, DPAD_DOWN_ANGLE);
        HSDPadButton driverRightDPad = new HSDPadButton(operatorGamepad, DPAD_RIGHT_ANGLE);

        HSDPadButton operatorUpDPad = new HSDPadButton(operatorGamepad, DPAD_UP_ANGLE);
        HSDPadButton operatorLeftDPad = new HSDPadButton(operatorGamepad, DPAD_LEFT_ANGLE);
        HSDPadButton operatorDownDPad = new HSDPadButton(operatorGamepad, DPAD_DOWN_ANGLE);
        HSDPadButton operatorRightDPad = new HSDPadButton(operatorGamepad, DPAD_RIGHT_ANGLE);

        operatorUpDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.PushDirection.OUT));
        operatorDownDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.PushDirection.IN));
        
        driverUpDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.PushDirection.OUT));
        driverDownDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.PushDirection.IN));
        
        driverGamepad.getButtonA().whilePressed(new ParallelCommandGroup(
            new SpinRollersIndefinite(1.0, RollerDirection.IN),
            new SpinIntakeIndefinite(1.0, IntakeDirection.IN)));

        driverGamepad.getButtonA().whenReleased(new MoveWristPosition(Wrist.MAX_FORWARD_POSITION));

        driverGamepad.getButtonX().whenPressed(new ParallelCommandGroup(
            new MoveElevatorMotionMagic(Elevator.LOW_SCORING_POSITION),
            new MoveWristPosition(Wrist.WRIST_ANGLE_SCORING)
        ));

        driverGamepad.getButtonY().whenPressed(new ParallelCommandGroup(
            new MoveElevatorPosition(Elevator.HIGH_SCORING_POSITION),
            new MoveWristPosition(Wrist.WRIST_ANGLE_SCORING)            
        ));

        driverGamepad.getButtonB().whenPressed(new ParallelCommandGroup(
            new MoveElevatorPosition(Elevator.MEDIUM_SCORING_POSITION), 
            new MoveWristPosition(Wrist.WRIST_ANGLE_SCORING)                    
        )); 

        driverGamepad.getButtonA().whenPressed(new ParallelCommandGroup(
            new MoveElevatorPosition(Elevator.INTAKE_POSITION),
            new MoveWristPosition(Wrist.WRIST_ANGLE_INTAKE)
        ));        
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
