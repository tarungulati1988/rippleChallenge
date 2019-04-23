### Implementation for: [Ripple challenge](https://gist.github.com/sappenin/4e649235cc33f83c743801696a702ae3)

#### Run the following commands to bring the servers up
- mvn spring-boot:run -Dserver=8080 -Dclient=9090
- mvn spring-boot:run -Dserver=9090 -Dclient=8080
- Each server/users uuid/account id is printed on startup. This would be used in framing the trustline request.
- POST :: http://localhost:{serverPort}/v1/trustline/send
```$xslt
{
	"amount": 20,
	"destination": "1a37ff4c-3f47-4c25-b889-f236f8013953",
	"source": "eca62837-d3ea-4a83-b41e-4bee5bc6e1ea"
}
```

Known improvements:
- validations for incoming users
- validations for requests to same user
- unit tests + component tests


