package com.adobe.CCSDK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * A complete Java class that shows how to open a URL, then read data (text) from that URL,
 * HttpURLConnection class (in combination with an InputStreamReader and BufferedReader).
 *
 * @author alvin alexander, http://alvinalexander.com.
 *
 */
public class JavaHttpUrlConnectionReader
{
  public static void main(String[] args)
  throws Exception
  {
    new JavaHttpUrlConnectionReader();
  }
  
  public JavaHttpUrlConnectionReader()
  {
    try
    {
    	//String myUrl = "https://cc-api-storage-stage.adobe.io/files";
      //String myUrl = "https://cc-api-storage.adobe.io/files";
      //String myUrl = "https://cc-api-storage.adobe.io/libraries";
    	String myUrl = "https://cc-api-storage.adobe.io/libraries/840d89be-c0d5-47cc-8045-80b856bd2142";
    	//String myUrl = "https://cc-api-storage.adobe.io/libraries/97be1e99-6afd-4fd2-8c10-80fd2f28295c/2ae0fee1-346f-4e65-beb4-1cc8679e1585";
      
    	
    	//CompositeNVP - "97be1e99-6afd-4fd2-8c10-80fd2f28295c","CSM Library";
    	//AssetNVP = "2ae0fee1-346f-4e65-beb4-1cc8679e1585","image.png";
    	
      // if your url can contain weird characters you will want to 
      // encode it here, something like this:
      // myUrl = URLEncoder.encode(myUrl, "UTF-8");

      String results = doHttpUrlConnectionAction(myUrl);
      System.out.println(results);
    }
    catch (Exception e)
    {
      // deal with the exception in your "controller"
    }
  }

  /**
   * Returns the output from the given URL.
   * 
   * I tried to hide some of the ugliness of the exception-handling
   * in this method, and just return a high level Exception from here.
   * Modify this behavior as desired.
   * 
   * @param desiredUrl
   * @return
   * @throws Exception
   */
  private String doHttpUrlConnectionAction(String desiredUrl)
  throws Exception
  {
    URL url = null;
    BufferedReader reader = null;
    StringBuilder stringBuilder;

    try
    {
      // create the HttpURLConnection
      url = new URL(desiredUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      
      // just want to do an HTTP GET here
      connection.setRequestMethod("GET");
      
      // Composite ID - /assets/adobe-libraries/97be1e99-6afd-4fd2-8c10-80fd2f28295c
      // 97be1e99-6afd-4fd2-8c10-80fd2f28295c/2ae0fee1-346f-4e65-beb4-1cc8679e1585
      
      // uncomment this if you want to write output to this url
      //connection.setDoOutput(true);
      connection.setRequestProperty("X-Request-Id", "ddd");
      connection.setRequestProperty("x-api-key", "cdba0670e9d34f05ac11a87f2a14b691");
      connection.setRequestProperty("Authorization", "Bearer eyJ4NXUiOiJpbXNfbmExLWtleS0xLmNlciIsImFsZyI6IlJTMjU2In0.eyJmZyI6IlJLVDI0REVFNzdSVVA3VFRWUlNRTUFOVUFBPT09PT09IiwiYyI6ImFXS0g4RUM1c0tZWDdsTy8yUXd2UEE9PSIsIm1vaSI6ImZjZDQ2NGFkIiwicnRpZCI6IjE0OTE4OTg1MDczNzMtNTNhZmUzMjYtYTY0Mi00OWJhLTkyN2YtYmY1OTdjZWE4MmRlIiwiY3JlYXRlZF9hdCI6IjE0OTE4OTg1MDczNzIiLCJydGVhIjoiMTQ5MzEwODEwNzM3MyIsInR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJjbGllbnRfaWQiOiJjZGJhMDY3MGU5ZDM0ZjA1YWMxMWE4N2YyYTE0YjY5MSIsImFzIjoiaW1zLW5hMSIsIm9jIjoicmVuZ2EqbmExcioxNWI1YzE0MjU2ZipOTkVUNjNWMFZIMDdRNEVaMkMzTk5EWlhaTSIsInVzZXJfaWQiOiI1NTJDNjEwNDQ2Njk1RDExOTkyMDE2QjdAQWRvYmVJRCIsInNjb3BlIjoiQWRvYmVJRCxvcGVuaWQsY3JlYXRpdmVfc2RrLGFkZHJlc3MsZW1haWwscHJvZmlsZSIsImlkIjoiMTQ5MTg5ODUwNzM3Mi0zMzczOTVlYi04MWI4LTQwZDYtOWIwMy0zOGFiZTBjNDJmMTQiLCJzdGF0ZSI6IiIsImV4cGlyZXNfaW4iOiI4NjQwMDAwMCJ9.kGOluzstZl8_JMSJPbzi1IsyhZzQQ0y95URv6lemLBnmgEfU2v8lPjRwCZLhZBb_G7AqclZGa0Rf6QATqguYpYSkMvpcQ8ELo34FHGX3I0nv7_Uyvp7Bk5v8Z9vez75-4ugSFUo37W8AGRCUT6ei0my-nJuo2o7KhrRT8UfpxbND99MHLqV7SnbUGDBybOelZUy3azpMGiENwZCf743n8koYnfb2l5hweQCcqauiaQrDD97vpTlId5EWTPDi9hXqfXsmABddo1r5hkP9PzMDrAgy7Rme2R_uL_Vz-1A796eZqIolOPyBAfdh6J6vGY0WgQlcpsT1UtKSI4yHTOpjow");
      
      // give it 15 seconds to respond
      connection.setReadTimeout(15*1000);
      connection.connect();

      // read the output from the server
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      stringBuilder = new StringBuilder();

      String line = null;
      while ((line = reader.readLine()) != null)
      {
        stringBuilder.append(line + "\n");
      }
      System.out.println(stringBuilder.toString());
      return stringBuilder.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      // close the reader; this can throw an exception too, so
      // wrap it in another try/catch block.
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException ioe)
        {
          ioe.printStackTrace();
        }
      }
    }
  }
}