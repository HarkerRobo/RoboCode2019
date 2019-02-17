package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.OI;
import frc.robot.subsystems.Wrist;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

public class MoveWristMotionMagicManual extends IndefiniteCommand {

    public MoveWristMotionMagicManual() {
        requires(Wrist.getInstance());
    }

    protected void initialize() {
        Wrist.getInstance().setupMotionMagic();
    }

    protected void execute() {
        int position = (int) ((Wrist.MAX_BACKWARD_POSITION - 10)* Math.abs(
            MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), 
                                        OI.DRIVER_DEADBAND)));
        Wrist.getInstance().getMasterTalon().set(ControlMode.MotionMagic, position);
    }
}