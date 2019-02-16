// package frc.robot.commands.drivetrain;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.function.Function;

// import edu.wpi.first.wpilibj.command.Command;
// import frc.robot.subsystems.Drivetrain;
// import harkerrobolib.auto.Path;
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
// public class GenerateAndFollowPath extends Command
// {
//     public static double dt = Path.DT_DEFAULT;
//     public static double maxVelocity = Path.V_DEFAULT;
//     public static double maxAcceleration = Path.ACCEL_DEFAULT;

//     // public static final Function<Double, Double> BASIS_FUNCTION_00 = 
//     //     t -> 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1;
//     // public static final Function<Double, Double> BASIS_FUNCTION_10 = 
//     //     t -> Math.pow(t, 3) - 2*Math.pow(t, 2) + t;
//     // public static final Function<Double, Double> BASIS_FUNCTION_01 = 
//     //     t -> -2*Math.pow(t, 3) + 3*Math.pow(t, 2);
//     // public static final Function<Double, Double> BASIS_FUNCTION_11 = 
//     //     t -> Math.pow(t, 3) - Math.pow(t, 2); 

//     public static final Function<Double, Double> BASIS_DERIVATIVE_00 = 
//         t -> 6*Math.pow(t, 2) - 6*t;
//     public static final Function<Double, Double> BASIS_DERIVATIVE_10 = 
//         t -> 3*Math.pow(t, 2) - 4*t + 1;
//     public static final Function<Double, Double> BASIS_DERIVATIVE_01 = 
//         t -> -6*Math.pow(t, 2) + 6*t;
//     public static final Function<Double, Double> BASIS_DERIVATIVE_11 = 
//         t -> 3*Math.pow(t, 2) - 2*t; 

//     // private Function<Double, Double> function;
//     private Function<Double, Double> derivative;

//     private Trajectory leftTrajectory;
//     private Trajectory rightTrajectory;

//     private Waypoint[] points;

//     public GenerateAndFollowPath(Waypoint[] points)
//     {
//         requires(Drivetrain.getInstance());
//         this.points = points;

//         for (Waypoint p : points)
//         {
//             p.angle = Math.tan(p.angle); //Convert headings to derivatives on a xy coordinate plane for calculations
//         }
//     }

//     /**
//      * 
//      * @param points The waypoints to follow
//      * @param dt The timestep between segments
//      */
//     public GenerateAndFollowPath(Waypoint [] points, double dt)
//     {
//         this(points);
//         this.dt = dt;
//     }

//     /**
//      * 
//      * @param points The waypoints to follow
//      * @param dt The timestep between segments
//      * @param maxVelocity The maximum velocity for the robot to have at any given point
//      * @param maxAcceleration The maximum acceleration for the robot to have at any given point
//      */
//     public GenerateAndFollowPath(Waypoint [] points, double dt, double maxVelocity, double maxAcceleration)
//     {
//         this(points, dt);
//         this.maxVelocity = maxVelocity;
//         this.maxAcceleration = maxAcceleration;
//     }

//     @Override
//     protected void initialize() 
//     {
//         List<Segment> segments = new ArrayList<Segment>();

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

//             // function = ( x -> //Formula for smooth spline between two waypoints
//             //     initialY * BASIS_FUNCTION_00.apply((x - initialX)/range) + 
//             //     initialDx * range * BASIS_FUNCTION_10.apply((x - initialX)/range) + 
//             //     finalY * BASIS_FUNCTION_01.apply((x - initialX)/range) + 
//             //     finalDx * range * BASIS_FUNCTION_11.apply((x - initialX)/range)
//             // );

//             derivative = ( x -> //Derivative of the spline to find heading at any point
//                 initialY * BASIS_DERIVATIVE_00.apply((x - initialX)/range) / range + 
//                 initialDx * BASIS_DERIVATIVE_10.apply((x - initialX)/range) + 
//                 finalY * BASIS_DERIVATIVE_01.apply((x - initialX)/range) / range + 
//                 finalDx * BASIS_DERIVATIVE_11.apply((x - initialX)/range)
//             );

//             //Creates segments based on spline until at desired distance
//             double x = initialX;
//             double y = initialY;
//             double prevVelocity = 0;
//             double prevAcceleration = 0;
//             double position = 0;

//             boolean isComplete = false;

//             while (!isComplete)
//             {
//                 //estimate spline length, more accurate as further through path
//                 double splineLength = Math.sqrt(Math.pow(finalX - x, 2) + Math.pow(finalY - y, 2));

//                 //calculate needed velocity
//                 double velocity = calculateVelocityOutput(prevVelocity, position, splineLength);
                
//                 position += velocity * dt;
//                 double acceleration = (velocity - prevVelocity)/dt;
//                 double jerk = (acceleration - prevAcceleration)/dt; //Should always be zero or +- maxAcceleration

//                 x += velocity * Math.cos(Math.atan(derivative.apply(x))) * dt; //x portion of velocity vector times timestep
//                 y += velocity * Math.sin(Math.atan(derivative.apply(x))) * dt; //y portion of velocity vector times timestep
//                 double heading = Math.atan(derivative.apply(x));

//                 segments.add(new Segment(dt, x, y, position, velocity, acceleration, jerk, heading));

//                 prevVelocity = velocity;
//                 prevAcceleration = acceleration;

//                 isComplete = prevVelocity <= 0 && position != 0;
//             }
//         } 


             
//     }

//     @Override
//     protected void execute() {
        
//     }

//     @Override
//     protected boolean isFinished() {
//         return false;
//     }

//     /**
//      * 
//      * @param prevVelocity The velocity of the last segment
//      * @param distanceTravelled The current distance travelled from the first waypoint to the next
//      * @param splineLength (Approximation of) the length of the spline
//      * @return
//      */
//     private double calculateVelocityOutput(double prevVelocity, double distanceTravelled, double splineLength)
//     {
//         double velPosAccel = Math.sqrt(Math.pow(prevVelocity, 2) + 2*maxAcceleration*distanceTravelled);
//         double velMax = maxVelocity;
//         double velNegAccel = Math.sqrt(Math.pow(prevVelocity, 2) + 2*maxAcceleration*(splineLength - distanceTravelled));
        
//         return Math.min(velMax, Math.min(velPosAccel, velNegAccel));
//     }

// }