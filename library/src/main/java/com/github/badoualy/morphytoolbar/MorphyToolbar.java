package com.github.badoualy.morphytoolbar;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.badoualy.morphytoolbar.nineoldandroid.ArgbEvaluator;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.github.badoualy.morphytoolbar.MorphyToolbarUtils.animateInnerLayout;
import static com.github.badoualy.morphytoolbar.MorphyToolbarUtils.animatePicture;
import static com.github.badoualy.morphytoolbar.MorphyToolbarUtils.animateToolbar;

@SuppressLint("ViewConstructor")
public class MorphyToolbar extends RelativeLayout {

    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private Builder builder;
    private AppCompatActivity activity;
    private Toolbar toolbar;

    private LinearLayout innerLayout;
    private TextView lblTitle;
    private TextView lblSubtitle;
    private CircleImageView imgPicture;

    // State fields
    private boolean collapsed = true;

    private MorphyToolbar(Builder builder) {
        super(builder.activity);
        this.builder = builder;
        this.activity = builder.activity;
        this.toolbar = builder.toolbar;

        inflate();
        replaceToolbar();
        init();

        MorphyToolbarUtils.init(this);
    }

    private void inflate() {
        final View view = inflate(getContext(), R.layout.morphy_toolbar, this);
        innerLayout = (LinearLayout) view.findViewById(R.id.mt_layout_toolbar);
        lblTitle = (TextView) view.findViewById(R.id.lbl_title);
        lblSubtitle = (TextView) view.findViewById(R.id.lbl_subtitle);
        imgPicture = (CircleImageView) view.findViewById(R.id.img_picture);
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

    public void expand(final int toolbarColor, final int statusBarColor, final OnMorphyToolbarExpandedListener listener) {
        Animation a = animateToolbar(toolbar.getMeasuredHeight(), builder.toolbarExpandedHeight, toolbarColor, statusBarColor);
        Animation b = animateInnerLayout(builder.innerLayoutExpandedMargins);
        Animation c = animatePicture(builder.hidePictureWhenCollapsed ? 0 : builder.pictureCollapsedSize,
                                                 builder.pictureExpandedSize);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
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
        Animation a = animateToolbar(toolbar.getHeight(), builder.toolbarCollapsedHeight, builder.toolbarColor,
                                                 builder.statusBarColor);
        Animation b = animateInnerLayout(builder.innerLayoutCollapsedMargins);
        Animation c = animatePicture(builder.pictureExpandedSize,
                                                 builder.hidePictureWhenCollapsed ? 0 : builder.pictureCollapsedSize);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
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

    public void setSubtitle(CharSequence subtitle) {
        builder.subtitle = subtitle;
        lblSubtitle.setText(subtitle);
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
