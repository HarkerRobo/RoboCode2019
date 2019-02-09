package frc.robot.commands.groups;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import frc.robot.util.Action;
import harkerrobolib.auto.SequentialCommandGroup;

/**
 * Sets the scoring position of elevator and wrist.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Jatin Kohli
 * @author Shahzeb Lakhani
 * @author Anirudh Kotamraju
 * @author Dawson Chen
 * @author Harsh Deep
 * @author Chirag Kaushik
 */
public class SetScoringPosition extends Command {
    private SequentialCommandGroup group;

    private Location endLocation;

    public enum Location {
        F1(Elevator.LOW_SCORING_POSITION, Wrist.ANGLE_SCORING_FRONT), 
        F2(Elevator.MEDIUM_SCORING_POSITION, Wrist.ANGLE_SCORING_FRONT), 
        F3(Elevator.HIGH_SCORING_POSITION, Wrist.ANGLE_SCORING_FRONT), 
        B1(Elevator.LOW_SCORING_POSITION, Wrist.ANGLE_SCORING_BACK),
        B2(Elevator.MEDIUM_SCORING_POSITION, Wrist.ANGLE_SCORING_BACK), 
        B3(Elevator.HIGH_SCORING_POSITION, Wrist.ANGLE_SCORING_BACK);    
        
        private int height;
        private int angle;

        private Location (int height, int angle) {
            this.height = height;
            this.angle = angle;
        }

        public int getHeight () {return height;}
        public int getAngle () {return angle;}

        public boolean isForward () {
            return angle == Wrist.ANGLE_SCORING_FRONT;
        }
    }
    
    public SetScoringPosition(Location endLocation) {
        this.endLocation = endLocation;
    }        

    @Override
    public void initialize() {
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        Location startLocation = null;
        boolean isWristForward = Wrist.getInstance().isAmbiguous() ? !Wrist.getInstance().isForward(endLocation.getAngle()) : Wrist.getInstance().isForward();

        if (isWristForward) {
            if (Elevator.getInstance().isBelow(Elevator.LOW_MIDDLE_BOUNDARY))
                startLocation = Location.F1;
            else if (Elevator.getInstance().isBelow(Elevator.RAIL_POSITION))
                startLocation = Location.F2;
            else
                startLocation = Location.F3;            
        }
        else {
            if (Elevator.getInstance().isBelow(Elevator.LOW_MIDDLE_BOUNDARY))
                startLocation = Location.B1;
            else if (Elevator.getInstance().isBelow(Elevator.RAIL_POSITION))
                startLocation = Location.B2;
            else
                startLocation = Location.B3; 
        }

        group = actions.get(new Action(startLocation, endLocation));

        group.start();
    }

    @Override
    public boolean isFinished()
    {
        return group.isCompleted();                        
    }
    
    @Override
    public void interrupted()
    {
        group.cancel();
    }

    @Override
    public void end()
    {
        interrupted();
    }

