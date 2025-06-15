package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Atele")
public class MecanumTeleOp extends LinearOpMode {
    public enum Robot {
        BASE,
        SCORE_SLIDES_DOWN,
        SCORE_CLAW,
        SCORE_SLIDES_UP,
        PICK_CLAW,
        PICK_SLIDES
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
//        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
//        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
//        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
//        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, "left_front");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "left_back");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "right_front");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "right_back");

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        double slides_up = 0.5;
        double slides_down = 0.76;
        double slides_pick = 0.66;
        double claw_open = 0.5;
        double claw_close = 0;

        Servo claw = hardwareMap.get(Servo.class, "claw");
        Servo slides = hardwareMap.get(Servo.class, "slides");

        ElapsedTime timer = new ElapsedTime();
        Robot state = Robot.BASE;

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        claw.setPosition(claw_open);
        slides.setPosition(slides_pick);

        waitForStart();
        timer.reset();


        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.back) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            if (gamepad1.right_trigger > 0.1) {
                frontLeftPower *= 0.3;
                backLeftPower *= 0.3;
                frontRightPower *= 0.3;
                backRightPower *= 0.3;
            } else {
                frontLeftPower *= 0.8;
                backLeftPower *= 0.8;
                frontRightPower *= 0.8;
                backRightPower *= 0.8;
            }

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);


            switch (state) {
                case BASE:
                    if (gamepad1.left_bumper) {
                        state = Robot.SCORE_SLIDES_DOWN; // start scoring
                        timer.reset();
                    } else if (gamepad1.right_bumper) {
                        state = Robot.PICK_CLAW; // start picking
                        timer.reset();
                    }
                    break;
                case SCORE_SLIDES_DOWN:
                    slides.setPosition(slides_down);;
                    if (timer.seconds() > 0.8) {
                        state = Robot.SCORE_CLAW;
                        timer.reset();
                    }
                    break;
                case SCORE_CLAW:
                    claw.setPosition(claw_open);
                    if (timer.seconds() > 0.1) {
                        state = Robot.SCORE_SLIDES_UP;
                        timer.reset();
                    }
                    break;
                case SCORE_SLIDES_UP:
                    slides.setPosition(slides_pick);
                    if (timer.seconds() > 0.4) {
                        state = Robot.BASE;
                        timer.reset();
                    }
                    break;
                case PICK_CLAW:
                    claw.setPosition(claw_close);
                    if (timer.seconds() > 0.2) {
                        state = Robot.PICK_SLIDES;
                        timer.reset();
                    }
                    break;
                case PICK_SLIDES:
                    slides.setPosition(slides_up);
                    if (timer.seconds() > 1.5) {
                        state = Robot.BASE;
                        timer.reset();
                    }
                    break;
            }
        }
    }
}