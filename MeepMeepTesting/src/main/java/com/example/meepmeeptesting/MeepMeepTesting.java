package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();




        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -61, Math.toRadians(90)))
                .strafeTo(new Vector2d(0, -32))
                .waitSeconds(1)
//                .strafeTo(new Vector2d(0, -38))
                        .setTangent(Math.toRadians(-90))
                        .splineToConstantHeading(new Vector2d(34, -33), Math.toRadians(90))
//                .strafeTo(new Vector2d(35.8, -38))
//                .strafeTo(new Vector2d(35.8, -10))
                        .setTangent(Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(45, -10), Math.toRadians(-90))
                        .waitSeconds(0.2)
                                .strafeTo(new Vector2d(45, -58))
                        .waitSeconds(0.2)
                        .setTangent(Math.toRadians(90))
                                .splineToConstantHeading(new Vector2d(45, -10), Math.toRadians(90))
                        .setTangent(Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(53.6, -10), Math.toRadians(-90))
                                .strafeTo(new Vector2d(54, -10))
                                .strafeTo(new Vector2d(54, -57))
//                        .strafeTo(new Vector2d(54, -5))

                                .waitSeconds(1)
                        .setTangent(Math.toRadians(90))
                        .splineToSplineHeading(new Pose2d(2, -34, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(1)
                .setTangent(Math.toRadians(-90))
             
                 .splineToSplineHeading(new Pose2d(40.4, -62, Math.toRadians(-90)), Math.toRadians(-90))
                 .waitSeconds(1)

                .setTangent(Math.toRadians(90))
                 .splineToSplineHeading(new Pose2d(4, -34, Math.toRadians(90)), Math.toRadians(90))
                 .waitSeconds(1)
                .setTangent(Math.toRadians(-90)       )
                .splineToSplineHeading(new Pose2d(40.4, -62, Math.toRadians(-90)), Math.toRadians(-90))
                        .waitSeconds(1)
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(6, -34, Math.toRadians(90)), Math.toRadians(90))



//                                      .turn(Math.toRadians(180))
//                                .strafeTo(new Vector2d(0, -34))



//                                .strafeTo(new Vector2d(35.8, -38))
//                .turn(Math.toRadians(90))
//                .lineToY(30)
//                .turn(Math.toRadians(90))
//                .lineToX(0)
//                .turn(Math.toRadians(90))
//                .lineToY(0)
//                .turn(Math.toRadians(90))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}