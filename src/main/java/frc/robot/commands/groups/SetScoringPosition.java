package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot.Side;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.groups.Passthrough.PassthroughType;
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
public class SetScoringPosition extends Command {
	private int desiredHeight;
	private int desiredAngle;
	private Side desiredSide;
	private CommandGroupWrapper commandGroup;
	
	public SetScoringPosition(int desiredHeight, int desiredAngle) {
		this.desiredHeight = desiredHeight;
		this.desiredAngle = desiredAngle;
		this.desiredSide = Wrist.getInstance().getSide(desiredAngle);
    }
	
	@Override
    public void initialize () {
		commandGroup = new CommandGroupWrapper();
        Side currentSide = Wrist.getInstance().getCurrentSide();
				Wrist.getInstance().getCurrentSide();
        if (currentSide != desiredSide) { //opposite side
            if (currentSide == Side.FRONT) { //front -> back
                if (Elevator.getInstance().isAbove(desiredHeight, Elevator.RAIL_POSITION)) {//desired height on back above rail
					commandGroup.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle));																																																											
				}
				else {
					commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
				}												
            }
            else { // back -> front
                if (Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
                    commandGroup.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle));
                }
                else {
                    commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle));
                    // /commandGroup.sequential (new MoveElevatorPosition());
                }
            }
        } else { //same side
			if(currentSide == Side.FRONT) { //front -> front
				commandGroup.sequential(new MoveElevatorMotionMagic(desiredHeight))
							.sequential(new MoveWristPosition(desiredAngle));
			} else { //back -> back
				if(Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) { //back 3
					commandGroup.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle))
								.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
				} else {
					commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
								.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle))
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