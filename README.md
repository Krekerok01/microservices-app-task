
# Microservices Blog Application
This is a blog application that is created using a microservice architecture.
# General Information

This application offers the following features:
* User registration and authentication
* Post publishing
* User-to-user subscription
* Self-post management
* Administrative capabilities for user management
* Access to up-to-date technology news and job vacancies

## Project architecture

![Architecture](microservices.png)


## Non-functional requirements
* JDK 17+;
* Docker-Compose / Docker
# Get started
* `docker compose up -d --build`

After launching all the containers, the application will be accessible via the following [link](http://localhost:3000/).


## Demo

After navigating to [http://localhost:3000](http://localhost:3000) you visit the **Home page** of our application.
![home_page](./photos/home_page_unauth.png)
From this step you can:
- check the vacancies (*follow the links on the left*)
- browse the latest tech news (*follow the links on the right*)
- check all users' posts
- sign up in the application (*click the "Sign up" button*)
- log in to the application (*click the "Log in" button*)
### Registration
After navigating to [http://localhost:3000/signup](http://localhost:3000/signup) or by clicking the *"Sign up"* button ([Home page](#Demo)) you visit the **Registration page** of our application.
![registration_page](./photos/registration_page.png)
From this step you can:
- browse to the home page (*click the "Home" button*)
- fill in the form and register in the application (*after typing your registration data, click the "Submit" button*)
### Logging in
After navigating to [http://localhost:3000/login](http://localhost:3000/signup) or by clicking the *"Log in"* button ([Home page](#Demo)) you visit the **Login page** of our application.
![login_page](./photos/login_page.png)
From this step you can:
- browse to the home page (*click the "Home" button*)
- fill in the form and log in to the application (*after typing your logging in data, click the "Submit" button*)
### User page
After navigating to [http://localhost:3000/myPage](http://localhost:3000/signup) and/or by passing the authorization you visit the **User page** of our application.
![user_page](./photos/user_page.png)
Here you can see your posts and the posts of all your followed authors.\
From this step you can:
- browse to the home page (*click the "Home" button*)
- browse to the account editing (*click the "Edit profile" button*)
- navigate to the users page (*click the "All users" button*)
- create a post (*click the "Create post" button*)
- delete your post (*click the "Delete post" button on the post*)
- update your post (*click the "Edit post" button on the post*)
### Creating a post
After navigating to [http://localhost:3000/addPost](http://localhost:3000/addPost) and by passing the authorization or by clicking the *"Create post"* button ([User page](#user-page)) you visit the **Post creating page** of our application.
![post_page](./photos/post_page.png)
From this step you can:
- browse to the home page (*click the "Home" button*)
- fill in the form and create a post (*after typing your post data, click the "Submit" button*)
### Users page
After navigating to [http://localhost:3000/users](http://localhost:3000/users) and by passing the authorization or by clicking the *"All users"* button ([User page](#user-page)) you visit the **All users' page** of our application.
![users_page](./photos/all_users_page.png)
You will see the list of all authors registered in the application.\
From this step you can:
- follow the author (*click the "Follow" button on the author*)
- unfollow the author (*click the "Unfollow" button on the author*)
### Profile editing
After navigating to [http://localhost:3000/myPage/edit](http://localhost:3000/myPage/edit) and by passing the authorization or by clicking the *"Edit profile"* button ([User page](#user-page)) you visit the **Profile editing page** of our application.
![user_edit_page](./photos/user_edit_page.png)
From this step you can:
- delete your profile (*click the "Delete profile" button*)
- fill in the form and update your profile (*after typing your profile data, click the "Submit" button*)
- change your profile password (*click the "Change password" button*)
### Password changing
After navigating to [http://localhost:3000/myPage/password](http://localhost:3000/myPage/password) and by passing the authorization or by clicking the *"Change password"* button ([User profile editing page](#profile-editing)) you visit the **Password changing page** of our application.
![password_edit_page](./photos/password_edit_page.png)
From this step you can:
- fill in the form and update your password (*after typing your old and new password, click the "Submit" button*)
### Updating a post
After clicking the *"Edit post"* button ([User page](#user-page)) you visit the **Post editing page** of our application.
![post_edit_page](./photos/post_edit_page.png)
From this step you can:
- browse to the home page (*click the "Home" button*)
- fill in the form and update your post (*after typing your post data, click the "Submit" button*)
### Additional
- you can navigate to your profile page by clicking on your username (**Profile picture**)
- to log out of your account, you need to click the **Log out** button (**Profile picture**)
- many pages require authorization

# API Usage

## Blog-service
#### Add a post

```http
  POST http://localhost:8080/posts
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `-`       | `-`      | Creates a new post provided by JSON-body in request|

##### JSON-body example:
```http
{
    "title":"Post about programming",
    "text":"Useful information"
}
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))\
**HTTP Response Code:** 201 (Created)

#### Get all posts by specification

```http
  GET http://localhost:8080/posts
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `long`   | **Optional**. Id of user to get associated posts  | 
| `title`      | `string`   | **Optional**. Title of posts to get  | 
| `creationDate`      | `date`   | **Optional**. Date of posts' creation  | 
| `modificationDate`      | `date`   | **Optional**. Date of posts' modification  | 

Returns a JSON-format of needed posts
##### Response JSON-body example:
```http
[
    {
        "postId": 2,
        "userId": 2,
        "username": "vladik",
        "title": "Second Post",
        "text": "Content of the second post goes here.",
        "creationDate": "14 Nov 2023, 12:30",
        "modificationDate": "14 Nov 2023, 12:30"
    },
    {
        "postId": 3,
        "userId": 3,
        "username": "admin",
        "title": "Interesting Post",
        "text": "Content of the interesting post goes here.",
        "creationDate": "14 Nov 2023, 12:30",
        "modificationDate": "14 Nov 2023, 12:30"
    }
]
```
###### **HTTP Response Code:** 200 (OK)
#### Get post by id

```http
  GET http://localhost:8080/posts/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id` | `long` | **Required**. Id of post to get  | 

Returns a JSON-format of post
 ##### Response JSON-body example:
```http
{
    "postId": 1,
    "userId": 1,
    "username": "default",
    "title": "Testt",
    "text": "This is the content of the test post",
    "creationDate": "15 Nov 2023, 08:00",
    "modificationDate": "06 Dec 2023, 15:05"
}
```
 ###### **HTTP Response Code:** 200 (OK)
 #### Update a post

```http
  PATCH http://localhost:8080/posts/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id` | `long` | **Required**. Id of post to update  |  

##### JSON-body example:
```http
{
    "title":"New title ",
    "text":" New interesting text."
}
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 200 (OK)
 #### Delete a post by id
```http
  DELETE http://localhost:8080/posts{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id` | `long` | **Required**. Id of post to delete  | 

**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 204 (No content)













## User-service
#### Get all users

```http
  GET http://localhost:8080/users
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email`       | `string`      | **Optional** Email of user to get |

##### Response JSON-body example:
```http
[
    {
        "id": 1,
        "username": "default",
        "email": "user@user.com",
        "role": "DEFAULT"
    },
    {
        "id": 2,
        "username": "vladik",
        "email": "vladik@specific-group.by",
        "role": "DEFAULT"
    },
    {
        "id": 3,
        "username": "admin",
        "email": "admin@admin.com",
        "role": "ADMIN"
    }
]
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))\
**HTTP Response Code:** 200 (OK)

#### Get one user

```http
  GET http://localhost:8080/users/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long`   | **Required**. Id of user to get  | 

Returns a JSON-format of one user by its id
##### Response JSON-body example:
```http
{
    "id": 1,
    "username": "default",
    "password": "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
    "email": "user@user.com",
    "role": "DEFAULT"
}
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 200 (OK)
#### Authenticate user

```http
  POST http://localhost:8080/users/auth
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `-` | `-` | Authenticates a user and returns his associated token  | 

##### JSON-body example:
```http
{
    "password": "password",
    "email": "user@user.com"
}
```
 ##### Response JSON-body example:
```http
{
    "userId": 1,
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc",
    "username": "default",
    "admin": false
}
```
 ###### **HTTP Response Code:** 200 (OK)
 #### Add a user

```http
  POST http://localhost:8080/users
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `-`       | `-`      | Adds an user provided by JSON-body in request| 

##### JSON-body example:
```http
{
    "username": "a",
    "password": "a",
    "email":"email@email.com",
    "role":"DEFAULT"
}
```
##### Response JSON-body example:
```http
{
    "id": 4,
    "username": "a",
    "email": "email@email.com",
    "role": "DEFAULT"
}
```
###### **HTTP Response Code:** 201 (Created)
 #### Delete a user by id
```http
  DELETE http://localhost:8080/users/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`       | `long`      | **Required** Id of user to delete   |


**NOTE**: User associated posts will also be deleted\
**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 204 (No content)
#### Update a user by id
```http
  PUT http://localhost:8080/users/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long`   | **Required.** Id of user to update|

##### JSON-body example:
```http
{
    "username": "aaaaaaaaaaaaaa",
    "email": "user@user.com",
    "role": "DEFAULT"
}
```
###### **HTTP Response Code:** 200 (OK)

#### Get user's username by id
```http
  GET http://localhost:8080/users/{id}/username
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long`   | **Required.** Id of user to get username|

##### Response JSON-body example:
```http
{
    "username": "aaaaaaaaaaaaaa"
}
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 200 (OK)


#### Update user's password
```http
  PUT http://localhost:8080/users/password/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long`   | **Required.** Id of user to change password|

##### JSON-body example:
```http
{
    "currentPassword": "admin",
    "newPassword": "adminnnnnn"
}
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 200 (OK)

#### Change user's privilege
```http
  PUT http://localhost:8080/users/privilege/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long`   | **Required.** Id of user to change privilege|

**Requires:** Authorization header with bearer token ([See](#authenticate-user))
###### **HTTP Response Code:** 200 (OK)




## Subscription-service
#### Create a subscription

```http
  POST http://localhost:8080/subscriptions/{userId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId`       | `long`      | **Required** Id of user to follow |

**Requires:** Authorization header with bearer token ([See](#authenticate-user))\
**HTTP Response Code:** 201 (Created)

#### Get subscriptions

```http
  GET http://localhost:8080/subscriptions
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userSubscriberId`       | `long`      | **Optional** Id of subscriber to get subscriptions |

Returns a JSON-format of needed subscriptions
##### Response JSON-body example:
```http
[
    {
        "subscriptionId": 1,
        "userSubscriberId": 1,
        "userPublisherId": 2
    },
    {
        "subscriptionId": 2,
        "userSubscriberId": 2,
        "userPublisherId": 3
    },
    {
        "subscriptionId": 3,
        "userSubscriberId": 1,
        "userPublisherId": 3
    },
    {
        "subscriptionId": 4,
        "userSubscriberId": 3,
        "userPublisherId": 1
    }
]
```
**Requires:** Authorization header with bearer token ([See](#authenticate-user))\
**HTTP Response Code:** 200 (OK)

#### Delete a subscription

```http
  DELETE http://localhost:8080/subscriptions/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id`       | `long`      | **Required** Id of subscription to delete |

**Requires:** Authorization header with bearer token ([See](#authenticate-user))\
**HTTP Response Code:** 204 (No content)
## Postman requests collection

You can also import collection of HTTP-requests to any service for Postman by downloading [Collections](https://github.com/Krekerok01/microservices-app-task/tree/master/endpoints) OR\
navigate to [Swagger](http://localhost:8080/swagger-ui.html) when application is up
## Authors
- [@Vladislav Savko](https://github.com/Vlad-Savko)
- [@Varvara Mamatsiuk](https://github.com/Krekerok01)