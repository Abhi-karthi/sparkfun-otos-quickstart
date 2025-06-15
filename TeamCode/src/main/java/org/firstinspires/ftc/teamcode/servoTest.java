package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class servoTest extends LinearOpMode {
    Servo servo;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, "servo");
        waitForStart();
        if (isStopRequested()) {
            return;
        }
        timer.reset();

        while (opModeIsActive()) {
            if (timer.milliseconds() > 300) {
                if (gamepad1.dpad_up) {
                    servo.setPosition(servo.getPosition() + 0.1);
                } else if (gamepad1.dpad_down) {
                    servo.setPosition(servo.getPosition() - 0.1);
                } else if (gamepad1.dpad_left) {
                    servo.setPosition(servo.getPosition() + 0.01);
                } else if (gamepad1.dpad_right) {
                    servo.setPosition(servo.getPosition() - 0.01);
                }
                telemetry.addData("Position", servo.getPosition());
                telemetry.update();
                timer.reset();
            }
        }
    }
}
