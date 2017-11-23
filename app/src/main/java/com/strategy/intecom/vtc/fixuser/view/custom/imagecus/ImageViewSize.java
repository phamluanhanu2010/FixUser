package com.strategy.intecom.vtc.fixuser.view.custom.imagecus;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Mr. Ha on 5/25/16.
 *
 * Set Image Size phụ thuộc với chiều cao của Image
 * (cho trường hợp kích thước vuông phụ thuộc theo chiều cao của ImageView, ngược lại tương tự).
 * Lưu ý, việc custom vuông này có thể áp dụng cho bất kỳ view nào, chỉ cần extends lại và gọi customview từ layout.
 */
public class ImageViewSize extends ImageView{


    public ImageViewSize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
