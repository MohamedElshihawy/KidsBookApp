package com.example.book_v2.data.models;

import java.io.Serializable;

public class Circle implements Serializable {

    private int x;
    private int y;
    private int circleCount;
    private int radius;
    private int distance;

    public Circle(int x, int y, int circleCount, int radius, int distance) {
        this.x = x;
        this.y = y;
        this.circleCount = circleCount;
        this.radius = radius;
        this.distance = distance;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCircleCount() {
        return circleCount;
    }

    public void setCircleCount(int circleCount) {
        this.circleCount = circleCount;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
