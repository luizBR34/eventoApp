pipeline {
	agent any
	stages {
		stage("Cleaning Stage") {
			steps {
                sh '"/var/jenkins_home/apache-maven-3.6.3/bin/mvn" -Dmaven.test.failure.ignore clean'
			}
		}
		stage("Testing Stage") {
			steps {
			    sh '"/var/jenkins_home/apache-maven-3.6.3/bin/mvn" -Dmaven.test.failure.ignore test'
			}
		}
		stage("Packaging Stage") {
			steps {
			    sh '"/var/jenkins_home/apache-maven-3.6.3/bin/mvn" -Dmaven.test.failure.ignore package'
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
