package com.strategy.intecom.vtc.fixuser.view.custom.imagecus;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Mr. Ha on 5/25/16.
 * IMAGEVIEW CÓ KÍCH THƯỚC TỈ LỆ THEO DRAWABLE CHỨA TRONG NÓ
 * Custom lại ImageView như bên dưới cho trường hợp giãn kích thước tối đa chiều ngang mà vẫn giữ tỉ lệ Drawable trong nó.
 * Lưu ý, việc custom vuông này có thể áp dụng cho bất kỳ view nào có chưa Drawable resource như ImageView, ImageButton..., chỉ cần extends lại và gọi customview từ layout.
 */
public class ImageViewAspectRatioHorizontal extends ImageView {


    public ImageViewAspectRatioHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getDrawable() != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
            setMeasuredDimension(width, height);
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

}