import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import OrderRecipientPage from '../../page/order_recipient_page';
import { fetchOrderById, changeOrderStatus } from '../../service/order';
import '@testing-library/jest-dom';

jest.mock('../../service/order');
jest.mock('antd', () => {
	const originalModule = jest.requireActual('antd');
	return {
		...originalModule,
		message: {
			error: jest.fn(),
		},
	};
});

describe('OrderRecipientPage', () => {
	beforeAll(() => {
		window.matchMedia = window.matchMedia || function () {
			return {
				matches: false, addListener: () => {
				}, removeListener: () => {
				}
			}
		}
	});

	beforeEach(() => {
		jest.clearAllMocks();
	});

	test("fetch order by id", async () => {
		const order = {
			name: 'Test',
			initiator_name: 'apple',
			recipient_name: 'banana',
			initiator_img: 'apple.jpg',
			recipient_img: 'banana.jpg',
			status: 'UNPAID',
			time: '2024-08-01 12:00:00',
			order_img: ['order.jpg'],
			description: 'This is a test order.',
		};

		fetchOrderById.mockResolvedValue(order);
		render(<Router><OrderRecipientPage/></Router>);
		expect(fetchOrderById).toHaveBeenCalled();
		expect(await screen.findByText('Test')).toBeInTheDocument();
		// expect(await screen.findByText('apple')).toBeInTheDocument();
		// expect(await screen.findByText('banana')).toBeInTheDocument();
		expect(await screen.findByText('未付款')).toBeInTheDocument();

		expect(await screen.findByText('This is a test order.')).toBeInTheDocument();
		// expect(await screen.findByText('2024-08-01 12:00:00')).toBeInTheDocument();
	}, 30000);

	test("fetch error", async () => {
		fetchOrderById.mockRejectedValue(new Error('fetch error'));
		render(<Router><OrderRecipientPage/></Router>);
		expect(fetchOrderById).toHaveBeenCalled();
	}, 30000);

	test("完成按钮", async () => {
		const order = {
			name: 'Test',
			initiator_name: 'apple',
			recipient_name: 'banana',
			initiator_img: 'apple.jpg',
			recipient_img: 'banana.jpg',
			status: 1,
			time: '2024-08-01 12:00:00',
			order_img: ['order.jpg'],
			description: 'This is a test order.',
		};

		fetchOrderById.mockResolvedValue(order);
		changeOrderStatus.mockResolvedValue();
		render(<Router><OrderRecipientPage/></Router>);
		expect(fetchOrderById).toHaveBeenCalled();
		expect(await screen.findByText('完成订单')).toBeInTheDocument();
		fireEvent.click(screen.getByText('完成订单'));
		expect(changeOrderStatus).toHaveBeenCalled();
	}, 30000);

	test("完成按钮失败", async () => {
		const order = {
			name: 'Test',
			initiator_name: 'apple',
			recipient_name: 'banana',
			initiator_img: 'apple.jpg',
			recipient_img: 'banana.jpg',
			status: 1,
			time: '2024-08-01 12:00:00',
			order_img: ['order.jpg'],
			description: 'This is a test order.',
		};

		fetchOrderById.mockResolvedValue(order);
		changeOrderStatus.mockRejectedValue(new Error('change status error'));
		render(<Router><OrderRecipientPage/></Router>);
		expect(fetchOrderById).toHaveBeenCalled();
		expect(await screen.findByText('完成订单')).toBeInTheDocument();
		fireEvent.click(screen.getByText('完成订单'));
		expect(changeOrderStatus).toHaveBeenCalled();
	}, 30000);


});