pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'JDK17'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Integration Test') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'marketo-api', usernameVariable: 'MARKETO_CLIENT_ID', passwordVariable: 'MARKETO_CLIENT_SECRET')
                ]) {
                    sh '''
                        mvn -Pintegration-test clean verify \
                            -Dmarketo.identity=https://749-IOM-373.mktorest.com/identity \
                            -Dmarketo.rest=https://749-IOM-373.mktorest.com/rest \
                            -Dmarketo.clientId=${MARKETO_CLIENT_ID} \
                            -Dmarketo.clientSecret=${MARKETO_CLIENT_SECRET}
                    '''
                }
            }
        }

        stage('Publish') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'Artifactory file', usernameVariable: 'continuous_integration', passwordVariable: 'ci_pass')
                ]) {
                    withEnv(["artifactory_user=${continuous_integration}", "artifactory_password=${ci_pass}"]) {
                        sh '''
                            mvn deploy --settings .mvn/settings.xml \
                                -DskipTests
                        '''
                    }
                }
            }
        }
    }
}
