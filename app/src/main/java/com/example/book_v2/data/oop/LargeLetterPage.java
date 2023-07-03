package com.example.book_v2.data.oop;


import com.example.book_v2.data.models.Circle;

import java.util.List;

public class LargeLetterPage extends Page {

    private String letter;
    private String largeLetterIndexesFile;
    private String mediumLetterIndexesFile;
    private String smallLetterIndexesFile;
    private String letterSoundPath;
    private String animalImagePath;
    private String animalSoundPath;

    private List<Circle> largeLetterRepresentation;
    private List<Circle> mediumLetterRepresentation;
    private List<Circle> smallLetterRepresentation;


    public LargeLetterPage() {

    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getLargeLetterIndexesFile() {
        return largeLetterIndexesFile;
    }

    public void setLargeLetterIndexesFile(String largeLetterIndexesFile) {
        this.largeLetterIndexesFile = largeLetterIndexesFile;
    }

    public String getMediumLetterIndexesFile() {
        return mediumLetterIndexesFile;
    }

    public void setMediumLetterIndexesFile(String mediumLetterIndexesFile) {
        this.mediumLetterIndexesFile = mediumLetterIndexesFile;
    }

    public String getSmallLetterIndexesFile() {
        return smallLetterIndexesFile;
    }

    public void setSmallLetterIndexesFile(String smallLetterIndexesFile) {
        this.smallLetterIndexesFile = smallLetterIndexesFile;
    }

    public String getLetterSoundPath() {
        return letterSoundPath;
    }

    public void setLetterSoundPath(String letterSoundPath) {
        this.letterSoundPath = letterSoundPath;
    }

    public String getAnimalImagePath() {
        return animalImagePath;
    }

    public void setAnimalImagePath(String animalImagePath) {
        this.animalImagePath = animalImagePath;
    }

    public String getAnimalSoundPath() {
        return animalSoundPath;
    }

    public void setAnimalSoundPath(String animalSoundPath) {
        this.animalSoundPath = animalSoundPath;
    }

    public List<Circle> getLargeLetterRepresentation() {
        return largeLetterRepresentation;
    }

    public void setLargeLetterRepresentation(List<Circle> largeLetterRepresentation) {
        this.largeLetterRepresentation = largeLetterRepresentation;
    }

    public List<Circle> getMediumLetterRepresentation() {
        return mediumLetterRepresentation;
    }

    public void setMediumLetterRepresentation(List<Circle> mediumLetterRepresentation) {
        this.mediumLetterRepresentation = mediumLetterRepresentation;
    }

    public List<Circle> getSmallLetterRepresentation() {
        return smallLetterRepresentation;
    }

    public void setSmallLetterRepresentation(List<Circle> smallLetterRepresentation) {
        this.smallLetterRepresentation = smallLetterRepresentation;
    }
}
