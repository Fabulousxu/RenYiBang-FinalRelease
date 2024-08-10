import React, { useState, useEffect } from 'react';
import BasicLayout from "../component/basic_layout";
import {Table, Tag, Tabs, message} from 'antd';
import { Link } from 'react-router-dom';
import moment from 'moment';
import { fetchInitiatorTasks, fetchRecipientTasks, fetchInitiatorServices, fetchRecipientServices } from '../service/order';

const { TabPane } = Tabs;

const statusMap = {
    0: { text: '未付款', color: 'gray' },
    1: { text: '进行中', color: 'blue' },
    2: { text: '已完成', color: 'green' },
    3: { text: '已确认', color: 'purple' },
    4: { text: '已取消', color: 'red' },
};

export default function OrderPage() {
    const [activeTab, setActiveTab] = useState('1');
    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                let responseData;
                switch (activeTab) {
                    case '1':
                        responseData = await fetchInitiatorTasks();
                        break;
                    case '2':
                        responseData = await fetchRecipientTasks();
                        break;
                    case '3':
                        responseData = await fetchInitiatorServices();
                        break;
                    case '4':
                        responseData = await fetchRecipientServices();
                        break;
                    default:
                        responseData = await fetchInitiatorTasks();
                }
                setData(responseData);
            } catch (error) {
                message.error(error);
            }
        };

        fetchData();
    }, [activeTab]);

    const generateLink = (record) => {
        switch (activeTab) {
            case '1':
                return `/order/task/initiator/${record.id}`;
            case '2':
                return `/order/task/recipient/${record.id}`;
            case '3':
                return `/order/service/initiator/${record.id}`;
            case '4':
                return `/order/service/recipient/${record.id}`;
            default:
                return `/order/${record.id}`;
        }
    };

    const columns = [{
        title: '任务标题',
        dataIndex: 'name',
        key: 'title',
        render: (text, record) => <Link to={generateLink(record)}>{text}</Link>,
    }, {
        title: '发起人', dataIndex: 'initiator', key: 'initiator',
    }, {
        title: '接收人', dataIndex: 'recipient', key: 'receiver',
    }, {
        title: '发起时间',
        dataIndex: 'time',
        key: 'time',
        sorter: (a, b) => moment(a.startTime).unix() - moment(b.startTime).unix(),
    }, {
        title: '当前状态',
        dataIndex: 'status',
        key: 'status',
        filters: [
            { text: '未付款', value: 0 },
            { text: '进行中', value: 1 },
            { text: '已完成', value: 2 },
            { text: '已确认', value: 3 },
            { text: '已取消', value: 4 },
        ],
        onFilter: (value, record) => record.status === value,
        render: (status) => {
            const { text, color } = statusMap[status] || { text: '未知', color: 'gray' };
            return <Tag color={color}>{text}</Tag>;
        },
    },];

    return (
      <BasicLayout page='order'>
          <Tabs defaultActiveKey="1" onChange={setActiveTab}>
              <TabPane tab="我发布的任务" key="1">
                  <Table columns={columns} dataSource={data} />
              </TabPane>
              <TabPane tab="我接收的任务" key="2">
                  <Table columns={columns} dataSource={data} />
              </TabPane>
              <TabPane tab="我发布的服务" key="3">
                  <Table columns={columns} dataSource={data} />
              </TabPane>
              <TabPane tab="我接收的服务" key="4">
                  <Table columns={columns} dataSource={data} />
              </TabPane>
          </Tabs>
      </BasicLayout>
    );
}
