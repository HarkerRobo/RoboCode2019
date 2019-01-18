package frc.robot.commands.intake;

import frc.robot.OI;
import frc.robot.subsystems.Intake;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;


/**
 * Controls ball intake.
 * 
 * @author Dawson Chen
 * @author Shahzeb Lakhani
 * @author Anirudh Kotamraju
 * @since 1/11/2019
 */

public class SpinIntakeManual extends IndefiniteCommand {
    
    public SpinIntakeManual() {
        requires(Intake.getInstance());
    }

    public void execute() {
        double operatorBallIntakeOutput = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getLeftY(), OI.OPERATOR_DEADBAND_JOYSTICK);
        Intake.getInstance().setTalonOutput(operatorBallIntakeOutput);
    }
}