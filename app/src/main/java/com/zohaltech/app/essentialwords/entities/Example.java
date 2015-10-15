package com.zohaltech.app.essentialwords.entities;

public class Example {
    private int    id;
    private int    vocabularyId;
    private int    ordinal;
    private String english;

    public Example(int id, int vocabularyId, int ordinal, String english) {
        this(vocabularyId, ordinal, english);
        this.id = id;
    }

    public Example(int vocabularyId, int ordinal, String english) {
        setVocabularyId(vocabularyId);
        setOrdinal(ordinal);
        setEnglish(english);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVocabularyId() {
        return vocabularyId;
    }

    public void setVocabularyId(int vocabularyId) {
        this.vocabularyId = vocabularyId;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
