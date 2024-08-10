import React from 'react';
import '@testing-library/jest-dom';
import ChatList from "../../component/chat_list";
import {render, fireEvent, screen} from "@testing-library/react";

describe('ChatList', () => {
  const chats = [{
    chatter: {avatar: 'avatar1', nickname: 'user1'},
    lastMessageContent: 'content1',
    lastMessageCreatedAt: '2024-08-01 12:00:00',
    unreadCount: 1,
  }, {
    chatter: {avatar: 'avatar2', nickname: 'user2'},
    lastMessageContent: 'content2',
    lastMessageCreatedAt: '2024-08-01 12:00:01',
    unreadCount: 0,
  }]

  beforeAll(() => {
    window.matchMedia = window.matchMedia || function () {
      return {matches: false, addListener: () => {}, removeListener: () => {}}
    }
  })

  test('render test', () => {
    render(<ChatList list={chats} onChat={() => {}}/>)
    expect(screen.getByText('user1')).toBeInTheDocument()
    expect(screen.getByText('user2')).toBeInTheDocument()
    expect(screen.getByText('content1')).toBeInTheDocument()
    expect(screen.getByText('content2')).toBeInTheDocument()
    expect(screen.getByText('2024-08-01 12:00:00')).toBeInTheDocument()
    expect(screen.getByText('2024-08-01 12:00:01')).toBeInTheDocument()
    expect(screen.getByText('1')).toBeInTheDocument()
  })

  test('onchat test', () => {
    const handleChat = jest.fn()
    render(<ChatList list={chats} onChat={handleChat}/>)
    fireEvent.click(screen.getByText('user1'))
    expect(handleChat).toHaveBeenCalledWith({
      chatter: {avatar: 'avatar1', nickname: 'user1'},
      lastMessageContent: 'content1',
      lastMessageCreatedAt: '2024-08-01 12:00:00',
      unreadCount: 0
    })
  })
})