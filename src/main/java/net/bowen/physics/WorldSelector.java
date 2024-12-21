package net.bowen.physics;

import javax.swing.*;
import java.awt.*;

public class WorldSelector extends JFrame {
    public WorldSelector() {
        setTitle("World Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new FlowLayout());

        JButton defaultWorldBtn = new JButton("Default World");
        defaultWorldBtn.addActionListener(actionEvent -> new Main(Worlds.getWorldByIndex(0)));

        JButton chainAroundCircleWorldBtn = new JButton("Chain Around Circle World");
        chainAroundCircleWorldBtn.addActionListener(actionEvent -> new Main(Worlds.getWorldByIndex(1)));

        add(defaultWorldBtn);
        add(chainAroundCircleWorldBtn);

        setVisible(true);
    }
}
