# Vidbox

Vidbox is a media-management application that allows users to upload pictures and videos and optionally group them into albums. Albums and files can be posted to a users Telegram channel.

Vidbox is an http-api demo project utilizing some of the following technologies:   
- Spring Boot (https://spring.io/projects/spring-boot)
- Keycloak (https://www.keycloak.org/)
- Docker & Docker Compose (https://www.docker.com/)
- Kotlin (https://kotlinlang.org/)

## Media
Vidbox allows uploading and storing of both image and video files. These files can be stored individually, or grouped into albums.  

## Albums
Media files can be grouped into albums, which can be used to perform basic video editing tasks such as clipping and concatenating.

## Broadcasting
When supplied with a Telegram api-key, Vidbox can be used to automatically broadcast individual files to a Telegram channel.

## Documentation
Vidbox api documentation can be found at path: /swagger-ui/index.html of the deployed project.

# Deployment

### Docker Compose
```bash
$ docker-compose up -d
```
