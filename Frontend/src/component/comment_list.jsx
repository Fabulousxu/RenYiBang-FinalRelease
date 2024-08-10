import {Avatar, Button, Col, List, Pagination, Radio, Rate, Row, Tabs} from "antd";
import Item from "antd/es/list/Item";
import {Link} from "react-router-dom";
import {DeleteOutlined, LikeFilled, LikeOutlined} from "@ant-design/icons";
import React, {useState} from "react";
import TextArea from "antd/es/input/TextArea";

export const totalCommentEntry = 10

export default function CommentList(props) {
  const [message, setMessage] = useState('')
  const [rating, setRating] = useState(0)
  const [orderValue, setOrderValue] = useState('likes')
  const [mode, setMode] = useState('comment')

  const list = (<List
    itemLayout="horizontal"
    dataSource={props.list}
    renderItem={(item, index) => (<Item index={index} style={{flexDirection: 'column'}}>
      <Row style={{width: '100%', alignItems: 'center'}}>
        <Col span={4}>
          <Link to={`/profile/${item.user.userId}`}>
            <Item.Meta
              avatar={<Avatar src={item.user.avatar}/>}
              title={item.user.nickname}
              description={`帮帮评分${(item.user.rating / 10).toFixed(1)}`}
              style={{width: '100%'}}
            />
          </Link>
        </Col>
        <Col
          span={20}
          style={{visibility: item.hasOwnProperty('rating') ? 'visible' : 'hidden'}}
        >
          <Rate disabled allowHalf value={item?.rating / 20}/>
        </Col>
      </Row>
      <p style={{width: '100%', paddingLeft: '48px'}}>{item.content}</p>
      <Row style={{width: '100%', paddingLeft: '48px', alignItems: 'center'}}>
        <Col style={{color: 'gray'}}>{item.createdAt}</Col>
        <Col style={{padding: '0 0 0 15px'}}>
          <Button
            icon={item.liked ? <LikeFilled/> : <LikeOutlined/>}
            style={{border: 'none', borderRadius: '100%'}}
            size='small'
            onClick={() => props.onLike(index)}
          />
        </Col>
        <Col>{item.likedNumber}</Col>

        {item.commenter && item.commenter.userId === Number(localStorage.getItem('userId')) &&
        <Col style={{padding: '0 0 0 15px'}}>
          <Button
            icon={<DeleteOutlined/>}
            style={{border: 'none', borderRadius: '100%'}}
            size='small'
            onClick={() => props.onDelete(index)}
          />
        </Col>}

        {item.messager && item.messager.userId === Number(localStorage.getItem('userId')) &&
          <Col style={{padding: '0 0 0 15px'}}>
            <Button
              icon={<DeleteOutlined/>}
              style={{border: 'none', borderRadius: '100%'}}
              size='small'
              onClick={() => props.onDelete(index)}
            />
          </Col>}
      </Row>
    </Item>)}
  />)

  return (
    <div style={{width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center'}}>

      <Row style={{width: '70%', alignItems: 'center', paddingBottom: '2rem'}}>
        <Radio.Group onChange={e => setMode(e.target.value)} value={mode}>
          <Radio value='comment'>评论</Radio>
          <Radio value='message'>留言</Radio>
        </Radio.Group>

        {mode === 'comment' ? (
          <>
            <Col style={{flexGrow: 1}}>
              <TextArea
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder='输入文字'
                style={{flexGrow: 1, height: '3rem'}}
              />
            </Col>
            {/* 打分用的星星 */}
            <Col style={{marginTop: '15px'}}>
              <Rate value={rating} onChange={value => setRating(value)}/>
            </Col>
            <Col style={{marginLeft: '15px'}}>
              <Button type="primary" onClick={() => {
                props.onComment(message, rating * 20)
                setMessage('')
              }}>评论</Button>
            </Col>
          </>
        ) : (
          <>
            <Col style={{flexGrow: 1}}>
              <TextArea
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder='输入文字'
                style={{flexGrow: 1, height: '3rem'}}
              />
            </Col>
            <Col style={{marginLeft: '15px'}}>
              <Button type="primary" onClick={() => {
                props.onMessage(message)
                setMessage('')
              }}>留言</Button>
            </Col>
          </>
        )}

      </Row>

      <Tabs
        type='card'
        style={{width: '100%'}}
        items={[{
          key: 'comment', label: `评论 ${props.commentTotal}条`, children: list
        }, {key: 'message', label: `留言 ${props.messageTotal}条`, children: list}]}
        onChange={key => {
          props.onChangeMode(key)
          setOrderValue('likes')
        }}
        tabBarExtraContent={<Radio.Group onChange={e => {
          setOrderValue(e.target.value)
          props.onChangeOrder(e.target.value)
        }} value={orderValue}>
          <Radio value='likes'>按热度排序</Radio>
          <Radio value='time'>按时间排序</Radio>
        </Radio.Group>}
      />
      <Pagination
        pageSize={totalCommentEntry}
        total={props.total}
        current={props.currentPage}
        showSizeChanger={false}
        style={{marginTop: '24px'}}
        onChange={props.onChange}
      />
    </div>)
}