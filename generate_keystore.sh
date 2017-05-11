echo "$#"
echo $1

if [ "$#" -ne 2 ]; then
  echo "generate_keystore.sh [SERVER_DOMAIN] [CLIENT_DOMAIN]"
  exit 1
fi

SERVER_DOMAIN=$1
CLIENT_DOMAIN=$2
keytool -genkeypair -alias serverkey -keyalg RSA -dname "CN=${SERVER_DOMAIN},OU=PCFS,O=Pivotal,L=Ave of Americas,S=TX,C=US" -keypass s3cr3t -keystore server.jks -storepass s3cr3t
keytool -exportcert -alias serverkey -file server-public.cer -keystore server.jks -storepass s3cr3t

keytool -genkeypair -alias clientkey -keyalg RSA -dname "CN=${CLIENT_DOMAIN},OU=PCFS,O=Pivotal,L=Ave of Americas,S=TX,C=US" -keypass s3cr3t -keystore client.jks -storepass s3cr3t
keytool -exportcert -alias clientkey -file client-public.cer -keystore client.jks -storepass s3cr3t

keytool -importcert -keystore server_trust.jks -alias clientcert -file client-public.cer -storepass s3cr3t -noprompt
keytool -importcert -keystore client_trust.jks -alias servercert -file server-public.cer -storepass s3cr3t -noprompt

cp server_trust.jks server.jks server/src/main/resources/
cp client_trust.jks client.jks client/src/main/resources/
