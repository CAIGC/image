package com.qywenji.image.module.imageInfo.utils;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.image.module.imageInfo.constant.ImageInfoConstant;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by CAI_GC on 2017/1/17.
 */
public class ImageFileUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageFileUtil.class);

    public static JSONObject write(byte[] bytes) {
        File file = new File(ImageInfoConstant.imageFilePath);
        FileLock fileLock = null;
        FileChannel fileChannel = null;
        JSONObject jsonObject = new JSONObject();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            fileChannel = randomAccessFile.getChannel();
            for (int i=0 ; file == null && i < 5; i++){
                fileLock = fileChannel.tryLock();
                if(fileLock == null){
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                        logger.info("获取文件读写锁失败");
                    }
                }
            }
           /* do {
                fileLock = fileChannel.tryLock();
            } while (fileLock == null);*/
            Long fileSize = randomAccessFile.length();
            randomAccessFile.seek(fileSize);
            fileChannel.write(ByteBuffer.wrap(bytes));
            jsonObject.put("begin",fileSize);
            jsonObject.put("end",fileSize+bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileLock != null){
                try {
                    fileLock.release();
                    fileLock = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileChannel != null) {
                try {
                    fileChannel.close();
                    fileChannel = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    public static byte[] read(Long begin, Long end) {
        File file = new File(ImageInfoConstant.imageFilePath);
        RandomAccessFile randomAccessFile = null;
        byte[] buf = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(begin);
            buf = new byte[(int) (end - begin)];
            randomAccessFile.read(buf);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buf;
    }

    /**
     * 图片按比例压缩 scale取值范围（0-1）
     * @param imgStream
     * @return
     * @throws IOException
     */
    public static byte[] thumbnailsByScale(InputStream imgStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage image = Thumbnails.of(imgStream).scale(1.0D).asBufferedImage();
        ImageIO.write(image, "jpg", out);
        return out.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        Thumbnails.of("D:/LOGO-1-2.png").scale(1f)
                .toFile("D:/image_1%.png");
    }
}
