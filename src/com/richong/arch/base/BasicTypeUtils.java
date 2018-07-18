package com.richong.arch.base;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;

import com.rskytech.ComacConstants;


/**
 * 基础类型的操作，如字符串空白信息的处理，替换，分割，日期，格式转换，MD5加密算法<br>
 * 
 * 因为是工具类，每个操作的方法给出了举例用法，详见每个方法的说明，另后续会不断补充说明
 * 
 * @author 侯青春
 * 
 */
public class BasicTypeUtils {

	/**
	 * 禁止外部实例化
	 */
	private BasicTypeUtils() {

	}

	/**
	 * 常量空白字符串
	 */
	public static final String EMPTY_STR = "";
	public static final String SQL_EMPTY_STR = "null";

	/**
	 * 长日期格式
	 */
	public static final String LONG_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 短日期格式
	 */
	public static final String SHORT_DATE_FMT = "yyyy-MM-dd";

	/**
	 * 格式化金额为两位数字
	 * 
	 * @param parml
	 * @return
	 */
	public static String formatNumberWtDigatls(Double parml) {
		if (null == parml) {
			return "";
		}
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("###0.00");
		return df.format(parml);
	}

	/**
	 * 取得当前的日期格式的日期
	 * 
	 * @return 返回当前日期格式的日期
	 */
	public static Date getCurrentDateforSQL() {
		return new Date();
	}
	
