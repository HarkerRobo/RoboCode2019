// package frc.robot.commands.drivetrain;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.function.Function;

// import com.ctre.phoenix.motion.MotionProfileStatus;
// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;

// import edu.wpi.first.wpilibj.Notifier;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.command.Command;
// import edu.wpi.first.wpilibj.command.InstantCommand;
// import frc.robot.RobotMap.Global;
// import frc.robot.subsystems.Drivetrain;
// import harkerrobolib.auto.Path;
// import harkerrobolib.util.Conversions;
// import harkerrobolib.util.Gains;
// import jaci.pathfinder.Trajectory;
// import jaci.pathfinder.Waypoint;
// import jaci.pathfinder.Trajectory.Segment;

// /**
//  * Generates a Cubic Hermite Spline for the Drivetrain to use as a trajectory.
//  * Uses data from the limelight to generate the two waypoints needed for the path.
//  * 
//  * https://en.wikipedia.org/wiki/Cubic_Hermite_spline
//  * 
//  * @author Jatin Kohli
//  * @author Arnav Gupta
//  */
// public class GenerateAndFollowPath extends InstantCommand
// {
// 	private double dt;
//     private double maxVelocity = Path.V_DEFAULT;
// 	private double maxAcceleration = Path.ACCEL_DEFAULT;
// 	private double wheelBase = Path.WHEELBASE_DEFAULT;

// 	public static final double SMALL_NUMBER = 0.001;

// 	private Function<Double, Double> function;

//     public static final Function<Double, Double> BASIS_FUNCTION_00 = 
//         t -> 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1;
//     public static final Function<Double, Double> BASIS_FUNCTION_10 = 
//         t -> Math.pow(t, 3) - 2*Math.pow(t, 2) + t;
//     public static final Function<Double, Double> BASIS_FUNCTION_01 = 
//         t -> -2*Math.pow(t, 3) + 3*Math.pow(t, 2);
//     public static final Function<Double, Double> BASIS_FUNCTION_11 = 
//         t -> Math.pow(t, 3) - Math.pow(t, 2); 


// 	private Function<Double, Double> derivative;

//     public static final Function<Double, Double> BASIS_DERIVATIVE_00 = 
//         t -> 6*Math.pow(t, 2) - 6*t;
//     public static final Function<Double, Double> BASIS_DERIVATIVE_10 = 
//         t -> 3*Math.pow(t, 2) - 4*t + 1;
//     public static final Function<Double, Double> BASIS_DERIVATIVE_01 = 
//         t -> -6*Math.pow(t, 2) + 6*t;
//     public static final Function<Double, Double> BASIS_DERIVATIVE_11 = 
//         t -> 3*Math.pow(t, 2) - 2*t; 

// 	private Waypoint[] points;

//     private Trajectory leftTrajectory;
//     private Trajectory rightTrajectory;
	
// 	public static final double 
// 		LEFT_KF = 0,
// 		LEFT_KD = 0, 
// 		LEFT_KI = 0, 
// 		LEFT_KP = 0,

// 		RIGHT_KF = 0,
// 		RIGHT_KD = 0, 
// 		RIGHT_KI = 0, 
// 		RIGHT_KP = 0;

// 	private MotionProfileStatus status; //status of left Talon
// 	private Notifier notifier;

// 	double period;
// 	public static final double MIN_POINTS = 10;

//     public GenerateAndFollowPath(Waypoint[] points)
//     {
//         requires(Drivetrain.getInstance());
// 		this.points = points;

//         for (Waypoint p : this.points)
//         {
//             p.angle = Math.tan(Math.toRadians(p.angle)); //Convert headings to derivatives on a xy coordinate plane for calculations
// 		}
		
// 		status = new MotionProfileStatus();

// 		notifier = new Notifier(
// 			new Runnable(){
// 				@Override
// 				public void run() {
// 					Drivetrain.getInstance().getLeftMaster().processMotionProfileBuffer();
// 					Drivetrain.getInstance().getLeftMaster().processMotionProfileBuffer();

