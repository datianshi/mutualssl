if [ "$#" -ne 1 ]; then
  echo "generate_keystore.sh [CLIENT_DOMAIN]"
  exit 1
fi

CLIENT_DOMAIN=$1

keytool -genkeypair -alias clientkey -keyalg RSA -dname "CN=${CLIENT_DOMAIN},OU=PCFS,O=Pivotal,L=Ave of Americas,S=TX,C=US" -keypass s3cr3t -keystore client.jks -storepass s3cr3t
keytool -exportcert -alias clientkey -file client-public.cer -keystore client.jks -storepass s3cr3t

keytool -importcert -keystore client_trust.jks -alias cfcert -file cf.crt -storepass s3cr3t -noprompt

cp client_trust.jks client.jks client/src/main/resources/
