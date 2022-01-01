/*******************************************************************************
 * Copyright (c) 2017 I3S and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     UCA - I3S Laboratory - julien.deantoni@univ-cotedazur.fr -> initial API
 *******************************************************************************/

package fr.univcotedazur.kairos.webots.polycreate.controler;

import java.util.Random;

//import org.eclipse.january.dataset.Dataset;
//import org.eclipse.january.dataset.DatasetFactory;

import com.cyberbotics.webots.controller.Camera;
import com.cyberbotics.webots.controller.CameraRecognitionObject;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.GPS;
import com.cyberbotics.webots.controller.LED;
import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.Node;
import com.cyberbotics.webots.controller.Pen;
import com.cyberbotics.webots.controller.PositionSensor;
import com.cyberbotics.webots.controller.Receiver;
import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Supervisor;
import com.cyberbotics.webots.controller.TouchSensor;
import com.yakindu.core.TimerService;

import fr.unice.polytech.si4.fsm.robot.RobotStateMachine;

import javax.swing.*;
import java.awt.event.*;

public class PolyCreateControler extends Supervisor {

	static int MAX_SPEED = 16;
	static int NULL_SPEED = 0;
	static int HALF_SPEED = 8;
	static int MIN_SPEED = -16;

	static double WHEEL_RADIUS = 0.031;//轮半径
	static double AXLE_LENGTH = 0.271756;//轴长
	static double ENCODER_RESOLUTION = 507.9188;//
	public RobotStateMachine theFSM;
	
	
	//控制界面
	
	protected JFrame jf;
	protected JPanel jp;
	protected JButton startWeeping,stopWeeping,startWriting,stopWriting,startMoving,stopMoving;
	
	

	/**
	 * the inkEvaporation parameter in the WorldInfo element of the robot scene may be interesting to access
	 */
	public Pen pen = null;

	public Pen getPen() {
		return pen;
	}


	public Motor[] gripMotors = new Motor[2];//motor是马达api
	public DistanceSensor gripperSensor = null;

	public Motor leftMotor = null;//左马达
	public Motor rightMotor = null;

	public PositionSensor leftSensor = null;
	public PositionSensor rightSensor = null;

	public LED ledOn = null;
	public LED ledPlay = null;
	public LED ledStep = null;

	//返回布尔值是否碰撞
	public TouchSensor leftBumper = null;//bumper保险杠，左接触传感器
	public TouchSensor rightBumper = null;//右接触传感器

	public DistanceSensor leftCliffSensor = null;//cliff悬崖，距离传感器有左/右/前左/前右方向
	public DistanceSensor rightCliffSensor = null;
	public DistanceSensor frontLeftCliffSensor = null;
	public DistanceSensor frontRightCliffSensor = null;

	public DistanceSensor frontDistanceSensor = null;
	public DistanceSensor frontLeftDistanceSensor = null;
	public DistanceSensor frontRightDistanceSensor = null;
	
	public Camera frontCamera = null;//前置与后置摄像头
	public Camera backCamera = null;

	public Receiver receiver = null; //接受数据

	public GPS gps = null;
	
	public 	int timestep = Integer.MAX_VALUE;
	public 	Random random = new Random();




