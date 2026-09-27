# Personal Notes MCP Server

A [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server built with Spring Boot and Spring AI. It lets an MCP client such as Claude Desktop create, read, update and delete personal notes stored in a PostgreSQL database.

The server communicates over **stdio**, so the MCP client starts it as a local process.

## Tech Stack

| Component                   | Version                  |
|-----------------------------|--------------------------|
| Java                        | 21                       |
| Spring Boot                 | 4.1.1                    |
| Spring AI (MCP Server)      | 2.0.1                    |
| Spring Data JPA / Hibernate | via Spring Boot          |
| PostgreSQL                  | any recent version       |
| Lombok                      | via Spring Boot          |
| Build tool                  | Maven (wrapper included) |

## MCP Tools

The server exposes the following tools, defined in `NoteServiceImpl` with `@Tool` annotations:

| Tool          | Description                                                   | Parameters                                             |
|---------------|---------------------------------------------------------------|--------------------------------------------------------|
| `addNote`     | Add a new note                                                | `title` (required), `content` (max 5000 chars), `tags` |
| `getNoteById` | Fetch a single note by its id                                 | `id`                                                   |
| `getAllNotes` | List notes page by page, newest first                         | `page` (starts at 0), `size`                           |
| `updateNote`  | Update an existing note; only the fields provided are changed | `id`, `title`, `content`, `tags`                       |
| `deleteNote`  | Permanently delete a note                                     | `id`                                                   |
| `searchNotes` | Find notes whose title or content contains the text (case-insensitive), newest first | `query`                          |
| `getNotesByTag` | List notes with an exact tag match (case-insensitive), newest first | `tag`                                            |
| `getRecentNotes` | Get the most recently created notes                        | `limit` (1 to 50)                                      |
| `countNotes`  | Count the total number of notes                               | none                                                   |

Each note is returned in this shape:

```json
{
  "id": 1,
  "title": "Groceries",
  "content": "Milk, eggs, bread",
  "tags": "personal,shopping",
  "createdAt": "2026-09-27T12:45:34"
}
```

`tags` is stored as a single free-form string (for example, comma-separated values).

## Project Structure

```
src/main/java/com/reon/mcppersonalnotes
├── config/        McpConfig: registers the note service's methods as MCP tools
├── dto/           CreateNote, UpdateNote, NoteResponse records
├── entity/        Note JPA entity (table: personal_notes)
├── exception/     NoteNotFoundException
├── mapper/        NoteMapper: entity <-> DTO conversion
├── repository/    NoteRepository (Spring Data JPA)
└── service/       NoteService (validation constraints) and NoteServiceImpl (the @Tool methods)
```

## Prerequisites

- JDK 21
- A running PostgreSQL instance
- An MCP client (for example, Claude Desktop)

## Configuration

Database credentials are read from a `.env` file in the project root. This file is ignored by git. Create it from the example file:

```bash
cp .env.example .env
```

Then fill in your values:

```properties
DB_URL=jdbc:postgresql://localhost:5432/notes
DB_USERNAME=your-db-username
DB_PASSWORD=your-db-password
```

`application.yml` imports this file with `spring.config.import: optional:file:.env[.properties]` and reads the values as `${DB_URL}`, `${DB_USERNAME}` and `${DB_PASSWORD}`. Environment variables with the same names work as well.

Notes:

- The `.env` path is resolved relative to the directory the application starts in. When an MCP client launches the server, pass the absolute path instead (see [Connecting to Claude Desktop](#connecting-to-claude-desktop)).
- `spring.jpa.hibernate.ddl-auto: update` creates the `personal_notes` table on first start, so no manual schema setup is needed.
- `spring.main.web-application-type: none`, `banner-mode: off` and `logging.console.enabled: false` are required for stdio mode. The MCP protocol runs over stdout, so nothing else should be written there.
- Logs are written to the file set in `logging.file.name`. Use an absolute path, because the MCP client decides the working directory the server starts in.

## Build

```bash
./mvnw clean package
```

On Windows:

```bash
mvnw.cmd clean package
```

This produces `target/personal-notes-mcp-server.jar`.

## Connecting to Claude Desktop

Open Claude Desktop → **Settings → Developer → Edit Config** and add the server to `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "personal-notes-mcp-server": {
      "command": "C:\\Program Files\\Java\\jdk-21\\bin\\java.exe",
      "args": [
        "-Dspring.config.import=optional:file:C:/path/to/mcp-personal-notes/.env[.properties]",
        "-jar",
        "C:\\path\\to\\mcp-personal-notes\\target\\personal-notes-mcp-server.jar"
      ]
    }
  }
}
```

Claude Desktop does not start the server from the project directory, so the `-Dspring.config.import` argument gives it the absolute path to your `.env` file. Alternatively, remove that argument and pass `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` in an `"env"` block.

Use absolute paths, with backslashes escaped as `\\` on Windows. Fully quit and reopen Claude Desktop after changing the config. The tools listed above should then be available in chat.

## Troubleshooting

MCP server logs from Claude Desktop are in `%APPDATA%\Claude\logs\mcp-server-personal-notes-mcp-server.log` on Windows.

| Symptom                                | Likely cause                                                                                    |
|----------------------------------------|-------------------------------------------------------------------------------------------------|
| `Unable to access jarfile`             | The jar path in `claude_desktop_config.json` is wrong or not absolute                           |
| `Access is denied` for the log file    | `logging.file.name` is a relative path; use an absolute path                                    |
| `Server transport closed unexpectedly` | The process exited early (check the database connection) or wrote non-protocol output to stdout |
