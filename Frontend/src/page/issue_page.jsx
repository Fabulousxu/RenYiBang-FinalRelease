import { Button, Input, Upload, Image, Radio, Modal, InputNumber } from 'antd';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { PlusOutlined } from '@ant-design/icons';
import BasicLayout from '../component/basic_layout';
import { issueService, issueTask } from '../service/issue';


export default function IssuePage() {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState(0);
  const [maxAccess, setMaxAccess] = useState(0);
  const [radioValue, setRadioValue] = useState(1);
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState('');
  const [fileList, setFileList] = useState([]);
  const navigate = useNavigate();

  const beforeUpload = (file) => {  
    // 阻止文件自动上传
    console.log(fileList);
    return false;
  };

  const getBase64 = (img) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.addEventListener('load', () => resolve(reader.result));
      reader.addEventListener('error', reject);
      reader.readAsDataURL(img);
    });
  };


  const handlePreview = async (file) => {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj);
    }
    setPreviewImage(file.url || file.preview);
    setPreviewOpen(true);
  };
  const handleChange = ({ fileList: newFileList }) => setFileList(newFileList);
  const uploadButton = (
    <button
      style={{
        border: 0,
        background: 'none',
      }}
      type="button"
    >
      <PlusOutlined />
      <div
        style={{
          marginTop: 8,
        }}
      >
        Upload
      </div>
    </button>
  );
  //上传图片界面设置

  const onRadioChange = (e) => {
    setRadioValue(e.target.value);
  };
  //单选框设置

  const getAllPictures = async () => {
    const promises = fileList.map((file) => getBase64(file.originFileObj));
    const base64List = await Promise.all(promises);
    return base64List;
  };

  const handleSubmit = () => { 
    if(!title || !description || !price || !maxAccess || fileList.length === 0){
      Modal.error({
        title: '请填写完整信息',
        content: '请填写完整信息',
      });
      return;
    }

    getAllPictures().then(Base64List => {

      console.log(Base64List);

      let newitem = {
        title: title,
        description: description,
        price: price * 100,
        images: Base64List,
        maxAccess: maxAccess,
      }

      if(radioValue === 1){
        issueTask(newitem).then(res => {
          Modal.success({
            title: '发布成功',
            content: '发布成功',
            onOk: () => {
              navigate('/task');
            }
          });
        }).catch(err => {
          Modal.error({
            title: '发布失败',
            content: err,
          });

        })
      }
      else if(radioValue === 2){
        issueService(newitem).then(res => {
          Modal.success({
            title: '发布成功',
            content: '发布成功',
            onOk: () => {
              navigate('/service');
            }
          });
        }).catch(err => {
          Modal.error({
            title: '发布失败',
            content: err,
          });
        })
      }

    });
  }

  return(
    <BasicLayout page='issue'>
      <h1>发布一个新的内容</h1>
      <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
        <div style={{ width: '40%', display: 'flex', 'flex-direction': 'column' }}>
          <h3>请上传预览照片，数量不超过8张</h3>
          <Upload
          beforeUpload={beforeUpload}
          listType="picture-card"
          fileList={fileList}
          onPreview={handlePreview}
          onChange={handleChange}
          onRemove={(file) => {
            setFileList(curr => curr.filter(item => item.uid !== file.uid));
          }}
          >
          {fileList.length >= 8 ? null : uploadButton}
          </Upload>
          {previewImage && (
          <Image
            wrapperStyle={{
            display: 'none',
            }}
            preview={{
            visible: previewOpen,
            onVisibleChange: (visible) => setPreviewOpen(visible),
            afterOpenChange: (visible) => !visible && setPreviewImage(''),
            }}
            src={previewImage}
          />
          )}
        </div>
        <div style={{ width: '40%', display: 'flex', 'flex-direction': 'column' }}>
          <h3 style={{ margin: '20px' }}>请填写标题</h3>
          <Input placeholder="请输入标题" style={{ margin: '20px', width: '80%' }} size='large' value={title} onChange={e => setTitle(e.target.value)}/>
          <h3 style={{ margin: '20px' }}>请选择发布类型</h3>
          <Radio.Group onChange={onRadioChange} value={radioValue} style={{ margin: '20px', width: '80%' }}>
            <Radio value={1}>任务</Radio>
            <Radio value={2}>服务</Radio>
          </Radio.Group>
          <h3 style={{ margin: '20px' }}>请填写描述</h3>
          <Input.TextArea placeholder="请输入描述" style={{ margin: '20px', width: '80%' }} value={description} onChange={e => setDescription(e.target.value)}/>
          <h3 style={{ margin: '20px' }}>请填写价格</h3>
          <InputNumber placeholder="初步定价" addonBefore="￥" style={{ margin: '20px', width: '80%' }} value={price} onChange={value => setPrice(value)}/>
          <h3 style={{ margin: '20px' }}>请填写最大接单数</h3>
          <InputNumber placeholder="最大接单数" style={{ margin: '20px', width: '80%' }} value={maxAccess} onChange={value => setMaxAccess(value)}/>
          <Button type="primary" style={{ margin: '20px', width: '100px' }} onClick={handleSubmit}>提交</Button>
        </div>
      </div>
    </BasicLayout>
  )
}