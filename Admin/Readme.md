# FightAlarm Admin
FightAlarm Admin Hybrid App

## Setting up Local Environment
1. Install NodeJS and NPM [here](https://www.guru99.com/download-install-node-js.html)

2. Install Ionic
```bash
    npm install -g ionic
```

3. Install Android Studio [here](https://developer.android.com/studio/install)

## Running Application
### Installing Dependencies
1. Install packages in package.json
```bash
    npm install
```

2. Serve App:
    
    a. For web application. 
    ```bash
        ionic serve
    ```
    
    b. For local mobile sample. 
    ```bash
        ionic serve --lab
    ```
    Follow other instructions provided.
    
    c. For mobile simulation or device.
    
    **Android Device**
    ```bash
            ionic cordova run android
    ``` 
       
    **IOS Device - Requires [Xcode](https://www.moncefbelyamani.com/how-to-install-xcode-homebrew-git-rvm-ruby-on-mac/) installed **
    ```bash
            ionic cordova run ios
    ``` 
    
    
    
## Deploying Application onf Firebase
### Using Firebase Hosting
1. Install firebase tools globally
```bash 
    npm install -g firebase-tools
```

2. Login to firebase
```bash 
    firebase login
```

3. Initialize the hosting setup
```bash 
    firebase init
```

**For Functions**
Navigate with the down arrow key to Functions, use space to select functions before pressing enter.

**For Hosting**
Navigate with the down arrow key to Hosting, use space to select hosting before pressing enter.

**Answers to next set of questions:**

? What do you want to use as your public directory? www

? Configure as a single-page app (rewrite all urls to /index.html)? Yes

? File www/index.html already exists. Overwrite? No

4. Your firebase.json file should look like below
```json
    {
      "hosting": {
        "public": "www",
        "ignore": [
          "firebase.json",
          "**/.*",
          "**/node_modules/**"
        ],
        "rewrites": [
          {
            "source": "**",
            "destination": "/index.html"
          }
        ]
      }
    }
```

5. Build App
```bash
    ionic build --prod --release
```

6. Deploy to firebase
```bash
    firebase deploy
```

7. After successful deployment of hosting, you will get a URL.
