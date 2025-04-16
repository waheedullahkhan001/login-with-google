# Login With Google on Android
This is a simple example of how to implement Google Sign-In in an Android app using Jetpack Compose without using Firebase.


# Setup - Quick
Basically, you need to create three OAuth 2.0 client IDs:
1. Android OAuth client ID for the debug keystore.
2. Android OAuth client ID for the release keystore.
3. Web application OAuth client ID.
and you will use the web application OAuth client ID in the app.


## Setup - Detailed
1. Create a new project in the Google Cloud Console.
2. From the left menu under APIs & Services, select Credentials.
3. Make sure to "Configure consent screen" before proceeding.
   - **App Information**
     - App name: Your app name
     - User support email: Your email address
   - **Audience**
     - Select "External".
   - **Contact Information**
     - Add your email address.
   > Note: You might want to add yourself in "Test users".
4. From the left menu under APIs & Services, select Credentials.
5. Click on "Create Credentials" and select "OAuth client ID".
6. Select "Android" as the application type.
7. Add the package name of your app.
8. Add the SHA-1 certificate fingerprint.
   - To get the SHA-1 certificate fingerprint, run the following command in your terminal:
     ```bash
     keytool -keystore PATH_TO_DEBUG_KEYSTORE_FILE -list -v
     ```
9. Now add another Android OAuth client ID for the release keystore and add the SHA-1 certificate fingerprint.
   - To get the SHA-1 certificate fingerprint, run the following command in your terminal:
     ```bash
     keytool -keystore PATH_TO_RELEASE_KEYSTORE_FILE -list -v
     ```
10. Finally create another OAuth 2.0 client ID and select "Web application" as the application type.
11. Copy the client ID of the web application and add it to the app.