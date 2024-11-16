package net.bowen;

import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class TrackTracer extends ArrayList<Vector2> {
    private final double pointRadius;
    private final int maxPoints;

    public TrackTracer(int maxPoints, double pointRadius) {
        super(maxPoints);
        this.maxPoints = maxPoints;
        this.pointRadius = pointRadius;
    }

    @Override
    public boolean add(Vector2 vector2) {
        if (size() == maxPoints) {
            remove(0);
        }
        return super.add(vector2);
    }

    public void drawPoints(Color color, Graphics2D g2d) {
        g2d.setColor(color);
        for (Vector2 point : this) {
            Path2D.Double p = new Path2D.Double();
            Ellipse2D.Double ellipse = new Ellipse2D.Double(
                    point.x - pointRadius,
                    point.y - pointRadius,
                    pointRadius, pointRadius
            );
            g2d.fill(ellipse);
        }
    }
}
