package callme.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Arrays;



public class callPS {

	
	public static void main (String[] args) throws GeneralSecurityException, IOException, URISyntaxException {

		
		//watched folder
		
		String logo="";
		Path faxFolder = Paths.get("/users/richardcurtis/monitorfolder");
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		String fileName ="";
		
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
			//	System.out.println(StandardWatchEventKinds.ENTRY_CREATE.);
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
				// Is file a file or directory
						fileName = event.context().toString();
						fileName = faxFolder +"/"+ fileName;
						//System.out.println(fileName);
						//	System.out.println(new File(faxFolder+'/'+fileName).isFile());
						if (new File(fileName).isFile()) {
								
							Process process = new ProcessBuilder("C:\\PathToExe\\MyExe.exe").start();
							InputStream is = process.getInputStream();
							InputStreamReader isr = new InputStreamReader(is);
							BufferedReader br = new BufferedReader(isr);
							String line;

//							System.out.printf("Output of running %s is:", Arrays.toString(x));

							while ((line = br.readLine()) != null) {
							System.out.println(line);
						
					    	}
						}
				}
					
			valid = watchKey.reset();

		} while (valid);
		{
		
		}
		
				
	
	
			}
}

	
	

