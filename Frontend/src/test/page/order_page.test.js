import React from 'react';
import {render, waitFor, screen, fireEvent} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import {BrowserRouter as Router, Link} from 'react-router-dom';
import {message, Table, Tabs, Tag} from 'antd';
import userEvent from "@testing-library/user-event";
import OrderPage from "../../page/order_page";
import { fetchInitiatorTasks, fetchRecipientTasks, fetchInitiatorServices, fetchRecipientServices } from '../../service/order';
import moment from "moment/moment";
import connectWebSocket, {getChatList} from "../../service/chat";
import TabPane from "antd/lib/tabs/TabPane";

jest.mock('../../service/order');
// jest.mock('../../component/basic_layout', () => (props) => <div>{props.children}</div>);

jest.mock('antd', () => {
	const originalModule = jest.requireActual('antd');
	return {
		...originalModule,
		message: {
			error: jest.fn(),
		},
	};
});

describe('OrderPage', () => {
	beforeAll(() => {
		window.matchMedia = window.matchMedia || function () {
			return { matches: false, addListener: () => {}, removeListener: () => {} };
		};
	});

	beforeEach(() => {
		jest.clearAllMocks()

		fetchRecipientTasks.mockImplementation(() => Promise.resolve({
			id: 1,
			name: 'Test1',
			initiator: 'test',
			recipient: 'test',
			status: 0,
			time: '2024-08-01 12:00:00',
		}));
		fetchInitiatorTasks.mockImplementation(() => Promise.resolve({
			id: 1,
			name: 'Test2',
			initiator: 'test',
			recipient: 'test',
			status: 0,
			time: '2024-08-01 12:00:00',
		}));

		fetchInitiatorServices.mockImplementation(() => Promise.resolve({
			id: 1,
			name: 'Test3',
			initiator: 'test',
			recipient: 'test',
			status: 0,
			time: '2024-08-01 12:00:00',
		}));

		fetchRecipientServices.mockImplementation(() => Promise.resolve({
			id: 1,
			name: 'Test4',
			initiator: 'test',
			recipient: 'test',
			status: 0,
			time: '2024-08-01 12:00:00',
		}));
	})


	test('fetchInitiatorTasks test', async () => {
		render(<Router><OrderPage/></Router>);
		await waitFor(() => expect(fetchInitiatorTasks).toHaveBeenCalled());
	}, 30000);

	test('fetchRecipientTasks test', async () => {
		render(<Router><OrderPage/></Router>);
		// 设置activeTab为2
		fireEvent.click(screen.getByText('我接收的任务'));
		await waitFor(() => expect(fetchRecipientTasks).toHaveBeenCalled());
	}, 30000);

	test('fetchInitiatorServices test', async () => {
		render(<Router><OrderPage/></Router>);
		// 设置activeTab为3
		fireEvent.click(screen.getByText('我发布的服务'));
		await waitFor(() => expect(fetchInitiatorServices).toHaveBeenCalled());
	}, 30000);

	test('fetchRecipientServices test', async () => {
		render(<Router><OrderPage/></Router>);
		// 设置activeTab为4
		fireEvent.click(screen.getByText('我接收的服务'));
		await waitFor(() => expect(fetchRecipientServices).toHaveBeenCalled());
	}, 30000);

	test('error test', async () => {
		const error = new Error('error');
		fetchInitiatorTasks.mockRejectedValue(error);
		render(<Router><OrderPage/></Router>);
		await waitFor(() => expect(message.error).toHaveBeenCalledWith(error));
	}, 30000);

	// test('generateLink test: activeTab=1', async () => {
	// 	render(<Router><OrderPage/></Router>);
	// 	fireEvent.click(screen.getByText('我发布的任务'));
	//
	// 	const link = '/order/task/initiator/1';
	// 	expect(screen.getByText('Test1')).toHaveAttribute('href', link);
	//
	// }, 30000);


});
