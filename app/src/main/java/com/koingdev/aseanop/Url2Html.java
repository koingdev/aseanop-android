package com.koingdev.aseanop;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by SSK on 07-Jun-17.
 */

class Url2Html extends AsyncTask<String, String, String>{
	String name;
	Context context;
	public Url2Html(String name, Context context){
		this.name = name;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		URL url;
		try {
			// get URL content
			url = new URL(params[0]);
			URLConnection conn = url.openConnection();
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
//			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File myDir =  new File(Environment.getExternalStorageDirectory() + "/aseanop-saved-files");
			if(!myDir.exists()){
				myDir.mkdirs();
			}
			//save to this filename
//			String fileName = path + "/" + name + ".html";
			String fileName = name + ".html";
			File file = new File(myDir, fileName);
			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			//System.out.println("SSKFILE: " + file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
			}
			bw.close();
			br.close();
		} catch(Exception ex){
		}
		return null;
	}
}

