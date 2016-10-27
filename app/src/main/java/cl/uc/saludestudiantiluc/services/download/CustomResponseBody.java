package cl.uc.saludestudiantiluc.services.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * Created by jchicao on 10/26/16.
 */

class CustomResponseBody extends ResponseBody {

  private ResponseBody mResponseBody;
  private BufferedSource mBufferedSource;
  private ProgressListener mProgressListener;

  CustomResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
    mResponseBody = responseBody;
    mProgressListener = progressListener;
  }

  @Override
  public MediaType contentType() {
    return mResponseBody.contentType();
  }

  @Override
  public long contentLength() {
    return mResponseBody.contentLength();
  }

  @Override
  public BufferedSource source() {
    if (mBufferedSource == null) {
      mBufferedSource = Okio.buffer(source(mResponseBody.source()));
    }
    return mBufferedSource;
  }

  private Source source(Source source) {
    return new ForwardingSource(source) {
      long mTotalBytesRead = 0L;

      @Override public long read(Buffer sink, long byteCount) throws IOException {
        long bytesRead = super.read(sink, byteCount);
        // read() returns the number of bytes read, or -1 if this source is exhausted.
        mTotalBytesRead += bytesRead != -1 ? bytesRead : 0;
        long contentLength = mResponseBody.contentLength();
        long percentage = (100L * mTotalBytesRead) / contentLength;
        if (mProgressListener != null) {
          mProgressListener.onUpdate(percentage);
        }
        return bytesRead;
      }
    };
  }
}
