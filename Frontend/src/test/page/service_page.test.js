import React from 'react';
import {render, waitFor, screen, fireEvent} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import ServicePage from '../../page/service_page';
import {collectService, searchService, uncollectService} from '../../service/service';
import { BrowserRouter as Router } from 'react-router-dom';
import { message } from 'antd';
import userEvent from "@testing-library/user-event";
import ItemList from "../../component/item_list";

jest.mock('../../service/service', () => ({
    searchService: jest.fn(),
    uncollectService: jest.fn(),
    collectService: jest.fn(),
}));
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

describe('ServicePage', () => {
    beforeAll(() => {
        searchService.mockResolvedValue(
            {
                total: 100,
                items: [{
                    serviceId: '1',
                    collected: true,
                    title: 'Test Service',
                    description: 'This is a test service.',
                    owner: {
                        userId: '1',
                        nickname: 'test',
                        rating: 100,
                        avatar: 'test.jpg',
                    }
                }],
            }
        );
        window.matchMedia = window.matchMedia || function () {
            return { matches: false, addListener: () => {}, removeListener: () => {} };
        };
    });

    test('renders ServicePage and fetches services', async () => {
        searchService.mockResolvedValueOnce({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
            }],
        });

        render(
            <Router>
                <ServicePage />
            </Router>
        );

        await waitFor(() => {
            expect(searchService).toHaveBeenCalledWith('', 24, 0, 'time', ['', ''], [0, -1]);
        });

        // 验证 ServicePage 是否渲染了服务
        const serviceElement = screen.getByRole('heading', { name: '服务 0条' });
        expect(serviceElement).toBeInTheDocument();
    });

    test('handles search service error', async () => {
        searchService.mockRejectedValueOnce('Search failed');

        render(
            <Router>
                <ServicePage />
            </Router>
        );

        await waitFor(() => {
            expect(message.error).toHaveBeenCalledWith('Search failed');
        });
    });

    test('ItemList onSearch function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        // Ensure that initial searchService call is made
        await waitFor(() => {
            expect(searchService).toHaveBeenCalledWith('', 24, 0, 'time', ['', ''], [0, -1]);
        });

        // Find and interact with search input and button
        const searchInput = screen.getByPlaceholderText('请输入服务关键词或用户关键词来搜索相关服务');
        const searchButton = screen.getByRole('button', { name: '搜 索' }); // Use the exact name as rendered

        await userEvent.type(searchInput, 'test');
        await userEvent.click(searchButton);

        // Wait for the searchService to be called with correct arguments
        await waitFor(() => {
            expect(searchService).toHaveBeenCalledWith('test', 24, 0, 'time', ['', ''], [0, -1]);
        });
    });


    test('ItemList onSearch function error', async () => {
        // Mock the searchService to return a rejected value
        searchService.mockRejectedValue('Search failed');

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        // Find and interact with search input and button
        const searchInput = screen.getByPlaceholderText('请输入服务关键词或用户关键词来搜索相关服务');
        const searchButton = screen.getByRole('button', { name: '搜 索' }); // Use the exact name as rendered

        await userEvent.type(searchInput, 'test');
        await userEvent.click(searchButton);

        // Wait for the message.error to be called with correct arguments
        await waitFor(() => {
            expect(message.error).toHaveBeenCalledWith('Search failed');
        });
    });

    test('ItemList onChangePriceRange function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        const inputNumber = screen.getAllByRole('spinbutton')[0];

        await userEvent.clear(inputNumber); // 清空输入框
        await userEvent.type(inputNumber, '50'); // 输入值
        await userEvent.keyboard('{Enter}'); // 按下 Enter 键
    });

    test('ItemList onChangeTimeRange function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        const beginDate = screen.getAllByRole('textbox')[1];
        const endDate = screen.getAllByRole('textbox')[2];

        await userEvent.click(beginDate);
        await userEvent.type(beginDate, '2022-01-01 00:00:00');
        await userEvent.keyboard('{Enter}');

        await userEvent.click(endDate);
        await userEvent.type(endDate, '2022-12-31 00:00:00');
        await userEvent.keyboard('{Enter}');
    });

    test('ItemList onChangeOrder function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        fireEvent.click(screen.getByLabelText('按评分排序'));
    });

    test('ItemList onChange function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        fireEvent.click(await screen.findByText('2'));
    });

    test('ItemList unCollect function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: true,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        uncollectService.mockResolvedValue({});

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        fireEvent.click(await screen.findByRole('button', { name: 'star' }));
    });

    test('ItemList collect function', async () => {
        // Mock the searchService to return a resolved value
        searchService.mockResolvedValue({
            total: 100,
            items: [{
                serviceId: '1',
                collected: false,
                title: 'Test Service',
                description: 'This is a test service.',
                owner: {
                    userId: '1',
                    nickname: 'test',
                    rating: 100,
                    avatar: 'test.jpg',
                }
            }],
        });

        // Mock the collectService to return a resolved value
        collectService.mockResolvedValue({});

        // Render ServicePage component
        render(
            <Router>
                <ServicePage />
            </Router>
        );

        fireEvent.click(await screen.findByRole('button', { name: 'star' }));
    });
});
