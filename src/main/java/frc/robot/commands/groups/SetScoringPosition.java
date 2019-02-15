package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot.Side;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.groups.Passthrough.PassthroughType;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Passes through and moves to a desired height.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @author Arnav Gupta
 * @since  2/11/19
 */
public class SetScoringPosition extends Command {
	public enum Location {
        F1(Elevator.LOW_SCORING_POSITION, Wrist.ANGLE_SCORING_FRONT), 
        F2(Elevator.MEDIUM_SCORING_POSITION, Wrist.ANGLE_SCORING_FRONT), 
        F3(Elevator.HIGH_SCORING_POSITION, Wrist.ANGLE_SCORING_FRONT), 
        B1(Elevator.LOW_SCORING_POSITION, Wrist.ANGLE_SCORING_BACK),
        B2(Elevator.MEDIUM_SCORING_POSITION, Wrist.ANGLE_SCORING_BACK), 
		B3(Elevator.HIGH_SCORING_POSITION, Wrist.ANGLE_SCORING_BACK),
		ZERO(0, 0), 
		HATCH_INTAKE(Elevator.HATCH_INTAKE_SCORING_POSITION, Wrist.HATCH_INTAKE_SCORING_ANGLE),
		CARGO_INTAKE(Elevator.CARGO_INTAKE_SCORING_POSITION, Wrist.CARGO_INTAKE_SCORING_ANGLE); 
        
        private int height;
        private int angle;

        private Location (int height, int angle) {
            this.height = height;
            this.angle = angle;
        }

        public int getHeight () {return height;}
        public int getAngle () {return angle;}
    }

	private int desiredHeight;
	private int desiredAngle;
	private Side desiredSide;
	private CommandGroupWrapper commandGroup;
	
	public SetScoringPosition(int desiredHeight, int desiredAngle) {
		this.desiredHeight = desiredHeight;
		this.desiredAngle = desiredAngle;
		this.desiredSide = Wrist.getInstance().getSide(desiredAngle);
    }
	
	public SetScoringPosition(Location desiredLocation) {
		this(desiredLocation.getHeight(), desiredLocation.getAngle());
	}
	
	@Override
    public void initialize () {
		commandGroup = new CommandGroupWrapper();
		Side currentSide = Wrist.getInstance().getCurrentSide();
		// if (currentSide == Side.AMBIGUOUS || desiredSide == Side.AMBIGUOUS) {
		// 	if()
		// } else
		 if (currentSide != desiredSide) { //opposite side
            if (currentSide == Side.BACK) { //back -> front
                if (Elevator.getInstance().isAbove(desiredHeight, Elevator.RAIL_POSITION)) { //desired height on back above rail
					commandGroup.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));																																																											
				}
				else {
					commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
				}												
            }
            else { // front -> back
                if (Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
					commandGroup.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
                }
                else {
                    commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
                    			.sequential (new MoveElevatorMotionMagic(desiredHeight));
                }
            }
		} else { //same side
			if (currentSide == Side.BACK) { //back -> back
				commandGroup.sequential(new MoveElevatorMotionMagic(desiredHeight))
							.sequential(new MoveWristMotionMagic(desiredAngle));
			} else { //front -> front
				if(Elevator.getInstance().isAbove(Elevator.RAIL_POSITION) && 
				   Elevator.getInstance().isBelow(desiredHeight, Elevator.RAIL_POSITION)) { //front 3
					commandGroup.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle))
								.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
				} 
				else {
					
					commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, desiredAngle))
								.sequential(new Passthrough(PassthroughType.HIGH, currentSide, desiredAngle))
								.sequential(new MoveWristMotionMagic(desiredAngle));
				}
			}
		}
		commandGroup.start();
    }

    @Override
	protected boolean isFinished() {
		return commandGroup.isCompleted();
	}
}