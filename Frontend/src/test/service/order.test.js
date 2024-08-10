import {changeOrderStatus, fetchInitiatorTasks, fetchRecipientServices, fetchRecipientTasks, fetchInitiatorServices, fetchOrderById} from "../../service/order";
import {apiURL, get, put, del, post} from "../../service/util";

jest.mock("../../service/util", () => ({
	apiURL: "http://mocked-api-url", get: jest.fn(), post: jest.fn(), put: jest.fn(), del: jest.fn()
})) // 模拟 util.js 中的 get, post, put, 和 del 函数

describe('order service', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	})

	test('changeOrderStatus test', async () => {
		const id = 1, status = 'status'
		await changeOrderStatus(id, status)
		expect(post).toHaveBeenCalledWith(`http://mocked-api-url/order/status?orderId=${id}&status=${status}`, {status})
	})

	test('fetchInitiatorTasks test', async () => {
		await fetchInitiatorTasks()
		expect(get).toHaveBeenCalledWith('http://mocked-api-url/order/task/initiator')
	})

	test('fetchRecipientTasks test', async () => {
		await fetchRecipientTasks()
		expect(get).toHaveBeenCalledWith('http://mocked-api-url/order/task/recipient')
	})

	test('fetchInitiatorServices test', async () => {
		await fetchInitiatorServices()
		expect(get).toHaveBeenCalledWith('http://mocked-api-url/order/service/initiator')
	})

	test('fetchRecipientServices test', async () => {
		await fetchRecipientServices()
		expect(get).toHaveBeenCalledWith('http://mocked-api-url/order/service/recipient')
	})

	test('fetchOrderById test', async () => {
		const id = 1
		await fetchOrderById(id)
		expect(get).toHaveBeenCalledWith(`http://mocked-api-url/order/${id}`)
	})

}, 30000) // 30s 超时时间