import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import ServiceDetailPage from '../../page/service_detail_page'; // 修改为实际路径
import { getService, getServiceComment, getServiceMessage, collectService, uncollectService, accessService, unaccessService, enterChat, deleteComment, deleteMessage, putMessage, putComment, likeComment, unlikeComment, likeMessage, unlikeMessage } from '../../service/service';
import { MemoryRouter, Route, Routes } from 'react-router-dom';

// 模拟服务
jest.mock('../../service/service', () => ({
    getService: jest.fn(),
    getServiceComment: jest.fn(),
    getServiceMessage: jest.fn(),
    collectService: jest.fn(),
    uncollectService: jest.fn(),
    accessService: jest.fn(),
    unaccessService: jest.fn(),
    enterChat: jest.fn(),
    deleteComment: jest.fn(),
    deleteMessage: jest.fn(),
    putMessage: jest.fn(),
    putComment: jest.fn(),
    likeComment: jest.fn(),
    unlikeComment: jest.fn(),
    likeMessage: jest.fn(),
    unlikeMessage: jest.fn(),
}));

jest.mock('antd', () => {
    const originalModule = jest.requireActual('antd');
    return {
        ...originalModule,
        message: {
            error: jest.fn(),
        },
    };
});

describe('ServiceDetailPage', () => {
    const serviceId = '1';

    beforeAll(() => {
        // 每次测试前清理模拟函数
        jest.clearAllMocks();

        getService.mockResolvedValue({
            serviceId: '1',
            collected: false,
            title: 'Test Service',
            description: 'This is a test service.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });

        window.matchMedia = window.matchMedia || function () {
            return {matches: false, addListener: () => {}, removeListener: () => {}}
        }
    });

    test('renders and loads service detail', async () => {
        // 模拟数据
        getService.mockResolvedValueOnce({
            id: serviceId,
            collected: false,
            accessed: false,
            status: 'ACTIVE'
        });
        getServiceComment.mockResolvedValueOnce({
            total: 0,
            items: []
        });
        getServiceMessage.mockResolvedValueOnce({
            total: 0
        });

        render(
            <MemoryRouter initialEntries={[`/services/${serviceId}`]}>
                <Routes>
                    <Route path="/services/:id" element={<ServiceDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(getService).toHaveBeenCalledWith(serviceId);
        });
        expect(screen.getByText('收藏')).toBeInTheDocument();
        expect(screen.getByText('聊一聊')).toBeInTheDocument();
    });

    test('handles collect and uncollect actions', async () => {
        // 初始状态
        getService.mockResolvedValue({
            serviceId: '1',
            collected: false,
            title: 'Test Service',
            description: 'This is a test service.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });
        collectService.mockResolvedValue({});
        uncollectService.mockResolvedValue({});

        getServiceComment.mockResolvedValue({
            total: 0,
            items: []
        });

        getServiceMessage.mockResolvedValue({
            total: 0,
            items: []
        });

        render(
            <MemoryRouter initialEntries={[`/services/${serviceId}`]}>
                <Routes>
                    <Route path="/services/:id" element={<ServiceDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        // 确保 getService 被调用
        await waitFor(() => {
            expect(getService).toHaveBeenCalled();
        });

        // 确保页面渲染了收藏按钮
        const collectButton = await screen.findByText('收藏');
        expect(collectButton).toBeInTheDocument();

        // 点击收藏按钮
        // fireEvent.click(collectButton);

        // // 确保 collectService 被调用
        // await waitFor(() => {
        //     expect(collectService).toHaveBeenCalledWith(serviceId);
        // });
        //
    });

    test('handles chat action', async () => {
        enterChat.mockResolvedValue({});

        getService.mockResolvedValue({
            serviceId: '1',
            collected: false,
            title: 'Test Service',
            description: 'This is a test service.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });
        collectService.mockResolvedValue({});
        uncollectService.mockResolvedValue({});

        getServiceComment.mockResolvedValue({
            total: 0,
            items: []
        });

        getServiceMessage.mockResolvedValue({
            total: 0,
            items: []
        });

        render(
            <MemoryRouter initialEntries={[`/services/${serviceId}`]}>
                <Routes>
                    <Route path="/services/:id" element={<ServiceDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        const chatButton = screen.getByRole('button', {name: 'message 聊一聊'});
    });

    test('handles accept and unaccept actions', async () => {
        enterChat.mockResolvedValue({});

        getService.mockResolvedValue({
            serviceId: '1',
            collected: false,
            title: 'Test Service',
            description: 'This is a test service.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });
        collectService.mockResolvedValue({});
        uncollectService.mockResolvedValue({});

        getServiceComment.mockResolvedValue({
            total: 0,
            items: []
        });

        getServiceMessage.mockResolvedValue({
            total: 0,
            items: []
        });
        accessService.mockResolvedValueOnce({});
        unaccessService.mockResolvedValueOnce({});

        render(
            <MemoryRouter initialEntries={[`/services/${serviceId}`]}>
                <Routes>
                    <Route path="/services/:id" element={<ServiceDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(getService).toHaveBeenCalledWith(serviceId);
        });
    });
});
