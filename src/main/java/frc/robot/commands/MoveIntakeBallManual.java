package frc.robot.commands;

import frc.robot.OI;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;
import frc.robot.subsystems.IntakeBall;


public class MoveIntakeBallManual extends IndefiniteCommand {
    public MoveIntakeBallManual() {
            IntakeBall.getInstance().setTalonOutput(operatorLeftY);            
        } else if(driverLeftTrigger > 0) {
            IntakeBall.getInstance().setTalonOutput(driverLeftTrigger);
        } else {
            IntakeBall.getInstance().setTalonOutput(0);
        }
    }

}