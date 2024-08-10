import React, { useEffect, useState } from 'react';
import BasicLayout from "../component/basic_layout";
import {Descriptions, Avatar, Typography, Button, Table, Tabs, Modal, Form, Input, Upload, message} from 'antd';
import { getSelfProfile } from '../service/user';
import {Link, Navigate, useParams} from "react-router-dom";
import {cancelTask, unaccessTask, uncollectTask} from '../service/task';
import TabPane from "antd/es/tabs/TabPane";
import moment from "moment/moment";
import {cancelService, unaccessService, uncollectService} from "../service/service";
import {
    fetchInitiatorServices,
    fetchInitiatorTasks,
    fetchRecipientServices,
    fetchRecipientTasks,
    fetchCollectServices,
    fetchCollectTasks,
  updateUserProfile
} from "../service/user";

const { Title } = Typography;

const PAGE_SIZE = 10;

export default function ProfilePage() {
    const [user, setUser] = useState({});
    const { id } = useParams();
    const [activeTab, setActiveTab] = useState('1');
    const [data, setData] = useState([]);
    const [pageIndex, setPageIndex] = useState(0);
    const [editMode, setEditMode] = useState(false);
    const [editUser, setEditUser] = useState({});
    const [avatar, setAvatar] = useState(null);

    useEffect(() => {
        getSelfProfile().then(res => {
            setUser(res);
            let _editUser = {
                nickname: res.nickname,
                intro: res.intro,
                phone: res.phone,
                email: res.email,
            };
            setEditUser(_editUser);
            setAvatar(res.avatar);
        }).catch(err => {
            console.error(err);
        });
    }, []);

    useEffect(() => {
        fetchData(activeTab, pageIndex, PAGE_SIZE);
    }, [activeTab, pageIndex]);

    const fetchData = async (tabKey, pageIndex, pageSize) => {
        try {
            let responseData;
            switch (tabKey) {
                case '1':
                    responseData = await fetchInitiatorTasks(pageSize, pageIndex);
                    break;
                case '2':
                    responseData = await fetchRecipientTasks(pageSize, pageIndex);
                    break;
                case '3':
                    responseData = await fetchInitiatorServices(pageSize, pageIndex);
                    break;
                case '4':
                    responseData = await fetchRecipientServices(pageSize, pageIndex);
                    break;
                case '5':
                    responseData = await fetchCollectTasks(pageSize, pageIndex);
                    break;
                case '6':
                    responseData = await fetchCollectServices(pageSize, pageIndex);
                default:
                    responseData = await fetchInitiatorTasks(pageSize, pageIndex);
            }
            if (Array.isArray(responseData.items)) {
                setData(responseData.items);
            } else {
                setData([]);
                console.error("Fetched data is not an array:", responseData);
            }
        } catch (error) {
            setData([]);
            console.error("Error fetching data:", error);
        }
    };

    const task_initiator_columns = [{
        title: '任务标题',
        dataIndex: 'title',
        key: 'title',
        render: (text, record) => <Link to={`/task/${record.taskId}`}>{text}</Link>,
    }, {
        title: '发起时间',
        dataIndex: 'createdAt',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    }, {
        title: '已接受人数',
        dataIndex: 'accessedNumber',
        key: 'accepted',
        render: (text, record) => <>{record.accessingNumber + record.succeedNumber + record.failedNumber} / {record.maxAccess}</>,
    },{
        title: '待确认人数',
        dataIndex: 'accessingNumber',
        key: 'accepted',
        render: (text, record) => <>{record.accessingNumber}</>,
    }, {
        title: '操作',
        key: 'action',
        render: (text, record) => (
          <Button type="primary" onClick={() => cancelTask(record.taskId).then(res => {
              // 删除本条记录
              message.success('取消任务成功');
              setData(data.filter(item => item.taskId !== record.taskId));
          })}>取消</Button>
        ),
    }, {
        title: '操作',
        key: 'action', // 跳转到select_page
        render: (text, record) => <Link to={`/select/task/${record.taskId}`}>选择接取人</Link>,

    }];

    const task_recipient_columns = [{
        title: '任务标题',
        dataIndex: 'title',
        key: 'title',
        render: (text, record) => <Link to={`/task/${record.taskId}`}>{text}</Link>,
    }, {
        title: '发起时间',
        dataIndex: 'createdAt',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    }, {
        title: '操作',
        key: 'action',
        render: (text, record) => (
            <Button type="primary" onClick={() => unaccessTask(record.taskId).then(res => {
                // 删除本条记录
                Modal.success({
                    title: '取消接取成功',
                    content: '取消接取成功',
                });
                setData(data.filter(item => item.taskId !== record.taskId));
            }
            )}>取消接取</Button>
        ),
    }];

    const service_initiator_columns = [{
        title: '任务标题',
        dataIndex: 'title',
        key: 'title',
        render: (text, record) => <Link to={`/service/${record.serviceId}`}>{text}</Link>,
    }, {
        title: '发起时间',
        dataIndex: 'createdAt',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    }, {
        title: '已接受人数',
        dataIndex: 'accessedNumber',
        key: 'accepted',
        render: (text, record) => <>{record.accessingNumber + record.succeedNumber + record.failedNumber} / {record.maxAccess}</>,
    }, {
        title: '待确认人数',
        dataIndex: 'accessingNumber',
        key: 'accepted',
        render: (text, record) => <>{record.accessingNumber}</>,
    }, {
        title: '操作',
        key: 'action',
        render: (text, record) => (
            <Button type="primary" onClick={() => cancelService(record.serviceId).then(res => {
                // 删除本条记录
                message.success('取消服务成功');
                setData(data.filter(item => item.serviceId !== record.serviceId));
            })}>取消</Button>
        ),
    }, {
        title: '操作',
        key: 'action', // 跳转到select_page
        render: (text, record) => <Link to={`/select/service/${record.serviceId}`}>选择接取人</Link>,
    }];

    const service_recipient_columns = [{
        title: '任务标题',
        dataIndex: 'title',
        key: 'title',
        render: (text, record) => <Link to={`/service/${record.serviceId}`}>{text}</Link>,
    }, {
        title: '发起时间',
        dataIndex: 'createdAt',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    }, {
        title: '操作',
        key: 'action',
        render: (text, record) => (
            <Button type="primary" onClick={() => unaccessService(record.serviceId).then(res => {
                    // 删除本条记录
                    Modal.success({
                        title: '取消接取成功',
                        content: '取消接取成功',
                    });
                    setData(data.filter(item => item.serviceId !== record.serviceId));
                }
            )}>取消接取</Button>
        ),
    }];

    const collect_task_columns = [{
        title: '任务标题',
        dataIndex: 'title',
        key: 'title',
        render: (text, record) => <Link to={`/task/${record.taskId}`}>{text}</Link>,
    }, {
        title: '发起时间',
        dataIndex: 'createdAt',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    }, {
        title: '操作',
        key: 'action',
        render: (text, record) => (
            <Button type="primary" onClick={() => uncollectTask(record.taskId).then(res => {
                    // 删除本条记录
                    message.success('取消收藏成功');
                    setData(data.filter(item => item.taskId !== record.taskId));
                })}>取消收藏</Button>
        ),
    }];

    const collect_service_columns = [{
        title: '任务标题',
        dataIndex: 'title',
        key: 'title',
        render: (text, record) => <Link to={`/service/${record.serviceId}`}>{text}</Link>,
    }, {
        title: '发起时间',
        dataIndex: 'createdAt',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    },{
        title: '操作',
        key: 'action',
        render: (text, record) => (
            <Button type="primary" onClick={() => uncollectService(record.serviceId).then(res => {
                    // 删除本条记录
                    message.success('取消收藏成功');
                    setData(data.filter(item => item.serviceId !== record.serviceId));
                }
            )}>取消收藏</Button>
        ),
    }];

    function getBase64(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result);
            reader.onerror = error => reject(error);
            reader.readAsDataURL(file);
        });
    }

    const handleUpdate = () => {
        let data = {
            nickname: editUser.nickname,
            avatar: avatar,
            intro: editUser.intro,
            phone: editUser.phone,
            email: editUser.email,
        };

        // 判断电话和邮箱是否合法
        if (!data.phone.match(/^1[3456789]\d{9}$/)) {
            Modal.error({
                title: '修改失败',
                content: '电话号码格式不正确',
            });
            return;
        }

        if (!data.email.match(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/)) {
            Modal.error({
                title: '修改失败',
                content: '邮箱格式不正确',
            });
            return;
        }

        updateUserProfile(data).then(res => {
            Modal.success({
                title: '修改成功',
                content: '修改成功',
            });
            setEditMode(false);
            setUser({...user, ...editUser, avatar: avatar});
        }).catch(err => {
            Modal.error({
                title: '修改失败',
                content: err,
            });
        });
    };

    return (
        <BasicLayout page='profile'>
            <div style={{ display: 'flex', alignItems: 'center', marginBottom: 24 }}>
                {
                    editMode ?
                      <Upload
                        name="avatar"
                        listType="picture-card"
                        className="avatar-uploader"
                        showUploadList={false}
                        beforeUpload={file => {
                            getBase64(file).then(base64 => {
                                setAvatar(base64)
                            });
                            return false; // 阻止自动上传
                        }}
                      >
                          {avatar ? <img src={avatar} alt="avatar" style={{ width: '100%' }} /> : <div>上传头像</div>}
                      </Upload>
                      :
                      <Avatar size={64} src={user.avatar} />
                }

                <div style={{ marginLeft: 24 }}>
                    {
                        editMode ?
                          <>
                              <Title level={4}> 昵称 </Title>
                              <Input defaultValue={user.nickname} style={{ width: 1000 }} onChange={e => setEditUser({...editUser, nickname: e.target.value})} />
                          </>
                          :
                          <Title level={2}>{user.nickname}</Title>
                    }

                    {
                        editMode ?
                          <>
                              <Title level={4}> 个人简介 </Title>
                              <Input defaultValue={user.intro} style={{ width: 1000 }} onChange={e => setEditUser({...editUser, intro: e.target.value})} />

                          </>

                          :
                          <Title level={4} type="secondary">{user.intro}</Title>
                    }

                </div>

                <div style={{ marginLeft: 24 }}></div>
                <Button type="primary" onClick={() => setEditMode(!editMode)}>{editMode ? '取消' : '修改'}</Button>
            </div>

            {editMode ? (
              <Form onFinish={handleUpdate}>
                  {/* 输入 */}
                  <Form.Item label="电话号码" name="phone">
                      <Input defaultValue={user.phone} onChange={e => setEditUser({...editUser, phone: e.target.value})} />
                  </Form.Item>
                  <Form.Item label="邮箱" name="email">
                      <Input defaultValue={user.email} onChange={e => setEditUser({...editUser, email: e.target.value})} />
                  </Form.Item>

                  <Button type="primary" htmlType="submit">
                      提交
                  </Button>
              </Form>
            ) : (
              <Descriptions title="用户信息" bordered column={4}>
                  {/*<Descriptions.Item label="用户名">{user.nickname}</Descriptions.Item>*/}
                  {/*<Descriptions.Item label="用户类型">{user.type}</Descriptions.Item>*/}
                  <Descriptions.Item label="评分">{(user.rating / 10).toFixed(1)}</Descriptions.Item>
                  <Descriptions.Item label="余额">{(user.balance / 100).toFixed(2)}元</Descriptions.Item>
                  <Descriptions.Item label="电话号码">{user.phone}</Descriptions.Item>
                  <Descriptions.Item label="邮箱">{user.email}</Descriptions.Item>
              </Descriptions>
            )}


            {/*一段占位的空白*/}
            <p style={{ margin: '30px 0', fontSize: '20px' }}>
                <br></br>
                与我相关的任务和服务
            </p>

            <Tabs defaultActiveKey="1" onChange={key => setActiveTab(key)}>
                <TabPane tab="我发布的任务" key="1">
                    <Table columns={task_initiator_columns} dataSource={data} />
                </TabPane>
                <TabPane tab="我接收的任务" key="2">
                    <Table columns={task_recipient_columns} dataSource={data} />
                </TabPane>
                <TabPane tab="我发布的服务" key="3">
                    <Table columns={service_initiator_columns} dataSource={data} />
                </TabPane>
                <TabPane tab="我接收的服务" key="4">
                    <Table columns={service_recipient_columns} dataSource={data} />
                </TabPane>
                <TabPane tab="我收藏的任务" key="5">
                    <Table columns={collect_task_columns} dataSource={data} />
                </TabPane>
                <TabPane tab="我收藏的服务" key="6">
                    <Table columns={collect_service_columns} dataSource={data} />
                </TabPane>
            </Tabs>
        </BasicLayout>
    );
}
