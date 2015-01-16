import java.io.IOException;

import com.techventus.server.voice.Voice;

public class reminder_bot {

	//credentials for the Google Voice Account
	private static String crowUser = "wake.up.bot.acc";
	private static String crowPass = "wakeupbotacc";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String text = "bitches AND THIS IS REMINDER_BOT.JAR";
		String pavlePhone = "14142183060";
		
		SendMessage(text, pavlePhone);

	}
	
	
	/**
	 * SendMessage
	 * 
	 * @param text String containing a reminder message, will be formatted and
	 *        sent to selected user
	 * @param phoneDestination the phone number of the user receiving the 
	 *        reminder
	 */
	private static void SendMessage(String text, String phoneDestination){
		try{
			 Voice voiceSender = new Voice(crowUser, crowPass);
			 String message = "Hello, you have a reminder set about:\n\n" + text
					 + "\n\nI am a bot, beep boop!";
			 voiceSender.sendSMS(phoneDestination, message);
			 
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}

}
