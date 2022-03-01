package MessengerBot;

import java.util.ArrayList;

public class Chat {
    private static Chat chat = null;
    private final ArrayList<String> messages = new ArrayList<>();
    private final ArrayList<String> photos = new ArrayList<>();
    private Chat(){}

    public static Chat Singleton()
    {
        if(chat == null)
            chat = new Chat();
        return chat;
    }
    public void addMessage(String message)
    {
        messages.add(message);
    }
    public void addPhoto(String photoPath)
    {
        photos.add(photoPath);
    }
    public String getMessage()
    {
        return messages.get(0);
    }
    public String getPhotoPath()
    {
        return photos.get(0);
    }
    public int getMessagesSize()
    {
        return messages.size();
    }
    public int getPhotosSize()
    {
        return photos.size();
    }
    public void removeSeenChat()
    {
        messages.remove(0);
        try{
            photos.remove(0);
        }catch (Exception ignored){}
    }
}
