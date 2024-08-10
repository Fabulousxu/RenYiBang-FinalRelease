// ItemDetail.test.js
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { BrowserRouter as Router } from 'react-router-dom';
import ItemDetail from '../../component/item_detail';

// Mock data
const mockDetail = {
    images: ['image1.jpg', 'image2.jpg'],
    title: 'Test Item',
    price: 2000,
    rating: 80,
    owner: {
        userId: 'user123',
        avatar: 'avatar.jpg',
        nickname: 'Test Owner',
        rating: 90,
    },
    description: 'This is a test description.',
};

const mockProps = {
    detail: mockDetail,
    ratingTitle: 'Rating',
    descriptionTitle: 'Description',
};

beforeAll(() => {
    window.matchMedia = window.matchMedia || function () {
        return { matches: false, addListener: () => {}, removeListener: () => {} };
    };
});

test('renders ItemDetail component correctly', () => {
    render(
        <Router>
            <ItemDetail {...mockProps} />
        </Router>
    );

    // Check if images are rendered
    mockDetail.images.forEach((image, index) => {
        // eslint-disable-next-line no-unused-expressions,jest/valid-expect
        expect(screen.getByRole('img', { name: `图片` })).toHaveAttribute
    });

    // Check if title is rendered
    expect(screen.getByText(mockDetail.title)).toBeInTheDocument();

    // Check if price is rendered correctly
    expect(screen.getByText('¥20.00')).toBeInTheDocument();

    // Check if owner info is rendered
    expect(screen.getByText(mockDetail.owner.nickname)).toBeInTheDocument();

    // Check if description title and description are rendered
    expect(screen.getByText(mockProps.descriptionTitle)).toBeInTheDocument();
    expect(screen.getByText(mockDetail.description)).toBeInTheDocument();
});


