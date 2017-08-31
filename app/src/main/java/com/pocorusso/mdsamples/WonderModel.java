package com.pocorusso.mdsamples;


public class WonderModel {

    String mCardName;
    int mImageResourceId;
    int mIsFav;
    int isTurned;

    public int getIsTurned() {
        return isTurned;
    }

    public void setIsTurned(int isTurned) {
        this.isTurned = isTurned;
    }

    public int getIsFav() {
        return mIsFav;
    }

    public void setIsFav(int isFav) {
        this.mIsFav = isFav;
    }

    public String getCardName() {
        return mCardName;
    }

    public void setCardName(String cardName) {
        this.mCardName = cardName;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.mImageResourceId = imageResourceId;
    }
}

