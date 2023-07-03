package com.example.book_v2.data.oop;


import android.util.Log;

import com.example.book_v2.data.models.MetaData;
import com.example.book_v2.ui.activities.BookCoverActivity;
import com.example.book_v2.utilities.ReadFiles;

import java.util.ArrayList;
import java.util.List;

public class FactoryBuilder {


    static final String TAG = "page builder";

    public static List<Page> BuildBookPages(List<String> pagesContent) {
        List<Page> bookPages = new ArrayList<>();
        List<MetaData> allBookPagesData = ParsePagesContent(pagesContent);
        for (MetaData pageData : allBookPagesData) {
            bookPages.add(BuildPage(pageData));
        }
        return bookPages;
    }

    // test stopped here and it was a success


    private static List<MetaData> ParsePagesContent(List<String> pagesMetaDataPaths) {
        List<MetaData> pagesDataParsed = new ArrayList<>();

        for (String pageMetaDataContent : pagesMetaDataPaths) {
            String[] pagesContent = ReadFiles.openTxtFile(pageMetaDataContent).split(";");
            String[] keys = new String[pagesContent.length];
            String[] values = new String[pagesContent.length];
            String[] paths = new String[pagesContent.length];
            MetaData pageMetData = new MetaData();
            for (int i = 0; i < pagesContent.length; i++) {

                int dataIndex = pagesContent[i].indexOf('=');
                String key = pagesContent[i].substring(0, dataIndex);
                String value = pagesContent[i].substring(dataIndex + 1).trim();
                //pagesContent[i] = pageMetaDataContent.concat(value);
                String path = BookCoverActivity.Companion.getDATA_PATH() + value;
                keys[i] = key;
                values[i] = value;
                paths[i] = path;
            }
            pageMetData.setKey(keys);
            pageMetData.setValue(values);
            pageMetData.setPath(paths);

            pagesDataParsed.add(pageMetData);
        }

        return pagesDataParsed;
    }


    private static Page BuildPage(MetaData pageData) {
        Page page = null;

        Log.d(TAG, "BuildPage: " + pageData.getValue()[0]);
        switch (pageData.getValue()[0]) {
            case "Large Letter Page":
                page = new LargeLetterPage();
                createLargeLetterPage((LargeLetterPage) page, pageData);
                break;
            case "Medium Letter Page":
                page = new MediumLetterPage();
                createMediumLetterPage((MediumLetterPage) page, pageData);
                break;
            case "Small Letter Page":
                page = new SmallLetterPage();
                createSmallLetterPage((SmallLetterPage) page, pageData);
                break;
            case "Coloring Page":
                page = new ColoringPage();
                createColoringPage((ColoringPage) page, pageData);
                break;
            case "Introduction Page":
                page = new IntroductionPage();
                createIntroPage((IntroductionPage) page, pageData);
                break;
            case "Free Matching Page":
                page = new FreeMatchingPage();
                createFreeMatchingPage((FreeMatchingPage) page, pageData);
                break;
            case "Constrained Matching Page":
                page = new ConstrainedMatchingPage();
                createConstrainedMatchingPage((ConstrainedMatchingPage) page, pageData);
                break;
            case "Letter First Preview Page":
                page = new LetterFirstPreviewPage();
                createLetterFirstPreviewPage((LetterFirstPreviewPage) page, pageData);
                break;
            case "Letter Second Preview Page":
                page = new LetterSecondPreviewPage();
                createLetterSecondPreviewPage((LetterSecondPreviewPage) page, pageData);
                break;
            case "Letter Pronunciation Page":
                page = new LetterPronunciationPage("", "", "");
                createLetterPronunciationPage((LetterPronunciationPage) page, pageData);
                break;
            case "Story Telling Page":
                page = new StoryTellingPage("", "", "");
                createStoryTellingPage((StoryTellingPage) page, pageData);
                break;
            case "Letter Prediction":
                page = new LetterPredictionPage("", "");
                createLetterPredictionPage((LetterPredictionPage) page, pageData);
                break;
            default:
                System.out.println("Error Parsing Page Data ");
        }
        return page;
    }

    private static void createLetterPredictionPage(LetterPredictionPage page, MetaData pageData) {
        page.setPageType(PageTypes.LETTER_PREDICTION_PAGE);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setTitle(pageData.getValue()[1]);
        page.setLetter(pageData.getValue()[3]);
        page.setTextToVoice(pageData.getValue()[4]);
    }

    private static void createStoryTellingPage(StoryTellingPage page, MetaData pageData) {
        page.setPageType(PageTypes.STORY_TELLING_PAGE);
        page.setPageNum(Short.parseShort(pageData.getValue()[1]));
        page.setTitle(pageData.getValue()[2]);
        page.setStoryContent(pageData.getValue()[3]);
        page.setStoryCoverPath(pageData.getPath()[4]);
    }

