package frc.robot.commands.drivetrain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.auto.Path;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.PositionUnit;
import harkerrobolib.util.Conversions.SpeedUnit;
import jaci.pathfinder.Waypoint;

/**
 * Generates a Cubic Hermite Spline for the Drivetrain to use as a trajectory.
 * Uses data from the limelight to generate the two waypoints needed for the path.
 * Closed loops to each point's velocity during the profile.
 *
 * https://en.wikipedia.org/wiki/Cubic_Hermite_spline
 *
 * @author Jatin Kohli
 * @author Arnav Gupta
 */
public class GenerateAndFollowPath extends Command
{
    //private static final int MIN_POINTS = 5;
    //private MotionProfileStatus status; //status of left Talon
    //private double startTime;

	private double dt = Path.DT_DEFAULT;
    private double maxVelocity = Drivetrain.MAX_FORWARD_VELOCITY;
    private double cruiseVelocity;
	private double maxAcceleration = Drivetrain.MAX_ACCELERATION;
	private double wheelBase = Drivetrain.DRIVETRAIN_DIAMETER;

	public static final double SMALL_NUMBER = 0.01;

	private Function<Double, Double> function;

    public static final Function<Double, Double> BASIS_FUNCTION_00 =
        t -> 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1;
    public static final Function<Double, Double> BASIS_FUNCTION_10 =
        t -> Math.pow(t, 3) - 2*Math.pow(t, 2) + t;
    public static final Function<Double, Double> BASIS_FUNCTION_01 =
        t -> -2*Math.pow(t, 3) + 3*Math.pow(t, 2);
    public static final Function<Double, Double> BASIS_FUNCTION_11 =
        t -> Math.pow(t, 3) - Math.pow(t, 2);


	private Function<Double, Double> derivative;

    public static final Function<Double, Double> BASIS_DERIVATIVE_00 =
        t -> 6*Math.pow(t, 2) - 6*t;
    public static final Function<Double, Double> BASIS_DERIVATIVE_10 =
        t -> 3*Math.pow(t, 2) - 4*t + 1;
    public static final Function<Double, Double> BASIS_DERIVATIVE_01 =
        t -> -6*Math.pow(t, 2) + 6*t;
    public static final Function<Double, Double> BASIS_DERIVATIVE_11 =
        t -> 3*Math.pow(t, 2) - 2*t;

    private Waypoint[] points;
    private static final int INITIAL_POINT_INDEX = 0;
    private static final int FINAL_POINT_INDEX = 1;

    private double initialX;
    private double initialY;
    private double initialDx;

    private double finalX;
    private double finalY;
    private double finalDx;

    private double x;
    private double y;

    private double initialVelocity;
    private double position;
    private double prevHeading;
    private double angularVelocity;

    private double prevLeftX;
    private double prevRightX;

    private double prevleftY;
    private double prevRightY;

    private double leftPosition;
    private double rightPosition;

    private boolean isGenerationComplete = false;

    private List<TrajectoryPoint> leftTrajectory;
    private List<TrajectoryPoint> rightTrajectory;

    //2018 Comp
    public static final double
        LEFT_KF = DriveWithVelocityManual.LEFT_KF,
        LEFT_KP = DriveWithVelocityManual.LEFT_KP,
        LEFT_KI = DriveWithVelocityManual.LEFT_KI,
        LEFT_KD = DriveWithVelocityManual.LEFT_KD,

        RIGHT_KF = DriveWithVelocityManual.RIGHT_KF,
        RIGHT_KP = DriveWithVelocityManual.RIGHT_KP,
        RIGHT_KI = DriveWithVelocityManual.RIGHT_KI,
        RIGHT_KD = DriveWithVelocityManual.RIGHT_KD;

    private static final int I_ZONE = 250;

	private Notifier notifier;

    private double period;

