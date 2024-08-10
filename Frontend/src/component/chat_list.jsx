import React from 'react';
import {List, Avatar, Row, Col} from 'antd';

export default function ChatList(props) {
  return (<List
    itemLayout="horizontal"
    dataSource={props.list}
    renderItem={(item, index) => <List.Item onClick={() => {
      item.unreadCount = 0
      props.onChat(item)
    }}>
      <Row style={{width: '100%'}}>
        <Col style={{flexGrow: 1}}>
          <List.Item.Meta
            avatar={<Avatar src={item?.chatter.avatar}/>}
            title={item?.chatter.nickname}
            description={<div style={{
              whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis'
            }}>{item?.lastMessageContent}</div>}
          />
        </Col>
        <Col style={{display: 'flex', flexDirection: 'column', alignItems: 'end'}}>
          <div style={{color: 'gray', fontSize: '0.8rem'}}>{item?.lastMessageCreatedAt}</div>
          <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            color: 'white',
            backgroundColor: '#ff2b2b',
            margin: 'auto 0',
            width: '16px',
            height: '16px',
            borderRadius: '100%',
            fontSize: '0.65rem',
            visibility: item?.unreadCount > 0 ? 'visible' : 'hidden'
          }}>{item?.unreadCount > 99 ? 99 : item?.unreadCount}</div>
        </Col>
      </Row>
    </List.Item>}
  />);
}
