import {
    searchTask,
    getTask,
    getTaskComment,
    getTaskMessage,
    collectTask,
    uncollectTask,
    likeComment,
    unlikeComment,
    likeMessage,
    unlikeMessage,
    putMessage,
    putComment,
    accessTask,
    unaccessTask,
    getTaskSelectInfo,
    cancelTask,
    confirmSelectTask,
    refuseSelectTask,
    getTaskSuccessPeople,
    getTaskRefusePeople,
    deleteComment,
    deleteMessage
} from '../../service/task'; // 修改为实际路径

import {get, put, del, apiURL} from '../../service/util'; // 修改为实际路径

// 模拟 util.js 中的 get, put, 和 del 函数
jest.mock('../../service/util', () => ({
    get: jest.fn(),
    put: jest.fn(),
    del: jest.fn()
}));

describe('API Functions', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('searchTask should call get with correct URL and return modified result', async () => {
        const mockResponse = {
            items: [{ taskId: '1' }]
        };
        get.mockResolvedValueOnce(mockResponse);

        const result = await searchTask('test', 10, 0, 'time', ['2024-01-01', '2024-12-31'], [0, 100]);

        expect(get).toHaveBeenCalledWith(
            `${apiURL}/task/search?keyword=test&pageSize=10&pageIndex=0&order=time&timeBegin=2024-01-01&timeEnd=2024-12-31&priceLow=0&priceHigh=100`
        );
        expect(result.items[0].url).toBe('/task/1');
    });

    test('getTask should call get with correct URL', async () => {
        const mockResponse = { id: '1' };
        get.mockResolvedValueOnce(mockResponse);

        const result = await getTask('1');

        expect(get).toHaveBeenCalledWith(`${apiURL}/task/1`);
        expect(result).toEqual(mockResponse);
    });

    test('getTaskComment should call get with correct URL and modify result', async () => {
        const mockResponse = {
            items: [{ commenter: 'user1' }]
        };
        get.mockResolvedValueOnce(mockResponse);

        const result = await getTaskComment('1', 10, 0, 'likes');

        expect(get).toHaveBeenCalledWith(`${apiURL}/task/1/comment?pageSize=10&pageIndex=0&order=likes`);
        expect(result.items[0].user).toBe('user1');
    });

    test('collectTask should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await collectTask('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/1/collect`);
    });

    test('uncollectTask should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await uncollectTask('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/1/uncollect`);
    });

    test('likeComment should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await likeComment('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/comment/1/like`);
    });

    test('unlikeComment should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await unlikeComment('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/comment/1/unlike`);
    });

    test('likeMessage should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await likeMessage('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/message/1/like`);
    });

    test('unlikeMessage should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await unlikeMessage('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/message/1/unlike`);
    });

    test('putMessage should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await putMessage('1', 'message content');

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/1/message`, { content: 'message content' });
    });

    test('putComment should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await putComment('1', 'comment content', 5);

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/1/comment`, { content: 'comment content', rating: 5 });
    });

    test('accessTask should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await accessTask('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/1/access`);
    });

    test('unaccessTask should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await unaccessTask('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/1/unaccess`);
    });

    test('getTaskSelectInfo should call get with correct URL', async () => {
        const mockResponse = {};
        get.mockResolvedValueOnce(mockResponse);

        const result = await getTaskSelectInfo('1', 10, 0);

        expect(get).toHaveBeenCalledWith(`${apiURL}/task/1/select/info?pageSize=10&pageIndex=0`);
        expect(result).toEqual(mockResponse);
    });

    test('cancelTask should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await cancelTask('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/1/cancel`);
    });

    test('confirmSelectTask should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await confirmSelectTask('1', ['user1', 'user2']);

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/1/select/confirm`, { userList: ['user1', 'user2'] });
    });

    test('refuseSelectTask should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await refuseSelectTask('1', ['user1', 'user2']);

        expect(put).toHaveBeenCalledWith(`${apiURL}/task/1/select/deny`, { userList: ['user1', 'user2'] });
    });

    test('getTaskSuccessPeople should call get with correct URL', async () => {
        const mockResponse = {};
        get.mockResolvedValueOnce(mockResponse);

        const result = await getTaskSuccessPeople('1', 10, 0);

        expect(get).toHaveBeenCalledWith(`${apiURL}/task/1/select/success?pageSize=10&pageIndex=0`);
        expect(result).toEqual(mockResponse);
    });

    test('getTaskRefusePeople should call get with correct URL', async () => {
        const mockResponse = {};
        get.mockResolvedValueOnce(mockResponse);

        const result = await getTaskRefusePeople('1', 10, 0);

        expect(get).toHaveBeenCalledWith(`${apiURL}/task/1/select/fail?pageSize=10&pageIndex=0`);
        expect(result).toEqual(mockResponse);
    });

    test('deleteComment should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await deleteComment('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/comment/1`);
    });

    test('deleteMessage should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await deleteMessage('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/task/message/1`);
    });

    test('getTaskMessage should call get with correct URL and modify result', async () => {
        const mockResponse = {
            items: [{ messager: 'user1' }]
        };
        get.mockResolvedValueOnce(mockResponse);

        const result = await getTaskMessage('1', 10, 0, 'time');

        expect(get).toHaveBeenCalledWith(`${apiURL}/task/1/message?pageSize=10&pageIndex=0&order=time`);
        expect(result.items[0].user).toBe('user1');
    });
});
