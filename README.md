# Distributed Systems 

* This code is the project for the Distributed Systems 
course at Harokopio University of Athens, Dept. of Informatics and Telematics.

* Team 33: **Καρακατσανιδη Αριάδνη - it2022029, 
             Λουκιδέλης Βύρωνας - it2022057, 
             Δημήτρης Σαρανταυγάς - it2021090**

## Clone project

```bash
git clone https://gitlab.hua.gr/it2022057/ds-project-2024.git
```
## Database
* you can create a free postgres database on [https://render.com/](https://render.com/)
* we created a free render database, which we connected to our app, but you can 
also use starter sql files
* you can run a postgres with docker
  ```bash
  docker run --name ds-lab-pg --rm \
    -e POSTGRES_PASSWORD=pass123 \
    -e POSTGRES_USER=dbuser \
    -e POSTGRES_DB=students \
    -d --net=host \
    -v pgdata:/var/lib/postgresql/data \
    postgres:16
  ```

## Fix database connection in application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/students
spring.datasource.username=dbuser
spring.datasource.password=pass123
```
## Run the project
* make sure you have set correctly ``JAVA_HOME`` and ``M2_HOME`` environmental variables
```bash
mvn spring-boot:run
```