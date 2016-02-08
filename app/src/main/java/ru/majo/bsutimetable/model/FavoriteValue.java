package ru.majo.bsutimetable.model;

/**
 * Created by Majo on 25.01.2016.
 */
public class FavoriteValue {
    private String mValue;
    private boolean isFavorite;

    public FavoriteValue(String value, boolean isFavorite) {
        mValue = value;
        this.isFavorite = isFavorite;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

}
