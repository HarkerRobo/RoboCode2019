package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.util.Gains;

/**
 * Turn to specified angle.
 * 
 * @author Angela Jia
 * @author Dawson Chen
 * @author Aimee Wang
 * @since 1/8/19
 */
public class TurnToAngle extends Command {

    private double angle;
    
	public static final double LEFT_KP = 0;
	public static final double LEFT_KI = 0;
	public static final double LEFT_KD = 0;
	
	public static final double RIGHT_KD = 0;
	public static final double RIGHT_KI = 0;
	public static final double RIGHT_KP = 0;

    public TurnToAngle(double pigeonHeadingOnehatch) {
        requires(Drivetrain.getInstance());
        this.angle = pigeonHeadingOnehatch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize() {
        Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.ANGLE_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.ANGLE_SLOT_INDEX, Global.PID_PRIMARY);    

        Drivetrain.getInstance().getLeftMaster().configRemoteFeedbackFilter(CAN_IDs.PIGEON,RemoteSensorSource.Pigeon_Yaw,Global.REMOTE_SLOT_0);
        Drivetrain.getInstance().getRightMaster().configRemoteFeedbackFilter(CAN_IDs.PIGEON, RemoteSensorSource.Pigeon_Yaw,Global.REMOTE_SLOT_0);
        
        Drivetrain.getInstance().getLeftMaster().configSelectedFeedbackSensor(RemoteFeedbackDevice.RemoteSensor0, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().configSelectedFeedbackSensor(RemoteFeedbackDevice.RemoteSensor0, Global.PID_PRIMARY);
    
        Drivetrain.getInstance().getLeftMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);
        
        Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_PIGEON_PHASE);
        Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_PIGEON_PHASE);
        
        Drivetrain.getInstance().configClosedLoopConstants(Drivetrain.ANGLE_SLOT_INDEX, 
                new Gains()
                            .kP(LEFT_KP)
                            .kI(LEFT_KI)
                            .kD(LEFT_KD), 
                new Gains()
                            .kP(RIGHT_KP)
                            .kI(RIGHT_KI)
                            .kD(RIGHT_KD)); // kF will be set to zero if not specified
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.Position, angle);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.Position, angle);
    }
}