    public GenerateAndFollowPath(Waypoint[] points)
    {
        //status = new MotionProfileStatus();

        requires(Drivetrain.getInstance());
        this.points = points;

        for (Waypoint p : this.points)
        {
            p.angle = Math.tan(Math.toRadians(p.angle)); //Convert headings to derivatives on a xy coordinate plane for calculations
		}

		notifier = new Notifier(
            new Runnable() {
                int currentPointIndex = 0;

                @Override
                public void run() {
                    if (currentPointIndex < leftTrajectory.size())
                    {
                        Drivetrain.getInstance().getLeftMaster().set(
                            ControlMode.Velocity, leftTrajectory.get(currentPointIndex).velocity
                        );
                        Drivetrain.getInstance().getRightMaster().set(
                            ControlMode.Velocity, rightTrajectory.get(currentPointIndex).velocity
                        );
                        currentPointIndex++;
                    }
                }
            }
        );

        leftTrajectory = new ArrayList<TrajectoryPoint>();
        rightTrajectory = new ArrayList<TrajectoryPoint>();
    }

    /**
     * @param points The waypoints to follow
     * @param dt The timestep between segments in seconds, must be at least 0.002s
     */
    public GenerateAndFollowPath(Waypoint [] points, double dt)
    {
		this(points);
        this.dt = dt;
    }

    /**
     *
     * @param points The waypoints to follow
     * @param dt The timestep between segments in seconds, must be at least 0.002s
     * @param cruiseVelocity The velocity for the robot to have when not speeding up or slowing down during the profile
     */
    public GenerateAndFollowPath(Waypoint [] points, double dt, double cruiseVelocity)
    {
        this(points, dt);
        this.cruiseVelocity = cruiseVelocity;
	}

