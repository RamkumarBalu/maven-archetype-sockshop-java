/*
rk.balasubramanian
*/
package com.kubernetes;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Sock Shop API Calls
 *
 */
public class App {
	public static void main(String[] args) throws IOException, ApiException {
		System.out.println("Welcome to Sock Shop Application Tool!");

		// file path to your KubeConfig

		String kubeConfigPath = System.getenv("HOME") + "/.kube/config";

		// loading the out-of-cluster config, a kubeconfig from file-system
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

		// set the global default api-client to the in-cluster one from above
		Configuration.setDefaultApiClient(client);

		CoreV1Api api = new CoreV1Api();
		AppsV1Api appsV1Api = new AppsV1Api();
		appsV1Api.setApiClient(api.getApiClient());

		/* To show the Deployment details */

		V1DeploymentList listNamespacedDeployment = appsV1Api.listNamespacedDeployment("sock-shop", null, null, null,
				null, null, null, null, null, null, Boolean.FALSE);
		V1DeploymentList listNamespacedDeployment1 = appsV1Api.listNamespacedDeployment("sock-shop1", null, null, null,
				null, null, null, null, null, null, Boolean.FALSE);

		App.printDeployment(listNamespacedDeployment.getItems());
		App.printDeployment(listNamespacedDeployment1.getItems());

		/* Namespace Difference with Details */
		V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
		App.printNameSpaceStatus(list);
	}

	private static void printDeployment(List<V1Deployment> deploymentsItems) {

		// Printing Name Space
		System.out.println("\n\n####################################################"
				+ deploymentsItems.get(0).getMetadata().getNamespace()
				+ "#######################################################");
		// Deployment List
		System.out.println(
				"-----------------------------------------------------------------------------------------------------------------------");
		System.out.println("Service \t\t | Version \t\t | Date Deployment was Updated");
		System.out.println(
				"-----------------------------------------------------------------------------------------------------------------------");
		for (V1Deployment item : deploymentsItems) {

			System.out.println(item.getMetadata().getName() + "\t\t\t\t | "
					+ item.getSpec().getTemplate().getSpec().getContainers().get(0).getImage() + "\t\t\t\t | "
					+ item.getMetadata().getCreationTimestamp());

			System.out.println(
					"-----------------------------------------------------------------------------------------------------------------------");
		}
	}

	private static void printNameSpaceStatus(V1PodList list) {
		for (int i = 0; i < list.getItems().size(); i++) {
			if (list.getItems().get(i).getMetadata().getNamespace().equals("sock-shop")
					|| list.getItems().get(i).getMetadata().getNamespace().equals("sock-shop1")) {
				// Printing Name Space
				System.out.println("\n\n####################################################"
						+ list.getItems().get(i).getMetadata().getNamespace()
						+ "#######################################################");
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------------");
				System.out.println("Name Of the Deployment \t\t | Status of the Deployment");
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------------");
				for (int j = 0; j < list.getItems().size(); j++) {
					System.out.println(list.getItems().get(j).getMetadata().getName() + "\t\t\t\t\t | " + list.getItems().get(j).getStatus().getPhase());
					System.out.println(
							"-----------------------------------------------------------------------------------------------------------------------");
				}
			}

		}
	}
}
