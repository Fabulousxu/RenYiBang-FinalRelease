import React from 'react';
import '@testing-library/jest-dom';
import _react2, { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import UserPage from '../../page/user_page';
import { getUserProfile } from '../../service/user';

// Mock window.matchMedia
global.matchMedia = global.matchMedia || function() {
    return {
        matches: false,
        addListener: function() {},
        removeListener: function() {}
    };
};

// Mock the getUserProfile service
jest.mock('../../service/user', () => ({
    getUserProfile: jest.fn(),
}));

describe('UserPage', () => {
    const userProfile = {
        avatar: 'avatar_url',
        nickname: 'test_user',
        intro: 'This is a test user.',
        rating: 95,
        phone: '123456789',
        email: 'test_user@example.com',
    };

    beforeAll(() => {
        getUserProfile.mockResolvedValue(userProfile);
    });

    test('renders user profile', async () => {
        getUserProfile.mockResolvedValue(userProfile);

        render(
            <MemoryRouter initialEntries={['/user/1']}>
                <Routes>
                    <Route path="/user/:id" element={<UserPage />} />
                </Routes>
            </MemoryRouter>
        );

        // 使用 getAllByText 获取所有包含 'test_user' 的元素
        const userElements = await screen.findAllByText('test_user');
        expect(userElements.length).toBeGreaterThan(0);

        expect(screen.getByText('This is a test user.')).toBeInTheDocument();
        expect(screen.getByText((userProfile.rating / 10).toFixed(1))).toBeInTheDocument();
        expect(screen.getByText('123456789')).toBeInTheDocument();
        expect(screen.getByText('test_user@example.com')).toBeInTheDocument();
    });
});