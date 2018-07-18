package com.rskytech.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class StringUtil {

private static final String CHARSEQ = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static final String URL_REG = "\\[URLCONTEXT\\]";
	
	private static final String URL_REPLACEMENT = "[URLCONTEXT]";
	
	private static SimpleDateFormat sdf;
	
	private static String BASE_PATH;
	
	static {
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		BASE_PATH = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	}
	
	/**
	 * 还原URL
	 * 
	 * @param content
	 * @return
	 */
	public static String recoveryContextPath(String content) {
		return content == null ? content : content.replaceAll(URL_REG, BASE_PATH);
	}
	
	/**
	 * 用标记替换URL
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceContextPath(String content) {
		return content == null ? content : content.replaceAll(BASE_PATH, URL_REPLACEMENT);
	}
	
	public static String replaceContextPath(String content, int w, int h) {
		return content == null ? content : replaceContent(content, w, h).replaceAll(BASE_PATH, URL_REPLACEMENT);
	}
	
	/**
	 * 图片压缩
	 * @param contentString
	 * @param w
	 * @param h
	 * @return
	 */
	public static String replaceContent(String contentString, int w, int h) {
		Pattern p = Pattern.compile("<\\s*img\\s+([^>]*)\\s*>", Pattern.CASE_INSENSITIVE);
		Pattern pSrc = Pattern.compile("src\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
		Pattern pWidth = Pattern.compile("width\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
		Pattern pHeight = Pattern.compile("height\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(contentString);
		Matcher mWidth = null;// 匹配宽度
		Matcher mHeight = null;// 匹配高度
		while (m.find()) {
			String img = m.group();// img标记
			Matcher mSrc = pSrc.matcher(img);
			String src = "";
			if (mSrc.find()) {
				src = mSrc.group().replaceAll("\\ *", "");
			}
			String imgPath = src.substring(5, src.length() - 1);// 图片路径
			int width = 0, height = 0;// img标记中的width和height值
			mWidth = pWidth.matcher(img);
			mHeight = pHeight.matcher(img);
			if (mWidth.find()) {
				String widthStr = mWidth.group().replaceAll("\\ *", "");// 宽度属性
				width = Integer.parseInt(widthStr.substring(7, widthStr.length() - 1));
				mWidth = null;
			}
			if (mHeight.find()) {
				String heightStr = mHeight.group().replaceAll("\\ *", "");// 高度属性
				height = Integer.parseInt(heightStr.substring(8, heightStr.length() - 1));
				mHeight = null;
			}
			int[] realSize = getImgSize(imgPath);// 图片实际尺寸大小
			if (width == 0 || height == 0) {// img标记中无width和height属性,则width和height默认是图片实际尺寸
				width = realSize[0];
				height = realSize[1];
			}
			int[] resultSize = getScaleSize(width, height, w, h);
			width = resultSize[0];
			height = resultSize[1];
			String imgNew = "<img " + src + " width=\"" + width + "\" height=\"" + height + "\" />";
			contentString = contentString.replaceAll(img, imgNew);
		}
		return contentString;
	}
	
	/**
	 * 获取等比例缩放图片后的宽高
	 * 
	 * @param width
	 * @param height
	 * @param w
	 * @param h
	 * @return
	 */
	private static int[] getScaleSize(float width, float height, float w, float h) {
		float r = w / h;// pdf页面大小限制
		float ratio = width / height;
		if (ratio > 1) {
			// 宽度处理
			if (width >= w) {
				if (r <= ratio) {// 页面限制宽度更小
					width = w;
					height = width / ratio;
				}
				else {
					height = h;
					width = height * r;
				}
			}
			else {
				if (height > h) {
					height = h;
					width = height * ratio;
				}
			}
		}
		else {
			// 高度处理
			if (height > h) {
				if (r >= ratio) {// 页面限制高度更小
					height = h;
					width = height * ratio;
				}
				else {
					width = w;
					height = width / r;
				}
			}
			else {
				if (width > w) {
					width = w;
					height = width / ratio;
				}
			}
		}
		return new int[] { (int) width, (int) height };
	}
	
	/**
	 * 获取图片实际尺寸
	 * 
	 * @param args
	 * @return
	 */
	private static int[] getImgSize(String args) {
		InputStream is = null;
		try {
			is = new URL(args).openStream();
			BufferedImage sourceImg = null;
			sourceImg = javax.imageio.ImageIO.read(is);
			return new int[] { sourceImg.getWidth(), sourceImg.getHeight() };
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		return new int[] { 0, 0 };
	}
	
	/**
	 * 产生随机字符串,默认25个字符
	 * 
	 * @return
	 */
	public static String getRandomString() {
		return getRandomStringBySpecifyLength(25);
	}
	
	/**
	 * 根据指定长度生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomStringBySpecifyLength(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int randomIndex = (int) (Math.random() * CHARSEQ.length());
			sb.append(CHARSEQ.charAt(randomIndex));
		}
		return sb.toString();
	}
	
	/**
	 * 指定长度截取字符串
	 * 
	 * @param src
	 * @param start
	 * @param length
	 * @return
	 */
	public static String subStr(String src, int start, int length) {
		if (src == null)
			throw new NullPointerException("参数字符串'src'不存在！");
		if (src.length() == 0)
			return src;
		return src.substring(start, length + start);
	}
	
	/**
	 * 生成【年月日时分秒毫秒】时间戳。
	 * 
	 * @return
	 */
	public static String getTimesString() {
		sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
	
	public static String getNowDate() {
		sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取当前天组成的字符串
	 */
	public static String getDataString() {
		sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}
	
	/**
	 *  根据指定的格式格式化时间
	 * @param date
	 * @param formatValue such as "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String formatDate(Date date,String formatValue){
		sdf = new SimpleDateFormat(formatValue);
		return sdf.format(date);
	}
}
