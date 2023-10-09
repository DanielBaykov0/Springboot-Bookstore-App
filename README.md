# Springboot-Bookstore-App

## Overview

The RESTful bookstore application is designed for seamless user interaction,
offering a user-centric interface for browsing and ordering products. Security is a priority,
employing JWT and refresh tokens to protect user data. Email notifications keep users
informed about orders and important updates. Testing services and controllers ensures application reliability and robustness.
Comprehensive logging captures a range of events for effective monitoring and troubleshooting.
Altogether, it delivers a secure, engaging, and efficient platform for book enthusiasts.

## Prerequisites

* Docker https://www.docker.com/ - Need to have docker running to run commands

### Docker Installation Tutorials:

* For Mac Users: https://www.youtube.com/watch?v=gcacQ29AjOo

* For Windows 11: https://www.youtube.com/watch?v=AAWNQ2wDVAg

* For Windows 10:https://www.youtube.com/watch?v=5nX8U8Fz5S0

* For Linux/Ubuntu: https://www.youtube.com/watch?v=TDLKQWsrSyk

## Photos

### Project photos

https://github.com/DanielBaykov0/Springboot-Bookstore-App/tree/master/photos

### Test coverage photo

## Setup backend environment

**0. Open cmd with "Run as administrator"**

**1. Clone the repository**

```bash
git clone https://github.com/DanielBaykov0/Springboot-Bookstore-App.git
```

**2.Navigate to project directory**

```bash
cd <path to project>
```

replace < path to project > with the path where you cloned the repository

**3. After navigating to the project directory, run the backend with docker script

In Command Prompt

```bash
docker-script.bat
```

Or in Intellij/ Or any other IDE/Editor

```bash
./docker-script.bat
```

REST API will start running at <http://localhost:8080/swagger-ui/index.html>

**4. You can stop the backend from running with**

```bash
docker compose down
```

## Endpoints

* http://localhost:8080/swagger-ui/index.html - Every endpoint is documented here.

# Key Features

Data for both products and users is already added to the system.

### ADMIN - There is a single admin.

        email: admin@gmail.com
        password: !Admin123

#### Admin privileges include:

* Viewing all user profiles with the ability to change their roles (adding or removing the "LIBRARIAN" role).
* Accessing the complete order history and the ability to do anything.
* Managing the bookstore by adding, editing, or deleting products.
* Viewing personal information and editing capabilities.

### LIBRARIAN - One librarian is manually assigned with MFA enabled.

        email: martin@gmail.com
        password: !Martin123

#### Worker privileges include:

* Access to view the bookstore.
* Access to the complete order history.
* Managing the bookstore by adding, editing, or deleting products.
* Viewing personal information with the ability to user profile.

### USER - Eight regular users are manually designated. Some(james) have MFA enabled, others don't.

        email: james@gmail.com
        password: !James123

#### User privileges include:

* Browsing the bookstore and adding products to their cart.
* Removing products from their cart.
* Placing orders.
* Viewing their order history and order details.
* Accessing their own cart.
* Viewing and editing their personal information, including names.
* Leaving comment and ratings for products.

### Fetch:

The system allows users to view the details of their orders,
providing them with a way to access and review the specifics of their placed orders.

### Error Handling:

The application employs a comprehensive approach to error handling.
Firstly, there is a global controller advice mechanism in place to handle situations where objects are not found,
ensuring that the user receives an appropriate response.
Additionally, specific exceptions are utilized to address issues related to incorrect data entered.
Furthermore, custom errors are thrown for every function of the application
to enhance the user experience when errors occur.

### Logging:

The bookstore application implements a robust and comprehensive logging mechanism to capture critical events
and activities within the system. This logging approach is instrumental in providing valuable insights into
the application's behavior and performance.


[//]: # (### Scheduled Jobs:)

[//]: # (Every day, the system offers promotional discounts on certain products.)

[//]: # (The specific discounts are determined based on the day of the week.)

[//]: # (This automated process adjusts product prices to provide users with lower prices on selected items,)

[//]: # (depending on which day they place their orders.)