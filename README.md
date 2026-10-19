# DocMind - RAG-Based Intelligent Document Assistant

An end-to-end, full-stack **Retrieval-Augmented Generation (RAG)** platform for multi-format document ingestion, semantic vector search, and conversational Q&A powered by **Google Gemini** and **PostgreSQL pgvector**.

---

## 👩‍💻 Author

* **Author:** **Vaishnavi Singh**
* **GitHub:** [@vaishnavisng](https://github.com/vaishnavisng)

---

## 🌟 Key Features

* **Multi-Format Document Ingestion:** Upload and process PDF, DOCX, TXT, Markdown, and CSV documents with real-time upload progress.
* **Intelligent Chunking & Embeddings:** Automated text chunking and vector embeddings using **Google Gemini** (`gemini-embedding-001`, 1536 dimensions).
* **High-Performance Vector Storage:** PostgreSQL with the **pgvector** extension using HNSW indexing and cosine similarity.
* **Hybrid Conversational RAG:** Chat naturally with your uploaded documents using **Gemini 3.5 Flash** with context grounding, document citations, and conversation memory.
* **Semantic Search & Document Inspection:** Dedicated views to explore document chunks, clauses, concepts, and factual extracts.
* **Modern & Responsive UI:** Built with React 19, TypeScript, Tailwind CSS v4, Lucide Icons, and Framer Motion.
* **Production-Ready Security:** JWT authentication, user-level document isolation, and clean API exception handling.

---

## 🏗️ Architecture & Tech Stack

### Backend
* **Language & Framework:** Java 21, Spring Boot 4.1.0
* **AI & LLM Integration:** Spring AI 2.0.0 (Google Gemini 3.5 Flash & Gemini Embedding)
* **Vector Database:** PostgreSQL 16 + pgvector (HNSW Indexing)
* **Security & Persistence:** Spring Security, JWT, Spring Data JPA / Hibernate
* **Document Parsing:** Apache Tika & Spring AI PDF/Document Readers
* **API Documentation:** SpringDoc OpenAPI / Swagger UI (`/swagger-ui.html`)

### Frontend
* **Core:** React 19, TypeScript, Vite
* **Styling:** Tailwind CSS v4, Lucide React Icons
* **State Management:** Zustand
* **Routing:** React Router v7
* **Markdown Rendering:** React Markdown, PrismJS, Remark GFM

---

## 🚀 Getting Started Locally

### Prerequisites
* **Java 21**
* **Node.js 18+**
* **Docker Desktop**
* **Google Gemini API Key** (from [Google AI Studio](https://aistudio.google.com/app/apikey))

### 1. Start the Database
Start PostgreSQL with the pgvector extension:
```bash
docker compose up -d
```

### 2. Run the Spring Boot Backend
Set your Gemini API key and launch the backend:

**PowerShell (Windows):**
```powershell
$env:GEMINI_API_KEY="your-gemini-api-key"
.\mvnw.cmd spring-boot:run
```

**Bash (Linux / macOS):**
```bash
export GEMINI_API_KEY="your-gemini-api-key"
./mvnw spring-boot:run
```
* Backend runs at: `http://localhost:8081`
* Swagger UI docs: `http://localhost:8081/swagger-ui.html`

### 3. Run the Frontend
In a new terminal:
```bash
cd frontend/docmind-frontend
npm install
npm run dev
```
* Open your browser at: **`http://localhost:3000`**

---

## ☁️ Deployment

### 100% Free Cloud Stack
* **Database:** [Supabase](https://supabase.com) (Free PostgreSQL + pgvector)
* **Backend:** [Render](https://render.com) (Free Docker Web Service with root `Dockerfile`)
* **Frontend:** [Vercel](https://vercel.com) (Free Vite deployment via `frontend/docmind-frontend` with `vercel.json` rewrites)

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
