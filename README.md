# CERN-web-app

# Task Management System

This is a simple task management system developed as part of a job interview at CERN. It consists of two main components: a Spring Boot backend for managing task categories and tasks, and an Angular frontend for user interaction.

## Installation

### Spring Boot Backend

1. Clone the repository to your local machine.
2. Navigate to the `backend` directory.
3. Run the following command to start the Spring Boot application:

```bash
./gradlew bootRun
```

### Angular Frontend

1. Navigate to the `frontend` directory.
2. Run the following command to install dependencies:

```bash
npm install
```

3. Run the following command to start the Angular application:

```bash
ng serve
```

## Usage

1. Access the Angular frontend by navigating to `http://localhost:4200` in your web browser.
2. Use the provided UI to manage task categories and tasks.

## APIs

### Task Categories API

- Endpoint: `/categories`
- Methods:
  - `POST`: Create a new task category
  - `GET`: Retrieve all task categories
  - `GET /{id}`: Retrieve a specific task category by ID
  - `PUT /{id}`: Update a task category
  - `DELETE /{id}`: Delete a task category

### Tasks API

- Endpoint: `/tasks`
- Methods:
  - `POST`: Create a new task
  - `GET`: Retrieve all tasks
  - `GET /{id}`: Retrieve a specific task by ID
  - `PUT /{id}`: Update a task
  - `DELETE /{id}`: Delete a task

## Notes

- It is required to create a task category before creating a task.
- Ensure the Spring Boot application is running before accessing the Angular frontend.
