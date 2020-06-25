package com.lei2j.douyu.netty;

import com.lei2j.douyu.TestService;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.util.LHUtil;
import com.lei2j.douyu.util.RandomUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by lei2j on 2018/8/5.
 */
public class TestNIO {

    @Test
    public void test4()throws Exception{
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel = channel.bind(new InetSocketAddress(8888));
        ByteBuffer dst = ByteBuffer.allocate(1024);
        byte[] b = new byte[1024];
        int len = 0;
        while (true){
            SocketChannel accept = channel.accept();
            dst.clear();
            len = accept.read(dst);
            System.out.println("1:"+dst.toString());
            dst.flip();
            dst.get(b,0,len);
            System.out.println(new String(b));
            dst.clear();
            dst.put("hello client".getBytes());
            System.out.println("2:"+dst.toString());
            dst.flip();
            accept.write(dst);
        }
    }

    @Test
    public void test5()throws Exception{
        int i=0;
        while (i<999){
            SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.put((i+"hello server I'm client").getBytes());
            buf.flip();
            channel.write(buf);
            buf.clear();
            int len = channel.read(buf);
            byte[] b = new byte[1024];
            buf.flip();
            buf.get(b,0,len);
            System.out.println(new String(b));
            channel.close();
            i++;
        }
    }

    @Test
    public void test6()throws Exception{
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress(8888));
        Selector selector = Selector.open();
        ByteBuffer dst = ByteBuffer.allocate(1024);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            selector.select();
//            selector.select(1000);
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if(selectionKey.isAcceptable()){
                    System.out.println("accept....");
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel channel = serverSocketChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);

                }else if(selectionKey.isReadable()){
                    dst.clear();
                    System.out.println("read...");
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    System.out.println("connect:"+channel.isConnected());
                    int read = channel.read(dst);
                    if(read==-1){
                        System.out.println("chanenl closed");
                        selectionKey.channel();
                        channel.close();
                        continue;
                    }
                    dst.flip();
                    byte[] b =new byte[1024];
                    dst.get(b,0,read);
                    System.out.println(new String(b,0,read));
                    dst.clear();
                    dst.put("hello client".getBytes());
                    dst.flip();
                    channel.write(dst);
                }else if(selectionKey.isConnectable()){
                    System.out.println("connected...");
                }else if(selectionKey.isWritable()){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer w = ByteBuffer.allocate(1024);
                    String msg = "hello"+ RandomUtil.getString(12);
                    byte[] bytes = msg.getBytes();
                    int len = bytes.length+4;
                    byte[] bs= LHUtil.toLowerInt(len);
                    w.put(bs);
                    w.put(bytes);
                    w.flip();
                    channel.write(w);
                    System.out.println(msg);
                }
            }
        }
    }

    @Test
    public void test8() throws InterruptedException,ExecutionException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new DefaultThreadFactory("Thread-douyu-keeplive-%d",true,10));
        ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(() -> System.out.println("run>>>"), 100, 100, TimeUnit.MILLISECONDS);
        while (true){
            Thread.sleep(5000);
              new Thread(){
                  @Override
                  public void run() {
                      System.out.println("cancel");
                      scheduledFuture.cancel(false);
                      super.run();
                  }
              }.start();
//            System.out.println(scheduledFuture.isCancelled());
        }
    }

    @Test
    public void test9(){
        int[] a = {1, 2};
        Arrays.stream(a).forEach(value -> System.out.println(value));
    }

    @Test
    public void test10(){
        int size = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(size, ()->{
            System.out.println("=========>end!");
        });
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final int x = i;
            Thread thread = new Thread(() -> {
                System.out.println("Thread:" + x);
                try {
                    int await = cyclicBarrier.await();
                    System.out.println("Thread:" + x + " " + await);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            list.add(thread);
        }
        list.forEach(c->{
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void test11() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.lei2j.douyu.netty.TestNIO",true,this.getClass().getClassLoader());
        System.out.println(aClass.getClassLoader());
    }

    @Test
    public void test12(){
        ServiceLoader<TestService> loader = ServiceLoader.load(TestService.class);
        Iterator<TestService> iterator = loader.iterator();
        while (iterator.hasNext()){
            TestService testService = iterator.next();
            testService.test();
        }
    }
}
