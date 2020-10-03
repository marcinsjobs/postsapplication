# postsaplication version 1.0
#### Application periodically downloads data from the public API:
#### https://jsonplaceholder.typicode.com/posts
#### and stores it in a local database where it can be interacted through application's API.
<br />

#### To build the application navigate to postsapplication/postsapplicatiion and type in a cmd:
```sh
$ mvnw clean install
```

#### To run:
```sh
$ mvnw spring-boot:run
```

### Or use the IntelliJ IDEA IDE

#### API description:
```sh
GET /api/posts
```
Returns all the posts read from the API and ApiError status. 
```sh
GET /db/posts?filterTitle=<"ascending"/"descending">&skipTitle=<true/false>
```
Returns posts read from the database as a list with the optional title filter and skipped userId.
```sh
GET /db/populate
```
Used to populate empty database with data from the external API. Returns ApiError status of the operation.
```sh
PUT /db/updatepost
```
Accepts PostData JSON and updates single post in the database according to the object's id. Returns ApiError status of the operation.
```sh
GET /db/updateposts
```
Updates all the database posts from the external API. Returns ApiError status of the operation.
```sh
DELETE /db/posts/{id}
```
Deletes record from the database according to the id. Returns ApiError status of the operation.

# Created by:
### Marcin Seyk (marcin.seyk.jobs@gmail.com)