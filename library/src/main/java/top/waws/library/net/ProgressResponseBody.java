package top.waws.library.net;

import android.util.Log;

import java.io.IOException;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @desc: 带进度条的 ResponseBody
 * @className: ProgressResponseBody
 * @author: thanatos
 */
public abstract class ProgressResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;

    private BufferedSource mBufferedSource;

    public ProgressResponseBody(@NonNull ResponseBody responseBody){
        this.mResponseBody = responseBody;
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
        if (mBufferedSource == null){
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink,byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;   //不断统计当前下载好的数据
                //接口回调
                download(totalBytesRead,mResponseBody.contentLength(),bytesRead == -1);
                return bytesRead;
            }
        };
    }


   abstract void download(long read, long total, boolean finish);
}
