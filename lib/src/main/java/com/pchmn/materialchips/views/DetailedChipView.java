package com.pchmn.materialchips.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pchmn.EmailValidator;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.R;
import com.pchmn.materialchips.R2;
import com.pchmn.materialchips.adapter.ChipsAdapter;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;
import com.pchmn.materialchips.util.ColorUtil;
import com.pchmn.materialchips.util.LetterTileProvider;
import com.pchmn.materialchips.util.MyWindowCallback;
import com.pchmn.materialchips.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class DetailedChipView extends RelativeLayout {

    private static final String TAG = DetailedChipView.class.toString();
    // context
    private Context mContext;
    // xml elements
    @BindView(R2.id.content) RelativeLayout mContentLayout;
    @BindView(R2.id.name) TextView mNameTextView;
    @BindView(R2.id.info) TextView mInfoTextView;
    @BindView(R2.id.delete_button) ImageButton mDeleteButton;
    private ChipView currentChip;
    // letter tile provider
    private static LetterTileProvider mLetterTileProvider;
    // attributes
    private ColorStateList mBackgroundColor;

    public DetailedChipView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public DetailedChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    /**
     * Inflate the view according to attributes
     *
     * @param attrs the attributes
     */
    private void init(AttributeSet attrs) {
        // inflate layout
        View rootView = inflate(getContext(), R.layout.detailed_chip_view, this);
        // butter knife
        ButterKnife.bind(this, rootView);
        // letter tile provider
        mLetterTileProvider = new LetterTileProvider(mContext);
        // hide on first
        setVisibility(GONE);
        // hide on touch outside
        hideOnTouchOutside();
    }

    /**
     * Hide the view on touch outside of it
     */
    private void hideOnTouchOutside() {
        // set focusable
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
    }

    public void currentChip(ChipView chipView) {
        this.currentChip = chipView;
    }
    /**
     * Fade in
     */
    public void fadeIn() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(VISIBLE);
        int color = ResourcesCompat.getColor(getResources(), R.color.colorSelectedChip, null); //with theme
        currentChip.setChipBackgroundColor(color);
        currentChip.setLabelColor(Color.WHITE);

        // focus on the view
        requestFocus();
    }

    /**
     * Fade out
     */
    public void fadeOut() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        startAnimation(anim);
        setVisibility(GONE);
        // fix onclick issue
        clearFocus();
        setClickable(false);
        int color1 = ResourcesCompat.getColor(getResources(), R.color.colorChipViewBackground, null); //with theme
        int color2 = ResourcesCompat.getColor(getResources(), R.color.colorWrongChip, null); //with theme

        String email = currentChip.getLabel();
        EmailValidator ev = new EmailValidator();

        if(ev.validateEmail(email)){
            currentChip.setChipBackgroundColor(color1);
        }else{
            currentChip.setChipBackgroundColor(color2);
        }
        currentChip.setLabelColor(Color.BLACK);
    }

    public void setName(String name) {
        mNameTextView.setText(name);
    }

    public void setInfo(String info) {
        if(info != null) {
            mInfoTextView.setVisibility(VISIBLE);
            mInfoTextView.setText(info);
        }
        else {
            mInfoTextView.setVisibility(GONE);
        }
    }

    public void setTextColor(ColorStateList color) {
        mNameTextView.setTextColor(color);
        mInfoTextView.setTextColor(ColorUtil.alpha(color.getDefaultColor(), 150));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setBackGroundcolor(ColorStateList color) {
        mBackgroundColor = color;
        mContentLayout.setBackground(getResources().getDrawable(R.drawable.bg_chip_view_opened,null));
//        mContentLayout.getBackground().setColorFilter(color.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
    }


    public int getBackgroundColor() {
        return mBackgroundColor == null ? ContextCompat.getColor(mContext, R.color.colorAccent) : mBackgroundColor.getDefaultColor();
    }

    public void setDeleteIconColor(ColorStateList color) {
        mDeleteButton.getDrawable().mutate().setColorFilter(color.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
    }

    public void setOnDeleteClicked(OnClickListener onClickListener) {
        mDeleteButton.setOnClickListener(onClickListener);
    }

    public void alignLeft() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.leftMargin = 0;
        mContentLayout.setLayoutParams(params);
    }

    public void alignRight() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentLayout.getLayoutParams();
        params.rightMargin = 0;
        mContentLayout.setLayoutParams(params);
    }


    public static class Builder {
        private Context context;
        private Uri avatarUri;
        private Drawable avatarDrawable;
        private String name;
        private String info;
        private ColorStateList textColor;
        private ColorStateList backgroundColor;
        private ColorStateList deleteIconColor;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder avatar(Uri avatarUri) {
            this.avatarUri = avatarUri;
            return this;
        }

        public Builder avatar(Drawable avatarDrawable) {
            this.avatarDrawable = avatarDrawable;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }

        public Builder chip(ChipInterface chip) {
            this.name = chip.getLabel();
            this.info = chip.getInfo();
            return this;
        }

        public Builder textColor(ColorStateList textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder backgroundColor(ColorStateList backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder deleteIconColor(ColorStateList deleteIconColor) {
            this.deleteIconColor = deleteIconColor;
            return this;
        }

        public DetailedChipView build() {
            return DetailedChipView.newInstance(this);
        }
    }

    private static DetailedChipView newInstance(Builder builder) {
        DetailedChipView detailedChipView = new DetailedChipView(builder.context);

        // background color
        if(builder.backgroundColor != null)
            detailedChipView.setBackGroundcolor(builder.backgroundColor);

        // text color
        if(builder.textColor != null)
            detailedChipView.setTextColor(builder.textColor);
        else if(ColorUtil.isColorDark(detailedChipView.getBackgroundColor()))
            detailedChipView.setTextColor(ColorStateList.valueOf(Color.WHITE));
        else
            detailedChipView.setTextColor(ColorStateList.valueOf(Color.BLACK));

        // delete icon color
        if(builder.deleteIconColor != null)
            detailedChipView.setDeleteIconColor(builder.deleteIconColor);
        else if(ColorUtil.isColorDark(detailedChipView.getBackgroundColor()))
            detailedChipView.setDeleteIconColor(ColorStateList.valueOf(Color.WHITE));
        else
            detailedChipView.setDeleteIconColor(ColorStateList.valueOf(Color.BLACK));

        detailedChipView.setName(builder.name);
        detailedChipView.setInfo(builder.info);
        return detailedChipView;
    }
}
