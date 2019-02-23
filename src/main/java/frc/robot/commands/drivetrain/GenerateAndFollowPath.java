// package frc.robot.commands.drivetrain;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.function.Function;

// import com.ctre.phoenix.motion.MotionProfileStatus;
// import com.ctre.phoenix.motion.TrajectoryPoint;
// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;

// import edu.wpi.first.wpilibj.Notifier;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.command.Command;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.RobotMap.Global;
// import frc.robot.subsystems.Drivetrain;
// import harkerrobolib.auto.Path;
// import harkerrobolib.util.Conversions;
// import harkerrobolib.util.Gains;
// import harkerrobolib.util.Conversions.PositionUnit;
// import harkerrobolib.util.Conversions.SpeedUnit;
// import jaci.pathfinder.Waypoint;

// /**
//  * Generates a Cubic Hermite Spline for the Drivetrain to use as a trajectory.
//  * Uses data from the limelight to generate the two waypoints needed for the path.
//  * 
//  * https://en.wikipedia.org/wiki/Cubic_Hermite_spline
//  * 
//  * @author Jatin Kohli
//  * @author Arnav Gupta
//  */
// public class GenerateAndFollowPath extends Command
// {
//     //double startTime;

// 	private double dt = Path.DT_DEFAULT;
//     private double maxVelocity = Path.V_DEFAULT;
// 	private double maxAcceleration = Path.ACCEL_DEFAULT;
// 	private double wheelBase = Path.WHEELBASE_DEFAULT;

// 	public static final double SMALL_NUMBER = 0.01;

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

//     private Waypoint[] points;
//     private static final int INITIAL_POINT_INDEX = 0;
//     private static final int FINAL_POINT_INDEX = 1;

//     private double initialX;
//     private double initialY;
//     private double initialDx;

//     private double finalX;
//     private double finalY;
//     private double finalDx;

//     private double x;
//     private double y;

//     private double initialVelocity;
//     private double position;
//     private double prevHeading;
//     private double angularVelocity;
    
//     private double prevLeftX;
//     private double prevRightX;

//     private double prevleftY;
//     private double prevRightY;

//     private double leftPosition;
//     private double rightPosition;

//     private boolean isGenerationComplete = false;

//     private List<TrajectoryPoint> leftTrajectory;
//     private List<TrajectoryPoint> rightTrajectory;
	
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
// 	// public static final double MIN_POINTS = 10;

//     public GenerateAndFollowPath(Waypoint[] points)
//     {
//         requires(Drivetrain.getInstance());
// 		this.points = points;

//         for (Waypoint p : this.points)
//         {
//             p.angle = Math.tan(Math.toRadians(p.angle)); //Convert headings to derivatives on a xy coordinate plane for calculations
// 		}
		
// 		// status = new MotionProfileStatus();

// 		notifier = new Notifier(
// 			new Runnable(){
// 				@Override
// 				public void run() {
// 					Drivetrain.getInstance().getLeftMaster().processMotionProfileBuffer();
// 					Drivetrain.getInstance().getLeftMaster().processMotionProfileBuffer();

// 					Drivetrain.getInstance().getLeftMaster().getMotionProfileStatus(status);
// 				}
// 			}
//         );
        
//         leftTrajectory = new ArrayList<TrajectoryPoint>();
//         rightTrajectory = new ArrayList<TrajectoryPoint>();
//     }

//     /**
//      * 
//      * @param points The waypoints to follow
//      * @param dt The timestep between segments in seconds
//      */
//     public GenerateAndFollowPath(Waypoint [] points, double dt)
//     {
// 		this(points);
//         this.dt = dt;
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
//         this.wheelBase = wheelBase;
//     }

//     @Override
//     protected void initialize() 
//     {
// 		//startTime = Timer.getFPGATimestamp();

//         initialX = points[INITIAL_POINT_INDEX].x;
//         initialY = points[INITIAL_POINT_INDEX].y;
//         initialDx = points[INITIAL_POINT_INDEX].angle;

