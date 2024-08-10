import {
  Card, Col, Row, Avatar, Divider, Pagination, Button, Slider, InputNumber, DatePicker, Radio
} from "antd";
import Meta from "antd/es/card/Meta";
import Search from "antd/es/input/Search";
import {Link} from "react-router-dom";
import {StarFilled, StarOutlined} from "@ant-design/icons";
import {useState} from "react";

export const totalEntry = 24;

export default function ItemList(props) {
  const [orderValue, setOrderValue] = useState('time')
  const [priceRange, setPriceRange] = useState([0, 100])
  return (<div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
    <Search
      placeholder={props.placeholder}
      allowClear
      enterButton='搜索'
      size='large'
      style={{width: '60%'}}
      onSearch={props.onSearch}
    />
    <Row style={{
      width: '60%', alignItems: 'center', justifyContent: 'center', margin: '20px auto 5px'
    }}>
      <Col style={{marginRight: '20px'}}>价格筛选:</Col>
      <Col style={{flexGrow: 1}}>
        <Slider
          range
          marks={{0: 0, 100: '2000+'}}
          value={priceRange}
          onChange={value => {
            setPriceRange(value)
            props.onChangePriceRange([value[0] * 2000, value[1] === 100 ? -1 : value[1] * 2000])
          }}
          tooltip={{formatter: null}}
          style={{width: '100%'}}
        />
      </Col>
      <Col style={{marginLeft: '40px'}}>
        ¥<InputNumber
        style={{margin: '0 5px'}}
        value={priceRange[0] * 20}
        onPressEnter={e => {
          let value = e.target.value
          if (value === '') value = 0
          setPriceRange([value / 20, priceRange[1]])
          props.onChangePriceRange([value * 100, priceRange[1] * 2000])
        }}
      />~<InputNumber
        style={{margin: '0 5px'}}
        value={priceRange[1] * 20}
        onPressEnter={e => {
          let value = e.target.value
          if (value === '') value = 2000
          setPriceRange([priceRange[0], value / 20])
          props.onChangePriceRange([priceRange[0] * 2000, value === 2000 ? -1 : value * 100])
        }}
      />
      </Col>
    </Row>
    <Row style={{width: '60%', alignItems: 'center', justifyContent: 'center', margin: '5px auto'}}>
      <Col style={{marginRight: '10px'}}>时间筛选:</Col>
      <Col style={{flexGrow: 1}}>
        <DatePicker.RangePicker
          showTime={{format: 'HH:mm:ss'}}
          format='YYYY-MM-DD HH:mm:ss'
          placeholder={['开始时间', '结束时间']}
          style={{width: '100%'}}
          onChange={(dates, dataStrings) => props.onChangeTimeRange(dataStrings)}
        />
      </Col>
    </Row>
    <Row
      style={{width: '60%', alignItems: 'center', justifyContent: 'center', margin: '10px auto 0'}}>
      <Radio.Group onChange={e => {
        setOrderValue(e.target.value)
        props.onChangeOrder(e.target.value)
      }} value={orderValue}>
        <Radio value='time'>按时间排序</Radio>
        <Radio value='rating'>按评分排序</Radio>
      </Radio.Group>
    </Row>
    <h3 style={{margin: '0 auto 0 10px'}}>{props.title}</h3>
    <Divider/>
    <Row gutter={[32, 24]} style={{width: '100%'}}>
      {Array.from({length: Math.min(totalEntry, props.list?.length)}).map((_, index) => (<Col
        key={index}
        xs={{span: 24 / 2}}
        sm={{span: 24 / 3}}
        md={{span: 24 / 3}}
        lg={{span: 24 / 4}}
        xl={{span: 24 / 4}}
      >
        <Link to={props.list[index]?.url}>
          <Card
            hoverable
            title={<div style={{
              display: 'flex', flexDirection: 'column'
            }}>
              <h1 style={{
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap',
                fontSize: '1rem',
                marginBottom: '2px'
              }}>{props.list[index]?.title}</h1>
              <h1 style={{color: 'red', fontSize: '1rem', marginTop: '2px'}}>
                ¥{(props.list[index]?.price / 100).toFixed(2)}
              </h1>
            </div>}
            cover={<div
              style={{
                position: 'relative', width: '100%', paddingBottom: '100%'
              }}
            >
              <img
                alt='封面'
                src={props.list[index]?.cover}
                style={{
                  position: 'absolute',
                  top: 0,
                  left: 0,
                  width: '100%',
                  height: '100%',
                  objectFit: 'cover'
                }}
              />
            </div>}
            actions={[<div>{props.ratingTitle}{(props.list[index]?.rating / 10).toFixed(1)}</div>,
              <Button
                icon={props.list[index]?.collected ? <StarFilled/> : <StarOutlined/>}
                size='middle'
                onClick={e => {
                  e.preventDefault()
                  props.onCollect(index)
                }}
                style={{border: 'none', height: 'auto', borderRadius: '100%'}}/>]}
          >
            <Row style={{alignItems: 'center'}}>
              <Col>
                <Link to={`/profile/${props.list[index]?.owner.userId}`}>
                  <Meta
                    avatar={<Avatar src={props.list[index]?.owner.avatar}/>}
                    title={props.list[index]?.owner.nickname}
                    description={`帮帮评分: ${(props.list[index]?.owner.rating / 10).toFixed(1)}`}
                  />
                </Link>
              </Col>
            </Row>
          </Card>
        </Link>
      </Col>))}
    </Row>
    <Pagination
      pageSize={totalEntry}
      total={props.total}
      current={props.currentPage}
      showSizeChanger={false}
      style={{marginTop: '24px'}}
      onChange={props.onChange}
    />
  </div>)
}