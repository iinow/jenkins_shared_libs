def call() {
    properties([parameters([string(defaultValue: 'test', description: 'test or verify', name: 'testType', trim: false)])])

    node {
        stage("git") {
            git url: 'https://github.com/iinow/java-maven-junit-helloworld.git', branch: "master"
        }
        
        stage('build') {
            // ,
            //        "PATH+JDK=${tool 'openjdk10'}/bin"
            // mvn: mvn-3.3.9, mvn-3.6.0, mvn-3.6.2
            // jdk: openjdk8, openjdk9, openjdk10
            withEnv(["PATH+MAVEN=${tool 'mvn-3.3.9'}/bin", 
                    "JAVA_HOME=${tool 'openjdk9'}"]) {
                sh 'ls -al $JAVA_HOME'
                sh 'javac -version'
                sh 'mvn --version'
                sh "mvn clean ${params.testType}"
            }
        }
        
        stage('report') {
            junit 'target/surefire-reports/*.xml'
            jacoco execPattern: 'target/**.exec'
        }
    }
}