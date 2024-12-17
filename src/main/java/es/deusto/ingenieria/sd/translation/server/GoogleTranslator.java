package es.deusto.ingenieria.sd.translation.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;

public class GoogleTranslator {

	public String translate(String langFrom, String langTo, String text) throws Exception {
		String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + 
					 langFrom + "&tl=" + langTo + "&dt=t&q=" + URLEncoder.encode(text, "UTF-8");

		URI uri = null;
		URL obj = null;
		HttpURLConnection con = null;
		
		try {
			uri = new URI(url);
			obj = uri.toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
		
		if (uri != null && obj != null) {
			con = (HttpURLConnection) obj.openConnection();
		}

		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		return parseResult(response.toString());
	}

	private String parseResult(String inputJson) throws Exception {
		/*
		 * inputJson for "sentence" translated from langFrom to langTo
		 * [[["result","sentence",,,1]],,"langFrom"] We have to get 'translated
		 * sentence' from this json.
		 */
		JSONArray jsonArray = new JSONArray(inputJson);
		JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
		JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);

		return jsonArray3.get(0).toString();
	}
}