package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.PositionUnit;
import harkerrobolib.util.Gains;

/**
 * Drives forward to desired position.
 * 
 * @author Angela Jia
 * @author Dawson Chen
 * @author Arnav Gupta
 * @since 1/8/19
 */
public class DriveToPosition extends Command {

	private double setpoint;
	private int leftError;
	private int rightError;
	
	public static final double LEFT_KD = 0;
	public static final double LEFT_KI = 0;
	public static final double LEFT_KP = 0;
	
	public static final double RIGHT_KD = 0;
	public static final double RIGHT_KI = 0;
	public static final double RIGHT_KP = 0;

	/**
	 * 
	 * @param setpoint desired position in feet
	 */
	public DriveToPosition(double setpoint) {
		this.setpoint = Conversions.convert(PositionUnit.FEET, setpoint, Conversions.PositionUnit.ENCODER_UNITS);
		requires(Drivetrain.getInstance());
	}

	/**
     * {@inheritDoc}
     */
	protected void initialize() {
		Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.POSITION_SLOT_INDEX, Global.PID_PRIMARY);
		Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.POSITION_SLOT_INDEX, Global.PID_PRIMARY);

		Drivetrain.getInstance().getLeftMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);
		Drivetrain.getInstance().getRightMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);

		Drivetrain.getInstance().getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
				Global.PID_PRIMARY);
		Drivetrain.getInstance().getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
				Global.PID_PRIMARY);

		Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
		Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

        Drivetrain.getInstance().configClosedLoopConstants(Drivetrain.POSITION_SLOT_INDEX, 
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
	protected void execute() {
		Drivetrain.getInstance().getLeftMaster().set(ControlMode.Position,
				Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
		Drivetrain.getInstance().getRightMaster().set(ControlMode.Position,
				Drivetrain.getInstance().getRightMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected boolean isFinished() {
		return Drivetrain.getInstance().isClosedLoopErrorWithin(Global.PID_PRIMARY, Drivetrain.ALLOWABLE_ERROR);
	}
}