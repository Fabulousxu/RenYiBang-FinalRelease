import {
    searchService,
    getService,
    getServiceComment,
    getServiceMessage,
    collectService,
    uncollectService,
    likeComment,
    unlikeComment,
    likeMessage,
    unlikeMessage,
    putMessage,
    putComment,
    accessService,
    unaccessService,
    getServiceSelectInfo,
    cancelService,
    confirmSelectService,
    refuseSelectService,
    getServiceSuccessPeople,
    getServiceRefusePeople,
    deleteComment,
    deleteMessage
} from '../../service/service'; // 修改为实际路径

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

    test('searchService should call get with correct URL and return modified result', async () => {
        const mockResponse = {
            items: [{ serviceId: '1' }]
        };
        get.mockResolvedValueOnce(mockResponse);

        const result = await searchService('test', 10, 0, 'time', ['2024-01-01', '2024-12-31'], [0, 100]);

        expect(get).toHaveBeenCalledWith(
            `${apiURL}/service/search?keyword=test&pageSize=10&pageIndex=0&order=time&timeBegin=2024-01-01&timeEnd=2024-12-31&priceLow=0&priceHigh=100`
        );
        expect(result.items[0].url).toBe('/service/1');
    });

    test('getService should call get with correct URL', async () => {
        const mockResponse = { id: '1' };
        get.mockResolvedValueOnce(mockResponse);

        const result = await getService('1');

        expect(get).toHaveBeenCalledWith(`${apiURL}/service/1`);
        expect(result).toEqual(mockResponse);
    });

    test('getServiceComment should call get with correct URL and modify result', async () => {
        const mockResponse = {
            items: [{ commenter: 'user1' }]
        };
        get.mockResolvedValueOnce(mockResponse);

        const result = await getServiceComment('1', 10, 0, 'likes');

        expect(get).toHaveBeenCalledWith(`${apiURL}/service/1/comment?pageSize=10&pageIndex=0&order=likes`);
        expect(result.items[0].user).toBe('user1');
    });

    test('collectService should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await collectService('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/1/collect`);
    });

    test('uncollectService should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await uncollectService('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/1/uncollect`);
    });

    test('likeComment should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await likeComment('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/comment/1/like`);
    });

    test('unlikeComment should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await unlikeComment('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/comment/1/unlike`);
    });

    test('likeMessage should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await likeMessage('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/message/1/like`);
    });

    test('unlikeMessage should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await unlikeMessage('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/message/1/unlike`);
    });

    test('putMessage should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await putMessage('1', 'message content');

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/1/message`, { content: 'message content' });
    });

    test('putComment should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await putComment('1', 'comment content', 5);

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/1/comment`, { content: 'comment content', rating: 5 });
    });

    test('accessService should call put with correct URL', async () => {
        put.mockResolvedValueOnce({});

        await accessService('1');

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/1/access`);
    });

    test('unaccessService should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await unaccessService('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/1/unaccess`);
    });

    test('getServiceSelectInfo should call get with correct URL', async () => {
        const mockResponse = {};
        get.mockResolvedValueOnce(mockResponse);

        const result = await getServiceSelectInfo('1', 10, 0);

        expect(get).toHaveBeenCalledWith(`${apiURL}/service/1/select/info?pageSize=10&pageIndex=0`);
        expect(result).toEqual(mockResponse);
    });

    test('cancelService should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await cancelService('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/1/cancel`);
    });

    test('confirmSelectService should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await confirmSelectService('1', ['user1', 'user2']);

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/1/select/confirm`, { userList: ['user1', 'user2'] });
    });

    test('refuseSelectService should call put with correct URL and payload', async () => {
        put.mockResolvedValueOnce({});

        await refuseSelectService('1', ['user1', 'user2']);

        expect(put).toHaveBeenCalledWith(`${apiURL}/service/1/select/deny`, { userList: ['user1', 'user2'] });
    });

    test('getServiceSuccessPeople should call get with correct URL', async () => {
        const mockResponse = {};
        get.mockResolvedValueOnce(mockResponse);

        const result = await getServiceSuccessPeople('1', 10, 0);

        expect(get).toHaveBeenCalledWith(`${apiURL}/service/1/select/success?pageSize=10&pageIndex=0`);
        expect(result).toEqual(mockResponse);
    });

    test('getServiceRefusePeople should call get with correct URL', async () => {
        const mockResponse = {};
        get.mockResolvedValueOnce(mockResponse);

        const result = await getServiceRefusePeople('1', 10, 0);

        expect(get).toHaveBeenCalledWith(`${apiURL}/service/1/select/fail?pageSize=10&pageIndex=0`);
        expect(result).toEqual(mockResponse);
    });

    test('deleteComment should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await deleteComment('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/comment/1`);
    });

    test('deleteMessage should call del with correct URL', async () => {
        del.mockResolvedValueOnce({});

        await deleteMessage('1');

        expect(del).toHaveBeenCalledWith(`${apiURL}/service/message/1`);
    });

    test('getServiceMessage should call get with correct URL and modify result', async () => {
        const mockResponse = {
            items: [{ messager: 'user1' }]
        };
        get.mockResolvedValueOnce(mockResponse);

        const result = await getServiceMessage('1', 10, 0, 'time');

        expect(get).toHaveBeenCalledWith(`${apiURL}/service/1/message?pageSize=10&pageIndex=0&order=time`);
        expect(result.items[0].user).toBe('user1');
    });
});
