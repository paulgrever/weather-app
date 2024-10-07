# weather-app



This single page app takes a zip code and then geocodes the zipcode using the [OpenWeather Geocoding API](https://openweathermap.org/api/geocoding-api#direct) it then uses the result to send a request to [OpenWeaters One Call 3 Api](https://openweathermap.org/api/one-call-3). This gets the weather for the next set of days along with the current weather. 

The current weather is then displayed openly for the city. The next future days weather is shown hidden but clicking will open the details. This app will cache the response for 1 hour online and 1 day offline. 


![Weather-App-Preview](https://github.com/user-attachments/assets/ecd063d1-6354-4a32-8643-095c35ae35ac)


## Dependencies 

To run this app you will need to register and sign up for a [OpenWeather One Call 3.0 API key](https://openweathermap.org/api/one-call-3)

You will then need to add that key to your `local.properties` file with the name `OPEN_WEATHER_API_KEY`
