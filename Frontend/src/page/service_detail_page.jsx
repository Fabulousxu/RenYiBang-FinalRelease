import React, {useEffect, useState, useCallback} from "react";
import BasicLayout from "../component/basic_layout";
import ItemDetail from "../component/item_detail";
import CommentList, {totalCommentEntry} from "../component/comment_list";
import {
  accessService,
  collectService,
  getService,
  getServiceComment,
  getServiceMessage,
  likeComment,
  likeMessage,
  putMessage,
  putComment,
  unaccessService,
  uncollectService,
  unlikeComment,
  unlikeMessage,
  deleteMessage,
  deleteComment,
} from "../service/service";
import {MessageOutlined, PayCircleOutlined, StarOutlined} from "@ant-design/icons";
import {Button, FloatButton, message, Space} from "antd";
import {useParams} from "react-router-dom";

export default function ServiceDetailPage(props) {
  const {id} = useParams();
  const [detail, setDetail] = useState(null);
  const [mode, setMode] = useState('comment');
  const [commentTotal, setCommentTotal] = useState(0);
  const [messageTotal, setMessageTotal] = useState(0);
  const [commentList, setCommentList] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);

  const getCommentWhenCommentMode = useCallback(() => {
    getServiceComment(id, totalCommentEntry, 0, 'likes').then(res => {
      setCommentTotal(res.total);
      setCommentList(res.items);
    }).catch(err => message.error(err))
    getServiceMessage(id, totalCommentEntry, 0, 'likes').then(res => {
      setMessageTotal(res.total);
    }).catch(err => message.error(err))
  }, [id]);

  const getCommentWhenMessageMode = () => {
    getServiceMessage(id, totalCommentEntry, 0, 'likes').then(res => {
      setMessageTotal(res.total);
      setCommentList(res.items);
    }).catch(err => message.error(err))
    getServiceComment(id, totalCommentEntry, 0, 'likes').then(res => {
      setCommentTotal(res.total);
    }).catch(err => message.error(err))
  };

  useEffect(() => {
    getService(id).then(res => {
      console.log(res);
      setDetail(res);
    }).catch(err => message.error(err))
    getCommentWhenCommentMode();
  }, [id, getCommentWhenCommentMode]);

  function handleCollect() {
    if (detail.collected) {
      uncollectService(id).then(res => {
        setDetail({...detail, collected: false});
        message.success('取消收藏成功');
      }).catch(err => {
        message.error(err);
      });
    } else {
      collectService(id).then(res => {
        setDetail({...detail, collected: true});
        message.success('收藏任务成功');
      }).catch(err => {
        message.error(err);
      });
    }
  }

  function handleChat() {
    // 将任务发起者添加为聊天对象，跳转到聊天页面

  }

  function handleAccept() {
    // 接取/取消接取任务
    if (detail.accessed) {
      unaccessService(id).then(res => {
        setDetail({...detail, accessed: false});
        message.success('取消接取成功');
      }).catch(err => {
        message.error(err);
      });
    } else {
      accessService(id).then(res => {
        setDetail({...detail, accessed: true});
        message.success('接取任务成功');
      }).catch(err => {
        // Message
        message.error(err);
      });
    }
  }

  function handleDeleteComment(serviceCommentId) {
    deleteComment(serviceCommentId).then(res => {
      message.success('删除评论成功');
      // 刷新评论列表
      getServiceComment(id, totalCommentEntry, 0, 'likes').then(res => {
        setCommentTotal(res.total);
        setCommentList(res.items);
      }).catch(err => message.error(err))
    }).catch(err => message.error(err))
  }

  function handleDeleteMessage(serviceMessageId) {
    deleteMessage(serviceMessageId).then(res => {
      message.success('删除留言成功');
      // 刷新留言列表
      getServiceMessage(id, totalCommentEntry, 0, 'likes').then(res => {
        setMessageTotal(res.total);
        setCommentList(res.items);
      }).catch(err => message.error(err))
    }).catch(err => message.error(err))
  }

  return (<BasicLayout page="service-detail">
    <ItemDetail detail={detail} descriptionTitle="任务描述" ratingTitle='任务评分:'/>
    <Space style={{display: 'flex', justifyContent: 'center', marginBottom: '20px'}}>
      {detail && detail.collected ? <Button type="primary" size="large"
                                            onClick={handleCollect}><StarOutlined/>取消收藏</Button> :
        <Button size="large" onClick={handleCollect}><StarOutlined/>收藏</Button>}
      <Button size="large" onClick={handleChat}><MessageOutlined/>聊一聊</Button>
      {detail && detail.status !== 'REMOVE' && detail.status !== 'DELETE' ? (detail.accessed ?
        <Button type="primary" size="large"
                onClick={handleAccept}><PayCircleOutlined/>取消接取</Button> : <Button size="large"
                                                                                       onClick={handleAccept}><PayCircleOutlined/>接任务</Button>) : (detail && detail.status === 'REMOVE' ?
        <Button size="large" disabled>任务已被移除</Button> :
        <Button size="large" disabled>任务已被删除</Button>)}
    </Space>
    <div style={{height: '60px'}}></div>
    <CommentList
      commentTotal={commentTotal}
      messageTotal={messageTotal}
      list={commentList}
      total={mode === 'comment' ? commentTotal : messageTotal}
      currentPage={currentPage}
      onMessage={msg => {
        console.log(msg)
        putMessage(id, msg).then(res => {
          message.success('留言成功');

          // 会出好多bug，先注释掉
          // setMode('message');
          // getServiceMessage(id, totalCommentEntry, 0, 'likes').then(res => {
          //   setMessageTotal(res.total);
          //   setCommentList(res.items);
          // }).catch(err => message.error(err))
        }).catch(err => message.error(err))
      }}
      onComment={(msg, rating) => {
        putComment(id, msg, rating).then(res => {
          message.success('评论成功');

          // 会出好多bug，先注释掉
          // setMode('comment')
          // getServiceComment(id, totalCommentEntry, 0, 'likes').then(res => {
          //   setCommentTotal(res.total);
          //   setCommentList(res.items);
          // }).catch(err => message.error(err))
        }).catch(err => message.error(err))
      }}
      onChangeMode={key => {
        setMode(key);
        setCurrentPage(0);
        (key === 'comment' ? getCommentWhenCommentMode : getCommentWhenMessageMode)();
      }}
      onChange={(page, pageSize) => {
        (mode === 'comment' ? getServiceComment : getServiceMessage)(id, pageSize, page - 1, 'likes')
          .then(res => {
            setCommentTotal(res.total);
            setCommentList(res.items);
            setCurrentPage(page);
          }).catch(err => message.error(err))
      }}
      onChangeOrder={order => {
        (mode === 'comment' ? getServiceComment : getServiceMessage)(id, totalCommentEntry, 0, order)
          .then(res => {
            setCommentTotal(res.total);
            setCommentList(res.items);
            setCurrentPage(1);
          }).catch(err => message.error(err))
      }}
      onLike={index => {
        if (commentList[index].liked) {
          (mode === 'comment' ? unlikeComment : unlikeMessage)(mode === 'comment' ? commentList[index].serviceCommentId : commentList[index].serviceMessageId).then(res => {
            commentList[index].liked = false
            commentList[index].likedNumber--
            setCommentList([...commentList])
            message.success('取消点赞成功')
          }).catch(err => message.error(err))
        } else {
          (mode === 'comment' ? likeComment : likeMessage)(mode === 'comment' ? commentList[index].serviceCommentId : commentList[index].serviceMessageId).then(res => {
            commentList[index].liked = true
            commentList[index].likedNumber++
            setCommentList([...commentList])
            message.success('点赞成功')
          }).catch(err => message.error(err))
        }
      }}

      onDelete={index => {
        mode === 'comment' ? handleDeleteComment(commentList[index].serviceCommentId) : handleDeleteMessage(commentList[index].serviceMessageId)
      }}
    />
  </BasicLayout>);
}
