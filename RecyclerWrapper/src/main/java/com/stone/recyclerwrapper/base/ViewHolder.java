package com.stone.recyclerwrapper.base;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private Context mContext;
    private View mConvertView;

    public ViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public static ViewHolder createViewHolder(Context context, View itemView) {
        return new ViewHolder(context, itemView);
    }

    public static ViewHolder createViewHolder(Context context,
                                              ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        return new ViewHolder(context, itemView);
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    @SuppressWarnings("unused")
    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    @SuppressWarnings("unused")
    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public ViewHolder setText(int viewId, CharSequence value) {
        TextView view = findViewById(viewId);
        if (value == null) value = "";
        view.setText(value);
        return this;
    }

    /**
     * 设置TV的文字以及文字颜色
     *
     * @param text      文案
     * @param textColor 字体颜色
     */
    public ViewHolder setTextAndColor(int viewId, CharSequence text, String textColor) {
        TextView view = findViewById(viewId);
        return setTextAndColor(view, text, textColor);
    }

    public ViewHolder setTextAndColor(@NonNull TextView textView, CharSequence text, String textColor) {
        if (text == null) text = "";
        textView.setText(text);
        if (!TextUtils.isEmpty(textColor)) {
            textView.setTextColor(Color.parseColor(textColor));
        }
        return this;
    }

    /**
     * 设置TV的文字以及文字颜色
     *
     * @param text      文案
     * @param textColor 字体颜色
     */
    @SuppressWarnings("deprecation")
    public ViewHolder setTextAndColor(int viewId, CharSequence text, @ColorRes int textColor) {
        TextView view = findViewById(viewId);
        if (text == null) text = "";
        view.setText(text);
        view.setTextColor(mContext.getResources().getColor(textColor));
        return this;
    }


    /**
     * view设置背景
     */
    public ViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    /**
     * view设置点击监听器
     */
    @SuppressWarnings("unused")
    public ViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

}
