
## Documentation

[Documentation](https://github.com/RamkumarBalu/maven-archetype-sockshop-java/blob/main/README.md)


# Sock Shop Sample 

This project uses Kuberenetes API provides a table summary with following details:

● Name of deployments
● Images of deployment  
● Date deployment updated

Tool gives the difference between two namespaces showing when some services are missing or running different image versions.  

Before execute this java code docker and minikuber must be in start status and if we want see the deployments on dashboard please launch minikube dashboard.
## Installation

Install the following packages

```bash
  minikube
  eclipse
  docker
  git
  maven
```
    
## Environment Variables

To run this project, you will need to add the following environment variables to your System.

```bash
Variable name : 'MAVEN_HOME'
Variable value: 'C:\Program Files\Maven\apache-maven-3.8.6'


Variable name : PATH
Variable value: 'C:\Program Files (x86)\CommonFiles\Oracle\Java\javapath;C:\tools\ruby31\bin;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0;C:\Windows\System32#\OpenSSH;C:\minikube;C:\Program Files\Docker\Docker\resources\bin;C:\ProgramData\DockerDesktop\versionbin;C:\ProgramData\chocolatey\bin;C:\terraform;%MAVEN_HOME%\bin;'
```



## Pull Locally

Clone the project

```bash
  git clone https://github.com/RamkumarBalu/maven-archetype-sockshop-java
```

Go to the project directory

```bash
  cd maven-archetype-sockshop-java
```

## Deployment

To deploy this project run

```bash
mvn clean
mvn install
mvn build
```

## Run Locally

```bash
mvn exec:java -Dexec.mainClass="com.kubernetes"
```

## Sample Output

Files in the git repo

```bash
output1.out - displays the name of the service, version and deployment date.
output2.out - displays the name of the service and status including namespace differences.
```


## Author

- [@RamkumarBalu](https://www.github.com/RamkumarBalu)

