package frc.robot;

import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.drivetrain.AlignWithLimelightDrive;
import frc.robot.commands.drivetrain.ToggleLimelightDriverMode;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.elevator.MoveElevatorPosition;
import frc.robot.commands.elevator.ZeroElevator;
import frc.robot.commands.groups.SetScoringPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.groups.SpinIntakeAndRollers;
import frc.robot.commands.rollers.SpinRollersIndefinite;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import frc.robot.subsystems.Wrist;
import frc.robot.util.CustomOperatorGamepad;
import frc.robot.util.Pair;
import harkerrobolib.auto.ParallelCommandGroup;
import harkerrobolib.util.MathUtil;
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
        operatorGamepad = new LogitechAnalogGamepad(OPERATOR_PORT);
        customOperatorGamepad = new CustomOperatorGamepad(OPERATOR_PORT);
        initBindings();
    }
    
    public void initBindings() {
        //driver bumpers
        //driverGamepad.getButtonBumperLeft().whenPressed(new SetArmPosition(ArmDirection.DOWN));
        //driverGamepad.getButtonBumperRight().whenPressed(new SetArmPosition(ArmDirection.UP));
        driverGamepad.getButtonBumperLeft().whileActive(new AlignWithLimelightDrive(198, 0, 4));
        driverGamepad.getButtonBumperRight().whenPressed(new ToggleLimelightDriverMode());

        //driver buttons
        driverGamepad.getButtonA().whenPressed(new ParallelCommandGroup(
            new MoveElevatorPosition(Elevator.INTAKE_POSITION),
            new MoveWristPosition(Wrist.ANGLE_INTAKE)
        ));

        driverGamepad.getButtonA().whilePressed(new SpinRollersIndefinite(Rollers.DEFAULT_ROLLER_MAGNITUDE, RollerDirection.IN));
                 
        driverGamepad.getButtonX().whenPressed(new ParallelCommandGroup(
            new MoveElevatorMotionMagic(Elevator.LOW_SCORING_POSITION),
            new MoveWristPosition(Wrist.ANGLE_SCORING_FRONT)
        ));

        driverGamepad.getButtonY().whenPressed(new ParallelCommandGroup(
            new MoveElevatorPosition(Elevator.MEDIUM_SCORING_POSITION),
            new MoveWristPosition(Wrist.ANGLE_SCORING_BACK)            
        ));

        driverGamepad.getButtonB().whenPressed(new SetArmPosition(ArmDirection.DOWN));
        driverGamepad.getButtonB().whilePressed(new SpinIntakeAndRollers());

        //driver dpad
        HSDPadButton driverUpDPad = new HSDPadButton(driverGamepad, DPAD_UP_ANGLE);
        HSDPadButton driverLeftDPad = new HSDPadButton(driverGamepad, DPAD_LEFT_ANGLE);
        HSDPadButton driverDownDPad = new HSDPadButton(driverGamepad, DPAD_DOWN_ANGLE);
        HSDPadButton driverRightDPad = new HSDPadButton(driverGamepad, DPAD_RIGHT_ANGLE);

        // driverUpDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.ExtenderDirection.OUT));
        // driverDownDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.ExtenderDirection.IN));

        //operator bumpers                 
        operatorGamepad.getButtonBumperRight().whenPressed(new SetArmPosition(ArmDirection.UP));
        operatorGamepad.getButtonBumperLeft().whenPressed(new SetArmPosition(ArmDirection.DOWN));
             //done           
        //operator dpad
        HSDPadButton operatorUpDPad = new HSDPadButton(operatorGamepad, DPAD_UP_ANGLE);
        HSDPadButton operatorLeftDPad = new HSDPadButton(operatorGamepad, DPAD_LEFT_ANGLE);
        HSDPadButton operatorDownDPad = new HSDPadButton(operatorGamepad, DPAD_DOWN_ANGLE);
        HSDPadButton operatorRightDPad = new HSDPadButton(operatorGamepad, DPAD_RIGHT_ANGLE);
        //done

        //operator dpad
        // operatorUpDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.ExtenderDirection.OUT));
        // operatorDownDPad.whenPressed(new SetHatchPusherDirection(HatchPusher.ExtenderDirection.IN));
        
        //operator buttons
        
        operatorGamepad.getButtonA().whilePressed(new SpinRollersIndefinite(SpinRollersIndefinite.magnitude1,RollerDirection.OUT));
        operatorGamepad.getButtonY().whilePressed(new SpinRollersIndefinite(SpinRollersIndefinite.magnitude2,RollerDirection.OUT));
    
        driverGamepad.getButtonStart().whenPressed(new ZeroElevator());

        customOperatorGamepad.getBottomLeftButton().whenPressed(new SetScoringPosition(Location.B1));
        customOperatorGamepad.getMiddleLeftButton().whenPressed(new SetScoringPosition(Location.B2));   
        customOperatorGamepad.getTopLeftButton().whenPressed(new SetScoringPosition(Location.B3));  

        customOperatorGamepad.getBottomRightButton().whenPressed(new SetScoringPosition(Location.F1));                              
        customOperatorGamepad.getMiddleRightButton().whenPressed(new SetScoringPosition(Location.F2));
        customOperatorGamepad.getTopRightButton().whenPressed(new SetScoringPosition(Location.F3));    
    }  

    public HSGamepad getDriverGamepad() {
        return driverGamepad;
    }

    public HSGamepad getOperatorGamepad() {
        return operatorGamepad;
    }

    public Pair<Double, Double> getDriverLeftJoystick() {
        double leftDriverX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
        double leftDriverY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);
        return new Pair<Double, Double>(leftDriverX, leftDriverY);
    }
    
    public static OI getInstance() {
        if(instance == null){
            instance = new OI();
        }
        return instance;
    }
}
