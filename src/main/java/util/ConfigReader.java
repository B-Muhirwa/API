package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	
	public static Properties prop;
	public static String getProperty(String key) {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream("src\\main\\java\\util\\config.properties");
			prop.load(fis);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return prop.getProperty(key);
		
	}

}
