vault mount -path jks_store generic
vault write jks_store/trust_store value=$(base64 trust.jks)
vault write secret/trust_pass value=s3cr3t
vault write jks_store/key_store value=$(base64 key.jks)
vault write secret/key_pass value=s3cr3t
