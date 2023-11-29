## Listing Service API

a REST API to facilitate interaction between the frontend application and a storage layer for accommodation listings.

### Libraries Used

- Koin - dependency injection
- Valiktor - schema based object validation
- Exposed - Database interactions
- Ktor - lightweight backend framework

## Running the REST API in Docker

This guide provides step-by-step instructions on how to run the REST API in a Docker container.

### Prerequisites

Before you begin, ensure that you have Docker installed on your machine. You can download Docker from [here](https://www.docker.com/get-started).

### Steps

1. **Clone the Repository:**

    ```bash
    git clone git@github.com:gissilali/listing-service.git
    cd listing-service
    ```

2. **Start the docker services**

    ```bash
    docker-compose up --build
    ```

## Assumptions

- Authentication is already implemented, to simulate it, I require a ``x-user-id`` header for every request
- Rooms and bookings modules are implemented, I do not record bookings or account for type of room being booked, I only account for number of rooms booked.

## Possible improvements

Considering the limited time I had to implement multiple features, I believe the following enhancements are necessary:
- Incorporating Integration Testing
- Implementing Unit Tests
- Introducing Pagination for accommodation listings
- Implementing Caching for frequently requested endpoints, such as  ``/accommodations?rating={ratingValue}``

