BACKEND_SERVER=https://server.mutual.shaozhenpcf.com \
KEY_STORE=./client.jks \
KEY_STORE_PASSWORD=s3cr3t \
TRUST_STORE=./client_trust.jks \
TRUST_STORE_PASSWORD=s3cr3t \
BACK_END=ENV \
java -jar client/target/client-0.0.1-SNAPSHOT.jar
