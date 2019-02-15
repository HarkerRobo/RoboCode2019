// package frc.robot.commands.drivetrain;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.function.Function;

// import edu.wpi.first.wpilibj.command.Command;
// import frc.robot.subsystems.Drivetrain;
// import harkerrobolib.auto.Path;
// import jaci.pathfinder.Waypoint;
// import jaci.pathfinder.Trajectory.Segment;

// /**
//  * Generates a Cubic Hermite Spline for the Drivetrain to use as a trajectory.
//  * 
//  * https://en.wikipedia.org/wiki/Cubic_Hermite_spline
//  */
// public class GenerateAndFollowPath extends Command
// {
//     public static double dt = Path.DT_DEFAULT;
//     public static double maxVelocity = Path.V_DEFAULT;
//     public static double maxAcceleration = Path.ACCEL_DEFAULT;

//     public static final Function<Double, Double> BASIS_FUNCTION_00 = 
//         t -> 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1;
//     public static final Function<Double, Double> BASIS_FUNCTION_10 = 
//         t -> Math.pow(t, 3) - 2*Math.pow(t, 2) + t;
//     public static final Function<Double, Double> BASIS_FUNCTION_01 = 
//         t -> -2*Math.pow(t, 3) + 3*Math.pow(t, 2);
//     public static final Function<Double, Double> BASIS_FUNCTION_11 = 
//         t -> Math.pow(t, 3) - Math.pow(t, 2); 

//     private static List<Function<Double, Double>> functions;

//     private Waypoint[] points;

//     public GenerateAndFollowPath(Waypoint[] points)
//     {
//         requires(Drivetrain.getInstance());
//         this.points = points;

//         for (Waypoint p : points)
//         {
//             p.angle = Math.tan(p.angle); //Convert headings to derivatives on a xy coordinate plane
//         }

//         functions = new ArrayList<Function<Double, Double>>();
//     }

//     public GenerateAndFollowPath(Waypoint [] points, double dt)
//     {
//         this(points);
//         this.dt = dt;
//     }

//     public GenerateAndFollowPath(Waypoint [] points, double dt, double maxVelocity, double maxAcceleration)
//     {
//         this(points, dt);
//         this.maxVelocity = maxVelocity;
//         this.maxAcceleration = maxAcceleration;
//     }

//     @Override
//     protected void initialize() {
//         for (int i = 0; i < points.length - 1; i++) //Get splines between all waypoints
//         {
//             //Generates spline
//             double initialX = points[i].x;
//             double initialY = points[i].y;
//             double initialDx = points[i].angle;

//             double finalX = points[i + 1].x;
//             double finalY = points[i + 1].y;
//             double finalDx = points[i + 1].angle;

//             double range = finalX - initialX;

//             functions.add( x -> //Formula for smooth path between two waypoints
//                 initialY * BASIS_FUNCTION_00.apply((x - initialX)/range) + 
//                 initialDx * range * BASIS_FUNCTION_10.apply((x - initialX)/range) + 
//                 finalY * BASIS_FUNCTION_01.apply((x - initialX)/range) + 
//                 finalDx * range * BASIS_FUNCTION_11.apply((x - initialX)/range)
//             );

//             //Creates segments based on spline until at desired distance
//             List<Segment> segments = new ArrayList<Segment>();

//             double prevVelocity = 0;
//             double prevAcceleration = 0;
//             double distanceTravelled = 0;
//             double splineDistance = Math.sqrt(Math.pow(finalX - initialX, 2) + Math.pow(finalY - initialY, 2));

//             boolean isComplete = prevVelocity <= 0 && distanceTravelled > 0;

//             while (!isComplete)
//             {
//                 if (distanceTravelled <= 0)
//                     distanceTravelled = 
//                 double velPosAccel = Math.sqrt(Math.pow(prevVelocity, 2) + 2*maxAcceleration*distanceTravelled);
//                 double velMax = maxVelocity;
//                 double velNegAccel = Math.sqrt(Math.pow(prevVelocity, 2) + 2*maxAcceleration*(splineDistance - distanceTravelled));

//                 double velOutput = Math.min(velMax, Math.min(velPosAccel, velNegAccel)); //Find minimum: Trapezoidal Velocity
//                 double position = velOutput * dt;
//                 double acceleration = (velOutput - prevVelocity)/dt;
//                 double jerk = (acceleration - prevAcceleration)/dt;
//                 double heading = Math.atan();

//                 segments.add(new Segment(dt, x, y, position, velocity, acceleration, jerk, heading));
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
// }