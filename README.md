# crypto-trading-bot
A simple crypto trading bot with 2 modes training and trading. Spring(Java) backend + Vite React frontend 

Setup Steps
To run the project locally:

1. Clone the repository.
2. Install required dependencies: Node.js for the frontend (React), Java JDK 17+ for the backend (Spring Boot), and PostgreSQL for the database.Initialize the database tables by running the databaseCreation.sql file in pgAdmin or with psql.
3. Configure the backend database connection in the application.properties file (set username, password, and database name).Start the backend by running the Spring Boot application.
4. Start the frontend by navigating to the React project folder, installing dependencies, and running npm run dev. The dev server will show a local URL (for example: http://localhost:5173).
5. Update CORS settings in WebConfig.java to match the frontend URL shown after starting the dev server
6. Open a browser at the frontend link (for example: http://localhost:5173) to use the application.

These steps ensure the system is correctly set up with the frontend, backend, and database working together.

