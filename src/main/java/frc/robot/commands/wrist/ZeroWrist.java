package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;

/**
 * Sets the wrist along the path that allows it
 * to hit the limit switch at the front.
 * 
 * @author Angela Jia
 * @since 2/8/19
 */
public class ZeroWrist extends Command {
    private static final double ZERO_SPEED = 0.5;
    
    public ZeroWrist() {
        requires(Wrist.getInstance());
    }

    @Override
    public void execute() {
        Wrist.getInstance().setWristPercentOutput(ZERO_SPEED, WristDirection.TO_FRONT);
    }

    /**
     * @return true if wrist has hit hard limit on front side
     */
	@Override
	protected boolean isFinished() {
		return Wrist.getInstance().getMasterTalon().getSensorCollection().isFwdLimitSwitchClosed();
	}    
}