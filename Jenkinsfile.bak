pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn install -f "server/openstorefront/pom.xml"'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'server/openstorefront/openstorefront-web/target/openstorefront.war'
                }
            }
        }
    }
}