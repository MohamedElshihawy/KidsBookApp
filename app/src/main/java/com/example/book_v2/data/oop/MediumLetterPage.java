package com.example.book_v2.data.oop;

import com.example.book_v2.data.models.Circle;

import java.util.List;

public class MediumLetterPage extends Page {


    private List<Circle> mediumLetterRepresentation;
    private String letterSoundPath;
    private String animalImagePath;
    private String animalSoundPath;
    private String letter;
    private String mediumLetterIndexesFile;

    public List<Circle> getMediumLetterRepresentation() {
        return mediumLetterRepresentation;
    }

    public void setMediumLetterRepresentation(List<Circle> mediumLetterRepresentation) {
        this.mediumLetterRepresentation = mediumLetterRepresentation;
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

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getMediumLetterIndexesFile() {
        return mediumLetterIndexesFile;
    }

    public void setMediumLetterIndexesFile(String mediumLetterIndexesFile) {
        this.mediumLetterIndexesFile = mediumLetterIndexesFile;
    }
}
