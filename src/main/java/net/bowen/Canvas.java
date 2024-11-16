package net.bowen;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Canvas extends JPanel {
    private final World<Body> world;

    private long loopStartSysTime;
    private long deltaTime;

    public Canvas(World<Body> world) {
        this.world = world;
    }

    public void play() {
        new Thread(() -> {
            while (true) {
                loopStartSysTime = System.currentTimeMillis();
                world.step((int) deltaTime);
                repaint();
                deltaTime = System.currentTimeMillis() - loopStartSysTime;
            }
        }).start();
    }

    public void fillPolygon(Vector2 center, double angle, Polygon polygon, Color color, Graphics2D g2d) {
        Vector2[] vertices = polygon.getVertices();

        Path2D.Double p = new Path2D.Double();
        p.moveTo(vertices[0].x, vertices[0].y);
        for (int i = 1; i < vertices.length; i++) {
            p.lineTo(vertices[i].x, vertices[i].y);
        }
        p.closePath();

        g2d.translate(center.x, center.y);
        g2d.rotate(angle);
        g2d.setColor(color);
        g2d.fill(p);
        g2d.rotate(-angle);
        g2d.translate(-center.x, -center.y);
    }

    public void fillCircle(Vector2 center, Circle circle, Color color, Graphics2D g2d) {
        double r = circle.getRadius();
        Ellipse2D.Double ellipse = new Ellipse2D.Double(center.x - r, center.y - r, r * 2, r * 2);
        g2d.setColor(color);
        g2d.fill(ellipse);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, getWidth(), getHeight());

        // Transform to Descartes coordinate
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.scale(1, -1);

        for (Body body : world.getBodies()) {
            // get the updated body center
            Vector2 xy = body.getWorldCenter();
            double angle = body.getTransform().getRotationAngle();

            for (BodyFixture fixture : body.getFixtures()) {
                Convex c = fixture.getShape();

                if (c instanceof Polygon) {
                    fillPolygon(xy, angle, (Polygon) c, Color.WHITE, g2d);
                } else if (c instanceof Circle){
                    fillCircle(xy, (Circle) c, Color.WHITE, g2d);
                } else throw new UnsupportedOperationException("Unknown shape: " + c.getClass().getName());
            }
        }
    }
}
