package com.honglu.future.widget.uploader;

public interface UploadProgressListener {
	void onProgress(long progress);

	void onSucceed(byte[] response);

	void onError();
}
