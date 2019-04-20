package frc.robot.commands.groups;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.OI;
import frc.robot.OI.TriggerMode;
import frc.robot.Robot;
import frc.robot.Robot.Side;
import frc.robot.commands.arm.SetArmState;
import frc.robot.commands.elevator.MoveElevatorManual;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.hatchpanelintake.SetExtenderState;
import frc.robot.commands.wrist.MoveWristManual;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.subsystems.Wrist;
import frc.robot.util.ConditionalCommand;
import frc.robot.util.Pair;
import harkerrobolib.auto.ParallelCommandGroup;
import harkerrobolib.auto.SequentialCommandGroup;
import harkerrobolib.commands.CallMethodCommand;
import harkerrobolib.util.MathUtil;

/**
 * Passes through and moves to a desired height.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @author Arnav Gupta
 * @since 2/11/19
 */
public class SetScoringPosition extends CommandGroup {
   public enum Location {
      F1(Elevator.LOW_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_FRONT_CARGO_2,
            Elevator.LOW_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH),
      F2(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO,
            Elevator.MEDIUM_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH),
      F3(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO,
            Elevator.HIGH_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH),
      B1(Elevator.LOW_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO,
            Elevator.LOW_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH),
      B2(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO_2,
            Elevator.MEDIUM_SCORING_POSITION_HATCH_BACK, Wrist.SCORING_POSITION_BACK_HATCH_2),
      B3(Elevator.HIGH_ROCKET_SCORING_POSITION_CARGO, Wrist.SCORING_POSITION_BACK_CARGO,
            Elevator.HIGH_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH),
      CARGO_SHIP_FRONT(Elevator.CARGO_SHIP_SCORING_POSITION_CARGO_FRONT, Wrist.SCORING_POSITION_FRONT_CARGO_SHIP,
            Elevator.LOW_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_FRONT_HATCH),
      CARGO_SHIP_BACK(Elevator.CARGO_SHIP_SCORING_POSITION_CARGO_FRONT, Wrist.SCORING_POSITION_BACK_CARGO_SHIP,
            Elevator.LOW_SCORING_POSITION_HATCH, Wrist.SCORING_POSITION_BACK_HATCH),
      PARALLEL_FRONT(0, Wrist.PARALLEL_FRONT, 0, Wrist.PARALLEL_FRONT),
      PARALLEL_BACK(0, Wrist.PARALLEL_BACK, 0, Wrist.PARALLEL_BACK), ZERO(0, 0, 0, 0),
      HATCH_INTAKE(Integer.MAX_VALUE, Integer.MAX_VALUE, Elevator.HATCH_INTAKING_POSITION,
            Wrist.HATCH_INTAKING_POSITION),
      CARGO_INTAKE(Elevator.CARGO_INTAKING_POSITION, Wrist.CARGO_INTAKING_POSITION, Integer.MAX_VALUE,
            Integer.MAX_VALUE);

      private int cargoHeight;
      private int cargoAngle;
      private int hatchHeight;
      private int hatchAngle;
      private boolean hasVariableValues; // whether the height/angle depends on the presence of hatch vs. cargo

      private Location(int cargoHeight, int cargoAngle, int hatchHeight, int hatchAngle) {
         this.cargoHeight = cargoHeight;
         this.cargoAngle = cargoAngle;
         this.hatchHeight = hatchHeight;
         this.hatchAngle = hatchAngle;
         hasVariableValues = (hatchHeight != Integer.MAX_VALUE || hatchAngle != Integer.MAX_VALUE)
               && (cargoHeight != Integer.MAX_VALUE || cargoAngle != Integer.MAX_VALUE);
      }

      public int getHatchHeight() {
         return hatchHeight;
      }

      public int getHatchAngle() {
         return hatchAngle;
      }

      public int getCargoHeight() {
         return cargoHeight;
      }

      public int getCargoAngle() {
         return cargoAngle;
      }

      public boolean getHasVariableValues() {
         return hasVariableValues;
      }

