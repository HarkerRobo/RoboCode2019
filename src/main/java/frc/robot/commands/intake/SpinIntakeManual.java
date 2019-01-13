package frc.robot.commands.intake;

import frc.robot.OI;
import frc.robot.subsystems.Intake;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;


/**
 * Controls ball intake.
 * 
 * @author Anirudh Kotamraju
 * @since 1/11/2019
 */

public class SpinIntakeManual extends IndefiniteCommand {
    
    public SpinIntakeManual() {
        requires(Intake.getInstance());
    }

    public void execute() {
        double operatorLeftY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.OPERATOR_DEADBAND);
        double driverLeftTrigger =  MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND);
        
        if(Math.abs(operatorLeftY) > 0) {
            Intake.getInstance().setTalonOutput(operatorLeftY);            
        } else if(driverLeftTrigger > 0) {
            Intake.getInstance().setTalonOutput(driverLeftTrigger);
        } else {
            Intake.getInstance().setTalonOutput(0);
        }
    }

}