package com.lei2j.douyu.netty;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.iflytek.cloud.speech.*;
import javazoom.jl.player.Player;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lei2j on 2018/12/16.
 */
public class TestRegex {

    @Test
    public void test4(){
        Matcher matcher = Pattern.compile("^(.(?!(\\.)\\2))+$").matcher("abc2.3.1$%^^%.&*((64");
        System.out.println(matcher.find());
    }

    @Test
    public void test5(){
        Matcher matcher = Pattern.compile("^\\w((?!(\\.)\\2)[\\w\\.]){0,62}\\w@((?!(\\.)\\4)[\\w\\.]){3,253}$").matcher("d.a.s@p.om");
        System.out.println(matcher.find());
    }

    @Test
    public void test6() throws Exception {
        deepPath(new File("/opt/prod"),1);
    }

    @Test
    public void test8() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("output.mp3");
        try {
            // 文件流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            // 文件编码
            AudioFormat audioFormat = audioInputStream.getFormat();
            // 转换文件编码
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                System.out.println(audioFormat.getEncoding());
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
                // 将数据流也转换成指定编码
                audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
            }


            // 打开输出设备
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            // 使数据行得到一个播放设备
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            // 将数据行用指定的编码打开
            sourceDataLine.open(audioFormat);
            // 使数据行得到数据时就开始播放
            sourceDataLine.start();

            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // 将流数据逐渐写入数据行,边写边播
            int numBytes = bytesPerFrame;
            byte[] audioBytes = new byte[numBytes];
            while (audioInputStream.read(audioBytes) != -1) {
                sourceDataLine.write(audioBytes, 0, audioBytes.length);
            }
            sourceDataLine.drain();
            sourceDataLine.stop();
            sourceDataLine.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test9(){
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream("output.mp3"));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void test7() throws FileNotFoundException {
        String appId = "11336420";
        String apiKey = "fxPutgRn0r5coTEXMNeU03Hq";
        String secretKey = "86878bafd0f3097c29810303ae18bccb";
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(appId, apiKey, secretKey);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        TtsResponse res = client.synthesis("土豪用户冰与火丶挣扎在直播间四八五五零三输出了100元，主播感谢您的支持", "zh", 1, null);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "output.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try {
            BufferedInputStream buffer = new BufferedInputStream(inputStream);
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void test10(){
        SpeechUtility.createUtility( SpeechConstant.APPID +"=5d107ee1");
        //1.创建SpeechSynthesizer对象
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "x_chunchun");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        //设置合成音频保存位置（可自定义保存位置），保存在“./tts_test.pcm”
        //如果不需要保存合成音频，注释该行代码
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./tts_test.pcm");
        //3.开始合成
        mTts.startSpeaking("土豪用户冰与火丶挣扎在直播间四八五五零三输出了100元，主播感谢您的支持", mSynListener);
        mSynListener.onSpeakBegin();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener(){
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {}

        @Override
        public void onEvent(int i, int i1, int i2, int i3, Object o, Object o1) {

        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}
        //开始播放
        public void onSpeakBegin() {}
        //暂停播放
        public void onSpeakPaused() {}
        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}
        //恢复播放回调接口
        public void onSpeakResumed() {}
    };

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }

    private void deepPath(File file,int deep) throws Exception {
        System.out.println("deep:"+deep);
        if (deep > 3) {
            throw new Exception("File hierarchy greater than 3");
        }
        if(file.exists()){
            if(file.isDirectory()){
                deep++;
                File[] files = file.listFiles();
                for (File fileItem :
                        files) {
                    if(fileItem.isFile()){
                        readFile(fileItem);
                    }else {
                        deepPath(fileItem,deep);
                    }
                }
            }else {
                readFile(file);
            }
        }
    }

    private void readFile(File file){
        System.out.println(file.getName());
    }

}
