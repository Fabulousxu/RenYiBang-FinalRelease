import React, { useState, useEffect } from 'react';
import { Upload, Button, message } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

const ImageUploader = ({ onImageUpload, initialImageUrl }) => {
    const [imageUrl, setImageUrl] = useState(initialImageUrl);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setImageUrl(initialImageUrl);
    }, [initialImageUrl]);

    const beforeUpload = (file) => {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('You can only upload JPG/PNG file!');
            return false;
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
            message.error('Image must smaller than 2MB!');
            return false;
        }
        return true;
    };

    const getBase64 = (img, callback) => {
        const reader = new FileReader();
        reader.addEventListener('load', () => callback(reader.result));
        reader.readAsDataURL(img);
    };

    const handleChange = info => {
        if (info.file.status === 'uploading') {
            setLoading(true);
            return;
        }
        if (info.file.status === 'done' || info.file.status === 'removed') {
            getBase64(info.file.originFileObj, imageUrl => {
                setLoading(false);
                setImageUrl(imageUrl);
                onImageUpload(imageUrl);
            });
        }
    };

    const customRequest = ({ file, onSuccess }) => {
        getBase64(file, imageUrl => {
            setLoading(false);
            setImageUrl(imageUrl);
            onSuccess("ok");
            onImageUpload(imageUrl);
        });
    };

    return (
        <Upload
            name="avatar"
            listType="picture-card"
            className="avatar-uploader"
            showUploadList={false}
            beforeUpload={beforeUpload}
            customRequest={customRequest}
            onChange={handleChange}
        >
            {imageUrl ? <img src={imageUrl} alt="avatar" style={{ width: '100%' }} /> : <Button icon={<UploadOutlined />} loading={loading}>Upload</Button>}
        </Upload>
    );
};

export default ImageUploader;
