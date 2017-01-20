package com.qywenji.image.module.imageInfo.dao;

import com.qywenji.image.commons.dao.BaseDao;
import com.qywenji.image.module.imageInfo.bean.ImageInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by CAI_GC on 2017/1/17.
 */
@Repository
public class ImageInfoDao extends BaseDao<ImageInfo> {



    public ImageInfo findByMdKey(String key) {
        String hql = "from ImageInfo where mdKey = ?";
        return super.get(hql,key);
    }
}