    @Override
    protected void initialize()
    {
        //double genTime = Timer.getFPGATimestamp();

        initialX = points[INITIAL_POINT_INDEX].x;
        initialY = points[INITIAL_POINT_INDEX].y;
        initialDx = points[INITIAL_POINT_INDEX].angle;

        finalX = points[FINAL_POINT_INDEX].x;
        finalY = points[FINAL_POINT_INDEX].y;
        finalDx = points[FINAL_POINT_INDEX].angle;

        double range = finalX - initialX;

        function = ( x -> //Formula for smooth spline between two waypoints
            initialY * BASIS_FUNCTION_00.apply((x - initialX)/range) +
            initialDx * range * BASIS_FUNCTION_10.apply((x - initialX)/range) +
            finalY * BASIS_FUNCTION_01.apply((x - initialX)/range) +
            finalDx * range * BASIS_FUNCTION_11.apply((x - initialX)/range)
        );

        derivative = ( x -> //Derivative of the spline, used to find heading at any point
            initialY * BASIS_DERIVATIVE_00.apply((x - initialX)/range) / range +
            initialDx * BASIS_DERIVATIVE_10.apply((x - initialX)/range) +
            finalY * BASIS_DERIVATIVE_01.apply((x - initialX)/range) / range +
            finalDx * BASIS_DERIVATIVE_11.apply((x - initialX)/range)
        );

        x = initialX;
        y = initialY;
        prevHeading = Math.toDegrees(Math.atan(initialDx));
        initialVelocity = 0; //Calculate

        position = 0;

        leftPosition = 0;
        rightPosition = 0;

        prevLeftX = initialX - wheelBase/2 * Math.cos(Math.atan(initialDx) - Math.PI/2);
        prevRightX = initialX + wheelBase/2 * Math.cos(Math.atan(initialDx) - Math.PI/2);

        prevleftY = initialY - wheelBase/2 * Math.sin(Math.atan(initialDx) - Math.PI/2);
        prevRightY = initialY + wheelBase/2 * Math.sin(Math.atan(initialDx) - Math.PI/2);

        Conversions.setWheelDiameter(Drivetrain.WHEEL_DIAMETER);

        double distanceLeft;
        double velocity = initialVelocity;
        double heading;

        double leftX, leftY, rightX, rightY , leftVelocity, rightVelocity;

        isGenerationComplete = false;

        while (!isGenerationComplete)
        {
            //estimate distance remaining and calculate robot's velocity
            distanceLeft = Math.sqrt(Math.pow(finalX - x, 2) + Math.pow(finalY - y, 2));
            velocity = calculateVelocityOutput(position, distanceLeft, angularVelocity); //Robot velocity

            x += velocity * Math.cos(Math.atan(derivative.apply(x))) * dt; //x portion of velocity vector
            y = function.apply(x);

            heading = Math.toDegrees(Math.atan(derivative.apply(x)));
            angularVelocity = (heading - prevHeading) / dt;

            //angularVelocity = (Math.PI / 360) * wheelBase * (heading - prevHeading) / (2 * dt); //angular velocity of robot
            // angularVelocity = (heading - prevHeading) / dt;
            // velocity *= velocity / (Math.abs(angularVelocity) + velocity); //Turning multiplier

            position += velocity * dt;

            //Use robot's x and y to calculate values for left and right sides
            leftX = x - wheelBase/2 * Math.cos(heading - Math.PI/2);
            rightX = x + wheelBase/2 * Math.cos(heading - Math.PI/2);

            leftY = y - wheelBase/2 * Math.sin(heading - Math.PI/2);
            rightY = y + wheelBase/2 * Math.sin(heading - Math.PI/2);

            //recalculate, find better estimation
            leftVelocity = Math.sqrt(Math.pow(leftX - prevLeftX, 2) + Math.pow(leftY - prevleftY, 2)) / dt;
            rightVelocity = Math.sqrt(Math.pow(rightX - prevRightX, 2) + Math.pow(rightY - prevRightY, 2)) / dt;

            // leftVelocity = velocity - angularVelocity;
            // rightVelocity = velocity + angularVelocity;

            leftPosition += leftVelocity * dt;
            rightPosition += rightVelocity * dt;

            // SmartDashboard.putNumber("Left Velocity", leftVelocity);
            // SmartDashboard.putNumber("Left Position", leftPosition);
            // SmartDashboard.putNumber("Right Velocity", rightVelocity);
            // SmartDashboard.putNumber("Right Position", rightPosition);
            // SmartDashboard.putNumber("Heading", heading);
            // SmartDashboard.putNumber("Forward Velocity", velocity);
            // SmartDashboard.putNumber("Angular Velocity", angularVelocity);

            TrajectoryPoint leftPoint = createTrajectoryPoint(
                Conversions.convert(PositionUnit.FEET, leftPosition, PositionUnit.ENCODER_UNITS),
                Conversions.convert(SpeedUnit.FEET_PER_SECOND, leftVelocity, SpeedUnit.ENCODER_UNITS),
                heading
            );

            TrajectoryPoint rightPoint = createTrajectoryPoint(
                Conversions.convert(PositionUnit.FEET, rightPosition, PositionUnit.ENCODER_UNITS),
                Conversions.convert(SpeedUnit.FEET_PER_SECOND, rightVelocity, SpeedUnit.ENCODER_UNITS),
                heading
            );

            prevHeading = heading;

            prevLeftX = leftX;
            prevRightX = rightX;

            prevleftY = leftY;
            prevRightY = rightY;

            leftTrajectory.add(leftPoint);
            rightTrajectory.add(rightPoint);

            isGenerationComplete = x - finalX >= 0; //Past the desired x

            // try{
            //     Thread.sleep(50);
            // }
            // catch(InterruptedException e)
            // {
            //     e.printStackTrace();
            // }
        }
        SmartDashboard.putNumber("points", leftTrajectory.size());

        //Setup and begin Motion Profile
        setLastTrajectoryPoints();
	    //configTalons();
        //startTime = Timer.getFPGATimestamp();
        //SmartDashboard.putNumber("Generation Time", startTime - genTime);
        //period = dt;
        //notifier.startPeriodic(period);

		// BufferedTrajectoryPointStream left = new BufferedTrajectoryPointStream();
		// BufferedTrajectoryPointStream right = new BufferedTrajectoryPointStream();

		// for (TrajectoryPoint leftPoint : leftTrajectory)
		// {
		//     left.Write(leftPoint);
		// }

		// for (TrajectoryPoint rightPoint : rightTrajectory)
		// {
		//     right.Write(rightPoint);
		// }

		// Drivetrain.getInstance().getLeftMaster().startMotionProfile(left, MIN_POINTS, ControlMode.MotionProfile);
		// Drivetrain.getInstance().getRightMaster().startMotionProfile(right, MIN_POINTS, ControlMode.MotionProfile);

		// Drivetrain.getInstance().getLeftMaster().set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
		// Drivetrain.getInstance().getRightMaster().set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
    }

