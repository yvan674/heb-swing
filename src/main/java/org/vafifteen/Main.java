package org.vafifteen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String data = null;
        try {
            File file = new File("/Users/Yvan/Git/VA_Project/src/main/data/gastech_data/data/adjacency.json");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                data = reader.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error occured");
            e.printStackTrace();
        }

        HebFrame hebFrame = new HebFrame(data);
        hebFrame.setSize(750, 750);

        JFrame frame = new JFrame("HEB Frame Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(1, 1));
        frame.add(hebFrame);
    }
}