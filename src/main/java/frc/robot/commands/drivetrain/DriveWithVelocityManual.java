package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap.Global;
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
    private static final double LEFT_KP = 0.4;
    private static final double LEFT_KI = 0.0;
    private static final double LEFT_KD = 0.0;
    private static final double LEFT_KF = 0.18;

    private static final double RIGHT_KP = 0.4;
    private static final double RIGHT_KI = 0.0;
    private static final double RIGHT_KD = 0.0;
    private static final double RIGHT_KF = 0.25;

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

        Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().configBothFeedbackSensors(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
        Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

    }

    public void execute() {
        double leftX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
        double leftY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);        
        Drivetrain.getInstance().arcadeDriveVelocity(leftY, Math.pow(leftX, 2) * Math.signum(leftX));
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

    }

    public void end () {
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);
    }
}