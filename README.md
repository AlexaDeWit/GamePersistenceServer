[![Known Vulnerabilities](https://snyk.io/test/github/alexadewit/gamepersistenceserver/badge.svg)](https://snyk.io/test/github/alexadewit/gamepersistenceserver)

## Setup

### Docker-Postgres

- Setup docker as instructed by the docker docs.
- Create the docker container via `sudo docker run --name GameDb -p 5432:5432 -e POSTGRES_PASSWORD=pgdev -d postgres`