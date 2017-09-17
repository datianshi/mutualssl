KEYSTORE=
if [ "${MUTUAL_AUTH}" = "true" ]
then
    KEYSTORE="-Djavax.net.ssl.keyStore=./client.jks -Djavax.net.ssl.keyStorePassword=s3cr3t"
fi

BACKEND_SERVER=https://server.mutual.shaozhenpcf.com \
SERVER_PORT=8082 \
java -Djavax.net.ssl.trustStore=./client_trust.jks -Djavax.net.ssl.trustStorePassword=s3cr3t $KEYSTORE -jar client/target/client-0.0.1-SNAPSHOT.jar