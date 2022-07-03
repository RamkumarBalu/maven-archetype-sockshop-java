
# Sock Shop

Below are the software and tools need to execute the project.

Setup a basic kubernetes cluster for testing and install an example application on it.

For Window - Minikube can be installed - https://github.com/kubernetes/minikube

Deploy the Socks Shop as a microservices based test application - https://microservices-demo.github.io/

In order to execute the application we need maven repository to download in built kuberantes api jar.




Before execute this java code docker and minikuber must be in start status and if we want see the deployments on dashboard please launch minikube dashboard.

And I attached the output for this test applcation inside the git repositry.
## Environment Variables

To run this project, you will need to add the following environment variables to your System.

```bash
Variable name : 'MAVEN_HOME'
Variable value: 'C:\Program Files\Maven\apache-maven-3.8.6'


Variable name : PATH
Variable value: 'C:\Program Files (x86)\CommonFiles\Oracle\Java\javapath;C:\tools\ruby31\bin;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0;C:\Windows\System32#\OpenSSH;C:\minikube;C:\Program Files\Docker\Docker\resources\bin;C:\ProgramData\DockerDesktop\versionbin;C:\ProgramData\chocolatey\bin;C:\terraform;%MAVEN_HOME%\bin;'
```



## Deployment

To deploy this project run

```bash
mvn clean
mvn install
mvn build
```


## Authors

- [@RamkumarBalu](https://www.github.com/RamkumarBalu)

