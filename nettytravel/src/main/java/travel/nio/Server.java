package travel.nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

    private int port = 8080;

    public void init() throws Exception{
        // 1.打开ServerSocketChannel
        ServerSocketChannel acceptorSvr = ServerSocketChannel.open();

        // 2.绑定监听地址 InetSocketAddress，设置连接为非阻塞模式
        acceptorSvr.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
        acceptorSvr.configureBlocking(false);

        // 3.创建Reactor线程，创建多路复用器并启动线程
        Selector selector = Selector.open();
        new Thread(new ReactorTask()).start();

        // 4.将ServerSocketChannel注册到Reactor线程的多路复用器Selector上，监听ACCEPT事件
        SelectionKey key = acceptorSvr.register(selector, SelectionKey.OP_ACCEPT);

        // 5.多路复用器在线程run方法的无限循环体内轮询准备就绪的Key
        int num = selector.select();
        Set selectKeys = selector.selectedKeys();
        Iterator it = selectKeys.iterator();
        while (it.hasNext()) {
            SelectionKey selectionKey = (SelectionKey)it.next();
            // deal with i/o event
        }

        // 6.多路复用器监听到有新的客户端接入，处理新的接入请求，完成tcp三次握手，建立物理链路

        SocketChannel channel = SocketChannel.open();


        // 7.设置客户端链路为非阻塞模式
        channel.configureBlocking(false);
        channel.socket().setReuseAddress(true);
        // ...

        // 8.将新接入的客户端连接注册到Reactor线程的多路复用器上，监听读操作，读取客户端发送的网络消息
        SelectionKey key1 = channel.register(selector, SelectionKey.OP_READ);


        // 9.异步读取客户端请求消息到缓冲区
        // int readNum = channel.read(reveivedBuffer);

        // 10.对ByteBuffer进行编解码，如果有半包消息指针reset，继续读取后续的报文，将解码成功的消息封装成Task，投递到业务线程池中，进行业务逻辑编排
//        Object message = null;
//        while (buffer.hasRemain()) {
//            byteBuffer.mark();
//            Object msg = decode(byteBuffer);
//            if (msg == null) {
//                byteBuffer.reset();
//                break;
//            }
//            messageList.add(msg);
//
//        }



        // 11.将POJO对象encode成ByteBuffer，调用SocketChannel的异步write接口，将消息异步发送给客户端
//        channel.write(buffer);
    }

    private class ReactorTask implements Runnable {
        public void run() {
        }
    }
}
