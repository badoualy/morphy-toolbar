package com.github.badoualy.morphytoolbar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ViewConstructor")
public class MorphyToolbar extends RelativeLayout {

    private Builder builder;
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private MorphyToolbarUtils utils;

    private LinearLayout innerLayout;
    private LinearLayout titleLayout;
    private TextView lblTitle;
    private TextView lblSubtitle;
    private CircleImageView imgPicture;

    // State fields
    private boolean animating = false;
    private boolean collapsed = true;

    private MorphyToolbar(Builder builder) {
        super(builder.activity);
        this.builder = builder;
        this.activity = builder.activity;
        this.toolbar = builder.toolbar;

        inflate();
        replaceToolbar();
        init();

        utils = new MorphyToolbarUtils(this);
    }

    private void inflate() {
        final View view = inflate(getContext(), R.layout.mt_morphy_toolbar, this);
        innerLayout = (LinearLayout) view.findViewById(R.id.mt_layout_toolbar);
        imgPicture = (CircleImageView) innerLayout.findViewById(R.id.mt_img_picture);
        titleLayout = (LinearLayout) innerLayout.findViewById(R.id.mt_layout_title);

        lblTitle = (TextView) titleLayout.findViewById(R.id.mt_lbl_title);
        lblSubtitle = (TextView) titleLayout.findViewById(R.id.mt_lbl_subtitle);

        setId(R.id.mt_morphy_toolbar);
    }

