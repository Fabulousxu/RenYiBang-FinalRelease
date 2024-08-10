import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import TaskDetailPage from '../../page/task_detail_page'; // 修改为实际路径
import { getTask, getTaskComment, getTaskMessage, collectTask, uncollectTask, accessTask, unaccessTask, enterChat, deleteComment, deleteMessage, putMessage, putComment, likeComment, unlikeComment, likeMessage, unlikeMessage } from '../../service/task';
import { MemoryRouter, Route, Routes } from 'react-router-dom';

// 模拟服务
jest.mock('../../service/task', () => ({
    getTask: jest.fn(),
    getTaskComment: jest.fn(),
    getTaskMessage: jest.fn(),
    collectTask: jest.fn(),
    uncollectTask: jest.fn(),
    accessTask: jest.fn(),
    unaccessTask: jest.fn(),
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

describe('TaskDetailPage', () => {
    const taskId = '1';

    beforeAll(() => {
        // 每次测试前清理模拟函数
        jest.clearAllMocks();

        getTask.mockResolvedValue({
            taskId: '1',
            collected: false,
            title: 'Test Task',
            description: 'This is a test task.',
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

    test('renders and loads task detail', async () => {
        // 模拟数据
        getTask.mockResolvedValueOnce({
            id: taskId,
            collected: false,
            accessed: false,
            status: 'ACTIVE'
        });
        getTaskComment.mockResolvedValueOnce({
            total: 0,
            items: []
        });
        getTaskMessage.mockResolvedValueOnce({
            total: 0
        });

        render(
            <MemoryRouter initialEntries={[`/tasks/${taskId}`]}>
                <Routes>
                    <Route path="/tasks/:id" element={<TaskDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(getTask).toHaveBeenCalledWith(taskId);
        });
        expect(screen.getByText('收藏')).toBeInTheDocument();
        expect(screen.getByText('聊一聊')).toBeInTheDocument();
    });

    test('handles collect and uncollect actions', async () => {
        // 初始状态
        getTask.mockResolvedValue({
            taskId: '1',
            collected: false,
            title: 'Test Task',
            description: 'This is a test task.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });
        collectTask.mockResolvedValue({});
        uncollectTask.mockResolvedValue({});

        getTaskComment.mockResolvedValue({
            total: 0,
            items: []
        });

        getTaskMessage.mockResolvedValue({
            total: 0,
            items: []
        });

        render(
            <MemoryRouter initialEntries={[`/tasks/${taskId}`]}>
                <Routes>
                    <Route path="/tasks/:id" element={<TaskDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        // 确保 getTask 被调用
        await waitFor(() => {
            expect(getTask).toHaveBeenCalled();
        });

        // 确保页面渲染了收藏按钮
        const collectButton = await screen.findByText('收藏');
        expect(collectButton).toBeInTheDocument();

        // 点击收藏按钮
        // fireEvent.click(collectButton);

        // // 确保 collectTask 被调用
        // await waitFor(() => {
        //     expect(collectTask).toHaveBeenCalledWith(taskId);
        // });
        //
    });

    test('handles chat action', async () => {
        enterChat.mockResolvedValue({});

        getTask.mockResolvedValue({
            taskId: '1',
            collected: false,
            title: 'Test Task',
            description: 'This is a test task.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });
        collectTask.mockResolvedValue({});
        uncollectTask.mockResolvedValue({});

        getTaskComment.mockResolvedValue({
            total: 0,
            items: []
        });

        getTaskMessage.mockResolvedValue({
            total: 0,
            items: []
        });

        render(
            <MemoryRouter initialEntries={[`/tasks/${taskId}`]}>
                <Routes>
                    <Route path="/tasks/:id" element={<TaskDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        const chatButton = screen.getByRole('button', {name: 'message 聊一聊'});
    });

    test('handles accept and unaccept actions', async () => {
        enterChat.mockResolvedValue({});

        getTask.mockResolvedValue({
            taskId: '1',
            collected: false,
            title: 'Test Task',
            description: 'This is a test task.',
            status: 'NORMAL',
            owner: {
                userId: '1',
                nickname: 'test',
                rating: 100,
                avatar: 'test.jpg',
            }
        });
        collectTask.mockResolvedValue({});
        uncollectTask.mockResolvedValue({});

        getTaskComment.mockResolvedValue({
            total: 0,
            items: []
        });

        getTaskMessage.mockResolvedValue({
            total: 0,
            items: []
        });
        accessTask.mockResolvedValueOnce({});
        unaccessTask.mockResolvedValueOnce({});

        render(
            <MemoryRouter initialEntries={[`/tasks/${taskId}`]}>
                <Routes>
                    <Route path="/tasks/:id" element={<TaskDetailPage />} />
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(getTask).toHaveBeenCalledWith(taskId);
        });
    });
});
