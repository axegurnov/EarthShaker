package com.axegurnov.android.earthshaker;

/**
 * Created by alexe on 08.03.2018.
 */

import android.os.AsyncTask;
import android.support.v4.content.res.TypedArrayUtils;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils {



    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Query the USGS dataset and return an {@link Event} object to represent a single earthquake.
     */
    public static List<EarthDate> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        //Event earthquake = extractFeatureFromJson(jsonResponse);

        List<EarthDate> earthquakes = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return earthquakes;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Event} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */


    private static List<EarthDate> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<EarthDate> earthquakes = new ArrayList<>();
        Log.e("JSON",earthquakeJSON);
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");

            // If there are results in the features array
            for (int i = 0; i < featureArray.length(); i++) {
                // Extract out the first feature (which is an earthquake)
                JSONObject firstFeature = featureArray.getJSONObject(i);
                JSONObject properties = firstFeature.getJSONObject("properties");

                // Extract out the title, number of people, and perceived strength values
                long unixSeconds = properties.getLong("time");
                //конвертируем время из UNIX в НОРМАЛЬНОЕ но у них ненормальное
                //
                Date dateD = new Date(unixSeconds);
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                String formatted = format.format(dateD);
                System.out.println(formatted);
                format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
                formatted = format.format(dateD);
                Log.e("DATE",formatted);

                String[] splitDatetime = formatted.split(" ");


                //возвращаем из json
                String coordinate = properties.getString("place");
                String[] split = coordinate.split(" ");

                String magnitude = properties.getString("mag");
                String location = properties.getString("place");

                String[] splitPlace = location.split(" ");

                int spPlacemusch = splitPlace.length;

                Log.e("how much",String.valueOf(spPlacemusch));
                Log.e("pla", splitPlace[spPlacemusch-2]+" "+ splitPlace[spPlacemusch-1]);

                String url = properties.getString("url");


                EarthDate eartQuake = new EarthDate(split[0] + " " + split[1], splitPlace[spPlacemusch-2]+" "+ splitPlace[spPlacemusch-1] , splitDatetime[0], splitDatetime[1], magnitude,url);
                earthquakes.add(eartQuake);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }


//    private static ArrayList<EarthDate> extractFromJson(String earthquakeJSON) {
//        // If the JSON string is empty or null, then return early.
//        if (TextUtils.isEmpty(earthquakeJSON)) {
//            return null;
//        }
//
//        ArrayList<EarthDate> earthDataInput = new ArrayList<EarthDate>();
//
//
//        try {
//            JSONObject jsonJsonResponce = new JSONObject(earthquakeJSON);
//            JSONArray arrayFatures = jsonJsonResponce.getJSONArray("features");
//
//            for (int i = 0; i < arrayFatures.length(); i++) {
//                JSONObject earthShaker = arrayFatures.getJSONObject(i);
//                JSONObject properties = earthShaker.getJSONObject("properties");
//
//                long unixSeconds = properties.getLong("time");
//
//                //конвертируем время из UNIX в НОРМАЛЬНОЕ но у них ненормальное
//                // convert seconds to milliseconds
//                Date date = new Date(unixSeconds * 1000);
//                SimpleDateFormat dateDate = new SimpleDateFormat("dd.MM.yyyy");
//                // give a timezone reference for formatting (see comment at the bottom)
//                String formattedDate = dateDate.format(date);
//                //Log.e("Date"," "+formattedDate);
//                SimpleDateFormat timeTime = new SimpleDateFormat("HH:mm:ss");
//                // give a timezone reference for formatting (see comment at the bottom)
//                String formattedTime = timeTime.format(date);
//                //Log.e("Time"," "+formattedTime);
//
//                //возвращаем из json
//                String coordinate = properties.getString("place");
//                String[] split = coordinate.split(" ");
//                String magnitude = properties.getString("mag");
//                String location = properties.getString("place");
//
//
//                EarthDate eartQuake = new EarthDate(split[0] + " " + split[1], location, formattedDate, formattedTime, magnitude);
//                earthDataInput.add(eartQuake);
//            }
//        } catch (JSONException e) {
//            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
//        }
//
//        return earthDataInput;
//    }
}