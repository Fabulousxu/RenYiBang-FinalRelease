import {apiURL} from "./util";
import {get, post, put, del} from "./util";

export async function changeOrderStatus(id, status) {
	const url = `${apiURL}/order/status?orderId=${id}&status=${status}`;
	return post(url, {status});
}

export async function fetchInitiatorTasks() {
	const url = `${apiURL}/order/task/initiator`;
	return get(url);
}

export async function fetchRecipientTasks() {
	const url = `${apiURL}/order/task/recipient`;
	return get(url);
}

export async function fetchInitiatorServices() {
	const url = `${apiURL}/order/service/initiator`;
	return get(url);
}

export async function fetchRecipientServices() {
  const url = `${apiURL}/order/service/recipient`;
	return get(url);
}

export async function fetchOrderById(id) {
	const url = `${apiURL}/order/${id}`;
	return get(url);
}