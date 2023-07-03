package com.example.book_v2.data.repositories;


import java.util.List;

public class BaseActivityRepository {

    public List<String> fileNames;
  //  ExecutorService executorService = Executors.newSingleThreadExecutor();

//
//    public List<String> getFileNames(String path) {
//
//        Thread t = new Thread(() -> fileNames = ReadFiles.getAllLettersFilesPaths(path));
//
//        try {
//            t.start();
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return fileNames;
//    }


}
