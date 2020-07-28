Interview question
==================


This is a very basic spring-boot app. Run it (using `mvn spring-boot:run`) or your favorite IDE.
Try the url `http://localhost:5000/greeting?name=David`, it should return the string: "Hello David".

There is also a package persistence which contains a basic CRUD example with persistence - this will be addressed in part 3.

# Requirements
### Part one - Basic local service
We would like to have a service that shorten URLs, i.e.:
* A REST API that takes as input a URL and gives back a short URL (hashcode perhaps?)
* A REST API that takes the short URL and gives back the original URL

#### Acceptance criteria
Sending `http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38` Should return a tiny expression (e.g., `44C0173`)

Sending `http://localhost:5000/long?tiny=44C0173` Should give back `https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json`

### Part two - Adding persistence layer
We would like to have persistence of the data in case the server drops.
`application.yml` is configured for H2 database, but feel free to use any other relational DB you are comfortable with to save the data.
Make sure that your app will work with H2 as well as it will be tested with H2 (integartion-tests can help here).

#### Acceptance criteria
Running `http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38` Should return a tiny expression (e.g., `44C0173`)
Restarting the spring-boot app and Sending `http://localhost:5000/long?tiny=44C0173` should give back `https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json`

### Part three - Adding timeout for data
In order to avoid extremely large DB, we would like to purge URLs older than 1/2 hour.
Check how to create a scheduled task in Spring to go over the DB and purge records older than 30mins.

#### Acceptance criteria
Running `http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38` Should return a tiny expression (e.g., `44C0173`)

Sending `http://localhost:5000/long?tiny=44C0173` **immediately** should give back `https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json`

Sending `http://localhost:5000/long?tiny=44C0173` **after 1/2 hour** should give back `URL not found in DB`

## Guidelines
* Fork this repository and push your commits
* Use the spring-boot template given
* Write unit-tests, integration-tests
* Write code documentation
* All classes given are meant to used as references - once they are not needed, they can be removed.
* This project uses [lombok](https://projectlombok.org/) - please use it when possible
* Add the required files to .gitignore, etc.
* Do all 3 parts, and use git tags to mark the commit fulfilling part 1, part 2 and part 3.

## Deliverables
* Send us a link to a repository fulfilling the requirements and the three tags to check part 1, 2, and 3.

