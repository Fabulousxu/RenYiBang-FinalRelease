import React, { useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import {Card, Avatar, Steps, Row, Col, Image, Typography, Divider, Space, Carousel, Button, message} from 'antd';
import { UserOutlined } from '@ant-design/icons';
import BasicLayout from "../component/basic_layout";
import { fetchOrderById, changeOrderStatus } from "../service/order";

const { Title, Paragraph } = Typography;
const { Step } = Steps;

const statusMap = {
  0: { text: '未付款', color: 'gray' },
  1: { text: '进行中', color: 'blue' },
  2: { text: '已完成', color: 'green' },
  3: { text: '已确认', color: 'purple' },
  4: { text: '已取消', color: 'red' },
};

export default function OrderInitiatorPage() {
  const { id } = useParams();
  const location = useLocation();
  const [order, setOrder] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const orderData = await fetchOrderById(id);
        setOrder(orderData);
      } catch (error) {
        message.error("Error fetching order data:", error);
      }
    };

    fetchData();
  }, [id]);

  const handleConfirm = async () => {
    try {
      await changeOrderStatus(id, 3); // 已完成
      setOrder({ ...order, status: 2 });
    } catch (error) {
      console.error(error);
    }
  };

  const handleCancel = async () => {
    try {
      await changeOrderStatus(id, 4); // 已取消
      setOrder({ ...order, status: 4 });
    } catch (error) {
      console.error(error);
    }
  };

  const handlePay = async () => {
    try {
      await changeOrderStatus(id, 1); // 进行中
      setOrder({ ...order, status: 1 });
    } catch (error) {
      console.error(error);
    }
  }

  if (!order) {
    return (
      <BasicLayout page='order-detail'>
        <Card style={{ margin: '20px' }}>
          <h1>订单未找到</h1>
        </Card>
      </BasicLayout>
    );
  }

  // const statusStep = getStatusStep(order.status);

  return (
    <BasicLayout>
      <Row gutter={16}>
        <Col span={9}>
          <Carousel autoplay>
            {order.order_img && order.order_img.map((imageUrl, index) => (
              <div key={index}>
                <Image
                  src={imageUrl}
                  alt={`Order image ${index + 1}`}
                  style={{ width: '100%', height: 'auto' }}
                />
              </div>
            ))}
          </Carousel>
        </Col>
        <Col span={15}>
          <Typography>
            <Title level={3}>{order.name}</Title>
            <Divider orientation="left">基本信息</Divider>
            <Space direction="vertical" size="middle">
              <Row align="middle">
                <Col>
                  <Avatar icon={<UserOutlined />} src={`path_to_initiator_avatar/${order.initiator_img}.png`} style={{ marginRight: 8 }} />
                  {`发起人：${order.initiator_name}`}
                </Col>
                <Col>
                  <Avatar icon={<UserOutlined />} src={`path_to_receiver_avatar/${order.receiver_img}.png`} style={{ marginRight: 8, marginLeft: 16 }} />
                  {`接收人：${order.recipient_name}`}
                </Col>
              </Row>
              <Divider type="vertical" />
              {`发起时间：${order.time}`}
            </Space>
            <Divider orientation="left">任务描述</Divider>
            <Paragraph>
              {order.description}
            </Paragraph>
            <Divider orientation="left">当前状态</Divider>
            <Steps current={order.status} size="small">
              <Step title={statusMap[0].text} />
              <Step title={statusMap[1].text} />
              <Step title={statusMap[2].text} />
              {order.status <= 3 ? <Step title={statusMap[3].text} /> : <Step title={statusMap[4].text} />}
            </Steps>
          </Typography>
          {(order.status === 0) ? (
            <div>
              <Button type="primary" onClick={() => handlePay(order.id)} style={{ margin: '20px' }}>支付订单</Button>
              <Button type="danger" onClick={() => handleCancel(order.id)} style={{ margin: '20px' }}>取消订单</Button>
            </div>
          ) : null}
          {(order.status === 1) ? (
            <div>
              等待对方完成...
            </div>
          ) : null}
          {(order.status === 2) ? (
            <div>
              <Button type="primary" onClick={() => handleConfirm(order.id)} style={{ margin: '20px' }}>确认完成</Button>
              <Button type="danger" onClick={() => handleCancel(order.id)} style={{ margin: '20px' }}>发起退款</Button>
            </div>
          ) : null}
          {(order.status === 3) ? (
            <div>
              您的订单已结束！
            </div>
          ) : null}
          {(order.status === 4) ? (
            <div>
              订单已取消！
            </div>
          ) : null}
        </Col>
      </Row>
    </BasicLayout>
  );
}
