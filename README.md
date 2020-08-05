##Part 1: Implementation ###

##A REST API that takes as input a URL and gives back a short URL

###Positive Scenario:
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38

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



##A REST API that takes the short URL and gives back the original URL

###Positive Scenario:
Request:
http://localhost:5000/long?tiny=44C0173

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38

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
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38

Response:
44C0173


###Positive Scenario #2(From DB):
Request:  
http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json#38

Response:
44C0174


###Positive Scenario #3(From server/property file):
Request:
http://localhost:5000/long?tiny=44C0174

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json#38



###Positive Scenario #4(From server/property file):
Request:
http://localhost:5000/long?tiny=44C0174

Response:
https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec1/browse/src/main/resources/schemas/definitions.json#38



