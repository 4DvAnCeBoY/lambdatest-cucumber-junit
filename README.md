## Run Selenium Tests With Cucumber JUnit On LambdaTest


### Environment Setup

1. Global Dependencies
    * Install [Maven](https://maven.apache.org/install.html)
    * Or Install Maven with [Homebrew](http://brew.sh/) (Easier)
    ```
    $ brew install maven
    ```
2. LambdaTest Labs Credentials
    * In the terminal, export your LambdaTest Labs credentials as environmental variables:
    ```
    $ export LT_USERNAME=<your LambdaTest username>
    $ export LT_ACCESS_KEY=<your LambdaTest accessKey>
    ```
3. Project Dependencies
    * Check that packages are available
    ```
    $ cd lambdatest-cucumber-junit
    $ mvn test-compile
    ```
    * You may also want to run the command below to check for outdated dependencies. Please be sure to verify and review updates before editing your pom.xml file as they may not be compatible with your code.
    ```
    $ mvn versions:display-dependency-updates
    ```
    
### Running the tests
Using Maven with the `sure-fire` plugin allows tests to be run in parallel by feature file. Each scenario in each feature file will be executed on a LambdaTest session. 
```
$ mvn clean install test
```

[LambdaTest Automation Dashboard](https://automation.lambdatest.com/build)


