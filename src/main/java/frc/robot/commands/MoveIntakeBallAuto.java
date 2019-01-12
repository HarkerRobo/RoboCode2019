package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * Intakes the ball into the robot
 * 
 * @author Anirudh Kotamraju
 * @author Austin Wang
 * @version 1/11/2019
 */
public class MoveIntakeBallTime extends TimedCommand{
    /**
     * Creates a new command with a s
     */
    public MoveIntakeBallAuto(double time) {
        super(time);
        requires(IntakeBall.getInstance());
    }


}