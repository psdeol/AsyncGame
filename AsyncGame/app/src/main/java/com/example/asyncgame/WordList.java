package com.example.asyncgame;

public class WordList {

    private int currentWordIndex = 0;
    private String word1;
    private String word2;
    private String word3;

    public WordList(){

    }

    public int getCurrentWordIndex(){
        return currentWordIndex;
    }

    public void setCurrentWordIndex(int index){
        currentWordIndex = index;
    }

    public void incrementCurrentWordIndex(){
        currentWordIndex++;
        if (currentWordIndex > 2){
            currentWordIndex = 0;
        }
    }

    public String getWord(){
        switch (currentWordIndex){
            case 0:
                return word1;
            case 1:
                return word2;
            case 2:
                return word3;
        }
        return "null";
    }

    public String getWord(int index){
        switch (index){
            case 0:
                return word1;
            case 1:
                return word2;
            case 2:
                return word3;
        }
        return "null";
    }

    public void setWord(int index, String word){
        switch (index){
            case 0:
                word1 = word;
                break;
            case 1:
                word2 = word;
                break;
            case 2:
                word3 = word;
                break;
        }
    }
}

