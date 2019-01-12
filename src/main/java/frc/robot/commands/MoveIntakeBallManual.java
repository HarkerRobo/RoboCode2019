package frc.robot.commands;

import frc.robot.OI;
import frc.robot.subsystems.BallIntake;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;


/**
 * Controls ball intake
 * 
 * @author Anirudh Kotamraju
 * @since 1/11/2019
 */

public class MoveIntakeBallManual extends IndefiniteCommand {
    
    public MoveIntakeBallManual() {
        requires(BallIntake.getInstance());
    }

    public void execute() {
        double operatorLeftY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.OPERATOR_DEADBAND);
        double driverLeftTrigger =  MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND);
        
        if(Math.abs(operatorLeftY) > 0) {
            BallIntake.getInstance().setTalonOutput(operatorLeftY);            
        } else if(driverLeftTrigger > 0) {
            BallIntake.getInstance().setTalonOutput(driverLeftTrigger);
        } else {
            BallIntake.getInstance().setTalonOutput(0);
        }
    }

}