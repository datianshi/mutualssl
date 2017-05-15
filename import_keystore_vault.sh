

#For demo purpose.... Assume Vault already authenticated

vault mount -path jks_store generic
vault write jks_store/server_trust value=$(base64 server_trust.jks)
vault write jks_store/server value=$(base64 server.jks)
vault write secret/server_trust_pass value=s3cr3t
vault write secret/server_pass value=s3cr3t
vault write jks_store/client_trust value=$(base64 client_trust.jks)
vault write jks_store/client value=$(base64 client.jks)
vault write secret/client_trust_pass value=s3cr3t
vault write secret/client_pass value=s3cr3t
