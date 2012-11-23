package test;
import java.io.InputStream;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;

import cn.hpc.android.res.AXMLParser;
import cn.hpc.android.util.AnalysisApk;


public class AXMLTest {

	public static void testApkInfo() {
		// TODO Auto-generated method stub
		System.out.println("main--------");
		String apkInfo[] = AnalysisApk.getApkFileInfo("/home/hpc/tmp/成语百事通.apk");
		System.out.println(apkInfo.toString());
		System.out.println("code: " + Integer.parseInt(apkInfo[0]) + ", versionName:" +  apkInfo[1] + ", package:" + apkInfo[2]);

	}
	public static void main(String[] arguments) {
		testApkInfo();
		try {
			String apkfile = "/home/hpc/eben-release/ICWidget.apk";
//			ZipFile zip=new ZipFile("/home/hpc/tmp/成语百事通.apk");
			ZipFile zip=new ZipFile(apkfile);
			InputStream stream=zip.getInputStream(zip.getEntry("AndroidManifest.xml"));
			
			AXMLParser parser=new AXMLParser(stream);
//					//"main.xml"
//					"keyguard_screen_sim_pin_portrait.xml" 
//					));
			StringBuilder indent=new StringBuilder(10);
			final String indentStep="    ";
			while (true) {
				int type=parser.next();
				//log(XmlPullParser.TYPES[type]);
				if (type==XmlPullParser.END_DOCUMENT) {
					break;
				}
				switch (type) {
					case XmlPullParser.START_DOCUMENT:
					{
						break;
					}
					case XmlPullParser.START_TAG:
					{
						log("%s<%s",indent,parser.getName());
						indent.append(indentStep);
						for (int i=0;i!=parser.getAttributeCount();++i) {
							log("%s%s%s=%s",
								indent,
								makeNamespacePrefix(parser.getAttributeNamespace(i)),
								parser.getAttributeName(i),
								makeAttributeValue(parser,i));
							if ("android:".equals(makeNamespacePrefix(parser.getAttributeNamespace(i))) && "versionCode".equals(parser.getAttributeName(i))){
								log("versionCode: %s", makeAttributeValue(parser,i));
//								break;
								return;
							}
//							log("    %s%s=%s [nr=%08X] (v=%08X) [vt=%08X]",
//									makeNamespacePrefix(parser.getAttributeNamespace(i)),
//									parser.getAttributeName(i),
//									parser.getAttributeValueString(i),
//									parser.getAttributeNameResourceID(i),
//									parser.getAttributeValue(i),
//									parser.getAttributeValueType(i));
						}
						log("%s>",indent);
						break;
					}
					case XmlPullParser.END_TAG:
					{
						indent.setLength(indent.length()-indentStep.length());
						log("%s</%s>",indent,parser.getName());
						break;
					}
					case XmlPullParser.TEXT:
					{
						log(parser.getName());
						break;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String makeNamespacePrefix(String namespace) {
		if (namespace==null || namespace.length()==0) {
			return "";
		}
		if (namespace.equals("http://schemas.android.com/apk/res/android")) {
			return "android:";
		}
		return namespace+":";		
	}
	
	private static String makeAttributeValue(AXMLParser parser,int index) {
		int type=parser.getAttributeValueType(index);
		if (type==0x03) {
			return '"'+parser.getAttributeValueString(index)+'"';
		}
		if (0x10 == type) {
			return String.format("\"%d\"", parser.getAttributeValue(index));
		}
		return String.format(
			"<0x%X, type 0x%02X>",
			parser.getAttributeValue(index),
			type);
	}
	

	private static void log(String format,Object...arguments) {
		System.out.printf(format,arguments);
		System.out.println();
	}
	
}

//	
//	public static void main(String[] arguments) {
//		
//		try {
//			RandomAccessFile input=new RandomAccessFile(
//					//"settings_view.xml",
//					//"test.xml",
//					"main.xml",
//					"r");
////			InputStream stream=new FileInputStream(
////					"settings_view.xml");
////					//"connecting.xml");
//			int header=readInt(input);
//			int length=readInt(input);
//			
//			int unknown1=readInt(input);
//			int someLength=readInt(input);
//			int stringCount=readInt(input);
//			int unknown2=readInt(input);
//			int unknown3=readInt(input);
//			int unknown4=readInt(input);
//			int unknown5=readInt(input);
//			
//			strings=new String[stringCount];
//			{
//				int offsets[]=new int[stringCount];
//				for (int i=0;i!=stringCount;++i) {
//					offsets[i]=readInt(input);
//				}
//				//Arrays.sort(stringOffsets);
//				
//				long baseOffset=input.getFilePointer();
//				long errorOffset=0;
//				for (int c=0;c!=stringCount;++c) {
//					long offset=input.getFilePointer()-baseOffset-errorOffset;
//					int index=0;
//					for (;index!=stringCount;++index) {
//						if (offsets[index]==offset) {
//							break;
//						}
//						if (offsets[index]==(offset-1)) {
//							errorOffset+=1;
//							break;
//						}
//					}
//					if (index==stringCount) {
//						log("Whoops!");
//					}
//					strings[index]=readString(input);
//				}
//			}
//			
//			{
//				int padding=(int)(input.getFilePointer() % 4);
//				input.skipBytes(padding);
//			}
//			
//			int sign=readInt(input);
//			int resourceIDLength=readInt(input);
//			int[] resourceIDs=new int[resourceIDLength/4];
//			for (int i=0;i!=resourceIDs.length;++i) {
//				resourceIDs[i]=readInt(input);
//			}
//			readInt(input,resourceIDLength%4);
//			
//			int x=readInt(input);
//			int y=readInt(input);
//			
//			
//			header=length;
//			
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	private static String[] strings;
//	
//	private static final int
//		TAG_OPEN			=0x10,
//		TAG_HAS_CHILDREN	=0x100000,
//		TAG_NEXT			=0x08;
//	
//	private static final void readTag(RandomAccessFile input) throws IOException {
//		int name=readInt(input);
//		int flags=readInt(input);
//		log("Tag: %s (%d) @ 0x%08X",strings[name],name,input.getFilePointer());
//		if ((flags & TAG_OPEN)!=0 && (flags & TAG_HAS_CHILDREN)!=0) {
//			int attributeCount=readInt(input);
//			int unknown=readInt(input);
//			log(" attribute count: %d, unknown: %d",attributeCount,unknown);
//			for (;attributeCount!=0;attributeCount--) {
//				int ns=readInt(input);
//				if (ns!=-1) {
//					log(" attribute namespace: %s",strings[ns]);
//				}
//				int attributeName=readInt(input);
//				int attributeValue=readInt(input);
//				log(" attribute: %s = %s",strings[attributeName],strings[attributeValue]);
//				int attributeFlags=readInt(input);
//				
//				int padding=readInt(input);
//				input.skipBytes(padding);
//			}
//		} else {
//			int whatever=readInt(input);
//			int huh=readInt(input);
//			
//		}
//	}
//	
//	private static final void readFully(DataInput input,byte[] bytes) throws IOException {
//		input.readFully(bytes);
////		if (input.read(bytes)!=bytes.length) {
////			throw new EOFException();
////		}
//	}
//	
//	private static final String readString(DataInput input) throws IOException {
//		int length=readShort(input);
//		if (length==0x0A0D) {
//			// WTF BUG??
//			length=0x0A | (readInt(input,1)<<8);
//		}
//		StringBuilder builder=new StringBuilder(length);
//		for (int i=0;i!=length;++i) {
//			builder.append((char)readShort(input));
//		}
//		readShort(input);
//		return builder.toString();
//	}
//	
//		private static final int readInt(DataInput input) throws IOException {
//			return readInt(input,4);
//		}
//		private static final int readShort(DataInput input) throws IOException {
//			return readInt(input,2);
//		}
//	
//	private static int m_savedByte=-1;
//	
//	private static final int readInt(DataInput input,int length) throws IOException {
//		int result=0;
//		for (int i=0;i!=length;++i) {
//			int b;
//			if (m_savedByte!=-1) {
//				b=m_savedByte;
//				m_savedByte=-1;
//			} else {
//				b=(0xFF & input.readByte());
//			}
//			result|=(b<<(i*8));
//		}
//		if (result==0x0A0D) {
//			m_savedByte=input.readByte();
//			if (m_savedByte==0) {
//				m_savedByte=-1;
//				result=0x0A;
//			}
//		}
//		return result;		
//	}
//	
//	
//	private static void log(String format,Object...arguments) {
//		System.out.printf(format,arguments);
//		System.out.println();
//	}
//
//}
