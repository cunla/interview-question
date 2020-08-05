##Part 1: Implementation ###

###A REST API that takes as input a URL and gives back a short URL

###Positive Scenario:
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json

Response:
44C0173

###Negative Scenario:
http://localhost:5000/short?url=https://stash.backbase.com
{
    "timestamp": "2020-08-04T22:57:28.747+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "Not a valid URL",
    "path": "/short"
}



###A REST API that takes the short URL and gives back the original URL

###Positive Scenario:
Request:
http://localhost:5000/long?tiny=44C0173

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json

###Negative Scenario:

http://localhost:5000/short?tiny=44C
{
    "timestamp": "2020-08-04T22:56:27.213+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "Not a valid hashCode",
    "path": "/long"
}




##Part 2 : Implementation - Persistence ###

###Positive Scenario #1(From server/property file):
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json

Response:
44C0173


###Positive Scenario #2(From DB):
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json

Response:
44C0174


###Positive Scenario #3(From server/property file):
Request:
http://localhost:5000/long?tiny=44C0173

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json



###Positive Scenario #4(From DB):
Request:
http://localhost:5000/long?tiny=44C0174

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json



##Part 3 : Implementation - Scheduler ###
Currently, scheduler initial delay is set to 1 min, and checks for records older than 30 mins in the H2 database every 2 mins.If it finds any records, it deletes the records.
Job enabled should be set to true in application.yml for scheduler to run 
jobs:
 enabled: true
If scheduler finds any older records, it deletes the records and throws exception that data is not found.


###Positive Scenario #1(From server/property file):
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json

Response:
44C0173


###Positive Scenario #2(From DB):
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json

Response:
44C0174


###Positive Scenario #3(From server/property file):
Request:
http://localhost:5000/long?tiny=44C0173

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json



###Positive Scenario #4(From DB):
Request:
http://localhost:5000/long?tiny=44C0174

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json


###Negative Scenario #1:

http://localhost:5000/long?tiny=44C0174

{
    "timestamp": "2020-08-05T02:57:20.347+0000",
    "status": 404,
    "error": "Not Found",
    "message": "No URL record exist for given hashcode",
    "path": "/long"
}

###Negative Scenario #2:
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resource/schemas/definitions.json

{
    "timestamp": "2020-08-05T03:01:55.492+0000",
    "status": 404,
    "error": "Not Found",
    "message": "No hashCode exist for given URL",
    "path": "/short"
}

