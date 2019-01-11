package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import frc.robot.OI;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * 
 * @version 1/10/19
 * @author Finn Frankis
 */
public class MoveWristManual extends IndefiniteCommand {
    public MoveWristManual () {
        requires (Wrist.getInstance());
    }
    
    public void initialize () {
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
    }

    public void execute() {
        double leftOperatorTrigger = OI.getInstance().getOperatorGamepad().getLeftTrigger();
        double rightOperatorTrigger = OI.getInstance().getOperatorGamepad().getRightTrigger();

        if (leftOperatorTrigger > rightOperatorTrigger) {
            Wrist.getInstance().setWrist(leftOperatorTrigger, WristDirection.TO_BACK);
        }
        else {
            Wrist.getInstance().setWrist(leftOperatorTrigger, WristDirection.TO_FRONT);
        }
    }
}