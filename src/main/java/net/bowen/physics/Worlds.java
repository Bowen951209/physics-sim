package net.bowen.physics;

import net.bowen.physics.bodies.Chain;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

import java.awt.*;

public class Worlds {
    public static World<Body> getWorldByIndex(int worldIndex) {
        World<Body> world;
        switch (worldIndex) {
            case 0 -> {
                System.out.println("Default world loaded.");
                world = defaultWorld();
            }
            case 1 -> {
                System.out.println("Chain around circle world loaded.");
                world = chainAroundCircle();
            }
            case 2 -> {
                System.out.println("Radiation with gravity world loaded.");
                world = radiationWithGravity();
            }
            default -> throw new IllegalArgumentException("Invalid world index.");
        }

        return world;
    }

    public static World<Body> defaultWorld() {
        World<Body> world = new World<>();

        // Static circle
        Body staticCircle = new Body();
        BodyFixture staticCircleFixture = new BodyFixture(Geometry.createCircle(5));
        staticCircle.addFixture(staticCircleFixture);
        staticCircle.translate(0, 50);
        staticCircle.setMass(MassType.INFINITE);
        staticCircle.setUserData(UserData.create(Color.PINK));
        world.addBody(staticCircle);

        // Pendulum circle
        Body pendulum = new Body();
        BodyFixture pendulumFixture = new BodyFixture(Geometry.createCircle(5));
        pendulum.addFixture(pendulumFixture);
        pendulum.translate(30, 50);
        pendulum.setMass(MassType.NORMAL);
        pendulum.setUserData(UserData.create(new TrackTracer(500, 2)));
        world.addBody(pendulum);

        // Pendulum joint
        RevoluteJoint<Body> pendulumJoint = new RevoluteJoint<>(pendulum, staticCircle, staticCircle.getWorldCenter());
        pendulumJoint.setUserData(UserData.create(Color.CYAN));
        world.addJoint(pendulumJoint);

        Chain chain = new Chain(new Vector2(-100, 150), new Vector2(100, 150), 20, 0.8, 2);
        chain.addToWorld(world);

        // Falling circle
        Body circle = new Body();
        BodyFixture circleFixture = new BodyFixture(Geometry.createCircle(10));
        circleFixture.setRestitution(1);
        circle.addFixture(circleFixture);
        circle.translate(0, 0);
        circle.setMass(MassType.NORMAL);
        circle.setUserData(UserData.create(new TrackTracer(500, 2)));
        world.addBody(circle);

        // Falling rect
        Body rect = new Body();
        BodyFixture rectFixture = new BodyFixture(Geometry.createRectangle(10, 10));
        rect.addFixture(rectFixture);
        rect.translate(7, 12);
        rect.setMass(MassType.NORMAL);
        rect.setAngularVelocity(0.2);
        rect.setGravityScale(0.1);
        world.addBody(rect);

        // Ground
        Body ground = new Body();
        ground.addFixture(Geometry.createRectangle(100, 10));
        ground.translate(0, -80);
        ground.setMass(MassType.INFINITE);
        world.addBody(ground);

        return world;
    }

    public static World<Body> chainAroundCircle() {
        World<Body> world = new World<>();

        // Static circle
        Body staticCircle = new Body();
        BodyFixture staticCircleFixture = new BodyFixture(Geometry.createCircle(50));
        staticCircle.addFixture(staticCircleFixture);
        staticCircle.translate(0, 0);
        staticCircle.setMass(MassType.INFINITE);
        staticCircle.setUserData(UserData.create(Color.WHITE));
        world.addBody(staticCircle);

        // Chain
        Chain chain = new Chain(new Vector2(0, 50), new Vector2(400, 50), 50, 4, 4);
        chain.addToWorld(world);

        // Pin the chain head
        Body chainHead = chain.getBody(0);
        chainHead.setMass(MassType.INFINITE);

        // Pin a pendulum to chain tail
        Body chainTail = chain.getBody(49);
        Body pendulum = new Body();
        BodyFixture pendulumFixture = new BodyFixture(Geometry.createCircle(5));
        pendulum.addFixture(pendulumFixture);
        pendulum.translate(400, 50);
        pendulum.setMass(MassType.NORMAL);
        pendulum.setUserData(UserData.create(new TrackTracer(800, 2)));
        world.addBody(pendulum);

        // Pendulum joint
        WeldJoint<Body> weldJoint = new WeldJoint<>(pendulum, chainTail, chainTail.getWorldCenter());
        world.addJoint(weldJoint);


        return world;
    }

    public static World<Body> radiationWithGravity() {
        World<Body> world = new World<>();

        int numBalls = 100;
        for (int i = 0; i < numBalls; i++) {
            Body ball = new Body();
            BodyFixture ballFixture = new BodyFixture(Geometry.createCircle(5));
            ballFixture.setFilter(filter -> false);// No collisions allowed
            ball.addFixture(ballFixture);
            ball.translate(0, 0);
            ball.setMass(MassType.NORMAL);

            double theta = Math.PI * 2 / numBalls * i;
            Vector2 impulse = new Vector2(Math.cos(theta), Math.sin(theta)).multiply(3000);
            ball.applyImpulse(impulse);

            ball.setUserData(UserData.create(new TrackTracer(250, 2)));
            world.addBody(ball);
        }

        return world;
    }
}
