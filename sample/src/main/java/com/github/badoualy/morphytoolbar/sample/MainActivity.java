package com.github.badoualy.morphytoolbar.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.github.badoualy.morphytoolbar.MorphyToolbar;

public class MainActivity extends AppCompatActivity {

    MorphyToolbar morphyToolbar;
    int primary2;
    int primaryDark2;

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    FloatingActionButton fabPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        primary2 = getResources().getColor(R.color.primary2);
        primaryDark2 = getResources().getColor(R.color.primary_dark2);

        appBarLayout = (AppBarLayout) findViewById(R.id.layout_app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabPhoto = (FloatingActionButton) findViewById(R.id.fab_photo);
        disableAppBarDrag();
        hideFab();

        morphyToolbar = MorphyToolbar.builder(this, toolbar)
                                     .withToolbarAsSupportActionBar()
                                     .withTitle("Minions [not so] serious talk")
                                     .withSubtitle("160 participants")
                                     .withPicture(R.drawable.img_profile)
                                     .withHidePictureWhenCollapsed(false)
                                     .build();

        morphyToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morphyToolbar.isCollapsed()) {
                    morphyToolbar.expand(primary2, primaryDark2, new MorphyToolbar.OnMorphyToolbarExpandedListener() {
                        @Override
                        public void onMorphyToolbarExpanded() {
                            showFab();
                        }
                    });
                } else {
                    hideFab();
                    morphyToolbar.collapse();
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                                                            | ActionBar.DISPLAY_SHOW_TITLE
                                                            | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void disableAppBarDrag() {
        // see http://stackoverflow.com/questions/34108501/how-to-disable-scrolling-of-appbarlayout-in-coordinatorlayout
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        params.setBehavior(behavior);
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
    }

    /** To hide fab, you need to remove its anchor */
    private void hideFab() {
        // Ugly bug makes the view go to bottom|center of screen before hiding, seems like you need to implement your own fab behavior...
        fabPhoto.setVisibility(View.GONE);
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fabPhoto.getLayoutParams();
        layoutParams.setAnchorId(View.NO_ID);
        fabPhoto.requestLayout();
        fabPhoto.hide();
    }

    private void showFab() {
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fabPhoto.getLayoutParams();
        layoutParams.setAnchorId(R.id.layout_app_bar);
        layoutParams.anchorGravity = Gravity.RIGHT | Gravity.END | Gravity.BOTTOM;
        fabPhoto.requestLayout();
        fabPhoto.show();
    }

    @Override
    public void onBackPressed() {
        if (!morphyToolbar.isCollapsed()) {
            hideFab();
            morphyToolbar.collapse();
        } else
            super.onBackPressed();
    }
}
