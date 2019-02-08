package frc.robot.commands.groups;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.SequentialCommandGroup;

/**
 * Sets the scoring position of elevator and wrist.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Jatin Kohli
 * @author Shahzeb Lakhani
 */
public class SetScoringPosition extends Command {
    private SequentialCommandGroup group;

    private Map<Action, SequentialCommandGroup> actions;

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
        requires(Elevator.getInstance());
        requires(Wrist.getInstance());

        this.endLocation = endLocation;

        actions = Map.ofEntries(
            Map.entry(new Action(Location.F1, Location.F1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F1, Location.F2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F1, Location.F3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F1, Location.B1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F1, Location.B2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F1, Location.B3), new SequentialCommandGroup()),
            //Location F1

            Map.entry(new Action(Location.F2, Location.F2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F2, Location.F1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F2, Location.F3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F2, Location.B1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F2, Location.B2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F2, Location.B3), new SequentialCommandGroup()),
            //Location F2

            Map.entry(new Action(Location.F3, Location.F3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F3, Location.F1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F3, Location.F2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F3, Location.B1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F3, Location.B2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.F3, Location.B3), new SequentialCommandGroup()),
            //Location F3

            Map.entry(new Action(Location.B1, Location.B1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B1, Location.F1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B1, Location.F2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B1, Location.F3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B1, Location.B2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B1, Location.B3), new SequentialCommandGroup()),
            //Location B1

            Map.entry(new Action(Location.B2, Location.B2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B2, Location.F1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B2, Location.F2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B2, Location.F3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B2, Location.B1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B2, Location.B3), new SequentialCommandGroup()),
            //Location B2

            Map.entry(new Action(Location.B3, Location.B3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B3, Location.F1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B3, Location.F2), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B3, Location.F3), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B3, Location.B1), new SequentialCommandGroup()),
            Map.entry(new Action(Location.B3, Location.B2), new SequentialCommandGroup()));
            //Location B3        
    }        

    @Override
    public void initialize() {
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        Location startLocation = null;
        boolean isWristForward = Wrist.getInstance().isFurtherForward(Wrist.SAFE_FORWARD_POSITION) || 
                                    (!Wrist.getInstance().isFurtherBack(Wrist.SAFE_BACKWARD_POSITION) ? // ambiguous case (not safely forward or backward)
                                            !endLocation.isForward() : false);

        if (isWristForward && Elevator.getInstance().isBelow(Elevator.LOW_MIDDLE_BOUNDARY)) {startLocation = Location.F1;}
        else if (isWristForward && Elevator.getInstance().isBelow(Elevator.MIDDLE_HIGH_BOUNDARY)) {startLocation = Location.F2;}
        else if (isWristForward && Elevator.getInstance().isAbove(Elevator.MIDDLE_HIGH_BOUNDARY)) {startLocation = Location.F3;}
        else if (!isWristForward && Elevator.getInstance().isBelow(Elevator.LOW_MIDDLE_BOUNDARY)) {startLocation = Location.B1;}
        else if (!isWristForward && Elevator.getInstance().isBelow(Elevator.MIDDLE_HIGH_BOUNDARY)) {startLocation = Location.B2;}
        else if (!isWristForward && Elevator.getInstance().isAbove(Elevator.MIDDLE_HIGH_BOUNDARY)) {startLocation = Location.B3;}
        else System.out.println("No valid starting location found.");
        
        group = actions.get(new Action(startLocation, endLocation));

        group.start();
    }

    @Override
    public boolean isFinished()
    {
        return group.isCompleted();                        
    }            

    private class Action {
        private Location startLocation;
        private Location endLocation;

        public Action (Location startLocation, Location endLocation) {
            this.startLocation = startLocation;
            this.endLocation = endLocation;
        }

        @Override
        public boolean equals (Object o) {
            if (o instanceof Action) {
                Action a = (Action) o;
                return startLocation == a.startLocation && endLocation == a.endLocation;
            }
            return false;
        }
    }
}

























