#!groovy

pipeline {
    agent none

    options {
        buildDiscarder(logRotator(artifactDaysToKeepStr: '7', artifactNumToKeepStr: '10', daysToKeepStr: '7', numToKeepStr: '10'))
    }

    stages {
        stage('Run tests & Junit report') {
            agent {
                label 'master'
            }

            steps {
                sh './mvnw clean package'
                stash 'test-results'
            }
            post {
                always {
                    junit "**/surefire-reports/*.xml"
                }
            }
        }

        stage('Run Integration Tests') {
            agent {
                label 'master'
            }

            steps {
                sh './mvnw -Dmarketo.identity=${bamboo.marketo.identityUrl} -Dmarketo.rest=${bamboo.marketo.restUrl} -Dmarketo.clientId=${bamboo.marketo.clientId} -Dmarketo.clientSecret=${bamboo.marketo.clientPassword} -Pintegration-test verify'
                stash 'test-results'
            }
            post {
                always {
                    junit "**/target/failsafe-reports/*.xml"
                }
            }
        }

        stage('Sonar: scan') {
            agent {
                label 'master'
            }

            steps {
                // Discard older builds
                milestone ordinal: 1, label: 'Sonar'

                unstash 'test-results'

                withSonarQubeEnv('sonar') {
                    sh './mvnw sonar:sonar -Dsonar.projectVersion=${BUILD_NUMBER}'
                }
            }
        }

        stage("Sonar: quality gate") {
            steps {
                script {
                    try {
                        timeout(time: 5, unit: 'MINUTES') {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                error "Pipeline aborted due to quality gate failure"
                            }
                        }
                    }
                    catch (err) {
                        // Catch timeout exception but not Quality Gate.
                        String errorString = err.getMessage();

                        if (errorString == "Pipeline aborted due to quality gate failure") {
                            error errorString
                        }
                    }
                }
            }
        }

        stage('Build and publish jar') {
            agent {
                label 'master'
            }

            steps {
                configFileProvider(
                    [configFile(
                        fileId: 'global-maven-setings',
                        targetLocation: 'settings.xml',
                        variable: 'mavenSettingsPath',
                        replaceTokens: true
                    )]
                ) {
                    sh './mvnw deploy --settings ${mavenSettingsPath} -P publish -DskipTests'
                }
            }
        }
    }

    post {
        always {
            node('master') {
                deleteDir()
            }
        }
    }
}
