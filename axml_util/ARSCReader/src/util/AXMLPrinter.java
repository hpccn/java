/*
 * Copyright 2008 Android4ME
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util;

import java.io.FileInputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;

import cn.hpc.android.res.AXMLParser;


/**
 * @author Dmitry Skiba
 * 
 * This is example usage of AXMLParser class.
 * 
 * Prints xml document from Android's binary xml file.
 * 
 * Resulting document will likely not be a valid xml, because of attributes.
 * String attributes are printed normally, like android:text="Hello", but
 * all other attribute types are printed in form "<[Value], type [Type]>".
 * Type is one of TypedValue.TYPE_ values, and Value is interpreted according to type.
 * For example, type 0x10 is just plain int, and value of that type is likely
 * a resource identifier.
 */
public class AXMLPrinter {

	public static void main(String[] arguments) {
		if (arguments.length<1) {
			log("Usage: AXMLPrinter <binary xml file>");
			return;
		}
		try {
			AXMLParser parser=new AXMLParser(new FileInputStream(arguments[0]));
			StringBuilder indent=new StringBuilder(10);
			final String indentStep="    ";
			while (true) {
				int type=parser.next();
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
						log("%s%s",indent,parser.getName());
						break;
					}
				}
			}
		}
		catch (IOException e) {
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