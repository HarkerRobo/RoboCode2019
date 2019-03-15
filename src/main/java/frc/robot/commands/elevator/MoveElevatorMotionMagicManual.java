package frc.robot.commands.elevator;

import frc.robot.subsystems.Elevator;
import harkerrobolib.commands.IndefiniteCommand;

public class MoveElevatorMotionMagicManual extends IndefiniteCommand {

   public MoveElevatorMotionMagicManual() {
      requires(Elevator.getInstance());
   }

   protected void initialize() {
      Elevator.getInstance().setUpMotionMagic();
   }

   protected void execute() {
      // int position = (int) ((Elevator.MAX_POSITION - 1000)* Math.abs(
      // MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(),
      // OI.DRIVER_DEADBAND)));
      // if (OI.getInstance().getDriverGamepad().getButtonBumperLeftState())
      // Elevator.getInstance().getMasterTalon().set(ControlMode.MotionMagic,
      // position);
   }
}