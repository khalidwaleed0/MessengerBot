package MessengerBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AutoReplySettings {
private static AutoReplySettings arSettings = null;
	public static String generalReply;
	public static String importantReply;
	public static AutoReplySettings Singleton() 
    { 
        if (arSettings == null) 
        { 
            arSettings = new AutoReplySettings(); 
        } 
        return arSettings; 
    }
	
	public void checkBasicReplySettings()
	{
		File basicSettings = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\basicReplySettings.txt");
		if(!basicSettings.exists())
		{
			String generalReply = "this is a programmed bot, I'm busy now so if you really need me please type the word important";
			String importantReply = "OK I'll inform him as fast as possible";
			submitBasicReplySettings(generalReply, importantReply);
		}
		else
			readBasicSettings();
	}
	
	public void submitBasicReplySettings(String generalReply,String importantReply)
	{
		try {
			FileWriter writer = new FileWriter(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\basicReplySettings.txt"));
			writer.write("general:"+generalReply+"\nimportant:"+importantReply);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		readBasicSettings();
	}
	public void readBasicSettings()
	{
		try {
			FileReader reader = new FileReader(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\basicReplySettings.txt"));
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			generalReply = line.replace("general:", "");
			line = br.readLine();
			importantReply = line.replace("important:", "");
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
//	public boolean checkAdvancedReplySettings()
//	{
//		File advancedSettings = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\advancedReplySettings.txt");
//		
//	}
}	
