package nioserver;

import java.nio.channels.SocketChannel;

/**
 * Created by zhengtengfei on 2018/12/29.
 */
public class Socket {
    public long socketId = 0;

    public SocketChannel socketChannel;

    public IMessageReader messageReader = null;
    public MessageWrite messageWrite = null;

    public Socket(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

}