// 					Drivetrain.getInstance().getLeftMaster().getMotionProfileStatus(status);
// 				}
// 			}
// 		);

// 		period = dt/2 * Conversions.MS_PER_SEC;
//     }

//     /**
//      * 
//      * @param points The waypoints to follow
//      * @param dt The timestep between segments in seconds
//      */
//     public GenerateAndFollowPath(Waypoint [] points, double dt)
//     {
// 		this(points);
// 		this.dt = dt;
//     }

//     /**
//      * 
//      * @param points The waypoints to follow
//      * @param dt The timestep between segments in seconds
//      * @param maxVelocity The maximum velocity for the robot to have at any given point in ft/s
//      * @param maxAcceleration The maximum acceleration for the robot to have at any given point in ft/s^2
//      */
//     public GenerateAndFollowPath(Waypoint [] points, double dt, double maxVelocity, double maxAcceleration)
//     {
//         this(points, dt);
//         this.maxVelocity = maxVelocity;
//         this.maxAcceleration = maxAcceleration;
// 	}
	
// 	/**
//      * 
//      * @param points The waypoints to follow
//      * @param dt The timestep between segments in seconds
//      * @param maxVelocity The maximum velocity for the robot to have at any given point in ft/s
//      * @param maxAcceleration The maximum acceleration for the robot to have at any given point in ft/s^2
// 	 * @param wheelBase	The diameter of the drivetrain in ft
//      */
//     public GenerateAndFollowPath(Waypoint [] points, double dt, double maxVelocity, double maxAcceleration, double wheelBase)
//     {
// 		this(points, dt, maxVelocity, maxAcceleration);
// 		this.wheelBase = wheelBase;
//     }

//     @Override
//     protected void initialize() 
//     {
// 		double startTime = Timer.getFPGATimestamp();

// 		List<Segment> leftSegments = new ArrayList<Segment>();
// 		List<Segment> rightSegments = new ArrayList<Segment>();

//         for (int i = 0; i < points.length - 1; i++)
//         {
//             //Generates spline
//             double initialX = points[i].x;
//             double initialY = points[i].y;
//             double initialDx = points[i].angle;

//             double finalX = points[i + 1].x;
//             double finalY = points[i + 1].y;
//             double finalDx = points[i + 1].angle;

//             double range = finalX - initialX;

//             function = ( x -> //Formula for smooth spline between two waypoints
//                 initialY * BASIS_FUNCTION_00.apply((x - initialX)/range) + 
//                 initialDx * range * BASIS_FUNCTION_10.apply((x - initialX)/range) + 
//                 finalY * BASIS_FUNCTION_01.apply((x - initialX)/range) + 
//                 finalDx * range * BASIS_FUNCTION_11.apply((x - initialX)/range)
//             );

//             derivative = ( x -> //Derivative of the spline, used to find heading at any point
//                 initialY * BASIS_DERIVATIVE_00.apply((x - initialX)/range) / range + 
//                 initialDx * BASIS_DERIVATIVE_10.apply((x - initialX)/range) + 
//                 finalY * BASIS_DERIVATIVE_01.apply((x - initialX)/range) / range + 
//                 finalDx * BASIS_DERIVATIVE_11.apply((x - initialX)/range)
//             );


//             //Creates segments based on spline until at desired distance
//             double x = initialX;
//             double y = initialY;
//             double prevVelocity = 0; 
// 			double position = 0;
// 			double prevHeading = Math.atan(initialDx);
// 			double angularVelocity = 0;
			
// // 			double prevLeftX = initialX - wheelBase/2 * Math.cos(Math.atan(initialDx) - Math.PI/2);
// // 			double prevRightX = initialX + wheelBase/2 * Math.cos(Math.atan(initialDx) - Math.PI/2);

