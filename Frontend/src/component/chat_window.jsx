import React, {useEffect, useRef, useState} from 'react';
import {Card, List, Input, Button, Avatar, Row, Col, Divider, message} from 'antd';
import TextArea from "antd/lib/input/TextArea";
import {getChatHistory} from "../service/chat";

const getHistoryCount = 10

export default function ChatWindow(props) {
  const selfId = props.self?.userId
  const [hasHistory, setHasHistory] = useState(true)
  const [inputMessage, setInputMessage] = useState('')
  const [messages, setMessages] = useState([])
  const messagesEndRef = useRef(null)
  const [scroll, setScroll] = useState(true)

  const getHistory = () => {
    if (!hasHistory) return
    getChatHistory(props.chat?.chatId, messages.length > 0 ? messages[0].messageId : '', getHistoryCount)
      .then(res => {
        if (res.length < getHistoryCount) setHasHistory(false)
        setMessages(messages => [...res.reverse(), ...messages])
      }).catch(error => message.error(error))
  }

  const onsend = () => {
    if (inputMessage) {
      props.socket.send({chatId: props.chat?.chatId, content: inputMessage})
      setInputMessage('')
      setScroll(true)
    }
  }

  useEffect(() => {
    setHasHistory(true)
    getChatHistory(props.chat?.chatId, '', getHistoryCount)
      .then(res => {
        setMessages(res.reverse())
        if (res.length < getHistoryCount) setHasHistory(false)
        setScroll(true)
      }).catch(error => message.error(error))
  }, [props.chat])

  useEffect(() => {
    if (scroll) {
      messagesEndRef.current.scrollIntoView({behavior: 'smooth'})
      setScroll(false)
    }
  }, [messages])

  useEffect(() => {
    if (props.socket) props.socket.onmessage = data => {
      if (data.chatId === props.chat?.chatId) setMessages(messages => [...messages, data])
      props.onmessage(data)
    }
  }, [props.socket, props.chat]);

  return (<Card
    title={props.chat?.chatter.nickname}
    style={{
      display: 'flex', flexDirection: 'column', width: '100%', height: 'calc(100vh - 10rem - 72px)'
    }}>
    <div style={{
      flex: 1,
      overflowY: 'auto',
      height: 'calc(100vh - 10rem - 271px)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
    }}>
      <a onClick={getHistory}
         style={{
           fontSize: '0.8rem', color: hasHistory ? '' : 'gray'
         }}>{hasHistory ? '获取更多聊天记录' : '无更多聊天记录'}</a>
      <List
        dataSource={messages}
        style={{width: '100%'}}
        renderItem={item => <div style={{
          display: 'flex',
          margin: '10px',
          justifyContent: item.senderId === selfId ? 'flex-end' : 'flex-start'
        }}>
          {item.senderId !== selfId &&
            <Avatar src={props.chat?.chatter.avatar} style={{margin: '0.8rem 10px 0 0'}}/>}
          <div style={{display: 'flex', flexDirection: 'column', maxWidth: '70%'}}>
            <div style={{
              color: 'gray',
              fontSize: '0.8rem',
              marginLeft: item.senderId === selfId ? 'auto' : 0,
              marginRight: item.senderId !== selfId ? 'auto' : 0
            }}>{item.createdAt}</div>
            <div style={{
              maxWidth: '100%',
              marginLeft: item.senderId === selfId ? 'auto' : 0,
              marginRight: item.senderId !== selfId ? 'auto' : 0,
              backgroundColor: item.senderId === selfId ? '#1677FF' : '#f1f0f0',
              color: item.senderId === selfId ? '#fff' : '#000',
              padding: '10px',
              borderRadius: '15px',
              wordWrap: 'break-word',
              whiteSpace: 'pre-wrap'
            }}>{item.content}</div>
          </div>
          {item.senderId === selfId &&
            <Avatar src={props?.self.avatar} style={{margin: '0.8rem 0 0 10px'}}/>}
        </div>}
      />
      <div ref={messagesEndRef}/>
    </div>
    <Divider/>
    <Row style={{width: '100%', alignItems: 'center'}}>
      <Col style={{flexGrow: 1}}>
        <TextArea
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          placeholder="输入消息"
          onPressEnter={onsend}
          style={{height: '3rem'}}
        />
      </Col>
      <Col style={{marginLeft: '15px'}}>
        <Button type="primary" onClick={onsend}>发送</Button>
      </Col>
    </Row>
  </Card>);
}
