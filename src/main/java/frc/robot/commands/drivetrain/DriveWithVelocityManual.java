package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotMap.Global;
import frc.robot.RobotMap.RobotType;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.MathUtil;
import harkerrobolib.util.Conversions.SpeedUnit;

/**
 * Drives with velocity manually.
 *
 * @author Chirag Kaushik
 * @author Angela Jia
 * @since February 2, 2019
 */
public class DriveWithVelocityManual extends IndefiniteCommand {
    public static final double
        LEFT_KF = 0.197,
		LEFT_KP = 0.8,
		LEFT_KI = 0.002,
		LEFT_KD = 0.5,

		RIGHT_KF = 0.197,
		RIGHT_KP = 0.8,
		RIGHT_KI = 0.002,
        RIGHT_KD = 0.5;

    private static final int I_ZONE = 250;

    public DriveWithVelocityManual() {
        requires(Drivetrain.getInstance());
    }

    public void initialize() {
        Drivetrain.getInstance().getLeftMaster().config_kP(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KP);
        Drivetrain.getInstance().getLeftMaster().config_kI(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KI);
        Drivetrain.getInstance().getLeftMaster().config_kD(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KD);
        Drivetrain.getInstance().getLeftMaster().config_kF(Drivetrain.VELOCITY_SLOT_INDEX, LEFT_KF);

        Drivetrain.getInstance().getRightMaster().config_kP(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KP);
        Drivetrain.getInstance().getRightMaster().config_kI(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KI);
        Drivetrain.getInstance().getRightMaster().config_kD(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KD);
        Drivetrain.getInstance().getRightMaster().config_kF(Drivetrain.VELOCITY_SLOT_INDEX, RIGHT_KF);

        Drivetrain.getInstance().getLeftMaster().config_IntegralZone(Drivetrain.VELOCITY_SLOT_INDEX, I_ZONE);
        Drivetrain.getInstance().getRightMaster().config_IntegralZone(Drivetrain.VELOCITY_SLOT_INDEX, I_ZONE);

        Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().configBothFeedbackSensors(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
        Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

    }

    public void execute() {
        double leftX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
        double leftY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);
        Drivetrain.getInstance().arcadeDriveVelocity(leftY, leftX);

        SmartDashboard.putNumber("left error", Drivetrain.getInstance().getLeftMaster().getClosedLoopError(Global.PID_PRIMARY));
        SmartDashboard.putNumber("right error", Drivetrain.getInstance().getRightMaster().getClosedLoopError(Global.PID_PRIMARY));
        // if(OI.getInstance().getDriverGamepad().getLeftY() > 0.5)
        //     Drivetrain.getInstance().arcadeDriveVelocity(0.8, OI.getInstance().getDriverGamepad().getLeftX());
        // else if(OI.getInstance().getDriverGamepad().getLeftY() < -0.5)
        //     Drivetrain.getInstance().arcadeDriveVelocity(-0.8, OI.getInstance().getDriverGamepad().getLeftX());
        // else
        //     Drivetrain.getInstance().arcadeDriveVelocity(0, OI.getInstance().getDriverGamepad().getLeftX());
        //Drivetrain.getInstance().getLeftMaster().set(ControlMode.Velocity, )
    }

    public void end () {
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);
    }
}