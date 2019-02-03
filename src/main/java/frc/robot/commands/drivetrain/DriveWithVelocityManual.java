package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Pair;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.Gains;

/**
 * Drives with velocity manually.
 * 
 * @author Chirag Kaushik
 * @since February 2, 2019
 */
public class DriveWithVelocityManual extends IndefiniteCommand {
    private static final double LEFT_KP = 0.0005;
    private static final double LEFT_KI = 0.0;
    private static final double LEFT_KD = 0.0;
    private static final double LEFT_KF = 0;//0.0002;

    private static final double RIGHT_KP = 0.0005;
    private static final double RIGHT_KI = 0.0;
    private static final double RIGHT_KD = 0.0;
    private static final double RIGHT_KF = 0;//0.0002;

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
        Pair<Double, Double> leftDriverJoystick = OI.getInstance().getDriverLeftJoystick();
        Drivetrain.getInstance().arcadeDriveVelocity(leftDriverJoystick.getSecond(), leftDriverJoystick.getFirst());
        SmartDashboard.putNumber("Left reading", Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition());
        SmartDashboard.putNumber("Right reading", Drivetrain.getInstance().getRightMaster().getSelectedSensorPosition());

    }
}