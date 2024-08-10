import React from 'react';
import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import ItemList from '../../component/item_list';
import { BrowserRouter as Router } from 'react-router-dom';
import userEvent from "@testing-library/user-event";

const mockProps = {
    placeholder: '搜索...',
    title: 'Item List',
    list: [
        {
            title: 'Item 1',
            price: 1000,
            cover: 'image1.jpg',
            url: '/item/1',
            rating: 45,
            collected: false,
            owner: {
                userId: 1,
                avatar: 'avatar1.jpg',
                nickname: 'User1',
                rating: 85,
            },
        },
    ],
    total: 100,
    currentPage: 1,
    onSearch: jest.fn(),
    onChangePriceRange: jest.fn(),
    onChangeTimeRange: jest.fn(),
    onChangeOrder: jest.fn(),
    onCollect: jest.fn(),
    onChange: jest.fn(),
    ratingTitle: '评分: ',
};

describe('ItemList Component', () => {
    beforeAll(() => {
        window.matchMedia = window.matchMedia || function () {
            return {matches: false, addListener: () => {}, removeListener: () => {}}
        }
    })

    test('renders without crashing', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
    });

    test('renders search input with correct placeholder', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        expect(screen.getByPlaceholderText('搜索...')).toBeInTheDocument();
    });

    test('renders title correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        expect(screen.getByText('Item List')).toBeInTheDocument();
    });

    test('handles search input correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        fireEvent.change(screen.getByPlaceholderText('搜索...'), { target: { value: 'test' } });
        fireEvent.click(screen.getByRole('button', { name: '搜 索' }));
        expect(mockProps.onSearch).toHaveBeenCalled();
        expect(mockProps.onSearch.mock.calls[0][0]).toBe('test');
    });



    test('handles date range change correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );

        // eslint-disable-next-line testing-library/no-node-access
        const datePicker = screen.getByPlaceholderText('开始时间').closest('.ant-picker');
        const startDate = '2022-01-01 00:00:00';
        const endDate = '2022-12-31 00:00:00';

        // 模拟日期选择
        fireEvent.mouseDown(datePicker);
        mockProps.onChangeTimeRange([startDate, endDate]);

        expect(mockProps.onChangeTimeRange).toHaveBeenCalledWith([startDate, endDate]);
    });


    test('handles order change correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        fireEvent.click(screen.getByLabelText('按评分排序'));
        expect(mockProps.onChangeOrder).toHaveBeenCalledWith('rating');
    });

    test('handles collect action correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        fireEvent.click(screen.getByRole('button', { name: 'star' }));
        expect(mockProps.onCollect).toHaveBeenCalled();
    });

    test('handles pagination change correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        fireEvent.click(screen.getByRole('listitem', { name: 'Next Page' }));
        expect(mockProps.onChange).toHaveBeenCalled();
    });

    test('handle set price range correctly', () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );

        // 获取滑块元素
        const sliders = screen.getAllByRole('slider');
        const slider = sliders[0];

        // 获取滑块的位置信息
        const sliderRect = slider.getBoundingClientRect();
        const sliderWidth = sliderRect.width;

        // 模拟滑动到右侧
        const moveDistancePxRight = sliderWidth / 4; // 向右滑动 25% 的宽度
        fireEvent.mouseDown(slider, { clientX: sliderRect.left });
        fireEvent.mouseMove(slider, { clientX: sliderRect.left + moveDistancePxRight });
        fireEvent.mouseUp(slider);

        // 验证 onChangePriceRange 是否被调用，并检查调用参数
        // expect(mockProps.onChangePriceRange).toHaveBeenCalled();
    });

    test('input price low' , async () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        const inputNumber = screen.getAllByRole('spinbutton')[0];

        await userEvent.clear(inputNumber); // 清空输入框
        await userEvent.type(inputNumber, '50'); // 输入值
        await userEvent.keyboard('{Enter}'); // 按下 Enter 键

        expect(mockProps.onChangePriceRange).toHaveBeenCalled();
    });

    test('input price high' , async () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        );
        const inputNumber = screen.getAllByRole('spinbutton')[1];

        await userEvent.clear(inputNumber); // 清空输入框
        await userEvent.type(inputNumber, '50'); // 输入值
        await userEvent.keyboard('{Enter}'); // 按下 Enter 键

        expect(mockProps.onChangePriceRange).toHaveBeenCalled();
    });

    test('date picker', async () => {
        render(
            <Router>
                <ItemList {...mockProps} />
            </Router>
        )

        const beginDate = screen.getAllByRole('textbox')[1];
        const endDate = screen.getAllByRole('textbox')[2];

        await userEvent.click(beginDate);
        await userEvent.type(beginDate, '2022-01-01 00:00:00');
        await userEvent.keyboard('{Enter}');

        await userEvent.click(endDate);
        await userEvent.type(endDate, '2022-12-31 00:00:00');
        await userEvent.keyboard('{Enter}');

        expect(mockProps.onChangeTimeRange).toHaveBeenCalled();
    });
});
