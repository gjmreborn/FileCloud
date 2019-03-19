# FileCloud
#### FileCloud - small web application written in Spring, HTML, CSS, JS and jQuery to store and manipulate on files on raspberry pi 3, which I don't use on daily basis.

FileCloud is application of SPA type. I used Bootstrap 4 to create beautiful layout.

Axios was used to do AJAX request to backend's API.

##### We have currently one implementation of FileService:
    1) Database implementation - DatabaseFileService qualifier

##### Generating bundle.js file after JS changes
    We use browserify to manage dependencies and minimize HTTP requests for depedencies.
    Build command in static directory is as follows:
        browserify src_js/app.js > dist_js/bundle.js

#### Available profiles
    database-version - files are stored in chosen relational database

##### Running FileCloud
    Clone this repository, then set in application.properties your database platform.
    Optional you can change server.port but it's not required.
    After this steps you can choose FileService implementation (default database implementation) by choosing one of the available profiles.