    private static void createLetterPronunciationPage(LetterPronunciationPage page, MetaData pageData) {
        page.setPageType(PageTypes.LETTER_PRONUNCIATION_PAGE);
        page.setPageNum(Short.parseShort(pageData.getValue()[1]));
        page.setTitle(pageData.getValue()[2]);
        page.setLetterTxt(pageData.getValue()[3]);
        page.setLetterPronunciation(pageData.getValue()[4]);
        page.setAlternativeLetterPronunciation(pageData.getValue()[5]);
    }

    private static void createConstrainedMatchingPage(ConstrainedMatchingPage page, MetaData pageData) {
        page.setPageType(PageTypes.CONSTRAINED_MATCHING_PAGE);
        page.setPageNum(Short.parseShort(pageData.getValue()[1]));
        page.setNumOfPatterns(Short.parseShort(pageData.getValue()[2]));
        ArrayList<String> paths = new ArrayList<>();
        for (int j = 3; j < page.getNumOfPatterns() + 3; j++) {
            paths.add(pageData.getPath()[j]);
        }
        page.setPatternsPaths(paths);

    }


    private static void createFreeMatchingPage(FreeMatchingPage page, MetaData pageData) {
        page.setPageType(PageTypes.FREE_MATCHING_PAGE);
        page.setPageNum(Short.parseShort(pageData.getValue()[1]));
        page.setViewSize(pageData.getValue()[2]);
        page.setLineType(pageData.getValue()[3]);
        page.setOrientation(pageData.getValue()[4]);
        page.setViewType(pageData.getValue()[5]);
        page.setNumOfColumns(Integer.parseInt(pageData.getValue()[6]));
        page.setNumOfLines(Integer.parseInt(pageData.getValue()[7]));
    }


    private static void createIntroPage(IntroductionPage page, MetaData pageData) {
        page.setPageType(PageTypes.INTRODUCTION_PAGE);
        page.setPageNum(Short.parseShort(pageData.getValue()[1]));
        page.setLetterText(pageData.getValue()[2]);
        page.setAnimalName(pageData.getValue()[3]);
        page.setLetterSoundPath(pageData.getPath()[4]);
        page.setAnimalImagePath(pageData.getPath()[5]);
        page.setAnimalSound(pageData.getPath()[6]);
    }


    private static void createColoringPage(ColoringPage page, MetaData pageData) {
        page.setPageType(PageTypes.COLORING_PAGE);
        page.setTitle(pageData.getValue()[1]);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setFilledImagePath(pageData.getPath()[3]);
        page.setOutlinedImagePath(pageData.getPath()[4]);
    }


    private static void createLargeLetterPage(LargeLetterPage page, MetaData pageData) {

        page.setPageType(PageTypes.LETTER_PAGE);
        page.setTitle(pageData.getValue()[1]);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setLetter(pageData.getValue()[3]);
        page.setLargeLetterIndexesFile(pageData.getPath()[4]);
        page.setLetterSoundPath(pageData.getPath()[5]);
        page.setAnimalImagePath(pageData.getPath()[6]);
        page.setAnimalSoundPath(pageData.getPath()[7]);

    }


    private static void createLetterFirstPreviewPage(LetterFirstPreviewPage page, MetaData pageData) {
        page.setPageType(PageTypes.LETTER_FIRST_PREVIEW_PAGE);
        page.setTitle(pageData.getValue()[1]);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setLetterText(pageData.getValue()[3]);
        page.setWordText(pageData.getValue()[4]);
    }

    private static void createLetterSecondPreviewPage(LetterSecondPreviewPage page, MetaData pageData) {
        page.setPageType(PageTypes.LETTER_SECOND_PREVIEW_PAGE);
        page.setTitle(pageData.getValue()[1]);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setLetterText(pageData.getValue()[3]);
    }


    private static void createMediumLetterPage(MediumLetterPage page, MetaData pageData) {
        page.setPageType(PageTypes.MEDIUM_LETTER);
        page.setTitle(pageData.getValue()[1]);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setLetter(pageData.getValue()[3]);
        page.setMediumLetterIndexesFile(pageData.getPath()[4]);
        page.setLetterSoundPath(pageData.getPath()[5]);
        page.setAnimalImagePath(pageData.getPath()[6]);
        page.setAnimalSoundPath(pageData.getPath()[7]);


    }

    private static void createSmallLetterPage(SmallLetterPage page, MetaData pageData) {
        page.setPageType(PageTypes.SMALL_LETTER);
        page.setTitle(pageData.getValue()[1]);
        page.setPageNum(Short.parseShort(pageData.getValue()[2]));
        page.setLetter(pageData.getValue()[3]);
        page.setSmallLetterIndexesFile(pageData.getPath()[4]);
        page.setLetterSoundPath(pageData.getPath()[5]);
        page.setAnimalImagePath(pageData.getPath()[6]);
        page.setAnimalSoundPath(pageData.getPath()[7]);
    }


}
