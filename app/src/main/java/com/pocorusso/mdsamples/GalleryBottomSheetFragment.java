package com.pocorusso.mdsamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GalleryBottomSheetFragment extends GalleryFragment implements View.OnClickListener {

    private BottomSheetBehavior mBottomSheetBehavior;

    public GalleryBottomSheetFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery_bottom_sheet, container, false);

        View bottomSheet = v.findViewById( R.id.bottom_sheet );
        Button button1 = (Button) v.findViewById( R.id.button_1 );
        Button button2 = (Button) v.findViewById( R.id.button_2 );

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(100);

        initGallery(v);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch( view.getId() ) {
            case R.id.button_1: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            }
            case R.id.button_2: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            }
        }
    }
}