	/**
	 * 返回给定日期所在月的最后一天
	 * 
	 * @param date
	 *            指定的日期
	 * @return 所在月的最后一天
	 */
	@SuppressWarnings("static-access")
	public static Date getMonthLastDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar(Locale.CHINA);
		Date date2 = date;
		String sDate = BasicTypeUtils.getShortFmtDate(date2);
		int year = Integer.parseInt(sDate.substring(0, 4));
		int month = Integer.parseInt(sDate.substring(5, 7));
		gc.set(year, month, 1, 0, 0);
		gc.add(gc.MONDAY, 0);
		gc.add(gc.SECOND, -59);
		return gc.getTime();
	}

	/**
	 * 根据指定的日期返回年/月/日的数值
	 * 
	 * @param date
	 * @param type
	 * @return
	 */
	public static int getYMDFromDate(Date date, String type) {
		String sDate = BasicTypeUtils.getShortFmtDate(date);
		if ("YEAR".equals(type)) {
			return Integer.parseInt(sDate.substring(0, 4));
		} else if ("MONTH".equals(type)) {
			return Integer.parseInt(sDate.substring(5, 7));
		} else {
			return Integer.parseInt(sDate.substring(8, 10));
		}
	}

	/**
	 * 取得当前的Timestamp给写入数据库使用
	 * 
	 * @return
	 */
	public static Timestamp getCurrentDateTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 将TimesStamp和当前的日期进行比较
	 * 
	 * @param timestamp
	 * @return 相同返回0，给定日期大于当前日期，返回1，反正返回-1
	 */
	public static int compareDateWtCurrentDate(Timestamp timestamp) {
		Timestamp nowTimestamp = getCurrentDateTimestamp();
		if (nowTimestamp.equals(timestamp)) {
			return 0;
		} else if (nowTimestamp.after(timestamp)) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * 检查文件格式是否支持
	 * @return true 支持 false 
	 */
	public static boolean checkDocType(String docContentType,String docFileFileName){
		if (docContentType != null && ComacConstants.ALLOW_DOC_FILE_TYPE.indexOf(docContentType) > -1){
			if(!docFileFileName.matches(ComacConstants.REG_FILE_SUFFIX)){//正则判断文件后缀名
				return false;
			}
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 检查文件大小
	 * @param doc 上传文件对象
	 * @return true 合格 false 超长
	 */
	public static boolean checkDocLength(File doc){
		if (doc.length() > ComacConstants.ALLOW_DOC_FILE_SIZE){
			return false;
		}else{
			return true;
		}		
	}
	/**
	 * 产生简易菜单
	 * 
	 * @param myMenu
	 * @param path
	 * @param url
	 * @return
	 */
	public static String getMenuHyperLink(String[] myMenu, String path,
			String url) {
		String origiaPageURL = myMenu[0];
		myMenu[0] = myMenu[0].substring(0, myMenu[0].indexOf("."));
		if (url.indexOf(myMenu[0]) != -1) {
			return "<a href=" + path + origiaPageURL + " class='current'>"
					+ myMenu[1] + "</a>";
		} else {
			return "<a href=" + path + origiaPageURL + " class='dot'>"
					+ myMenu[1] + "</a>";
		}
	}

	/**
	 * 取得差值日期的时间，共数据库比较使用
	 * 
	 * @param days
	 * @return
	 */
	public static Timestamp getAddDaysTimestamp(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 取得当前给予日期的差值的新日期
	 * 
	 * @param parmlDate
	 * @param days
	 * @return
	 */
	public static Timestamp getAddDaysTimestamp(Timestamp parmlDate, int days) {
		Long newParmlDate = parmlDate.getTime() + days * 24 * 60 * 60 * 1000;
		return new Timestamp(newParmlDate);
	}

	/**
	 * 取得当前今天的最后一刻的Timstamp
	 * 
	 * @return
	 */
	public static Timestamp getThisDayBylastMinuteTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);

		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 取得当前短日期的Timestamp
	 * 
	 * @return
	 */
	public static Timestamp getShortFmtCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 取得给定字符串的日前函数 格式：yyyy-MM-dd
	 * 
	 * @param parml
	 *            设定的字符串
	 * @return 返回当前的字符串的日期格式
	 * @throws ParseException
	 *             日期格式不符合当前的格式
	 */
	public static Date getCurrentDateforSQL(String parml) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FMT);
		return sdf.parse(parml);
	}

	/**
	 * 取得当前长格式的日期字符串 <br>
	 * 用法举例： BasicTypeUtils.getLongFmtDate()= "2009-12-22"
	 * 
	 * @return String 返回当前长格式的日期字符串
	 */
	public static String getLongFmtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FMT);
		return sdf.format(new Date());
	}

	/**
	 * 取得给定日期的长格式的日期字符串 <br>
	 * 用法举例： BasicTypeUtils.getLongFmtDate(Date date)= "2009-12-22 16:43:00"
	 * 
	 * @return String 返回当前长格式的日期字符串
	 */
	public static String getLongFmtDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FMT);
		return sdf.format(date);
	}

	/**
	 * 取得当前短格式的日期字符串 <br>
	 * 用法举例： BasicTypeUtils.getShortFmtDate()= "2009-12-22 16:43:00"
	 * 
	 * @return String 返回当前短格式的日期字符串
	 */
	public static String getShortFmtDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FMT);
		return sdf.format(new Date());
	}

	/**
	 * 取得指定日期的短格式日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getShortFmtDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FMT);
		return sdf.format(date);
	}

	/**
	 * 整数类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的整数
	 * 
	 * @return String 整数类型转换字符型
	 */
	public static String parseToString(int parml) {
		return String.valueOf(parml);
	}

	/**
	 * 双精度类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的双精度
	 * 
	 * @return String 双精度类型转换字符型
	 */
	public static String parseToString(double parml) {
		return String.valueOf(parml);
	}

	/**
	 * 长整类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的长整数
	 * 
	 * @return String 长整数类型转换字符型
	 */
	public static String parseToString(long parml) {
		return String.valueOf(parml);
	}

	/**
	 * stringBuffer类型转换字符型<br>
	 * 用法举例： BasicTypeUtils.parseToString(0)= "0"
	 * 
	 * @param parml
	 *            需要转换的stringBuffer
	 * 
	 * @return String stringBuffer类型转换字符型
	 */
	public static String parseToString(StringBuffer parml) {
		return String.valueOf(parml);
	}

	/**
	 * 字符串转换为双精度类型 <br>
	 * 用法举例： BasicTypeUtils.parseToDouble("1.0005")= 1.005
	 * 
	 * @param parml
	 *            需要转换的字符串
	 * @return double 返回双精度类型的转换结果
	 */
	public static double parseToDouble(String parml) {
		return NumberUtils.toDouble(parml);
	}

	/**
	 * 字符串转换为长整型 <br>
	 * 用法举例： BasicTypeUtils.parseLong("0")= 0
	 * 
	 * @param parml
	 *            需要转换的字符串
	 * @return String 返回的长整类型的转换结果
	 */
	public static long parseLong(String parml) {
		return NumberUtils.toLong(parml);
	}

	/**
	 * 字符串转换为整数型 <br>
	 * 用法举例： BasicTypeUtils.parseInt("0")= 0
	 * 
	 * @param parml
	 *            需要转换的字符串
	 * @return String 返回的整数类型的转换结果
	 */
	public static int parseInt(String parml) {
		return NumberUtils.toInt(parml);
	}

	/**
	 * 判断当前的字符串是否为空或者为Null<br>
	 * 用法举例： BasicTypeUtils.isNullorBlank("")= true
	 * 
	 * @param parml
	 *            需要检查的字符串
	 * @return Boolean 返回是否为空或者为Null
	 */
	public static boolean isNullorBlank(String parml) {
		if (parml == null){
			return true;
		}else if (parml.trim().equals(EMPTY_STR)){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 计算一个字符或字符串在另外一个字符串出现的次数
	 * 
	 * @return 返回该字符或该字符串在另外一个字符串出现的次数
	 */
	public static int getCount(String str, String sub) {
		return StringUtils.countMatches(str, sub);
	}

	/**
	 * 判断该字符串是否全为汉字
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean isGB2312(String str) {
		for (int i = 0; i < str.length(); i++) {
			String bb = str.substring(i, i + 1);
			// 生成一个Pattern,同时编译一个正则表达式
			boolean cc = java.util.regex.Pattern.matches("[\u4E00-\u9FA5]", bb);
			if (cc == false) {
				return cc;
			}
		}
		return true;

	}

	/**
	 * 对字符串进行取空格处理，对Null并转换为空值 <br>
	 * 用法举例： BasicTypeUtils.notNull("ABC ")= "ABC"
	 * 
	 * @param parml
	 *            需要处理的字符串
	 * @return 对格式处理后的字符串
	 */
	public static String notNull(String parml) {
		return StringUtils.trimToEmpty(parml);
	}

	/**
	 * 判断传入的Long变量是否有值
	 * 
	 * @param longNumber
	 *            需要判断的变量
	 * @return 返回是否为空
	 */
	public static boolean isNullNumber(Long longNumber) {
		return (longNumber == null);
	}

	/**
	 * 产生一个给定长度的字符串 <br>
	 * 用法举例： BasicTypeUtils.leftPad("1",5,"*")= "****5"
	 * 
	 * @param parml
	 *            给定需要处理的字符串
	 * @param size
	 *            需要产生的长度
	 * @return 产生的最终字符串
	 */
	public static String leftPad(String parml, int size, String padChar) {
		return StringUtils.leftPad(parml, size, padChar);
	}

	/**
	 * 对字符串进行MD5加密 <br>
	 * 用法举例： BasicTypeUtils.MD5("")="d41d8cd98f00b204e9800998ecf8427e"
	 * 
	 * @param parml
	 *            需要加密的字符串
	 * @return 对字符串进行MD5加密后的值
	 */
	public static String MD5(String parml) {
		return DigestUtils.md5Hex(notNull(parml));
	}

	/**
	 * 对字符串进行空值分割，并进行第一个字母的大写处理<br>
	 * 用法举例： BasicTypeUtils.leftPad("hello world")= "Hello World"
	 * 
	 * @param parml
	 *            需要首字母处理的字符串
	 * @return 首字母大写处理后的字符串
	 */
	public static String firstCharToUpperCase(String parml) {
		return WordUtils.capitalize(parml);

	}

	/**
	 * 对字符串进行去除HTML的操作 <br>
	 * 用法举例： BasicTypeUtils.leftPad("<a></a>")= "&lt;a&gt;&lt;/a&gt;"
	 * 
	 * @param parml
	 *            需要去除HTML的字符串
	 * @return 去除HTML标签后的字符串
	 */
	public static String escapeHtmlWords(String parml) {
		return StringEscapeUtils.escapeHtml(parml);
	}

	/**
	 * 对字符串进行SQL标记的操作 <br>
	 * 用法举例： BasicTypeUtils.escepeSQLwords("A ' ")= "A '' "
	 * 
	 * @param parml
	 *            需要去除SQL标记的字符串
	 * @return 去除SQL标记后的字符串
	 */
	public static String escepeSQLwords(String parml) {
		return StringEscapeUtils.escapeSql(parml);
	}

	/**
	 * 
	 * 在给定的字符串内进行查找特定字符串，并换成对应的字符串 <br>
	 * 用法举例： BasicTypeUtils.leftPad("abc|ABC|cde","|","*")= "abc*ABC*cde"
	 * 
	 * @param parml
	 * @param searchString
	 * @param replacement
	 * @return
	 */
	public static String replace(String parml, String searchString,
			String replacement) {
		return StringUtils.replace(parml, searchString, replacement);
	}

	/**
	 * 
	 * 对给定的字符串以给定的字符进行分割<br>
	 * 用法举例： BasicTypeUtils.leftPad("abc|ABC|cde","|")={"abc","ABC,"cde"}
	 * 
	 * @param parml
	 * @param separatorChar
	 * @return Array
	 */
	public static String[] split(String parml, String separatorChar) {
		return StringUtils.split(parml, separatorChar);
	}

	/**
	 * 取得一个给定长度的数字字符混合字符串 <br>
	 * 用法举例：
	 * BasicTypeUtils.randomAlphanumeric(100)="wSDtzWYHp0EDEgvM9NyNbgVYeC5zXDyx8hQqODptHVJyWvzjTa7c01jZJfDK3PXj8hrN3XWALQ3Q129tQtv4OCEdUxFluELYzhBk"
	 * 
	 * @param count
	 *            需要产生随机字符串的长度
	 * @return 产生的给定长度的数字字符随机字符串
	 */
	public static String randomAlphanumeric(int count) {
		return RandomStringUtils.randomAlphanumeric(count);
	}

	/**
	 * 取得一个给定长度的纯数字的随机字符串 <br>
	 * 用法举例：
	 * BasicTypeUtils.randomNumeric(100)="9811849487110345870189117021421446235298267390404127961970424649355464106939895627617950304876536495"
	 * 
	 * @param count
	 *            需要产生随机数字的长度
	 * @return String 产生的给定长度的数字字符随机字符串
	 */
	public static String randomNumeric(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	/**
	 * 取得一个给定长度的纯字符的随机字符串 <br>
	 * 用法举例：
	 * BasicTypeUtils.randomAlphabetic(100)="GeGgIDMKoTamhCERPfQipnDJCIRXLViDJftJElTKPxxUhqzhEkFhRiItujlZmKHFYUGFkdnIhZPFILismVVyVCOlniOTNJhOEglP"
	 * 
	 * @param count
	 *            需要产生随机数字的长度
	 * @return String 产生的给定长度的纯字符型的随机字符串
	 */
	public static String randomAlphabetic(int count) {
		return RandomStringUtils.randomAlphabetic(count);
	}

	/**
	 * 判断给定的字符串是否为数字
	 * @param number 字符串形式的数字，如"123"
	 * @return
	 */
	public static Boolean isNumberString(String number){
		if(BasicTypeUtils.isNullorBlank(number)){
			return false;
		}
		if(number.matches("\\d*"))
		{
			return true; 
		}else{
			return false;
		}
	}

	/**
	 * 计算当前data进行换算大小，如m,g,t,k等
	 * 
	 * @param dataSize
	 * @return
	 */
	public static String sizeFormat(long dataSize) {
		if (dataSize >= 1073741824) {
			return ((double) Math.round(dataSize / 1073741824d * 100) / 100)
					+ " GB";
		} else if (dataSize >= 1048576) {
			return ((double) Math.round(dataSize / 1048576d * 100) / 100)
					+ " MB";
		} else if (dataSize >= 1024) {
			return ((double) Math.round(dataSize / 1024d * 100) / 100) + " KB";
		} else {
			return dataSize + " Bytes";
		}
	}

	/**
	 * 将给定字符串的首字母转为大写
	 * @param string
	 * @return
	 */
	public static String upperFirstChar(String string) {
		return string.replaceFirst(string.substring(0, 1), string.substring(0,
				1).toUpperCase());
	}
	
	/**
	 * 检查图片大小
	 * @param doc 上传文件对象
	 * @return true 合格 false 超长
	 */
	public static boolean checkImgLength(File doc){
		if (doc.length() > ComacConstants.ALLOW_IMAGE_FILE_SIZE){
			return false;
		}else{
			return true;
		}		
	}
	
	/**
	 * 检查图片格式是否支持
	 * @param 文件格式
	 * @return true 支持 false 
	 */
	public static boolean checkImgType(String docContentType){
		String all=ComacConstants.ALLOW_IMAGE_FILE_TYPE;
		String type=docContentType;
		int num=all.indexOf(type);
		if (docContentType != null &&num>=0 ){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 如果给定的整型值为空值，转为0返回
	 * @param number
	 * @return
	 */
	public static Integer notNullInteger(Integer number){
		if(number==null){
			return 0;
		}
		return number;
	}
	
	public static String delZeroString(String str){
		int j=0;
		for(int i=0;i<str.length();i++){
			if(!"0".equals(str.substring(i, i+1))){
				j=i;
				break;
			}
		}
		return str.substring(j);
	}
	
	/**
	 * 判断给定的整型是否为有效的ID，有效的ID是大于0的整数
	 * @param id
	 * @return
	 *//*
	public static boolean isValidId(Integer id){
		if(id==null || id<1){
			return false;
		}
		return true;
	}*/

////////////////////////////////////////////////////常毅飞创建begin/////////////////////////////////	
	/**
	 * 为实体对象的指定字段赋值  
	 * @param objEntity 实体对象 
	 * @param objFiledNm 实体类中的指定字段名
	 * @param objValue 要给实体指定字段赋的值 
	 * @author changyf
	 * createdate 2011-09-02
	 */
	public static void setEntityObjValue(Object objEntity, Object objFiledNm, Object objValue) {

		// 取得实体类的Class对象
		Class<?> srcClass = objEntity.getClass();
		// 取得类的指定字段
		Field field = getClassField(srcClass, objFiledNm);
		// 指定类中存在指定名称的字段时
		if (field != null) {

			try {
				String strMethodNm = getSetMethod(objFiledNm.toString().trim());
				// 取得指定名称的方法对象
				Method method = srcClass.getMethod(strMethodNm, field.getType());
				// 为类中指定方法设定指定的值
				method.invoke(objEntity, objValue);
				//objEntity.setName(objValue)
			} catch (SecurityException e) {
				e.printStackTrace();				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获得实体对象的指定字段的值  
	 * @param objEntity 实体对象 
	 * @param objFiledNm 实体类中的指定字段名
	 * @author changyf
	 * createdate 2011-09-02
	 */
	public static Object getEntityObjValue(Object objEntity, Object objFiledNm){
		// 取得实体类的Class对象
		Class<?> srcClass = objEntity.getClass();
		// 取得类的指定字段
		Field field = getClassField(srcClass, objFiledNm);
		// 指定类中存在指定名称的字段时
		if (field != null) {

			try {
				String strMethodNm = getGetMethod(objFiledNm.toString().trim());
				// 取得指定名称的方法对象
				Method method = srcClass.getMethod(strMethodNm);
				// 为类中指定方法设定指定的值
				return method.invoke(objEntity);
			} catch (SecurityException e) {
				e.printStackTrace();				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 根据属性名获取set方法
	 * @param strFieldNm 属性名
	 * @return set方法名
	 * @author changyf
	 * createdate 2011-09-02
	 */
	private static String getSetMethod(String strFieldNm) {

		if (BasicTypeUtils.isNullorBlank(strFieldNm)) {
			return null;
		}
		return "set" + strFieldNm.substring(0, 1).toUpperCase() + strFieldNm.substring(1);	
	}
	
	/**
	 * 根据属性名获取get方法
	 * @param strFieldNm 属性名
	 * @return set方法名
	 * @author changyf
	 * createdate 2011-09-02
	 */
	private static String getGetMethod(String strFieldNm) {

		if (BasicTypeUtils.isNullorBlank(strFieldNm)) {
			return null;
		}
		return "get" + strFieldNm.substring(0, 1).toUpperCase() + strFieldNm.substring(1);	
	}
	
	/**
	 * 取得类的指定字段 
	 * @param srcClass 类对象 
	 * @param objFieldNm 字段名称 
	 * @return 字段对象 
	 * @author changyf
	 * createdate 2011-09-02
	 */
	@SuppressWarnings("rawtypes")
	private static Field getClassField(Class srcClass, Object objFieldNm) {
		Field field = null;
		try {
			field = srcClass.getDeclaredField(objFieldNm.toString());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return field;
	}
	
	public static String getServletContextPath(){
		return ServletActionContext.getServletContext().getRealPath("/");
	}
	
	/**
	 * 去除ArrayList中的重复值
	 * @param arlList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicate(ArrayList arlList) {
		HashSet h = new HashSet(arlList);
		arlList.clear();
		arlList.addAll(h);
	} 
	

	/**
	 *获取中英文语言文件
	 * @param language
	 * @param fileName
	 * @return
	 * @author chendexu
	 * createdate 2012-11-12
	 */
	public static ResourceBundle getProperties(String language, String fileName ){
		Locale locale = Locale.CHINA;
		
		
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resource/"+fileName,locale);
		return resourceBundle;
	}
	
	
	/**
	 * 对integerlist进行升序排序
	 * @param list
	 * @return
	 * @author changyf
	 * createdate 2012-08-22 
	 */
	public static ArrayList<Integer> sortIntegerListAsc(ArrayList<Integer> list){
	      	Object[] array = list.toArray();
	        for(int i = 0; i < array.length; i++){
	        	for(int j = i + 1 ; j < array.length; j++){
	        		Integer valueI = BasicTypeUtils.parseInt(array[i].toString());
	        		Integer valueJ = BasicTypeUtils.parseInt(array[j].toString());
	        		if (valueI > valueJ){
	        			Object temp = array[i];
	        			array[i] = array[j];
	        			array[j] = temp; 
	        		}
	        	}
	        }
	        ArrayList<Integer> sortList = new ArrayList<Integer>();
	        for(int i = 0 ; i < array.length; i++){
	        	sortList.add(BasicTypeUtils.parseInt(array[i].toString()));
	        }
	        return sortList;
	}
//////////////////////////////////////////////////常毅飞创建end/////////////////////////////////
	/**
	 * 根据传入的字符串转换成二维float数组
	 * @param str
	 * @return int型数组
	 * @author wubo
	 * createdate 2012-11-7
	 */
	public static float[][] GetFloatArray(String str){
		String[] strArr = str.split(",");
		float[][] floatArr = new float[strArr.length][1] ;
		for (int i = 0; i < strArr.length; i++) {
			try{
				floatArr[i][0] = Float.parseFloat(strArr[i]);
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return floatArr;
	}

	/**
	 * 根据传入的字符串转换成二维double数组
	 * @param str
	 * @return double型数组
	 * @author wubo
	 * createdate 2012-11-7
	 */
	public static double[][] GetDoubleArray2D(String str){
		str = str.replaceAll("，",","); 
		String[] strArr = str.split(",");
		double[][] doubleArr = new double[strArr.length][1] ;
		for (int i = 0; i < strArr.length; i++) {
			try{
				doubleArr[i][0] = Double.parseDouble(strArr[i].toString());
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return doubleArr;
	}
	
	/**
	 * 根据传入的字符串转换成一维double数组
	 * @param str
	 * @return double型数组
	 * @author wubo
	 * createdate 2012-11-7
	 */
	public static double[] GetDoubleArray(String str){
		String[] strArr = str.split(",");
		double[] doubleArr = new double[strArr.length] ;
		for (int i = 0; i < strArr.length; i++) {
			try{
				doubleArr[i] = Double.parseDouble(strArr[i]);
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return doubleArr;
	}
	
	/**
	 * 检查文件名长度是否超长
	 * @param docFileName 上传文件名
	 * @return true 未超长 false 超长
	 */
	public static boolean checkFileNameLength(String docFileName){
		if (docFileName.length() > 40){// 文件名大小不能超过40 
			return false;
		}else{
			return true;
		}
	}
}
