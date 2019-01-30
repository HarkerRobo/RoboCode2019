// package frc.robot.auto.automode;

// import edu.wpi.first.wpilibj.command.Command;
// import frc.robot.commands.drivetrain.AlignWithLimelight;
// import frc.robot.commands.drivetrain.DriveToPosition;
// import frc.robot.commands.drivetrain.DriveWithVelocityTimed;
// import frc.robot.commands.drivetrain.TurnToAngle;
// import frc.robot.commands.hatchpanelintake.SetHatchPusherDirection;
// import frc.robot.subsystems.HatchPusher;
// import harkerrobolib.auto.AutoMode;
// import harkerrobolib.auto.SequentialCommandGroup;

// /**
//  * 
//  */
// public class ReachTarget extends AutoMode {
    
//     public ReachTarget(Location startLoc, Location endLoc) {
//         super(startLoc, endLoc);
//     }
//     private static final double FLYING_TIME = 2.0;
//     private static final double FLYING_VELOCITY= 4.20;
//     private static final double CAMERA_HORIZONTAL_DISTANCE_FROM_TARGET = -1;
//     private static final double CAMERA_X_DISTANCE_FROM_TARGET = 0.0;
//     private static final double PIGEON_ANGLE_FROM_TARGET = 0;
//     private static final double BACK_UP_DISTANCE = -2.0;

//     private static final double PIGEON_HEADING_ONEHATCH = 0;

//     @Override
//     public Command getLeftCommands(Location endLoc){
//         return new SequentialCommandGroup(
//             new DriveWithVelocityTimed(FLYING_TIME, FLYING_VELOCITY),
//             new AlignWithLimelight(CAMERA_HORIZONTAL_DISTANCE_FROM_TARGET, CAMERA_X_DISTANCE_FROM_TARGET, PIGEON_ANGLE_FROM_TARGET), 
//             new SetHatchPusherDirection(HatchPusher.ExtenderDirection.OUT),
//             new  DriveToPosition(BACK_UP_DISTANCE)
//         );                              
//     }    
    
//     @Override
//     public Command getRightCommands(Location endLoc){
//         return new SequentialCommandGroup(
//             new DriveWithVelocityTimed(FLYING_TIME, FLYING_VELOCITY),
//             new AlignWithLimelight(CAMERA_HORIZONTAL_DISTANCE_FROM_TARGET, CAMERA_X_DISTANCE_FROM_TARGET, PIGEON_ANGLE_FROM_TARGET),
//             new SetHatchPusherDirection(HatchPusher.ExtenderDirection.OUT),
//             new  DriveToPosition(BACK_UP_DISTANCE)
//         );                              
//     } 
    
//     private SequentialCommandGroup oneHatchAuto = new SequentialCommandGroup(
//         new DriveWithVelocityTimed(FLYING_TIME, FLYING_VELOCITY),
//         new TurnToAngle(PIGEON_HEADING_ONEHATCH),
//         new AlignWithLimelight(CAMERA_HORIZONTAL_DISTANCE_FROM_TARGET, CAMERA_X_DISTANCE_FROM_TARGET,
//                     PIGEON_ANGLE_FROM_TARGET)
//     );
// }