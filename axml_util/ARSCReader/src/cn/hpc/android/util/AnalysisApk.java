package cn.hpc.android.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;

import cn.hpc.android.res.AXMLParser;


public class AnalysisApk {
	/**
	 *使用方法： 	
	 * public static void testApkInfo() {
		// TODO Auto-generated method stub
		System.out.println("main--------");
		String apkInfo[] = AnalysisApk.getApkFileInfo("/home/hpc/tmp/成语百事通.apk");
		System.out.println(apkInfo.toString());
		System.out.println("code: " + Integer.parseInt(apkInfo[0]) + ", versionName:" +  apkInfo[1] + ", package:" + apkInfo[2]);

	}
	 * @param apkFile
	 * @return
	 */
	
	 public static String[] getApkFileInfo(String apkFile)
	   {  
	      //[0]:版本号;[1]:版本名;[2]包名
	      String[] st = new String[3];
	      byte b[] = new byte [1024];
	      int length;
	      ZipFile zipFile;
	      try {
	          zipFile = new ZipFile( new File(apkFile));      
	          Enumeration enumeration = zipFile.entries();
	          ZipEntry zipEntry = null ;          
	          while (enumeration.hasMoreElements()) {
	             zipEntry = (ZipEntry) enumeration.nextElement();          
	             if (zipEntry.isDirectory()) {
	               
	             } else {
	                 if("AndroidManifest.xml".equals(zipEntry.getName()))
	                 {
	                     try {
	                         AXMLParser parser=new AXMLParser(zipFile.getInputStream(zipEntry));
	                         while (true) {
	                             int type=parser.next();
	                             if (type==XmlPullParser.END_DOCUMENT) {
	                                 break;
	                             }
	                             switch (type) {
	                                 case XmlPullParser.START_TAG:
	                                 {
	                                     for (int i=0;i!=parser.getAttributeCount();++i) {
	                                         if("versionCode".equals(parser.getAttributeName(i))){
	                                             st[0] = getAttributeValue(parser,i);
	                                         }else if ("versionName".equals(parser.getAttributeName(i))){
	                                        	 st[1] = getAttributeValue(parser,i);
	                                         }else if("package".equals(parser.getAttributeName(i))){
	                                             st[2] = getAttributeValue(parser,i);
	                                         }
	                                     }
	                                 }
	                             }
	                         }
	                     }
	                     catch (Exception e) {
	                         e.printStackTrace();
	                     }
	                     break;// 找到版本号
	                 }
	                
//	                 if("res/drawable-ldpi/icon.png".equals(zipEntry.getName())){
//	                     OutputStream outputStream = new FileOutputStream(logoUrl);
//	                     InputStream inputStream = zipFile.getInputStream(zipEntry);
//	                     while ((length = inputStream.read(b)) > 0)
//	                        outputStream.write(b, 0, length);
//	                 }
	             }
	          }
	      } catch (IOException e) {
	          // TODO Auto-generated catch block
	          //e.printStackTrace();
	      }
	      return st;
	   }
	  
	   private static String getAttributeValue(AXMLParser parser,int index) {
	        int type=parser.getAttributeValueType(index);
	        int data=parser.getAttributeValue(index);
	        if (type==TypedValue.TYPE_STRING) {
	            return parser.getAttributeValueString(index);
	        }
	        if (type==TypedValue.TYPE_ATTRIBUTE) {
	            return String.format("?%sX",getPackage(data),data);
	        }
	        if (type==TypedValue.TYPE_REFERENCE) {
	            return String.format("@%sX",getPackage(data),data);
	        }
	        if (type==TypedValue.TYPE_FLOAT) {
	            return String.valueOf(Float.intBitsToFloat(data));
	        }
	        if (type==TypedValue.TYPE_INT_HEX) {
	            return String.format("0xX",data);
	        }
	        if (type==TypedValue.TYPE_INT_BOOLEAN) {
	            return data!=0?"true":"false";
	        }
	        if (type==TypedValue.TYPE_DIMENSION) {
	            return Float.toString(complexToFloat(data))+
	                DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
	        }
	        if (type==TypedValue.TYPE_FRACTION) {
	            return Float.toString(complexToFloat(data))+
	                FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
	        }
	        if (type>=TypedValue.TYPE_FIRST_COLOR_INT && type<=TypedValue.TYPE_LAST_COLOR_INT) {
	            return String.format("#X",data);
	        }
	        if (type>=TypedValue.TYPE_FIRST_INT && type<=TypedValue.TYPE_LAST_INT) {
	            return String.valueOf(data);
	        }
	        return String.format("<0x%X, type 0xX>",data,type);
	    }
	  
	   private static String getPackage(int id) {
	        if (id>>>24==1) {
	            return "android:";
	        }
	        return "";
	    }
	  
	   /////////////////////////////////// ILLEGAL STUFF, DONT LOOK :)
	    public static float complexToFloat(int complex) {
	        return (float)(complex & 0xFFFFFF00)*RADIX_MULTS[(complex>>4) & 3];
	    }
	   
	    private static final float RADIX_MULTS[]={
	        0.00390625F,3.051758E-005F,1.192093E-007F,4.656613E-010F
	    };
	    private static final String DIMENSION_UNITS[]={
	        "px","dip","sp","pt","in","mm","",""
	    };
	    private static final String FRACTION_UNITS[]={
	        "%","%p","","","","","",""
	    };
}
