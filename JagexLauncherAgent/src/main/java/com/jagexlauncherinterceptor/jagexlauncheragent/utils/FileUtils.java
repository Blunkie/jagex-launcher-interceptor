/*
 * Copyright (c) 2021, Melxin <https://github.com/melxin/> 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jagexlauncherinterceptor.jagexlauncheragent.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils
{
	/**
	 * Execute any file
	 *
	 * @param file
	 */
	public static void executeFile(File file)
	{
		// Execute jar file
		if (file.getName().endsWith(".jar"))
		{
			executeJarFile(file);
			return;
		}

		try
		{
			// Execute file using Desktop if it is supported
			if (Desktop.isDesktopSupported())
			{
				Desktop desktop = Desktop.getDesktop();
				desktop.open(file);
			}
			else
			{
				// Try to execute using runtime
				String command = OSUtils.isWindows ? "start " + "\"" + file.getPath() + "\"" : file.getPath();
				Runtime.getRuntime().exec(command);
			}
		}
		catch (IOException ex)
		{
			Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void executeJarFile(File jarFile)
	{
		String command = OSUtils.isWindows
				? "\"" + System.getProperty("java.home") + "\\bin\\javaw.exe" + "\"" + " -jar " + "\"" + jarFile.getPath() + "\""
				: "java -jar " + jarFile.toPath();
		try
		{
			Runtime.getRuntime().exec(command);
		}
		catch (IOException ex)
		{
			Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
