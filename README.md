## Mutual SSL Solutions

### Demo Concepts

* One front end Application (client) with /hello endpoint.
* One Backend application (server) with /hello endpoint.
* Server has a TCP routes with mutual SSL enabled
* Client use restTemplate to communicate with server through mutual SSL
* Enable java ssl debug

### How to Run

* Have a tcp domain/routes enabled and point the DNS record to tcp load balancer

```
Shaozhen-Ding-MacBook-Pro:mutualssl sding$ cf domains
Getting domains in org test as admin...
name                              status   type
cfapps.haas-50.pez.pivotal.io     shared
mutulssl-server.shaozhenpcf.com   shared   tcp
```

* Generate Certificates

  * This script generates client and server keystores, trust-stores and certificates.

  * Import client cert to server trust store
  * Import server cert to client trust store
  * Place them to src/main/resources folder to client and server projects



```
generate_keystore.sh [SERVER_DOMAIN] [CLIENT_DOMAIN]

E.g ./generate_keystore.sh mutulssl-server.shaozhenpcf.com mutualssl-client.cfapps.haas-50.pez.pivotal.io
```

* Build the project

```
mvn package
```

* Configure the backend server on manifest.yml

```
---
applications:
- name: mutualssl-client
  memory: 512M
  path: client/target/client-0.0.1-SNAPSHOT.jar
  env:
    BACKEND_SERVER: https://mutulssl-server.shaozhenpcf.com:5000/hello

```

* Configure the client keystores

  Currently there are *two ways* to inject the client keystores to JVM.

  1. Through environment variables

     ```  
     applications:
     - name: mutualssl-client
       memory: 512M
       path: client/target/client-0.0.1-SNAPSHOT.jar
       env:
         BACKEND_SERVER: https://mutulssl-server.shaozhenpcf.com:5000/hello
         KEY_STORE: /home/vcap/app/BOOT-INF/classes/client.jks
         KEY_STORE_PASSWORD: s3cr3t
         TRUST_STORE: /home/vcap/app/BOOT-INF/classes/client_trust.jks
         TRUST_STORE_PASSWORD: s3cr3t
         BACK_END: ENV  
     ```
  2. Through VAULT

    *Implementation Details: Load keystore files with base64 encoding to vault. Use Java to grab the keystores, decode, create a temp jks file and load to JVM*

    ```
    Using Vault CLI to load key stores
    ./import_keystore_vault.sh
    ---
    applications:
    - name: mutualssl-client
      memory: 512M
      path: client/target/client-0.0.1-SNAPSHOT.jar
      env:
        BACKEND_SERVER: https://mutulssl-server.shaozhenpcf.com:5000/hello
        KEY_STORE: jks_store/client
        KEY_STORE_PASSWORD: secret/client_pass
        TRUST_STORE: jks_store/client_trust
        TRUST_STORE_PASSWORD: secret/client_trust_pass
        BACK_END: VAULT
        VAULT_SERVER: http://10.193.53.7:8200
        VAULT_TOKEN: 7c51e5f5-c909-2943-3657-a1c63305f11d
    ```

* cf push

```
Shaozhen-Ding-MacBook-Pro:mutualssl sding$ cf apps
Getting apps in org test / space staging as admin...
OK

name               requested state   instances   memory   disk   urls
mutualssl-client   started           1/1         512M     1G     mutualssl-client.cfapps.haas-50.pez.pivotal.io
mutualssl-server   started           1/1         512M     1G     mutulssl-server.shaozhenpcf.com:5000
```

### Verification

* Access client app through browser or curl

```
https://mutualssl-client.cfapps.haas-50.pez.pivotal.io/hello
```

* It returns the message from the backend server

* Watch the ssl handshake on both client and server side logs

* If access server directly through curl or browser. It will fail (Watch logs see the client cert request failure)