      public Pair<Integer, Integer> getDesiredHeightAndAngle() {
         Pair<Integer, Integer> desiredPair; // desired pair with height followed by angle
         boolean hasHatch = HatchLatcher.getInstance().hasHatch();
         if (hasVariableValues) {
            desiredPair = hasHatch ? new Pair<Integer, Integer>(hatchHeight, hatchAngle)
                  : new Pair<Integer, Integer>(cargoHeight, cargoAngle);
         } else {
            desiredPair = (hatchHeight == Integer.MAX_VALUE && hatchAngle == Integer.MAX_VALUE)
                  ? new Pair<Integer, Integer>(cargoHeight, cargoAngle)
                  : new Pair<Integer, Integer>(hatchHeight, hatchAngle);
         }
         return desiredPair;
      }
   }



   public static final double PASSTHROUGH_WAIT_TIME = 0;//.25;

   public SetScoringPosition(Location desiredLocation) {
      this(desiredLocation, () -> HatchLatcher.getInstance().hasHatch());
   }

   public SetScoringPosition(Location desiredLocation, Supplier<Boolean> doHatch) {
      Supplier<Integer> getDesiredHeight = () -> doHatch.get() ? desiredLocation.getHatchHeight()
            : desiredLocation.getCargoHeight();
      Supplier<Integer> getDesiredAngle = () -> doHatch.get() ? desiredLocation.getHatchAngle()
            : desiredLocation.getCargoAngle();
      Supplier<Side> getDesiredSide = () -> Wrist.getInstance().getSide(getDesiredAngle.get());
      Supplier<Integer> getSafePassthroughHeight = () -> doHatch.get() ? Elevator.SAFE_LOW_PASSTHROUGH_POSITION_HATCH
            : Elevator.SAFE_LOW_PASSTHROUGH_POSITION_CARGO;
      BooleanSupplier mustPassthroughLow = () -> Wrist.getInstance().getCurrentSide() != getDesiredSide.get();
      BooleanSupplier mustPassthroughHigh = () -> Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)
            && (Wrist.getInstance().getCurrentSide() == Side.FRONT
                  || Wrist.getInstance().getCurrentSide() == Side.AMBIGUOUS);
      BooleanSupplier isDefenseMode = () -> Wrist.getInstance().isAmbiguous()
            && Arm.getInstance().getDirection() == ArmDirection.UP;

      addSequential(new CallMethodCommand(() -> Wrist.getInstance().getFollowerTalon().follow(Wrist.getInstance().getMasterTalon())));
      addSequential(new CallMethodCommand(() -> Robot.log("SetScoringPosition to " + desiredLocation.name()
            + " with desired Height, " + getDesiredHeight.get() + " , desired Angle, " + getDesiredAngle.get() + ".")));
      addSequential(new InstantCommand() {
         @Override
         public void initialize() {
            Elevator.getInstance().getMasterTalon().set(ControlMode.Disabled, 0.0);
            Wrist.getInstance().getMasterTalon().set(ControlMode.Disabled, 0.0);
         }
      });

      addSequential(new CallMethodCommand(() -> System.out.println("desired height " + getDesiredHeight.get()
            + " desired angle: " + getDesiredAngle.get() + " must passthrough:" + mustPassthroughLow.getAsBoolean()
            + " safe passthrough height: " + getSafePassthroughHeight.get())));

      addSequential(new ConditionalCommand(isDefenseMode, new MoveWristMotionMagic(Wrist.SAFE_BACKWARD_POSITION)));

