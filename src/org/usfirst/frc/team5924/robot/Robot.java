package org.usfirst.frc.team5924.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */


public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	Joystick driveStick;
	RobotDrive myDrive;
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
//No IMU
	long start;
	long end;
//Yes IMU
	double start1;
	double end1;
	
	int mode = 0;
	ADIS16448_IMU myIMU;

	SmartDashboard dash;
	
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		myDrive = new RobotDrive(3,1,6,2);
		//This is for port 1
		driveStick = new Joystick(0);
		//This is for port 2
		//driveStick = new Joystick(1);
		mode = 0;
		myIMU = new ADIS16448_IMU(ADIS16448_IMU.Axis.kY, ADIS16448_IMU.AHRSAlgorithm.kComplementary);
		myIMU.reset();
		start = System.currentTimeMillis() *3;
		end = System.currentTimeMillis() *3;
		start1 = 0;
		end1 = 0;
		System.out.println("Version 1");
	}

	@Override
	public void autonomousInit() {
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	@Override
	public void teleopInit() {
		myIMU.reset();
		myIMU.calibrate();
	}
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		/**This is for a Joystick
		 * if(driveStick.getRawAxis(2) > .5){
			myDrive.arcadeDrive(driveStick.getRawAxis(1), -driveStick.getRawAxis(0));
		}
		*/
		//Xbox controller Code
		//Regular speed of robot.
		if(mode % 2 == 0){
			myDrive.arcadeDrive(driveStick.getRawAxis(1), -driveStick.getRawAxis(2), false);
		}
		//Half speed for Robot
		if(driveStick.getRawButton(4)){
			mode++;
		}
		if(mode % 2 == 1){
		myDrive.arcadeDrive(driveStick.getRawAxis(1)/2, -driveStick.getRawAxis(2), false);
		}
		//this is to turn the robot around 180 (Not accurate)
		if(driveStick.getRawButton(1)){
			start = System.currentTimeMillis();
			end = System.currentTimeMillis() + 1650;
		}
		if(start < end){
			myDrive.arcadeDrive(0.25, 0.7);
			System.out.println("Turning");
			start = System.currentTimeMillis();
		}
		//This is a kill button for ^^^^^^
		if(driveStick.getRawButton(10)){
			start = System.currentTimeMillis() *3;
			end = System.currentTimeMillis()*3;
		}
		//Turn 180 degrees using IMU
		if(driveStick.getRawButton(2)){
			start1 = myIMU.getYaw();
			end1 = -myIMU.getYaw();
		}
			if(start1 < end1){
			myDrive.arcadeDrive(0.25, 0.7);																																																																																																																																																																																																																														
			System.out.println("Turning");
			start1 = myIMU.getYaw();
		}
			
			
		dash.putData("IMU_DATA", myIMU);	
			
		dash.putNumber("val", myIMU.getAngle());
			
		dash.putNumber("Yaw Value", myIMU.getYaw());
		dash.putNumber("angle", myIMU.getAngleX());
		
		System.out.println(myIMU.getAngle());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

