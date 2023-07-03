package com.example.book_v2.data.oop;

import java.nio.file.Path;
import java.util.List;

public class FreeMatchingPage extends Page {

    private String viewSize;
    private String LineType;
    private List<Path> userDrawings;
    private boolean rotateShape =false;
    private String orientation;
    private int numOfLines;

    private String ViewType;

    // if the view is grid layout this will decide the number or columns
    private int numOfColumns;


    FreeMatchingPage() {

    }

    public String getViewSize() {
        return viewSize;
    }

    public void setViewSize(String viewSize) {
        this.viewSize = viewSize;
    }

    public String getLineType() {
        return LineType;
    }

    public void setLineType(String lineType) {
        LineType = lineType;
    }

    public List<Path> getUserDrawings() {
        return userDrawings;
    }

    public void setUserDrawings(List<Path> userDrawings) {
        this.userDrawings = userDrawings;
    }

    public boolean isRotateShape() {
        return rotateShape;
    }

    public void setRotateShape(boolean rotateShape) {
        this.rotateShape = rotateShape;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getNumOfLines() {
        return numOfLines;
    }

    public void setNumOfLines(int numOfLines) {
        this.numOfLines = numOfLines;
    }

    public String getViewType() {
        return ViewType;
    }

    public void setViewType(String viewType) {
        ViewType = viewType;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public void setNumOfColumns(int numOfColumns) {
        this.numOfColumns = numOfColumns;
    }
}
