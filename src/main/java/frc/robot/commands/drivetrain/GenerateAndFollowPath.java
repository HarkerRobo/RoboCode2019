package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.PositionUnit;
import harkerrobolib.util.Conversions.SpeedUnit;

import frc.robot.util.FalconPathPlanner;

/**
 * Uses data from the limelight to generate the two waypoints needed for the path
 * and uses SmoothPathPlanner to generate a trajectory for each side of the drivetrain.
 * Uses Talon SRX Motion Profiling to follow each trajectory accurately.
 *
 * https://github.com/KHEngineering/SmoothPathPlanner
 *
 * @author Jatin Kohli
 * @author Arnav Gupta
 */
public class GenerateAndFollowPath extends Command
{
    private FalconPathPlanner f;

    private MotionProfileStatus status; //status of left Talon

	private double dt = 0.05;
    private static final double WHEELBASE = Drivetrain.DRIVETRAIN_DIAMETER;
    private double pathTime = 5;

    private static final double LL_Z_OFFSET = -36.7;
    private static final double LL_X_OFFSET = 4.0;

    BufferedTrajectoryPointStream left;
    BufferedTrajectoryPointStream right;

    //2018 Comp
    private static final double
        LEFT_KF = 0.5,//0.5
        LEFT_KP = 1.1,//1
        LEFT_KI = 0,//0
        LEFT_KD = 15,//35 or 30

        RIGHT_KF = 0.5,//0.5
        RIGHT_KP = 1.1,//1
        RIGHT_KI = 0,//0
        RIGHT_KD = 15;//35 or 30

    private static final int I_ZONE = 0;

    private static final int MIN_POINTS = 5;

    public GenerateAndFollowPath()
    {
        requires(Drivetrain.getInstance());
        status = new MotionProfileStatus();
    }

    /**
     * @param dt The timestep between segments in seconds
     */
    public GenerateAndFollowPath(double dt)
    {
        this();
        this.dt = dt;
    }

    /**
     * @param dt The timestep between segments in seconds
     * @param pathTime The maximum time for the path to execute
     */
    public GenerateAndFollowPath(double dt, double pathTime)
    {
        this(dt);
        this.pathTime = pathTime;
    }

    private void initPath()
    {
        double[][] waypoints = getPointsFromLimeLight(); 

        f = new FalconPathPlanner(waypoints);
        f.calculate(pathTime, dt, WHEELBASE);
    }

    @Override
    protected void initialize()
    {
        double startTime = Timer.getFPGATimestamp();
        initPath();
        System.out.println("Gen time: " + (Timer.getFPGATimestamp() - startTime));

        left = fillStream(f.leftPath, f.smoothLeftVelocity, f.heading);
        right = fillStream(f.rightPath, f.smoothRightVelocity, f.heading);

        configTalons();

        Drivetrain.getInstance().getLeftMaster().startMotionProfile(left, MIN_POINTS, ControlMode.MotionProfile);
        Drivetrain.getInstance().getRightMaster().startMotionProfile(right, MIN_POINTS, ControlMode.MotionProfile);
    }

    @Override
    protected void execute() 
    {
        Drivetrain.getInstance().getLeftMaster().getMotionProfileStatus(status);

        SmartDashboard.putNumber("Top Buffer", status.topBufferCnt);
        SmartDashboard.putNumber("Bottom Buffer", status.btmBufferCnt);  
        SmartDashboard.putBoolean("Is Valid", status.activePointValid);
        SmartDashboard.putNumber("left error", Drivetrain.getInstance().getLeftMaster().getClosedLoopError(Global.PID_PRIMARY));
        SmartDashboard.putNumber("right error", Drivetrain.getInstance().getRightMaster().getClosedLoopError(Global.PID_PRIMARY));
        
        SmartDashboard.putNumber("Yaw", Limelight.getInstance().getCamtranYaw());
        SmartDashboard.putNumber("X distance (corrected)", Limelight.getInstance().getCamtranZ());
        SmartDashboard.putNumber("Y distance", Limelight.getInstance().getCamtranX());
    }

    @Override
    protected boolean isFinished() {
        return status.isLast || 
                Math.abs(OI.getInstance().getDriverGamepad().getLeftX()) >= OI.DRIVER_DEADBAND ||
                Math.abs(OI.getInstance().getDriverGamepad().getLeftY()) >= OI.DRIVER_DEADBAND;
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
     * Gets the waypoints for the necessary path from the Limelight's localization features
     */
    private double[][] getPointsFromLimeLight()
    {
        Limelight instance = Limelight.getInstance();

        double finalX = (instance.getCamtranZ() - LL_Z_OFFSET) / Conversions.INCHES_PER_FOOT;
        double finalY = (instance.getCamtranX()) / Conversions.INCHES_PER_FOOT;
        double finalHeading = instance.getCamtranYaw();

        double offset = Math.tan(Math.toRadians(finalHeading))*finalX/5;

		double[][] waypoints = new double[][]{
			{0, LL_X_OFFSET},
			{finalX/5, LL_X_OFFSET},
			{finalX*4/5, finalY - offset},
			{finalX, finalY}
        };

        for (double[] point : waypoints)
        {
            System.out.println("{" + -point[0] + ", " + -point[1] + "},");
        }

        return waypoints;
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

    /**
     * Converts a 2-D array representing a path into TractoryPoints and writes
     * the TrajectoryPoints into a BufferedTrajectoryPointStream for Motion Profiling
     * 
     * @param path The 2-D Array representing the x and y coordinates of the robot for every point
     * @param velocity The 2-D Array with the 2nd column representing the velocity of the robot at every point
     * @param heading The 2-D Array with the 2nd column representing the heading of the robot at every point
     * 
     * @return A BufferedTrajectoryPointStream with all the TrajectoryPoints of the path
     */
    private BufferedTrajectoryPointStream fillStream(double[][] path, double[][] velocity, double[][] heading)
    {
        TrajectoryPoint[] trajPoints = new TrajectoryPoint[path.length];

        double p = 0;
        for (int row = 0; row < path.length; row++)
		{
            double v = Conversions.convert(SpeedUnit.FEET_PER_SECOND, velocity[row][1], SpeedUnit.ENCODER_UNITS);
            double h = heading[row][1];

            trajPoints[row] = createTrajectoryPoint(
                p, 
                v, 
                h
            );

            int modifier = (row == path.length - 1 || path[row + 1][0] - path[row][0] > 0) ? 1 : -1;

            double prevX = row == 0 ? path[0][0] : path[row - 1][0];
            double prevY = row == 0 ? path[0][1] : path[row - 1][1];

            p += modifier * Conversions.convert(PositionUnit.FEET, 
                Math.sqrt(Math.pow(path[row][0] - prevX, 2) + Math.pow(path[row][1] - prevY, 2)),
                PositionUnit.ENCODER_UNITS);
        }

        BufferedTrajectoryPointStream stream = new BufferedTrajectoryPointStream();
        stream.Write(trajPoints);
        return stream;
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
