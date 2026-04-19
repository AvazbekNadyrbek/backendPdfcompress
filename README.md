# PDF Squeeze — Backend

Spring Boot REST API for compressing PDF files using Ghostscript.

## Tech Stack

- Java 21
- Spring Boot 3.2
- Ghostscript (PDF compression engine)
- Maven

## Architecture

```
iOS Client
    ↓ multipart/form-data
Spring Boot API
    ↓ ProcessBuilder
Ghostscript CLI
    ↓ compressed bytes
Spring Boot API
    ↓ application/pdf
iOS Client
```

## Getting Started

### Prerequisites

- Java 21+
- Maven
- Ghostscript

```bash
# macOS
brew install ghostscript

# Ubuntu / Debian
apt-get install ghostscript

# Verify
gs --version
```

### Run Locally

```bash
git clone https://github.com/YOUR_USERNAME/pdf-squeeze-backend.git
cd pdf-squeeze-backend
mvn spring-boot:run
```

Server starts on `http://localhost:8080`

## API

### POST /api/pdf/compress

Compress a PDF file.

**Request**

| Field   | Type   | Description                          |
|---------|--------|--------------------------------------|
| file    | File   | PDF file (max 50MB)                  |
| quality | String | `low` / `medium` / `high` (default: medium) |

**Quality levels**

| Value  | Ghostscript Setting | Use Case              |
|--------|--------------------|-----------------------|
| low    | /screen            | Maximum compression   |
| medium | /ebook             | Balanced (recommended)|
| high   | /printer           | Best quality          |

**Response**

Returns compressed PDF as `application/pdf`

**Example**

```bash
curl -X POST http://localhost:8080/api/pdf/compress \
  -F "file=@document.pdf" \
  -F "quality=medium" \
  --output compressed.pdf
```

**Validation**

- File must be a PDF
- Max file size: 50MB
- Returns `400 Bad Request` if validation fails

## Configuration

`src/main/resources/application.properties`

```properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

## Privacy

Uploaded files are stored in a temporary directory during processing only.
Both input and output files are deleted immediately after compression.
No files are persisted on the server.

## Deployment

### Docker (coming soon)

```dockerfile
FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y ghostscript
COPY target/backend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Hetzner VPS

```bash
# Install dependencies
apt-get install ghostscript java-21-openjdk

# Run
java -jar backend.jar
```

## Roadmap

- [ ] Docker support
- [ ] Rate limiting
- [ ] API key authentication
- [ ] Deploy to Hetzner
- [ ] Connect iOS client

## License

MIT
