# NYT Technology News Reader

A Spring Boot application that fetches and displays technology news articles from the New York Times RSS feed, with caching and internationalization support.

## Features

- **RSS Feed Integration**: Fetches real-time data from NYT Technology RSS feed
- **Reactive Architecture**: Built with Spring WebFlux for non-blocking I/O
- **Caching**: 15-minute cache duration for RSS feed responses
- **Multi-Language Support**: English/Spanish UI with dynamic language switching
- **Responsive UI**: Thymeleaf-based single-page application with modern design
- **Configurable**: Easy-to-modify timeouts, cache settings, and API endpoints

## Prerequisites

- Java 21
- Maven 3.6+
- Internet access (for NYT RSS feed)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/Kham300/nyt-tech-rss-single-page-app.git
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

## Configuration

Edit `application.yml`:
```yaml
nyt:
  api:
    base-url: https://rss.nytimes.com
    rss-path: /services/xml/rss/nyt/Technology.xml
    cache-duration: 15m
    timeouts:
      connect: 5s
      read: 10s

spring:
  messages:
    basename: i18n/messages
```

## Usage

- **API Endpoint**: `GET http://localhost:8080/api/articles`
- **Web UI**: `http://localhost:8080`
    - Switch languages using the ENG/ESP links
    - Click article cards to open original NYT articles

## Testing

Run integration tests:
```bash
mvn test
```

Tests include:
- Successful feed parsing
- Error handling scenarios
- Cache behavior validation
- UI rendering checks

## Technology Stack

- Java 21
- Spring Boot 3.2
- WebFlux/WebClient
- Thymeleaf
- Caffeine Cache
- JUnit 5
- MockWebServer

