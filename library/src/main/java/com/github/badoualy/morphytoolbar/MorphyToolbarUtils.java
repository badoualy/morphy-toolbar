package com.github.badoualy.morphytoolbar;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import de.hdodenhof.circleimageview.CircleImageView;

final class MorphyToolbarUtils {

    private static MorphyToolbar morphyToolbar;
    private static MorphyToolbarBuilder builder;
    private static Toolbar toolbar;

    private static int toolbarCurrentColor;
    private static int statusBarCurrentColor;

    private MorphyToolbarUtils() {

    }

    static void init(MorphyToolbar morphyToolbar) {
        MorphyToolbarUtils.morphyToolbar = morphyToolbar;
        MorphyToolbarUtils.builder = morphyToolbar.getBuilder();
        toolbar = builder.toolbar;
        toolbarCurrentColor = builder.toolbarColor;
        statusBarCurrentColor = builder.statusBarColor;
    }

    /**
     * Animate the toolbar height and color (+ status bar color if possible)
     *
     * @param startHeight    toolbar height at animation start
     * @param endHeight      toolbar height at animation end
     * @param toolbarColor   toolbar color at animation end
     * @param statusBarColor status bar color at animation end (if possible)
     * @return Animation to run
     */
    public static Animation animateToolbar(final int startHeight, final int endHeight, final int toolbarColor, final int statusBarColor) {
        final int deltaHeight = endHeight - startHeight;
        final int toolbarStartColor = toolbarCurrentColor;
        final int statusBarStartColor = statusBarCurrentColor;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                toolbar.getLayoutParams().height = startHeight + (int) (deltaHeight * interpolatedTime);
                toolbar.requestLayout();

                if (toolbarColor != toolbarStartColor) {
                    toolbarCurrentColor = (Integer) ArgbEvaluator.evaluate(interpolatedTime, toolbarStartColor, toolbarColor);
                    toolbar.setBackgroundColor(toolbarCurrentColor);
                }

                if (statusBarColor != statusBarStartColor) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        statusBarCurrentColor = (Integer) ArgbEvaluator.evaluate(interpolatedTime, statusBarStartColor, statusBarColor);
                        builder.activity.getWindow().setStatusBarColor(statusBarCurrentColor);
                    }
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setInterpolator(new DecelerateInterpolator());
        a.setDuration(builder.animationDuration);
        return a;
    }

    /**
     * Animate the inner layout to the given margins
     *
     * @param endMargins array of margins at animation end
     * @return Animation to run
     */
    public static Animation animateInnerLayout(int[] endMargins) {
        final LinearLayout innerLayout = morphyToolbar.getInnerLayout();
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) innerLayout.getLayoutParams();
        final int[] startMargins = {layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin};
        final int[] deltaMargins = {endMargins[0] - startMargins[0], endMargins[1] - startMargins[1],
                                    endMargins[2] - startMargins[2], endMargins[3] - startMargins[3]};
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                layoutParams.setMargins(startMargins[0] + (int) (deltaMargins[0] * interpolatedTime),
                                        startMargins[1] + (int) (deltaMargins[1] * interpolatedTime),
                                        startMargins[2] + (int) (deltaMargins[2] * interpolatedTime),
                                        startMargins[3] + (int) (deltaMargins[3] * interpolatedTime));
                innerLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setInterpolator(new DecelerateInterpolator());
        a.setDuration(builder.animationDuration);
        return a;
    }

    /**
     * Animate the picture to the given size
     *
     * @param startSize size at animation start
     * @param endSize   size at animation end
     * @return Animation to run
     */
    public static Animation animatePicture(final int startSize, final int endSize) {
        final CircleImageView imgPicture = morphyToolbar.getImgPicture();
        final ViewGroup.LayoutParams layoutParams = imgPicture.getLayoutParams();
        final int deltaSize = endSize - startSize;

        Animation c = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final int newSize = startSize + (int) (deltaSize * interpolatedTime);
                layoutParams.width = newSize;
                layoutParams.height = newSize;
                imgPicture.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        c.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (startSize == 0)
                    imgPicture.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (endSize == 0)
                    imgPicture.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        c.setInterpolator(new DecelerateInterpolator());
        c.setDuration(builder.animationDuration);
        return c;
    }
}