    /** Replace the toolbar in the activity view by the MorphyToolbar, wrapping the toolbar */
    private void replaceToolbar() {
        final ViewGroup toolbarParent = (ViewGroup) toolbar.getParent();
        int toolbarIndex = toolbarParent.indexOfChild(toolbar);
        toolbarParent.removeViewAt(toolbarIndex);
        addView(toolbar, 0);
        toolbarParent.addView(this, toolbarIndex);

        // Align content at toolbar's bottom
        final LayoutParams layoutParams = (LayoutParams) innerLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, toolbar.getId());
        innerLayout.requestLayout();
    }

    private void init() {
        final LayoutParams layoutParams = (LayoutParams) innerLayout.getLayoutParams();
        layoutParams.leftMargin = builder.innerLayoutCollapsedMargins[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            layoutParams.setMarginStart(builder.innerLayoutCollapsedMargins[0]);

        // The title will be displayed in our own TextView :)
        toolbar.setTitle(null);
        toolbar.setSubtitle(null);
        activity.setTitle(null);
        setTitle(builder.title);

        lblTitle.setTextColor(builder.titleColor);
        lblSubtitle.setTextColor(builder.subtitleColor);

        if (builder.subtitle != null) {
            lblSubtitle.setText(builder.subtitle);
        } else lblSubtitle.setVisibility(GONE);

        if (builder.pictureRes != -1)
            imgPicture.setImageResource(builder.pictureRes);
        else if (builder.pictureBitmap != null)
            imgPicture.setImageBitmap(builder.pictureBitmap);
        else if (builder.pictureDrawable != null)
            imgPicture.setImageDrawable(builder.pictureDrawable);

        if (builder.hidePictureWhenCollapsed)
            imgPicture.setVisibility(GONE);
    }

    /** Expand the view without changing the toolbar/status bar background color */
    public void expand() {
        expand(builder.toolbarColor, builder.statusBarColor, null);
    }

    /** Expand the view without changing the toolbar/status bar background color */
    public void expand(OnMorphyToolbarExpandedListener listener) {
        expand(builder.toolbarColor, builder.statusBarColor, listener);
    }

    public void expand(final int toolbarColor, final int statusBarColor) {
        expand(toolbarColor, statusBarColor, null);
    }

    public void expand(final int toolbarColor, final int statusBarColor, final OnMorphyToolbarExpandedListener listener) {
        if (animating)
            return;
        animating = true;
        Animation a = utils.animateToolbar(toolbar.getMeasuredHeight(), builder.toolbarExpandedHeight, toolbarColor, statusBarColor);
        Animation b = utils.animateInnerLayout(builder.innerLayoutExpandedMargins);
        Animation c = utils.animatePicture(builder.hidePictureWhenCollapsed ? 0 : builder.pictureCollapsedSize,
                                           builder.pictureExpandedSize);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animating = false;
                collapsed = false;
                if (listener != null)
                    listener.onMorphyToolbarExpanded();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        toolbar.startAnimation(a);
        innerLayout.startAnimation(b);
        imgPicture.startAnimation(c);
    }

    public void collapse() {
        collapse(null);
    }

    public void collapse(final OnMorphyToolbarCollapsedListener listener) {
        if (animating)
            return;
        animating = true;
        Animation a = utils.animateToolbar(toolbar.getHeight(), builder.toolbarCollapsedHeight, builder.toolbarColor,
                                           builder.statusBarColor);
        Animation b = utils.animateInnerLayout(builder.innerLayoutCollapsedMargins);
        Animation c = utils.animatePicture(builder.pictureExpandedSize,
                                           builder.hidePictureWhenCollapsed ? 0 : builder.pictureCollapsedSize);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animating = false;
                collapsed = true;
                if (listener != null)
                    listener.onMorphyToolbarCollapsed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        toolbar.startAnimation(a);
        innerLayout.startAnimation(b);
        imgPicture.startAnimation(c);
    }

    public void setAsSupportActionBar() {
        activity.setSupportActionBar(toolbar);
        activity.setTitle(null);
    }

    public void setTitle(CharSequence title) {
        builder.title = title;
        lblTitle.setText(title);
    }

    public void setTitle(@StringRes int res) {
        builder.title = activity.getText(res);
        lblTitle.setText(builder.title);
    }

    public void setSubtitle(CharSequence subtitle) {
        builder.subtitle = subtitle;
        lblSubtitle.setText(subtitle);
    }

    public void setSubtitle(@StringRes int res) {
        builder.subtitle = activity.getText(res);
        lblSubtitle.setText(builder.subtitle);
    }

    public void setPicture(Bitmap bitmap) {
        builder.pictureRes = -1;
        builder.pictureBitmap = bitmap;
        builder.pictureDrawable = null;
        imgPicture.setImageBitmap(bitmap);
    }

    public void setPicture(Drawable drawable) {
        builder.pictureRes = -1;
        builder.pictureBitmap = null;
        builder.pictureDrawable = drawable;
        imgPicture.setImageDrawable(drawable);
    }

    public void setHidePictureWhenCollapsed(boolean value) {
        if (value && collapsed && !builder.hidePictureWhenCollapsed) {
            final ViewGroup.LayoutParams layoutParams = imgPicture.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = 0;
            imgPicture.requestLayout();
            imgPicture.setVisibility(GONE);
        } else if (!value && collapsed && builder.hidePictureWhenCollapsed) {
            final ViewGroup.LayoutParams layoutParams = imgPicture.getLayoutParams();
            layoutParams.height = builder.pictureCollapsedSize;
            layoutParams.width = builder.pictureCollapsedSize;
            imgPicture.requestLayout();
            imgPicture.setVisibility(VISIBLE);
        }
        builder.hidePictureWhenCollapsed = value;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        innerLayout.setOnClickListener(l);
    }

    Builder getBuilder() {
        return builder;
    }

    LinearLayout getInnerLayout() {
        return innerLayout;
    }

    CircleImageView getImgPicture() {
        return imgPicture;
    }

    TextView getLblTitle() {
        return lblTitle;
    }

    TextView getLblSubtitle() {
        return lblSubtitle;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public boolean isAnimating() {
        return animating;
    }

    public static MorphyToolbar findInActivity(@NonNull AppCompatActivity activity) {
        return (MorphyToolbar) activity.findViewById(R.id.mt_morphy_toolbar);
    }

    public static MorphyToolbarBuilder builder(@NonNull AppCompatActivity activity, @NonNull Toolbar toolbar) {
        return new Builder(activity, toolbar);
    }

    private static final class Builder extends MorphyToolbarBuilder {

        public Builder(@NonNull AppCompatActivity activity, @NonNull Toolbar toolbar) {
            super(activity, toolbar);
        }

        @Override
        public MorphyToolbar build() {
            return new MorphyToolbar(this);
        }
    }

    public interface OnMorphyToolbarExpandedListener {
        void onMorphyToolbarExpanded();
    }

    public interface OnMorphyToolbarCollapsedListener {
        void onMorphyToolbarCollapsed();
    }
}
