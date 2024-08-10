import {useState} from 'react';
import {Link} from "react-router-dom";
import {Layout, Menu, theme} from 'antd';
import {Content, Header, Footer} from "antd/es/layout/layout";
import {
  AccountBookOutlined,
  MessageOutlined,
  PayCircleOutlined,
  QuestionCircleOutlined,
  TeamOutlined,
  ThunderboltOutlined,
  UserOutlined,
  PlusCircleOutlined
} from "@ant-design/icons";

export default function BasicLayout(props) {
  const items = [{
    key: 'task', label: <Link to='/task'>任务大厅</Link>, icon: <AccountBookOutlined/>
  }, {
    key: 'service', label: <Link to='/service'>服务大厅</Link>, icon: <ThunderboltOutlined/>
  }, {
    key: 'issue', label: <Link to='/issue'>发布内容</Link>, icon: <PlusCircleOutlined/>,
  }, {
    key: 'order',
    label: <Link to='/order'>订单</Link>,
    icon: <PayCircleOutlined/>,
    style: {marginLeft: 'auto'}
  }, {
    key: 'message', label: <Link to='/message'>消息</Link>, icon: <MessageOutlined/>
  }, {
    key: 'profile', label: <Link to='/profile/self'>用户</Link>, icon: <UserOutlined/>
  }]
  const {token: {colorBgContainer, borderRadiusLG}} = theme.useToken();
  const [current, setCurrent] = useState(props.page)
  const onClick = e => setCurrent(e.key)
  return (<Layout>
    <Header
      style={{
        position: 'fixed',
        zIndex: '100',
        width: '100%',
        height: '4rem',
        display: 'flex',
        alignItems: 'center',
        backgroundColor: 'white',
        whiteSpace: 'nowrap'
      }}
    >
      <h1 style={{marginRight: '10%', fontSize: '1.5rem'}}>
        <TeamOutlined/><PayCircleOutlined/><TeamOutlined/> 任易帮
      </h1>
      <Menu
        onClick={onClick}
        selectedKeys={[current]}
        mode='horizontal'
        items={items}
        style={{backgroundColor: 'transparent', flexGrow: 1}}
      />
    </Header>
    <Content
      style={{
        margin: '6rem 6% 20px',
        padding: '24px 48px',
        minHeight: 'calc(100vh - 10rem - 24px)',
        background: colorBgContainer,
        borderRadius: borderRadiusLG,
      }}
    >{props.children}</Content>
    <Footer style={{textAlign: 'center'}}>
      RenYiBang Design ©{new Date().getFullYear()} Created by RenYiBang
    </Footer>
  </Layout>);
}