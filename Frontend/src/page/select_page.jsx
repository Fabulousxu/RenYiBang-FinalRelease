import React from "react";
import {Table, Image, Button, Modal} from "antd";
import { useState, useEffect } from "react";
import { useLocation, useParams } from "react-router-dom";
import BasicLayout from "../component/basic_layout";
import ImageGallery from "../component/image_gallary";
import { getTask, getTaskSelectInfo, confirmSelectTask } from "../service/task";
import { getService, getServiceSelectInfo, confirmSelectService } from "../service/service";
import useSyncCallback from "../util/useSyncCallback";
import { refuseSelectTask} from "../service/task";
import { refuseSelectService } from "../service/service";
import { getServiceSuccessPeople, getServiceRefusePeople } from "../service/service";
import { getTaskSuccessPeople, getTaskRefusePeople } from "../service/task";

export default function SelectPage() {
    const [selectedperson, setSelectedPerson] = useState();
    const [task, setTask] = useState({});
    const [flag, setFlag] = useState(false);
    const [pageIndex, setPageIndex] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [total, setTotal] = useState(0);
    const location = useLocation();
    const isTask = location.pathname.includes("task");
    const isService = location.pathname.includes("service");
    const [proposers, setProposers] = useState([]);
    let { id } = useParams();
    const [succeedPeople, setSucceedPeople] = useState([]);
    const [refusePeople, setRefusePeople] = useState([]);

    const handleClick = () => {
        // 提取selectedperson的userId，组成新数组
        let userList = selectedperson.map(person => person.userId);

        if (isTask) {
            if (userList) {
                confirmSelectTask(id, userList).then(res => {
                    setFlag(true);

                    Modal.success({
                        title: "生成订单成功",
                        content: "请在订单页面查看",
                        onOk: () => {
                            window.location.href = "/order";
                        }

                    });
                });
            }
        }else{
            if (userList) {
                confirmSelectService(id, userList).then(res => {
                    setFlag(true);

                    Modal.success({
                        title: "生成订单成功",
                        content: "请在订单页面查看",
                        onOk: () => {
                            window.location.href = "/order";
                        }
                    });
                });
            }
        }
    }

    const handleRefuse = () => {
        // 提取selectedperson的userId，组成新数组
        let userList = selectedperson.map(person => person.userId);

        if (isTask) {
            if (userList) {
                refuseSelectTask(id, userList).then(res => {
                    setFlag(true);

                    Modal.success({
                        title: "拒绝成功",
                        content: "拒绝成功",
                    });
                });
            }
        }else {
            if (userList) {
                refuseSelectService(id, userList).then(res => {
                    setFlag(true);

                    Modal.success({
                        title: "拒绝成功",
                        content: "拒绝成功",
                    });
                });
            }
        }
    }

    const fetchdata = async (pageIndex, pageSize) => {
        if (isTask) {
            getTask(id).then(res => {
                setTask(res);
            });
            getTaskSelectInfo(id, pageSize, pageIndex).then(res => {
                setTotal(res.total);
                setProposers(res.items);
            })
            getTaskSuccessPeople(id, pageSize, pageIndex).then(res => {
                setSucceedPeople(res.items);
            });
            getTaskRefusePeople(id, pageSize, pageIndex).then(res => {
                setRefusePeople(res.items);
            });
        } else if (isService) {
            getService(id).then(res => {
                setTask(res);
            });
            getServiceSelectInfo(id, pageSize, pageIndex).then(res => {
                setTotal(res.total);
                setProposers(res.items);
            });
            getServiceSuccessPeople(id, pageSize, pageIndex).then(res => {
                setSucceedPeople(res.items);
            });
            getServiceRefusePeople(id, pageSize, pageIndex).then(res => {
                setRefusePeople(res.items);
            });
        }
    };

    const syncconsole = useSyncCallback(() => {
        console.log(task);
    });

    useEffect(() => {
        fetchdata(pageIndex, pageSize);
        // syncconsole();
    }, [pageIndex, pageSize, flag]);

    const handleTableChange = (pagination) => {
        setPageIndex(pagination.current - 1);
        setPageSize(pagination.pageSize);
    };

    const columns = [
        {
            title: "用户名",
            dataIndex: "nickname",
            render: (text) => <div>{text}</div>,
        },
        {
            title: "头像",
            dataIndex: "avatar",
            render: (text) => <Image src={text} width={50} />,
        },
        {
            title: "评分",
            dataIndex: "rating",
            render: (text) => <div>{text}</div>,
        },
    ];

    return (
        <BasicLayout>
            <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
                <ImageGallery
                    images={task && Array.isArray(task.images) ? task.images : []}
                    style={{ width: '55%', display: 'flex', 'flex-direction': 'column', margin: '20px' }}
                />
                <div style={{ width: '40%', display: 'flex', 'flex-direction': 'column', margin: '20px' }}>
                    <h1>{task.title}</h1>
                    <h3>项目描述：</h3>
                    <p>{task.description}</p>
                    <h3>项目价格：￥{task.price / 100}</h3>

                    { succeedPeople.length > 0 && <h3>已确认用户：</h3>}
                    { succeedPeople.length > 0 && <Table
                        columns={columns}
                        dataSource={succeedPeople.map(person => ({
                            ...person,
                            key: person.userId,
                        }))}
                    />}

                    { refusePeople.length > 0 && <h3>已拒绝用户：</h3>}
                    { refusePeople.length > 0 && <Table
                        columns={columns}
                        dataSource={refusePeople.map(person => ({
                            ...person,
                            key: person.userId,
                        }))}
                    />}

                    <h3>请选择候选人：</h3>
                    <Table
                        columns={columns}
                        rowSelection={{
                            onChange: (_, selectedPerson) => {
                                setSelectedPerson(selectedPerson);
                            },
                        }}
                        dataSource={proposers.map(proposer => ({
                            ...proposer,
                            key: proposer.userId,
                        }))}
                        pagination={{
                            current: pageIndex + 1,
                            pageSize: pageSize,
                            total: total,
                            onChange: handleTableChange
                        }}
                    />
                    <div style={{ marginTop: 16 }}>
                        <Button type="primary" onClick={handleClick}>
                            确认选择
                        </Button>
                        <Button type="danger" onClick={handleRefuse}>
                            拒绝接取
                        </Button>
                    </div>
                </div>
            </div>
        </BasicLayout>
    );
}
