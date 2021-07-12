pipeline {
	agent any
	def mvnHome
	stages {
		stage("Cleaning Stage") {
		mvnHome = tool 'M3'
			steps {
               withEnv(["MVN_HOME=$mvnHome"]) {
                    sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean'
                }
			}
		}
		stage("Testing Stage") {
			steps {
				sh "mvn test"
			}
		}
		stage("Packaging Stage") {
			steps {
				sh "mvn package"
			}
		}
		stage("Consolidate Results") {
			steps {
				input("Do you want to capture results?")
				junit '**/target/surefire-reports/TEST-*.xml'
				archive 'target/*.jar'
			}
		}
		stage("Email Build Status"){
			steps {
				mail body: "${env.JOB_NAME}  - Build # ${env.BUILD_NUMBER}  - ${currentBuild.currentResult} \n\nCheck console output at ${env.BUILD_URL} to view the results.", subject: "${env.JOB_NAME}  - Build # ${env.BUILD_NUMBER}  - ${currentBuild.currentResult}!!", to: 'luizfernandomail@gmail.com'
			}
		}
	}
}
