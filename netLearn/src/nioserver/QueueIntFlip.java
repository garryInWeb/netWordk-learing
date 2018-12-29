package nioserver;

/**
 * Created by zhengtengfei on 2018/12/29.
 */
public class QueueIntFlip {

    public int[] element = null;

    private boolean flipped = false;
    private int capacity = 0;
    private int writePos = 0;
    private int readPos = 0;

    public QueueIntFlip(int capacity) {
        this.capacity = capacity;
        this.element = new int[capacity];
    }

    public int take(){
        if (!flipped){
            if (readPos < writePos){
                return element[readPos++];
            }else{
                return -1;
            }
        } else{
            if (readPos == capacity){
                readPos = 0;
                flipped = false;

                if (readPos < writePos){
                    return element[readPos];
                }else{
                    return -1;
                }
            } else{
                return element[readPos];
            }
        }
    }
}
