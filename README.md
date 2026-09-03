# AI Job Agent

AI Job Assistant Agent built with Java 17, Spring Boot, Maven, Spring AI, REST APIs, PostgreSQL, and Spring Data JPA.

## Verified Spring AI API

- Spring Boot: `3.5.15`
- Spring AI: `1.1.8`
- `@Tool` import: `org.springframework.ai.tool.annotation.Tool`
- Tool parameter import: `org.springframework.ai.tool.annotation.ToolParam`
- ChatClient tool registration: `ChatClient.Builder.defaultTools(jobSearchTool, skillMatchTool, calculatorTool)`
- Tool objects are registered directly, not by string names.

References:

- Spring AI tool calling reference: https://docs.spring.io/spring-ai/reference/api/tools.html
- Spring AI ChatClient reference: https://docs.spring.io/spring-ai/reference/api/chatclient.html
- Spring AI compatibility notes: https://github.com/spring-projects/spring-ai

## Folder Structure

```text
src/main/java/com/example/aijobagent
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── service
└── tools
```

## Environment Variables

```bash
OPENAI_API_KEY=your_openai_api_key
ADZUNA_APP_ID=your_adzuna_app_id
ADZUNA_APP_KEY=your_adzuna_app_key
DATABASE_URL=jdbc:postgresql://localhost:5432/ai_job_agent
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

Live job listings

The job search uses the Adzuna API to display current listings. Register at https://developer.adzuna.com/ and add the returned `app_id` and `app_key` as `ADZUNA_APP_ID` and `ADZUNA_APP_KEY`. The default country is India (`in`); change it with `ADZUNA_COUNTRY` if needed. Without Adzuna credentials, the local seeded jobs remain available through the database endpoints, but the live search page will ask for configuration.

## Run

```bash
mvn clean install
mvn spring-boot:run
```

## Sample Postman Requests

### AI Agent

`POST http://localhost:8080/api/agent/ask`

```json
{
  "question": "Find Java developer jobs in Pune"
}
```

### Create Job

`POST http://localhost:8080/api/jobs`

```json
{
  "title": "Java Developer",
  "company": "Acme Tech",
  "location": "Pune",
  "technology": "Java, Spring Boot, PostgreSQL",
  "experience": "2-4 years",
  "salary": "12-18 LPA",
  "description": "Build REST APIs using Java 17, Spring Boot, JPA, PostgreSQL, and Maven."
}
```

### Get Jobs

`GET http://localhost:8080/api/jobs`

### Update Job

`PUT http://localhost:8080/api/jobs/1`

```json
{
  "title": "Senior Java Developer",
  "company": "Acme Tech",
  "location": "Pune",
  "technology": "Java, Spring Boot, PostgreSQL, AWS",
  "experience": "4-6 years",
  "salary": "20-28 LPA",
  "description": "Lead backend development with Java 17, Spring Boot, JPA, PostgreSQL, AWS, and REST APIs."
}
```

### Delete Job

`DELETE http://localhost:8080/api/jobs/1`
