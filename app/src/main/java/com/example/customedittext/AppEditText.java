package com.example.customedittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.customedittext.databinding.LayoutViewAppEditTextBinding;


public class AppEditText extends FrameLayout implements View.OnFocusChangeListener {
    private LayoutViewAppEditTextBinding binding;
    int colorResFocusDefault = R.color.esu_blue;
    int colorResUnFocusDefault = R.color.black;
    int colorResUnFocus = R.color.black;
    int colorResFocus = R.color.esu_blue;
    private int backgroundRes;

    Drawable icon = null;
    Drawable iconFocus = null;
    boolean showIcon = false;
    Drawable iconError = null;

    private boolean showErrorFlag = false;

    public AppEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutViewAppEditTextBinding.inflate(inflater);
        addView(binding.getRoot());
        initControls(attrs);
    }

    @SuppressLint("ResourceAsColor")
    private void initControls(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditText);

        String hint = "";
        String text = "";
        int inputType = EditorInfo.TYPE_CLASS_TEXT;
        int padding = 0, paddingLeft = 0, paddingRight = 0, paddingTop = 0, paddingBottom = 0;
        float textSize = 0;
        int gravity = 0;
        int maxLength = 0;

        if (attrs != null) {
            hint = typedArray.getString(R.styleable.AppEditText_android_hint);
            text = typedArray.getString(R.styleable.AppEditText_android_text);
            inputType = typedArray.getInt(R.styleable.AppEditText_android_inputType, EditorInfo.TYPE_CLASS_TEXT);
            showIcon = typedArray.getBoolean(R.styleable.AppEditText_showIcon, false);
            icon = typedArray.getDrawable(R.styleable.AppEditText_icon);
            iconFocus = typedArray.getDrawable(R.styleable.AppEditText_iconFocus);
            padding = typedArray.getDimensionPixelSize(R.styleable.AppEditText_android_padding, 0);
            paddingLeft = typedArray.getDimensionPixelSize(R.styleable.AppEditText_android_paddingLeft, 0);
            paddingRight = typedArray.getDimensionPixelSize(R.styleable.AppEditText_android_paddingRight, 0);
            paddingTop = typedArray.getDimensionPixelSize(R.styleable.AppEditText_android_paddingTop, 0);
            paddingBottom = typedArray.getDimensionPixelSize(R.styleable.AppEditText_android_paddingBottom, 0);
            textSize = typedArray.getDimension(R.styleable.AppEditText_android_textSize, 0);
            gravity = typedArray.getInt(R.styleable.AppEditText_android_gravity, 0);
            maxLength = typedArray.getInt(R.styleable.AppEditText_android_maxLength, 0);
            colorResFocus = typedArray.getResourceId(R.styleable.AppEditText_colorFocus, colorResFocusDefault);
            colorResUnFocus = typedArray.getResourceId(R.styleable.AppEditText_colorUnFocus, colorResUnFocusDefault);
            backgroundRes = typedArray.getResourceId(R.styleable.AppEditText_background, R.drawable.background_cover_grey_with_radius);
        }

        binding.editText.setText(text);
        binding.editText.setHint(hint);
        binding.editText.setInputType(inputType);
        binding.editText.setOnFocusChangeListener(this);
        binding.editText.setHintTextColor(getResources().getColor(R.color.black));
        binding.layoutEditText.setBackgroundResource(backgroundRes);

        if (showIcon && icon != null) {
            binding.ivIcon.setImageDrawable(icon);
            binding.editText.setPadding((int) getResources().getDimension(R.dimen._15sdp), 0, 0, 0);
        } else {
            binding.ivIcon.setVisibility(GONE);
            binding.editText.setPadding(5, 0, 0, 0);
        }
        if (textSize != 0) {
            binding.editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        if (gravity != 0) {
            binding.editText.setGravity(gravity);
        }

        if (maxLength != 0) {
            binding.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        if (!isInEditMode()) {
            Typeface tf = ResourcesCompat.getFont(getContext(),R.font.sf_pro_display_regular);
            binding.editText.setTypeface(tf);
        }
        typedArray.recycle();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        binding.layoutEditText.setSelected(hasFocus);
        refreshLayout(hasFocus);
    }

    private void refreshLayout(boolean hasFocus) {
        binding.layoutEditText.setBackgroundResource(hasFocus ?
                R.drawable.background_cover_red_with_radius
                :
                R.drawable.background_cover_grey_with_radius);
//        binding.bottomLine.setBackgroundColor(hasFocus ?
//                getResources().getColor(colorResFocus)
//                :
//                getResources().getColor(colorResUnFocus));

        binding.editText.setHintTextColor(hasFocus ?
                getResources().getColor(colorResFocus)
                :
                getResources().getColor(colorResUnFocus));

        if (showIcon && icon != null && iconFocus != null) {
            binding.ivIcon.setImageDrawable(hasFocus ? iconFocus : icon);
        }
    }

    public Editable getText() {
        return binding.editText.getText();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        binding.editText.addTextChangedListener(textWatcher);
    }

    public void requestFocus(boolean isFocus) {
        if (isFocus) {
            binding.editText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT);
        } else {
            binding.editText.clearFocus();
        }
    }
//    public void showError(){
//        showErrorFlag = true;
//        binding.bottomLine.setBackgroundColor(getResources().getColor(R.color.esu_red));
//        if(iconError != null){
//            binding.ivIcon.setBackgroundDrawable(iconError);
//        }
//    }
//
//    public void hideError(){
//        if(showErrorFlag){
//            showErrorFlag = false;
//            refreshLayout(binding.editText.hasFocus());
//        }
//    }
}
