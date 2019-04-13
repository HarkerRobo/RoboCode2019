package frc.robot.util;

import edu.wpi.first.wpilibj.buttons.Trigger;
import harkerrobolib.wrappers.HSGamepad;

public class TriggerButton extends Trigger {
    private HSGamepad gamepad;
    private TriggerSide side;

    public static final double TRIGGER_DEADBAND = 0.5;

    public enum TriggerSide {
        LEFT, RIGHT;
    }

    public TriggerButton (HSGamepad gamepad, TriggerSide side) {
        this.gamepad = gamepad;
        this.side = side;
    }

    @Override
    public boolean get() {
        return (side == TriggerSide.LEFT) ? gamepad.getLeftTrigger() > TRIGGER_DEADBAND : gamepad.getRightTrigger() > TRIGGER_DEADBAND;
    }

}