package com.flylikewind.mobilesecurityguard.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownloadHelper {

	public static File getApkFile(String url, ProgressDialog pd){
		//下载 读到sd 把文件返回回去
		int last = url.lastIndexOf("/");
		File file = new File(Environment.getExternalStorageDirectory(), url.substring(last+1));
		try {
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			if(con.getResponseCode()==200){
				pd.setMax(con.getContentLength());
				int num=0;
				InputStream is = con.getInputStream();
				FileOutputStream os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len;
				while((len=is.read(buffer))!=-1){
					os.write(buffer, 0, len);
					num += len;
					Thread.sleep(30);
					pd.setProgress(num);
				}
				os.flush();
				os.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	
}
