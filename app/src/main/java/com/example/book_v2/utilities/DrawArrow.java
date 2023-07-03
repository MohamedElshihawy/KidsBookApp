package com.example.book_v2.utilities;

import android.graphics.Path;
import android.graphics.Point;

public class DrawArrow {

    public void drawLineWithArrowhead(Point p0,Point p1, int headLength) {

        Path path = new Path();
        // constants (could be declared as globals outside this function)
        double PI = Math.PI;
        double degreesInRadians225 = 225 * PI / 180;
        double degreesInRadians135 = 135 * PI / 180;

        // calc the angle of the line
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        double angle = Math.atan2(dy, dx);

        // calc arrowhead points
        int x225 = (int) ( p1.x + headLength * Math.cos(angle + degreesInRadians225));
        int y225 = (int) (p1.y + headLength * Math.sin(angle + degreesInRadians225));
        int x135 = (int) (p1.x + headLength * Math.cos(angle + degreesInRadians135));
        int y135 = (int) (p1.y + headLength * Math.sin(angle + degreesInRadians135));


        // draw the line from p0 to p1
        path.moveTo(p0.x, p0.y);
        path.lineTo(p1.x, p1.y);
        // draw partial arrowhead at 225 degrees
        path.moveTo(p1.x, p1.y);
        path.lineTo(x225, y225);
        // draw partial arrowhead at 135 degrees
        path.moveTo(p1.x, p1.y);
        path.lineTo(x135, y135);
    }
}
