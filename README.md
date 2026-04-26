\# SIT305 Task 6.1D - LLM-Enhanced Learning Assistant App



This repository contains my Android application for \*\*SIT305 Task 6.1D: LLM-Enhanced Learning Assistant App\*\*.



The app is designed to support student learning by providing personalised learning tasks based on selected interests. It also integrates LLM-powered learning utilities through a local backend API.



The Android app does \*\*not\*\* store any API key directly. Instead, it sends learning prompts to a local Node.js backend, and the backend securely calls the Groq API.



\---



\## Project Overview



The app follows a simple student learning flow:



1\. The student signs up or logs in.

2\. The student selects learning interests.

3\. The home screen displays learning tasks based on those interests.

4\. The student completes multiple-choice learning questions.

5\. The app provides AI-generated hints and answer explanations.

6\. The results screen shows the student’s score, selected answers, correct answers, and AI explanations.



This structure is intended to demonstrate how an LLM can be used for learning support, rather than only acting as a general chatbot.



\---



\## Main Features



\### Android App



\- Login screen

\- Sign up screen

\- Interest selection screen

\- Home screen with interest-based learning tasks

\- Task screen with multiple-choice questions

\- Results screen with score and answer feedback

\- Prompt and AI response displayed directly in the UI

\- Loading state while waiting for AI responses

\- Error handling for failed AI/backend requests



\### LLM-Powered Learning Utilities



This app includes two LLM-powered learning utilities:



\#### 1. Generate Hint



Available on the task screen.



The app sends the selected topic, question, and answer options to the backend.  

The LLM returns a short beginner-friendly hint without revealing the answer directly.



Example information sent in the prompt:



```text

Topic: Testing

Question: Why do we test software?

Options: To find bugs, To make it more expensive, To change the icon

Task: Give one short beginner-friendly hint without revealing the correct answer.

```



\#### 2. Explain My Answer



Available on the results screen.



The app sends the topic, question, correct answer, and the student’s selected answer to the backend.  

The LLM explains why the student’s answer is correct or incorrect.



Example information sent in the prompt:



```text

Topic: Testing

Question: Why do we test software?

Correct answer: To find bugs

Student answer: To find bugs

Task: Explain why this answer is correct.

```



\---



\## Technology Stack



\### Android



\- Kotlin

\- XML layouts

\- ViewBinding

\- Fragment-based navigation

\- RecyclerView

\- ViewModel

\- LiveData

\- Retrofit

\- Material UI components



\### Backend



\- Node.js

\- Express.js

\- Groq SDK

\- dotenv

\- CORS



\### LLM Provider



\- Groq API

\- Model used in backend: `llama-3.3-70b-versatile`



\---



\## Repository Structure



```text

SIT305-Task-6.1D-LLM-Enhanced-Learning-Assistant-App/

│

├── app/

│   └── Android application source code

│

├── gradle/

│   └── Gradle wrapper files

│

├── learning-ai-backend/

│   ├── server.js

│   ├── package.json

│   ├── package-lock.json

│   └── .env.example

│

├── build.gradle.kts

├── settings.gradle.kts

├── gradle.properties

├── gradlew

├── gradlew.bat

├── .gitignore

└── README.md

```



\---



\## Backend Setup



The backend is required for real LLM API responses.



The Android app sends requests to:



```text

http://10.0.2.2:3000/

```



This address allows the Android emulator to connect to a backend running on the computer’s localhost.



\---



\### 1. Open the Backend Folder



From the project root:



```bash

cd learning-ai-backend

```



\---



\### 2. Install Backend Dependencies



```bash

npm install

```



\---



\### 3. Create the `.env` File



Create a new file named:



```text

.env

```



Use `.env.example` as a guide:



```env

GROQ\_API\_KEY=your\_groq\_api\_key\_here

PORT=3000

```



Replace `your\_groq\_api\_key\_here` with your own Groq API key.



Do not upload the `.env` file to GitHub.



\---



\### 4. Start the Backend



```bash

npm start

```



If the backend starts successfully, the terminal should show:



```text

Learning AI backend running on http://localhost:3000

```



You can also test the backend in a browser:



```text

http://localhost:3000

```



Expected result:



```text

Learning AI backend is running.

```



\---



\## Testing the Backend API



\### Test Hint Endpoint



```powershell

Invoke-RestMethod `

&#x20; -Uri "http://localhost:3000/ai/hint" `

&#x20; -Method POST `

&#x20; -ContentType "application/json" `

&#x20; -Body '{"prompt":"Topic: Web Development. Question: What is HTML? Give a short hint without revealing the answer."}'

```



\### Test Explanation Endpoint



```powershell

Invoke-RestMethod `

&#x20; -Uri "http://localhost:3000/ai/explain" `

&#x20; -Method POST `

&#x20; -ContentType "application/json" `

&#x20; -Body '{"prompt":"Topic: Web Development. Question: What language is used for web structure? Correct answer: HTML. Student answer: CSS. Explain why the correct answer is correct and why the student answer is incorrect."}'

```



Both endpoints should return a JSON response containing an AI-generated response.



\---



\## Running the Android App



1\. Open the project in Android Studio.

2\. Start the backend first:



```bash

cd learning-ai-backend

npm install

npm start

```



3\. Run the Android app on an emulator.

4\. Use the app flow:



```text

Sign Up → Select Interests → Login → Home → Task → Results

```



5\. On the task screen, tap:



```text

Get Hint

```



6\. On the results screen, tap:



```text

Explain My Answer

```



The app should display both:



```text

PROMPT SENT

```



and:



```text

AI RESPONSE / AI EXPLANATION

```



inside the app UI.



\---



\## Android Backend Configuration



The Android app uses Retrofit to communicate with the backend.



Backend URL location:



```text

app/src/main/java/com/example/llm\_enhancedlearningassistantapp/network/RetrofitClient.kt

```



Current emulator backend URL:



```kotlin

private const val BASE\_URL = "http://10.0.2.2:3000/"

```



The Android manifest includes internet access and allows local HTTP traffic for the local backend:



```xml

<uses-permission android:name="android.permission.INTERNET" />

```



```xml

android:usesCleartextTraffic="true"

```



\---



\## Security Notes



The API key is not stored in the Android app.



The real key should only be placed in:



```text

learning-ai-backend/.env

```



This file is ignored by Git and should not be pushed to GitHub.



The repository only includes:



```text

learning-ai-backend/.env.example

```



This allows another user to understand the required environment variable format without exposing a real API key.



\---



\## Files Ignored by Git



The following files and folders should not be pushed:



```text

learning-ai-backend/.env

learning-ai-backend/node\_modules/

local.properties

.gradle/

build/

app/build/

.idea/

```



These files are excluded using `.gitignore`.



\---



\## Notes for Assessment



This project demonstrates:



\- Android UI structure using multiple screens

\- Student interest selection

\- Generated learning tasks using dummy data

\- Multiple-choice question interaction

\- Score calculation

\- LLM-generated learning hints

\- LLM-generated answer explanations

\- Prompt and response visibility in the UI

\- Backend API communication using Retrofit

\- Basic responsible API key handling through a backend layer



\---



\## Author



Thien Khang Nguyen

