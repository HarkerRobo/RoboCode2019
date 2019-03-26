package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.auto.Path;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.PositionUnit;
import harkerrobolib.util.Conversions.SpeedUnit;

/**
 * Follows the given Trajectories, which must have the same timestep, 
 * for the left and right wheels using Motion Profiling
 *
 * @author Jatin Kohli
 */
public class FollowPath extends Command
{
    private MotionProfileStatus status;

	private double dt = Path.DT_DEFAULT;

    private BufferedTrajectoryPointStream leftStream;
    private BufferedTrajectoryPointStream rightStream;

    //2018 Comp
    private static final double
        LEFT_KF = 0.5,//0.5
        LEFT_KP = 1.0,//1
        LEFT_KI = 0,//0
        LEFT_KD = 25,//35 or 30

        RIGHT_KF = 0.5,//0.5
        RIGHT_KP = 1.0,//1
        RIGHT_KI = 0,//0
        RIGHT_KD = 25;//35 or 30

    private static final int I_ZONE = 0;

    private static final int MIN_POINTS = 5;

    public FollowPath(double[][] leftPath, double[][] rightPath, double dt)
    {
        requires(Drivetrain.getInstance());
        this.dt = dt;
        status = new MotionProfileStatus();

        leftStream = fillStream(leftPath);
        rightStream = fillStream(rightPath);
    }

    @Override
    protected void initialize()
    {
        configTalons();
    
        Drivetrain.getInstance().getLeftMaster().startMotionProfile(leftStream, MIN_POINTS, ControlMode.MotionProfile);
		Drivetrain.getInstance().getRightMaster().startMotionProfile(rightStream, MIN_POINTS, ControlMode.MotionProfile);
    }

    @Override
    protected void execute() {
		Drivetrain.getInstance().getLeftMaster().getMotionProfileStatus(status);

		SmartDashboard.putNumber("Top Buffer", status.topBufferCnt);
        SmartDashboard.putNumber("Bottom Buffer", status.btmBufferCnt);        
		SmartDashboard.putNumber("left error", Drivetrain.getInstance().getLeftMaster().getClosedLoopError(Global.PID_PRIMARY));
		SmartDashboard.putNumber("right error", Drivetrain.getInstance().getRightMaster().getClosedLoopError(Global.PID_PRIMARY));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

	@Override
	protected void end()
	{
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);

		Drivetrain.getInstance().getLeftMaster().clearMotionProfileTrajectories();
        Drivetrain.getInstance().getRightMaster().clearMotionProfileTrajectories();

        Drivetrain.getInstance().getLeftMaster().setNeutralMode(NeutralMode.Coast);
    	Drivetrain.getInstance().getRightMaster().setNeutralMode(NeutralMode.Coast);
    }

	@Override
	protected void interrupted()
	{
		end();
	}

    /**
     * Converts a 2-D array representing a path into TractoryPoints and writes
     * the TrajectoryPoints into a BufferedTrajectoryPointStream for Motion Profiling
     * 
     * @param points The 2-D array of points representing the path.
     *               The first column must have the position, the second column must have the velocity,
     *               the third column must have the acceleration, and the fourth column must have the heading
     *               for a given point
     * @return A BufferedTrajectoryPointStream with all the TrajectoryPoints of the path
     */
    private BufferedTrajectoryPointStream fillStream(double[][] points)
    {
        TrajectoryPoint[] trajPoints = new TrajectoryPoint[points.length];

        for (int row = 0; row < points.length; row++)
		{
            double position = Conversions.convert(PositionUnit.FEET, points[row][0], PositionUnit.ENCODER_UNITS);
            double velocity = Conversions.convert(SpeedUnit.FEET_PER_SECOND, points[row][1], SpeedUnit.ENCODER_UNITS);
            double heading = points[row][3];

            trajPoints[row] = createTrajectoryPoint(
                position, 
                velocity, 
                heading
            );
		}

        BufferedTrajectoryPointStream path = new BufferedTrajectoryPointStream();
        path.Write(trajPoints);
        return path;
    }

