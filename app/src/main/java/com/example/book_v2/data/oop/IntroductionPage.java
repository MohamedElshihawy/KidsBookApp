package com.example.book_v2.data.oop;

import androidx.annotation.NonNull;

public class IntroductionPage extends Page {

    private String animalImagePath;
    private String animalSound;
    private String letterText;
    private String letterSoundPath;
    private String AnimalName;


    public IntroductionPage() {

    }


    public String getAnimalImagePath() {
        return animalImagePath;
    }

    public void setAnimalImagePath(String animalImagePath) {
        this.animalImagePath = animalImagePath;
    }

    public String getAnimalSound() {
        return animalSound;
    }

    public void setAnimalSound(String animalSound) {
        this.animalSound = animalSound;
    }

    public String getLetterText() {
        return letterText;
    }

    public void setLetterText(String letterImagePath) {
        this.letterText = letterImagePath;
    }

    public String getLetterSoundPath() {
        return letterSoundPath;
    }

    public void setLetterSoundPath(String letterSoundPath) {
        this.letterSoundPath = letterSoundPath;
    }

    public String getAnimalName() {
        return AnimalName;
    }

    public void setAnimalName(String animalName) {
        AnimalName = animalName;
    }



}
