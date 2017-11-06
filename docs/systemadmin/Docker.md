## Using Docker ##

The Open Storefront application contains a Dockerfile in order to build Docker images.

Open Storefront also automatically builds release versions of the application for consumption by users.  These builds are available on [Docker Hub](http://hub.docker.com).


### Using Docker Hub ###

#### Command Line ####

To pull the latest version:

    docker pull flammablefork/openstorefront:latest
    
To pull a specific version:

    docker pull flammablefork/openstorefront:v2.2.1


#### Kitematic ####

When using a GUI application, such as Kitematic, simply search Docker Hub for 'openstorefront' (without quotes). Kitematic will return the matching results, and you can download the latest version (default). The container will start automatically, and you can use the controls in the application to start, stop and delete the container.

Kitematic will automatically assign a random port for you to access the web application in your browser. Click on the screen shot to launch a browser to the appropriate port. If the browser is not automatically launched, view the settings for the container and locate the random port (the container will always have the port 8080, so use the other port). Open a browser, and navigate to the appropriate page:

    http://localhost:[Random Port]

##### Upgrading #####

The Docker containers have the ability to upgrade to more recent versions.

For cross-platform compatibility, the database for the web application is stored, by default, in the container itself.  Downloading the latest image and creating a new container will not re-use any existing data in the application. You must upgrade the container to use your existing data with a newer version of the application.

In Kitematic, select the option to execute inside the running container. Once the terminal opens up, run the following command:

    ./upgrade.sh --to-version=[Desired Version]

For example, if I wanted to upgrade to version 2.2.1, execute:

    ./upgrade.sh --to-version=2.2.1

### Using Dockerfile ###

The Dockerfile contained in GitHub is configured for development purposes. It expects to find a WAR file built in the path of the downloaded source. This WAR file is not included when the source code is checked out. You, as a developer, must build the project first, in order to create the necessary WAR file prior to attempting to build the Docker image.  Once the WAR file is built, the build process for the Docker image will import the WAR file you have created.

#### Build ####

To build the Docker image:

    docker build -t [Tag] [Repository Name]/[Application Name] [Path To Dockerfile]

For example, if I had navigated to the folder containing the Dockerfile, and was wanting to build the 'development' version for the repository above, I would use the following example command:

    docker build -t development flammablefork/openstorefront .

#### Run ####

To create and run a Docker container:

    docker run [Options] [Repository Name]/[Application Name]

After building, I would need to then create and run a Docker container from the image:

    docker run -d -p 8080:8080 flammablefork/openstorefront:development
(This will create and run a Docker container using the flammablefork/openstorefront image. '-d' means the container would detach from the terminal, running it in the background. It would also expose port 8080 on my development workstation, which maps to the container's internal port of 8080)

#### Stop ####

In order to stop a container, you will need the container ID:

    docker ps

To stop a running container:

    docker stop [Container ID]

#### Start ####

**docker run always creates a new container - Do not use it to simply start a container**

In order to start a container, you will need the container ID:

    docker ps -a

To start an existing container:

    docker start [Container ID]

#### Upgrading ####

The Docker containers have the ability to upgrade to more recent versions.

For cross-platform compatibility, the database for the web application is stored, by default, in the container itself.  Downloading the latest image and creating a new container will not re-use any existing data in the application. You must upgrade the container to use your existing data with a newer version of the application.

To upgrade the container:

    docker exec -d [Container ID] upgrade.sh --to-version=[Desired Version]

To get the container ID:

    docker ps

For example, if I wanted to upgrade to version 2.2.1:

    docker exec -d [Container ID] upgrade.sh --to-version=2.2.1
