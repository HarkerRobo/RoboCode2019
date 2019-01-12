package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

public class SetArmPosition extends InstantCommand {
    private ArmDirection direction;
                                    
    public SetArmPosition(ArmDirection direction) {
        requires(Arm.getInstance());
        this.direction = direction;
   }

   public void initialize() {
        Arm.getInstance().setState(direction.getState());
   }      
}