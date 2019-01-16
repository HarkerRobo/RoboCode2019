package frc.robot;

/**
 * Stores all non subsystem-specific constants and CAN IDs.
 * 
 * @since 1/7/19
 */
public class RobotMap {

    public class CAN_IDs {
        public static final int DT_LEFT_MASTER = 2, DT_RIGHT_MASTER = 1,
                                DT_LEFT_FOLLOWER = 4, DT_RIGHT_FOLLOWER = 3,
                                EL_MASTER = 0, EL_VICTOR_ONE = 0, EL_VICTOR_TWO = 0,
                                WRIST_MASTER = 0, WRIST_FOLLOWER = 0,
                                BALL_INTAKE_MASTER = 0,
                                ARM_MASTER = 0;
        public static final int RO_TOP = 0; 
        public static final int RO_BOTTOM = 0;
        public static final int ARM_FORWARD_CHANNEL = 0, ARM_REVERSE_CHANNEL = 1;
        public static final int HATCH_FORWARD_CHANNEL = 2, HATCH_REVERSE_CHANNEL = 3;
        public static final int PIGEON = 0;
        public static final int PCM = 0;
    }

    public class Global {
        public static final int PID_PRIMARY = 0;
        public static final int PID_AUXILIARY = 1;
        public static final int REMOTE_SLOT_0 = 0;
        public static final int REMOTE_SLOT_1 = 1;
    }
    
}