package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;

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

    public TurnToAngle(int angle) {
        requires(Drivetrain.getInstance());
        this.angle = angle;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

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
    }

    @Override
    protected void execute() {
        Drivetrain.getInstance().getLeftMaster().set
    }
}