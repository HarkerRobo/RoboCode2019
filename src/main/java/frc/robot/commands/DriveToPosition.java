package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.PositionUnit;

/**
 * Drives forward to desired position.
 * 
 * @author Angela Jia
 * @author Dawson Chen
 * @since 1/8/19
 */
public class DriveToPosition extends Command {

    private double setpoint;
    private int leftError;
    private int rightError;

    /**
     * 
     * @param setpoint desired position in feet
     */
    public DriveToPosition(double setpoint) {
        this.setpoint = Conversions.convert(PositionUnit.FEET, setpoint,
                Conversions.PositionUnit.ENCODER_UNITS);
        requires(Drivetrain.getInstance());
    }

    @Override
    protected boolean isFinished() {
        return Drivetrain.getInstance().isClosedLoopErrorWithin(Global.PID_PRIMARY, Drivetrain.ALLOWABLE_ERROR);
    }

    protected void initialize() {
        Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.POSITION_SLOT_INDEX, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.POSITION_SLOT_INDEX, Global.PID_PRIMARY);

        Drivetrain.getInstance().getLeftMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);

        Drivetrain.getInstance().getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        Drivetrain.getInstance().getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
       
       Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_SENSOR_PHASE);
       Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_SENSOR_PHASE);
    }

    protected void execute() {
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.Position, Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
        Drivetrain.getInstance().getRightMaster().set(ControlMode.Position, Drivetrain.getInstance().getRightMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
    }
}