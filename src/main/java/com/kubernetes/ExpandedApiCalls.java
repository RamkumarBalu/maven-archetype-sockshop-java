/*
rk.balasubramanian
*/
package com.kubernetes;

import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.util.Config;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple example of how to use the Java API
 *
 * Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="com.kubernetes.ExpandedApiCalls"
 *
 */
public class ExpandedApiCalls {

	private static CoreV1Api COREV1_API;
	private static final String DEFAULT_NAME_SPACE = "default";
	private static final Integer TIME_OUT_VALUE = 180;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpandedApiCalls.class);
	private static final String PADDING = "                              ";

	/**
	 * Main method
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ApiClient client = Config.defaultClient();
			// To change the context of k8s cluster, you can use
			// io.kubernetes.client.util.KubeConfig
			Configuration.setDefaultApiClient(client);
			COREV1_API = new CoreV1Api(client);

			// List all of the namaspaces and pods
			List<String> nameSpaces = getAllNameSpaces();
			nameSpaces.stream().forEach(namespace -> {
				try {
					System.out.println("----- " + namespace + " -----");
					getNamespacedPod(namespace).stream().forEach(System.out::println);
				} catch (ApiException ex) {
					LOGGER.warn("Couldn't get the pods in namespace:" + namespace, ex);
				}
			});

			// Print all of the Services
			System.out.println("----- Print list all Services Start -----");
			List<String> services = getServices();
			services.stream().forEach(System.out::println);
			System.out.println("----- Print list all Services End -----");

			// Print log of specific pod. In this example show the first pod logs.
			System.out.println("----- Print Log of Specific Pod Start -----");
			String firstPodName = getPods().get(0);
			printLog(DEFAULT_NAME_SPACE, firstPodName);
			System.out.println("----- Print Log of Specific Pod End -----");

		} catch (ApiException | IOException ex) {
			LOGGER.warn("Exception had occured ", ex);
		}
	}

	/**
	 * Get all namespaces in k8s cluster
	 *
	 * @return
	 * @throws ApiException
	 */
	public static List<String> getAllNameSpaces() throws ApiException {
		V1NamespaceList listNamespace = COREV1_API.listNamespace("true", null, null, null, null, 0, null, null,
				Integer.MAX_VALUE, Boolean.FALSE);

		List<String> list = listNamespace.getItems().stream().map(v1Namespace -> v1Namespace.getMetadata().getName())
				.collect(Collectors.toList());

		Iterator i = list.iterator();
		String str = "";
		while (i.hasNext()) {
			str = (String) i.next();
			if (str.equals("sock-shop") || str.equals("sock-shop1")) {
			} else {
				i.remove();
			}
		}
		return list;
	}

	/**
	 * List all pod names in all namespaces in k8s cluster
	 *
	 * @return
	 * @throws ApiException
	 */
	public static List<String> getPods() throws ApiException {
		V1PodList v1podList = COREV1_API.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null,
				null);
		List<String> podList = v1podList.getItems().stream().map(v1Pod -> v1Pod.getMetadata().getName())
				.collect(Collectors.toList());
		return podList;
	}

	/**
	 * List all pod in the default namespace
	 *
	 * @return
	 * @throws ApiException
	 */
	public static List<String> getNamespacedPod() throws ApiException {
		return getNamespacedPod(DEFAULT_NAME_SPACE, null);
	}

	/**
	 * List pod in specific namespace
	 *
	 * @param namespace
	 * @return
	 * @throws ApiException
	 */
	public static List<String> getNamespacedPod(String namespace) throws ApiException {
		return getNamespacedPod(namespace, null);
	}

	/**
	 * List pod in specific namespace with label
	 *
	 * @param namespace
	 * @param label
	 * @return
	 * @throws ApiException
	 */
	public static List<String> getNamespacedPod(String namespace, String label) throws ApiException {
		V1PodList listNamespacedPod = COREV1_API.listNamespacedPod(namespace, null, null, null, null, label,
				Integer.MAX_VALUE, null, null, TIME_OUT_VALUE, Boolean.FALSE);
		List<String> listPods = listNamespacedPod.getItems().stream().map(v1pod -> v1pod.getMetadata().getName())
				.collect(Collectors.toList());
		return listPods;
	}

	/**
	 * List all Services in default namespace
	 *
	 * @return
	 * @throws ApiException
	 */
	public static List<String> getServices() throws ApiException {
		V1ServiceList listNamespacedService = COREV1_API.listNamespacedService(DEFAULT_NAME_SPACE, null, null, null,
				null, null, Integer.MAX_VALUE, null, null, TIME_OUT_VALUE, Boolean.FALSE);
		return listNamespacedService.getItems().stream().map(v1service -> v1service.getMetadata().getName())
				.collect(Collectors.toList());
	}

	/**
	 * Scale up/down the number of pod in Deployment
	 *
	 * @param deploymentName
	 * @param numberOfReplicas
	 * @throws ApiException
	 */
	public static void scaleDeployment(String deploymentName, int numberOfReplicas) throws ApiException {
		AppsV1Api appsV1Api = new AppsV1Api();
		appsV1Api.setApiClient(COREV1_API.getApiClient());
		V1DeploymentList listNamespacedDeployment = appsV1Api.listNamespacedDeployment(null, null, null, null, null,
				null, null, null, null, null, Boolean.FALSE);

		List<V1Deployment> appsV1DeploymentItems = listNamespacedDeployment.getItems();
		Optional<V1Deployment> findedDeployment = appsV1DeploymentItems.stream()
				.filter((V1Deployment deployment) -> deployment.getMetadata().getName().equals(deploymentName))
				.findFirst();
		findedDeployment.ifPresent((V1Deployment deploy) -> {
			try {
				V1DeploymentSpec newSpec = deploy.getSpec().replicas(numberOfReplicas);
				V1Deployment newDeploy = deploy.spec(newSpec);
				appsV1Api.replaceNamespacedDeployment(deploymentName, DEFAULT_NAME_SPACE, newDeploy, null, null, null);
			} catch (ApiException ex) {
				LOGGER.warn("Scale the pod failed for Deployment:" + deploymentName, ex);
			}
		});
	}

	/**
	 * Print out the Log for specific Pods
	 *
	 * @param namespace
	 * @param podName
	 * @throws ApiException
	 */
	public static void printLog(String namespace, String podName) throws ApiException {
		String readNamespacedPodLog = COREV1_API.readNamespacedPodLog(podName, namespace, null, Boolean.FALSE, null,
				Integer.MAX_VALUE, null, Boolean.FALSE, Integer.MAX_VALUE, 40, Boolean.FALSE);
		System.out.println(readNamespacedPodLog);
	}

	/*
	 * public static void nodPodCPU(ApiClient client) {
	 * 
	 * String ns = cli.getOptionValue("n", "default"); List<Pair<V1Node,
	 * NodeMetrics>> nodes = top(V1Node.class,
	 * NodeMetrics.class).apiClient(client).metric("cpu") .execute();
	 * System.out.println(pad("Node") + "\tCPU\t\tMemory"); for (Pair<V1Node,
	 * NodeMetrics> node : nodes) {
	 * System.out.println(pad(node.getLeft().getMetadata().getName()) + "\t" +
	 * node.getRight().getUsage().get("cpu").getNumber() + "\t" +
	 * node.getRight().getUsage().get("memory").getNumber()); } List<Pair<V1Pod,
	 * PodMetrics>> pods = top(V1Pod.class,
	 * PodMetrics.class).apiClient(client).namespace(ns) .metric("cpu").execute();
	 * System.out.println(pad("Pod") + "\tCPU\t\tMemory"); for (Pair<V1Pod,
	 * PodMetrics> pod : pods) {
	 * System.out.println(pad(pod.getLeft().getMetadata().getName()) + "\t" +
	 * podMetricSum(pod.getRight(), "cpu") + "\t" + podMetricSum(pod.getRight(),
	 * "memory")); } }
	 */

	private static String pad(String value) {
		while (value.length() < PADDING.length()) {
			value += " ";
		}
		return value;
	}

	public static double podMetricSum(PodMetrics podMetrics, String metricName) {
		double sum = 0;
		for (ContainerMetrics containerMetrics : podMetrics.getContainers()) {
			Quantity value = containerMetrics.getUsage().get(metricName);
			if (value != null) {
				sum += value.getNumber().doubleValue();
			}
		}
		return sum;
	}

}
