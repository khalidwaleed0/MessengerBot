package MessengerBot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomSoundDownloader implements Runnable {

	@Override
	public void run() {
		File userNameFile = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\UserName.txt");
		if(userNameFile.exists())
		{
			String downloadLink = getSoundLink(getUserName());
			if(!downloadLink.isEmpty())
				download(downloadLink);
		}
	}
	private String getUserName()
	{
		String line = "";
		try {
			FileReader reader = new FileReader(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\UserName.txt"));
			BufferedReader br = new BufferedReader(reader);
			line = br.readLine();
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return line;
	}
	private String getSoundLink(String userName)
	{
		String downloadLink = "";
		try {
			URL url = new URL("https://ttsmp3.com/makemp3_new.php");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			String jsonInputString = "msg="+"Sorry to interrupt you "+userName+", but someone needs you urgently"+"&lang="+"Salli"+"&source=ttsmp3";
			try(OutputStream os = con.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);			
			}
			try(BufferedReader br = new BufferedReader(
					  new InputStreamReader(con.getInputStream(), "utf-8"))) {
					    StringBuilder response = new StringBuilder();
					    String responseLine = null;
					    while ((responseLine = br.readLine()) != null) {
					        response.append(responseLine.trim());
					    }
					    Pattern pat = Pattern.compile("(\\w+\\.mp3)");
					    Matcher mat = pat.matcher(response.toString());
					    if(mat.find())
					    	downloadLink = "https://ttsmp3.com/dlmp3.php?mp3="+mat.group(1);
					}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return downloadLink;
	}
	private void download(String downloadLink)
	{
		File downloadedsoundFile = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\customSound.mp3");
		try (BufferedInputStream inputStream = new BufferedInputStream(new URL(downloadLink).openStream());
			FileOutputStream fileOS = new FileOutputStream(downloadedsoundFile)) {
				byte data[] = new byte[1024];
				int byteContent;
				while ((byteContent = inputStream.read(data, 0, 1024)) != -1)
				{
					fileOS.write(data, 0, byteContent);
				}
		} catch (IOException e) {
				
		}
	}
}