    /**
     * Creates a new TrajectoryPoint with the desired values
     *
     * @return the new TrajectoryPoint
     */
    private TrajectoryPoint createTrajectoryPoint(double position, double velocity, double headingDeg)
    {
        TrajectoryPoint point = new TrajectoryPoint();

        point.position = position;
        point.velocity = velocity;
        point.headingDeg = headingDeg;

        point.profileSlotSelect0 = Drivetrain.MOTION_PROF_SLOT;

        return point;
    }

	private void configTalons()
	{
        Drivetrain.getInstance().getLeftMaster().config_kP(Drivetrain.MOTION_PROF_SLOT, LEFT_KP);
        Drivetrain.getInstance().getLeftMaster().config_kI(Drivetrain.MOTION_PROF_SLOT, LEFT_KI);
        Drivetrain.getInstance().getLeftMaster().config_kD(Drivetrain.MOTION_PROF_SLOT, LEFT_KD);
        Drivetrain.getInstance().getLeftMaster().config_kF(Drivetrain.MOTION_PROF_SLOT, LEFT_KF);

        Drivetrain.getInstance().getRightMaster().config_kP(Drivetrain.MOTION_PROF_SLOT, RIGHT_KP);
        Drivetrain.getInstance().getRightMaster().config_kI(Drivetrain.MOTION_PROF_SLOT, RIGHT_KI);
        Drivetrain.getInstance().getRightMaster().config_kD(Drivetrain.MOTION_PROF_SLOT, RIGHT_KD);
        Drivetrain.getInstance().getRightMaster().config_kF(Drivetrain.MOTION_PROF_SLOT, RIGHT_KF);

        Drivetrain.getInstance().getLeftMaster().config_IntegralZone(Drivetrain.MOTION_PROF_SLOT, I_ZONE);
        Drivetrain.getInstance().getRightMaster().config_IntegralZone(Drivetrain.MOTION_PROF_SLOT, I_ZONE);

		Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.MOTION_PROF_SLOT, Global.PID_PRIMARY);
		Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.MOTION_PROF_SLOT, Global.PID_PRIMARY);
        
		Drivetrain.getInstance().getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
		Drivetrain.getInstance().getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);

		Drivetrain.getInstance().getLeftMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);
		Drivetrain.getInstance().getRightMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);

		Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
        Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

        Drivetrain.getInstance().getLeftMaster().setNeutralMode(NeutralMode.Brake);
        Drivetrain.getInstance().getRightMaster().setNeutralMode(NeutralMode.Brake);
        
		Drivetrain.getInstance().getLeftMaster().changeMotionControlFramePeriod(Math.max((int)(dt * Conversions.MS_PER_SEC / 2), 1));
		Drivetrain.getInstance().getRightMaster().changeMotionControlFramePeriod(Math.max((int)(dt * Conversions.MS_PER_SEC / 2), 1));

		Drivetrain.getInstance().getLeftMaster().configMotionProfileTrajectoryPeriod((int)(dt * Conversions.MS_PER_SEC)); //We want no additional time per point
        Drivetrain.getInstance().getRightMaster().configMotionProfileTrajectoryPeriod((int)(dt * Conversions.MS_PER_SEC)); //0
        
		Drivetrain.getInstance().getLeftMaster().configMotionProfileTrajectoryInterpolationEnable(false);
        Drivetrain.getInstance().getRightMaster().configMotionProfileTrajectoryInterpolationEnable(false);

        Drivetrain.getInstance().getLeftMaster().clearMotionProfileHasUnderrun();
        Drivetrain.getInstance().getRightMaster().clearMotionProfileHasUnderrun();
        Drivetrain.getInstance().getLeftFollower().clearMotionProfileHasUnderrun(10);
        Drivetrain.getInstance().getLeftFollower().clearMotionProfileHasUnderrun(10);
    }
}
