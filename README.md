# SIT305 Task 6.1D - LLM-Enhanced Learning Assistant App

This project is an Android learning assistant app developed for SIT305 Task 6.1D.

The app allows students to sign up, select learning interests, complete learning tasks, generate AI hints, and receive AI explanations for their answers.

The LLM integration is handled through a local Node.js backend. The Android app does not store the API key directly.

---

## Main Features

- Login and sign up flow
- Interest selection screen
- Home screen with learning tasks based on selected interests
- Multiple-choice task screen
- Results screen with score, selected answer, and correct answer
- AI-generated hint for a question
- AI-generated explanation for the student's answer
- Prompt and AI response displayed in the app UI
- Loading and error handling for AI requests

---

## LLM Learning Utilities

This app includes two LLM-powered learning utilities.

### 1. Generate Hint

On the task screen, the student can tap **Get Hint**.

The app sends the topic, question, and answer options to the backend. The LLM returns a short beginner-friendly hint without directly revealing the answer.

### 2. Explain My Answer

On the results screen, the student can tap **Explain My Answer**.

The app sends the topic, question, correct answer, and the student's selected answer to the backend. The LLM returns a short explanation of why the answer is correct or incorrect.

---

## Technology Stack

### Android

- Kotlin
- XML layouts
- ViewBinding
- Fragment navigation
- ViewModel and LiveData
- RecyclerView
- Retrofit

### Backend

- Node.js
- Express
- Groq API
- dotenv

---

## Backend Setup

The backend is required for real AI responses.

Open the backend folder:

```bash
cd learning-ai-backend
```

Install backend dependencies:

```bash
npm install
```

Create a `.env` file based on `.env.example`:

```env
GROQ_API_KEY=your_groq_api_key_here
PORT=3000
```

Start the backend:

```bash
npm start
```

The backend should run at:

```bash
http://localhost:3000
```

The Android emulator connects to the local backend using:

```bash
http://10.0.2.2:3000/
```

This URL is configured in:

```bash
app/src/main/java/com/example/llm_enhancedlearningassistantapp/network/RetrofitClient.kt
```

---

## Running the App

Start the backend first:

```bash
cd learning-ai-backend
npm install
npm start
```

Then open the Android project in Android Studio and run the app on an emulator.

Use the app flow:

```bash
Sign Up -> Select Interests -> Login -> Home -> Task -> Results
```

On the task screen, tap:

```bash
Get Hint
```

On the results screen, tap:

```bash
Explain My Answer
```

The app displays both the prompt sent to the backend and the AI response returned from the backend.

---

## Security Note

The real API key should only be stored in:

```bash
learning-ai-backend/.env
```

This file is ignored by Git and should not be uploaded to GitHub.

Only this example file is included in the repository:

```bash
learning-ai-backend/.env.example
```

---

## Author

Thien Khang Nguyen