//Add allowable error
        // if (!Elevator.getInstance().isAt(height) || !Wrist.getInstance().isAt(angle)) //checks if already at the desired angle and height
        // {
        //     if (height == Elevator.HIGH_SCORING_POSITION) //If we want to end in high position
        //     {
        //         boolean mustPassThrough = Wrist.getInstance().isFurtherForward(Wrist.ANGLE_SCORING_BACK);
        //         if (mustPassThrough && Elevator.getInstance().isBelow(Elevator.HIGH_SCORING_POSITION)) //If we need to passthrough (Not at right height or on wrong side)
        //         {
        //             if (hasHatchPanel && Elevator.getInstance().isBelow(Elevator.MAX_HATCH_PASSTHROUGH_POSITION)) //If we have a hatch and need to lower
        //                 group.sequential(new MoveElevatorMotionMagic(Elevator.MAX_HATCH_PASSTHROUGH_POSITION)); //Lower
        //             else if (!hasHatchPanel && Elevator.getInstance().isBelow(Elevator.MAX_NORM_PASSTHROUGH_POSITION)) //If we dont have a hatch and need to lower
        //                 group.sequential(new MoveElevatorMotionMagic(Elevator.MAX_NORM_PASSTHROUGH_POSITION)); //Lower

        //             group.sequential(new MoveWristPosition(Wrist.ANGLE_SCORING_BACK)); //Passthrough
        //         }
        //         group.sequential(new MoveElevatorMotionMagic(height)) //Go to top
        //             .sequential(new MoveWristPosition(angle));        //Go to angle
        //     }
        //     else if (Elevator.getInstance().isBelow(Elevator.MAX_HATCH_PASSTHROUGH_POSITION) || 
        //             !hasHatchPanel && Elevator.getInstance().isBelow(Elevator.MAX_NORM_PASSTHROUGH_POSITION)) //If we can passthrough
        //     {
        //         group.sequential(new MoveWristPosition(angle)) //passthrough
        //                  .sequential(new MoveElevatorMotionMagic(height)); //go to desired height
        //     }
        //     else //If we can't passthrough and don't want to score in the high goals
        //     {
        //         if (Elevator.getInstance().isAt(Elevator.HIGH_SCORING_POSITION)) //If we are currently at the high position
        //             group.sequential(new MoveWristPosition(Wrist.ANGLE_SCORING_BACK)); //Move to safe angle to lower elevator

        //         if (Wrist.getInstance().isAt(angle)) //If we need to passthrough
        //         {
        //             if (!hasHatchPanel && Elevator.getInstance().isBelow(Elevator.MAX_NORM_PASSTHROUGH_POSITION)) //if we have no hatch panel and need to lower
        //                 group.sequential(new MoveElevatorMotionMagic(Elevator.MAX_NORM_PASSTHROUGH_POSITION)); //Setup for passthrough
        //             else //If we have a hatch panel
        //                 group.sequential(new MoveElevatorMotionMagic(0)); //Setup for passthrough
                    
        //             group.sequential(new MoveWristPosition(angle)); //Passthrough
        //         }

        //         group.sequential(new MoveElevatorMotionMagic(height)); //Go to desired height
        //     }
        // }
        // public enum ScoreHeight {  
        //     LOW(Elevator.LOW_SCORING_POSITION), MIDDLE(Elevator.MEDIUM_SCORING_POSITION), HIGH(Elevator.HIGH_SCORING_POSITION);     
        //     private int height;
        //     private ScoreHeight(int height){
        //         this.height = height;
        //     }
        //     public int getHeight(){
        //         return height;
        //     }
        // }
        
        // public enum ScoreAngle {
        //     FORWARD_LOW(Wrist.ANGLE_SCORING_FRONT), FORWARD_HIGH(Wrist.ANGLE_SCORING_BACK);
    
        //     private int angle;
            
        //     private ScoreAngle(int angle)
        //     {
        //         this.angle = angle;                                                    
        //     }
                  
        //     public int getAngle() {
        //         return angle;
        //     }                                  
        // }         