// // 			double prevleftY = initialY - wheelBase/2 * Math.sin(Math.atan(initialDx) - Math.PI/2);
// // 			double prevRightY = initialY + wheelBase/2 * Math.sin(Math.atan(initialDx) - Math.PI/2);

// // 			double prevLeftVelocity = 0;
// // 			double prevRightVelocity = 0;

// // 			double leftPosition = 0;
// // 			double rightPosition = 0;

// // 			double prevLeftAcceleration = 0;
// // 			double prevRightAcceleration = 0;

// //             boolean isComplete = false; 

// //             while (!isComplete)
// //             {
// //                 //estimate distance remaining and calculate robot's velocity
// // 				double distanceLeft = Math.sqrt(Math.pow(finalX - x, 2) + Math.pow(finalY - y, 2));
// // 				double velocity = calculateVelocityOutput(prevVelocity, position, distanceLeft, angularVelocity);
                
// //                 position += velocity * dt;

// //                 x += velocity * Math.cos(Math.atan(derivative.apply(x))) * dt; //x portion of velocity vector
// //                 y = function.apply(x);
// // 				double heading = Math.atan(derivative.apply(x));
// // 				angularVelocity = (heading - prevHeading)/dt;

// // 				//Use robot's x and y to calculate values for left and right sides
// // 				double leftX = x - wheelBase/2 * Math.cos(heading - Math.PI/2);
// // 				double rightX = x + wheelBase/2 * Math.cos(heading - Math.PI/2);

// // 				double leftY = y - wheelBase/2 * Math.sin(heading - Math.PI/2);
// // 				double rightY = y + wheelBase/2 * Math.sin(heading - Math.PI/2);

// // 				double leftVelocity = Math.sqrt(Math.pow(leftX - prevLeftX, 2) + Math.pow(leftY - prevleftY, 2)) / dt;
// // 				double rightVelocity = Math.sqrt(Math.pow(rightX - prevRightX, 2) + Math.pow(rightY - prevRightY, 2)) / dt;

// // 				leftPosition += leftVelocity * dt;
// // 				rightPosition += rightVelocity * dt;

// // 				double leftAcceleration = (leftVelocity - prevLeftVelocity)/dt;
// // 				double rightAcceleration = (rightVelocity - prevRightVelocity)/dt;

// // 				double leftJerk = (leftAcceleration - prevLeftAcceleration)/dt;
// // 				double rightJerk = (rightAcceleration - prevRightAcceleration)/dt;

// // 				leftSegments.add(new Segment(dt, leftX, leftY, leftPosition, leftVelocity, leftAcceleration, leftJerk, heading));
// // 				rightSegments.add(new Segment(dt, rightX, rightY, rightPosition, rightVelocity, rightAcceleration, rightJerk, heading));

// // 				prevVelocity = velocity;
				
// // 				prevHeading = heading;

// // 				prevLeftX = leftX;
// // 				prevRightX = rightX;
	
// // 				prevleftY = leftY;
// // 				prevRightY = rightY;
	
// // 				prevLeftVelocity = leftVelocity;
// // 				prevRightVelocity = rightVelocity;
	
// // 				prevLeftAcceleration = leftAcceleration;
// // 				prevRightAcceleration = rightAcceleration;

//                 isComplete = isApproximately(finalX, x) && isApproximately(finalY, y);
//             }
// 		}

// // 		leftTrajectory = createTrajectory(leftSegments);
// // 		rightTrajectory = createTrajectory(rightSegments);

// 		double endTime = Timer.getFPGATimestamp();
// 		System.out.print("Time taken: " + (endTime - startTime));
// 		System.out.println("Number of left points: " + leftTrajectory.segments.length);
// 		System.out.println("Number of right points: " + rightTrajectory.segments.length);

// 		//Start motion profile setup
// 		configTalons();
// 		notifier.startPeriodic(period);
// 	}

