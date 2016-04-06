package com.umlparser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class App {

	public static void main(String[] args) throws IOException, URISyntaxException {

		String path = args[0];
		String fileName, ext, fullPath;

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        fileName = listOfFiles[i].getName();

		        ext = fileName.substring(fileName.length() - 4);
		        if(ext.equals("java")){
		        	fullPath = path + fileName;
		        	MyUMLParser mup = new MyUMLParser(fullPath);  //object be removed
		        	mup.testPrint();
		        }
		      }
		    }

		 String finalURL = GeneratedClasses.getInstance().getFinalString();
		 URL url = new URL(finalURL);
		 InputStream in = new BufferedInputStream(url.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();

		 FileOutputStream fos = new FileOutputStream(args[1] + ".png");
		 fos.write(response);
		 fos.close();
		 System.out.println("PNG File created.");

	}
}