    @Override
    protected void execute() {
		//Drivetrain.getInstance().getLeftMaster().getMotionProfileStatus(status);

		// SmartDashboard.putNumber("Top Buffer", status.topBufferCnt);
		// SmartDashboard.putNumber("Bottom Buffer", status.btmBufferCnt);
		// SmartDashboard.putBoolean("Is Valid", status.activePointValid);
			//System.out.println(Drivetrain.getInstance().getLeftMaster().getActiveTrajectoryVelocity(Global.PID_PRIMARY));
		//System.out.println(Drivetrain.getInstance().getRightMaster().getActiveTrajectoryPosition(Global.PID_PRIMARY));        
		// SmartDashboard.putNumber("left error", Drivetrain.getInstance().getLeftMaster().getClosedLoopError(Global.PID_PRIMARY));
		// SmartDashboard.putNumber("right error", Drivetrain.getInstance().getRightMaster().getClosedLoopError(Global.PID_PRIMARY));
		// SmartDashboard.putNumber("Left Encoder vel", Drivetrain.getInstance().getLeftMaster().getSelectedSensorVelocity(Global.PID_PRIMARY));
		// SmartDashboard.putNumber("Right Encoder vel", Drivetrain.getInstance().getRightMaster().getSelectedSensorVelocity(Global.PID_PRIMARY));
    }

    @Override
    protected boolean isFinished() {
        //return status.activePointValid && status.isLast;
        //return Drivetrain.getInstance().getLeftMaster().isMotionProfileFinished();
        return false;
    }

