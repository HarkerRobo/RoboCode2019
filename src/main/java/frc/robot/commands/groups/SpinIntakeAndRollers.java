package frc.robot.commands.groups;

import frc.robot.Robot;
import frc.robot.commands.intake.SpinIntakeIndefinite;
import frc.robot.commands.rollers.SpinRollersIndefinite;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Rollers;
import harkerrobolib.auto.ParallelCommandGroup;
import harkerrobolib.commands.CallMethodCommand;

/**
 * Spins the Intake and Rollers Indefinitely
 * 
 * @author Chirag Kaushik
 * @since 1/17/19
 */
public class SpinIntakeAndRollers extends ParallelCommandGroup {
   public SpinIntakeAndRollers() {
      super(new CallMethodCommand(() -> Robot.log("Intaking with intake and rollers.")),
            new SpinIntakeIndefinite(Intake.DEFAULT_INTAKE_MAGNITUDE, Intake.IntakeDirection.IN),
            new SpinRollersIndefinite(Rollers.DEFAULT_ROLLER_MAGNITUDE, Rollers.RollerDirection.IN));
   }
}