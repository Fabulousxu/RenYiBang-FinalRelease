import React, { useState } from 'react';
import { Image } from 'antd';

const ImageGallery = ({ images }) => {
  // 状态管理当前选中的图片索引，默认为0
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  // 切换图片的函数
  const selectImage = (index) => {
    setSelectedImageIndex(index);
  };

  // 渲染大图
  const renderMainImage = () => (
    <Image
      src={images[selectedImageIndex]}
      style={{ width: '200%', maxHeight: '1000px', objectFit: 'cover' }}
    />
  );

  // 渲染缩略图
  const renderThumbnails = () => (
    <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', marginTop: '10px', overflowX: 'auto', overflowY: 'hidden' }}>
      {images.map((src, index) => (
        <Image
          key={src}
          src={src}
          style={{
            width: '100px',
            cursor: 'pointer',
            border: index === selectedImageIndex ? '2px solid blue' : '',
          }}
          onClick={() => selectImage(index)}
        />
      ))}
    </div>
  );

  return (
    <div>
      {renderMainImage()}
      {renderThumbnails()}
    </div>
  );
};

export default ImageGallery;