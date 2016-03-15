package com.flylikewind.mobilesecurityguard.utils;

import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import com.flylikewind.mobilesecurityguard.bean.UpdateInfo;

public class XmlParseUtil {

	/**
	 * 解析xml
	 * 
	 * @param inputStream
	 * @return
	 */
	public static UpdateInfo getUpdataInfo(InputStream inputStream) {
		// 拿到解析器 初始化 拿到事件类型 如果是制定内容就放到bean中
		XmlPullParser parser = Xml.newPullParser();
		UpdateInfo bean = new UpdateInfo();
		try {
			parser.setInput(inputStream, "UTF-8");
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						bean.setVersion(parser.nextText());
					} else if ("description".equals(parser.getName())) {
						bean.setDes(parser.nextText());
					} else if ("apkurl".equals(parser.getName())) {
						bean.setApkUrl(parser.nextText());
					}
					break;
				}
				type = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
