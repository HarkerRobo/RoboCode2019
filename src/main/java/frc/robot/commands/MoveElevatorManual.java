package frc.robot.commands;

import frc.robot.OI;
import frc.robot.subsystems.Elevator;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

public class MoveElevatorManual extends IndefiniteCommand {

    public MoveElevatorManual() {
        requires(Elevator.getInstance());
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        //MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), )
            
        //Elevator.getInstance().getMaster().set(
    }
}