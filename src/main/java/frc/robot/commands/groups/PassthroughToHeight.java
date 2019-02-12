package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot.Side;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.elevator.MoveElevatorPosition;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Passes through and moves to a desired height.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since  2/11/19
 */
public class PassthroughToHeight extends Command {
	private int desiredHeight;
	private int desiredAngle;
	private Side desiredSide;
	private CommandGroupWrapper commandGroup;
	
	public PassthroughToHeight(int desiredHeight, int desiredAngle) {
		this.desiredHeight = desiredHeight;
		this.desiredAngle = desiredAngle;
		this.desiredSide = Wrist.getInstance().getSide(desiredAngle);
    }
	
	@Override
    public void initialize () {
		commandGroup = new CommandGroupWrapper();
        Side currentSide = Wrist.getInstance().isAmbiguous() ? 
        		(desiredSide == Side.FRONT ? Side.BACK : Side.FRONT) : 
				Wrist.getInstance().getCurrentSide();
        if (currentSide != desiredSide) { //opposite side
            if (currentSide == Side.FRONT) { //front -> back
                if (Elevator.getInstance().isAbove(desiredHeight, Elevator.RAIL_POSITION)) {//desired height on back above rail
					commandGroup.sequential(new PassthroughToHigh(currentSide, desiredSide));																																																											
				}
				else {
					commandGroup.sequential(new Passthrough())
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
				}												
            }
            else { // back -> front
                if (Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
                    commandGroup.sequential(new PassthroughToHigh(currentSide, desiredSide));
                }
                else {
                    commandGroup.sequential(new Passthrough());
                    // /commandGroup.sequential (new MoveElevatorPosition());
                }
            }
        } else { //same side
			if(currentSide == Side.FRONT) { //front -> front
				commandGroup.sequential(new MoveElevatorMotionMagic(desiredHeight))
							.sequential(new MoveWristPosition(desiredAngle));
			} else { //back -> back
				if(Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) { //back 3
					commandGroup.sequential(new PassthroughToHigh(currentSide, desiredSide))
								.sequential(new Passthrough(desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
				} else {
					commandGroup.sequential(new Passthrough())
								.sequential(new PassthroughToHigh(currentSide, desiredSide))
								.sequential(new MoveWristPosition(desiredAngle));
				}
			}
		}
		
    }

    @Override
	protected boolean isFinished() {
		return commandGroup.isCompleted();
	}
}