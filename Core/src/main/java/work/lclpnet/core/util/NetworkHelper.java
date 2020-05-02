package work.lclpnet.core.util;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class NetworkHelper {

	public static boolean downloadFile(String url, String path) {
		try {
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(path);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static BufferedImage loadImageFromUrl(String url) {
		if(url == null) return null;

		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			System.out.println("Error reading image from url '" + url.toString() + "'.");
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> extractUrls(String s) {
		Pattern pattern = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)");
		Matcher matcher = pattern.matcher(s);

		List<String> matching = new ArrayList<>();
		while (matcher.find()) matching.add(s.substring(matcher.start(1), matcher.end(1)));
		
		return matching;
	}
	
}
