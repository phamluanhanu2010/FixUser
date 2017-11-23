package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;

public class DlChoicePhoto extends Dialog {

	private TextView btn_library;
	private TextView btn_capture;
	private TextView btn_cancel;

	private IActionDialogPhoto onClickDialogPhoto;

	public DlChoicePhoto(Context context, int width) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dl_media);

		width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		setCancelable(false);
		initUI();
	}


	private void initUI() {

		btn_library = (TextView) this.findViewById(R.id.btn_library);
		btn_capture = (TextView) this.findViewById(R.id.btn_capture);
		btn_cancel = (TextView) this.findViewById(R.id.btn_cancel);
		setEvent();
	}

	private void setEvent() {

		btn_library.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getOnClickDialogPhoto().onClickGetPictureLibrary();
				dismiss();
			}
		});


		btn_capture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getOnClickDialogPhoto().onClickGetCamera();
				dismiss();
			}
		});

		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		dismiss();
	}

	public interface IActionDialogPhoto {
		void onClickGetPictureLibrary();

		void onClickGetCamera();
	}

	public IActionDialogPhoto getOnClickDialogPhoto() {
		return onClickDialogPhoto;
	}

	public void setOnClickDialogPhoto(IActionDialogPhoto onClickDialogPhoto) {
		this.onClickDialogPhoto = onClickDialogPhoto;
	}
}
