# tmdb
The Movie DB Android Kotlin:

Functionality : 
Home page:  shows the most popular movies in decreasing order (works offline with the data it already cached).
Movie page:  displays movie details when you click on a movie view (works offline on available movies).
Search page: Allows to search for  movies by title (requires internet connectivity)

Under the Hood:

The app uses MVVM architecture for all the app pages, with Dagger 2 framework for dependencies injection. The app relies on Retrofit for fetching data from online rest service, Room for storing the data (caching), Fresco for illustration of images, and RXAndroid for better reactive programming.

More details about each page:
Home page : 
The View asks the ViewModel to load the data and listen to database changes, while the ViewModel on its turn asks the Model to load the data asynchronously which saves them in the DB after fetching from the server, listening to the DB changes the View can update it self on it’s own.
Offline functionality is done with the help of this architecture, since the View is getting the DB data and listening to them, being offline will only mean that you won’t be getting new pages (obviously).
Refreshing the pages clears the DB and starts over, if offline nothing happens.


Movie page : Using the same strategy as the Home page, this page works perfectly offline as it fetches data from the local database. It might not be able to show all the movie details if it was never fetched previously from the web API. 


This page’s behaviour depends on the source that it’s coming from, to either update the movie in the db permanently with more details (if it’s coming from home page or if the movie was already in the db) or saves the movie in the DB  temporarily (for logic consistency) then deletes it after being left so it won’t affect home page data.

Search page: This page can search for movies by name as you type it into the search bar with some wait period to avoid overwhelming the backend API. Since the end user might type something and retype it again after a while, I decided to have the responses cached in memory with the query being the key for each response.  As the user re-enters the same phrase again, we can fetch it directly from in-memory storage, avoiding creating extra load on the API, and vastly improving the end-user experience, as well as conserving bandwidth and battery. Due to the nature of the feature, this page only works in online mode.

Dependencies : 
Room : For caching data in local database
Dagger 2 : For dependency injection
Fresco : For image loading and better memory management
Retrofit 2 : For calling web services and online APIs.
RXJava 2 : For multithreading and reactive programming.


Prerequisites to run the app:
Having Android Studio 3.2 ready is the only thing needed to get the project to run.
Any simulator recently updated will run the app, minimum sdk version is set to 16
