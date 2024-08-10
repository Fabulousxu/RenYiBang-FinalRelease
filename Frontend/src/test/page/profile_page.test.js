import React from 'react';
import '@testing-library/jest-dom';
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import ProfilePage from '../../page/profile_page';
import {MemoryRouter} from 'react-router-dom';
import {getSelfProfile} from '../../service/user';

jest.mock('../../service/user');

describe('ProfilePage', () => {
  beforeAll(() => {
    window.matchMedia = window.matchMedia || function () {
      return {matches: false, addListener: () => {}, removeListener: () => {}}
    }
  })

  test('should display user information on load', async () => {
    getSelfProfile.mockResolvedValue({
      nickname: 'John Doe',
      intro: 'Developer',
      phone: '1234567890',
      email: 'john@example.com',
      avatar: 'avatar_url',
    });

    render(<MemoryRouter>
      <ProfilePage/>
    </MemoryRouter>);

    // 检查用户信息是否显示
    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument();
      expect(screen.getByText('Developer')).toBeInTheDocument();
      expect(screen.getByText('1234567890')).toBeInTheDocument();
      expect(screen.getByText('john@example.com')).toBeInTheDocument();
    });
  });
});


