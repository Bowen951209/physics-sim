package net.bowen;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.World;

import javax.swing.*;
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

        // Falling circle
        Body circle = new Body();
        BodyFixture circleFixture = new BodyFixture(Geometry.createCircle(10));
        circle.addFixture(circleFixture);
        circle.translate(0, 0);
        circle.setMass(MassType.NORMAL);
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