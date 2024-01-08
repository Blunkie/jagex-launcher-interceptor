/*
 * Copyright (c) 2024, Melxin <https://github.com/melxin/> 
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
package com.jagexlauncherinterceptor.jagexlauncheragent;

import com.jagexlauncherinterceptor.jagexlauncheragent.utils.FileUtils;
import com.jagexlauncherinterceptor.jagexlauncheragent.utils.ResourceLoader;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JagexLauncherAgent
{
	public static void premain(String agentOps, Instrumentation inst)
	{
		instrument(agentOps, inst);
	}

	public static void agentmain(String agentOps, Instrumentation inst)
	{
		instrument(agentOps, inst);
	}

	private static void instrument(String agentOps, Instrumentation inst)
	{
		System.out.println("agent loaded: " + agentOps);

		boolean CONTAINS_JX_ENV_KEY = System.getenv().keySet().stream().filter(k -> k.contains("JX_")).findAny().isPresent();
		if (CONTAINS_JX_ENV_KEY)
		{
			System.out.println("Found JX_ ENV variable");
		}

		File tmpFile = new File(System.getProperty("java.io.tmpdir"), "devious-client-launcher.jar");
		if (!tmpFile.exists())
		{
			try
			{
			    Files.copy(ResourceLoader.DEVIOUS_EXECUTABLE, tmpFile.toPath());
			}
			catch (IOException ex)
			{
				Logger.getLogger(JagexLauncherAgent.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		// Launch devious
		FileUtils.executeFile(tmpFile);

		// Kill current, we do not want to launch the other
		System.exit(0);
	}
}
