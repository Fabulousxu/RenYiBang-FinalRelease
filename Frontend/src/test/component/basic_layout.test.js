import React from 'react';
import '@testing-library/jest-dom';
import {render, fireEvent, screen} from "@testing-library/react";
import BasicLayout from "../../component/basic_layout";
import {BrowserRouter} from "react-router-dom";

jest.mock('antd', () => {
  return {
    ...jest.requireActual('antd'), Menu: ({onClick, selectedKeys, mode, items, style}) => (<div>
      {items.map(item => (<div
        key={item.key}
        onClick={() => onClick({key: item.key})}
      >{item.label}</div>))}
    </div>)
  }
})

describe('BasicLayout', () => {
  test('render test', () => {
    render(<BrowserRouter><BasicLayout page="task">
      <div>content</div>
    </BasicLayout></BrowserRouter>)
    expect(screen.getByText('任易帮')).toBeInTheDocument()
    expect(screen.getByText('任务大厅')).toBeInTheDocument()
    expect(screen.getByText('服务大厅')).toBeInTheDocument()
    expect(screen.getByText('发布内容')).toBeInTheDocument()
    expect(screen.getByText('订单')).toBeInTheDocument()
    expect(screen.getByText('消息')).toBeInTheDocument()
    expect(screen.getByText('用户')).toBeInTheDocument()
    expect(screen.getByText('content')).toBeInTheDocument()
  })

  test('onmenu test', () => {
    render(<BrowserRouter><BasicLayout page="task"/></BrowserRouter>)
    const serviceMenuItem = screen.getByText('服务大厅')
    fireEvent.click(serviceMenuItem)
  })
})
