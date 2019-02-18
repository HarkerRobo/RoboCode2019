 package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot.Side;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.hatchpanelintake.SetExtenderManual;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.Wrist;
import frc.robot.util.Pair;
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
        F1(Elevator.LOW_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_FRONT_CARGO, Elevator.LOW_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH), 
        F2(Elevator.MEDIUM_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_FRONT_CARGO, Elevator.MEDIUM_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH),
        F3(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_FRONT_CARGO, Elevator.HIGH_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH), 
        B1(Elevator.LOW_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO, Elevator.LOW_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH), 
        B2(Elevator.MEDIUM_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO, Elevator.MEDIUM_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH), 
		B3(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO, Elevator.HIGH_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH),
		CARGO_SHIP_FRONT(Elevator.CARGO_SHIP_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_FRONT_CARGO, Elevator.CARGO_SHIP_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH),
		CARGO_SHIP_BACK(Elevator.CARGO_SHIP_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO, Elevator.CARGO_SHIP_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH),

		ZERO(0, 0, 0, 0), 
		HATCH_INTAKE(Integer.MAX_VALUE, Integer.MAX_VALUE, Elevator.HATCH_INTAKING_POSITION, Wrist.HATCH_INTAKING_POSITION),
		CARGO_INTAKE(Elevator.CARGO_INTAKING_POSITION, Wrist.CARGO_INTAKING_POSITION, Integer.MAX_VALUE, Integer.MAX_VALUE); 
		
		
        private int cargoHeight;
		private int cargoAngle;
		private int hatchHeight;
		private int hatchAngle;
		private boolean hasVariableValues; // whether the height/angle depends on the presence of hatch vs. cargo

        private Location (int cargoHeight, int cargoAngle, int hatchHeight, int hatchAngle) {
			this.cargoHeight = cargoHeight;
			this.cargoAngle = cargoAngle;
			this.hatchHeight = hatchHeight;
			this.hatchAngle = hatchAngle;
			hasVariableValues = (hatchHeight != Integer.MAX_VALUE || hatchAngle != Integer.MAX_VALUE) && 
								(cargoHeight != Integer.MAX_VALUE || cargoAngle != Integer.MAX_VALUE);
        }

        public int getHatchHeight () {return hatchHeight;}
		public int getHatchAngle () {return hatchAngle;}
		public int getCargoHeight () {return cargoHeight;}
		public int getCargoAngle () {return cargoAngle;}
		public boolean getHasVariableValues () {return hasVariableValues;}
		public Pair<Integer, Integer> getDesiredHeightAndAngle () {
			Pair<Integer, Integer> desiredPair; // desired pair with height followed by angle 
			boolean hasHatch = HatchLatcher.getInstance().hasHatch();
			if (hasVariableValues) {desiredPair = hasHatch ? new Pair<Integer, Integer>(hatchHeight, hatchAngle) : 
															  new Pair<Integer, Integer>(cargoHeight, cargoAngle);}
			else {desiredPair = (hatchHeight == Integer.MAX_VALUE && hatchAngle == Integer.MAX_VALUE) ? new Pair<Integer, Integer>(cargoHeight, cargoAngle) :
																									   new Pair<Integer, Integer> (hatchHeight, hatchAngle);}
			return desiredPair;
		}
    }

	private Location desiredLocation;

	private Side desiredSide;
	private CommandGroupWrapper commandGroup;
	
	public SetScoringPosition(Location desiredLocation) {
		this.desiredLocation = desiredLocation;
	}
	
	@Override
    public void initialize () {
		ArmDirection initialDirection = Arm.getInstance().getDirection();
		commandGroup = new CommandGroupWrapper();
		commandGroup.addSequential(new SetExtenderManual(ExtenderDirection.IN));

		int desiredHeight = desiredLocation.getDesiredHeightAndAngle().getFirst();
		int desiredAngle = desiredLocation.getDesiredHeightAndAngle().getSecond();

		desiredSide = Wrist.getInstance().getSide(desiredAngle);
		Side currentSide = Wrist.getInstance().getCurrentSide();
		// if (currentSide == Side.AMBIGUOUS || desiredSide == Side.AMBIGUOUS) {
		// 	if(currentSide == Side.AMBIGUOUS && desiredSide == Side.AMBIGUOUS) {
		// 		if(Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
		// 			commandGroup.sequential(new PassthroughHigh(Side.AMBIGUOUS, desiredAngle)); 
		// 		}
		// 		else {
		// 			if() {
						
		// 			}
		// 		}
		// 	} else if(currentSide == Side.AMBIGUOUS) {
		// 		if(Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
		// 			if(desiredSide == Side.BACK) {

		// 			}
		// 		}
		// 	} else { // desired is ambiguous but not current
				
		// 	}
		// } else
		if (currentSide != desiredSide) { //opposite side
            if (currentSide == Side.BACK) { //back -> front
                if (Elevator.getInstance().isAbove(desiredHeight, Elevator.RAIL_POSITION)) { //desired height on back above rail
					commandGroup.sequential(new PassthroughHigh(currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));	
					System.out.println("a");																																																										
				}
				else {
					commandGroup.sequential(new PassthroughLow(currentSide, desiredAngle));
					commandGroup.sequential(new MoveElevatorMotionMagic(desiredHeight));
					System.out.println("b");
				}												
            }
            else { // front -> back
                if (Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
					commandGroup.sequential(new PassthroughHigh(currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
					System.out.println("c");
                }
                else {
                    commandGroup.sequential(new PassthroughLow(currentSide, desiredAngle))
								.sequential (new MoveElevatorMotionMagic(desiredHeight));
					System.out.println("d");
                }
            }
		} else { //same side
			if (currentSide == Side.BACK) { //back -> back
				commandGroup.sequential(new MoveElevatorMotionMagic(desiredHeight))
							.sequential(new MoveWristMotionMagic(desiredAngle));
				System.out.println("e");
			} else { //front -> front
				if(Elevator.getInstance().isAbove(Elevator.RAIL_POSITION) && 
				   Elevator.getInstance().isBelow(desiredHeight, Elevator.RAIL_POSITION)) { //front 3
					commandGroup.sequential(new PassthroughHigh(currentSide, desiredAngle))
								.sequential(new PassthroughLow(currentSide, desiredAngle))
								.sequential(new MoveElevatorMotionMagic(desiredHeight));
					System.out.println("f");
				} 
				else {
					
					commandGroup.sequential(new PassthroughLow(currentSide, desiredAngle))
								.sequential(new PassthroughHigh(currentSide, desiredAngle))
								.sequential(new MoveWristMotionMagic(desiredAngle));
					System.out.println("g");
				}
			}
		}
		System.out.println("Going to position " + desiredHeight + " " + desiredAngle + " " + desiredSide);
		commandGroup.sequential(new SetArmPosition(initialDirection));
		commandGroup.start();
    }

    @Override
	protected boolean isFinished() {
		return commandGroup.isCompleted();
	}
}