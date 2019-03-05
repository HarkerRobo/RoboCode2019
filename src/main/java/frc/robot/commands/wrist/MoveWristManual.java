package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Moves the wrist manually.
 * @author Finn Frankis
 * @author Angela Jia 
 * 
 * @since 1/10/19
 */
public class MoveWristManual extends IndefiniteCommand {
    private boolean isHolding;
    private boolean shouldClosedLoop;
    private double lastPos;

    public MoveWristManual () {
        requires (Wrist.getInstance());
        isHolding = false;
        shouldClosedLoop = false;
        lastPos = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize () {
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
        Wrist.getInstance().setupMotionMagic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        double leftOperatorTrigger = OI.getInstance().getDriverGamepad().getLeftTrigger();
        double rightOperatorTrigger = OI.getInstance().getDriverGamepad().getRightTrigger();

        double magnitude = 0;
        WristDirection direction;
        //double currentPosition = Wrist.getInstance().getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY);
        if (OI.getInstance().getWristToggleMode() && (leftOperatorTrigger > OI.DRIVER_DEADBAND_TRIGGER || rightOperatorTrigger > OI.DRIVER_DEADBAND_TRIGGER)) {
            isHolding = false;
            shouldClosedLoop = true;
            
            if (leftOperatorTrigger > rightOperatorTrigger) {
                magnitude = 0.35 * leftOperatorTrigger;
                direction = WristDirection.TO_BACK;
            }
            else {
                magnitude = 0.35 * rightOperatorTrigger;
                direction = WristDirection.TO_FRONT;
            }

            Wrist.getInstance().setWristPercentOutput(magnitude, direction);
        }
        else {
            if (!isHolding) {lastPos = Wrist.getInstance().getCurrentAngleEncoder();}
            isHolding = true;
        }

        if (isHolding && shouldClosedLoop) {
            Wrist.getInstance().setWrist(ControlMode.MotionMagic, lastPos);
            SmartDashboard.putNumber("Wrist Error",  Wrist.getInstance().getMasterTalon().getClosedLoopError());
        }
    }

    public void disableClosedLoop () {
        shouldClosedLoop = false;
    }
}