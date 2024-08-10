import { get, post, put, del, apiURL } from '../../service/util';

global.fetch = jest.fn();

describe('HTTP utility functions', () => {
  const url = 'http://example.com/api';
  const jwt = 'mock-jwt-token';

  beforeEach(() => {
    localStorage.setItem('jwt', jwt);
    jest.clearAllMocks();
  });

  afterEach(() => {
    localStorage.removeItem('jwt');
  });

  const mockFetch = (status, response) => {
    global.fetch.mockResolvedValueOnce({
      status: status,
      json: jest.fn().mockResolvedValue(response),
    });
  };

  const mockFetchError = (status) => {
    global.fetch.mockResolvedValueOnce({
      status: status,
      json: jest.fn().mockResolvedValue({ ok: false, message: 'Error message' }),
    });
  };

  test('get request success', async () => {
    const data = { key: 'value' };
    mockFetch(200, { ok: true, data });

    const result = await get(url);
    expect(result).toEqual(data);
    expect(global.fetch).toHaveBeenCalledWith(url, {
      method: 'GET',
      headers: { jwt },
      credentials: 'include',
    });
  });

  test('get request failure', async () => {
    mockFetchError(400);

    await expect(get(url)).rejects.toEqual(400);
  });

  test('post request success', async () => {
    const data = { key: 'value' };
    const response = { ok: true, data };
    mockFetch(200, response);

    const result = await post(url, data);
    expect(result).toEqual(data);
    expect(global.fetch).toHaveBeenCalledWith(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', jwt },
      body: JSON.stringify(data),
      credentials: 'include',
    });
  });

  test('post request failure', async () => {
    const data = { key: 'value' };
    mockFetchError(400);

    await expect(post(url, data)).rejects.toEqual(400);
  });

  test('put request success', async () => {
    const data = { key: 'value' };
    const response = { ok: true, data };
    mockFetch(200, response);

    const result = await put(url, data);
    expect(result).toEqual(data);
    expect(global.fetch).toHaveBeenCalledWith(url, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json', jwt },
      body: JSON.stringify(data),
      credentials: 'include',
    });
  });

  test('put request failure', async () => {
    const data = { key: 'value' };
    mockFetchError(400);

    await expect(put(url, data)).rejects.toEqual(400);
  });

  test('delete request success', async () => {
    const data = { key: 'value' };
    const response = { ok: true, data };
    mockFetch(200, response);

    const result = await del(url, data);
    expect(result).toEqual(data);
    expect(global.fetch).toHaveBeenCalledWith(url, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json', jwt },
      body: JSON.stringify(data),
      credentials: 'include',
    });
  });

  test('delete request failure', async () => {
    const data = { key: 'value' };
    mockFetchError(400);

    await expect(del(url, data)).rejects.toEqual(400);
  });
});
