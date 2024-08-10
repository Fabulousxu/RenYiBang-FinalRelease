package com.renyibang.serviceapi.util;

import java.util.List;

public class ImageUtil {
    public static String mergeImages(List<String> images)
    {
        if (images == null) {
            return "图片不存在！";
        }

        else if(images.isEmpty())
        {
            return "";
        }

        else
        {
            return String.join(" ", images);
        }
    }
}
