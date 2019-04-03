// package frc.robot.commands.climber;

// import frc.robot.OI;
// import frc.robot.OI.TriggerMode;
// import frc.robot.subsystems.ClimberSuction;
// import harkerrobolib.commands.IndefiniteCommand;
// import harkerrobolib.util.MathUtil;

// /**
//  * Moves climber in a specified direction until it hits a limit switch.
//  * 
//  * @author Finn Frankis
//  * @author Angela Jia
//  * 
//  * @since 3/15/19
//  */
// public class MoveClimberManual extends IndefiniteCommand {

//    public MoveClimberManual(ClimbDirection climberDirection) {
//       requires(ClimberSuction.getInstance());
//    }

//    @Override
//    protected void execute() {

//       double leftTriggerOutput = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND_TRIGGER);
//       double rightTriggerOutput = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightTrigger(), OI.DRIVER_DEADBAND_TRIGGER);
         
//       if (OI.getInstance().getCurrentTriggerMode() == TriggerMode.CLIMB) {
//          ClimberSuction.getInstance().setClimberOutput(leftTriggerOutput, rightTriggerOutput);
//       } else {
//          ClimberSuction.getInstance().setClimberOutput(ClimbDirection.UP, 0);
//       }
//    }
// }