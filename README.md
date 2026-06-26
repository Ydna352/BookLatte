# Book Latte ☕📚

**Book Latte** is a native Android application (developed in Java) that combines a passion for reading with a love for coffee. It allows users to browse a pre-loaded book catalog, share reviews with ratings and comments, and locate nearby cafes and bookstores in real-time on an interactive map using Google APIs.

This repository contains the final version of the project, designed and implemented by:
* Martínez Arroyo Andrea
* Paúl Ortega Naomi Astrid
* Piedras Mora Jorge Bryan

---

## 🚀 Key Features

1. **Animated Splash Screen (`SplashActivity.java`):** An interactive start with a progress bar to prepare the user experience.
2. **Local Authentication (SQLite):** User registration and secure login stored locally.
3. **Profile Management:** Edit biography and upload a profile picture from the gallery, automatically resized and compressed for memory-efficient database storage.
4. **Book Catalog:** A dynamic list of popular books (e.g., *Love, Theoretically*, *The Prince of the Sun*, *Almond*, etc.) displayed using an optimized `RecyclerView`.
5. **Review System:** Rate each book using stars and write detailed text comments, which are stored and displayed in each user's profile.
6. **Cafe & Bookstore Locator (Google Maps + Google Places API):** Search for nearby establishments with dynamic marker placement based on the user's location.

---

## 🗺️ Geolocational API Integration (Google)

The search and map module (`MapsFragment.java`) implements geolocalisation as follows:
* **Real-time Location:** Requests the required fine location permission (`ACCESS_FINE_LOCATION`) and retrieves the device's exact coordinates using `FusedLocationProviderClient`.
* **Google Places API (Nearby Search):** Makes direct HTTP requests in the background (`AsyncTask`) to the Google Places API to search for places matching `"book_store"` and `"cafe"` types within a 5,000-meter (5km) radius of the user:
  ```text
  https://maps.googleapis.com/maps/api/place/nearbysearch/json?location={lat},{lng}&radius=5000&type={book_store|cafe}&sensor=true&key={API_KEY}
  ```
* **JSON Parsing (`JsonParser.java`):** Parses the Google server response, extracting the name (`name`) and geographical latitude/longitude coordinates of each location.
* **Dynamic Markers:** Clears previous markers and draws new pins (`GoogleMap.addMarker`) on the map, showing each found business with its name.

---

## 🔐 Security and API Key Configuration (Important!)

This project has been secured to prevent exposing sensitive Google Cloud API Keys on GitHub. The Google Maps API Key is now read dynamically during compilation from a local properties file.

### How it works:
* The Google Maps API key has been removed from `strings.xml`.
* During build time, `app/build.gradle.kts` reads the key from `local.properties` (which is excluded from Git control in `.gitignore`).
* It dynamically injects it as a resource (`@string/google_maps_key`) so the manifest and source files can reference it seamlessly.

---

## 🛠️ Local Configuration and Setup Guide

If you clone this repository to test it locally, follow these steps to run the application correctly:

### Step 1: Clone the Repository
Clone the repository to your local workspace:
```bash
git clone <your_repository_url>
```

### Step 2: Generate your own Google API Key
To use the Map and Places feature, you must generate your own Google Maps API Key:
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project.
3. Enable the following two APIs in the library panel:
   * **Maps SDK for Android** (to render the map UI).
   * **Places API** (to search for nearby businesses).
4. Go to **APIs & Services > Credentials** and click **Create Credentials > API Key**. Copy the generated key.

### Step 3: Configure your local environment
1. In the project root folder (`BookLatte`), copy `local.properties.example` and name the new file `local.properties`.
2. Open `local.properties` and add your Google API key to the `MAPS_API_KEY` property:
   ```properties
   MAPS_API_KEY=YOUR_GENERATED_API_KEY_HERE
   ```
3. Gradle will read this key when building the project and inject it automatically.

### Step 4: Configure Permissions and GPS in the Emulator/Device
1. If using an **Android Emulator**, open the extended controls menu (`...`), go to the **Location** tab, and simulate coordinates.
2. Ensure GPS and location services are enabled on the device/emulator.
3. When navigating to the map tab in the app, **allow the location permission** when prompted.
4. Select the location type from the dropdown list and press **"Find"** (Buscar).
