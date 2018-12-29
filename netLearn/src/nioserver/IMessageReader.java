package nioserver;

/**
 * Created by zhengtengfei on 2018/12/29.
 */
public interface IMessageReader {

    public void process(Message message,WriteProxy writeProxy);

    void init(MessageBuffer messageBuffer);
}
