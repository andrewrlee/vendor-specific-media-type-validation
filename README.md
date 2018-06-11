
## Dynamically validating requests with vendor specific media types

Performing up front validation of requests based on a content type to json schema mapping.

Then processing accepted models with custom fields in a generic fashion, using jackson's `@JsonAnySetter`.  


### Request Validation Example:

Our client, "Fizzbuzz Incorporated" has been assigned a media type of `application/vnd.optimisticpanda.fizzbuzz.product-v1+json`.
It usually sends payloads to our import endpoint in the following structure: 

```json
[
  {
    "id" : 1111,
    "name": "Turpentine",
    "price": 12.23,
    "origin" : "Singapore",
    "count" : 100,
    "tags" : ["paint supplies"],
    "dimensions" : {
      "length": 100,
      "width" :  30.21,
      "height": 20,
      "weight": 10.1
    },
    "warehouseLocation" : {
      "latitude": 1.0,
      "longitude" : 2.0
    }

  }
]
```

This time though it has sent us an order in the format of its arch rival, "Acme LTD" with a media type of `application/vnd.optimisticpanda.acme.product-v1+json` 
This media type is verified under the following (incompatible) schema:

```json
{
    "$schema": "http://json-schema.org/draft-06/schema#",
    "title": "Product set",
    "type": "array",
    "items": {
        "title": "Product",
        "type": "object",
        "additionalProperties": false,
        "properties": {
            "id": {
                "description": "The unique identifier for a product",
                "type": "number"
            },
            "name": {
                "type": "string"
            },
            "price": {
                "type": "number",
                "exclusiveMinimum": 0
            },
            "date_produced": {
                "type": "string",
              "format": "date-time"
            },
            "catalog_id": {
                "type": "string"
            },
            "tags": {
                "type": "array",
                "items": {
                    "type": "string"
                },
                "minItems": 1,
                "uniqueItems": true
            },
            "dimensions": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                    "length": {"type": "number"},
                    "width": {"type": "number"},
                    "height": {"type": "number"}
                },
                "required": ["length", "width", "height"]
            },
            "warehouseLocation": {
                "description": "Coordinates of the warehouse with the product",
                "$ref": "http://json-schema.org/geo"
            }
        },
        "required": ["id", "name", "price", "date_produced", "catalog_id"]
    }
}
```

This causes Fizzbuzz to fail to have their order validated with a 400 error response with the error payload: 

```json
{
  "errors": [
    "#/0: 5 schema violations found",
    "#/0: required key [date_produced] not found",
    "#/0: required key [catalog_id] not found",
    "#/0: extraneous key [origin] is not permitted",
    "#/0: extraneous key [count] is not permitted",
    "#/0/dimensions: extraneous key [weight] is not permitted"]
}
```


### Generic Processing Example:

An Acme request like: 

```json
[
  {
    "id" : 1111,
    "name": "Hedge Cutters",
    "price": 12.23,
    "date_produced" : "2015-09-28T10:40:00+02:00",
    "catalog_id" : "111",
    "tags" : ["garden"],
    "dimensions" : {
      "length": 1000,
      "width" :  12.21,
      "height": 122
    },
    "warehouseLocation" : {
      "latitude": 1.0,
      "longitude" : 2.0
    }
  }
]

```

Is processed by our backend system into an internal canonical representation like:

```json
{
  "count": 1,
  "records": [
    {
      "id": "1111",
      "name": "Hedge Cutters",
      "price": "12.23",
      "tags": [
        "garden"
      ],
      "dimensions": {
        "length": 1000,
        "width": 12,
        "extras": {
          "height": "122"
        }
      },
      "warehouseLocation": {
        "extras": {
          "latitude": "1.0",
          "longitude": "2.0"
        }
      },
      "extras": {
        "catalog_id": "111",
        "date_produced": "2015-09-28T10:40:00+02:00"
      }
    }
  ]
}
```

Whereas, a fizzbuzz request of: 

```json
[
  {
    "id" : 1111,
    "name": "Turpentine",
    "price": 12.23,
    "origin" : "Singapore",
    "count" : 100,
    "tags" : ["paint supplies"],
    "dimensions" : {
      "length": 100,
      "width" :  30.21,
      "height": 20,
      "weight": 10.1
    },
    "warehouseLocation" : {
      "latitude": 1.0,
      "longitude" : 2.0
    }

  }
]

```

is converted to a canoncial representation matching:

```json
{
	"count": 1,
	"records": [{
		"id": "1111",
		"name": "Turpentine",
		"price": "12.23",
		"tags": ["paint supplies"],
		"dimensions": {
			"length": 100,
			"width": 30,
			"extras": {
				"weight": "10.1",
				"height": "20"
			}
		},
		"warehouseLocation": {
			"extras": {
				"latitude": "1.0",
				"longitude": "2.0"
			}
		},
		"extras": {
			"origin": "Singapore",
			"count": "100"
		}
	}]
}
```

### TODO

 * Json schema has suggestions of media type formats
 * Tidy up a bit
 * Dynamic state validation

