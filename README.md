## alura-backend-challenge

Alura Backend Challenge, implemented using **SpringBoot MVC and JPA**.

- Testing: **Postman** to create HTTP requests.
- Checkstyle: **SonarQube** in a localhost.

The API version is currently in **v1**, meaning for REST operations use as the example: `localhost:8080/v1/receitas/?descricao={teste}`

For authentication, use `localhost:8080/authentication`, you should use a **JWT** Token with username and password on the request. With the valid information, you should receive all permissions needed for the REST API operations. 
