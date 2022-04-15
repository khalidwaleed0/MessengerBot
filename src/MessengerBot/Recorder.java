package MessengerBot;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Recorder implements Runnable {
    public static boolean isRecording = false;
    private String recordedKey;

    @Override
    public void run() {
        recordedKey = AppSetup.Singleton().getRecordedKey();
        if (!recordedKey.isEmpty()) {
            if (recordedKey.contains("Mouse")) {
                changeMouseSideButtonsValue();
                createMouseHook();
            } else
                createKeyboardHook();
        }
    }

    private void changeMouseSideButtonsValue() {
        recordedKey = recordedKey.replaceAll("\\D", "");
        if (recordedKey.equals("4"))
            recordedKey = "32";
        else if (recordedKey.equals("5"))
            recordedKey = "64";
    }

    private void createMouseHook() {
        GlobalMouseHook mouseHook = new GlobalMouseHook();
        mouseHook.addMouseListener(new GlobalMouseAdapter() {

            @Override
            public void mousePressed(GlobalMouseEvent event) {
                System.out.println(event);
                if (event.getButton() == Integer.parseInt(recordedKey)) {
                    System.out.println(recordedKey + " Pressed !");
                    sendRecord();
                }
            }
        });
    }

    private void createKeyboardHook() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {

            @Override
            public void keyPressed(GlobalKeyEvent event) {
                System.out.println(event);
                if (event.getVirtualKeyCode() == Integer.parseInt(recordedKey)) {
                    System.out.println("Record started");
                    sendRecord();
                }
            }
        });
    }

    public void sendRecord() {
        if (!isRecording) {
            isRecording = true;
            Chat.Singleton().addMessage("Recording..  Click again to send");
            Scraper.Singleton().startRecord();
        } else {
            Scraper.Singleton().stopRecord();
            try {
                Chat.Singleton().addMessage("Sending Record..");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isRecording = false;
        }
    }
}
