pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn install -f "server/openstorefront/pom.xml"'
                sh '''
                    if [ "$(~/docker ps -a | grep $BRANCH_NAME)" ]
                    then
                        ~/docker rm -f $BRANCH_NAME
                        sleep 5
                    fi

                    DOCKER_PORT=`shuf -i 9001-9020 -n 1`

                    # run with built in dataset
                    ~/docker create --name $BRANCH_NAME\
                    -p $DOCKER_PORT:8080 \
                    -e MONGO_URL='mongodb://mongo:27017' \
                    --mount type=bind,source=/home/spoon-data/,target=/var/openstorefront/ \
                    --net=spoon-net \
                    --env CATALINA_OPTS="-Xmx2048m" \
                    --memory 4g \
                    spoon:ES-7.2 \

                    ~/docker cp $WORKSPACE/server/openstorefront/openstorefront-web/target/openstorefront.war $BRANCH_NAME:/usr/local/tomcat/webapps

                    printf "###############################################\n"
                    printf "Docker Containers Created\n"
                    printf "\t$BRANCH_NAME on port $DOCKER_PORT\n"
                    printf "###############################################\n"
                '''
            }
            post {
                success {
                    archiveArtifacts artifacts: 'server/openstorefront/openstorefront-web/target/openstorefront.war'
                }
            }
        }
    }
}