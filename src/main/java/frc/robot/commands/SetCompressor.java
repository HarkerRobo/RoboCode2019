package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

/**
 * Starts the compressor.
 * 
 * @author Shahzeb Lakhani
 * @since 1/31/19
 */
public class SetCompressor extends InstantCommand{
    public enum CompressorState {
        ON (true), OFF (false);

        private boolean state;
        private CompressorState (boolean state) {
            this.state = state;
        }
        public boolean getState() { return state; }
    }

    private CompressorState cs; 

    public SetCompressor (CompressorState cs) {
        this.cs = cs;
    }

    public void initalize() {
        Robot.getCompressor().setClosedLoopControl(cs.getState());
    }
}
