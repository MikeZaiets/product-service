#README
1. Code is covered by unit and integration tests using Spring
2. Code has error handling for REST
3. API responses are in JSON format
4. Used PostgesSQL for working environment
5. Latest version of Spring Boot. 
6. Java version 11
7. The project uses a Swagger to manage the API of the Service.
8. Execute scripts before running the application:
      - **src/main/resources/db/init-db.sql  -** to configure the database,
      - **src/main/resources/db/fill-db.sql  -** to populate the database,
   - OR - **src/test/resources/db/fill-db.sql  -** to populate the database with test data;

9. Use page http://localhost:8080/swagger-ui/#/product-controller   
   to view **_product-service_** API 
   
10. Before run **Tests**, docker must be installed and running (tests works with testcontainers)
11. Before run Application or Tests set parameter _"page.size"_ in properties respectively
      - **db.query.page-size: 10** 


