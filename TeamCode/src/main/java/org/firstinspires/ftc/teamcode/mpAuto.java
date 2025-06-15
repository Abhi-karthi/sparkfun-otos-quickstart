package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class mpAuto extends LinearOpMode {
    DcMotor left_front;
    DcMotor left_back;// = hardwareMap.get(DcMotor.class, "left_back");
    DcMotor right_front;// = hardwareMap.get(DcMotor.class, "right_front");
    DcMotor right_back;// = hardwareMap.get(DcMotor.class, "right_back");
    Servo claw;
    Servo slides;
    ElapsedTime timer = new ElapsedTime();

    double slides_up = 0.5;
    double slides_down = 0.76;
    double get_spec = 0.66;
    double claw_open = 0.5;
    double claw_close = 0;
    public void straight(int t, double p) {
        // positive is forward
        // negative is backward
        left_front.setPower(p);
        left_back.setPower(p);
        right_front.setPower(p);
        right_back.setPower(p);

        sleep(t);

        left_front.setPower(0);
        left_back.setPower(0);
        right_front.setPower(0);
        right_back.setPower(0);
    }
    public void strafe(int t, double p) {
        // positive is right
        // negative is left
        left_front.setPower(-p);
        left_back.setPower(p);
        right_front.setPower(p);
        right_back.setPower(-p);

        sleep(t);

        left_front.setPower(0);
        left_back.setPower(0);
        right_front.setPower(0);
        right_back.setPower(0);
    }
    public void turn(int t, double p) {
        // positive is right
        // negative is left
        left_front.setPower(p);
        left_back.setPower(p);
        right_front.setPower(-p);
        right_back.setPower(-p);

        sleep(t);

        left_front.setPower(0);
        left_back.setPower(0);
        right_front.setPower(0);
        right_back.setPower(0);
    }

    public void set_claw_pos(double pos){
        claw.setPosition(pos);
    }

    public void set_slides_pos(double pos){
        slides.setPosition(pos);
    }

    public void score () {
        set_slides_pos(slides_down);
        sleep(1000);
        set_claw_pos(claw_open);
        sleep(100);
        set_slides_pos(get_spec);
        sleep(400);
    }

    public void pick() {
        claw.setPosition(claw_close);
        sleep(200);
        slides.setPosition(slides_up);
        sleep(750);
    }


    @Override
    public void runOpMode() throws InterruptedException {
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        right_back = hardwareMap.get(DcMotor.class, "right_back");

        claw = hardwareMap.get(Servo.class, "claw");
        slides = hardwareMap.get(Servo.class, "slides");

        left_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.REVERSE);

        left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pick();

        waitForStart();

//        left_front.setPower(0.5);
//        left_back.setPower(0.5);
//        right_front.setPower(0.5);
//        right_back.setPower(0.5);


        straight(975, 0.4);
        score();
//        sleep(1000);


        straight(180, -0.3);

        strafe(1950, -0.3);
        straight(1000, 0.3);
        strafe(800, -0.3);
        straight(2650, -0.3);


        straight(2500, 0.3);
        strafe(750, -0.3);
        straight(2600, -0.3);
        straight(240, -0.3);
        turn(1600, 0.3);
        straight(500, 0.3);
        pick();
//        strafe(1000, -0.3);
////
//        straight(3400, -0.3);
//        straight(3400, 0.3);
//        strafe(750, -0.3);
//        straight(3400, -0.3);
//        straight(3400, 0.3);




        telemetry.addData("Startedtatus", "Started");
        telemetry.update();
    }
}
