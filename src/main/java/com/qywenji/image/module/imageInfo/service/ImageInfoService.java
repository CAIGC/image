package com.qywenji.image.module.imageInfo.service;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.image.commons.service.BaseService;
import com.qywenji.image.commons.utils.Md5Utils;
import com.qywenji.image.module.imageInfo.bean.ImageInfo;
import com.qywenji.image.module.imageInfo.dao.ImageInfoDao;
import com.qywenji.image.module.imageInfo.utils.ImageFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CAI_GC on 2017/1/17.
 */
@Service
public class ImageInfoService extends BaseService<ImageInfo> {


    @Value("${access_path}")
    private String accessPath;

    @Autowired
    private ImageInfoDao imageDao;

    public ImageInfo writeFileAndSaveDb(MultipartFile imageFile) {
        JSONObject jsonObject = null;
        ImageInfo imageInfo = null;
        String mdKey = "";
        try {
            mdKey = Md5Utils.encode(imageFile.getBytes());
            imageInfo = this.findByMdKey(mdKey);
            if (imageInfo != null) {
                return imageInfo;
            }
//            jsonObject = ImageFileUtil.write(ImageFileUtil.thumbnailsByScale(imageFile.getInputStream()));
            /**大于30k就进行压缩*/
            if(imageFile.getSize() > 30*1024){
                jsonObject = ImageFileUtil.write(ImageFileUtil.thumbnailsByScale(imageFile.getInputStream()));
            }else{
                jsonObject = ImageFileUtil.write(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonObject == null || jsonObject.isEmpty()) {
            return imageInfo;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        imageInfo = new ImageInfo(mdKey, jsonObject.getLong("begin"), jsonObject.getLong("end"),accessPath+"/read/"+mdKey,dateFormat.format(new Date()));
        this.save(imageInfo);
        return imageInfo;
    }

    public ImageInfo findByMdKey(String key) {
        return imageDao.findByMdKey(key);
    }

}
