package org.vafifteen;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HebFrame extends JPanel{
    private int[][][] adjacencyMatrix;
    // This is a mapping from the email to the user's name and ID
    private final Map<String, Map<String, Object>> emailNameIdMap = new HashMap<>();
    private int radius;

    public HebFrame(String jsonString) {
        readDataFromJsonString(jsonString);
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, y);
        if (x > y) {
            this.radius = (y / 2) - 15;
        } else {
            this.radius = (x / 2) - 15;
        }
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        if (d.width > d.height) {
            this.radius = (d.height / 2) - 15;
        } else {
            this.radius = (d.width / 2) - 15;
        }
    }

    private void readDataFromJsonString(String jsonString) {
        // Takes a json string and turns it into a hashmap
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(jsonString, Map.class);

        // Has the user's email as the key
        // Has a map as value with keys "Name", "Id", "Department, "DeptId"
        Map<String, Map<String, Object>> userData = (Map<String, Map<String, Object>>) data.get("emailNameIdMap");

        float angleSeparation = (float) (Math.PI * 2.0) / (float) userData.size();
        for (String userEmail: userData.keySet()) {
            // Process the raw string input into the correct types and add the angle that user should be placed at.
            String name = (String) userData.get(userEmail).get("Name");
            int id;
            try {
                id = (int) Double.parseDouble((String) userData.get(userEmail).get("Id"));
            } catch (ClassCastException e) {
                id = ((Double) userData.get(userEmail).get("Id")).intValue();
            }
            String department = (String) userData.get(userEmail).get("Department");
            int deptId = ((Double) userData.get(userEmail).get("DeptId")).intValue();
            float angle = (float) id * angleSeparation;

            // Creates the new mapping
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("Name", name);
            userInfo.put("Id", id);
            userInfo.put("Department", department);
            userInfo.put("DeptId", deptId);
            userInfo.put("Angle", angle);

            this.emailNameIdMap.put(userEmail, userInfo);
        }

        // Process edges by first getting the raw string representation
        Map<String, Map<String, Map<String, Double>>> adjMatrix = (Map<String, Map<String, Map<String, Double>>>) data.get("edges");

        ArrayList<Double> shape = (ArrayList<Double>) data.get("edgesShape");

        // Actually give a shape for the adj matrix
        this.adjacencyMatrix = new int[shape.get(0).intValue()][shape.get(1).intValue()][shape.get(2).intValue()];

        // Parse the initial map into an array for the adjacency matrix
        for (String i: adjMatrix.keySet()) {
            int i_int = Integer.parseInt(i);
            for (String j: adjMatrix.get(i).keySet()) {
                int j_int = Integer.parseInt(j);
                for (String k: adjMatrix.get(i).get(j).keySet()) {
                    int k_int = Integer.parseInt(k);
                    this.adjacencyMatrix[i_int][j_int][k_int] = adjMatrix.get(i).get(j).get(k).intValue();
                }
            }
        }
    }

    private int[] polarToCartesian(float r, float theta) {
        // Converts polar coordinates to cartesian centered at the center of the panel
        int[] origin = new int[]{this.getSize().width / 2, this.getSize().height / 2};
        int[] result = new int[2];
        result[0] = (int) (r * Math.cos(theta)) + origin[0];
        result[1] = (int) (r * Math.sin(theta)) + origin[1];
        return result;
    }

    private void drawCircleFromCenter(int centerX, int centerY, int r, Graphics2D g2d) {
        // Draws a circle from a given center with radius r
        g2d.fillOval(centerX - r, centerY - r, r * 2, r * 2);
    }

    public void paint(Graphics g) {
        // Draws a cubic curve
        Graphics2D g2d = (Graphics2D) g;

//        CubicCurve2D curve = new CubicCurve2D.Double(100, 100, 100, 150, 300, 150, 300, 100);
//        // Make curve thicker
//        g2d.setStroke(new BasicStroke(5));
//
        // Make things anti-aliased
        RenderingHints rhints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.draw(curve);
        for (String email:this.emailNameIdMap.keySet()) {
            Map<String, Object> data = this.emailNameIdMap.get(email);
            int[] center = polarToCartesian(this.radius, (float) data.get("Angle"));
            drawCircleFromCenter(center[0], center[1], 4, g2d);
        }
        // TODO: Add arcs
    }
}
