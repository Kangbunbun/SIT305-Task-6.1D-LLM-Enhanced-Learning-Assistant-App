import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import Groq from "groq-sdk";

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

if (!process.env.GROQ_API_KEY) {
  console.error("Missing GROQ_API_KEY in .env file.");
  process.exit(1);
}

const groq = new Groq({
  apiKey: process.env.GROQ_API_KEY,
});

async function askGroq(prompt) {
  if (!prompt || prompt.trim().length === 0) {
    throw new Error("Prompt is empty.");
  }

  const completion = await groq.chat.completions.create({
    model: "llama-3.3-70b-versatile",
    messages: [
      {
        role: "system",
        content:
          "You are a helpful learning assistant for students. Keep answers short, clear, beginner-friendly, and focused on learning.",
      },
      {
        role: "user",
        content: prompt,
      },
    ],
    temperature: 0.4,
    max_completion_tokens: 180,
  });

  return completion.choices?.[0]?.message?.content || "";
}

app.post("/ai/hint", async (req, res) => {
  try {
    const { prompt } = req.body;
    const response = await askGroq(prompt);

    if (!response.trim()) {
      return res.status(500).json({
        response: "",
        error: "Empty AI response.",
      });
    }

    res.json({ response });
  } catch (error) {
    res.status(500).json({
      response: "",
      error: error.message || "Failed to generate hint.",
    });
  }
});

app.post("/ai/explain", async (req, res) => {
  try {
    const { prompt } = req.body;
    const response = await askGroq(prompt);

    if (!response.trim()) {
      return res.status(500).json({
        response: "",
        error: "Empty AI response.",
      });
    }

    res.json({ response });
  } catch (error) {
    res.status(500).json({
      response: "",
      error: error.message || "Failed to generate explanation.",
    });
  }
});

app.get("/", (req, res) => {
  res.send("Learning AI backend is running.");
});

const port = process.env.PORT || 3000;

app.listen(port, () => {
  console.log(`Learning AI backend running on http://localhost:${port}`);
});