echo "This is profile.d"
echo $PATH
export PATH=$PATH:./.java-buildpack/open_jdk_jre/bin
openssl pkcs12 -export -in $CF_INSTANCE_CERT -inkey $CF_INSTANCE_KEY -out temp_pk12.p12 -passout "pass:${STORE_PASS}"
keytool -importkeystore -deststorepass ${STORE_PASS} -destkeystore server.jks -srckeystore temp_pk12.p12 -srcstoretype PKCS12 -srcstorepass ${STORE_PASS} -noprompt
mv server.jks BOOT-INF/classes/
