import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import CommentList from '../../component/comment_list';
import userEvent from '@testing-library/user-event';
import {BrowserRouter as Router} from 'react-router-dom';
import ItemList from "../../component/item_list";
import React from "react";

describe('CommentList', () => {
    const mockOnComment = jest.fn();
    const mockOnMessage = jest.fn();
    const mockOnLike = jest.fn();
    const mockOnDelete = jest.fn();
    const mockOnChange = jest.fn();
    const mockOnChangeMode = jest.fn();
    const mockOnChangeOrder = jest.fn();

    const defaultProps = {
        list: [
            {
                user: {userId: 1, avatar: 'avatar_url', nickname: 'User1', rating: 80},
                content: 'This is a comment',
                createdAt: '2024-08-06',
                liked: false,
                likedNumber: 10,
                commenter: {userId: 1},
                messager: {userId: 1},
            }
        ],
        commentTotal: 1,
        messageTotal: 1,
        total: 100,
        currentPage: 1,
        onComment: mockOnComment,
        onMessage: mockOnMessage,
        onLike: mockOnLike,
        onDelete: mockOnDelete,
        onChange: mockOnChange,
        onChangeMode: mockOnChangeMode,
        onChangeOrder: mockOnChangeOrder,
    };

    beforeAll(
        () => {
            window.matchMedia = window.matchMedia || function () {
                return {
                    matches: false, addListener: () => {
                    }, removeListener: () => {
                    }
                };
            };

            localStorage.setItem('userId', '1');
        }
    )

    test('renders CommentList component', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>);
        expect(screen.getByText('评论 1条')).toBeInTheDocument();
        expect(screen.getByText('留言 1条')).toBeInTheDocument();
    });

    test('handles mode switch between comment and message', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>);

        const commentRadio = screen.getByLabelText('评论');
        const messageRadio = screen.getByLabelText('留言');

        // Default mode is 'comment'
        expect(commentRadio).toBeChecked();
        expect(messageRadio).not.toBeChecked();

        // Switch to 'message'
        userEvent.click(messageRadio);
        expect(messageRadio).toBeChecked();
        expect(commentRadio).not.toBeChecked();
    });

    test('handles like button click', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>);
        userEvent.click(screen.getByRole('button', {name: /like/i}));
        expect(mockOnLike).toHaveBeenCalledWith(0);
    });


    test('handles pagination change correctly', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );
        fireEvent.click(screen.getByRole('listitem', {name: 'Next Page'}));
        expect(defaultProps.onChange).toHaveBeenCalled();
    });

    test('handles delete comment button click', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        const deleteButton = screen.getAllByRole('button', {name: /delete/i});
        userEvent.click(deleteButton[0]);
        expect(mockOnDelete).toHaveBeenCalledWith(0);
    });

    test('handles delete message button click', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        const deleteButton = screen.getAllByRole('button', {name: /delete/i});
        userEvent.click(deleteButton[1]);
        expect(mockOnDelete).toHaveBeenCalledWith(0);
    });

    test('updates rating when user changes it', async () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        // Open the comment mode
        userEvent.click(screen.getByLabelText('评论'));

        // Ensure the rate component is present
        const rateComponent = screen.getByRole('radiogroup');

        // Check if the rate component has the correct number of stars (replace with actual number if necessary)
        expect(rateComponent).toBeInTheDocument();

        // Simulate changing the rating (make sure to select the correct star element)
        userEvent.click(screen.getAllByRole('radio', { name: 'star star' })[0]); // Adjust according to the rate component

        // Wait for any state updates to take effect
        await waitFor(() => {
            expect(screen.getAllByRole('radio', { name: 'star star' })[0]).toBeChecked();
        });
    });

    test('handles comment button click', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        const commentButton = screen.getByRole('button', {name: "评 论"});
        userEvent.click(commentButton);
        expect(mockOnComment).toHaveBeenCalled();
    });

    test('handles message button click', () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        const modeChange = screen.getByRole('radio', {name: '留言'});
        userEvent.click(modeChange);

        const messageButton = screen.getByRole('button', {name: "留 言"});
        userEvent.click(messageButton);
        expect(mockOnMessage).toHaveBeenCalled();
    });

    test('handles input comment correctly', async () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        // 切换到评论模式
        const commentRadio = screen.getByRole('radio', {name: '评论'});
        fireEvent.click(commentRadio);

        // 输入评论文字
        const inputComment = screen.getByRole('textbox');
        userEvent.type(inputComment, 'This is a test comment');

        // 验证输入框的值
        expect(inputComment).toHaveValue('This is a test comment');

        // 提交评论
        const commentButton = screen.getByRole('button', {name: '评 论'});
        fireEvent.click(commentButton);

        // 检查是否调用了 onComment
        expect(defaultProps.onComment).toHaveBeenCalledWith('This is a test comment', 0);
    });

    test('handles input message correctly', async () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        // 切换到留言模式
        const messageRadio = screen.getByRole('radio', {name: '留言'});
        fireEvent.click(messageRadio);

        // 输入留言文字
        const inputMessage = screen.getByRole('textbox');
        userEvent.type(inputMessage, 'This is a test message');

        // 验证输入框的值
        expect(inputMessage).toHaveValue('This is a test message');

        // 提交留言
        const messageButton = screen.getByRole('button', {name: '留 言'});
        fireEvent.click(messageButton);

        // 检查是否调用了 onMessage
        expect(defaultProps.onMessage).toHaveBeenCalledWith('This is a test message');
    });

    test('handles tab change and order change correctly', async () => {
        render(
            <Router>
                <CommentList {...defaultProps} />
            </Router>
        );

        // 切换到留言标签
        const messageTab = screen.getByRole('tab', {name: '留言 1条'});
        fireEvent.click(messageTab);

        // 检查是否调用了 onChangeMode
        expect(defaultProps.onChangeMode).toHaveBeenCalled();

        // 切换排序方式
        const sortByTime = screen.getByRole('radio', {name: '按时间排序'});
        fireEvent.click(sortByTime);

        // 检查是否调用了 onChangeOrder
        expect(defaultProps.onChangeOrder).toHaveBeenCalledWith('time');
    });
});