// 	@Override
// 	protected void end()
// 	{
// 		Drivetrain.getInstance().getLeftMaster().clearMotionProfileTrajectories();
// 		Drivetrain.getInstance().getRightMaster().clearMotionProfileTrajectories();

// 		Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
// 		Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);
// 	}

// 	@Override
// 	protected void interrupted()
// 	{
// 		end();
// 	}

//     /**
//      * 
//      * @param prevVelocity The velocity of the last segment
//      * @param distanceTravelled The current distance travelled from the first waypoint to the next
//      * @param splineLength (Approximation of) the length of the spline
// 	 * @param angularVelocity The angular velocity of the robot
//      * @return
//      */
//     private double calculateVelocityOutput(double prevVelocity, double distanceTravelled, double distanceLeft, double angularVelocity)
//     {
// 		//If prevVelocity is near zero, then we want to accelerate as fast as possible in the case we want to accelerate at all
// 		double velPosAccel = isApproximately(prevVelocity, 0) ? 
// 			maxAcceleration * dt : Math.sqrt(Math.pow(prevVelocity, 2) + 2*maxAcceleration*distanceTravelled);
//         double velNegAccel = Math.sqrt(Math.pow(prevVelocity, 2) + 2*maxAcceleration*distanceLeft);
		
// // 		double turningMultiplier = maxVelocity / (Math.abs(angularVelocity * wheelBase/2) + maxVelocity);
// // 		double velMax = maxVelocity * turningMultiplier;

// //         return Math.min(velMax, Math.min(velPosAccel, velNegAccel));
// //     }

// // 	private boolean isApproximately(double a, double b)
// // 	{
// // 		return Math.abs(a - b) <= SMALL_NUMBER;
// // 	}

// // 	/**
// // 	 * Creates a trajectory from the given list of segments
// // 	 */
// // 	private Trajectory createTrajectory(List<Segment> segments)
// // 	{
// // 		return new Trajectory(
// // 			segments.toArray(new Segment[segments.size()])
// // 		);
// // 	}

// // 	private void configTalons()
// // 	{
// // 		Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.MOTION_PROF_SLOT, Global.PID_PRIMARY);
// // 		Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.MOTION_PROF_SLOT, Global.PID_PRIMARY);

// // 		Drivetrain.getInstance().getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
// // 		Drivetrain.getInstance().getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
	
// 		Drivetrain.getInstance().getLeftMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);
// 		Drivetrain.getInstance().getRightMaster().setSelectedSensorPosition(0, Global.PID_PRIMARY);

// 		Drivetrain.getInstance().getLeftMaster().setSensorPhase(Drivetrain.LEFT_POSITION_PHASE);
// 		Drivetrain.getInstance().getRightMaster().setSensorPhase(Drivetrain.RIGHT_POSITION_PHASE);

// 		Drivetrain.getInstance().configClosedLoopConstants(Drivetrain.MOTION_PROF_SLOT, 
// 				new Gains()
// 					.kF(LEFT_KF)
// 					.kP(LEFT_KP)
// 					.kI(LEFT_KI)
// 					.kD(LEFT_KD), 
// 				new Gains()
// 					.kF(RIGHT_KF)
// 					.kP(RIGHT_KP)
// 					.kI(RIGHT_KI)
// 					.kD(RIGHT_KD));

// 		Drivetrain.getInstance().getLeftMaster().changeMotionControlFramePeriod((int)period);
// 		Drivetrain.getInstance().getRightMaster().changeMotionControlFramePeriod((int)period);

// 		Drivetrain.getInstance().getLeftMaster().configMotionProfileTrajectoryPeriod(0);
// 		Drivetrain.getInstance().getRightMaster().configMotionProfileTrajectoryPeriod(0);

// 		Drivetrain.getInstance().getLeftMaster().configMotionProfileTrajectoryInterpolationEnable(false);
// 		Drivetrain.getInstance().getRightMaster().configMotionProfileTrajectoryInterpolationEnable(false);
// 	}
// }
