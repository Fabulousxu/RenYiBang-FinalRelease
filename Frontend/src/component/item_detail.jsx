import {Avatar, Card, Carousel, Col, Divider, Image, Rate, Row} from "antd";
import React from "react";
import {Link} from "react-router-dom";
import Meta from "antd/es/card/Meta";

export default function ItemDetail(props) {
  return (<Row gutter={16}>
    <Col>
      <Carousel style={{width: '400px', height: '400px'}}>
        {props.detail?.images.map((src, index) => (<div
          key={index}
          style={{width: '400px', height: '400px', overflow: 'hidden'}}
        >
          <Image
            src={src}
            style={{width: '400px', height: '400px', objectFit: 'cover'}}
            alt='图片'/>
        </div>))}
      </Carousel>
    </Col>
    <Col style={{flexGrow: 1}}>
      <Card
        title={<div style={{
          display: 'flex', flexDirection: 'column'
        }}>
          <h1 style={{
            overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: '2rem'
          }}>{props.detail?.title}</h1>
          <Row style={{alignItems: 'center', width: '60%'}}>
            <Col style={{
              color: 'red', fontSize: '1.5rem'
            }}> ¥{(props.detail?.price / 100).toFixed(2)}</Col>
            <Col style={{marginLeft: 'auto'}}>
              <Row>
                <Col style={{marginRight: '20px'}}>{props.ratingTitle}</Col>
                <Col> <Rate disabled allowHalf value={props.detail?.rating / 20}/></Col>
              </Row>
            </Col>
          </Row>
        </div>}
        style={{border: 'none'}}
      >
        <Link to={`/profile/${props.detail?.owner.userId}`}>
          <Meta
            avatar={<Avatar src={props.detail?.owner.avatar}/>}
            title={props.detail?.owner.nickname}
            description={`帮帮评分: ${(props.detail?.owner.rating / 10).toFixed(1)}`}
          />
        </Link>
      </Card>
      <Divider>{props.descriptionTitle}</Divider>
      <p style={{padding: '12px 24px'}}>{props.detail?.description}</p>
    </Col>
  </Row>)
}