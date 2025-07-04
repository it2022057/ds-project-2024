# Distributed Systems 

* This code is the project for the Distributed Systems 
course at Harokopio University of Athens, Department of Informatics and Telematics.

* Team 33: **Καρακατσανίδη Αριάδνη - it2022029,
             Λουκιδέλης Βύρωνας - it2022057,
             Δημήτρης Σαρανταυγάς - it2021090**

## Clone project

```bash
git clone https://gitlab.hua.gr/it2022057/ds-project-2024.git
```
## Database
* you can create a free postgres database on [https://render.com/](https://render.com/)
* we created a free render database, which we connected to our app, with a starter sql file if you want to use
```bash
src/main/resources/sql/starter_data.sql
 ```

## Ensure you have our render database connection in application.properties

```properties
spring.datasource.username=dsuser
spring.datasource.password=88h6NBea9KnsXQKWdNTtm8roh7QpgMdw
spring.datasource.url=jdbc:postgresql://dpg-d14oovgdl3ps738g9j1g-a.oregon-postgres.render.com:5432/dsdb_iklh
```
## Run the project
* make sure you have set correctly ``JAVA_HOME`` and ``M2_HOME`` environmental variables
```bash
mvn spring-boot:run
```

## Test the project
```bash
./mvnw test
```
