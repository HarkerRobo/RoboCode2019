package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotMap.Global;
import frc.robot.RobotMap.RobotType;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Drives with velocity manually.
 * 
 * @author Chirag Kaushik
 * @author Angela Jia
 * @since February 2, 2019
 */
public class DriveWithVelocityManual extends IndefiniteCommand {
    public static final double LEFT_KP;
    public static final double LEFT_KI;
    public static final double LEFT_KD;
    public static final double LEFT_KF;

    public static final double RIGHT_KP;
    public static final double RIGHT_KI;
    public static final double RIGHT_KD;
    public static final double RIGHT_KF;

    static {
        if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
            LEFT_KP = 0.4;
            LEFT_KI = 0.0;
            LEFT_KD = 0.0;
            LEFT_KF = 0.18;
            
            RIGHT_KP = 0.4;
            RIGHT_KI = 0.0;
            RIGHT_KD = 0.0;
            RIGHT_KF = 0.18;
        } else {
            LEFT_KP = 0.4;
            LEFT_KI = 0.0;
            LEFT_KD = 0.0;
            LEFT_KF = 0.18;
            
            RIGHT_KP = 0.4;
            RIGHT_KI = 0.0;
            RIGHT_KD = 0.0;
            RIGHT_KF = 0.18; 
        }
    }

    public DriveWithVelocityManual() {
        requires(Drivetrain.getInstance());
        Robot.log("DriveWithVelocityManual constructed.");
    }

    public void initialize() {
        // Drivetrain.getInstance().getLeftMaster().config_kP(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KP);
        // Drivetrain.getInstance().getLeftMaster().config_kI(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KI);
        // Drivetrain.getInstance().getLeftMaster().config_kD(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KD);
        // Drivetrain.getInstance().getLeftMaster().config_kF(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KF);

        // Drivetrain.getInstance().getRightMaster().config_kP(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KP);
        // Drivetrain.getInstance().getRightMaster().config_kI(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KI);
        // Drivetrain.getInstance().getRightMaster().config_kD(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KD);
        // Drivetrain.getInstance().getRightMaster().config_kF(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KF);

        // Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        // Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        // Drivetrain.getInstance().configBothFeedbackSensors(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        // Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
        // Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

    }

    public void execute() {
        System.out.println(OI.getInstance().getDriveStraightMode());
        double leftX = OI.getInstance().getDriveStraightMode() ? 0 : MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
        double leftY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);
        // Drivetrain.getInstance().arcadeDrivePercentOutput(leftY, Math.pow(leftX, 2) * Math.signum(leftX));
        if(Drivetrain.getInstance().getRightMaster().getSensorCollection().getPulseWidthPosition() == Global.DISCONNECTED_PULSE_WIDTH_POSITION ||
            Drivetrain.getInstance().getLeftMaster().getSensorCollection().getPulseWidthPosition() == Global.DISCONNECTED_PULSE_WIDTH_POSITION) {
                Drivetrain.getInstance().arcadeDrivePercentOutput(leftY, Math.pow(leftX, 2) * Math.signum(leftX));
            } else {
                    Drivetrain.getInstance().arcadeDriveVelocity(leftY, Math.pow(leftX, 2) * Math.signum(leftX));
        }
        
        // if(OI.getInstance().getDriverGamepad().getLeftY() > 0.5)
        //     Drivetrain.getInstance().arcadeDriveVelocity(0.8, OI.getInstance().getDriverGamepad().getLeftX());
        // else if(OI.getInstance().getDriverGamepad().getLeftY() < -0.5)
        //     Drivetrain.getInstance().arcadeDriveVelocity(-0.8, OI.getInstance().getDriverGamepad().getLeftX());
        // else
        //     Drivetrain.getInstance().arcadeDriveVelocity(0, OI.getInstance().getDriverGamepad().getLeftX());
        SmartDashboard.putNumber("Left reading", Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition());
        SmartDashboard.putNumber("Right reading", Drivetrain.getInstance().getRightMaster().getSelectedSensorPosition());
        SmartDashboard.putNumber("Error", Drivetrain.getInstance().getLeftMaster().getClosedLoopError(0));
        //Drivetrain.getInstance().getLeftMaster().set(ControlMode.Velocity, )
        SmartDashboard.putNumber("sensor health", Drivetrain.getInstance().getRightMaster().getSensorCollection().getPulseWidthPosition());

    }

    public void end () {
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);
    }
}