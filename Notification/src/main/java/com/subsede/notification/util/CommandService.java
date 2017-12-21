package com.subsede.notification.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;



@Service
public class CommandService {
	
	private static Logger logger = LogManager.getLogger(CommandService.class);
	
	private static int MAX_RETRIES = 3;
	private static String TRAN_LOG_PATH = "";
	
	public void execute(Command cmd) {
		try {
			cmd.execute();
		} catch (Exception ex) {
			
		}
	}
	
	public Object executeReTry(Command cmd) throws Exception {
		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				return cmd.execute();
			} catch(Exception ex) {
				if ( i == MAX_RETRIES - 1)
					throw ex;
				else 
					continue;
			}
		}
		return null;
	}

	public Object executeRetryLog(Command cmd, String message) throws Exception {
		try {
			return this.executeReTry(cmd);
		} catch(Exception ex) {
			logToFile(cmd.hashCode(), message);
			throw ex;
		}
	}

	private void logToFile(int hashCode, String message) {
		String fileName = TRAN_LOG_PATH  + "Recovery_" + System.nanoTime() +  "_" + hashCode + ".txt";
		File f = new File(fileName);
		System.out.println(f.getAbsolutePath());
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(fileName);
			pw.write(message);			
		} catch (FileNotFoundException ex) {
			logger.error("Error while writing transaction to a file", ex);
		} catch(Exception ex) {
			logger.error("Error while writing transaction to a file", ex);			
		} finally {
			if (null != pw)
				try {
					pw.close();
				} catch (Exception ex) {
					
				}
		}
	}
	
	private void logToRedis(String message) {
		
	}
	
/*	public static void main(String[] args) throws Exception {
		String output = (String) new CommandService().executeRetryLog(new Command() {
			int i = 0;
			public Object execute() {
				i++;
				if (i < 4)
					throw new RuntimeException("I am failing");
				else 
					return "I am returning gracefully";
			}
		}, "This will go into file");
		System.out.println(output);
	}
*/}
