package frc.robot.commands.drivetrain;

/**
 * Move the elevator 
 * 
 * @author Dawson Chen
 * @since 1/14/19
 */
public class MoveElevatorMagic extends Command {

    private double position;

    public MoveElevatorMagic() {
        requires(Elevator.getInstance());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Elevator.POSITION_SLOT_INDEX, Global.PID_PRIMARY);
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
        Drivetrain.getInstance().getLeftMaster().set(angle,Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
        Drivetrain.getInstance().getRightMaster().set(angle,Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
    }
}