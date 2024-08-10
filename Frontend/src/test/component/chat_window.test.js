import React from 'react';
import '@testing-library/jest-dom';
import ChatWindow from "../../component/chat_window";
import {render, fireEvent, screen, waitFor} from "@testing-library/react";
import {getChatHistory} from "../../service/chat";
import {message} from "antd";

jest.mock('../../service/chat')
jest.mock('antd', () => { return {...jest.requireActual('antd'), message: {error: jest.fn()}}})

describe('ChatWindow', () => {
  const chat = {chatId: 'chat', chatter: {userId: 2, nickname: 'user', avatar: 'avatar1'}}
  const self = {userId: 1, avatar: 'avatar2'}
  const socket = {send: jest.fn(), onmessage: null}
  const messages2 = [{
    messageId: '2', senderId: 1, createdAt: '2024-08-01 12:00:01', content: 'content2'
  }, {
    messageId: '1', senderId: 2, createdAt: '2024-08-01 12:00:00', content: 'content1'
  }]
  const messages10 = [{
    messageId: '12', senderId: 1, createdAt: '2024-08-01 12:00:11', content: 'content12'
  }, {
    messageId: '11', senderId: 2, createdAt: '2024-08-01 12:00:10', content: 'content11'
  }, {
    messageId: '10', senderId: 1, createdAt: '2024-08-01 12:00:09', content: 'content10'
  }, {
    messageId: '9', senderId: 2, createdAt: '2024-08-01 12:00:08', content: 'content9'
  }, {
    messageId: '8', senderId: 1, createdAt: '2024-08-01 12:00:07', content: 'content8'
  }, {
    messageId: '7', senderId: 2, createdAt: '2024-08-01 12:00:06', content: 'content7'
  }, {
    messageId: '6', senderId: 1, createdAt: '2024-08-01 12:00:05', content: 'content6'
  }, {
    messageId: '5', senderId: 2, createdAt: '2024-08-01 12:00:04', content: 'content5'
  }, {
    messageId: '4', senderId: 1, createdAt: '2024-08-01 12:00:03', content: 'content4'
  }, {
    messageId: '3', senderId: 2, createdAt: '2024-08-01 12:00:02', content: 'content3'
  }]
  const receive = {
    chatId: 'chat',
    messageId: 13,
    senderId: 2,
    createdAt: '2024-08-01 12:00:12',
    content: 'content13'
  }

  beforeAll(() => {
    window.matchMedia = window.matchMedia || function () {
      return {matches: false, addListener: () => {}, removeListener: () => {}};
    }
    window.HTMLElement.prototype.scrollIntoView = jest.fn()
  })

  beforeEach(() => {
    jest.clearAllMocks()
    getChatHistory.mockResolvedValue(messages10)
  })

  test('render test', async () => {
    getChatHistory.mockResolvedValue(messages2)
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={msg => {}}/>)
    await waitFor(() => expect(getChatHistory).toHaveBeenCalledWith('chat', '', 10))
    expect(screen.getByText('user')).toBeInTheDocument()
    expect(await screen.findByText('content1')).toBeInTheDocument()
    expect(await screen.findByText('content2')).toBeInTheDocument()
    expect(await screen.findByText('2024-08-01 12:00:00')).toBeInTheDocument()
    expect(await screen.findByText('2024-08-01 12:00:01')).toBeInTheDocument()
    expect(await screen.findByText('无更多聊天记录')).toBeInTheDocument()
  })

  test('onsend test1', async () => {
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={msg => {}}/>)
    fireEvent.change(screen.getByPlaceholderText('输入消息'), {target: {value: 'input1'}})
    fireEvent.click(screen.getByText('发 送'))
    expect(socket.send).toHaveBeenCalledWith({chatId: 'chat', content: 'input1'})
    expect(screen.getByPlaceholderText('输入消息').value).toBe('')
  })

  test('onsend test2', async () => {
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={msg => {}}/>)
    fireEvent.change(screen.getByPlaceholderText('输入消息'), {target: {value: ''}})
    fireEvent.click(screen.getByText('发 送'))
    expect(socket.send).not.toHaveBeenCalled()
  })

  test('onload history test', async () => {
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={msg => {}}/>)
    await waitFor(() => expect(getChatHistory).toHaveBeenCalledWith('chat', '', 10))
    expect(await screen.findByText('获取更多聊天记录')).toBeInTheDocument()
    fireEvent.click(await screen.findByText('获取更多聊天记录'))
    await waitFor(() => expect(getChatHistory).toHaveBeenCalledWith('chat', '', 10))
  })

  test('onmessage test', async () => {
    const handleOnmessage = jest.fn()
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={handleOnmessage}/>)
    expect(socket.onmessage).not.toBe(null)
    socket.onmessage(receive)
    expect(handleOnmessage).toHaveBeenCalledWith(receive)
  })

  test('error test1', async () => {
    const error = new Error('error')
    getChatHistory.mockRejectedValue(error)
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={msg => {}}/>)
    await waitFor(() => expect(message.error).toHaveBeenCalledWith(error))
  })

  test('error test2', async () => {
    render(<ChatWindow chat={chat} self={self} socket={socket} onmessage={msg => {}}/>)
    await waitFor(() => expect(getChatHistory).toHaveBeenCalledWith('chat', '', 10))
    expect(await screen.findByText('获取更多聊天记录')).toBeInTheDocument()
    const error = new Error('error')
    getChatHistory.mockRejectedValue(error)
    fireEvent.click(await screen.findByText('获取更多聊天记录'))
    await waitFor(() => expect(message.error).toHaveBeenCalledWith(error))
  })
})