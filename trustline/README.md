### Implementation for: [Ripple challenge](https://gist.github.com/sappenin/4e649235cc33f83c743801696a702ae3)

#### Run the following commands to bring the servers up
- mvn spring-boot:run -Dserver=8080 -Dclient=9090
- mvn spring-boot:run -Dserver=9090 -Dclient=8080
- Each server/users uuid/account id is printed on startup. This would be used in framing the trustline request.
- POST :: http://localhost:{serverPort}/v1/trustline/send
```$xslt
{
    "amount": 10,
    "destination": "{client_account_id}",
    "server": "{server_account_id}",
}
```

Known improvements:
- validations for incoming users
- validations for requests to same user
- unit tests + component tests


