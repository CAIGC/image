package com.qywenji.image.module.imageInfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.image.commons.controller.BaseController;
import com.qywenji.image.commons.utils.Md5Utils;
import com.qywenji.image.module.imageInfo.bean.ImageInfo;
import com.qywenji.image.module.imageInfo.service.ImageInfoService;
import com.qywenji.image.module.imageInfo.utils.ImageFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CAI_GC on 2017/1/17.
 */
@RestController
public class ImageController extends BaseController {


    @Autowired
    private ImageInfoService imageService;

    @RequestMapping(value = "/upload")
    public Object uploadImage(HttpServletRequest request) throws IOException{
        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = mrequest.getFiles("files");
        List<ImageInfo> imageInfoList = new ArrayList<>();
        for(MultipartFile imageFile : files){
            ImageInfo imageInfo = imageService.writeFileAndSaveDb(imageFile);
            if(imageInfo != null){
                imageInfoList.add(imageInfo);
            }
        }
        return imageInfoList.isEmpty()?super.error("save fail"):super.success(imageInfoList);
    }

    @RequestMapping(value = "/test")
    public Object test() throws IOException{
        BufferedImage bufferedImage = ImageIO.read(new File("D:/getheadimg.jpg"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        String md5Str = Md5Utils.encode(os.toByteArray());
        JSONObject jsonObject = ImageFileUtil.write(ImageFileUtil.thumbnailsByScale(is));
        jsonObject.put("md5Str",md5Str);
        return super.success(jsonObject);
    }

    @RequestMapping(value = "/read")
    public void readImage(Long begin,Long end,HttpServletResponse response) throws IOException{
        byte[] image = ImageFileUtil.read(begin,end);
        response.setContentType("image/jpg");
        response.setHeader("Cache-Control","max-age=604800");
        OutputStream os = response.getOutputStream();
        os.write(image);
        os.flush();
        os.close();
    }
    @RequestMapping(value = "/read/{mdKey}")
    public void readByMd5Str(@PathVariable String mdKey,HttpServletResponse response) throws IOException{
        ImageInfo imageInfo = imageService.findByMdKey(mdKey);
        if(imageInfo == null){
            return;
        }
        byte[] image = ImageFileUtil.read(imageInfo.getBegin(),imageInfo.getEnd());
        response.setContentType("image/jpg");
        response.setHeader("Cache-Control","max-age=604800");
        OutputStream os = response.getOutputStream();
        os.write(image);
        os.flush();
        os.close();
    }
}
