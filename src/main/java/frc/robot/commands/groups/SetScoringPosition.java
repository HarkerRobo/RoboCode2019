package frc.robot.commands.groups;

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

    
}