//         finalX = points[FINAL_POINT_INDEX].x;
//         finalY = points[FINAL_POINT_INDEX].y;
//         finalDx = points[FINAL_POINT_INDEX].angle;

//         double range = finalX - initialX;

//         function = ( x -> //Formula for smooth spline between two waypoints
//             initialY * BASIS_FUNCTION_00.apply((x - initialX)/range) + 
//             initialDx * range * BASIS_FUNCTION_10.apply((x - initialX)/range) + 
//             finalY * BASIS_FUNCTION_01.apply((x - initialX)/range) + 
//             finalDx * range * BASIS_FUNCTION_11.apply((x - initialX)/range)
//         );

//         derivative = ( x -> //Derivative of the spline, used to find heading at any point
//             initialY * BASIS_DERIVATIVE_00.apply((x - initialX)/range) / range + 
//             initialDx * BASIS_DERIVATIVE_10.apply((x - initialX)/range) + 
//             finalY * BASIS_DERIVATIVE_01.apply((x - initialX)/range) / range + 
//             finalDx * BASIS_DERIVATIVE_11.apply((x - initialX)/range)
//         );

//         isGenerationComplete = false;

//         x = initialX;
//         y = initialY;
//         prevHeading = Math.atan(initialDx);

//         angularVelocity = 0;
//         position = 0;

//         leftPosition = 0;
//         rightPosition = 0;

//         prevLeftX = initialX - wheelBase/2 * Math.cos(Math.atan(initialDx) - Math.PI/2);
//         prevRightX = initialX + wheelBase/2 * Math.cos(Math.atan(initialDx) - Math.PI/2);

//         prevleftY = initialY - wheelBase/2 * Math.sin(Math.atan(initialDx) - Math.PI/2);
//         prevRightY = initialY + wheelBase/2 * Math.sin(Math.atan(initialDx) - Math.PI/2);

//         //Motion Profile Setup
//         period = dt/2;
// 		configTalons();
//         //notifier.startPeriodic(period);
// 	}

//     @Override
//     protected void execute() 
//     { 
//         if (!isGenerationComplete)
//         {
//             double distanceLeft;
//             double velocity;
//             double heading;

//             double leftX, leftY, rightX, rightY;
//             double leftVelocity, rightVelocity;

//             //estimate distance remaining and calculate robot's velocity
//             distanceLeft = Math.sqrt(Math.pow(finalX - x, 2) + Math.pow(finalY - y, 2));
//             velocity = calculateVelocityOutput(position, distanceLeft, angularVelocity);

//             position += velocity * dt;

//             x += velocity * Math.cos(Math.atan(derivative.apply(x))) * dt; //x portion of velocity vector
//             y = function.apply(x);

//             heading = Math.toDegrees(Math.atan(derivative.apply(x)));

//             //Use robot's x and y to calculate values for left and right sides
//             leftX = x - wheelBase/2 * Math.cos(heading - Math.PI/2);
//             rightX = x + wheelBase/2 * Math.cos(heading - Math.PI/2);

//             leftY = y - wheelBase/2 * Math.sin(heading - Math.PI/2);
//             rightY = y + wheelBase/2 * Math.sin(heading - Math.PI/2);

//             //recalculate, find better estimation
//             leftVelocity = Math.sqrt(Math.pow(leftX - prevLeftX, 2) + Math.pow(leftY - prevleftY, 2)) / dt;
//             rightVelocity = Math.sqrt(Math.pow(rightX - prevRightX, 2) + Math.pow(rightY - prevRightY, 2)) / dt;

//             leftPosition += leftVelocity * dt;
//             rightPosition += rightVelocity * dt;

//             TrajectoryPoint leftPoint = createTrajectoryPoint(
//                 Conversions.convert(PositionUnit.FEET, leftPosition, PositionUnit.ENCODER_UNITS), 
//                 Conversions.convert(SpeedUnit.FEET_PER_SECOND, leftVelocity, SpeedUnit.ENCODER_UNITS), 
//                 heading
//             );

