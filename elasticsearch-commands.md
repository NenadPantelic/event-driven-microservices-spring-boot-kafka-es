Index API:

`PUT localhost:9200/twitter-index`
```json
{
  "mappings": {
    "properties": {
      "userId": {
        "type": "long"
      },
      "id": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "createdAt": {
        "type": "date",
        "format": "yyyy-MM-dd'T'HH:mm:ssZZ"
      },
      "text": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      }
    }
  }
}
```

`POST twitter-index/_doc/1`
```json

{
  "userId": "1",
  "id": "1",
  "createdAt": "2020-01-01T23:00:50+0000",
  "text": "test multi word"
}

```

I found the solution. The problem has to do with the disk usage in total as described in the answer from sastorsl here: low disk watermark [??%] exceeded on

The storage of the cluster I worked on was 98% used. While there were 400GB free, Elasticsearch only looks at the percentages, thus shutting down any write permissions of indices.

The solution is to manually set the watermarks after the nodes have started (setting them in the elasticsearch.yml didn't work for me for some reason):

`curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_cluster/settings -d '{ "transient": { "cluster.routing.allocation.disk.threshold_enabled": false } }'`
`curl -XPUT -H "Content-Type: application/json" http://localhost:9200/_all/_settings -d '{"index.blocks.read_only_allow_delete": null}'`


sudo sysctl -w vm.max_map_count=262144
Briefly, this error occurs when the disk usage on an OpenSearch node exceeds the “flood stage” watermark, which is a set threshold (usually 95%). When this happens, OpenSearch automatically sets all indices on the node to read-only to prevent further data ingestion and potential data loss.

`GET localhost:9200/twitter-index/_search?q=id:1`

ElasticSearch operations:
```
GET twitter-index/_search -> Get all data (Max 10_000 records - use scroll API to get more)
GET twitter-index/_search?size=2000 -> specify size in query
GET twitter-index/_search?q=id:1 -> get data with id = 1
GET twitter-index/_search?q=text:test -> get data with text=test
```


Query API:
POST twitter-index/_search
```json
{
  "query": {
    "term": {
      "text": "test"
    }
  }
}
```

Query types:

1. Match query
- Match uses analyzed words to match any documents. It uses every word in input query, analyze them and get combined
results for each word

```json
{
  "query": {
    "match": {
      "text": "test multi word"
    }
  }
}
```

Above query will get records that include test or multi or word and return all of them in one result.

2. Term query
- uses exact term and does not analyze the input. So for a multi-word sentence if you search all sentence
with term it will look for a whole sentence in inverted index.


```json
{
  "query": {
    "term": {
      "text": "test multi word"
    }
  }
}
```
A bit faster than the match query (does not use analyzers)

Inverted index indexes word by word, not the whole text. This query will return nothing.

3. Using keyword

```json
{
  "text": {
    "type": "text",
    "fields": {
      "keyword": {
        "type": "keyword",
        "ignore_above": 256
      }
    }
  }
}
```


```json
{
  "query": {
    "term": {
      "text.keyword": "test multi word"
    }
  }
}
```

For the exact search the term. (.keyword does not use analyzers, compares the exact term).

4. Wildcard query
- uses wildcards to match the search term. It can be slow as it requires scan.
```json
{
  "query": {
    "wildcard": {
      "text": "te*"
    }
  }
}
```

The above query will return all documents that include word that start with `te`. Here `*` means 0 or more 
characters. We can also use `?` for a single character.

5. Query string
`query_string`: analyses input (can use wildcards). Can give multiple fields in `query_string` fields
property to search inside all fields. Similar to wildcard search, but more flexible syntax.
```json
{
  "query": {
    "query_string": {
      "query": "text:te*"
    }
  }
}
```

6. Complex queries
Returns documents where text contains both `test` and `word`.
```json

{
  "from": 0,
  "size": 20,
  "query": {
    "bool": {
      "must": [{
        "match": {
           "text": "test" 
          }
        },
        {
          "match": {
            "text": "word"
          }
        }
      ]
    }
  }
}

```

Returns the union of documents where text contains `test` or `word`.

```json

{
  "from": 0,
  "size": 20,
  "query": {
    "bool": {
      "should": [{
        "match": {
           "text": "test" 
          }
        },
        {
          "match": {
            "text": "word"
          }
        }
      ]
    }
  }
}

```