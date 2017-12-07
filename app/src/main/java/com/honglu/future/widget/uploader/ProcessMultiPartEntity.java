package com.honglu.future.widget.uploader;




import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProcessMultiPartEntity extends MultipartEntity {

	private UploadProgressListener mProcessListener;
	private CountingOutputStream mOutputStream_;
	private OutputStream mLastOutputStream_;

	// the parameter is the same as the ProgressListener class in tuler's answer
	public ProcessMultiPartEntity(UploadProgressListener listener) {
		super(HttpMultipartMode.BROWSER_COMPATIBLE);
		mProcessListener = listener;
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		// If we have yet to create the CountingOutputStream, or the
		// OutputStream being passed in is different from the OutputStream used
		// to create the current CountingOutputStream
		if ((mLastOutputStream_ == null) || (mLastOutputStream_ != out)) {
			mLastOutputStream_ = out;
			mOutputStream_ = new CountingOutputStream(out);
		}

		super.writeTo(mOutputStream_);
	}

	private class CountingOutputStream extends FilterOutputStream {

		private long mTransferred = 0;
		private OutputStream mWrappedOutputStream_;

		public CountingOutputStream(final OutputStream out) {
			super(out);
			mWrappedOutputStream_ = out;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			mWrappedOutputStream_.write(b, off, len);
			mTransferred += len;
			if (mProcessListener != null) {
				mProcessListener.onProgress(mTransferred);
			}
		}

		public void write(int b) throws IOException {
//			super.write(b);
			mWrappedOutputStream_.write(b);
			mTransferred ++;
			if (mProcessListener != null) {
				mProcessListener.onProgress(mTransferred);
			}
		}
	}
}
