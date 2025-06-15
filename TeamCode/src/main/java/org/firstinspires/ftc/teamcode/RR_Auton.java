package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RR_Auton extends LinearOpMode {
    Servo claw;
    Servo slides;

    double slides_up = 0.5;
    double slides_down = 0.76;
    double slides_pick = 0.66;
    double claw_open = 0.5;
    double claw_close = 0;

    public void set_claw_pos(double pos){
        claw.setPosition(pos);
    }

    public void set_slides_pos(double pos){
        slides.setPosition(pos);
    }

    public Action setClaw(double pos) {
        return new InstantAction(() -> {
            set_claw_pos(pos);
        });
    }

    public Action setSlides(double pos) {
        return new InstantAction(() -> {
            set_slides_pos(pos);
        });
    }

    public Action score() {
        return new SequentialAction(
                new InstantAction(() -> slides.setPosition(slides_down)),
                new SleepAction(.8),
                new InstantAction(() -> claw.setPosition(claw_open)),
//                new SleepAction(.1),
                new InstantAction(() -> slides.setPosition(slides_pick)),
                new SleepAction(.4)
        );
    }

    public Action pick() {
        return new SequentialAction(
                new InstantAction(() -> claw.setPosition(claw_close)),
                new SleepAction(.2),
                new InstantAction(() -> slides.setPosition(slides_up)),
                new SleepAction(1.5)
        );
    }




    @Override
    public void runOpMode() throws InterruptedException {
        PinpointDrive drive = new PinpointDrive(hardwareMap, new Pose2d(0, -61, Math.toRadians(90)));
        claw = hardwareMap.get(Servo.class, "claw");
        slides = hardwareMap.get(Servo.class, "slides");

//        A
        Action scorePreload = drive.actionBuilder(new Pose2d(0, -61, Math.toRadians(90)))
                .strafeTo(new Vector2d(0, -29))
                .stopAndAdd(score())
                .build();
        Action pushingSamples = drive.actionBuilder(new Pose2d(0, -34, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(32.5, -33), Math.toRadians(90))
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(48, -10), Math.toRadians(-90))
                .waitSeconds(0.05)
                .strafeTo(new Vector2d(48, -58))
//                .waitSeconds(0.05)
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(48, -10), Math.toRadians(90))//, new TranslationalVelConstraint(80))
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(58, -10), Math.toRadians(-90))
                .waitSeconds(0.05)
                .strafeTo(new Vector2d(58, -56.5)) //, new TranslationalVelConstraint(80))
//                        .strafeTo(new Vector2d(54, -5))
                .build();
        Action pickupScoreFirstSpec = drive.actionBuilder(new Pose2d(58, -57, Math.toRadians(90)))
//                .strafeTo(new Vector2d(58, -42.5))
//                .turn(Math.toRadians(180))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(36, -50, Math.toRadians(-90)), Math.toRadians(-90))
//                .waitSeconds(0.1)
                .strafeTo(new Vector2d(36, -61))
                .afterTime(0.05, pick())

                .waitSeconds(.1)
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(2, -29, Math.toRadians(90)), Math.toRadians(90))
                .stopAndAdd(score())
                .build();
        Action pickupScoreSecondSpec = drive.actionBuilder(new Pose2d(2, -29, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))

                .splineToSplineHeading(new Pose2d(40, -57, Math.toRadians(-90)), Math.toRadians(-90))
                .strafeTo(new Vector2d(40, -61))
                .afterTime(0.05, pick())

                .waitSeconds(.1)
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(4, -29, Math.toRadians(90)), Math.toRadians(90))
                .stopAndAdd(score())
                .build();
        Action pickupScoreThirdSpec = drive.actionBuilder(new Pose2d(4, -30, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToSplineHeading(new Pose2d(40, -57, Math.toRadians(-90)), Math.toRadians(-90))
                .strafeTo(new Vector2d(40, -63))
                .afterTime(0.05, pick())

//                .waitSeconds(.1)
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(8, -29.25, Math.toRadians(90)), Math.toRadians(90))
                .stopAndAdd(score())
                .build();


        claw.setPosition(claw_close);
        slides.setPosition(slides_up);

        waitForStart();

        if (isStopRequested()) return;



        Actions.runBlocking(
                new SequentialAction(
                        scorePreload,
                        pushingSamples,
                        pickupScoreFirstSpec,
                        pickupScoreSecondSpec,
                        pickupScoreThirdSpec
                )
        );
    }
}
