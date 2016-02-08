package com.github.badoualy.morphytoolbar;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

public abstract class MorphyToolbarBuilder {

    AppCompatActivity activity;
    Toolbar toolbar;

    CharSequence title;
    int titleColor;
    CharSequence subtitle;
    int subtitleColor;

    int animationDuration = 200;

    boolean hidePictureWhenCollapsed = true;
    int pictureRes = -1;
    Bitmap pictureBitmap;
    Drawable pictureDrawable;
    int pictureCollapsedSize, pictureExpandedSize;

    int statusBarColor = Color.BLACK;
    int toolbarColor;
    int toolbarCollapsedHeight, toolbarExpandedHeight;

    int[] innerLayoutCollapsedMargins, innerLayoutExpandedMargins;

    MorphyToolbarBuilder(@NonNull AppCompatActivity activity, @NonNull Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
        title = activity.getTitle();

        final Resources resources = activity.getResources();

        titleColor = resources.getColor(R.color.mt_title_color);
        subtitleColor = resources.getColor(R.color.mt_subtitle_color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            statusBarColor = activity.getWindow().getStatusBarColor();
        TypedValue typedValue = new TypedValue();
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        toolbarColor = a.getColor(0, 0);
        a.recycle();
        toolbarCollapsedHeight = resources.getDimensionPixelSize(R.dimen.mt_toolbar_height);
        toolbarExpandedHeight = (int) (2.5f * toolbarCollapsedHeight);

        pictureCollapsedSize = resources.getDimensionPixelSize(R.dimen.mt_toolbar_picture_size_collapsed);
        pictureExpandedSize = resources.getDimensionPixelSize(R.dimen.mt_toolbar_picture_size_expanded);

        innerLayoutCollapsedMargins = new int[]{resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_start_collapsed),
                                                resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_top_collapsed),
                                                resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_end_collapsed),
                                                resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_bottom_collapsed)};

        innerLayoutExpandedMargins = new int[]{resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_start_expanded),
                                               resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_top_expanded),
                                               resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_end_expanded),
                                               resources.getDimensionPixelSize(R.dimen.mt_toolbar_content_margin_bottom_expanded)};
    }

    public MorphyToolbarBuilder withToolbarAsSupportActionBar() {
        activity.setSupportActionBar(toolbar);
        activity.setTitle(null);
        return this;
    }

    public MorphyToolbarBuilder withTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public MorphyToolbarBuilder withTitle(@StringRes int res) {
        this.title = activity.getText(res);
        return this;
    }

    public MorphyToolbarBuilder withSubtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public MorphyToolbarBuilder withSubtitle(@StringRes int res) {
        this.subtitle = activity.getText(res);
        return this;
    }

    public MorphyToolbarBuilder withPicture(@DrawableRes int res) {
        this.pictureRes = res;
        return this;
    }

    public MorphyToolbarBuilder withPicture(@NonNull Bitmap bitmap) {
        this.pictureBitmap = bitmap;
        return this;
    }

    public MorphyToolbarBuilder withPicture(@NonNull Drawable drawable) {
        this.pictureDrawable = drawable;
        return this;
    }

    public MorphyToolbarBuilder withHidePictureWhenCollapsed(boolean hidePictureWhenCollapsed) {
        this.hidePictureWhenCollapsed = hidePictureWhenCollapsed;
        return this;
    }

    public MorphyToolbarBuilder withAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        return this;
    }

    public MorphyToolbarBuilder withToolbarExpandedHeight(int heightInPx) {
        toolbarExpandedHeight = Math.min(toolbarExpandedHeight, heightInPx);
        return this;
    }

    public MorphyToolbarBuilder withToolbarExpandedHeightRes(@DimenRes int res) {
        toolbarExpandedHeight = Math.min(toolbarExpandedHeight, activity.getResources().getDimensionPixelSize(res));
        return this;
    }

    public MorphyToolbarBuilder withToolbarExpandedFactor(float factor) {
        toolbarExpandedHeight = Math.min(toolbarExpandedHeight, (int) (factor * toolbarCollapsedHeight));
        return this;
    }

    public MorphyToolbarBuilder withTitleColor(@ColorInt int color) {
        this.titleColor = color;
        return this;
    }

    public MorphyToolbarBuilder withTitleColorRes(@ColorRes int res) {
        this.titleColor = activity.getResources().getColor(res);
        return this;
    }

    public MorphyToolbarBuilder withSubtitleColor(@ColorInt int color) {
        this.subtitleColor = color;
        return this;
    }

    public MorphyToolbarBuilder withSubtitleColorRes(@ColorRes int res) {
        this.subtitleColor = activity.getResources().getColor(res);
        return this;
    }

    public MorphyToolbarBuilder withContentMarginStart(int marginInPx) {
        innerLayoutCollapsedMargins[0] = marginInPx;
        return this;
    }

    public MorphyToolbarBuilder withContentMarginStartRes(@DimenRes int res) {
        innerLayoutCollapsedMargins[0] = activity.getResources().getDimensionPixelSize(res);
        return this;
    }

    public MorphyToolbarBuilder withContentExpandedMarginStart(int marginInPx) {
        innerLayoutExpandedMargins[0] = marginInPx;
        return this;
    }

    public MorphyToolbarBuilder withContentExpandedMarginStartRes(@DimenRes int res) {
        innerLayoutExpandedMargins[0] = activity.getResources().getDimensionPixelSize(res);
        return this;
    }

    public abstract MorphyToolbar build();
}
