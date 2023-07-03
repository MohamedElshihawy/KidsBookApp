package com.example.book_v2.utilities;

import com.example.book_v2.data.models.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawLetters {

    public static ArrayList<Circle> getAllPointsFromFile(String fileName) {
        ArrayList<Circle> circlesPoints = new ArrayList<>();
        int xCenter, yCenter, circleCount, r, distance;
        String[] letter, line;
        String letterFromTxt = ReadFiles.openTxtFile(fileName);
        letter = letterFromTxt.split(";");
        for (String s : letter) {
            line = s.split("\\s+");
            xCenter = Math.round(Float.parseFloat(line[0].substring(line[0].indexOf("=") + 1)));
            yCenter = Math.round(Float.parseFloat(line[1].substring(line[1].indexOf("=") + 1)));
            r = Math.round(Float.parseFloat(line[2].substring(line[2].indexOf("=") + 1)));
            circleCount = Integer.parseInt(line[3].substring(line[3].indexOf("=") + 1));
            distance = Integer.parseInt(line[4].substring(line[4].indexOf("=") + 1));
            circlesPoints.add(new Circle(xCenter, yCenter, circleCount, r, distance));
        }
        return circlesPoints;
    }


    public static List<Circle> calculateCirclesPositionsInPanel(int canvasWidth, int canvasHeight, List<Circle> circleList) {
        List<Integer> xPoints = new ArrayList<>(), yPoints = new ArrayList<>();

        float minX;
        float minY;
        float maxX;
        float maxY;
        int letterWidth;
        int letterHeight;

        for (Circle circle : circleList) {
            xPoints.add(circle.getX());
            yPoints.add(circle.getY());
        }
        minX = Collections.min(xPoints);
        maxX = Collections.max(xPoints);
        minY = Collections.min(yPoints);
        maxY = Collections.max(yPoints);

        letterWidth = (int) (maxX - minX);
        letterHeight = (int) (maxY - minY);
        int d;
        if (canvasHeight < letterHeight) {
            d = canvasHeight / circleList.size();
            repositionPointsInSmallScreen(circleList, d);
        } else if (canvasWidth < letterWidth) {
            d = canvasWidth / circleList.size();
            repositionPointsInSmallScreen(circleList, d);
        }
        for (Circle circle : circleList) {
            xPoints.clear();
            yPoints.clear();
            xPoints.add(circle.getX());
            yPoints.add(circle.getY());
        }
        minX = Collections.min(xPoints);
        maxX = Collections.max(xPoints);
        minY = Collections.min(yPoints);
        maxY = Collections.max(yPoints);

        letterWidth = (int) (maxX - minX);
        letterHeight = (int) (maxY - minY);

        float letterStartW = (float) ((canvasWidth - letterWidth) / 2);
        float letterStartH = (float) ((canvasHeight - letterHeight) / 2);

        for (int i = 0; i < xPoints.size(); i++) {
            xPoints.set(i, Math.round((xPoints.get(i) - minX) + letterStartW));
            yPoints.set(i, Math.round((yPoints.get(i) - minY) + letterStartH));
        }
        for (int i = 0; i < circleList.size(); i++) {
            Circle circle = circleList.get(i);
            circle.setX(xPoints.get(i));
            circle.setY(yPoints.get(i));
            circleList.set(i, circle);
        }

        return circleList;
    }

    private static void repositionPointsInSmallScreen(List<Circle> points, int d) {

        int xDiff;
        int yDiff;
        Circle nextCircle;
        for (int i = 0; i < points.size(); i++) {
            if (i != points.size() - 1) {
                nextCircle = points.get(i + 1);
                xDiff = nextCircle.getX() - points.get(i).getX();
                yDiff = nextCircle.getY() - points.get(i).getY();
                float slope = (float) yDiff / xDiff;
                int newX = (int) (d * (1 / (Math.sqrt(1 + Math.pow(slope, 2)))) + points.get(i).getX());
                int newY = (int) (d * (slope / (Math.sqrt(1 + Math.pow(slope, 2)))) + points.get(i).getY());
                nextCircle.setX(newX);
                nextCircle.setY(newY);
                points.set(i + 1, nextCircle);
            }
        }
    }


    public static List<Circle> ShiftPointsInScreen(int canvasWidth, int canvasHeight, ArrayList<Circle> circleList) {
        List<Integer> xPoints = new ArrayList<>(), yPoints = new ArrayList<>();

        float minX;
        float minY;
        float maxX;
        float maxY;
        int letterWidth;
        int letterHeight;

        for (Circle circle : circleList) {
            xPoints.add(circle.getX());
            yPoints.add(circle.getY());
        }
        minX = Collections.min(xPoints);
        maxX = Collections.max(xPoints);
        minY = Collections.min(yPoints);
        maxY = Collections.max(yPoints);

        letterWidth = (int) (maxX - minX);
        letterHeight = (int) (maxY - minY);

        float letterStartW = (float) ((canvasWidth - letterWidth) / 2);
        float letterStartH = (float) ((canvasHeight - letterHeight) / 2);

        for (int i = 0; i < xPoints.size(); i++) {
            xPoints.set(i, Math.round((xPoints.get(i) - minX) + letterStartW));
            yPoints.set(i, Math.round((yPoints.get(i) - minY) + letterStartH));
        }
        for (int i = 0; i < circleList.size(); i++) {
            Circle circle = circleList.get(i);
            circle.setX(xPoints.get(i));
            circle.setY(yPoints.get(i));
            circleList.set(i, circle);
        }

        return circleList;
    }


}
