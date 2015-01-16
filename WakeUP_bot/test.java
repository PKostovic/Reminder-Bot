import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.techventus.server.voice.Voice;

public class test {

	public static void main(String[] args) {

		Scanner scnr = null;
		String filePath = "Z:\\Archives\\Schedule.txt";
		String[] text = null;
		String line = "";
		String message = "";
		String setNextReminder = "";
		String nextTaskTime = "";
		Boolean shouldSend = true;
		File file = new File(filePath);

		// step 1: process current reminder
		// get first line of text
		try {
			// initialize scanner, and read in the first line.
			// format will be <time>;<message>;<phone #>
			scnr = new Scanner(file);
			if (scnr.hasNextLine()) {
				line = scnr.nextLine();
			}
			text = line.split(";");
		}
		// catch any error with the file. Shouldn't start because the file
		// is hard coded in.
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if there are no active reminders, ie there is nothing in the file
		if (line.equals("") == true) {
			nextTaskTime = tryAgainOneHour();
			shouldSend = false;
		}
		// if there is a first line, continue further
		else {
			message = text[1];
			// if there is no next line, check again in another hour
			if (scnr.hasNext() == false) {
				nextTaskTime = tryAgainOneHour();
			}
			// otherwise, get the next entered time
			else {
				line = scnr.nextLine();
				text = line.split(";");
				nextTaskTime = text[0];
			}
		}
		
		scnr.close();
		
		// set the time of the next task
		setNextReminder = "schtasks /change /tn reminder_bot /RU Pavle /RP kepler"
				+ " /tr Z:\\Archive\\main_WUB.jar /ST " + nextTaskTime;
		
		//TODO
		System.out.println(setNextReminder);

		// step 2: send message at alloted time.
		if(shouldSend){
			sendMessage(message + "\n \nI am a bot, beep boop!");
			
			//TODO
			System.out.println("Send message: " + "\n" + message);
		}
		
		// step 3: reconstruct the file

		ArrayList<String> textInNewFile = new ArrayList<String>();
		
		try {
			scnr = new Scanner(file);
			scnr.nextLine();
			while(scnr.hasNextLine()){
				textInNewFile.add(scnr.nextLine());
			}
			file.delete();
			
			writeToNewFile(filePath, textInNewFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		// step 4: end the reminder_bot task
		
		String endCurrentReminder = "schtasks /end /tn reminder_bot";
		sendToCMD(endCurrentReminder);
		
		// step 5: set next reminder_bot event.
			
		sendToCMD(setNextReminder);
		


	}// close main
	
	public static void writeToNewFile(String filePath, ArrayList<String> data)
	{
	    PrintWriter writer;
	    File file;
	    try
	    {
	        file = new File(filePath);
	        file.createNewFile();
	        writer = new PrintWriter(new FileWriter(file));
	        for(String s: data){
	        	writer.println(s);
	        }
	        writer.flush();
	        writer.close();
	     }catch(Exception e){e.printStackTrace();}
	     writer = null;
	     file = null;
	     //setting file & writer to null releases all the system resources and allows the files to be accessed again later
	}
	
	private static void sendToCMD(String command){
		try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
	}

	private static void sendMessage(String input) {
		String username = "wake.up.bot.acc";
		String password = "wakeupbotacc";
		String pavlePhone = "14142183060";
		String message = input;

		try {
			Voice voice = new Voice(username, password);
			voice.sendSMS(pavlePhone, message);

			// voice.call(originNumber, pavlePhone, "1");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}// close sendMessage

	private static String tryAgainOneHour() {
		// schedule an event to run this program at (current time + 1 hour)
		String tempString = "";
		// get current time
		String currTime = new SimpleDateFormat("HH:mm:SS").format(new Date());

		// get the current hour
		String[] time = currTime.split(":");

		// increment current hour by 1. if CH is midnight (24), set it to 01
		if (Integer.parseInt(time[0]) == 24) {
			time[0] = "01";
		}
		// otherwise increment the hour
		else {
			time[0] = (Integer.parseInt(time[0]) + 1) + "";
		}

		// save the next time to call the program
		for (int i = 0; i < time.length - 1; i++) {
			tempString = time[0] + ":" + time[i];
		}
		return tempString;

	}// close tryAgainOneHour
}// close class
