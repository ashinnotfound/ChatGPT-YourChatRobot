package com.ashin.util;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.internal.deps.okhttp3.OkHttpClient;
import net.mamoe.mirai.internal.deps.okhttp3.Request;
import net.mamoe.mirai.internal.deps.okhttp3.Response;
import net.mamoe.mirai.internal.deps.okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图像工具类
 *
 * @author ashinnotfound
 * @date 2023/08/16
 */
@Slf4j
public class ImageUtil {
    public static File download(String imageUrl) {
        log.info("正在下载图片({})", imageUrl);
        String savePath = System.currentTimeMillis() + ".jpg"; // 保存图片的文件路径和名称

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    try (FileOutputStream outputStream = new FileOutputStream(savePath)) {
                        outputStream.write(responseBody.bytes());
                    }
                }
            } else {
                log.error("图片下载失败: 无法连接到URL，HTTP响应码 {}", response.code());
            }
        } catch (IOException e) {
            log.error("图片下载失败: {}", e.getMessage());
        }

        File file = new File(savePath);
        log.info("图片下载完成！({})", file.getPath());
        return file;
    }
}
