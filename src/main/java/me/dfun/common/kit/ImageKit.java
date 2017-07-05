package me.dfun.common.kit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

/**
 * 图片工具
 */
public class ImageKit {

	private static Logger log = LoggerFactory.getLogger(ImageKit.class);

	/**
	 * 上传Base64位编码的图片
	 */
	public static String uploadBase64Image(String base64) {
		String filePath = null;
		if (StringUtils.isNotEmpty(base64)) {
			String start = "data:image/";
			String key = "base64,";
			String suffix = base64.substring(base64.indexOf(start) + start.length(), base64.indexOf(key) - 1);
			if (base64.indexOf(key) != -1) {
				base64 = base64.substring(base64.indexOf(key) + key.length());
			}
			String forder = FileKit.getFolderPath("file");
			FileKit.checkFolder(FileKit.getBasePath() + forder);
			filePath = forder + File.separator + FileKit.getNewName(suffix);
			ImageKit.base64ToImage(base64, FileKit.getBasePath() + filePath);
		}
		return filePath;
	}

	/**
	 * 将Base64位编码的图片并保存到指定路径
	 */
	public static void base64ToImage(String base64, String filePath) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			FileOutputStream write = new FileOutputStream(new File(filePath));
			byte[] decoderBytes = decoder.decodeBuffer(base64);
			write.write(decoderBytes);
			write.close();
		} catch (IOException e) {
			log.error("ImageKit error : {}", e);
		}
	}
}