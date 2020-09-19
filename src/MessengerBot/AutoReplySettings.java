package MessengerBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class AutoReplySettings {
private static AutoReplySettings arSettings = null;
	public static String generalReply;
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
			String generalReply = "this is a programmed bot, I'm busy now so if you really need me please type the word important and then write your important message";
			submitBasicReplySettings(generalReply);
		}
		else
			readBasicSettings();
	}
	
	public void submitBasicReplySettings(String generalReply)
	{
		try {
			PrintWriter writer = new PrintWriter(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\basicReplySettings.txt","UTF-8");
			writer.write("general:"+generalReply);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		readBasicSettings();
	}
	public void readBasicSettings()
	{
		try {
			FileReader reader = new FileReader(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\basicReplySettings.txt"),StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			generalReply = line.replace("general:", "");
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}	
