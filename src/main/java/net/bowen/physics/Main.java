package net.bowen.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.world.World;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main extends JFrame {
    private Main(World<Body> world) {
        setTitle("Physics Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });
        setSize(1200, 800);

        Canvas canvas = new Canvas(world);
        getContentPane().add(canvas);
        setVisible(true);
        SwingUtilities.invokeLater(canvas::play);

    }

    public static void main(String[] args) {
        if (args.length > 0) {
            new Main(Worlds.getWorldByIndex(Integer.parseInt(args[0])));
        } else new Main(Worlds.defaultWorld());
    }
}