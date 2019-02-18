package frc.robot.commands.groups;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot.Side;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.util.ConditionalCommand;
import frc.robot.util.Pair;
import harkerrobolib.commands.CallMethodCommand;
import harkerrobolib.commands.PrintCommand;

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
        F3(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO, Elevator.HIGH_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH), 
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
	
	public SetScoringPosition () {

	}

	public SetScoringPosition(Location desiredLocation) {
		this.desiredLocation = desiredLocation;

		Supplier<Integer> getDesiredHeight = () -> desiredLocation.getDesiredHeightAndAngle().getFirst();
		Supplier<Integer> getDesiredAngle = () -> desiredLocation.getDesiredHeightAndAngle().getSecond();
		Supplier<Side> getDesiredSide = () -> Wrist.getInstance().getSide(getDesiredAngle.get());
		Supplier<Integer> getSafePassthroughHeight = () -> HatchLatcher.getInstance().hasHatch() ? Elevator.SAFE_LOW_PASSTHROUGH_POSITION_HATCH : 
														Elevator.SAFE_LOW_PASSTHROUGH_POSITION_CARGO;
		BooleanSupplier mustPassthroughLow = () -> Wrist.getInstance().getCurrentSide() != getDesiredSide.get();
		BooleanSupplier mustPassthroughHigh = () -> Elevator.getInstance().isAbove(Elevator.RAIL_POSITION) &&
													(Wrist.getInstance().getCurrentSide() == Side.FRONT || 
													Wrist.getInstance().getCurrentSide() == Side.AMBIGUOUS);

		addSequential(new InstantCommand() {
			@Override
			public void initialize() {
				Elevator.getInstance().getMasterTalon().set(ControlMode.Disabled, 0.0);
				Wrist.getInstance().getMasterTalon().set(ControlMode.Disabled, 0.0);
			}
		});
		
		addSequential(new SetArmPosition(ArmDirection.DOWN));
		addSequential (new ConditionalCommand(mustPassthroughHigh, new MoveWristMotionMagic(Wrist.BACK_LOW_PASSTHROUGH_ANGLE))); 
		addSequential (new ConditionalCommand(() -> mustPassthroughLow.getAsBoolean() && Elevator.getInstance().isAbove(getSafePassthroughHeight.get()), // needs to pass through robot and lower to max passthrough
						new MoveElevatorMotionMagic(getSafePassthroughHeight))); 
		 addSequential(new MoveWristMotionMagic(() -> (mustPassthroughLow.getAsBoolean() ? 
		 												(getDesiredSide.get() == Side.FRONT ? Wrist.FRONT_LOW_PASSTHROUGH_ANGLE : Wrist.BACK_LOW_PASSTHROUGH_ANGLE) 
														 : getDesiredAngle.get()))); // perform the passthrough OR simply move to desired angle
		addSequential (new MoveElevatorMotionMagic(getDesiredHeight));
		addSequential (new MoveWristMotionMagic( (desiredLocation == Location.F3) ? (() -> (HatchLatcher.getInstance().hasHatch() ? Wrist.FRONT_HIGH_PASSTHROUGH_HATCH : Wrist.FRONT_HIGH_PASSTHROUGH_CARGO)) : getDesiredAngle));
	}

	public SetScoringPosition(Location desiredLocation, boolean doHatch) {
		this.desiredLocation = desiredLocation;

		Supplier<Integer> getDesiredHeight = () -> doHatch ? desiredLocation.getHatchHeight() : desiredLocation.getCargoHeight();
		Supplier<Integer> getDesiredAngle = () -> doHatch ? desiredLocation.getHatchAngle() : desiredLocation.getCargoAngle();
		Supplier<Side> getDesiredSide = () -> Wrist.getInstance().getSide(getDesiredAngle.get());
		Supplier<Integer> getSafePassthroughHeight = () -> doHatch ? Elevator.SAFE_LOW_PASSTHROUGH_POSITION_HATCH : 
														Elevator.SAFE_LOW_PASSTHROUGH_POSITION_CARGO;
		BooleanSupplier mustPassthroughLow = () -> Wrist.getInstance().getCurrentSide() != getDesiredSide.get();
		BooleanSupplier mustPassthroughHigh = () -> Elevator.getInstance().isAbove(Elevator.RAIL_POSITION) &&
													(Wrist.getInstance().getCurrentSide() == Side.FRONT || 
													Wrist.getInstance().getCurrentSide() == Side.AMBIGUOUS);

		addSequential(new InstantCommand() {
			@Override
			public void initialize() {
				Elevator.getInstance().getMasterTalon().set(ControlMode.Disabled, 0.0);
				Wrist.getInstance().getMasterTalon().set(ControlMode.Disabled, 0.0);
			}
		});
		
		addSequential(new SetArmPosition(ArmDirection.DOWN));
		addSequential (new ConditionalCommand(mustPassthroughHigh, new MoveWristMotionMagic(Wrist.BACK_LOW_PASSTHROUGH_ANGLE))); 
		addSequential (new ConditionalCommand(() -> mustPassthroughLow.getAsBoolean() && Elevator.getInstance().isAbove(getSafePassthroughHeight.get()), // needs to pass through robot and lower to max passthrough
						new MoveElevatorMotionMagic(getSafePassthroughHeight))); 
		 addSequential(new MoveWristMotionMagic(() -> (mustPassthroughLow.getAsBoolean() ? 
		 												(getDesiredSide.get() == Side.FRONT ? Wrist.FRONT_LOW_PASSTHROUGH_ANGLE : Wrist.BACK_LOW_PASSTHROUGH_ANGLE) 
														 : getDesiredAngle.get()))); // perform the passthrough OR simply move to desired angle
		addSequential (new MoveElevatorMotionMagic(getDesiredHeight));
		addSequential (new MoveWristMotionMagic( (desiredLocation == Location.F3) ? (() -> (doHatch ? Wrist.FRONT_HIGH_PASSTHROUGH_HATCH : Wrist.FRONT_HIGH_PASSTHROUGH_CARGO)) : getDesiredAngle));
	}
}