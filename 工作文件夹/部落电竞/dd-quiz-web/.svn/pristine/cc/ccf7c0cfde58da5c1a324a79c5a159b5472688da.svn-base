package com.esportzoo.esport.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @description: 视频处理工具
 *
 * @author: Haitao.Li
 *
 * @create: 2019-09-04 14:56
 **/
public class VideoUtils {

	private transient static final Logger logger = LoggerFactory.getLogger(VideoUtils.class);

	/**
	 * 获取指定视频的帧并保存为图片至指定目录
	 * @param videofile  源视频文件路径
	 * @param framefile  截取帧的图片存放路径
	 * @param targetFileName  截取帧的图片名称
	  * @throws Exception
	 */
	public static void randomGrabberFFmpegImage(String filePath, String targerFilePath, String targetFileName) throws Exception {
		logger.info("视频截图开始，filePath：【{}】，targerFilePath 【{}】", filePath, targerFilePath);
		FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
		logger.info("时长：【{}】，总帧数 【{}】", ff.getFrameRate());
		ff.setFormat("mp4");
		ff.start();
		String rotate = ff.getVideoMetadata("rotate");
		Frame f;
		int i = 0;
		while (i < 18) {

			f = ff.grabImage();
			opencv_core.IplImage src = null;
			if (null != rotate && rotate.length() > 1) {
				OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
				src = converter.convert(f);

				f = converter.convert(rotate(src, Integer.valueOf(rotate)));
			}
			if (i==17){
				doExecuteFrame(f, targerFilePath, targetFileName);
			}



			i++;
		}

		ff.stop();

	}

	/*
	 * 旋转角度的
	 */
	public static opencv_core.IplImage rotate(opencv_core.IplImage src, int angle) {
		opencv_core.IplImage img = opencv_core.IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
		opencv_core.cvTranspose(src, img);
		opencv_core.cvFlip(img, img, angle);
		return img;
	}

	public static void doExecuteFrame(Frame f, String targerFilePath, String targetFileName) {

		if (null == f || null == f.image) {
			return;
		}
		Java2DFrameConverter converter = new Java2DFrameConverter();
		String imageMat = "jpg";
		String FileName = targerFilePath + File.separator + targetFileName + "." + imageMat;
		BufferedImage bi = converter.getBufferedImage(f);
		System.out.println("width:" + bi.getWidth());
		System.out.println("height:" + bi.getHeight());
		File output = new File(FileName);
		try {
			ImageIO.write(bi, imageMat, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			long startTime = System.currentTimeMillis();
//			randomGrabberFFmpegImage("https://daily-rs.esportzoo.com/upload/interface/shortcms/20190904/1567567690134_0.mp4",
			randomGrabberFFmpegImage("https://rs.esportzoo.com/upload/interface/shortcms/20191023/1571792967621_0.MOV",
					"/Users/haitao.li/Downloads", "test");
			logger.info("请求结束------执行消耗时间：{}ms--------", cn.hutool.core.date.DateUtil.spendMs(startTime));
		} catch (Exception e) {
			e.printStackTrace();

		}

	}


}