	public PolyCreateControler() {



		timestep = (int) Math.round(this.getBasicTimeStep());//round四舍五入，时间步骤


		theFSM = new RobotStateMachine(); 
		TimerService timer = new TimerService();
		theFSM.setTimerService(timer);
		
		theFSM.getDoForward().subscribe(new myDoForwardObserver(this));
		theFSM.getLeftTurn().subscribe(new myLeftTurnObserver(this));
		theFSM.getRightTurn().subscribe(new myRightTurnObserver(this));
		theFSM.getBackTurn().subscribe(new myGoBackwardObserver(this));
//		theFSM.getIsBump().subscribe(new myIsBumpObserver(this));

		

		
		
		
		
		pen = createPen("pen");

		gripMotors[0] = createMotor("motor 7");
		gripMotors[1] = createMotor("motor 7 left");
		gripperSensor = createDistanceSensor("gripper middle distance sensor");
		gripperSensor.enable(timestep);

		leftMotor = createMotor("left wheel motor");
		rightMotor = createMotor("right wheel motor");
		leftMotor.setPosition(Double.POSITIVE_INFINITY);
		rightMotor.setPosition(Double.POSITIVE_INFINITY);
		leftMotor.setVelocity(0.0);
		rightMotor.setVelocity(0.0);

		leftSensor = createPositionSensor("left wheel sensor");
		rightSensor = createPositionSensor("right wheel sensor");
		leftSensor.enable(timestep);//启动左距离传感器的测量
		rightSensor.enable(timestep);//启动右距离传感器的测量

		ledOn = createLED("led_on");//Led灯
		ledPlay = createLED("led_play");
		ledStep = createLED("led_step");

		leftBumper = createTouchSensor("bumper_left");
		rightBumper = createTouchSensor("bumper_right");
		leftBumper.enable(timestep);//左接触传感器启动
		rightBumper.enable(timestep);//右接触传感器启动

		leftCliffSensor = createDistanceSensor("cliff_left");
		rightCliffSensor = createDistanceSensor("cliff_right");
		frontLeftCliffSensor = createDistanceSensor("cliff_front_left");
		frontRightCliffSensor = createDistanceSensor("cliff_front_right");
		leftCliffSensor.enable(timestep);
		rightCliffSensor.enable(timestep);
		frontLeftCliffSensor.enable(timestep);
		frontRightCliffSensor.enable(timestep);
		
		frontDistanceSensor = createDistanceSensor("front distance sensor");
		frontDistanceSensor.enable(timestep);
		frontLeftDistanceSensor = createDistanceSensor("front left distance sensor");
		frontLeftDistanceSensor.enable(timestep);
		frontRightDistanceSensor = createDistanceSensor("front right distance sensor");
		frontRightDistanceSensor.enable(timestep);
		
		frontCamera = createCamera("frontCamera");
		frontCamera.enable(timestep);//开摄像头
		frontCamera.recognitionEnable(timestep);//可识别

		backCamera = createCamera("backCamera");
		backCamera.enable(timestep);
		backCamera.recognitionEnable(timestep);

		receiver = createReceiver("receiver");
		receiver.enable(timestep);

		gps = createGPS("gps");
		gps.enable(timestep);
		
		PolyCreateControler ctrl = this;
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				System.out.println("Shutdown hook ran!");
				ctrl.delete();
			}
		});
		
		//设置界面
		
		jf=new JFrame();
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setResizable(true);
		jf.setTitle("RobotManagement");
		
		jp =new JPanel();
		
		startMoving =new JButton("startMoving");
		startMoving.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				theFSM.raiseMoveButton();
			}
		});
		
		
		
		stopMoving =new JButton("stopMoving");
		startMoving.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				theFSM.raiseStopMoving();
			}
		});
		
		startWriting =new JButton("startWriting");
		startWriting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				theFSM.raiseWriteButton();
			}
		});
		
		
		
		stopWriting =new JButton("stopWriting");
		stopWriting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				theFSM.raiseStopWriting();
			}
		});
		
		jp.add(startMoving);
		jp.add(stopMoving);
		jp.add(startWriting);
		jp.add(stopWriting);
		
		
		jf.setContentPane(jp);
		jf.setVisible(true);
        jf.setLocationRelativeTo(null);
		
		theFSM.enter();
	}
	


	public void openGripper() {
		gripMotors[0].setPosition(0.5);//gripper伸出机器人0.5,表示夹子开始工作 单位m
		gripMotors[1].setPosition(0.5);
	}

	public void closeGripper() {
		gripMotors[0].setPosition(-0.2);//gripper收回机器人0.2,表示夹子开始工作
		gripMotors[1].setPosition(-0.2);
	}
	
	/**
	 * give the obstacle distance from the gripper sensor. max distance (i.e., no obstacle detected) is 1500
	 * @return
	 */
	public double getObjectDistanceToGripper() { //距离架子的距离
		return gripperSensor.getValue();
	}

	public boolean isThereCollisionAtLeft() {//接触传感器只有0/1，1表示撞上,0表示没有撞上
		return (leftBumper.getValue() != 0.0);
	}

	public boolean isThereCollisionAtRight() {//该功能返回接收方队列中当前存在的数据包数
		return (rightBumper.getValue() != 0.0);//功能删除头包。队列中的下一个数据包（如果有）成为新的头包
	}

	public void flushIRReceiver() {
		while (receiver.getQueueLength() > 0)//接受序列存在数据包,即表明有虚拟墙
			receiver.nextPacket();
	}

	public boolean isThereVirtualwall() {
		return (receiver.getQueueLength() > 0);
	}
	
	public void goForward() {
		leftMotor.setVelocity(MAX_SPEED);
		rightMotor.setVelocity(MAX_SPEED);
	}

	public void goBackward() {
		leftMotor.setVelocity(-HALF_SPEED);
		rightMotor.setVelocity(-HALF_SPEED);
		passiveWait(0.5);
	
	}
	
	public void backTurn() {
		goBackward();
		passiveWait(0.5);
		turn(Math.PI);
	}
	
	public void leftTurn() {

		goBackward();
		passiveWait(0.5);
		turn(Math.PI * randdouble()+0.6);
		
		
	}

	public void rightTurn() {
		
		goBackward();
		passiveWait(0.5);
		turn(-Math.PI * randdouble()+0.6);
			
		
	}
	
	public void isBump() {
		
		try {
			openGripper();
			pen.write(true);
			ledOn.set(1);
			passiveWait(0.5);
			System.out.println("let's start");
			while (true) {
				/**
				 * The position and orientation are expressed relatively to the camera (the relative position is the one of the center of the object which can differ from its origin) and the units are meter and radian.
				 * https://www.cyberbotics.com/doc/reference/camera?tab-language=python#wb_camera_has_recognition
				 */
				Node anObj = getFromDef("can"); //should not be there, only to have another orientation for testing...
				passiveWait(0.1);
				
				
				System.out.println("->  the orientation of the robot is " +Math.atan2(getSelf().getOrientation()[0], getSelf().getOrientation()[8]));
				System.out.println("    the position of the robot is " +Math.round(getSelf().getPosition()[0]*100)+";"+Math.round(getSelf().getPosition()[2]*100));

				System.out.println("    front distance: "+frontDistanceSensor.getValue());
				
				CameraRecognitionObject[] backObjs = backCamera.getRecognitionObjects();
				if (backObjs.length > 0) {
					CameraRecognitionObject obj = backObjs[0];
					int oid = obj.getId();
					double[] backObjPos = obj.getPosition();
					/**
					 * The position and orientation are expressed relatively to the camera (the relative position is the one of the center of the object which can differ from its origin) and the units are meter and radian.
					 */
					System.out.println("        I saw an object on back Camera at : "+backObjPos[0]+","+backObjPos[1]);
				}
				CameraRecognitionObject[] frontObjs = frontCamera.getRecognitionObjects();
				if (frontObjs.length > 0) {
					for(CameraRecognitionObject obj : frontObjs) {
						double[] frontObjPos = obj.getPosition();
						System.out.println("        I saw "+obj.getModel()+" on front Camera at : "+((double)Math.round(frontObjPos[1]*1000))/10+"; "+Math.round(frontObjPos[0]*180/Math.PI));
					}
				}
				System.out.println("         gripper distance sensor is "+getObjectDistanceToGripper());
				if (isThereVirtualwall()) {
					System.out.println("Virtual wall detected\n");
					theFSM.raiseSenseWall();
				} else if (isThereCollisionAtLeft() || frontLeftDistanceSensor.getValue() < 250) {

					System.out.println("          Left obstacle detected\n");
					theFSM.raiseLeftBump();
				} else if (isThereCollisionAtRight()|| frontRightDistanceSensor.getValue() < 250 ||frontDistanceSensor.getValue() < 250) {
					System.out.println("          Right obstacle detected\n");
					theFSM.raiseRightBump();
				} else {
					goForward();
				}
				flushIRReceiver();
				
				
				
				
				
			}

		}catch (Exception e) {
			delete();
		}

		

	}
	
	
	
	public void stop() {
		leftMotor.setVelocity(NULL_SPEED);
		rightMotor.setVelocity(NULL_SPEED);
	}

	public void passiveWait(double sec) {
		double start_time = getTime();
		do {
			step(timestep);
		} while (start_time + sec > getTime());
	}

	public double randdouble() {
		return  random.nextDouble();//生成一个随机的 double 值，数值介于 [0,1.0)
	}

	public void turn(double angle) {
		stop();
		double l_offset = leftSensor.getValue();//返回左传感器位置的测量值
		double r_offset = rightSensor.getValue();//返回右传感器位置的测量值
		step(timestep);
		double neg = (angle < 0.0) ? -1.0 : 1.0;
		leftMotor.setVelocity(neg * HALF_SPEED);
		rightMotor.setVelocity(-neg * HALF_SPEED);
		double orientation;
		do {
			double l = leftSensor.getValue() - l_offset;
			double r = rightSensor.getValue() - r_offset;
			double dl = l * WHEEL_RADIUS;                 // distance covered by left wheel in meter
			double dr = r * WHEEL_RADIUS;                 // distance covered by right wheel in meter
			orientation = neg * (dl - dr) / AXLE_LENGTH;  // delta orientation in radian
			step(timestep);
		} while (orientation < neg * angle);
		stop();
		step(timestep);
	}
	
	public void catchObject() {
		// TODO Auto-generated method stub
		
	}
	
	public void releaseObject() {
		// TODO Auto-generated method stub
		
	}
	
	public void searchObject() {
		// TODO Auto-generated method stub
		
	}
	
	



	/**
	 * The values are returned as a 3D-vector, therefore only the indices 0, 1, and 2 are valid for accessing the vector.
	 * The returned vector indicates the absolute position of the GPS device. This position can either be expressed in the 
	 * cartesian coordinate system of Webots or using latitude-longitude-altitude, depending on the value of the 
	 * gpsCoordinateSystem field of the WorldInfo node. The gpsReference field of the WorldInfo node can be used to define
	 * the reference point of the GPS. The wb_gps_get_speed function returns the current GPS speed in meters per second.
	 * @return
	 */
	public double[] getPosition() {
		return gps.getValues();
	}
	
	


	public static void main(String[] args) {
		PolyCreateControler controler = new PolyCreateControler();
		controler.isBump();
		}
		

	@Override
	protected void finalize() {
		this.delete();
		super.finalize();
	}



	





	



	
	

}
