# ztl-country-service


### Running the app locally 

1) Make sure to build the app:

   ```mvn clean install```

2) Build and tag the dockerfile:

   ```docker build -t ztl-country-service:0.0.1 .```

3) Run the image with port forwarding:

   ```docker run -p 8084:8080  ztl-country-service:0.0.1```

4) Executing a request:

   ```localhost:8084/api/countries/europe```

### Potential Deployment:

1) Build the jar and push it to a container repository(jenkins or similar software)
2) Define k8s deployment files using the docker image in the container repository
3) Let argo CD sync the k8s deployment descriptors to a k8s cluster