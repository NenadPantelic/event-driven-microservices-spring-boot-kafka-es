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

Briefly, this error occurs when the disk usage on an OpenSearch node exceeds the “flood stage” watermark, which is a set threshold (usually 95%). When this happens, OpenSearch automatically sets all indices on the node to read-only to prevent further data ingestion and potential data loss.

`GET localhost:9200/twitter-index/_search?q=id:1`