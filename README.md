![Repo Cover](https://github.com/user-attachments/assets/ec8ac50b-21a6-4e92-8be1-934e6296d5ef)
### SkyAi is Ai Integrated weather forecast app that provides hourly & daily forecast with Ai powered suggestions based on detailed weather states.


## App Features:
- Modern UI Design with dynamic live background
- Hourly detailed weather data and daily forecast
- Weather data offline caching
- Ai suggestions that helps you take better decisions for your day to day activities
- Daily weather notifications
- 3 different styles weather widgets
- Supports Metric & Imperial units
- Supports 12 display Languages to set (For versions older than android 13 app will follow system language)

## Implementation Highlights:
- MVVM Clean Architecture
- LiveData
- Kotlin Coroutine & Flows
- Retrofit + Gson
- Background WorkManager
- Dependency Injection: Hilt
- Testing:
  - Junit4
  - MockK
  - Instancio
  - Espresso
- Design:
  - Figma
  - Adobe Photoshop
- Data Providers:
  - Weather: [Tommorow.io](https://app.tomorrow.io/home)
  - Ai Model: [Google Gemini](https://gemini.google.com/app)

## Video Demo:
<div align="center"><video src="https://github.com/user-attachments/assets/f76dc32c-d782-4cb9-b754-23cdf104226b"></div>

## App Apk:
<div align="center">
<a href="https://github.com/amrk000/SkyAi-Android-App/releases/download/AppDemo/SkyAi.apk"><img src="https://github.com/user-attachments/assets/ea703870-5a05-4dd8-8dcb-363d5a0cc4f3" width="250"></a>
</div>

## Project Configs:
### API Keys are secured in local.properties file
### Get your api keys : [Tomorrow API Key](https://app.tomorrow.io/development/keys) | [Gemini API Key](https://aistudio.google.com/app/apikey)
### Add the following lines with your api key in local.properties file
```xml
# Tomorrow Weather API Key
WEATHER_API_KEY = "apiKy"

#Ai Gemini API Key
AI_API_KEY = "apiKy"
```
