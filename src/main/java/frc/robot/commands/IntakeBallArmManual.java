package frc.robot.commands;

import frc.robot.OI;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;
import frc.robot.subsystems.IntakeBall;


/**
 * Controls ball intake
 * 
 * @author Anirudh Kotamraju
 * @version 1/11/2019
 */

public class IntakeBallArmManual extends IndefiniteCommand {
    
    public IntakeBallArmManual() {
        requires(IntakeBall.getInstance());
    }

    public void execute() {
        double operatorLeftY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.OPERATOR_DEADBAND);
        double driverLeftTrigger =  MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND);
        
        if(Math.abs(operatorLeftY) > 0) {
            IntakeBall.getInstance().setTalonOutput(operatorLeftY);            
        } else if(driverLeftTrigger > 0) {
            IntakeBall.getInstance().setTalonOutput(driverLeftTrigger);
        } else {
            IntakeBall.getInstance().setTalonOutput(0);
        }
    }

}