      addSequential(new ConditionalCommand(() -> Arm.getInstance().getDirection() == ArmDirection.UP && 
                                                !(Wrist.getInstance().getCurrentSide() == Side.BACK && getDesiredSide.get() == Side.BACK), //is it bc sometimes desired side changes throughout?
            new ConditionalCommand(
                  () -> Wrist.getInstance().getCurrentSide() == Side.FRONT
                        && Elevator.getInstance().isBelow(Elevator.ARM_COLLISION_HEIGHT),
                  new SequentialCommandGroup(new SetArmState(ArmDirection.DOWN),
                        new WaitCommand(Arm.DOWN_SAFE_ACTUATION_TIME)), new SetArmState(ArmDirection.DOWN))));
      addSequential(new ConditionalCommand(
            () -> (mustPassthroughHigh.getAsBoolean() || mustPassthroughLow.getAsBoolean()
                  || desiredLocation == Location.HATCH_INTAKE || desiredLocation == Location.CARGO_INTAKE),
            new SetExtenderState(ExtenderDirection.IN)));
      addSequential(
            new ConditionalCommand(mustPassthroughHigh, new MoveWristMotionMagic(Wrist.BACK_HIGH_PASSTHROUGH_ANGLE)));
      addSequential(new ConditionalCommand(
            () -> mustPassthroughLow.getAsBoolean() && Elevator.getInstance().isAbove(getSafePassthroughHeight.get()), // needs
                                                                                                                       // to
                                                                                                                       // pass
                                                                                                                       // through
                                                                                                                       // robot
                                                                                                                       // and
                                                                                                                       // lower
                                                                                                                       // to
                                                                                                                       // max
                                                                                                                       // passthrough
            new ParallelCommandGroup(new MoveWristMotionMagic(() -> Wrist.getInstance().getCurrentSide() == Side.FRONT ? 
                                                                  Wrist.FRONT_LOW_PASSTHROUGH_ANGLE
                                                                  : Wrist.BACK_LOW_PASSTHROUGH_ANGLE
                                                                  ), new MoveElevatorMotionMagic(getSafePassthroughHeight))));
      addSequential(
            new ConditionalCommand(() -> mustPassthroughLow.getAsBoolean(), new WaitCommand(PASSTHROUGH_WAIT_TIME)));
      addSequential(new MoveWristMotionMagic(
            () -> (mustPassthroughLow.getAsBoolean()
                  ? (getDesiredSide.get() == Side.FRONT ? Wrist.FRONT_LOW_PASSTHROUGH_ANGLE
                        : Wrist.BACK_LOW_PASSTHROUGH_ANGLE)
                  : getDesiredAngle.get()))); // perform the passthrough OR simply move to desired angle
      addSequential(new MoveElevatorMotionMagic(getDesiredHeight));

      if (desiredLocation == Location.F3) {
         addSequential(new MoveWristMotionMagic(
               () -> (doHatch.get() ? Wrist.FRONT_HIGH_PASSTHROUGH_HATCH : Wrist.SCORING_POSITION_FRONT_CARGO_3)));
      } else if (desiredLocation == Location.F2) {
         addSequential(new MoveWristMotionMagic(
               () -> (doHatch.get() ? Wrist.SCORING_POSITION_FRONT_HATCH : Wrist.SCORING_POSITION_FRONT_CARGO_2)));
      } else {
         addSequential(new MoveWristMotionMagic(getDesiredAngle));
      }

      addSequential(new MoveElevatorMotionMagic(getDesiredHeight));
      if (desiredLocation == Location.CARGO_INTAKE)
            addSequential(new SetExtenderState(ExtenderDirection.OUT));
      if (desiredLocation == Location.F1 || desiredLocation == Location.F2 || desiredLocation == Location.F3
            || desiredLocation == Location.CARGO_SHIP_FRONT) {
         addSequential(new SetArmState(ArmDirection.UP));
      }

      addSequential(new ConditionalCommand(
            () -> HatchLatcher.getInstance().hasHatch() && desiredLocation != Location.HATCH_INTAKE
                  && desiredLocation != Location.CARGO_INTAKE
                  && desiredLocation != Location.PARALLEL_BACK 
                  && desiredLocation != Location.PARALLEL_FRONT,
            new SetExtenderState(ExtenderDirection.OUT)));
      addSequential(new ConditionalCommand(() -> desiredLocation != Location.CARGO_INTAKE && 
                                                 desiredLocation != Location.PARALLEL_BACK && 
                                                 desiredLocation != Location.PARALLEL_FRONT, 
                        new SetArmState(ArmDirection.UP)));
   }

   public void end() {
      Robot.log("Set scoring position end");
      ((MoveElevatorManual) Elevator.getInstance().getDefaultCommand()).setLastPosition();
      ((MoveWristManual) Wrist.getInstance().getDefaultCommand()).setLastPosition();
   }

   @Override
   public boolean isFinished() {
         return super.isFinished() || 
         MathUtil.mapJoystickOutput(Math.abs(OI.getInstance().getOperatorGamepad().getRightY()), OI.OPERATOR_DEADBAND_JOYSTICK) > 0 || 
         MathUtil.mapJoystickOutput(Math.abs(OI.getInstance().getOperatorGamepad().getLeftY()), OI.OPERATOR_DEADBAND_JOYSTICK) > 0;
   }

   public int getDesiredHeight () {
      return 1;
   }
}