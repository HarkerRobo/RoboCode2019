package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.subsystems.IntakeBall;

/**
 * Intakes the ball into the robot
 * 
 * @author Anirudh Kotamraju
 * @author Austin Wang
 * @version 1/11/2019
 */
public class MoveIntakeBallTime extends TimedCommand {
    private double percentOutput;

    /**
     * Creates a new command that runs for a specific time
     * @param time the time (in seconds) that the command should run for
     */
    public MoveIntakeBallTime(double percentOutput, double time) {
        super(time);
        requires(IntakeBall.getInstance());
        this.percentOutput = percentOutput;
    }

    public void execute(){
        
    }
}

