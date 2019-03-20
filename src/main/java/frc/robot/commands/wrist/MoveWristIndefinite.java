package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.OI.TriggerMode;
import frc.robot.Robot;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Moves the wrist manually.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * 
 * @since 1/10/19
 */
public class MoveWristIndefinite extends IndefiniteCommand {
    private double output;
    private WristDirection direction;
   public MoveWristIndefinite(double output, WristDirection direction) {
      requires(Wrist.getInstance());
      this.output = output;
      this.direction = direction;
      Robot.log("MoveWristIndefinite constructed.");
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
    Wrist.getInstance().setWristPercentOutput(output, direction);
   }

      /**
    * {@inheritDoc}
    */
    @Override
    public void end() {
     Wrist.getInstance().setWristPercentOutput(0, direction);
    }
 

  
}