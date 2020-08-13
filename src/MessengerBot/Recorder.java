package MessengerBot;

import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Recorder implements Runnable{
	String recordKey;
	@Override
	public void run() {
		GlobalMouseHook mouseHook = new GlobalMouseHook();
		mouseHook.addMouseListener(new GlobalMouseAdapter() {
			
			@Override 
			public void mousePressed(GlobalMouseEvent event)  {
				System.out.println(event);
				if (event.getButton()==GlobalMouseEvent.BUTTON_X1) {
					System.out.println("side button 1");
				}
				if (event.getButton()==GlobalMouseEvent.BUTTON_X2) {
					System.out.println("side button 2");
				}
			}
		});
	}
	
//	private boolean isKeyPressed()
//	{
//		
//	}
	
//	private String getRecordKey()
//	{
//		
//	}
	
}
