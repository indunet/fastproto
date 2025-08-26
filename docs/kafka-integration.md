## Kafka Integration

This guide shows how to integrate FastProto with Apache Kafka to encode/decode message values.

### Dependency

Add the Kafka clients dependency in your application (FastProto itself declares it as provided):

```xml
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-clients</artifactId>
  <version>3.8.x</version>
</dependency>
```

### Codecs

- Serializer: `FastProtoSerializer` encodes objects to `byte[]`.
- Deserializer: `FastProtoDeserializer` decodes `byte[]` to objects.
- Serde: `FastProtoSerde` for Kafka Streams or unified configs.

FastProto only handles the in-frame schema. Kafka provides message framing per record.

### Producer/Consumer Configuration

```java
Properties props = new Properties();
props.put("value.serializer", FastProtoSerializer.class.getName());
props.put("value.deserializer", FastProtoDeserializer.class.getName());
props.put("fastproto.value.type", "com.example.MyPojo");
```

- `fastproto.value.type`: target class for deserialization (FQCN or `Class`).

### Serde Usage (Kafka Streams)

```java
Map<String, Object> cfg = new HashMap<>();
cfg.put("fastproto.value.type", MyPojo.class.getName());
Serde<MyPojo> serde = new FastProtoSerde<>();
serde.configure(cfg, false);
```

### Notes

- Deserialization requires `fastproto.value.type`; otherwise, an exception is thrown.
- Your POJO must be annotated with FastProto mapping annotations.
- Coordinate schema layout between producers and consumers when evolving messages.

### Testing

Round-trip serialization test:

```java
MyPojo pojo = ...;
byte[] bytes = new FastProtoSerializer<MyPojo>().serialize("t", pojo);
FastProtoDeserializer<MyPojo> d = new FastProtoDeserializer<>();
Map<String, Object> cfg = new HashMap<>();
cfg.put("fastproto.value.type", MyPojo.class.getName());
d.configure(cfg, false);
MyPojo decoded = d.deserialize("t", bytes);
``` 