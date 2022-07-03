/*
rk.balasubramanian
*/
package com.kubernetes;

import java.io.FileReader;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Watch;

public class watchApi {
	public static void main(String[] args) throws IOException, ApiException {

		String kubeConfigPath = System.getenv("HOME") + "/.kube/config";

		// loading the out-of-cluster config, a kubeconfig from file-system
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

		// set the global default api-client to the in-cluster one from above
		Configuration.setDefaultApiClient(client);

		CoreV1Api api = new CoreV1Api();

		Watch<V1Namespace> watch = Watch.createWatch(client,
				api.listNamespaceCall(null, Boolean.TRUE, null, null,null, 5, null, null, null,Boolean.TRUE, null),
				new TypeToken<Watch.Response<V1Namespace>>() {
				}.getType());

		for (Watch.Response<V1Namespace> item : watch) {
			System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
		}

	}

}
