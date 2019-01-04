package nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by zhengtengfei on 2018/12/29.
 */
public class Socket {
    public long socketId = 0;

    public SocketChannel socketChannel;

    public IMessageReader messageReader = null;
    public MessageWrite messageWrite = null;

    public boolean endOfStreamReached = false;

    public Socket(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        int bytesRead = this.socketChannel.read(byteBuffer);
        int totalBytesRead = bytesRead;

        while(bytesRead > 0){
            bytesRead = this.socketChannel.read(byteBuffer);
            totalBytesRead += bytesRead;
        }
        if (bytesRead == -1){
            this.endOfStreamReached = true;
        }
        return totalBytesRead;
    }

    public int write(ByteBuffer byteBuffer) {

        return 0;
    }
}
