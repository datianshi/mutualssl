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
kubo.shaozhenpcf.com              shared   tcp
mutulssl-server.
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
    CLIENT_KEY_STORE: /home/vcap/app/BOOT-INF/classes/client.jks
    CLIENT_TRUST_STORE: /home/vcap/app/BOOT-INF/classes/client_trust.jks

- name: mutualssl-server
  memory: 512M
  path: server/target/server-0.0.1-SNAPSHOT.jar
  routes:
    - route: mutulssl-server.shaozhenpcf.com:5000
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
