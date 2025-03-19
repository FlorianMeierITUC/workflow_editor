# Worflow editor / ITUCKI

##How to run
Environment variables are manages via the `.env` file found in the root folder of the project. Make a copy of `.env.sample` and fill in the secrets for mongodb.
There are two profiles for running the application. To select on of the modes, set ´SPRING_ACTIVE_PROFILE´ to either `dev` or `prod`.

### Dev
Uses a local mongodb for storing application data.

### Prod
Uses the live mongodb hosted on our server.
