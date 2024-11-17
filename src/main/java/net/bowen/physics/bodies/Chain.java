package net.bowen.physics.bodies;

import net.bowen.physics.UserData;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chain {
    private final List<Body> rectangles;
    private final List<Joint<Body>> joints;

    public Chain(Vector2 head, Vector2 tail, int numSegments, double segmentLength, double segmentThickness) {
        this.rectangles = new ArrayList<>(numSegments);
        this.joints = new ArrayList<>(numSegments - 1);
        Vector2 directionStep = new Vector2(tail).subtract(head).divide(numSegments);
        Vector2 rectCenter = new Vector2(head);

        // Add segment bodies
        for (int i = 0; i < numSegments; i++) {
            Body rect = new Body();
            BodyFixture fixture = new BodyFixture(Geometry.createRectangle(segmentLength, segmentThickness));
            rect.addFixture(fixture);

            rect.translate(rectCenter.add(directionStep));
            rect.setMass(MassType.NORMAL);

            rectangles.add(rect);
        }

        // Join the bodies
        for (int i = 0; i < numSegments - 1; i++) {
            Body bodyA = rectangles.get(i);
            Body bodyB = rectangles.get(i + 1);
            Joint<Body> joint = new DistanceJoint<>(bodyA, bodyB, bodyA.getWorldCenter(), bodyB.getWorldCenter());
            joint.setUserData(UserData.create(Color.CYAN));
            joints.add(joint);
        }
    }

    public void addToWorld(World<Body> world) {
        rectangles.forEach(world::addBody);
        joints.forEach(world::addJoint);
    }
}
