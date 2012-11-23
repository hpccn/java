package test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.zip.ZipFile;

import cn.hpc.android.res.ARSCFile;
import cn.hpc.android.res.ARSCFile.Configuration;
import cn.hpc.android.res.ARSCFile.Content;


public class ARSCTest {

	public static void main1(String[] arguments) {
		try {
			ZipFile zip = new ZipFile("..\\AndroidTest\\bin\\resources.ap_");
			InputStream stream = zip.getInputStream(zip
					.getEntry("resources.arsc"));
			// InputStream stream=new FileInputStream("resources.arsc");

//			ARSCFile resources = ARSCFile.read(stream);

			// for (Folder folder: resources.folders) {
			// for (Content content: folder.contents) {
			// log(resources.folderNames.getRaw(content.folderID-1)+
			// formatConfiguration(content.configuration));
			// }
			// }

			m_printOutput = System.out;
			// m_printOutput=new PrintStream("resources.string2");

//			Folder stringFolder = findFolder(resources, "string");
			// printf("flags(%d)",stringFolder.flags.length);
			// printIntArray(stringFolder.flags,4);
			// printf("");

//			for (Content content : stringFolder.contents) {
//				printf(resources.folderNames.getRaw(content.folderID - 1)
//						+ formatConfiguration(content.configuration));
//				for (int i = 0; i != content.offsets.length; ++i) {
//					int offset = content.offsets[i];
//					if (offset == -1) {
//						continue;
//					}
//					offset /= 4;
//					int type1 = content.data[offset++];
//					int id1 = content.data[offset++];
//					int type2 = content.data[offset++];
//					int id2 = content.data[offset++];
//
////					printf("%08X | %08X %08X %s='%s'", stringFolder.flags[i],
////							type1, type2, resources.stringIDs.getRaw(id1),
////							escapeString(resources.stringValues.getHTML(id2)));
//				}
//				printf("");
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////

	private static String escapeString(String string) {
		if (string == null) {
			return string;
		}
		return string.replace("\n", "\\n");
	}

//	private static Folder findFolder(ARSCFile resources, String name) {
//		for (Folder folder : resources.folders) {
//			if (resources.folderNames.getRaw(folder.id - 1).equals(name)) {
//				return folder;
//			}
//		}
//		return null;
//	}

	// ///////////////////////////////////////////

	private static final void printf(String format, Object... arguments) {
		m_printOutput.printf(format, arguments);
		m_printOutput.println();
	}

	private static final void printIntArray(int[] array, int columns) {
		if (columns == 0) {
			columns = 1;
		}
		for (int i = 0; i != array.length;) {
			StringBuilder line = new StringBuilder(columns * 8);
			for (int c = 0; c != columns && i != array.length; ++c, ++i) {
				if (c != 0) {
					line.append(' ');
				}
				line.append(String.format("%08X", array[i]));
			}
			printf(line.toString());
		}
	}

	private static PrintStream m_printOutput;

	// ///////////////////////////////////////////

	private static String getString(int index, String[] strings) {
		if (index < 0 || index >= strings.length) {
			return "[" + index + "]";
		}
		return strings[index];
	}

	private static String formatConfiguration(Configuration configuration) {
		StringBuilder result = new StringBuilder(16);
		if (configuration.language != null) {
			result.append('-');
			result.append(configuration.language);
		}
		if (configuration.country != null) {
			result.append('-');
			result.append('r');
			result.append(configuration.country);
		}
		if (configuration.orientation != 0) {
			result.append('-');
			result.append(getString(configuration.orientation - 1,
					new String[] { "port", "land", "square" }));
		}
		if (configuration.touchscreen != 0) {
			result.append('-');
			result.append(getString(configuration.touchscreen - 1,
					new String[] { "notouch", "stylus", "finger" }));
		}
		if (configuration.keyboardHidden != 0) {
			result.append('-');
			result.append(getString(configuration.keyboardHidden - 1,
					new String[] { "keysexposed", "keyshidden" }));
		}
		if (configuration.keyboard != 0) {
			result.append('-');
			result.append(getString(configuration.keyboard - 1, new String[] {
					"nokeys", "qwerty", "12key" }));
		}
		if (configuration.navigation != 0) {
			result.append('-');
			result.append(getString(configuration.navigation - 1, new String[] {
					"notouch", "dpad", "trackball", "wheel" }));
		}
		if (configuration.screenWidth != 0 && configuration.screenHeight != 0) {
			result.append('-');
			result.append(Math.max(configuration.screenWidth,
					configuration.screenHeight));
			result.append('x');
			result.append(Math.min(configuration.screenWidth,
					configuration.screenHeight));
		}
		return result.toString();
	}

	private static void log(String format, Object... arguments) {
		System.out.printf(format, arguments);
		System.out.println();
	}
}
