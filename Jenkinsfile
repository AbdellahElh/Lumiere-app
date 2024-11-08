pipeline {
    agent any

    environment {
        ANDROID_HOME = "/opt/android-sdk"  // Adjust to your actual SDK path
        GRADLE_HOME = "/opt/gradle-8.10.2"        // Adjust to your actual Gradle path
        PATH = "${ANDROID_HOME}/platform-tools:${GRADLE_HOME}/bin:${env.PATH}"  // Add tools to PATH
        GRADLE_OPTS="-Xmx4g -Dorg.gradle.jvmargs=-Xmx4g"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                 git branch: 'main', credentialsId: 'keycontainer', url: 'git@github.com:HOGENT-RISE/android-2425-gent5.git'
            }
        }
        
        stage('Build') {
            steps {
                // Ensure gradlew has execute permissions
                sh 'chmod +x ./gradlew'
                sh './gradlew wrapper --gradle-version 8.10.2'
                sh './gradlew --version'
                // Use Gradle to clean and build the APK
                sh './gradlew clean assembleRelease'
            }
        }
        
        stage('Sign APK') {
            steps {              
            // Align the APK
            sh '''
            $ANDROID_HOME/build-tools/35.0.0/zipalign -v 4 \
            app/build/outputs/apk/release/app-release-unsigned.apk \
            app/build/outputs/apk/release/app-release-aligned.apk
            '''

            // Sign the APK using apksigner
            sh '''
            $ANDROID_HOME/build-tools/35.0.0/apksigner sign \
            --ks /var/jenkins_home/keystore.jks \
            --ks-key-alias RiseAndroid \
            --ks-pass pass:Devops \
            --key-pass pass:Devops \
            --out app/build/outputs/apk/release/app-release-signed.apk \
            app/build/outputs/apk/release/app-release-aligned.apk
            '''
        }
}
        
        stage('Archive APK') {
            steps {
                // Archive the APK in Jenkins
                archiveArtifacts artifacts: 'app/build/outputs/apk/release/app-release-signed.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo "APK build complete and available for download!"
        }
        failure {
            echo "Build failed. Check the console output for errors."
        }
    }
}