    private static Map<Action, SequentialCommandGroup> actions = Map.ofEntries(
        //Location F1
        Map.entry(new Action(Location.F1, Location.F1), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F1.getHeight()),
            new MoveWristPosition(Location.F1.getAngle())
        )),
        Map.entry(new Action(Location.F1, Location.F2), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F2.getHeight()),
            new MoveWristPosition(Location.F2.getAngle())
        )),
        Map.entry(new Action(Location.F1, Location.F3), new SequentialCommandGroup(
            new Passthrough(Location.B1.getAngle()),
            new MoveElevatorMotionMagic(Location.F3.getHeight()),
            new Passthrough(Location.F3.getAngle())
        )),
        Map.entry(new Action(Location.F1, Location.B1), new SequentialCommandGroup(
            new Passthrough(Location.B1.getAngle()),
            new MoveElevatorMotionMagic(Location.B1.getHeight())
        )),
        Map.entry(new Action(Location.F1, Location.B2), new SequentialCommandGroup(
            new Passthrough(Location.B2.getAngle()),
            new MoveElevatorMotionMagic(Location.B2.getHeight())
        )),
        Map.entry(new Action(Location.F1, Location.B3), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.B3.getHeight())
        )),

        //Location F2
        Map.entry(new Action(Location.F2, Location.F2), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F2.getHeight()),
            new MoveWristPosition(Location.F2.getAngle())
        )),
        Map.entry(new Action(Location.F2, Location.F1), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F1.getHeight()),
            new MoveWristPosition(Location.F1.getAngle())
        )),
        Map.entry(new Action(Location.F2, Location.F3), new SequentialCommandGroup(
            new Passthrough(Location.B2.getAngle()),
            new MoveElevatorMotionMagic(Location.F3.getHeight()),
            new Passthrough(Location.F3.getAngle())   
        )),
        Map.entry(new Action(Location.F2, Location.B1), new SequentialCommandGroup(
            new Passthrough(Location.B1.getAngle()),
            new MoveElevatorMotionMagic(Location.B1.getHeight())
        )),
        Map.entry(new Action(Location.F2, Location.B2), new SequentialCommandGroup(
            new Passthrough(Location.B2.getAngle()),
            new MoveElevatorMotionMagic(Location.B2.getHeight())
        )),
        Map.entry(new Action(Location.F2, Location.B3), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.B3.getHeight())
        )),

        //Location F3
        Map.entry(new Action(Location.F3, Location.F3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F3.getHeight()),
            new MoveWristPosition(Location.F3.getAngle())
        )),
        Map.entry(new Action(Location.F3, Location.F1), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.B1.getHeight()),
            new Passthrough(Location.F1.getAngle()),
            new MoveElevatorMotionMagic(Location.F1.getHeight())           
        )),
        Map.entry(new Action(Location.F3, Location.F2), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.F1.getHeight()),
            new Passthrough(Location.F2.getAngle()),
            new MoveElevatorMotionMagic(Location.F2.getHeight())                                        
        )),
        Map.entry(new Action(Location.F3, Location.B1), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.B1.getHeight())            
        )),
        Map.entry(new Action(Location.F3, Location.B2), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.B2.getHeight())
        )),
        Map.entry(new Action(Location.F3, Location.B3), new SequentialCommandGroup(
            new Passthrough(Location.B3.getAngle()),
            new MoveElevatorMotionMagic(Location.B3.getHeight())                                        
        )),

        //Location B1
        Map.entry(new Action(Location.B1, Location.B1), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B1.getHeight()),
            new MoveWristPosition(Location.B1.getAngle())
        )),
        Map.entry(new Action(Location.B1, Location.F1), new SequentialCommandGroup(
            new Passthrough(Location.F1.getAngle()),
            new MoveElevatorMotionMagic(Location.F1.getHeight())
        )),
        Map.entry(new Action(Location.B1, Location.F2), new SequentialCommandGroup(
            new Passthrough(Location.F2.getAngle()),
            new MoveElevatorMotionMagic(Location.F2.getHeight())
        )),
        Map.entry(new Action(Location.B1, Location.F3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F3.getHeight()),
            new Passthrough(Location.F3.getAngle()) 
        )), 
        Map.entry(new Action(Location.B1, Location.B2), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B2.getHeight()),
            new MoveWristPosition(Location.B2.getAngle())
        )),
        Map.entry(new Action(Location.B1, Location.B3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B3.getHeight()),
            new MoveWristPosition(Location.B3.getAngle())
        )),

        //Location B2
        Map.entry(new Action(Location.B2, Location.B2), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B2.getHeight()),
            new MoveWristPosition(Location.B2.getAngle())
        )),
        Map.entry(new Action(Location.B2, Location.F1), new SequentialCommandGroup(
            new Passthrough(Location.F1.getAngle()),
            new MoveElevatorMotionMagic(Location.F1.getHeight())
        )),
        Map.entry(new Action(Location.B2, Location.F2), new SequentialCommandGroup(
            new Passthrough(Location.F2.getAngle()),
            new MoveElevatorMotionMagic(Location.F2.getHeight())
        )),
        Map.entry(new Action(Location.B2, Location.F3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F3.getHeight()),
            new Passthrough(Location.F3.getAngle())                                                                                                                                   
        )),
        Map.entry(new Action(Location.B2, Location.B1), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B1.getHeight()),
            new MoveWristPosition(Location.B1.getAngle())
        )),
        Map.entry(new Action(Location.B2, Location.B3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B3.getHeight()),
            new MoveWristPosition(Location.B3.getAngle())
        )),
  
        //Location B3
        Map.entry(new Action(Location.B3, Location.B3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B3.getHeight()),
            new MoveWristPosition(Location.B3.getAngle())
        )),
        Map.entry(new Action(Location.B3, Location.F1), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B1.getHeight()),
            new Passthrough(Location.F1.getAngle()),
            new MoveElevatorMotionMagic(Location.F1.getHeight())
        )),
        Map.entry(new Action(Location.B3, Location.F2), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B1.getHeight()),
            new Passthrough(Location.F2.getAngle()),
            new MoveElevatorMotionMagic(Location.F2.getHeight())
        )),
        Map.entry(new Action(Location.B3, Location.F3), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.F3.getHeight()),
            new Passthrough(Location.F3.getAngle())
        )),
        Map.entry(new Action(Location.B3, Location.B1), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B1.getHeight()),
            new MoveWristPosition(Location.B1.getAngle())
        )),
        Map.entry(new Action(Location.B3, Location.B2), new SequentialCommandGroup(
            new MoveElevatorMotionMagic(Location.B2.getHeight()),
            new MoveWristPosition(Location.B2.getAngle())
        ))
    );
}