package nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by zhengtengfei on 2018/12/29.
 */
public class SocketProcessor implements Runnable{

    private Queue<Socket> inboundSocketQueue = null;

    private MessageBuffer readMessageBuffer = null;
    private MessageBuffer writeMessageBuffer = null;

    private IMessageReaderFactory messageReaderFactory = null;

    private Queue<Message> outboundMessageQueue = new LinkedList<>();

    private Map<Long,Socket> socketMap = new HashMap<>();

    private ByteBuffer readByteBuffer = ByteBuffer.allocate(1024*1024);
    private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024*1024);
    private Selector readSelector = null;
    private Selector writeSelector = null;

    private IMessageProcessor messageProcessor = null;
    private WriteProxy writeProxy = null;

    private long nextSocketId = 16 * 1024; //start incoming socket ids from 16K - reserve bottom ids for pre-defined sockets (servers).


    public SocketProcessor(Queue<Socket> inboundSocketQueue, MessageBuffer readMessageBuffer, MessageBuffer writeMessageBuffer, IMessageReaderFactory messageReaderFactory, IMessageProcessor messageProcessor) throws IOException {
        this.inboundSocketQueue = inboundSocketQueue;
        this.readMessageBuffer = readMessageBuffer;
        this.writeMessageBuffer = writeMessageBuffer;
        this.messageReaderFactory = messageReaderFactory;
        this.messageProcessor = messageProcessor;

        this.writeProxy = new WriteProxy(writeMessageBuffer,this.outboundMessageQueue);
        this.readSelector = Selector.open();
        this.writeSelector = Selector.open();

    }

    @Override
    public void run() {
        while(true){
            try{
                executeCycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCycle() throws IOException {
        takeNewSocket();
    }

    private void takeNewSocket() throws IOException {
        Socket socket = this.inboundSocketQueue.poll();

        while(socket != null){
            socket.socketId = nextSocketId++;
            socket.socketChannel.configureBlocking(false);

            socket.messageReader = this.messageReaderFactory.createMessageReader();
            socket.messageReader.init(this.readMessageBuffer);

            socket.messageWrite = new MessageWrite();

            this.socketMap.put(socket.socketId,socket);

            SelectionKey key = socket.socketChannel.register(this.readSelector,SelectionKey.OP_READ);

            key.attach(socket);

            socket = inboundSocketQueue.poll();
        }
    }
}
