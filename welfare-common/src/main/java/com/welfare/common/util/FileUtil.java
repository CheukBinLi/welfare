package com.welfare.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {
    /**
     * 创建文件夹
     */
    public void createFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 获取网络文件流
     */
    public static InputStream getInternetFileInputStream(String fileInternetUrl) throws IOException {
        if (StringUtils.isBlank(fileInternetUrl)) {
            return null;
        }

        URL url = new URL(fileInternetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = conn.getInputStream();

        return inputStream;
    }

    public static InputStream outputStreamParseInputStream(OutputStream outputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        return inputStream;
    }

    public static void inputStreamToFile(InputStream inputStream, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileMd5String(File file) {
        String fileMd5Value = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            MappedByteBuffer byteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byteBuffer);
            BigInteger bigInteger = new BigInteger(1, messageDigest.digest());
            fileMd5Value = bigInteger.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
        return fileMd5Value;
    }
}
