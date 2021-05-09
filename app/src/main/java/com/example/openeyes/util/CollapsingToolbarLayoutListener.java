package com.example.openeyes.util;


import com.google.android.material.appbar.AppBarLayout;

public abstract class CollapsingToolbarLayoutListener implements AppBarLayout.OnOffsetChangedListener {
    protected final int STATE_COLLAPSED = 0;
    protected final int STATE_IDLE = 1;
    protected final int STATE_EXPANDED = 2;

    private int mCurrentState = STATE_IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if(i == 0){
            if(mCurrentState != STATE_EXPANDED){
                onStateChanged(appBarLayout, STATE_EXPANDED);
            }
            mCurrentState = STATE_EXPANDED;
        }else if(Math.abs(i) >= appBarLayout.getTotalScrollRange()){
            if(mCurrentState != STATE_COLLAPSED){
                onStateChanged(appBarLayout, STATE_COLLAPSED);
            }
            mCurrentState = STATE_COLLAPSED;
        }else{
            if(mCurrentState != STATE_IDLE){
                onStateChanged(appBarLayout, STATE_IDLE);
            }
            mCurrentState = STATE_IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, int state);
}
