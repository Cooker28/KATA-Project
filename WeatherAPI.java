import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAPI {

	public static void main(String[] args) {

		try {
			// Create a scanner to get the city name from the user.
			Scanner scnr = new Scanner(System.in);
			System.out.println("Enter a City.");
			String city = "";
			if (scnr.hasNext()) {
				city = scnr.next();
				scnr.close();
			} else {
				scnr.close();
				throw new Exception();

			}

			String apiReturn = getAPIStr(city);

			double latitude = extractDouble(apiReturn, "\"lat\":");
			double longitude = extractDouble(apiReturn, "\"lon\":");

			System.out.println("Latitude: " + latitude);
			System.out.println("Longitude: " + longitude);

			// Uses the latitude and longitude from the other api call to make another call
			// to get the current weather information for the given location.

			URL weatherAPI = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="
					+ longitude + "&appid=45547f8b31292f5e334af764d02d8163");

			HttpURLConnection conn = (HttpURLConnection) weatherAPI.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

			System.out.println(response.toString());
			
			// String  of what has been returned by the api call. 
			String returnInfo = response.toString();

			// Prints out some of the current weather information.
			String currDescription = extractString(returnInfo, "\"description\":");
			String currTemp = extractString(returnInfo, "\"temp\":");
			System.out.println(currDescription);
			System.out.println(currTemp);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Gets either the latitude or the longitude from the city that has been
	 * entered.
	 * 
	 * @param apiData String of the data that has been returned by the api
	 * @param key     what is before the specific data that you would like to find
	 * @return the latitude or the longitude of the city that has been entered in to
	 *         the program
	 */
	private static double extractDouble(String apiData, String key) {
		int startIndex = apiData.indexOf(key) + key.length();
		int endIndex = apiData.indexOf(",", startIndex);
		if (endIndex == -1) {
			endIndex = apiData.indexOf("}", startIndex);
		}

		String value = apiData.substring(startIndex, endIndex).trim();
		return Double.parseDouble(value);
	}

	/**
	 * Return a string of what the weather is of a current description such as
	 * temperature, wind, etc.
	 * 
	 * @param apiData String of the data that has been returned by the api
	 * @param key     what is before the specific data that you would like to find
	 * @return the string of what the weather is given the current description
	 */
	private static String extractString(String apiData, String key) {
		int startIndex = apiData.indexOf(key) + key.length();
		int endIndex = apiData.indexOf(",", startIndex);
		if (endIndex == -1) {
			endIndex = apiData.indexOf("}", startIndex);
		}

		String value = apiData.substring(startIndex, endIndex).trim();
		return value;
	}

	/**
	 * Gets the json string from the api call, to be able to pull a lat and long so
	 * the use doesn't have to look that up and can just enter the city they would
	 * like to see the weather for
	 * 
	 * @param city is the given location that the use wants the weather for
	 * @return A string with the latitude and longitude coordinates for the given
	 *         location
	 */
	private static String getAPIStr(String city) {
		// Use the city name to get the coordinates back from the API.
		try {
			URL API = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + city

					+ "&limit=5&appid=45547f8b31292f5e334af764d02d8163");
			HttpURLConnection conn = (HttpURLConnection) API.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

			System.out.println(response.toString());

			String returnInfo = response.toString();
			return returnInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

}
