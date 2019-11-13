/*******************************************************************************
 * Copyright (c) 2019 Niek Knijnenburg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package nl.siwoc.mediainfo.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {


	protected static class MyLevel extends Level{

		private static final long serialVersionUID = 1L;

		protected MyLevel(String name, int value) {
			super(name, value);
		}
		
	}
	
	public static final Level ERROR = new MyLevel("ERROR", Level.SEVERE.intValue());
	public static final Level DEBUG = new MyLevel("DEBUG", Level.FINE.intValue());
	public static final Level TRACE = new MyLevel("TRACE", Level.FINER.intValue());
	
	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Logger.class.getName());

	static {
		try {
			new File("log").mkdir();
			FileHandler handler = new FileHandler("log/MediaInfo.log", 500000, 2, true);
			handler.setFormatter(new SimpleFormatter());
			java.util.logging.Logger.getLogger(Logger.class.getName()).addHandler(handler);
			String logLevel = Properties.getProperty("logging.level");
			setLogLevel(logLevel);
		} catch (IOException e) {
			throw new RuntimeException("Could not create log file",e);
		}
	}

	public static void setLogLevel(String logLevel) {
		Level level = Level.parse(logLevel);
		Handler[] handlers = LOGGER.getHandlers();
		LOGGER.setLevel(level);
		for (Handler h : handlers) {
			if(h instanceof FileHandler)
				h.setLevel(level);
		}
		LOGGER.log(level, "Setting log-level to: " + logLevel);
	}

	public static void log(String level, String message) {
		LOGGER.log(Level.parse(level.toUpperCase()), message);
	}

	public static void logError(String message) {
		LOGGER.log(Level.SEVERE, message);
	}

	public static void logWarning(String message) {
		LOGGER.log(Level.WARNING, message);
	}

	public static void logInfo(String message) {
		LOGGER.log(Level.INFO, message);
	}

	public static void logDebug(String message) {
		LOGGER.log(Level.FINE, message);
	}

	public static void logTrace(String message) {
		LOGGER.log(Level.FINER, message);
	}

	public static boolean isInTrace() {
		return (LOGGER.getLevel().intValue() <= Level.FINER.intValue());
	}

	public static void logError(String message, Throwable e) {
		LOGGER.log(ERROR, message, e);
		
	}

}
