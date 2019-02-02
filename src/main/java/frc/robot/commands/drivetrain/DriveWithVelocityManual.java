package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.Gains;

/**
 * Drives with velocity manually.
 * 
 * @author Chirag Kaushik
 * @since February 2, 2019
 */
public class DriveWithVelocityManual extends IndefiniteCommand {
    private static final double LEFT_KP = 0.0;
    private static final double LEFT_KI = 0.0;
    private static final double LEFT_KD = 0.0;
    private static final double LEFT_KF = 0.0;

    private static final double RIGHT_KP = 0.0;
    private static final double RIGHT_KI = 0.0;
    private static final double RIGHT_KD = 0.0;
    private static final double RIGHT_KF = 0.0;

    private double speed;
    private double turn;

    public DriveWithVelocityManual(double speed, double turn) {
        requires(Drivetrain.getInstance());
        this.speed = speed;
        this.turn = turn;
    }

    public void initialize() {
        Drivetrain.getInstance().configClosedLoopConstants(
            Drivetrain.VELOCITY_SLOT_INDEX, 
            new Gains().kF(LEFT_KF)
                       .kP(LEFT_KP)
                       .kI(LEFT_KI)
                       .kD(LEFT_KD),
            new Gains().kF(RIGHT_KF)
                       .kP(RIGHT_KP)
                       .kI(RIGHT_KI)
                       .kD(RIGHT_KD)
        );
        Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.VELOCITY_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().configBothFeedbackSensors(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);
        Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
        Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

    }

    public void execute() {
        Drivetrain.getInstance().arcadeDriveVelocity(speed, turn);
    }
}