	@Override
	protected void end()
	{
        notifier.stop();
        notifier.close();

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
     * Creates a new TrajectoryPoint with the desired values
     *
     * @return the new TrajectoryPoint
     */
    private TrajectoryPoint createTrajectoryPoint(double position, double velocity, double headingDeg)
    {
        TrajectoryPoint point = new TrajectoryPoint();

        point.position = position;
        point.velocity = velocity;
        point.headingDeg =  headingDeg;
        point.isLastPoint = false;
        point.timeDur = (int)(dt * Conversions.MS_PER_SEC);

        return point;
    }

    /**
     * @param position The current distance travelled from the first waypoint to the next
     * @param distanceLeft (Approximation of) the distance remaining in the path
     */
    private double calculateVelocityOutput(double position, double distanceLeft, double angularVelocity)
    {
		double velPosAccel = isApproximately(position, 0) ?
			maxAcceleration * dt : Math.sqrt(Math.pow(initialVelocity, 2) + 2*maxAcceleration*position);
        double velNegAccel = Math.sqrt(Math.pow(initialVelocity, 2) + 2*maxAcceleration*distanceLeft);

        double velMax = Math.pow(cruiseVelocity, 2) / (Math.abs(angularVelocity) + cruiseVelocity); //Turning multiplier

        return Math.min(velMax, Math.min(velPosAccel, velNegAccel));
    }

	private boolean isApproximately(double a, double b)
	{
		return Math.abs(a - b) <= SMALL_NUMBER;
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

		Drivetrain.getInstance().getLeftMaster().configMotionProfileTrajectoryPeriod(0); //We want no additional time per point
		Drivetrain.getInstance().getRightMaster().configMotionProfileTrajectoryPeriod(0);

		Drivetrain.getInstance().getLeftMaster().configMotionProfileTrajectoryInterpolationEnable(false);
        Drivetrain.getInstance().getRightMaster().configMotionProfileTrajectoryInterpolationEnable(false);

        Drivetrain.getInstance().getLeftMaster().clearMotionProfileHasUnderrun();
        Drivetrain.getInstance().getRightMaster().clearMotionProfileHasUnderrun();
    }

    private void setLastTrajectoryPoints()
    {
        leftTrajectory.get(leftTrajectory.size() - 1).isLastPoint = true;
        rightTrajectory.get(rightTrajectory.size() - 1).isLastPoint = true;

        leftTrajectory.get(leftTrajectory.size() - 1).velocity = 0;
        rightTrajectory.get(rightTrajectory.size() - 1).velocity = 0;
    }


    // private class MotionProfileRunnable implements Runnable
    // {
    //     private int state;
    //     private int currentPointIndex;

    //     private final int DISABLED = SetValueMotionProfile.Disable.value;
    //     private final int ENABLED = SetValueMotionProfile.Enable.value;
    //     private final int HOLD = SetValueMotionProfile.Hold.value;

    //     public MotionProfileRunnable()
    //     {
    //         state = 0;
    //         currentPointIndex = 0;
    //     }

    //     @Override
    //     public void run() {
    //         Drivetrain.getInstance().getLeftMaster().getMotionProfileStatus(status);

    //         Drivetrain.getInstance().getLeftMaster().processMotionProfileBuffer();
    //             //Drivetrain.getInstance().getRightMaster().processMotionProfileBuffer();

    //         switch (state)
    //         {
    //             //Begining to load points
    //             case 0:
    //             {
    //                     //Drivetrain.getInstance().setBoth(ControlMode.MotionProfile, DISABLED);
    //                 Drivetrain.getInstance().getLeftMaster().set(ControlMode.MotionProfile, DISABLED);

    //                 pushNextPoints();

    //                 if (status.btmBufferCnt >= MIN_POINTS || currentPointIndex > leftTrajectory.size() - 1) //If we have enough points to start, then start
    //                     state++;

    //                 //Drivetrain.getInstance().getLeftMaster().clearMotionProfileHasUnderrun();
    //             }
    //             case 1:
    //             {
    //                 if (status.isLast)
    //                 {
    //                     state++;
    //                     //Drivetrain.getInstance().getLeftMaster().set(ControlMode.MotionProfile, HOLD);

    //                     double endTime = Timer.getFPGATimestamp();
    //                     SmartDashboard.putNumber("Time", endTime - startTime);
    //                     //Drivetrain.getInstance().setBoth(ControlMode.MotionProfile, HOLD);
    //                 }
    //                 else
    //                 {
    //                     Drivetrain.getInstance().getLeftMaster().set(ControlMode.MotionProfile, ENABLED);
    //                     //Drivetrain.getInstance().setBoth(ControlMode.MotionProfile, ENABLED);
    //                     boolean allPointsPushed = currentPointIndex > leftTrajectory.size() - 1;

    //                     if (!allPointsPushed)
    //                         pushNextPoints();
    //                 }
    //             }
    //             case 2:
    //             {
    //                 //Drivetrain.getInstance().getLeftMaster().set(ControlMode.MotionProfile, ENABLED);
    //                 //Drivetrain.getInstance().setBoth(ControlMode.MotionProfile, HOLD);
    //             }

    //         }
    //         SmartDashboard.putNumber("state", state);
    //         SmartDashboard.putNumber("current point index", currentPointIndex);
    //         SmartDashboard.putNumber("Top Buffer", status.topBufferCnt);
    //         SmartDashboard.putNumber("Bottom Buffer", status.btmBufferCnt);
    //         SmartDashboard.putBoolean("Is Valid", status.activePointValid);
    //         SmartDashboard.putNumber("Velocity",
    //             status.activePointValid ? Drivetrain.getInstance().getLeftMaster().getActiveTrajectoryVelocity(Global.PID_PRIMARY) : -1 );
    //         SmartDashboard.putNumber("Position",
    //             status.activePointValid ? Drivetrain.getInstance().getLeftMaster().getActiveTrajectoryPosition(Global.PID_PRIMARY) : -1 );
    //         // SmartDashboard.putNumber("Heading",
    //         //     status.activePointValid ? Drivetrain.getInstance().getLeftMaster().getActiveTrajectoryHeading() : -1);
    //         SmartDashboard.putBoolean("Is Last", status.isLast);
    //     }

    //     private void pushNextPoints()
    //     {
    //         TrajectoryPoint t = leftTrajectory.get(currentPointIndex);
    //         Drivetrain.getInstance().getLeftMaster().pushMotionProfileTrajectory(t);

    //             //Drivetrain.getInstance().getRightMaster().pushMotionProfileTrajectory(rightTrajectory.get(currentPointIndex));
    //         currentPointIndex++;
    //     }
    // }
}
