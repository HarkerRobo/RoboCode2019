package frc.robot;

import com.ctre.phoenix.CANifier.LEDChannel;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Stores all non subsystem-specific constants and CAN IDs.
 * 
 * @since 1/7/19
 */
public class RobotMap {
    public class CAN_IDs {
        public static final int DT_LEFT_MASTER = 4, DT_RIGHT_MASTER = 1,
                                DT_LEFT_FOLLOWER = 4, DT_RIGHT_FOLLOWER = 1;
        public static final int EL_MASTER = 6, EL_VICTOR_LEFT_FRONT = 3, EL_VICTOR_LEFT_BACK = 5, EL_TALON_FOLLOWER = 2;
        public static final int WRIST_MASTER = 9, WRIST_FOLLOWER = 9;
        public static final int BALL_INTAKE_MASTER = 1;
        public static final int ARM_MASTER = 0;
        public static final int RO_TOP = 3, RO_BOTTOM = 7;
        public static final int ARM_FORWARD_CHANNEL = 0, ARM_REVERSE_CHANNEL = 1;
        public static final int EXTENDER_FORWARD_CHANNEL = 2, EXTENDER_REVERSE_CHANNEL = 5,
                                FLOWER_FORWARD_CHANNEL = 0, FLOWER_REVERSE_CHANNEL = 0;
        public static final int PIGEON = 1;
        public static final int PCM = 0;
    }

    public static class Global {
        public static final int PID_PRIMARY = 0;
        public static final int PID_AUXILIARY = 1;
        public static final int REMOTE_SLOT_0 = 0;
        public static final int REMOTE_SLOT_1 = 1;
        public static final double WAIT_TIME = 0;
        public static final int REMOTE_0 = 0;
        public static final NeutralMode DISABLED_NEUTRAL_MODE = NeutralMode.Coast;
        public static final LEDChannel RED_CHANNEL= LEDChannel.LEDChannelA;
        public static final LEDChannel GREEN_CHANNEL = LEDChannel.LEDChannelB;
        public static final LEDChannel BLUE_CHANNEL = LEDChannel.LEDChannelC;

        public static final String LIMELIGHT_URL = "http://10.10.72.11:5802";
    }
    
}