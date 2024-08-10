import {apiURL, get, post} from "../../service/util";
import connectWebSocket, {getChatList, getChatHistory, enterChat} from "../../service/chat";

jest.mock("../../service/util", () => ({
  apiURL: "http://mocked-api-url", get: jest.fn(), post: jest.fn(),
}))

describe('chat', () => {
  beforeAll(() => {
    global.WebSocket = jest.fn().mockImplementation(() => {
      return {
        send: jest.fn(), close: jest.fn(), onopen: null, onclose: null, onmessage: null
      }
    })
  })

  afterEach(() => { jest.clearAllMocks() })

  test('open websocket test', () => {
    const userId = 1
    const ws = connectWebSocket(userId)
    const socket = global.WebSocket.mock.instances[0]
    expect(global.WebSocket).toHaveBeenCalledWith(`${process.env.REACT_APP_WS_CHAT_URL}/chat`)
    // expect(socket.send).toHaveBeenCalledWith(JSON.stringify({type: 'register', userId}))
  })

  test('close websocket test', () => {
    const userId = 1
    const ws = connectWebSocket(userId)
    const socket = global.WebSocket.mock.instances[0]
    // ws.close()
    // expect(socket.close).toHaveBeenCalledWith()
  })

  test('onmessage test', () => {
    const userId = 1
    const ws = connectWebSocket(userId)
    const socket = global.WebSocket.mock.instances[0]
    const message = {data: JSON.stringify({chatId: 'chat1', content: 'content'})}
    const onmessage = jest.fn()
    ws.onmessage = onmessage
    socket.onmessage(message)
    expect(onmessage).toHaveBeenCalledWith({chatId: 'chat1', content: 'content'})
  })

  test('onsend test', () => {
    const userId = 1
    const ws = connectWebSocket(userId)
    const socket = global.WebSocket.mock.instances[0]
    const message = {chatId: 'chat1', content: 'content'}
    // ws.send(message)
    // expect(socket.send).toHaveBeenCalledWith(JSON.stringify(message))
  })

  test('getChatList test', async () => {
    await getChatList()
    expect(get).toHaveBeenCalledWith('http://mocked-api-url/chat/list')
  })

  test('getChatHistory test', async () => {
    const chatId = 'chat'
    const lastMessageId = 'message'
    const count = 10
    await getChatHistory(chatId, lastMessageId, count)
    expect(get).toHaveBeenCalledWith(`http://mocked-api-url/chat/history?chatId=${chatId}&lastMessageId=${lastMessageId}&count=${count}`);
  })

  test('enterChat test', async () => {
    const type = 'task'
    const ofId = 1
    await enterChat('task', 1)
    expect(post).toHaveBeenCalledWith(`http://mocked-api-url/chat/enter/${type}/${ofId}`)
  })
})
