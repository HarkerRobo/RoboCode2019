package frc.robot.commands.groups;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
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
public class SetScoringPosition extends CommandGroup {
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
	
	public SetScoringPosition(Location desiredLocation) {
		this.desiredLocation = desiredLocation;
		BooleanSupplier shouldRunWrist1 = () ->;
		BooleanSupplier shouldRunElevator1 = () ->;
		BooleanSupplier shouldRunWrist2 = () ->;
		BooleanSupplier shouldRunElevator2 = () ->;

		Supplier<Integer> getWrist1Position = () ->;
		Supplier<Integer> getElevator1Position = () ->;
		Supplier<Integer> getWrist2Position = () ->;
		Supplier<Integer> getElevator2Position = () ->;

		addSequential (new ConditionalCommand(shouldRunWrist1, new MoveWristMotionMagic(getWrist1Position)));
		addSequential (new ConditionalCommand(shouldRunElevator1, new MoveElevatorMotionMagic(getElevator1Position)));
		addSequential (new ConditionalCommand(shouldRunWrist2, new MoveWristMotionMagic(getWrist2Position)));
		addSequential (new ConditionalCommand(shouldRunElevator2, new MoveElevatorMotionMagic(getElevator2Position)));
	}
}