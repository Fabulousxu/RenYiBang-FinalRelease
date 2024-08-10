import React, {act, useEffect} from 'react';
import '@testing-library/jest-dom';
import {render, waitFor, screen, fireEvent} from '@testing-library/react';
import MessagePage from '../../page/message_page';
import {BrowserRouter} from "react-router-dom";
import connectWebSocket, {getChatList} from "../../service/chat";
import {message} from "antd";

jest.mock('../../service/chat')
jest.mock('antd', () => { return {...jest.requireActual('antd'), message: {error: jest.fn()}}})
jest.mock('../../component/chat_window', () => props => {
  if (props.socket) props.socket.onmessage = data => props.onmessage(data)
  return <div/>
})

describe('MessagePage', () => {
  const ws = {send: jest.fn(), onmessage: null, close: jest.fn()}
  const self = {userId: 1, nickname: 'self', avatar: 'avatar1'}
  const chats = [{
    chatId: 'chat1',
    chatter: {userId: 2, avatar: 'avatar2', nickname: 'user2'},
    lastMessageContent: 'content1',
    lastMessageCreatedAt: '2024-08-01 12:00:00',
    unreadCount: 1,
  }, {
    chatId: 'chat2',
    chatter: {userId: 3, avatar: 'avatar3', nickname: 'user3'},
    lastMessageContent: 'content2',
    lastMessageCreatedAt: '2024-08-01 12:00:01',
    unreadCount: 0,
  }]

  beforeAll(() => {
    window.matchMedia = window.matchMedia || function () {
      return {matches: false, addListener: () => {}, removeListener: () => {}}
    }
  })

  beforeEach(() => {
    jest.clearAllMocks()
    getChatList.mockResolvedValue({self, chats})
    connectWebSocket.mockReturnValue(ws)
  })

  test('render test', async () => {
    render(<BrowserRouter><MessagePage/></BrowserRouter>)
    await waitFor(() => expect(getChatList).toHaveBeenCalled())
    expect(await screen.findByText('user2')).toBeInTheDocument()
    expect(screen.getByText('user2')).toBeInTheDocument()
    expect(screen.getByText('content1')).toBeInTheDocument()
    expect(screen.getByText('content2')).toBeInTheDocument()
  })

  test('websocket test', async () => {
    render(<BrowserRouter><MessagePage/></BrowserRouter>)
    await waitFor(() => expect(connectWebSocket).toHaveBeenCalledWith(self.userId))
    const message = {
      chatId: 'chat1',
      messageId: 3,
      senderId: 2,
      createdAt: '2024-08-01 12:00:02',
      content: 'content3'
    }
    await act(() => ws.onmessage(message))
  })

  test('error test1', async () => {
    const error = new Error('error')
    getChatList.mockRejectedValue(error)
    render(<BrowserRouter><MessagePage/></BrowserRouter>)
    await waitFor(() => expect(message.error).toHaveBeenCalledWith(error))
  })
});
