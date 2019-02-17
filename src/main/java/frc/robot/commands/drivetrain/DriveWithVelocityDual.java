// package frc.robot.commands.drivetrain;

// import com.ctre.phoenix.motorcontrol.ControlMode;

// import edu.wpi.first.wpilibj.PIDController;
// import edu.wpi.first.wpilibj.PIDSourceType;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.OI;
// import frc.robot.Robot;
// import frc.robot.subsystems.Drivetrain;
// import frc.robot.util.Limelight;
// import frc.robot.util.PIDOutputGetter;
// import frc.robot.util.PIDSourceCustomGet;
// import harkerrobolib.commands.IndefiniteCommand;
// import harkerrobolib.util.MathUtil;

// /**
//  * Represents a driver-assisted mode where the driver has full forward-backward control
//  * and turn is assisted by the limelight tx loop.
//  * 
//  * @author Finn Frankis
//  * @author Angela Jia
//  * 
//  * @since 1/31/19
//  */
// public class DriveWithVelocityDual extends IndefiniteCommand {

//     private Limelight limelight;

//     // public static final double TURN_KP, TURN_KI, TURN_KD, TURN_KF;
//     // public static final double FORWARD_KF, FORWARD,KP, FORWARD_KI, FORWARD_KD;

//     // static {
//     //     if()
//     // }

//     // public static final double TURN_KP = .07; //0.09
//     // public static final double TURN_KI = 0.001;
//     // public static final double TURN_KD = 0.3;
//     // public static final double TURN_KF = 0;
    
//     // private static final double FORWARD_KF = 0;
//     // private static final double FORWARD_KP = 0.045;
//     // private static final double FORWARD_KI = 0;//0.001;
//     // private static final double FOWARD_KD = 0.2;   

//     // public static final double TURN_ALLOWABLE_ERROR = 0.054;
//     // public static final double FORWARD_ALLOWABLE_ERROR = 0.05;

//     // private static final double POS_TX_SETPOINT = 1.0; 
//     // private static final double MAX_ALLOWABLE_TX = 7.5;   

//     // private static final double THOR_SWITCH_POINT = 50.0;

//     // private PIDOutputGetter turnOutput;
//     // private PIDOutputGetter forwardOutput;

//     // private PIDController turnController;
//     // private PIDController forwardController;

//     // private double thorSetpoint;
//     // private double txSetpoint;

//     // private static boolean LEFT_MASTER_INVERTED = false;
//     // private static boolean RIGHT_MASTER_INVERTED = true;
//     // private static boolean LEFT_FOLLOWER_INVERTED = false;
//     // private static boolean RIGHT_FOLLOWER_INVERTED = true;

//     // public DriveWithVelocityDual(double thorSetpoint, double txSetpoint, double angleSetpoint) {
//     //     requires(Drivetrain.getInstance());

//     //     this.txSetpoint = txSetpoint;
//     //     this.thorSetpoint = Limelight.THOR_LINEARIZATION_FUNCTION.apply(thorSetpoint);

//     //     limelight = Limelight.getInstance();

//     //     turnOutput = new PIDOutputGetter();
//     //     forwardOutput = new PIDOutputGetter();
//     // }

//     // /**
//     //  * {@inheritDoc}
//     //  */
//     // @Override
//     // public void initialize() {

//     //     turnController = new PIDController(
//     //         TURN_KP, TURN_KI, TURN_KD, TURN_KF, 
//     //         new PIDSourceCustomGet(() -> limelight.getTx(), PIDSourceType.kDisplacement), 
//     //         turnOutput
//     //     );
        
//     //     forwardController = new PIDController(
//     //         FORWARD_KP, FORWARD_KI, FOWARD_KD, FORWARD_KF, 
//     //         new PIDSourceCustomGet(() -> limelight.getThor(), Limelight.THOR_LINEARIZATION_FUNCTION, 
//     //                                 PIDSourceType.kDisplacement),
//     //         forwardOutput
//     //     );
     
//     // //    targetHeading = (int)Drivetrain.getInstance().getPigeon().getFusedHeading() % 360;
//     // //    if (targetHeading < 0 ) targetHeading += 360;
//     // //    targetHeading = Math.round(targetHeading / 90.0) * 90;
//     // //    isRobotRightOfTarget = Drivetrain.getInstance().getPigeon().getFusedHeading() > targetHeading;

//     //     turnController.enable();
//     //     forwardController.enable();
    
//     //     turnController.setSetpoint(txSetpoint);
//     //     forwardController.setSetpoint(thorSetpoint);

//     //     Drivetrain.getInstance().invertTalons(LEFT_MASTER_INVERTED, RIGHT_MASTER_INVERTED, LEFT_FOLLOWER_INVERTED, RIGHT_FOLLOWER_INVERTED);
//     // }

//     // /**
//     //  * {@inheritDoc}
//     //  */
//     // public void execute () {
//     //     double leftDriverX;
//     //     double turnOutputVal = turnOutput.getOutput();
//     //     System.out.println("Before getting"+Robot.getTime());
//     //     boolean shouldAlign = OI.getInstance().getDriverGamepad().getButtonBumperLeftState();
//     //     System.out.println("After getting" + Robot.getTime());
//     //     SmartDashboard.putNumber("Output", Drivetrain.getInstance().getLeftMaster().getMotorOutputPercent());
//     //     SmartDashboard.putNumber("Output Val", turnOutputVal);

//     //     if(shouldAlign){
//     //         leftDriverX= turnOutputVal;
//     //     }else{
//     //         leftDriverX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
//     //     }

       
//     //      double leftDriverY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);

//     //     Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, leftDriverY + leftDriverX /*turnOutputVal*/);
//     //     Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, leftDriverY - leftDriverX /*turnOutputVal*/);
//     // }

//     // /**
//     //  * {@inheritDoc}
//     //  */
//     // @Override
//     // public void end() {
//     //     Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
//     //     turnController.disable();
//     //     forwardController.disable();
//     //     Drivetrain.getInstance().resetTalonInverts();
//     // }

//     // /**
//     //  * {@inheritDoc}
//     //  */
//     // @Override
//     // public void interrupted() {
//     //     System.out.println("Interrupted");
//     //     end();
//     // }
// }