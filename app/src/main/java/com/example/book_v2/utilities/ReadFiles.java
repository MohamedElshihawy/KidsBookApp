package com.example.book_v2.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadFiles {

    public static final String META_DATA = "metadata";
    static ArrayList<String> filesNames = new ArrayList<>();
    public static ArrayList<String> getAllLettersFilesPaths(String path) {

        File dataFolder = new File(path);
        if (dataFolder.exists()) {
            File[] filesList = dataFolder.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    if (file.isDirectory()) {
                        filesNames = getAllLettersFilesPaths(file.getAbsolutePath());
                    } else {
                        filesNames.add(file.getPath());
                    }
                }
            }
        }
        return filesNames;
    }


    public static List<String> getMetaDataOfAllPages(List<String> filesList) {
        List<String> metaDate = new ArrayList<>();
        for (String fileName : filesList) {
            if (fileName.contains(META_DATA)) {
                metaDate.add(fileName);
            }
        }
        return metaDate;
    }


    public static String openTxtFile(String fileName) {
        StringBuilder data = new StringBuilder();
        BufferedReader bufferedReader = null;
        File file = new File(fileName);
        try {
            if (file.exists()) {
                bufferedReader = new BufferedReader(new InputStreamReader
                        (new FileInputStream(file)));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

}
