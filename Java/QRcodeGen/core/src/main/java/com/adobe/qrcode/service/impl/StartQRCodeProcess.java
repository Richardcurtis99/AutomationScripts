package com.adobe.qrcode.service.impl;

import java.util.*;
import java.io.*;


import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;



import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class StartQRCodeProcess {

	public static void main(String[] args) throws IOException,
			InterruptedException {


		Path faxFolder = Paths.get("/users/richardcurtis/monitorfolder");
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
			//	System.out.println(StandardWatchEventKinds.ENTRY_CREATE.);
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
				// Is file a file or directory
						String fileName = event.context().toString();
						fileName = faxFolder +"/"+ fileName;
						//System.out.println(fileName);
						//	System.out.println(new File(faxFolder+'/'+fileName).isFile());

						if (new File(fileName).isFile()) {
						System.out.println("File Created:" + fileName);
					}
				}
			}
			valid = watchKey.reset();

		} while (valid);

	}
}