package net.bowen.physics;

import net.bowen.physics.bodies.Chain;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main extends JFrame {
    private Main() {
        setTitle("Physics Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });
        setSize(800, 600);

        Canvas canvas = new Canvas(getWorld());
        getContentPane().add(canvas);
        setVisible(true);
        SwingUtilities.invokeLater(canvas::play);

    }

    private World<Body> getWorld() {
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

    public static void main(String[] args) {
        new Main();
    }
}