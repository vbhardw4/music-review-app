# Music Review App

Music Review App is currently having 3 microservices namely
1.  music-info-service
2.  music-review-service
3.  music-service

The application is based on the Spring Web Flux which uses the underlying Netty server for simulating asynchronous communication between the microservices.
Unlike the traditional Spring MVC architecture, which uses the Thread per Request Model to handle the client's request and the response, this uses the event loop system to handle the request and response.

This architecture can be scaled as per the load and does not require additional configuration to manage the thread pools unlike the MVC architecture where we need to manage the thread pool and clear the thread context and manage thread pool once the request is handled by the Tomcat server.

### Functionality handled by 3 micro-services -

1.  music-info-service - 
 
This micro-service contains the information or details related to the music. The object MusicInfo currently has below attributes -

1.  musicInfoId - String
2.  name - String
3.  year - Integer
4.  cast - List<String>
5.  releaseDate - LocalDate

NoSql Database MongoDB has been used to persist the movie related information.

2. music-review-service - 

  The reviews for movies are stored with the help of music-review-service. This module has the below object model.

1.  reviewId - String
2.  musicInfoId - Long
3.  comment - String
4.  rating - Double

3. music-service - 
  
  This is the service with which the clients interacts with. This service acts as client to the other micro-services (which acts as a backend). 
  For now, the only endpoint which is configured in the service is to retrieve all the music information along with it's comments and ratings and display it to the   user.
  
### Flow  -

  Both the music-review-service and music-info-service is connected to the mongo-db for persisting all the data.
  These two micro-services acts as a backend and serve the request made by the music-service.
  
### Automation - 

  The best practices when it comes to developing anything in the world of Software Development is writing automated test cases.
  Whole application is tested using the unit-tests and integration tests. 

  All the endpoints are covered in the unit testing and whole end-to-end testing is automated under the integration test module including but not limited to mongo-   db as well. The embedded mongo-db has been used for testing purposes.

  In addition to that, WireMock is leveraged becuase of it's great capabilities to mock the endpoints configured in the movies-service for creating the movies         information and movies reviews.
  
### Future Work - 

  This application will be serving as a backend for the code contained in the repository ece9065-vbhardw4-project/music-review-app/

  Also, this application will be deployed to the GCP using the GKE (Google Kubernetes Engine) so that it can automatically scale up and down as per the load.