//             TrajectoryPoint rightPoint = createTrajectoryPoint(
//                 Conversions.convert(PositionUnit.FEET, rightPosition, PositionUnit.ENCODER_UNITS), 
//                 Conversions.convert(SpeedUnit.FEET_PER_SECOND, rightVelocity, SpeedUnit.ENCODER_UNITS), 
//                 heading
//             );

//             initialVelocity = 0; //Calculate
            
//             angularVelocity = (heading - prevHeading)/dt;
//             prevHeading = heading;

//             prevLeftX = leftX;
//             prevRightX = rightX;

//             prevleftY = leftY;
//             prevRightY = rightY;

//             leftTrajectory.add(leftPoint);
//             rightTrajectory.add(rightPoint);

//             isGenerationComplete = x - finalX >= 0; //Past the desired x
//         }
//         else
//         {
//             leftTrajectory.get(leftTrajectory.size()).isLastPoint = true;
//         }

//     }

// 	@Override
// 	protected void end()
// 	{
// 		Drivetrain.getInstance().getLeftMaster().clearMotionProfileTrajectories();
// 		Drivetrain.getInstance().getRightMaster().clearMotionProfileTrajectories();

// 		Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
// 		Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);
//     }
    
//     @Override
//     protected boolean isFinished() {
//         return isGenerationComplete;
//     }

// 	@Override
// 	protected void interrupted()
// 	{
// 		end();
// 	}

//     /**
//      * Creates a new TrajectoryPoint
//      * 
//      * @return the new TrajectoryPoint
//      */
//     private TrajectoryPoint createTrajectoryPoint(double position, double velocity, double headingDeg)        
//     {
//         TrajectoryPoint point = new TrajectoryPoint();
        
//         point.position = position;
//         point.velocity = velocity;
//         point.headingDeg =  headingDeg;        
//         point.isLastPoint = false;
//         point.timeDur = (int)(dt * Conversions.MS_PER_SEC);
        
//         return point;
//     }
    
//     /**
//      * 
//      * @param distanceTravelled The current distance travelled from the first waypoint to the next
//      * @param splineLength (Approximation of) the length of the spline
// 	 * @param angularVelocity The angular velocity of the robot
//      * @return
//      */
//     private double calculateVelocityOutput(double distanceTravelled, double distanceLeft, double angularVelocity)
//     {
// 		double velPosAccel = isApproximately(distanceTravelled, 0) ? 
// 			maxAcceleration * dt : Math.sqrt(Math.pow(initialVelocity, 2) + 2*maxAcceleration*distanceTravelled);
//         double velNegAccel = Math.sqrt(Math.pow(initialVelocity, 2) + 2*maxAcceleration*distanceLeft);
        
// 		//double turningMultiplier = maxVelocity / (Math.abs(angularVelocity * wheelBase/2) + maxVelocity);
// 		double velMax = maxVelocity /* turningMultiplier*/;

//         return Math.min(velMax, Math.min(velPosAccel, velNegAccel));
//     }

// 	private boolean isApproximately(double a, double b)
// 	{
// 		return Math.abs(a - b) <= SMALL_NUMBER;
// 	}

// 	private void configTalons()
// 	{
// 		Drivetrain.getInstance().getLeftMaster().selectProfileSlot(Drivetrain.MOTION_PROF_SLOT, Global.PID_PRIMARY);
// 		Drivetrain.getInstance().getRightMaster().selectProfileSlot(Drivetrain.MOTION_PROF_SLOT, Global.PID_PRIMARY);

// 		Drivetrain.getInstance().getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
// 		Drivetrain.getInstance().getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
	
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
//     }

//     private void setLastTrajectoryPoints()
//     {
//         leftTrajectory.get(leftTrajectory.size()).isLastPoint = true;
//         rightTrajectory.get(rightTrajectory.size()).isLastPoint = true;
//     }
// }
