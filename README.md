# Natural Language to SQL Query Generator
_A cross-database AI-powered SQL generation tool with safety, audit, and on-prem LLM support._

## Overview
This project enables users to convert natural language requests into **database-specific SQL queries**.  
It supports **Oracle, MySQL, PostgreSQL, and H2** out of the box, with AI safety features to block destructive queries.  
You can run it **entirely on-premise** using [Ollama](https://ollama.ai/) for local LLM inference, or connect to **GPT models** in production.

## Key Features
- **Multi-DB Support**: Oracle, MySQL, PostgreSQL, H2 (extendable to more)
- **Local LLM Support**: Runs with Ollama for offline inference
- **Safe Query Generation**:
  - Blocks unsafe queries (`DROP`, `TRUNCATE`, `DELETE` without `WHERE`)
  - Dry-run mode for UPDATE/DELETE
- **DB-Specific Syntax**: Date formats, limit/offset handling per DB
- **Query Audit Logging**: Stores prompts, SQL, timestamp, and DB type
- **Role-based Restrictions**: Only authorized users can execute queries
- **Switchable AI Provider**: Swap between Ollama (local) and GPT (cloud)

## Architecture
[Frontend UI] --> [Spring Boot REST API]
                  |-> Prompt Builder (DB-specific)
                  |-> AI Service (Ollama or GPT)
                  |-> SQL Safety Validator
                  |-> Audit Logger (H2)

## Tech Stack
- **Backend**: Java 17, Spring Boot
- **Database**: H2 (demo), Oracle/MySQL/PostgreSQL (target)
- **LLM**: Ollama (local), OpenAI GPT (production-ready)
- **Frontend**: HTML/JS (minimal) or React (optional)
- **Build Tool**: Maven or Gradle

## Setup Instructions

### 1. Prerequisites
- macOS / Linux / Windows (Mac M1 tested)
- Java 17+
- Maven or Gradle
- Node.js (optional for React UI)
- [Ollama](https://ollama.ai/) installed locally

### 2. Install Ollama Model
Example using Mistral:
```bash
ollama pull mistral
```
Or a SQL-tuned model if available:
```bash
ollama pull sqlcoder
```

### 3. Clone Repository
```bash
git clone https://github.com/<your-repo>.git
cd <your-repo>
```

### 4. Configure Application
Edit `src/main/resources/application.yml`:
```yaml
app:
  ai:
    provider: ollama   # ollama or openai
    model: mistral
  db:
    type: h2           # h2, oracle, mysql, postgres
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password:
```

### 5. Run Backend
```bash
mvn spring-boot:run
```

### 6. Access UI / API
- **Frontend** (if implemented): `http://localhost:8080`
- **REST API**:
  - Generate SQL: `POST /api/generate`
    ```json
    {
      "prompt": "List all active employees",
      "dbType": "oracle"
    }
    ```
  - View history: `GET /api/history`

## Usage Examples

**Prompt**:
```
List all employees who joined after 2020
```

**Generated SQL** (Oracle):
```sql
SELECT * FROM employees WHERE join_date > TO_DATE('2020-01-01', 'YYYY-MM-DD');
```

## Security & Safety
- **Query Blocking**: Prevents accidental/destructive DDL/DML commands
- **Audit Logs**: Every query is stored for review
- **On-Prem Execution**: Sensitive data never leaves your environment when using Ollama
- **Role Restriction**: Only users with execute rights can run queries

## Future Enhancements
- Support for more DB dialects
- Integration with corporate SSO
- Fine-tuned SQL generation model
- Query optimization suggestions

## License
MIT License — see [LICENSE](LICENSE) for details.
