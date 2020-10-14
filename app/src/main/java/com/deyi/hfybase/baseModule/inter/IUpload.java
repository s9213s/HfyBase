package com.deyi.hfybase.baseModule.inter;

import java.io.File;
import java.util.List;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public interface IUpload {
    //压缩图片的方法
    void compress();

    //上传图片
    void doUpload(List<File> files);
}
